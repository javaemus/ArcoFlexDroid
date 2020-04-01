package net.movegaga.jemu2.driver.arcade.hyperspt;

import net.movegaga.jemu2.driver.BaseVideo;

import jef.machine.EmulatorProperties;
import jef.map.WriteHandler;
import jef.video.BitMap;
import jef.video.BitMapImpl;
import jef.video.PaletteInitializer;
import jef.video.VideoEmulator;
import jef.video.VideoInitializer;
import jef.video.VideoRenderer;

public class VHyperspt extends BaseVideo implements VideoEmulator, VideoRenderer, VideoInitializer, PaletteInitializer, WriteHandler {
   BitMap backBuffer;
   BitMap bgLayer;
   boolean[] dirty = new boolean[2048];
   Hyperspt driver;
   int[] scroll = new int[32];

   public VHyperspt(Hyperspt var1) {
      this.driver = var1;
   }

   public void init(EmulatorProperties var1) {
      super.init(var1);
      this.backBuffer = this.getBackBuffer();
   }

   public void initPalette() {
      int var1 = 0;

      int var2;
      for(var2 = 0; var2 < this.getTotalColors(); ++var1) {
         char var5 = this.driver.mem_prom[var1];
         int var4 = (var5 & 7) << 5;
         int var3 = (var5 >> 3 & 7) << 5;
         int var6 = (var5 >> 6 & 3) << 6;
         this.setPaletteColor(var2, var4 >> 3 | var4 | var4 >> 6, var3 >> 3 | var3 | var3 >> 6, var6 >> 2 | var6 | var6 >> 4 | var6 >> 6);
         ++var2;
      }

      for(var2 = 0; var2 < this.getTotalColors(1); ++var1) {
         this.setColor(1, var2, this.driver.mem_prom[var1] & 15);
         ++var2;
      }

      for(var2 = 0; var2 < this.getTotalColors(0); ++var1) {
         this.setColor(0, var2, (this.driver.mem_prom[var1] & 15) + 16);
         ++var2;
      }

   }

   public VideoInitializer initRoadf() {
      return new InitRoadf();
   }

   public int initVideo() {
      this.bgLayer = new BitMapImpl(512, 256);
      return 0;
   }

   public BitMap renderVideo() {
      int var1;
      int var2;
      char var3;
      char var5;
      char var6;
      BitMap var11;
      for(var1 = 2047; var1 >= 0; --var1) {
         if(this.dirty[var1]) {
            this.dirty[var1] = false;
            var2 = var1 / 64;
            var5 = this.driver.mem_cpu1[var1 + 10240];
            char var4 = this.driver.mem_cpu1[var1 + 10240];
            var11 = this.bgLayer;
            var3 = this.driver.mem_cpu1[var1 + 8192];
            var6 = this.driver.mem_cpu1[var1 + 10240];
            this.draw(var11, 0, ((this.driver.mem_cpu1[var1 + 10240] & 64) << 3) + var3 + ((var6 & 128) << 1), this.driver.mem_cpu1[var1 + 10240] & 15, var5 & 16, var4 & 32, var1 % 64 * 8, var2 * 8, 0, -1, 0);
         }
      }

      for(var1 = 0; var1 < 32; ++var1) {
         this.scroll[var1] = -(this.driver.mem_cpu1[var1 * 2 + 4288] + (this.driver.mem_cpu1[var1 * 2 + 4288 + 1] & 1) * 256);
      }

      this.copyScrollBitMap(this.backBuffer, this.bgLayer, 32, this.scroll, 0, 0, this.getVisibleArea(), -1, 0);

      for(var1 = 188; var1 >= 0; var1 -= 4) {
         var3 = this.driver.mem_cpu1[var1 + 4096 + 3];
         var5 = this.driver.mem_cpu1[var1 + 4096 + 1];
         var2 = ~this.driver.mem_cpu1[var1 + 4096] & 64;
         int var12 = this.driver.mem_cpu1[var1 + 4096] & 128;
         int var13 = 240 - var5 + 1;
         var11 = this.backBuffer;
         char var8 = this.driver.mem_cpu1[var1 + 4096 + 2];
         var6 = this.driver.mem_cpu1[var1 + 4096];
         char var7 = this.driver.mem_cpu1[var1 + 4096];
         boolean var9;
         if(var2 != 0) {
            var9 = true;
         } else {
            var9 = false;
         }

         boolean var10;
         if(var12 != 0) {
            var10 = true;
         } else {
            var10 = false;
         }

         this.drawVisible(var11, 1, var8 + (var6 & 32) * 8, var7 & 15, var9, var10, var3, var13, 2, 0);
         var11 = this.backBuffer;
         var6 = this.driver.mem_cpu1[var1 + 4096 + 2];
         var7 = this.driver.mem_cpu1[var1 + 4096];
         var8 = this.driver.mem_cpu1[var1 + 4096];
         if(var2 != 0) {
            var9 = true;
         } else {
            var9 = false;
         }

         if(var12 != 0) {
            var10 = true;
         } else {
            var10 = false;
         }

         this.drawVisible(var11, 1, var6 + (var7 & 32) * 8, var8 & 15, var9, var10, var3 - 256, var13, 2, 0);
      }

      return this.backBuffer;
   }

   public VideoRenderer rendererRoadf() {
      return new RendererRoadf();
   }

   public void write(int var1, int var2) {
      if(this.driver.mem_cpu1[var1] != var2) {
         this.driver.mem_cpu1[var1] = (char)var2;
         this.dirty[var1 & 2047] = true;
      }

   }

   class InitRoadf implements VideoInitializer {
      public int initVideo() {
         VHyperspt.this.bgLayer = new BitMapImpl(256, 512);
         return 0;
      }
   }

   class RendererRoadf implements VideoRenderer {
      public BitMap renderVideo() {
         int var1;
         char var2;
         char var5;
         char var6;
         boolean var9;
         boolean var10;
         BitMap var11;
         VHyperspt var12;
         for(var1 = 2047; var1 >= 0; --var1) {
            if(VHyperspt.this.dirty[var1]) {
               VHyperspt.this.dirty[var1] = false;
               int var7 = var1 / 64;
               var2 = VHyperspt.this.driver.mem_cpu1[var1 + 10240];
               var12 = VHyperspt.this;
               var11 = VHyperspt.this.bgLayer;
               var5 = VHyperspt.this.driver.mem_cpu1[var1 + 8192];
               char var3 = VHyperspt.this.driver.mem_cpu1[var1 + 10240];
               var6 = VHyperspt.this.driver.mem_cpu1[var1 + 10240];
               char var4 = VHyperspt.this.driver.mem_cpu1[var1 + 10240];
               if((~var2 & 16) != 0) {
                  var9 = true;
               } else {
                  var9 = false;
               }

               if(true) {
                  var10 = true;
               } else {
                  var10 = false;
               }

               var12.drawVisible(var11, 0, var5 + ((var3 & 128) << 1) + ((var6 & 96) << 4), var4 & 15, var9, var10, (63 - var1 % 64) * 8, (31 - var7) * 8, -1, 0);
            }
         }

         for(var1 = 0; var1 < 32; ++var1) {
            VHyperspt.this.scroll[var1] = -(VHyperspt.this.driver.mem_cpu1[var1 * 2 + 4288] + (VHyperspt.this.driver.mem_cpu1[var1 * 2 + 4288 + 1] & 1) * 256);
         }

         VHyperspt.this.copyScrollBitMap(VHyperspt.this.backBuffer, VHyperspt.this.bgLayer, 32, VHyperspt.this.scroll, 0, 0, VHyperspt.this.getVisibleArea(), -1, 0);

         for(var1 = 188; var1 >= 0; var1 -= 4) {
            var2 = VHyperspt.this.driver.mem_cpu1[var1 + 4096 + 3];
            var5 = VHyperspt.this.driver.mem_cpu1[var1 + 4096 + 1];
            int var14 = ~VHyperspt.this.driver.mem_cpu1[var1 + 4096] & 64;
            int var13 = ~VHyperspt.this.driver.mem_cpu1[var1 + 4096] & 128;
            int var15 = var5 + 1;
            var12 = VHyperspt.this;
            var11 = VHyperspt.this.backBuffer;
            char var16 = VHyperspt.this.driver.mem_cpu1[var1 + 4096 + 2];
            char var8 = VHyperspt.this.driver.mem_cpu1[var1 + 4096];
            var6 = VHyperspt.this.driver.mem_cpu1[var1 + 4096];
            if(var14 != 0) {
               var9 = true;
            } else {
               var9 = false;
            }

            if(var13 != 0) {
               var10 = true;
            } else {
               var10 = false;
            }

            var12.drawVisible(var11, 1, var16 + (var8 & 32) * 8, var6 & 15, var9, var10, var2, var15, 2, 0);
            var12 = VHyperspt.this;
            var11 = VHyperspt.this.backBuffer;
            var8 = VHyperspt.this.driver.mem_cpu1[var1 + 4096 + 2];
            var16 = VHyperspt.this.driver.mem_cpu1[var1 + 4096];
            var6 = VHyperspt.this.driver.mem_cpu1[var1 + 4096];
            if(var14 != 0) {
               var9 = true;
            } else {
               var9 = false;
            }

            if(var13 != 0) {
               var10 = true;
            } else {
               var10 = false;
            }

            var12.drawVisible(var11, 1, var8 + (var16 & 32) * 8, var6 & 15, var9, var10, var2 - 256, var15, 2, 0);
         }

         return VHyperspt.this.backBuffer;
      }

      public void renderVideoPost() {
      }
   }
}
