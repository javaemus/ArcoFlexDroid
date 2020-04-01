package jef.video;

public class GfxLayout {
   public int bytes;
   public int h;
   public int[] offsPlane;
   public int[] offsX;
   public int[] offsY;
   public int planes;
   public int total;
   public int w;

   public GfxLayout(int var1, int var2, int var3, int var4, int[] var5, int[] var6, int[] var7, int var8) {
      this.w = var1;
      this.h = var2;
      this.total = var3;
      this.planes = var4;
      this.offsPlane = var5;
      this.offsX = var6;
      this.offsY = var7;
      this.bytes = var8;
   }
}
