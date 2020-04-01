package net.movegaga.jemu2.driver.arcade.c1943;

import net.movegaga.jemu2.driver.BaseVideo;

import java.lang.reflect.Array;

import jef.map.WriteHandler;
import jef.video.BitMap;
import jef.video.BitMapImpl;
import jef.video.PaletteInitializer;
import jef.video.VideoEmulator;
import jef.video.VideoInitializer;
import jef.video.VideoRenderer;

public class VC1943 extends BaseVideo implements VideoEmulator, VideoRenderer, VideoInitializer, PaletteInitializer {
   static final int SCROLL_BG_1943 = 55299;
   static final int SCROLL_X_1943 = 55298;
   static final int SCROLL_X_GUNSMOKE = 55298;
   static final int SCROLL_Y_1943 = 55296;
   static final int SCROLL_Y_GUNSMOKE = 55296;
   public int bgon = 0;
   public int chon = 0;
   boolean[] dirty;
   C1943 driver;
   int flipscreen;
   public int objon = 0;
   BitMap sc1bitmap;
   int[][][] sc1map;
   public int sc1on = 0;
   BitMap sc2bitmap;
   int[][][] sc2map;
   public int sc2on = 0;
   public int sprite3bank = 0;

   public VC1943(C1943 var1) {
      this.sc2map = (int[][][])Array.newInstance(Integer.TYPE, new int[]{9, 8, 2});
      this.sc1map = (int[][][])Array.newInstance(Integer.TYPE, new int[]{9, 9, 2});
      this.driver = var1;
      this.dirty = new boolean[1024];
   }

   private void fill(BitMap var1, int var2) {
      int[] var4 = var1.getPixels();

      for(int var3 = 0; var3 < var4.length; ++var3) {
         var4[var3] = var2;
      }

   }

   public void initPalette() {
      int var1;
      for(var1 = 0; var1 < this.getTotalColors(); ++var1) {
         char var2 = this.driver.mem_prom[this.getTotalColors() * 0 + var1];
         char var3 = this.driver.mem_prom[this.getTotalColors() * 1 + var1];
         char var4 = this.driver.mem_prom[this.getTotalColors() * 2 + var1];
         this.setPaletteColor(var1, var2 << 4 | var2, var3 << 4 | var3, var4 << 4 | var4);
      }

      var1 = 0 + this.getTotalColors() * 3;

      int var5;
      for(var5 = 0; var5 < this.getTotalColors(0); ++var1) {
         this.setColor(0, var5, this.driver.mem_prom[var1] + 64);
         ++var5;
      }

      var5 = var1 + 128;

      for(var1 = 0; var1 < this.getTotalColors(1); ++var1) {
         if(var1 % this.getColorGranularity(1) == 0) {
            this.setColor(1, var1, 0);
         } else {
            this.setColor(1, var1, this.driver.mem_prom[var5 + 0] + (this.driver.mem_prom[var5 + 256] & 3) * 16);
         }

         ++var5;
      }

      var5 += this.getTotalColors(1);

      for(var1 = 0; var1 < this.getTotalColors(2); ++var1) {
         this.setColor(2, var1, this.driver.mem_prom[var5 + 0] + (this.driver.mem_prom[var5 + 256] & 3) * 16);
         ++var5;
      }

      var5 += this.getTotalColors(2);

      for(var1 = 0; var1 < this.getTotalColors(3); ++var1) {
         this.setColor(3, var1, this.driver.mem_prom[var5 + 0] + (this.driver.mem_prom[var5 + 256] & 7) * 16 + 128);
         ++var5;
      }

      this.getTotalColors(3);
   }

   public int initVideo() {
      this.sc2bitmap = new BitMapImpl(256, 288);
      this.sc1bitmap = new BitMapImpl(288, 288, true);
      this.getGfxManager(1).setTransparencyOverwrite(true);
      return 0;
   }

   public PaletteInitializer piGunsmoke() {
      return new GunsmokePalette();
   }

   public BitMap renderVideo() {
      BitMap var10 = this.getBackBuffer();
      int var1;
      int var2;
      int var3;
      int var5;
      int[] var11;
      char var18;
      if(this.sc2on != 0) {
         var3 = this.driver.mem_cpu1['\ud803'] + this.driver.mem_cpu1['\ud804'] * 256;
         var2 = ((var3 >> 5) + 8) * 16;

         for(var1 = 0; var1 < 9; ++var1) {
            int var4 = (var1 + 0) % 9;
            var5 = var2 & 32767;

            for(var2 = 0; var2 < 8; ++var2) {
               var11 = this.sc2map[var4][var2];
               int var7 = var5 + var2 * 2;
               char var6 = this.driver.mem_gfx5['耀' + var7];
               var18 = this.driver.mem_gfx5['耀' + var7 + 1];
               if(var6 != var11[0] || var18 != var11[1]) {
                  var11[0] = var6;
                  var11[1] = var18;
                  if(var6 != 0) {
                     this.drawVisible(this.sc2bitmap, 2, var6, (var18 & 60) >> 2, var18 & 64, var18 & 128, (8 - var4) * 32, var2 * 32, -1, 0);
                  }
               }
            }

            var2 = var5 - 16;
         }

         this.copyScrollBitMap(var10, this.sc2bitmap, 1, 287 - (var3 & 31), 1, 0, -1, 0);
      } else {
         this.fill(var10, 0);
      }

      char var14;
      char var15;
      if(this.objon != 0) {
         for(var1 = 4064; var1 >= 0; var1 -= 32) {
            var2 = this.driver.mem_cpu1['\uf000' + var1 + 1] & 15;
            if(var2 == 10 || var2 == 11) {
               var15 = this.driver.mem_cpu1['\uf000' + var1 + 3];
               char var12 = this.driver.mem_cpu1['\uf000' + var1 + 1];
               var14 = this.driver.mem_cpu1['\uf000' + var1 + 2];
               this.drawVisible(var10, 3, this.driver.mem_cpu1['\uf000' + var1] + ((this.driver.mem_cpu1['\uf000' + var1 + 1] & 224) << 3), var2, false, false, var15 - ((var12 & 16) << 4), var14, 1, 0);
            }
         }
      }

      if(this.sc1on != 0) {
         var5 = this.driver.mem_cpu1['\ud800'] + this.driver.mem_cpu1['\ud801'] * 256;
         var14 = this.driver.mem_cpu1['\ud802'];
         var2 = ((var5 >> 5) + 8) * 16 + (var14 >> 5) * 2;
         var1 = var2;
         if((var14 & 128) != 0) {
            var1 = var2 - 16;
         }

         byte var13 = 0;
         var2 = var1;

         for(var1 = var13; var1 < 9; ++var1) {
            var3 = (var1 + 0) % 9;
            int var17 = var2 & 32767;

            for(var2 = 0; var2 < 9; ++var2) {
               int var8 = (var2 + 0) % 9;
               var11 = this.sc1map[var3][var8];
               int var9 = var17 + var2 * 2;
               var18 = this.driver.mem_gfx5[var9];
               char var19 = this.driver.mem_gfx5[var9 + 1];
               if(var18 != var11[0] || var19 != var11[1]) {
                  var11[0] = var18;
                  var11[1] = var19;
                  this.drawVisible(this.sc1bitmap, 1, var18 + (var19 & 1) * 256, (var19 & 60) >> 2, var19 & 64, var19 & 128, (8 - var3) * 32, var8 * 32, 2, 0);
               }
            }

            var2 = var17 - 16;
         }

         this.copyScrollBitMap(var10, this.sc1bitmap, 1, 255 - (var5 & 31), 1, var14 & 31, 2, 0);
      }

      if(this.objon != 0) {
         for(var1 = 4064; var1 >= 0; var1 -= 32) {
            var3 = this.driver.mem_cpu1['\uf000' + var1 + 1] & 15;
            if(var3 != 10 && var3 != 11) {
               var15 = this.driver.mem_cpu1['\uf000' + var1 + 3];
               var14 = this.driver.mem_cpu1['\uf000' + var1 + 1];
               char var16 = this.driver.mem_cpu1['\uf000' + var1 + 2];
               this.drawVisible(var10, 3, this.driver.mem_cpu1['\uf000' + var1] + ((this.driver.mem_cpu1['\uf000' + var1 + 1] & 224) << 3), var3, false, false, var15 - ((var14 & 16) << 4), var16, 1, 0);
            }
         }
      }

      if(this.chon != 0) {
         for(var1 = 1023; var1 >= 0; --var1) {
            var3 = var1 / 32;
            var2 = this.driver.mem_cpu1['퀀' + var1] + ((this.driver.mem_cpu1['퐀' + var1] & 224) << 3);
            if(var2 != 36) {
               this.drawVisible(var10, 0, var2, this.driver.mem_cpu1['퐀' + var1] & 31, false, false, var1 % 32 * 8, var3 * 8, 2, 79);
            }
         }
      }

      return var10;
   }

   public VideoInitializer vsGunsmoke() {
      return new GunsmokeVs();
   }

   public VideoRenderer vuGunsmoke() {
      return new GunsmokeVideoUpdate();
   }

   public WriteHandler writeVRAM() {
      return new WriteVRAM();
   }

   public class GunsmokePalette implements PaletteInitializer {
      public void initPalette() {
         int var1;
         for(var1 = 0; var1 < VC1943.this.getTotalColors(); ++var1) {
            char var2 = VC1943.this.driver.mem_prom[VC1943.this.getTotalColors() * 0 + var1];
            char var3 = VC1943.this.driver.mem_prom[VC1943.this.getTotalColors() * 1 + var1];
            char var4 = VC1943.this.driver.mem_prom[VC1943.this.getTotalColors() * 2 + var1];
            VC1943.this.setPaletteColor(var1, var2 << 4 | var2, var3 << 4 | var3, var4 << 4 | var4);
         }

         var1 = 0 + VC1943.this.getTotalColors() * 3;

         int var5;
         for(var5 = 0; var5 < VC1943.this.getTotalColors(0); ++var1) {
            VC1943.this.setColor(0, var5, VC1943.this.driver.mem_prom[var1] + 64);
            ++var5;
         }

         var5 = var1 + 128;

         for(var1 = 0; var1 < VC1943.this.getTotalColors(1); ++var1) {
            VC1943.this.setColor(1, var1, VC1943.this.driver.mem_prom[var5 + 0] + (VC1943.this.driver.mem_prom[var5 + 256] & 3) * 16);
            ++var5;
         }

         var5 += VC1943.this.getTotalColors(1);

         for(var1 = 0; var1 < VC1943.this.getTotalColors(2); ++var1) {
            VC1943.this.setColor(2, var1, VC1943.this.driver.mem_prom[var5 + 0] + (VC1943.this.driver.mem_prom[var5 + 256] & 7) * 16 + 128);
            ++var5;
         }

         VC1943.this.getTotalColors(2);
      }
   }

   public class GunsmokeVideoUpdate implements VideoRenderer {
      public BitMap renderVideo() {
         BitMap var10 = VC1943.this.getBackBuffer();
         int var1;
         int var2;
         char var5;
         int var12;
         if(VC1943.this.bgon != 0) {
            int var4 = VC1943.this.driver.mem_cpu1['\ud800'] + VC1943.this.driver.mem_cpu1['\ud801'] * 256;
            var5 = VC1943.this.driver.mem_cpu1['\ud802'];
            var2 = ((var4 >> 5) + 8) * 16 + (var5 >> 5) * 2;
            var1 = var2;
            if((var5 & 128) != 0) {
               var1 = var2 - 16;
            }

            byte var3 = 0;
            var2 = var1;

            for(var1 = var3; var1 < 9; ++var1) {
               var12 = (var1 + 0) % 9;
               int var6 = var2 & 32767;

               for(var2 = 0; var2 < 9; ++var2) {
                  int var7 = (var2 + 0) % 9;
                  int[] var11 = VC1943.this.sc1map[var12][var7];
                  int var9 = var6 + var2 * 2;
                  char var8 = VC1943.this.driver.mem_gfx4[var9];
                  char var16 = VC1943.this.driver.mem_gfx4[var9 + 1];
                  if(var8 != var11[0] || var16 != var11[1]) {
                     var11[0] = var8;
                     var11[1] = var16;
                     VC1943.this.drawVisible(VC1943.this.sc1bitmap, 1, var8 + (var16 & 1) * 256, (var16 & 60) >> 2, var16 & 64, var16 & 128, (8 - var12) * 32, var7 * 32, -1, 0);
                  }
               }

               var2 = var6 - 16;
            }

            VC1943.this.copyScrollBitMap(var10, VC1943.this.sc1bitmap, 1, 255 - (var4 & 31), 1, var5 & 31, -1, 0);
         } else {
            VC1943.this.fill(var10, 0);
         }

         if(VC1943.this.objon != 0) {
            for(var1 = 4064; var1 >= 0; var1 -= 32) {
               var12 = (VC1943.this.driver.mem_cpu1['\uf000' + var1 + 1] & 192) >> 6;
               var2 = var12;
               if(var12 == 3) {
                  var2 = var12 + VC1943.this.sprite3bank;
               }

               char var13 = VC1943.this.driver.mem_cpu1['\uf000' + var1 + 3];
               var5 = VC1943.this.driver.mem_cpu1['\uf000' + var1 + 1];
               char var14 = VC1943.this.driver.mem_cpu1['\uf000' + var1 + 2];
               char var15 = VC1943.this.driver.mem_cpu1['\uf000' + var1 + 1];
               VC1943.this.drawVisible(var10, 2, VC1943.this.driver.mem_cpu1['\uf000' + var1] + var2 * 256, VC1943.this.driver.mem_cpu1['\uf000' + var1 + 1] & 15, 0, var15 & 16, var13 - ((var5 & 32) << 3), var14, 1, 0);
            }
         }

         if(VC1943.this.chon != 0) {
            for(var1 = 1023; var1 >= 0; --var1) {
               var2 = var1 / 32;
               VC1943.this.drawVisible(var10, 0, VC1943.this.driver.mem_cpu1['퀀' + var1] + ((VC1943.this.driver.mem_cpu1['퐀' + var1] & 192) << 2), VC1943.this.driver.mem_cpu1['퐀' + var1] & 31, true, true, var1 % 32 * 8, var2 * 8, 2, 79);
            }
         }

         return var10;
      }

      public void renderVideoPost() {
      }
   }

   public class GunsmokeVs implements VideoInitializer {
      public int initVideo() {
         VC1943.this.sc1bitmap = new BitMapImpl(288, 288, true);
         return 0;
      }
   }

   public class WriteVRAM implements WriteHandler {
      public void write(int var1, int var2) {
         if(VC1943.this.driver.mem_cpu1[var1] != var2) {
            VC1943.this.dirty[var1 & 1023] = true;
            VC1943.this.driver.mem_cpu1[var1] = (char)var2;
         }

      }
   }
}
