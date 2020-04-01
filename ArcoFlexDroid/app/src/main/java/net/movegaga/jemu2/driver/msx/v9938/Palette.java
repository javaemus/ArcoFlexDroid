package net.movegaga.jemu2.driver.msx.v9938;

import net.movegaga.jemu2.JEmu2;

public class Palette {
   private int b0;
   private int b1;
   private int counter = 0;
   private V9938 vdp;

   public Palette(V9938 var1) {
      this.vdp = var1;
   }

   public void port2(int var1) {
      if(this.counter == 0) {
         this.b0 = var1 << 1;
         ++this.counter;
      } else {
         this.b1 = var1 << 13;
         if(JEmu2.IS_APPLET) {
            var1 = (this.b0 & 15) << 4 | (this.b0 & 240) << 16 | this.b1;
         } else {
            var1 = (this.b0 & 15) << 20 | this.b0 & 240 | this.b1;
         }

         this.vdp.RGB[this.vdp.regControl[16] & 15] = var1;
         this.vdp.regControl[16] = this.vdp.regControl[16] + 1 & 15;
         this.counter = 0;
      }

   }
}
