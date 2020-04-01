package net.movegaga.jemu2.driver.arcade.gberet;

import net.movegaga.jemu2.driver.BaseDriver;

import java.net.URL;

import jef.cpu.Z80;
import jef.cpuboard.CpuDriver;
import jef.machine.Emulator;
import jef.machine.EmulatorProperties;
import jef.map.InputPort;
import jef.map.InterruptHandler;
import jef.map.MemoryReadAddress;
import jef.map.MemoryWriteAddress;
import jef.map.WriteHandler;
import jef.scoring.HighScoreParser;
import jef.sound.SoundChip;
import jef.sound.chip.SN76496;
import jef.util.RomLoader;
import jef.video.GfxDecodeInfo;

public class Gberet extends BaseDriver {
   int[][] charlayout;
   Z80 cpu = new Z80();
   MGberet emu;
   EmulatorProperties emuProps;
   InputPort[] in;
   InterruptHandler interrupt;
   char[] mem_cpu = new char[81920];
   char[] mem_gfx1;
   char[] mem_gfx2;
   char[] mem_prom;
   MemoryReadAddress mra;
   MemoryWriteAddress mwa;
   SN76496 sn;
   int[][] spritelayout;
   VGberet video;
   WriteHandler writeInterruptEnable;

   public Gberet() {
      this.mra = new MemoryReadAddress(this.mem_cpu);
      this.mwa = new MemoryWriteAddress(this.mem_cpu);
      this.emu = new MGberet();
      this.interrupt = this.emu;
      this.writeInterruptEnable = this.emu;
      this.video = new VGberet(this);
      this.mem_gfx1 = new char[16384];
      this.mem_gfx2 = new char[65536];
      this.mem_prom = new char[544];
      this.sn = new SN76496(1536000);
      this.in = new InputPort[6];
      this.emuProps = new EmulatorProperties();
      int[] var4 = new int[]{8};
      int[] var5 = new int[]{8};
      int[] var6 = new int[]{512};
      int[] var7 = new int[]{4};
      int[] var3 = new int[]{0, 1, 2, 3};
      int[] var2 = new int[]{0, 4, 8, 12, 16, 20, 24, 28};
      int[] var1 = new int[]{0, 32, 64, 96, 128, 160, 192, 224};
      this.charlayout = new int[][]{var4, var5, var6, var7, var3, var2, var1, {256}};
      var4 = new int[]{16};
      var5 = new int[]{16};
      var2 = new int[]{0, 1, 2, 3};
      var3 = new int[]{0, 4, 8, 12, 16, 20, 24, 28, 256, 260, 264, 268, 272, 276, 280, 284};
      var1 = new int[]{0, 32, 64, 96, 128, 160, 192, 224, 512, 544, 576, 608, 640, 672, 704, 736};
      var6 = new int[]{1024};
      this.spritelayout = new int[][]{var4, var5, {512}, {4}, var2, var3, var1, var6};
   }

   private GfxDecodeInfo[] gfxdecodeinfo() {
      return new GfxDecodeInfo[]{new GfxDecodeInfo(this.mem_gfx1, 0, this.charlayout, 0, 16), new GfxDecodeInfo(this.mem_gfx2, 0, this.spritelayout, 256, 16)};
   }

   private boolean initInput() {
      this.in[0] = new InputPort();
      this.in[0].setBit(1, 8);
      this.in[0].setBit(2, 9);
      this.in[0].setBit(4, 10);
      this.in[0].setBit(8, 11);
      this.in[0].setBit(16, 12);
      this.in[0].setBit(32, 13);
      this.in[0].setBits(192, 192);
      this.in[1] = new InputPort();
      this.in[1].setBit(1, 136);
      this.in[1].setBit(2, 137);
      this.in[1].setBit(4, 138);
      this.in[1].setBit(8, 139);
      this.in[1].setBit(16, 140);
      this.in[1].setBit(32, 141);
      this.in[1].setBits(192, 192);
      this.in[2] = new InputPort();
      this.in[2].setBit(1, 1);
      this.in[2].setBit(2, 2);
      this.in[2].setBit(4, 2);
      this.in[2].setBit(8, 4);
      this.in[2].setBit(16, 5);
      this.in[2].setBits(224, 224);
      this.in[3] = new InputPort();
      this.in[3].setBits(255, 255);
      this.in[4] = new InputPort();
      this.in[4].setBits(3, 2);
      this.in[4].setBits(4, 0);
      this.in[4].setBits(24, 24);
      this.in[4].setBits(96, 96);
      this.in[4].setBits(128, 0);
      this.in[5] = new InputPort();
      this.in[5].setBits(255, 255);
      return true;
   }

   private boolean initInputMrGoemon() {
      this.in[0] = new InputPort();
      this.in[0].setBit(1, 8);
      this.in[0].setBit(2, 9);
      this.in[0].setBit(4, 10);
      this.in[0].setBit(8, 11);
      this.in[0].setBit(16, 12);
      this.in[0].setBit(32, 13);
      this.in[0].setBits(192, 192);
      this.in[1] = new InputPort();
      this.in[1].setBit(1, 136);
      this.in[1].setBit(2, 137);
      this.in[1].setBit(4, 138);
      this.in[1].setBit(8, 139);
      this.in[1].setBit(16, 140);
      this.in[1].setBit(32, 141);
      this.in[1].setBits(192, 192);
      this.in[2] = new InputPort();
      this.in[2].setBit(1, 1);
      this.in[2].setBit(2, 2);
      this.in[2].setBit(4, 2);
      this.in[2].setBit(8, 4);
      this.in[2].setBit(16, 5);
      this.in[2].setBits(224, 224);
      this.in[3] = new InputPort();
      this.in[3].setBits(255, 255);
      this.in[4] = new InputPort();
      this.in[4].setBits(3, 2);
      this.in[4].setBits(4, 0);
      this.in[4].setBits(24, 24);
      this.in[4].setBits(96, 96);
      this.in[4].setBits(128, 0);
      this.in[5] = new InputPort();
      this.in[5].setBits(1, 1);
      this.in[5].setBits(2, 2);
      this.in[5].setService(4);
      this.in[5].setBits(8, 8);
      this.in[5].setBits(240, 240);
      return true;
   }

   private void initMemMap() {
      this.mra.set('\uf200', '\uf200', this.in[4]);
      this.mra.set('\uf400', '\uf400', this.in[5]);
      this.mra.set('\uf600', '\uf600', this.in[3]);
      this.mra.set('\uf601', '\uf601', this.in[1]);
      this.mra.set('\uf602', '\uf602', this.in[0]);
      this.mra.set('\uf603', '\uf603', this.in[2]);
      this.mra.setMR('\uf800', '\uf800', 2);
      this.mwa.setMW(0, '뿿', 1);
      this.mwa.set('쀀', '쿿', this.video);
      this.mwa.set('\ue044', '\ue044', this.writeInterruptEnable);
      this.mwa.setMW('\uf200', '\uf200', 2);
      this.mwa.set('\uf400', '\uf400', this.sn.getWriteCommandHandler());
   }

   private void initMemMapMrGoemon() {
      this.mra.set('\uf200', '\uf200', this.in[4]);
      this.mra.set('\uf400', '\uf400', this.in[5]);
      this.mra.set('\uf600', '\uf600', this.in[3]);
      this.mra.set('\uf601', '\uf601', this.in[1]);
      this.mra.set('\uf602', '\uf602', this.in[0]);
      this.mra.set('\uf603', '\uf603', this.in[2]);
      this.mra.setMR('\uf800', '\uffff', 3);
      this.mwa.setMW(0, '뿿', 1);
      this.mwa.set('쀀', '쿿', this.video);
      this.mwa.set('\ue044', '\ue044', this.writeInterruptEnable);
      this.mwa.set('\uf000', '\uf000', new WriteBankSwitch());
      this.mwa.setMW('\uf200', '\uf200', 2);
      this.mwa.set('\uf400', '\uf400', this.sn.getWriteCommandHandler());
   }

   private void loadRom(URL var1) {
      RomLoader var2 = new RomLoader();
      var2.setZip("gberet");
      var2.setMemory(this.mem_cpu);
      var2.loadROM("c10_l03.bin", 0, 16384, -1372986113);
      var2.loadROM("c08_l02.bin", 16384, 16384, 604518053);
      var2.loadROM("c07_l01.bin", '耀', 16384, 1106918943);
      var2.setMemory(this.mem_gfx1);
      var2.loadROM("f03_l07.bin", 0, 16384, 1302838555);
      var2.setMemory(this.mem_gfx2);
      var2.loadROM("e05_l06.bin", 0, 16384, 253538506);
      var2.loadROM("e04_l05.bin", 16384, 16384, 1379568486);
      var2.loadROM("f04_l08.bin", '耀', 16384, -2009517148);
      var2.loadROM("e03_l04.bin", '쀀', 16384, -856892852);
      var2.setMemory(this.mem_prom);
      var2.loadROM("577h09", 0, 32, -1050772352);
      var2.loadROM("577h10", 32, 256, -371319213);
      var2.loadROM("577h11", 288, 256, 706386219);
      var2.loadZip(var1);
   }

   private void loadRomMrGoemon(URL var1) {
      RomLoader var2 = new RomLoader();
      var2.setZip("mrgoemon");
      var2.setMemory(this.mem_cpu);
      var2.loadROM("621d01.10c", 0, '耀', -1306420138);
      var2.loadROM("621d02.12c", '耀', 16384, -1020036457);
      var2.continueROM(65536, 16384);
      var2.setMemory(this.mem_gfx1);
      var2.loadROM("621a05.6d", 0, 16384, -257499195);
      var2.setMemory(this.mem_gfx2);
      var2.loadROM("621d03.4d", 0, '耀', 1727183219);
      var2.loadROM("621d04.5d", '耀', '耀', 1205822209);
      var2.setMemory(this.mem_prom);
      var2.loadROM("621a06.5f", 0, 32, 2089868895);
      var2.loadROM("621a07.6f", 32, 256, 964734172);
      var2.loadROM("621a08.7f", 288, 256, 800212189);
      var2.loadZip(var1);
   }

   public Emulator createEmulator(URL var1, String var2) {
      super.createEmulator(var1, var2);
      if(var2.equals("gberet")) {
         this.loadRom(var1);
         this.initInput();
         this.initEmu();
         new HighScoreParser(this.emu, this.mem_cpu, 16, 0, 0, '좄', 1, '좉', 0, 1);
      } else if(var2.equals("mrgoemon")) {
         this.loadRomMrGoemon(var1);
         this.initInputMrGoemon();
         this.initEmuMrGoemon();
      }

      this.video.init(this.emuProps);
      this.emu.init(this.emuProps);
      return this.emu;
   }

   public void initEmu() {
      this.initMemMap();
      CpuDriver var2 = new CpuDriver(this.cpu, 3072000, this.mra, this.mwa, this.interrupt, 32);
      SN76496 var1 = this.sn;
      this.emuProps.setCpuDriver(new CpuDriver[]{var2});
      this.emuProps.setFPS(30);
      this.emuProps.setSoundChips(new SoundChip[]{var1});
      this.emuProps.setVideoDimensions(256, 256);
      this.emuProps.setVisibleArea(8, 247, 16, 239);
      this.emuProps.setGfxDecodeInfo(this.gfxdecodeinfo());
      this.emuProps.setPaletteLength(32);
      this.emuProps.setColorTableLength(512);
      this.emuProps.setVideoEmulation(this.video);
      this.emuProps.setInputPorts(this.in);
   }

   public void initEmuMrGoemon() {
      this.initMemMapMrGoemon();
      CpuDriver var2 = new CpuDriver(this.cpu, 3072000, this.mra, this.mwa, this.interrupt, 16);
      SN76496 var1 = this.sn;
      this.emuProps.setCpuDriver(new CpuDriver[]{var2});
      this.emuProps.setFPS(60);
      this.emuProps.setSoundChips(new SoundChip[]{var1});
      this.emuProps.setVideoDimensions(256, 256);
      this.emuProps.setVisibleArea(8, 247, 16, 239);
      this.emuProps.setGfxDecodeInfo(this.gfxdecodeinfo());
      this.emuProps.setPaletteLength(32);
      this.emuProps.setColorTableLength(512);
      this.emuProps.setVideoEmulation(this.video);
      this.emuProps.setInputPorts(this.in);
   }

   class WriteBankSwitch implements WriteHandler {
      public void write(int var1, int var2) {
         Gberet.this.mra.setBankAddress(1, 65536 + ((var2 & 224) >> 5) * 2048);
      }
   }
}
