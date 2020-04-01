package net.movegaga.jemu2.driver.msx.v9938;

public class Commands {
   static final String[] COMMANDS = new String[]{"Stop       ", "-          ", "-          ", "-          ", "GetPixel   ", "PutPixel   ", "SearchPixel", "Line       ", "LogFillRect", "LogCopyRect", "LogGetPixels", "LogPutPixels", "HSFillRect ", "HSCopyRect ", "HSCopyVert ", "HSPutBytes "};
   static final boolean TRACE = false;
   static final int[] colmask;
   static final int[] lbytes;
   static final int[] pixbyte;
   static final int[] xmask;
   static final int[] xshift;
   static final int[] xsize;
   static final int[] ymask;
   int r44_ex;
   int r44_ey;
   int r44_sx;
   int r44_sy;
   int r44_x;
   int r44_xsize;
   int r44_y;
   int v_lop;
   int v_xbytes;
   int v_xmask;
   int v_xshift;
   int v_ymask;
   int v_yshift;
   private V9938 vdp;
   int xdir;
   int ydir;

   static {
      int[] var0 = new int[9];
      var0[5] = 256;
      var0[6] = 512;
      var0[7] = 512;
      var0[8] = 256;
      xsize = var0;
      var0 = new int[9];
      var0[5] = 255;
      var0[6] = 511;
      var0[7] = 511;
      var0[8] = 255;
      xmask = var0;
      var0 = new int[9];
      var0[5] = 1023;
      var0[6] = 1023;
      var0[7] = 511;
      var0[8] = 511;
      ymask = var0;
      var0 = new int[9];
      var0[5] = 1;
      var0[6] = 2;
      var0[7] = 1;
      xshift = var0;
      var0 = new int[9];
      var0[5] = 128;
      var0[6] = 128;
      var0[7] = 256;
      var0[8] = 256;
      lbytes = var0;
      var0 = new int[9];
      var0[5] = 2;
      var0[6] = 4;
      var0[7] = 2;
      var0[8] = 1;
      pixbyte = var0;
      var0 = new int[9];
      var0[5] = 15;
      var0[6] = 3;
      var0[7] = 15;
      var0[8] = 255;
      colmask = var0;
   }

   public Commands(V9938 var1) {
      this.vdp = var1;
   }

   private final void cmdEnd(int var1, int var2) {
      this.vdp.setBusy(false);
      int var3 = var1;
      if(var1 == 0) {
         byte var4;
         if((this.vdp.regControl[45] & 8) != 0) {
            var4 = -1;
         } else {
            var4 = 1;
         }

         var3 = var1 + var4;
      }

      this.vdp.regControl[42] = var3 & 255;
      this.vdp.regControl[43] = var3 >> 8 & 3;
      this.vdp.regControl[38] = var2 & 255;
      this.vdp.regControl[39] = var2 >> 8 & 3;
   }

   private final int getArg() {
      return this.vdp.regControl[45];
   }

   private final int getDX() {
      return this.vdp.regControl[36] | (this.vdp.regControl[37] & 1) << 8;
   }

   private final int getDY() {
      return this.vdp.regControl[38] | (this.vdp.regControl[39] & 3) << 8;
   }

   private final int getData() {
      return this.vdp.regControl[44];
   }

   private final int getHeight() {
      return this.vdp.regControl[42] | (this.vdp.regControl[43] & 3) << 8;
   }

   private final int getPix(int var1, int var2) {
      int var6 = ymask[this.vdp.getScrMode()];
      int var4 = lbytes[this.vdp.getScrMode()];
      int var5 = xmask[this.vdp.getScrMode()];
      int var3 = xshift[this.vdp.getScrMode()];
      var3 = this.vdp.vram[(var6 & var2) * var4 + ((var5 & var1) >> var3)];
      var2 = var3;
      switch(this.vdp.getScrMode()) {
      case 6:
         switch(var1 & 3) {
         case 0:
            var2 = var3 >> 6;
            return var2;
         case 1:
            var2 = var3 >> 4 & 3;
            return var2;
         case 2:
            var2 = var3 >> 2 & 3;
            return var2;
         case 3:
            var2 = var3 & 3;
            return var2;
         default:
            var2 = var3;
            return var2;
         }
      case 7:
      default:
         if((var1 & 1) == 0) {
            var2 = var3 >> 4;
         } else {
            var2 = var3 & 15;
         }
      case 8:
      }

      return var2;
   }

   private final int getSX() {
      return this.vdp.regControl[32] | (this.vdp.regControl[33] & 1) << 8;
   }

   private final int getSY() {
      return this.vdp.regControl[34] | (this.vdp.regControl[35] & 3) << 8;
   }

   private int getVRAMaddr(int var1, int var2) {
      return ((xmask[this.vdp.getScrMode()] & var1) >> this.v_xshift) + ((ymask[this.vdp.getScrMode()] & var2) << this.v_yshift);
   }

   private final int getWidth() {
      return this.vdp.regControl[40] | (this.vdp.regControl[41] & 1) << 8;
   }

   private void hsCopyRect() {
      int var7 = lbytes[this.vdp.getScrMode()];
      int var5 = xshift[this.vdp.getScrMode()];
      int var8 = this.getSX() >> var5;
      int var4 = this.getDX();
      int var9 = this.getSY();
      int var2 = this.getDY();
      int var1 = this.getWidth() >> var5;
      int var6 = this.getHeight();
      int var3 = var1;
      if(var1 == 0) {
         var3 = var7 - var8;
      }

      var1 = var9 * var7 + var8;
      var2 = var2 * var7 + (var4 >> var5);
      this.vdp.addCycles(var6 * var3);

      for(var4 = 0; var4 < var6; ++var4) {
         for(var5 = 0; var5 < var3; ++var1) {
            this.vdp.vram[var2] = this.vdp.vram[var1];
            ++var5;
            ++var2;
         }

         var2 += var7 - var3;
         var1 += var7 - var3;
      }

   }

   private void hsCopyVert() {
      int var5 = lbytes[this.vdp.getScrMode()];
      int var2 = xshift[this.vdp.getScrMode()];
      int var1 = this.getSY();
      int var3 = this.getDX() >> var2;
      var2 = this.getDY();
      int var6 = var5 - var3;
      int var7 = this.getHeight();
      var1 = var1 * var5 + var3;
      var2 = var2 * var5 + var3;
      this.vdp.addCycles(var7 * var6);

      for(var3 = 0; var3 < var7; ++var3) {
         for(int var4 = 0; var4 < var6; ++var4) {
            this.vdp.vram[var2 % this.vdp.vram.length] = this.vdp.vram[var1 % this.vdp.vram.length];
            ++var2;
            ++var1;
         }

         var2 += var5 - var6;
         var1 += var5 - var6;
      }

   }

   private void hsFillRect() {
      int var6 = lbytes[this.vdp.getScrMode()];
      int var1 = xshift[this.vdp.getScrMode()];
      int var5 = this.getDX() >> var1;
      int var4 = this.getDY();
      var1 = this.getWidth() >> var1;
      int var7 = this.getHeight();
      int var3 = var1;
      if(var1 == 0) {
         var3 = var6 - var5;
      }

      int var2 = this.getData();
      var1 = var2;
      switch(pixbyte[this.vdp.getScrMode()]) {
      case 2:
         var1 = var2 & 15;
         var1 |= var1 << 4;
      case 3:
         break;
      case 4:
         var1 = var2 & 3;
         var1 = var1 << 2 | var1 | var1 << 4 | var1 << 6;
         break;
      default:
         var1 = var2;
      }

      var2 = var4 * var6 + var5;

      for(var4 = 0; var4 < var7; ++var4) {
         for(var5 = 0; var5 < var3; ++var5) {
            this.vdp.vram[var2 % this.vdp.vram.length] = var1;
            ++var2;
         }

         var2 += var6 - var3;
      }

   }

   private void lgCopyRect(int var1, boolean var2) {
      int var5 = this.getSX();
      int var7 = this.getSY();
      int var8 = this.getDX();
      int var9 = this.getDY();
      int var3 = this.getWidth();
      int var6 = this.getHeight();
      var1 = var3;
      if(var3 == 0) {
         var1 = 256 - var5;
      }

      this.vdp.addCycles(var6 * var1);

      for(var3 = 0; var3 < var6; ++var3) {
         for(int var4 = 0; var4 < var1; ++var4) {
            int var10 = this.getPix(var5 + var4, var7 + var3);
            if(var10 != 0 || !var2) {
               this.plot(var8 + var4, var9 + var3, this.logicalOp(this.getPix(var8 + var4, var8 + var3), var10));
            }
         }
      }

   }

   private void lgFillRect(int var1, boolean var2) {
      int var8 = this.getDX();
      int var7 = this.getDY();
      int var3 = this.getWidth();
      int var6 = this.getHeight();
      int var5 = this.getData() & colmask[this.vdp.getScrMode()];
      var1 = var3;
      if(var3 == 0) {
         var1 = xsize[this.vdp.getScrMode()] - var8;
      }

      for(var3 = 0; var3 < var6; ++var3) {
         for(int var4 = 0; var4 < var1; ++var4) {
            if(var5 != 0 || !var2) {
               try {
                  this.plot(var8 + var4, var7 + var3, this.logicalOp(this.getPix(var8 + var4, var7 + var3), var5));
               } catch (Exception var10) {
                  System.out.println(var10);
                  var10.printStackTrace();
               }
            }
         }
      }

   }

   private void lgGetPixels() {
      int var1 = this.getSX();
      this.r44_x = var1;
      this.r44_sx = var1;
      var1 = this.getSY();
      this.r44_y = var1;
      this.r44_sy = var1;
      this.r44_ex = this.getSX() + this.getWidth();
      this.r44_ey = this.getSY() + this.getHeight();
   }

   private void lgPutPixels() {
      int var1 = this.getDX();
      this.r44_x = var1;
      this.r44_sx = var1;
      var1 = this.getDY();
      this.r44_y = var1;
      this.r44_sy = var1;
      this.r44_ex = this.getDX() + this.getWidth();
      this.r44_ey = this.getDY() + this.getHeight();
      this.nextLgPixel(this.vdp.regControl[44]);
   }

   private void line(int var1, boolean var2) {
      int var8 = this.getDX();
      int var7 = this.getDY();
      var1 = this.vdp.regControl[40] + (this.vdp.regControl[41] << 8) & 1023;
      int var10 = this.vdp.regControl[42];
      int var9 = this.vdp.regControl[43];
      int var3 = var1;
      if(var1 == 0) {
         var3 = 1;
      }

      for(var1 = 0; var1 <= var3; ++var1) {
         int var4 = var1 * (var10 + (var9 << 8) & 511) / var3;
         int var5;
         if((this.vdp.regControl[45] & 1) != 0) {
            if((this.vdp.regControl[45] & 8) != 0) {
               var5 = -var1;
            } else {
               var5 = var1;
            }

            var5 += var7;
            if((this.vdp.regControl[45] & 4) != 0) {
               var4 = -var4;
            }

            var4 += var8;
         } else {
            if((this.vdp.regControl[45] & 4) != 0) {
               var5 = -var1;
            } else {
               var5 = var1;
            }

            var5 += var8;
            if((this.vdp.regControl[45] & 8) != 0) {
               var4 = -var4;
            }

            int var6 = var7 + var4;
            var4 = var5;
            var5 = var6;
         }

         this.plot(var4, var5, this.logicalOp(this.getPix(var4, var5), this.getData() & colmask[this.vdp.getScrMode()]));
      }

   }

   private int logicalOp(int var1, int var2) {
      int var3 = var1;
      switch(this.v_lop) {
      case 0:
         var3 = var2;
         break;
      case 1:
         var3 = var1 & var2;
         break;
      case 2:
         var3 = var1 | var2;
         break;
      case 3:
         var3 = var1 ^ var2;
         break;
      case 4:
         var3 = ~var2;
      case 5:
      case 6:
      case 7:
         break;
      case 8:
         var3 = var1;
         if(var2 != 0) {
            var3 = var2;
         }
         break;
      case 9:
         var3 = var1;
         if(var2 != 0) {
            var3 = var1 & var2;
         }
         break;
      case 10:
         var3 = var1;
         if(var2 != 0) {
            var3 = var1 | var2;
         }
         break;
      case 11:
         var3 = var1;
         if(var2 != 0) {
            var3 = var1 ^ var2;
         }
         break;
      case 12:
         var3 = var1;
         if(var2 != 0) {
            var3 = ~var2;
         }
         break;
      default:
         var3 = var1;
      }

      return var3;
   }

   private final void plot(int var1, int var2, int var3) {
      var2 = (ymask[this.vdp.getScrMode()] & var2) * lbytes[this.vdp.getScrMode()] + ((xmask[this.vdp.getScrMode()] & var1) >> xshift[this.vdp.getScrMode()]);
      int var4 = this.vdp.vram[var2];
      switch(this.vdp.getScrMode()) {
      case 6:
         var3 &= 3;
         switch(var1 & 3) {
         case 0:
            this.vdp.vram[var2] = var4 & 63 | var3 << 6;
            return;
         case 1:
            this.vdp.vram[var2] = var4 & 207 | var3 << 4;
            return;
         case 2:
            this.vdp.vram[var2] = var4 & 243 | var3 << 2;
            return;
         case 3:
            this.vdp.vram[var2] = var4 & 252 | var3;
            return;
         default:
            return;
         }
      case 7:
      default:
         if((var1 & 1) == 0) {
            this.vdp.vram[var2] = var4 & 15 | var3 << 4;
         } else {
            this.vdp.vram[var2] = var4 & 240 | var3;
         }
         break;
      case 8:
         this.vdp.vram[var2] = var4;
      }

   }

   private void putPixel(int var1, boolean var2) {
      this.plot(this.getDX(), this.getDY(), this.logicalOp(this.getPix(this.getDX(), this.getDY()), this.getData() & colmask[this.vdp.getScrMode()]));
   }

   private void writeVRAM(int var1, int var2, int var3) {
      int var7 = xmask[this.vdp.getScrMode()];
      int var5 = this.v_xshift;
      int var4 = ymask[this.vdp.getScrMode()];
      int var6 = this.v_yshift;
      this.vdp.vram[((var7 & var1) >> var5) + ((var4 & var2) << var6)] = var3;
   }

   public void exec(int var1) {
      int var4 = var1 >> 4;
      int var2 = var1 & 7;
      boolean var5;
      if((var1 & 8) != 0) {
         var5 = true;
      } else {
         var5 = false;
      }

      int var3 = this.vdp.getScrMode();
      if(var3 >= 5 && var3 <= 8) {
         this.v_xbytes = xsize[var3] >> xshift[var3];
         this.v_xmask = xsize[var3] - 1;
         this.v_xshift = xshift[var3];
         this.v_ymask = 131072 / this.v_xbytes - 1;
         this.v_yshift = this.getshift(this.v_xbytes - 1);
         this.vdp.setBusy(true);
         this.v_lop = var1 & 15;
         switch(var4) {
         case 0:
            this.vdp.setBusy(false);
            break;
         case 1:
         case 2:
         case 3:
         case 4:
         case 6:
         default:
            System.out.println("WARNING: unimplented V9938 opc " + var4);
            break;
         case 5:
            this.putPixel(var2, var5);
            break;
         case 7:
            this.line(var2, var5);
            break;
         case 8:
            this.lgFillRect(var2, var5);
            break;
         case 9:
            this.lgCopyRect(var2, var5);
            break;
         case 10:
            this.lgGetPixels();
            break;
         case 11:
            this.lgPutPixels();
            break;
         case 12:
            this.hsFillRect();
            break;
         case 13:
            this.hsCopyRect();
            break;
         case 14:
            this.hsCopyVert();
            break;
         case 15:
            this.hsPutBytes();
         }

         if(var4 != 10 && var4 != 11 && var4 != 9 && var4 != 13 && var4 != 14 && var4 != 15) {
            this.vdp.setBusy(false);
         }
      }

   }

   int getshift(int var1) {
      int var2;
      for(var2 = 0; (1 << var2 & var1) != 0; ++var2) {
         ;
      }

      return var2;
   }

   void hsPutBytes() {
      int var1 = this.getDX() & -2;
      this.r44_x = var1;
      this.r44_sx = var1;
      var1 = this.getDY();
      this.r44_y = var1;
      this.r44_sy = var1;
      this.r44_ex = this.getDX() + this.getWidth() & -2;
      this.r44_ey = this.getDY() + this.getHeight();
      this.r44_xsize = this.getWidth() & -2;
      if((this.vdp.regControl[45] & 4) == 4) {
         var1 = this.r44_sx - this.r44_ex;
         this.r44_sx += var1;
         this.r44_ex += var1;
         this.r44_x = this.r44_ex;
         this.xdir = -1;
      } else {
         this.xdir = 1;
      }

      if((this.vdp.regControl[45] & 8) == 8) {
         var1 = this.r44_sy - this.r44_ey;
         this.r44_sy += var1;
         this.r44_ey += var1;
         this.r44_y = this.r44_ey;
         this.ydir = -1;
      } else {
         this.ydir = 1;
      }

      this.nextHsByte(this.getData());
   }

   public void nextGetLgPixel() {
      int var1 = this.getPix(this.r44_x, this.r44_y);
      int[] var2 = this.vdp.regStatus;
      this.vdp.regControl[44] = var1;
      var2[7] = var1;
      if(this.r44_sx <= this.r44_ex) {
         var1 = this.r44_x + 1;
         this.r44_x = var1;
         if(var1 >= this.r44_ex) {
            this.r44_x = this.r44_sx;
            if(this.r44_sy < this.r44_ey) {
               var1 = this.r44_y + 1;
               this.r44_y = var1;
               if(var1 >= this.r44_ey) {
                  this.vdp.setBusy(false);
               }
            } else {
               var1 = this.r44_y - 1;
               this.r44_y = var1;
               if(var1 < this.r44_ey) {
                  this.vdp.setBusy(false);
               }
            }
         }
      } else {
         var1 = this.r44_x - 1;
         this.r44_x = var1;
         if(var1 < this.r44_ex) {
            this.r44_x = this.r44_sx;
            if(this.r44_sy <= this.r44_ey) {
               var1 = this.r44_y + 1;
               this.r44_y = var1;
               if(var1 > this.r44_ey) {
                  this.vdp.setBusy(false);
               }
            } else {
               var1 = this.r44_y - 1;
               this.r44_y = var1;
               if(var1 < this.r44_ey) {
                  this.vdp.setBusy(false);
               }
            }
         }
      }

   }

   void nextHsByte(int var1) {
      this.writeVRAM(this.r44_x, this.r44_y, var1);
      if(this.xdir == 1) {
         this.r44_x += 1 << this.v_xshift;
         if(this.r44_x >= this.r44_ex) {
            this.r44_x = this.r44_sx;
            if(this.ydir == 1) {
               var1 = this.r44_y + 1;
               this.r44_y = var1;
               if(var1 >= this.r44_ey) {
                  this.vdp.setBusy(false);
               }
            } else {
               var1 = this.r44_y - 1;
               this.r44_y = var1;
               if(var1 <= this.r44_sy) {
                  this.vdp.setBusy(false);
               }
            }
         }
      } else {
         this.r44_x -= 1 << this.v_xshift;
         if(this.r44_x <= this.r44_sx) {
            this.r44_x = this.r44_ex;
            if(this.ydir == 1) {
               var1 = this.r44_y + 1;
               this.r44_y = var1;
               if(var1 >= this.r44_ey) {
                  this.vdp.setBusy(false);
               }
            } else {
               var1 = this.r44_y - 1;
               this.r44_y = var1;
               if(var1 <= this.r44_sy) {
                  this.vdp.setBusy(false);
               }
            }
         }
      }

   }

   public void nextLgPixel(int var1) {
      int var2 = this.getVRAMaddr(this.r44_x, this.r44_y);
      label43:
      switch(this.vdp.getScrMode()) {
      case 5:
      case 7:
         var1 = this.logicalOp(this.vdp.vram[var2] >> (1 - (this.r44_x & 1)) * 4 & 15, var1 & 15);
         if((this.r44_x & 1) == 0) {
            this.vdp.vram[var2] = this.vdp.vram[var2] & 15 | var1 << 4;
         } else {
            this.vdp.vram[var2] = this.vdp.vram[var2] & 240 | var1;
         }
         break;
      case 6:
         var1 = this.logicalOp(this.vdp.vram[var2] >> (1 - (this.r44_x & 3)) * 2 & 3, var1 & 3);
         switch(this.r44_x & 3) {
         case 0:
            this.vdp.vram[var2] = this.vdp.vram[var2] & 63 | var1 << 6;
            break label43;
         case 1:
            this.vdp.vram[var2] = this.vdp.vram[var2] & 207 | var1 << 4;
            break label43;
         case 2:
            this.vdp.vram[var2] = this.vdp.vram[var2] & 243 | var1 << 2;
            break label43;
         case 3:
            this.vdp.vram[var2] = this.vdp.vram[var2] & 252 | var1;
         default:
            break label43;
         }
      case 8:
         this.vdp.vram[var2] = this.logicalOp(this.vdp.vram[var2], var1 & 255);
      }

      if(this.r44_sx <= this.r44_ex) {
         var1 = this.r44_x + 1;
         this.r44_x = var1;
         if(var1 >= this.r44_ex) {
            this.r44_x = this.r44_sx;
            if(this.r44_sy < this.r44_ey) {
               var1 = this.r44_y + 1;
               this.r44_y = var1;
               if(var1 >= this.r44_ey) {
                  this.vdp.setBusy(false);
               }
            } else {
               var1 = this.r44_y - 1;
               this.r44_y = var1;
               if(var1 < this.r44_ey) {
                  this.vdp.setBusy(false);
               }
            }
         }
      } else {
         var1 = this.r44_x - 1;
         this.r44_x = var1;
         if(var1 < this.r44_ex) {
            this.r44_x = this.r44_sx;
            if(this.r44_sy <= this.r44_ey) {
               var1 = this.r44_y + 1;
               this.r44_y = var1;
               if(var1 > this.r44_ey) {
                  this.vdp.setBusy(false);
               }
            } else {
               var1 = this.r44_y - 1;
               this.r44_y = var1;
               if(var1 < this.r44_ey) {
                  this.vdp.setBusy(false);
               }
            }
         }
      }

   }
}
