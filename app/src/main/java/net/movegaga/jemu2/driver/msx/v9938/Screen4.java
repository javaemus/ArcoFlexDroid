package net.movegaga.jemu2.driver.msx.v9938;

public class Screen4 implements ScreenMode {
   SpriteMode2 sprites = new SpriteMode2();

   public void frame(V9938 var1) {
   }

   public void renderLine(V9938 var1, int var2) {
      int var3;
      if((var1.regControl[1] & 64) == 64 && var2 < var1.linesPerField) {
         int var14 = (var1.regControl[23] & 255) + var2 & 255;
         int var7 = var1.RGB[var1.colBackGround];
         int var13 = var14 >> 3;
         int var10 = var1.regControl[4];
         int var15 = var1.regControl[10];
         int var11 = var1.regControl[3];
         int var12 = var1.regControl[2];
         int var5 = 16;
         int var4 = var1.adjustHOR;
         var3 = 0;

         int var6;
         for(var6 = var2 * 272; var3 < 8 - var4; ++var6) {
            var1.pixels[var6] = 0;
            --var5;
            ++var3;
         }

         for(int var8 = 0; var8 < 256; var8 += 8) {
            var3 = (var1.vram[(var8 >> 3) + ((var12 & 127) << 10) + (var13 << 5)] + (var13 >> 3) * 256 << 3) + (var14 & 7);
            int var16 = var1.vram[((var10 & 60) << 11) + var3];
            var3 = var1.vram[((var15 & 7) << 14 | (var11 & 128) << 6) + var3];
            var4 = var3 >> 4;
            var3 &= 15;
            if(var3 == 0) {
               var3 = var7;
            } else {
               var3 = var1.RGB[var3];
            }

            if(var4 == 0) {
               var4 = var7;
            } else {
               var4 = var1.RGB[var4];
            }

            int[] var18 = var1.pixels;
            int var17 = var6 + 1;
            int var9;
            if((var16 & 128) != 0) {
               var9 = var4;
            } else {
               var9 = var3;
            }

            var18[var6] = var9;
            var18 = var1.pixels;
            var9 = var17 + 1;
            if((var16 & 64) != 0) {
               var6 = var4;
            } else {
               var6 = var3;
            }

            var18[var17] = var6;
            var18 = var1.pixels;
            var17 = var9 + 1;
            if((var16 & 32) != 0) {
               var6 = var4;
            } else {
               var6 = var3;
            }

            var18[var9] = var6;
            var18 = var1.pixels;
            var9 = var17 + 1;
            if((var16 & 16) != 0) {
               var6 = var4;
            } else {
               var6 = var3;
            }

            var18[var17] = var6;
            var18 = var1.pixels;
            var17 = var9 + 1;
            if((var16 & 8) != 0) {
               var6 = var4;
            } else {
               var6 = var3;
            }

            var18[var9] = var6;
            var18 = var1.pixels;
            var9 = var17 + 1;
            if((var16 & 4) != 0) {
               var6 = var4;
            } else {
               var6 = var3;
            }

            var18[var17] = var6;
            var18 = var1.pixels;
            var17 = var9 + 1;
            if((var16 & 2) != 0) {
               var6 = var4;
            } else {
               var6 = var3;
            }

            var18[var9] = var6;
            var18 = var1.pixels;
            var6 = var17 + 1;
            if((var16 & 1) != 0) {
               var3 = var4;
            }

            var18[var17] = var3;
         }

         while(var5 > 0) {
            var1.pixels[var6] = 0;
            --var5;
            ++var6;
         }

         this.sprites.renderLine(var1, var2);
      } else {
         for(var3 = 0; var3 < 272; ++var3) {
            var1.pixels[var2 * 272 + var3] = 0;
         }
      }

   }
}
