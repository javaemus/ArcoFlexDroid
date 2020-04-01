package net.movegaga.jemu2.driver.sms.video;

import net.movegaga.jemu2.JEmu2;

public class Palette {
   public int[] rgb = new int[32];

   public int rgb(int var1, int var2) {
      byte var3;
      if(var1 != 0) {
         var3 = 16;
      } else {
         var3 = 0;
      }

      return this.rgb[var3 + var2];
   }

   public void setPalette(int var1, int var2) {
      if(JEmu2.IS_APPLET) {
         this.rgb[var1] = (var2 & 3) * 85 << 16 | (var2 >> 2 & 3) * 85 << 8 | (var2 >> 4 & 3) * 85;
      } else {
         this.rgb[var1] = (var2 & 3) * 85 | (var2 >> 2 & 3) * 85 << 8 | (var2 >> 4 & 3) * 85 << 16;
      }

   }
}
