package net.movegaga.jemu2.driver.arcade.snowbros;

import net.movegaga.jemu2.driver.BaseDriver;
import net.movegaga.jemu2.driver.Driver;

import java.net.URL;

import jef.cpu.Cpu;
import jef.cpu.m68000.jM68k;
import jef.cpuboard.CpuDriver;
import jef.machine.BasicEmulator;
import jef.machine.Emulator;
import jef.machine.EmulatorProperties;
import jef.map.InputPort;
import jef.map.InterruptHandler;
import jef.map.MemoryMap;
import jef.map.ReadHandler;
import jef.util.ZipLoader;
import jef.video.GfxDecodeInfo;
import jef.video.GfxLayout;
import jef.video.VideoConstants;

public class Snowbros extends BaseDriver implements Driver, VideoConstants {
   Emulator emu = new BasicEmulator();
   InputPort[] in;
   InterruptHandler interrupt1;
   Cpu m68000 = new jM68k();
   MemoryMap map;
   char[] mem_cpu1 = new char[262144];
   char[] mem_gfx1;
   char[] mem_gfx2;
   VSnowbros video;

   public Snowbros() {
      this.map = new MemoryMap(this.mem_cpu1);
      this.interrupt1 = new Interrupt1();
      this.video = new VSnowbros();
      this.mem_gfx1 = new char[524288];
      this.mem_gfx2 = new char[4194304];
      this.in = new InputPort[5];
   }

   public Emulator createEmulator(URL var1, String var2) {
      super.createEmulator(var1, var2);
      short var4 = 512;
      short var5 = 8191;
      int var3 = 8000000;
      ZipLoader var11;
      if(var2.equals("snowbros")) {
         var11 = new ZipLoader("snowbros");
         var11.queue((char[])this.mem_gfx1, 384854842, 0, 524288);
         var11.queue((char[])this.mem_cpu1, 1218043343, 0, 131072, 2);
         var11.queue((char[])this.mem_cpu1, -1389294273, 1, 131072, 2);
         var11.load();
      } else if(var2.equals("snowbro3")) {
         var11 = new ZipLoader("snowbro3");
         var11.queue((char[])this.mem_cpu1, 432095229, 0, 131072, 2);
         var11.queue((char[])this.mem_cpu1, 1060305429, 1, 131072, 2);
         var11.queue((char[])this.mem_gfx1, 100983685, 0, 524288);
         var11.queue((char[])this.mem_gfx2, 1249499724, 0, 2097152);
         var11.queue(this.mem_gfx2, 2051367332, 2097152, 2097152);
         var11.load();
         char[] var12 = new char[this.mem_cpu1.length];

         for(var3 = 0; var3 < this.mem_cpu1.length; ++var3) {
            var12[var3] = this.mem_cpu1[this.bitswap24(var3, 23, 22, 21, 20, 19, 18, 17, 16, 15, 14, 13, 12, 11, 10, 9, 8, 7, 6, 5, 3, 4, 1, 2, 0)];
         }

         System.arraycopy(var12, 0, this.mem_cpu1, 0, this.mem_cpu1.length);
         var12 = (char[])null;
         this.video.mode = 1;
         var4 = 1024;
         var5 = 8703;
         var3 = 16000000;
      }

      EmulatorProperties var14 = new EmulatorProperties();
      this.in[0] = new InputPort();
      this.in[1] = new InputPort();
      this.in[2] = new InputPort();
      this.in[3] = new InputPort();
      this.in[4] = new InputPort();
      this.in[0].setBit(1, 10);
      this.in[0].setBit(2, 11);
      this.in[0].setBit(4, 8);
      this.in[0].setBit(8, 9);
      this.in[0].setBit(16, 12);
      this.in[0].setBit(32, 13);
      this.in[0].setBit(64, 14);
      this.in[0].setBit(128, 0, 0);
      this.in[1].setBit(1, 138);
      this.in[1].setBit(2, 139);
      this.in[1].setBit(4, 136);
      this.in[1].setBit(8, 137);
      this.in[1].setBit(16, 140);
      this.in[1].setBit(32, 141);
      this.in[1].setBit(64, 142);
      this.in[1].setBit(128, 0, 0);
      this.in[2].setBit(1, 4);
      this.in[2].setBit(2, 5);
      this.in[2].setBit(4, 1);
      this.in[2].setBit(8, 2);
      this.in[2].setBit(16, 0);
      this.in[2].setBit(32, 15);
      this.in[2].setBit(64, 2);
      this.in[2].setBit(128, 0);
      this.in[3].setDipName(1, 1, "Country (Affects Coinage)");
      this.in[3].setDipName(2, 2, "Flip Screen");
      this.in[3].setService(4);
      this.in[3].setDipName(8, 8, "Demo Sounds");
      this.in[3].setDipName(48, 48, "Coin A America/Europe");
      this.in[3].setDipName(192, 192, "Coin B America/Europe");
      this.in[4].setDipName(3, 3, "Difficulty");
      this.in[4].setDipName(12, 12, "Bonus Life");
      this.in[4].setDipName(48, 48, "Lives");
      this.in[4].setDipName(64, 64, "Invulnerability");
      this.in[4].setDipName(128, 128, "Allow Continue");
      MemoryMap var13 = this.map;
      var13.setMR(0, 262143, 1);
      var13.setMR(1048576, 1064959, 0);
      var13.set(3145728, 3145729, new ReadSound());
      var13.set(5242880, 5242885, new ReadInput());
      var13.set(6291456, 6291456 + var4 - 1, this.video.readPaletteRAM());
      var13.setMR(7340032, 7340032 + var5, 0);
      var13 = this.map;
      var13.setMW(0, 262143, 1);
      var13.setMW(1048576, 1064959, 0);
      var13.setMW(2097152, 2097153, 2);
      var13.setMW(3145728, 3145729, 2);
      var13.set(6291456, 6291456 + var4 - 1, this.video.writePaletteRAM());
      var13.setMW(7340032, 7340032 + var5, 0);
      var13.setMW(8388608, 8388609, 2);
      var13.setMW(9437184, 9437185, 2);
      var13.setMW(10485760, 10485761, 2);
      int[] var9 = new int[]{0, 1, 2, 3};
      int[] var8 = new int[]{0, 4, 8, 12, 16, 20, 24, 28, 256, 260, 264, 268, 272, 276, 280, 284};
      int[] var10 = new int[]{0, 32, 64, 96, 128, 160, 192, 224, 512, 544, 576, 608, 640, 672, 704, 736};
      int[] var15 = new int[]{0, 1, 2, 3, 8, 9, 10, 11};
      int[] var6 = new int[]{0, 4, 16, 20, 32, 36, 48, 52, 512, 516, 528, 532, 544, 548, 560, 564};
      int[] var7 = new int[]{0, 64, 128, 192, 256, 320, 384, 448, 1024, 1088, 1152, 1216, 1280, 1344, 1408, 1472};
      GfxLayout var19 = new GfxLayout(16, 16, 4096, 4, var9, var8, var10, 1024);
      GfxLayout var16 = new GfxLayout(16, 16, 16384, 8, var15, var6, var7, 2048);
      GfxDecodeInfo var18 = new GfxDecodeInfo(this.mem_gfx1, 0, var19, 0, 16);
      GfxDecodeInfo var17 = new GfxDecodeInfo(this.mem_gfx2, 0, var16, 0, 2);
      var14.setCpuDriver(new CpuDriver[]{new CpuDriver(this.m68000, var3, this.map, this.map, this.interrupt1, 3)});
      var14.setFPS(60);
      var14.setVBlankDuration(1000);
      var14.setTimeSlicesPerFrame(1);
      var14.setVideoWidth(256);
      var14.setVideoHeight(256);
      var14.setVisibleArea(0, 255, 16, 239);
      var14.setGfxDecodeInfo(new GfxDecodeInfo[]{var18, var17});
      var14.setColorTableLength(var4);
      var14.setPaletteLength(var4);
      var14.setVideoUpdater(this.video);
      var14.setInputPorts(this.in);
      var14.setDynamicPalette(true);
      this.video.setRegions(this.map);
      this.video.init(var14);
      this.emu.init(var14);
      return this.emu;
   }

   class Interrupt1 implements InterruptHandler {
      int irq_count = 2;

      public int irq() {
         this.irq_count = (this.irq_count + 1) % 3;
         return 4 - this.irq_count;
      }
   }

   class ReadInput implements ReadHandler {
      public int read(int var1) {
         switch(var1) {
         case 0:
            var1 = Snowbros.this.in[0].read(var1);
            break;
         case 1:
            var1 = Snowbros.this.in[3].read(var1);
            break;
         case 2:
            var1 = Snowbros.this.in[1].read(var1);
            break;
         case 3:
            var1 = Snowbros.this.in[4].read(var1);
            break;
         case 4:
            var1 = Snowbros.this.in[2].read(var1);
            break;
         default:
            var1 = 0;
         }

         return var1;
      }
   }

   class ReadSound implements ReadHandler {
      public int read(int var1) {
         byte var2;
         if((var1 & 1) == 1) {
            var2 = 3;
         } else {
            var2 = 0;
         }

         return var2;
      }
   }
}
