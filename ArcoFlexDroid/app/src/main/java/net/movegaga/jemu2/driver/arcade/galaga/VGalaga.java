package net.movegaga.jemu2.driver.arcade.galaga;

import net.movegaga.jemu2.driver.BaseVideo;

import jef.machine.EmulatorProperties;
import jef.video.BitMap;
import jef.video.BitMapImpl;
import jef.video.PaletteInitializer;
import jef.video.VideoEmulator;
import jef.video.VideoInitializer;
import jef.video.VideoRenderer;

public class VGalaga extends BaseVideo implements VideoEmulator, VideoRenderer, VideoInitializer, PaletteInitializer {
   private static final int MAX_STARS = 250;
   private static final int SPRITES_MEM_LOC_1 = 35712;
   private static final int SPRITES_MEM_LOC_2 = 37760;
   private static final int SPRITES_MEM_LOC_3 = 39808;
   private static final int SPRITES_MEM_SIZE = 128;
   private static final int STARS_COLOR_BASE = 32;
   private static final int STAR_CONTROL = 40960;
   BitMap charLayer;
   public boolean[] dirtybuffer = new boolean[1024];
   char[] mem_cpu;
   char[] mem_prom;
   Star[] stars = new Star[250];
   private int starsScroll = 0;
   private int totalStars;

   public void init(EmulatorProperties var1) {
      super.init(var1);
      this.charLayer = new BitMapImpl(this.getBackBuffer().getWidth(), this.getBackBuffer().getHeight());
   }

   public void initPalette() {
      int var1;
      int var2;
      for(var1 = 0; var1 < 32; ++var1) {
         int var3 = (this.mem_prom[31 - var1] & 7) << 1;
         var2 = (this.mem_prom[31 - var1] >> 3 & 7) << 1;
         int var4 = (this.mem_prom[31 - var1] >> 6 & 7) << 2;
         this.setPaletteColor(var1, var3 | var3 << 4, var2 | var2 << 4, var4 | var4 << 4);
      }

      var1 = 32;

      for(var2 = 0; var2 < this.getTotalColors(0); ++var1) {
         this.setColor(0, var2, 15 - (this.mem_prom[var1] & 15));
         ++var2;
      }

      var2 = var1 + 128;

      for(var1 = 0; var1 < this.getTotalColors(1); ++var1) {
         if(var1 % 4 == 0) {
            this.setColor(1, var1, 0);
         } else {
            this.setColor(1, var1, 15 - (this.mem_prom[var2] & 15) + 16);
         }

         ++var2;
      }

      for(var1 = 32; var1 < 96; ++var1) {
         int[] var5 = new int[]{0, 136, 204, 255};
         this.setPaletteColor(var1, var5[var1 - 32 >> 0 & 3], var5[var1 - 32 >> 2 & 3], var5[var1 - 32 >> 4 & 3]);
      }

   }

   public int initVideo() {
      int var2 = 0;
      this.totalStars = 0;
      int var1 = 0;

      int var5;
      for(int var3 = 0; var3 <= 255; ++var3) {
         for(int var4 = 511; var4 >= 0; var2 = var5) {
            var5 = var1 << 1;
            var1 = var5;
            if((~var5 >> 17 & 1 ^ var5 >> 5 & 1) != 0) {
               var1 = var5 | 1;
            }

            var5 = var2;
            if((~var1 >> 16 & 1) != 0) {
               var5 = var2;
               if((var1 & 255) == 255) {
                  int var6 = ~(var1 >> 8) & 63;
                  var5 = var2;
                  if(var6 != 0) {
                     var5 = var2;
                     if(this.totalStars < 250) {
                        this.stars[this.totalStars] = new Star();
                        this.stars[this.totalStars].x = var4;
                        this.stars[this.totalStars].y = var3;
                        this.stars[this.totalStars].col = this.getPaletteColor(var6 + 32);
                        this.stars[this.totalStars].set = var2;
                        var5 = var2 + 1;
                        var2 = var5;
                        if(var5 > 3) {
                           var2 = 0;
                        }

                        ++this.totalStars;
                        var5 = var2;
                     }
                  }
               }
            }

            --var4;
         }
      }

      return 0;
   }

   public BitMap renderVideo() {
      int var1;
      int var2;
      int var3;
      int var4;
      for(var3 = 1023; var3 >= 0; --var3) {
         if(this.dirtybuffer[var3]) {
            this.dirtybuffer[var3] = false;
            var2 = var3 % 32;
            var4 = var3 / 32;
            if(var4 <= 1) {
               var1 = var4 + 34;
               var2 -= 2;
            } else if(var4 >= 30) {
               var1 = var4 - 30;
               var2 -= 2;
            } else {
               var1 = var2 + 2;
               var2 = var4 - 2;
            }

            char var10 = this.mem_cpu['耀' + var3];
            this.drawVisible(this.charLayer, 0, var10 & 127, this.mem_cpu['萀' + var3] & 31, false, false, var1 * 8, var2 * 8, -1, 0);
         }
      }

      this.charLayer.toPixels(this.getBackBuffer().getPixels());

      for(var1 = 0; var1 < 128; var1 += 2) {
         if((this.mem_cpu['鮀' + var1 + 1] & 2) == 0) {
            char var5 = this.mem_cpu['讀' + var1];
            var2 = this.mem_cpu['讀' + var1 + 1] & 31;
            boolean var6;
            if((this.mem_cpu['鮀' + var1] & 2) != 0) {
               var6 = true;
            } else {
               var6 = false;
            }

            boolean var7;
            if((this.mem_cpu['鮀' + var1] & 1) != 0) {
               var7 = true;
            } else {
               var7 = false;
            }

            var4 = (this.mem_cpu['鎀' + var1 + 1] & 255) - 40 + (this.mem_cpu['鮀' + var1 + 1] & 1) * 256;
            var3 = 224 - (this.mem_cpu['鎀' + var1] & 255);
            if(var3 > -16) {
               if((this.mem_cpu['鮀' + var1] & 12) == 12) {
                  this.drawVisible(1, var5 + 1, var2, var7, var6, var4 + 16, var3 - 16, 2, 0);
                  this.drawVisible(1, var5 + 3, var2, var7, var6, var4 + 16, var3 - 0, 2, 0);
                  this.drawVisible(1, var5, var2, var7, var6, var4 + 0, var3 - 16, 2, 0);
                  this.drawVisible(1, var5 + 2, var2, var7, var6, var4 + 0, var3 - 0, 2, 0);
               } else if((this.mem_cpu['鮀' + var1] & 8) != 0) {
                  this.drawVisible(1, var5, var2, var7, var6, var4, var3 - 16, 2, 0);
                  this.drawVisible(1, var5 + 2, var2, var7, var6, var4, var3 - 0, 2, 0);
               } else if((this.mem_cpu['鮀' + var1] & 4) != 0) {
                  this.drawVisible(1, var5 + 1, var2, var7, var6, var4 + 16, var3, 2, 0);
                  this.drawVisible(1, var5, var2, var7, var6, var4 + 0, var3, 2, 0);
               } else {
                  this.drawVisible(1, var5, var2, var7, var6, var4, var3, 2, 0);
               }
            }
         }
      }

      if((this.mem_cpu['ꀅ'] & 1) != 0) {
         var2 = this.getPaletteColor(0);

         for(var1 = 0; var1 < this.totalStars; ++var1) {
            int[][] var8 = new int[4][];
            int[] var9 = new int[]{0, 3};
            var8[0] = var9;
            var9 = new int[]{0, 1};
            var8[1] = var9;
            var8[2] = new int[]{2, 3};
            var8[3] = new int[]{2, 1};
            var3 = (this.mem_cpu['ꀄ'] << 1 | this.mem_cpu['ꀃ']) & 3;
            if(this.stars[var1].set == var8[var3][0] || this.stars[var1].set == var8[var3][1]) {
               var4 = (this.stars[var1].x + this.starsScroll & 511) / 2 + 16;
               var3 = this.stars[var1].y + (this.starsScroll + this.stars[var1].x) / 512 & 255;
               if(var3 >= this.getVisibleArea()[2] && var3 <= this.getVisibleArea()[3] && this.readPixel(this.getBackBuffer(), var3, var4) == var2) {
                  this.plotPixel(this.getBackBuffer(), var3, var4, this.stars[var1].col);
               }
            }
         }
      }

      return this.getBackBuffer();
   }

   public void setRefs(char[] var1, char[] var2) {
      this.mem_prom = var2;
      this.mem_cpu = var1;
   }

   public void updateStars() {
      int[] var4 = new int[]{2, 3, 4, 0, -4, -3, -2, 0};
      char var3 = this.mem_cpu['ꀀ'];
      char var1 = this.mem_cpu['ꀁ'];
      char var2 = this.mem_cpu['ꀂ'];
      this.starsScroll -= var4[(var1 & 1) * 2 + (var3 & 1) + (var2 & 1) * 4];
   }

   class Star {
      public int col = 0;
      public int set = 0;
      public int x = 0;
      public int y = 0;
   }
}
