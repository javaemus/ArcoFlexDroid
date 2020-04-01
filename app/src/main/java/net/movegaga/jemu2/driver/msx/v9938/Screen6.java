package net.movegaga.jemu2.driver.msx.v9938;

public class Screen6 implements ScreenMode {
   static final boolean BLEND = true;

   private static final int blend(int var0, int var1) {
      return (var0 & 16711680) + (var1 & 16711680) >> 1 & 16711680 | (var0 & '\uff00') + (var1 & '\uff00') >> 1 & '\uff00' | var0 + var1 >> 1;
   }

   public void frame(V9938 var1) {
      int var2 = 8;
      int var3;
      int var4;
      if((var1.regControl[1] & 64) == 64) {
         int var7 = var1.regControl[2];
         int var8 = var1.regControl[23];

         for(var3 = 0; var3 < 212; ++var3) {
            byte var6 = 0;
            int var5 = ((var7 & 96) << 10) + ((var3 + (var8 & 255)) % 256 << 7);
            var4 = var2;

            for(var2 = var6; var2 < 256; ++var5) {
               int var11 = var1.vram[var5];
               int[] var10 = var1.pixels;
               int var9 = var4 + 1;
               var10[var4] = blend(var1.RGB[var11 >> 6], var1.RGB[var11 >> 4 & 3]);
               var10 = var1.pixels;
               var4 = var9 + 1;
               var10[var9] = blend(var1.RGB[var11 >> 2 & 3], var1.RGB[var11 & 3]);
               var2 += 2;
            }

            var2 = var4 + 16;
         }
      } else {
         for(var3 = 0; var3 < 212; ++var3) {
            for(var4 = 0; var4 < 256; ++var2) {
               var1.pixels[var2] = 0;
               ++var4;
            }

            var2 += 16;
         }
      }

   }

   public void renderLine(V9938 var1, int var2) {
   }
}
