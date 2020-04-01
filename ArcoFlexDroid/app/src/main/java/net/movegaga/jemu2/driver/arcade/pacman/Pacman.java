package net.movegaga.jemu2.driver.arcade.pacman;

import net.movegaga.jemu2.driver.BaseDriver;
import net.movegaga.jemu2.driver.Driver;

import java.net.URL;

import jef.cpu.Z80;
import jef.cpuboard.CpuDriver;
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
import jef.sound.chip.Namco;
import jef.sound.chip.SN76496;
import jef.util.RomLoader;
import jef.video.GfxDecodeInfo;
import jef.video.GfxLayout;
import jef.video.VideoConstants;

public class Pacman extends BaseDriver implements Driver, VideoConstants {
   AY8910 ay8910;
   Z80 cpu = new Z80();
   MPacman emu;
   InputPort[] in;
   IOReadPort ior;
   IOWritePort iow;
   char[] mem_cpu = new char[131072];
   char[] mem_gfx1;
   char[] mem_gfx2;
   char[] mem_prom;
   MemoryReadAddress mra;
   MemoryWriteAddress mwa;
   Namco namco;
   ReadHandler readTheGlobROMDecrypt;
   SN76496 sn1;
   SN76496 sn2;
   SwitchedInterrupt switchedIRQ;
   SwitchedInterrupt switchedNMI;
   TheGlobDecoder theGlobDecoder;
   VPacman video;
   WriteHandler writeAY8910ControlPort0;
   WriteHandler writeAY8910WritePort0;
   WriteHandler writeNamcoSoundRegs;
   WriteHandler writeVRAM;
   WriteHandler writeZ80IRQVector;

   public Pacman() {
      this.mra = new MemoryReadAddress(this.mem_cpu);
      this.mwa = new MemoryWriteAddress(this.mem_cpu);
      this.ior = new IOReadPort();
      this.iow = new IOWritePort();
      this.video = new VPacman(this);
      this.mem_gfx1 = new char[8192];
      this.mem_gfx2 = new char[8192];
      this.mem_prom = new char[288];
      this.writeVRAM = this.video.videoram_w();
      this.namco = new Namco(this.mem_cpu, 3, 96000);
      this.ay8910 = new AY8910(1, 1789750);
      this.sn1 = new SN76496(1789750);
      this.sn2 = new SN76496(1789750);
      this.writeNamcoSoundRegs = this.namco.getPengoInterface(20544);
      this.writeAY8910WritePort0 = this.ay8910.AY8910_write_port_0_w();
      this.writeAY8910ControlPort0 = this.ay8910.AY8910_control_port_0_w();
      this.emu = new MPacman();
      this.in = new InputPort[4];
      this.switchedIRQ = new SwitchedInterrupt(0);
      this.switchedNMI = new SwitchedInterrupt(1);
      this.writeZ80IRQVector = this.cpu;
      this.theGlobDecoder = new TheGlobDecoder(this.mem_cpu);
      this.readTheGlobROMDecrypt = this.theGlobDecoder.getDecryptionSetter();
   }

   private GfxLayout createCharLayout() {
      int[] var1 = new int[]{0, 4};
      int[] var3 = new int[]{64, 65, 66, 67, 0, 1, 2, 3};
      int[] var2 = new int[]{0, 8, 16, 24, 32, 40, 48, 56};
      return new GfxLayout(8, 8, 256, 2, var1, var3, var2, 128);
   }

   private GfxDecodeInfo[] createGfxDecodeInfo() {
      return new GfxDecodeInfo[]{new GfxDecodeInfo(this.mem_gfx1, 0, this.createCharLayout(), 0, 32), new GfxDecodeInfo(this.mem_gfx2, 0, this.createSpriteLayout(), 0, 32)};
   }

   private GfxLayout createSpriteLayout() {
      int[] var2 = new int[]{0, 4};
      int[] var1 = new int[]{64, 65, 66, 67, 128, 129, 130, 131, 192, 193, 194, 195, 0, 1, 2, 3};
      int[] var3 = new int[]{0, 8, 16, 24, 32, 40, 48, 56, 256, 264, 272, 280, 288, 296, 304, 312};
      return new GfxLayout(16, 16, 64, 2, var2, var1, var3, 512);
   }

   private void decodeEyes(int var1, char[] var2) {
      char[] var5 = new char[8];

      int var3;
      for(var3 = 0; var3 < 8; ++var3) {
         var5[var3] = var2[(var3 >> 2) + var1 + (var3 & 2) + ((var3 & 1) << 2)];
      }

      for(var3 = 0; var3 < 8; ++var3) {
         char var4 = var5[var3];
         var2[var1 + var3] = (char)(var4 & 128 | (var4 & 16) << 2 | var4 & 32 | (var4 & 64) >> 2 | var4 & 15);
      }

   }

   private void initEyes() {
      char[] var2 = this.mem_cpu;

      int var1;
      for(var1 = 0; var1 < 16384; ++var1) {
         var2[var1] = (char)(var2[var1] & 192 | (var2[var1] & 8) << 2 | var2[var1] & 16 | (var2[var1] & 32) >> 2 | var2[var1] & 7);
      }

      var2 = this.mem_gfx1;

      for(var1 = 0; var1 < this.mem_gfx1.length; var1 += 8) {
         this.decodeEyes(var1, var2);
      }

      var2 = this.mem_gfx2;

      for(var1 = 0; var1 < this.mem_gfx2.length; var1 += 8) {
         this.decodeEyes(var1, var2);
      }

   }

   private void initInputDremshpr() {
      this.in[0].setBit(1, 10);
      this.in[0].setBit(2, 8);
      this.in[0].setBit(4, 9);
      this.in[0].setBit(8, 11);
      this.in[0].setBit(16, 12);
      this.in[0].setBit(32, 1);
      this.in[0].setBit(64, 0);
      this.in[0].setBit(128, 2);
      this.in[1].setBit(1, 10);
      this.in[1].setBit(2, 8);
      this.in[1].setBit(4, 9);
      this.in[1].setBit(8, 11);
      this.in[1].setBit(16, 12);
      this.in[1].setBit(32, 4);
      this.in[1].setBit(64, 5);
      this.in[1].setBit(128, 0);
      this.in[2].setDipName(1, 1, "Cabinet");
      this.in[2].setDipName(2, 2, "Flip screen");
      this.in[2].setDipName(12, 12, "Bonus life");
      this.in[2].setDipName(48, 48, "Lives");
      this.in[2].setDipName(192, 192, "Coinage");
      this.in[3].setBit(254, 0);
   }

   private void initInputEyes() {
      this.in[0].setBit(1, 10);
      this.in[0].setBit(2, 8);
      this.in[0].setBit(4, 9);
      this.in[0].setBit(8, 11);
      this.in[0].setService(16);
      this.in[0].setBit(32, 1);
      this.in[0].setBit(64, 15);
      this.in[0].setBit(128, 2);
      this.in[1].setBit(1, 10);
      this.in[1].setBit(2, 8);
      this.in[1].setBit(4, 9);
      this.in[1].setBit(8, 11);
      this.in[1].setBit(16, 12);
      this.in[1].setBit(32, 4);
      this.in[1].setBit(64, 5);
      this.in[2].setDipName(3, 3, "Coinage");
      this.in[2].setDipName(12, 8, "Lives");
      this.in[2].setDipName(48, 48, "Bonus life");
      this.in[2].setDipName(64, 64, "Cabinet");
      this.in[2].setDipName(128, 128, "Unknown");
      this.in[3].setBit(255, 0, 0);
   }

   private void initInputLizWiz() {
      this.in[0].setBit(1, 10);
      this.in[0].setBit(2, 8);
      this.in[0].setBit(4, 9);
      this.in[0].setBit(8, 11);
      this.in[0].setService(16);
      this.in[0].setBit(32, 1);
      this.in[0].setBit(64, 15);
      this.in[0].setBit(128, 2);
      this.in[1].setBit(1, 138);
      this.in[1].setBit(2, 136);
      this.in[1].setBit(4, 137);
      this.in[1].setBit(8, 139);
      this.in[1].setBit(16, 12);
      this.in[1].setBit(32, 4);
      this.in[1].setBit(64, 5);
      this.in[1].setBit(128, 140);
      this.in[2].setDipName(3, 3, "Coinage");
      this.in[2].setDipName(12, 8, "Lives");
      this.in[2].setDipName(48, 48, "Bonus life");
      this.in[2].setDipName(64, 64, "Difficulty");
      this.in[2].setDipName(128, 128, "Unknown");
      this.in[3].setBit(255, 0, 0);
   }

   private void initInputMrTnt() {
      this.in[0].setBit(1, 10);
      this.in[0].setBit(2, 8);
      this.in[0].setBit(4, 9);
      this.in[0].setBit(8, 11);
      this.in[0].setService(16);
      this.in[0].setBit(32, 1);
      this.in[0].setBit(64, 15);
      this.in[0].setBit(128, 2);
      this.in[1].setBit(1, 10);
      this.in[1].setBit(2, 8);
      this.in[1].setBit(4, 9);
      this.in[1].setBit(8, 11);
      this.in[1].setBit(16, 12);
      this.in[1].setBit(32, 4);
      this.in[1].setBit(64, 5);
      this.in[1].setBit(128, 12);
      this.in[2].setDipName(3, 3, "Coinage");
      this.in[2].setDipName(12, 8, "Lives");
      this.in[2].setDipName(48, 48, "Bonus life");
      this.in[2].setDipName(64, 64, "Cabinet");
      this.in[2].setDipName(128, 128, "Unknown");
      this.in[3].setBit(255, 0, 0);
   }

   private void initInputMsPacman() {
      this.in[0].setBit(1, 10);
      this.in[0].setBit(2, 8);
      this.in[0].setBit(4, 9);
      this.in[0].setBit(8, 11);
      this.in[0].setDipName(16, 16, "Rack Test");
      this.in[0].setBit(32, 1);
      this.in[0].setBit(64, 2);
      this.in[0].setBit(128, 2);
      this.in[1].setBit(1, 10);
      this.in[1].setBit(2, 8);
      this.in[1].setBit(4, 9);
      this.in[1].setBit(8, 11);
      this.in[1].setService(16);
      this.in[1].setBit(32, 4);
      this.in[1].setBit(64, 5);
      this.in[1].setDipName(128, 128, "Cabinet");
      this.in[2].setDipName(3, 1, "Coinage");
      this.in[2].setDipName(12, 8, "Lives");
      this.in[2].setDipName(48, 0, "Bonus life");
      this.in[2].setDipName(64, 64, "Difficulty");
      this.in[2].setBit(128, 0);
      this.in[3].setBit(255, 0, 0);
   }

   private void initInputPacman() {
      this.in[0].setBit(1, 10);
      this.in[0].setBit(2, 8);
      this.in[0].setBit(4, 9);
      this.in[0].setBit(8, 11);
      this.in[0].setDipName(16, 16, "Rack Test");
      this.in[0].setBit(32, 1);
      this.in[0].setBit(64, 2);
      this.in[0].setBit(128, 2);
      this.in[1].setBit(1, 10);
      this.in[1].setBit(2, 8);
      this.in[1].setBit(4, 9);
      this.in[1].setBit(8, 11);
      this.in[1].setService(16);
      this.in[1].setBit(32, 4);
      this.in[1].setBit(64, 5);
      this.in[1].setDipName(128, 128, "Cabinet");
      this.in[2].setDipName(3, 1, "Coinage");
      this.in[2].setDipName(12, 8, "Lives");
      this.in[2].setDipName(48, 0, "Bonus life");
      this.in[2].setDipName(64, 64, "Difficulty");
      this.in[2].setDipName(128, 128, "Ghost Names");
      this.in[3].setBit(255, 0, 0);
   }

   private void initInputPonpoko() {
      this.in[0].setBit(1, 0, 10);
      this.in[0].setBit(2, 0, 8);
      this.in[0].setBit(4, 0, 9);
      this.in[0].setBit(8, 0, 11);
      this.in[0].setBit(16, 0, 12);
      this.in[0].setBit(32, 1);
      this.in[0].setBit(64, 2);
      this.in[0].setBit(128, 2);
      this.in[1].setBit(1, 0, 138);
      this.in[1].setBit(2, 0, 136);
      this.in[1].setBit(4, 0, 137);
      this.in[1].setBit(8, 0, 139);
      this.in[1].setBit(16, 0, 140);
      this.in[1].setBit(32, 0, 4);
      this.in[1].setBit(64, 0, 5);
      this.in[1].setBit(128, 0, 0);
      this.in[2].setDipName(3, 1, "Bonus life");
      this.in[2].setDipName(12, 0, "Unknown");
      this.in[2].setDipName(48, 32, "Lives");
      this.in[2].setDipName(64, 64, "Cabinet");
      this.in[2].setDipName(128, 128, "Unknown");
      this.in[3].setDipName(15, 1, "Coinage");
      this.in[3].setDipName(16, 16, "Unknown");
      this.in[3].setDipName(32, 32, "Unknown");
      this.in[3].setDipName(64, 0, "Demo sounds");
      this.in[3].setDipName(128, 128, "Unknown");
   }

   private void initInputTheGlob() {
      this.in[0].setBit(1, 10);
      this.in[0].setBit(2, 8);
      this.in[0].setBit(4, 9);
      this.in[0].setBit(8, 11);
      this.in[0].setBit(16, 0);
      this.in[0].setBit(32, 1);
      this.in[0].setBit(64, 0);
      this.in[0].setBit(128, 13);
      this.in[1].setBit(1, 10);
      this.in[1].setBit(2, 8);
      this.in[1].setBit(4, 9);
      this.in[1].setBit(8, 11);
      this.in[1].setBit(16, 12);
      this.in[1].setBit(32, 4);
      this.in[1].setBit(32, 12);
      this.in[1].setBit(64, 5);
      this.in[1].setBit(64, 13);
      this.in[1].setDipName(128, 128, "Cabinet");
      this.in[2].setDipName(3, 3, "Lives");
      this.in[2].setDipName(28, 28, "Difficulty");
      this.in[2].setDipName(32, 0, "Demo sounds");
      this.in[2].setDipName(64, 64, "Unknown");
      this.in[2].setDipName(128, 128, "Unknown");
      this.in[3].setBit(255, 0, 0);
   }

   private void initInputVanVan() {
      this.in[0].setBit(1, 10);
      this.in[0].setBit(2, 8);
      this.in[0].setBit(4, 9);
      this.in[0].setBit(8, 11);
      this.in[0].setBit(16, 12);
      this.in[0].setBit(32, 1);
      this.in[0].setBit(64, 0);
      this.in[0].setBit(128, 2);
      this.in[1].setBit(1, 10);
      this.in[1].setBit(2, 8);
      this.in[1].setBit(4, 9);
      this.in[1].setBit(8, 11);
      this.in[1].setBit(16, 12);
      this.in[1].setBit(32, 4);
      this.in[1].setBit(64, 5);
      this.in[1].setBit(128, 0);
      this.in[2].setDipName(1, 0, "Cabinet");
      this.in[2].setDipName(2, 2, "Flip screen");
      this.in[2].setDipName(4, 4, "Unknown");
      this.in[2].setDipName(8, 8, "Unknown");
      this.in[2].setDipName(48, 48, "Lives");
      this.in[2].setDipName(64, 64, "Coin A");
      this.in[2].setDipName(128, 128, "Coin B");
      this.in[3].setDipName(1, 0, "Unknown");
      this.in[3].setDipName(4, 0, "Unknown");
      this.in[3].setDipName(8, 0, "Unknown");
      this.in[3].setDipName(16, 0, "Unknown");
      this.in[3].setDipName(32, 0, "Unknown");
      this.in[3].setDipName(64, 0, "Unknown");
      this.in[3].setDipName(128, 0, "Unknown");
   }

   private void initPonpoko() {
      char[] var4 = this.mem_gfx1;

      int var1;
      int var2;
      char var3;
      for(var1 = 0; var1 < this.mem_gfx1.length; var1 += 16) {
         for(var2 = 0; var2 < 8; ++var2) {
            var3 = var4[var1 + var2 + 8];
            var4[var1 + var2 + 8] = var4[var1 + var2 + 0];
            var4[var1 + var2 + 0] = (char)var3;
         }
      }

      var4 = this.mem_gfx2;

      for(var1 = 0; var1 < this.mem_gfx2.length; var1 += 32) {
         for(var2 = 0; var2 < 8; ++var2) {
            var3 = var4[var1 + var2 + 24];
            var4[var1 + var2 + 24] = var4[var1 + var2 + 16];
            var4[var1 + var2 + 16] = var4[var1 + var2 + 8];
            var4[var1 + var2 + 8] = var4[var1 + var2 + 0];
            var4[var1 + var2 + 0] = (char)var3;
         }
      }

   }

   private void initReadMap() {
      this.mra.setMR(0, 16383, 1);
      this.mra.setMR(16384, 18431, 0);
      this.mra.setMR(18432, 20479, 0);
      this.mra.set(20480, 20543, this.in[0]);
      this.mra.set(20544, 20607, this.in[1]);
      this.mra.set(20608, 20671, this.in[2]);
      this.mra.set(20672, 20735, this.in[3]);
      this.mra.setMR('耀', '뿿', 1);
   }

   private void initReadMapTheGlob() {
      this.mra.set(0, 16383, this.theGlobDecoder);
      this.mra.setMR(16384, 18431, 0);
      this.mra.setMR(18432, 20479, 0);
      this.mra.set(20480, 20543, this.in[0]);
      this.mra.set(20544, 20607, this.in[1]);
      this.mra.set(20608, 20671, this.in[2]);
      this.mra.set(20672, 20735, this.in[3]);
   }

   private void initReadMapVanVan() {
      this.mra.setMR(0, 16383, 1);
      this.mra.setMR(16384, 18431, 0);
      this.mra.setMR(18432, 20479, 0);
      this.mra.set(20480, 20480, this.in[0]);
      this.mra.set(20544, 20544, this.in[1]);
      this.mra.set(20608, 20608, this.in[2]);
      this.mra.set(20672, 20672, this.in[3]);
      this.mra.setMR('耀', '迿', 1);
   }

   private void initReadPort() {
   }

   private void initReadPortTheGlob() {
      this.ior.set(0, 255, this.readTheGlobROMDecrypt);
   }

   private void initWriteMap() {
      this.mwa.setMW(0, 16383, 1);
      this.mwa.set(16384, 18431, this.writeVRAM);
      this.mwa.setMW(18432, 20463, 0);
      this.mwa.setMW(20464, 20479, 0);
      this.mwa.set(20480, 20480, this.switchedIRQ);
      this.mwa.setMW(20482, 20482, 2);
      this.mwa.set(20544, 20575, this.writeNamcoSoundRegs);
      this.mwa.setMW(20576, 20591, 0);
      this.mwa.setMW('耀', '뿿', 1);
      this.mwa.set('쀀', '쟯', this.writeVRAM);
      this.mwa.setMW('\uffff', '\uffff', 2);
   }

   private void initWriteMapVanVan() {
      this.mwa.setMW(0, 16383, 1);
      this.mwa.set(16384, 18431, this.writeVRAM);
      this.mwa.setMW(18432, 20463, 0);
      this.mwa.setMW(20464, 20479, 0);
      this.mwa.set(20480, 20480, this.switchedNMI);
      this.mwa.setMW(20485, 20486, 2);
      this.mwa.setMW(20576, 20591, 0);
      this.mwa.setMW(20608, 20608, 2);
      this.mwa.setMW('耀', '迿', 1);
      this.mwa.setMW('렀', '롿', 2);
   }

   private void initWritePort() {
      this.iow.set(0, 0, this.writeZ80IRQVector);
   }

   private void initWritePortDremshpr() {
      this.iow.set(6, 6, this.writeAY8910WritePort0);
      this.iow.set(7, 7, this.writeAY8910ControlPort0);
   }

   private void initWritePortVanVan() {
      this.iow.set(1, 1, this.sn1.getWriteCommandHandler());
      this.iow.set(2, 2, this.sn2.getWriteCommandHandler());
   }

   private void loadCrushROM(URL var1) {
      RomLoader var2 = new RomLoader();
      var2.setZip("crush");
      var2.setMemory(this.mem_cpu);
      var2.loadROM("crushkrl.6e", 0, 4096, -1461874860);
      var2.loadROM("crushkrl.6f", 4096, 4096, -1858571623);
      var2.loadROM("crushkrl.6h", 8192, 4096, -733651161);
      var2.loadROM("crushkrl.6j", 12288, 4096, -710950319);
      var2.setMemory(this.mem_gfx1);
      var2.loadROM("maketrax.5e", 0, 4096, -1850027302);
      var2.setMemory(this.mem_gfx2);
      var2.loadROM("maketrax.5f", 0, 4096, -1364746411);
      var2.setMemory(this.mem_prom);
      var2.loadROM("82s123.7f", 0, 32, 801525949);
      var2.loadROM("crush.4a", 32, 256, 734384953);
      var2.loadZip(var1);
   }

   private void loadDremshprROM(URL var1) {
      RomLoader var2 = new RomLoader();
      var2.setZip("dremshpr");
      var2.setMemory(this.mem_cpu);
      var2.loadROM("red_1.50", 0, 4096, -2096340127);
      var2.loadROM("red_2.51", 4096, 4096, -769306164);
      var2.loadROM("red_3.52", 8192, 4096, 118727498);
      var2.loadROM("red_4.53", 12288, 4096, -208942422);
      var2.loadROM("red_5.39", '耀', 4096, 1782063719);
      var2.loadROM("red_6.40", '退', 4096, 1291366689);
      var2.loadROM("red_7.41", 'ꀀ', 4096, -1118845766);
      var2.setMemory(this.mem_gfx1);
      var2.loadROM("red-20.18", 0, 4096, 761698524);
      var2.setMemory(this.mem_gfx2);
      var2.loadROM("red-21.19", 0, 4096, 952749723);
      var2.setMemory(this.mem_prom);
      var2.loadROM("6331-1.6", 0, 32, -836922109);
      var2.loadROM("6301-1.37", 32, 256, 970390364);
      var2.loadZip(var1);
   }

   private void loadEyesROM(URL var1) {
      RomLoader var2 = new RomLoader();
      var2.setZip("eyes");
      var2.setMemory(this.mem_cpu);
      var2.loadROM("d7", 0, 4096, 990489737);
      var2.loadROM("e7", 4096, 4096, -1760991147);
      var2.loadROM("f7", 8192, 4096, 1931356494);
      var2.loadROM("h7", 12288, 4096, 586655513);
      var2.setMemory(this.mem_gfx1);
      var2.loadROM("d5", 0, 4096, -693174224);
      var2.setMemory(this.mem_gfx2);
      var2.loadROM("e5", 0, 4096, -1540664831);
      var2.setMemory(this.mem_prom);
      var2.loadROM("82s123.7f", 0, 32, 801525949);
      var2.loadROM("82s129.4a", 32, 256, -656963543);
      var2.loadZip(var1);
   }

   private void loadLizWizROM(URL var1) {
      RomLoader var2 = new RomLoader();
      var2.setZip("lizwiz");
      var2.setMemory(this.mem_cpu);
      var2.loadROM("6e.cpu", 0, 4096, 851188112);
      var2.loadROM("6f.cpu", 4096, 4096, -282807276);
      var2.loadROM("6h.cpu", 8192, 4096, 817813565);
      var2.loadROM("6j.cpu", 12288, 4096, -586564885);
      var2.loadROM("wiza", '耀', 4096, -153181274);
      var2.loadROM("wizb", '退', 4096, -226511448);
      var2.setMemory(this.mem_gfx1);
      var2.loadROM("5e.cpu", 0, 4096, 1157996147);
      var2.setMemory(this.mem_gfx2);
      var2.loadROM("5f.cpu", 0, 4096, -767125737);
      var2.setMemory(this.mem_prom);
      var2.loadROM("7f.cpu", 0, 32, 1967761735);
      var2.loadROM("4a.cpu", 32, 256, 1608295734);
      var2.loadZip(var1);
   }

   private void loadMrTntROM(URL var1) {
      RomLoader var2 = new RomLoader();
      var2.setZip("mrtnt");
      var2.setMemory(this.mem_cpu);
      var2.loadROM("tnt.1", 0, 4096, 243492230);
      var2.loadROM("tnt.2", 4096, 4096, 2006731867);
      var2.loadROM("tnt.3", 8192, 4096, -1385183608);
      var2.loadROM("tnt.4", 12288, 4096, -680175693);
      var2.setMemory(this.mem_gfx1);
      var2.loadROM("tnt.5", 0, 4096, 809028622);
      var2.setMemory(this.mem_gfx2);
      var2.loadROM("tnt.6", 0, 4096, -1755099765);
      var2.setMemory(this.mem_prom);
      var2.loadROM("mrtnt08.bin", 0, 32, 0);
      var2.loadROM("mrtnt04.bin", 32, 256, 0);
      var2.loadZip(var1);
   }

   private void loadMsPacmanROM(URL var1) {
      RomLoader var2 = new RomLoader();
      var2.setZip("mspacman");
      var2.setMemory(this.mem_cpu);
      var2.loadROM("boot1", 0, 4096, -781504073);
      var2.loadROM("boot2", 4096, 4096, 221437534);
      var2.loadROM("boot3", 8192, 4096, 404876811);
      var2.loadROM("boot4", 12288, 4096, 375037400);
      var2.loadROM("boot5", '耀', 4096, -1942065690);
      var2.loadROM("boot6", '退', 4096, 915190117);
      var2.setMemory(this.mem_gfx1);
      var2.loadROM("5e", 0, 4096, 1546132737);
      var2.setMemory(this.mem_gfx2);
      var2.loadROM("5f", 0, 4096, 1633351945);
      var2.setMemory(this.mem_prom);
      var2.loadROM("82s123.7f", 0, 32, 801525949);
      var2.loadROM("82s126.4a", 32, 256, 1051961572);
      var2.loadZip(var1);
   }

   private void loadPacPlusROM(URL var1) {
      RomLoader var2 = new RomLoader();
      var2.setZip("pacplus");
      var2.setMemory(this.mem_cpu);
      var2.loadROM("pacplus.6e", 0, 4096, -703467672);
      var2.loadROM("pacplus.6f", 4096, 4096, -954174122);
      var2.loadROM("pacplus.6h", 8192, 4096, -1372089296);
      var2.loadROM("pacplus.6j", 12288, 4096, 1517158267);
      var2.setMemory(this.mem_gfx1);
      var2.loadROM("pacplus.5e", 0, 4096, 36451802);
      var2.setMemory(this.mem_gfx2);
      var2.loadROM("pacplus.5f", 0, 4096, 1306942685);
      var2.setMemory(this.mem_prom);
      var2.loadROM("pacplus.7f", 0, 32, 104715578);
      var2.loadROM("pacplus.4a", 32, 256, -495869594);
      var2.loadZip(var1);
   }

   private void loadPacmanROM(URL var1) {
      RomLoader var2 = new RomLoader();
      var2.setZip("pacman");
      var2.setMemory(this.mem_cpu);
      var2.loadROM("namcopac.6e", 0, 4096, -18717773);
      var2.loadROM("namcopac.6f", 4096, 4096, 970062979);
      var2.loadROM("namcopac.6h", 8192, 4096, 34093827);
      var2.loadROM("namcopac.6j", 12288, 4096, 2050424405);
      var2.setMemory(this.mem_gfx1);
      var2.loadROM("pacman.5e", 0, 4096, 211044708);
      var2.setMemory(this.mem_gfx2);
      var2.loadROM("pacman.5f", 0, 4096, -1785729543);
      var2.setMemory(this.mem_prom);
      var2.loadROM("82s123.7f", 0, 32, 801525949);
      var2.loadROM("82s126.4a", 32, 256, 1051961572);
      var2.loadZip(var1);
   }

   private void loadPonpokoROM(URL var1) {
      RomLoader var2 = new RomLoader();
      var2.setZip("ponpoko");
      var2.setMemory(this.mem_cpu);
      var2.loadROM("ppokoj1.bin", 0, 4096, -6045692);
      var2.loadROM("ppokoj2.bin", 4096, 4096, 1246324838);
      var2.loadROM("ppokoj3.bin", 8192, 4096, 400190627);
      var2.loadROM("ppokoj4.bin", 12288, 4096, -1657166491);
      var2.loadROM("ppoko5.bin", '耀', 4096, 1422540157);
      var2.loadROM("ppoko6.bin", '退', 4096, 810928096);
      var2.loadROM("ppoko7.bin", 'ꀀ', 4096, 1019103178);
      var2.loadROM("ppokoj8.bin", '뀀', 4096, 79052742);
      var2.setMemory(this.mem_gfx1);
      var2.loadROM("ppoko9.bin", 0, 4096, -1220666874);
      var2.setMemory(this.mem_gfx2);
      var2.loadROM("ppoko10.bin", 0, 4096, 1644600157);
      var2.setMemory(this.mem_prom);
      var2.loadROM("82s123.7f", 0, 32, 801525949);
      var2.loadROM("82s126.4a", 32, 256, 1051961572);
      var2.loadZip(var1);
   }

   private void loadTheGlobROM(URL var1) {
      RomLoader var2 = new RomLoader();
      var2.setZip("theglob");
      var2.setMemory(this.mem_cpu);
      var2.loadROM("glob.u2", 0, 8192, -2103637014);
      var2.loadROM("glob.u3", 8192, 8192, 836658728);
      var2.setMemory(this.mem_gfx1);
      var2.loadROM("glob.5e", 0, 4096, 1399358048);
      var2.setMemory(this.mem_gfx2);
      var2.loadROM("glob.5f", 0, 4096, 85940679);
      var2.setMemory(this.mem_prom);
      var2.loadROM("glob.7f", 0, 32, 526480679);
      var2.loadROM("glob.4a", 32, 256, 687515497);
      var2.loadZip(var1);
   }

   private void loadVanVanROM(URL var1) {
      RomLoader var2 = new RomLoader();
      var2.setZip("vanvan");
      var2.setMemory(this.mem_cpu);
      var2.loadROM("van-1.50", 0, 4096, -820302352);
      var2.loadROM("van-2.51", 4096, 4096, -547823157);
      var2.loadROM("van-3.52", 8192, 4096, 358030884);
      var2.loadROM("van-4.53", 12288, 4096, -1222325280);
      var2.loadROM("van-5.39", '耀', 4096, -613990068);
      var2.setMemory(this.mem_gfx1);
      var2.loadROM("van-20.18", 0, 4096, 1626324582);
      var2.setMemory(this.mem_gfx2);
      var2.loadROM("van-21.19", 0, 4096, 1574254371);
      var2.setMemory(this.mem_prom);
      var2.loadROM("6331-1.6", 0, 32, -836922109);
      var2.loadROM("6301-1.37", 32, 256, 1266695583);
      var2.loadZip(var1);
   }

   public Emulator createEmulator(URL var1, String var2) {
      super.createEmulator(var1, var2);
      this.in[0] = new InputPort();
      this.in[1] = new InputPort();
      this.in[2] = new InputPort();
      this.in[3] = new InputPort();
      EmulatorProperties var3 = null;
      if(var2.equals("pacman")) {
         this.loadPacmanROM(var1);
         this.initInputPacman();
         var3 = this.createMachineDriverPacman();
         new HighScoreParser(this.emu, this.mem_cpu, 64, 0, 0, 17395, 1, 17389, 250, 1);
      } else if(var2.equals("pacplus")) {
         this.loadPacPlusROM(var1);
         this.initInputPacman();
         var3 = this.createMachineDriverPacman();
         new HighScoreParser(this.emu, this.mem_cpu, 64, 0, 0, 17395, 1, 17389, 250, 1);
         (new PacPlusDecoder(this)).pacplus_decode();
      } else if(var2.equals("mspacman")) {
         this.loadMsPacmanROM(var1);
         this.initInputMsPacman();
         var3 = this.createMachineDriverPacman();
         new HighScoreParser(this.emu, this.mem_cpu, 64, 0, 0, 17395, 1, 17389, 250, 1);
      } else if(var2.equals("ponpoko")) {
         this.loadPonpokoROM(var1);
         this.initInputPonpoko();
         var3 = this.createMachineDriverPacman();
         var3.setRotation(0);
         this.initPonpoko();
      } else if(var2.equals("eyes")) {
         this.loadEyesROM(var1);
         this.initInputEyes();
         var3 = this.createMachineDriverPacman();
         this.initEyes();
      } else if(var2.equals("mrtnt")) {
         this.loadMrTntROM(var1);
         this.initInputMrTnt();
         var3 = this.createMachineDriverPacman();
         this.initEyes();
      } else if(var2.equals("lizwiz")) {
         this.loadLizWizROM(var1);
         this.initInputLizWiz();
         var3 = this.createMachineDriverPacman();
      } else if(var2.equals("theglob")) {
         this.loadTheGlobROM(var1);
         this.initInputTheGlob();
         this.emu.fastBoard = false;
         var3 = this.createMachineDriverTheGlob();
         this.theGlobDecoder.init();
      } else if(var2.equals("vanvan")) {
         this.loadVanVanROM(var1);
         this.initInputVanVan();
         var3 = this.createMachineDriverVanVan();
         var3.setRotation(3);
      } else if(var2.equals("dremshpr")) {
         this.loadDremshprROM(var1);
         this.initInputDremshpr();
         var3 = this.createMachineDriverDremshpr();
         var3.setRotation(3);
      }

      this.emu.init(var3);
      return this.emu;
   }

   public EmulatorProperties createMachineDriverDremshpr() {
      this.initReadMap();
      this.initWriteMap();
      this.initReadPort();
      this.initWritePortDremshpr();
      CpuDriver var3 = new CpuDriver(this.cpu, 3072000, this.mra, this.mwa, this.ior, this.iow, this.switchedNMI, 1);
      AY8910 var1 = this.ay8910;
      EmulatorProperties var2 = new EmulatorProperties();
      var2.setCpuDriver(new CpuDriver[]{var3});
      var2.setFPS(60);
      var2.setVBlankDuration(2500);
      var2.setVideoWidth(288);
      var2.setVideoHeight(224);
      var2.setVisibleArea(0, 287, 0, 223);
      var2.setGfxDecodeInfo(this.createGfxDecodeInfo());
      var2.setPaletteLength(16);
      var2.setColorTableLength(128);
      var2.setPaletteInitializer(this.video);
      var2.setVideoEmulator(this.video);
      var2.setVideoUpdater(this.video);
      var2.setRotation(1);
      var2.setSoundChips(new SoundChip[]{var1});
      var2.setInputPorts(this.in);
      return var2;
   }

   public EmulatorProperties createMachineDriverPacman() {
      this.initReadMap();
      this.initWriteMap();
      this.initReadPort();
      this.initWritePort();
      CpuDriver var2 = new CpuDriver(this.cpu, 3072000, this.mra, this.mwa, this.ior, this.iow, this.switchedIRQ, 1);
      Namco var1 = this.namco;
      EmulatorProperties var3 = new EmulatorProperties();
      var3.setCpuDriver(new CpuDriver[]{var2});
      var3.setFPS(60);
      var3.setVBlankDuration(2500);
      var3.setVideoWidth(288);
      var3.setVideoHeight(224);
      var3.setVisibleArea(0, 287, 0, 223);
      var3.setGfxDecodeInfo(this.createGfxDecodeInfo());
      var3.setPaletteLength(16);
      var3.setColorTableLength(128);
      var3.setPaletteInitializer(this.video);
      var3.setVideoEmulator(this.video);
      var3.setVideoUpdater(this.video);
      var3.setRotation(1);
      var3.setSoundChips(new SoundChip[]{var1});
      var3.setInputPorts(this.in);
      return var3;
   }

   public EmulatorProperties createMachineDriverTheGlob() {
      this.initReadMapTheGlob();
      this.initWriteMap();
      this.initReadPortTheGlob();
      this.initWritePort();
      CpuDriver var2 = new CpuDriver(this.cpu, 3072000, this.mra, this.mwa, this.ior, this.iow, this.switchedIRQ, 1);
      Namco var1 = this.namco;
      EmulatorProperties var3 = new EmulatorProperties();
      var3.setCpuDriver(new CpuDriver[]{var2});
      var3.setFPS(60);
      var3.setVBlankDuration(2500);
      var3.setVideoWidth(288);
      var3.setVideoHeight(224);
      var3.setVisibleArea(0, 287, 0, 223);
      var3.setGfxDecodeInfo(this.createGfxDecodeInfo());
      var3.setPaletteLength(16);
      var3.setColorTableLength(128);
      var3.setPaletteInitializer(this.video);
      var3.setVideoEmulator(this.video);
      var3.setVideoUpdater(this.video);
      var3.setRotation(1);
      var3.setSoundChips(new SoundChip[]{var1});
      var3.setInputPorts(this.in);
      return var3;
   }

   public EmulatorProperties createMachineDriverVanVan() {
      this.initReadMapVanVan();
      this.initWriteMapVanVan();
      this.initReadPort();
      this.initWritePortVanVan();
      CpuDriver var2 = new CpuDriver(this.cpu, 3072000, this.mra, this.mwa, this.ior, this.iow, this.switchedNMI, 1);
      SN76496 var1 = this.sn1;
      SN76496 var4 = this.sn2;
      EmulatorProperties var3 = new EmulatorProperties();
      var3.setCpuDriver(new CpuDriver[]{var2});
      var3.setFPS(60);
      var3.setVBlankDuration(2500);
      var3.setVideoWidth(288);
      var3.setVideoHeight(224);
      var3.setVisibleArea(0, 287, 0, 223);
      var3.setGfxDecodeInfo(this.createGfxDecodeInfo());
      var3.setPaletteLength(16);
      var3.setColorTableLength(128);
      var3.setPaletteInitializer(this.video);
      var3.setVideoEmulator(this.video);
      var3.setVideoUpdater(this.video);
      var3.setRotation(1);
      var3.setSoundChips(new SoundChip[]{var1, var4});
      var3.setInputPorts(this.in);
      return var3;
   }
}
