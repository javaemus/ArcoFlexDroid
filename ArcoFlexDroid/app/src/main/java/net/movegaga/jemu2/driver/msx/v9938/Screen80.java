package net.movegaga.jemu2.driver.msx.v9938;

public class Screen80 implements ScreenMode {
   public void frame(V9938 var1) {
   }

   public void renderLine(V9938 var1, int var2) {
      int var6 = (var2 >> 3) * 80;
      int var3 = var1.RGB[var1.colBackGround];
      int var4 = var1.RGB[var1.colForeGround];
      int var5 = 0;

      int var8;
      for(int var7 = var2 * 272 + 8; var5 < 240; var7 = var8 + 1) {
         int var9 = var1.vram[var1.vram[var6] * 8 + 4096 + (var2 & 7)];
         int[] var11 = var1.pixels;
         int var10 = var7 + 1;
         if((var9 & 128) == 128) {
            var8 = var4;
         } else {
            var8 = var3;
         }

         var11[var7] = var8;
         var11 = var1.pixels;
         var8 = var10 + 1;
         if((var9 & 32) == 32) {
            var7 = var4;
         } else {
            var7 = var3;
         }

         var11[var10] = var7;
         var11 = var1.pixels;
         if((var9 & 8) == 8) {
            var7 = var4;
         } else {
            var7 = var3;
         }

         var11[var8] = var7;
         var5 += 3;
         ++var6;
      }

   }
}
