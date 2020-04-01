package net.movegaga.jemu2.driver.sms.video;

import jef.video.BitMap;
import jef.video.BitMapImpl;

public class Renderer {
   private static final int[] PIX8 = new int[8];
   private int addrNameTbl;
   private int addrSprInfoTbl;
   BitMap bitmap;
   private boolean enable;
   private boolean hideLeftColumn;
   private int[] pixels;
   private int scrollX;
   private int scrollY;
   private boolean topPanel;
   private VDP vdp;
   private int[] vram;

   public Renderer(VDP var1) {
      this.vdp = var1;
      this.bitmap = new BitMapImpl(256, 192);
      this.pixels = this.bitmap.getPixels();
      this.vram = var1.vram;
   }

   private void drawBgLine(int var1) {
      int var4 = (this.scrollY + var1) % 224;

      for(int var2 = 0; var2 < 256; var2 += 8) {
         int var7 = var4 & 7;
         int var3 = this.addrNameTbl + (var4 >> 3 << 6) + (var2 >> 3 << 1);
         int var5 = this.vdp.vram[var3 + 1] << 8 | this.vdp.vram[var3];
         int var8 = var5 & 511;
         boolean var9;
         if((var5 & 512) != 0) {
            var9 = true;
         } else {
            var9 = false;
         }

         boolean var10;
         if((var5 & 1024) != 0) {
            var10 = true;
         } else {
            var10 = false;
         }

         int var6 = (var5 & 2048) >> 7;
         if(!var10) {
            var3 = (var7 << 2) + (var8 << 5);
         } else {
            var3 = (7 - var7 << 2) + (var8 << 5);
         }

         if((var5 & 4096) == 0) {
            this.drawTileRowOpaque(var3, var2 + this.scrollX, var1, var9, var6);
         } else {
            this.drawTileRowZero(this.scrollX + var2, var1, var6);
         }
      }

   }

   private void drawFgLine(int var1) {
      int var4 = (this.scrollY + var1) % 224;
      int var5 = var4 & 7;

      for(int var2 = 0; var2 < 256; var2 += 8) {
         int var3 = this.addrNameTbl + (var4 >> 3 << 6) + (var2 >> 2);
         int var6 = this.vdp.vram[var3 + 1] << 8 | this.vdp.vram[var3];
         int var7 = var6 & 511;
         boolean var8;
         if((var6 & 512) != 0) {
            var8 = true;
         } else {
            var8 = false;
         }

         boolean var9;
         if((var6 & 1024) != 0) {
            var9 = true;
         } else {
            var9 = false;
         }

         if((var6 & 4096) != 0) {
            if(!var9) {
               var3 = var5 * 4 + var7 * 32;
            } else {
               var3 = (7 - var5) * 4 + var7 * 32;
            }

            this.drawTileRowTrans(var3, var2 + this.scrollX, var1, var8, (var6 & 2048) >> 7);
         }
      }

   }

   private void drawSprLine(int var1) {
      int var2 = 63;

      int var3;
      for(var3 = 0; var3 < 64 && var2 == 63; ++var3) {
         if(this.vram[this.addrSprInfoTbl + var3] == 208) {
            var2 = var3 - 1;
         }
      }

      for(; var2 >= 0; --var2) {
         int var4 = this.vram[this.addrSprInfoTbl + var2];
         var3 = var4;
         if(var4 > 240) {
            var3 = var4 - 256;
         }

         if(var3 <= var1 && var3 + 16 >= var1) {
            int var5 = this.vram[this.addrSprInfoTbl + 128 + var2 * 2];
            var4 = var5;
            if((this.vdp.getControl(0) & 8) != 0) {
               var4 = var5 - 8;
            }

            int var6 = this.vram[this.addrSprInfoTbl + 129 + var2 * 2];
            var5 = var6;
            if((this.vdp.getControl(6) & 4) != 0) {
               var5 = var6 + 256;
            }

            var6 = var1 - var3;
            if((this.vdp.getControl(1) & 2) == 0) {
               if(var6 >= 0 && var6 < 8) {
                  this.drawTileRowTrans((var6 << 2) + (var5 << 5), var4, var1, false, 16);
               }
            } else {
               var5 &= -2;
               if(var6 >= 0 && var6 < 8) {
                  this.drawTileRowTrans((var6 << 2) + (var5 << 5), var4, var1, false, 16);
               } else {
                  var3 = var1 - (var3 + 8);
                  if(var3 >= 0 && var3 < 8) {
                     this.drawTileRowTrans((var3 << 2) + (var5 + 1 << 5), var4, var1, false, 16);
                  }
               }
            }
         }
      }

   }

   private void drawTileRowOpaque(int var1, int var2, int var3, boolean var4, int var5) {
      int[] var17 = this.vram;
      int var6 = var1 + 1;
      int var13 = var17[var1];
      var17 = this.vram;
      var1 = var6 + 1;
      int var15 = var17[var6];
      int var14 = this.vram[var1];
      int var16 = this.vram[var1 + 1];
      int var7;
      int var8;
      int var9;
      int var10;
      int var11;
      int var12;
      if(!var4) {
         var7 = (var13 & 128 | (var15 & 128) << 1 | (var14 & 128) << 2 | (var16 & 128) << 3) >> 7;
         var9 = (var13 & 64 | (var15 & 64) << 1 | (var14 & 64) << 2 | (var16 & 64) << 3) >> 6;
         var10 = (var13 & 32 | (var15 & 32) << 1 | (var14 & 32) << 2 | (var16 & 32) << 3) >> 5;
         var12 = (var13 & 16 | (var15 & 16) << 1 | (var14 & 16) << 2 | (var16 & 16) << 3) >> 4;
         var6 = (var13 & 8 | (var15 & 8) << 1 | (var14 & 8) << 2 | (var16 & 8) << 3) >> 3;
         var8 = (var13 & 4 | (var15 & 4) << 1 | (var14 & 4) << 2 | (var16 & 4) << 3) >> 2;
         var11 = (var13 & 2 | (var15 & 2) << 1 | (var14 & 2) << 2 | (var16 & 2) << 3) >> 1;
         var1 = var13 & 1 | (var15 & 1) << 1 | (var14 & 1) << 2 | (var16 & 1) << 3;
      } else {
         var1 = (var13 & 128 | (var15 & 128) << 1 | (var14 & 128) << 2 | (var16 & 128) << 3) >> 7;
         var11 = (var13 & 64 | (var15 & 64) << 1 | (var14 & 64) << 2 | (var16 & 64) << 3) >> 6;
         var8 = (var13 & 32 | (var15 & 32) << 1 | (var14 & 32) << 2 | (var16 & 32) << 3) >> 5;
         var6 = (var13 & 16 | (var15 & 16) << 1 | (var14 & 16) << 2 | (var16 & 16) << 3) >> 4;
         var12 = (var13 & 8 | (var15 & 8) << 1 | (var14 & 8) << 2 | (var16 & 8) << 3) >> 3;
         var10 = (var13 & 4 | (var15 & 4) << 1 | (var14 & 4) << 2 | (var16 & 4) << 3) >> 2;
         var9 = (var13 & 2 | (var15 & 2) << 1 | (var14 & 2) << 2 | (var16 & 2) << 3) >> 1;
         var7 = var13 & 1 | (var15 & 1) << 1 | (var14 & 1) << 2 | (var16 & 1) << 3;
      }

      this.plot(this.pixels, var2, var3, var7, var5);
      this.plot(this.pixels, var2 + 1, var3, var9, var5);
      this.plot(this.pixels, var2 + 2, var3, var10, var5);
      this.plot(this.pixels, var2 + 3, var3, var12, var5);
      this.plot(this.pixels, var2 + 4, var3, var6, var5);
      this.plot(this.pixels, var2 + 5, var3, var8, var5);
      this.plot(this.pixels, var2 + 6, var3, var11, var5);
      this.plot(this.pixels, var2 + 7, var3, var1, var5);
   }

   private void drawTileRowTrans(int var1, int var2, int var3, boolean var4, int var5) {
      int[] var17 = this.vram;
      int var6 = var1 + 1;
      int var13 = var17[var1];
      var17 = this.vram;
      var1 = var6 + 1;
      int var15 = var17[var6];
      int var14 = this.vram[var1];
      int var16 = this.vram[var1 + 1];
      int var7;
      int var8;
      int var9;
      int var10;
      int var11;
      int var12;
      if(!var4) {
         var1 = (var13 & 128 | (var15 & 128) << 1 | (var14 & 128) << 2 | (var16 & 128) << 3) >> 7;
         var7 = (var13 & 64 | (var15 & 64) << 1 | (var14 & 64) << 2 | (var16 & 64) << 3) >> 6;
         var6 = (var13 & 32 | (var15 & 32) << 1 | (var14 & 32) << 2 | (var16 & 32) << 3) >> 5;
         var11 = (var13 & 16 | (var15 & 16) << 1 | (var14 & 16) << 2 | (var16 & 16) << 3) >> 4;
         var8 = (var13 & 8 | (var15 & 8) << 1 | (var14 & 8) << 2 | (var16 & 8) << 3) >> 3;
         var9 = (var13 & 4 | (var15 & 4) << 1 | (var14 & 4) << 2 | (var16 & 4) << 3) >> 2;
         var10 = (var13 & 2 | (var15 & 2) << 1 | (var14 & 2) << 2 | (var16 & 2) << 3) >> 1;
         var12 = var13 & 1 | (var15 & 1) << 1 | (var14 & 1) << 2 | (var16 & 1) << 3;
      } else {
         var12 = (var13 & 128 | (var15 & 128) << 1 | (var14 & 128) << 2 | (var16 & 128) << 3) >> 7;
         var10 = (var13 & 64 | (var15 & 64) << 1 | (var14 & 64) << 2 | (var16 & 64) << 3) >> 6;
         var9 = (var13 & 32 | (var15 & 32) << 1 | (var14 & 32) << 2 | (var16 & 32) << 3) >> 5;
         var8 = (var13 & 16 | (var15 & 16) << 1 | (var14 & 16) << 2 | (var16 & 16) << 3) >> 4;
         var11 = (var13 & 8 | (var15 & 8) << 1 | (var14 & 8) << 2 | (var16 & 8) << 3) >> 3;
         var6 = (var13 & 4 | (var15 & 4) << 1 | (var14 & 4) << 2 | (var16 & 4) << 3) >> 2;
         var7 = (var13 & 2 | (var15 & 2) << 1 | (var14 & 2) << 2 | (var16 & 2) << 3) >> 1;
         var1 = var13 & 1 | (var15 & 1) << 1 | (var14 & 1) << 2 | (var16 & 1) << 3;
      }

      this.tplot(this.pixels, var2, var3, var1, var5);
      this.tplot(this.pixels, var2 + 1, var3, var7, var5);
      this.tplot(this.pixels, var2 + 2, var3, var6, var5);
      this.tplot(this.pixels, var2 + 3, var3, var11, var5);
      this.tplot(this.pixels, var2 + 4, var3, var8, var5);
      this.tplot(this.pixels, var2 + 5, var3, var9, var5);
      this.tplot(this.pixels, var2 + 6, var3, var10, var5);
      this.tplot(this.pixels, var2 + 7, var3, var12, var5);
   }

   private void drawTileRowZero(int var1, int var2, int var3) {
      this.plot(this.pixels, var1, var2, 0, var3);
      this.plot(this.pixels, var1 + 1, var2, 0, var3);
      this.plot(this.pixels, var1 + 2, var2, 0, var3);
      this.plot(this.pixels, var1 + 3, var2, 0, var3);
      this.plot(this.pixels, var1 + 4, var2, 0, var3);
      this.plot(this.pixels, var1 + 5, var2, 0, var3);
      this.plot(this.pixels, var1 + 6, var2, 0, var3);
      this.plot(this.pixels, var1 + 7, var2, 0, var3);
   }

   private void plot(int[] var1, int var2, int var3, int var4, int var5) {
      var1[(var3 << 8) + (var2 & 255)] = this.vdp.palette.rgb[var5 + var4];
   }

   private void tplot(int[] var1, int var2, int var3, int var4, int var5) {
      if(var4 != 0) {
         var1[(var3 << 8) + (var2 & 255)] = this.vdp.palette.rgb[var5 + var4];
      }

   }

   public void drawLine(int var1) {
      boolean var3;
      if((this.vdp.getControl(0) & 32) != 0) {
         var3 = true;
      } else {
         var3 = false;
      }

      this.hideLeftColumn = var3;
      if((this.vdp.getControl(0) & 64) != 0) {
         var3 = true;
      } else {
         var3 = false;
      }

      this.topPanel = var3;
      this.addrNameTbl = (this.vdp.getControl(2) & 14) << 10;
      this.addrSprInfoTbl = (this.vdp.getControl(5) & 126) << 7;
      int var2;
      if(var1 < 16 && this.topPanel) {
         var2 = 0;
      } else {
         var2 = this.vdp.getControl(8);
      }

      this.scrollX = var2;
      this.scrollY = this.vdp.getControl(9);
      if((this.vdp.getControl(1) & 64) != 0) {
         var3 = true;
      } else {
         var3 = false;
      }

      this.enable = var3;
      if(this.enable) {
         this.drawBgLine(var1);
         this.drawSprLine(var1);
         this.drawFgLine(var1);
         if(this.hideLeftColumn) {
            System.arraycopy(PIX8, 0, this.pixels, var1 << 8, 8);
         }
      } else {
         for(var2 = 0; var2 < 256; var2 += 8) {
            System.arraycopy(PIX8, 0, this.pixels, (var1 << 8) + var2, 8);
         }
      }

   }
}
