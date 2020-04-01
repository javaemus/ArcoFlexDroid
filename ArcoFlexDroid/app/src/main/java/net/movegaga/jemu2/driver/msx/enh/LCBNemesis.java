package net.movegaga.jemu2.driver.msx.enh;

import net.movegaga.jemu2.driver.msx.MSX;
import net.movegaga.jemu2.driver.msx.v9938.LineCallBack;
import net.movegaga.jemu2.driver.msx.v9938.V9938;

import jef.video.BitMap;

public class LCBNemesis extends LineCallBack {
   private int address;
   protected MSX msx;
   int[] prev = new int[192];
   int[] scroll = new int[192];
   private V9938 vdp;

   public LCBNemesis(int var1, MSX var2) {
      System.out.println("Nemesis Line Callback enabled " + Integer.toHexString(var1));
      this.msx = var2;
      this.vdp = var2.vdp;
      this.address = var1;
   }

   private void setScroll(int var1, int var2) {
      if(var1 != this.prev[var2]) {
         this.prev[var2] = var1;
      } else {
         switch(var1) {
         case 1:
            this.scroll[var2] = 0;
            break;
         case 2:
            this.scroll[var2] = 1;
            break;
         case 4:
            this.scroll[var2] = 2;
            break;
         case 8:
            this.scroll[var2] = 3;
            break;
         case 16:
            this.scroll[var2] = 4;
            break;
         case 32:
            this.scroll[var2] = 5;
            break;
         case 64:
            this.scroll[var2] = 6;
            break;
         case 128:
            this.scroll[var2] = 6;
         }
      }

   }

   public void exec(int var1) {
      if(var1 >= 4 && var1 < 186) {
         this.setScroll(this.msx.ramMapper.read(this.address), var1);
         BitMap var3 = this.vdp.bitmap;
         int[] var2 = var3.getPixels();
         System.arraycopy(var2, var3.getWidth() * var1 + 8 + this.scroll[var1], var2, var3.getWidth() * var1 + 8, 256 - this.scroll[var1]);
      }

   }
}
