package net.movegaga.jemu2.driver.msx.v9938;

public class Screen5 implements ScreenMode {
   SpriteMode2 sprites = new SpriteMode2();

   public void frame(V9938 var1) {
   }

   public void renderLine(V9938 var1, int var2) {
      if(var2 < 212) {
         int var4 = var2 * 272;
         if((var1.regControl[1] & 64) == 64 && var2 < var1.linesPerField) {
            int var5 = var1.RGB[var1.colBackGround];
            int var3 = 16;
            int var7 = var1.adjustHOR;

            int var6;
            for(var6 = 0; var6 < 8 - var7; ++var4) {
               var1.pixels[var4] = 0;
               --var3;
               ++var6;
            }

            int var8 = var1.regControl[2];
            var7 = var1.regControl[23];
            var6 = 0;

            for(var7 = ((var8 & 96) << 10) + ((var2 + (var7 & 255)) % 256 << 7); var6 < 256; ++var7) {
               int var9 = var1.vram[var7];
               var8 = var9 >> 4;
               var9 &= 15;
               if(var8 == 0) {
                  var8 = var5;
               } else {
                  var8 = var1.RGB[var8];
               }

               if(var9 == 0) {
                  var9 = var5;
               } else {
                  var9 = var1.RGB[var9];
               }

               int[] var11 = var1.pixels;
               int var10 = var4 + 1;
               var11[var4] = var8;
               var11 = var1.pixels;
               var4 = var10 + 1;
               var11[var10] = var9;
               var6 += 2;
            }

            while(var3 > 0) {
               var1.pixels[var4] = 0;
               --var3;
               ++var4;
            }

            this.sprites.renderLine(var1, var2);
         } else {
            for(var2 = 0; var2 < 272; ++var4) {
               var1.pixels[var4] = 0;
               ++var2;
            }
         }
      }

   }
}
