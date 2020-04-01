package net.movegaga.jemu2.driver.arcade.c1943;

import net.movegaga.jemu2.driver.BaseDriver;
import net.movegaga.jemu2.driver.Driver;

import java.net.URL;

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
import jef.map.Register;
import jef.map.WriteHandler;
import jef.scoring.HighScoreParser;
import jef.sound.SoundChip;
import jef.sound.chip.fm.FMIRQHandler;
import jef.sound.chip.fm.FMTimerHandler;
import jef.sound.chip.ym2203.YM2203;
import jef.util.RomLoader;
import jef.video.GfxDecodeInfo;
import jef.video.PaletteInitializer;
import jef.video.VideoConstants;
import jef.video.VideoInitializer;
import jef.video.VideoRenderer;

public class C1943 extends BaseDriver implements Driver, VideoConstants {
   int bankaddress;
   int[][] bgTileLayout;
   int[][] charLayout;
   int[][] charLayoutGunsmoke;
   BasicEmulator emu = new BasicEmulator();
   int[][] fgTileLayout;
   InputPort[] in;
   InterruptHandler interrupt;
   char[] mem_cpu1 = new char[196608];
   char[] mem_cpu2;
   char[] mem_gfx1;
   char[] mem_gfx2;
   char[] mem_gfx3;
   char[] mem_gfx4;
   char[] mem_gfx5;
   char[] mem_prom;
   MemoryReadAddress mra1;
   MemoryReadAddress mra2;
   MemoryWriteAddress mwa1;
   MemoryWriteAddress mwa2;
   PaletteInitializer piGunsmoke;
   ReadHandler readProtection;
   ReadHandler readUnknown;
   Register soundLatch;
   int[][] spriteLayout;
   VideoInitializer viGunsmoke;
   VC1943 video;
   VideoRenderer vrGunsmoke;
   WriteHandler writeC804;
   WriteHandler writeD806;
   WriteHandler writeGunsmokeC804;
   WriteHandler writeGunsmokeD806;
   WriteHandler writeVRAM;
   YM2203 ym;
   Z80 z80 = new Z80();
   Z80 z80_2;

   public C1943() {
      this.mra1 = new MemoryReadAddress(this.mem_cpu1);
      this.mwa1 = new MemoryWriteAddress(this.mem_cpu1);
      this.z80_2 = new Z80();
      this.mem_cpu2 = new char[65536];
      this.mra2 = new MemoryReadAddress(this.mem_cpu2);
      this.mwa2 = new MemoryWriteAddress(this.mem_cpu2);
      this.video = new VC1943(this);
      this.mem_gfx1 = new char['耀'];
      this.mem_gfx2 = new char[262144];
      this.mem_gfx3 = new char[262144];
      this.mem_gfx4 = new char[262144];
      this.mem_gfx5 = new char[65536];
      this.mem_prom = new char[3072];
      this.writeVRAM = this.video.writeVRAM();
      this.viGunsmoke = this.video.vsGunsmoke();
      this.vrGunsmoke = this.video.vuGunsmoke();
      this.piGunsmoke = this.video.piGunsmoke();
      this.ym = new YM2203(2, 1500000, (FMTimerHandler)null, (FMIRQHandler)null);
      this.soundLatch = new Register();
      this.in = new InputPort[5];
      this.interrupt = this.emu.irq0_line_hold();
      this.readProtection = new ReadProtection();
      this.writeC804 = new WriteC804();
      this.writeD806 = new WriteD806();
      this.readUnknown = new ReadUnknown();
      this.writeGunsmokeC804 = new WriteGunsmokeC804();
      this.writeGunsmokeD806 = new WriteGunsmokeD806();
      int[] var2 = new int[]{4, 0};
      int[] var1 = new int[]{0, 1, 2, 3, 8, 9, 10, 11};
      int[] var3 = new int[]{0, 16, 32, 48, 64, 80, 96, 112};
      int[] var4 = new int[]{128};
      this.charLayout = new int[][]{{8}, {8}, {2048}, {2}, var2, var1, var3, var4};
      var4 = new int[]{8};
      int[] var5 = new int[]{1024};
      var3 = new int[]{4, 0};
      var2 = new int[]{0, 1, 2, 3, 8, 9, 10, 11};
      var1 = new int[]{0, 16, 32, 48, 64, 80, 96, 112};
      int[] var6 = new int[]{128};
      this.charLayoutGunsmoke = new int[][]{{8}, var4, var5, {2}, var3, var2, var1, var6};
      var4 = new int[]{16};
      var1 = new int[]{4, 0, 1048580, 1048576};
      var3 = new int[]{0, 1, 2, 3, 8, 9, 10, 11, 256, 257, 258, 259, 264, 265, 266, 267};
      var2 = new int[]{0, 16, 32, 48, 64, 80, 96, 112, 128, 144, 160, 176, 192, 208, 224, 240};
      this.spriteLayout = new int[][]{{16}, var4, {2048}, {4}, var1, var3, var2, {512}};
      var4 = new int[]{32};
      var3 = new int[]{4, 0, 1048580, 1048576};
      var1 = new int[]{0, 1, 2, 3, 8, 9, 10, 11, 512, 513, 514, 515, 520, 521, 522, 523, 1024, 1025, 1026, 1027, 1032, 1033, 1034, 1035, 1536, 1537, 1538, 1539, 1544, 1545, 1546, 1547};
      var2 = new int[]{0, 16, 32, 48, 64, 80, 96, 112, 128, 144, 160, 176, 192, 208, 224, 240, 256, 272, 288, 304, 320, 336, 352, 368, 384, 400, 416, 432, 448, 464, 480, 496};
      var5 = new int[]{2048};
      this.fgTileLayout = new int[][]{var4, {32}, {512}, {4}, var3, var1, var2, var5};
      var4 = new int[]{32};
      var1 = new int[]{4, 0, 262148, 262144};
      var3 = new int[]{0, 1, 2, 3, 8, 9, 10, 11, 512, 513, 514, 515, 520, 521, 522, 523, 1024, 1025, 1026, 1027, 1032, 1033, 1034, 1035, 1536, 1537, 1538, 1539, 1544, 1545, 1546, 1547};
      var2 = new int[]{0, 16, 32, 48, 64, 80, 96, 112, 128, 144, 160, 176, 192, 208, 224, 240, 256, 272, 288, 304, 320, 336, 352, 368, 384, 400, 416, 432, 448, 464, 480, 496};
      this.bgTileLayout = new int[][]{{32}, var4, {128}, {4}, var1, var3, var2, {2048}};
   }

   private GfxDecodeInfo[] createDecodeInfoGunsmoke() {
      return new GfxDecodeInfo[]{new GfxDecodeInfo(this.mem_gfx1, 0, this.charLayoutGunsmoke, 0, 32), new GfxDecodeInfo(this.mem_gfx2, 0, this.fgTileLayout, 128, 16), new GfxDecodeInfo(this.mem_gfx3, 0, this.spriteLayout, 384, 16)};
   }

   private GfxDecodeInfo[] createGfxDecodeInfo() {
      return new GfxDecodeInfo[]{new GfxDecodeInfo(this.mem_gfx1, 0, this.charLayout, 0, 32), new GfxDecodeInfo(this.mem_gfx2, 0, this.fgTileLayout, 128, 16), new GfxDecodeInfo(this.mem_gfx3, 0, this.bgTileLayout, 384, 16), new GfxDecodeInfo(this.mem_gfx4, 0, this.spriteLayout, 640, 16)};
   }

   private void initInput() {
      this.in[0] = new InputPort();
      this.in[0].setBit(1, 4);
      this.in[0].setBit(2, 5);
      this.in[0].setBit(4, 0);
      this.in[0].setBit(8, 0);
      this.in[0].setBit(16, 0);
      this.in[0].setBit(32, 0);
      this.in[0].setBit(64, 1);
      this.in[0].setBit(128, 2);
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
      this.in[2].setBit(1, 137);
      this.in[2].setBit(2, 136);
      this.in[2].setBit(4, 139);
      this.in[2].setBit(8, 138);
      this.in[2].setBit(16, 140);
      this.in[2].setBit(32, 141);
      this.in[2].setBit(64, 0);
      this.in[2].setBit(128, 0);
      this.in[3] = new InputPort();
      this.in[3].setDipName(15, 15, "Difficulty");
      this.in[3].setDipName(16, 16, "2 Players Game");
      this.in[3].setDipName(32, 32, "Flip screen");
      this.in[3].setDipName(64, 64, "Freeze");
      this.in[3].setService(128);
      this.in[4] = new InputPort();
      this.in[4].setDipName(7, 7, "Coinage A");
      this.in[4].setDipName(56, 56, "Coinage B");
      this.in[4].setDipName(64, 64, "Allow Continue");
      this.in[4].setDipName(128, 128, "Demo Sounds");
   }

   private void initInputGunsmoke() {
      this.in[0] = new InputPort();
      this.in[0].setBit(1, 4);
      this.in[0].setBit(2, 5);
      this.in[0].setBit(4, 0);
      this.in[0].setBit(8, 0);
      this.in[0].setBit(16, 2);
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
      this.in[1].setBit(64, 14);
      this.in[1].setBit(128, 0);
      this.in[2] = new InputPort();
      this.in[2].setBit(1, 9);
      this.in[2].setBit(2, 8);
      this.in[2].setBit(4, 11);
      this.in[2].setBit(8, 10);
      this.in[2].setBit(16, 12);
      this.in[2].setBit(32, 13);
      this.in[2].setBit(64, 14);
      this.in[2].setBit(128, 0);
      this.in[3] = new InputPort();
      this.in[3].setDipName(3, 3, "Bonus Life");
      this.in[3].setDipName(4, 4, "Demonstration");
      this.in[3].setDipName(8, 0, "Cabinet");
      this.in[3].setDipName(48, 48, "Difficulty");
      this.in[3].setDipName(64, 64, "Freeze");
      this.in[3].setService(128);
      this.in[4] = new InputPort();
      this.in[4].setDipName(7, 7, "Coinage B");
      this.in[4].setDipName(56, 56, "Coinage A");
      this.in[4].setDipName(64, 64, "Allow Continue");
      this.in[4].setDipName(128, 128, "Demo Sounds");
   }

   private boolean initMemoryMap() {
      this.mra1.setMR(0, 32767, 1);
      this.mra1.setMR('耀', '뿿', 3);
      this.mra1.setMR('퀀', '\ud7ff', 0);
      this.mra1.set('쀀', '쀀', this.in[0]);
      this.mra1.set('쀁', '쀁', this.in[1]);
      this.mra1.set('쀂', '쀂', this.in[2]);
      this.mra1.set('쀃', '쀃', this.in[3]);
      this.mra1.set('쀄', '쀄', this.in[4]);
      this.mra1.set('쀇', '쀇', this.readProtection);
      this.mra1.setMR('\ue000', '\uffff', 0);
      this.mwa1.setMW(0, '뿿', 1);
      this.mwa1.set('저', '저', this.soundLatch);
      this.mwa1.set('전', '전', this.writeC804);
      this.mwa1.setMW('젇', '젇', 2);
      this.mwa1.set('퀀', '\ud7ff', this.writeVRAM);
      this.mwa1.setMW('\ud800', '\ud801', 0);
      this.mwa1.setMW('\ud802', '\ud802', 0);
      this.mwa1.setMW('\ud803', '\ud804', 0);
      this.mwa1.set('\ud806', '\ud806', this.writeD806);
      this.mwa1.setMW('\ue000', '\uefff', 0);
      this.mwa1.setMW('\uf000', '\uffff', 0);
      return true;
   }

   private void initMemoryMapGunsmoke() {
      this.mra1.setMR(0, 32767, 1);
      this.mra1.setMR('耀', '뿿', 3);
      this.mra1.set('쀀', '쀀', this.in[0]);
      this.mra1.set('쀁', '쀁', this.in[1]);
      this.mra1.set('쀂', '쀂', this.in[2]);
      this.mra1.set('쀃', '쀃', this.in[3]);
      this.mra1.set('쀄', '쀄', this.in[4]);
      this.mra1.set('쓉', '쓋', this.readUnknown);
      this.mra1.setMR('퀀', '폿', 0);
      this.mra1.setMR('퐀', '\ud7ff', 0);
      this.mra1.setMR('\ue000', '\uffff', 0);
      this.mwa1.setMW(0, '뿿', 1);
      this.mwa1.set('저', '저', this.soundLatch);
      this.mwa1.set('전', '전', this.writeGunsmokeC804);
      this.mwa1.setMW('젆', '젆', 2);
      this.mwa1.set('퀀', '\ud7ff', this.writeVRAM);
      this.mwa1.setMW('\ud800', '\ud801', 0);
      this.mwa1.setMW('\ud802', '\ud802', 0);
      this.mwa1.set('\ud806', '\ud806', this.writeGunsmokeD806);
      this.mwa1.setMW('\ue000', '\uefff', 0);
      this.mwa1.setMW('\uf000', '\uffff', 0);
   }

   private void initMemoryMapSound() {
      this.mra2.setMR(0, 32767, 1);
      this.mra2.setMR('쀀', '쟿', 0);
      this.mra2.set('저', '저', this.soundLatch);
      this.mwa2.setMW(0, 32767, 1);
      this.mwa2.setMW('쀀', '쟿', 0);
      this.mwa2.set('\ue000', '\ue000', this.ym.ym2203_control_port_0_w());
      this.mwa2.set('\ue001', '\ue001', this.ym.ym2203_write_port_0_w());
      this.mwa2.set('\ue002', '\ue002', this.ym.ym2203_control_port_1_w());
      this.mwa2.set('\ue003', '\ue003', this.ym.ym2203_write_port_1_w());
   }

   private boolean loadROM1943(URL var1) {
      RomLoader var2 = new RomLoader();
      var2.setZip("1943");
      var2.setMemory(this.mem_cpu1);
      var2.loadROM("1943.01", 0, '耀', -964244388);
      var2.loadROM("1943.02", 65536, 65536, -662173119);
      var2.loadROM("1943.03", 131072, 65536, 1057940076);
      var2.setMemory(this.mem_cpu2);
      var2.loadROM("1943.05", 0, '耀', -299117865);
      var2.setMemory(this.mem_gfx1);
      var2.loadROM("1943.04", 0, '耀', 1187749181);
      var2.setMemory(this.mem_gfx2);
      var2.loadROM("1943.15", 0, '耀', 1796867139);
      var2.loadROM("1943.16", '耀', '耀', 600377538);
      var2.loadROM("1943.17", 65536, '耀', 1186782471);
      var2.loadROM("1943.18", 98304, '耀', -424772704);
      var2.loadROM("1943.19", 131072, '耀', -2037728580);
      var2.loadROM("1943.20", 163840, '耀', 152561108);
      var2.loadROM("1943.21", 196608, '耀', -1678045815);
      var2.loadROM("1943.22", 229376, '耀', 83083892);
      var2.setMemory(this.mem_gfx3);
      var2.loadROM("1943.24", 0, '耀', 286474294);
      var2.loadROM("1943.25", '耀', '耀', 153942465);
      var2.setMemory(this.mem_gfx4);
      var2.loadROM("1943.06", 0, '耀', -1750284113);
      var2.loadROM("1943.07", '耀', '耀', -678465129);
      var2.loadROM("1943.08", 65536, '耀', 442656264);
      var2.loadROM("1943.09", 98304, '耀', -1841265664);
      var2.loadROM("1943.10", 131072, '耀', -2076662710);
      var2.loadROM("1943.11", 163840, '耀', 1818834205);
      var2.loadROM("1943.12", 196608, '耀', 1585380791);
      var2.loadROM("1943.13", 229376, '耀', 289637018);
      var2.setMemory(this.mem_gfx5);
      var2.loadROM("1943.14", 0, '耀', 1295803393);
      var2.loadROM("1943.23", '耀', '耀', -1523913539);
      var2.setMemory(this.mem_prom);
      var2.loadROM("bmprom.01", 0, 256, 1950490392);
      var2.loadROM("bmprom.02", 256, 256, -1406708705);
      var2.loadROM("bmprom.03", 512, 256, 622835455);
      var2.loadROM("bmprom.05", 768, 256, 543626192);
      var2.loadROM("bmprom.10", 1024, 256, 868370716);
      var2.loadROM("bmprom.09", 1280, 256, -1360377097);
      var2.loadROM("bmprom.12", 1536, 256, -1047879370);
      var2.loadROM("bmprom.11", 1792, 256, 1079684663);
      var2.loadROM("bmprom.08", 2048, 256, -1040119138);
      var2.loadROM("bmprom.07", 2304, 256, -1251004221);
      var2.loadROM("bmprom.04", 2560, 256, -1851219231);
      var2.loadROM("bmprom.06", 2816, 256, 246370648);
      var2.loadZip(var1);
      return true;
   }

   private boolean loadROM1943Kai(URL var1) {
      RomLoader var2 = new RomLoader();
      var2.setZip("1943kai");
      var2.setMemory(this.mem_cpu1);
      var2.loadROM("1943kai.01", 0, '耀', 2099384795);
      var2.loadROM("1943kai.02", 65536, 65536, 784058565);
      var2.loadROM("1943kai.03", 131072, 65536, 1197107909);
      var2.setMemory(this.mem_cpu2);
      var2.loadROM("1943kai.05", 0, '耀', 636713303);
      var2.setMemory(this.mem_gfx1);
      var2.loadROM("1943kai.04", 0, '耀', -2008381806);
      var2.setMemory(this.mem_gfx2);
      var2.loadROM("1943kai.15", 0, '耀', 1796867139);
      var2.loadROM("1943kai.16", '耀', '耀', -1810432499);
      var2.loadROM("1943kai.17", 65536, '耀', 1029360313);
      var2.loadROM("1943kai.18", 98304, '耀', 2070075933);
      var2.loadROM("1943kai.19", 131072, '耀', -2037728580);
      var2.loadROM("1943kai.20", 163840, '耀', -1190959935);
      var2.loadROM("1943kai.21", 196608, '耀', -1937774774);
      var2.loadROM("1943kai.22", 229376, '耀', -705721842);
      var2.setMemory(this.mem_gfx3);
      var2.loadROM("1943kai.24", 0, '耀', -1088917774);
      var2.loadROM("1943kai.25", '耀', '耀', -1487537423);
      var2.setMemory(this.mem_gfx4);
      var2.loadROM("1943kai.06", 0, '耀', 1602107571);
      var2.loadROM("1943kai.07", '耀', '耀', -13151747);
      var2.loadROM("1943kai.08", 65536, '耀', 362631613);
      var2.loadROM("1943kai.09", 98304, '耀', -2038176814);
      var2.loadROM("1943kai.10", 131072, '耀', 504206705);
      var2.loadROM("1943kai.11", 163840, '耀', -235118879);
      var2.loadROM("1943kai.12", 196608, '耀', 256950273);
      var2.loadROM("1943kai.13", 229376, '耀', -48574578);
      var2.setMemory(this.mem_gfx5);
      var2.loadROM("1943kai.14", 0, '耀', -821077421);
      var2.loadROM("1943kai.23", '耀', '耀', 402095865);
      var2.setMemory(this.mem_prom);
      var2.loadROM("bmk01.bin", 0, 256, -536745421);
      var2.loadROM("bmk02.bin", 256, 256, -1355491046);
      var2.loadROM("bmk03.bin", 512, 256, 1139406575);
      var2.loadROM("bmk05.bin", 768, 256, 1099401524);
      var2.loadROM("bmk10.bin", 1024, 256, -565921976);
      var2.loadROM("bmk09.bin", 1280, 256, 1508530112);
      var2.loadROM("bmk12.bin", 1536, 256, -2023360336);
      var2.loadROM("bmk11.bin", 1792, 256, -2018998962);
      var2.loadROM("bmk08.bin", 2048, 256, -623804883);
      var2.loadROM("bmk07.bin", 2304, 256, 1982889869);
      var2.loadROM("bmprom.04", 2560, 256, -1851219231);
      var2.loadROM("bmprom.06", 2816, 256, 246370648);
      var2.loadZip(var1);
      return true;
   }

   private boolean loadROMGunsmoke(URL var1) {
      RomLoader var2 = new RomLoader();
      var2.setZip("gunsmoke");
      var2.setMemory(this.mem_cpu1);
      var2.loadROM("09n_gs03.bin", 0, '耀', 1084255471);
      var2.loadROM("10n_gs04.bin", 65536, '耀', -1924447681);
      var2.loadROM("12n_gs05.bin", 98304, '耀', 727083003);
      var2.setMemory(this.mem_cpu2);
      var2.loadROM("14h_gs02.bin", 0, '耀', -847631304);
      var2.setMemory(this.mem_gfx1);
      var2.loadROM("11f_gs01.bin", 0, 16384, -1239495013);
      var2.setMemory(this.mem_gfx2);
      var2.loadROM("06c_gs13.bin", 0, '耀', -159998011);
      var2.loadROM("05c_gs12.bin", '耀', '耀', -644368500);
      var2.loadROM("04c_gs11.bin", 65536, '耀', 307996046);
      var2.loadROM("02c_gs10.bin", 98304, '耀', -194395844);
      var2.loadROM("06a_gs09.bin", 131072, '耀', 1402935341);
      var2.loadROM("05a_gs08.bin", 163840, '耀', -394374547);
      var2.loadROM("04a_gs07.bin", 196608, '耀', 1132642514);
      var2.loadROM("02a_gs06.bin", 229376, '耀', 1286596518);
      var2.setMemory(this.mem_gfx3);
      var2.loadROM("06n_gs22.bin", 0, '耀', -593735540);
      var2.loadROM("04n_gs21.bin", '耀', '耀', 1753757513);
      var2.loadROM("03n_gs20.bin", 65536, '耀', 199832301);
      var2.loadROM("01n_gs19.bin", 98304, '耀', 1661415315);
      var2.loadROM("06l_gs18.bin", 131072, '耀', -157664132);
      var2.loadROM("04l_gs17.bin", 163840, '耀', 1318606378);
      var2.loadROM("03l_gs16.bin", 196608, '耀', 228180915);
      var2.loadROM("01l_gs15.bin", 229376, '耀', 2132027150);
      var2.setMemory(this.mem_gfx4);
      var2.loadROM("11c_gs14.bin", 0, '耀', 183826411);
      var2.setMemory(this.mem_prom);
      var2.loadROM("03b_g-01.bin", 0, 256, 49632649);
      var2.loadROM("04b_g-02.bin", 256, 256, -505188903);
      var2.loadROM("05b_g-03.bin", 512, 256, -1735157312);
      var2.loadROM("09d_g-04.bin", 768, 256, -1872358731);
      var2.loadROM("14a_g-06.bin", 1024, 256, 1251844491);
      var2.loadROM("15a_g-07.bin", 1280, 256, -879520516);
      var2.loadROM("09f_g-09.bin", 1536, 256, 1022236702);
      var2.loadROM("08f_g-08.bin", 1792, 256, -275657262);
      var2.loadROM("02j_g-10.bin", 2048, 256, 246370648);
      var2.loadROM("01f_g-05.bin", 2304, 256, 633932842);
      var2.loadZip(var1);
      return true;
   }

   public Emulator createEmulator(URL var1, String var2) {
      super.createEmulator(var1, var2);
      EmulatorProperties var3 = null;
      if(var2.equals("1943")) {
         this.loadROM1943(var1);
         var3 = this.createMachineDriver1943();
         new HighScoreParser(this.emu, this.mem_cpu1, 36, 0, 0, '큾', 32, '턾', 0, 1);
      } else if(var2.equals("1943kai")) {
         this.loadROM1943Kai(var1);
         var3 = this.createMachineDriver1943();
         new HighScoreParser(this.emu, this.mem_cpu1, 36, 0, 0, '큾', 32, '턾', 0, 1);
      } else if(var2.equals("gunsmoke")) {
         this.loadROMGunsmoke(var1);
         var3 = this.createMachineDriverGunsmoke();
         new HighScoreParser(this.emu, this.mem_cpu1, 36, 0, 0, '큾', 32, '턾', 0, 10);
      }

      this.video.init(var3);
      this.emu.init(var3);
      return this.emu;
   }

   public EmulatorProperties createMachineDriver1943() {
      EmulatorProperties var1 = new EmulatorProperties();
      this.initInput();
      this.initMemoryMap();
      this.initMemoryMapSound();
      CpuDriver[] var3 = new CpuDriver[]{new CpuDriver(this.z80, 6000000, this.mra1, this.mwa1, this.interrupt, 1), new CpuDriver(this.z80_2, 3000000, this.mra2, this.mwa2, this.interrupt, 4)};
      var3[1].isAudioCpu = true;
      YM2203 var2 = this.ym;
      var1.setCpuDriver(var3);
      var1.setSoundChips(new SoundChip[]{var2});
      var1.setFPS(60);
      var1.setVideoWidth(256);
      var1.setVideoHeight(256);
      var1.setVisibleArea(0, 255, 16, 239);
      var1.setGfxDecodeInfo(this.createGfxDecodeInfo());
      var1.setPaletteLength(256);
      var1.setColorTableLength(896);
      var1.setPaletteInitializer(this.video);
      var1.setVideoInitializer(this.video);
      var1.setVideoUpdater(this.video);
      var1.setInputPorts(this.in);
      var1.setRotation(3);
      return var1;
   }

   public EmulatorProperties createMachineDriverGunsmoke() {
      EmulatorProperties var3 = new EmulatorProperties();
      this.initInputGunsmoke();
      this.initMemoryMapGunsmoke();
      this.initMemoryMapSound();
      CpuDriver[] var1 = new CpuDriver[]{new CpuDriver(this.z80, 4000000, this.mra1, this.mwa1, this.interrupt, 1), new CpuDriver(this.z80_2, 3000000, this.mra2, this.mwa2, this.interrupt, 4)};
      var1[1].isAudioCpu = true;
      YM2203 var2 = this.ym;
      var3.setCpuDriver(var1);
      var3.setSoundChips(new SoundChip[]{var2});
      var3.setFPS(60);
      var3.setVideoWidth(256);
      var3.setVideoHeight(256);
      var3.setVisibleArea(0, 255, 16, 239);
      var3.setGfxDecodeInfo(this.createDecodeInfoGunsmoke());
      var3.setPaletteLength(256);
      var3.setColorTableLength(896);
      var3.setPaletteInitializer(this.piGunsmoke);
      var3.setVideoInitializer(this.viGunsmoke);
      var3.setVideoUpdater(this.vrGunsmoke);
      var3.setInputPorts(this.in);
      var3.setRotation(3);
      return var3;
   }

   public class ReadProtection implements ReadHandler {
      public int read(int var1) {
         return C1943.this.z80.B;
      }
   }

   public class ReadUnknown implements ReadHandler {
      public int read(int var1) {
         return 0;
      }
   }

   public class WriteC804 implements WriteHandler {
      public void write(int var1, int var2) {
         C1943.this.bankaddress = 65536 + (var2 & 28) * 4096;
         C1943.this.mra1.setBankAddress(1, C1943.this.bankaddress);
         C1943.this.video.chon = var2 & 128;
      }
   }

   public class WriteD806 implements WriteHandler {
      public void write(int var1, int var2) {
         C1943.this.video.sc1on = var2 & 16;
         C1943.this.video.sc2on = var2 & 32;
         C1943.this.video.objon = var2 & 64;
      }
   }

   public class WriteGunsmokeC804 implements WriteHandler {
      public void write(int var1, int var2) {
         C1943.this.mra1.setBankAddress(1, 65536 + (var2 & 12) * 4096);
         C1943.this.video.chon = var2 & 128;
      }
   }

   public class WriteGunsmokeD806 implements WriteHandler {
      public void write(int var1, int var2) {
         C1943.this.video.sprite3bank = var2 & 7;
         C1943.this.video.bgon = var2 & 16;
         C1943.this.video.objon = var2 & 32;
      }
   }
}
