package net.movegaga.jemu2.driver.msx.v9938;

public class Screen2 implements ScreenMode {
   int[] cols = new int[2];
   SpriteMode1 sprites = new SpriteMode1();

   public void frame(V9938 var1) {
      int var3 = var1.RGB[var1.colBackGround];
      int var2;
      int var4;
      if((var1.regControl[1] & 64) == 64) {
         var2 = 0 + 2720 + 8;
         int var5 = var1.addrChrPatterns;
         var4 = var1.addrColorTable;
         int var8 = var1.addrChrTable;

         for(int var6 = 0; var6 < 3; var4 += 2048) {
            for(int var7 = 8; var7 != 0; var2 += 1920) {
               for(int var9 = 0; var9 < 32; ++var8) {
                  int var12 = var1.vram[var8] << 3;
                  int var10 = 0;
                  int var11 = var4 + var12;
                  int var13 = var5 + var12;
                  var12 = var2;

                  for(var2 = var13; var10 < 8; var12 += 265) {
                     int var14 = var1.vram[var2];
                     int var15 = var1.vram[var11];
                     var13 = var15 >> 4;
                     var15 &= 15;
                     int[] var16 = this.cols;
                     if(var13 == 0) {
                        var13 = var3;
                     } else {
                        var13 = var1.RGB[var13];
                     }

                     var16[1] = var13;
                     var16 = this.cols;
                     if(var15 == 0) {
                        var13 = var3;
                     } else {
                        var13 = var1.RGB[var15];
                     }

                     var16[0] = var13;
                     var16 = var1.pixels;
                     var15 = var12 + 1;
                     var16[var12] = this.cols[var14 >> 7];
                     var16 = var1.pixels;
                     var13 = var15 + 1;
                     var16[var15] = this.cols[var14 >> 6 & 1];
                     var16 = var1.pixels;
                     var12 = var13 + 1;
                     var16[var13] = this.cols[var14 >> 5 & 1];
                     var16 = var1.pixels;
                     var13 = var12 + 1;
                     var16[var12] = this.cols[var14 >> 4 & 1];
                     var16 = var1.pixels;
                     var12 = var13 + 1;
                     var16[var13] = this.cols[var14 >> 3 & 1];
                     var16 = var1.pixels;
                     var13 = var12 + 1;
                     var16[var12] = this.cols[var14 >> 2 & 1];
                     var16 = var1.pixels;
                     var12 = var13 + 1;
                     var16[var13] = this.cols[var14 >> 1 & 1];
                     var1.pixels[var12] = this.cols[var14 & 1];
                     ++var10;
                     ++var11;
                     ++var2;
                  }

                  var2 = var12 - 2168;
                  ++var9;
               }

               --var7;
            }

            ++var6;
            var5 += 2048;
         }

         if(!var1.skipSprites) {
            var1.renderSprites();
         }
      } else {
         var2 = 8;

         for(var3 = 0; var3 < 212; ++var3) {
            byte var17 = 0;
            var4 = var2;

            for(var2 = var17; var2 < 256; ++var4) {
               var1.pixels[var4] = 0;
               ++var2;
            }

            var2 = var4 + 16;
         }
      }

   }

   public void renderLine(V9938 var1, int var2) {
      int var3;
      if((var1.regControl[1] & 64) == 64 && var2 >= 10 && var2 < 202) {
         int var9 = var2 - 10;
         int var10 = (var1.regControl[23] & 255) + var9 & 255;
         int var6 = var1.RGB[var1.colBackGround];
         int var14 = var10 >> 3;
         int var15 = var1.regControl[4];
         int var12 = var1.regControl[10];
         int var11 = var1.regControl[3];
         int var13 = var1.regControl[2];
         int var4 = 16;
         var3 = var1.adjustHOR;
         var2 = 0;

         int var5;
         for(var5 = (var9 + 10) * 272; var2 < 8 - var3; ++var5) {
            var1.pixels[var5] = 0;
            --var4;
            ++var2;
         }

         for(int var7 = 0; var7 < 256; var7 += 8) {
            var2 = (var1.vram[(var7 >> 3) + ((var13 & 127) << 10) + (var14 << 5)] + (var14 >> 3) * 256 << 3) + (var10 & 7);
            int var16 = var1.vram[((var15 & 60) << 11) + var2];
            var2 = var1.vram[((var12 & 7) << 14 | (var11 & 128) << 6) + var2];
            var3 = var2 >> 4;
            var2 &= 15;
            if(var2 == 0) {
               var2 = var6;
            } else {
               var2 = var1.RGB[var2];
            }

            if(var3 == 0) {
               var3 = var6;
            } else {
               var3 = var1.RGB[var3];
            }

            int[] var18 = var1.pixels;
            int var17 = var5 + 1;
            int var8;
            if((var16 & 128) != 0) {
               var8 = var3;
            } else {
               var8 = var2;
            }

            var18[var5] = var8;
            var18 = var1.pixels;
            var8 = var17 + 1;
            if((var16 & 64) != 0) {
               var5 = var3;
            } else {
               var5 = var2;
            }

            var18[var17] = var5;
            var18 = var1.pixels;
            var17 = var8 + 1;
            if((var16 & 32) != 0) {
               var5 = var3;
            } else {
               var5 = var2;
            }

            var18[var8] = var5;
            var18 = var1.pixels;
            var8 = var17 + 1;
            if((var16 & 16) != 0) {
               var5 = var3;
            } else {
               var5 = var2;
            }

            var18[var17] = var5;
            var18 = var1.pixels;
            var17 = var8 + 1;
            if((var16 & 8) != 0) {
               var5 = var3;
            } else {
               var5 = var2;
            }

            var18[var8] = var5;
            var18 = var1.pixels;
            var8 = var17 + 1;
            if((var16 & 4) != 0) {
               var5 = var3;
            } else {
               var5 = var2;
            }

            var18[var17] = var5;
            var18 = var1.pixels;
            var17 = var8 + 1;
            if((var16 & 2) != 0) {
               var5 = var3;
            } else {
               var5 = var2;
            }

            var18[var8] = var5;
            var18 = var1.pixels;
            var5 = var17 + 1;
            if((var16 & 1) != 0) {
               var2 = var3;
            }

            var18[var17] = var2;
         }

         while(var4 > 0) {
            var1.pixels[var5] = 0;
            --var4;
            ++var5;
         }

         if(var1.lineCallBack != null) {
            var1.lineCallBack.exec(var9);
         } else {
            this.sprites.renderLine(var1, var9);
         }
      } else {
         for(var3 = 0; var3 < 272; ++var3) {
            var1.pixels[var2 * 272 + var3] = 0;
         }
      }

   }
}
