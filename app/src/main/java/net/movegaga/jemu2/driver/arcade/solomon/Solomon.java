package net.movegaga.jemu2.driver.arcade.solomon;

import net.movegaga.jemu2.driver.BaseDriver;

import java.net.URL;

import jef.cpu.Z80;
import jef.cpuboard.CpuDriver;
import jef.machine.BasicEmulator;
import jef.machine.Emulator;
import jef.machine.EmulatorProperties;
import jef.map.IOReadPort;
import jef.map.IOWritePort;
import jef.map.InputPort;
import jef.map.MemoryReadAddress;
import jef.map.MemoryWriteAddress;
import jef.map.ReadHandler;
import jef.map.SwitchedInterrupt;
import jef.map.WriteHandler;
import jef.scoring.HighScoreParser;
import jef.sound.SoundChip;
import jef.sound.chip.AY8910;
import jef.util.RomLoader;
import jef.video.GfxDecodeInfo;

public class Solomon extends BaseDriver {
   AY8910 ay8910;
   int[][] charlayout;
   Z80 cpu1 = new Z80();
   Z80 cpu2;
   BasicEmulator emu;
   InputPort[] in;
   SwitchedInterrupt interruptIRQ;
   SwitchedInterrupt interruptNMI;
   IOReadPort ior;
   IOWritePort iow;
   char[] mem_cpu1 = new char[65536];
   char[] mem_cpu2;
   char[] mem_gfx1;
   char[] mem_gfx2;
   char[] mem_gfx3;
   MemoryReadAddress mra1;
   MemoryReadAddress mra2;
   MemoryWriteAddress mwa1;
   MemoryWriteAddress mwa2;
   int soundLatch;
   int[][] spritelayout;
   VSolomon video;
   WriteHandler writeEnableNMI;
   WriteHandler writePaletteRAM;
   WriteHandler writeSoundCmd;
   WriteHandler writeVRAM;

   public Solomon() {
      this.mra1 = new MemoryReadAddress(this.mem_cpu1);
      this.mwa1 = new MemoryWriteAddress(this.mem_cpu1);
      this.cpu2 = new Z80();
      this.mem_cpu2 = new char[65536];
      this.mra2 = new MemoryReadAddress(this.mem_cpu2);
      this.mwa2 = new MemoryWriteAddress(this.mem_cpu2);
      this.ior = new IOReadPort();
      this.iow = new IOWritePort();
      this.mem_gfx1 = new char[65536];
      this.mem_gfx2 = new char[65536];
      this.mem_gfx3 = new char[65536];
      this.ay8910 = new AY8910(3, 1500000);
      this.video = new VSolomon(this);
      this.writeVRAM = this.video;
      this.writePaletteRAM = this.video.writePaletteRAM();
      this.writeSoundCmd = new WriteSoundCommand();
      this.emu = new BasicEmulator();
      this.interruptNMI = new SwitchedInterrupt(1);
      this.interruptIRQ = new SwitchedInterrupt(0);
      this.writeEnableNMI = this.interruptNMI;
      this.in = new InputPort[5];
      this.soundLatch = 0;
      int[] var4 = new int[]{8};
      int[] var5 = new int[]{8};
      int[] var6 = new int[]{2048};
      int[] var7 = new int[]{4};
      int[] var1 = new int[]{0, 1, 2, 3};
      int[] var3 = new int[]{0, 4, 8, 12, 16, 20, 24, 28};
      int[] var2 = new int[]{0, 32, 64, 96, 128, 160, 192, 224};
      this.charlayout = new int[][]{var4, var5, var6, var7, var1, var3, var2, {256}};
      var4 = new int[]{16};
      var5 = new int[]{16};
      var6 = new int[]{512};
      var7 = new int[]{4};
      var1 = new int[]{393216, 262144, 131072, 0};
      var3 = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 64, 65, 66, 67, 68, 69, 70, 71};
      var2 = new int[]{0, 8, 16, 24, 32, 40, 48, 56, 128, 136, 144, 152, 160, 168, 176, 184};
      this.spritelayout = new int[][]{var4, var5, var6, var7, var1, var3, var2, {256}};
   }

   public Emulator createEmulator(URL var1, String var2) {
      super.createEmulator(var1, var2);
      RomLoader var8 = new RomLoader();
      var8.setZip("solomon");
      var8.setMemory(this.mem_cpu1);
      var8.loadROM("slmn_06.bin", 0, 16384, -455859713);
      var8.loadROM("slmn_07.bin", '耀', 16384, -718438856);
      var8.continueROM(16384, 16384);
      var8.loadROM("slmn_08.bin", '\uf000', 4096, -1188769438);
      var8.setMemory(this.mem_cpu2);
      var8.loadROM("slmn_01.bin", 0, 16384, -93432274);
      var8.setMemory(this.mem_gfx1);
      var8.loadROM("slmn_12.bin", 0, '耀', -1440292917);
      var8.loadROM("slmn_11.bin", '耀', '耀', 1872024239);
      var8.setMemory(this.mem_gfx2);
      var8.loadROM("slmn_10.bin", 0, '耀', -2096053599);
      var8.loadROM("slmn_09.bin", '耀', '耀', -1417778110);
      var8.setMemory(this.mem_gfx3);
      var8.loadROM("slmn_02.bin", 0, 16384, -2131088413);
      var8.loadROM("slmn_03.bin", 16384, 16384, 593561268);
      var8.loadROM("slmn_04.bin", '耀', 16384, 143648217);
      var8.loadROM("slmn_05.bin", '쀀', 16384, -2090458326);
      var8.loadZip(var1);
      this.in[0] = new InputPort();
      this.in[0].setBit(1, 0, 9);
      this.in[0].setBit(2, 0, 8);
      this.in[0].setBit(4, 0, 10);
      this.in[0].setBit(8, 0, 11);
      this.in[0].setBit(16, 0, 13);
      this.in[0].setBit(32, 0, 12);
      this.in[0].setBits(192, 0);
      this.in[1] = new InputPort();
      this.in[1].setBit(1, 0, 137);
      this.in[1].setBit(2, 0, 136);
      this.in[1].setBit(4, 0, 138);
      this.in[1].setBit(8, 0, 139);
      this.in[1].setBit(16, 0, 141);
      this.in[1].setBit(32, 0, 140);
      this.in[0].setBits(192, 0);
      this.in[2] = new InputPort();
      this.in[2].setBit(1, 0, 4);
      this.in[2].setBit(2, 0, 5);
      this.in[2].setBit(4, 0, 1);
      this.in[2].setBit(8, 0, 2);
      this.in[0].setBits(240, 0);
      this.in[3] = new InputPort();
      this.in[3].setBits(255, 2);
      this.in[4] = new InputPort();
      this.in[4].setBits(255, 0);
      this.mra1.set('\ue600', '\ue600', this.in[0]);
      this.mra1.set('\ue601', '\ue601', this.in[1]);
      this.mra1.set('\ue602', '\ue602', this.in[2]);
      this.mra1.set('\ue604', '\ue604', this.in[3]);
      this.mra1.set('\ue605', '\ue605', this.in[4]);
      this.mwa1.setMW(0, '뿿', 1);
      this.mwa1.set('퀀', '\udfff', this.writeVRAM);
      this.mwa1.set('\ue400', '\ue5ff', this.writePaletteRAM);
      this.mwa1.set('\ue600', '\ue600', this.writeEnableNMI);
      this.mwa1.set('\ue800', '\ue800', this.writeSoundCmd);
      this.mwa1.setMW('\uf000', '\uffff', 1);
      this.mra2.set('耀', '耀', new ReadSoundLatch());
      this.mwa2.setMW(0, 16383, 1);
      this.mwa2.setMW('\uffff', '\uffff', 2);
      this.iow.set(16, 16, this.ay8910.AY8910_control_port_0_w());
      this.iow.set(17, 17, this.ay8910.AY8910_write_port_0_w());
      this.iow.set(32, 32, this.ay8910.AY8910_control_port_1_w());
      this.iow.set(33, 33, this.ay8910.AY8910_write_port_1_w());
      this.iow.set(48, 48, this.ay8910.AY8910_control_port_2_w());
      this.iow.set(49, 49, this.ay8910.AY8910_write_port_2_w());
      GfxDecodeInfo var4 = new GfxDecodeInfo(this.mem_gfx1, 0, this.charlayout, 0, 8);
      GfxDecodeInfo var7 = new GfxDecodeInfo(this.mem_gfx2, 0, this.charlayout, 128, 8);
      GfxDecodeInfo var3 = new GfxDecodeInfo(this.mem_gfx3, 0, this.spritelayout, 0, 8);
      EmulatorProperties var9 = new EmulatorProperties();
      CpuDriver[] var5 = new CpuDriver[]{new CpuDriver(this.cpu1, 4000000, this.mra1, this.mwa1, this.interruptNMI, 1), new CpuDriver(this.cpu2, 3072000, this.mra2, this.mwa2, this.ior, this.iow, this.interruptIRQ, 2)};
      var5[1].isAudioCpu = true;
      AY8910 var6 = this.ay8910;
      var9.setCpuDriver(var5);
      var9.setSoundChips(new SoundChip[]{var6});
      var9.setTimeSlicesPerFrame(100);
      var9.setFPS(60);
      var9.setDynamicPalette(true);
      var9.setVideoWidth(256);
      var9.setVideoHeight(256);
      var9.setVisibleArea(0, 255, 16, 239);
      var9.setGfxDecodeInfo(new GfxDecodeInfo[]{var4, var7, var3});
      var9.setPaletteLength(256);
      var9.setVideoEmulation(this.video);
      var9.setInputPorts(this.in);
      new HighScoreParser(this.emu, this.mem_cpu1, 0, 48, 0, '푃', 1, '푊', 0, 1);
      this.video.init(var9);
      this.emu.init(var9);
      return this.emu;
   }

   public class ReadSoundLatch implements ReadHandler {
      public int read(int var1) {
         return Solomon.this.soundLatch;
      }
   }

   public class WriteSoundCommand implements WriteHandler {
      public void write(int var1, int var2) {
         Solomon.this.soundLatch = var2;
         Solomon.this.cpu2.interrupt(1, true);
      }
   }
}
