package net.movegaga.jemu2.driver.arcade.jrpacman;

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
import jef.map.SwitchedInterrupt;
import jef.map.WriteHandler;
import jef.scoring.HighScoreParser;
import jef.sound.SoundChip;
import jef.sound.chip.Namco;
import jef.util.RomLoader;
import jef.video.GfxDecodeInfo;

public class Jrpacman extends BaseDriver {
   Z80 cpu = new Z80();
   BasicEmulator emu = new BasicEmulator();
   SwitchedInterrupt interrupt = new SwitchedInterrupt(0);
   char[] mem_cpu = new char[65536];
   char[] mem_gfx1;
   char[] mem_gfx2;
   char[] mem_prom;
   Namco namco;
   VJrpacman video;
   WriteHandler writeCharBank;
   WriteHandler writeColTableBank;
   WriteHandler writeInterruptEnable;
   WriteHandler writeInterruptVector;
   WriteHandler writePaletteBank;
   WriteHandler writeVRAM;

   public Jrpacman() {
      this.writeInterruptEnable = this.interrupt;
      this.writeInterruptVector = this.cpu;
      this.video = new VJrpacman(this);
      this.mem_gfx1 = new char[8192];
      this.mem_gfx2 = new char[8192];
      this.mem_prom = new char[768];
      this.writeVRAM = this.video;
      this.writePaletteBank = this.video.writePaletteBank();
      this.writeColTableBank = this.video.writeColTableBank();
      this.writeCharBank = this.video.writeCharBank();
      this.namco = new Namco(this.mem_cpu, 3, 96000);
   }

   public Emulator createEmulator(URL var1, String var2) {
      super.createEmulator(var1, var2);
      this.emu = new BasicEmulator();
      Namco var20 = this.namco;
      RomLoader var5 = new RomLoader();
      var5.setZip("jrpacman");
      var5.setMemory(this.mem_cpu);
      var5.loadROM("jrp8d.bin", 0, 8192, -470116562);
      var5.loadROM("jrp8e.bin", 8192, 8192, -326590828);
      var5.loadROM("jrp8h.bin", '耀', 8192, 905051246);
      var5.loadROM("jrp8j.bin", 'ꀀ', 8192, -1758000738);
      var5.loadROM("jrp8k.bin", '쀀', 8192, 1381162391);
      var5.setMemory(this.mem_gfx1);
      var5.loadROM("jrp2c.bin", 0, 8192, 86507419);
      var5.setMemory(this.mem_gfx2);
      var5.loadROM("jrp2e.bin", 0, 8192, 1934061971);
      var5.setMemory(this.mem_prom);
      var5.loadROM("jrprom.9e", 0, 256, 43857348);
      var5.loadROM("jrprom.9f", 256, 256, -287094151);
      var5.loadROM("jrprom.9p", 512, 256, -1620137512);
      var5.loadZip(var1);
      this.decrypt();
      InputPort[] var19 = new InputPort[]{new InputPort(), null, null};
      var19[0].setBit(1, 10);
      var19[0].setBit(2, 8);
      var19[0].setBit(4, 9);
      var19[0].setBit(8, 11);
      var19[0].setBits(16, 16);
      var19[0].setBit(32, 1);
      var19[0].setBit(64, 2);
      var19[0].setBit(128, 2);
      var19[1] = new InputPort();
      var19[1].setBit(1, 138);
      var19[1].setBit(2, 136);
      var19[1].setBit(4, 137);
      var19[1].setBit(8, 139);
      var19[1].setBit(32, 4);
      var19[1].setBit(64, 5);
      var19[1].setBits(144, 144);
      var19[2] = new InputPort();
      var19[2].setBits(255, 201);
      MemoryReadAddress var21 = new MemoryReadAddress(this.mem_cpu);
      var21.setMR(0, 16383, 1);
      var21.setMR(16384, 20479, 0);
      var21.set(20480, 20543, var19[0]);
      var21.set(20544, 20607, var19[1]);
      var21.set(20608, 20671, var19[2]);
      var21.setMR(20672, '\uffff', 1);
      MemoryWriteAddress var7 = new MemoryWriteAddress(this.mem_cpu);
      var7.setMW(0, '\uffff', 0);
      var7.setMW(0, 16383, 1);
      var7.set(16384, 18431, this.writeVRAM);
      var7.setMW(18432, 20479, 0);
      var7.set(20480, 20480, this.writeInterruptEnable);
      var7.set(20544, 20575, this.namco.getPengoInterface(20544));
      var7.setMW(20576, 20591, 0);
      var7.set(20592, 20592, this.writePaletteBank);
      var7.set(20593, 20593, this.writeColTableBank);
      var7.setMW(20595, 20595, 0);
      var7.set(20596, 20596, this.writeCharBank);
      var7.setMW(20597, 20597, 0);
      var7.setMW(20608, 20608, 0);
      var7.setMW(20672, 20672, 2);
      var7.setMW('耀', '\udfff', 1);
      IOWritePort var8 = new IOWritePort();
      var8.set(0, 0, this.writeInterruptVector);
      IOReadPort var6 = new IOReadPort();
      int[] var17 = new int[]{8};
      int var3 = this.RGN_FRAC(1, 1);
      int[] var14 = new int[]{0, 4};
      int[] var16 = new int[]{64, 65, 66, 67, 0, 1, 2, 3};
      int[] var15 = new int[]{0, 8, 16, 24, 32, 40, 48, 56};
      int[] var18 = new int[]{128};
      int[] var12 = new int[]{16};
      int var4 = this.RGN_FRAC(1, 1);
      int[] var13 = new int[]{2};
      int[] var10 = new int[]{0, 4};
      int[] var11 = new int[]{64, 65, 66, 67, 128, 129, 130, 131, 192, 193, 194, 195, 0, 1, 2, 3};
      int[] var9 = new int[]{0, 8, 16, 24, 32, 40, 48, 56, 256, 264, 272, 280, 288, 296, 304, 312};
      GfxDecodeInfo var25 = new GfxDecodeInfo(this.mem_gfx1, 0, new int[][]{var17, {8}, {var3}, {2}, var14, var16, var15, var18}, 0, 128);
      GfxDecodeInfo var24 = new GfxDecodeInfo(this.mem_gfx2, 0, new int[][]{{16}, var12, {var4}, var13, var10, var11, var9, {512}}, 0, 128);
      CpuDriver var22 = new CpuDriver(this.cpu, 3072000, var21, var7, var6, var8, this.interrupt, 1);
      EmulatorProperties var23 = new EmulatorProperties();
      var23.setCpuDriver(new CpuDriver[]{var22});
      var23.setFPS(60);
      var23.setVideoDimensions(288, 224);
      var23.setVisibleArea(0, 287, 0, 223);
      var23.setGfxDecodeInfo(new GfxDecodeInfo[]{var25, var24});
      var23.setPaletteLength(32);
      var23.setColorTableLength(512);
      var23.setVideoEmulation(this.video);
      var23.setInputPorts(var19);
      var23.setRotation(1);
      var23.setSoundChips(new SoundChip[]{var20});
      new HighScoreParser(this.emu, this.mem_cpu, 64, 0, 0, 18301, 1, 18295, 250, 1);
      this.video.init(var23);
      this.emu.init(var23);
      return this.emu;
   }

   public void decrypt() {
      int[] var4 = new int[]{193, 0, 2, 128, 4, 0, 6, 128, 3, 0, 2, 128, 9, 0, 4, 128, '饨', 0, 1, 128, 2, 0, 1, 128, 9, 0, 2, 128, 9, 0, 1, 128, 175, 0, 14, 4, 2, 0, 4, 4, 30, 0, 1, 128, 2, 0, 1, 128, 2, 0, 2, 128, 9, 0, 2, 128, 9, 0, 2, 128, 131, 0, 1, 4, 1, 1, 1, 0, 2, 5, 1, 0, 3, 4, 3, 1, 2, 0, 1, 4, 3, 1, 3, 0, 3, 4, 1, 1, 46, 0, 120, 1, 1, 4, 1, 5, 1, 0, 1, 1, 1, 4, 2, 0, 1, 1, 1, 4, 2, 0, 1, 1, 1, 4, 2, 0, 1, 1, 1, 4, 1, 5, 1, 0, 1, 1, 1, 4, 2, 0, 1, 1, 1, 4, 2, 0, 1, 1, 1, 4, 1, 5, 1, 0, 432, 1, 1, 0, 2, 1, 173, 0, 49, 1, 92, 0, 5, 1, 24654, 0};
      int var2 = 0;

      for(int var1 = 0; var1 < 160; var1 += 2) {
         for(int var3 = 0; var3 < var4[var1]; ++var3) {
            char[] var5 = this.mem_cpu;
            var5[var2] = (char)(var5[var2] ^ var4[var1 + 1]);
            ++var2;
         }
      }

   }
}
