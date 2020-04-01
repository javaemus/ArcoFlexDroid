package net.movegaga.jemu2.driver.arcade.cps1;

import jef.video.BitMap;
import jef.video.BitMapImpl;
import jef.video.VideoRenderer;

public class VCps1 implements VideoRenderer {
   BitMap backBuffer;
   CPSConfig config;
   private Cps1 driver;
   long[] gfx1;
   int[] pix;

   public VCps1(Cps1 var1) {
      this.driver = var1;
   }

   private void decode() {
      for(int var3 = 0; var3 < this.gfx1.length * 2; ++var3) {
         int var6 = getInt(this.gfx1, var3);
         int var5 = 0;

         for(int var4 = 0; var4 < 8; ++var4) {
            int var2 = 0;
            int var7 = -2139062144 >> var4 & var6;
            if((var7 & 255) != 0) {
               var2 = 0 | 1;
            }

            int var1 = var2;
            if(('\uff00' & var7) != 0) {
               var1 = var2 | 2;
            }

            var2 = var1;
            if((16711680 & var7) != 0) {
               var2 = var1 | 4;
            }

            var1 = var2;
            if((-16777216 & var7) != 0) {
               var1 = var2 | 8;
            }

            var5 |= var1 << var4 * 4;
         }

         putInt(this.gfx1, var3, var5);
      }

   }

   private static int getInt(long[] var0, int var1) {
      int var2 = var1 >> 1;
      if((var1 & 1) == 0) {
         var1 = (int)(var0[var2] >>> 32);
      } else {
         var1 = (int)(var0[var2] & -1L);
      }

      return var1;
   }

   private static void putInt(long[] var0, int var1, int var2) {
      int var3 = var1 >> 1;
      if((var1 & 1) == 0) {
         var0[var3] = var0[var3] & -1L | (long)(var2 << 32);
      } else {
         var0[var3] = (long)((int)(var0[var3] >>> 32) << 32 | var2);
      }

   }

   public void init() {
      this.config = this.driver.config;
      this.gfx1 = this.driver.gfx1;
      this.backBuffer = new BitMapImpl(384, 224);
      this.pix = this.backBuffer.getPixels();
      this.decode();
   }

   public int readPort(int var1) {
      return 0;
   }

   public BitMap renderVideo() {
      return this.backBuffer;
   }

   public void renderVideoPost() {
   }

   public void writePort(int var1, int var2) {
   }
}
