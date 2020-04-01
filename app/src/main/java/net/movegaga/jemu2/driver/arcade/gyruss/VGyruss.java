package net.movegaga.jemu2.driver.arcade.gyruss;

import net.movegaga.jemu2.driver.BaseVideo;

import jef.machine.EmulatorProperties;
import jef.map.WriteHandler;
import jef.video.BitMap;
import jef.video.BitMapImpl;
import jef.video.PaletteInitializer;
import jef.video.VideoEmulator;
import jef.video.VideoRenderer;

public class VGyruss extends BaseVideo implements VideoEmulator, VideoRenderer, PaletteInitializer {
   char[] REGION_CPU;
   char[] REGION_CPU4;
   char[] REGION_PROMS;
   BitMap charLayer;
   boolean[] dirtybuffer = new boolean[1024];
   int gyruss_spritebank = 0;

   public int SprTrans(int var1) {
      char var3 = this.REGION_CPU4[this.REGION_CPU[var1] + '\ue000'];
      int var2 = this.REGION_CPU[var1 + 3] * 2;
      this.REGION_CPU[var1] = (char)(this.REGION_CPU4['\ue600' + var2 + 1] * var3 >> 8);
      byte var5;
      if(this.REGION_CPU[var1] >= 128) {
         this.REGION_CPU[var1 + 3] = 0;
         var5 = 0;
      } else {
         if(this.REGION_CPU4['\ue600' + var2] != 0) {
            if(this.REGION_CPU[var1] >= 120) {
               this.REGION_CPU[var1 + 3] = 0;
               var5 = 0;
               return var5;
            }

            this.REGION_CPU[var1] = (char)(-this.REGION_CPU[var1] & 255);
         }

         this.REGION_CPU[var1 + 3] = (char)(this.REGION_CPU4['\ue400' + var2 + 1] * var3 >> 8);
         if(this.REGION_CPU[var1 + 3] >= 128) {
            this.REGION_CPU[var1 + 3] = 0;
            var5 = 0;
         } else {
            if(this.REGION_CPU4['\ue400' + var2] != 0) {
               this.REGION_CPU[var1 + 3] = (char)(-this.REGION_CPU[var1 + 3] & 255);
            }

            char[] var4;
            if((this.REGION_CPU[var1 + 2] & 16) != 0) {
               var4 = this.REGION_CPU;
               var4[var1] = (char)(var4[var1] + 120);
            } else {
               var4 = this.REGION_CPU;
               var4[var1] = (char)(var4[var1] + 124);
            }

            var4 = this.REGION_CPU;
            var1 += 3;
            var4[var1] = (char)(var4[var1] + 120);
            var5 = 1;
         }
      }

      return var5;
   }

   public boolean gyruss() {
      return true;
   }

   public WriteHandler gyruss_queuereg_w(VGyruss var1) {
      return new Gyruss_queuereg_w(var1);
   }

   public WriteHandler gyruss_spritebank_w(VGyruss var1) {
      return new Gyruss_spritebank_w(var1);
   }

   public void init(EmulatorProperties var1) {
      super.init(var1);
      this.charLayer = new BitMapImpl(this.getBackBuffer().getWidth(), this.getBackBuffer().getHeight());
   }

   public void initPalette() {
      System.out.println("Converting color proms...");
      int var1 = 0;

      int var2;
      for(var2 = 0; var2 < this.getTotalColors(); ++var1) {
         this.setPaletteColor(var1, (this.REGION_PROMS[var1] >> 0 & 1) * 33 + (this.REGION_PROMS[var1] >> 1 & 1) * 71 + (this.REGION_PROMS[var1] >> 2 & 1) * 151, (this.REGION_PROMS[var1] >> 3 & 1) * 33 + (this.REGION_PROMS[var1] >> 4 & 1) * 71 + (this.REGION_PROMS[var1] >> 5 & 1) * 151, (this.REGION_PROMS[var1] >> 6 & 1) * 71 + 0 + (this.REGION_PROMS[var1] >> 7 & 1) * 151);
         ++var2;
      }

      for(var2 = 0; var2 < this.getTotalColors(1); ++var1) {
         this.setColor(1, var2, this.REGION_PROMS[var1] & 15);
         this.setColor(2, var2, this.REGION_PROMS[var1] & 15);
         this.setColor(3, var2, this.REGION_PROMS[var1] & 15);
         ++var2;
      }

      for(var2 = 0; var2 < this.getTotalColors(0); ++var1) {
         this.setColor(0, var2, (this.REGION_PROMS[var1] & 15) + 16);
         ++var2;
      }

   }

   public void queuereg_w(int var1) {
      if(var1 == 1) {
         char var3;
         if(this.gyruss_spritebank == 0) {
            var3 = 'ꀀ';
         } else {
            var3 = 'ꈀ';
         }

         int var2;
         if(this.REGION_CPU['ꟼ'] != 0) {
            this.SprTrans(var3 + 16);
            this.REGION_CPU[var3 + 20 + 3] = 0;
         } else {
            for(var2 = 4; var2 < 24; var2 += 2) {
               this.SprTrans(var2 * 4 + var3);
               this.REGION_CPU[(var2 + 1) * 4 + var3 + 3] = 0;
            }
         }

         for(var2 = 24; var2 < 60; ++var2) {
            this.SprTrans(var2 * 4 + var3);
         }

         if(this.REGION_CPU['ꟽ'] == 0) {
            for(var2 = 64; var2 < 78; ++var2) {
               this.SprTrans(var2 * 4 + var3);
            }
         }

         for(var2 = 78; var2 < 86; ++var2) {
            if(this.SprTrans(var2 * 4 + var3) != 0) {
               this.REGION_CPU[(var2 + 8) * 4 + var3 + 3] = (char)(this.REGION_CPU[var2 * 4 + var3 + 3] - 4 & 255);
               this.REGION_CPU[(var2 + 8) * 4 + var3 + 0] = (char)(this.REGION_CPU[var2 * 4 + var3] + 4 & 255);
            } else {
               this.REGION_CPU[(var2 + 8) * 4 + var3 + 3] = 0;
            }
         }
      }

   }

   public BitMap renderVideo() {
      char var3;
      char var5;
      char var6;
      boolean var8;
      boolean var9;
      BitMap var10;
      for(int var1 = 1023; var1 >= 0; --var1) {
         if(this.dirtybuffer[var1]) {
            this.dirtybuffer[var1] = false;
            int var7 = var1 / 32;
            var6 = this.REGION_CPU['耀' + var1];
            var5 = this.REGION_CPU['耀' + var1];
            var10 = this.charLayer;
            char var2 = this.REGION_CPU['萀' + var1];
            var3 = this.REGION_CPU['耀' + var1];
            char var4 = this.REGION_CPU['耀' + var1];
            if((var6 & 64) != 0) {
               var8 = true;
            } else {
               var8 = false;
            }

            if((var5 & 128) != 0) {
               var9 = true;
            } else {
               var9 = false;
            }

            this.draw(var10, 0, (var3 & 32) * 8 + var2, var4 & 15, var8, var9, var1 % 32 * 8, var7 * 8, -1, 0);
         }
      }

      this.charLayer.toPixels(this.getBackBuffer().getPixels());
      char var11;
      if(this.gyruss_spritebank == 0) {
         var11 = 'ꀀ';
      } else {
         var11 = 'ꈀ';
      }

      for(int var12 = 376; var12 >= 0; var12 -= 8) {
         if(this.REGION_CPU[var11 + var12 + 0] != 0) {
            var10 = this.getBackBuffer();
            var5 = this.REGION_CPU[var11 + var12 + 1];
            int var13 = this.REGION_CPU[var11 + var12 + 1] / 2;
            var6 = this.REGION_CPU[var11 + var12 + 2];
            var3 = this.REGION_CPU[var11 + var12 + 2];
            if((this.REGION_CPU[var11 + var12 + 2] & 64) != 0) {
               var8 = false;
            } else {
               var8 = true;
            }

            if((this.REGION_CPU[var11 + var12 + 2] & 128) != 0) {
               var9 = true;
            } else {
               var9 = false;
            }

            this.draw(var10, (var5 & 1) + 1, (var6 & 32) * 4 + var13, var3 & 15, var8, var9, this.REGION_CPU[var11 + var12 + 0], 241 - this.REGION_CPU[var11 + var12 + 3], 1, 0);
         }
      }

      return this.getBackBuffer();
   }

   public void setRegions(char[] var1, char[] var2, char[] var3) {
      this.REGION_PROMS = var1;
      this.REGION_CPU = var2;
      this.REGION_CPU4 = var3;
   }

   public WriteHandler videoram_w(char[] var1, VGyruss var2) {
      return new Videoram_w(var1, var2);
   }

   public void writeVRAM(int var1, int var2, int var3) {
      this.dirtybuffer[var2 & 1023] = true;
   }

   public class Gyruss_queuereg_w implements WriteHandler {
      VGyruss video;

      public Gyruss_queuereg_w(VGyruss var2) {
         this.video = var2;
      }

      public void write(int var1, int var2) {
         this.video.queuereg_w(var2);
      }
   }

   public class Gyruss_spritebank_w implements WriteHandler {
      VGyruss video;

      public Gyruss_spritebank_w(VGyruss var2) {
         this.video = var2;
      }

      public void write(int var1, int var2) {
         VGyruss var5 = this.video;
         char[] var4 = this.video.REGION_CPU;
         char var3 = (char)var2;
         var4[var1] = var3;
         var5.gyruss_spritebank = var3;
      }
   }

   public class Videoram_w implements WriteHandler {
      char[] mem;
      VGyruss video;

      public Videoram_w(char[] var2, VGyruss var3) {
         this.mem = var2;
         this.video = var3;
      }

      public void write(int var1, int var2) {
         this.mem[var1] = (char)var2;
         this.video.writeVRAM(0, var1, var2);
      }
   }
}
