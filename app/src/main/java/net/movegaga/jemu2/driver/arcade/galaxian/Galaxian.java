package net.movegaga.jemu2.driver.arcade.galaxian;

import net.movegaga.jemu2.driver.BaseDriver;

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
import jef.map.SwitchedInterrupt;
import jef.map.WriteHandler;
import jef.util.RomLoader;
import jef.video.GfxDecodeInfo;
import jef.video.GfxLayout;

public class Galaxian extends BaseDriver {
   InputPort[] in;
   InterruptHandler interruptGalaxian;
   BasicEmulator m;
   char[] mem_cpu = new char[65536];
   char[] mem_gfx;
   char[] mem_prom;
   MemoryReadAddress mra;
   MemoryWriteAddress mwa;
   ReadHandler readScramblbProtection1;
   ReadHandler readScramblbProtection2;
   ReadHandler readVRAM;
   InterruptHandler scrambleInterrupt;
   Scramble sm;
   VGalaxian v;
   WriteHandler writeAttributes;
   WriteHandler writeInterruptEnable;
   WriteHandler writePiscesGfxBank;
   WriteHandler writeStars;
   WriteHandler writeVRAM;
   Z80 z80 = new Z80();

   public Galaxian() {
      this.mra = new MemoryReadAddress(this.mem_cpu);
      this.mwa = new MemoryWriteAddress(this.mem_cpu);
      this.v = new VGalaxian();
      this.readVRAM = this.v;
      this.writeVRAM = this.v;
      this.interruptGalaxian = this.v;
      this.writeAttributes = this.v.writeAttributes();
      this.writeStars = this.v.writeStars();
      this.writeInterruptEnable = this.v.writeInterruptEnable();
      this.writePiscesGfxBank = this.v.writePiscesGfxBank();
      this.scrambleInterrupt = this.v.interruptScramble();
      this.mem_gfx = new char[8192];
      this.mem_prom = new char[32];
      this.m = new BasicEmulator();
      this.sm = new Scramble();
      this.readScramblbProtection1 = this.sm.readProtection1(this.z80);
      this.readScramblbProtection2 = this.sm.readProtection2(this.z80);
      this.in = new InputPort[3];
   }

   private GfxLayout characters() {
      int[] var2 = new int[]{16384, 0};
      int[] var3 = new int[]{0, 1, 2, 3, 4, 5, 6, 7};
      int[] var1 = new int[]{0, 8, 16, 24, 32, 40, 48, 56};
      return new GfxLayout(8, 8, 256, 2, var2, var3, var1, 64);
   }

   private GfxDecodeInfo[] gdi() {
      return new GfxDecodeInfo[]{new GfxDecodeInfo(this.mem_gfx, 0, this.characters(), 0, 8), new GfxDecodeInfo(this.mem_gfx, 0, this.sprites(), 0, 8)};
   }

   private GfxDecodeInfo[] gdiPacmanbl() {
      return new GfxDecodeInfo[]{new GfxDecodeInfo(this.mem_gfx, 0, this.characters(), 0, 8), new GfxDecodeInfo(this.mem_gfx, 4096, this.sprites(), 0, 8)};
   }

   private InputPort[] input() {
      this.in[0].setBit(1, 0, 1);
      this.in[0].setBit(2, 0, 2);
      this.in[0].setBit(4, 0, 8);
      this.in[0].setBit(8, 0, 9);
      this.in[0].setBit(16, 0, 12);
      this.in[0].setBit(128, 0, 2);
      this.in[0].setBits(96, 0);
      this.in[1].setBit(1, 0, 4);
      this.in[1].setBit(2, 0, 5);
      this.in[1].setBit(4, 0, 8);
      this.in[1].setBit(8, 0, 9);
      this.in[1].setBit(16, 0, 140);
      this.in[1].setBits(224, 0);
      this.in[2].setBits(255, 4);
      return this.in;
   }

   private InputPort[] inputScramblb() {
      this.in[0].setBit(1, 0, 1);
      this.in[0].setBit(2, 0, 139);
      this.in[0].setBit(4, 0, 8);
      this.in[0].setBit(8, 0, 9);
      this.in[0].setBit(16, 0, 12);
      this.in[0].setBit(32, 0, 13);
      this.in[0].setBit(64, 0, 11);
      this.in[0].setBit(128, 0, 10);
      this.in[1].setBit(1, 0, 4);
      this.in[1].setBit(2, 0, 5);
      this.in[1].setBit(4, 0, 136);
      this.in[1].setBit(8, 0, 137);
      this.in[1].setBit(16, 0, 140);
      this.in[1].setBit(32, 0, 141);
      this.in[1].setBit(128, 0, 138);
      this.in[1].setBits(64, 0);
      this.in[2].setBits(255, 0);
      return this.in;
   }

   private InputPort[] inputSuperG() {
      this.in[0].setBit(1, 0, 1);
      this.in[0].setBit(2, 0, 2);
      this.in[0].setBit(4, 0, 8);
      this.in[0].setBit(8, 0, 9);
      this.in[0].setBit(16, 0, 12);
      this.in[0].setBits(96, 0);
      this.in[1].setBit(1, 0, 4);
      this.in[1].setBit(2, 0, 5);
      this.in[1].setBit(4, 0, 8);
      this.in[1].setBit(8, 0, 9);
      this.in[1].setBit(16, 0, 140);
      this.in[1].setBits(224, 0);
      this.in[2].setBits(255, 1);
      return this.in;
   }

   private InputPort[] inputWarOfBug() {
      this.in[0].setBit(1, 0, 1);
      this.in[0].setBit(4, 0, 8);
      this.in[0].setBit(8, 0, 9);
      this.in[0].setBit(16, 0, 12);
      this.in[0].setBit(64, 0, 11);
      this.in[0].setBit(128, 0, 10);
      this.in[0].setBits(34, 0);
      this.in[1].setBit(1, 0, 4);
      this.in[1].setBit(2, 0, 5);
      this.in[1].setBit(8, 0, 2);
      this.in[1].setBit(16, 0, 2);
      this.in[1].setBits(228, 0);
      this.in[2].setBits(255, 10);
      return this.in;
   }

   private InputPort[] inputZigZag() {
      this.in[0].setBit(1, 0, 1);
      this.in[0].setBit(2, 0, 0);
      this.in[0].setBit(4, 0, 8);
      this.in[0].setBit(8, 0, 9);
      this.in[0].setBit(16, 0, 12);
      this.in[0].setBit(32, 0, 10);
      this.in[0].setBit(64, 0, 11);
      this.in[0].setBit(128, 0, 139);
      this.in[1].setBit(1, 0, 4);
      this.in[1].setBit(2, 0, 5);
      this.in[1].setBit(4, 0, 136);
      this.in[1].setBit(8, 0, 137);
      this.in[1].setBit(16, 0, 140);
      this.in[1].setBit(32, 0, 138);
      this.in[1].setBits(192, 0);
      this.in[2].setBits(255, 2);
      return this.in;
   }

   private void loadRom(URL var1) {
      RomLoader var2 = new RomLoader();
      var2.setZip("galaxian");
      var2.setMemory(this.mem_cpu);
      var2.loadROM("galmidw.u", 0, 2048, 1952329057);
      var2.loadROM("galmidw.v", 2048, 2048, -1667655104);
      var2.loadROM("galmidw.w", 4096, 2048, -1249294043);
      var2.loadROM("galmidw.y", 6144, 2048, 1799135499);
      var2.loadROM("7l", 8192, 2048, 462631431);
      var2.setMemory(this.mem_gfx);
      var2.loadROM("1h.bin", 0, 2048, 972768164);
      var2.loadROM("1k.bin", 2048, 2048, 2118080162);
      var2.setMemory(this.mem_prom);
      var2.loadROM("6l.bpr", 0, 32, -1012099993);
      var2.loadZip(var1);
   }

   private void loadRomScramblb(URL var1) {
      RomLoader var2 = new RomLoader();
      var2.setZip("scramblb");
      var2.setMemory(this.mem_cpu);
      var2.loadROM("scramble.1k", 0, 2048, -1644012470);
      var2.loadROM("scramble.2k", 2048, 2048, 812611646);
      var2.loadROM("scramble.3k", 4096, 2048, 83932929);
      var2.loadROM("scramble.4k", 6144, 2048, -583529950);
      var2.loadROM("scramble.5k", 8192, 2048, -552888760);
      var2.loadROM("scramble.1j", 10240, 2048, -1195345092);
      var2.loadROM("scramble.2j", 12288, 2048, -2001991776);
      var2.loadROM("scramble.3j", 14336, 2048, -964864054);
      var2.setMemory(this.mem_gfx);
      var2.loadROM("5f.k", 0, 2048, 1191740507);
      var2.loadROM("5h.k", 2048, 2048, 301803655);
      var2.setMemory(this.mem_prom);
      var2.loadROM("82s123.6e", 0, 32, 1312599723);
      var2.loadZip(var1);
   }

   private void loadRomWarOfBug(URL var1) {
      RomLoader var2 = new RomLoader();
      var2.setZip("warofbug");
      var2.setMemory(this.mem_cpu);
      var2.loadROM("warofbug.u", 0, 2048, -1193297949);
      var2.loadROM("warofbug.v", 2048, 2048, -41397024);
      var2.loadROM("warofbug.w", 4096, 2048, 1150659092);
      var2.loadROM("warofbug.y", 6144, 2048, -1052093409);
      var2.loadROM("warofbug.z", 8192, 2048, -1050149291);
      var2.setMemory(this.mem_gfx);
      var2.loadROM("warofbug.1k", 0, 2048, -2130642299);
      var2.loadROM("warofbug.1j", 2048, 2048, -786298135);
      var2.setMemory(this.mem_prom);
      var2.loadROM("warofbug.clr", 0, 32, -2037848501);
      var2.loadZip(var1);
   }

   private void loadRomZigZag(URL var1) {
      RomLoader var2 = new RomLoader();
      var2.setZip("zigzag");
      var2.setMemory(this.mem_cpu);
      var2.loadROM("zz_d1.bin", 0, 4096, -1933537919);
      var2.loadROM("zz_d2.bin", 4096, 4096, 846040389);
      var2.loadROM("zz_d4.bin", 8192, 4096, -1454450390);
      var2.loadROM("zz_d3.bin", 12288, 4096, -832669184);
      var2.setMemory(this.mem_gfx);
      var2.loadROM("zz_6_h1.bin", 0, 2048, 2014058026);
      var2.continueROM(4096, 2048);
      var2.loadROM("zz_5.bin", 2048, 2048, -204603707);
      var2.continueROM(6144, 2048);
      var2.setMemory(this.mem_prom);
      var2.loadROM("zzbp_e9.bin", 0, 32, -1438093872);
      var2.loadZip(var1);
   }

   private MemoryReadAddress mra() {
      this.mra.set(21504, 22527, this.readVRAM);
      this.mra.set(24576, 24576, this.in[0]);
      this.mra.set(26624, 26624, this.in[1]);
      this.mra.set(28672, 28672, this.in[2]);
      return this.mra;
   }

   private MemoryReadAddress mraScramblb() {
      this.mra.set(24576, 24576, this.in[0]);
      this.mra.set(26624, 26624, this.in[1]);
      this.mra.set(28672, 28672, this.in[2]);
      this.mra.set('脂', '脂', this.readScramblbProtection1);
      this.mra.set('舂', '舂', this.readScramblbProtection2);
      return this.mra;
   }

   private MemoryReadAddress mraZigZag() {
      this.mra.setMR(8192, 12287, 3);
      this.mra.setMR(12288, 16383, 4);
      this.mra.set(24576, 24576, this.in[0]);
      this.mra.set(26624, 26624, this.in[1]);
      this.mra.set(28672, 28672, this.in[2]);
      return this.mra;
   }

   private MemoryWriteAddress mwa() {
      this.mwa.setMW(0, 16383, 1);
      this.mwa.set(20480, 21503, this.writeVRAM);
      this.mwa.set(22528, 22591, this.writeAttributes);
      this.mwa.set(28673, 28673, this.writeInterruptEnable);
      this.mwa.set(28676, 28676, this.writeStars);
      return this.mwa;
   }

   private MemoryWriteAddress mwaScramblb() {
      this.mwa.setMW(0, 16383, 1);
      this.mwa.set(18432, 19455, this.writeVRAM);
      this.mwa.set(20480, 20543, this.writeAttributes);
      this.mwa.set(28673, 28673, this.writeInterruptEnable);
      this.mwa.set(28676, 28676, this.writeStars);
      return this.mwa;
   }

   private MemoryWriteAddress mwaWarOfBug() {
      this.mwa.set(24578, 24578, this.writePiscesGfxBank);
      return this.mwa;
   }

   private MemoryWriteAddress mwaZigZag() {
      this.mwa.setMW(0, 16383, 1);
      this.mwa.setMW(18432, 18432, 2);
      this.mwa.setMW(18944, 18944, 2);
      this.mwa.set(20480, 21503, this.writeVRAM);
      this.mwa.set(22528, 22591, this.writeAttributes);
      this.mwa.set(28673, 28673, this.writeInterruptEnable);
      this.mwa.set(28674, 28674, new ZigZagProtection());
      return this.mwa;
   }

   private GfxLayout sprites() {
      int[] var2 = new int[]{16384, 0};
      int[] var3 = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 64, 65, 66, 67, 68, 69, 70, 71};
      int[] var1 = new int[]{0, 8, 16, 24, 32, 40, 48, 56, 128, 136, 144, 152, 160, 168, 176, 184};
      return new GfxLayout(16, 16, 64, 2, var2, var3, var1, 256);
   }

   public EmulatorProperties createEmuPropsGalaxian() {
      EmulatorProperties var1 = new EmulatorProperties();
      var1.setCpuDriver(new CpuDriver(this.z80, 3072000, this.mra(), this.mwa(), this.interruptGalaxian, 1));
      var1.setFPS(60);
      var1.setVBlankDuration(2500);
      var1.setVideoDimensions(256, 256);
      var1.setVisibleArea(0, 255, 16, 239);
      var1.setGfxDecodeInfo(this.gdi());
      var1.setPaletteLength(97);
      var1.setColorTableLength(164);
      var1.setVideoEmulation(this.v);
      var1.setInputPorts(this.in);
      return var1;
   }

   public EmulatorProperties createEmuPropsScramblb() {
      EmulatorProperties var1 = new EmulatorProperties();
      var1.setCpuDriver(new CpuDriver(this.z80, 3072000, this.mraScramblb(), this.mwaScramblb(), this.scrambleInterrupt, 1));
      var1.setFPS(60);
      var1.setVBlankDuration(2500);
      var1.setVideoDimensions(256, 256);
      var1.setVisibleArea(0, 255, 16, 239);
      var1.setGfxDecodeInfo(this.gdi());
      var1.setPaletteLength(97);
      var1.setColorTableLength(164);
      var1.setVideoEmulation(this.v);
      var1.setInputPorts(this.in);
      return var1;
   }

   public EmulatorProperties createEmuPropsZigZag() {
      EmulatorProperties var1 = new EmulatorProperties();
      var1.setCpuDriver(new CpuDriver(this.z80, 3072000, this.mraZigZag(), this.mwaZigZag(), new SwitchedInterrupt(1), 1));
      var1.setFPS(60);
      var1.setVBlankDuration(2500);
      var1.setVideoDimensions(256, 256);
      var1.setVisibleArea(0, 255, 16, 239);
      var1.setGfxDecodeInfo(this.gdiPacmanbl());
      var1.setPaletteLength(97);
      var1.setColorTableLength(164);
      var1.setVideoEmulation(this.v);
      var1.setInputPorts(this.in);
      return var1;
   }

   public Emulator createEmulator(URL var1, String var2) {
      super.createEmulator(var1, var2);
      this.in[0] = new InputPort();
      this.in[1] = new InputPort();
      this.in[2] = new InputPort();
      EmulatorProperties var3 = null;
      if(var2.equals("galaxian")) {
         this.loadRom(var1);
         this.input();
         var3 = this.createEmuPropsGalaxian();
         this.v.galaxian();
      } else if(var2.equals("scramblb")) {
         this.loadRomScramblb(var1);
         this.inputScramblb();
         var3 = this.createEmuPropsScramblb();
         this.v.scramble();
      } else if(var2.equals("warofbug")) {
         this.loadRomWarOfBug(var1);
         this.inputWarOfBug();
         var3 = this.createEmuPropsGalaxian();
         this.v.galaxian();
      } else if(var2.equals("zigzag")) {
         this.loadRomZigZag(var1);
         this.inputZigZag();
         var3 = this.createEmuPropsZigZag();
         this.v.zigzag();
      }

      var3.setRotation(1);
      this.v.setRegions(this.mem_prom, this.mem_cpu);
      this.v.init(var3);
      this.m.init(var3);
      return this.m;
   }

   class ZigZagProtection implements WriteHandler {
      public void write(int var1, int var2) {
         if(var2 != 0) {
            Galaxian.this.mra.setBankAddress(1, 12288);
            Galaxian.this.mra.setBankAddress(2, 8192);
         } else {
            Galaxian.this.mra.setBankAddress(1, 8192);
            Galaxian.this.mra.setBankAddress(2, 12288);
         }

      }
   }
}
