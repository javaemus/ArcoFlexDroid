package net.movegaga.jemu2.driver.arcade.yiear;

import net.movegaga.jemu2.driver.BaseDriver;

import java.net.URL;

import jef.cpu.M6809;
import jef.cpuboard.CpuDriver;
import jef.machine.Emulator;
import jef.machine.EmulatorProperties;
import jef.map.InputPort;
import jef.map.InterruptHandler;
import jef.map.MemoryReadAddress;
import jef.map.MemoryWriteAddress;
import jef.map.WriteHandler;
import jef.scoring.HighScoreParser;
import jef.sound.chip.SN76496;
import jef.util.RomLoader;
import jef.video.GfxDecodeInfo;

public class Yiear extends BaseDriver {
   static int soundLatch = 0;
   int[][] characters;
   M6809 cpu = new M6809();
   MYiear emu;
   InputPort[] in;
   InterruptHandler interrupt;
   InterruptHandler interruptNMI;
   char[] mem_cpu = new char[65536];
   char[] mem_gfx1;
   char[] mem_gfx2;
   char[] mem_prom;
   char[] mem_sound;
   MemoryReadAddress mra;
   MemoryWriteAddress mwa;
   SN76496 sn;
   int[][] sprites;
   VYiear video;
   WriteHandler writeControl;
   WriteHandler writeInterruptEnable;
   WriteHandler writeInterruptNMIEnable;
   WriteHandler writeVRAM;

   public Yiear() {
      this.mra = new MemoryReadAddress(this.mem_cpu);
      this.mwa = new MemoryWriteAddress(this.mem_cpu);
      this.video = new VYiear(this);
      this.writeVRAM = this.video;
      this.mem_gfx1 = new char[16384];
      this.mem_gfx2 = new char[65536];
      this.mem_prom = new char[32];
      this.mem_sound = new char[8192];
      this.sn = new SN76496(1536000);
      this.emu = new MYiear();
      this.interrupt = this.emu.interruptIRQ();
      this.interruptNMI = this.emu.interruptNMI();
      this.writeInterruptEnable = this.emu.interruptEnable();
      this.writeInterruptNMIEnable = this.emu.nmiEnable();
      this.writeControl = new WriteControl();
      this.in = new InputPort[6];
      int[] var4 = new int[]{8};
      int[] var5 = new int[]{8};
      int[] var6 = new int[]{this.RGN_FRAC(1, 2)};
      int[] var7 = new int[]{4};
      int[] var1 = new int[]{this.RGN_FRAC(1, 2) + 4, this.RGN_FRAC(1, 2), 4, 0};
      int[] var2 = new int[]{0, 1, 2, 3, 64, 65, 66, 67};
      int[] var3 = new int[]{0, 8, 16, 24, 32, 40, 48, 56};
      this.characters = new int[][]{var4, var5, var6, var7, var1, var2, var3, {128}};
      var4 = new int[]{16};
      var5 = new int[]{16};
      var6 = new int[]{this.RGN_FRAC(1, 2)};
      var3 = new int[]{this.RGN_FRAC(1, 2) + 4, this.RGN_FRAC(1, 2), 4, 0};
      var2 = new int[]{0, 1, 2, 3, 64, 65, 66, 67, 128, 129, 130, 131, 192, 193, 194, 195};
      var1 = new int[]{0, 8, 16, 24, 32, 40, 48, 56, 256, 264, 272, 280, 288, 296, 304, 312};
      var7 = new int[]{512};
      this.sprites = new int[][]{var4, var5, var6, {4}, var3, var2, var1, var7};
   }

   public Emulator createEmulator(URL var1, String var2) {
      super.createEmulator(var1, var2);
      RomLoader var6 = new RomLoader();
      var6.setZip("yiear");
      var6.setMemory(this.mem_cpu);
      var6.loadROM("i08.10d", '耀', 16384, -489208437);
      var6.loadROM("i07.8d", '쀀', 16384, 2109162542);
      var6.setMemory(this.mem_gfx1);
      var6.loadROM("g16_1.bin", 0, 8192, -1232086755);
      var6.loadROM("g15_2.bin", 8192, 8192, -642684986);
      var6.setMemory(this.mem_gfx2);
      var6.loadROM("g04_5.bin", 0, 16384, 1158716201);
      var6.loadROM("g03_6.bin", 16384, 16384, 493160336);
      var6.loadROM("g06_3.bin", '耀', 16384, -425028517);
      var6.loadROM("g05_4.bin", '쀀', 16384, -870810590);
      var6.setMemory(this.mem_prom);
      var6.loadROM("yiear.clr", 0, 32, -1031547105);
      var6.setMemory(this.mem_sound);
      var6.loadROM("a12_9.bin", 0, 8192, -145091271);
      var6.loadZip(var1);
      this.in[0] = new InputPort();
      this.in[1] = new InputPort();
      this.in[2] = new InputPort();
      this.in[3] = new InputPort();
      this.in[4] = new InputPort();
      this.in[5] = new InputPort();
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
      this.in[1].setBit(64, 14);
      this.in[1].setBits(128, 128);
      this.in[2].setBit(1, 136);
      this.in[2].setBit(2, 137);
      this.in[2].setBit(4, 138);
      this.in[2].setBit(8, 139);
      this.in[2].setBit(16, 140);
      this.in[2].setBit(32, 141);
      this.in[2].setBit(64, 142);
      this.in[2].setBits(128, 128);
      this.in[3].setBits(255, 89);
      this.in[4].setBits(255, 255);
      this.in[5].setBits(255, 255);
      this.mra.set(19456, 19456, this.in[3]);
      this.mra.set(19712, 19712, this.in[4]);
      this.mra.set(19968, 19968, this.in[0]);
      this.mra.set(19969, 19969, this.in[1]);
      this.mra.set(19970, 19970, this.in[2]);
      this.mra.set(19971, 19971, this.in[5]);
      this.mwa.set(16384, 16384, this.writeControl);
      this.mwa.set(18432, 18432, new WriteSoundLatch());
      this.mwa.set(18688, 18688, new WriteSNCommand());
      this.mwa.set(22528, 24575, this.writeVRAM);
      this.mwa.setMW('耀', '\uffff', 1);
      GfxDecodeInfo var4 = new GfxDecodeInfo(this.mem_gfx1, 0, this.characters, 16, 1);
      GfxDecodeInfo var7 = new GfxDecodeInfo(this.mem_gfx2, 0, this.sprites, 0, 1);
      CpuDriver var5 = new CpuDriver(this.cpu, 1152000, this.mra, this.mwa, this.interrupt, 1);
      EmulatorProperties var3 = new EmulatorProperties();
      var3.setCpuDriver(var5);
      var3.setSoundChip(this.sn);
      var3.setFPS(60);
      var3.setVideoDimensions(256, 256);
      var3.setVisibleArea(0, 255, 16, 239);
      var3.setGfxDecodeInfo(new GfxDecodeInfo[]{var4, var7});
      var3.setPaletteLength(32);
      var3.setColorTableLength(32);
      var3.setVideoEmulation(this.video);
      var3.setInputPorts(this.in);
      new HighScoreParser(this.emu, this.mem_cpu, 16, 0, 0, 22721, 2, 22735, 0, 1);
      this.video.init(var3);
      this.emu.init(var3);
      return this.emu;
   }

   class WriteControl implements WriteHandler {
      public void write(int var1, int var2) {
         Yiear.this.writeInterruptNMIEnable.write(0, var2 & 2);
         Yiear.this.writeInterruptEnable.write(0, var2 & 4);
      }
   }

   public class WriteSNCommand implements WriteHandler {
      public void write(int var1, int var2) {
         Yiear.this.sn.writeCommand(Yiear.soundLatch);
      }
   }

   public class WriteSoundLatch implements WriteHandler {
      public void write(int var1, int var2) {
         Yiear.soundLatch = var2;
      }
   }
}
