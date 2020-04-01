package jef.cpu;

import jef.cpuboard.CpuBoard;
import jef.map.WriteHandler;

public final class Z80 implements Cpu, Z80debug, WriteHandler {
   private static final int CF = 1;
   public static boolean DEBUG = false;
   private static final int HF = 16;
   private static final int NF = 2;
   private static final int PF = 4;
   private static final int SF = 128;
   private static final int[] SZ = new int[256];
   private static final int[] SZHVC_Add = new int[131072];
   private static final int[] SZHVC_sub = new int[131072];
   private static final int[] SZHV_dec = new int[256];
   private static final int[] SZHV_inc = new int[256];
   private static final int[] SZP = new int[256];
   private static final int[] SZ_BIT = new int[256];
   private static final int VF = 4;
   private static final int XF = 8;
   private static final int YF = 32;
   private static final int ZF = 64;
   private static final int[] bitRes = new int[]{254, 253, 251, 247, 239, 223, 191, 127};
   private static final int[] bitSet = new int[]{1, 2, 4, 8, 16, 32, 64, 128};
   static int[] breg_tmp2;
   public static int debugBreakPoint = 0;
   public static boolean debugEnabled = false;
   static int[][] drep_tmp1;
   static int[][] irep_tmp1;
   private static final boolean[] parity = new boolean[256];
   public int A;
   public int A1;
   public int B;
   public int B1;
   public int C;
   public int C1;
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
   public int IX;
   public int IXYd = 0;
   public int IY;
   public int I_Vector = 0;
   public int L;
   public int L1;
   public boolean NMI = false;
   public int PC;
   private int PPC = 0;
   public int R;
   public int R2;
   public int SP = 65536;
   public int cycle = 0;
   int debugLevel = 0;
   private boolean goingToirq = false;
   private int instruction = 0;
   private CpuBoard ram;
   boolean startSlice = true;
   public boolean state_HALT = false;
   private String tag;
   private int tmp;
   private int tmp1;
   private int tmp2;
   private int tmp3;

   static {
      int[] var9 = new int[4];
      var9[2] = 1;
      int[] var11 = new int[]{0, 1, 0, 1};
      int[] var12 = new int[]{1, 0, 1, 1};
      int[] var10 = new int[]{0, 1, 1, 0};
      irep_tmp1 = new int[][]{var9, var11, var12, var10};
      var11 = new int[4];
      var11[1] = 1;
      var10 = new int[]{1, 0, 0, 1};
      var9 = new int[4];
      var9[2] = 1;
      var12 = new int[]{0, 1, 0, 1};
      drep_tmp1 = new int[][]{var11, var10, var9, var12};
      var9 = new int[256];
      var9[2] = 1;
      var9[3] = 1;
      var9[5] = 1;
      var9[8] = 1;
      var9[9] = 1;
      var9[12] = 1;
      var9[14] = 1;
      var9[15] = 1;
      var9[17] = 1;
      var9[20] = 1;
      var9[22] = 1;
      var9[23] = 1;
      var9[26] = 1;
      var9[27] = 1;
      var9[29] = 1;
      var9[32] = 1;
      var9[33] = 1;
      var9[36] = 1;
      var9[38] = 1;
      var9[39] = 1;
      var9[42] = 1;
      var9[43] = 1;
      var9[45] = 1;
      var9[48] = 1;
      var9[50] = 1;
      var9[51] = 1;
      var9[53] = 1;
      var9[56] = 1;
      var9[57] = 1;
      var9[60] = 1;
      var9[62] = 1;
      var9[63] = 1;
      var9[65] = 1;
      var9[68] = 1;
      var9[70] = 1;
      var9[71] = 1;
      var9[74] = 1;
      var9[75] = 1;
      var9[77] = 1;
      var9[80] = 1;
      var9[82] = 1;
      var9[83] = 1;
      var9[85] = 1;
      var9[88] = 1;
      var9[89] = 1;
      var9[92] = 1;
      var9[94] = 1;
      var9[95] = 1;
      var9[98] = 1;
      var9[99] = 1;
      var9[101] = 1;
      var9[104] = 1;
      var9[105] = 1;
      var9[108] = 1;
      var9[110] = 1;
      var9[111] = 1;
      var9[113] = 1;
      var9[116] = 1;
      var9[118] = 1;
      var9[119] = 1;
      var9[122] = 1;
      var9[123] = 1;
      var9[125] = 1;
      var9[128] = 1;
      var9[129] = 1;
      var9[132] = 1;
      var9[134] = 1;
      var9[135] = 1;
      var9[138] = 1;
      var9[139] = 1;
      var9[141] = 1;
      var9[144] = 1;
      var9[146] = 1;
      var9[147] = 1;
      var9[149] = 1;
      var9[152] = 1;
      var9[153] = 1;
      var9[156] = 1;
      var9[158] = 1;
      var9[159] = 1;
      var9[162] = 1;
      var9[163] = 1;
      var9[165] = 1;
      var9[168] = 1;
      var9[169] = 1;
      var9[172] = 1;
      var9[174] = 1;
      var9[175] = 1;
      var9[177] = 1;
      var9[180] = 1;
      var9[182] = 1;
      var9[183] = 1;
      var9[186] = 1;
      var9[187] = 1;
      var9[189] = 1;
      var9[192] = 1;
      var9[194] = 1;
      var9[195] = 1;
      var9[197] = 1;
      var9[200] = 1;
      var9[201] = 1;
      var9[204] = 1;
      var9[206] = 1;
      var9[207] = 1;
      var9[209] = 1;
      var9[212] = 1;
      var9[214] = 1;
      var9[215] = 1;
      var9[218] = 1;
      var9[219] = 1;
      var9[221] = 1;
      var9[224] = 1;
      var9[225] = 1;
      var9[228] = 1;
      var9[230] = 1;
      var9[231] = 1;
      var9[234] = 1;
      var9[235] = 1;
      var9[237] = 1;
      var9[240] = 1;
      var9[242] = 1;
      var9[243] = 1;
      var9[245] = 1;
      var9[248] = 1;
      var9[249] = 1;
      var9[252] = 1;
      var9[254] = 1;
      var9[255] = 1;
      breg_tmp2 = var9;

      int var0;
      int var1;
      int var2;
      int var3;
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

         var2 = var0;
         if((var3 & 4) != 0) {
            var2 = var0 + 1;
         }

         var1 = var2;
         if((var3 & 8) != 0) {
            var1 = var2 + 1;
         }

         var0 = var1;
         if((var3 & 16) != 0) {
            var0 = var1 + 1;
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
         var9 = SZ_BIT;
         if(var3 != 0) {
            var1 = var3 & 128;
         } else {
            var1 = 68;
         }

         var9[var3] = var1;
         var9 = SZ_BIT;
         var9[var3] |= var3 & 40;
         var9 = SZP;
         var1 = SZ[var3];
         byte var13;
         if((var0 & 1) != 0) {
            var13 = 0;
         } else {
            var13 = 4;
         }

         var9[var3] = var1 | var13;
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

      var2 = 0;
      var1 = 65536;
      int var4 = 0;
      var3 = 65536;

      for(var0 = 0; var0 < 256; ++var0) {
         for(int var5 = 0; var5 < 256; ++var5) {
            int var6 = var5 - var0;
            if(var5 != 0) {
               if((var5 & 128) != 0) {
                  SZHVC_Add[var4] = 128;
               } else {
                  SZHVC_Add[var4] = 0;
               }
            } else {
               SZHVC_Add[var4] = 64;
            }

            var9 = SZHVC_Add;
            var9[var4] |= var5 & 40;
            if((var5 & 15) < (var0 & 15)) {
               var9 = SZHVC_Add;
               var9[var4] |= 16;
            }

            if(var5 < var0) {
               var9 = SZHVC_Add;
               var9[var4] |= 1;
            }

            if(((var6 ^ var5) & (var6 ^ var0 ^ 128) & 128) != 0) {
               var9 = SZHVC_Add;
               var9[var4] |= 4;
            }

            ++var4;
            var6 = var5 - var0 - 1;
            if(var5 != 0) {
               if((var5 & 128) != 0) {
                  SZHVC_Add[var3] = 128;
               } else {
                  SZHVC_Add[var3] = 0;
               }
            } else {
               SZHVC_Add[var3] = 64;
            }

            var9 = SZHVC_Add;
            var9[var3] |= var5 & 40;
            if((var5 & 15) <= (var0 & 15)) {
               var9 = SZHVC_Add;
               var9[var3] |= 16;
            }

            if(var5 <= var0) {
               var9 = SZHVC_Add;
               var9[var3] |= 1;
            }

            if(((var6 ^ var5) & (var6 ^ var0 ^ 128) & 128) != 0) {
               var9 = SZHVC_Add;
               var9[var3] |= 4;
            }

            ++var3;
            if(var5 != 0) {
               if((var5 & 128) != 0) {
                  SZHVC_sub[var2] = 130;
               } else {
                  SZHVC_sub[var2] = 2;
               }
            } else {
               SZHVC_sub[var2] = 66;
            }

            var9 = SZHVC_sub;
            var9[var2] |= var5 & 40;
            if((var5 & 15) > (var0 & 15)) {
               var9 = SZHVC_sub;
               var9[var2] |= 16;
            }

            if(var5 > var0) {
               var9 = SZHVC_sub;
               var9[var2] |= 1;
            }

            if(((var0 - var5 ^ var0) & (var0 ^ var5) & 128) != 0) {
               var9 = SZHVC_sub;
               var9[var2] |= 4;
            }

            ++var2;
            if(var5 != 0) {
               if((var5 & 128) != 0) {
                  SZHVC_sub[var1] = 130;
               } else {
                  SZHVC_sub[var1] = 2;
               }
            } else {
               SZHVC_sub[var1] = 66;
            }

            var9 = SZHVC_sub;
            var9[var1] |= var5 & 40;
            if((var5 & 15) >= (var0 & 15)) {
               var9 = SZHVC_sub;
               var9[var1] |= 16;
            }

            if(var5 >= var0) {
               var9 = SZHVC_sub;
               var9[var1] |= 1;
            }

            if(((var0 - var5 - 1 ^ var0) & (var0 ^ var5) & 128) != 0) {
               var9 = SZHVC_sub;
               var9[var1] |= 4;
            }

            ++var1;
         }
      }

   }

   public Z80() {
      this.debugLevel = 0;
   }

   private final int AF() {
      return this.A << 8 | this.F;
   }

   private final int BC() {
      return this.B << 8 | this.C;
   }

   private final void BC(int var1) {
      this.B = var1 >> 8;
      this.C = var1 & 255;
   }

   private final int DE() {
      return this.D << 8 | this.E;
   }

   private final void DE(int var1) {
      this.D = var1 >> 8;
      this.E = var1 & 255;
   }

   private final int HL() {
      return this.H << 8 | this.L;
   }

   private final void HL(int var1) {
      this.H = var1 >> 8;
      this.L = var1 & 255;
   }

   private final int HLi() {
      return this.ram.read8(this.HL());
   }

   private int R() {
      return this.R & 255;
   }

   private final int adcA_8(int var1, int var2) {
      int var3 = this.F & 1;
      var1 = var2 + var1 + var3 & 255;
      this.F = SZHVC_Add[var3 << 16 | var2 << 8 | var1];
      return var1;
   }

   private final void adcHL(int var1) {
      int var4 = this.HL();
      int var3 = var4 + var1 + (this.F & 1);
      byte var2;
      if(('\uffff' & var3) != 0) {
         var2 = 0;
      } else {
         var2 = 64;
      }

      this.F = (var4 ^ var3 ^ var1) >> 8 & 16 | var3 >> 16 & 1 | var3 >> 8 & 128 | var2 | ((var1 ^ var4 ^ '耀') & (var1 ^ var3) & '耀') >> 13;
      this.H = var3 >> 8 & 255;
      this.L = var3 & 255;
   }

   private final void adc_A(int var1, int var2) {
      this.cycle -= var2;
      this.A = this.adcA_8(var1, this.A);
   }

   private final void adc_A_n() {
      this.cycle -= 7;
      CpuBoard var2 = this.ram;
      int var1 = this.PC;
      this.PC = var1 + 1;
      this.A = this.adcA_8(var2.read8arg(var1), this.A);
   }

   private final int add16(int var1, int var2) {
      int var3 = var1 + var2;
      this.F = this.F & 196 | (var1 ^ var3 ^ var2) >> 8 & 16 | var3 >> 16 & 1;
      return '\uffff' & var3;
   }

   private final int addA_8(int var1, int var2) {
      this.tmp = var2 + var1 & 255;
      this.F = SZHVC_Add[var2 << 8 | this.tmp];
      return this.tmp;
   }

   private final void add_A(int var1, int var2) {
      this.cycle -= var2;
      this.A = this.addA_8(var1, this.A);
   }

   private final void add_A_n() {
      this.cycle -= 7;
      CpuBoard var2 = this.ram;
      int var1 = this.PC;
      this.PC = var1 + 1;
      this.A = this.addA_8(var2.read8arg(var1), this.A);
   }

   private final void add_HL_BC() {
      this.cycle -= 11;
      this.HL(this.add16(this.HL(), this.BC()));
   }

   private final void add_HL_DE() {
      this.cycle -= 11;
      this.HL(this.add16(this.HL(), this.DE()));
   }

   private final void add_HL_HL() {
      this.cycle -= 11;
      int var1 = this.HL();
      this.HL(this.add16(var1, var1));
   }

   private final void add_HL_SP() {
      this.cycle -= 11;
      this.HL(this.add16(this.HL(), this.SP));
   }

   private final int andA(int var1, int var2) {
      var1 &= var2;
      this.F = SZP[var1] | 16;
      return var1;
   }

   private final void and_A(int var1, int var2) {
      this.cycle -= var2;
      this.A = this.andA(var1, this.A);
   }

   private final void and_A_n() {
      this.cycle -= 7;
      CpuBoard var2 = this.ram;
      int var1 = this.PC;
      this.PC = var1 + 1;
      this.A = this.andA(var2.read8arg(var1), this.A);
   }

   private final void bit(int var1, int var2) {
      this.F = this.F & 1 | 16 | SZ_BIT[bitSet[var1] & var2];
   }

   private final void call_C_nn() {
      if((this.F & 1) != 0) {
         this.cycle -= 17;
         this.push(this.PC + 2);
         this.PC = this.ram.read16arg(this.PC);
      } else {
         this.cycle -= 10;
         this.PC += 2;
      }

   }

   private final void call_M_nn() {
      if((this.F & 128) != 0) {
         this.cycle -= 17;
         this.push(this.PC + 2);
         this.PC = this.ram.read16arg(this.PC);
      } else {
         this.cycle -= 10;
         this.PC += 2;
      }

   }

   private final void call_NC_nn() {
      if((this.F & 1) == 0) {
         this.cycle -= 17;
         this.push(this.PC + 2);
         this.PC = this.ram.read16arg(this.PC);
      } else {
         this.cycle -= 10;
         this.PC += 2;
      }

   }

   private final void call_NZ_nn() {
      if((this.F & 64) == 0) {
         this.cycle -= 17;
         this.push(this.PC + 2);
         this.PC = this.ram.read16arg(this.PC);
      } else {
         this.cycle -= 10;
         this.PC += 2;
      }

   }

   private final void call_PE_nn() {
      if((this.F & 4) != 0) {
         this.cycle -= 17;
         this.push(this.PC + 2);
         this.PC = this.ram.read16arg(this.PC);
      } else {
         this.cycle -= 10;
         this.PC += 2;
      }

   }

   private final void call_PO_nn() {
      if((this.F & 4) == 0) {
         this.cycle -= 17;
         this.push(this.PC + 2);
         this.PC = this.ram.read16arg(this.PC);
      } else {
         this.cycle -= 10;
         this.PC += 2;
      }

   }

   private final void call_P_nn() {
      if((this.F & 128) == 0) {
         this.cycle -= 17;
         this.push(this.PC + 2);
         this.PC = this.ram.read16arg(this.PC);
      } else {
         this.cycle -= 10;
         this.PC += 2;
      }

   }

   private final void call_Z_nn() {
      if((this.F & 64) != 0) {
         this.cycle -= 17;
         this.push(this.PC + 2);
         this.PC = this.ram.read16arg(this.PC);
      } else {
         this.cycle -= 10;
         this.PC += 2;
      }

   }

   private final void call_nn() {
      this.cycle -= 17;
      this.push(this.PC + 2);
      this.PC = this.ram.read16arg(this.PC);
   }

   private final void ccf() {
      this.cycle -= 4;
      this.F = (this.F & 197 | (this.F & 1) << 4 | this.A & 40) ^ 1;
   }

   private int checkInterrupt(int var1) {
      int var2;
      if(!this.NMI) {
         var2 = var1;
         if(!this.IFF0) {
            return var2;
         }

         var2 = var1;
         if(!this.IRQ) {
            return var2;
         }
      }

      int var3 = var1;
      if(this.NMI) {
         this.state_HALT = false;
         this.IFF1 = this.IFF0;
         this.IFF0 = false;
         this.NMI = false;
         this.push(this.PC);
         this.PC = 102;
         var3 = var1 - 13;
      }

      var2 = var3;
      if(this.IFF0) {
         var2 = var3;
         if(this.IRQ) {
            this.state_HALT = false;
            switch(this.IM) {
            case 0:
               this.IFF0 = false;
               this.IRQ = false;
               this.push(this.PC);
               if(this.I_Vector != 0 && this.I_Vector != 255) {
                  this.PC = this.I_Vector;
               } else {
                  this.PC = 56;
               }

               var2 = var3 - 13;
               break;
            case 1:
               this.IFF0 = false;
               this.IRQ = false;
               this.push(this.PC);
               this.PC = 56;
               var2 = var3 - 13;
               break;
            case 2:
               this.IFF0 = false;
               this.IRQ = false;
               this.push(this.PC);
               this.PC = this.ram.read16arg(this.I << 8 | this.I_Vector);
               var2 = var3 - 19;
               break;
            default:
               var2 = var3;
            }
         }
      }

      return var2;
   }

   private final void cpA_8(int var1, int var2) {
      this.F = SZHVC_sub[var2 << 8 | var2 - var1 & 255];
   }

   private final void cp_A(int var1, int var2) {
      this.cycle -= var2;
      this.cpA_8(var1, this.A);
   }

   private final void cp_n() {
      this.cycle -= 7;
      CpuBoard var2 = this.ram;
      int var1 = this.PC;
      this.PC = var1 + 1;
      this.cpA_8(var2.read8arg(var1), this.A);
   }

   private final void cpl() {
      this.cycle -= 4;
      this.A ^= 255;
      this.F = this.F & 197 | 18 | this.A & 40;
   }

   private final void daa() {
      this.cycle -= 4;
      this.tmp1 = this.A;
      this.tmp2 = 0;
      this.tmp3 = this.F & 1;
      int var2 = this.tmp3;
      if((this.F & 16) != 0 || (this.tmp1 & 15) > 9) {
         this.tmp2 |= 6;
      }

      int var1;
      label33: {
         if(this.tmp3 != 1 && this.tmp1 <= 159) {
            var1 = var2;
            if(this.tmp1 <= 143) {
               break label33;
            }

            var1 = var2;
            if((this.tmp1 & 15) <= 9) {
               break label33;
            }
         }

         this.tmp2 |= 96;
         var1 = 1;
      }

      if(this.tmp1 > 153) {
         var1 = 1;
      }

      if((this.F & 2) != 0) {
         this.cycle -= 4;
         this.A = this.subA_8(this.tmp2, this.A);
      } else {
         this.cycle -= 4;
         this.A = this.addA_8(this.tmp2, this.A);
      }

      this.F = this.F & 254 | var1;
      if(parity[this.A]) {
         this.F = this.F & 251 | 4;
      } else {
         this.F &= 251;
      }

   }

   private void debug(int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9, int var10, int var11, int var12, int var13, int var14, int var15) {
      if(var2 == debugBreakPoint) {
         debugEnabled = true;
      }

      if(debugEnabled) {
         if(this.startSlice) {
            System.out.println("*** " + this.tag + " ***");
            this.startSlice = false;
         }

         var3 = var1 & '\uff00';
         String var17 = this.hex(var2, 4) + ": " + this.hex(this.ram.read8(var2), 2) + "," + this.hex(this.ram.read8(var2 + 1), 2) + "," + this.hex(this.ram.read8(var2 + 2), 2) + "," + this.hex(this.ram.read8(var2 + 3), 2) + "    \t";
         String var16;
         if(var1 < 256) {
            var16 = var17 + opc1[var1];
         } else if(var3 == '\ued00') {
            var16 = var17 + opc3[var1 & 255];
         } else {
            var16 = var17;
            if(var3 == '쬀') {
               var16 = var17 + opc2[var1 & 255];
            }
         }

         var16 = var16 + "   AF:" + this.hex(this.AF(), 4) + " BC:" + this.hex(this.BC(), 4) + " DE:" + this.hex(this.DE(), 4) + " HL:" + this.hex(this.HL(), 4) + " SP:" + this.hex(var11, 4) + " IX:" + this.hex(var12, 4) + " IY:" + this.hex(var13, 4) + " I:" + this.hex(var14, 4) + " Cycles:" + Integer.toString(var15);
         System.out.println(var16);
      }

   }

   private final int dec8(int var1) {
      var1 = var1 - 1 & 255;
      this.F = this.F & 1 | SZHV_dec[var1];
      return var1;
   }

   private final int decL16(int var1) {
      return '\uff00' & var1 | this.dec8(var1 & 255);
   }

   private final void dec_A() {
      this.cycle -= 4;
      this.A = this.dec8(this.A);
   }

   private final void dec_B() {
      this.cycle -= 4;
      this.B = this.dec8(this.B);
   }

   private final void dec_BC() {
      this.cycle -= 6;
      this.BC(this.BC() - 1 & '\uffff');
   }

   private final void dec_C() {
      this.cycle -= 4;
      this.C = this.dec8(this.C);
   }

   private final void dec_D() {
      this.cycle -= 4;
      this.D = this.dec8(this.D);
   }

   private final void dec_DE() {
      this.cycle -= 6;
      this.DE(this.DE() - 1 & '\uffff');
   }

   private final void dec_E() {
      this.cycle -= 4;
      this.E = this.dec8(this.E);
   }

   private final void dec_H() {
      this.cycle -= 4;
      this.H = this.dec8(this.H);
   }

   private final void dec_HL() {
      this.cycle -= 6;
      this.HL(this.HL() - 1 & '\uffff');
   }

   private final void dec_HLi() {
      this.cycle -= 11;
      int var1 = this.HL();
      this.ram.write8(var1, this.dec8(this.ram.read8(var1)));
   }

   private final void dec_L() {
      this.cycle -= 4;
      this.L = this.dec8(this.L);
   }

   private final void dec_SP() {
      this.cycle -= 6;
      this.SP = this.SP - 1 & '\uffff';
   }

   private final void di() {
      this.cycle -= 4;
      this.IFF1 = false;
      this.IFF0 = false;
   }

   private final void djnz_n() {
      this.B = this.B - 1 & 255;
      if(this.B != 0) {
         this.cycle -= 13;
         this.PC += this.sign(this.ram.read8arg(this.PC));
         ++this.PC;
      } else {
         this.cycle -= 8;
         ++this.PC;
      }

   }

   private final void ei() {
      this.cycle -= 4;
      this.IFF1 = true;
      this.IFF0 = true;
      this.goingToirq = true;
      this.cycle = this.checkInterrupt(this.cycle);
   }

   private final void error(int var1, int var2) {
      System.out.println("CPU error: illegal instruction $" + Integer.toHexString(var1) + " at $" + Integer.toHexString(var2));
   }

   private final void ex_AF_AF() {
      this.cycle -= 4;
      this.tmp = this.A;
      this.A = this.A1;
      this.A1 = this.tmp;
      this.tmp = this.F;
      this.F = this.F1;
      this.F1 = this.tmp;
   }

   private final void ex_DE_HL() {
      this.cycle -= 4;
      this.tmp = this.D;
      this.D = this.H;
      this.H = this.tmp;
      this.tmp = this.E;
      this.E = this.L;
      this.L = this.tmp;
   }

   private final void ex_SPi_HL() {
      this.cycle -= 19;
      this.tmp = this.ram.read8(this.SP + 1);
      this.ram.write8fast(this.SP + 1, this.H);
      this.H = this.tmp;
      this.tmp = this.ram.read8(this.SP);
      this.ram.write8fast(this.SP, this.L);
      this.L = this.tmp;
   }

   private final int execXY(int var1) {
      int var2 = this.instruction;
      CpuBoard var4 = this.ram;
      int var3 = this.PC;
      this.PC = var3 + 1;
      this.instruction = (var2 << 8) + var4.read8opc(var3);
      this.IXYd = this.sign(this.ram.read8arg(this.PC)) + var1 & '\uffff';
      switch(this.instruction & 255) {
      case 0:
      case 1:
      case 2:
      case 3:
      case 4:
      case 5:
      case 6:
      case 7:
      case 8:
         this.error(this.instruction, this.PPC);
         break;
      case 9:
         this.cycle -= 15;
         var1 = this.add16(var1, this.BC());
         break;
      case 10:
      case 11:
      case 12:
      case 13:
      case 14:
      case 15:
      case 16:
      case 17:
      case 18:
      case 19:
      case 20:
      case 21:
      case 22:
      case 23:
      case 24:
         this.error(this.instruction, this.PPC);
         break;
      case 25:
         this.cycle -= 15;
         var1 = this.add16(var1, this.DE());
         break;
      case 26:
      case 27:
      case 28:
      case 29:
      case 30:
      case 31:
      case 32:
         this.error(this.instruction, this.PPC);
         break;
      case 33:
         this.cycle -= 14;
         var1 = this.ram.read16arg(this.PC);
         this.PC += 2;
         break;
      case 34:
         this.cycle -= 20;
         this.ram.write16(this.ram.read16arg(this.PC), var1);
         this.PC += 2;
         break;
      case 35:
         this.cycle -= 10;
         var1 = var1 + 1 & '\uffff';
         break;
      case 36:
      case 37:
         this.error(this.instruction, this.PPC);
         break;
      case 38:
         this.cycle -= 11;
         var4 = this.ram;
         var2 = this.PC;
         this.PC = var2 + 1;
         var1 = var1 & 255 | var4.read8arg(var2) << 8;
         break;
      case 39:
      case 40:
         this.error(this.instruction, this.PPC);
         break;
      case 41:
         this.cycle -= 15;
         var1 = this.add16(var1, var1);
         break;
      case 42:
         this.cycle -= 20;
         var1 = this.ram.read16(this.ram.read16arg(this.PC));
         this.PC += 2;
         break;
      case 43:
         this.cycle -= 10;
         var1 = var1 - 1 & '\uffff';
         break;
      case 44:
         this.cycle -= 15;
         var1 = this.incL16(var1);
         break;
      case 45:
         this.cycle -= 15;
         var1 = this.decL16(var1);
         break;
      case 46:
         this.cycle -= 11;
         var4 = this.ram;
         var2 = this.PC;
         this.PC = var2 + 1;
         var1 = '\uff00' & var1 | var4.read8arg(var2);
         break;
      case 47:
      case 48:
      case 49:
      case 50:
      case 51:
         this.error(this.instruction, this.PPC);
         break;
      case 52:
         this.cycle -= 15;
         this.ram.write8(this.IXYd, this.inc8(this.ram.read8(this.IXYd)));
         ++this.PC;
         break;
      case 53:
         this.cycle -= 15;
         this.ram.write8(this.IXYd, this.dec8(this.ram.read8(this.IXYd)));
         ++this.PC;
         break;
      case 54:
         this.cycle -= 19;
         this.ram.write8(this.IXYd, this.ram.read8arg(this.PC + 1));
         this.PC += 2;
         break;
      case 55:
      case 56:
         this.error(this.instruction, this.PPC);
         break;
      case 57:
         this.cycle -= 15;
         var2 = this.SP + var1 & '\uffff';
         if(var2 < var1) {
            this.F = this.F & 253 | 1;
            var1 = var2;
         } else {
            this.F &= 253;
            var1 = var2;
         }
         break;
      case 58:
      case 59:
      case 60:
      case 61:
      case 62:
      case 63:
      case 64:
      case 65:
      case 66:
      case 67:
      case 68:
      case 69:
         this.error(this.instruction, this.PPC);
         break;
      case 70:
         this.cycle -= 11;
         this.B = this.ram.read8(this.IXYd);
         ++this.PC;
         break;
      case 71:
      case 72:
      case 73:
      case 74:
      case 75:
         this.error(this.instruction, this.PPC);
         break;
      case 76:
         this.cycle -= 11;
         this.C = var1 >> 8;
         break;
      case 77:
         this.cycle -= 11;
         this.C = var1 & 255;
         break;
      case 78:
         this.cycle -= 11;
         this.C = this.ram.read8(this.IXYd);
         ++this.PC;
         break;
      case 79:
      case 80:
      case 81:
      case 82:
      case 83:
         this.error(this.instruction, this.PPC);
         break;
      case 84:
         this.cycle -= 11;
         this.D = var1 >> 8;
         break;
      case 85:
         this.cycle -= 11;
         this.D = var1 & 255;
         break;
      case 86:
         this.cycle -= 11;
         this.D = this.ram.read8(this.IXYd);
         ++this.PC;
         break;
      case 87:
      case 88:
      case 89:
      case 90:
      case 91:
         this.error(this.instruction, this.PPC);
         break;
      case 92:
         this.cycle -= 11;
         this.E = var1 >> 8;
         break;
      case 93:
         this.cycle -= 11;
         this.E = var1 & 255;
         break;
      case 94:
         this.cycle -= 11;
         this.E = this.ram.read8(this.IXYd);
         ++this.PC;
         break;
      case 95:
         this.error(this.instruction, this.PPC);
         break;
      case 96:
         this.cycle -= 11;
         var1 = this.ldXYH_8(var1, this.B);
         break;
      case 97:
         this.cycle -= 11;
         var1 = this.ldXYH_8(var1, this.C);
         break;
      case 98:
         this.cycle -= 11;
         var1 = this.ldXYH_8(var1, this.D);
         break;
      case 99:
         this.cycle -= 11;
         var1 = this.ldXYH_8(var1, this.E);
         break;
      case 100:
      case 101:
         this.error(this.instruction, this.PPC);
         break;
      case 102:
         this.cycle -= 11;
         this.H = this.ram.read8(this.IXYd);
         ++this.PC;
         break;
      case 103:
         this.cycle -= 11;
         var1 = var1 & 255 | this.A << 8;
         break;
      case 104:
         this.cycle -= 11;
         var1 = this.ldXYL_8(var1, this.B);
         break;
      case 105:
         this.cycle -= 11;
         var1 = this.ldXYL_8(var1, this.C);
         break;
      case 106:
         this.cycle -= 11;
         var1 = this.ldXYL_8(var1, this.D);
         break;
      case 107:
         this.cycle -= 11;
         var1 = this.ldXYL_8(var1, this.E);
         break;
      case 108:
      case 109:
         this.error(this.instruction, this.PPC);
         break;
      case 110:
         this.cycle -= 11;
         this.L = this.ram.read8(this.IXYd);
         ++this.PC;
         break;
      case 111:
         this.cycle -= 11;
         var1 = '\uff00' & var1 | this.A;
         break;
      case 112:
         this.cycle -= 11;
         this.ram.write8(this.IXYd, this.B);
         ++this.PC;
         break;
      case 113:
         this.cycle -= 11;
         this.ram.write8(this.IXYd, this.C);
         ++this.PC;
         break;
      case 114:
         this.cycle -= 11;
         this.ram.write8(this.IXYd, this.D);
         ++this.PC;
         break;
      case 115:
         this.cycle -= 11;
         this.ram.write8(this.IXYd, this.E);
         ++this.PC;
         break;
      case 116:
         this.cycle -= 11;
         this.ram.write8(this.IXYd, this.H);
         ++this.PC;
         break;
      case 117:
         this.cycle -= 11;
         this.ram.write8(this.IXYd, this.L);
         ++this.PC;
         break;
      case 118:
      case 191:
      case 192:
      case 193:
      case 194:
      case 195:
      case 196:
      case 197:
      case 198:
      case 199:
      case 200:
      case 201:
      case 202:
      case 204:
      case 205:
      case 206:
      case 207:
      case 208:
      case 209:
      case 210:
      case 211:
      case 212:
      case 213:
      case 214:
      case 215:
      case 216:
      case 217:
      case 218:
      case 219:
      case 220:
      case 221:
      case 222:
      case 223:
      case 224:
      case 226:
      case 228:
      case 230:
      case 231:
      case 232:
      case 234:
      case 236:
      case 237:
      case 238:
      case 239:
      case 240:
      case 241:
      case 242:
      case 243:
      case 244:
      case 245:
      case 246:
      case 247:
      case 248:
      default:
         this.error(this.instruction, this.PPC);
         break;
      case 119:
         this.cycle -= 11;
         this.ram.write8(this.IXYd, this.A);
         ++this.PC;
         break;
      case 120:
      case 121:
      case 122:
      case 123:
         this.error(this.instruction, this.PPC);
         break;
      case 124:
         this.cycle -= 11;
         this.A = ('\uff00' & var1) >> 8;
         break;
      case 125:
         this.cycle -= 11;
         this.A = var1 & 255;
         break;
      case 126:
         this.cycle -= 11;
         this.A = this.ram.read8(this.IXYd);
         ++this.PC;
         break;
      case 127:
      case 128:
      case 129:
      case 130:
      case 131:
         this.error(this.instruction, this.PPC);
         break;
      case 132:
         this.cycle -= 11;
         this.A = this.addA_8(var1 >> 8, this.A);
         break;
      case 133:
         this.cycle -= 11;
         this.A = this.addA_8(var1 & 255, this.A);
         break;
      case 134:
         this.cycle -= 11;
         this.A = this.addA_8(this.ram.read8(this.IXYd), this.A);
         ++this.PC;
         break;
      case 135:
      case 136:
      case 137:
      case 138:
      case 139:
      case 140:
      case 141:
         this.error(this.instruction, this.PPC);
         break;
      case 142:
         this.cycle -= 11;
         this.A = this.adcA_8(this.ram.read8(this.IXYd), this.A);
         ++this.PC;
         break;
      case 143:
      case 144:
      case 145:
      case 146:
      case 147:
         this.error(this.instruction, this.PPC);
         break;
      case 148:
         this.cycle -= 11;
         this.A = this.subA_8(var1 >> 8, this.A);
         break;
      case 149:
         this.cycle -= 11;
         this.A = this.subA_8(var1 & 255, this.A);
         break;
      case 150:
         this.cycle -= 11;
         this.A = this.subA_8(this.ram.read8(this.IXYd), this.A);
         ++this.PC;
         break;
      case 151:
      case 152:
      case 153:
      case 154:
      case 155:
      case 156:
      case 157:
         this.error(this.instruction, this.PPC);
         break;
      case 158:
         this.cycle -= 11;
         this.sbcA_8(this.ram.read8(this.IXYd));
         ++this.PC;
         break;
      case 159:
      case 160:
      case 161:
      case 162:
      case 163:
      case 164:
      case 165:
         this.error(this.instruction, this.PPC);
         break;
      case 166:
         this.cycle -= 11;
         this.A = this.andA(this.ram.read8(this.IXYd), this.A);
         ++this.PC;
         break;
      case 167:
      case 168:
      case 169:
      case 170:
      case 171:
         this.error(this.instruction, this.PPC);
         break;
      case 172:
         this.cycle -= 11;
         this.A = this.xorA(var1 >> 8, this.A);
         break;
      case 173:
         this.error(this.instruction, this.PPC);
         break;
      case 174:
         this.cycle -= 11;
         this.A = this.xorA(this.ram.read8(this.IXYd), this.A);
         ++this.PC;
         break;
      case 175:
      case 176:
      case 177:
      case 178:
      case 179:
      case 180:
      case 181:
         this.error(this.instruction, this.PPC);
         break;
      case 182:
         this.cycle -= 11;
         this.A = this.orA(this.ram.read8(this.IXYd), this.A);
         ++this.PC;
         break;
      case 183:
      case 184:
      case 185:
      case 186:
      case 187:
      case 188:
      case 189:
         this.error(this.instruction, this.PPC);
         break;
      case 190:
         this.cycle -= 11;
         this.cpA_8(this.ram.read8(this.IXYd), this.A);
         ++this.PC;
         break;
      case 203:
         var4 = this.ram;
         var2 = this.PC;
         this.PC = var2 + 1;
         this.IXYd = this.sign(var4.read8arg(var2)) + var1;
         var3 = this.instruction;
         var4 = this.ram;
         var2 = this.PC;
         this.PC = var2 + 1;
         this.instruction = (var3 << 8) + var4.read8(var2);
         switch(this.instruction & 255) {
         case 6:
            this.cycle -= 15;
            this.ram.write8(this.IXYd, this.rlc(this.ram.read8(this.IXYd)));
            return var1;
         case 14:
            this.cycle -= 15;
            this.ram.write8(this.IXYd, this.rrc(this.ram.read8(this.IXYd)));
            return var1;
         case 22:
            this.cycle -= 12;
            this.ram.write8(this.IXYd, this.rl(this.ram.read8(this.IXYd)));
            return var1;
         case 30:
            this.cycle -= 12;
            this.ram.write8(this.IXYd, this.rr(this.ram.read8(this.IXYd)));
            return var1;
         case 38:
            this.cycle -= 12;
            this.ram.write8(this.IXYd, this.sla(this.ram.read8(this.IXYd)));
            return var1;
         case 46:
            this.cycle -= 12;
            this.ram.write8(this.IXYd, this.sra(this.ram.read8(this.IXYd)));
            return var1;
         case 54:
            this.cycle -= 12;
            this.ram.write8(this.IXYd, this.sll(this.ram.read8(this.IXYd)));
            return var1;
         case 62:
            this.cycle -= 12;
            this.ram.write8(this.IXYd, this.srl(this.ram.read8(this.IXYd)));
            return var1;
         case 64:
         case 65:
         case 66:
         case 67:
         case 68:
         case 69:
         case 70:
         case 71:
            this.cycle -= 12;
            this.bit(0, this.ram.read8(this.IXYd));
            return var1;
         case 72:
         case 73:
         case 74:
         case 75:
         case 76:
         case 77:
         case 78:
         case 79:
            this.cycle -= 12;
            this.bit(1, this.ram.read8(this.IXYd));
            return var1;
         case 80:
         case 81:
         case 82:
         case 83:
         case 84:
         case 85:
         case 86:
         case 87:
            this.cycle -= 12;
            this.bit(2, this.ram.read8(this.IXYd));
            return var1;
         case 88:
         case 89:
         case 90:
         case 91:
         case 92:
         case 93:
         case 94:
         case 95:
            this.cycle -= 12;
            this.bit(3, this.ram.read8(this.IXYd));
            return var1;
         case 96:
         case 97:
         case 98:
         case 99:
         case 100:
         case 101:
         case 102:
         case 103:
            this.cycle -= 12;
            this.bit(4, this.ram.read8(this.IXYd));
            return var1;
         case 104:
         case 105:
         case 106:
         case 107:
         case 108:
         case 109:
         case 110:
         case 111:
            this.cycle -= 12;
            this.bit(5, this.ram.read8(this.IXYd));
            return var1;
         case 112:
         case 113:
         case 114:
         case 115:
         case 116:
         case 117:
         case 118:
         case 119:
            this.cycle -= 12;
            this.bit(6, this.ram.read8(this.IXYd));
            return var1;
         case 120:
         case 121:
         case 122:
         case 123:
         case 124:
         case 125:
         case 126:
         case 127:
            this.cycle -= 12;
            this.bit(7, this.ram.read8(this.IXYd));
            return var1;
         case 134:
            this.cycle -= 12;
            this.ram.write8(this.IXYd, this.res(0, this.ram.read8(this.IXYd)));
            return var1;
         case 142:
            this.cycle -= 12;
            this.ram.write8(this.IXYd, this.res(1, this.ram.read8(this.IXYd)));
            return var1;
         case 150:
            this.cycle -= 12;
            this.ram.write8(this.IXYd, this.res(2, this.ram.read8(this.IXYd)));
            return var1;
         case 158:
            this.cycle -= 12;
            this.ram.write8(this.IXYd, this.res(3, this.ram.read8(this.IXYd)));
            return var1;
         case 166:
            this.cycle -= 12;
            this.ram.write8(this.IXYd, this.res(4, this.ram.read8(this.IXYd)));
            return var1;
         case 174:
            this.cycle -= 12;
            this.ram.write8(this.IXYd, this.res(5, this.ram.read8(this.IXYd)));
            return var1;
         case 182:
            this.cycle -= 12;
            this.ram.write8(this.IXYd, this.res(6, this.ram.read8(this.IXYd)));
            return var1;
         case 190:
            this.cycle -= 12;
            this.ram.write8(this.IXYd, this.res(7, this.ram.read8(this.IXYd)));
            return var1;
         case 198:
            this.cycle -= 12;
            this.ram.write8(this.IXYd, this.set(0, this.ram.read8(this.IXYd)));
            return var1;
         case 206:
            this.cycle -= 12;
            this.ram.write8(this.IXYd, this.set(1, this.ram.read8(this.IXYd)));
            return var1;
         case 214:
            this.cycle -= 12;
            this.ram.write8(this.IXYd, this.set(2, this.ram.read8(this.IXYd)));
            return var1;
         case 222:
            this.cycle -= 12;
            this.ram.write8(this.IXYd, this.set(3, this.ram.read8(this.IXYd)));
            return var1;
         case 230:
            this.cycle -= 12;
            this.ram.write8(this.IXYd, this.set(4, this.ram.read8(this.IXYd)));
            return var1;
         case 238:
            this.cycle -= 12;
            this.ram.write8(this.IXYd, this.set(5, this.ram.read8(this.IXYd)));
            return var1;
         case 246:
            this.cycle -= 12;
            this.ram.write8(this.IXYd, this.set(6, this.ram.read8(this.IXYd)));
            return var1;
         case 254:
            this.cycle -= 12;
            this.ram.write8(this.IXYd, this.set(7, this.ram.read8(this.IXYd)));
            return var1;
         default:
            this.error(this.instruction, this.PPC);
            return var1;
         }
      case 225:
         this.cycle -= 14;
         var1 = this.pop();
         break;
      case 227:
         this.cycle -= 23;
         var2 = this.ram.read16(this.SP);
         this.ram.write16(this.SP, var1);
         var1 = var2;
         break;
      case 229:
         this.cycle -= 14;
         this.ram.write16fast(this.SP - 2, var1);
         this.SP -= 2;
         break;
      case 233:
         this.cycle -= 8;
         this.PC = var1;
         break;
      case 235:
         this.cycle -= 8;
         var2 = this.DE();
         this.D = var1 >> 8;
         this.E = var1 & 255;
         var1 = var2;
         break;
      case 249:
         this.cycle -= 10;
         this.SP = var1;
      }

      return var1;
   }

   private final void exx() {
      this.cycle -= 4;
      this.tmp = this.B;
      this.B = this.B1;
      this.B1 = this.tmp;
      this.tmp = this.C;
      this.C = this.C1;
      this.C1 = this.tmp;
      this.tmp = this.D;
      this.D = this.D1;
      this.D1 = this.tmp;
      this.tmp = this.E;
      this.E = this.E1;
      this.E1 = this.tmp;
      this.tmp = this.H;
      this.H = this.H1;
      this.H1 = this.tmp;
      this.tmp = this.L;
      this.L = this.L1;
      this.L1 = this.tmp;
   }

   private final void halt() {
      this.cycle -= 4;
      this.state_HALT = true;
      this.goingToirq = false;
      --this.PC;
      this.cycle = 0;
   }

   private final int in(int var1) {
      var1 = this.ram.in(var1);
      this.F = this.F & 1 | SZP[var1];
      return var1;
   }

   private final void in_A_n() {
      this.cycle -= 11;
      CpuBoard var2 = this.ram;
      int var1 = this.PC;
      this.PC = var1 + 1;
      this.A = this.in(var2.read8arg(var1));
   }

   private final int inc8(int var1) {
      var1 = var1 + 1 & 255;
      this.F = this.F & 1 | SZHV_inc[var1];
      return var1;
   }

   private final int incL16(int var1) {
      return '\uff00' & var1 | this.inc8(var1 & 255);
   }

   private final void inc_A() {
      this.cycle -= 4;
      this.A = this.inc8(this.A);
   }

   private final void inc_B() {
      this.cycle -= 4;
      this.B = this.inc8(this.B);
   }

   private final void inc_BC() {
      this.cycle -= 6;
      this.BC(this.BC() + 1 & '\uffff');
   }

   private final void inc_C() {
      this.cycle -= 4;
      this.C = this.inc8(this.C);
   }

   private final void inc_D() {
      this.cycle -= 4;
      this.D = this.inc8(this.D);
   }

   private final void inc_DE() {
      this.cycle -= 6;
      this.DE(this.DE() + 1 & '\uffff');
   }

   private final void inc_E() {
      this.cycle -= 4;
      this.E = this.inc8(this.E);
   }

   private final void inc_H() {
      this.cycle -= 4;
      this.H = this.inc8(this.H);
   }

   private final void inc_HL() {
      this.cycle -= 6;
      this.HL(this.HL() + 1 & '\uffff');
   }

   private final void inc_HLi() {
      this.cycle -= 11;
      int var1 = this.HL();
      this.ram.write8(var1, this.inc8(this.ram.read8(var1)));
   }

   private final void inc_L() {
      this.cycle -= 4;
      this.L = this.inc8(this.L);
   }

   private final void inc_SP() {
      this.cycle -= 6;
      this.SP = this.SP + 1 & '\uffff';
   }

   private void ini() {
      int var1 = this.in(this.C);
      this.B = this.B - 1 & 255;
      this.ram.write8(this.HL(), var1);
      this.HL(this.HL() + 1 & '\uffff');
      this.F = SZ[this.B];
      if((var1 & 128) != 0) {
         this.F |= 2;
      }

      if((this.C + var1 + 1 & 256) != 0) {
         this.F |= 17;
      }

      if(((irep_tmp1[this.C & 3][var1 & 3] ^ breg_tmp2[this.B] ^ this.C >> 2 ^ var1 >> 2) & 1) != 0) {
         this.F |= 4;
      }

   }

   private final void jp_C_nn() {
      this.cycle -= 10;
      if((this.F & 1) != 0) {
         this.PC = this.ram.read16arg(this.PC);
      } else {
         this.PC += 2;
      }

   }

   private final void jp_HLi() {
      this.cycle -= 4;
      this.PC = this.HL();
   }

   private final void jp_M_nn() {
      this.cycle -= 10;
      if((this.F & 128) != 0) {
         this.PC = this.ram.read16arg(this.PC);
      } else {
         this.PC += 2;
      }

   }

   private final void jp_NC_nn() {
      this.cycle -= 10;
      if((this.F & 1) == 0) {
         this.PC = this.ram.read16arg(this.PC);
      } else {
         this.PC += 2;
      }

   }

   private final void jp_NZ_nn() {
      this.cycle -= 10;
      if((this.F & 64) == 0) {
         this.PC = this.ram.read16arg(this.PC);
      } else {
         this.PC += 2;
      }

   }

   private final void jp_PE_nn() {
      this.cycle -= 10;
      if((this.F & 4) != 0) {
         this.PC = this.ram.read16arg(this.PC);
      } else {
         this.PC += 2;
      }

   }

   private final void jp_PO_nn() {
      this.cycle -= 10;
      if((this.F & 4) == 0) {
         this.PC = this.ram.read16arg(this.PC);
      } else {
         this.PC += 2;
      }

   }

   private final void jp_P_nn() {
      this.cycle -= 10;
      if((this.F & 128) == 0) {
         this.PC = this.ram.read16arg(this.PC);
      } else {
         this.PC += 2;
      }

   }

   private final void jp_Z_nn() {
      this.cycle -= 10;
      if((this.F & 64) != 0) {
         this.PC = this.ram.read16arg(this.PC);
      } else {
         this.PC += 2;
      }

   }

   private final void jp_nn() {
      this.cycle -= 10;
      this.PC = this.ram.read16arg(this.PC);
   }

   private final void jr_C_e() {
      if((this.F & 1) != 0) {
         this.cycle -= 12;
         this.PC += this.sign(this.ram.read8arg(this.PC));
         ++this.PC;
      } else {
         ++this.PC;
         this.cycle -= 7;
      }

   }

   private final void jr_NC_e() {
      if((this.F & 1) == 0) {
         this.cycle -= 12;
         this.PC += this.sign(this.ram.read8arg(this.PC));
         ++this.PC;
      } else {
         ++this.PC;
         this.cycle -= 7;
      }

   }

   private final void jr_NZ_e() {
      if((this.F & 64) == 0) {
         this.cycle -= 12;
         this.PC += this.sign(this.ram.read8arg(this.PC));
         ++this.PC;
      } else {
         ++this.PC;
         this.cycle -= 7;
      }

   }

   private final void jr_Z_e() {
      if((this.F & 64) != 0) {
         this.cycle -= 12;
         this.PC += this.sign(this.ram.read8arg(this.PC));
         ++this.PC;
      } else {
         ++this.PC;
         this.cycle -= 7;
      }

   }

   private final void jr_e() {
      this.cycle -= 12;
      this.PC += this.sign(this.ram.read8arg(this.PC));
      ++this.PC;
   }

   private final int ldXYH_8(int var1, int var2) {
      return var1 & 255 | var2 << 8;
   }

   private final int ldXYL_8(int var1, int var2) {
      return '\uff00' & var1 | var2;
   }

   private final void ld_A(int var1, int var2) {
      this.cycle -= var2;
      this.A = var1;
   }

   private final void ld_A_BCi() {
      this.cycle -= 7;
      this.A = this.ram.read8(this.BC());
   }

   private final void ld_A_DEi() {
      this.cycle -= 7;
      this.A = this.ram.read8(this.DE());
   }

   private final void ld_A_n() {
      this.cycle -= 7;
      CpuBoard var2 = this.ram;
      int var1 = this.PC;
      this.PC = var1 + 1;
      this.A = var2.read8arg(var1);
   }

   private final void ld_A_ni() {
      this.cycle -= 13;
      this.A = this.ram.read8(this.ram.read16arg(this.PC));
      this.PC += 2;
   }

   private final void ld_B(int var1, int var2) {
      this.cycle -= var2;
      this.B = var1;
   }

   private final void ld_BC_nn() {
      this.cycle -= 10;
      CpuBoard var2 = this.ram;
      int var1 = this.PC;
      this.PC = var1 + 1;
      this.C = var2.read8arg(var1);
      var2 = this.ram;
      var1 = this.PC;
      this.PC = var1 + 1;
      this.B = var2.read8arg(var1);
   }

   private final void ld_BCi_A() {
      this.cycle -= 7;
      this.ram.write8(this.BC(), this.A);
   }

   private final void ld_B_n() {
      this.cycle -= 7;
      CpuBoard var2 = this.ram;
      int var1 = this.PC;
      this.PC = var1 + 1;
      this.B = var2.read8arg(var1);
   }

   private final void ld_C(int var1, int var2) {
      this.cycle -= var2;
      this.C = var1;
   }

   private final void ld_C_n() {
      this.cycle -= 7;
      CpuBoard var2 = this.ram;
      int var1 = this.PC;
      this.PC = var1 + 1;
      this.C = var2.read8arg(var1);
   }

   private final void ld_D(int var1, int var2) {
      this.cycle -= var2;
      this.D = var1;
   }

   private final void ld_DE_nn() {
      this.cycle -= 10;
      CpuBoard var2 = this.ram;
      int var1 = this.PC;
      this.PC = var1 + 1;
      this.E = var2.read8arg(var1);
      var2 = this.ram;
      var1 = this.PC;
      this.PC = var1 + 1;
      this.D = var2.read8arg(var1);
   }

   private final void ld_DEi_A() {
      this.cycle -= 7;
      this.ram.write8(this.DE(), this.A);
   }

   private final void ld_D_n() {
      this.cycle -= 7;
      CpuBoard var2 = this.ram;
      int var1 = this.PC;
      this.PC = var1 + 1;
      this.D = var2.read8arg(var1);
   }

   private final void ld_E(int var1, int var2) {
      this.cycle -= var2;
      this.E = var1;
   }

   private final void ld_E_n() {
      this.cycle -= 7;
      CpuBoard var2 = this.ram;
      int var1 = this.PC;
      this.PC = var1 + 1;
      this.E = var2.read8arg(var1);
   }

   private final void ld_H(int var1, int var2) {
      this.cycle -= var2;
      this.H = var1;
   }

   private final void ld_HL_ni() {
      this.cycle -= 16;
      int var1 = this.ram.read16arg(this.PC);
      this.H = this.ram.read8(var1 + 1);
      this.L = this.ram.read8(var1);
      this.PC += 2;
   }

   private final void ld_HL_nn() {
      this.cycle -= 10;
      CpuBoard var2 = this.ram;
      int var1 = this.PC;
      this.PC = var1 + 1;
      this.L = var2.read8arg(var1);
      var2 = this.ram;
      var1 = this.PC;
      this.PC = var1 + 1;
      this.H = var2.read8arg(var1);
   }

   private final void ld_HLi(int var1, int var2) {
      this.cycle -= var2;
      this.ram.write8(this.HL(), var1);
   }

   private final void ld_HLi_n() {
      this.cycle -= 10;
      CpuBoard var4 = this.ram;
      int var1 = this.HL();
      CpuBoard var3 = this.ram;
      int var2 = this.PC;
      this.PC = var2 + 1;
      var4.write8(var1, var3.read8arg(var2));
   }

   private final void ld_H_n() {
      this.cycle -= 7;
      CpuBoard var2 = this.ram;
      int var1 = this.PC;
      this.PC = var1 + 1;
      this.H = var2.read8arg(var1);
   }

   private final void ld_L(int var1, int var2) {
      this.cycle -= var2;
      this.L = var1;
   }

   private final void ld_L_n() {
      this.cycle -= 7;
      CpuBoard var2 = this.ram;
      int var1 = this.PC;
      this.PC = var1 + 1;
      this.L = var2.read8arg(var1);
   }

   private final void ld_SP_HL() {
      this.cycle -= 6;
      this.SP = this.HL();
   }

   private final void ld_SP_nn() {
      this.cycle -= 10;
      this.SP = this.ram.read16arg(this.PC);
      this.PC += 2;
   }

   private final void ld_ea_ind16(int var1) {
      this.ram.write16(this.ram.read16arg(this.PC), var1);
      this.PC += 2;
   }

   private final void ld_ea_ind8(int var1) {
      this.ram.write8(this.ram.read16arg(this.PC), var1);
      this.PC += 2;
   }

   private final void ld_ni_A() {
      this.cycle -= 13;
      this.ld_ea_ind8(this.A);
   }

   private final void ld_ni_HL() {
      this.cycle -= 16;
      this.ld_ea_ind16(this.HL());
   }

   private final void nop() {
      this.cycle -= 4;
   }

   private final int orA(int var1, int var2) {
      var1 |= var2;
      this.F = SZP[var1];
      return var1;
   }

   private final void or_A(int var1, int var2) {
      this.cycle -= var2;
      this.A = this.orA(var1, this.A);
   }

   private final void or_n() {
      this.cycle -= 7;
      CpuBoard var2 = this.ram;
      int var1 = this.PC;
      this.PC = var1 + 1;
      this.A = this.orA(var2.read8arg(var1), this.A);
   }

   private final void out_n_A() {
      this.cycle -= 11;
      CpuBoard var3 = this.ram;
      CpuBoard var2 = this.ram;
      int var1 = this.PC;
      this.PC = var1 + 1;
      var3.out(var2.read8arg(var1), this.A);
   }

   private final int pop() {
      int var1 = this.ram.read16arg(this.SP);
      this.SP = this.SP + 2 & '\uffff';
      return var1;
   }

   private final void pop_AF() {
      this.cycle -= 10;
      CpuBoard var2 = this.ram;
      int var1 = this.SP;
      this.SP = var1 + 1;
      this.F = var2.read8(var1);
      var2 = this.ram;
      var1 = this.SP;
      this.SP = var1 + 1;
      this.A = var2.read8(var1);
   }

   private final void pop_BC() {
      this.cycle -= 10;
      CpuBoard var2 = this.ram;
      int var1 = this.SP;
      this.SP = var1 + 1;
      this.C = var2.read8(var1);
      var2 = this.ram;
      var1 = this.SP;
      this.SP = var1 + 1;
      this.B = var2.read8(var1);
   }

   private final void pop_DE() {
      this.cycle -= 10;
      CpuBoard var2 = this.ram;
      int var1 = this.SP;
      this.SP = var1 + 1;
      this.E = var2.read8(var1);
      var2 = this.ram;
      var1 = this.SP;
      this.SP = var1 + 1;
      this.D = var2.read8(var1);
   }

   private final void pop_HL() {
      this.cycle -= 10;
      CpuBoard var2 = this.ram;
      int var1 = this.SP;
      this.SP = var1 + 1;
      this.L = var2.read8(var1);
      var2 = this.ram;
      var1 = this.SP;
      this.SP = var1 + 1;
      this.H = var2.read8(var1);
   }

   private final void prefix_BC() {
      int var1 = this.instruction;
      CpuBoard var3 = this.ram;
      int var2 = this.PC;
      this.PC = var2 + 1;
      this.instruction = (var1 << 8) + var3.read8opc(var2);
      switch(this.instruction & 255) {
      case 0:
         this.cycle -= 8;
         this.B = this.rlc(this.B);
         break;
      case 1:
         this.cycle -= 8;
         this.C = this.rlc(this.C);
         break;
      case 2:
         this.cycle -= 8;
         this.D = this.rlc(this.D);
         break;
      case 3:
         this.cycle -= 8;
         this.E = this.rlc(this.E);
         break;
      case 4:
         this.cycle -= 8;
         this.H = this.rlc(this.H);
         break;
      case 5:
         this.cycle -= 8;
         this.L = this.rlc(this.L);
         break;
      case 6:
         this.cycle -= 15;
         this.ram.write8(this.HL(), this.rlc(this.HLi()));
         break;
      case 7:
         this.cycle -= 8;
         this.A = this.rlc(this.A);
         break;
      case 8:
         this.cycle -= 8;
         this.B = this.rrc(this.B);
         break;
      case 9:
         this.cycle -= 8;
         this.C = this.rrc(this.C);
         break;
      case 10:
         this.cycle -= 8;
         this.D = this.rrc(this.D);
         break;
      case 11:
         this.cycle -= 8;
         this.E = this.rrc(this.E);
         break;
      case 12:
         this.cycle -= 8;
         this.H = this.rrc(this.H);
         break;
      case 13:
         this.cycle -= 8;
         this.L = this.rrc(this.L);
         break;
      case 14:
         this.cycle -= 15;
         this.ram.write8(this.HL(), this.rrc(this.HLi()));
         break;
      case 15:
         this.cycle -= 8;
         this.A = this.rrc(this.A);
         break;
      case 16:
         this.cycle -= 8;
         this.B = this.rl(this.B);
         break;
      case 17:
         this.cycle -= 8;
         this.C = this.rl(this.C);
         break;
      case 18:
         this.cycle -= 8;
         this.D = this.rl(this.D);
         break;
      case 19:
         this.cycle -= 8;
         this.E = this.rl(this.E);
         break;
      case 20:
         this.cycle -= 8;
         this.H = this.rl(this.H);
         break;
      case 21:
         this.cycle -= 8;
         this.L = this.rl(this.L);
         break;
      case 22:
         this.cycle -= 15;
         this.ram.write8(this.HL(), this.rl(this.HLi()));
         break;
      case 23:
         this.cycle -= 8;
         this.A = this.rl(this.A);
         break;
      case 24:
         this.cycle -= 8;
         this.B = this.rr(this.B);
         break;
      case 25:
         this.cycle -= 8;
         this.C = this.rr(this.C);
         break;
      case 26:
         this.cycle -= 8;
         this.D = this.rr(this.D);
         break;
      case 27:
         this.cycle -= 8;
         this.E = this.rr(this.E);
         break;
      case 28:
         this.cycle -= 8;
         this.H = this.rr(this.H);
         break;
      case 29:
         this.cycle -= 8;
         this.L = this.rr(this.L);
         break;
      case 30:
         this.cycle -= 15;
         this.ram.write8(this.HL(), this.rr(this.HLi()));
         break;
      case 31:
         this.cycle -= 8;
         this.A = this.rr(this.A);
         break;
      case 32:
         this.cycle -= 8;
         this.B = this.sla(this.B);
         break;
      case 33:
         this.cycle -= 8;
         this.C = this.sla(this.C);
         break;
      case 34:
         this.cycle -= 8;
         this.D = this.sla(this.D);
         break;
      case 35:
         this.cycle -= 8;
         this.E = this.sla(this.E);
         break;
      case 36:
         this.cycle -= 8;
         this.H = this.sla(this.H);
         break;
      case 37:
         this.cycle -= 8;
         this.L = this.sla(this.L);
         break;
      case 38:
         this.cycle -= 12;
         this.ram.write8(this.HL(), this.sla(this.HLi()));
         break;
      case 39:
         this.cycle -= 8;
         this.A = this.sla(this.A);
         break;
      case 40:
         this.cycle -= 8;
         this.B = this.sra(this.B);
         break;
      case 41:
         this.cycle -= 8;
         this.C = this.sra(this.C);
         break;
      case 42:
         this.cycle -= 8;
         this.D = this.sra(this.D);
         break;
      case 43:
         this.cycle -= 8;
         this.E = this.sra(this.E);
         break;
      case 44:
         this.cycle -= 8;
         this.H = this.sra(this.H);
         break;
      case 45:
         this.cycle -= 8;
         this.L = this.sra(this.L);
         break;
      case 46:
         this.cycle -= 12;
         this.ram.write8(this.HL(), this.sra(this.HLi()));
         break;
      case 47:
         this.cycle -= 8;
         this.A = this.sra(this.A);
         break;
      case 48:
         this.cycle -= 8;
         this.B = this.sll(this.B);
         break;
      case 49:
         this.cycle -= 8;
         this.C = this.sll(this.C);
         break;
      case 50:
         this.cycle -= 8;
         this.D = this.sll(this.D);
         break;
      case 51:
         this.cycle -= 8;
         this.E = this.sll(this.E);
         break;
      case 52:
         this.cycle -= 8;
         this.H = this.sll(this.H);
         break;
      case 53:
         this.cycle -= 8;
         this.L = this.sll(this.L);
         break;
      case 54:
         this.cycle -= 12;
         this.ram.write8(this.HL(), this.sll(this.HLi()));
         break;
      case 55:
         this.cycle -= 8;
         this.A = this.sll(this.A);
         break;
      case 56:
         this.cycle -= 8;
         this.B = this.srl(this.B);
         break;
      case 57:
         this.cycle -= 8;
         this.C = this.srl(this.C);
         break;
      case 58:
         this.cycle -= 8;
         this.D = this.srl(this.D);
         break;
      case 59:
         this.cycle -= 8;
         this.E = this.srl(this.E);
         break;
      case 60:
         this.cycle -= 8;
         this.H = this.srl(this.H);
         break;
      case 61:
         this.cycle -= 8;
         this.L = this.srl(this.L);
         break;
      case 62:
         this.cycle -= 12;
         this.ram.write8(this.HL(), this.srl(this.HLi()));
         break;
      case 63:
         this.cycle -= 8;
         this.A = this.srl(this.A);
         break;
      case 64:
         this.cycle -= 8;
         this.bit(0, this.B);
         break;
      case 65:
         this.cycle -= 8;
         this.bit(0, this.C);
         break;
      case 66:
         this.cycle -= 8;
         this.bit(0, this.D);
         break;
      case 67:
         this.cycle -= 8;
         this.bit(0, this.E);
         break;
      case 68:
         this.cycle -= 8;
         this.bit(0, this.H);
         break;
      case 69:
         this.cycle -= 8;
         this.bit(0, this.L);
         break;
      case 70:
         this.cycle -= 12;
         this.bit(0, this.HLi());
         break;
      case 71:
         this.cycle -= 8;
         this.bit(0, this.A);
         break;
      case 72:
         this.cycle -= 8;
         this.bit(1, this.B);
         break;
      case 73:
         this.cycle -= 8;
         this.bit(1, this.C);
         break;
      case 74:
         this.cycle -= 8;
         this.bit(1, this.D);
         break;
      case 75:
         this.cycle -= 8;
         this.bit(1, this.E);
         break;
      case 76:
         this.cycle -= 8;
         this.bit(1, this.H);
         break;
      case 77:
         this.cycle -= 8;
         this.bit(1, this.L);
         break;
      case 78:
         this.cycle -= 12;
         this.bit(1, this.HLi());
         break;
      case 79:
         this.cycle -= 8;
         this.bit(1, this.A);
         break;
      case 80:
         this.cycle -= 8;
         this.bit(2, this.B);
         break;
      case 81:
         this.cycle -= 8;
         this.bit(2, this.C);
         break;
      case 82:
         this.cycle -= 8;
         this.bit(2, this.D);
         break;
      case 83:
         this.cycle -= 8;
         this.bit(2, this.E);
         break;
      case 84:
         this.cycle -= 8;
         this.bit(2, this.H);
         break;
      case 85:
         this.cycle -= 8;
         this.bit(2, this.L);
         break;
      case 86:
         this.cycle -= 12;
         this.bit(2, this.HLi());
         break;
      case 87:
         this.cycle -= 8;
         this.bit(2, this.A);
         break;
      case 88:
         this.cycle -= 8;
         this.bit(3, this.B);
         break;
      case 89:
         this.cycle -= 8;
         this.bit(3, this.C);
         break;
      case 90:
         this.cycle -= 8;
         this.bit(3, this.D);
         break;
      case 91:
         this.cycle -= 8;
         this.bit(3, this.E);
         break;
      case 92:
         this.cycle -= 8;
         this.bit(3, this.H);
         break;
      case 93:
         this.cycle -= 8;
         this.bit(3, this.L);
         break;
      case 94:
         this.cycle -= 12;
         this.bit(3, this.HLi());
         break;
      case 95:
         this.cycle -= 8;
         this.bit(3, this.A);
         break;
      case 96:
         this.cycle -= 8;
         this.bit(4, this.B);
         break;
      case 97:
         this.cycle -= 8;
         this.bit(4, this.C);
         break;
      case 98:
         this.cycle -= 8;
         this.bit(4, this.D);
         break;
      case 99:
         this.cycle -= 8;
         this.bit(4, this.E);
         break;
      case 100:
         this.cycle -= 8;
         this.bit(4, this.H);
         break;
      case 101:
         this.cycle -= 8;
         this.bit(4, this.L);
         break;
      case 102:
         this.cycle -= 12;
         this.bit(4, this.HLi());
         break;
      case 103:
         this.cycle -= 8;
         this.bit(4, this.A);
         break;
      case 104:
         this.cycle -= 8;
         this.bit(5, this.B);
         break;
      case 105:
         this.cycle -= 8;
         this.bit(5, this.C);
         break;
      case 106:
         this.cycle -= 8;
         this.bit(5, this.D);
         break;
      case 107:
         this.cycle -= 8;
         this.bit(5, this.E);
         break;
      case 108:
         this.cycle -= 8;
         this.bit(5, this.H);
         break;
      case 109:
         this.cycle -= 8;
         this.bit(5, this.L);
         break;
      case 110:
         this.cycle -= 12;
         this.bit(5, this.HLi());
         break;
      case 111:
         this.cycle -= 8;
         this.bit(5, this.A);
         break;
      case 112:
         this.cycle -= 8;
         this.bit(6, this.B);
         break;
      case 113:
         this.cycle -= 8;
         this.bit(6, this.C);
         break;
      case 114:
         this.cycle -= 8;
         this.bit(6, this.D);
         break;
      case 115:
         this.cycle -= 8;
         this.bit(6, this.E);
         break;
      case 116:
         this.cycle -= 8;
         this.bit(6, this.H);
         break;
      case 117:
         this.cycle -= 8;
         this.bit(6, this.L);
         break;
      case 118:
         this.cycle -= 12;
         this.bit(6, this.HLi());
         break;
      case 119:
         this.cycle -= 8;
         this.bit(6, this.A);
         break;
      case 120:
         this.cycle -= 8;
         this.bit(7, this.B);
         break;
      case 121:
         this.cycle -= 8;
         this.bit(7, this.C);
         break;
      case 122:
         this.cycle -= 8;
         this.bit(7, this.D);
         break;
      case 123:
         this.cycle -= 8;
         this.bit(7, this.E);
         break;
      case 124:
         this.cycle -= 8;
         this.bit(7, this.H);
         break;
      case 125:
         this.cycle -= 8;
         this.bit(7, this.L);
         break;
      case 126:
         this.cycle -= 12;
         this.bit(7, this.HLi());
         break;
      case 127:
         this.cycle -= 8;
         this.bit(7, this.A);
         break;
      case 128:
         this.cycle -= 8;
         this.B = this.res(0, this.B);
         break;
      case 129:
         this.cycle -= 8;
         this.C = this.res(0, this.C);
         break;
      case 130:
         this.cycle -= 8;
         this.D = this.res(0, this.D);
         break;
      case 131:
         this.cycle -= 8;
         this.E = this.res(0, this.E);
         break;
      case 132:
         this.cycle -= 8;
         this.H = this.res(0, this.H);
         break;
      case 133:
         this.cycle -= 8;
         this.L = this.res(0, this.L);
         break;
      case 134:
         this.cycle -= 12;
         this.ram.write8(this.HL(), this.res(0, this.HLi()));
         break;
      case 135:
         this.cycle -= 8;
         this.A = this.res(0, this.A);
         break;
      case 136:
         this.cycle -= 8;
         this.B = this.res(1, this.B);
         break;
      case 137:
         this.cycle -= 8;
         this.C = this.res(1, this.C);
         break;
      case 138:
         this.cycle -= 8;
         this.D = this.res(1, this.D);
         break;
      case 139:
         this.cycle -= 8;
         this.E = this.res(1, this.E);
         break;
      case 140:
         this.cycle -= 8;
         this.H = this.res(1, this.H);
         break;
      case 141:
         this.cycle -= 8;
         this.L = this.res(1, this.L);
         break;
      case 142:
         this.cycle -= 12;
         this.ram.write8(this.HL(), this.res(1, this.HLi()));
         break;
      case 143:
         this.cycle -= 8;
         this.A = this.res(1, this.A);
         break;
      case 144:
         this.cycle -= 8;
         this.B = this.res(2, this.B);
         break;
      case 145:
         this.cycle -= 8;
         this.C = this.res(2, this.C);
         break;
      case 146:
         this.cycle -= 8;
         this.D = this.res(2, this.D);
         break;
      case 147:
         this.cycle -= 8;
         this.E = this.res(2, this.E);
         break;
      case 148:
         this.cycle -= 8;
         this.H = this.res(2, this.H);
         break;
      case 149:
         this.cycle -= 8;
         this.L = this.res(2, this.L);
         break;
      case 150:
         this.cycle -= 12;
         this.ram.write8(this.HL(), this.res(2, this.HLi()));
         break;
      case 151:
         this.cycle -= 8;
         this.A = this.res(2, this.A);
         break;
      case 152:
         this.cycle -= 8;
         this.B = this.res(3, this.B);
         break;
      case 153:
         this.cycle -= 8;
         this.C = this.res(3, this.C);
         break;
      case 154:
         this.cycle -= 8;
         this.D = this.res(3, this.D);
         break;
      case 155:
         this.cycle -= 8;
         this.E = this.res(3, this.E);
         break;
      case 156:
         this.cycle -= 8;
         this.H = this.res(3, this.H);
         break;
      case 157:
         this.cycle -= 8;
         this.L = this.res(3, this.L);
         break;
      case 158:
         this.cycle -= 12;
         this.ram.write8(this.HL(), this.res(3, this.HLi()));
         break;
      case 159:
         this.cycle -= 8;
         this.A = this.res(3, this.A);
         break;
      case 160:
         this.cycle -= 8;
         this.B = this.res(4, this.B);
         break;
      case 161:
         this.cycle -= 8;
         this.C = this.res(4, this.C);
         break;
      case 162:
         this.cycle -= 8;
         this.D = this.res(4, this.D);
         break;
      case 163:
         this.cycle -= 8;
         this.E = this.res(4, this.E);
         break;
      case 164:
         this.cycle -= 8;
         this.H = this.res(4, this.H);
         break;
      case 165:
         this.cycle -= 8;
         this.L = this.res(4, this.L);
         break;
      case 166:
         this.cycle -= 12;
         this.ram.write8(this.HL(), this.res(4, this.HLi()));
         break;
      case 167:
         this.cycle -= 8;
         this.A = this.res(4, this.A);
         break;
      case 168:
         this.cycle -= 8;
         this.B = this.res(5, this.B);
         break;
      case 169:
         this.cycle -= 8;
         this.C = this.res(5, this.C);
         break;
      case 170:
         this.cycle -= 8;
         this.D = this.res(5, this.D);
         break;
      case 171:
         this.cycle -= 8;
         this.E = this.res(5, this.E);
         break;
      case 172:
         this.cycle -= 8;
         this.H = this.res(5, this.H);
         break;
      case 173:
         this.cycle -= 8;
         this.L = this.res(5, this.L);
         break;
      case 174:
         this.cycle -= 12;
         this.ram.write8(this.HL(), this.res(5, this.HLi()));
         break;
      case 175:
         this.cycle -= 8;
         this.A = this.res(5, this.A);
         break;
      case 176:
         this.cycle -= 8;
         this.B = this.res(6, this.B);
         break;
      case 177:
         this.cycle -= 8;
         this.C = this.res(6, this.C);
         break;
      case 178:
         this.cycle -= 8;
         this.D = this.res(6, this.D);
         break;
      case 179:
         this.cycle -= 8;
         this.E = this.res(6, this.E);
         break;
      case 180:
         this.cycle -= 8;
         this.H = this.res(6, this.H);
         break;
      case 181:
         this.cycle -= 8;
         this.L = this.res(6, this.L);
         break;
      case 182:
         this.cycle -= 12;
         this.ram.write8(this.HL(), this.res(6, this.HLi()));
         break;
      case 183:
         this.cycle -= 8;
         this.A = this.res(6, this.A);
         break;
      case 184:
         this.cycle -= 8;
         this.B = this.res(7, this.B);
         break;
      case 185:
         this.cycle -= 8;
         this.C = this.res(7, this.C);
         break;
      case 186:
         this.cycle -= 8;
         this.D = this.res(7, this.D);
         break;
      case 187:
         this.cycle -= 8;
         this.E = this.res(7, this.E);
         break;
      case 188:
         this.cycle -= 8;
         this.H = this.res(7, this.H);
         break;
      case 189:
         this.cycle -= 8;
         this.L = this.res(7, this.L);
         break;
      case 190:
         this.cycle -= 12;
         this.ram.write8(this.HL(), this.res(7, this.HLi()));
         break;
      case 191:
         this.cycle -= 8;
         this.A = this.res(7, this.A);
         break;
      case 192:
         this.cycle -= 8;
         this.B = this.set(0, this.B);
         break;
      case 193:
         this.cycle -= 8;
         this.C = this.set(0, this.C);
         break;
      case 194:
         this.cycle -= 8;
         this.D = this.set(0, this.D);
         break;
      case 195:
         this.cycle -= 8;
         this.E = this.set(0, this.E);
         break;
      case 196:
         this.cycle -= 8;
         this.H = this.set(0, this.H);
         break;
      case 197:
         this.cycle -= 8;
         this.L = this.set(0, this.L);
         break;
      case 198:
         this.cycle -= 12;
         this.ram.write8(this.HL(), this.set(0, this.HLi()));
         break;
      case 199:
         this.cycle -= 8;
         this.A = this.set(0, this.A);
         break;
      case 200:
         this.cycle -= 8;
         this.B = this.set(1, this.B);
         break;
      case 201:
         this.cycle -= 8;
         this.C = this.set(1, this.C);
         break;
      case 202:
         this.cycle -= 8;
         this.D = this.set(1, this.D);
         break;
      case 203:
         this.cycle -= 8;
         this.E = this.set(1, this.E);
         break;
      case 204:
         this.cycle -= 8;
         this.H = this.set(1, this.H);
         break;
      case 205:
         this.cycle -= 8;
         this.L = this.set(1, this.L);
         break;
      case 206:
         this.cycle -= 12;
         this.ram.write8(this.HL(), this.set(1, this.HLi()));
         break;
      case 207:
         this.cycle -= 8;
         this.A = this.set(1, this.A);
         break;
      case 208:
         this.cycle -= 8;
         this.B = this.set(2, this.B);
         break;
      case 209:
         this.cycle -= 8;
         this.C = this.set(2, this.C);
         break;
      case 210:
         this.cycle -= 8;
         this.D = this.set(2, this.D);
         break;
      case 211:
         this.cycle -= 8;
         this.E = this.set(2, this.E);
         break;
      case 212:
         this.cycle -= 8;
         this.H = this.set(2, this.H);
         break;
      case 213:
         this.cycle -= 8;
         this.L = this.set(2, this.L);
         break;
      case 214:
         this.cycle -= 12;
         this.ram.write8(this.HL(), this.set(2, this.HLi()));
         break;
      case 215:
         this.cycle -= 8;
         this.A = this.set(2, this.A);
         break;
      case 216:
         this.cycle -= 8;
         this.B = this.set(3, this.B);
         break;
      case 217:
         this.cycle -= 8;
         this.C = this.set(3, this.C);
         break;
      case 218:
         this.cycle -= 8;
         this.D = this.set(3, this.D);
         break;
      case 219:
         this.cycle -= 8;
         this.E = this.set(3, this.E);
         break;
      case 220:
         this.cycle -= 8;
         this.H = this.set(3, this.H);
         break;
      case 221:
         this.cycle -= 8;
         this.L = this.set(3, this.L);
         break;
      case 222:
         this.cycle -= 12;
         this.ram.write8(this.HL(), this.set(3, this.HLi()));
         break;
      case 223:
         this.cycle -= 8;
         this.A = this.set(3, this.A);
         break;
      case 224:
         this.cycle -= 8;
         this.B = this.set(4, this.B);
         break;
      case 225:
         this.cycle -= 8;
         this.C = this.set(4, this.C);
         break;
      case 226:
         this.cycle -= 8;
         this.D = this.set(4, this.D);
         break;
      case 227:
         this.cycle -= 8;
         this.E = this.set(4, this.E);
         break;
      case 228:
         this.cycle -= 8;
         this.H = this.set(4, this.H);
         break;
      case 229:
         this.cycle -= 8;
         this.L = this.set(4, this.L);
         break;
      case 230:
         this.cycle -= 12;
         this.ram.write8(this.HL(), this.set(4, this.HLi()));
         break;
      case 231:
         this.cycle -= 8;
         this.A = this.set(4, this.A);
         break;
      case 232:
         this.cycle -= 8;
         this.B = this.set(5, this.B);
         break;
      case 233:
         this.cycle -= 8;
         this.C = this.set(5, this.C);
         break;
      case 234:
         this.cycle -= 8;
         this.D = this.set(5, this.D);
         break;
      case 235:
         this.cycle -= 8;
         this.E = this.set(5, this.E);
         break;
      case 236:
         this.cycle -= 8;
         this.H = this.set(5, this.H);
         break;
      case 237:
         this.cycle -= 8;
         this.L = this.set(5, this.L);
         break;
      case 238:
         this.cycle -= 12;
         this.ram.write8(this.HL(), this.set(5, this.HLi()));
         break;
      case 239:
         this.cycle -= 8;
         this.A = this.set(5, this.A);
         break;
      case 240:
         this.cycle -= 8;
         this.B = this.set(6, this.B);
         break;
      case 241:
         this.cycle -= 8;
         this.C = this.set(6, this.C);
         break;
      case 242:
         this.cycle -= 8;
         this.D = this.set(6, this.D);
         break;
      case 243:
         this.cycle -= 8;
         this.E = this.set(6, this.E);
         break;
      case 244:
         this.cycle -= 8;
         this.H = this.set(6, this.H);
         break;
      case 245:
         this.cycle -= 8;
         this.L = this.set(6, this.L);
         break;
      case 246:
         this.cycle -= 12;
         this.ram.write8(this.HL(), this.set(6, this.HLi()));
         break;
      case 247:
         this.cycle -= 8;
         this.A = this.set(6, this.A);
         break;
      case 248:
         this.cycle -= 8;
         this.B = this.set(7, this.B);
         break;
      case 249:
         this.cycle -= 8;
         this.C = this.set(7, this.C);
         break;
      case 250:
         this.cycle -= 8;
         this.D = this.set(7, this.D);
         break;
      case 251:
         this.cycle -= 8;
         this.E = this.set(7, this.E);
         break;
      case 252:
         this.cycle -= 8;
         this.H = this.set(7, this.H);
         break;
      case 253:
         this.cycle -= 8;
         this.L = this.set(7, this.L);
         break;
      case 254:
         this.cycle -= 12;
         this.ram.write8(this.HL(), this.set(7, this.HLi()));
         break;
      case 255:
         this.cycle -= 8;
         this.A = this.set(7, this.A);
      }

   }

   private final void prefix_ED() {
      this.instruction = (this.instruction << 8) + this.ram.read8opc(this.PC);
      this.PC = this.PC + 1 & '\uffff';
      int var1;
      int var2;
      int var3;
      int var4;
      CpuBoard var5;
      CpuBoard var6;
      switch(this.instruction & 255) {
      case 64:
         this.cycle -= 12;
         this.B = this.in(this.C);
         break;
      case 65:
         this.cycle -= 12;
         this.ram.out(this.C, this.B);
         break;
      case 66:
         this.cycle -= 15;
         this.sbcHL(this.BC());
         break;
      case 67:
         this.cycle -= 20;
         this.ram.write16(this.ram.read16arg(this.PC), this.BC());
         this.PC += 2;
         break;
      case 68:
         this.cycle -= 13;
         var1 = this.A;
         this.A = 0;
         this.A = this.subA_8(var1, this.A);
         break;
      case 69:
         this.cycle -= 14;
         this.PC = this.ram.read16arg(this.SP);
         this.SP += 2;
         this.IFF0 = this.IFF1;
         this.cycle = this.checkInterrupt(this.cycle);
         break;
      case 70:
         this.cycle -= 8;
         this.IM = 0;
         break;
      case 71:
         this.cycle -= 9;
         this.I = this.A;
         break;
      case 72:
         this.cycle -= 12;
         this.C = this.in(this.C);
         break;
      case 73:
         this.cycle -= 12;
         this.ram.out(this.C, this.C);
         break;
      case 74:
         this.cycle -= 15;
         this.adcHL(this.BC());
         break;
      case 75:
         this.cycle -= 20;
         this.B = this.ram.read8(this.ram.read16arg(this.PC) + 1);
         this.C = this.ram.read8(this.ram.read16arg(this.PC));
         this.PC += 2;
         break;
      case 76:
         this.cycle -= 13;
         var1 = this.A;
         this.A = 0;
         this.A = this.subA_8(var1, this.A);
         break;
      case 77:
         this.cycle -= 14;
         this.PC = this.ram.read16arg(this.SP);
         this.SP += 2;
         break;
      case 78:
         this.cycle -= 8;
         this.IM = 0;
         break;
      case 79:
         this.cycle -= 9;
         this.R = this.A;
         this.R2 = this.A & 128;
         break;
      case 80:
         this.cycle -= 12;
         this.D = this.in(this.C);
         break;
      case 81:
         this.cycle -= 12;
         this.ram.out(this.C, this.D);
         break;
      case 82:
         this.cycle -= 15;
         this.sbcHL(this.DE());
         break;
      case 83:
         this.cycle -= 20;
         this.ram.write16(this.ram.read16arg(this.PC), this.DE());
         this.PC += 2;
         break;
      case 84:
         this.cycle -= 13;
         var1 = this.A;
         this.A = 0;
         this.A = this.subA_8(var1, this.A);
         break;
      case 85:
         this.cycle -= 14;
         this.PC = this.ram.read16arg(this.SP);
         this.SP += 2;
         this.IFF0 = this.IFF1;
         this.cycle = this.checkInterrupt(this.cycle);
         break;
      case 86:
         this.cycle -= 8;
         this.IM = 1;
         break;
      case 87:
         this.cycle -= 9;
         this.A = this.I;
         break;
      case 88:
         this.cycle -= 12;
         this.E = this.in(this.C);
         break;
      case 89:
         this.cycle -= 12;
         this.ram.out(this.C, this.E);
         break;
      case 90:
         this.cycle -= 15;
         this.adcHL(this.DE());
         break;
      case 91:
         this.cycle -= 20;
         this.D = this.ram.read8(this.ram.read16arg(this.PC) + 1);
         this.E = this.ram.read8(this.ram.read16arg(this.PC));
         this.PC += 2;
         break;
      case 92:
         this.cycle -= 13;
         var1 = this.A;
         this.A = 0;
         this.A = this.subA_8(var1, this.A);
         break;
      case 93:
         this.cycle -= 14;
         this.PC = this.ram.read16arg(this.SP);
         this.SP += 2;
         break;
      case 94:
         this.cycle -= 8;
         this.IM = 2;
         break;
      case 95:
         this.cycle -= 9;
         this.A = this.R() & 127 | this.R2;
         var2 = this.F;
         var3 = SZ[this.A];
         byte var7;
         if(this.IFF1) {
            var7 = 2;
         } else {
            var7 = 0;
         }

         this.F = var2 & 1 | var3 | var7;
         break;
      case 96:
         this.cycle -= 12;
         this.H = this.in(this.C);
         break;
      case 97:
         this.cycle -= 12;
         this.ram.out(this.C, this.H);
         break;
      case 98:
         this.cycle -= 15;
         this.sbcHL(this.HL());
         break;
      case 99:
         this.cycle -= 20;
         this.ram.write16(this.ram.read16arg(this.PC), this.HL());
         this.PC += 2;
         break;
      case 100:
         this.cycle -= 13;
         var1 = this.A;
         this.A = 0;
         this.A = this.subA_8(var1, this.A);
         break;
      case 101:
         this.cycle -= 14;
         this.PC = this.ram.read16arg(this.SP);
         this.SP += 2;
         this.IFF0 = this.IFF1;
         this.cycle = this.checkInterrupt(this.cycle);
         break;
      case 102:
         this.cycle -= 8;
         this.IM = 0;
         break;
      case 103:
         this.cycle -= 18;
         this.rrd_A();
         break;
      case 104:
         this.cycle -= 12;
         this.L = this.in(this.C);
         break;
      case 105:
         this.cycle -= 12;
         this.ram.out(this.C, this.L);
         break;
      case 106:
         this.cycle -= 15;
         this.adcHL(this.HL());
         break;
      case 107:
         this.cycle -= 20;
         this.H = this.ram.read8(this.ram.read16arg(this.PC) + 1);
         this.L = this.ram.read8(this.ram.read16arg(this.PC));
         this.PC += 2;
         break;
      case 108:
         this.cycle -= 13;
         var1 = this.A;
         this.A = 0;
         this.A = this.subA_8(var1, this.A);
         break;
      case 109:
         this.cycle -= 14;
         this.PC = this.ram.read16arg(this.SP);
         this.SP += 2;
         break;
      case 110:
         this.cycle -= 8;
         this.IM = 0;
         break;
      case 111:
         this.cycle -= 18;
         this.rld_A();
         break;
      case 112:
      case 116:
      case 117:
      case 118:
      case 119:
      case 124:
      case 125:
      case 126:
      case 127:
      case 128:
      case 129:
      case 130:
      case 131:
      case 132:
      case 133:
      case 134:
      case 135:
      case 136:
      case 137:
      case 138:
      case 139:
      case 140:
      case 141:
      case 142:
      case 143:
      case 144:
      case 145:
      case 146:
      case 147:
      case 148:
      case 149:
      case 150:
      case 151:
      case 152:
      case 153:
      case 154:
      case 155:
      case 156:
      case 157:
      case 158:
      case 159:
      case 164:
      case 165:
      case 166:
      case 167:
      case 169:
      case 171:
      case 172:
      case 173:
      case 174:
      case 175:
      case 180:
      case 181:
      case 182:
      case 183:
      default:
         this.error(this.instruction, this.PPC);
         break;
      case 113:
         this.cycle -= 12;
         this.ram.out(this.C, 0);
         break;
      case 114:
         this.cycle -= 15;
         this.sbcHL(this.SP);
         break;
      case 115:
         this.cycle -= 20;
         this.ram.write16(this.ram.read16arg(this.PC), this.SP);
         this.PC += 2;
         break;
      case 120:
         this.cycle -= 12;
         this.A = this.in(this.C);
         break;
      case 121:
         this.cycle -= 11;
         this.ram.out(this.C, this.A);
         break;
      case 122:
         this.cycle -= 15;
         this.adcHL(this.SP);
         break;
      case 123:
         this.cycle -= 20;
         this.SP = this.ram.read16(this.ram.read16arg(this.PC));
         this.PC += 2;
         break;
      case 160:
         this.cycle -= 21;
         var4 = this.DE();
         var3 = this.HL();
         var2 = this.BC();
         var6 = this.ram;
         var5 = this.ram;
         var1 = var3 + 1;
         var6.write8(var4, var5.read8(var3));
         var3 = var4 + 1 & '\uffff';
         var2 = var2 - 1 & '\uffff';
         this.E = var3 & 255;
         this.D = var3 >> 8;
         this.L = var1 & 255;
         this.H = var1 >> 8;
         this.C = var2 & 255;
         this.B = var2 >> 8;
         this.F &= 193;
         if(var2 != 0) {
            this.F |= 4;
         }
         break;
      case 161:
         this.cycle -= 21;
         var3 = this.HL();
         var4 = this.BC();
         var1 = this.ram.read8arg(var3);
         var2 = this.A - var1 & 255;
         ++var3;
         --var4;
         this.F = this.F & 1 | SZ[var2] & -41 | (this.A ^ var1 ^ var2) & 16 | 2;
         var1 = var2;
         if((this.F & 16) != 0) {
            var1 = var2 - 1 & 255;
         }

         if((var1 & 2) != 0) {
            this.F |= 32;
         }

         if((var1 & 8) != 0) {
            this.F |= 8;
         }

         if(var4 != 0) {
            this.F |= 4;
         }

         this.H = var3 >> 8;
         this.L = var3 & 255;
         this.B = var4 >> 8;
         this.C = var4 & 255;
         break;
      case 162:
         this.cycle -= 16;
         this.ini();
         break;
      case 163:
         this.cycle -= 16;
         var2 = this.HL();
         var5 = this.ram;
         var1 = var2 + 1;
         var2 = var5.read8(var2);
         this.B = this.B - 1 & 255;
         this.ram.out(this.C, var2);
         this.H = var1 >> 8;
         this.L = var1 & 255;
         this.F = SZ[this.B];
         var1 = this.L + var2;
         if((var2 & 128) != 0) {
            this.F |= 2;
         }

         if((var1 & 256) != 0) {
            this.F |= 17;
         }

         this.F |= SZP[var1 & 7 ^ this.B] & 4;
         break;
      case 168:
         this.cycle -= 21;
         var3 = this.DE();
         var4 = this.HL();
         var2 = this.BC();
         var5 = this.ram;
         var6 = this.ram;
         var1 = var4 - 1;
         var5.write8(var3, var6.read8(var4));
         var3 = var3 - 1 & '\uffff';
         var2 = var2 - 1 & '\uffff';
         this.E = var3 & 255;
         this.D = var3 >> 8;
         this.L = var1 & 255;
         this.H = var1 >> 8;
         this.C = var2 & 255;
         this.B = var2 >> 8;
         this.F &= 193;
         if(var2 != 0) {
            this.F |= 4;
         }
         break;
      case 170:
         this.cycle -= 16;
         var1 = this.HL();
         this.B = this.B - 1 & 255;
         this.ram.write8(var1, this.in(this.C));
         var1 = var1 - 1 & '\uffff';
         this.H = var1 >> 8;
         this.L = var1 & 255;
         if(this.B == 0) {
            this.F |= 64;
         } else {
            this.F &= -65;
         }

         this.F |= 2;
         break;
      case 176:
         this.cycle -= this.BC() * 16 + 5;
         var2 = this.DE();
         var1 = this.HL();

         for(var3 = this.BC(); var3 > 0; --var3) {
            this.ram.write8(var2, this.ram.read8(var1));
            var2 = var2 + 1 & '\uffff';
            var1 = var1 + 1 & '\uffff';
         }

         this.E = var2 & 255;
         this.D = var2 >> 8;
         this.C = 0;
         this.B = 0;
         this.L = var1 & 255;
         this.H = var1 >> 8;
         this.F &= 233;
         break;
      case 177:
         var1 = this.F;
         var3 = this.BC();
         var2 = this.HL();
         this.cycle -= 4;
         this.cpA_8(this.ram.read8(var2), this.A);
         ++var2;
         --var3;
         this.H = var2 >> 8;
         this.L = var2 & 255;
         this.B = var3 >> 8;
         this.C = var3 & 255;
         this.F = this.F & 254 | var1 & 1;
         if(var3 != 0 && (this.F & 64) == 0) {
            this.PC -= 2;
            this.F |= 4;
            this.cycle -= 21;
         } else {
            this.F &= 251;
            this.cycle -= 16;
         }
         break;
      case 178:
         this.cycle -= this.B * 16 + 5;

         while(this.B > 0) {
            var1 = this.HL();
            this.B = this.B - 1 & 255;
            this.ram.write8(var1, this.in(this.C));
            var1 = var1 + 1 & '\uffff';
            this.H = var1 >> 8;
            this.L = var1 & 255;
            if(this.B == 0) {
               this.F |= 64;
            } else {
               this.F &= -65;
            }
         }

         this.F |= 2;
         break;
      case 179:
         this.cycle -= this.B * 16 + 5;

         while(this.B > 0) {
            var1 = this.HL();
            var5 = this.ram;
            var2 = this.C;
            var6 = this.ram;
            var3 = var1 + 1;
            var5.out(var2, var6.read8(var1));
            this.B = this.B - 1 & 255;
            this.H = var3 >> 8;
            this.L = var3 & 255;
            if(this.B == 0) {
               this.F |= 64;
            } else {
               this.F &= -65;
            }
         }

         this.F |= 2;
         break;
      case 184:
         this.cycle -= this.BC() * 16 + 5;
         var2 = this.DE();
         var1 = this.HL();

         for(var3 = this.BC(); var3 > 0; --var2) {
            this.ram.write8(var2, this.ram.read8(var1));
            --var3;
            --var1;
         }

         this.E = var2 & 255;
         this.D = var2 >> 8;
         this.C = 0;
         this.B = 0;
         this.L = var1 & 255;
         this.H = var1 >> 8;
         this.F &= 233;
         break;
      case 185:
         var1 = this.F;
         var3 = this.BC();
         var2 = this.HL();
         this.cycle -= 4;
         this.cpA_8(this.ram.read8(var2), this.A);
         --var2;
         --var3;
         this.H = var2 >> 8;
         this.L = var2 & 255;
         this.B = var3 >> 8;
         this.C = var3 & 255;
         this.F = this.F & 254 | var1 & 1;
         if(var3 != 0 && (this.F & 64) == 0) {
            this.PC -= 2;
            this.F |= 4;
            this.cycle -= 21;
         } else {
            this.F &= 251;
            this.cycle -= 16;
         }
      }

   }

   private final void push(int var1) {
      this.SP = this.SP - 2 & '\uffff';
      this.ram.write16fast(this.SP, var1);
   }

   private final void push_AF() {
      this.cycle -= 11;
      this.push(this.AF());
   }

   private final void push_BC() {
      this.cycle -= 11;
      this.push(this.BC());
   }

   private final void push_DE() {
      this.cycle -= 11;
      this.push(this.DE());
   }

   private final void push_HL() {
      this.cycle -= 11;
      this.push(this.HL());
   }

   private final int res(int var1, int var2) {
      return var2 & bitRes[var1];
   }

   private final void ret() {
      this.cycle -= 10;
      this.PC = this.pop();
   }

   private final void ret_C() {
      if((this.F & 1) != 0) {
         this.cycle -= 11;
         this.PC = this.pop();
      } else {
         this.cycle -= 5;
      }

   }

   private final void ret_M() {
      if((this.F & 128) != 0) {
         this.cycle -= 11;
         this.PC = this.pop();
      } else {
         this.cycle -= 5;
      }

   }

   private final void ret_NC() {
      if((this.F & 1) == 0) {
         this.cycle -= 11;
         this.PC = this.pop();
      } else {
         this.cycle -= 5;
      }

   }

   private final void ret_NZ() {
      if((this.F & 64) == 0) {
         this.cycle -= 11;
         this.PC = this.pop();
      } else {
         this.cycle -= 5;
      }

   }

   private final void ret_P() {
      if((this.F & 128) == 0) {
         this.cycle -= 11;
         this.PC = this.pop();
      } else {
         this.cycle -= 5;
      }

   }

   private final void ret_PE() {
      if((this.F & 4) != 0) {
         this.cycle -= 11;
         this.PC = this.pop();
      } else {
         this.cycle -= 5;
      }

   }

   private final void ret_PO() {
      if((this.F & 4) == 0) {
         this.cycle -= 11;
         this.PC = this.ram.read16(this.SP);
         this.SP += 2;
      } else {
         this.cycle -= 5;
      }

   }

   private final void ret_Z() {
      if((this.F & 64) != 0) {
         this.cycle -= 11;
         this.PC = this.pop();
      } else {
         this.cycle -= 5;
      }

   }

   private final int rl(int var1) {
      int var2 = (var1 << 1 | this.F & 1) & 255;
      this.F = SZP[var2] | (var1 & 128) >> 7;
      return var2;
   }

   private final void rl_A() {
      int var2 = (this.A << 1 | this.F & 1) & 255;
      byte var1;
      if((this.A & 128) != 0) {
         var1 = 1;
      } else {
         var1 = 0;
      }

      this.F = this.F & 196 | var1 | var2 & 40;
      this.A = var2;
   }

   private final void rla() {
      this.cycle -= 4;
      this.rl_A();
   }

   private final int rlc(int var1) {
      int var2 = (var1 << 1 | var1 >> 7) & 255;
      this.F = SZP[var2] | (var1 & 128) >> 7;
      return var2;
   }

   private final void rlc_A() {
      this.A = (this.A << 1 | this.A >> 7) & 255;
      this.F = this.F & 196 | this.A & 41;
   }

   private final void rlca() {
      this.cycle -= 4;
      this.rlc_A();
   }

   private final void rld_A() {
      int var1 = this.ram.read8(this.HL());
      this.ram.write8(this.HL(), (var1 << 4 | this.A & 15) & 255);
      this.A = this.A & 240 | var1 >> 4;
      this.F = this.F & 1 | SZP[this.A];
   }

   private final int rr(int var1) {
      int var2 = (var1 >> 1 | this.F << 7) & 255;
      this.F = SZP[var2] | var1 & 1;
      return var2;
   }

   private final void rr_A() {
      int var2 = (this.A >> 1 | this.F << 7) & 255;
      byte var1;
      if((this.A & 1) != 0) {
         var1 = 1;
      } else {
         var1 = 0;
      }

      this.F = this.F & 196 | var1 | var2 & 40;
      this.A = var2;
   }

   private final void rra() {
      this.cycle -= 4;
      this.rr_A();
   }

   private final int rrc(int var1) {
      byte var2;
      if((var1 & 1) != 0) {
         var2 = 1;
      } else {
         var2 = 0;
      }

      var1 = (var1 >> 1 | var1 << 7) & 255;
      this.F = SZP[var1] | var2;
      return var1;
   }

   private final void rrc_A() {
      this.F = this.F & 196 | this.A & 41;
      this.A = (this.A >> 1 | this.A << 7) & 255;
   }

   private final void rrca() {
      this.cycle -= 4;
      this.rrc_A();
   }

   private final void rrd_A() {
      int var1 = this.ram.read8(this.HL());
      this.ram.write8(this.HL(), (var1 >> 4 | this.A << 4) & 255);
      this.A = this.A & 240 | var1 & 15;
      this.F = this.F & 1 | SZP[this.A];
   }

   private final void rst(int var1) {
      this.cycle -= 11;
      this.push(this.PC);
      this.PC = var1;
   }

   private final void sbcA_8(int var1) {
      int var2 = this.F & 1;
      var1 = this.A - var1 - var2 & 255;
      this.F = SZHVC_sub[var2 << 16 | this.A << 8 | var1];
      this.A = var1;
   }

   private final void sbcHL(int var1) {
      int var3 = this.HL();
      int var4 = var3 - var1 - (this.F & 1);
      byte var2;
      if(('\uffff' & var4) != 0) {
         var2 = 0;
      } else {
         var2 = 64;
      }

      this.F = (var3 ^ var4 ^ var1) >> 8 & 16 | 2 | var4 >> 16 & 1 | var4 >> 8 & 128 | var2 | ((var1 ^ var3) & (var3 ^ var4) & '耀') >> 13;
      this.H = var4 >> 8 & 255;
      this.L = var4 & 255;
   }

   private final void sbc_A(int var1, int var2) {
      this.cycle -= var2;
      this.sbcA_8(var1);
   }

   private final void sbc_A_n() {
      this.cycle -= 7;
      CpuBoard var2 = this.ram;
      int var1 = this.PC;
      this.PC = var1 + 1;
      this.sbcA_8(var2.read8arg(var1));
   }

   private final void scf() {
      this.cycle -= 4;
      this.F = this.F & 196 | 1 | this.A & 40;
   }

   private final int set(int var1, int var2) {
      return var2 | bitSet[var1];
   }

   private final int sign(int var1) {
      return var1 - ((var1 & 128) << 1);
   }

   private final int sla(int var1) {
      int var2 = var1 << 1 & 255;
      this.F = SZP[var2] | (var1 & 128) >> 7;
      return var2;
   }

   private final int sll(int var1) {
      int var2 = (var1 << 1 | 1) & 255;
      this.F = SZP[var2] | (var1 & 128) >> 7;
      return var2;
   }

   private final int sra(int var1) {
      int var2 = var1 >> 1 | var1 & 128;
      this.F = SZP[var2] | var1 & 1;
      return var2;
   }

   private final int srl(int var1) {
      int var2 = var1 >> 1 & 255;
      this.F = SZP[var2] | var1 & 1;
      return var2;
   }

   private final int subA_8(int var1, int var2) {
      var1 = var2 - var1 & 255;
      this.F = SZHVC_sub[var2 << 8 | var1];
      return var1;
   }

   private final void sub_A(int var1, int var2) {
      this.cycle -= var2;
      this.A = this.subA_8(var1, this.A);
   }

   private final void sub_A_n() {
      this.cycle -= 7;
      CpuBoard var2 = this.ram;
      int var1 = this.PC;
      this.PC = var1 + 1;
      this.A = this.subA_8(var2.read8arg(var1), this.A);
   }

   private final int xorA(int var1, int var2) {
      var1 ^= var2;
      this.F = SZP[var1];
      return var1;
   }

   private final void xor_A(int var1, int var2) {
      this.cycle -= var2;
      this.A = this.xorA(var1, this.A);
   }

   private final void xor_n() {
      this.cycle -= 7;
      CpuBoard var2 = this.ram;
      int var1 = this.PC;
      this.PC = var1 + 1;
      this.A = this.xorA(var2.read8arg(var1), this.A);
   }

   public final void exec(int var1) {
      this.cycle += var1;
      this.cycle = this.checkInterrupt(this.cycle);
      if(DEBUG) {
         this.startSlice = true;
      }

      while(this.cycle > 0) {
         this.instruction = this.ram.read8opc(this.PC);
         this.PPC = this.PC;
         this.PC = this.PC + 1 & '\uffff';
         ++this.R;
         switch(this.instruction) {
         case 0:
            this.nop();
            break;
         case 1:
            this.ld_BC_nn();
            break;
         case 2:
            this.ld_BCi_A();
            break;
         case 3:
            this.inc_BC();
            break;
         case 4:
            this.inc_B();
            break;
         case 5:
            this.dec_B();
            break;
         case 6:
            this.ld_B_n();
            break;
         case 7:
            this.rlca();
            break;
         case 8:
            this.ex_AF_AF();
            break;
         case 9:
            this.add_HL_BC();
            break;
         case 10:
            this.ld_A_BCi();
            break;
         case 11:
            this.dec_BC();
            break;
         case 12:
            this.inc_C();
            break;
         case 13:
            this.dec_C();
            break;
         case 14:
            this.ld_C_n();
            break;
         case 15:
            this.rrca();
            break;
         case 16:
            this.djnz_n();
            break;
         case 17:
            this.ld_DE_nn();
            break;
         case 18:
            this.ld_DEi_A();
            break;
         case 19:
            this.inc_DE();
            break;
         case 20:
            this.inc_D();
            break;
         case 21:
            this.dec_D();
            break;
         case 22:
            this.ld_D_n();
            break;
         case 23:
            this.rla();
            break;
         case 24:
            this.jr_e();
            break;
         case 25:
            this.add_HL_DE();
            break;
         case 26:
            this.ld_A_DEi();
            break;
         case 27:
            this.dec_DE();
            break;
         case 28:
            this.inc_E();
            break;
         case 29:
            this.dec_E();
            break;
         case 30:
            this.ld_E_n();
            break;
         case 31:
            this.rra();
            break;
         case 32:
            this.jr_NZ_e();
            break;
         case 33:
            this.ld_HL_nn();
            break;
         case 34:
            this.ld_ni_HL();
            break;
         case 35:
            this.inc_HL();
            break;
         case 36:
            this.inc_H();
            break;
         case 37:
            this.dec_H();
            break;
         case 38:
            this.ld_H_n();
            break;
         case 39:
            this.daa();
            break;
         case 40:
            this.jr_Z_e();
            break;
         case 41:
            this.add_HL_HL();
            break;
         case 42:
            this.ld_HL_ni();
            break;
         case 43:
            this.dec_HL();
            break;
         case 44:
            this.inc_L();
            break;
         case 45:
            this.dec_L();
            break;
         case 46:
            this.ld_L_n();
            break;
         case 47:
            this.cpl();
            break;
         case 48:
            this.jr_NC_e();
            break;
         case 49:
            this.ld_SP_nn();
            break;
         case 50:
            this.ld_ni_A();
            break;
         case 51:
            this.inc_SP();
            break;
         case 52:
            this.inc_HLi();
            break;
         case 53:
            this.dec_HLi();
            break;
         case 54:
            this.ld_HLi_n();
            break;
         case 55:
            this.scf();
            break;
         case 56:
            this.jr_C_e();
            break;
         case 57:
            this.add_HL_SP();
            break;
         case 58:
            this.ld_A_ni();
            break;
         case 59:
            this.dec_SP();
            break;
         case 60:
            this.inc_A();
            break;
         case 61:
            this.dec_A();
            break;
         case 62:
            this.ld_A_n();
            break;
         case 63:
            this.ccf();
            break;
         case 64:
            this.nop();
            break;
         case 65:
            this.ld_B(this.C, 4);
            break;
         case 66:
            this.ld_B(this.D, 4);
            break;
         case 67:
            this.ld_B(this.E, 4);
            break;
         case 68:
            this.ld_B(this.H, 4);
            break;
         case 69:
            this.ld_B(this.L, 4);
            break;
         case 70:
            this.ld_B(this.HLi(), 7);
            break;
         case 71:
            this.ld_B(this.A, 4);
            break;
         case 72:
            this.ld_C(this.B, 4);
            break;
         case 73:
            this.nop();
            break;
         case 74:
            this.ld_C(this.D, 4);
            break;
         case 75:
            this.ld_C(this.E, 4);
            break;
         case 76:
            this.ld_C(this.H, 4);
            break;
         case 77:
            this.ld_C(this.L, 4);
            break;
         case 78:
            this.ld_C(this.HLi(), 7);
            break;
         case 79:
            this.ld_C(this.A, 4);
            break;
         case 80:
            this.ld_D(this.B, 4);
            break;
         case 81:
            this.ld_D(this.C, 4);
            break;
         case 82:
            this.nop();
            break;
         case 83:
            this.ld_D(this.E, 4);
            break;
         case 84:
            this.ld_D(this.H, 4);
            break;
         case 85:
            this.ld_D(this.L, 4);
            break;
         case 86:
            this.ld_D(this.HLi(), 7);
            break;
         case 87:
            this.ld_D(this.A, 4);
            break;
         case 88:
            this.ld_E(this.B, 4);
            break;
         case 89:
            this.ld_E(this.C, 4);
            break;
         case 90:
            this.ld_E(this.D, 4);
            break;
         case 91:
            this.nop();
            break;
         case 92:
            this.ld_E(this.H, 4);
            break;
         case 93:
            this.ld_E(this.L, 4);
            break;
         case 94:
            this.ld_E(this.HLi(), 7);
            break;
         case 95:
            this.ld_E(this.A, 4);
            break;
         case 96:
            this.ld_H(this.B, 4);
            break;
         case 97:
            this.ld_H(this.C, 4);
            break;
         case 98:
            this.ld_H(this.D, 4);
            break;
         case 99:
            this.ld_H(this.E, 4);
            break;
         case 100:
            this.nop();
            break;
         case 101:
            this.ld_H(this.L, 4);
            break;
         case 102:
            this.ld_H(this.HLi(), 7);
            break;
         case 103:
            this.ld_H(this.A, 4);
            break;
         case 104:
            this.ld_L(this.B, 4);
            break;
         case 105:
            this.ld_L(this.C, 4);
            break;
         case 106:
            this.ld_L(this.D, 4);
            break;
         case 107:
            this.ld_L(this.E, 4);
            break;
         case 108:
            this.ld_L(this.H, 4);
            break;
         case 109:
            this.nop();
            break;
         case 110:
            this.ld_L(this.HLi(), 7);
            break;
         case 111:
            this.ld_L(this.A, 4);
            break;
         case 112:
            this.ld_HLi(this.B, 7);
            break;
         case 113:
            this.ld_HLi(this.C, 7);
            break;
         case 114:
            this.ld_HLi(this.D, 7);
            break;
         case 115:
            this.ld_HLi(this.E, 7);
            break;
         case 116:
            this.ld_HLi(this.H, 7);
            break;
         case 117:
            this.ld_HLi(this.L, 7);
            break;
         case 118:
            this.halt();
            break;
         case 119:
            this.ld_HLi(this.A, 7);
            break;
         case 120:
            this.ld_A(this.B, 4);
            break;
         case 121:
            this.ld_A(this.C, 4);
            break;
         case 122:
            this.ld_A(this.D, 4);
            break;
         case 123:
            this.ld_A(this.E, 4);
            break;
         case 124:
            this.ld_A(this.H, 4);
            break;
         case 125:
            this.ld_A(this.L, 4);
            break;
         case 126:
            this.ld_A(this.HLi(), 7);
            break;
         case 127:
            this.nop();
            break;
         case 128:
            this.add_A(this.B, 4);
            break;
         case 129:
            this.add_A(this.C, 4);
            break;
         case 130:
            this.add_A(this.D, 4);
            break;
         case 131:
            this.add_A(this.E, 4);
            break;
         case 132:
            this.add_A(this.H, 4);
            break;
         case 133:
            this.add_A(this.L, 4);
            break;
         case 134:
            this.add_A(this.HLi(), 7);
            break;
         case 135:
            this.add_A(this.A, 4);
            break;
         case 136:
            this.adc_A(this.B, 4);
            break;
         case 137:
            this.adc_A(this.C, 4);
            break;
         case 138:
            this.adc_A(this.D, 4);
            break;
         case 139:
            this.adc_A(this.E, 4);
            break;
         case 140:
            this.adc_A(this.H, 4);
            break;
         case 141:
            this.adc_A(this.L, 4);
            break;
         case 142:
            this.adc_A(this.HLi(), 7);
            break;
         case 143:
            this.adc_A(this.A, 4);
            break;
         case 144:
            this.sub_A(this.B, 4);
            break;
         case 145:
            this.sub_A(this.C, 4);
            break;
         case 146:
            this.sub_A(this.D, 4);
            break;
         case 147:
            this.sub_A(this.E, 4);
            break;
         case 148:
            this.sub_A(this.H, 4);
            break;
         case 149:
            this.sub_A(this.L, 4);
            break;
         case 150:
            this.sub_A(this.HLi(), 7);
            break;
         case 151:
            this.sub_A(this.A, 4);
            break;
         case 152:
            this.sbc_A(this.B, 4);
            break;
         case 153:
            this.sbc_A(this.C, 4);
            break;
         case 154:
            this.sbc_A(this.D, 4);
            break;
         case 155:
            this.sbc_A(this.E, 4);
            break;
         case 156:
            this.sbc_A(this.H, 4);
            break;
         case 157:
            this.sbc_A(this.L, 4);
            break;
         case 158:
            this.sbc_A(this.HLi(), 7);
            break;
         case 159:
            this.sbc_A(this.A, 4);
            break;
         case 160:
            this.and_A(this.B, 4);
            break;
         case 161:
            this.and_A(this.C, 4);
            break;
         case 162:
            this.and_A(this.D, 4);
            break;
         case 163:
            this.and_A(this.E, 4);
            break;
         case 164:
            this.and_A(this.H, 4);
            break;
         case 165:
            this.and_A(this.L, 4);
            break;
         case 166:
            this.and_A(this.HLi(), 7);
            break;
         case 167:
            this.and_A(this.A, 4);
            break;
         case 168:
            this.xor_A(this.B, 4);
            break;
         case 169:
            this.xor_A(this.C, 4);
            break;
         case 170:
            this.xor_A(this.D, 4);
            break;
         case 171:
            this.xor_A(this.E, 4);
            break;
         case 172:
            this.xor_A(this.H, 4);
            break;
         case 173:
            this.xor_A(this.L, 4);
            break;
         case 174:
            this.xor_A(this.HLi(), 7);
            break;
         case 175:
            this.xor_A(this.A, 4);
            break;
         case 176:
            this.or_A(this.B, 4);
            break;
         case 177:
            this.or_A(this.C, 4);
            break;
         case 178:
            this.or_A(this.D, 4);
            break;
         case 179:
            this.or_A(this.E, 4);
            break;
         case 180:
            this.or_A(this.H, 4);
            break;
         case 181:
            this.or_A(this.L, 4);
            break;
         case 182:
            this.or_A(this.HLi(), 7);
            break;
         case 183:
            this.or_A(this.A, 4);
            break;
         case 184:
            this.cp_A(this.B, 4);
            break;
         case 185:
            this.cp_A(this.C, 4);
            break;
         case 186:
            this.cp_A(this.D, 4);
            break;
         case 187:
            this.cp_A(this.E, 4);
            break;
         case 188:
            this.cp_A(this.H, 4);
            break;
         case 189:
            this.cp_A(this.L, 4);
            break;
         case 190:
            this.cp_A(this.HLi(), 7);
            break;
         case 191:
            this.cp_A(this.A, 4);
            break;
         case 192:
            this.ret_NZ();
            break;
         case 193:
            this.pop_BC();
            break;
         case 194:
            this.jp_NZ_nn();
            break;
         case 195:
            this.jp_nn();
            break;
         case 196:
            this.call_NZ_nn();
            break;
         case 197:
            this.push_BC();
            break;
         case 198:
            this.add_A_n();
            break;
         case 199:
            this.rst(0);
            break;
         case 200:
            this.ret_Z();
            break;
         case 201:
            this.ret();
            break;
         case 202:
            this.jp_Z_nn();
            break;
         case 203:
            this.prefix_BC();
            break;
         case 204:
            this.call_Z_nn();
            break;
         case 205:
            this.call_nn();
            break;
         case 206:
            this.adc_A_n();
            break;
         case 207:
            this.rst(8);
            break;
         case 208:
            this.ret_NC();
            break;
         case 209:
            this.pop_DE();
            break;
         case 210:
            this.jp_NC_nn();
            break;
         case 211:
            this.out_n_A();
            break;
         case 212:
            this.call_NC_nn();
            break;
         case 213:
            this.push_DE();
            break;
         case 214:
            this.sub_A_n();
            break;
         case 215:
            this.rst(16);
            break;
         case 216:
            this.ret_C();
            break;
         case 217:
            this.exx();
            break;
         case 218:
            this.jp_C_nn();
            break;
         case 219:
            this.in_A_n();
            break;
         case 220:
            this.call_C_nn();
            break;
         case 221:
            this.IX = this.execXY(this.IX);
            break;
         case 222:
            this.sbc_A_n();
            break;
         case 223:
            this.rst(24);
            break;
         case 224:
            this.ret_PO();
            break;
         case 225:
            this.pop_HL();
            break;
         case 226:
            this.jp_PO_nn();
            break;
         case 227:
            this.ex_SPi_HL();
            break;
         case 228:
            this.call_PO_nn();
            break;
         case 229:
            this.push_HL();
            break;
         case 230:
            this.and_A_n();
            break;
         case 231:
            this.rst(32);
            break;
         case 232:
            this.ret_PE();
            break;
         case 233:
            this.jp_HLi();
            break;
         case 234:
            this.jp_PE_nn();
            break;
         case 235:
            this.ex_DE_HL();
            break;
         case 236:
            this.call_PE_nn();
            break;
         case 237:
            this.prefix_ED();
            break;
         case 238:
            this.xor_n();
            break;
         case 239:
            this.rst(40);
            break;
         case 240:
            this.ret_P();
            break;
         case 241:
            this.pop_AF();
            break;
         case 242:
            this.jp_P_nn();
            break;
         case 243:
            this.di();
            break;
         case 244:
            this.call_P_nn();
            break;
         case 245:
            this.push_AF();
            break;
         case 246:
            this.or_n();
            break;
         case 247:
            this.rst(48);
            break;
         case 248:
            this.ret_M();
            break;
         case 249:
            this.ld_SP_HL();
            break;
         case 250:
            this.jp_M_nn();
            break;
         case 251:
            this.ei();
            break;
         case 252:
            this.call_M_nn();
            break;
         case 253:
            this.IY = this.execXY(this.IY);
            break;
         case 254:
            this.cp_n();
            break;
         case 255:
            this.rst(56);
         }

         if(DEBUG) {
            this.debug(this.instruction, this.PPC, this.A, this.F, this.B, this.C, this.D, this.E, this.H, this.L, this.SP, this.IX, this.IY, this.I, this.cycle);
         }
      }

   }

   public final int getCyclesLeft() {
      return this.cycle;
   }

   public final int getDebug() {
      return this.debugLevel;
   }

   public final long getInstruction() {
      return (long)this.instruction;
   }

   public final int getPC() {
      return this.PC;
   }

   public String getTag() {
      return this.tag;
   }

   String hex(int var1, int var2) {
      String var3;
      for(var3 = Integer.toHexString(var1); var3.length() < var2; var3 = "0" + var3) {
         ;
      }

      return var3;
   }

   public boolean init(CpuBoard var1, int var2) {
      this.ram = var1;
      return true;
   }

   public final void interrupt(int var1, boolean var2) {
      if(var1 == 1) {
         this.nmi();
      } else if(var1 == 0) {
         this.irq();
      }

   }

   public final void irq() {
      this.IRQ = true;
   }

   public final void nmi() {
      this.NMI = true;
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
      this.IX = '\uffff';
      this.IY = '\uffff';
      this.cycle = 0;
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
   }

   public final void setProperty(int var1, int var2) {
      if(var1 == 0) {
         this.I_Vector = var2;
      }

   }

   public void setTag(String var1) {
      this.tag = var1;
   }

   public void write(int var1, int var2) {
      this.I_Vector = var2;
   }
}
