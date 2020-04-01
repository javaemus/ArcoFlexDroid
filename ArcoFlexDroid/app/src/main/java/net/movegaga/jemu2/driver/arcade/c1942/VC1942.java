package net.movegaga.jemu2.driver.arcade.c1942;

import net.movegaga.jemu2.driver.BaseVideo;

import jef.machine.EmulatorProperties;
import jef.map.WriteHandler;
import jef.video.BitMap;
import jef.video.BitMapImpl;
import jef.video.PaletteInitializer;
import jef.video.VideoEmulator;
import jef.video.VideoInitializer;
import jef.video.VideoRenderer;

public class VC1942 extends BaseVideo implements VideoEmulator, VideoRenderer, VideoInitializer, PaletteInitializer {
   C1942 driver;
   int paletteBank;
   int[] scroll = new int[2];
   BitMap scrollBitMap;

   public VC1942(C1942 var1) {
      this.driver = var1;
   }

   public void drawSprites() {
      for(int var2 = 124; var2 >= 0; var2 -= 4) {
         char var8 = this.driver.mem_cpu1['찀' + var2];
         char var5 = this.driver.mem_cpu1['찀' + var2 + 1];
         char var10 = this.driver.mem_cpu1['찀' + var2];
         char var7 = this.driver.mem_cpu1['찀' + var2 + 1];
         char var9 = this.driver.mem_cpu1['찀' + var2 + 3];
         char var6 = this.driver.mem_cpu1['찀' + var2 + 1];
         char var4 = this.driver.mem_cpu1['찀' + var2 + 2];
         int var3 = (this.driver.mem_cpu1['찀' + var2 + 1] & 192) >> 6;
         int var1 = var3;
         if(var3 == 2) {
            var1 = 3;
         }

         do {
            this.drawVisible(2, (var8 & 127) + (var5 & 32) * 4 + (var10 & 128) * 2 + var1, var7 & 15, false, false, var9 - (var6 & 16) * 16, var4 + var1 * 16 * 1, 1, 15);
            var3 = var1 - 1;
            var1 = var3;
         } while(var3 >= 0);
      }

   }

   public void init(EmulatorProperties var1) {
      super.init(var1);
   }

   public void initPalette() {
      int var1;
      for(var1 = 0; var1 < this.getTotalColors(); ++var1) {
         char var2 = this.driver.mem_prom[this.getTotalColors() * 0 + var1];
         char var4 = this.driver.mem_prom[this.getTotalColors() * 1 + var1];
         char var3 = this.driver.mem_prom[this.getTotalColors() * 2 + var1];
         this.setPaletteColor(var1, var2 << 4 | var2, var4 << 4 | var4, var3 << 4 | var3);
      }

      var1 = 0 + this.getTotalColors() * 3;

      int var5;
      for(var5 = 0; var5 < this.getTotalColors(0); ++var1) {
         this.setColor(0, var5, this.driver.mem_prom[var1] + 128);
         ++var5;
      }

      for(var5 = 0; var5 < this.getTotalColors(1) / 4; ++var5) {
         this.setColor(1, var5, this.driver.mem_prom[var1]);
         this.setColor(1, var5 + 256, this.driver.mem_prom[var1] + 16);
         this.setColor(1, var5 + 512, this.driver.mem_prom[var1] + 32);
         this.setColor(1, var5 + 768, this.driver.mem_prom[var1] + 48);
         ++var1;
      }

      for(var5 = 0; var5 < this.getTotalColors(2); ++var1) {
         this.setColor(2, var5, this.driver.mem_prom[var1] + 64);
         ++var5;
      }

   }

   public int initVideo() {
      this.scrollBitMap = new BitMapImpl(256, 512);
      return 0;
   }

   public BitMap renderVideo() {
      int var1;
      int var3;
      int var4;
      char var9;
      for(var1 = 511; var1 >= 0; --var1) {
         var3 = var1 / 16;
         var4 = var1 & 15 | (var1 & 496) << 1;
         char var2 = this.driver.mem_cpu1['\ud800' + var4];
         var9 = this.driver.mem_cpu1['\ud800' + var4 + 16];
         this.drawVisible(this.scrollBitMap, 1, var2 + ((var9 & 128) << 1), this.paletteBank * 32 + (var9 & 31), var9 & 32, var9 & 64, var3 * 16, var1 % 16 * 16, -1, 0);
      }

      int[] var7 = this.scrollBitMap.getPixels();
      int[] var6 = this.getBackBuffer().getPixels();
      var1 = 256 - (this.scroll[0] | this.scroll[1] << 8) & 511;
      var3 = var1;
      if(var1 < 0) {
         var3 = var1 + 512;
      }

      var1 = 0;
      int var8 = 0;
      int var5;
      if(var3 + 256 <= 512) {
         var1 = var3 * 256;

         for(var3 = 0; var3 < 256; var1 = var4) {
            var5 = 0;
            var4 = var1;

            for(var1 = var8; var5 < 224; ++var4) {
               var6[var1] = var7[var4];
               ++var5;
               ++var1;
            }

            var4 += 32;
            ++var3;
            var8 = var1;
         }
      } else {
         var8 = var3 * 256;

         for(var4 = 0; var4 < 512 - var3; ++var4) {
            for(var5 = 0; var5 < 224; ++var8) {
               var6[var1] = var7[var8];
               ++var5;
               ++var1;
            }

            var8 += 32;
         }

         var8 = 0;

         for(var4 = 0; var4 < 256 - (512 - var3); ++var4) {
            for(var5 = 0; var5 < 224; ++var8) {
               var6[var1] = var7[var8];
               ++var5;
               ++var1;
            }

            var8 += 32;
         }
      }

      this.drawSprites();

      for(var1 = 1023; var1 >= 0; --var1) {
         var8 = var1 / 32;
         char var10 = this.driver.mem_cpu1['퀀' + var1];
         var9 = this.driver.mem_cpu1['퀀' + var1 + 1024];
         this.drawVisible(0, var10 + ((var9 & 128) << 1), var9 & 63, false, false, var1 % 32 * 8, var8 * 8, 1, 0);
      }

      return this.getBackBuffer();
   }

   public WriteHandler writePaletteBank() {
      return new WritePaletteBank();
   }

   public WriteHandler writeScrollRegs() {
      return new WriteScrollRegisters();
   }

   class WritePaletteBank implements WriteHandler {
      public void write(int var1, int var2) {
         if(VC1942.this.paletteBank != var2) {
            VC1942.this.paletteBank = var2;
         }

      }
   }

   class WriteScrollRegisters implements WriteHandler {
      public void write(int var1, int var2) {
         VC1942.this.scroll[var1 & 1] = var2;
      }
   }
}
