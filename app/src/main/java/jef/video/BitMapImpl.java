package jef.video;

public final class BitMapImpl implements BitMap {
   final int h;
   final int h1;
   int[] pixels;
   BitMapImpl scaled;
   final int w;
   final int w1;

   public BitMapImpl(int w, int h) {
      this.w = w;
      this.h = h;
      this.w1 = w - 1;
      this.h1 = h - 1;
      this.pixels = new int[(w * h)];
   }

   public BitMapImpl(int w, int h, boolean transparent) {
      this.w = w;
      this.h = h;
      this.w1 = w - 1;
      this.h1 = h - 1;
      this.pixels = new int[(w * h)];
      if (transparent) {
         for (int i = 0; i < this.pixels.length; i++) {
            this.pixels[i] = -1;
         }
      }
   }

   public BitMapImpl(int w, int h, int[] pixels) {
      this.w = w;
      this.h = h;
      this.w1 = w - 1;
      this.h1 = h - 1;
      this.pixels = pixels;
   }

   public final void setPixels(int[] pixels) {
      this.pixels = pixels;
   }

   public final int[] getPixels() {
      return this.pixels;
   }

   public final int getWidth() {
      return this.w;
   }

   public final int getHeight() {
      return this.h;
   }

   public final void setPixel(int x, int y, int c) {
      if (x >= 0 && x < this.w && y >= 0 && y < this.h) {
         this.pixels[(this.w * y) + x] = c;
      }
   }

   public final void setPixelFast(int x, int y, int c) {
      this.pixels[(this.w * y) + x] = c;
   }

   public final int getPixel(int x, int y) {
      if (x < 0 || x >= this.w || y < 0 || y >= this.h) {
         return -1;
      }
      return this.pixels[(this.w * y) + x];
   }

   public final void toPixels(int[] target) {
      System.arraycopy(this.pixels, 0, target, 0, target.length);
   }

   public final void toBitMap(BitMap bm, int x, int y) {
      int xw = x + this.w;
      int ofs = 0;
      int iy = 0;
      while (iy < this.h) {
         int ix = x;
         int ofs2 = ofs;
         while (ix < xw) {
            ofs = ofs2 + 1;
            int c = this.pixels[ofs2];
            if (c >= 0) {
               int ix2 = ix + 1;
               bm.setPixel(ix, y, c);
               ix = ix2;
               ofs2 = ofs;
            } else {
               ix++;
               ofs2 = ofs;
            }
         }
         y++;
         iy++;
         ofs = ofs2;
      }
   }

   public final void toBitMap(BitMap bm, int x, int y, boolean fx, boolean fy) {
      int iy = 0;
      while (iy < this.h) {
         int iiy = fy ? (this.h1 - iy) * this.w : this.w * iy;
         for (int ix = 0; ix < this.w; ix++) {
            int iix;
            if (fx) {
               iix = this.w1 - ix;
            } else {
               iix = ix;
            }
            int c = this.pixels[iiy + iix];
            if (c >= 0) {
               bm.setPixel(x + ix, y, c);
            }
         }
         y++;
         iy++;
      }
   }

   public final void toBitMap(BitMap bm, int x, int y, boolean fx, boolean fy, boolean overwriteTransparency) {
      int maxx = this.w;
      if (maxx + x > bm.getWidth()) {
         maxx = bm.getWidth() - x;
      }
      int maxy = this.h;
      if (maxy + y > bm.getHeight()) {
         maxy = bm.getHeight() - y;
      }
      int yd = y;
      int iy;
      int iiy;
      int ix;
      int iix;
      if (overwriteTransparency) {
         iy = 0;
         while (iy < maxy) {
            iiy = fy ? (this.h1 - iy) * this.w : this.w * iy;
            for (ix = 0; ix < maxx; ix++) {
               if (fx) {
                  iix = this.w1 - ix;
               } else {
                  iix = ix;
               }
               bm.setPixel(x + ix, yd, this.pixels[iiy + iix]);
            }
            yd++;
            iy++;
         }
         return;
      }
      iy = 0;
      while (iy < maxy) {
         iiy = fy ? (this.h1 - iy) * this.w : this.w * iy;
         for (ix = 0; ix < maxx; ix++) {
            if (fx) {
               iix = this.w1 - ix;
            } else {
               iix = ix;
            }
            int c = this.pixels[iiy + iix];
            if (c >= 0) {
               bm.setPixel(x + ix, yd, c);
            }
         }
         yd++;
         iy++;
      }
   }

   public final void toBitMap(BitMap bm, int x, int y, int sx, int sy, int sw, int sh) {
      int l = this.pixels.length;
      int i = (this.w * sy) + sx;
      for (int iy = 0; iy < sh; iy++) {
         int ix = 0;
         int ii = i;
         while (ix < sw) {
            int ii2;
            if (ii < 0 || ii >= l) {
               ii2 = ii;
            } else {
               ii2 = ii + 1;
               int c = this.pixels[ii];
               if (c >= 0) {
                  bm.setPixel(x + ix, y + iy, c);
               }
            }
            ix++;
            ii = ii2;
         }
         i += this.w;
      }
   }

   public final void toBitMap(BitMap bm, int x, int y, int sx, int sy, int sw, int sh, int transp) {
      int l = this.pixels.length;
      int i = (this.w * sy) + sx;
      int iy;
      int ix;
      int ii;
      int ii2;
      int c;
      if (transp == -1) {
         for (iy = 0; iy < sh; iy++) {
            ix = 0;
            ii = i;
            while (ix < sw) {
               if (ii < 0 || ii >= l) {
                  ii2 = ii;
               } else {
                  ii2 = ii + 1;
                  c = this.pixels[ii];
                  if (c >= 0) {
                     bm.setPixel(x + ix, y + iy, c);
                  }
               }
               ix++;
               ii = ii2;
            }
            i += this.w;
         }
         return;
      }
      for (iy = 0; iy < sh; iy++) {
         ix = 0;
         ii = i;
         while (ix < sw) {
            if (ii < 0 || ii >= l) {
               ii2 = ii;
            } else {
               ii2 = ii + 1;
               c = this.pixels[ii];
               if (c != transp) {
                  bm.setPixel(x + ix, y + iy, c);
               }
            }
            ix++;
            ii = ii2;
         }
         i += this.w;
      }
   }

   public final void toBitMapScrollXY(BitMap dest, int xScroll, int yScroll, int transparency, int transcolor) {
      int sw = this.w;
      int sh = this.h;
      int dw = dest.getWidth();
      int dh = dest.getHeight();
      xScroll %= sw;
      yScroll %= sh;
      if (xScroll + dw <= sw && yScroll + dh <= sh) {
         toBitMap(dest, 0, 0, xScroll, yScroll, dw, dh);
      } else if (xScroll + dw > sw && yScroll + dh <= sh) {
         toBitMap(dest, 0, 0, xScroll, yScroll, sw - xScroll, dh);
         toBitMap(dest, sw - xScroll, 0, xScroll + (sw - xScroll), yScroll, dw - (sw - xScroll), dh);
      } else if (xScroll + dw > sw || yScroll + dh <= sh) {
         toBitMap(dest, sw - xScroll, sh - yScroll, 0, 0, (xScroll + dw) - sw, (yScroll + dh) - sh);
         toBitMap(dest, 0, sh - yScroll, xScroll, 0, sw - xScroll, (yScroll + dh) - sh);
         toBitMap(dest, sw - xScroll, 0, 0, yScroll, (this.w + xScroll) - sw, sh - yScroll);
         toBitMap(dest, 0, 0, xScroll, yScroll, sw - xScroll, sh - yScroll);
      } else {
         toBitMap(dest, 0, 0, xScroll, yScroll, dw, dh - ((yScroll + dh) - sh));
         toBitMap(dest, 0, dh - ((yScroll + dh) - sh), xScroll, 0, dw, (yScroll + dh) - sh);
      }
   }

   public final void toBitMapScrollXRow(BitMap dest, int xScroll, int ySrc, int srcHeight, int yDst) {
   }

   public final void toBitMapScrollYCol(BitMap dest, int yScroll, int xSrc, int srcWidth, int xDst) {
   }

   public final BitMap getScaledBitMap(int scale, int renderMode) {
      switch (scale) {
         case 0:
            System.err.println("ERROR getting scaled BitMap: scale factor 0.");
            break;
         case 1:
            return this;
         default:
            if (this.scaled == null) {
               this.scaled = new BitMapImpl(this.w << 1, this.h << 1);
            }
            processScaledImage(scale, renderMode);
            break;
      }
      return this.scaled;
   }

   protected void processScaledImage(int scale, int mode) {
      int dx = this.w;
      int dy = this.h;
      int srcofs = 0;
      int dstofs = 0;
      int[] dpixels = this.scaled.getPixels();
      int y;
      int x;
      int i1;
      int i2;
      switch (mode) {
         case 1:
            for (y = 0; y < dy; y++) {
               for (x = 0; x < dx - 1; x++) {
                  int p1 = this.pixels[srcofs];
                  int p2 = blendColors(p1, this.pixels[srcofs + 1]);
                  i1 = dstofs + (x << 1);
                  i2 = i1 + this.scaled.w;
                  dpixels[i1] = p1;
                  dpixels[i1 + 1] = p1;
                  dpixels[i2] = (p1 >> 1) & 8355711;
                  dpixels[i2 + 1] = (p2 >> 1) & 8355711;
                  srcofs++;
               }
               srcofs++;
               dstofs += this.scaled.w << 1;
            }
            return;
         case 2:
            srcofs = dx + 1;
            dstofs = this.scaled.w << 1;
            for (y = 1; y < dy - 1; y++) {
               for (x = 1; x < dx - 1; x++) {
                  int E0;
                  int E1;
                  int E2;
                  int E3;
                  int B = this.pixels[srcofs - dx];
                  int D = this.pixels[srcofs - 1];
                  int E = this.pixels[srcofs];
                  int F = this.pixels[srcofs + 1];
                  int H = this.pixels[srcofs + dx];
                  if (D != B || B == F || D == H) {
                     E0 = E;
                  } else {
                     E0 = D;
                  }
                  if (B != F || B == D || F == H) {
                     E1 = E;
                  } else {
                     E1 = F;
                  }
                  if (D != H || D == B || H == F) {
                     E2 = E;
                  } else {
                     E2 = D;
                  }
                  if (H != F || D == H || B == F) {
                     E3 = E;
                  } else {
                     E3 = F;
                  }
                  i1 = dstofs + (x << 1);
                  i2 = i1 + this.scaled.w;
                  dpixels[i1] = E0;
                  dpixels[i1 + 1] = E1;
                  dpixels[i2] = E2;
                  dpixels[i2 + 1] = E3;
                  srcofs++;
               }
               srcofs += 2;
               dstofs += this.scaled.w << 1;
            }
            return;
         default:
            return;
      }
   }

   protected final int blendColors(int col1, int col2) {
      return (((((col1 >> 16) + (col2 >> 16)) >> 1) << 16) | (((((col1 >> 8) & 255) + ((col2 >> 8) & 255)) >> 1) << 8)) | (((col1 & 255) + (col2 & 255)) >> 1);
   }

   protected final int dimColor(int col) {
      return (col >> 1) & 8355711;
   }

   public String toString() {
      return "BitMapImpl (" + this.w + ", " + this.h + ")";
   }
}