package jef.video;

public interface BitMap {
   int SCALE_MODE_NORMAL = 0;
   int SCALE_MODE_SCALE2X = 2;
   int SCALE_MODE_TV = 1;

   int getHeight();

   int getPixel(int var1, int var2);

   int[] getPixels();

   BitMap getScaledBitMap(int var1, int var2);

   int getWidth();

   void setPixel(int var1, int var2, int var3);

   void setPixelFast(int var1, int var2, int var3);

   void setPixels(int[] var1);

   void toBitMap(BitMap var1, int var2, int var3);

   void toBitMap(BitMap var1, int var2, int var3, int var4, int var5, int var6, int var7);

   void toBitMap(BitMap var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8);

   void toBitMap(BitMap var1, int var2, int var3, boolean var4, boolean var5);

   void toBitMap(BitMap var1, int var2, int var3, boolean var4, boolean var5, boolean var6);

   void toBitMapScrollXRow(BitMap var1, int var2, int var3, int var4, int var5);

   void toBitMapScrollXY(BitMap var1, int var2, int var3, int var4, int var5);

   void toBitMapScrollYCol(BitMap var1, int var2, int var3, int var4, int var5);

   void toPixels(int[] var1);
}
