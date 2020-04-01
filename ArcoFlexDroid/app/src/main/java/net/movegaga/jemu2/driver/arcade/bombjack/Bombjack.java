package net.movegaga.jemu2.driver.arcade.bombjack;

import net.movegaga.jemu2.driver.BaseDriver;

import java.net.URL;

import jef.cpu.Z80;
import jef.cpuboard.CpuDriver;
import jef.machine.Emulator;
import jef.machine.EmulatorProperties;
import jef.map.IOReadPort;
import jef.map.IOWritePort;
import jef.map.InputPort;
import jef.map.InterruptHandler;
import jef.map.MemoryReadAddress;
import jef.map.MemoryWriteAddress;
import jef.map.ReadHandler;
import jef.map.WriteHandler;
import jef.scoring.HighScoreParser;
import jef.sound.SoundChip;
import jef.sound.chip.AY8910;
import jef.util.RomLoader;
import jef.video.GfxDecodeInfo;

public class Bombjack extends BaseDriver {
   AY8910 ay8910;
   int[][] charlayout1;
   int[][] charlayout2;
   Z80 cpu1 = new Z80();
   Z80 cpu2;
   InputPort[] in;
   InterruptHandler interrupt;
   IOReadPort ior;
   IOWritePort iow;
   int latch;
   MBombjack m;
   char[] mem_cpu1 = new char[65536];
   char[] mem_cpu2;
   char[] mem_gfx1;
   char[] mem_gfx2;
   char[] mem_gfx3;
   char[] mem_gfx4;
   MemoryReadAddress mra1;
   MemoryReadAddress mra2;
   MemoryWriteAddress mwa1;
   MemoryWriteAddress mwa2;
   ReadHandler readSoundLatch;
   int[][] spritelayout1;
   int[][] spritelayout2;
   VBombjack v;
   WriteHandler writeBackground;
   WriteHandler writeEnableInterrupt;
   WriteHandler writePaletteRAM;
   WriteHandler writeSoundLatch;
   WriteHandler writeVRAM;

   public Bombjack() {
      this.mra1 = new MemoryReadAddress(this.mem_cpu1);
      this.mwa1 = new MemoryWriteAddress(this.mem_cpu1);
      this.cpu2 = new Z80();
      this.mem_cpu2 = new char[65536];
      this.mra2 = new MemoryReadAddress(this.mem_cpu2);
      this.mwa2 = new MemoryWriteAddress(this.mem_cpu2);
      this.iow = new IOWritePort();
      this.ior = new IOReadPort();
      this.v = new VBombjack(this);
      this.mem_gfx1 = new char[12288];
      this.mem_gfx2 = new char[24576];
      this.mem_gfx3 = new char[24576];
      this.mem_gfx4 = new char[4096];
      this.writeVRAM = this.v.writeVRAM();
      this.writeBackground = this.v.bombjack_background_w();
      this.writePaletteRAM = this.v.writePaletteRAM();
      this.ay8910 = new AY8910(3, 1500000);
      this.writeSoundLatch = new Bombjack_soundlatch_w();
      this.readSoundLatch = new Bombjack_soundlatch_r();
      this.in = new InputPort[5];
      this.m = new MBombjack();
      this.interrupt = this.m.interruptNMI();
      this.writeEnableInterrupt = this.m.writeEnableInterrupt();
      int[] var3 = new int[]{65536, '耀', 0};
      int[] var1 = new int[]{0, 1, 2, 3, 4, 5, 6, 7};
      int[] var2 = new int[]{0, 8, 16, 24, 32, 40, 48, 56};
      this.charlayout1 = new int[][]{{8}, {8}, {512}, {3}, var3, var1, var2, {64}};
      var1 = new int[]{131072, 65536, 0};
      var3 = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 64, 65, 66, 67, 68, 69, 70, 71};
      var2 = new int[]{0, 8, 16, 24, 32, 40, 48, 56, 128, 136, 144, 152, 160, 168, 176, 184};
      this.charlayout2 = new int[][]{{16}, {16}, {256}, {3}, var1, var3, var2, {256}};
      int[] var4 = new int[]{16};
      int[] var5 = new int[]{128};
      int[] var6 = new int[]{3};
      var2 = new int[]{131072, 65536, 0};
      var3 = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 64, 65, 66, 67, 68, 69, 70, 71};
      var1 = new int[]{0, 8, 16, 24, 32, 40, 48, 56, 128, 136, 144, 152, 160, 168, 176, 184};
      int[] var7 = new int[]{256};
      this.spritelayout1 = new int[][]{var4, {16}, var5, var6, var2, var3, var1, var7};
      var2 = new int[]{131072, 65536, 0};
      var1 = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 64, 65, 66, 67, 68, 69, 70, 71, 256, 257, 258, 259, 260, 261, 262, 263, 320, 321, 322, 323, 324, 325, 326, 327};
      var3 = new int[]{0, 8, 16, 24, 32, 40, 48, 56, 128, 136, 144, 152, 160, 168, 176, 184, 512, 520, 528, 536, 544, 552, 560, 568, 640, 648, 656, 664, 672, 680, 688, 696};
      this.spritelayout2 = new int[][]{{32}, {32}, {32}, {3}, var2, var1, var3, {1024}};
   }

   public Emulator createEmulator(URL var1, String var2) {
      super.createEmulator(var1, var2);
      RomLoader var9 = new RomLoader();
      var9.setZip("bombjack");
      var9.setMemory(this.mem_cpu1);
      var9.loadROM("09_j01b.bin", 0, 8192, -966206416);
      var9.loadROM("10_l01b.bin", 8192, 8192, 1386341883);
      var9.loadROM("11_m01b.bin", 16384, 8192, -1232468438);
      var9.loadROM("12_n01b.bin", 24576, 8192, 490655461);
      var9.loadROM("13.1r", '쀀', 8192, 1893737549);
      var9.setMemory(this.mem_cpu2);
      var9.loadROM("01_h03t.bin", 0, 8192, -2079878787);
      var9.setMemory(this.mem_gfx1);
      var9.loadROM("03_e08t.bin", 0, 4096, -1627098923);
      var9.loadROM("04_h08t.bin", 4096, 4096, -2115235098);
      var9.loadROM("05_k08t.bin", 8192, 4096, -394344271);
      var9.setMemory(this.mem_gfx2);
      var9.loadROM("06_l08t.bin", 0, 8192, 1374600585);
      var9.loadROM("07_n08t.bin", 8192, 8192, -1646686563);
      var9.loadROM("08_r08t.bin", 16384, 8192, 827715197);
      var9.setMemory(this.mem_gfx3);
      var9.loadROM("16_m07b.bin", 0, 8192, -1805041513);
      var9.loadROM("15_l07b.bin", 8192, 8192, 20928754);
      var9.loadROM("14_j07b.bin", 16384, 8192, 270304653);
      var9.setMemory(this.mem_gfx4);
      var9.loadROM("02_p04t.bin", 0, 4096, 965560834);
      var9.loadZip(var1);
      this.in[0] = new InputPort();
      this.in[0].setBit(1, 0, 9);
      this.in[0].setBit(2, 0, 8);
      this.in[0].setBit(4, 0, 10);
      this.in[0].setBit(8, 0, 11);
      this.in[0].setBit(16, 0, 12);
      this.in[0].setBits(224, 224);
      this.in[1] = new InputPort();
      this.in[1].setBit(1, 0, 137);
      this.in[1].setBit(2, 0, 136);
      this.in[1].setBit(4, 0, 138);
      this.in[1].setBit(8, 0, 139);
      this.in[1].setBit(16, 0, 140);
      this.in[1].setBits(224, 224);
      this.in[2] = new InputPort();
      this.in[2].setBit(1, 0, 1);
      this.in[2].setBit(2, 0, 2);
      this.in[2].setBit(4, 0, 4);
      this.in[2].setBit(8, 0, 5);
      this.in[2].setBits(240, 240);
      this.in[3] = new InputPort();
      this.in[3].setBits(255, 192);
      this.in[4] = new InputPort();
      this.in[4].setBits(255, 0);
      this.mra1.set('뀀', '뀀', this.in[0]);
      this.mra1.set('뀁', '뀁', this.in[1]);
      this.mra1.set('뀂', '뀂', this.in[2]);
      this.mra1.setMR('뀃', '뀃', 2);
      this.mra1.set('뀄', '뀄', this.in[3]);
      this.mra1.set('뀅', '뀅', this.in[4]);
      this.mra1.setMR('쀀', '\udfff', 1);
      this.mwa1.setMW(0, 32767, 1);
      this.mwa1.set('退', '響', this.writeVRAM);
      this.mwa1.setMW('騀', '騀', 2);
      this.mwa1.set('鰀', '鳿', this.writePaletteRAM);
      this.mwa1.set('鸀', '鸀', this.writeBackground);
      this.mwa1.set('뀀', '뀀', this.writeEnableInterrupt);
      this.mwa1.set('렀', '렀', this.writeSoundLatch);
      this.mra2.set(24576, 24576, this.readSoundLatch);
      this.mwa2.setMW(0, 8191, 1);
      this.iow.set(0, 0, this.ay8910.AY8910_control_port_0_w());
      this.iow.set(1, 1, this.ay8910.AY8910_write_port_0_w());
      this.iow.set(16, 16, this.ay8910.AY8910_control_port_1_w());
      this.iow.set(17, 17, this.ay8910.AY8910_write_port_1_w());
      this.iow.set(128, 128, this.ay8910.AY8910_control_port_2_w());
      this.iow.set(129, 129, this.ay8910.AY8910_write_port_2_w());
      GfxDecodeInfo var4 = new GfxDecodeInfo(this.mem_gfx1, 0, this.charlayout1, 0, 16);
      GfxDecodeInfo var3 = new GfxDecodeInfo(this.mem_gfx2, 0, this.charlayout2, 0, 16);
      GfxDecodeInfo var6 = new GfxDecodeInfo(this.mem_gfx3, 0, this.spritelayout1, 0, 16);
      GfxDecodeInfo var7 = new GfxDecodeInfo(this.mem_gfx3, 4096, this.spritelayout2, 0, 16);
      EmulatorProperties var8 = new EmulatorProperties();
      CpuDriver[] var5 = new CpuDriver[]{new CpuDriver(this.cpu1, 4000000, this.mra1, this.mwa1, this.interrupt, 1), new CpuDriver(this.cpu2, 3072000, this.mra2, this.mwa2, this.ior, this.iow, this.interrupt, 2)};
      var5[1].isAudioCpu = true;
      AY8910 var10 = this.ay8910;
      var8.setCpuDriver(var5);
      var8.setSoundChips(new SoundChip[]{var10});
      var8.setTimeSlicesPerFrame(1);
      var8.setFPS(60);
      var8.setDynamicPalette(true);
      var8.setVideoWidth(256);
      var8.setVideoHeight(256);
      var8.setVisibleArea(0, 255, 16, 239);
      var8.setGfxDecodeInfo(new GfxDecodeInfo[]{var4, var3, var6, var7});
      var8.setPaletteLength(128);
      var8.setVideoEmulation(this.v);
      var8.setRotation(1);
      var8.setInputPorts(this.in);
      new HighScoreParser(this.m, this.mem_cpu1, 0, 48, 0, '鎁', 32, '銡', 0, 1);
      this.v.init(var8);
      this.m.init(var8);
      return this.m;
   }

   public class Bombjack_soundlatch_r implements ReadHandler {
      public int read(int var1) {
         var1 = Bombjack.this.latch;
         Bombjack.this.latch = 0;
         return var1;
      }
   }

   public class Bombjack_soundlatch_w implements WriteHandler {
      public void write(int var1, int var2) {
         Bombjack.this.latch = var2;
      }
   }
}
