package net.movegaga.jemu2.driver.msx.v9938;

public class Screen0 implements ScreenMode {
   public void frame(V9938 var1) {
      int var2 = var1.RGB[var1.colForeGround];
      int var3 = var1.RGB[var1.colBackGround];
      int var4;
      if((var1.regControl[1] & 64) == 64) {
         var4 = 0 + 2720 + 16;
         int var6 = var1.addrChrTable;

         for(int var5 = 24; var5 != 0; var4 += 1936) {
            for(int var7 = 0; var7 < 40; ++var6) {
               int var10 = var1.addrChrPatterns;
               int var9 = var1.vram[var6];
               int var8 = 0;
               var10 += var9 << 3;
               var9 = var4;

               for(var4 = var10; var8 < 8; var9 = var10 + 267) {
                  int var11 = var1.vram[var4];
                  int[] var14 = var1.pixels;
                  int var12 = var9 + 1;
                  if((var11 & 128) == 128) {
                     var10 = var2;
                  } else {
                     var10 = var3;
                  }

                  var14[var9] = var10;
                  var14 = var1.pixels;
                  int var13 = var12 + 1;
                  if((var11 & 64) == 64) {
                     var9 = var2;
                  } else {
                     var9 = var3;
                  }

                  var14[var12] = var9;
                  var14 = var1.pixels;
                  var10 = var13 + 1;
                  if((var11 & 32) == 32) {
                     var9 = var2;
                  } else {
                     var9 = var3;
                  }

                  var14[var13] = var9;
                  var14 = var1.pixels;
                  var12 = var10 + 1;
                  if((var11 & 16) == 16) {
                     var9 = var2;
                  } else {
                     var9 = var3;
                  }

                  var14[var10] = var9;
                  var14 = var1.pixels;
                  var10 = var12 + 1;
                  if((var11 & 8) == 8) {
                     var9 = var2;
                  } else {
                     var9 = var3;
                  }

                  var14[var12] = var9;
                  var14 = var1.pixels;
                  if((var11 & 4) == 4) {
                     var9 = var2;
                  } else {
                     var9 = var3;
                  }

                  var14[var10] = var9;
                  ++var8;
                  ++var4;
               }

               var4 = var9 - 2170;
               ++var7;
            }

            --var5;
         }
      } else {
         var2 = 8;

         for(var3 = 0; var3 < 212; ++var3) {
            byte var15 = 0;
            var4 = var2;

            for(var2 = var15; var2 < 256; ++var4) {
               var1.pixels[var4] = 0;
               ++var2;
            }

            var2 = var4 + 16;
         }
      }

   }

   public void renderLine(V9938 var1, int var2) {
   }
}
