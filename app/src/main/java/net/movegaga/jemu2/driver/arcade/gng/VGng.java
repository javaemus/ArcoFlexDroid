package net.movegaga.jemu2.driver.arcade.gng;

import net.movegaga.jemu2.driver.BaseVideo;

import jef.machine.EmulatorProperties;
import jef.map.WriteHandler;
import jef.video.BitMap;
import jef.video.BitMapImpl;
import jef.video.Blitter;
import jef.video.VideoEmulator;
import jef.video.VideoRenderer;

public class VGng extends BaseVideo implements VideoEmulator, VideoRenderer {
   static final int VRAM_BG = 10240;
   static final int VRAM_FG = 8192;
   BitMap bgBitMap;
   boolean[] bgDirty = new boolean[1024];
   Gng driver;
   BitMap fgBitMap;
   boolean[] fgDirty = new boolean[1024];
   int[] scrollx = new int[2];
   int[] scrolly = new int[2];

   public VGng(Gng var1) {
      this.driver = var1;
   }

   public void drawSprites() {
      for(int var1 = 508; var1 >= 0; var1 -= 4) {
         char var3 = this.driver.mem_cpu1[var1 + 7680 + 1];
         char var4 = this.driver.mem_cpu1[var1 + 7680 + 3];
         char var2 = this.driver.mem_cpu1[var1 + 7680 + 2];
         boolean var5;
         if((var3 & 4) != 0) {
            var5 = true;
         } else {
            var5 = false;
         }

         boolean var6;
         if((var3 & 8) != 0) {
            var6 = true;
         } else {
            var6 = false;
         }

         this.drawVisible(2, (var3 << 2 & 768) + this.driver.mem_cpu1[var1 + 7680], var3 >> 4 & 3, var5, var6, var4 - (var3 & 1) * 256, var2, 1, 15);
      }

   }

   public void init(EmulatorProperties var1) {
      super.init(var1);
      this.fgBitMap = new BitMapImpl(256, 256);
      this.bgBitMap = new BitMapImpl(512, 512);
      this.getGfxManager(0).setTransparencyOverwrite(true);
   }

   public BitMap renderVideo() {
      BitMap var8 = this.getBackBuffer();

      int var1;
      char var3;
      int var4;
      for(var1 = 1023; var1 >= 0; --var1) {
         char var2 = this.driver.mem_cpu1[var1 + 10240 + 1024];
         if(this.bgDirty[var1] || this.getGfxManager(1).colorCodeHasChanged(var2 & 7)) {
            this.bgDirty[var1] = false;
            var4 = var1 / 32;
            var3 = this.driver.mem_cpu1[var1 + 10240];
            this.drawVisible(this.bgBitMap, 1, var3 + ((var2 & 192) << 2), var2 & 7, var2 & 16, var2 & 32, var4 * 16, var1 % 32 * 16, -1, 0);
         }
      }

      this.bgBitMap.getPixels();
      var8.getPixels();
      var4 = this.scrollx[0];
      int var12 = this.scrollx[1];
      int var11 = this.scrolly[0];
      var1 = this.scrolly[1];
      Blitter.copyWrappedBitMap(this.bgBitMap, var8, (var4 | var12 << 8) & 511, (var11 | var1 << 8) & 511);
      this.drawSprites();

      for(var1 = 1023; var1 >= 0; --var1) {
         var3 = this.driver.mem_cpu1[var1 + 8192 + 1024];
         if(this.fgDirty[var1] || this.getGfxManager(0).colorCodeHasChanged(var3 & 15)) {
            this.fgDirty[var1] = false;
            var11 = var1 / 32;
            boolean var6;
            if((var3 & 16) != 0) {
               var6 = true;
            } else {
               var6 = false;
            }

            boolean var7;
            if((var3 & 32) != 0) {
               var7 = true;
            } else {
               var7 = false;
            }

            char var13 = this.driver.mem_cpu1[var1 + 8192];
            this.drawVisible(this.fgBitMap, 0, var13 + ((var3 & 192) << 2), var3 & 15, var6, var7, var1 % 32 * 8, var11 * 8, 1, 3);
         }
      }

      int[] var9 = this.fgBitMap.getPixels();
      int[] var10 = var8.getPixels();
      var12 = 0;
      var1 = 0;

      for(var11 = 0; var11 < 224; ++var11) {
         for(var4 = 0; var4 < 256; ++var1) {
            int var5 = var9[var1];
            if(var5 != -1) {
               var10[var12] = var5;
            }

            ++var12;
            ++var4;
         }
      }

      return var8;
   }

   public WriteHandler writeBgVRAM() {
      return new WriteBgVRAM();
   }

   public WriteHandler writeFgVRAM() {
      return new WriteFgVRAM();
   }

   public WriteHandler writePaletteRAM1() {
      return new WritePaletteRAM1();
   }

   public WriteHandler writePaletteRAM2() {
      return new WritePaletteRAM2();
   }

   public WriteHandler writeScrollX() {
      return new WriteScrollX();
   }

   public WriteHandler writeScrollY() {
      return new WriteScrollY();
   }

   class WriteBgVRAM implements WriteHandler {
      public void write(int var1, int var2) {
         VGng.this.driver.mem_cpu1[var1] = (char)var2;
         VGng.this.bgDirty[var1 & 1023] = true;
      }
   }

   class WriteFgVRAM implements WriteHandler {
      public void write(int var1, int var2) {
         VGng.this.driver.mem_cpu1[var1] = (char)var2;
         VGng.this.fgDirty[var1 & 1023] = true;
      }
   }

   public class WritePaletteRAM1 implements WriteHandler {
      public void write(int var1, int var2) {
         VGng.this.driver.mem_cpu1[var1] = (char)var2;
         VGng.this.changecolor_RRRRGGGGBBBBxxxx(var1 - 14592, VGng.this.driver.mem_cpu1[var1] | VGng.this.driver.mem_cpu1[var1 - 14592 + 14336] << 8);
      }
   }

   public class WritePaletteRAM2 implements WriteHandler {
      public void write(int var1, int var2) {
         VGng.this.driver.mem_cpu1[var1] = (char)var2;
         VGng.this.changecolor_RRRRGGGGBBBBxxxx(var1 - 14336, VGng.this.driver.mem_cpu1[var1 - 14336 + 14592] | VGng.this.driver.mem_cpu1[var1] << 8);
      }
   }

   class WriteScrollX implements WriteHandler {
      public void write(int var1, int var2) {
         VGng.this.scrollx[var1 & 1] = var2;
      }
   }

   class WriteScrollY implements WriteHandler {
      public void write(int var1, int var2) {
         VGng.this.scrolly[var1 & 1] = var2;
      }
   }
}
