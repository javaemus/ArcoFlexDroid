package net.movegaga.jemu2.driver.msx.v9938;

public class Screen3 implements ScreenMode {
   public void frame(V9938 var1) {
      if((var1.regControl[1] & 64) == 64) {
         int var2 = 0 + 2720 + 8;
         int var4 = var1.addrChrTable;

         for(int var3 = 0; var3 < 24; var2 += 1920) {
            for(int var5 = 0; var5 < 32; ++var4) {
               int var7 = var1.addrColorTable + (var1.vram[var4] << 3) + ((var3 & 3) << 1);
               int var8 = var1.RGB[var1.vram[var7] >> 4];
               int var9 = var1.RGB[var1.vram[var7] & 15];

               int var6;
               int var10;
               int[] var12;
               for(var6 = 0; var6 < 4; var2 = var10 + 265) {
                  var12 = var1.pixels;
                  int var11 = var2 + 1;
                  var12[var2] = var8;
                  var12 = var1.pixels;
                  var10 = var11 + 1;
                  var12[var11] = var8;
                  var12 = var1.pixels;
                  var2 = var10 + 1;
                  var12[var10] = var8;
                  var12 = var1.pixels;
                  var11 = var2 + 1;
                  var12[var2] = var8;
                  var12 = var1.pixels;
                  var10 = var11 + 1;
                  var12[var11] = var9;
                  var12 = var1.pixels;
                  var2 = var10 + 1;
                  var12[var10] = var9;
                  var12 = var1.pixels;
                  var10 = var2 + 1;
                  var12[var2] = var9;
                  var1.pixels[var10] = var9;
                  ++var6;
               }

               var6 = var7 + 1;
               var8 = var1.RGB[var1.vram[var6] >> 4];
               var9 = var1.RGB[var1.vram[var6] & 15];
               byte var13 = 0;
               var6 = var2;

               for(var2 = var13; var2 < 4; var6 += 265) {
                  var12 = var1.pixels;
                  var7 = var6 + 1;
                  var12[var6] = var8;
                  var12 = var1.pixels;
                  var10 = var7 + 1;
                  var12[var7] = var8;
                  var12 = var1.pixels;
                  var6 = var10 + 1;
                  var12[var10] = var8;
                  var12 = var1.pixels;
                  var7 = var6 + 1;
                  var12[var6] = var8;
                  var12 = var1.pixels;
                  var6 = var7 + 1;
                  var12[var7] = var9;
                  var12 = var1.pixels;
                  var7 = var6 + 1;
                  var12[var6] = var9;
                  var12 = var1.pixels;
                  var6 = var7 + 1;
                  var12[var7] = var9;
                  var1.pixels[var6] = var9;
                  ++var2;
               }

               var2 = var6 - 2168;
               ++var5;
            }

            ++var3;
         }

         var1.renderSprites();
      }

   }

   public void renderLine(V9938 var1, int var2) {
   }
}
