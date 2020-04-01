package jef.cpu;

import jef.cpuboard.CpuBoard;

public final class I8080 implements Cpu {
   private static final int INTERRUPT_NMI = 1;
   private static final int[] SZ = new int[256];
   private static final int[] SZHVC_add = new int[131072];
   private static final int[] SZHVC_sub = new int[131072];
   private static final int[] SZHV_dec = new int[256];
   private static final int[] SZHV_inc = new int[256];
   private static final int[] SZP = new int[256];
   private static final boolean[] parity = new boolean[256];
   public int A;
   public int A1;
   public int B;
   public int B1;
   public int C;
   public int C1;
   private int CurInstr = 0;
   public int D;
   public int D1;
   public int E;
   public int E1;
   public int F;
   public int F1;
   public int H;
   public int H1;
   public int I;
   public boolean IFF0 = false;
   public boolean IFF1 = false;
   public int IM = 0;
   public boolean IRQ = false;
   public int I_Vector = 0;
   public int L;
   public int L1;
   public boolean NMI = false;
   public int PC;
   private int PPC = 0;
   public int R;
   public int SP;
   public int cycles_left = 0;
   public boolean debugDisabled = true;
   public int debugFrom;
   public int debugLevel = 0;
   public int debugMemLoc = 0;
   public int debugMemVal = 0;
   public boolean debugSelectMemEqVal = false;
   public boolean debugSelectStart = false;
   public int debugSkipped = 0;
   public int debugUntil;
   public int error = 0;
   private boolean goingToirq = false;
   public int ignoreBreakpoint = 0;
   public String logFile = "I8080.log";
   private CpuBoard ram;
   public boolean state_HALT = false;
   private String tag;

   static {
      int var0;
      int var1;
      int var2;
      int var3;
      int[] var9;
      for(var3 = 0; var3 < 256; ++var3) {
         boolean var8 = true;

         boolean var7;
         for(var0 = 0; var0 < 8; var8 = var7) {
            var7 = var8;
            if((1 << var0 & var3) != 0) {
               if(var8) {
                  var7 = false;
               } else {
                  var7 = true;
               }
            }

            ++var0;
         }

         parity[var3] = var8;
         var1 = 0;
         if((var3 & 1) != 0) {
            var1 = 0 + 1;
         }

         var0 = var1;
         if((var3 & 2) != 0) {
            var0 = var1 + 1;
         }

         var1 = var0;
         if((var3 & 4) != 0) {
            var1 = var0 + 1;
         }

         var2 = var1;
         if((var3 & 8) != 0) {
            var2 = var1 + 1;
         }

         var0 = var2;
         if((var3 & 16) != 0) {
            var0 = var2 + 1;
         }

         var1 = var0;
         if((var3 & 32) != 0) {
            var1 = var0 + 1;
         }

         var0 = var1;
         if((var3 & 64) != 0) {
            var0 = var1 + 1;
         }

         if((var3 & 128) != 0) {
            ++var0;
         }

         var9 = SZ;
         if(var3 != 0) {
            var1 = var3 & 128;
         } else {
            var1 = 64;
         }

         var9[var3] = var1;
         var9 = SZ;
         var9[var3] |= var3 & 40;
         var9 = SZP;
         var1 = SZ[var3];
         byte var10;
         if((var0 & 1) != 0) {
            var10 = 0;
         } else {
            var10 = 4;
         }

         var9[var3] = var1 | var10;
         SZHV_inc[var3] = SZ[var3];
         if(var3 == 128) {
            var9 = SZHV_inc;
            var9[var3] |= 4;
         }

         if((var3 & 15) == 0) {
            var9 = SZHV_inc;
            var9[var3] |= 16;
         }

         SZHV_dec[var3] = SZ[var3] | 2;
         if(var3 == 127) {
            var9 = SZHV_dec;
            var9[var3] |= 4;
         }

         if((var3 & 15) == 15) {
            var9 = SZHV_dec;
            var9[var3] |= 16;
         }
      }

      var1 = 0;
      var2 = 65536;
      int var4 = 0;
      var3 = 65536;

      for(var0 = 0; var0 < 256; ++var0) {
         for(int var5 = 0; var5 < 256; ++var5) {
            int var6 = var5 - var0;
            if(var5 != 0) {
               if((var5 & 128) != 0) {
                  SZHVC_add[var4] = 128;
               } else {
                  SZHVC_add[var4] = 0;
               }
            } else {
               SZHVC_add[var4] = 64;
            }

            var9 = SZHVC_add;
            var9[var4] |= var5 & 40;
            if((var5 & 15) < (var0 & 15)) {
               var9 = SZHVC_add;
               var9[var4] |= 16;
            }

            if(var5 < var0) {
               var9 = SZHVC_add;
               var9[var4] |= 1;
            }

            if(((var6 ^ var5) & (var6 ^ var0 ^ 128) & 128) != 0) {
               var9 = SZHVC_add;
               var9[var4] |= 4;
            }

            ++var4;
            var6 = var5 - var0 - 1;
            if(var5 != 0) {
               if((var5 & 128) != 0) {
                  SZHVC_add[var3] = 128;
               } else {
                  SZHVC_add[var3] = 0;
               }
            } else {
               SZHVC_add[var3] = 64;
            }

            var9 = SZHVC_add;
            var9[var3] |= var5 & 40;
            if((var5 & 15) <= (var0 & 15)) {
               var9 = SZHVC_add;
               var9[var3] |= 16;
            }

            if(var5 <= var0) {
               var9 = SZHVC_add;
               var9[var3] |= 1;
            }

            if(((var6 ^ var5) & (var6 ^ var0 ^ 128) & 128) != 0) {
               var9 = SZHVC_add;
               var9[var3] |= 4;
            }

            ++var3;
            if(var5 != 0) {
               if((var5 & 128) != 0) {
                  SZHVC_sub[var1] = 2 | 128;
               } else {
                  SZHVC_sub[var1] = 2;
               }
            } else {
               SZHVC_sub[var1] = 2 | 64;
            }

            var9 = SZHVC_sub;
            var9[var1] |= var5 & 40;
            if((var5 & 15) > (var0 & 15)) {
               var9 = SZHVC_sub;
               var9[var1] |= 16;
            }

            if(var5 > var0) {
               var9 = SZHVC_sub;
               var9[var1] |= 1;
            }

            if(((var0 - var5 ^ var0) & (var0 ^ var5) & 128) != 0) {
               var9 = SZHVC_sub;
               var9[var1] |= 4;
            }

            ++var1;
            if(var5 != 0) {
               if((var5 & 128) != 0) {
                  SZHVC_sub[var2] = 2 | 128;
               } else {
                  SZHVC_sub[var2] = 2;
               }
            } else {
               SZHVC_sub[var2] = 2 | 64;
            }

            var9 = SZHVC_sub;
            var9[var2] |= var5 & 40;
            if((var5 & 15) >= (var0 & 15)) {
               var9 = SZHVC_sub;
               var9[var2] |= 16;
            }

            if(var5 >= var0) {
               var9 = SZHVC_sub;
               var9[var2] |= 1;
            }

            if(((var0 - var5 - 1 ^ var0) & (var0 ^ var5) & 128) != 0) {
               var9 = SZHVC_sub;
               var9[var2] |= 4;
            }

            ++var2;
         }
      }

   }

   private final int adcA_8(int var1, int var2) {
      int var3 = this.F & 1;
      var1 = var2 + var1 + var3 & 255;
      this.F = SZHVC_add[var3 << 16 | var2 << 8 | var1];
      return var1;
   }

   private final int add16(int var1, int var2) {
      int var3 = var1 + var2;
      this.F = this.F & 196 | (var1 ^ var3 ^ var2) >> 8 & 16 | var3 >> 16 & 1;
      return '\uffff' & var3;
   }

   private final int addA_8(int var1, int var2) {
      var1 = var2 + var1 & 255;
      this.F = SZHVC_add[var2 << 8 | var1];
      return var1;
   }

   private final int andA(int var1, int var2) {
      var1 &= var2;
      this.F = SZP[var1] | 16;
      return var1;
   }

   private final void cpA_8(int var1, int var2) {
      this.F = SZHVC_sub[var2 << 8 | var2 - var1 & 255];
   }

   private final int dec8(int var1) {
      var1 = var1 - 1 & 255;
      this.F = this.F & 1 | SZHV_dec[var1];
      return var1;
   }

   private final int in(int var1, int var2) {
      var1 = this.ram.in(var1);
      this.F = this.F & 1 | SZP[var2];
      return var1;
   }

   private final int inc8(int var1) {
      var1 = var1 + 1 & 255;
      this.F = this.F & 1 | SZHV_inc[var1];
      return var1;
   }

   private final int orA(int var1, int var2) {
      var1 |= var2;
      this.F = SZP[var1];
      return var1;
   }

   private final void out(int var1, int var2) {
      this.ram.out(var1, var2);
   }

   private final int rl_A(int var1) {
      int var2 = this.F;
      this.F = this.F & 236 | var1 >> 7;
      return (var1 << 1 | var2 & 1) & 255;
   }

   private final int rlc_A(int var1) {
      this.F = this.F & 236 | var1 >> 7;
      return (var1 << 1) + ((var1 & 128) >> 7) & 255;
   }

   private final int rr_A(int var1) {
      int var2 = this.F;
      this.F = this.F & 236 | var1 & 1;
      return (var1 >> 1 | (var2 & 1) << 7) & 255;
   }

   private final int rrc_A(int var1) {
      this.F = this.F & 236 | var1 & 41;
      return (var1 >> 1) + ((var1 & 1) << 7) & 255;
   }

   private final int sbcA_8(int var1, int var2) {
      int var3 = this.F & 1;
      var1 = var2 - var1 - var3 & 255;
      this.F = SZHVC_sub[var3 << 16 | var2 << 8 | var1];
      return var1;
   }

   private final int subA_8(int var1, int var2) {
      var1 = var2 - var1 & 255;
      this.F = SZHVC_sub[var2 << 8 | var1];
      return var1;
   }

   private final int xorA(int var1, int var2) {
      var1 ^= var2;
      this.F = SZP[var1];
      return var1;
   }

   public final void exec(int var1) {
      this.cycles_left += var1;
      int var14 = this.error;
      boolean var23 = this.NMI;
      boolean var28 = this.IRQ;
      boolean var19 = this.IFF0;
      boolean var20 = this.IFF1;
      int var12 = this.CurInstr;
      var1 = this.PC;
      int var13 = this.PPC;
      int var3 = this.SP;
      int var6 = this.B;
      int var9 = this.C;
      int var7 = this.D;
      int var8 = this.E;
      int var4 = this.H;
      int var5 = this.L;
      int var2 = this.A;
      CpuBoard var30 = this.ram;

      while(this.cycles_left > 0 && var14 == 0) {
         int var10;
         int var11;
         boolean var25;
         boolean var26;
         boolean var27;
         boolean var29;
         label421: {
            if(!var23) {
               var25 = var19;
               var29 = var20;
               var27 = var28;
               var26 = var23;
               var10 = var1;
               var11 = var3;
               if(!this.IFF0) {
                  break label421;
               }

               var25 = var19;
               var29 = var20;
               var27 = var28;
               var26 = var23;
               var10 = var1;
               var11 = var3;
               if(!this.IRQ) {
                  break label421;
               }
            }

            boolean var24 = var19;
            boolean var22 = var20;
            boolean var21 = var23;
            var13 = var1;
            var12 = var3;
            if(var23) {
               if(!this.goingToirq) {
                  this.state_HALT = false;
                  var24 = false;
                  var21 = false;
                  var12 = var3 - 2;
                  var30.write16fast(var12, var1);
                  var13 = 16;
                  this.cycles_left -= 13;
                  var22 = var19;
               } else {
                  this.goingToirq = false;
                  var24 = var19;
                  var22 = var20;
                  var21 = var23;
                  var13 = var1;
                  var12 = var3;
               }
            }

            var25 = var24;
            var29 = var22;
            var27 = var28;
            var26 = var21;
            var10 = var13;
            var11 = var12;
            if(var24) {
               var25 = var24;
               var29 = var22;
               var27 = var28;
               var26 = var21;
               var10 = var13;
               var11 = var12;
               if(var28) {
                  if(!this.goingToirq) {
                     this.state_HALT = false;
                     var25 = false;
                     var27 = false;
                     var11 = var12 - 2;
                     var30.write16fast(var11, var13);
                     var10 = 8;
                     this.cycles_left -= 13;
                     var26 = var21;
                     var29 = var22;
                  } else {
                     this.goingToirq = false;
                     var25 = var24;
                     var29 = var22;
                     var27 = var28;
                     var26 = var21;
                     var10 = var13;
                     var11 = var12;
                  }
               }
            }
         }

         var12 = var30.read8opc(var10);
         var13 = var10;
         int var16 = var10 + 1 & '\uffff';
         int var15;
         switch(var12) {
         case 0:
            this.cycles_left -= 4;
            var19 = var25;
            var20 = var29;
            var28 = var27;
            var23 = var26;
            var1 = var16;
            var3 = var11;
            break;
         case 1:
            this.cycles_left -= 10;
            var6 = var30.read8(var16 + 1);
            var9 = var30.read8arg(var16);
            var1 = var16 + 2;
            var19 = var25;
            var20 = var29;
            var28 = var27;
            var23 = var26;
            var3 = var11;
            break;
         case 2:
            this.cycles_left -= 7;
            var30.write8(var6 << 8 | var9, var2);
            var19 = var25;
            var20 = var29;
            var28 = var27;
            var23 = var26;
            var1 = var16;
            var3 = var11;
            break;
         case 3:
            this.cycles_left -= 6;
            var1 = (var6 << 8 | var9) + 1 & '\uffff';
            var6 = var1 >> 8;
            var9 = var1 & 255;
            var19 = var25;
            var20 = var29;
            var28 = var27;
            var23 = var26;
            var1 = var16;
            var3 = var11;
            break;
         case 4:
            this.cycles_left -= 4;
            var6 = this.inc8(var6);
            var19 = var25;
            var20 = var29;
            var28 = var27;
            var23 = var26;
            var1 = var16;
            var3 = var11;
            break;
         case 5:
            this.cycles_left -= 4;
            var6 = this.dec8(var6);
            var19 = var25;
            var20 = var29;
            var28 = var27;
            var23 = var26;
            var1 = var16;
            var3 = var11;
            break;
         case 6:
            this.cycles_left -= 7;
            var6 = var30.read8arg(var16);
            var1 = var16 + 1;
            var19 = var25;
            var20 = var29;
            var28 = var27;
            var23 = var26;
            var3 = var11;
            break;
         case 7:
            this.cycles_left -= 4;
            var2 = this.rlc_A(var2);
            var19 = var25;
            var20 = var29;
            var28 = var27;
            var23 = var26;
            var1 = var16;
            var3 = var11;
            break;
         case 8:
            this.cycles_left -= 4;
            var1 = this.A1;
            this.A1 = var2;
            var2 = this.F;
            this.F = this.F1;
            this.F1 = var2;
            var2 = var1;
            var19 = var25;
            var20 = var29;
            var28 = var27;
            var23 = var26;
            var1 = var16;
            var3 = var11;
            break;
         case 9:
            this.cycles_left -= 11;
            var1 = this.add16(var4 << 8 | var5, var6 << 8 | var9);
            var5 = var1 & 255;
            var4 = var1 >> 8;
            var19 = var25;
            var20 = var29;
            var28 = var27;
            var23 = var26;
            var1 = var16;
            var3 = var11;
            break;
         case 10:
            this.cycles_left -= 7;
            var2 = var30.read8(var6 << 8 | var9);
            var19 = var25;
            var20 = var29;
            var28 = var27;
            var23 = var26;
            var1 = var16;
            var3 = var11;
            break;
         case 11:
            this.cycles_left -= 6;
            var1 = (var6 << 8 | var9) - 1 & '\uffff';
            var6 = var1 >> 8;
            var9 = var1 & 255;
            var19 = var25;
            var20 = var29;
            var28 = var27;
            var23 = var26;
            var1 = var16;
            var3 = var11;
            break;
         case 12:
            this.cycles_left -= 4;
            var9 = this.inc8(var9);
            var19 = var25;
            var20 = var29;
            var28 = var27;
            var23 = var26;
            var1 = var16;
            var3 = var11;
            break;
         case 13:
            this.cycles_left -= 4;
            var9 = this.dec8(var9);
            var19 = var25;
            var20 = var29;
            var28 = var27;
            var23 = var26;
            var1 = var16;
            var3 = var11;
            break;
         case 14:
            this.cycles_left -= 7;
            var9 = var30.read8arg(var16);
            var1 = var16 + 1;
            var19 = var25;
            var20 = var29;
            var28 = var27;
            var23 = var26;
            var3 = var11;
            break;
         case 15:
            this.cycles_left -= 4;
            var2 = this.rrc_A(var2);
            var19 = var25;
            var20 = var29;
            var28 = var27;
            var23 = var26;
            var1 = var16;
            var3 = var11;
            break;
         case 16:
            var6 = var6 - 1 & 255;
            if(var6 != 0) {
               var1 = var30.read8arg(var16);
               this.cycles_left -= 13;
               var1 = var16 + 1 + (var1 - ((var1 & 128) << 1));
               var19 = var25;
               var20 = var29;
               var28 = var27;
               var23 = var26;
               var3 = var11;
            } else {
               var1 = var16 + 1;
               this.cycles_left -= 8;
               var19 = var25;
               var20 = var29;
               var28 = var27;
               var23 = var26;
               var3 = var11;
            }
            break;
         case 17:
            this.cycles_left -= 10;
            var7 = var30.read8(var16 + 1);
            var8 = var30.read8arg(var16);
            var1 = var16 + 2;
            var19 = var25;
            var20 = var29;
            var28 = var27;
            var23 = var26;
            var3 = var11;
            break;
         case 18:
            this.cycles_left -= 7;
            var30.write8(var7 << 8 | var8, var2);
            var19 = var25;
            var20 = var29;
            var28 = var27;
            var23 = var26;
            var1 = var16;
            var3 = var11;
            break;
         case 19:
            this.cycles_left -= 6;
            var1 = (var7 << 8 | var8) + 1 & '\uffff';
            var7 = var1 >> 8;
            var8 = var1 & 255;
            var19 = var25;
            var20 = var29;
            var28 = var27;
            var23 = var26;
            var1 = var16;
            var3 = var11;
            break;
         case 20:
            this.cycles_left -= 4;
            var7 = this.inc8(var7);
            var19 = var25;
            var20 = var29;
            var28 = var27;
            var23 = var26;
            var1 = var16;
            var3 = var11;
            break;
         case 21:
            this.cycles_left -= 4;
            var7 = this.dec8(var7);
            var19 = var25;
            var20 = var29;
            var28 = var27;
            var23 = var26;
            var1 = var16;
            var3 = var11;
            break;
         case 22:
            this.cycles_left -= 7;
            var7 = var30.read8arg(var16);
            var1 = var16 + 1;
            var19 = var25;
            var20 = var29;
            var28 = var27;
            var23 = var26;
            var3 = var11;
            break;
         case 23:
            this.cycles_left -= 4;
            var2 = this.rl_A(var2);
            var19 = var25;
            var20 = var29;
            var28 = var27;
            var23 = var26;
            var1 = var16;
            var3 = var11;
            break;
         case 24:
            this.cycles_left -= 12;
            var1 = var30.read8arg(var16);
            var1 = var16 + 1 + (var1 - ((var1 & 128) << 1));
            var19 = var25;
            var20 = var29;
            var28 = var27;
            var23 = var26;
            var3 = var11;
            break;
         case 25:
            this.cycles_left -= 11;
            var1 = this.add16(var4 << 8 | var5, var7 << 8 | var8);
            var5 = var1 & 255;
            var4 = var1 >> 8;
            var19 = var25;
            var20 = var29;
            var28 = var27;
            var23 = var26;
            var1 = var16;
            var3 = var11;
            break;
         case 26:
            this.cycles_left -= 7;
            var2 = var30.read8(var7 << 8 | var8);
            var19 = var25;
            var20 = var29;
            var28 = var27;
            var23 = var26;
            var1 = var16;
            var3 = var11;
            break;
         case 27:
            this.cycles_left -= 6;
            var1 = (var7 << 8 | var8) - 1 & '\uffff';
            var7 = var1 >> 8;
            var8 = var1 & 255;
            var19 = var25;
            var20 = var29;
            var28 = var27;
            var23 = var26;
            var1 = var16;
            var3 = var11;
            break;
         case 28:
            this.cycles_left -= 4;
            var8 = this.inc8(var8);
            var19 = var25;
            var20 = var29;
            var28 = var27;
            var23 = var26;
            var1 = var16;
            var3 = var11;
            break;
         case 29:
            this.cycles_left -= 4;
            var8 = this.dec8(var8);
            var19 = var25;
            var20 = var29;
            var28 = var27;
            var23 = var26;
            var1 = var16;
            var3 = var11;
            break;
         case 30:
            this.cycles_left -= 7;
            var8 = var30.read8arg(var16);
            var1 = var16 + 1;
            var19 = var25;
            var20 = var29;
            var28 = var27;
            var23 = var26;
            var3 = var11;
            break;
         case 31:
            this.cycles_left -= 4;
            var2 = this.rr_A(var2);
            var19 = var25;
            var20 = var29;
            var28 = var27;
            var23 = var26;
            var1 = var16;
            var3 = var11;
            break;
         case 32:
            if((this.F & 64) == 0) {
               var1 = var30.read8arg(var16);
               this.cycles_left -= 12;
               var1 = var16 + 1 + (var1 - ((var1 & 128) << 1));
               var19 = var25;
               var20 = var29;
               var28 = var27;
               var23 = var26;
               var3 = var11;
            } else {
               var1 = var16 + 1;
               this.cycles_left -= 7;
               var19 = var25;
               var20 = var29;
               var28 = var27;
               var23 = var26;
               var3 = var11;
            }
            break;
         case 33:
            this.cycles_left -= 10;
            var4 = var30.read8(var16 + 1);
            var5 = var30.read8arg(var16);
            var1 = var16 + 2;
            var19 = var25;
            var20 = var29;
            var28 = var27;
            var23 = var26;
            var3 = var11;
            break;
         case 34:
            this.cycles_left -= 16;
            var30.write16(var30.read16arg(var16), var4 << 8 | var5);
            var1 = var16 + 2;
            var19 = var25;
            var20 = var29;
            var28 = var27;
            var23 = var26;
            var3 = var11;
            break;
         case 35:
            this.cycles_left -= 6;
            var1 = (var4 << 8 | var5) + 1 & '\uffff';
            var4 = var1 >> 8;
            var5 = var1 & 255;
            var19 = var25;
            var20 = var29;
            var28 = var27;
            var23 = var26;
            var1 = var16;
            var3 = var11;
            break;
         case 36:
            this.cycles_left -= 4;
            var4 = this.inc8(var4);
            var19 = var25;
            var20 = var29;
            var28 = var27;
            var23 = var26;
            var1 = var16;
            var3 = var11;
            break;
         case 37:
            this.cycles_left -= 4;
            var4 = this.dec8(var4);
            var19 = var25;
            var20 = var29;
            var28 = var27;
            var23 = var26;
            var1 = var16;
            var3 = var11;
            break;
         case 38:
            this.cycles_left -= 7;
            var4 = var30.read8arg(var16);
            var1 = var16 + 1;
            var19 = var25;
            var20 = var29;
            var28 = var27;
            var23 = var26;
            var3 = var11;
            break;
         case 39:
            this.cycles_left -= 4;
            var3 = 0;
            var1 = this.F & 1;
            var10 = var1;
            if((this.F & 16) != 0 || (var2 & 15) > 9) {
               var3 = 0 | 6;
            }

            label400: {
               if(var1 != 1 && var2 <= 159) {
                  var15 = var3;
                  var1 = var1;
                  if(var2 <= 143) {
                     break label400;
                  }

                  var15 = var3;
                  var1 = var10;
                  if((var2 & 15) <= 9) {
                     break label400;
                  }
               }

               var15 = var3 | 96;
               var1 = 1;
            }

            if(var2 > 153) {
               var1 = 1;
            }

            if((this.F & 2) != 0) {
               this.cycles_left -= 4;
               var2 = this.subA_8(var15, var2);
            } else {
               this.cycles_left -= 4;
               var2 = this.addA_8(var15, var2);
            }

            this.F = this.F & 254 | var1;
            if(parity[var2]) {
               this.F = this.F & 251 | 4;
               var19 = var25;
               var20 = var29;
               var28 = var27;
               var23 = var26;
               var1 = var16;
               var3 = var11;
            } else {
               this.F &= 251;
               var19 = var25;
               var20 = var29;
               var28 = var27;
               var23 = var26;
               var1 = var16;
               var3 = var11;
            }
            break;
         case 40:
            if((this.F & 64) != 0) {
               var1 = var30.read8arg(var16);
               this.cycles_left -= 12;
               var1 = var16 + 1 + (var1 - ((var1 & 128) << 1));
               var19 = var25;
               var20 = var29;
               var28 = var27;
               var23 = var26;
               var3 = var11;
            } else {
               var1 = var16 + 1;
               this.cycles_left -= 7;
               var19 = var25;
               var20 = var29;
               var28 = var27;
               var23 = var26;
               var3 = var11;
            }
            break;
         case 41:
            this.cycles_left -= 11;
            var1 = this.add16(var4 << 8 | var5, var4 << 8 | var5);
            var5 = var1 & 255;
            var4 = var1 >> 8;
            var19 = var25;
            var20 = var29;
            var28 = var27;
            var23 = var26;
            var1 = var16;
            var3 = var11;
            break;
         case 42:
            this.cycles_left -= 16;
            var4 = var30.read8(var30.read16arg(var16) + 1);
            var5 = var30.read8(var30.read16arg(var16));
            var1 = var16 + 2;
            var19 = var25;
            var20 = var29;
            var28 = var27;
            var23 = var26;
            var3 = var11;
            break;
         case 43:
            this.cycles_left -= 6;
            var1 = (var4 << 8 | var5) - 1 & '\uffff';
            var4 = var1 >> 8;
            var5 = var1 & 255;
            var19 = var25;
            var20 = var29;
            var28 = var27;
            var23 = var26;
            var1 = var16;
            var3 = var11;
            break;
         case 44:
            this.cycles_left -= 4;
            var5 = this.inc8(var5);
            var19 = var25;
            var20 = var29;
            var28 = var27;
            var23 = var26;
            var1 = var16;
            var3 = var11;
            break;
         case 45:
            this.cycles_left -= 4;
            var5 = this.dec8(var5);
            var19 = var25;
            var20 = var29;
            var28 = var27;
            var23 = var26;
            var1 = var16;
            var3 = var11;
            break;
         case 46:
            this.cycles_left -= 7;
            var5 = var30.read8arg(var16);
            var1 = var16 + 1;
            var19 = var25;
            var20 = var29;
            var28 = var27;
            var23 = var26;
            var3 = var11;
            break;
         case 47:
            this.cycles_left -= 4;
            var2 ^= 255;
            this.F = this.F & 197 | 18 | var2 & 40;
            var19 = var25;
            var20 = var29;
            var28 = var27;
            var23 = var26;
            var1 = var16;
            var3 = var11;
            break;
         case 48:
            if((this.F & 1) == 0) {
               var1 = var30.read8arg(var16);
               this.cycles_left -= 12;
               var1 = var16 + 1 + (var1 - ((var1 & 128) << 1));
               var19 = var25;
               var20 = var29;
               var28 = var27;
               var23 = var26;
               var3 = var11;
            } else {
               var1 = var16 + 1;
               this.cycles_left -= 7;
               var19 = var25;
               var20 = var29;
               var28 = var27;
               var23 = var26;
               var3 = var11;
            }
            break;
         case 49:
            this.cycles_left -= 10;
            var3 = var30.read16(var16);
            var1 = var16 + 2;
            var19 = var25;
            var20 = var29;
            var28 = var27;
            var23 = var26;
            break;
         case 50:
            this.cycles_left -= 13;
            var30.write8(var30.read16arg(var16), var2);
            var1 = var16 + 2;
            var19 = var25;
            var20 = var29;
            var28 = var27;
            var23 = var26;
            var3 = var11;
            break;
         case 51:
            this.cycles_left -= 6;
            var3 = var11 + 1 & '\uffff';
            var19 = var25;
            var20 = var29;
            var28 = var27;
            var23 = var26;
            var1 = var16;
            break;
         case 52:
            this.cycles_left -= 11;
            var1 = var4 << 8 | var5;
            var30.write8(var1, this.inc8(var30.read8(var1)));
            var19 = var25;
            var20 = var29;
            var28 = var27;
            var23 = var26;
            var1 = var16;
            var3 = var11;
            break;
         case 53:
            this.cycles_left -= 11;
            var1 = var4 << 8 | var5;
            var30.write8(var1, this.dec8(var30.read8(var1)));
            var19 = var25;
            var20 = var29;
            var28 = var27;
            var23 = var26;
            var1 = var16;
            var3 = var11;
            break;
         case 54:
            this.cycles_left -= 10;
            var30.write8(var4 << 8 | var5, var30.read8arg(var16));
            var1 = var16 + 1;
            var19 = var25;
            var20 = var29;
            var28 = var27;
            var23 = var26;
            var3 = var11;
            break;
         case 55:
            this.cycles_left -= 4;
            this.F = this.F & 196 | 1 | var2 & 40;
            var19 = var25;
            var20 = var29;
            var28 = var27;
            var23 = var26;
            var1 = var16;
            var3 = var11;
            break;
         case 56:
            if((this.F & 1) != 0) {
               var1 = var30.read8arg(var16);
               this.cycles_left -= 12;
               var1 = var16 + 1 + (var1 - ((var1 & 128) << 1));
               var19 = var25;
               var20 = var29;
               var28 = var27;
               var23 = var26;
               var3 = var11;
            } else {
               var1 = var16 + 1;
               this.cycles_left -= 7;
               var19 = var25;
               var20 = var29;
               var28 = var27;
               var23 = var26;
               var3 = var11;
            }
            break;
         case 57:
            this.cycles_left -= 11;
            var1 = this.add16(var4 << 8 | var5, var11);
            var5 = var1 & 255;
            var4 = var1 >> 8;
            var19 = var25;
            var20 = var29;
            var28 = var27;
            var23 = var26;
            var1 = var16;
            var3 = var11;
            break;
         case 58:
            this.cycles_left -= 13;
            var2 = var30.read8(var30.read16arg(var16));
            var1 = var16 + 2;
            var19 = var25;
            var20 = var29;
            var28 = var27;
            var23 = var26;
            var3 = var11;
            break;
         case 59:
            this.cycles_left -= 6;
            var3 = var11 - 1 & '\uffff';
            var19 = var25;
            var20 = var29;
            var28 = var27;
            var23 = var26;
            var1 = var16;
            break;
         case 60:
            this.cycles_left -= 4;
            var2 = this.inc8(var2);
            var19 = var25;
            var20 = var29;
            var28 = var27;
            var23 = var26;
            var1 = var16;
            var3 = var11;
            break;
         case 61:
            this.cycles_left -= 4;
            var2 = this.dec8(var2);
            var19 = var25;
            var20 = var29;
            var28 = var27;
            var23 = var26;
            var1 = var16;
            var3 = var11;
            break;
         case 62:
            this.cycles_left -= 7;
            var2 = var30.read8arg(var16);
            var1 = var16 + 1;
            var19 = var25;
            var20 = var29;
            var28 = var27;
            var23 = var26;
            var3 = var11;
            break;
         case 63:
            this.cycles_left -= 4;
            this.F = (this.F & 197 | (this.F & 1) << 4 | var2 & 40) ^ 1;
            var19 = var25;
            var20 = var29;
            var28 = var27;
            var23 = var26;
            var1 = var16;
            var3 = var11;
            break;
         case 64:
            this.cycles_left -= 4;
            var19 = var25;
            var20 = var29;
            var28 = var27;
            var23 = var26;
            var1 = var16;
            var3 = var11;
            break;
         case 65:
            this.cycles_left -= 4;
            var6 = var9;
            var19 = var25;
            var20 = var29;
            var28 = var27;
            var23 = var26;
            var1 = var16;
            var3 = var11;
            break;
         case 66:
            this.cycles_left -= 4;
            var6 = var7;
            var19 = var25;
            var20 = var29;
            var28 = var27;
            var23 = var26;
            var1 = var16;
            var3 = var11;
            break;
         case 67:
            this.cycles_left -= 4;
            var6 = var8;
            var19 = var25;
            var20 = var29;
            var28 = var27;
            var23 = var26;
            var1 = var16;
            var3 = var11;
            break;
         case 68:
            this.cycles_left -= 4;
            var6 = var4;
            var19 = var25;
            var20 = var29;
            var28 = var27;
            var23 = var26;
            var1 = var16;
            var3 = var11;
            break;
         case 69:
            this.cycles_left -= 4;
            var6 = var5;
            var19 = var25;
            var20 = var29;
            var28 = var27;
            var23 = var26;
            var1 = var16;
            var3 = var11;
            break;
         case 70:
            this.cycles_left -= 7;
            var6 = var30.read8(var4 << 8 | var5);
            var19 = var25;
            var20 = var29;
            var28 = var27;
            var23 = var26;
            var1 = var16;
            var3 = var11;
            break;
         case 71:
            this.cycles_left -= 4;
            var6 = var2;
            var19 = var25;
            var20 = var29;
            var28 = var27;
            var23 = var26;
            var1 = var16;
            var3 = var11;
            break;
         case 72:
            this.cycles_left -= 4;
            var9 = var6;
            var19 = var25;
            var20 = var29;
            var28 = var27;
            var23 = var26;
            var1 = var16;
            var3 = var11;
            break;
         case 73:
            this.cycles_left -= 4;
            var19 = var25;
            var20 = var29;
            var28 = var27;
            var23 = var26;
            var1 = var16;
            var3 = var11;
            break;
         case 74:
            this.cycles_left -= 4;
            var9 = var7;
            var19 = var25;
            var20 = var29;
            var28 = var27;
            var23 = var26;
            var1 = var16;
            var3 = var11;
            break;
         case 75:
            this.cycles_left -= 4;
            var9 = var8;
            var19 = var25;
            var20 = var29;
            var28 = var27;
            var23 = var26;
            var1 = var16;
            var3 = var11;
            break;
         case 76:
            this.cycles_left -= 4;
            var9 = var4;
            var19 = var25;
            var20 = var29;
            var28 = var27;
            var23 = var26;
            var1 = var16;
            var3 = var11;
            break;
         case 77:
            this.cycles_left -= 4;
            var9 = var5;
            var19 = var25;
            var20 = var29;
            var28 = var27;
            var23 = var26;
            var1 = var16;
            var3 = var11;
            break;
         case 78:
            this.cycles_left -= 7;
            var9 = var30.read8(var4 << 8 | var5);
            var19 = var25;
            var20 = var29;
            var28 = var27;
            var23 = var26;
            var1 = var16;
            var3 = var11;
            break;
         case 79:
            this.cycles_left -= 4;
            var9 = var2;
            var19 = var25;
            var20 = var29;
            var28 = var27;
            var23 = var26;
            var1 = var16;
            var3 = var11;
            break;
         case 80:
            this.cycles_left -= 4;
            var7 = var6;
            var19 = var25;
            var20 = var29;
            var28 = var27;
            var23 = var26;
            var1 = var16;
            var3 = var11;
            break;
         case 81:
            this.cycles_left -= 4;
            var7 = var9;
            var19 = var25;
            var20 = var29;
            var28 = var27;
            var23 = var26;
            var1 = var16;
            var3 = var11;
            break;
         case 82:
            this.cycles_left -= 4;
            var19 = var25;
            var20 = var29;
            var28 = var27;
            var23 = var26;
            var1 = var16;
            var3 = var11;
            break;
         case 83:
            this.cycles_left -= 4;
            var7 = var8;
            var19 = var25;
            var20 = var29;
            var28 = var27;
            var23 = var26;
            var1 = var16;
            var3 = var11;
            break;
         case 84:
            this.cycles_left -= 4;
            var7 = var4;
            var19 = var25;
            var20 = var29;
            var28 = var27;
            var23 = var26;
            var1 = var16;
            var3 = var11;
            break;
         case 85:
            this.cycles_left -= 4;
            var7 = var5;
            var19 = var25;
            var20 = var29;
            var28 = var27;
            var23 = var26;
            var1 = var16;
            var3 = var11;
            break;
         case 86:
            this.cycles_left -= 7;
            var7 = var30.read8(var4 << 8 | var5);
            var19 = var25;
            var20 = var29;
            var28 = var27;
            var23 = var26;
            var1 = var16;
            var3 = var11;
            break;
         case 87:
            this.cycles_left -= 4;
            var7 = var2;
            var19 = var25;
            var20 = var29;
            var28 = var27;
            var23 = var26;
            var1 = var16;
            var3 = var11;
            break;
         case 88:
            this.cycles_left -= 4;
            var8 = var6;
            var19 = var25;
            var20 = var29;
            var28 = var27;
            var23 = var26;
            var1 = var16;
            var3 = var11;
            break;
         case 89:
            this.cycles_left -= 4;
            var8 = var9;
            var19 = var25;
            var20 = var29;
            var28 = var27;
            var23 = var26;
            var1 = var16;
            var3 = var11;
            break;
         case 90:
            this.cycles_left -= 4;
            var8 = var7;
            var19 = var25;
            var20 = var29;
            var28 = var27;
            var23 = var26;
            var1 = var16;
            var3 = var11;
            break;
         case 91:
            this.cycles_left -= 4;
            var19 = var25;
            var20 = var29;
            var28 = var27;
            var23 = var26;
            var1 = var16;
            var3 = var11;
            break;
         case 92:
            this.cycles_left -= 4;
            var8 = var4;
            var19 = var25;
            var20 = var29;
            var28 = var27;
            var23 = var26;
            var1 = var16;
            var3 = var11;
            break;
         case 93:
            this.cycles_left -= 4;
            var8 = var5;
            var19 = var25;
            var20 = var29;
            var28 = var27;
            var23 = var26;
            var1 = var16;
            var3 = var11;
            break;
         case 94:
            this.cycles_left -= 7;
            var8 = var30.read8(var4 << 8 | var5);
            var19 = var25;
            var20 = var29;
            var28 = var27;
            var23 = var26;
            var1 = var16;
            var3 = var11;
            break;
         case 95:
            this.cycles_left -= 4;
            var8 = var2;
            var19 = var25;
            var20 = var29;
            var28 = var27;
            var23 = var26;
            var1 = var16;
            var3 = var11;
            break;
         case 96:
            this.cycles_left -= 4;
            var4 = var6;
            var19 = var25;
            var20 = var29;
            var28 = var27;
            var23 = var26;
            var1 = var16;
            var3 = var11;
            break;
         case 97:
            this.cycles_left -= 4;
            var4 = var9;
            var19 = var25;
            var20 = var29;
            var28 = var27;
            var23 = var26;
            var1 = var16;
            var3 = var11;
            break;
         case 98:
            this.cycles_left -= 4;
            var4 = var7;
            var19 = var25;
            var20 = var29;
            var28 = var27;
            var23 = var26;
            var1 = var16;
            var3 = var11;
            break;
         case 99:
            this.cycles_left -= 4;
            var4 = var8;
            var19 = var25;
            var20 = var29;
            var28 = var27;
            var23 = var26;
            var1 = var16;
            var3 = var11;
            break;
         case 100:
            this.cycles_left -= 4;
            var19 = var25;
            var20 = var29;
            var28 = var27;
            var23 = var26;
            var1 = var16;
            var3 = var11;
            break;
         case 101:
            this.cycles_left -= 4;
            var4 = var5;
            var19 = var25;
            var20 = var29;
            var28 = var27;
            var23 = var26;
            var1 = var16;
            var3 = var11;
            break;
         case 102:
            this.cycles_left -= 7;
            var4 = var30.read8(var4 << 8 | var5);
            var19 = var25;
            var20 = var29;
            var28 = var27;
            var23 = var26;
            var1 = var16;
            var3 = var11;
            break;
         case 103:
            this.cycles_left -= 4;
            var4 = var2;
            var19 = var25;
            var20 = var29;
            var28 = var27;
            var23 = var26;
            var1 = var16;
            var3 = var11;
            break;
         case 104:
            this.cycles_left -= 4;
            var5 = var6;
            var19 = var25;
            var20 = var29;
            var28 = var27;
            var23 = var26;
            var1 = var16;
            var3 = var11;
            break;
         case 105:
            this.cycles_left -= 4;
            var5 = var9;
            var19 = var25;
            var20 = var29;
            var28 = var27;
            var23 = var26;
            var1 = var16;
            var3 = var11;
            break;
         case 106:
            this.cycles_left -= 4;
            var5 = var7;
            var19 = var25;
            var20 = var29;
            var28 = var27;
            var23 = var26;
            var1 = var16;
            var3 = var11;
            break;
         case 107:
            this.cycles_left -= 4;
            var5 = var8;
            var19 = var25;
            var20 = var29;
            var28 = var27;
            var23 = var26;
            var1 = var16;
            var3 = var11;
            break;
         case 108:
            this.cycles_left -= 4;
            var5 = var4;
            var19 = var25;
            var20 = var29;
            var28 = var27;
            var23 = var26;
            var1 = var16;
            var3 = var11;
            break;
         case 109:
            this.cycles_left -= 4;
            var19 = var25;
            var20 = var29;
            var28 = var27;
            var23 = var26;
            var1 = var16;
            var3 = var11;
            break;
         case 110:
            this.cycles_left -= 7;
            var5 = var30.read8(var4 << 8 | var5);
            var19 = var25;
            var20 = var29;
            var28 = var27;
            var23 = var26;
            var1 = var16;
            var3 = var11;
            break;
         case 111:
            this.cycles_left -= 4;
            var5 = var2;
            var19 = var25;
            var20 = var29;
            var28 = var27;
            var23 = var26;
            var1 = var16;
            var3 = var11;
            break;
         case 112:
            this.cycles_left -= 7;
            var30.write8(var4 << 8 | var5, var6);
            var19 = var25;
            var20 = var29;
            var28 = var27;
            var23 = var26;
            var1 = var16;
            var3 = var11;
            break;
         case 113:
            this.cycles_left -= 7;
            var30.write8(var4 << 8 | var5, var9);
            var19 = var25;
            var20 = var29;
            var28 = var27;
            var23 = var26;
            var1 = var16;
            var3 = var11;
            break;
         case 114:
            this.cycles_left -= 7;
            var30.write8(var4 << 8 | var5, var7);
            var19 = var25;
            var20 = var29;
            var28 = var27;
            var23 = var26;
            var1 = var16;
            var3 = var11;
            break;
         case 115:
            this.cycles_left -= 7;
            var30.write8(var4 << 8 | var5, var8);
            var19 = var25;
            var20 = var29;
            var28 = var27;
            var23 = var26;
            var1 = var16;
            var3 = var11;
            break;
         case 116:
            this.cycles_left -= 7;
            var30.write8(var4 << 8 | var5, var4);
            var19 = var25;
            var20 = var29;
            var28 = var27;
            var23 = var26;
            var1 = var16;
            var3 = var11;
            break;
         case 117:
            this.cycles_left -= 7;
            var30.write8(var4 << 8 | var5, var5);
            var19 = var25;
            var20 = var29;
            var28 = var27;
            var23 = var26;
            var1 = var16;
            var3 = var11;
            break;
         case 118:
            this.cycles_left -= 4;
            this.state_HALT = true;
            var1 = var16 - 1;
            this.cycles_left = 0;
            var19 = var25;
            var20 = var29;
            var28 = var27;
            var23 = var26;
            var3 = var11;
            break;
         case 119:
            this.cycles_left -= 7;
            var30.write8(var4 << 8 | var5, var2);
            var19 = var25;
            var20 = var29;
            var28 = var27;
            var23 = var26;
            var1 = var16;
            var3 = var11;
            break;
         case 120:
            this.cycles_left -= 4;
            var2 = var6;
            var19 = var25;
            var20 = var29;
            var28 = var27;
            var23 = var26;
            var1 = var16;
            var3 = var11;
            break;
         case 121:
            this.cycles_left -= 4;
            var2 = var9;
            var19 = var25;
            var20 = var29;
            var28 = var27;
            var23 = var26;
            var1 = var16;
            var3 = var11;
            break;
         case 122:
            this.cycles_left -= 4;
            var2 = var7;
            var19 = var25;
            var20 = var29;
            var28 = var27;
            var23 = var26;
            var1 = var16;
            var3 = var11;
            break;
         case 123:
            this.cycles_left -= 4;
            var2 = var8;
            var19 = var25;
            var20 = var29;
            var28 = var27;
            var23 = var26;
            var1 = var16;
            var3 = var11;
            break;
         case 124:
            this.cycles_left -= 4;
            var2 = var4;
            var19 = var25;
            var20 = var29;
            var28 = var27;
            var23 = var26;
            var1 = var16;
            var3 = var11;
            break;
         case 125:
            this.cycles_left -= 4;
            var2 = var5;
            var19 = var25;
            var20 = var29;
            var28 = var27;
            var23 = var26;
            var1 = var16;
            var3 = var11;
            break;
         case 126:
            this.cycles_left -= 7;
            var2 = var30.read8(var4 << 8 | var5);
            var19 = var25;
            var20 = var29;
            var28 = var27;
            var23 = var26;
            var1 = var16;
            var3 = var11;
            break;
         case 127:
            this.cycles_left -= 4;
            var19 = var25;
            var20 = var29;
            var28 = var27;
            var23 = var26;
            var1 = var16;
            var3 = var11;
            break;
         case 128:
            this.cycles_left -= 4;
            var2 = this.addA_8(var6, var2);
            var19 = var25;
            var20 = var29;
            var28 = var27;
            var23 = var26;
            var1 = var16;
            var3 = var11;
            break;
         case 129:
            this.cycles_left -= 4;
            var2 = this.addA_8(var9, var2);
            var19 = var25;
            var20 = var29;
            var28 = var27;
            var23 = var26;
            var1 = var16;
            var3 = var11;
            break;
         case 130:
            this.cycles_left -= 4;
            var2 = this.addA_8(var7, var2);
            var19 = var25;
            var20 = var29;
            var28 = var27;
            var23 = var26;
            var1 = var16;
            var3 = var11;
            break;
         case 131:
            this.cycles_left -= 4;
            var2 = this.addA_8(var8, var2);
            var19 = var25;
            var20 = var29;
            var28 = var27;
            var23 = var26;
            var1 = var16;
            var3 = var11;
            break;
         case 132:
            this.cycles_left -= 4;
            var2 = this.addA_8(var4, var2);
            var19 = var25;
            var20 = var29;
            var28 = var27;
            var23 = var26;
            var1 = var16;
            var3 = var11;
            break;
         case 133:
            this.cycles_left -= 4;
            var2 = this.addA_8(var5, var2);
            var19 = var25;
            var20 = var29;
            var28 = var27;
            var23 = var26;
            var1 = var16;
            var3 = var11;
            break;
         case 134:
            this.cycles_left -= 7;
            var2 = this.addA_8(var30.read8(var4 << 8 | var5), var2);
            var19 = var25;
            var20 = var29;
            var28 = var27;
            var23 = var26;
            var1 = var16;
            var3 = var11;
            break;
         case 135:
            this.cycles_left -= 4;
            var2 = this.addA_8(var2, var2);
            var19 = var25;
            var20 = var29;
            var28 = var27;
            var23 = var26;
            var1 = var16;
            var3 = var11;
            break;
         case 136:
            this.cycles_left -= 4;
            var2 = this.adcA_8(var6, var2);
            var19 = var25;
            var20 = var29;
            var28 = var27;
            var23 = var26;
            var1 = var16;
            var3 = var11;
            break;
         case 137:
            this.cycles_left -= 4;
            var2 = this.adcA_8(var9, var2);
            var19 = var25;
            var20 = var29;
            var28 = var27;
            var23 = var26;
            var1 = var16;
            var3 = var11;
            break;
         case 138:
            this.cycles_left -= 4;
            var2 = this.adcA_8(var7, var2);
            var19 = var25;
            var20 = var29;
            var28 = var27;
            var23 = var26;
            var1 = var16;
            var3 = var11;
            break;
         case 139:
            this.cycles_left -= 4;
            var2 = this.adcA_8(var8, var2);
            var19 = var25;
            var20 = var29;
            var28 = var27;
            var23 = var26;
            var1 = var16;
            var3 = var11;
            break;
         case 140:
            this.cycles_left -= 4;
            var2 = this.adcA_8(var4, var2);
            var19 = var25;
            var20 = var29;
            var28 = var27;
            var23 = var26;
            var1 = var16;
            var3 = var11;
            break;
         case 141:
            this.cycles_left -= 4;
            var2 = this.adcA_8(var5, var2);
            var19 = var25;
            var20 = var29;
            var28 = var27;
            var23 = var26;
            var1 = var16;
            var3 = var11;
            break;
         case 142:
            this.cycles_left -= 7;
            var2 = this.adcA_8(var30.read8(var4 << 8 | var5), var2);
            var19 = var25;
            var20 = var29;
            var28 = var27;
            var23 = var26;
            var1 = var16;
            var3 = var11;
            break;
         case 143:
            this.cycles_left -= 4;
            var2 = this.adcA_8(var2, var2);
            var19 = var25;
            var20 = var29;
            var28 = var27;
            var23 = var26;
            var1 = var16;
            var3 = var11;
            break;
         case 144:
            this.cycles_left -= 4;
            var2 = this.subA_8(var6, var2);
            var19 = var25;
            var20 = var29;
            var28 = var27;
            var23 = var26;
            var1 = var16;
            var3 = var11;
            break;
         case 145:
            this.cycles_left -= 4;
            var2 = this.subA_8(var9, var2);
            var19 = var25;
            var20 = var29;
            var28 = var27;
            var23 = var26;
            var1 = var16;
            var3 = var11;
            break;
         case 146:
            this.cycles_left -= 4;
            var2 = this.subA_8(var7, var2);
            var19 = var25;
            var20 = var29;
            var28 = var27;
            var23 = var26;
            var1 = var16;
            var3 = var11;
            break;
         case 147:
            this.cycles_left -= 4;
            var2 = this.subA_8(var8, var2);
            var19 = var25;
            var20 = var29;
            var28 = var27;
            var23 = var26;
            var1 = var16;
            var3 = var11;
            break;
         case 148:
            this.cycles_left -= 4;
            var2 = this.subA_8(var4, var2);
            var19 = var25;
            var20 = var29;
            var28 = var27;
            var23 = var26;
            var1 = var16;
            var3 = var11;
            break;
         case 149:
            this.cycles_left -= 4;
            var2 = this.subA_8(var5, var2);
            var19 = var25;
            var20 = var29;
            var28 = var27;
            var23 = var26;
            var1 = var16;
            var3 = var11;
            break;
         case 150:
            this.cycles_left -= 7;
            var2 = this.subA_8(var30.read8(var4 << 8 | var5), var2);
            var19 = var25;
            var20 = var29;
            var28 = var27;
            var23 = var26;
            var1 = var16;
            var3 = var11;
            break;
         case 151:
            this.cycles_left -= 4;
            var2 = this.subA_8(var2, var2);
            var19 = var25;
            var20 = var29;
            var28 = var27;
            var23 = var26;
            var1 = var16;
            var3 = var11;
            break;
         case 152:
            this.cycles_left -= 4;
            var2 = this.sbcA_8(var6, var2);
            var19 = var25;
            var20 = var29;
            var28 = var27;
            var23 = var26;
            var1 = var16;
            var3 = var11;
            break;
         case 153:
            this.cycles_left -= 4;
            var2 = this.sbcA_8(var9, var2);
            var19 = var25;
            var20 = var29;
            var28 = var27;
            var23 = var26;
            var1 = var16;
            var3 = var11;
            break;
         case 154:
            this.cycles_left -= 4;
            var2 = this.sbcA_8(var7, var2);
            var19 = var25;
            var20 = var29;
            var28 = var27;
            var23 = var26;
            var1 = var16;
            var3 = var11;
            break;
         case 155:
            this.cycles_left -= 4;
            var2 = this.sbcA_8(var8, var2);
            var19 = var25;
            var20 = var29;
            var28 = var27;
            var23 = var26;
            var1 = var16;
            var3 = var11;
            break;
         case 156:
            this.cycles_left -= 4;
            var2 = this.sbcA_8(var4, var2);
            var19 = var25;
            var20 = var29;
            var28 = var27;
            var23 = var26;
            var1 = var16;
            var3 = var11;
            break;
         case 157:
            this.cycles_left -= 4;
            var2 = this.sbcA_8(var5, var2);
            var19 = var25;
            var20 = var29;
            var28 = var27;
            var23 = var26;
            var1 = var16;
            var3 = var11;
            break;
         case 158:
            this.cycles_left -= 7;
            var2 = this.sbcA_8(var30.read8(var4 << 8 | var5), var2);
            var19 = var25;
            var20 = var29;
            var28 = var27;
            var23 = var26;
            var1 = var16;
            var3 = var11;
            break;
         case 159:
            this.cycles_left -= 4;
            var2 = this.sbcA_8(var2, var2);
            var19 = var25;
            var20 = var29;
            var28 = var27;
            var23 = var26;
            var1 = var16;
            var3 = var11;
            break;
         case 160:
            this.cycles_left -= 4;
            var2 = this.andA(var6, var2);
            var19 = var25;
            var20 = var29;
            var28 = var27;
            var23 = var26;
            var1 = var16;
            var3 = var11;
            break;
         case 161:
            this.cycles_left -= 4;
            var2 = this.andA(var9, var2);
            var19 = var25;
            var20 = var29;
            var28 = var27;
            var23 = var26;
            var1 = var16;
            var3 = var11;
            break;
         case 162:
            this.cycles_left -= 4;
            var2 = this.andA(var7, var2);
            var19 = var25;
            var20 = var29;
            var28 = var27;
            var23 = var26;
            var1 = var16;
            var3 = var11;
            break;
         case 163:
            this.cycles_left -= 4;
            var2 = this.andA(var8, var2);
            var19 = var25;
            var20 = var29;
            var28 = var27;
            var23 = var26;
            var1 = var16;
            var3 = var11;
            break;
         case 164:
            this.cycles_left -= 4;
            var2 = this.andA(var4, var2);
            var19 = var25;
            var20 = var29;
            var28 = var27;
            var23 = var26;
            var1 = var16;
            var3 = var11;
            break;
         case 165:
            this.cycles_left -= 4;
            var2 = this.andA(var5, var2);
            var19 = var25;
            var20 = var29;
            var28 = var27;
            var23 = var26;
            var1 = var16;
            var3 = var11;
            break;
         case 166:
            this.cycles_left -= 7;
            var2 = this.andA(var30.read8(var4 << 8 | var5), var2);
            var19 = var25;
            var20 = var29;
            var28 = var27;
            var23 = var26;
            var1 = var16;
            var3 = var11;
            break;
         case 167:
            this.cycles_left -= 4;
            var2 = this.andA(var2, var2);
            var19 = var25;
            var20 = var29;
            var28 = var27;
            var23 = var26;
            var1 = var16;
            var3 = var11;
            break;
         case 168:
            this.cycles_left -= 4;
            var2 = this.xorA(var6, var2);
            var19 = var25;
            var20 = var29;
            var28 = var27;
            var23 = var26;
            var1 = var16;
            var3 = var11;
            break;
         case 169:
            this.cycles_left -= 4;
            var2 = this.xorA(var9, var2);
            var19 = var25;
            var20 = var29;
            var28 = var27;
            var23 = var26;
            var1 = var16;
            var3 = var11;
            break;
         case 170:
            this.cycles_left -= 4;
            var2 = this.xorA(var7, var2);
            var19 = var25;
            var20 = var29;
            var28 = var27;
            var23 = var26;
            var1 = var16;
            var3 = var11;
            break;
         case 171:
            this.cycles_left -= 4;
            var2 = this.xorA(var8, var2);
            var19 = var25;
            var20 = var29;
            var28 = var27;
            var23 = var26;
            var1 = var16;
            var3 = var11;
            break;
         case 172:
            this.cycles_left -= 4;
            var2 = this.xorA(var4, var2);
            var19 = var25;
            var20 = var29;
            var28 = var27;
            var23 = var26;
            var1 = var16;
            var3 = var11;
            break;
         case 173:
            this.cycles_left -= 4;
            var2 = this.xorA(var5, var2);
            var19 = var25;
            var20 = var29;
            var28 = var27;
            var23 = var26;
            var1 = var16;
            var3 = var11;
            break;
         case 174:
            this.cycles_left -= 7;
            var2 = this.xorA(var30.read8(var4 << 8 | var5), var2);
            var19 = var25;
            var20 = var29;
            var28 = var27;
            var23 = var26;
            var1 = var16;
            var3 = var11;
            break;
         case 175:
            this.cycles_left -= 4;
            var2 = this.xorA(var2, var2);
            var19 = var25;
            var20 = var29;
            var28 = var27;
            var23 = var26;
            var1 = var16;
            var3 = var11;
            break;
         case 176:
            this.cycles_left -= 4;
            var2 = this.orA(var6, var2);
            var19 = var25;
            var20 = var29;
            var28 = var27;
            var23 = var26;
            var1 = var16;
            var3 = var11;
            break;
         case 177:
            this.cycles_left -= 4;
            var2 = this.orA(var9, var2);
            var19 = var25;
            var20 = var29;
            var28 = var27;
            var23 = var26;
            var1 = var16;
            var3 = var11;
            break;
         case 178:
            this.cycles_left -= 4;
            var2 = this.orA(var7, var2);
            var19 = var25;
            var20 = var29;
            var28 = var27;
            var23 = var26;
            var1 = var16;
            var3 = var11;
            break;
         case 179:
            this.cycles_left -= 4;
            var2 = this.orA(var8, var2);
            var19 = var25;
            var20 = var29;
            var28 = var27;
            var23 = var26;
            var1 = var16;
            var3 = var11;
            break;
         case 180:
            this.cycles_left -= 4;
            var2 = this.orA(var4, var2);
            var19 = var25;
            var20 = var29;
            var28 = var27;
            var23 = var26;
            var1 = var16;
            var3 = var11;
            break;
         case 181:
            this.cycles_left -= 4;
            var2 = this.orA(var5, var2);
            var19 = var25;
            var20 = var29;
            var28 = var27;
            var23 = var26;
            var1 = var16;
            var3 = var11;
            break;
         case 182:
            this.cycles_left -= 7;
            var2 = this.orA(var30.read8(var4 << 8 | var5), var2);
            var19 = var25;
            var20 = var29;
            var28 = var27;
            var23 = var26;
            var1 = var16;
            var3 = var11;
            break;
         case 183:
            this.cycles_left -= 4;
            var2 = this.orA(var2, var2);
            var19 = var25;
            var20 = var29;
            var28 = var27;
            var23 = var26;
            var1 = var16;
            var3 = var11;
            break;
         case 184:
            this.cycles_left -= 4;
            this.cpA_8(var6, var2);
            var19 = var25;
            var20 = var29;
            var28 = var27;
            var23 = var26;
            var1 = var16;
            var3 = var11;
            break;
         case 185:
            this.cycles_left -= 4;
            this.cpA_8(var9, var2);
            var19 = var25;
            var20 = var29;
            var28 = var27;
            var23 = var26;
            var1 = var16;
            var3 = var11;
            break;
         case 186:
            this.cycles_left -= 4;
            this.cpA_8(var7, var2);
            var19 = var25;
            var20 = var29;
            var28 = var27;
            var23 = var26;
            var1 = var16;
            var3 = var11;
            break;
         case 187:
            this.cycles_left -= 4;
            this.cpA_8(var8, var2);
            var19 = var25;
            var20 = var29;
            var28 = var27;
            var23 = var26;
            var1 = var16;
            var3 = var11;
            break;
         case 188:
            this.cycles_left -= 4;
            this.cpA_8(var4, var2);
            var19 = var25;
            var20 = var29;
            var28 = var27;
            var23 = var26;
            var1 = var16;
            var3 = var11;
            break;
         case 189:
            this.cycles_left -= 4;
            this.cpA_8(var5, var2);
            var19 = var25;
            var20 = var29;
            var28 = var27;
            var23 = var26;
            var1 = var16;
            var3 = var11;
            break;
         case 190:
            this.cycles_left -= 7;
            this.cpA_8(var30.read8(var4 << 8 | var5), var2);
            var19 = var25;
            var20 = var29;
            var28 = var27;
            var23 = var26;
            var1 = var16;
            var3 = var11;
            break;
         case 191:
            this.cycles_left -= 4;
            this.cpA_8(var2, var2);
            var19 = var25;
            var20 = var29;
            var28 = var27;
            var23 = var26;
            var1 = var16;
            var3 = var11;
            break;
         case 192:
            if((this.F & 64) == 0) {
               this.cycles_left -= 11;
               var1 = var30.read16arg(var11);
               var3 = var11 + 2;
               var19 = var25;
               var20 = var29;
               var28 = var27;
               var23 = var26;
            } else {
               this.cycles_left -= 5;
               var19 = var25;
               var20 = var29;
               var28 = var27;
               var23 = var26;
               var1 = var16;
               var3 = var11;
            }
            break;
         case 193:
            this.cycles_left -= 10;
            var6 = var30.read8(var11 + 1);
            var9 = var30.read8(var11);
            var3 = var11 + 2;
            var19 = var25;
            var20 = var29;
            var28 = var27;
            var23 = var26;
            var1 = var16;
            break;
         case 194:
            this.cycles_left -= 10;
            if((this.F & 64) == 0) {
               var1 = var30.read16arg(var16);
               var19 = var25;
               var20 = var29;
               var28 = var27;
               var23 = var26;
               var3 = var11;
            } else {
               var1 = var16 + 2;
               var19 = var25;
               var20 = var29;
               var28 = var27;
               var23 = var26;
               var3 = var11;
            }
            break;
         case 195:
            this.cycles_left -= 10;
            var1 = var30.read16arg(var16);
            var19 = var25;
            var20 = var29;
            var28 = var27;
            var23 = var26;
            var3 = var11;
            break;
         case 196:
            if((this.F & 64) == 0) {
               var3 = var11 - 2;
               var30.write16fast(var3, var16 + 2);
               this.cycles_left -= 17;
               var1 = var30.read16arg(var16);
               var19 = var25;
               var20 = var29;
               var28 = var27;
               var23 = var26;
            } else {
               var1 = var16 + 2;
               this.cycles_left -= 10;
               var19 = var25;
               var20 = var29;
               var28 = var27;
               var23 = var26;
               var3 = var11;
            }
            break;
         case 197:
            this.cycles_left -= 11;
            var30.write16(var11 - 2, var6 << 8 | var9);
            var3 = var11 - 2;
            var19 = var25;
            var20 = var29;
            var28 = var27;
            var23 = var26;
            var1 = var16;
            break;
         case 198:
            this.cycles_left -= 7;
            var2 = this.addA_8(var30.read8(var16), var2);
            var1 = var16 + 1;
            var19 = var25;
            var20 = var29;
            var28 = var27;
            var23 = var26;
            var3 = var11;
            break;
         case 199:
            this.cycles_left -= 11;
            var3 = var11 - 2;
            var30.write16fast(var3, var16);
            var1 = 0;
            var19 = var25;
            var20 = var29;
            var28 = var27;
            var23 = var26;
            break;
         case 200:
            if((this.F & 64) != 0) {
               this.cycles_left -= 11;
               var1 = var30.read16arg(var11);
               var3 = var11 + 2;
               var19 = var25;
               var20 = var29;
               var28 = var27;
               var23 = var26;
            } else {
               this.cycles_left -= 5;
               var19 = var25;
               var20 = var29;
               var28 = var27;
               var23 = var26;
               var1 = var16;
               var3 = var11;
            }
            break;
         case 201:
            this.cycles_left -= 10;
            var1 = var30.read16arg(var11);
            var3 = var11 + 2;
            var19 = var25;
            var20 = var29;
            var28 = var27;
            var23 = var26;
            break;
         case 202:
            this.cycles_left -= 10;
            if((this.F & 64) != 0) {
               var1 = var30.read16arg(var16);
               var19 = var25;
               var20 = var29;
               var28 = var27;
               var23 = var26;
               var3 = var11;
            } else {
               var1 = var16 + 2;
               var19 = var25;
               var20 = var29;
               var28 = var27;
               var23 = var26;
               var3 = var11;
            }
            break;
         case 203:
         case 221:
         case 237:
         case 253:
         default:
            var14 = var12;
            var19 = var25;
            var20 = var29;
            var28 = var27;
            var23 = var26;
            var1 = var16;
            var3 = var11;
            break;
         case 204:
            if((this.F & 64) != 0) {
               var3 = var11 - 2;
               var30.write16fast(var3, var16 + 2);
               this.cycles_left -= 17;
               var1 = var30.read16arg(var16);
               var19 = var25;
               var20 = var29;
               var28 = var27;
               var23 = var26;
            } else {
               this.cycles_left -= 10;
               var1 = var16 + 2;
               var19 = var25;
               var20 = var29;
               var28 = var27;
               var23 = var26;
               var3 = var11;
            }
            break;
         case 205:
            this.cycles_left -= 17;
            var3 = var11 - 2;
            var30.write8fast(var3 + 1, var16 + 2 >> 8);
            var30.write8fast(var3, var16 + 2 & 255);
            var1 = var30.read16arg(var16);
            var19 = var25;
            var20 = var29;
            var28 = var27;
            var23 = var26;
            break;
         case 206:
            this.cycles_left -= 7;
            var2 = this.adcA_8(var30.read8arg(var16), var2);
            var1 = var16 + 1;
            var19 = var25;
            var20 = var29;
            var28 = var27;
            var23 = var26;
            var3 = var11;
            break;
         case 207:
            this.cycles_left -= 11;
            var3 = var11 - 2;
            var30.write16fast(var3, var16);
            var1 = 8;
            var19 = var25;
            var20 = var29;
            var28 = var27;
            var23 = var26;
            break;
         case 208:
            if((this.F & 1) == 0) {
               this.cycles_left -= 11;
               var1 = var30.read16arg(var11);
               var3 = var11 + 2;
               var19 = var25;
               var20 = var29;
               var28 = var27;
               var23 = var26;
            } else {
               this.cycles_left -= 5;
               var19 = var25;
               var20 = var29;
               var28 = var27;
               var23 = var26;
               var1 = var16;
               var3 = var11;
            }
            break;
         case 209:
            this.cycles_left -= 10;
            var7 = var30.read8(var11 + 1);
            var8 = var30.read8(var11);
            var3 = var11 + 2;
            var19 = var25;
            var20 = var29;
            var28 = var27;
            var23 = var26;
            var1 = var16;
            break;
         case 210:
            this.cycles_left -= 10;
            if((this.F & 1) == 0) {
               var1 = var30.read16arg(var16);
               var19 = var25;
               var20 = var29;
               var28 = var27;
               var23 = var26;
               var3 = var11;
            } else {
               var1 = var16 + 2;
               var19 = var25;
               var20 = var29;
               var28 = var27;
               var23 = var26;
               var3 = var11;
            }
            break;
         case 211:
            this.cycles_left -= 11;
            this.out(var30.read8arg(var16), var2);
            var1 = var16 + 1;
            var19 = var25;
            var20 = var29;
            var28 = var27;
            var23 = var26;
            var3 = var11;
            break;
         case 212:
            if((this.F & 1) == 0) {
               var3 = var11 - 2;
               var30.write16fast(var3, var16 + 2);
               this.cycles_left -= 17;
               var1 = var30.read16arg(var16);
               var19 = var25;
               var20 = var29;
               var28 = var27;
               var23 = var26;
            } else {
               this.cycles_left -= 10;
               var1 = var16 + 2;
               var19 = var25;
               var20 = var29;
               var28 = var27;
               var23 = var26;
               var3 = var11;
            }
            break;
         case 213:
            this.cycles_left -= 11;
            var30.write16(var11 - 2, var7 << 8 | var8);
            var3 = var11 - 2;
            var19 = var25;
            var20 = var29;
            var28 = var27;
            var23 = var26;
            var1 = var16;
            break;
         case 214:
            this.cycles_left -= 7;
            var2 = this.subA_8(var30.read8arg(var16), var2);
            var1 = var16 + 1;
            var19 = var25;
            var20 = var29;
            var28 = var27;
            var23 = var26;
            var3 = var11;
            break;
         case 215:
            this.cycles_left -= 11;
            var3 = var11 - 2;
            var30.write16fast(var3, var16);
            var1 = 16;
            var19 = var25;
            var20 = var29;
            var28 = var27;
            var23 = var26;
            break;
         case 216:
            if((this.F & 1) != 0) {
               this.cycles_left -= 11;
               var1 = var30.read16arg(var11);
               var3 = var11 + 2;
               var19 = var25;
               var20 = var29;
               var28 = var27;
               var23 = var26;
            } else {
               this.cycles_left -= 5;
               var19 = var25;
               var20 = var29;
               var28 = var27;
               var23 = var26;
               var1 = var16;
               var3 = var11;
            }
            break;
         case 217:
            this.cycles_left -= 4;
            int var18 = this.B1;
            this.B1 = var6;
            int var17 = this.C1;
            this.C1 = var9;
            var3 = this.D1;
            this.D1 = var7;
            var1 = this.E1;
            this.E1 = var8;
            var15 = this.H1;
            this.H1 = var4;
            var10 = this.L1;
            this.L1 = var5;
            var6 = var18;
            var9 = var17;
            var7 = var3;
            var8 = var1;
            var4 = var15;
            var19 = var25;
            var20 = var29;
            var28 = var27;
            var5 = var10;
            var23 = var26;
            var1 = var16;
            var3 = var11;
            break;
         case 218:
            this.cycles_left -= 10;
            if((this.F & 1) != 0) {
               var1 = var30.read16arg(var16);
               var19 = var25;
               var20 = var29;
               var28 = var27;
               var23 = var26;
               var3 = var11;
            } else {
               var1 = var16 + 2;
               var19 = var25;
               var20 = var29;
               var28 = var27;
               var23 = var26;
               var3 = var11;
            }
            break;
         case 219:
            this.cycles_left -= 11;
            var2 = this.in(var30.read8arg(var16), var2);
            var1 = var16 + 1;
            var19 = var25;
            var20 = var29;
            var28 = var27;
            var23 = var26;
            var3 = var11;
            break;
         case 220:
            if((this.F & 1) != 0) {
               var3 = var11 - 2;
               var30.write16fast(var3, var16 + 2);
               this.cycles_left -= 17;
               var1 = var30.read16arg(var16);
               var19 = var25;
               var20 = var29;
               var28 = var27;
               var23 = var26;
            } else {
               this.cycles_left -= 10;
               var1 = var16 + 2;
               var19 = var25;
               var20 = var29;
               var28 = var27;
               var23 = var26;
               var3 = var11;
            }
            break;
         case 222:
            this.cycles_left -= 7;
            var2 = this.sbcA_8(var30.read8arg(var16), var2);
            var1 = var16 + 1;
            var19 = var25;
            var20 = var29;
            var28 = var27;
            var23 = var26;
            var3 = var11;
            break;
         case 223:
            this.cycles_left -= 11;
            var3 = var11 - 2;
            var30.write16fast(var3, var16);
            var1 = 24;
            var19 = var25;
            var20 = var29;
            var28 = var27;
            var23 = var26;
            break;
         case 224:
            if((this.F & 4) == 0) {
               this.cycles_left -= 11;
               var1 = var30.read16arg(var11);
               var3 = var11 + 2;
               var19 = var25;
               var20 = var29;
               var28 = var27;
               var23 = var26;
            } else {
               this.cycles_left -= 5;
               var19 = var25;
               var20 = var29;
               var28 = var27;
               var23 = var26;
               var1 = var16;
               var3 = var11;
            }
            break;
         case 225:
            this.cycles_left -= 10;
            var4 = var30.read8(var11 + 1);
            var5 = var30.read8(var11);
            var3 = var11 + 2;
            var19 = var25;
            var20 = var29;
            var28 = var27;
            var23 = var26;
            var1 = var16;
            break;
         case 226:
            this.cycles_left -= 10;
            if((this.F & 4) == 0) {
               var1 = var30.read16arg(var16);
               var19 = var25;
               var20 = var29;
               var28 = var27;
               var23 = var26;
               var3 = var11;
            } else {
               var1 = var16 + 2;
               var19 = var25;
               var20 = var29;
               var28 = var27;
               var23 = var26;
               var3 = var11;
            }
            break;
         case 227:
            this.cycles_left -= 19;
            var1 = var30.read8(var11 + 1);
            var30.write8(var11 + 1, var4);
            var4 = var1;
            var1 = var30.read8(var11);
            var30.write8(var11, var5);
            var5 = var1;
            var19 = var25;
            var20 = var29;
            var28 = var27;
            var23 = var26;
            var1 = var16;
            var3 = var11;
            break;
         case 228:
            this.cycles_left -= 10;
            if((this.F & 4) == 0) {
               var3 = var11 - 2;
               var30.write16fast(var3, var16 + 2);
               this.cycles_left -= 17;
               var1 = var30.read16arg(var16);
               var19 = var25;
               var20 = var29;
               var28 = var27;
               var23 = var26;
            } else {
               var1 = var16 + 2;
               var19 = var25;
               var20 = var29;
               var28 = var27;
               var23 = var26;
               var3 = var11;
            }
            break;
         case 229:
            this.cycles_left -= 11;
            var30.write16(var11 - 2, var4 << 8 | var5);
            var3 = var11 - 2;
            var19 = var25;
            var20 = var29;
            var28 = var27;
            var23 = var26;
            var1 = var16;
            break;
         case 230:
            this.cycles_left -= 7;
            var2 = this.andA(var30.read8arg(var16), var2);
            var1 = var16 + 1;
            var19 = var25;
            var20 = var29;
            var28 = var27;
            var23 = var26;
            var3 = var11;
            break;
         case 231:
            this.cycles_left -= 11;
            var3 = var11 - 2;
            var30.write16fast(var3, var16);
            var1 = 32;
            var19 = var25;
            var20 = var29;
            var28 = var27;
            var23 = var26;
            break;
         case 232:
            if((this.F & 4) != 0) {
               this.cycles_left -= 11;
               var1 = var30.read16arg(var11);
               var3 = var11 + 2;
               var19 = var25;
               var20 = var29;
               var28 = var27;
               var23 = var26;
            } else {
               this.cycles_left -= 5;
               var19 = var25;
               var20 = var29;
               var28 = var27;
               var23 = var26;
               var1 = var16;
               var3 = var11;
            }
            break;
         case 233:
            this.cycles_left -= 4;
            var1 = var4 << 8 | var5;
            var19 = var25;
            var20 = var29;
            var28 = var27;
            var23 = var26;
            var3 = var11;
            break;
         case 234:
            this.cycles_left -= 10;
            if((this.F & 4) != 0) {
               var1 = var30.read16arg(var16);
               var19 = var25;
               var20 = var29;
               var28 = var27;
               var23 = var26;
               var3 = var11;
            } else {
               var1 = var16 + 2;
               var19 = var25;
               var20 = var29;
               var28 = var27;
               var23 = var26;
               var3 = var11;
            }
            break;
         case 235:
            this.cycles_left -= 4;
            var1 = var4;
            var4 = var7;
            var3 = var5;
            var5 = var8;
            var7 = var1;
            var8 = var3;
            var19 = var25;
            var20 = var29;
            var28 = var27;
            var23 = var26;
            var1 = var16;
            var3 = var11;
            break;
         case 236:
            this.cycles_left -= 10;
            if((this.F & 4) != 0) {
               var3 = var11 - 2;
               var30.write16fast(var3, var16 + 2);
               this.cycles_left -= 17;
               var1 = var30.read16arg(var16);
               var19 = var25;
               var20 = var29;
               var28 = var27;
               var23 = var26;
            } else {
               var1 = var16 + 2;
               var19 = var25;
               var20 = var29;
               var28 = var27;
               var23 = var26;
               var3 = var11;
            }
            break;
         case 238:
            this.cycles_left -= 7;
            var2 = this.xorA(var30.read8arg(var16), var2);
            var1 = var16 + 1;
            var19 = var25;
            var20 = var29;
            var28 = var27;
            var23 = var26;
            var3 = var11;
            break;
         case 239:
            this.cycles_left -= 11;
            var3 = var11 - 2;
            var30.write16fast(var3, var16);
            var1 = 40;
            var19 = var25;
            var20 = var29;
            var28 = var27;
            var23 = var26;
            break;
         case 240:
            if((this.F & 128) == 0) {
               this.cycles_left -= 11;
               var1 = var30.read16arg(var11);
               var3 = var11 + 2;
               var19 = var25;
               var20 = var29;
               var28 = var27;
               var23 = var26;
            } else {
               this.cycles_left -= 5;
               var19 = var25;
               var20 = var29;
               var28 = var27;
               var23 = var26;
               var1 = var16;
               var3 = var11;
            }
            break;
         case 241:
            this.cycles_left -= 10;
            var2 = var30.read8(var11 + 1);
            this.F = var30.read8(var11);
            var3 = var11 + 2;
            var19 = var25;
            var20 = var29;
            var28 = var27;
            var23 = var26;
            var1 = var16;
            break;
         case 242:
            this.cycles_left -= 10;
            if((this.F & 128) == 0) {
               var1 = var30.read16arg(var16);
               var19 = var25;
               var20 = var29;
               var28 = var27;
               var23 = var26;
               var3 = var11;
            } else {
               var1 = var16 + 2;
               var19 = var25;
               var20 = var29;
               var28 = var27;
               var23 = var26;
               var3 = var11;
            }
            break;
         case 243:
            this.cycles_left -= 4;
            var20 = false;
            var19 = false;
            var28 = var27;
            var23 = var26;
            var1 = var16;
            var3 = var11;
            break;
         case 244:
            this.cycles_left -= 10;
            if((this.F & 128) == 0) {
               var3 = var11 - 2;
               var30.write16fast(var3, var16 + 2);
               this.cycles_left -= 17;
               var1 = var30.read16arg(var16);
               var19 = var25;
               var20 = var29;
               var28 = var27;
               var23 = var26;
            } else {
               var1 = var16 + 2;
               var19 = var25;
               var20 = var29;
               var28 = var27;
               var23 = var26;
               var3 = var11;
            }
            break;
         case 245:
            this.cycles_left -= 11;
            var30.write16(var11 - 2, var2 << 8 | this.F);
            var3 = var11 - 2;
            var19 = var25;
            var20 = var29;
            var28 = var27;
            var23 = var26;
            var1 = var16;
            break;
         case 246:
            this.cycles_left -= 7;
            var2 = this.orA(var30.read8arg(var16), var2);
            var1 = var16 + 1;
            var19 = var25;
            var20 = var29;
            var28 = var27;
            var23 = var26;
            var3 = var11;
            break;
         case 247:
            this.cycles_left -= 11;
            var3 = var11 - 2;
            var30.write16fast(var3, var16);
            var1 = 48;
            var19 = var25;
            var20 = var29;
            var28 = var27;
            var23 = var26;
            break;
         case 248:
            if((this.F & 128) != 0) {
               this.cycles_left -= 11;
               var1 = var30.read16arg(var11);
               var3 = var11 + 2;
               var19 = var25;
               var20 = var29;
               var28 = var27;
               var23 = var26;
            } else {
               this.cycles_left -= 5;
               var19 = var25;
               var20 = var29;
               var28 = var27;
               var23 = var26;
               var1 = var16;
               var3 = var11;
            }
            break;
         case 249:
            this.cycles_left -= 6;
            var3 = var4 << 8 | var5;
            var19 = var25;
            var20 = var29;
            var28 = var27;
            var23 = var26;
            var1 = var16;
            break;
         case 250:
            this.cycles_left -= 10;
            if((this.F & 128) != 0) {
               var1 = var30.read16arg(var16);
               var19 = var25;
               var20 = var29;
               var28 = var27;
               var23 = var26;
               var3 = var11;
            } else {
               var1 = var16 + 2;
               var19 = var25;
               var20 = var29;
               var28 = var27;
               var23 = var26;
               var3 = var11;
            }
            break;
         case 251:
            this.cycles_left -= 4;
            var20 = true;
            var19 = true;
            this.goingToirq = true;
            var28 = var27;
            var23 = var26;
            var1 = var16;
            var3 = var11;
            break;
         case 252:
            this.cycles_left -= 10;
            if((this.F & 128) != 0) {
               var3 = var11 - 2;
               var30.write16fast(var3, var16 + 2);
               this.cycles_left -= 17;
               var1 = var30.read16arg(var16);
               var19 = var25;
               var20 = var29;
               var28 = var27;
               var23 = var26;
            } else {
               var1 = var16 + 2;
               var19 = var25;
               var20 = var29;
               var28 = var27;
               var23 = var26;
               var3 = var11;
            }
            break;
         case 254:
            this.cycles_left -= 7;
            this.cpA_8(var30.read8arg(var16), var2);
            var1 = var16 + 1;
            var19 = var25;
            var20 = var29;
            var28 = var27;
            var23 = var26;
            var3 = var11;
            break;
         case 255:
            this.cycles_left -= 11;
            var3 = var11 - 2;
            var30.write16fast(var3, var16);
            var1 = 56;
            var19 = var25;
            var20 = var29;
            var28 = var27;
            var23 = var26;
         }
      }

      this.CurInstr = var12;
      this.IRQ = var28;
      this.NMI = var23;
      this.IFF0 = var19;
      this.IFF1 = var20;
      this.PC = var1;
      this.PPC = var13;
      this.SP = var3;
      this.B = var6;
      this.C = var9;
      this.D = var7;
      this.E = var8;
      this.H = var4;
      this.L = var5;
      this.A = var2;
   }

   public int getCyclesLeft() {
      return this.cycles_left;
   }

   public final int getDebug() {
      return this.debugLevel;
   }

   public final long getInstruction() {
      return (long)this.CurInstr;
   }

   public String getTag() {
      return this.tag;
   }

   public boolean init(CpuBoard var1, int var2) {
      this.ram = var1;
      return true;
   }

   public final void interrupt(int var1, boolean var2) {
      if(var1 == 1) {
         this.NMI = true;
      } else {
         this.IRQ = true;
      }

   }

   public final void reset() {
      this.SP = 65536;
      this.PC = 0;
      this.A = 0;
      this.F = 0;
      this.B = 0;
      this.C = 0;
      this.D = 0;
      this.E = 0;
      this.H = 0;
      this.L = 0;
      this.I = 0;
      this.R = 0;
      this.cycles_left = 0;
      this.A1 = 0;
      this.F1 = 0;
      this.B1 = 0;
      this.C1 = 0;
      this.D1 = 0;
      this.E1 = 0;
      this.H1 = 0;
      this.L1 = 0;
   }

   public final void setDebug(int var1) {
      if(!this.debugDisabled) {
         this.debugLevel = var1;
      }

   }

   public final void setProperty(int var1, int var2) {
   }

   public void setTag(String var1) {
      this.tag = var1;
   }
}
