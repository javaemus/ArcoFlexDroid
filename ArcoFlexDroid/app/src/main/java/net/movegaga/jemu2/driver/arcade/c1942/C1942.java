package net.movegaga.jemu2.driver.arcade.c1942;

import net.movegaga.jemu2.driver.BaseDriver;
import net.movegaga.jemu2.driver.Driver;

import java.net.URL;

import jef.cpu.Cpu;
import jef.cpu.Z80;
import jef.cpuboard.CpuDriver;
import jef.machine.BasicEmulator;
import jef.machine.Emulator;
import jef.machine.EmulatorProperties;
import jef.map.InputPort;
import jef.map.InterruptHandler;
import jef.map.MemoryReadAddress;
import jef.map.MemoryWriteAddress;
import jef.map.Register;
import jef.map.WriteHandler;
import jef.scoring.HighScoreParser;
import jef.sound.SoundChip;
import jef.sound.chip.AY8910;
import jef.util.RomLoader;
import jef.video.GfxDecodeInfo;
import jef.video.VideoConstants;

public class C1942 extends BaseDriver implements Driver, VideoConstants {
   AY8910 ay;
   Cpu cpu1 = new Z80();
   Cpu cpu2;
   BasicEmulator emu = new BasicEmulator();
   InputPort[] in;
   InterruptHandler interrupt1;
   InterruptHandler interrupt2;
   private final int[][] layoutBgTiles;
   private final int[][] layoutCharacters;
   private final int[][] layoutSprites;
   char[] mem_cpu1 = new char[114688];
   char[] mem_cpu2;
   char[] mem_gfx1;
   char[] mem_gfx2;
   char[] mem_gfx3;
   char[] mem_prom;
   MemoryReadAddress mra1;
   MemoryReadAddress mra2;
   MemoryWriteAddress mwa1;
   MemoryWriteAddress mwa2;
   EmulatorProperties props = new EmulatorProperties();
   Register soundLatch;
   VC1942 video;
   WriteHandler writeBankSwitch;
   WriteHandler writePaletteBank;
   WriteHandler writeScroll;

   public C1942() {
      this.mra1 = new MemoryReadAddress(this.mem_cpu1);
      this.mwa1 = new MemoryWriteAddress(this.mem_cpu1);
      this.interrupt1 = new Interrupt1((Interrupt1)null);
      this.cpu2 = new Z80();
      this.mem_cpu2 = new char[65536];
      this.mra2 = new MemoryReadAddress(this.mem_cpu2);
      this.mwa2 = new MemoryWriteAddress(this.mem_cpu2);
      this.interrupt2 = this.emu.irq0_line_hold();
      this.video = new VC1942(this);
      this.mem_gfx1 = new char[8192];
      this.mem_gfx2 = new char['쀀'];
      this.mem_gfx3 = new char[65536];
      this.mem_prom = new char[2560];
      this.writeScroll = this.video.writeScrollRegs();
      this.writePaletteBank = this.video.writePaletteBank();
      this.ay = new AY8910(2, 1500000);
      this.soundLatch = new Register();
      this.in = new InputPort[5];
      this.writeBankSwitch = new WriteBankSwitch((WriteBankSwitch)null);
      int[] var8 = new int[]{this.RGN_FRAC(1, 1)};
      int[] var9 = new int[]{2};
      int[] var5 = new int[]{4, 0};
      int[] var7 = new int[]{0, 1, 2, 3, 8, 9, 10, 11};
      int[] var6 = new int[]{0, 16, 32, 48, 64, 80, 96, 112};
      int[] var10 = new int[]{128};
      this.layoutCharacters = new int[][]{{8}, {8}, var8, var9, var5, var7, var6, var10};
      int var4 = this.RGN_FRAC(1, 3);
      int var1 = this.RGN_FRAC(2, 3);
      int var3 = this.RGN_FRAC(1, 3);
      int var2 = this.RGN_FRAC(0, 3);
      var6 = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 128, 129, 130, 131, 132, 133, 134, 135};
      var5 = new int[]{0, 8, 16, 24, 32, 40, 48, 56, 64, 72, 80, 88, 96, 104, 112, 120};
      this.layoutBgTiles = new int[][]{{16}, {16}, {var4}, {3}, {var1, var3, var2}, var6, var5, {256}};
      var1 = this.RGN_FRAC(1, 2);
      var5 = new int[]{4, 0, this.RGN_FRAC(1, 2) + 4, this.RGN_FRAC(1, 2) + 0};
      var6 = new int[]{0, 1, 2, 3, 8, 9, 10, 11, 256, 257, 258, 259, 264, 265, 266, 267};
      var7 = new int[]{0, 16, 32, 48, 64, 80, 96, 112, 128, 144, 160, 176, 192, 208, 224, 240};
      this.layoutSprites = new int[][]{{16}, {16}, {var1}, {4}, var5, var6, var7, {512}};
   }

   private boolean initInput() {
      this.in[0] = new InputPort();
      this.in[0].setBit(1, 4);
      this.in[0].setBit(2, 5);
      this.in[0].setBit(4, 0);
      this.in[0].setBit(8, 0);
      this.in[0].setBit(16, 16);
      this.in[0].setBit(32, 0);
      this.in[0].setBit(64, 2);
      this.in[0].setBit(128, 1);
      this.in[1] = new InputPort();
      this.in[1].setBit(1, 9);
      this.in[1].setBit(2, 8);
      this.in[1].setBit(4, 11);
      this.in[1].setBit(8, 10);
      this.in[1].setBit(16, 12);
      this.in[1].setBit(32, 13);
      this.in[1].setBit(64, 0);
      this.in[1].setBit(128, 0);
      this.in[2] = new InputPort();
      this.in[2].setBit(1, 9);
      this.in[2].setBit(2, 8);
      this.in[2].setBit(4, 11);
      this.in[2].setBit(8, 10);
      this.in[2].setBit(16, 12);
      this.in[2].setBit(32, 13);
      this.in[2].setBit(64, 0);
      this.in[2].setBit(128, 0);
      this.in[3] = new InputPort();
      this.in[3].setDipName(7, 7, "Coinage A");
      this.in[3].setDipName(8, 0, "Cabinet");
      this.in[3].setDipName(48, 48, "Bonus Life");
      this.in[3].setDipName(192, 192, "Lives");
      this.in[4] = new InputPort();
      this.in[4].setDipName(7, 7, "Coinage B");
      this.in[4].setService(8);
      this.in[4].setDipName(16, 16, "Flip Screen");
      this.in[4].setDipName(96, 96, "Difficulty");
      this.in[4].setDipName(128, 128, "Freeze");
      return true;
   }

   private void initMemoryMap() {
      this.mra1.setMR(0, 32767, 1);
      this.mra1.setMR('耀', '뿿', 3);
      this.mra1.set('쀀', '쀀', this.in[0]);
      this.mra1.set('쀁', '쀁', this.in[1]);
      this.mra1.set('쀂', '쀂', this.in[2]);
      this.mra1.set('쀃', '쀃', this.in[3]);
      this.mra1.set('쀄', '쀄', this.in[4]);
      this.mra1.setMR('퀀', '\udbff', 0);
      this.mra1.setMR('\ue000', '\uefff', 0);
      this.mwa1.setMW(0, '\uffff', 0);
      this.mwa1.setMW(0, '뿿', 1);
      this.mwa1.set('저', '저', this.soundLatch);
      this.mwa1.set('젂', '젃', this.writeScroll);
      this.mwa1.set('젅', '젅', this.writePaletteBank);
      this.mwa1.set('젆', '젆', this.writeBankSwitch);
      this.mwa1.setMW('찀', '챿', 0);
      this.mwa1.setMW('퀀', '\ud7ff', 0);
      this.mwa1.setMW('\ud800', '\udbff', 0);
      this.mwa1.setMW('\ue000', '\uefff', 0);
      this.mra2.setMR(0, 16383, 1);
      this.mra2.setMR(16384, 18431, 0);
      this.mra2.set(24576, 24576, this.soundLatch);
      this.mwa2.setMW(0, 16383, 1);
      this.mwa2.setMW(16384, 18431, 0);
      this.mwa2.set('耀', '耀', this.ay.AY8910_control_port_0_w());
      this.mwa2.set('老', '老', this.ay.AY8910_write_port_0_w());
      this.mwa2.set('쀀', '쀀', this.ay.AY8910_control_port_1_w());
      this.mwa2.set('쀁', '쀁', this.ay.AY8910_write_port_1_w());
   }

   private void loadRoms(URL var1) {
      RomLoader var2 = new RomLoader();
      var2.setZip("1942");
      var2.setMemory(this.mem_cpu1);
      var2.loadROM("1-n3a.bin", 0, 16384, 1075846059);
      var2.loadROM("1-n4.bin", 16384, 16384, -1509243324);
      var2.loadROM("1-n5.bin", 65536, 16384, -2090894556);
      var2.loadROM("1-n6.bin", 81920, 8192, -2112068479);
      var2.loadROM("1-n7.bin", 98304, 16384, 1576347105);
      var2.setMemory(this.mem_cpu2);
      var2.loadROM("1-c11.bin", 0, 16384, -1115164565);
      var2.setMemory(this.mem_gfx1);
      var2.loadROM("1-f2.bin", 0, 8192, 1857855889);
      var2.setMemory(this.mem_gfx2);
      var2.loadROM("2-a1.bin", 0, 8192, 948230635);
      var2.loadROM("2-a2.bin", 8192, 8192, -1717766432);
      var2.loadROM("2-a3.bin", 16384, 8192, -1898240198);
      var2.loadROM("2-a4.bin", 24576, 8192, 975644355);
      var2.loadROM("2-a5.bin", '耀', 8192, 466868411);
      var2.loadROM("2-a6.bin", 'ꀀ', 8192, 1703871172);
      var2.setMemory(this.mem_gfx3);
      var2.loadROM("2-l1.bin", 0, 16384, 623427270);
      var2.loadROM("2-l2.bin", 16384, 16384, -124614742);
      var2.loadROM("2-n1.bin", '耀', 16384, 38017272);
      var2.loadROM("2-n2.bin", '쀀', 16384, -490216311);
      var2.setMemory(this.mem_prom);
      var2.loadROM("08e_sb-5.bin", 0, 256, -1817476781);
      var2.loadROM("09e_sb-6.bin", 256, 256, -1967894659);
      var2.loadROM("10e_sb-7.bin", 512, 256, -189929052);
      var2.loadROM("f01_sb-0.bin", 768, 256, 1615321371);
      var2.loadROM("06d_sb-4.bin", 1024, 256, 1213765261);
      var2.loadROM("03k_sb-8.bin", 1280, 256, -151332541);
      var2.loadROM("01d_sb-2.bin", 1536, 256, -1950829601);
      var2.loadROM("02d_sb-3.bin", 1792, 256, 990681519);
      var2.loadROM("k06_sb-1.bin", 2048, 256, 1898628360);
      var2.loadROM("01m_sb-9.bin", 2304, 256, 1226924892);
      var2.loadZip(var1);
   }

   public Emulator createEmulator(URL var1, String var2) {
      super.createEmulator(var1, var2);
      this.emu = new BasicEmulator();
      this.initInput();
      this.initMemoryMap();
      this.loadRoms(var1);
      CpuDriver var6 = new CpuDriver(this.cpu1, 4000000, this.mra1, this.mwa1, this.interrupt1, 2);
      CpuDriver var7 = new CpuDriver(this.cpu2, 3000000, this.mra2, this.mwa2, this.interrupt2, 4);
      AY8910 var5 = this.ay;
      GfxDecodeInfo var4 = new GfxDecodeInfo(this.mem_gfx1, 0, this.layoutCharacters, 0, 64);
      GfxDecodeInfo var3 = new GfxDecodeInfo(this.mem_gfx2, 0, this.layoutBgTiles, 256, 128);
      GfxDecodeInfo var8 = new GfxDecodeInfo(this.mem_gfx3, 0, this.layoutSprites, 1280, 16);
      this.props.setCpuDriver(new CpuDriver[]{var6, var7});
      this.props.setSoundChips(new SoundChip[]{var5});
      this.props.setFPS(60);
      this.props.setTimeSlicesPerFrame(100);
      this.props.setVisibleArea(0, 255, 16, 239);
      this.props.setVideoWidth(256);
      this.props.setVideoHeight(256);
      this.props.setGfxDecodeInfo(new GfxDecodeInfo[]{var4, var3, var8});
      this.props.setColorTableLength(1536);
      this.props.setPaletteLength(256);
      this.props.setPaletteInitializer(this.video);
      this.props.setVideoInitializer(this.video);
      this.props.setVideoUpdater(this.video);
      this.props.setRotation(3);
      this.props.setInputPorts(this.in);
      this.video.init(this.props);
      this.emu.init(this.props);
      new HighScoreParser(this.emu, this.mem_cpu1, 48, 0, 0, '큾', 32, '턾', 0, 1);
      return this.emu;
   }

   private class Interrupt1 implements InterruptHandler {
      private Interrupt1() {
      }

      // $FF: synthetic method
      Interrupt1(Interrupt1 var2) {
         this();
      }

      public int irq() {
         if(C1942.this.emu.getCurrentSlice() != 0) {
            C1942.this.cpu1.setProperty(0, 8);
         } else {
            C1942.this.cpu1.setProperty(0, 16);
         }

         return 0;
      }
   }

   private class WriteBankSwitch implements WriteHandler {
      private WriteBankSwitch() {
      }

      // $FF: synthetic method
      WriteBankSwitch(WriteBankSwitch var2) {
         this();
      }

      public void write(int var1, int var2) {
         C1942.this.mra1.setBankAddress(1, 65536 + (var2 & 3) * 16384);
      }
   }
}
