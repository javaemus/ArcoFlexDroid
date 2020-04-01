package net.movegaga.jemu2.driver.msx.v9938;

import net.movegaga.jemu2.JEmu2;

public class Screen8 implements ScreenMode {
   SpriteMode2 sprites = new SpriteMode2();

   public void frame(V9938 var1) {
   }

   public void renderLine(V9938 var1, int var2) {
      if(var2 < 212) {
         int var5 = var1.regControl[2];
         int var6 = var1.regControl[23];
         int var4 = 0;
         int var3 = var2 * 272 + 8;
         var5 = ((var5 & 64) << 11) + ((var2 + (var6 & 255)) % 256 << 8);
         var2 = var3;

         for(var3 = var5; var4 < 256; ++var3) {
            int var7 = var1.vram[var3];
            var6 = (var7 & 30) << 3;
            var5 = (var7 & 224) << 0;
            var7 = (var7 & 3) << 6;
            if(JEmu2.IS_APPLET) {
               var5 = var6 << 16 | var5 << 8 | var7;
            } else {
               var5 = var7 << 16 | var5 << 8 | var6;
            }

            var1.pixels[var2] = var5;
            ++var4;
            ++var2;
         }

         this.sprites.render(var1);
      }

   }
}
