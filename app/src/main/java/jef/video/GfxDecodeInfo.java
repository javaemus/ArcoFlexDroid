package jef.video;

public class GfxDecodeInfo {
   public int colorOffset;
   public GfxLayout gfx;
   public char[] mem;
   public int numberOfColors;
   public int offset;

   public GfxDecodeInfo(char[] var1, int var2, GfxLayout var3, int var4, int var5) {
      this.mem = var1;
      this.offset = var2;
      this.gfx = var3;
      this.colorOffset = var4;
      this.numberOfColors = var5;
   }

   public GfxDecodeInfo(char[] var1, int var2, int[][] var3, int var4, int var5) {
      this.mem = var1;
      this.offset = var2;
      int var6;
      if(this.IS_FRAC(var3[2][0])) {
         var6 = this.FRAC_NUM(var3[2][0]);
         var2 = this.FRAC_DEN(var3[2][0]);
         var3[2][0] = var1.length * 8 * var6 / (var3[7][0] * var2);
      }

      for(var2 = 0; var2 < var3[4].length; ++var2) {
         if(this.IS_FRAC(var3[4][var2])) {
            int var7 = this.FRAC_NUM(var3[4][var2]);
            var6 = this.FRAC_DEN(var3[4][var2]);
            int var8 = this.FRAC_OFFSET(var3[4][var2]);
            var3[4][var2] = var1.length * 8 * var7 / var6 + var8;
         }
      }

      this.gfx = new GfxLayout(var3[0][0], var3[1][0], var3[2][0], var3[3][0], var3[4], var3[5], var3[6], var3[7][0]);
      this.colorOffset = var4;
      this.numberOfColors = var5;
   }

   public int FRAC_DEN(int var1) {
      return var1 >> 23 & 15;
   }

   public int FRAC_NUM(int var1) {
      return var1 >> 27 & 15;
   }

   public int FRAC_OFFSET(int var1) {
      return 8388607 & var1;
   }

   public boolean IS_FRAC(int var1) {
      boolean var2;
      if((Integer.MIN_VALUE & var1) != 0) {
         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
   }
}
