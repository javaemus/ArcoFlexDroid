package net.movegaga.jemu2.driver.arcade.commando;

import net.movegaga.jemu2.driver.BaseDriver;
import net.movegaga.jemu2.driver.Driver;

import java.net.URL;

import jef.cpu.Cpu;
import jef.cpu.Z80;
import jef.cpuboard.CpuDriver;
import jef.machine.DecryptingEmulator;
import jef.machine.Emulator;
import jef.machine.EmulatorProperties;
import jef.map.InputPort;
import jef.map.InterruptHandler;
import jef.map.MemoryReadAddress;
import jef.map.MemoryWriteAddress;
import jef.map.Register;
import jef.map.WriteHandler;
import jef.map.WriteMap;
import jef.scoring.HighScoreParser;
import jef.sound.SoundChip;
import jef.sound.chip.ym2203.YM2203;
import jef.util.RomLoader;
import jef.video.GfxDecodeInfo;
import jef.video.GfxLayout;
import jef.video.VideoConstants;

public class Commando extends BaseDriver implements Driver, VideoConstants {
   DecryptingEmulator emu        = new DecryptingEmulator();

   // -----------------------------------------------------------
   // Properties related to CPU #1
   // -----------------------------------------------------------
   Z80                cpu1       = new Z80();
   char[]             mem_cpu1   = new char[0x20000];
   InterruptHandler   interrupt1 = new CommandoInterrupt();

   // -----------------------------------------------------------
   // Properties related to CPU #2
   // -----------------------------------------------------------
   Z80                cpu2       = new Z80();
   char[]             mem_cpu2   = new char[0x20000];

   // -----------------------------------------------------------
   // Properties related to Video
   // -----------------------------------------------------------
   VCommando          video      = new VCommando(this);
   char[]             mem_gfx1   = new char[0x4000];
   char[]             mem_gfx2   = new char[0x18000];
   char[]             mem_gfx3   = new char[0x18000];
   char[]             mem_prom   = new char[0x600];
   WriteHandler       writeVRAM  = video.writeVRAM();

   // -----------------------------------------------------------
   // Properties related to Sound
   // -----------------------------------------------------------
   YM2203             ym         = new YM2203(2, 1500000, null, null);
   Register           soundLatch = new Register();

   // -----------------------------------------------------------
   // MISC. Handlers
   // -----------------------------------------------------------
   InputPort[]        in         = new InputPort[5];

   public class CommandoInterrupt implements InterruptHandler {

      public int irq() {
         cpu1.setProperty(Cpu.PROPERTY_Z80_IRQ_VECTOR, 0x10);
         return Cpu.INTERRUPT_TYPE_IRQ;
      }

   }

   private MemoryReadAddress readmem() {
      MemoryReadAddress mra = new MemoryReadAddress(mem_cpu1);
      mra.set(0xc000, 0xc000, in[0]);
      mra.set(0xc001, 0xc001, in[1]);
      mra.set(0xc002, 0xc002, in[2]);
      mra.set(0xc003, 0xc003, in[3]);
      mra.set(0xc004, 0xc004, in[4]);
      return mra;
   }

   private MemoryWriteAddress writemem() {
      MemoryWriteAddress mwa = new MemoryWriteAddress(mem_cpu1);
      mwa.setMW(0x0000, 0xbfff, WriteMap.WR_ROM);
      mwa.set(0xc800, 0xc800, soundLatch);
      mwa.set(0xd000, 0xdfff, writeVRAM);

      return mwa;
   }

   private MemoryReadAddress sound_readmem() {
      MemoryReadAddress mra = new MemoryReadAddress(mem_cpu2);
      mra.set(0x6000, 0x6000, soundLatch);
      return mra;
   }

   private MemoryWriteAddress sound_writemem() {
      MemoryWriteAddress mwa = new MemoryWriteAddress(mem_cpu2);
      mwa.setMW(0x0000, 0x3fff, WriteMap.WR_ROM);
      mwa.set(0x8000, 0x8000, ym.ym2203_control_port_0_w());
      mwa.set(0x8001, 0x8001, ym.ym2203_write_port_0_w());
      mwa.set(0x8002, 0x8002, ym.ym2203_control_port_1_w());
      mwa.set(0x8003, 0x8003, ym.ym2203_write_port_1_w());
      return mwa;
   }

   private InputPort[] initInput() {
      in[0] = new InputPort();
      in[0].setBit(0x01, InputPort.START1);
      in[0].setBit(0x02, InputPort.START2);
      in[0].setBits(0x3c, 0x3c);
      in[0].setBit(0x40, InputPort.COIN1);
      in[0].setBit(0x80, InputPort.COIN2);

      in[1] = new InputPort();
      in[1].setBit(0x01, InputPort.JOY_RIGHT);
      in[1].setBit(0x02, InputPort.JOY_LEFT);
      in[1].setBit(0x04, InputPort.JOY_DOWN);
      in[1].setBit(0x08, InputPort.JOY_UP);
      in[1].setBit(0x10, InputPort.BUTTON1);
      in[1].setBit(0x20, InputPort.BUTTON2);
      in[1].setBits(0xc0, 0xc0);

      in[2] = new InputPort();
      in[2].setBit(0x01, InputPort.JOY_RIGHT_2);
      in[2].setBit(0x02, InputPort.JOY_LEFT_2);
      in[2].setBit(0x04, InputPort.JOY_DOWN_2);
      in[2].setBit(0x08, InputPort.JOY_UP_2);
      in[2].setBit(0x10, InputPort.BUTTON1_2);
      in[2].setBit(0x20, InputPort.BUTTON2_2);
      in[2].setBits(0xc0, 0xc0);

      in[3] = new InputPort();
      in[3].setBits(0xff, 0xff);

      in[4] = new InputPort();
      in[4].setBits(0xff, 0x1f);

      return in;
   }

   public Emulator createEmulator(URL url, String name) {
      super.createEmulator(url, name);

      initInput();
      loadRoms(url);
      EmulatorProperties md = machine_driver_commando();

      decode();

      video.init(md);
      emu.init(md);
      HighScoreParser highScore = new HighScoreParser(emu, mem_cpu1, 0x20, 0x0, 0, 0xd05e, 0x20, 0xd0fe, 0, 10);
      return (Emulator) emu;
   }

   /**
    *
    */
   private void decode() {
      System.out.print("Decrypting ROMs...");
      int A;
      int diff = 0x10000;

      mem_cpu1[0x10000] = mem_cpu1[0];
      for (A = 1; A < 0xc000; A++) {
         int src;

         src = mem_cpu1[A];
         mem_cpu1[A + diff] = (char) (src ^ (src & 0xee) ^ ((src & 0xe0) >> 4) ^ ((src & 0x0e) << 4));
      }

      for (int i = 0; i < 0x4000; i++) {
         mem_cpu2[i + diff] = mem_cpu2[i];
      }

      System.out.println("OK!");

   }

   /**
    * @return
    */
   private boolean loadRoms(URL url) {
      RomLoader romLoader = new RomLoader();
      romLoader.setZip("commando");

      romLoader.setMemory(mem_cpu1);
      romLoader.loadROM("m09_cm04.bin", 0x0000, 0x8000, 0x8438b694);
      romLoader.loadROM("m08_cm03.bin", 0x8000, 0x4000, 0x35486542);

      romLoader.setMemory(mem_cpu2);
      romLoader.loadROM("f09_cm02.bin", 0x0000, 0x4000, 0xf9cc4a74);

      romLoader.setMemory(mem_gfx1);
      romLoader.loadROM("d05_vt01.bin", 0x00000, 0x4000, 0x505726e0); /* characters */

      romLoader.setMemory(mem_gfx2);
      romLoader.loadROM("a05_vt11.bin", 0x00000, 0x4000, 0x7b2e1b48); /* tiles */
      romLoader.loadROM("a06_vt12.bin", 0x04000, 0x4000, 0x81b417d3);
      romLoader.loadROM("a07_vt13.bin", 0x08000, 0x4000, 0x5612dbd2);
      romLoader.loadROM("a08_vt14.bin", 0x0c000, 0x4000, 0x2b2dee36);
      romLoader.loadROM("a09_vt15.bin", 0x10000, 0x4000, 0xde70babf);
      romLoader.loadROM("a10_vt16.bin", 0x14000, 0x4000, 0x14178237);

      romLoader.setMemory(mem_gfx3);
      romLoader.loadROM("e07_vt05.bin", 0x00000, 0x4000, 0x79f16e3d); /* sprites */
      romLoader.loadROM("e08_vt06.bin", 0x04000, 0x4000, 0x26fee521);
      romLoader.loadROM("e09_vt07.bin", 0x08000, 0x4000, 0xca88bdfd);
      romLoader.loadROM("h07_vt08.bin", 0x0c000, 0x4000, 0x2019c883);
      romLoader.loadROM("h08_vt09.bin", 0x10000, 0x4000, 0x98703982);
      romLoader.loadROM("h09_vt10.bin", 0x14000, 0x4000, 0xf069d2f8);

      romLoader.setMemory(mem_prom);
      romLoader.loadROM("01d_vtb1.bin", 0x0000, 0x0100, 0x3aba15a1);
      romLoader.loadROM("02d_vtb2.bin", 0x0100, 0x0100, 0x88865754);
      romLoader.loadROM("03d_vtb3.bin", 0x0200, 0x0100, 0x4c14c3f6);
      romLoader.loadROM("01h_vtb4.bin", 0x0300, 0x0100, 0xb388c246);
      romLoader.loadROM("06l_vtb5.bin", 0x0400, 0x0100, 0x712ac508);
      romLoader.loadROM("06e_vtb6.bin", 0x0500, 0x0100, 0x0eaf5158);

      romLoader.loadZip(url);

      return true;
   }

   /**
    * @return
    */
   private EmulatorProperties machine_driver_commando() {
      CpuDriver[] cpuDriver = new CpuDriver[2];
      cpuDriver[0] = new CpuDriver(cpu1, 4000000, readmem(), writemem(), interrupt1, 1);
      cpuDriver[1] = new CpuDriver(cpu2, 3000000, sound_readmem(), sound_writemem(), emu.interrupt(), 4);
      cpuDriver[1].isAudioCpu = true;

      SoundChip[] soundChip = new SoundChip[1];
      soundChip[0] = (SoundChip) ym;

      EmulatorProperties md = new EmulatorProperties();
      md.setCpuDriver(cpuDriver);
      md.setFPS(60);
      md.setVBlankDuration(1000);
      md.setTimeSlicesPerFrame(100);
      md.setVideoWidth(256);
      md.setVideoHeight(256);
      md.setVisibleArea(0, 255, 16, 239);
      md.setGfxDecodeInfo(gfxDecodeInfo());
      md.setColorTableLength(256);
      md.setPaletteLength(256);
      md.setPaletteInitializer(video);
      md.setVhRefresh(video);
      md.setInputPorts(in);
      md.setSoundChips(soundChip);
      md.setRotation(ROT270);

      return md;
   }

   private GfxLayout charlayout() {

      int[] pOffs = { 4, 0 };
      int[] xOffs = { 0, 1, 2, 3, 8 + 0, 8 + 1, 8 + 2, 8 + 3 };
      int[] yOffs = { 0 * 16, 1 * 16, 2 * 16, 3 * 16, 4 * 16, 5 * 16, 6 * 16, 7 * 16 };

      return new GfxLayout(8, 8, 1024, 2, pOffs, xOffs, yOffs, 16 * 8);
   }

   private GfxLayout spritelayout() {

      int[] pOffs = { 4, 0, 768 * 64 * 8 + 4, 768 * 64 * 8 + 0 };
      int[] xOffs = { 0, 1, 2, 3, 8 + 0, 8 + 1, 8 + 2, 8 + 3, 32 * 8 + 0, 32 * 8 + 1, 32 * 8 + 2, 32 * 8 + 3,
              33 * 8 + 0, 33 * 8 + 1, 33 * 8 + 2, 33 * 8 + 3 };
      int[] yOffs = { 0 * 16, 1 * 16, 2 * 16, 3 * 16, 4 * 16, 5 * 16, 6 * 16, 7 * 16, 8 * 16, 9 * 16, 10 * 16,
              11 * 16, 12 * 16, 13 * 16, 14 * 16, 15 * 16 };

      return new GfxLayout(16, 16, 768, 4, pOffs, xOffs, yOffs, 64 * 8);
   }

   private GfxLayout tilelayout() {

      int[] pOffs = { 2 * 1024 * 32 * 8, 1024 * 32 * 8, 0 };
      int[] xOffs = { 0, 1, 2, 3, 4, 5, 6, 7, 16 * 8 + 0, 16 * 8 + 1, 16 * 8 + 2, 16 * 8 + 3, 16 * 8 + 4, 16 * 8 + 5,
              16 * 8 + 6, 16 * 8 + 7 };
      int[] yOffs = { 0 * 8, 1 * 8, 2 * 8, 3 * 8, 4 * 8, 5 * 8, 6 * 8, 7 * 8, 8 * 8, 9 * 8, 10 * 8, 11 * 8, 12 * 8,
              13 * 8, 14 * 8, 15 * 8 };

      return new GfxLayout(16, 16, 1024, 3, pOffs, xOffs, yOffs, 32 * 8);
   }

   /**
    * @return
    */
   private GfxDecodeInfo[] gfxDecodeInfo() {
      GfxDecodeInfo gdi[] = new GfxDecodeInfo[3];
      gdi[0] = new GfxDecodeInfo(mem_gfx1, 0, charlayout(), 192, 16);
      gdi[1] = new GfxDecodeInfo(mem_gfx2, 0, tilelayout(), 0, 16);
      gdi[2] = new GfxDecodeInfo(mem_gfx3, 0, spritelayout(), 128, 4);
      return gdi;
   }

}
