package net.movegaga.jemu2.driver.msx.v9938;

public class Screen7 implements ScreenMode {
   static final boolean BLEND = true;
   SpriteMode2 sprites = new SpriteMode2();

   private static final int blend(int var0, int var1) {
      return (var0 & 16711680) + (var1 & 16711680) >> 1 & 16711680 | (var0 & '\uff00') + (var1 & '\uff00') >> 1 & '\uff00' | var0 + var1 >> 1;
   }

   public void frame(V9938 var1) {
      int var2 = 8;
      int var3;
      int var4;
      if((var1.regControl[1] & 64) == 64) {
         int var6 = var1.regControl[2];
         int var7 = var1.regControl[23];

         for(var3 = 0; var3 < 212; ++var3) {
            int var5 = 0;

            for(var4 = ((var6 & 64) << 11) + ((var3 + (var7 & 255)) % 256 << 8); var5 < 256; ++var2) {
               int var8 = var1.vram[var4];
               var1.pixels[var2] = blend(var1.RGB[var8 >> 4], var1.RGB[var8 & 15]);
               ++var5;
               ++var4;
            }

            var2 += 16;
            this.sprites.renderLine(var1, var3);
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
