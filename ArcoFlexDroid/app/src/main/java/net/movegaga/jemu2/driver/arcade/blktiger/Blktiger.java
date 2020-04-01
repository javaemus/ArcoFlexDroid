package net.movegaga.jemu2.driver.arcade.blktiger;

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
import jef.map.Register;
import jef.map.SwitchedInterrupt;
import jef.map.WriteHandler;
import jef.scoring.HighScoreParser;
import jef.sound.chip.fm.FMIRQHandler;
import jef.sound.chip.fm.FMTimerHandler;
import jef.sound.chip.ym2203.YM2203;
import jef.util.RomLoader;
import jef.video.GfxDecodeInfo;
import jef.video.GfxLayout;

public class Blktiger extends BaseDriver {
   int bankaddress;
   Z80 cpu1 = new Z80();
   Z80 cpu2;
   BasicEmulator emu = new BasicEmulator();
   InputPort[] in;
   InterruptHandler interrupt;
   IOReadPort ior;
   IOWritePort iow;
   char[] mem_cpu1 = new char[327680];
   char[] mem_cpu2;
   char[] mem_gfx1;
   char[] mem_gfx2;
   char[] mem_gfx3;
   char[] mem_prom;
   MemoryReadAddress mra1;
   MemoryReadAddress mra2;
   MemoryWriteAddress mwa1;
   MemoryWriteAddress mwa2;
   ReadHandler readBackGround;
   ReadHandler readBank;
   ReadHandler readProtection;
   FMIRQHandler sndIrq;
   Register soundLatch;
   VBlktiger video;
   WriteHandler writeBackGround;
   WriteHandler writeBankSwitch;
   WriteHandler writePaletteRAM1;
   WriteHandler writePaletteRAM2;
   WriteHandler writeScreenLayout;
   WriteHandler writeScrollBank;
   WriteHandler writeScrollX;
   WriteHandler writeScrollY;
   WriteHandler writeVRAM;
   YM2203 ym;

   public Blktiger() {
      this.mra1 = new MemoryReadAddress(this.mem_cpu1);
      this.mwa1 = new MemoryWriteAddress(this.mem_cpu1);
      this.ior = new IOReadPort();
      this.iow = new IOWritePort();
      this.interrupt = new SwitchedInterrupt(0);
      this.cpu2 = new Z80();
      this.mem_cpu2 = new char[65536];
      this.mra2 = new MemoryReadAddress(this.mem_cpu2);
      this.mwa2 = new MemoryWriteAddress(this.mem_cpu2);
      this.video = new VBlktiger(this);
      this.writeVRAM = this.video.writeVRAM(this.mem_cpu1);
      this.writePaletteRAM1 = this.video.writePaletteRAM(this.mem_cpu1);
      this.writePaletteRAM2 = this.video.writePaletteRAM2(this.mem_cpu1);
      this.writeScrollX = this.video.writeScrollX();
      this.writeScrollY = this.video.writeScrollY();
      this.writeScrollBank = this.video.writeScrollBank();
      this.writeScreenLayout = this.video.writeScreenLayout();
      this.writeBackGround = this.video.writeBackGround(this.mem_cpu1);
      this.readBackGround = this.video.readBackGround(this.mem_cpu1);
      this.mem_gfx1 = new char['耀'];
      this.mem_gfx2 = new char[262144];
      this.mem_gfx3 = new char[262144];
      this.mem_prom = new char[1024];
      this.sndIrq = new SndIrq();
      this.ym = new YM2203(2, 3579545, (FMTimerHandler)null, this.sndIrq);
      this.soundLatch = new Register();
      this.bankaddress = 0;
      this.in = new InputPort[6];
      this.readBank = new Bankread();
      this.readProtection = new ReadProtection();
      this.writeBankSwitch = new WriteBankSwitch();
   }

   private GfxLayout charlayout() {
      int[] var2 = new int[]{4, 0};
      int[] var1 = new int[]{0, 1, 2, 3, 8, 9, 10, 11};
      int[] var3 = new int[]{0, 16, 32, 48, 64, 80, 96, 112};
      return new GfxLayout(8, 8, 2048, 2, var2, var1, var3, 128);
   }

   private GfxLayout spritelayout() {
      int[] var1 = new int[]{4, 0, 1048580, 1048576};
      int[] var2 = new int[]{0, 1, 2, 3, 8, 9, 10, 11, 256, 257, 258, 259, 264, 265, 266, 267};
      int[] var3 = new int[]{0, 16, 32, 48, 64, 80, 96, 112, 128, 144, 160, 176, 192, 208, 224, 240};
      return new GfxLayout(16, 16, 2048, 4, var1, var2, var3, 512);
   }

   public Emulator createEmulator(URL var1, String var2) {
      super.createEmulator(var1, var2);
      RomLoader var7 = new RomLoader();
      var7.setZip("blktiger");
      var7.setMemory(this.mem_cpu1);
      var7.loadROM("blktiger.5e", 0, '耀', -1460039902);
      var7.loadROM("blktiger.6e", 65536, 65536, 2079299304);
      var7.loadROM("blktiger.8e", 131072, 65536, 1082777943);
      var7.loadROM("blktiger.9e", 196608, 65536, -311757076);
      var7.loadROM("blktiger.10e", 262144, 65536, -1369852114);
      var7.setMemory(this.mem_cpu2);
      var7.loadROM("blktiger.1l", 0, '耀', 754270836);
      var7.setMemory(this.mem_gfx1);
      var7.loadROM("blktiger.2n", 0, '耀', 1880579448);
      var7.setMemory(this.mem_gfx2);
      var7.loadROM("blktiger.5b", 0, 65536, -1001240173);
      var7.loadROM("blktiger.4b", 65536, 65536, 2033371247);
      var7.loadROM("blktiger.9b", 131072, 65536, -599172806);
      var7.loadROM("blktiger.8b", 196608, 65536, 2128060706);
      var7.setMemory(this.mem_gfx3);
      var7.loadROM("blktiger.5a", 0, 65536, -487492552);
      var7.loadROM("blktiger.4a", 65536, 65536, 1607253287);
      var7.loadROM("blktiger.9a", 131072, 65536, -63714106);
      var7.loadROM("blktiger.8a", 196608, 65536, -196485631);
      var7.loadZip(var1);
      this.in[0] = new InputPort();
      this.in[1] = new InputPort();
      this.in[2] = new InputPort();
      this.in[3] = new InputPort();
      this.in[4] = new InputPort();
      this.in[5] = new InputPort();
      this.in[0].setBit(1, 4);
      this.in[0].setBit(2, 5);
      this.in[0].setBit(4, 0);
      this.in[0].setBit(8, 0);
      this.in[0].setBit(16, 0);
      this.in[0].setBit(32, 2);
      this.in[0].setBit(64, 1);
      this.in[0].setBit(128, 2);
      this.in[1].setBit(1, 9);
      this.in[1].setBit(2, 8);
      this.in[1].setBit(4, 11);
      this.in[1].setBit(8, 10);
      this.in[1].setBit(16, 12);
      this.in[1].setBit(32, 13);
      this.in[1].setBit(64, 0);
      this.in[1].setBit(128, 0);
      this.in[2].setBit(1, 137);
      this.in[2].setBit(2, 136);
      this.in[2].setBit(4, 139);
      this.in[2].setBit(8, 138);
      this.in[2].setBit(16, 140);
      this.in[2].setBit(32, 141);
      this.in[2].setBit(64, 0);
      this.in[2].setBit(128, 0);
      this.in[3].setDipName(7, 7, "Coun A");
      this.in[3].setDipName(56, 56, "Coin B");
      this.in[3].setDipName(64, 64, "Flip Screen");
      this.in[3].setService(128);
      this.in[4].setDipName(3, 3, "Lives");
      this.in[4].setDipName(28, 28, "Difficulty");
      this.in[4].setDipName(32, 32, "Demo Sounds");
      this.in[4].setDipName(64, 64, "Allow Continue");
      this.in[4].setDipName(128, 0, "Cabinet");
      this.in[5].setDipName(1, 1, "Freeze");
      this.mra1.setMR(0, 32767, 1);
      this.mra1.set('耀', '뿿', this.readBank);
      this.mra1.set('쀀', '쿿', this.readBackGround);
      this.mra1.setMR('퀀', '\uffff', 0);
      this.mwa1.setMW(0, '뿿', 1);
      this.mwa1.set('쀀', '쿿', this.writeBackGround);
      this.mwa1.set('퀀', '\ud7ff', this.writeVRAM);
      this.mwa1.set('\ud800', '\udbff', this.writePaletteRAM1);
      this.mwa1.set('\udc00', '\udfff', this.writePaletteRAM2);
      this.mwa1.setMW('\ue000', '\ufdff', 0);
      this.mwa1.setMW('︀', '\uffff', 0);
      this.mra2.setMR(0, 32767, 1);
      this.mra2.setMR('쀀', '쟿', 0);
      this.mra2.set('저', '저', this.soundLatch);
      this.mra2.set('\ue000', '\ue000', this.ym.ym2203_status_port_0_r());
      this.mra2.set('\ue001', '\ue001', this.ym.ym2203_read_port_0_r());
      this.mra2.set('\ue002', '\ue002', this.ym.ym2203_status_port_1_r());
      this.mra2.set('\ue003', '\ue003', this.ym.ym2203_read_port_1_r());
      this.mwa2.setMW(0, 32767, 1);
      this.mwa2.setMW('쀀', '쟿', 0);
      this.mwa2.set('\ue000', '\ue000', this.ym.ym2203_control_port_0_w());
      this.mwa2.set('\ue001', '\ue001', this.ym.ym2203_write_port_0_w());
      this.mwa2.set('\ue002', '\ue002', this.ym.ym2203_control_port_1_w());
      this.mwa2.set('\ue003', '\ue003', this.ym.ym2203_write_port_1_w());
      this.ior.set(0, 0, this.in[0]);
      this.ior.set(1, 1, this.in[1]);
      this.ior.set(2, 2, this.in[2]);
      this.ior.set(3, 3, this.in[3]);
      this.ior.set(4, 4, this.in[4]);
      this.ior.set(5, 5, this.in[5]);
      this.ior.set(7, 7, this.readProtection);
      this.iow.set(0, 0, this.soundLatch);
      this.iow.set(1, 1, this.writeBankSwitch);
      this.iow.set(8, 9, this.writeScrollX);
      this.iow.set(10, 11, this.writeScrollY);
      this.iow.set(13, 13, this.writeScrollBank);
      this.iow.set(14, 14, this.writeScreenLayout);
      GfxDecodeInfo var6 = new GfxDecodeInfo(this.mem_gfx1, 0, this.charlayout(), 768, 32);
      GfxDecodeInfo var8 = new GfxDecodeInfo(this.mem_gfx2, 0, this.spritelayout(), 0, 16);
      GfxDecodeInfo var5 = new GfxDecodeInfo(this.mem_gfx3, 0, this.spritelayout(), 512, 8);
      EmulatorProperties var3 = new EmulatorProperties();
      CpuDriver[] var4 = new CpuDriver[]{new CpuDriver(this.cpu1, 4000000, this.mra1, this.mwa1, this.ior, this.iow, this.interrupt, 1), new CpuDriver(this.cpu2, 3000000, this.mra2, this.mwa2, (InterruptHandler)null, 0)};
      var4[1].isAudioCpu = true;
      var3.setInputPorts(this.in);
      var3.setCpuDriver(var4);
      var3.setFPS(60);
      var3.setVBlankDuration(1500);
      var3.setTimeSlicesPerFrame(100);
      var3.setVideoDimensions(256, 256);
      var3.setVisibleArea(0, 255, 16, 239);
      var3.setGfxDecodeInfo(new GfxDecodeInfo[]{var6, var8, var5});
      var3.setPaletteLength(1024);
      var3.setColorTableLength(1024);
      var3.setDynamicPalette(true);
      var3.setVideoUpdater(this.video);
      var3.setSoundChip(this.ym);
      this.video.init(var3);
      this.emu.init(var3);
      new HighScoreParser(this.emu, this.mem_cpu1, 32, 0, 0, '큢', 1, '큨', 0, 1);
      return this.emu;
   }

   public class Bankread implements ReadHandler {
      public int read(int var1) {
         return Blktiger.this.mem_cpu1[Blktiger.this.bankaddress + (var1 - '耀')];
      }
   }

   public class ReadProtection implements ReadHandler {
      public int read(int var1) {
         return Blktiger.this.cpu1.m_d8;
      }
   }

   public class SndIrq implements FMIRQHandler {
      int curState = 0;

      public void irq(int var1, int var2) {
         Blktiger.this.cpu2.IRQ_SELF_ACK = false;
         Z80 var4 = Blktiger.this.cpu2;
         boolean var3;
         if(var2 != 0) {
            var3 = true;
         } else {
            var3 = false;
         }

         var4.interrupt(0, var3);
         Blktiger.this.cpu2.exec(1500);
      }
   }

   public class WriteBankSwitch implements WriteHandler {
      public void write(int var1, int var2) {
         Blktiger.this.bankaddress = 65536 + (var2 & 15) * 16384;
      }
   }
}
