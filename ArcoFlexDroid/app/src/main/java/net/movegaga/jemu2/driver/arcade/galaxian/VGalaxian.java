package net.movegaga.jemu2.driver.arcade.galaxian;

import net.movegaga.jemu2.driver.BaseVideo;

import jef.machine.EmulatorProperties;
import jef.map.InterruptHandler;
import jef.map.ReadHandler;
import jef.map.WriteHandler;
import jef.video.BitMap;
import jef.video.BitMapImpl;
import jef.video.PaletteInitializer;
import jef.video.VideoEmulator;
import jef.video.VideoRenderer;

public class VGalaxian extends BaseVideo implements VideoEmulator, VideoRenderer, PaletteInitializer, ReadHandler, WriteHandler, InterruptHandler {
   static final int[] MAP;
   static final int MAX_STARS = 250;
   BitMap backBuffer;
   int baseAttributes;
   int baseBullets;
   int baseSprites;
   int baseVideo;
   int blinkCount = 0;
   BitMap charLayer;
   int colorMask;
   boolean[] dirty = new boolean[1024];
   boolean doDrawBullets = true;
   Draw drawBullets;
   int generator = 0;
   boolean irqEnabled = false;
   int piscesGfxBank = 0;
   char[] proms;
   char[] ram;
   int[] starCode = new int[250];
   int[] starX = new int[250];
   int[] starY = new int[250];
   boolean starsOn = false;
   int starsScroll = 0;
   int starsType = 0;
   int stars_blink = 0;
   int totalStars = 0;

   static {
      int[] var0 = new int[]{0, 136, 204, 255};
      MAP = var0;
   }

   private void plotStar(int var1, int var2, int var3) {
      if((this.backBuffer.getPixel(var2, var1) & 16777215) == 0) {
         this.backBuffer.setPixel(var2, var1, this.getPaletteColor(var3));
      }

   }

   public void drawStars() {
      if(this.starsOn) {
         int var1;
         int var2;
         int var3;
         switch(this.starsType) {
         case -1:
         default:
            break;
         case 0:
         case 3:
            for(var1 = 0; var1 < this.totalStars; ++var1) {
               var2 = (this.starX[var1] + this.starsScroll) % 512 / 2;
               var3 = (this.starY[var1] + (this.starsScroll + this.starX[var1]) / 512) % 256;
               if(var3 >= 16 && var3 <= 239 && (this.starsType != 3 || var2 >= 64 && (var2 < 176 || var2 >= 216) && (var2 < 224 || var2 >= 248)) && (var3 & 1 ^ var2 >> 4 & 1) != 0) {
                  this.plotStar(var2, var3, this.starCode[var1]);
               }
            }

            return;
         case 1:
         case 2:
            for(var1 = 0; var1 < this.totalStars; ++var1) {
               var2 = this.starX[var1] / 2;
               var3 = this.starY[var1];
               if(var3 >= 16 && var3 <= 239 && (this.starsType != 2 || var2 < 128) && (var3 & 1 ^ var2 >> 4 & 1) != 0) {
                  switch(this.stars_blink) {
                  case 0:
                     if((this.starCode[var1] & 1) == 0) {
                        continue;
                     }
                     break;
                  case 1:
                     if((this.starCode[var1] & 4) == 0) {
                        continue;
                     }
                     break;
                  case 2:
                     if((this.starX[var1] & 4) == 0) {
                        continue;
                     }
                  }

                  this.plotStar(var2, var3, this.starCode[var1]);
               }
            }
         }
      }

   }

   public void galaxian() {
      this.starsType = 0;
      this.baseVideo = 20480;
      this.baseAttributes = 22528;
      this.baseBullets = 22624;
      this.baseSprites = 22592;
      this.drawBullets = new DrawBulletsGalaxian();
   }

   public void init(EmulatorProperties var1) {
      super.init(var1);
      this.backBuffer = this.getBackBuffer();
      this.charLayer = new BitMapImpl(this.backBuffer.getWidth(), this.backBuffer.getHeight());
   }

   public void initPalette() {
      byte var1;
      if(super.getColorGranularity(0) == 4) {
         var1 = 7;
      } else {
         var1 = 3;
      }

      this.colorMask = var1;
      int var2 = 0;

      int var3;
      int var6;
      for(var6 = 0; var2 < 32; ++var6) {
         char var5 = this.proms[var6];
         int var4 = (var5 & 7) << 5;
         var3 = (var5 >> 3 & 7) << 5;
         int var7 = (var5 >> 6 & 3) << 6;
         this.setPaletteColor(var6, var4 >> 3 | var4 | var4 >> 6, var3 >> 3 | var3 | var3 >> 6, var7 >> 2 | var7 | var7 >> 4 | var7 >> 6);
         ++var2;
      }

      for(var2 = 0; var2 < super.getTotalColors(0); ++var2) {
         if((super.getColorGranularity(0) - 1 & var2) == 0) {
            this.setColor(0, var2, 0);
         }
      }

      for(var2 = 0; var2 < 64; ++var2) {
         this.setPaletteColor(var6, MAP[var2 >> 0 & 3] << 16);
         this.setPaletteColor(var6, MAP[var2 >> 2 & 3] << 8);
         this.setPaletteColor(var6, MAP[var2 >> 4 & 3]);
      }

      this.totalStars = 0;
      this.generator = 0;

      for(var6 = 255; var6 >= 0; --var6) {
         for(var2 = 511; var2 >= 0; --var2) {
            this.generator <<= 1;
            if((~this.generator >> 17 & 1 ^ this.generator >> 5 & 1) != 0) {
               this.generator |= 1;
            }

            if((~this.generator >> 16 & 1) != 0 && (this.generator & 255) == 255) {
               var3 = ~(this.generator >> 8) & 63;
               if(var3 != 0 && this.totalStars < 250) {
                  this.starX[this.totalStars] = var2;
                  this.starY[this.totalStars] = var6;
                  this.starCode[this.totalStars] = var3;
                  ++this.totalStars;
               }
            }
         }
      }

   }

   public InterruptHandler interruptScramble() {
      return new InterruptScramble();
   }

   public int irq() {
      ++this.starsScroll;
      byte var1;
      if(this.irqEnabled) {
         var1 = 1;
      } else {
         var1 = -1;
      }

      return var1;
   }

   public int read(int var1) {
      return this.ram[var1 - 1024];
   }

   public BitMap renderVideo() {
      int var1;
      int var2;
      char var3;
      char var5;
      for(var1 = 0; var1 < 32; ++var1) {
         var5 = this.ram[this.baseAttributes + (var1 << 1)];
         var3 = this.ram[this.baseAttributes + (var1 << 1) | 1];
         int var4 = this.colorMask;

         for(var2 = 0; var2 < 32; ++var2) {
            char var6 = this.ram[this.baseVideo + (var2 << 5) | var1];
            this.drawVisible(this.backBuffer, 0, var6, var3 & var4, false, false, var1 * 8, var2 * 8 - var5 & 255, -1, 0);
         }
      }

      char var10;
      if(this.doDrawBullets) {
         for(var1 = 0; var1 < 32; var1 += 4) {
            var10 = this.ram[this.baseBullets + var1 + 1];
            var3 = this.ram[this.baseBullets + var1 + 3];
            this.drawBullets.draw(var1, 255 - var3, 255 - var10);
         }
      }

      for(var1 = 28; var1 >= 0; var1 -= 4) {
         int var11 = this.baseSprites + var1;
         char var12 = this.ram[var11 + 1];
         var5 = this.ram[var11 + 3];
         var10 = this.ram[var11];
         boolean var8;
         if((var12 & 64) != 0) {
            var8 = true;
         } else {
            var8 = false;
         }

         boolean var9;
         if((var12 & 128) != 0) {
            var9 = true;
         } else {
            var9 = false;
         }

         char var7 = this.ram[var11 + 2];
         int var13 = this.colorMask;
         var11 = 240 - var10;
         var2 = var11;
         if(var1 < 12) {
            var2 = var11 + 1;
         }

         this.drawVisible(this.backBuffer, 1, var12, var7 & var13, var8, var9, var5 + 1, var2, 1, 0);
      }

      this.drawStars();
      return this.backBuffer;
   }

   public void scramble() {
      this.starsType = 1;
      this.baseVideo = 18432;
      this.baseAttributes = 20480;
      this.baseBullets = 20576;
      this.baseSprites = 20544;
      this.drawBullets = new DrawBulletsScramble();
   }

   public void setRegions(char[] var1, char[] var2) {
      this.proms = var1;
      this.ram = var2;
   }

   public void write(int var1, int var2) {
      this.ram[var1] = (char)var2;
      this.dirty[var1 & 1023] = true;
   }

   public WriteHandler writeAttributes() {
      return new WriteAttributes();
   }

   public WriteHandler writeInterruptEnable() {
      return new WriteInterruptEnable();
   }

   public WriteHandler writePiscesGfxBank() {
      return new WritePiscesGfxBank();
   }

   public WriteHandler writeStars() {
      return new WriteStars();
   }

   public void zigzag() {
      this.starsType = 1;
      this.baseVideo = 20480;
      this.baseAttributes = 22528;
      this.baseSprites = 22592;
      this.doDrawBullets = false;
   }

   interface Draw {
      void draw(int var1, int var2, int var3);
   }

   class DrawBulletsGalaxian implements Draw {
      public void draw(int var1, int var2, int var3) {
         byte var5 = 0;
         int var4 = var2;

         int var6;
         for(var2 = var5; var2 < 4; var4 = var6) {
            var6 = var4 - 1;
            if(var6 >= 0 && var6 <= 255) {
               if(var1 == 28) {
                  var4 = 16776960;
               } else {
                  var4 = 16777215;
               }

               VGalaxian.this.backBuffer.setPixel(239 - var3, var6, var4);
            }

            ++var2;
         }

      }
   }

   class DrawBulletsScramble implements Draw {
      public void draw(int var1, int var2, int var3) {
         var1 = var2 - 6;
         if(var1 >= 0 && var1 <= 255) {
            VGalaxian.this.backBuffer.setPixel(239 - var3, var1, 16776960);
         }

      }
   }

   public class InterruptScramble implements InterruptHandler {
      public int irq() {
         VGalaxian var2 = VGalaxian.this;
         ++var2.blinkCount;
         if(VGalaxian.this.blinkCount >= 45) {
            VGalaxian.this.blinkCount = 0;
            VGalaxian.this.stars_blink = VGalaxian.this.stars_blink + 1 & 3;
         }

         byte var1;
         if(VGalaxian.this.irqEnabled) {
            var1 = 1;
         } else {
            var1 = -1;
         }

         return var1;
      }
   }

   public class WriteAttributes implements WriteHandler {
      public void write(int var1, int var2) {
         int var3 = VGalaxian.this.baseAttributes;
         if((var1 & 1) != 0 && VGalaxian.this.ram[var1] != var2) {
            for(var3 = (var1 - var3) / 2; var3 < 1024; var3 += 32) {
               VGalaxian.this.dirty[var3] = true;
            }
         }

         VGalaxian.this.ram[var1] = (char)var2;
      }
   }

   public class WriteInterruptEnable implements WriteHandler {
      public void write(int var1, int var2) {
         VGalaxian var4 = VGalaxian.this;
         boolean var3;
         if(var2 != 0) {
            var3 = true;
         } else {
            var3 = false;
         }

         var4.irqEnabled = var3;
      }
   }

   public class WritePiscesGfxBank implements WriteHandler {
      public void write(int var1, int var2) {
         VGalaxian.this.piscesGfxBank = var2 & 1;
      }
   }

   public class WriteStars implements WriteHandler {
      public void write(int var1, int var2) {
         VGalaxian var4 = VGalaxian.this;
         boolean var3;
         if(var2 != 0) {
            var3 = true;
         } else {
            var3 = false;
         }

         var4.starsOn = var3;
         VGalaxian.this.starsScroll = 0;
      }
   }
}
