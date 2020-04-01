package net.movegaga.jemu2.driver.arcade.gng;

import net.movegaga.jemu2.driver.BaseDriver;

import java.net.URL;

import jef.cpu.M6809;
import jef.cpu.Z80;
import jef.cpuboard.CpuDriver;
import jef.machine.BasicEmulator;
import jef.machine.Emulator;
import jef.machine.EmulatorProperties;
import jef.map.InputPort;
import jef.map.InterruptHandler;
import jef.map.MemoryReadAddress;
import jef.map.MemoryWriteAddress;
import jef.map.ReadHandler;
import jef.map.ReadMap;
import jef.map.Register;
import jef.map.SwitchedInterrupt;
import jef.map.WriteHandler;
import jef.map.WriteMap;
import jef.scoring.HighScoreParser;
import jef.sound.SoundChip;
import jef.sound.chip.fm.FMIRQHandler;
import jef.sound.chip.fm.FMTimerHandler;
import jef.sound.chip.ym2203.YM2203;
import jef.util.RomLoader;
import jef.video.GfxDecodeInfo;

public class Gng extends BaseDriver {
   int[][] charlayout;
   M6809 cpu1 = new M6809();
   Z80 cpu2;
   BasicEmulator emu = new BasicEmulator();
   WriteHandler gng_bankswitch_w;
   WriteHandler gng_bgscrollx_w;
   WriteHandler gng_bgscrolly_w;
   WriteHandler gng_bgvideoram_w;
   WriteHandler gng_fgvideoram_w;
   InputPort[] in;
   InterruptHandler interrupt;
   char[] mem_cpu1 = new char[131072];
   char[] mem_cpu2;
   char[] mem_gfx1;
   char[] mem_gfx2;
   char[] mem_gfx3;
   char[] mem_prom;
   ReadMap mra1;
   ReadMap mra2;
   WriteMap mwa1;
   WriteMap mwa2;
   Register soundLatch;
   int[][] spritelayout;
   int[][] tilelayout;
   VGng v;
   YM2203 ym;

   public Gng() {
      this.mra1 = new MemoryReadAddress(this.mem_cpu1);
      this.mwa1 = new MemoryWriteAddress(this.mem_cpu1);
      this.interrupt = new SwitchedInterrupt(0);
      this.cpu2 = new Z80();
      this.mem_cpu2 = new char[65536];
      this.mra2 = new MemoryReadAddress(this.mem_cpu2);
      this.mwa2 = new MemoryWriteAddress(this.mem_cpu2);
      this.v = new VGng(this);
      this.mem_gfx1 = new char[16384];
      this.mem_gfx2 = new char[98304];
      this.mem_gfx3 = new char[98304];
      this.mem_prom = new char[8192];
      this.gng_fgvideoram_w = this.v.writeFgVRAM();
      this.gng_bgvideoram_w = this.v.writeBgVRAM();
      this.gng_bgscrollx_w = this.v.writeScrollX();
      this.gng_bgscrolly_w = this.v.writeScrollY();
      this.ym = new YM2203(2, 1500000, (FMTimerHandler)null, (FMIRQHandler)null);
      this.soundLatch = new Register();
      this.gng_bankswitch_w = new WriteBankSwitch();
      this.in = new InputPort[5];
      int[] var8 = new int[]{8};
      int[] var9 = new int[]{this.RGN_FRAC(1, 1)};
      int[] var10 = new int[]{2};
      int[] var7 = new int[]{4, 0};
      int[] var5 = new int[]{0, 1, 2, 3, 8, 9, 10, 11};
      int[] var6 = new int[]{0, 16, 32, 48, 64, 80, 96, 112};
      int[] var11 = new int[]{128};
      this.charlayout = new int[][]{var8, {8}, var9, var10, var7, var5, var6, var11};
      var7 = new int[]{16};
      int var2 = this.RGN_FRAC(1, 3);
      int var1 = this.RGN_FRAC(0, 3);
      int var3 = this.RGN_FRAC(1, 3);
      int var4 = this.RGN_FRAC(2, 3);
      var5 = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 128, 129, 130, 131, 132, 133, 134, 135};
      var6 = new int[]{0, 8, 16, 24, 32, 40, 48, 56, 64, 72, 80, 88, 96, 104, 112, 120};
      this.tilelayout = new int[][]{{16}, var7, {var2}, {3}, {var1, var3, var4}, var5, var6, {256}};
      var8 = new int[]{16};
      var9 = new int[]{this.RGN_FRAC(1, 2)};
      var6 = new int[]{4, 0, this.RGN_FRAC(1, 2) + 4, this.RGN_FRAC(1, 2) + 0};
      var5 = new int[]{0, 1, 2, 3, 8, 9, 10, 11, 256, 257, 258, 259, 264, 265, 266, 267};
      var7 = new int[]{0, 16, 32, 48, 64, 80, 96, 112, 128, 144, 160, 176, 192, 208, 224, 240};
      var10 = new int[]{512};
      this.spritelayout = new int[][]{{16}, var8, var9, {4}, var6, var5, var7, var10};
   }

   private GfxDecodeInfo[] gfxdecodeinfo() {
      return new GfxDecodeInfo[]{new GfxDecodeInfo(this.mem_gfx1, 0, this.charlayout, 128, 16), new GfxDecodeInfo(this.mem_gfx2, 0, this.tilelayout, 0, 8), new GfxDecodeInfo(this.mem_gfx3, 0, this.spritelayout, 64, 4)};
   }

   private void initInputDiamond() {
      this.in[0].setBit(1, 4);
      this.in[0].setBit(2, 5);
      this.in[0].setBit(60, 60);
      this.in[0].setBit(64, 1);
      this.in[0].setBit(128, 2);
      this.in[1].setBit(1, 9);
      this.in[1].setBit(2, 8);
      this.in[1].setBit(4, 11);
      this.in[1].setBit(8, 10);
      this.in[1].setBit(16, 12);
      this.in[1].setBits(224, 224);
      this.in[2].setBits(255, 255);
      this.in[3].setBits(255, 129);
      this.in[4].setBits(255, 0);
   }

   private void initInputGng() {
      this.in[0].setBit(1, 4);
      this.in[0].setBit(2, 5);
      this.in[0].setBits(28, 28);
      this.in[0].setBit(32, 16);
      this.in[0].setBit(64, 1);
      this.in[0].setBit(128, 2);
      this.in[1].setBit(1, 9);
      this.in[1].setBit(2, 8);
      this.in[1].setBit(4, 11);
      this.in[1].setBit(8, 10);
      this.in[1].setBit(16, 12);
      this.in[1].setBit(32, 13);
      this.in[1].setBits(192, 192);
      this.in[2].setBit(1, 137);
      this.in[2].setBit(2, 136);
      this.in[2].setBit(4, 139);
      this.in[2].setBit(8, 138);
      this.in[2].setBit(16, 140);
      this.in[2].setBit(32, 141);
      this.in[2].setBits(192, 192);
      this.in[3].setBits(255, 223);
      this.in[4].setBits(255, 251);
   }

   private void initMemoryMapCpu1() {
      this.mra1.set(12288, 12288, this.in[0]);
      this.mra1.set(12289, 12289, this.in[1]);
      this.mra1.set(12290, 12290, this.in[2]);
      this.mra1.set(12291, 12291, this.in[3]);
      this.mra1.set(12292, 12292, this.in[4]);
      this.mra1.setMR(15360, 15360, 2);
      this.mra1.setMR(16384, 24575, 3);
      this.mwa1.set(8192, 10239, this.gng_fgvideoram_w);
      this.mwa1.set(10240, 12287, this.gng_bgvideoram_w);
      this.mwa1.set(14336, 14591, this.v.writePaletteRAM2());
      this.mwa1.set(14592, 14847, this.v.writePaletteRAM1());
      this.mwa1.set(14848, 14848, this.soundLatch);
      this.mwa1.set(15112, 15113, this.gng_bgscrollx_w);
      this.mwa1.set(15114, 15115, this.gng_bgscrolly_w);
      this.mwa1.setMW(15360, 15360, 2);
      this.mwa1.set(15872, 15872, this.gng_bankswitch_w);
      this.mwa1.setMW(16384, '\uffff', 1);
   }

   private void initMemoryMapCpu2() {
      this.mra2.set('저', '저', this.soundLatch);
      this.mwa2.setMW(0, 32767, 1);
      this.mwa2.set('\ue000', '\ue000', this.ym.ym2203_control_port_0_w());
      this.mwa2.set('\ue001', '\ue001', this.ym.ym2203_write_port_0_w());
      this.mwa2.set('\ue002', '\ue002', this.ym.ym2203_control_port_1_w());
      this.mwa2.set('\ue003', '\ue003', this.ym.ym2203_write_port_1_w());
   }

   private void loadRomDiamond(URL var1) {
      RomLoader var2 = new RomLoader();
      var2.setZip("diamond");
      var2.setMemory(this.mem_cpu1);
      var2.loadROM("d3o", 16384, 16384, -1169425935);
      var2.loadROM("d3", '耀', '耀', -197732614);
      var2.loadROM("d5o", 65536, '耀', -1369916102);
      var2.loadROM("d5", 98304, '耀', 1161772958);
      var2.setMemory(this.mem_cpu2);
      var2.loadROM("d2", 0, '耀', 1633639279);
      var2.setMemory(this.mem_gfx1);
      var2.loadROM("d1", 0, 16384, 975496452);
      var2.setMemory(this.mem_gfx2);
      var2.loadROM("d11", 0, 16384, 1967347671);
      var2.loadROM("d10", 16384, 16384, 1966206413);
      var2.loadROM("d9", '耀', 16384, 586074632);
      var2.loadROM("d8", '쀀', 16384, 1801567840);
      var2.loadROM("d7", 65536, 16384, -44477836);
      var2.loadROM("d6", 81920, 16384, 2136071378);
      var2.setMemory(this.mem_gfx3);
      var2.loadROM("d17", 0, 16384, -2124107771);
      var2.loadROM("d14", '쀀', 16384, 1863524707);
      var2.setMemory(this.mem_prom);
      var2.loadROM("prom1", 0, 256, 246370648);
      var2.loadROM("prom2", 256, 256, 1242727844);
      var2.loadZip(var1);
   }

   private void loadRomGng(URL var1) {
      RomLoader var2 = new RomLoader();
      var2.setZip("gng");
      var2.setMemory(this.mem_cpu1);
      var2.loadROM("gg4.bin", 16384, 16384, 1717595115);
      var2.loadROM("gg3.bin", '耀', '耀', -1644050850);
      var2.loadROM("gg5.bin", 65536, '耀', -700875989);
      var2.setMemory(this.mem_cpu2);
      var2.loadROM("gg2.bin", 0, '耀', 1633639279);
      var2.setMemory(this.mem_gfx1);
      var2.loadROM("gg1.bin", 0, 16384, -318976249);
      var2.setMemory(this.mem_gfx2);
      var2.loadROM("gg11.bin", 0, 16384, -573214807);
      var2.loadROM("gg10.bin", 16384, 16384, 1929532061);
      var2.loadROM("gg9.bin", '耀', 16384, 537091034);
      var2.loadROM("gg8.bin", '쀀', 16384, -248798607);
      var2.loadROM("gg7.bin", 65536, 16384, -450551683);
      var2.loadROM("gg6.bin", 81920, 16384, 762833330);
      var2.setMemory(this.mem_gfx3);
      var2.loadROM("gg17.bin", 0, 16384, -1813706097);
      var2.loadROM("gg16.bin", 16384, 16384, 114812362);
      var2.loadROM("gg15.bin", '耀', 16384, -1138761683);
      var2.loadROM("gg14.bin", '쀀', 16384, 1789858553);
      var2.loadROM("gg13.bin", 65536, 16384, -401850422);
      var2.loadROM("gg12.bin", 81920, 16384, 2004920613);
      var2.setMemory(this.mem_prom);
      var2.loadROM("tbp24s10.14k", 0, 256, 246370648);
      var2.loadROM("63s141.2e", 256, 256, 1242727844);
      var2.loadZip(var1);
   }

   private void loadRomMakaimur(URL var1) {
      RomLoader var2 = new RomLoader();
      var2.setZip("makaimur");
      var2.setMemory(this.mem_cpu1);
      var2.loadROM("10n.rom", 16384, 16384, -2115672096);
      var2.loadROM("8n.rom", '耀', '耀', -1777150356);
      var2.loadROM("12n.rom", 65536, '耀', 1705421179);
      var2.setMemory(this.mem_cpu2);
      var2.loadROM("gg2.bin", 0, '耀', 1633639279);
      var2.setMemory(this.mem_gfx1);
      var2.loadROM("gg1.bin", 0, 16384, -318976249);
      var2.setMemory(this.mem_gfx2);
      var2.loadROM("gg11.bin", 0, 16384, -573214807);
      var2.loadROM("gg10.bin", 16384, 16384, 1929532061);
      var2.loadROM("gg9.bin", '耀', 16384, 537091034);
      var2.loadROM("gg8.bin", '쀀', 16384, -248798607);
      var2.loadROM("gg7.bin", 65536, 16384, -450551683);
      var2.loadROM("gg6.bin", 81920, 16384, 762833330);
      var2.setMemory(this.mem_gfx3);
      var2.loadROM("gng13.n4", 0, 16384, 1175695324);
      var2.loadROM("gg16.bin", 16384, 16384, 114812362);
      var2.loadROM("gg15.bin", '耀', 16384, -1138761683);
      var2.loadROM("gng16.l4", '쀀', 16384, 1619880149);
      var2.loadROM("gg13.bin", 65536, 16384, -401850422);
      var2.loadROM("gg12.bin", 81920, 16384, 2004920613);
      var2.setMemory(this.mem_prom);
      var2.loadROM("tbp24s10.14k", 0, 256, 246370648);
      var2.loadROM("63s141.2e", 256, 256, 1242727844);
      var2.loadZip(var1);
   }

   public Emulator createEmulator(URL var1, String var2) {
      super.createEmulator(var1, var2);
      this.in[0] = new InputPort();
      this.in[1] = new InputPort();
      this.in[2] = new InputPort();
      this.in[3] = new InputPort();
      this.in[4] = new InputPort();
      EmulatorProperties var3 = this.createMachineDriver();
      this.initMemoryMapCpu1();
      this.initMemoryMapCpu2();
      if(var2.equals("gng")) {
         this.initInputGng();
         this.loadRomGng(var1);
         new HighScoreParser(this.emu, this.mem_cpu1, 32, 0, 0, 8289, 1, 8296, 0, 1);
      } else if(var2.equals("makaimur")) {
         this.initInputGng();
         this.loadRomMakaimur(var1);
      } else if(var2.equals("diamond")) {
         this.initInputDiamond();
         this.loadRomDiamond(var1);
         this.mra1.set(24576, 24576, new ReadDiamondHack());
      }

      this.v.init(var3);
      this.emu.init(var3);
      return this.emu;
   }

   public EmulatorProperties createMachineDriver() {
      EmulatorProperties var1 = new EmulatorProperties();
      CpuDriver[] var2 = new CpuDriver[]{new CpuDriver(this.cpu1, 1500000, this.mra1, this.mwa1, this.interrupt, 1), new CpuDriver(this.cpu2, 3000000, this.mra2, this.mwa2, this.interrupt, 4)};
      var2[1].isAudioCpu = true;
      YM2203 var3 = this.ym;
      var1.setCpuDriver(var2);
      var1.setFPS(60);
      var1.setSoundChips(new SoundChip[]{var3});
      var1.setVBlankDuration(3750);
      var1.setTimeSlicesPerFrame(100);
      var1.setVideoDimensions(256, 256);
      var1.setVisibleArea(0, 255, 16, 239);
      var1.setGfxDecodeInfo(this.gfxdecodeinfo());
      var1.setPaletteLength(256);
      var1.setVideoEmulation(this.v);
      var1.setDynamicPalette(true);
      var1.setInputPorts(this.in);
      return var1;
   }

   public class ReadDiamondHack implements ReadHandler {
      public int read(int var1) {
         return 0;
      }
   }

   public class WriteBankSwitch implements WriteHandler {
      public void write(int var1, int var2) {
         if(var2 == 4) {
            Gng.this.mra1.setBankAddress(1, 16384);
         } else {
            Gng.this.mra1.setBankAddress(1, 65536 + (var2 & 3) * 8192);
         }

      }
   }
}
