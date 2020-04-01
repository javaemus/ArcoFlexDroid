package net.movegaga.jemu2.driver.arcade.galaga;

import net.movegaga.jemu2.driver.BaseDriver;
import net.movegaga.jemu2.driver.Driver;

import java.net.URL;

import jef.cpu.Z80;
import jef.cpuboard.CpuDriver;
import jef.machine.Emulator;
import jef.machine.EmulatorProperties;
import jef.map.InputPort;
import jef.map.InterruptHandler;
import jef.map.MemoryReadAddress;
import jef.map.MemoryWriteAddress;
import jef.map.ReadHandler;
import jef.map.WriteHandler;
import jef.scoring.HighScoreParser;
import jef.sound.SoundChip;
import jef.sound.chip.Namco;
import jef.util.RomLoader;
import jef.video.GfxDecodeInfo;
import jef.video.GfxLayout;
import jef.video.VideoConstants;

public class Galaga extends BaseDriver implements Driver, VideoConstants {
   public Z80 cpu1 = new Z80();
   public Z80 cpu2;
   public Z80 cpu3;
   MGalaga emu = new MGalaga();
   InputPort[] in;
   InterruptHandler interrupt1;
   InterruptHandler interrupt2;
   InterruptHandler interrupt3;
   char[] mem_cpu1 = new char[65536];
   char[] mem_cpu2;
   char[] mem_cpu3;
   char[] mem_gfx1;
   char[] mem_gfx2;
   char[] mem_prom;
   char[] mem_sound;
   MemoryReadAddress mra1;
   MemoryReadAddress mra2;
   MemoryReadAddress mra3;
   MemoryWriteAddress mwa1;
   MemoryWriteAddress mwa2;
   MemoryWriteAddress mwa3;
   Namco namco;
   ReadHandler readCustomIO;
   ReadHandler readCustomIOData;
   ReadHandler readDips;
   ReadHandler readSharedRAM;
   VGalaga video;
   WriteHandler writeCustomIO;
   WriteHandler writeCustomIOData;
   WriteHandler writeHalt;
   WriteHandler writeInterruptEnable1;
   WriteHandler writeInterruptEnable2;
   WriteHandler writeInterruptEnable3;
   WriteHandler writeSharedRAM;
   WriteHandler writeSoundRegs;

   public Galaga() {
      this.mra1 = new MemoryReadAddress(this.mem_cpu1);
      this.mwa1 = new MemoryWriteAddress(this.mem_cpu1);
      this.interrupt1 = this.emu.galaga_interrupt_1();
      this.cpu2 = new Z80();
      this.mem_cpu2 = new char[65536];
      this.mra2 = new MemoryReadAddress(this.mem_cpu2);
      this.mwa2 = new MemoryWriteAddress(this.mem_cpu2);
      this.interrupt2 = this.emu.galaga_interrupt_2();
      this.cpu3 = new Z80();
      this.mem_cpu3 = new char[65536];
      this.mra3 = new MemoryReadAddress(this.mem_cpu3);
      this.mwa3 = new MemoryWriteAddress(this.mem_cpu3);
      this.interrupt3 = this.emu.galaga_interrupt_3();
      this.video = new VGalaga();
      this.mem_gfx1 = new char[4096];
      this.mem_gfx2 = new char[8192];
      this.mem_prom = new char[800];
      this.mem_sound = new char[256];
      this.namco = new Namco(this.mem_cpu3, 3, 96000, this.mem_sound);
      this.writeSoundRegs = this.namco.getPengoInterface(26624);
      this.in = new InputPort[5];
      this.writeInterruptEnable1 = this.emu.writeInterruptEnable1();
      this.writeInterruptEnable2 = this.emu.writeInterruptEnable2();
      this.writeInterruptEnable3 = this.emu.writeInterruptEnable3();
      this.writeSharedRAM = this.emu.writeSharedRAM();
      this.writeCustomIOData = this.emu.writeCustomIOData();
      this.writeCustomIO = this.emu.writeCustomIO();
      this.writeHalt = this.emu.writeHalt();
      this.readSharedRAM = this.emu.readSharedRAM();
      this.readDips = this.emu.readDips();
      this.readCustomIOData = this.emu.readCustomIOData();
      this.readCustomIO = this.emu.readCustomIO();
   }

   private GfxLayout createCharLayout() {
      int[] var2 = new int[]{0, 4};
      int[] var3 = new int[]{64, 65, 66, 67, 0, 1, 2, 3};
      int[] var1 = new int[]{0, 8, 16, 24, 32, 40, 48, 56};
      return new GfxLayout(8, 8, 128, 2, var2, var3, var1, 128);
   }

   private GfxDecodeInfo[] createGfxDecodeInfo() {
      return new GfxDecodeInfo[]{new GfxDecodeInfo(this.mem_gfx1, 0, this.createCharLayout(), 0, 32), new GfxDecodeInfo(this.mem_gfx2, 0, this.createSpriteLayout(), 128, 32)};
   }

   private GfxLayout createSpriteLayout() {
      int[] var3 = new int[]{0, 4};
      int[] var1 = new int[]{0, 1, 2, 3, 64, 65, 66, 67, 128, 129, 130, 131, 192, 193, 194, 195};
      int[] var2 = new int[]{0, 8, 16, 24, 32, 40, 48, 56, 256, 264, 272, 280, 288, 296, 304, 312};
      return new GfxLayout(16, 16, 128, 2, var3, var1, var2, 512);
   }

   private void initInput() {
      this.in[0].setBits(255, 151);
      this.in[1].setBits(255, 255);
      this.in[2].setBit(1, 0);
      this.in[2].setBit(2, 9);
      this.in[2].setBit(4, 0);
      this.in[2].setBit(8, 8);
      this.in[2].setBit(16, 12);
      this.in[2].setBit(192, 0);
      this.in[3].setBit(1, 0);
      this.in[3].setBit(2, 9);
      this.in[3].setBit(4, 0);
      this.in[3].setBit(8, 8);
      this.in[3].setBit(16, 12);
      this.in[3].setBit(192, 0);
      this.in[4].setBit(4, 4);
      this.in[4].setBit(8, 5);
      this.in[4].setBit(16, 1);
      this.in[4].setBit(32, 2);
      this.in[4].setBit(64, 2);
      this.in[4].setService(128);
   }

   private void initMemoryMap() {
      this.mra1.set('耀', '\u9fff', this.readSharedRAM);
      this.mra1.set(26624, 26631, this.readDips);
      this.mra1.set(28672, 28687, this.readCustomIOData);
      this.mra1.set(28928, 28928, this.readCustomIO);
      this.mra1.setMR(0, 16383, 1);
      this.mra2.set('耀', '\u9fff', this.readSharedRAM);
      this.mra2.set(26624, 26631, this.readDips);
      this.mra2.setMR(0, 8191, 1);
      this.mra3.set('耀', '\u9fff', this.readSharedRAM);
      this.mra3.set(26624, 26631, this.readDips);
      this.mra3.setMR(0, 8191, 1);
      this.mwa1.set('耀', '\u9fff', this.writeSharedRAM);
      this.mwa1.setMW(26672, 26672, 2);
      this.mwa1.set(28672, 28687, this.writeCustomIOData);
      this.mwa1.set(28928, 28928, this.writeCustomIO);
      this.mwa1.setMW('ꀀ', 'ꀅ', 0);
      this.mwa1.set(26656, 26656, this.writeInterruptEnable1);
      this.mwa1.set(26658, 26658, this.writeInterruptEnable3);
      this.mwa1.set(26659, 26659, this.writeHalt);
      this.mwa1.setMW(0, 16383, 1);
      this.mwa2.set('耀', '\u9fff', this.writeSharedRAM);
      this.mwa2.set(26657, 26657, this.writeInterruptEnable2);
      this.mwa2.setMW(0, 8191, 1);
      this.mwa3.set('耀', '\u9fff', this.writeSharedRAM);
      this.mwa3.set(26624, 26655, this.writeSoundRegs);
      this.mwa3.set(26658, 26658, this.writeInterruptEnable3);
      this.mwa3.setMW(0, 8191, 1);
   }

   private void loadROM(URL var1) {
      RomLoader var2 = new RomLoader();
      var2.setZip("galaga");
      var2.setMemory(this.mem_cpu1);
      var2.loadROM("04m_g01.bin", 0, 4096, -1549732029);
      var2.loadROM("04k_g02.bin", 4096, 4096, 1136332124);
      var2.loadROM("04j_g03.bin", 8192, 4096, 1966925059);
      var2.loadROM("04h_g04.bin", 12288, 4096, -2088287166);
      var2.setMemory(this.mem_cpu2);
      var2.loadROM("04e_g05.bin", 0, 4096, 822279373);
      var2.setMemory(this.mem_cpu3);
      var2.loadROM("04d_g06.bin", 0, 4096, -1986721651);
      var2.setMemory(this.mem_gfx1);
      var2.loadROM("07m_g08.bin", 0, 4096, 1488123004);
      var2.setMemory(this.mem_gfx2);
      var2.loadROM("07e_g10.bin", 0, 4096, -1388020608);
      var2.loadROM("07h_g09.bin", 4096, 4096, -579921156);
      var2.setMemory(this.mem_prom);
      var2.loadROM("5n.bin", 0, 32, 1415593067);
      var2.loadROM("2n.bin", 32, 256, -1522019525);
      var2.loadROM("1c.bin", 288, 256, -1225423365);
      var2.loadROM("5c.bin", 544, 256, -1948949002);
      var2.setMemory(this.mem_sound);
      var2.loadROM("1d.bin", 0, 256, -2032587996);
      var2.loadZip(var1);
   }

   public Emulator createEmulator(URL var1, String var2) {
      super.createEmulator(var1, var2);
      this.in[0] = new InputPort();
      this.in[1] = new InputPort();
      this.in[2] = new InputPort();
      this.in[3] = new InputPort();
      this.in[4] = new InputPort();
      EmulatorProperties var7 = new EmulatorProperties();
      this.loadROM(var1);
      this.initInput();
      this.initMemoryMap();
      CpuDriver var4 = new CpuDriver(this.cpu1, 3125000, this.mra1, this.mwa1, this.interrupt1, 1);
      CpuDriver var6 = new CpuDriver(this.cpu2, 3125000, this.mra2, this.mwa2, this.interrupt2, 1);
      CpuDriver var5 = new CpuDriver(this.cpu3, 3125000, this.mra3, this.mwa3, this.interrupt3, 2);
      Namco var3 = this.namco;
      var7.setCpuDriver(new CpuDriver[]{var4, var6, var5});
      var7.setFPS(60);
      var7.setTimeSlicesPerFrame(99);
      var7.setVideoWidth(288);
      var7.setVideoHeight(224);
      var7.setVisibleArea(0, 287, 0, 223);
      var7.setColorTableLength(96);
      var7.setPaletteLength(256);
      var7.setGfxDecodeInfo(this.createGfxDecodeInfo());
      var7.setPaletteInitializer(this.video);
      var7.setVideoInitializer(this.video);
      var7.setVhRefresh(this.video);
      var7.setSoundChips(new SoundChip[]{var3});
      var7.setInputPorts(this.in);
      var7.setRotation(1);
      this.emu.setRefs(this.mem_cpu1, this, this.video);
      this.video.setRefs(this.mem_cpu1, this.mem_prom);
      this.video.init(var7);
      this.emu.init(var7);
      new HighScoreParser(this.emu, this.emu.mem_shared, 36, 0, 0, '菽', 1, '菸', 1000, 1);
      return this.emu;
   }
}
