package jef.video;

public class Blitter {
   public static void copyEqualSizedBitMap(BitMap var0, BitMap var1) {
      var0.toPixels(var1.getPixels());
   }

   public static void copyEqualSizedTransparentBitMap(BitMap var0, BitMap var1) {
      int[] var10 = var0.getPixels();
      int[] var9 = var1.getPixels();
      int var4 = 0;
      int var2 = 0;
      int var6 = var1.getHeight();
      int var7 = var1.getWidth();

      for(int var3 = 0; var3 < var6; ++var3) {
         for(int var5 = 0; var5 < var7; ++var2) {
            int var8 = var10[var2];
            if(var8 != -1) {
               var9[var4] = var8;
            }

            ++var4;
            ++var5;
         }
      }

   }

   public static void copyWrappedBitMap(BitMap var0, BitMap var1, int var2, int var3) {
      int[] var10 = var0.getPixels();
      int[] var11 = var1.getPixels();
      int var6 = var0.getWidth();
      int var8 = var1.getWidth();
      int var9 = var0.getHeight();

      int var7;
      for(var7 = var1.getHeight(); var2 < 0; var2 += var6) {
         ;
      }

      while(var3 < 0) {
         var3 += var9;
      }

      for(int var4 = 0; var4 < var7; ++var4) {
         for(int var5 = 0; var5 < var8; ++var5) {
            var11[var4 * var8 + var5] = var10[(var3 + var4) % var9 * var6 + (var2 + var5) % var6];
         }
      }

   }

   public static void copyWrappedBitMap(BitMap var0, BitMap var1, int var2, int var3, int var4) {
      int[] var12 = var0.getPixels();
      int[] var13 = var1.getPixels();
      int var10 = var0.getWidth();
      int var7 = var1.getWidth();
      int var8 = var0.getHeight();

      int var9;
      for(var9 = var1.getHeight(); var2 < 0; var2 += var10) {
         ;
      }

      while(var3 < 0) {
         var3 += var8;
      }

      for(int var5 = 0; var5 < var9; ++var5) {
         for(int var6 = 0; var6 < var7; ++var6) {
            int var11 = var12[(var2 + var6) % var10 + (var3 + var5) % var8 * var10];
            if(var11 != var4) {
               var13[var5 * var7 + var6] = var11;
            }
         }
      }

   }

   public static void copyWrappedRowsHorizontal(BitMap var0, BitMap var1, int[] var2, int var3, int var4) {
      int[] var13 = var0.getPixels();
      int[] var12 = var1.getPixels();
      int var10 = var0.getWidth();
      int var9 = var1.getWidth();
      int var8 = var0.getHeight();

      int var11;
      for(var11 = var1.getHeight(); var4 < 0; var4 += var8) {
         ;
      }

      for(int var5 = 0; var5 < var11; ++var5) {
         int var6;
         for(var6 = var2[var5 / var3]; var6 < 0; var6 += var10) {
            ;
         }

         for(int var7 = 0; var7 < var9; ++var7) {
            var12[var5 * var9 + var7] = var13[(var6 + var7) % var10 + (var4 + var5) % var8 * var10];
         }
      }

   }
}
