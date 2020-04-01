package net.movegaga.jemu2.driver.arcade.bublbobl;

import net.movegaga.jemu2.driver.BaseDriver;

import java.net.URL;

import jef.cpu.z80.Z80;
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
import jef.sound.chip.fm.FMIRQHandler;
import jef.sound.chip.fm.FMTimerHandler;
import jef.sound.chip.ym2203.YM2203;
import jef.util.RomLoader;
import jef.video.GfxDecodeInfo;

public class Bublbobl extends BaseDriver {
   int[][] charLayout;
   Z80 cpu1 = new Z80();
   Z80 cpu2;
   Z80 cpu3;
   BasicEmulator emu = new BasicEmulator();
   InputPort[] in;
   InterruptHandler interrupt;
   char[] mem_cpu1 = new char[196608];
   char[] mem_cpu2;
   char[] mem_cpu3;
   char[] mem_gfx;
   char[] mem_prom;
   char[] mem_shared1;
   char[] mem_shared2;
   MemoryReadAddress mra1;
   MemoryReadAddress mra2;
   MemoryReadAddress mra3;
   MemoryWriteAddress mwa1;
   MemoryWriteAddress mwa2;
   MemoryWriteAddress mwa3;
   boolean nmiEnableSound;
   boolean nmiPending;
   Register soundLatch;
   VBublbobl video;
   YM2203 ym;

   public Bublbobl() {
      this.mra1 = new MemoryReadAddress(this.mem_cpu1);
      this.mwa1 = new MemoryWriteAddress(this.mem_cpu1);
      this.cpu2 = new Z80();
      this.mem_cpu2 = new char[65536];
      this.mra2 = new MemoryReadAddress(this.mem_cpu2);
      this.mwa2 = new MemoryWriteAddress(this.mem_cpu2);
      this.cpu3 = new Z80();
      this.mem_cpu3 = new char[65536];
      this.mra3 = new MemoryReadAddress(this.mem_cpu3);
      this.mwa3 = new MemoryWriteAddress(this.mem_cpu3);
      this.video = new VBublbobl(this);
      this.mem_gfx = new char[524288];
      this.mem_prom = new char[256];
      this.ym = new YM2203(1, 3000000, (FMTimerHandler)null, new SndIrq());
      this.soundLatch = new Register();
      this.in = new InputPort[5];
      this.interrupt = this.emu.irq0_line_hold();
      this.mem_shared1 = new char[6144];
      this.mem_shared2 = new char[768];
      int[] var4 = new int[]{16384};
      int[] var3 = new int[]{2097152, 2097156, 0, 4};
      int[] var1 = new int[]{3, 2, 1, 0, 11, 10, 9, 8};
      int[] var2 = new int[]{0, 16, 32, 48, 64, 80, 96, 112};
      this.charLayout = new int[][]{{8}, {8}, var4, {4}, var3, var1, var2, {128}};
   }

   private void initBoblBobl() {
      this.modPage(3, '驱', 0);
      this.modPage(3, '驲', 0);
      this.modPage(3, '驳', 0);
      this.modPage(3, '꒯', 0);
      this.modPage(3, '꒰', 0);
      this.modPage(3, '꒱', 0);
      this.modPage(3, 'ꕝ', 0);
      this.modPage(3, 'ꕞ', 0);
      this.modPage(3, 'ꕟ', 0);
      this.modPage(3, '땡', 0);
      this.modPage(3, '땢', 0);
      this.modPage(3, '땣', 0);
      this.initBublBobl();
   }

   private void initBublBobl() {
      for(int var1 = 0; var1 < 16384; ++var1) {
         this.mem_cpu1['耀' + var1] = this.mem_cpu1[65536 + var1];
      }

   }

   private void initInput() {
      this.in[0] = new InputPort();
      this.in[0].setBits(1, 0);
      this.in[0].setBits(2, 2);
      this.in[0].setService(4);
      this.in[0].setBits(8, 8);
      this.in[0].setBits(48, 48);
      this.in[0].setBits(192, 192);
      this.in[1] = new InputPort();
      this.in[1].setBits(3, 3);
      this.in[1].setBits(12, 12);
      this.in[1].setBits(48, 48);
      this.in[1].setBits(192, 0);
      this.in[2] = new InputPort();
      this.in[2].setBit(1, 8);
      this.in[2].setBit(2, 9);
      this.in[2].setBit(4, 2);
      this.in[2].setBit(8, 1);
      this.in[2].setBit(16, 13);
      this.in[2].setBit(32, 12);
      this.in[2].setBit(64, 4);
      this.in[2].setBits(128, 128);
      this.in[3] = new InputPort();
      this.in[3].setBit(1, 136);
      this.in[3].setBit(2, 137);
      this.in[3].setBit(4, 15);
      this.in[3].setBit(8, 16);
      this.in[3].setBit(16, 141);
      this.in[3].setBit(32, 140);
      this.in[3].setBit(64, 5);
      this.in[3].setBits(128, 128);
      this.in[4] = new InputPort();
   }

   private void initInputTokio() {
      this.in[0] = new InputPort();
      this.in[0].setBits(1, 0);
      this.in[0].setBits(2, 2);
      this.in[0].setService(4);
      this.in[0].setBits(8, 8);
      this.in[0].setBits(48, 48);
      this.in[0].setBits(192, 192);
      this.in[1] = new InputPort();
      this.in[1].setBits(3, 2);
      this.in[1].setBits(12, 8);
      this.in[1].setBits(48, 48);
      this.in[1].setBits(64, 64);
      this.in[1].setBits(128, 0);
      this.in[2] = new InputPort();
      this.in[2].setBit(1, 15);
      this.in[2].setBit(2, 16);
      this.in[2].setBit(4, 1);
      this.in[2].setBit(8, 2);
      this.in[2].setBits(240, 240);
      this.in[3] = new InputPort();
      this.in[3].setBit(1, 8);
      this.in[3].setBit(2, 9);
      this.in[3].setBit(4, 11);
      this.in[3].setBit(8, 10);
      this.in[3].setBit(16, 13);
      this.in[3].setBit(32, 12);
      this.in[3].setBit(64, 4);
      this.in[3].setBits(128, 128);
      this.in[4] = new InputPort();
      this.in[4].setBit(1, 136);
      this.in[4].setBit(2, 137);
      this.in[4].setBit(4, 139);
      this.in[4].setBit(8, 138);
      this.in[4].setBit(16, 141);
      this.in[4].setBit(32, 140);
      this.in[4].setBit(64, 5);
      this.in[4].setBits(128, 128);
   }

   private void initMemMap() {
      this.mra1.setMR(0, 32767, 1);
      this.mra1.setMR('耀', '뿿', 3);
      this.mra1.setMR('쀀', '\udfff', 0);
      this.mra1.set('\ue000', '\uf7ff', new ReadSharedRAM1('\ue000'));
      this.mra1.setMR('\uf800', '刺', 0);
      this.mra1.set('ﰀ', '\ufeff', new ReadSharedRAM2('ﰀ'));
      this.mra1.set('\uff00', '\uff00', this.in[0]);
      this.mra1.set('！', '！', this.in[1]);
      this.mra1.set('＂', '＂', this.in[2]);
      this.mra1.set('＃', '＃', this.in[3]);
      this.mwa1.setMW(0, '뿿', 1);
      this.mwa1.setMW('쀀', '\udcff', 0);
      this.mwa1.setMW('\udd00', '\udfff', 0);
      this.mwa1.set('\ue000', '\uf7ff', new WriteSharedRAM1('\ue000'));
      this.mwa1.set('\uf800', '刺', this.video.writePaletteRAM());
      this.mwa1.set('切', '切', new WriteSoundCommand());
      this.mwa1.setMW('婢', '婢', 2);
      this.mwa1.set('ﬀ', 'ﬀ', new WriteNmiTrigger());
      this.mwa1.set('נּ', 'נּ', new WriteBankSwitch());
      this.mwa1.set('ﰀ', '\ufeff', new WriteSharedRAM2('ﰀ'));
      this.mra2.setMR(0, 32767, 1);
      this.mra2.set('\ue000', '\uf7ff', new ReadSharedRAM1('\ue000'));
      this.mra2.setMR('\uf800', '刺', 0);
      this.mra2.set('ﰀ', '\ufeff', new ReadSharedRAM2('ﰀ'));
      this.mwa2.setMW(0, 32767, 1);
      this.mwa2.set('\ue000', '\uf7ff', new WriteSharedRAM1('\ue000'));
      this.mwa2.set('\uf800', '刺', this.video.writePaletteRAM());
      this.mwa2.set('ﰀ', '\ufeff', new WriteSharedRAM2('ﰀ'));
      this.mra3.setMR(0, 32767, 1);
      this.mra3.setMR('耀', '迿', 0);
      this.mra3.set('退', '退', this.ym.ym2203_status_port_0_r());
      this.mra3.set('送', '送', this.ym.ym2203_read_port_0_r());
      this.mra3.set('뀀', '뀀', this.soundLatch);
      this.mra3.setMR('뀁', '뀁', 2);
      this.mra3.setMR('\ue000', '\uefff', 1);
      this.mwa3.setMW(0, 32767, 1);
      this.mwa3.setMW('耀', '迿', 0);
      this.mwa3.set('退', '退', this.ym.ym2203_control_port_0_w());
      this.mwa3.set('送', '送', this.ym.ym2203_write_port_0_w());
      this.mwa3.setMW('뀀', '뀀', 2);
      this.mwa3.set('뀁', '뀁', new WriteSoundNmiEnable());
      this.mwa3.set('뀂', '뀂', new WriteSoundNmiDisable());
      this.mwa3.setMW('\ue000', '\uefff', 1);
   }

   private void initMemMapTokio() {
      this.mra1.setMR(0, 32767, 1);
      this.mra1.setMR('耀', '뿿', 3);
      this.mra1.setMR('쀀', '\udfff', 0);
      this.mra1.set('\ue000', '\uf7ff', new ReadSharedRAM1('\ue000'));
      this.mra1.setMR('\uf800', '刺', 0);
      this.mra1.set('糖', '糖', this.in[0]);
      this.mra1.set('宅', '宅', this.in[1]);
      this.mra1.set('洞', '洞', this.in[2]);
      this.mra1.set('暴', '暴', this.in[3]);
      this.mra1.set('輻', '輻', this.in[4]);
      this.mra1.setMR('行', '\ufdff', 0);
      this.mra1.set('︀', '︀', new TokioHack());
      this.mra1.setMR('︁', '\uffff', 0);
      this.mwa1.setMW(0, '뿿', 1);
      this.mwa1.setMW('쀀', '\udcff', 0);
      this.mwa1.setMW('\udd00', '\udfff', 0);
      this.mwa1.set('\ue000', '\uf7ff', new WriteSharedRAM1('\ue000'));
      this.mwa1.set('\uf800', '刺', this.video.writePaletteRAM());
      this.mwa1.setMW('切', '切', 2);
      this.mwa1.setMW('度', '奔', 0);
      this.mwa1.set('婢', '婢', new WriteBankSwitchTokio());
      this.mwa1.setMW('嬨', '\ufaff', 0);
      this.mwa1.set('ﬀ', 'ﬀ', new WriteVideoControlTokio());
      this.mwa1.setMW('ﬁ', 'ﭿ', 0);
      this.mwa1.set('ﮀ', 'ﮀ', new WriteNmiTrigger());
      this.mwa1.setMW('ﮁ', 'ﯿ', 0);
      this.mwa1.set('ﰀ', 'ﰀ', new WriteSoundCommand());
      this.mwa1.setMW('ﰀ', '\ufdff', 0);
      this.mwa1.setMW('︀', '︀', 2);
      this.mwa1.setMW('︁', '\uffff', 0);
      this.mra2.setMR(0, 32767, 1);
      this.mra2.set('耀', '響', new ReadSharedRAM1('耀'));
      this.mwa2.setMW(0, 32767, 1);
      this.mwa2.set('耀', '響', new WriteSharedRAM1('耀'));
      this.mra3.setMR(0, 32767, 1);
      this.mra3.setMR('耀', '迿', 0);
      this.mra3.set('退', '退', this.soundLatch);
      this.mra3.set('뀀', '뀀', this.ym.ym2203_status_port_0_r());
      this.mra3.set('뀁', '뀁', this.ym.ym2203_read_port_0_r());
      this.mra3.setMR('\ue000', '\uefff', 1);
      this.mwa3.setMW(0, 32767, 1);
      this.mwa3.setMW('耀', '迿', 0);
      this.mwa3.set('ꀀ', 'ꀀ', new WriteSoundNmiDisable());
      this.mwa3.set('ꠀ', 'ꠀ', new WriteSoundNmiEnable());
      this.mwa3.set('뀀', '뀀', this.ym.ym2203_control_port_0_w());
      this.mwa3.set('뀁', '뀁', this.ym.ym2203_write_port_0_w());
      this.mwa3.setMW('\ue000', '\uefff', 1);
   }

   private void loadRom(URL var1) {
      RomLoader var2 = new RomLoader();
      var2.setZip("boblbobl");
      var2.setMemory(this.mem_cpu1);
      var2.loadROM("bb3", 0, '耀', 33036598);
      var2.loadROM("bb5", 65536, '耀', 319917745);
      var2.loadROM("bb4", 98304, '耀', -1344628264);
      var2.setMemory(this.mem_cpu2);
      var2.loadROM("a78-08.37", 0, '耀', -1374576517);
      var2.setMemory(this.mem_cpu3);
      var2.loadROM("a78-07.46", 0, '耀', 1335502568);
      var2.setMemory(this.mem_gfx);
      var2.setInvert(true);
      var2.loadROM("a78-09.12", 0, '耀', 540380194);
      var2.loadROM("a78-10.13", '耀', '耀', -1828624215);
      var2.loadROM("a78-11.14", 65536, '耀', -1754012398);
      var2.loadROM("a78-12.15", 98304, '耀', -800762725);
      var2.loadROM("a78-13.16", 131072, '耀', -793823803);
      var2.loadROM("a78-14.17", 163840, '耀', 2069064104);
      var2.loadROM("a78-15.30", 262144, '耀', 1801561107);
      var2.loadROM("a78-16.31", 294912, '耀', -1253495401);
      var2.loadROM("a78-17.32", 327680, '耀', -694721835);
      var2.loadROM("a78-18.33", 360448, '耀', -1625015448);
      var2.loadROM("a78-19.34", 393216, '耀', 1726563212);
      var2.loadROM("a78-20.35", 425984, '耀', -1627888723);
      var2.setInvert(false);
      var2.setMemory(this.mem_prom);
      var2.loadROM("a71-25.41", 0, 256, 755991877);
      var2.loadZip(var1);
   }

   private void loadRomTokio(URL var1) {
      RomLoader var2 = new RomLoader();
      var2.setZip("tokiob");
      var2.setMemory(this.mem_cpu1);
      var2.loadROM("2", 0, '耀', -175918609);
      var2.loadROM("3", 65536, '耀', 1775947588);
      var2.loadROM("a71-04.256", 98304, '耀', -1599812082);
      var2.loadROM("a71-05.256", 131072, '耀', 1839249733);
      var2.loadROM("6", 163840, '耀', 345041243);
      var2.setMemory(this.mem_cpu2);
      var2.loadROM("a71-01.256", 0, '耀', 141018887);
      var2.setMemory(this.mem_cpu3);
      var2.loadROM("a71-07.256", 0, '耀', -224867205);
      var2.setMemory(this.mem_gfx);
      var2.setInvert(true);
      var2.loadROM("a71-08.256", 0, '耀', 70888211);
      var2.loadROM("a71-09.256", '耀', '耀', -306982145);
      var2.loadROM("a71-10.256", 65536, '耀', 1777371276);
      var2.loadROM("a71-11.256", 98304, '耀', 1256225841);
      var2.loadROM("a71-12.256", 131072, '耀', 1064032006);
      var2.loadROM("a71-13.256", 163840, '耀', -221697366);
      var2.loadROM("a71-14.256", 196608, '耀', -982206542);
      var2.loadROM("a71-15.256", 229376, '耀', 316178047);
      var2.loadROM("a71-16.256", 262144, '耀', 198063542);
      var2.loadROM("a71-17.256", 294912, '耀', -556113017);
      var2.loadROM("a71-18.256", 327680, '耀', 856480215);
      var2.loadROM("a71-19.256", 360448, '耀', -62182944);
      var2.loadROM("a71-20.256", 393216, '耀', 1705816677);
      var2.loadROM("a71-21.256", 425984, '耀', 869132722);
      var2.loadROM("a71-22.256", 458752, '耀', -73864512);
      var2.loadROM("a71-23.256", 491520, '耀', 817710765);
      var2.setInvert(false);
      var2.setMemory(this.mem_prom);
      var2.loadROM("a71-25.bin", 0, 256, 755991877);
      var2.loadZip(var1);
   }

   private void modPage(int var1, int var2, int var3) {
      this.mem_cpu1[var2 - '耀' + 65536 + var1 * 16384] = (char)var3;
   }

   public EmulatorProperties createBoblboblEP(URL var1) {
      this.loadRom(var1);
      this.initInput();
      this.initMemMap();
      EmulatorProperties var2 = new EmulatorProperties();
      var2.addCpuDriver(new CpuDriver(this.cpu1, 6000000, this.mra1, this.mwa1, this.interrupt, 1));
      var2.addCpuDriver(new CpuDriver(this.cpu2, 6000000, this.mra2, this.mwa2, this.interrupt, 1));
      var2.addCpuDriver(new CpuDriver(this.cpu3, 3000000, this.mra3, this.mwa3, (InterruptHandler)null, 0));
      var2.addSoundChip(this.ym);
      var2.setTimeSlicesPerFrame(100);
      var2.setDynamicPalette(true);
      var2.setVideoDimensions(256, 256);
      var2.setVisibleArea(0, 255, 16, 239);
      var2.setGfxDecodeInfo(new GfxDecodeInfo(this.mem_gfx, 0, this.charLayout, 0, 16));
      var2.setPaletteLength(256);
      var2.setVideoUpdater(this.video);
      var2.setInputPorts(this.in);
      return var2;
   }

   public Emulator createEmulator(URL var1, String var2) {
      super.createEmulator(var1, var2);
      EmulatorProperties var3 = null;
      if(var2.equals("boblbobl")) {
         var3 = this.createBoblboblEP(var1);
         this.initBoblBobl();
         (new HighScoreParser(this.emu, this.mem_cpu1, 96, 123, 0, '쵆', 64, '컆', 0, 1)).setValidator(new Validate());
      } else if(var2.equals("tokiob")) {
         var3 = this.createTokioEP(var1);
         this.video.bublbobl_video_enable = 1;
         var3.setRotation(1);
      }

      this.video.init(var3);
      this.emu.init(var3);
      return this.emu;
   }

   public EmulatorProperties createTokioEP(URL var1) {
      this.loadRomTokio(var1);
      this.initInputTokio();
      this.initMemMapTokio();
      EmulatorProperties var2 = new EmulatorProperties();
      var2.addCpuDriver(new CpuDriver(this.cpu1, 6000000, this.mra1, this.mwa1, this.interrupt, 1));
      var2.addCpuDriver(new CpuDriver(this.cpu2, 6000000, this.mra2, this.mwa2, this.interrupt, 1));
      var2.addCpuDriver(new CpuDriver(this.cpu3, 3000000, this.mra3, this.mwa3, (InterruptHandler)null, 0));
      var2.addSoundChip(this.ym);
      var2.setTimeSlicesPerFrame(100);
      var2.setDynamicPalette(true);
      var2.setVideoDimensions(256, 256);
      var2.setVisibleArea(0, 255, 16, 239);
      var2.setGfxDecodeInfo(new GfxDecodeInfo(this.mem_gfx, 0, this.charLayout, 0, 16));
      var2.setPaletteLength(256);
      var2.setVideoUpdater(this.video);
      var2.setInputPorts(this.in);
      return var2;
   }

   void nmiCallBack(int var1) {
      if(this.nmiEnableSound) {
         this.cpu3.interrupt(1, true);
      } else {
         this.nmiPending = true;
      }

   }

   public class ReadSharedRAM1 implements ReadHandler {
      public int startofs = 0;

      public ReadSharedRAM1(int var2) {
         this.startofs = var2;
      }

      public int read(int var1) {
         return Bublbobl.this.mem_shared1[var1 - this.startofs];
      }
   }

   public class ReadSharedRAM2 implements ReadHandler {
      public int startofs = 0;

      public ReadSharedRAM2(int var2) {
         this.startofs = var2;
      }

      public int read(int var1) {
         return Bublbobl.this.mem_shared2[var1 - this.startofs];
      }
   }

   public class SndIrq implements FMIRQHandler {
      int curState = 0;

      public void irq(int var1, int var2) {
         Bublbobl.this.cpu3.IRQ_SELF_ACK = false;
         Z80 var4 = Bublbobl.this.cpu3;
         boolean var3;
         if(var2 != 0) {
            var3 = true;
         } else {
            var3 = false;
         }

         var4.interrupt(0, var3);
         this.curState = var2;
      }
   }

   public class TokioHack implements ReadHandler {
      public int read(int var1) {
         return 191;
      }
   }

   public class WriteBankSwitch implements WriteHandler {
      public void write(int var1, int var2) {
         Bublbobl.this.mra1.setBankAddress(1, 65536 + ((var2 ^ 4) & 7) * 16384);
         Bublbobl.this.video.bublbobl_video_enable = var2 & 64;
      }
   }

   public class WriteBankSwitchTokio implements WriteHandler {
      public void write(int var1, int var2) {
         Bublbobl.this.mra1.setBankAddress(1, 65536 + (var2 & 7) * 16384);
      }
   }

   public class WriteNmiTrigger implements WriteHandler {
      public void write(int var1, int var2) {
         Bublbobl.this.cpu2.interrupt(1, true);
      }
   }

   public class WriteSharedRAM1 implements WriteHandler {
      public int offset = 0;

      public WriteSharedRAM1(int var2) {
         this.offset = var2;
      }

      public void write(int var1, int var2) {
         Bublbobl.this.mem_shared1[var1 - this.offset] = (char)var2;
      }
   }

   public class WriteSharedRAM2 implements WriteHandler {
      public int startofs = 0;

      public WriteSharedRAM2(int var2) {
         this.startofs = var2;
      }

      public void write(int var1, int var2) {
         Bublbobl.this.mem_shared2[var1 - this.startofs] = (char)var2;
      }
   }

   public class WriteSoundCommand implements WriteHandler {
      public void write(int var1, int var2) {
         Bublbobl.this.soundLatch.write(var1, var2);
         Bublbobl.this.nmiCallBack(var2);
      }
   }

   public class WriteSoundNmiDisable implements WriteHandler {
      public void write(int var1, int var2) {
         Bublbobl.this.nmiEnableSound = false;
      }
   }

   public class WriteSoundNmiEnable implements WriteHandler {
      public void write(int var1, int var2) {
         Bublbobl.this.nmiEnableSound = true;
         if(Bublbobl.this.nmiPending) {
            Bublbobl.this.cpu3.interrupt(1, true);
            Bublbobl.this.nmiPending = false;
         }

      }
   }

   public class WriteVideoControlTokio implements WriteHandler {
      public void write(int var1, int var2) {
      }
   }
}
