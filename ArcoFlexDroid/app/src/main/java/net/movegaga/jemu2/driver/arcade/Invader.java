package net.movegaga.jemu2.driver.arcade;

import java.lang.reflect.Array;
import java.net.URL;

import jef.cpu.I8080;
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
import jef.map.WriteHandler;
import jef.util.RomLoader;
import jef.video.BitMap;
import jef.video.BitMapImpl;
import jef.video.VideoRenderer;

public class Invader implements InterruptHandler, VideoRenderer, WriteHandler {
   static final int CPU_CLOCK_SPEED = 2000000;
   static final int CPU_IRQ_PER_FRAME = 2;
   public static final int SCREEN_FPS = 60;
   public static final int SCREEN_HEIGHT = 256;
   public static final int SCREEN_WIDTH = 224;
   URL base_URL;
   BitMapImpl bitmap;
   int count = 0;
   int[][] gfx;
   InputPort[] input;
   Emulator m = new BasicEmulator();
   char[] memory = new char[65536];
   int[] pal;
   int[] pixels;
   int shift_amount = 0;
   int shift_data1 = 0;
   int shift_data2 = 0;

   public Invader() {
      int[] var1 = new int[]{0, 16777215};
      this.pal = var1;
      this.gfx = (int[][])Array.newInstance(Integer.TYPE, new int[]{256, 8});
   }

   private IOReadPort createIOReadPort() {
      IOReadPort var1 = new IOReadPort();
      var1.set(1, 1, this.input[0]);
      var1.set(2, 2, this.input[1]);
      var1.set(3, 3, new ShiftDataR());
      return var1;
   }

   private IOWritePort createIOWritePort() {
      IOWritePort var1 = new IOWritePort();
      var1.set(2, 2, new ShiftAmountW());
      var1.set(4, 4, new ShiftDataW());
      return var1;
   }

   private InputPort[] createInputPorts() {
      InputPort[] var1 = new InputPort[]{new InputPort(), new InputPort()};
      var1[0].setBit(1, 255, 1);
      var1[0].setBit(2, 0, 5);
      var1[0].setBit(4, 0, 4);
      var1[0].setBit(16, 0, 12);
      var1[0].setBit(32, 0, 8);
      var1[0].setBit(64, 0, 9);
      var1[0].setBit(128, 255, 0);
      var1[1].setDipName(3, 0, "Lives");
      var1[1].setDipSetting(0, "3");
      var1[1].setDipSetting(1, "4");
      var1[1].setDipSetting(2, "5");
      var1[1].setDipSetting(3, "6");
      var1[1].setBit(4, 0, 15);
      var1[1].setDipName(8, 0, "Bonus Live");
      var1[1].setDipSetting(8, "1000");
      var1[1].setDipSetting(0, "1500");
      var1[1].setBit(16, 0, 140);
      var1[1].setBit(32, 0, 136);
      var1[1].setBit(64, 0, 137);
      var1[1].setDipName(128, 0, "Coport Info");
      var1[1].setDipSetting(128, "Off");
      var1[1].setDipSetting(0, "on");
      return var1;
   }

   private MemoryReadAddress createMemoryReadAddress() {
      MemoryReadAddress var1 = new MemoryReadAddress(this.memory);
      var1.setMR(0, 8191, 1);
      var1.setMR(8192, 16383, 0);
      var1.setMR(16384, 25599, 1);
      return var1;
   }

   private MemoryWriteAddress createMemoryWriteAddress() {
      MemoryWriteAddress var1 = new MemoryWriteAddress(this.memory[0]);
      var1.setMW(0, 8191, 1);
      var1.setMW(8192, 9215, 0);
      var1.set(9216, 16383, this);
      var1.setMW(16384, 25599, 1);
      return var1;
   }

   private void loadROMs() {
      RomLoader var1 = new RomLoader();
      var1.setZip("invaders");
      var1.setMemory(this.memory);
      var1.loadROM("invaders.h", 0, 2048, 1934580440);
      var1.loadROM("invaders.g", 2048, 2048, 1811597898);
      var1.loadROM("invaders.f", 4096, 2048, 214871446);
      var1.loadROM("invaders.e", 6144, 2048, 350566576);
      var1.loadZip(this.base_URL);
   }

   public CpuDriver[] createCpuDriver() {
      return new CpuDriver[]{new CpuDriver(new I8080(), 2000000, this.createMemoryReadAddress(), this.createMemoryWriteAddress(), this.createIOReadPort(), this.createIOWritePort(), this, 2)};
   }

   public Emulator createEmulator() {
      this.base_URL = this.getClass().getClassLoader().getResource(".");
      this.bitmap = new BitMapImpl(224, 256);
      this.pixels = this.bitmap.getPixels();

      for(int var1 = 0; var1 < 256; ++var1) {
         this.gfx[var1][7] = this.pal[var1 >> 7];
         this.gfx[var1][6] = this.pal[(var1 & 64) >> 6];
         this.gfx[var1][5] = this.pal[(var1 & 32) >> 5];
         this.gfx[var1][4] = this.pal[(var1 & 16) >> 4];
         this.gfx[var1][3] = this.pal[(var1 & 8) >> 3];
         this.gfx[var1][2] = this.pal[(var1 & 4) >> 2];
         this.gfx[var1][1] = this.pal[(var1 & 2) >> 1];
         this.gfx[var1][0] = this.pal[var1 & 1];
      }

      this.loadROMs();
      this.input = this.createInputPorts();
      EmulatorProperties var2 = new EmulatorProperties(this.createCpuDriver(), 224, 256, this);
      var2.setFPS(60);
      var2.setInputPorts(this.input);
      this.m.init(var2);
      return this.m;
   }

   public int irq() {
      ++this.count;
      byte var1;
      if((this.count & 1) == 1) {
         var1 = 0;
      } else {
         var1 = 1;
      }

      return var1;
   }

   public BitMap renderVideo() {
      return this.bitmap;
   }

   public void renderVideoPost() {
   }

   public void write(int var1, int var2) {
      this.memory[var1] = (char)var2;
      int var3 = 7167 - (var1 - 9216);

      for(var1 = 0; var1 < 8; ++var1) {
         this.pixels[((var3 & 31) * 8 + var1) * 224 + (223 - (var3 >> 5))] = this.pal[this.gfx[var2][7 - var1] & 1];
      }

   }

   public class ShiftAmountW implements WriteHandler {
      public void write(int var1, int var2) {
         Invader.this.shift_amount = var2;
      }
   }

   public class ShiftDataR implements ReadHandler {
      public int read(int var1) {
         return (Invader.this.shift_data1 << 8 | Invader.this.shift_data2) << (Invader.this.shift_amount & 7) >> 8 & 255;
      }
   }

   public class ShiftDataW implements WriteHandler {
      public void write(int var1, int var2) {
         Invader.this.shift_data2 = Invader.this.shift_data1;
         Invader.this.shift_data1 = var2;
      }
   }
}
