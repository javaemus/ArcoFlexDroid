package net.movegaga.jemu2.driver.arcade.m62;

import net.movegaga.jemu2.driver.BaseDriver;

import java.net.URL;

import jef.cpu.z80.Z80;
import jef.cpuboard.CpuDriver;
import jef.machine.BasicEmulator;
import jef.machine.Emulator;
import jef.machine.EmulatorProperties;
import jef.map.IOReadPort;
import jef.map.IOWritePort;
import jef.map.InputPort;
import jef.map.InterruptHandler;
import jef.map.MemoryReadAddress;
import jef.map.MemoryWriteAddress;
import jef.map.ReadHandler;
import jef.map.SwitchedInterrupt;
import jef.map.WriteHandler;
import jef.util.RomLoader;
import jef.video.GfxDecodeInfo;
import jef.video.VideoInitializer;

public class M62 extends BaseDriver {
   int bankSwap;
   Z80 cpu1 = new Z80();
   BasicEmulator emu = new BasicEmulator();
   InputPort[] in;
   InterruptHandler interrupt1;
   IOReadPort ior1;
   IOWritePort iow1;
   char[] mem_cpu1 = new char[98304];
   char[] mem_cpu2;
   char[] mem_gfx1;
   char[] mem_gfx2;
   char[] mem_prom;
   MemoryReadAddress mra1;
   MemoryWriteAddress mwa1;
   int[][] spriteLayout1024;
   int[][] spriteLayout256;
   int[][] spriteLayout512;
   int[][] tileLayout1024;
   int[][] tileLayout2048;
   VM62 video;
   VideoInitializer videoInitKungFum;

   public M62() {
      this.mra1 = new MemoryReadAddress(this.mem_cpu1);
      this.mwa1 = new MemoryWriteAddress(this.mem_cpu1);
      this.ior1 = new IOReadPort();
      this.iow1 = new IOWritePort();
      this.interrupt1 = new SwitchedInterrupt(0);
      this.mem_cpu2 = new char[65536];
      this.video = new VM62(this);
      this.mem_gfx1 = new char['쀀'];
      this.mem_gfx2 = new char[98304];
      this.mem_prom = new char[2080];
      this.videoInitKungFum = this.video.initKungFum();
      this.in = new InputPort[5];
      int[] var8 = new int[]{8};
      int[] var9 = new int[]{8};
      int[] var10 = new int[]{1024};
      int[] var11 = new int[]{3};
      int[] var5 = new int[]{0, 65536, 131072};
      int[] var6 = new int[]{0, 1, 2, 3, 4, 5, 6, 7};
      int[] var7 = new int[]{0, 8, 16, 24, 32, 40, 48, 56};
      this.tileLayout1024 = new int[][]{var8, var9, var10, var11, var5, var6, var7, {64}};
      var8 = new int[]{8};
      var6 = new int[]{0, 131072, 262144};
      var7 = new int[]{0, 1, 2, 3, 4, 5, 6, 7};
      var5 = new int[]{0, 8, 16, 24, 32, 40, 48, 56};
      this.tileLayout2048 = new int[][]{var8, {8}, {2048}, {3}, var6, var7, var5, {64}};
      var7 = new int[]{16};
      int var1 = this.RGN_FRAC(1, 3);
      int var3 = this.RGN_FRAC(0, 3);
      int var2 = this.RGN_FRAC(1, 3);
      int var4 = this.RGN_FRAC(2, 3);
      var5 = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 128, 129, 130, 131, 132, 133, 134, 135};
      var6 = new int[]{0, 8, 16, 24, 32, 40, 48, 56, 64, 72, 80, 88, 96, 104, 112, 120};
      this.spriteLayout256 = new int[][]{var7, {16}, {var1}, {3}, {var3, var2, var4}, var5, var6, {256}};
      var8 = new int[]{16};
      var6 = new int[]{0, 131072, 262144};
      var7 = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 128, 129, 130, 131, 132, 133, 134, 135};
      var5 = new int[]{0, 8, 16, 24, 32, 40, 48, 56, 64, 72, 80, 88, 96, 104, 112, 120};
      var9 = new int[]{256};
      this.spriteLayout512 = new int[][]{var8, {16}, {512}, {3}, var6, var7, var5, var9};
      var8 = new int[]{16};
      var9 = new int[]{16};
      var10 = new int[]{1024};
      var11 = new int[]{3};
      var6 = new int[]{0, 262144, 524288};
      var7 = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 128, 129, 130, 131, 132, 133, 134, 135};
      var5 = new int[]{0, 8, 16, 24, 32, 40, 48, 56, 64, 72, 80, 88, 96, 104, 112, 120};
      this.spriteLayout1024 = new int[][]{var8, var9, var10, var11, var6, var7, var5, {256}};
   }

   private GfxDecodeInfo[] createGfxDecodeInfoKungFum() {
      return new GfxDecodeInfo[]{new GfxDecodeInfo(this.mem_gfx1, 0, this.tileLayout1024, 0, 32), new GfxDecodeInfo(this.mem_gfx2, 0, this.spriteLayout1024, 256, 32)};
   }

   private GfxDecodeInfo[] createGfxDecodeInfoLdrun() {
      return new GfxDecodeInfo[]{new GfxDecodeInfo(this.mem_gfx1, 0, this.tileLayout1024, 0, 32), new GfxDecodeInfo(this.mem_gfx2, 0, this.spriteLayout256, 256, 32)};
   }

   private GfxDecodeInfo[] createGfxDecodeInfoLdrun2() {
      return new GfxDecodeInfo[]{new GfxDecodeInfo(this.mem_gfx1, 0, this.tileLayout1024, 0, 32), new GfxDecodeInfo(this.mem_gfx2, 0, this.spriteLayout512, 256, 32)};
   }

   private GfxDecodeInfo[] createGfxDecodeInfoLdrun3() {
      return new GfxDecodeInfo[]{new GfxDecodeInfo(this.mem_gfx1, 0, this.tileLayout2048, 0, 32), new GfxDecodeInfo(this.mem_gfx2, 0, this.spriteLayout512, 256, 32)};
   }

   private GfxDecodeInfo[] createGfxDecodeInfoLdrun4() {
      return new GfxDecodeInfo[]{new GfxDecodeInfo(this.mem_gfx1, 0, this.tileLayout2048, 0, 32), new GfxDecodeInfo(this.mem_gfx2, 0, this.spriteLayout1024, 256, 32)};
   }

   private EmulatorProperties createMachineDriverKungfum() {
      EmulatorProperties var1 = new EmulatorProperties();
      this.initReadMap();
      this.initWriteMapKungfum();
      this.initReadPortLdrun();
      this.initWritePortLdrun();
      var1.setCpuDriver(new CpuDriver[]{new CpuDriver(this.cpu1, 4000000, this.mra1, this.mwa1, this.ior1, this.iow1, this.interrupt1, 1)});
      var1.setFPS(60);
      var1.setVBlankDuration(1790);
      var1.setVideoDimensions(512, 256);
      var1.setVisibleArea(128, 383, 0, 255);
      var1.setGfxDecodeInfo(this.createGfxDecodeInfoKungFum());
      var1.setPaletteLength(512);
      var1.setPaletteInitializer(this.video);
      var1.setVideoInitializer(this.videoInitKungFum);
      var1.setVideoUpdater(this.video.kungfumRenderer());
      return var1;
   }

   private void initInput() {
      this.in[0] = new InputPort();
      this.in[0].setBit(1, 4);
      this.in[0].setBit(2, 5);
      this.in[0].setBit(4, 16);
      this.in[0].setBit(8, 1);
      this.in[0].setBits(240, 240);
      this.in[1] = new InputPort();
      this.in[1].setBit(1, 9);
      this.in[1].setBit(2, 8);
      this.in[1].setBit(4, 11);
      this.in[1].setBit(8, 10);
      this.in[1].setBits(80, 80);
      this.in[1].setBit(32, 13);
      this.in[1].setBit(128, 12);
      this.in[2] = new InputPort();
      this.in[2].setBits(255, 255);
      this.in[3] = new InputPort();
      this.in[3].setBits(255, 255);
      this.in[4] = new InputPort();
      this.in[4].setBits(255, 255);
   }

   private void initReadMap() {
   }

   private void initReadMapLdrun2() {
      this.mra1.setMR('耀', '\u9fff', 3);
   }

   private void initReadMapLdrun3() {
      this.mra1.set('저', '저', new ReadProtection5Ldrun3());
      this.mra1.set('찀', '찀', new ReadProtection5Ldrun3());
      this.mra1.set('쿿', '쿿', new ReadProtection7Ldrun3());
   }

   private void initReadPortLdrun() {
      this.ior1.set(0, 0, this.in[0]);
      this.ior1.set(1, 1, this.in[1]);
      this.ior1.set(2, 2, this.in[2]);
      this.ior1.set(3, 3, this.in[3]);
      this.ior1.set(4, 4, this.in[4]);
   }

   private void initReadPortLdrun2() {
      this.ior1.set(0, 0, this.in[0]);
      this.ior1.set(1, 1, this.in[1]);
      this.ior1.set(2, 2, this.in[2]);
      this.ior1.set(3, 3, this.in[3]);
      this.ior1.set(4, 4, this.in[4]);
      this.ior1.set(128, 128, new ReadLdrun2Bankswitch());
   }

   private void initWriteMap() {
      this.mwa1.setMW(0, 32767, 1);
      this.mwa1.set('퀀', '\udfff', this.video);
   }

   private void initWriteMapKungfum() {
      this.mwa1.setMW(0, 32767, 1);
      this.mwa1.set('ꀀ', 'ꀀ', this.video.writeScrollLow());
      this.mwa1.set('뀀', '뀀', this.video.writeScrollHigh());
      this.mwa1.set('퀀', '\udfff', this.video);
   }

   private void initWriteMapLdrun2() {
      this.mwa1.setMW(0, '\u9fff', 1);
      this.mwa1.set('퀀', '\udfff', this.video);
   }

   private void initWriteMapLdrun3() {
      this.mwa1.setMW(0, '뿿', 1);
      this.mwa1.set('퀀', '\udfff', this.video);
   }

   private void initWritePortLdrun() {
   }

   private void initWritePortLdrun2() {
      this.iow1.set(128, 128, new WriteLdrun2Bankswitch());
   }

   private void initWritePortLdrun3() {
      this.iow1.set(128, 128, this.video.writeVScroll());
   }

   private void loadRomKungFum(URL var1) {
      RomLoader var2 = new RomLoader();
      var2.setZip("kungfum");
      var2.setMemory(this.mem_cpu1);
      var2.loadROM("a-4e-c.bin", 0, 16384, -1226649469);
      var2.loadROM("a-4d-c.bin", 16384, 16384, 1966248334);
      var2.setMemory(this.mem_cpu2);
      var2.loadROM("a-3e-.bin", 'ꀀ', 8192, 1491630768);
      var2.loadROM("a-3f-.bin", '쀀', 8192, -937545238);
      var2.loadROM("a-3h-.bin", '\ue000', 8192, -643843691);
      var2.setMemory(this.mem_gfx1);
      var2.loadROM("g-4c-a.bin", 0, 8192, 1798097352);
      var2.loadROM("g-4d-a.bin", 8192, 8192, -968297128);
      var2.loadROM("g-4e-a.bin", 16384, 8192, -68606098);
      var2.setMemory(this.mem_gfx2);
      var2.loadROM("b-4k-.bin", 0, 8192, 385569104);
      var2.loadROM("b-4f-.bin", 8192, 8192, 1735678515);
      var2.loadROM("b-4l-.bin", 16384, 8192, -1122229663);
      var2.loadROM("b-4h-.bin", 24576, 8192, -1966740166);
      var2.loadROM("b-3n-.bin", '耀', 8192, 681710506);
      var2.loadROM("b-4n-.bin", 'ꀀ', 8192, -719155725);
      var2.loadROM("b-4m-.bin", '쀀', 8192, -1318198030);
      var2.loadROM("b-3m-.bin", '\ue000', 8192, -341780885);
      var2.loadROM("b-4c-.bin", 65536, 8192, 19499141);
      var2.loadROM("b-4e-.bin", 73728, 8192, -948205612);
      var2.loadROM("b-4d-.bin", 81920, 8192, 1785749855);
      var2.loadROM("b-4a-.bin", 90112, 8192, 1636423206);
      var2.setMemory(this.mem_prom);
      var2.loadROM("g-1j-.bin", 0, 256, 1720609738);
      var2.loadROM("b-1m-.bin", 256, 256, 1992317596);
      var2.loadROM("g-1f-.bin", 512, 256, -1773443947);
      var2.loadROM("b-1n-.bin", 768, 256, 602958745);
      var2.loadROM("g-1h-.bin", 1024, 256, 1426416609);
      var2.loadROM("b-1l-.bin", 1280, 256, 904155169);
      var2.loadROM("b-5f-.bin", 1536, 32, 2053119037);
      var2.loadROM("b-6f-.bin", 1568, 256, -2101211886);
      var2.loadZip(var1);
   }

   private void loadRomLdrun(URL var1) {
      RomLoader var2 = new RomLoader();
      var2.setZip("ldrun");
      var2.setMemory(this.mem_cpu1);
      var2.loadROM("lr-a-4e", 0, 8192, 1568549453);
      var2.loadROM("lr-a-4d", 8192, 8192, -1762524045);
      var2.loadROM("lr-a-4b", 16384, 8192, -1337867095);
      var2.loadROM("lr-a-4a", 24576, 8192, 1683899050);
      var2.setMemory(this.mem_cpu2);
      var2.loadROM("lr-a-3f", '쀀', 8192, 2056694989);
      var2.loadROM("lr-a-3h", '\ue000', 8192, 1065302329);
      var2.setMemory(this.mem_gfx1);
      var2.loadROM("lr-e-2d", 0, 8192, 620344717);
      var2.loadROM("lr-e-2j", 8192, 8192, 1125604872);
      var2.loadROM("lr-e-2f", 16384, 8192, -533630684);
      var2.setMemory(this.mem_gfx2);
      var2.loadROM("lr-b-4k", 0, 8192, -2126430146);
      var2.loadROM("lr-b-3n", 8192, 8192, 1427456340);
      var2.loadROM("lr-b-4c", 16384, 8192, -1840368432);
      var2.setMemory(this.mem_prom);
      var2.loadROM("lr-e-3m", 0, 256, 1392772118);
      var2.loadROM("lr-b-1m", 256, 256, 1269701669);
      var2.loadROM("lr-e-3l", 512, 256, 1735942199);
      var2.loadROM("lr-b-1n", 768, 256, -1663837292);
      var2.loadROM("lr-e-3n", 1024, 256, 1534158903);
      var2.loadROM("lr-b-1l", 1280, 256, 148426650);
      var2.loadROM("lr-b-5p", 1536, 32, -534812190);
      var2.loadROM("lr-b-6f", 1568, 256, 886607164);
      var2.loadZip(var1);
   }

   private void loadRomLdrun2(URL var1) {
      RomLoader var2 = new RomLoader();
      var2.setZip("ldrun2");
      var2.setMemory(this.mem_cpu1);
      var2.loadROM("lr2-a-4e.a", 0, 8192, 573649703);
      var2.loadROM("lr2-a-4d", 8192, 8192, -278638215);
      var2.loadROM("lr2-a-4a.a", 16384, 8192, -1323442343);
      var2.loadROM("lr2-a-4a", 24576, 8192, 1192020129);
      var2.loadROM("lr2-h-1c.a", 65536, 8192, 2126294460);
      var2.loadROM("lr2-h-1d.a", 73728, 8192, 1691072505);
      var2.setMemory(this.mem_cpu2);
      var2.loadROM("lr2-a-3e", 'ꀀ', 8192, -2059454312);
      var2.loadROM("lr2-a-3f", '쀀', 8192, 2056694989);
      var2.loadROM("lr2-a-3h", '\ue000', 8192, 705594314);
      var2.setMemory(this.mem_gfx1);
      var2.loadROM("lr2-h-1e", 0, 8192, -1654413057);
      var2.loadROM("lr2-h-1j", 8192, 8192, 1077095357);
      var2.loadROM("lr2-h-1h", 16384, 8192, -1811647875);
      var2.setMemory(this.mem_gfx2);
      var2.loadROM("lr2-b-4k", 0, 8192, 2039519345);
      var2.loadROM("lr2-b-4f", 8192, 8192, 112860916);
      var2.loadROM("lr2-b-3n", 16384, 8192, 1019578687);
      var2.loadROM("lr2-b-4n", 24576, 8192, 1237397314);
      var2.loadROM("lr2-b-4c", '耀', 8192, -68758964);
      var2.loadROM("lr2-b-4e", 'ꀀ', 8192, 1964453151);
      var2.setMemory(this.mem_prom);
      var2.loadROM("lr2-h-3m", 0, 256, 744325963);
      var2.loadROM("lr2-b-1m", 256, 256, 1321843517);
      var2.loadROM("lr2-h-3l", 512, 256, 988191434);
      var2.loadROM("lr2-b-1n", 768, 256, 498016164);
      var2.loadROM("lr2-h-3n", 1024, 256, 724086469);
      var2.loadROM("lr2-b-1l", 1280, 256, -923045750);
      var2.loadROM("lr2-b-5p", 1536, 32, -534812190);
      var2.loadROM("lr2-b-6f", 1568, 256, 886607164);
      var2.loadZip(var1);
   }

   private void loadRomLdrun3(URL var1) {
      RomLoader var2 = new RomLoader();
      var2.setZip("ldrun3");
      var2.setMemory(this.mem_cpu1);
      var2.loadROM("lr3a4eb.bin", 0, 16384, 162528327);
      var2.loadROM("lr3a4db.bin", 16384, 16384, 597696888);
      var2.loadROM("lr3a4bb.bin", '耀', 16384, 1028659738);
      var2.setMemory(this.mem_cpu2);
      var2.loadROM("lr3-a-3d", '耀', 16384, 683567309);
      var2.loadROM("lr3-a-3f", '쀀', 16384, -881752393);
      var2.setMemory(this.mem_gfx1);
      var2.loadROM("lr3-n-2a", 0, 16384, -105427474);
      var2.loadROM("lr3-n-2c", 16384, 16384, -17365062);
      var2.loadROM("lr3-n-2b", '耀', 16384, -1354946631);
      var2.setMemory(this.mem_gfx2);
      var2.loadROM("lr3b4kb.bin", 0, 16384, 569170117);
      var2.loadROM("snxb4fb.bin", 16384, 16384, -311321221);
      var2.loadROM("lr3b3nb.bin", '耀', 16384, -628293717);
      var2.loadROM("snxb4nb.bin", '쀀', 16384, -597209085);
      var2.loadROM("snxb4cb.bin", 65536, 16384, 1482334788);
      var2.loadROM("snxb4eb.bin", 81920, 16384, 758868445);
      var2.setMemory(this.mem_prom);
      var2.loadROM("lr3-n-2l", 0, 256, -394217365);
      var2.loadROM("lr3-b-1m", 256, 256, -265457305);
      var2.loadROM("lr3-n-2k", 512, 256, 75423825);
      var2.loadROM("lr3-b-1n", 768, 256, -1640500863);
      var2.loadROM("lr3-n-2m", 1024, 256, 1772979832);
      var2.loadROM("lr3-b-1l", 1280, 256, 1527890973);
      var2.loadROM("lr3-b-5p", 1536, 32, -534812190);
      var2.loadROM("lr3-n-4f", 1568, 256, -546878487);
      var2.loadROM("lr3-b-6f", 1824, 256, 886607164);
      var2.loadZip(var1);
   }

   private void loadRomLdrun4(URL var1) {
      RomLoader var2 = new RomLoader();
      var2.setZip("ldrun4");
      var2.setMemory(this.mem_cpu1);
      var2.loadROM("lr4-a-4e", 0, 16384, 1401153983);
      var2.loadROM("lr4-a-4d.c", 16384, 16384, 696973878);
      var2.loadROM("lr4-v-4k", 65536, '耀', -1960539459);
      var2.setMemory(this.mem_cpu2);
      var2.loadROM("lr4-a-3d", '耀', 16384, -2033789883);
      var2.loadROM("lr4-a-3f", '쀀', 16384, 159149066);
      var2.setMemory(this.mem_gfx1);
      var2.loadROM("lr4-v-2b", 0, 16384, 1092150794);
      var2.loadROM("lr4-v-2d", 16384, 16384, 1412150709);
      var2.loadROM("lr4-v-2c", '耀', 16384, -949672340);
      var2.setMemory(this.mem_gfx2);
      var2.loadROM("lr4-b-4k", 0, 16384, -402759156);
      var2.loadROM("lr4-b-4f", 16384, 16384, 1862534107);
      var2.loadROM("lr4-b-3n", '耀', 16384, -1390429669);
      var2.loadROM("lr4-b-4n", '쀀', 16384, 240553899);
      var2.loadROM("lr4-b-4c", 65536, 16384, -2101004695);
      var2.loadROM("lr4-b-4e", 81920, 16384, 1987711826);
      var2.setMemory(this.mem_prom);
      var2.loadROM("lr4-v-1m", 0, 256, -28197091);
      var2.loadROM("lr4-b-1m", 256, 256, 1569527760);
      var2.loadROM("lr4-v-1n", 512, 256, -637118235);
      var2.loadROM("lr4-b-1n", 768, 256, -636409390);
      var2.loadROM("lr4-v-1p", 1024, 256, 233979582);
      var2.loadROM("lr4-b-1l", 1280, 256, 227128978);
      var2.loadROM("lr4-b-5p", 1536, 32, -534812190);
      var2.loadROM("lr4-v-4h", 1568, 256, -546878487);
      var2.loadROM("lr4-b-6f", 1824, 256, 886607164);
      var2.loadZip(var1);
   }

   public Emulator createEmulator(URL var1, String var2) {
      super.createEmulator(var1, var2);
      this.initInput();
      EmulatorProperties var3 = null;
      if(var2.equals("ldrun")) {
         this.loadRomLdrun(var1);
         var3 = this.createMachineDriverLdrun();
      } else if(var2.equals("ldrun2")) {
         this.loadRomLdrun2(var1);
         var3 = this.createMachineDriverLdrun2();
      } else if(var2.equals("ldrun3")) {
         this.loadRomLdrun3(var1);
         var3 = this.createMachineDriverLdrun3();
      } else if(var2.equals("ldrun4")) {
         this.loadRomLdrun4(var1);
         var3 = this.createMachineDriverLdrun4();
      } else if(var2.equals("kungfum")) {
         this.loadRomKungFum(var1);
         var3 = this.createMachineDriverKungfum();
      }

      var3.setInputPorts(this.in);
      this.video.init(var3);
      this.emu.init(var3);
      return this.emu;
   }

   public EmulatorProperties createMachineDriverLdrun() {
      EmulatorProperties var1 = new EmulatorProperties();
      this.initReadMap();
      this.initWriteMap();
      this.initReadPortLdrun();
      this.initWritePortLdrun();
      var1.setCpuDriver(new CpuDriver[]{new CpuDriver(this.cpu1, 4000000, this.mra1, this.mwa1, this.ior1, this.iow1, this.interrupt1, 1)});
      var1.setFPS(60);
      var1.setVBlankDuration(1790);
      var1.setVideoDimensions(512, 256);
      var1.setVisibleArea(64, 447, 0, 255);
      var1.setGfxDecodeInfo(this.createGfxDecodeInfoLdrun());
      var1.setPaletteLength(512);
      var1.setVideoEmulation(this.video);
      return var1;
   }

   public EmulatorProperties createMachineDriverLdrun2() {
      EmulatorProperties var1 = new EmulatorProperties();
      this.initReadMapLdrun2();
      this.initWriteMapLdrun2();
      this.initReadPortLdrun2();
      this.initWritePortLdrun2();
      var1.setCpuDriver(new CpuDriver[]{new CpuDriver(this.cpu1, 4000000, this.mra1, this.mwa1, this.ior1, this.iow1, this.interrupt1, 1)});
      var1.setFPS(60);
      var1.setVBlankDuration(1790);
      var1.setVideoDimensions(512, 256);
      var1.setVisibleArea(64, 447, 0, 255);
      var1.setGfxDecodeInfo(this.createGfxDecodeInfoLdrun2());
      var1.setPaletteLength(512);
      var1.setPaletteInitializer(this.video);
      var1.setVideoUpdater(this.video);
      return var1;
   }

   public EmulatorProperties createMachineDriverLdrun3() {
      EmulatorProperties var1 = new EmulatorProperties();
      this.initReadMapLdrun3();
      this.initWriteMapLdrun3();
      this.initReadPortLdrun();
      this.initWritePortLdrun3();
      var1.setCpuDriver(new CpuDriver[]{new CpuDriver(this.cpu1, 4000000, this.mra1, this.mwa1, this.ior1, this.iow1, this.interrupt1, 1)});
      var1.setFPS(60);
      var1.setVBlankDuration(1790);
      var1.setVideoDimensions(512, 256);
      var1.setVisibleArea(64, 447, 0, 255);
      var1.setGfxDecodeInfo(this.createGfxDecodeInfoLdrun3());
      var1.setPaletteLength(512);
      var1.setPaletteInitializer(this.video);
      var1.setVideoUpdater(this.video);
      return var1;
   }

   public EmulatorProperties createMachineDriverLdrun4() {
      EmulatorProperties var1 = new EmulatorProperties();
      this.initReadMap();
      this.initWriteMap();
      this.initReadPortLdrun();
      this.initWritePortLdrun();
      var1.setCpuDriver(new CpuDriver[]{new CpuDriver(this.cpu1, 4000000, this.mra1, this.mwa1, this.ior1, this.iow1, this.interrupt1, 1)});
      var1.setFPS(60);
      var1.setVBlankDuration(1790);
      var1.setVideoDimensions(512, 256);
      var1.setVisibleArea(64, 447, 0, 255);
      var1.setGfxDecodeInfo(this.createGfxDecodeInfoLdrun4());
      var1.setPaletteLength(512);
      var1.setPaletteInitializer(this.video);
      var1.setVideoUpdater(this.video);
      return var1;
   }

   public class ReadLdrun2Bankswitch implements ReadHandler {
      public int read(int var1) {
         if(M62.this.bankSwap != 0) {
            M62 var2 = M62.this;
            --var2.bankSwap;
            if(M62.this.bankSwap == 0) {
               M62.this.mra1.setBankAddress(1, 73728);
            }
         }

         return 0;
      }
   }

   public class ReadProtection5Ldrun3 implements ReadHandler {
      public int read(int var1) {
         return 5;
      }
   }

   public class ReadProtection7Ldrun3 implements ReadHandler {
      public int read(int var1) {
         return 7;
      }
   }

   public class WriteLdrun2Bankswitch implements WriteHandler {
      int[] bankcontrol = new int[2];
      int[] banks;

      public WriteLdrun2Bankswitch() {
         int[] var2 = new int[30];
         var2[5] = 1;
         var2[7] = 1;
         var2[11] = 1;
         var2[12] = 1;
         var2[13] = 1;
         var2[14] = 1;
         var2[15] = 1;
         var2[20] = 1;
         var2[22] = 1;
         var2[23] = 1;
         var2[24] = 1;
         var2[25] = 1;
         var2[26] = 1;
         var2[27] = 1;
         var2[28] = 1;
         var2[29] = 1;
         this.banks = var2;
      }

      public void write(int var1, int var2) {
         var1 -= 128;
         this.bankcontrol[var1] = var2;
         if(var1 == 0) {
            if(var2 >= 1 && var2 <= 30) {
               var1 = this.banks[var2 - 1];
               M62.this.mra1.setBankAddress(1, 65536 + var1 * 8192);
            }
         } else if(this.bankcontrol[0] == 1 && var2 == 13) {
            M62.this.bankSwap = 2;
         } else {
            M62.this.bankSwap = 0;
         }

      }
   }
}
