package net.movegaga.jemu2.driver.arcade.hyperspt;

import net.movegaga.jemu2.driver.BaseDriver;
import net.movegaga.jemu2.driver.arcade.Konami;

import java.net.URL;

import jef.cpu.M6809;
import jef.cpu.Z80;
import jef.cpuboard.CpuDriver;
import jef.machine.Emulator;
import jef.machine.EmulatorProperties;
import jef.map.InputPort;
import jef.map.MemoryReadAddress;
import jef.map.MemoryWriteAddress;
import jef.map.ReadHandler;
import jef.map.Register;
import jef.map.SwitchedInterrupt;
import jef.map.WriteHandler;
import jef.sound.SoundChip;
import jef.sound.chip.SN76496;
import jef.util.RomLoader;
import jef.video.GfxDecodeInfo;
import jef.video.VideoInitializer;
import jef.video.VideoRenderer;

public class Hyperspt extends BaseDriver {
   M6809 cpu1 = new M6809();
   Z80 cpu2;
   Konami emu;
   int[][] hyperspt_charlayout;
   int[][] hyperspt_spritelayout;
   InputPort[] in;
   VideoInitializer initRoadf;
   SwitchedInterrupt interrupt;
   int latch;
   char[] mem_cpu1 = new char[131072];
   char[] mem_cpu2;
   char[] mem_gfx1;
   char[] mem_gfx2;
   char[] mem_prom;
   MemoryReadAddress mra1;
   MemoryReadAddress mra2;
   MemoryWriteAddress mwa1;
   MemoryWriteAddress mwa2;
   VideoRenderer rendererRoadf;
   int[][] roadf_charlayout;
   int[][] roadf_spritelayout;
   SN76496 sn1;
   Register soundLatch;
   VHyperspt video;
   WriteHandler writeInterruptEnable;
   WriteHandler writeSn;
   WriteHandler writeSnLatch;
   WriteHandler writeSoundIRQ;
   WriteHandler writeVRAM;

   public Hyperspt() {
      this.mra1 = new MemoryReadAddress(this.mem_cpu1);
      this.mwa1 = new MemoryWriteAddress(this.mem_cpu1);
      this.interrupt = new SwitchedInterrupt(0);
      this.writeInterruptEnable = this.interrupt;
      this.cpu2 = new Z80();
      this.mem_cpu2 = new char[65536];
      this.mra2 = new MemoryReadAddress(this.mem_cpu2);
      this.mwa2 = new MemoryWriteAddress(this.mem_cpu2);
      this.video = new VHyperspt(this);
      this.mem_gfx1 = new char['쀀'];
      this.mem_gfx2 = new char[65536];
      this.mem_prom = new char[544];
      this.writeVRAM = this.video;
      this.initRoadf = this.video.initRoadf();
      this.rendererRoadf = this.video.rendererRoadf();
      this.sn1 = new SN76496(1789772);
      this.soundLatch = new Register();
      this.writeSnLatch = new WriteSnLatch();
      this.writeSn = new WriteSN76496();
      this.writeSoundIRQ = new WriteSoundCpuIRQ();
      this.emu = new Konami();
      this.in = new InputPort[5];
      this.latch = 0;
      int[] var4 = new int[]{1024};
      int[] var3 = new int[]{4, 0, 131076, 131072};
      int[] var1 = new int[]{0, 1, 2, 3, 64, 65, 66, 67};
      int[] var2 = new int[]{0, 8, 16, 24, 32, 40, 48, 56};
      this.hyperspt_charlayout = new int[][]{{8}, {8}, var4, {4}, var3, var1, var2, {128}};
      var4 = new int[]{16};
      var3 = new int[]{4, 0, 262148, 262144};
      var1 = new int[]{0, 1, 2, 3, 64, 65, 66, 67, 128, 129, 130, 131, 192, 193, 194, 195};
      var2 = new int[]{0, 8, 16, 24, 32, 40, 48, 56, 256, 264, 272, 280, 288, 296, 304, 312};
      this.hyperspt_spritelayout = new int[][]{{16}, var4, {512}, {4}, var3, var1, var2, {512}};
      var1 = new int[]{4, 0, 196612, 196608};
      var2 = new int[]{0, 1, 2, 3, 64, 65, 66, 67};
      var3 = new int[]{0, 8, 16, 24, 32, 40, 48, 56};
      var4 = new int[]{128};
      this.roadf_charlayout = new int[][]{{8}, {8}, {1536}, {4}, var1, var2, var3, var4};
      var4 = new int[]{16};
      int[] var5 = new int[]{256};
      var2 = new int[]{4, 0, 131076, 131072};
      var3 = new int[]{0, 1, 2, 3, 64, 65, 66, 67, 128, 129, 130, 131, 192, 193, 194, 195};
      var1 = new int[]{0, 8, 16, 24, 32, 40, 48, 56, 256, 264, 272, 280, 288, 296, 304, 312};
      this.roadf_spritelayout = new int[][]{{16}, var4, var5, {4}, var2, var3, var1, {512}};
   }

   private void initMemMap() {
      this.mra1.set(5632, 5632, this.in[4]);
      this.mra1.set(5760, 5760, this.in[0]);
      this.mra1.set(5761, 5761, this.in[1]);
      this.mra1.set(5762, 5762, this.in[2]);
      this.mra1.set(5763, 5763, this.in[3]);
      this.mwa1.set(5249, 5249, this.writeSoundIRQ);
      this.mwa1.set(5255, 5255, this.writeInterruptEnable);
      this.mwa1.set(5376, 5376, this.soundLatch);
      this.mwa1.set(8192, 12287, this.writeVRAM);
      this.mwa1.setMW(16384, '\uffff', 1);
      this.mwa2.setMW(0, 16383, 1);
      this.mwa2.set('\ue001', '\ue001', this.writeSnLatch);
      this.mwa2.set('\ue002', '\ue002', this.writeSn);
      this.mra2.set(24576, 24576, this.soundLatch);
      this.mra2.set('耀', '耀', new ReadHandler() {
         public int read(int var1) {
            return Hyperspt.this.cpu2.cycle >> 10 & 3;
         }
      });
   }

   private void ipt_hyperspt() {
      this.in[0].setBit(1, 1);
      this.in[0].setBit(2, 2);
      this.in[0].setBit(4, 2);
      this.in[0].setBit(8, 4);
      this.in[0].setBit(16, 5);
      this.in[0].setBits(224, 224);
      this.in[1].setBits(1, 14);
      this.in[1].setBit(2, 13);
      this.in[1].setBit(4, 12);
      this.in[1].setBit(8, 6);
      this.in[1].setBit(16, 142);
      this.in[1].setBit(32, 141);
      this.in[1].setBit(64, 140);
      this.in[1].setBits(128, 128);
      this.in[2].setBits(255, 255);
      this.in[3].setBits(255, 255);
      this.in[4].setBits(255, 255);
   }

   private void ipt_roadf() {
      this.in[0] = new InputPort();
      this.in[0].setBit(1, 1);
      this.in[0].setBit(2, 2);
      this.in[0].setBit(4, 2);
      this.in[0].setBit(8, 4);
      this.in[0].setBit(16, 5);
      this.in[0].setBits(224, 224);
      this.in[1].setBit(1, 8);
      this.in[1].setBit(2, 9);
      this.in[1].setBit(4, 10);
      this.in[1].setBit(8, 11);
      this.in[1].setBit(16, 12);
      this.in[1].setBit(32, 13);
      this.in[1].setBits(192, 192);
      this.in[2].setBit(1, 136);
      this.in[2].setBit(2, 137);
      this.in[2].setBit(4, 138);
      this.in[2].setBit(8, 139);
      this.in[2].setBit(16, 140);
      this.in[2].setBit(32, 141);
      this.in[2].setBits(192, 128);
      this.in[3].setBits(255, 255);
      this.in[4].setBits(255, 62);
   }

   public Emulator createEmulator(URL var1, String var2) {
      super.createEmulator(var1, var2);
      this.in[0] = new InputPort();
      this.in[1] = new InputPort();
      this.in[2] = new InputPort();
      this.in[3] = new InputPort();
      this.in[4] = new InputPort();
      EmulatorProperties var3 = null;
      RomLoader var4;
      if(var2.equals("hyperspt")) {
         var4 = new RomLoader();
         var4.setZip("hyperspt");
         var4.setMemory(this.mem_cpu1);
         var4.loadROM("c01", 16384, 8192, 208801515);
         var4.loadROM("c02", 24576, 8192, 1442994400);
         var4.loadROM("c03", '耀', 8192, -1694382106);
         var4.loadROM("c04", 'ꀀ', 8192, 282585506);
         var4.loadROM("c05", '쀀', 8192, -1325029171);
         var4.loadROM("c06", '\ue000', 8192, 439658569);
         var4.setMemory(this.mem_cpu2);
         var4.loadROM("c10", 0, 8192, 1036101375);
         var4.loadROM("c09", 8192, 8192, -1689101250);
         var4.setMemory(this.mem_gfx1);
         var4.loadROM("c26", 0, 8192, -1500938580);
         var4.loadROM("c24", 8192, 8192, 1605513408);
         var4.loadROM("c22", 16384, 8192, -309169760);
         var4.loadROM("c20", 24576, 8192, 406799140);
         var4.setMemory(this.mem_gfx2);
         var4.loadROM("c14", 0, 8192, -953326658);
         var4.loadROM("c13", 8192, 8192, 1985369608);
         var4.loadROM("c12", 16384, 8192, 1959971945);
         var4.loadROM("c11", 24576, 8192, 1724631885);
         var4.loadROM("c18", '耀', 8192, -316283287);
         var4.loadROM("c17", 'ꀀ', 8192, -1320832097);
         var4.loadROM("c16", '쀀', 8192, -671113429);
         var4.loadROM("c15", '\ue000', 8192, -204188442);
         var4.setMemory(this.mem_prom);
         var4.loadROM("c03_c27.bin", 0, 32, -1131783850);
         var4.loadROM("j12_c28.bin", 32, 256, 747183449);
         var4.loadROM("a09_c29.bin", 288, 256, -2128986305);
         var4.loadZip(var1);
         this.ipt_hyperspt();
         this.initMemMap();
         var3 = this.createMachineDriver();
      } else if(var2.equals("roadf")) {
         var4 = new RomLoader();
         var4.setZip("roadf");
         var4.setMemory(this.mem_cpu1);
         var4.loadROM("g05_g01.bin", 16384, 8192, -498521594);
         var4.loadROM("g07_f02.bin", 24576, 8192, 200757605);
         var4.loadROM("g09_g03.bin", '耀', 8192, -572259848);
         var4.loadROM("g11_f04.bin", 'ꀀ', 8192, -1322763145);
         var4.loadROM("g13_f05.bin", '쀀', 8192, 181720982);
         var4.loadROM("g15_f06.bin", '\ue000', 8192, -96280339);
         var4.setMemory(this.mem_cpu2);
         var4.loadROM("a17_d10.bin", 0, 8192, -1019440514);
         var4.setMemory(this.mem_gfx1);
         var4.loadROM("a14_e26.bin", 0, 16384, -171493150);
         var4.loadROM("a12_d24.bin", 16384, 8192, 763545904);
         var4.loadROM("c14_e22.bin", 24576, 16384, -70271303);
         var4.loadROM("c12_d20.bin", 'ꀀ', 8192, 1577908628);
         var4.setMemory(this.mem_gfx2);
         var4.loadROM("j19_e14.bin", 0, 16384, 382909695);
         var4.loadROM("g19_e18.bin", 16384, 16384, 1225164287);
         var4.setMemory(this.mem_prom);
         var4.loadROM("c03_c27.bin", 0, 32, 1171645266);
         var4.loadROM("j12_c28.bin", 32, 256, 693493791);
         var4.loadROM("a09_c29.bin", 288, 256, 1530617642);
         var4.loadZip(var1);
         this.ipt_roadf();
         this.initMemMap();
         var3 = this.createMachineDriverRoadf();
      }

      this.video.init(var3);
      this.emu.init(var3);
      this.emu.konami1_decode(this.mem_cpu1);
      return this.emu;
   }

   public EmulatorProperties createMachineDriver() {
      GfxDecodeInfo var3 = new GfxDecodeInfo(this.mem_gfx1, 0, this.hyperspt_charlayout, 0, 16);
      GfxDecodeInfo var2 = new GfxDecodeInfo(this.mem_gfx2, 0, this.hyperspt_spritelayout, 256, 16);
      EmulatorProperties var1 = new EmulatorProperties();
      var1.setCpuDriver(new CpuDriver[]{new CpuDriver(this.cpu1, 2048000, this.mra1, this.mwa1, this.interrupt, 1)});
      var1.setFPS(60);
      var1.setTimeSlicesPerFrame(100);
      var1.setVideoDimensions(256, 256);
      var1.setVisibleArea(0, 255, 16, 239);
      var1.setGfxDecodeInfo(new GfxDecodeInfo[]{var3, var2});
      var1.setPaletteLength(32);
      var1.setColorTableLength(512);
      var1.setPaletteInitializer(this.video);
      var1.setVideoInitializer(this.video);
      var1.setVideoUpdater(this.video);
      var1.setInputPorts(this.in);
      return var1;
   }

   public EmulatorProperties createMachineDriverRoadf() {
      GfxDecodeInfo var4 = new GfxDecodeInfo(this.mem_gfx1, 0, this.roadf_charlayout, 0, 16);
      GfxDecodeInfo var3 = new GfxDecodeInfo(this.mem_gfx2, 0, this.roadf_spritelayout, 256, 16);
      EmulatorProperties var2 = new EmulatorProperties();
      CpuDriver var1 = new CpuDriver(this.cpu1, 2048000, this.mra1, this.mwa1, this.interrupt, 1);
      CpuDriver var5 = new CpuDriver(this.cpu2, 3579545, this.mra2, this.mwa2, this.interrupt, 4);
      SN76496 var6 = this.sn1;
      var2.setCpuDriver(new CpuDriver[]{var1, var5});
      var2.setFPS(60);
      var2.setSoundChips(new SoundChip[]{var6});
      var2.setTimeSlicesPerFrame(100);
      var2.setVideoDimensions(256, 256);
      var2.setVisibleArea(0, 255, 16, 239);
      var2.setGfxDecodeInfo(new GfxDecodeInfo[]{var4, var3});
      var2.setPaletteLength(32);
      var2.setColorTableLength(512);
      var2.setPaletteInitializer(this.video);
      var2.setVideoInitializer(this.initRoadf);
      var2.setVideoUpdater(this.rendererRoadf);
      var2.setInputPorts(this.in);
      var2.setRotation(1);
      return var2;
   }

   class WriteSN76496 implements WriteHandler {
      WriteHandler sn;

      WriteSN76496() {
         this.sn = Hyperspt.this.sn1.getWriteCommandHandler();
      }

      public void write(int var1, int var2) {
         Hyperspt.this.sn1.writeCommand(Hyperspt.this.latch);
      }
   }

   class WriteSnLatch implements WriteHandler {
      public void write(int var1, int var2) {
         Hyperspt.this.latch = var2;
      }
   }

   class WriteSoundCpuIRQ implements WriteHandler {
      int last;

      public void write(int var1, int var2) {
         if(this.last == 0 && var2 != 0) {
            Hyperspt.this.cpu2.setProperty(0, 255);
            Hyperspt.this.cpu2.interrupt(0, true);
            Hyperspt.this.cpu2.exec(1000);
         }

         this.last = var2;
      }
   }
}
