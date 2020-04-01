package jef.cpu.z80;

import jef.cpu.Cpu;
import jef.cpuboard.CpuBoard;
import jef.util.ByteArray;
import jef.util.Persistent;

public class Z80 implements Cpu, Persistent {
   private static final int ADDSUBTRACT_MASK = 2;
   public static final int BRKPT_ADDR = -1;
   private static final int CARRY_MASK = 1;
   private static final int FIVE_MASK = 32;
   private static final int HALFCARRY_MASK = 16;
   private static final int ONE_MASK = 1;
   private static final int OVERFLOW_MASK = 4;
   private static final int PARITY_MASK = 4;
   private static final int SIGN_MASK = 128;
   public static final int STATE_A = 0;
   public static final int STATE_AF1 = 8;
   public static final int STATE_B = 2;
   public static final int STATE_BC1 = 10;
   public static final int STATE_C = 3;
   public static final int STATE_D = 4;
   public static final int STATE_DE1 = 12;
   public static final int STATE_E = 5;
   public static final int STATE_F = 1;
   public static final int STATE_H = 6;
   public static final int STATE_HL1 = 14;
   public static final int STATE_I = 27;
   public static final int STATE_IFF1 = 29;
   public static final int STATE_IFF2 = 30;
   public static final int STATE_IM = 28;
   public static final int STATE_IRQ = 32;
   public static final int STATE_IX = 16;
   public static final int STATE_IY = 18;
   public static final int STATE_L = 7;
   public static final int STATE_NMI = 33;
   public static final int STATE_PC = 24;
   public static final int STATE_R = 26;
   public static final int STATE_SP = 22;
   public static final int STATE_X = 31;
   public static final int STATE_XX = 20;
   private static final int THREE_MASK = 8;
   public static final boolean TRACE = false;
   private static final boolean TRACE_BUSY_LOOP = false;
   private static final int ZERO_MASK = 64;
   private static boolean[] m_halfcarryTable;
   private static boolean[] m_overflowTable;
   private static final boolean[] m_parityTable;
   private static boolean[] m_subhalfcarryTable;
   private static boolean[] m_suboverflowTable;
   private int[] ADDRESS_STACK = new int[8];
   public boolean IRQ_SELF_ACK = true;
   private int addressStackPointer = 0;
   private boolean irq;
   private boolean m_3F;
   private boolean m_5F;
   public int m_a8;
   private boolean m_addsubtractF;
   public int m_af16alt;
   public int m_b8;
   public int m_bc16alt;
   public int m_c8;
   private boolean m_carryF;
   public int m_d8;
   public int m_de16alt;
   public int m_e8;
   public int m_f8;
   public int m_h8;
   private boolean m_halfcarryF;
   public int m_hl16alt;
   public int m_i8;
   public int m_iff1a;
   public int m_iff1b;
   public int m_im2;
   public int m_ix16;
   public int m_iy16;
   public int m_l8;
   private CpuBoard m_memory;
   private boolean m_parityoverflowF;
   protected volatile boolean m_pause;
   public int m_pc16;
   public int m_r8;
   private boolean m_signF;
   public int m_sp16;
   protected volatile boolean m_stop;
   private int m_tstates;
   private int m_x8;
   public int m_xx16;
   private boolean m_zeroF;
   private boolean nmi;
   Patch patch;
   public String tag = this.toString();
   public boolean tracing = false;

   static {
      boolean[] var0 = new boolean[256];
      var0[0] = true;
      var0[3] = true;
      var0[5] = true;
      var0[6] = true;
      var0[9] = true;
      var0[10] = true;
      var0[12] = true;
      var0[15] = true;
      var0[17] = true;
      var0[18] = true;
      var0[20] = true;
      var0[23] = true;
      var0[24] = true;
      var0[27] = true;
      var0[29] = true;
      var0[30] = true;
      var0[33] = true;
      var0[34] = true;
      var0[36] = true;
      var0[39] = true;
      var0[40] = true;
      var0[43] = true;
      var0[45] = true;
      var0[46] = true;
      var0[48] = true;
      var0[51] = true;
      var0[53] = true;
      var0[54] = true;
      var0[57] = true;
      var0[58] = true;
      var0[60] = true;
      var0[63] = true;
      var0[65] = true;
      var0[66] = true;
      var0[68] = true;
      var0[71] = true;
      var0[72] = true;
      var0[75] = true;
      var0[77] = true;
      var0[78] = true;
      var0[80] = true;
      var0[83] = true;
      var0[85] = true;
      var0[86] = true;
      var0[89] = true;
      var0[90] = true;
      var0[92] = true;
      var0[95] = true;
      var0[96] = true;
      var0[99] = true;
      var0[101] = true;
      var0[102] = true;
      var0[105] = true;
      var0[106] = true;
      var0[108] = true;
      var0[111] = true;
      var0[113] = true;
      var0[114] = true;
      var0[116] = true;
      var0[119] = true;
      var0[120] = true;
      var0[123] = true;
      var0[125] = true;
      var0[126] = true;
      var0[129] = true;
      var0[130] = true;
      var0[132] = true;
      var0[135] = true;
      var0[136] = true;
      var0[139] = true;
      var0[141] = true;
      var0[142] = true;
      var0[144] = true;
      var0[147] = true;
      var0[149] = true;
      var0[150] = true;
      var0[153] = true;
      var0[154] = true;
      var0[156] = true;
      var0[159] = true;
      var0[160] = true;
      var0[163] = true;
      var0[165] = true;
      var0[166] = true;
      var0[169] = true;
      var0[170] = true;
      var0[172] = true;
      var0[175] = true;
      var0[177] = true;
      var0[178] = true;
      var0[180] = true;
      var0[183] = true;
      var0[184] = true;
      var0[187] = true;
      var0[189] = true;
      var0[190] = true;
      var0[192] = true;
      var0[195] = true;
      var0[197] = true;
      var0[198] = true;
      var0[201] = true;
      var0[202] = true;
      var0[204] = true;
      var0[207] = true;
      var0[209] = true;
      var0[210] = true;
      var0[212] = true;
      var0[215] = true;
      var0[216] = true;
      var0[219] = true;
      var0[221] = true;
      var0[222] = true;
      var0[225] = true;
      var0[226] = true;
      var0[228] = true;
      var0[231] = true;
      var0[232] = true;
      var0[235] = true;
      var0[237] = true;
      var0[238] = true;
      var0[240] = true;
      var0[243] = true;
      var0[245] = true;
      var0[246] = true;
      var0[249] = true;
      var0[250] = true;
      var0[252] = true;
      var0[255] = true;
      m_parityTable = var0;
      var0 = new boolean[8];
      var0[2] = true;
      var0[4] = true;
      var0[6] = true;
      var0[7] = true;
      m_halfcarryTable = var0;
      var0 = new boolean[8];
      var0[1] = true;
      var0[2] = true;
      var0[3] = true;
      var0[7] = true;
      m_subhalfcarryTable = var0;
      var0 = new boolean[8];
      var0[1] = true;
      var0[6] = true;
      m_overflowTable = var0;
      var0 = new boolean[8];
      var0[3] = true;
      var0[4] = true;
      m_suboverflowTable = var0;
   }

   private void adc_a(int var1) {
      int var3 = this.m_a8;
      byte var2;
      if(this.m_carryF) {
         var2 = 1;
      } else {
         var2 = 0;
      }

      int var5 = var3 + var1 + var2;
      var1 = (this.m_a8 & 136) >> 1 | (var1 & 136) >> 2 | (var5 & 136) >> 3;
      this.m_a8 = var5 & 255;
      boolean var4;
      if((this.m_a8 & 128) != 0) {
         var4 = true;
      } else {
         var4 = false;
      }

      this.m_signF = var4;
      if(this.m_a8 == 0) {
         var4 = true;
      } else {
         var4 = false;
      }

      this.m_zeroF = var4;
      this.m_halfcarryF = m_halfcarryTable[var1 & 7];
      this.m_parityoverflowF = m_overflowTable[var1 >> 4];
      this.m_addsubtractF = false;
      if((var5 & 256) != 0) {
         var4 = true;
      } else {
         var4 = false;
      }

      this.m_carryF = var4;
      if((this.m_a8 & 8) != 0) {
         var4 = true;
      } else {
         var4 = false;
      }

      this.m_3F = var4;
      if((this.m_a8 & 32) != 0) {
         var4 = true;
      } else {
         var4 = false;
      }

      this.m_5F = var4;
   }

   private void adc_hl(int var1) {
      int var3 = this.hl16();
      byte var2;
      if(this.m_carryF) {
         var2 = 1;
      } else {
         var2 = 0;
      }

      int var5 = var3 + var1 + var2;
      var1 = (var3 & '蠀') >> 9 | (var1 & '蠀') >> 10 | (var5 & '蠀') >> 11;
      var3 = var5 & '\uffff';
      this.hl16(var3);
      boolean var4;
      if(('耀' & var3) != 0) {
         var4 = true;
      } else {
         var4 = false;
      }

      this.m_signF = var4;
      if(var3 == 0) {
         var4 = true;
      } else {
         var4 = false;
      }

      this.m_zeroF = var4;
      this.m_halfcarryF = m_halfcarryTable[var1 & 7];
      this.m_parityoverflowF = m_overflowTable[var1 >> 4];
      this.m_addsubtractF = false;
      if((65536 & var5) != 0) {
         var4 = true;
      } else {
         var4 = false;
      }

      this.m_carryF = var4;
      if((this.m_h8 & 8) != 0) {
         var4 = true;
      } else {
         var4 = false;
      }

      this.m_3F = var4;
      if((this.m_h8 & 32) != 0) {
         var4 = true;
      } else {
         var4 = false;
      }

      this.m_5F = var4;
   }

   private int add16(int var1, int var2) {
      return var1 + var2 & '\uffff';
   }

   private void add_a(int var1) {
      int var2 = this.m_a8 + var1;
      var1 = (this.m_a8 & 136) >> 1 | (var1 & 136) >> 2 | (var2 & 136) >> 3;
      this.m_a8 = var2 & 255;
      boolean var3;
      if((this.m_a8 & 128) != 0) {
         var3 = true;
      } else {
         var3 = false;
      }

      this.m_signF = var3;
      if(this.m_a8 == 0) {
         var3 = true;
      } else {
         var3 = false;
      }

      this.m_zeroF = var3;
      this.m_halfcarryF = m_halfcarryTable[var1 & 7];
      this.m_parityoverflowF = m_overflowTable[var1 >> 4];
      this.m_addsubtractF = false;
      if((var2 & 256) != 0) {
         var3 = true;
      } else {
         var3 = false;
      }

      this.m_carryF = var3;
      if((this.m_a8 & 8) != 0) {
         var3 = true;
      } else {
         var3 = false;
      }

      this.m_3F = var3;
      if((this.m_a8 & 32) != 0) {
         var3 = true;
      } else {
         var3 = false;
      }

      this.m_5F = var3;
   }

   private void add_hl(int var1) {
      this.m_x8 = this.m_h8;
      int var3 = this.hl16();
      int var2 = var3 + var1;
      this.hl16('\uffff' & var2);
      this.m_halfcarryF = m_halfcarryTable[(var3 & 2048) >> 9 | (var1 & 2048) >> 10 | (var2 & 2048) >> 11];
      this.m_addsubtractF = false;
      boolean var4;
      if((65536 & var2) != 0) {
         var4 = true;
      } else {
         var4 = false;
      }

      this.m_carryF = var4;
      if((this.m_h8 & 8) != 0) {
         var4 = true;
      } else {
         var4 = false;
      }

      this.m_3F = var4;
      if((this.m_h8 & 32) != 0) {
         var4 = true;
      } else {
         var4 = false;
      }

      this.m_5F = var4;
   }

   private void add_xx(int var1) {
      int var2 = this.m_xx16 + var1;
      int var3 = this.m_xx16;
      this.m_xx16 = '\uffff' & var2;
      this.m_halfcarryF = m_halfcarryTable[(var3 & 2048) >> 9 | (var1 & 2048) >> 10 | (var2 & 2048) >> 11];
      this.m_addsubtractF = false;
      boolean var4;
      if((65536 & var2) != 0) {
         var4 = true;
      } else {
         var4 = false;
      }

      this.m_carryF = var4;
      var1 = this.m_xx16 >> 8;
      if((var1 & 8) != 0) {
         var4 = true;
      } else {
         var4 = false;
      }

      this.m_3F = var4;
      if((var1 & 32) != 0) {
         var4 = true;
      } else {
         var4 = false;
      }

      this.m_5F = var4;
   }

   private int af16() {
      return this.m_a8 << 8 | this.m_f8;
   }

   private void af16(int var1) {
      this.m_a8 = var1 >> 8;
      this.m_f8 = var1 & 255;
   }

   private void and_a(int var1) {
      this.m_a8 &= var1;
      boolean var2;
      if((this.m_a8 & 128) != 0) {
         var2 = true;
      } else {
         var2 = false;
      }

      this.m_signF = var2;
      if(this.m_a8 == 0) {
         var2 = true;
      } else {
         var2 = false;
      }

      this.m_zeroF = var2;
      this.m_halfcarryF = true;
      this.m_parityoverflowF = m_parityTable[this.m_a8];
      this.m_addsubtractF = false;
      this.m_carryF = false;
      if((this.m_a8 & 8) != 0) {
         var2 = true;
      } else {
         var2 = false;
      }

      this.m_3F = var2;
      if((this.m_a8 & 32) != 0) {
         var2 = true;
      } else {
         var2 = false;
      }

      this.m_5F = var2;
   }

   private int bc16() {
      return this.m_b8 << 8 | this.m_c8;
   }

   private void bc16(int var1) {
      this.m_b8 = var1 >> 8;
      this.m_c8 = var1 & 255;
   }

   private void bit(int var1, int var2) {
      boolean var3;
      if((1 << var1 & var2) == 0) {
         var3 = true;
      } else {
         var3 = false;
      }

      this.m_zeroF = var3;
      this.m_halfcarryF = true;
      this.m_parityoverflowF = this.m_zeroF;
      this.m_addsubtractF = false;
      if((1 << var1 & var2) == 128) {
         var3 = true;
      } else {
         var3 = false;
      }

      this.m_signF = var3;
      if(var1 == 3 && !this.m_zeroF) {
         var3 = true;
      } else {
         var3 = false;
      }

      this.m_3F = var3;
      if(var1 == 5 && !this.m_zeroF) {
         var3 = true;
      } else {
         var3 = false;
      }

      this.m_5F = var3;
   }

   private void bit_hl(int var1, int var2) {
      boolean var3;
      if((1 << var1 & var2) == 0) {
         var3 = true;
      } else {
         var3 = false;
      }

      this.m_zeroF = var3;
      this.m_halfcarryF = true;
      this.m_parityoverflowF = this.m_zeroF;
      this.m_addsubtractF = false;
      if((1 << var1 & var2) == 128) {
         var3 = true;
      } else {
         var3 = false;
      }

      this.m_signF = var3;
      if((this.m_x8 & 8) != 0) {
         var3 = true;
      } else {
         var3 = false;
      }

      this.m_3F = var3;
      if((this.m_x8 & 32) != 0) {
         var3 = true;
      } else {
         var3 = false;
      }

      this.m_5F = var3;
   }

   private void bit_xx(int var1, int var2) {
      boolean var3;
      if((1 << var1 & var2) == 0) {
         var3 = true;
      } else {
         var3 = false;
      }

      this.m_zeroF = var3;
      this.m_halfcarryF = true;
      this.m_parityoverflowF = this.m_zeroF;
      this.m_addsubtractF = false;
      if((1 << var1 & var2) == 128) {
         var3 = true;
      } else {
         var3 = false;
      }

      this.m_signF = var3;
      if((this.xx16high8() & 8) != 0) {
         var3 = true;
      } else {
         var3 = false;
      }

      this.m_3F = var3;
      if((this.xx16high8() & 32) != 0) {
         var3 = true;
      } else {
         var3 = false;
      }

      this.m_5F = var3;
   }

   private void call_c() {
      if(this.m_carryF) {
         this.m_tstates += 17;
         this.push(this.incinc16(this.m_pc16));
         this.m_pc16 = this.m_memory.read16arg(this.m_pc16);
      } else {
         this.m_tstates += 10;
         this.m_pc16 = this.incinc16(this.m_pc16);
      }

   }

   private void call_m() {
      if(this.m_signF) {
         this.m_tstates += 17;
         this.push(this.incinc16(this.m_pc16));
         this.m_pc16 = this.m_memory.read16arg(this.m_pc16);
      } else {
         this.m_tstates += 10;
         this.m_pc16 = this.incinc16(this.m_pc16);
      }

   }

   private void call_nc() {
      if(!this.m_carryF) {
         this.m_tstates += 17;
         this.push(this.incinc16(this.m_pc16));
         this.m_pc16 = this.m_memory.read16arg(this.m_pc16);
      } else {
         this.m_tstates += 10;
         this.m_pc16 = this.incinc16(this.m_pc16);
      }

   }

   private void call_nz() {
      if(!this.m_zeroF) {
         this.m_tstates += 17;
         this.push(this.incinc16(this.m_pc16));
         this.m_pc16 = this.m_memory.read16arg(this.m_pc16);
      } else {
         this.m_tstates += 10;
         this.m_pc16 = this.incinc16(this.m_pc16);
      }

   }

   private void call_p() {
      if(!this.m_signF) {
         this.m_tstates += 17;
         this.push(this.incinc16(this.m_pc16));
         this.m_pc16 = this.m_memory.read16arg(this.m_pc16);
      } else {
         this.m_tstates += 10;
         this.m_pc16 = this.incinc16(this.m_pc16);
      }

   }

   private void call_pe() {
      if(this.m_parityoverflowF) {
         this.m_tstates += 17;
         this.push(this.incinc16(this.m_pc16));
         this.m_pc16 = this.m_memory.read16arg(this.m_pc16);
      } else {
         this.m_tstates += 10;
         this.m_pc16 = this.incinc16(this.m_pc16);
      }

   }

   private void call_po() {
      if(!this.m_parityoverflowF) {
         this.m_tstates += 17;
         this.push(this.incinc16(this.m_pc16));
         this.m_pc16 = this.m_memory.read16arg(this.m_pc16);
      } else {
         this.m_tstates += 10;
         this.m_pc16 = this.incinc16(this.m_pc16);
      }

   }

   private void call_z() {
      if(this.m_zeroF) {
         this.m_tstates += 17;
         this.push(this.incinc16(this.m_pc16));
         this.m_pc16 = this.m_memory.read16arg(this.m_pc16);
      } else {
         this.m_tstates += 10;
         this.m_pc16 = this.incinc16(this.m_pc16);
      }

   }

   private void checkInterrupt() {
      if(this.nmi) {
         this.nmi();
      }

      if(this.irq) {
         this.irq();
      }

   }

   private void cmp_a(int var1) {
      int var3 = this.m_a8 - var1;
      int var2 = (this.m_a8 & 136) >> 1 | (var1 & 136) >> 2 | (var3 & 136) >> 3;
      boolean var4;
      if((var3 & 128) != 0) {
         var4 = true;
      } else {
         var4 = false;
      }

      this.m_signF = var4;
      if((var3 & 255) == 0) {
         var4 = true;
      } else {
         var4 = false;
      }

      this.m_zeroF = var4;
      this.m_halfcarryF = m_subhalfcarryTable[var2 & 7];
      this.m_parityoverflowF = m_suboverflowTable[var2 >> 4];
      this.m_addsubtractF = true;
      if((var3 & 256) != 0) {
         var4 = true;
      } else {
         var4 = false;
      }

      this.m_carryF = var4;
      if((var1 & 8) != 0) {
         var4 = true;
      } else {
         var4 = false;
      }

      this.m_3F = var4;
      if((var1 & 32) != 0) {
         var4 = true;
      } else {
         var4 = false;
      }

      this.m_5F = var4;
   }

   private void cmp_a_special(int var1) {
      int var2 = this.m_a8 - var1;
      int var3 = this.m_a8;
      boolean var4;
      if((var2 & 128) != 0) {
         var4 = true;
      } else {
         var4 = false;
      }

      this.m_signF = var4;
      if((var2 & 255) == 0) {
         var4 = true;
      } else {
         var4 = false;
      }

      this.m_zeroF = var4;
      this.m_halfcarryF = m_subhalfcarryTable[((var3 & 136) >> 1 | (var1 & 136) >> 2 | (var2 & 136) >> 3) & 7];
      this.m_addsubtractF = true;
   }

   private void daa() {
      this.m_tstates += 4;
      boolean var4 = this.m_carryF;
      boolean var5 = this.m_addsubtractF;
      int var1;
      boolean var3;
      if(!var5) {
         label43: {
            byte var2 = 0;
            if(this.m_halfcarryF || (this.m_a8 & 15) > 9) {
               var2 = 6;
            }

            if(!this.m_carryF && this.m_a8 >> 4 <= 9) {
               var3 = var4;
               var1 = var2;
               if(this.m_a8 >> 4 < 9) {
                  break label43;
               }

               var3 = var4;
               var1 = var2;
               if((this.m_a8 & 15) <= 9) {
                  break label43;
               }
            }

            var1 = var2 | 96;
            var3 = true;
         }
      } else if(this.m_carryF) {
         if(this.m_halfcarryF) {
            var1 = 154;
         } else {
            var1 = 160;
         }

         var3 = var4;
      } else {
         if(this.m_halfcarryF) {
            var1 = 250;
         } else {
            var1 = 0;
         }

         var3 = var4;
      }

      this.add_a(var1);
      this.m_addsubtractF = var5;
      this.m_parityoverflowF = m_parityTable[this.m_a8];
      this.m_carryF = var3;
   }

   private int de16() {
      return this.m_d8 << 8 | this.m_e8;
   }

   private void de16(int var1) {
      this.m_d8 = var1 >> 8;
      this.m_e8 = var1 & 255;
   }

   private int dec16(int var1) {
      return var1 - 1 & '\uffff';
   }

   private int dec16bc() {
      int var1 = this.bc16();
      this.bc16(var1 - 1 & '\uffff');
      return var1;
   }

   private int dec16de() {
      int var1 = this.de16();
      this.de16(var1 - 1 & '\uffff');
      return var1;
   }

   private int dec16hl() {
      int var1 = this.hl16();
      this.hl16(var1 - 1 & '\uffff');
      return var1;
   }

   private int dec16pc() {
      int var1 = this.m_pc16;
      this.m_pc16 = this.m_pc16 - 1 & '\uffff';
      return var1;
   }

   private int dec16sp() {
      int var1 = this.m_sp16;
      this.m_sp16 = this.m_sp16 - 1 & '\uffff';
      return var1;
   }

   private int dec16xx() {
      int var1 = this.m_xx16;
      this.m_xx16 = this.m_xx16 - 1 & '\uffff';
      return var1;
   }

   private int dec8(int var1) {
      var1 = var1 - 1 & 255;
      boolean var2;
      if((var1 & 128) != 0) {
         var2 = true;
      } else {
         var2 = false;
      }

      this.m_signF = var2;
      if(var1 == 0) {
         var2 = true;
      } else {
         var2 = false;
      }

      this.m_zeroF = var2;
      if((var1 & 15) == 15) {
         var2 = true;
      } else {
         var2 = false;
      }

      this.m_halfcarryF = var2;
      if(var1 == 127) {
         var2 = true;
      } else {
         var2 = false;
      }

      this.m_parityoverflowF = var2;
      this.m_addsubtractF = true;
      if((var1 & 8) != 0) {
         var2 = true;
      } else {
         var2 = false;
      }

      this.m_3F = var2;
      if((var1 & 32) != 0) {
         var2 = true;
      } else {
         var2 = false;
      }

      this.m_5F = var2;
      return var1;
   }

   private int decdec16(int var1) {
      return var1 - 2 & '\uffff';
   }

   private void decodeCB(int var1) {
      switch(var1) {
      case 0:
         this.m_tstates += 8;
         this.m_b8 = this.rlc8(this.m_b8);
         break;
      case 1:
         this.m_tstates += 8;
         this.m_c8 = this.rlc8(this.m_c8);
         break;
      case 2:
         this.m_tstates += 8;
         this.m_d8 = this.rlc8(this.m_d8);
         break;
      case 3:
         this.m_tstates += 8;
         this.m_e8 = this.rlc8(this.m_e8);
         break;
      case 4:
         this.m_tstates += 8;
         this.m_h8 = this.rlc8(this.m_h8);
         break;
      case 5:
         this.m_tstates += 8;
         this.m_l8 = this.rlc8(this.m_l8);
         break;
      case 6:
         this.m_tstates += 15;
         var1 = this.rlc8(this.m_memory.read8(this.hl16()));
         this.m_memory.write8(this.hl16(), var1);
         break;
      case 7:
         this.m_tstates += 8;
         this.m_a8 = this.rlc8(this.m_a8);
         break;
      case 8:
         this.m_tstates += 8;
         this.m_b8 = this.rrc8(this.m_b8);
         break;
      case 9:
         this.m_tstates += 8;
         this.m_c8 = this.rrc8(this.m_c8);
         break;
      case 10:
         this.m_tstates += 8;
         this.m_d8 = this.rrc8(this.m_d8);
         break;
      case 11:
         this.m_tstates += 8;
         this.m_e8 = this.rrc8(this.m_e8);
         break;
      case 12:
         this.m_tstates += 8;
         this.m_h8 = this.rrc8(this.m_h8);
         break;
      case 13:
         this.m_tstates += 8;
         this.m_l8 = this.rrc8(this.m_l8);
         break;
      case 14:
         this.m_tstates += 15;
         var1 = this.rrc8(this.m_memory.read8(this.hl16()));
         this.m_memory.write8(this.hl16(), var1);
         break;
      case 15:
         this.m_tstates += 8;
         this.m_a8 = this.rrc8(this.m_a8);
         break;
      case 16:
         this.m_tstates += 8;
         this.m_b8 = this.rl8(this.m_b8);
         break;
      case 17:
         this.m_tstates += 8;
         this.m_c8 = this.rl8(this.m_c8);
         break;
      case 18:
         this.m_tstates += 8;
         this.m_d8 = this.rl8(this.m_d8);
         break;
      case 19:
         this.m_tstates += 8;
         this.m_e8 = this.rl8(this.m_e8);
         break;
      case 20:
         this.m_tstates += 8;
         this.m_h8 = this.rl8(this.m_h8);
         break;
      case 21:
         this.m_tstates += 8;
         this.m_l8 = this.rl8(this.m_l8);
         break;
      case 22:
         this.m_tstates += 15;
         var1 = this.rl8(this.m_memory.read8(this.hl16()));
         this.m_memory.write8(this.hl16(), var1);
         break;
      case 23:
         this.m_tstates += 8;
         this.m_a8 = this.rl8(this.m_a8);
         break;
      case 24:
         this.m_tstates += 8;
         this.m_b8 = this.rr8(this.m_b8);
         break;
      case 25:
         this.m_tstates += 8;
         this.m_c8 = this.rr8(this.m_c8);
         break;
      case 26:
         this.m_tstates += 8;
         this.m_d8 = this.rr8(this.m_d8);
         break;
      case 27:
         this.m_tstates += 8;
         this.m_e8 = this.rr8(this.m_e8);
         break;
      case 28:
         this.m_tstates += 8;
         this.m_h8 = this.rr8(this.m_h8);
         break;
      case 29:
         this.m_tstates += 8;
         this.m_l8 = this.rr8(this.m_l8);
         break;
      case 30:
         this.m_tstates += 15;
         var1 = this.rr8(this.m_memory.read8(this.hl16()));
         this.m_memory.write8(this.hl16(), var1);
         break;
      case 31:
         this.m_tstates += 8;
         this.m_a8 = this.rr8(this.m_a8);
         break;
      case 32:
         this.m_tstates += 8;
         this.m_b8 = this.sla8(this.m_b8);
         break;
      case 33:
         this.m_tstates += 8;
         this.m_c8 = this.sla8(this.m_c8);
         break;
      case 34:
         this.m_tstates += 8;
         this.m_d8 = this.sla8(this.m_d8);
         break;
      case 35:
         this.m_tstates += 8;
         this.m_e8 = this.sla8(this.m_e8);
         break;
      case 36:
         this.m_tstates += 8;
         this.m_h8 = this.sla8(this.m_h8);
         break;
      case 37:
         this.m_tstates += 8;
         this.m_l8 = this.sla8(this.m_l8);
         break;
      case 38:
         this.m_tstates += 15;
         var1 = this.sla8(this.m_memory.read8(this.hl16()));
         this.m_memory.write8(this.hl16(), var1);
         break;
      case 39:
         this.m_tstates += 8;
         this.m_a8 = this.sla8(this.m_a8);
         break;
      case 40:
         this.m_tstates += 8;
         this.m_b8 = this.sra8(this.m_b8);
         break;
      case 41:
         this.m_tstates += 8;
         this.m_c8 = this.sra8(this.m_c8);
         break;
      case 42:
         this.m_tstates += 8;
         this.m_d8 = this.sra8(this.m_d8);
         break;
      case 43:
         this.m_tstates += 8;
         this.m_e8 = this.sra8(this.m_e8);
         break;
      case 44:
         this.m_tstates += 8;
         this.m_h8 = this.sra8(this.m_h8);
         break;
      case 45:
         this.m_tstates += 8;
         this.m_l8 = this.sra8(this.m_l8);
         break;
      case 46:
         this.m_tstates += 15;
         var1 = this.sra8(this.m_memory.read8(this.hl16()));
         this.m_memory.write8(this.hl16(), var1);
         break;
      case 47:
         this.m_tstates += 8;
         this.m_a8 = this.sra8(this.m_a8);
         break;
      case 48:
         this.m_tstates += 8;
         this.m_b8 = this.sli8(this.m_b8);
         break;
      case 49:
         this.m_tstates += 8;
         this.m_c8 = this.sli8(this.m_c8);
         break;
      case 50:
         this.m_tstates += 8;
         this.m_d8 = this.sli8(this.m_d8);
         break;
      case 51:
         this.m_tstates += 8;
         this.m_e8 = this.sli8(this.m_e8);
         break;
      case 52:
         this.m_tstates += 8;
         this.m_h8 = this.sli8(this.m_h8);
         break;
      case 53:
         this.m_tstates += 8;
         this.m_l8 = this.sli8(this.m_l8);
         break;
      case 54:
         this.m_tstates += 15;
         var1 = this.sli8(this.m_memory.read8(this.hl16()));
         this.m_memory.write8(this.hl16(), var1);
         break;
      case 55:
         this.m_tstates += 8;
         this.m_a8 = this.sli8(this.m_a8);
         break;
      case 56:
         this.m_tstates += 8;
         this.m_b8 = this.srl8(this.m_b8);
         break;
      case 57:
         this.m_tstates += 8;
         this.m_c8 = this.srl8(this.m_c8);
         break;
      case 58:
         this.m_tstates += 8;
         this.m_d8 = this.srl8(this.m_d8);
         break;
      case 59:
         this.m_tstates += 8;
         this.m_e8 = this.srl8(this.m_e8);
         break;
      case 60:
         this.m_tstates += 8;
         this.m_h8 = this.srl8(this.m_h8);
         break;
      case 61:
         this.m_tstates += 8;
         this.m_l8 = this.srl8(this.m_l8);
         break;
      case 62:
         this.m_tstates += 15;
         var1 = this.srl8(this.m_memory.read8(this.hl16()));
         this.m_memory.write8(this.hl16(), var1);
         break;
      case 63:
         this.m_tstates += 8;
         this.m_a8 = this.srl8(this.m_a8);
         break;
      case 64:
         this.m_tstates += 8;
         this.bit(0, this.m_b8);
         break;
      case 65:
         this.m_tstates += 8;
         this.bit(0, this.m_c8);
         break;
      case 66:
         this.m_tstates += 8;
         this.bit(0, this.m_d8);
         break;
      case 67:
         this.m_tstates += 8;
         this.bit(0, this.m_e8);
         break;
      case 68:
         this.m_tstates += 8;
         this.bit(0, this.m_h8);
         break;
      case 69:
         this.m_tstates += 8;
         this.bit(0, this.m_l8);
         break;
      case 70:
         this.m_tstates += 12;
         this.bit_hl(0, this.m_memory.read8(this.hl16()));
         break;
      case 71:
         this.m_tstates += 8;
         this.bit(0, this.m_a8);
         break;
      case 72:
         this.m_tstates += 8;
         this.bit(1, this.m_b8);
         break;
      case 73:
         this.m_tstates += 8;
         this.bit(1, this.m_c8);
         break;
      case 74:
         this.m_tstates += 8;
         this.bit(1, this.m_d8);
         break;
      case 75:
         this.m_tstates += 8;
         this.bit(1, this.m_e8);
         break;
      case 76:
         this.m_tstates += 8;
         this.bit(1, this.m_h8);
         break;
      case 77:
         this.m_tstates += 8;
         this.bit(1, this.m_l8);
         break;
      case 78:
         this.m_tstates += 12;
         this.bit_hl(1, this.m_memory.read8(this.hl16()));
         break;
      case 79:
         this.m_tstates += 8;
         this.bit(1, this.m_a8);
         break;
      case 80:
         this.m_tstates += 8;
         this.bit(2, this.m_b8);
         break;
      case 81:
         this.m_tstates += 8;
         this.bit(2, this.m_c8);
         break;
      case 82:
         this.m_tstates += 8;
         this.bit(2, this.m_d8);
         break;
      case 83:
         this.m_tstates += 8;
         this.bit(2, this.m_e8);
         break;
      case 84:
         this.m_tstates += 8;
         this.bit(2, this.m_h8);
         break;
      case 85:
         this.m_tstates += 8;
         this.bit(2, this.m_l8);
         break;
      case 86:
         this.m_tstates += 12;
         this.bit_hl(2, this.m_memory.read8(this.hl16()));
         break;
      case 87:
         this.m_tstates += 8;
         this.bit(2, this.m_a8);
         break;
      case 88:
         this.m_tstates += 8;
         this.bit(3, this.m_b8);
         break;
      case 89:
         this.m_tstates += 8;
         this.bit(3, this.m_c8);
         break;
      case 90:
         this.m_tstates += 8;
         this.bit(3, this.m_d8);
         break;
      case 91:
         this.m_tstates += 8;
         this.bit(3, this.m_e8);
         break;
      case 92:
         this.m_tstates += 8;
         this.bit(3, this.m_h8);
         break;
      case 93:
         this.m_tstates += 8;
         this.bit(3, this.m_l8);
         break;
      case 94:
         this.m_tstates += 12;
         this.bit_hl(3, this.m_memory.read8(this.hl16()));
         break;
      case 95:
         this.m_tstates += 8;
         this.bit(3, this.m_a8);
         break;
      case 96:
         this.m_tstates += 8;
         this.bit(4, this.m_b8);
         break;
      case 97:
         this.m_tstates += 8;
         this.bit(4, this.m_c8);
         break;
      case 98:
         this.m_tstates += 8;
         this.bit(4, this.m_d8);
         break;
      case 99:
         this.m_tstates += 8;
         this.bit(4, this.m_e8);
         break;
      case 100:
         this.m_tstates += 8;
         this.bit(4, this.m_h8);
         break;
      case 101:
         this.m_tstates += 8;
         this.bit(4, this.m_l8);
         break;
      case 102:
         this.m_tstates += 12;
         this.bit_hl(4, this.m_memory.read8(this.hl16()));
         break;
      case 103:
         this.m_tstates += 8;
         this.bit(4, this.m_a8);
         break;
      case 104:
         this.m_tstates += 8;
         this.bit(5, this.m_b8);
         break;
      case 105:
         this.m_tstates += 8;
         this.bit(5, this.m_c8);
         break;
      case 106:
         this.m_tstates += 8;
         this.bit(5, this.m_d8);
         break;
      case 107:
         this.m_tstates += 8;
         this.bit(5, this.m_e8);
         break;
      case 108:
         this.m_tstates += 8;
         this.bit(5, this.m_h8);
         break;
      case 109:
         this.m_tstates += 8;
         this.bit(5, this.m_l8);
         break;
      case 110:
         this.m_tstates += 12;
         this.bit_hl(5, this.m_memory.read8(this.hl16()));
         break;
      case 111:
         this.m_tstates += 8;
         this.bit(5, this.m_a8);
         break;
      case 112:
         this.m_tstates += 8;
         this.bit(6, this.m_b8);
         break;
      case 113:
         this.m_tstates += 8;
         this.bit(6, this.m_c8);
         break;
      case 114:
         this.m_tstates += 8;
         this.bit(6, this.m_d8);
         break;
      case 115:
         this.m_tstates += 8;
         this.bit(6, this.m_e8);
         break;
      case 116:
         this.m_tstates += 8;
         this.bit(6, this.m_h8);
         break;
      case 117:
         this.m_tstates += 8;
         this.bit(6, this.m_l8);
         break;
      case 118:
         this.m_tstates += 12;
         this.bit_hl(6, this.m_memory.read8(this.hl16()));
         break;
      case 119:
         this.m_tstates += 8;
         this.bit(6, this.m_a8);
         break;
      case 120:
         this.m_tstates += 8;
         this.bit(7, this.m_b8);
         break;
      case 121:
         this.m_tstates += 8;
         this.bit(7, this.m_c8);
         break;
      case 122:
         this.m_tstates += 8;
         this.bit(7, this.m_d8);
         break;
      case 123:
         this.m_tstates += 8;
         this.bit(7, this.m_e8);
         break;
      case 124:
         this.m_tstates += 8;
         this.bit(7, this.m_h8);
         break;
      case 125:
         this.m_tstates += 8;
         this.bit(7, this.m_l8);
         break;
      case 126:
         this.m_tstates += 12;
         this.bit_hl(7, this.m_memory.read8(this.hl16()));
         break;
      case 127:
         this.m_tstates += 8;
         this.bit(7, this.m_a8);
         break;
      case 128:
         this.m_tstates += 8;
         this.m_b8 &= 254;
         break;
      case 129:
         this.m_tstates += 8;
         this.m_c8 &= 254;
         break;
      case 130:
         this.m_tstates += 8;
         this.m_d8 &= 254;
         break;
      case 131:
         this.m_tstates += 8;
         this.m_e8 &= 254;
         break;
      case 132:
         this.m_tstates += 8;
         this.m_h8 &= 254;
         break;
      case 133:
         this.m_tstates += 8;
         this.m_l8 &= 254;
         break;
      case 134:
         this.m_tstates += 15;
         this.m_memory.write8(this.hl16(), this.m_memory.read8(this.hl16()) & 254);
         break;
      case 135:
         this.m_tstates += 8;
         this.m_a8 &= 254;
         break;
      case 136:
         this.m_tstates += 8;
         this.m_b8 &= 253;
         break;
      case 137:
         this.m_tstates += 8;
         this.m_c8 &= 253;
         break;
      case 138:
         this.m_tstates += 8;
         this.m_d8 &= 253;
         break;
      case 139:
         this.m_tstates += 8;
         this.m_e8 &= 253;
         break;
      case 140:
         this.m_tstates += 8;
         this.m_h8 &= 253;
         break;
      case 141:
         this.m_tstates += 8;
         this.m_l8 &= 253;
         break;
      case 142:
         this.m_tstates += 15;
         this.m_memory.write8(this.hl16(), this.m_memory.read8(this.hl16()) & 253);
         break;
      case 143:
         this.m_tstates += 8;
         this.m_a8 &= 253;
         break;
      case 144:
         this.m_tstates += 8;
         this.m_b8 &= 251;
         break;
      case 145:
         this.m_tstates += 8;
         this.m_c8 &= 251;
         break;
      case 146:
         this.m_tstates += 8;
         this.m_d8 &= 251;
         break;
      case 147:
         this.m_tstates += 8;
         this.m_e8 &= 251;
         break;
      case 148:
         this.m_tstates += 8;
         this.m_h8 &= 251;
         break;
      case 149:
         this.m_tstates += 8;
         this.m_l8 &= 251;
         break;
      case 150:
         this.m_tstates += 15;
         this.m_memory.write8(this.hl16(), this.m_memory.read8(this.hl16()) & 251);
         break;
      case 151:
         this.m_tstates += 8;
         this.m_a8 &= 251;
         break;
      case 152:
         this.m_tstates += 8;
         this.m_b8 &= 247;
         break;
      case 153:
         this.m_tstates += 8;
         this.m_c8 &= 247;
         break;
      case 154:
         this.m_tstates += 8;
         this.m_d8 &= 247;
         break;
      case 155:
         this.m_tstates += 8;
         this.m_e8 &= 247;
         break;
      case 156:
         this.m_tstates += 8;
         this.m_h8 &= 247;
         break;
      case 157:
         this.m_tstates += 8;
         this.m_l8 &= 247;
         break;
      case 158:
         this.m_tstates += 15;
         this.m_memory.write8(this.hl16(), this.m_memory.read8(this.hl16()) & 247);
         break;
      case 159:
         this.m_tstates += 8;
         this.m_a8 &= 247;
         break;
      case 160:
         this.m_tstates += 8;
         this.m_b8 &= 239;
         break;
      case 161:
         this.m_tstates += 8;
         this.m_c8 &= 239;
         break;
      case 162:
         this.m_tstates += 8;
         this.m_d8 &= 239;
         break;
      case 163:
         this.m_tstates += 8;
         this.m_e8 &= 239;
         break;
      case 164:
         this.m_tstates += 8;
         this.m_h8 &= 239;
         break;
      case 165:
         this.m_tstates += 8;
         this.m_l8 &= 239;
         break;
      case 166:
         this.m_tstates += 15;
         this.m_memory.write8(this.hl16(), this.m_memory.read8(this.hl16()) & 239);
         break;
      case 167:
         this.m_tstates += 8;
         this.m_a8 &= 239;
         break;
      case 168:
         this.m_tstates += 8;
         this.m_b8 &= 223;
         break;
      case 169:
         this.m_tstates += 8;
         this.m_c8 &= 223;
         break;
      case 170:
         this.m_tstates += 8;
         this.m_d8 &= 223;
         break;
      case 171:
         this.m_tstates += 8;
         this.m_e8 &= 223;
         break;
      case 172:
         this.m_tstates += 8;
         this.m_h8 &= 223;
         break;
      case 173:
         this.m_tstates += 8;
         this.m_l8 &= 223;
         break;
      case 174:
         this.m_tstates += 15;
         this.m_memory.write8(this.hl16(), this.m_memory.read8(this.hl16()) & 223);
         break;
      case 175:
         this.m_tstates += 8;
         this.m_a8 &= 223;
         break;
      case 176:
         this.m_tstates += 8;
         this.m_b8 &= 191;
         break;
      case 177:
         this.m_tstates += 8;
         this.m_c8 &= 191;
         break;
      case 178:
         this.m_tstates += 8;
         this.m_d8 &= 191;
         break;
      case 179:
         this.m_tstates += 8;
         this.m_e8 &= 191;
         break;
      case 180:
         this.m_tstates += 8;
         this.m_h8 &= 191;
         break;
      case 181:
         this.m_tstates += 8;
         this.m_l8 &= 191;
         break;
      case 182:
         this.m_tstates += 15;
         this.m_memory.write8(this.hl16(), this.m_memory.read8(this.hl16()) & 191);
         break;
      case 183:
         this.m_tstates += 8;
         this.m_a8 &= 191;
         break;
      case 184:
         this.m_tstates += 8;
         this.m_b8 &= 127;
         break;
      case 185:
         this.m_tstates += 8;
         this.m_c8 &= 127;
         break;
      case 186:
         this.m_tstates += 8;
         this.m_d8 &= 127;
         break;
      case 187:
         this.m_tstates += 8;
         this.m_e8 &= 127;
         break;
      case 188:
         this.m_tstates += 8;
         this.m_h8 &= 127;
         break;
      case 189:
         this.m_tstates += 8;
         this.m_l8 &= 127;
         break;
      case 190:
         this.m_tstates += 15;
         this.m_memory.write8(this.hl16(), this.m_memory.read8(this.hl16()) & 127);
         break;
      case 191:
         this.m_tstates += 8;
         this.m_a8 &= 127;
         break;
      case 192:
         this.m_tstates += 8;
         this.m_b8 |= 1;
         break;
      case 193:
         this.m_tstates += 8;
         this.m_c8 |= 1;
         break;
      case 194:
         this.m_tstates += 8;
         this.m_d8 |= 1;
         break;
      case 195:
         this.m_tstates += 8;
         this.m_e8 |= 1;
         break;
      case 196:
         this.m_tstates += 8;
         this.m_h8 |= 1;
         break;
      case 197:
         this.m_tstates += 8;
         this.m_l8 |= 1;
         break;
      case 198:
         this.m_tstates += 15;
         this.m_memory.write8(this.hl16(), this.m_memory.read8(this.hl16()) | 1);
         break;
      case 199:
         this.m_tstates += 8;
         this.m_a8 |= 1;
         break;
      case 200:
         this.m_tstates += 8;
         this.m_b8 |= 2;
         break;
      case 201:
         this.m_tstates += 8;
         this.m_c8 |= 2;
         break;
      case 202:
         this.m_tstates += 8;
         this.m_d8 |= 2;
         break;
      case 203:
         this.m_tstates += 8;
         this.m_e8 |= 2;
         break;
      case 204:
         this.m_tstates += 8;
         this.m_h8 |= 2;
         break;
      case 205:
         this.m_tstates += 8;
         this.m_l8 |= 2;
         break;
      case 206:
         this.m_tstates += 15;
         this.m_memory.write8(this.hl16(), this.m_memory.read8(this.hl16()) | 2);
         break;
      case 207:
         this.m_tstates += 8;
         this.m_a8 |= 2;
         break;
      case 208:
         this.m_tstates += 8;
         this.m_b8 |= 4;
         break;
      case 209:
         this.m_tstates += 8;
         this.m_c8 |= 4;
         break;
      case 210:
         this.m_tstates += 8;
         this.m_d8 |= 4;
         break;
      case 211:
         this.m_tstates += 8;
         this.m_e8 |= 4;
         break;
      case 212:
         this.m_tstates += 8;
         this.m_h8 |= 4;
         break;
      case 213:
         this.m_tstates += 8;
         this.m_l8 |= 4;
         break;
      case 214:
         this.m_tstates += 15;
         this.m_memory.write8(this.hl16(), this.m_memory.read8(this.hl16()) | 4);
         break;
      case 215:
         this.m_tstates += 8;
         this.m_a8 |= 4;
         break;
      case 216:
         this.m_tstates += 8;
         this.m_b8 |= 8;
         break;
      case 217:
         this.m_tstates += 8;
         this.m_c8 |= 8;
         break;
      case 218:
         this.m_tstates += 8;
         this.m_d8 |= 8;
         break;
      case 219:
         this.m_tstates += 8;
         this.m_e8 |= 8;
         break;
      case 220:
         this.m_tstates += 8;
         this.m_h8 |= 8;
         break;
      case 221:
         this.m_tstates += 8;
         this.m_l8 |= 8;
         break;
      case 222:
         this.m_tstates += 15;
         this.m_memory.write8(this.hl16(), this.m_memory.read8(this.hl16()) | 8);
         break;
      case 223:
         this.m_tstates += 8;
         this.m_a8 |= 8;
         break;
      case 224:
         this.m_tstates += 8;
         this.m_b8 |= 16;
         break;
      case 225:
         this.m_tstates += 8;
         this.m_c8 |= 16;
         break;
      case 226:
         this.m_tstates += 8;
         this.m_d8 |= 16;
         break;
      case 227:
         this.m_tstates += 8;
         this.m_e8 |= 16;
         break;
      case 228:
         this.m_tstates += 8;
         this.m_h8 |= 16;
         break;
      case 229:
         this.m_tstates += 8;
         this.m_l8 |= 16;
         break;
      case 230:
         this.m_tstates += 15;
         this.m_memory.write8(this.hl16(), this.m_memory.read8(this.hl16()) | 16);
         break;
      case 231:
         this.m_tstates += 8;
         this.m_a8 |= 16;
         break;
      case 232:
         this.m_tstates += 8;
         this.m_b8 |= 32;
         break;
      case 233:
         this.m_tstates += 8;
         this.m_c8 |= 32;
         break;
      case 234:
         this.m_tstates += 8;
         this.m_d8 |= 32;
         break;
      case 235:
         this.m_tstates += 8;
         this.m_e8 |= 32;
         break;
      case 236:
         this.m_tstates += 8;
         this.m_h8 |= 32;
         break;
      case 237:
         this.m_tstates += 8;
         this.m_l8 |= 32;
         break;
      case 238:
         this.m_tstates += 15;
         this.m_memory.write8(this.hl16(), this.m_memory.read8(this.hl16()) | 32);
         break;
      case 239:
         this.m_tstates += 8;
         this.m_a8 |= 32;
         break;
      case 240:
         this.m_tstates += 8;
         this.m_b8 |= 64;
         break;
      case 241:
         this.m_tstates += 8;
         this.m_c8 |= 64;
         break;
      case 242:
         this.m_tstates += 8;
         this.m_d8 |= 64;
         break;
      case 243:
         this.m_tstates += 8;
         this.m_e8 |= 64;
         break;
      case 244:
         this.m_tstates += 8;
         this.m_h8 |= 64;
         break;
      case 245:
         this.m_tstates += 8;
         this.m_l8 |= 64;
         break;
      case 246:
         this.m_tstates += 15;
         this.m_memory.write8(this.hl16(), this.m_memory.read8(this.hl16()) | 64);
         break;
      case 247:
         this.m_tstates += 8;
         this.m_a8 |= 64;
         break;
      case 248:
         this.m_tstates += 8;
         this.m_b8 |= 128;
         break;
      case 249:
         this.m_tstates += 8;
         this.m_c8 |= 128;
         break;
      case 250:
         this.m_tstates += 8;
         this.m_d8 |= 128;
         break;
      case 251:
         this.m_tstates += 8;
         this.m_e8 |= 128;
         break;
      case 252:
         this.m_tstates += 8;
         this.m_h8 |= 128;
         break;
      case 253:
         this.m_tstates += 8;
         this.m_l8 |= 128;
         break;
      case 254:
         this.m_tstates += 15;
         this.m_memory.write8(this.hl16(), this.m_memory.read8(this.hl16()) | 128);
         break;
      case 255:
         this.m_tstates += 8;
         this.m_a8 |= 128;
      }

   }

   private void decodeED(int var1) {
      int var2;
      int var3;
      boolean var4;
      byte var5;
      switch(var1) {
      case 64:
         this.m_tstates += 12;
         this.m_b8 = this.in8(this.bc16());
         break;
      case 65:
         this.m_tstates += 12;
         this.m_memory.out(this.bc16(), this.m_b8);
         break;
      case 66:
         this.m_tstates += 15;
         this.sbc_hl(this.bc16());
         break;
      case 67:
         this.m_tstates += 20;
         this.m_memory.write16(this.m_memory.read16arg(this.m_pc16), this.bc16());
         this.m_pc16 = this.incinc16(this.m_pc16);
         break;
      case 68:
      case 76:
      case 84:
      case 92:
      case 100:
      case 108:
      case 116:
      case 124:
         this.m_tstates += 8;
         var1 = this.m_a8;
         this.m_a8 = 0;
         this.sub_a(var1);
         break;
      case 69:
      case 85:
      case 93:
      case 101:
      case 109:
      case 117:
      case 125:
         this.m_tstates += 14;
         this.m_pc16 = this.pop16();
         this.m_iff1a = this.m_iff1b;
         this.checkInterrupt();
         break;
      case 70:
      case 78:
      case 102:
      case 110:
         this.m_tstates += 8;
         this.m_im2 = 0;
         break;
      case 71:
         this.m_tstates += 9;
         this.m_i8 = this.m_a8;
         break;
      case 72:
         this.m_tstates += 12;
         this.m_c8 = this.in8(this.bc16());
         break;
      case 73:
         this.m_tstates += 12;
         this.m_memory.out(this.bc16(), this.m_c8);
         break;
      case 74:
         this.m_tstates += 15;
         this.adc_hl(this.bc16());
         break;
      case 75:
         this.m_tstates += 20;
         this.bc16(this.m_memory.read16(this.m_memory.read16arg(this.m_pc16)));
         this.m_pc16 = this.incinc16(this.m_pc16);
         break;
      case 77:
         this.m_tstates += 14;
         this.m_pc16 = this.pop16();
         break;
      case 79:
         this.m_tstates += 9;
         this.m_r8 = this.m_a8;
         break;
      case 80:
         this.m_tstates += 12;
         this.m_d8 = this.in8(this.bc16());
         break;
      case 81:
         this.m_tstates += 12;
         this.m_memory.out(this.bc16(), this.m_d8);
         break;
      case 82:
         this.m_tstates += 15;
         this.sbc_hl(this.de16());
         break;
      case 83:
         this.m_tstates += 20;
         this.m_memory.write16(this.m_memory.read16arg(this.m_pc16), this.de16());
         this.m_pc16 = this.incinc16(this.m_pc16);
         break;
      case 86:
      case 118:
         this.m_tstates += 8;
         this.m_im2 = 1;
         break;
      case 87:
         this.m_tstates += 9;
         this.ld_a_special(this.m_i8);
         break;
      case 88:
         this.m_tstates += 12;
         this.m_e8 = this.in8(this.bc16());
         break;
      case 89:
         this.m_tstates += 12;
         this.m_memory.out(this.bc16(), this.m_e8);
         break;
      case 90:
         this.m_tstates += 15;
         this.adc_hl(this.de16());
         break;
      case 91:
         this.m_tstates += 20;
         this.de16(this.m_memory.read16(this.m_memory.read16arg(this.m_pc16)));
         this.m_pc16 = this.incinc16(this.m_pc16);
         break;
      case 94:
      case 126:
         this.m_tstates += 8;
         this.m_im2 = 2;
         break;
      case 95:
         this.m_tstates += 9;
         this.ld_a_special(this.m_r8);
         break;
      case 96:
         this.m_tstates += 12;
         this.m_h8 = this.in8(this.bc16());
         break;
      case 97:
         this.m_tstates += 12;
         this.m_memory.out(this.bc16(), this.m_h8);
         break;
      case 98:
         this.m_tstates += 15;
         this.sbc_hl(this.hl16());
         break;
      case 99:
         this.m_tstates += 20;
         this.m_memory.write16(this.m_memory.read16arg(this.m_pc16), this.hl16());
         this.m_pc16 = this.incinc16(this.m_pc16);
         break;
      case 103:
         this.m_tstates += 18;
         var1 = this.m_memory.read8(this.hl16());
         this.m_memory.write8(this.hl16(), var1 >> 4 | this.m_a8 << 4);
         this.m_a8 = this.m_a8 & 240 | var1 & 15;
         if((this.m_a8 & 128) != 0) {
            var4 = true;
         } else {
            var4 = false;
         }

         this.m_signF = var4;
         if(this.m_a8 == 0) {
            var4 = true;
         } else {
            var4 = false;
         }

         this.m_zeroF = var4;
         this.m_halfcarryF = false;
         this.m_parityoverflowF = m_parityTable[this.m_a8];
         this.m_addsubtractF = false;
         if((this.m_a8 & 8) != 0) {
            var4 = true;
         } else {
            var4 = false;
         }

         this.m_3F = var4;
         if((this.m_a8 & 32) != 0) {
            var4 = true;
         } else {
            var4 = false;
         }

         this.m_5F = var4;
         break;
      case 104:
         this.m_tstates += 12;
         this.m_l8 = this.in8(this.bc16());
         break;
      case 105:
         this.m_tstates += 12;
         this.m_memory.out(this.bc16(), this.m_l8);
         break;
      case 106:
         this.m_tstates += 15;
         this.adc_hl(this.hl16());
         break;
      case 107:
         this.m_tstates += 20;
         this.hl16(this.m_memory.read16(this.m_memory.read16arg(this.m_pc16)));
         this.m_pc16 = this.incinc16(this.m_pc16);
         break;
      case 111:
         this.m_tstates += 18;
         var1 = this.m_memory.read8(this.hl16());
         this.m_memory.write8(this.hl16(), (var1 << 4 | this.m_a8 & 15) & 255);
         this.m_a8 = this.m_a8 & 240 | var1 >> 4;
         if((this.m_a8 & 128) != 0) {
            var4 = true;
         } else {
            var4 = false;
         }

         this.m_signF = var4;
         if(this.m_a8 == 0) {
            var4 = true;
         } else {
            var4 = false;
         }

         this.m_zeroF = var4;
         this.m_halfcarryF = false;
         this.m_parityoverflowF = m_parityTable[this.m_a8];
         this.m_addsubtractF = false;
         if((this.m_a8 & 8) != 0) {
            var4 = true;
         } else {
            var4 = false;
         }

         this.m_3F = var4;
         if((this.m_a8 & 32) != 0) {
            var4 = true;
         } else {
            var4 = false;
         }

         this.m_5F = var4;
         break;
      case 112:
         this.m_tstates += 12;
         this.in8(this.bc16());
         break;
      case 113:
         this.m_tstates += 12;
         this.m_memory.out(this.bc16(), 0);
         break;
      case 114:
         this.m_tstates += 15;
         this.sbc_hl(this.m_sp16);
         break;
      case 115:
         this.m_tstates += 20;
         this.m_memory.write16(this.m_memory.read16arg(this.m_pc16), this.m_sp16);
         this.m_pc16 = this.incinc16(this.m_pc16);
         break;
      case 119:
      case 127:
         this.m_tstates += 8;
         break;
      case 120:
         this.m_tstates += 12;
         this.m_a8 = this.in8(this.bc16());
         break;
      case 121:
         this.m_tstates += 12;
         this.m_memory.out(this.bc16(), this.m_a8);
         break;
      case 122:
         this.m_tstates += 15;
         this.adc_hl(this.m_sp16);
         break;
      case 123:
         this.m_tstates += 20;
         this.m_sp16 = this.m_memory.read16(this.m_memory.read16arg(this.m_pc16));
         this.m_pc16 = this.incinc16(this.m_pc16);
         break;
      case 160:
         this.m_tstates += 16;
         var1 = this.m_memory.read8(this.hl16());
         this.m_memory.write8(this.de16(), var1);
         this.inc16de();
         this.inc16hl();
         this.dec16bc();
         if(this.bc16() != 0) {
            var4 = true;
         } else {
            var4 = false;
         }

         this.m_parityoverflowF = var4;
         this.m_halfcarryF = false;
         this.m_addsubtractF = false;
         var1 += this.m_a8;
         if((var1 & 8) != 0) {
            var4 = true;
         } else {
            var4 = false;
         }

         this.m_3F = var4;
         if((var1 & 1) != 0) {
            var4 = true;
         } else {
            var4 = false;
         }

         this.m_5F = var4;
         break;
      case 161:
         this.m_tstates += 16;
         var2 = this.m_memory.read8(this.hl16());
         this.cmp_a_special(var2);
         this.inc16hl();
         this.dec16bc();
         if(this.bc16() != 0) {
            var4 = true;
         } else {
            var4 = false;
         }

         this.m_parityoverflowF = var4;
         var3 = this.m_a8;
         if(this.m_halfcarryF) {
            var5 = 1;
         } else {
            var5 = 0;
         }

         var1 = var3 - var2 - var5;
         if((var1 & 8) != 0) {
            var4 = true;
         } else {
            var4 = false;
         }

         this.m_3F = var4;
         if((var1 & 1) != 0) {
            var4 = true;
         } else {
            var4 = false;
         }

         this.m_5F = var4;
         break;
      case 162:
         this.m_tstates += 16;
         this.m_memory.write8(this.hl16(), this.m_memory.in(this.bc16()));
         this.m_b8 = this.m_b8 - 1 & 255;
         this.inc16hl();
         if(this.m_b8 == 0) {
            var4 = true;
         } else {
            var4 = false;
         }

         this.m_zeroF = var4;
         this.m_addsubtractF = true;
         break;
      case 163:
         this.m_tstates += 16;
         this.m_b8 = this.m_b8 - 1 & 255;
         this.m_memory.out(this.bc16(), this.m_memory.read8(this.hl16()));
         this.inc16hl();
         if(this.m_b8 == 0) {
            var4 = true;
         } else {
            var4 = false;
         }

         this.m_zeroF = var4;
         this.m_addsubtractF = true;
         break;
      case 168:
         this.m_tstates += 16;
         var1 = this.m_memory.read8(this.hl16());
         this.m_memory.write8(this.de16(), var1);
         this.dec16de();
         this.dec16hl();
         this.dec16bc();
         if(this.bc16() != 0) {
            var4 = true;
         } else {
            var4 = false;
         }

         this.m_parityoverflowF = var4;
         this.m_halfcarryF = false;
         this.m_addsubtractF = false;
         var1 += this.m_a8;
         if((var1 & 8) != 0) {
            var4 = true;
         } else {
            var4 = false;
         }

         this.m_3F = var4;
         if((var1 & 1) != 0) {
            var4 = true;
         } else {
            var4 = false;
         }

         this.m_5F = var4;
         break;
      case 169:
         this.m_tstates += 16;
         var2 = this.m_memory.read8(this.hl16());
         this.cmp_a_special(var2);
         this.dec16hl();
         this.dec16bc();
         if(this.bc16() != 0) {
            var4 = true;
         } else {
            var4 = false;
         }

         this.m_parityoverflowF = var4;
         var3 = this.m_a8;
         if(this.m_halfcarryF) {
            var5 = 1;
         } else {
            var5 = 0;
         }

         var1 = var3 - var2 - var5;
         if((var1 & 8) != 0) {
            var4 = true;
         } else {
            var4 = false;
         }

         this.m_3F = var4;
         if((var1 & 1) != 0) {
            var4 = true;
         } else {
            var4 = false;
         }

         this.m_5F = var4;
         break;
      case 170:
         this.m_tstates += 16;
         this.m_memory.write8(this.hl16(), this.m_memory.in(this.bc16()));
         this.m_b8 = this.m_b8 - 1 & 255;
         this.dec16hl();
         if(this.m_b8 == 0) {
            var4 = true;
         } else {
            var4 = false;
         }

         this.m_zeroF = var4;
         this.m_addsubtractF = true;
         break;
      case 171:
         this.m_tstates += 16;
         this.m_b8 = this.m_b8 - 1 & 255;
         this.m_memory.out(this.bc16(), this.m_memory.read8(this.hl16()));
         this.dec16hl();
         if(this.m_b8 == 0) {
            var4 = true;
         } else {
            var4 = false;
         }

         this.m_zeroF = var4;
         this.m_addsubtractF = true;
         break;
      case 176:
         this.m_tstates += 16;
         var1 = this.m_memory.read8(this.hl16());
         this.m_memory.write8(this.de16(), var1);
         this.inc16hl();
         this.inc16de();
         this.dec16bc();
         if(this.bc16() != 0) {
            var4 = true;
         } else {
            var4 = false;
         }

         this.m_parityoverflowF = var4;
         this.m_halfcarryF = false;
         this.m_addsubtractF = false;
         if(this.m_parityoverflowF) {
            this.m_tstates += 5;
            this.m_pc16 = this.decdec16(this.m_pc16);
         }

         var1 += this.m_a8;
         if((var1 & 8) != 0) {
            var4 = true;
         } else {
            var4 = false;
         }

         this.m_3F = var4;
         if((var1 & 1) != 0) {
            var4 = true;
         } else {
            var4 = false;
         }

         this.m_5F = var4;
         break;
      case 177:
         this.m_tstates += 16;
         var2 = this.m_memory.read8(this.hl16());
         this.cmp_a_special(var2);
         this.inc16hl();
         this.dec16bc();
         if(this.bc16() != 0) {
            var4 = true;
         } else {
            var4 = false;
         }

         this.m_parityoverflowF = var4;
         if(this.m_parityoverflowF && !this.m_zeroF) {
            this.m_tstates += 5;
            this.m_pc16 = this.decdec16(this.m_pc16);
         }

         var3 = this.m_a8;
         if(this.m_halfcarryF) {
            var5 = 1;
         } else {
            var5 = 0;
         }

         var1 = var3 - var2 - var5;
         if((var1 & 8) != 0) {
            var4 = true;
         } else {
            var4 = false;
         }

         this.m_3F = var4;
         if((var1 & 1) != 0) {
            var4 = true;
         } else {
            var4 = false;
         }

         this.m_5F = var4;
         break;
      case 178:
         this.m_tstates += 16;
         this.m_memory.write8(this.hl16(), this.m_memory.in(this.bc16()));
         this.m_b8 = this.m_b8 - 1 & 255;
         this.inc16hl();
         if(this.m_b8 == 0) {
            var4 = true;
         } else {
            var4 = false;
         }

         this.m_zeroF = var4;
         this.m_addsubtractF = true;
         if(!this.m_zeroF) {
            this.m_tstates += 5;
            this.m_pc16 = this.decdec16(this.m_pc16);
         }
         break;
      case 179:
         this.m_tstates += 16;
         this.m_b8 = this.m_b8 - 1 & 255;
         this.m_memory.out(this.bc16(), this.m_memory.read8(this.hl16()));
         this.inc16hl();
         if(this.m_b8 == 0) {
            var4 = true;
         } else {
            var4 = false;
         }

         this.m_zeroF = var4;
         this.m_addsubtractF = true;
         if(!this.m_zeroF) {
            this.m_tstates += 5;
            this.m_pc16 = this.decdec16(this.m_pc16);
         }
         break;
      case 184:
         this.m_tstates += 16;
         var1 = this.m_memory.read8(this.hl16());
         this.m_memory.write8(this.de16(), var1);
         this.dec16hl();
         this.dec16de();
         this.dec16bc();
         if(this.bc16() != 0) {
            var4 = true;
         } else {
            var4 = false;
         }

         this.m_parityoverflowF = var4;
         this.m_halfcarryF = false;
         this.m_addsubtractF = false;
         if(this.m_parityoverflowF) {
            this.m_tstates += 5;
            this.m_pc16 = this.decdec16(this.m_pc16);
         }

         var1 += this.m_a8;
         if((var1 & 8) != 0) {
            var4 = true;
         } else {
            var4 = false;
         }

         this.m_3F = var4;
         if((var1 & 1) != 0) {
            var4 = true;
         } else {
            var4 = false;
         }

         this.m_5F = var4;
         break;
      case 185:
         this.m_tstates += 16;
         var3 = this.m_memory.read8(this.hl16());
         this.cmp_a_special(var3);
         this.dec16hl();
         this.dec16bc();
         if(this.bc16() != 0) {
            var4 = true;
         } else {
            var4 = false;
         }

         this.m_parityoverflowF = var4;
         if(this.m_parityoverflowF && !this.m_zeroF) {
            this.m_tstates += 5;
            this.m_pc16 = this.decdec16(this.m_pc16);
         }

         var2 = this.m_a8;
         if(this.m_halfcarryF) {
            var5 = 1;
         } else {
            var5 = 0;
         }

         var1 = var2 - var3 - var5;
         if((var1 & 8) != 0) {
            var4 = true;
         } else {
            var4 = false;
         }

         this.m_3F = var4;
         if((var1 & 1) != 0) {
            var4 = true;
         } else {
            var4 = false;
         }

         this.m_5F = var4;
         break;
      case 186:
         this.m_tstates += 16;
         this.m_memory.write8(this.hl16(), this.m_memory.in(this.bc16()));
         this.m_b8 = this.m_b8 - 1 & 255;
         this.dec16hl();
         if(this.m_b8 == 0) {
            var4 = true;
         } else {
            var4 = false;
         }

         this.m_zeroF = var4;
         this.m_addsubtractF = true;
         if(!this.m_zeroF) {
            this.m_tstates += 5;
            this.m_pc16 = this.decdec16(this.m_pc16);
         }
         break;
      case 187:
         this.m_tstates += 16;
         this.m_b8 = this.m_b8 - 1 & 255;
         this.m_memory.out(this.bc16(), this.m_memory.read8(this.hl16()));
         this.dec16hl();
         if(this.m_b8 == 0) {
            var4 = true;
         } else {
            var4 = false;
         }

         this.m_zeroF = var4;
         this.m_addsubtractF = true;
         if(!this.m_zeroF) {
            this.m_tstates += 5;
            this.m_pc16 = this.decdec16(this.m_pc16);
         }
         break;
      case 251:
         this.m_tstates += 8;
         break;
      case 252:
         this.m_tstates += 8;
         break;
      case 253:
         this.m_tstates += 8;
         break;
      case 254:
         if(this.patch != null) {
            this.patch.exec(this.m_pc16 - 2);
            break;
         }
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
      case 172:
      case 173:
      case 174:
      case 175:
      case 180:
      case 181:
      case 182:
      case 183:
      case 188:
      case 189:
      case 190:
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
      case 203:
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
      case 225:
      case 226:
      case 227:
      case 228:
      case 229:
      case 230:
      case 231:
      case 232:
      case 233:
      case 234:
      case 235:
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
      case 249:
      case 250:
      default:
         this.m_tstates += 8;
         System.out.println("Unimplemented instruction: " + this.m_memory.read8(this.decdec16(this.m_pc16)) + " " + this.m_memory.read8(this.dec16(this.m_pc16)) + " at " + this.decdec16(this.m_pc16));
      }

   }

   private void decodeXXCB(int var1) {
      byte var3 = (byte)this.m_memory.read8arg(this.inc16pc());
      this.m_xx16 = this.add16(this.m_xx16, var3);
      int var2;
      switch(this.m_memory.read8opc(this.inc16pc())) {
      case 0:
         this.m_tstates += 23;
         this.m_b8 = this.m_memory.read8(this.m_xx16);
         this.m_b8 = this.rlc8(this.m_b8);
         this.m_memory.write8(this.m_xx16, this.m_b8);
         break;
      case 1:
         this.m_tstates += 23;
         this.m_c8 = this.m_memory.read8(this.m_xx16);
         this.m_c8 = this.rlc8(this.m_c8);
         this.m_memory.write8(this.m_xx16, this.m_c8);
         break;
      case 2:
         this.m_tstates += 23;
         this.m_d8 = this.m_memory.read8(this.m_xx16);
         this.m_d8 = this.rlc8(this.m_d8);
         this.m_memory.write8(this.m_xx16, this.m_d8);
         break;
      case 3:
         this.m_tstates += 23;
         this.m_e8 = this.m_memory.read8(this.m_xx16);
         this.m_e8 = this.rlc8(this.m_e8);
         this.m_memory.write8(this.m_xx16, this.m_e8);
         break;
      case 4:
         this.m_tstates += 23;
         this.m_h8 = this.m_memory.read8(this.m_xx16);
         this.m_h8 = this.rlc8(this.m_h8);
         this.m_memory.write8(this.m_xx16, this.m_h8);
         break;
      case 5:
         this.m_tstates += 23;
         this.m_l8 = this.m_memory.read8(this.m_xx16);
         this.m_l8 = this.rlc8(this.m_l8);
         this.m_memory.write8(this.m_xx16, this.m_l8);
         break;
      case 6:
         this.m_tstates += 23;
         var2 = this.rlc8(this.m_memory.read8(this.m_xx16));
         this.m_memory.write8(this.m_xx16, var2);
         break;
      case 7:
         this.m_tstates += 23;
         this.m_a8 = this.m_memory.read8(this.m_xx16);
         this.m_a8 = this.rlc8(this.m_a8);
         this.m_memory.write8(this.m_xx16, this.m_a8);
         break;
      case 8:
         this.m_tstates += 23;
         this.m_b8 = this.m_memory.read8(this.m_xx16);
         this.m_b8 = this.rrc8(this.m_b8);
         this.m_memory.write8(this.m_xx16, this.m_b8);
         break;
      case 9:
         this.m_tstates += 23;
         this.m_c8 = this.m_memory.read8(this.m_xx16);
         this.m_c8 = this.rrc8(this.m_c8);
         this.m_memory.write8(this.m_xx16, this.m_c8);
         break;
      case 10:
         this.m_tstates += 23;
         this.m_d8 = this.m_memory.read8(this.m_xx16);
         this.m_d8 = this.rrc8(this.m_d8);
         this.m_memory.write8(this.m_xx16, this.m_d8);
         break;
      case 11:
         this.m_tstates += 23;
         this.m_e8 = this.m_memory.read8(this.m_xx16);
         this.m_e8 = this.rrc8(this.m_e8);
         this.m_memory.write8(this.m_xx16, this.m_e8);
         break;
      case 12:
         this.m_tstates += 23;
         this.m_h8 = this.m_memory.read8(this.m_xx16);
         this.m_h8 = this.rrc8(this.m_h8);
         this.m_memory.write8(this.m_xx16, this.m_h8);
         break;
      case 13:
         this.m_tstates += 23;
         this.m_l8 = this.m_memory.read8(this.m_xx16);
         this.m_l8 = this.rrc8(this.m_l8);
         this.m_memory.write8(this.m_xx16, this.m_l8);
         break;
      case 14:
         this.m_tstates += 23;
         var2 = this.rrc8(this.m_memory.read8(this.m_xx16));
         this.m_memory.write8(this.m_xx16, var2);
         break;
      case 15:
         this.m_tstates += 23;
         this.m_a8 = this.m_memory.read8(this.m_xx16);
         this.m_a8 = this.rrc8(this.m_a8);
         this.m_memory.write8(this.m_xx16, this.m_a8);
         break;
      case 16:
         this.m_tstates += 23;
         this.m_b8 = this.m_memory.read8(this.m_xx16);
         this.m_b8 = this.rl8(this.m_b8);
         this.m_memory.write8(this.m_xx16, this.m_b8);
         break;
      case 17:
         this.m_tstates += 23;
         this.m_c8 = this.m_memory.read8(this.m_xx16);
         this.m_c8 = this.rl8(this.m_c8);
         this.m_memory.write8(this.m_xx16, this.m_c8);
         break;
      case 18:
         this.m_tstates += 23;
         this.m_d8 = this.m_memory.read8(this.m_xx16);
         this.m_d8 = this.rl8(this.m_d8);
         this.m_memory.write8(this.m_xx16, this.m_d8);
         break;
      case 19:
         this.m_tstates += 23;
         this.m_e8 = this.m_memory.read8(this.m_xx16);
         this.m_e8 = this.rl8(this.m_e8);
         this.m_memory.write8(this.m_xx16, this.m_e8);
         break;
      case 20:
         this.m_tstates += 23;
         this.m_h8 = this.m_memory.read8(this.m_xx16);
         this.m_h8 = this.rl8(this.m_h8);
         this.m_memory.write8(this.m_xx16, this.m_h8);
         break;
      case 21:
         this.m_tstates += 23;
         this.m_l8 = this.m_memory.read8(this.m_xx16);
         this.m_l8 = this.rl8(this.m_l8);
         this.m_memory.write8(this.m_xx16, this.m_l8);
         break;
      case 22:
         this.m_tstates += 23;
         var2 = this.rl8(this.m_memory.read8(this.m_xx16));
         this.m_memory.write8(this.m_xx16, var2);
         break;
      case 23:
         this.m_tstates += 23;
         this.m_a8 = this.m_memory.read8(this.m_xx16);
         this.m_a8 = this.rl8(this.m_a8);
         this.m_memory.write8(this.m_xx16, this.m_a8);
         break;
      case 24:
         this.m_tstates += 23;
         this.m_b8 = this.m_memory.read8(this.m_xx16);
         this.m_b8 = this.rr8(this.m_b8);
         this.m_memory.write8(this.m_xx16, this.m_b8);
         break;
      case 25:
         this.m_tstates += 23;
         this.m_c8 = this.m_memory.read8(this.m_xx16);
         this.m_c8 = this.rr8(this.m_c8);
         this.m_memory.write8(this.m_xx16, this.m_c8);
         break;
      case 26:
         this.m_tstates += 23;
         this.m_d8 = this.m_memory.read8(this.m_xx16);
         this.m_d8 = this.rr8(this.m_d8);
         this.m_memory.write8(this.m_xx16, this.m_d8);
         break;
      case 27:
         this.m_tstates += 23;
         this.m_e8 = this.m_memory.read8(this.m_xx16);
         this.m_e8 = this.rr8(this.m_e8);
         this.m_memory.write8(this.m_xx16, this.m_e8);
         break;
      case 28:
         this.m_tstates += 23;
         this.m_h8 = this.m_memory.read8(this.m_xx16);
         this.m_h8 = this.rr8(this.m_h8);
         this.m_memory.write8(this.m_xx16, this.m_h8);
         break;
      case 29:
         this.m_tstates += 23;
         this.m_l8 = this.m_memory.read8(this.m_xx16);
         this.m_l8 = this.rr8(this.m_l8);
         this.m_memory.write8(this.m_xx16, this.m_l8);
         break;
      case 30:
         this.m_tstates += 23;
         var2 = this.rr8(this.m_memory.read8(this.m_xx16));
         this.m_memory.write8(this.m_xx16, var2);
         break;
      case 31:
         this.m_tstates += 23;
         this.m_a8 = this.m_memory.read8(this.m_xx16);
         this.m_a8 = this.rr8(this.m_a8);
         this.m_memory.write8(this.m_xx16, this.m_a8);
         break;
      case 32:
         this.m_tstates += 23;
         this.m_b8 = this.m_memory.read8(this.m_xx16);
         this.m_b8 = this.sla8(this.m_b8);
         this.m_memory.write8(this.m_xx16, this.m_b8);
         break;
      case 33:
         this.m_tstates += 23;
         this.m_c8 = this.m_memory.read8(this.m_xx16);
         this.m_c8 = this.sla8(this.m_c8);
         this.m_memory.write8(this.m_xx16, this.m_c8);
         break;
      case 34:
         this.m_tstates += 23;
         this.m_d8 = this.m_memory.read8(this.m_xx16);
         this.m_d8 = this.sla8(this.m_d8);
         this.m_memory.write8(this.m_xx16, this.m_d8);
         break;
      case 35:
         this.m_tstates += 23;
         this.m_e8 = this.m_memory.read8(this.m_xx16);
         this.m_e8 = this.sla8(this.m_e8);
         this.m_memory.write8(this.m_xx16, this.m_e8);
         break;
      case 36:
         this.m_tstates += 23;
         this.m_h8 = this.m_memory.read8(this.m_xx16);
         this.m_h8 = this.sla8(this.m_h8);
         this.m_memory.write8(this.m_xx16, this.m_h8);
         break;
      case 37:
         this.m_tstates += 23;
         this.m_l8 = this.m_memory.read8(this.m_xx16);
         this.m_l8 = this.sla8(this.m_l8);
         this.m_memory.write8(this.m_xx16, this.m_l8);
         break;
      case 38:
         this.m_tstates += 23;
         var2 = this.sla8(this.m_memory.read8(this.m_xx16));
         this.m_memory.write8(this.m_xx16, var2);
         break;
      case 39:
         this.m_tstates += 23;
         this.m_a8 = this.m_memory.read8(this.m_xx16);
         this.m_a8 = this.sla8(this.m_a8);
         this.m_memory.write8(this.m_xx16, this.m_a8);
         break;
      case 40:
         this.m_tstates += 23;
         this.m_b8 = this.m_memory.read8(this.m_xx16);
         this.m_b8 = this.sra8(this.m_b8);
         this.m_memory.write8(this.m_xx16, this.m_b8);
         break;
      case 41:
         this.m_tstates += 23;
         this.m_c8 = this.m_memory.read8(this.m_xx16);
         this.m_c8 = this.sra8(this.m_c8);
         this.m_memory.write8(this.m_xx16, this.m_c8);
         break;
      case 42:
         this.m_tstates += 23;
         this.m_d8 = this.m_memory.read8(this.m_xx16);
         this.m_d8 = this.sra8(this.m_d8);
         this.m_memory.write8(this.m_xx16, this.m_d8);
         break;
      case 43:
         this.m_tstates += 23;
         this.m_e8 = this.m_memory.read8(this.m_xx16);
         this.m_e8 = this.sra8(this.m_e8);
         this.m_memory.write8(this.m_xx16, this.m_e8);
         break;
      case 44:
         this.m_tstates += 23;
         this.m_h8 = this.m_memory.read8(this.m_xx16);
         this.m_h8 = this.sra8(this.m_h8);
         this.m_memory.write8(this.m_xx16, this.m_h8);
         break;
      case 45:
         this.m_tstates += 23;
         this.m_l8 = this.m_memory.read8(this.m_xx16);
         this.m_l8 = this.sra8(this.m_l8);
         this.m_memory.write8(this.m_xx16, this.m_l8);
         break;
      case 46:
         this.m_tstates += 23;
         var2 = this.sra8(this.m_memory.read8(this.m_xx16));
         this.m_memory.write8(this.m_xx16, var2);
         break;
      case 47:
         this.m_tstates += 23;
         this.m_a8 = this.m_memory.read8(this.m_xx16);
         this.m_a8 = this.sra8(this.m_a8);
         this.m_memory.write8(this.m_xx16, this.m_a8);
         break;
      case 48:
         this.m_tstates += 23;
         this.m_b8 = this.m_memory.read8(this.m_xx16);
         this.m_b8 = this.sli8(this.m_b8);
         this.m_memory.write8(this.m_xx16, this.m_b8);
         break;
      case 49:
         this.m_tstates += 23;
         this.m_c8 = this.m_memory.read8(this.m_xx16);
         this.m_c8 = this.sli8(this.m_c8);
         this.m_memory.write8(this.m_xx16, this.m_c8);
         break;
      case 50:
         this.m_tstates += 23;
         this.m_d8 = this.m_memory.read8(this.m_xx16);
         this.m_d8 = this.sli8(this.m_d8);
         this.m_memory.write8(this.m_xx16, this.m_d8);
         break;
      case 51:
         this.m_tstates += 23;
         this.m_e8 = this.m_memory.read8(this.m_xx16);
         this.m_e8 = this.sli8(this.m_e8);
         this.m_memory.write8(this.m_xx16, this.m_e8);
         break;
      case 52:
         this.m_tstates += 23;
         this.m_h8 = this.m_memory.read8(this.m_xx16);
         this.m_h8 = this.sli8(this.m_h8);
         this.m_memory.write8(this.m_xx16, this.m_h8);
         break;
      case 53:
         this.m_tstates += 23;
         this.m_l8 = this.m_memory.read8(this.m_xx16);
         this.m_l8 = this.sli8(this.m_l8);
         this.m_memory.write8(this.m_xx16, this.m_l8);
         break;
      case 54:
         this.m_tstates += 23;
         var2 = this.sli8(this.m_memory.read8(this.m_xx16));
         this.m_memory.write8(this.m_xx16, var2);
         break;
      case 55:
         this.m_tstates += 23;
         this.m_a8 = this.m_memory.read8(this.m_xx16);
         this.m_a8 = this.sli8(this.m_a8);
         this.m_memory.write8(this.m_xx16, this.m_a8);
         break;
      case 56:
         this.m_tstates += 23;
         this.m_b8 = this.m_memory.read8(this.m_xx16);
         this.m_b8 = this.srl8(this.m_b8);
         this.m_memory.write8(this.m_xx16, this.m_b8);
         break;
      case 57:
         this.m_tstates += 23;
         this.m_c8 = this.m_memory.read8(this.m_xx16);
         this.m_c8 = this.srl8(this.m_c8);
         this.m_memory.write8(this.m_xx16, this.m_c8);
         break;
      case 58:
         this.m_tstates += 23;
         this.m_d8 = this.m_memory.read8(this.m_xx16);
         this.m_d8 = this.srl8(this.m_d8);
         this.m_memory.write8(this.m_xx16, this.m_d8);
         break;
      case 59:
         this.m_tstates += 23;
         this.m_e8 = this.m_memory.read8(this.m_xx16);
         this.m_e8 = this.srl8(this.m_e8);
         this.m_memory.write8(this.m_xx16, this.m_e8);
         break;
      case 60:
         this.m_tstates += 23;
         this.m_h8 = this.m_memory.read8(this.m_xx16);
         this.m_h8 = this.srl8(this.m_h8);
         this.m_memory.write8(this.m_xx16, this.m_h8);
         break;
      case 61:
         this.m_tstates += 23;
         this.m_l8 = this.m_memory.read8(this.m_xx16);
         this.m_l8 = this.srl8(this.m_l8);
         this.m_memory.write8(this.m_xx16, this.m_l8);
         break;
      case 62:
         this.m_tstates += 23;
         var2 = this.srl8(this.m_memory.read8(this.m_xx16));
         this.m_memory.write8(this.m_xx16, var2);
         break;
      case 63:
         this.m_tstates += 23;
         this.m_a8 = this.m_memory.read8(this.m_xx16);
         this.m_a8 = this.srl8(this.m_a8);
         this.m_memory.write8(this.m_xx16, this.m_a8);
         break;
      case 64:
      case 65:
      case 66:
      case 67:
      case 68:
      case 69:
      case 70:
      case 71:
         this.m_tstates += 20;
         this.bit_xx(0, this.m_memory.read8(this.m_xx16));
         break;
      case 72:
      case 73:
      case 74:
      case 75:
      case 76:
      case 77:
      case 78:
      case 79:
         this.m_tstates += 20;
         this.bit_xx(1, this.m_memory.read8(this.m_xx16));
         break;
      case 80:
      case 81:
      case 82:
      case 83:
      case 84:
      case 85:
      case 86:
      case 87:
         this.m_tstates += 20;
         this.bit_xx(2, this.m_memory.read8(this.m_xx16));
         break;
      case 88:
      case 89:
      case 90:
      case 91:
      case 92:
      case 93:
      case 94:
      case 95:
         this.m_tstates += 20;
         this.bit_xx(3, this.m_memory.read8(this.m_xx16));
         break;
      case 96:
      case 97:
      case 98:
      case 99:
      case 100:
      case 101:
      case 102:
      case 103:
         this.m_tstates += 20;
         this.bit_xx(4, this.m_memory.read8(this.m_xx16));
         break;
      case 104:
      case 105:
      case 106:
      case 107:
      case 108:
      case 109:
      case 110:
      case 111:
         this.m_tstates += 20;
         this.bit_xx(5, this.m_memory.read8(this.m_xx16));
         break;
      case 112:
      case 113:
      case 114:
      case 115:
      case 116:
      case 117:
      case 118:
      case 119:
         this.m_tstates += 20;
         this.bit_xx(6, this.m_memory.read8(this.m_xx16));
         break;
      case 120:
      case 121:
      case 122:
      case 123:
      case 124:
      case 125:
      case 126:
      case 127:
         this.m_tstates += 20;
         this.bit_xx(7, this.m_memory.read8(this.m_xx16));
         break;
      case 128:
         this.m_tstates += 23;
         this.m_b8 = this.m_memory.read8(this.m_xx16) & 254;
         this.m_memory.write8(this.m_xx16, this.m_b8);
         break;
      case 129:
         this.m_tstates += 23;
         this.m_c8 = this.m_memory.read8(this.m_xx16) & 254;
         this.m_memory.write8(this.m_xx16, this.m_c8);
         break;
      case 130:
         this.m_tstates += 23;
         this.m_d8 = this.m_memory.read8(this.m_xx16) & 254;
         this.m_memory.write8(this.m_xx16, this.m_d8);
         break;
      case 131:
         this.m_tstates += 23;
         this.m_e8 = this.m_memory.read8(this.m_xx16) & 254;
         this.m_memory.write8(this.m_xx16, this.m_e8);
         break;
      case 132:
         this.m_tstates += 23;
         this.m_h8 = this.m_memory.read8(this.m_xx16) & 254;
         this.m_memory.write8(this.m_xx16, this.m_h8);
         break;
      case 133:
         this.m_tstates += 23;
         this.m_l8 = this.m_memory.read8(this.m_xx16) & 254;
         this.m_memory.write8(this.m_xx16, this.m_l8);
         break;
      case 134:
         this.m_tstates += 23;
         this.m_memory.write8(this.m_xx16, this.m_memory.read8(this.m_xx16) & 254);
         break;
      case 135:
         this.m_tstates += 23;
         this.m_a8 = this.m_memory.read8(this.m_xx16) & 254;
         this.m_memory.write8(this.m_xx16, this.m_a8);
         break;
      case 136:
         this.m_tstates += 23;
         this.m_b8 = this.m_memory.read8(this.m_xx16) & 253;
         this.m_memory.write8(this.m_xx16, this.m_b8);
         break;
      case 137:
         this.m_tstates += 23;
         this.m_c8 = this.m_memory.read8(this.m_xx16) & 253;
         this.m_memory.write8(this.m_xx16, this.m_c8);
         break;
      case 138:
         this.m_tstates += 23;
         this.m_d8 = this.m_memory.read8(this.m_xx16) & 253;
         this.m_memory.write8(this.m_xx16, this.m_d8);
         break;
      case 139:
         this.m_tstates += 23;
         this.m_e8 = this.m_memory.read8(this.m_xx16) & 253;
         this.m_memory.write8(this.m_xx16, this.m_e8);
         break;
      case 140:
         this.m_tstates += 23;
         this.m_h8 = this.m_memory.read8(this.m_xx16) & 253;
         this.m_memory.write8(this.m_xx16, this.m_h8);
         break;
      case 141:
         this.m_tstates += 23;
         this.m_l8 = this.m_memory.read8(this.m_xx16) & 253;
         this.m_memory.write8(this.m_xx16, this.m_l8);
         break;
      case 142:
         this.m_tstates += 23;
         this.m_memory.write8(this.m_xx16, this.m_memory.read8(this.m_xx16) & 253);
         break;
      case 143:
         this.m_tstates += 23;
         this.m_a8 = this.m_memory.read8(this.m_xx16) & 253;
         this.m_memory.write8(this.m_xx16, this.m_a8);
         break;
      case 144:
         this.m_tstates += 23;
         this.m_b8 = this.m_memory.read8(this.m_xx16) & 251;
         this.m_memory.write8(this.m_xx16, this.m_b8);
         break;
      case 145:
         this.m_tstates += 23;
         this.m_c8 = this.m_memory.read8(this.m_xx16) & 251;
         this.m_memory.write8(this.m_xx16, this.m_c8);
         break;
      case 146:
         this.m_tstates += 23;
         this.m_d8 = this.m_memory.read8(this.m_xx16) & 251;
         this.m_memory.write8(this.m_xx16, this.m_d8);
         break;
      case 147:
         this.m_tstates += 23;
         this.m_e8 = this.m_memory.read8(this.m_xx16) & 251;
         this.m_memory.write8(this.m_xx16, this.m_e8);
         break;
      case 148:
         this.m_tstates += 23;
         this.m_h8 = this.m_memory.read8(this.m_xx16) & 251;
         this.m_memory.write8(this.m_xx16, this.m_h8);
         break;
      case 149:
         this.m_tstates += 23;
         this.m_l8 = this.m_memory.read8(this.m_xx16) & 251;
         this.m_memory.write8(this.m_xx16, this.m_l8);
         break;
      case 150:
         this.m_tstates += 23;
         this.m_memory.write8(this.m_xx16, this.m_memory.read8(this.m_xx16) & 251);
         break;
      case 151:
         this.m_tstates += 23;
         this.m_a8 = this.m_memory.read8(this.m_xx16) & 251;
         this.m_memory.write8(this.m_xx16, this.m_a8);
         break;
      case 152:
         this.m_tstates += 23;
         this.m_b8 = this.m_memory.read8(this.m_xx16) & 247;
         this.m_memory.write8(this.m_xx16, this.m_b8);
         break;
      case 153:
         this.m_tstates += 23;
         this.m_c8 = this.m_memory.read8(this.m_xx16) & 247;
         this.m_memory.write8(this.m_xx16, this.m_c8);
         break;
      case 154:
         this.m_tstates += 23;
         this.m_d8 = this.m_memory.read8(this.m_xx16) & 247;
         this.m_memory.write8(this.m_xx16, this.m_d8);
         break;
      case 155:
         this.m_tstates += 23;
         this.m_e8 = this.m_memory.read8(this.m_xx16) & 247;
         this.m_memory.write8(this.m_xx16, this.m_e8);
         break;
      case 156:
         this.m_tstates += 23;
         this.m_h8 = this.m_memory.read8(this.m_xx16) & 247;
         this.m_memory.write8(this.m_xx16, this.m_h8);
         break;
      case 157:
         this.m_tstates += 23;
         this.m_l8 = this.m_memory.read8(this.m_xx16) & 247;
         this.m_memory.write8(this.m_xx16, this.m_l8);
         break;
      case 158:
         this.m_tstates += 23;
         this.m_memory.write8(this.m_xx16, this.m_memory.read8(this.m_xx16) & 247);
         break;
      case 159:
         this.m_tstates += 23;
         this.m_a8 = this.m_memory.read8(this.m_xx16) & 247;
         this.m_memory.write8(this.m_xx16, this.m_a8);
         break;
      case 160:
         this.m_tstates += 23;
         this.m_b8 = this.m_memory.read8(this.m_xx16) & 239;
         this.m_memory.write8(this.m_xx16, this.m_b8);
         break;
      case 161:
         this.m_tstates += 23;
         this.m_c8 = this.m_memory.read8(this.m_xx16) & 239;
         this.m_memory.write8(this.m_xx16, this.m_c8);
         break;
      case 162:
         this.m_tstates += 23;
         this.m_d8 = this.m_memory.read8(this.m_xx16) & 239;
         this.m_memory.write8(this.m_xx16, this.m_d8);
         break;
      case 163:
         this.m_tstates += 23;
         this.m_e8 = this.m_memory.read8(this.m_xx16) & 239;
         this.m_memory.write8(this.m_xx16, this.m_e8);
         break;
      case 164:
         this.m_tstates += 23;
         this.m_h8 = this.m_memory.read8(this.m_xx16) & 239;
         this.m_memory.write8(this.m_xx16, this.m_h8);
         break;
      case 165:
         this.m_tstates += 23;
         this.m_l8 = this.m_memory.read8(this.m_xx16) & 239;
         this.m_memory.write8(this.m_xx16, this.m_l8);
         break;
      case 166:
         this.m_tstates += 23;
         this.m_memory.write8(this.m_xx16, this.m_memory.read8(this.m_xx16) & 239);
         break;
      case 167:
         this.m_tstates += 23;
         this.m_a8 = this.m_memory.read8(this.m_xx16) & 239;
         this.m_memory.write8(this.m_xx16, this.m_a8);
         break;
      case 168:
         this.m_tstates += 23;
         this.m_b8 = this.m_memory.read8(this.m_xx16) & 223;
         this.m_memory.write8(this.m_xx16, this.m_b8);
         break;
      case 169:
         this.m_tstates += 23;
         this.m_c8 = this.m_memory.read8(this.m_xx16) & 223;
         this.m_memory.write8(this.m_xx16, this.m_c8);
         break;
      case 170:
         this.m_tstates += 23;
         this.m_d8 = this.m_memory.read8(this.m_xx16) & 223;
         this.m_memory.write8(this.m_xx16, this.m_d8);
         break;
      case 171:
         this.m_tstates += 23;
         this.m_e8 = this.m_memory.read8(this.m_xx16) & 223;
         this.m_memory.write8(this.m_xx16, this.m_e8);
         break;
      case 172:
         this.m_tstates += 23;
         this.m_h8 = this.m_memory.read8(this.m_xx16) & 223;
         this.m_memory.write8(this.m_xx16, this.m_h8);
         break;
      case 173:
         this.m_tstates += 23;
         this.m_l8 = this.m_memory.read8(this.m_xx16) & 223;
         this.m_memory.write8(this.m_xx16, this.m_l8);
         break;
      case 174:
         this.m_tstates += 23;
         this.m_memory.write8(this.m_xx16, this.m_memory.read8(this.m_xx16) & 223);
         break;
      case 175:
         this.m_tstates += 23;
         this.m_a8 = this.m_memory.read8(this.m_xx16) & 223;
         this.m_memory.write8(this.m_xx16, this.m_a8);
         break;
      case 176:
         this.m_tstates += 23;
         this.m_b8 = this.m_memory.read8(this.m_xx16) & 191;
         this.m_memory.write8(this.m_xx16, this.m_b8);
         break;
      case 177:
         this.m_tstates += 23;
         this.m_c8 = this.m_memory.read8(this.m_xx16) & 191;
         this.m_memory.write8(this.m_xx16, this.m_c8);
         break;
      case 178:
         this.m_tstates += 23;
         this.m_d8 = this.m_memory.read8(this.m_xx16) & 191;
         this.m_memory.write8(this.m_xx16, this.m_d8);
         break;
      case 179:
         this.m_tstates += 23;
         this.m_e8 = this.m_memory.read8(this.m_xx16) & 191;
         this.m_memory.write8(this.m_xx16, this.m_e8);
         break;
      case 180:
         this.m_tstates += 23;
         this.m_h8 = this.m_memory.read8(this.m_xx16) & 191;
         this.m_memory.write8(this.m_xx16, this.m_h8);
         break;
      case 181:
         this.m_tstates += 23;
         this.m_l8 = this.m_memory.read8(this.m_xx16) & 191;
         this.m_memory.write8(this.m_xx16, this.m_l8);
         break;
      case 182:
         this.m_tstates += 23;
         this.m_memory.write8(this.m_xx16, this.m_memory.read8(this.m_xx16) & 191);
         break;
      case 183:
         this.m_tstates += 23;
         this.m_a8 = this.m_memory.read8(this.m_xx16) & 191;
         this.m_memory.write8(this.m_xx16, this.m_a8);
         break;
      case 184:
         this.m_tstates += 23;
         this.m_b8 = this.m_memory.read8(this.m_xx16) & 127;
         this.m_memory.write8(this.m_xx16, this.m_b8);
         break;
      case 185:
         this.m_tstates += 23;
         this.m_c8 = this.m_memory.read8(this.m_xx16) & 127;
         this.m_memory.write8(this.m_xx16, this.m_c8);
         break;
      case 186:
         this.m_tstates += 23;
         this.m_d8 = this.m_memory.read8(this.m_xx16) & 127;
         this.m_memory.write8(this.m_xx16, this.m_d8);
         break;
      case 187:
         this.m_tstates += 23;
         this.m_e8 = this.m_memory.read8(this.m_xx16) & 127;
         this.m_memory.write8(this.m_xx16, this.m_e8);
         break;
      case 188:
         this.m_tstates += 23;
         this.m_h8 = this.m_memory.read8(this.m_xx16) & 127;
         this.m_memory.write8(this.m_xx16, this.m_h8);
         break;
      case 189:
         this.m_tstates += 23;
         this.m_l8 = this.m_memory.read8(this.m_xx16) & 127;
         this.m_memory.write8(this.m_xx16, this.m_l8);
         break;
      case 190:
         this.m_tstates += 23;
         this.m_memory.write8(this.m_xx16, this.m_memory.read8(this.m_xx16) & 127);
         break;
      case 191:
         this.m_tstates += 23;
         this.m_a8 = this.m_memory.read8(this.m_xx16) & 127;
         this.m_memory.write8(this.m_xx16, this.m_a8);
         break;
      case 192:
         this.m_tstates += 23;
         this.m_b8 = this.m_memory.read8(this.m_xx16) | 1;
         this.m_memory.write8(this.m_xx16, this.m_b8);
         break;
      case 193:
         this.m_tstates += 23;
         this.m_c8 = this.m_memory.read8(this.m_xx16) | 1;
         this.m_memory.write8(this.m_xx16, this.m_c8);
         break;
      case 194:
         this.m_tstates += 23;
         this.m_d8 = this.m_memory.read8(this.m_xx16) | 1;
         this.m_memory.write8(this.m_xx16, this.m_d8);
         break;
      case 195:
         this.m_tstates += 23;
         this.m_e8 = this.m_memory.read8(this.m_xx16) | 1;
         this.m_memory.write8(this.m_xx16, this.m_e8);
         break;
      case 196:
         this.m_tstates += 23;
         this.m_h8 = this.m_memory.read8(this.m_xx16) | 1;
         this.m_memory.write8(this.m_xx16, this.m_h8);
         break;
      case 197:
         this.m_tstates += 23;
         this.m_l8 = this.m_memory.read8(this.m_xx16) | 1;
         this.m_memory.write8(this.m_xx16, this.m_l8);
         break;
      case 198:
         this.m_tstates += 23;
         this.m_memory.write8(this.m_xx16, this.m_memory.read8(this.m_xx16) | 1);
         break;
      case 199:
         this.m_tstates += 23;
         this.m_a8 = this.m_memory.read8(this.m_xx16) | 1;
         this.m_memory.write8(this.m_xx16, this.m_a8);
         break;
      case 200:
         this.m_tstates += 23;
         this.m_b8 = this.m_memory.read8(this.m_xx16) | 2;
         this.m_memory.write8(this.m_xx16, this.m_b8);
         break;
      case 201:
         this.m_tstates += 23;
         this.m_c8 = this.m_memory.read8(this.m_xx16) | 2;
         this.m_memory.write8(this.m_xx16, this.m_c8);
         break;
      case 202:
         this.m_tstates += 23;
         this.m_d8 = this.m_memory.read8(this.m_xx16) | 2;
         this.m_memory.write8(this.m_xx16, this.m_d8);
         break;
      case 203:
         this.m_tstates += 23;
         this.m_e8 = this.m_memory.read8(this.m_xx16) | 2;
         this.m_memory.write8(this.m_xx16, this.m_e8);
         break;
      case 204:
         this.m_tstates += 23;
         this.m_h8 = this.m_memory.read8(this.m_xx16) | 2;
         this.m_memory.write8(this.m_xx16, this.m_h8);
         break;
      case 205:
         this.m_tstates += 23;
         this.m_l8 = this.m_memory.read8(this.m_xx16) | 2;
         this.m_memory.write8(this.m_xx16, this.m_l8);
         break;
      case 206:
         this.m_tstates += 23;
         this.m_memory.write8(this.m_xx16, this.m_memory.read8(this.m_xx16) | 2);
         break;
      case 207:
         this.m_tstates += 23;
         this.m_a8 = this.m_memory.read8(this.m_xx16) | 2;
         this.m_memory.write8(this.m_xx16, this.m_a8);
         break;
      case 208:
         this.m_tstates += 23;
         this.m_b8 = this.m_memory.read8(this.m_xx16) | 4;
         this.m_memory.write8(this.m_xx16, this.m_b8);
         break;
      case 209:
         this.m_tstates += 23;
         this.m_c8 = this.m_memory.read8(this.m_xx16) | 4;
         this.m_memory.write8(this.m_xx16, this.m_c8);
         break;
      case 210:
         this.m_tstates += 23;
         this.m_d8 = this.m_memory.read8(this.m_xx16) | 4;
         this.m_memory.write8(this.m_xx16, this.m_d8);
         break;
      case 211:
         this.m_tstates += 23;
         this.m_e8 = this.m_memory.read8(this.m_xx16) | 4;
         this.m_memory.write8(this.m_xx16, this.m_e8);
         break;
      case 212:
         this.m_tstates += 23;
         this.m_h8 = this.m_memory.read8(this.m_xx16) | 4;
         this.m_memory.write8(this.m_xx16, this.m_h8);
         break;
      case 213:
         this.m_tstates += 23;
         this.m_l8 = this.m_memory.read8(this.m_xx16) | 4;
         this.m_memory.write8(this.m_xx16, this.m_l8);
         break;
      case 214:
         this.m_tstates += 23;
         this.m_memory.write8(this.m_xx16, this.m_memory.read8(this.m_xx16) | 4);
         break;
      case 215:
         this.m_tstates += 23;
         this.m_a8 = this.m_memory.read8(this.m_xx16) | 4;
         this.m_memory.write8(this.m_xx16, this.m_a8);
         break;
      case 216:
         this.m_tstates += 23;
         this.m_b8 = this.m_memory.read8(this.m_xx16) | 8;
         this.m_memory.write8(this.m_xx16, this.m_b8);
         break;
      case 217:
         this.m_tstates += 23;
         this.m_c8 = this.m_memory.read8(this.m_xx16) | 8;
         this.m_memory.write8(this.m_xx16, this.m_c8);
         break;
      case 218:
         this.m_tstates += 23;
         this.m_d8 = this.m_memory.read8(this.m_xx16) | 8;
         this.m_memory.write8(this.m_xx16, this.m_d8);
         break;
      case 219:
         this.m_tstates += 23;
         this.m_e8 = this.m_memory.read8(this.m_xx16) | 8;
         this.m_memory.write8(this.m_xx16, this.m_e8);
         break;
      case 220:
         this.m_tstates += 23;
         this.m_h8 = this.m_memory.read8(this.m_xx16) | 8;
         this.m_memory.write8(this.m_xx16, this.m_h8);
         break;
      case 221:
         this.m_tstates += 23;
         this.m_l8 = this.m_memory.read8(this.m_xx16) | 8;
         this.m_memory.write8(this.m_xx16, this.m_l8);
         break;
      case 222:
         this.m_tstates += 23;
         this.m_memory.write8(this.m_xx16, this.m_memory.read8(this.m_xx16) | 8);
         break;
      case 223:
         this.m_tstates += 23;
         this.m_a8 = this.m_memory.read8(this.m_xx16) | 8;
         this.m_memory.write8(this.m_xx16, this.m_a8);
         break;
      case 224:
         this.m_tstates += 23;
         this.m_b8 = this.m_memory.read8(this.m_xx16) | 16;
         this.m_memory.write8(this.m_xx16, this.m_b8);
         break;
      case 225:
         this.m_tstates += 23;
         this.m_c8 = this.m_memory.read8(this.m_xx16) | 16;
         this.m_memory.write8(this.m_xx16, this.m_c8);
         break;
      case 226:
         this.m_tstates += 23;
         this.m_d8 = this.m_memory.read8(this.m_xx16) | 16;
         this.m_memory.write8(this.m_xx16, this.m_d8);
         break;
      case 227:
         this.m_tstates += 23;
         this.m_e8 = this.m_memory.read8(this.m_xx16) | 16;
         this.m_memory.write8(this.m_xx16, this.m_e8);
         break;
      case 228:
         this.m_tstates += 23;
         this.m_h8 = this.m_memory.read8(this.m_xx16) | 16;
         this.m_memory.write8(this.m_xx16, this.m_h8);
         break;
      case 229:
         this.m_tstates += 23;
         this.m_l8 = this.m_memory.read8(this.m_xx16) | 16;
         this.m_memory.write8(this.m_xx16, this.m_l8);
         break;
      case 230:
         this.m_tstates += 23;
         this.m_memory.write8(this.m_xx16, this.m_memory.read8(this.m_xx16) | 16);
         break;
      case 231:
         this.m_tstates += 23;
         this.m_a8 = this.m_memory.read8(this.m_xx16) | 16;
         this.m_memory.write8(this.m_xx16, this.m_a8);
         break;
      case 232:
         this.m_tstates += 23;
         this.m_b8 = this.m_memory.read8(this.m_xx16) | 32;
         this.m_memory.write8(this.m_xx16, this.m_b8);
         break;
      case 233:
         this.m_tstates += 23;
         this.m_c8 = this.m_memory.read8(this.m_xx16) | 32;
         this.m_memory.write8(this.m_xx16, this.m_c8);
         break;
      case 234:
         this.m_tstates += 23;
         this.m_d8 = this.m_memory.read8(this.m_xx16) | 32;
         this.m_memory.write8(this.m_xx16, this.m_d8);
         break;
      case 235:
         this.m_tstates += 23;
         this.m_e8 = this.m_memory.read8(this.m_xx16) | 32;
         this.m_memory.write8(this.m_xx16, this.m_e8);
         break;
      case 236:
         this.m_tstates += 23;
         this.m_h8 = this.m_memory.read8(this.m_xx16) | 32;
         this.m_memory.write8(this.m_xx16, this.m_h8);
         break;
      case 237:
         this.m_tstates += 23;
         this.m_l8 = this.m_memory.read8(this.m_xx16) | 32;
         this.m_memory.write8(this.m_xx16, this.m_l8);
         break;
      case 238:
         this.m_tstates += 23;
         this.m_memory.write8(this.m_xx16, this.m_memory.read8(this.m_xx16) | 32);
         break;
      case 239:
         this.m_tstates += 23;
         this.m_a8 = this.m_memory.read8(this.m_xx16) | 32;
         this.m_memory.write8(this.m_xx16, this.m_a8);
         break;
      case 240:
         this.m_tstates += 23;
         this.m_b8 = this.m_memory.read8(this.m_xx16) | 64;
         this.m_memory.write8(this.m_xx16, this.m_b8);
         break;
      case 241:
         this.m_tstates += 23;
         this.m_c8 = this.m_memory.read8(this.m_xx16) | 64;
         this.m_memory.write8(this.m_xx16, this.m_c8);
         break;
      case 242:
         this.m_tstates += 23;
         this.m_d8 = this.m_memory.read8(this.m_xx16) | 64;
         this.m_memory.write8(this.m_xx16, this.m_d8);
         break;
      case 243:
         this.m_tstates += 23;
         this.m_e8 = this.m_memory.read8(this.m_xx16) | 64;
         this.m_memory.write8(this.m_xx16, this.m_e8);
         break;
      case 244:
         this.m_tstates += 23;
         this.m_h8 = this.m_memory.read8(this.m_xx16) | 64;
         this.m_memory.write8(this.m_xx16, this.m_h8);
         break;
      case 245:
         this.m_tstates += 23;
         this.m_l8 = this.m_memory.read8(this.m_xx16) | 64;
         this.m_memory.write8(this.m_xx16, this.m_l8);
         break;
      case 246:
         this.m_tstates += 23;
         this.m_memory.write8(this.m_xx16, this.m_memory.read8(this.m_xx16) | 64);
         break;
      case 247:
         this.m_tstates += 23;
         this.m_a8 = this.m_memory.read8(this.m_xx16) | 64;
         this.m_memory.write8(this.m_xx16, this.m_a8);
         break;
      case 248:
         this.m_tstates += 23;
         this.m_b8 = this.m_memory.read8(this.m_xx16) | 128;
         this.m_memory.write8(this.m_xx16, this.m_b8);
         break;
      case 249:
         this.m_tstates += 23;
         this.m_c8 = this.m_memory.read8(this.m_xx16) | 128;
         this.m_memory.write8(this.m_xx16, this.m_c8);
         break;
      case 250:
         this.m_tstates += 23;
         this.m_d8 = this.m_memory.read8(this.m_xx16) | 128;
         this.m_memory.write8(this.m_xx16, this.m_d8);
         break;
      case 251:
         this.m_tstates += 23;
         this.m_e8 = this.m_memory.read8(this.m_xx16) | 128;
         this.m_memory.write8(this.m_xx16, this.m_e8);
         break;
      case 252:
         this.m_tstates += 23;
         this.m_h8 = this.m_memory.read8(this.m_xx16) | 128;
         this.m_memory.write8(this.m_xx16, this.m_h8);
         break;
      case 253:
         this.m_tstates += 23;
         this.m_l8 = this.m_memory.read8(this.m_xx16) | 128;
         this.m_memory.write8(this.m_xx16, this.m_l8);
         break;
      case 254:
         this.m_tstates += 23;
         this.m_memory.write8(this.m_xx16, this.m_memory.read8(this.m_xx16) | 128);
         break;
      case 255:
         this.m_tstates += 23;
         this.m_a8 = this.m_memory.read8(this.m_xx16) | 128;
         this.m_memory.write8(this.m_xx16, this.m_a8);
      }

      this.m_xx16 = this.sub16(this.m_xx16, var3);
   }

   private int getState(byte[] var1, int var2) {
      return (var1[var2] & 255) << 8 | var1[var2 + 1] & 255;
   }

   private int hl16() {
      return this.m_h8 << 8 | this.m_l8;
   }

   private void hl16(int var1) {
      this.m_h8 = var1 >> 8;
      this.m_l8 = var1 & 255;
   }

   private int in8(int var1) {
      var1 = this.m_memory.in(var1);
      boolean var2;
      if((var1 & 128) != 0) {
         var2 = true;
      } else {
         var2 = false;
      }

      this.m_signF = var2;
      if(var1 == 0) {
         var2 = true;
      } else {
         var2 = false;
      }

      this.m_zeroF = var2;
      this.m_halfcarryF = false;
      this.m_parityoverflowF = m_parityTable[var1];
      this.m_addsubtractF = false;
      if((var1 & 8) != 0) {
         var2 = true;
      } else {
         var2 = false;
      }

      this.m_3F = var2;
      if((var1 & 32) != 0) {
         var2 = true;
      } else {
         var2 = false;
      }

      this.m_5F = var2;
      return var1;
   }

   private int inc16(int var1) {
      return var1 + 1 & '\uffff';
   }

   private int inc16bc() {
      int var1 = this.bc16();
      this.bc16(var1 + 1 & '\uffff');
      return var1;
   }

   private int inc16de() {
      int var1 = this.de16();
      this.de16(var1 + 1 & '\uffff');
      return var1;
   }

   private int inc16hl() {
      int var1 = this.hl16();
      this.hl16(var1 + 1 & '\uffff');
      return var1;
   }

   private int inc16pc() {
      int var1 = this.m_pc16;
      this.m_pc16 = this.m_pc16 + 1 & '\uffff';
      return var1;
   }

   private int inc16sp() {
      int var1 = this.m_sp16;
      this.m_sp16 = this.m_sp16 + 1 & '\uffff';
      return var1;
   }

   private int inc16xx() {
      int var1 = this.m_xx16;
      this.m_xx16 = this.m_xx16 + 1 & '\uffff';
      return var1;
   }

   private int inc8(int var1) {
      var1 = var1 + 1 & 255;
      boolean var2;
      if((var1 & 128) != 0) {
         var2 = true;
      } else {
         var2 = false;
      }

      this.m_signF = var2;
      if(var1 == 0) {
         var2 = true;
      } else {
         var2 = false;
      }

      this.m_zeroF = var2;
      if((var1 & 15) == 0) {
         var2 = true;
      } else {
         var2 = false;
      }

      this.m_halfcarryF = var2;
      if(var1 == 128) {
         var2 = true;
      } else {
         var2 = false;
      }

      this.m_parityoverflowF = var2;
      this.m_addsubtractF = false;
      if((var1 & 8) != 0) {
         var2 = true;
      } else {
         var2 = false;
      }

      this.m_3F = var2;
      if((var1 & 32) != 0) {
         var2 = true;
      } else {
         var2 = false;
      }

      this.m_5F = var2;
      return var1;
   }

   private int incinc16(int var1) {
      return var1 + 2 & '\uffff';
   }

   private void jp_c() {
      this.m_tstates += 10;
      if(this.m_carryF) {
         this.m_pc16 = this.m_memory.read16arg(this.m_pc16);
      } else {
         this.m_pc16 = this.incinc16(this.m_pc16);
      }

   }

   private void jp_m() {
      this.m_tstates += 10;
      if(this.m_signF) {
         this.m_pc16 = this.m_memory.read16arg(this.m_pc16);
      } else {
         this.m_pc16 = this.incinc16(this.m_pc16);
      }

   }

   private void jp_nc() {
      this.m_tstates += 10;
      if(!this.m_carryF) {
         this.m_pc16 = this.m_memory.read16arg(this.m_pc16);
      } else {
         this.m_pc16 = this.incinc16(this.m_pc16);
      }

   }

   private void jp_nz() {
      this.m_tstates += 10;
      if(!this.m_zeroF) {
         this.m_pc16 = this.m_memory.read16arg(this.m_pc16);
      } else {
         this.m_pc16 = this.incinc16(this.m_pc16);
      }

   }

   private void jp_p() {
      this.m_tstates += 10;
      if(!this.m_signF) {
         this.m_pc16 = this.m_memory.read16arg(this.m_pc16);
      } else {
         this.m_pc16 = this.incinc16(this.m_pc16);
      }

   }

   private void jp_pe() {
      this.m_tstates += 10;
      if(this.m_parityoverflowF) {
         this.m_pc16 = this.m_memory.read16arg(this.m_pc16);
      } else {
         this.m_pc16 = this.incinc16(this.m_pc16);
      }

   }

   private void jp_po() {
      this.m_tstates += 10;
      if(!this.m_parityoverflowF) {
         this.m_pc16 = this.m_memory.read16arg(this.m_pc16);
      } else {
         this.m_pc16 = this.incinc16(this.m_pc16);
      }

   }

   private void jp_z() {
      this.m_tstates += 10;
      if(this.m_zeroF) {
         this.m_pc16 = this.m_memory.read16arg(this.m_pc16);
      } else {
         this.m_pc16 = this.incinc16(this.m_pc16);
      }

   }

   private void ld_a_special(int var1) {
      this.m_a8 = var1;
      boolean var2;
      if((this.m_a8 & 128) != 0) {
         var2 = true;
      } else {
         var2 = false;
      }

      this.m_signF = var2;
      if(this.m_a8 == 0) {
         var2 = true;
      } else {
         var2 = false;
      }

      this.m_zeroF = var2;
      this.m_halfcarryF = false;
      if(this.m_iff1b != 0) {
         var2 = true;
      } else {
         var2 = false;
      }

      this.m_parityoverflowF = var2;
      this.m_addsubtractF = false;
      if((this.m_a8 & 8) != 0) {
         var2 = true;
      } else {
         var2 = false;
      }

      this.m_3F = var2;
      if((this.m_a8 & 32) != 0) {
         var2 = true;
      } else {
         var2 = false;
      }

      this.m_5F = var2;
   }

   private int mone8() {
      this.m_r8 = this.m_r8 & 128 | this.m_r8 + 1 & 127;
      return this.m_memory.read8opc(this.inc16pc());
   }

   private void nextInstruction() {
      int var1;
      boolean var2;
      switch(this.mone8()) {
      case 0:
         this.m_tstates += 4;
         break;
      case 1:
         this.m_tstates += 10;
         this.bc16(this.m_memory.read16arg(this.m_pc16));
         this.m_pc16 = this.incinc16(this.m_pc16);
         break;
      case 2:
         this.m_tstates += 7;
         this.m_memory.write8(this.bc16(), this.m_a8);
         break;
      case 3:
         this.m_tstates += 6;
         this.inc16bc();
         break;
      case 4:
         this.m_tstates += 4;
         this.m_b8 = this.inc8(this.m_b8);
         break;
      case 5:
         this.m_tstates += 4;
         this.m_b8 = this.dec8(this.m_b8);
         break;
      case 6:
         this.m_tstates += 7;
         this.m_b8 = this.m_memory.read8arg(this.inc16pc());
         break;
      case 7:
         this.rlca();
         break;
      case 8:
         this.ex_af_af();
         break;
      case 9:
         this.m_tstates += 11;
         this.add_hl(this.bc16());
         break;
      case 10:
         this.m_tstates += 7;
         this.m_a8 = this.m_memory.read8(this.bc16());
         break;
      case 11:
         this.m_tstates += 6;
         this.dec16bc();
         break;
      case 12:
         this.m_tstates += 4;
         this.m_c8 = this.inc8(this.m_c8);
         break;
      case 13:
         this.m_tstates += 4;
         this.m_c8 = this.dec8(this.m_c8);
         break;
      case 14:
         this.m_tstates += 7;
         this.m_c8 = this.m_memory.read8arg(this.inc16pc());
         break;
      case 15:
         this.rrca();
         break;
      case 16:
         this.m_b8 = this.m_b8 - 1 & 255;
         if(this.m_b8 != 0) {
            this.m_tstates += 13;
            this.m_pc16 = this.add16(this.m_pc16, (byte)this.m_memory.read8arg(this.m_pc16) + 1);
         } else {
            this.m_tstates += 8;
            this.inc16pc();
         }
         break;
      case 17:
         this.m_tstates += 10;
         this.de16(this.m_memory.read16arg(this.m_pc16));
         this.m_pc16 = this.incinc16(this.m_pc16);
         break;
      case 18:
         this.m_tstates += 7;
         this.m_memory.write8(this.de16(), this.m_a8);
         break;
      case 19:
         this.m_tstates += 6;
         this.inc16de();
         break;
      case 20:
         this.m_tstates += 4;
         this.m_d8 = this.inc8(this.m_d8);
         break;
      case 21:
         this.m_tstates += 4;
         this.m_d8 = this.dec8(this.m_d8);
         break;
      case 22:
         this.m_tstates += 7;
         this.m_d8 = this.m_memory.read8arg(this.inc16pc());
         break;
      case 23:
         this.rla();
         break;
      case 24:
         this.m_tstates += 12;
         this.m_pc16 = this.add16(this.m_pc16, (byte)this.m_memory.read8arg(this.m_pc16) + 1);
         this.m_x8 = this.m_pc16 >> 8;
         break;
      case 25:
         this.m_tstates += 11;
         this.add_hl(this.de16());
         break;
      case 26:
         this.m_tstates += 7;
         this.m_a8 = this.m_memory.read8(this.de16());
         break;
      case 27:
         this.m_tstates += 6;
         this.dec16de();
         break;
      case 28:
         this.m_tstates += 4;
         this.m_e8 = this.inc8(this.m_e8);
         break;
      case 29:
         this.m_tstates += 4;
         this.m_e8 = this.dec8(this.m_e8);
         break;
      case 30:
         this.m_tstates += 7;
         this.m_e8 = this.m_memory.read8arg(this.inc16pc());
         break;
      case 31:
         this.rra();
         break;
      case 32:
         if(!this.m_zeroF) {
            this.m_tstates += 12;
            this.m_pc16 = this.add16(this.m_pc16, (byte)this.m_memory.read8arg(this.m_pc16) + 1);
         } else {
            this.m_tstates += 7;
            this.inc16pc();
         }
         break;
      case 33:
         this.m_tstates += 10;
         this.hl16(this.m_memory.read16arg(this.m_pc16));
         this.m_pc16 = this.incinc16(this.m_pc16);
         break;
      case 34:
         this.m_tstates += 16;
         this.m_memory.write16(this.m_memory.read16arg(this.m_pc16), this.hl16());
         this.m_pc16 = this.incinc16(this.m_pc16);
         break;
      case 35:
         this.m_tstates += 6;
         this.inc16hl();
         break;
      case 36:
         this.m_tstates += 4;
         this.m_h8 = this.inc8(this.m_h8);
         break;
      case 37:
         this.m_tstates += 4;
         this.m_h8 = this.dec8(this.m_h8);
         break;
      case 38:
         this.m_tstates += 7;
         this.m_h8 = this.m_memory.read8arg(this.inc16pc());
         break;
      case 39:
         this.daa();
         break;
      case 40:
         if(this.m_zeroF) {
            this.m_tstates += 12;
            this.m_pc16 = this.add16(this.m_pc16, (byte)this.m_memory.read8arg(this.m_pc16) + 1);
         } else {
            this.m_tstates += 7;
            this.inc16pc();
         }
         break;
      case 41:
         this.m_tstates += 11;
         this.add_hl(this.hl16());
         break;
      case 42:
         this.m_tstates += 16;
         this.hl16(this.m_memory.read16(this.m_memory.read16arg(this.m_pc16)));
         this.m_pc16 = this.incinc16(this.m_pc16);
         break;
      case 43:
         this.m_tstates += 6;
         this.dec16hl();
         break;
      case 44:
         this.m_tstates += 4;
         this.m_l8 = this.inc8(this.m_l8);
         break;
      case 45:
         this.m_tstates += 4;
         this.m_l8 = this.dec8(this.m_l8);
         break;
      case 46:
         this.m_tstates += 7;
         this.m_l8 = this.m_memory.read8arg(this.inc16pc());
         break;
      case 47:
         this.m_tstates += 4;
         this.m_a8 ^= 255;
         this.m_halfcarryF = true;
         this.m_addsubtractF = true;
         if((this.m_a8 & 8) != 0) {
            var2 = true;
         } else {
            var2 = false;
         }

         this.m_3F = var2;
         if((this.m_a8 & 32) != 0) {
            var2 = true;
         } else {
            var2 = false;
         }

         this.m_5F = var2;
         break;
      case 48:
         if(!this.m_carryF) {
            this.m_tstates += 12;
            this.m_pc16 = this.add16(this.m_pc16, (byte)this.m_memory.read8arg(this.m_pc16) + 1);
         } else {
            this.m_tstates += 7;
            this.inc16pc();
         }
         break;
      case 49:
         this.m_tstates += 10;
         this.m_sp16 = this.m_memory.read16arg(this.m_pc16);
         this.m_pc16 = this.incinc16(this.m_pc16);
         break;
      case 50:
         this.m_tstates += 13;
         this.m_memory.write8(this.m_memory.read16arg(this.m_pc16), this.m_a8);
         this.m_pc16 = this.incinc16(this.m_pc16);
         break;
      case 51:
         this.m_tstates += 6;
         this.inc16sp();
         break;
      case 52:
         this.m_tstates += 11;
         var1 = this.inc8(this.m_memory.read8(this.hl16()));
         this.m_memory.write8(this.hl16(), var1);
         break;
      case 53:
         this.m_tstates += 11;
         var1 = this.dec8(this.m_memory.read8(this.hl16()));
         this.m_memory.write8(this.hl16(), var1);
         break;
      case 54:
         this.m_tstates += 10;
         this.m_memory.write8(this.hl16(), this.m_memory.read8arg(this.inc16pc()));
         break;
      case 55:
         this.m_tstates += 4;
         this.m_halfcarryF = false;
         this.m_addsubtractF = false;
         this.m_carryF = true;
         if((this.m_a8 & 8) != 0) {
            var2 = true;
         } else {
            var2 = false;
         }

         this.m_3F = var2;
         if((this.m_a8 & 32) != 0) {
            var2 = true;
         } else {
            var2 = false;
         }

         this.m_5F = var2;
         break;
      case 56:
         if(this.m_carryF) {
            this.m_tstates += 12;
            this.m_pc16 = this.add16(this.m_pc16, (byte)this.m_memory.read8arg(this.m_pc16) + 1);
         } else {
            this.m_tstates += 7;
            this.inc16pc();
         }
         break;
      case 57:
         this.m_tstates += 11;
         this.add_hl(this.m_sp16);
         break;
      case 58:
         this.m_tstates += 13;
         this.m_a8 = this.m_memory.read8(this.m_memory.read16arg(this.m_pc16));
         this.m_pc16 = this.incinc16(this.m_pc16);
         break;
      case 59:
         this.m_tstates += 6;
         this.dec16sp();
         break;
      case 60:
         this.m_tstates += 4;
         this.m_a8 = this.inc8(this.m_a8);
         break;
      case 61:
         this.m_tstates += 4;
         this.m_a8 = this.dec8(this.m_a8);
         break;
      case 62:
         this.m_tstates += 7;
         this.m_a8 = this.m_memory.read8arg(this.inc16pc());
         break;
      case 63:
         this.m_tstates += 4;
         this.m_halfcarryF = this.m_carryF;
         this.m_addsubtractF = false;
         if(this.m_carryF) {
            var2 = false;
         } else {
            var2 = true;
         }

         this.m_carryF = var2;
         if((this.m_a8 & 8) != 0) {
            var2 = true;
         } else {
            var2 = false;
         }

         this.m_3F = var2;
         if((this.m_a8 & 32) != 0) {
            var2 = true;
         } else {
            var2 = false;
         }

         this.m_5F = var2;
         break;
      case 64:
         this.m_tstates += 4;
         break;
      case 65:
         this.m_tstates += 4;
         this.m_b8 = this.m_c8;
         break;
      case 66:
         this.m_tstates += 4;
         this.m_b8 = this.m_d8;
         break;
      case 67:
         this.m_tstates += 4;
         this.m_b8 = this.m_e8;
         break;
      case 68:
         this.m_tstates += 4;
         this.m_b8 = this.m_h8;
         break;
      case 69:
         this.m_tstates += 4;
         this.m_b8 = this.m_l8;
         break;
      case 70:
         this.m_tstates += 7;
         this.m_b8 = this.m_memory.read8(this.hl16());
         break;
      case 71:
         this.m_tstates += 4;
         this.m_b8 = this.m_a8;
         break;
      case 72:
         this.m_tstates += 4;
         this.m_c8 = this.m_b8;
         break;
      case 73:
         this.m_tstates += 4;
         break;
      case 74:
         this.m_tstates += 4;
         this.m_c8 = this.m_d8;
         break;
      case 75:
         this.m_tstates += 4;
         this.m_c8 = this.m_e8;
         break;
      case 76:
         this.m_tstates += 4;
         this.m_c8 = this.m_h8;
         break;
      case 77:
         this.m_tstates += 4;
         this.m_c8 = this.m_l8;
         break;
      case 78:
         this.m_tstates += 7;
         this.m_c8 = this.m_memory.read8(this.hl16());
         break;
      case 79:
         this.m_tstates += 4;
         this.m_c8 = this.m_a8;
         break;
      case 80:
         this.m_tstates += 4;
         this.m_d8 = this.m_b8;
         break;
      case 81:
         this.m_tstates += 4;
         this.m_d8 = this.m_c8;
         break;
      case 82:
         this.m_tstates += 4;
         break;
      case 83:
         this.m_tstates += 4;
         this.m_d8 = this.m_e8;
         break;
      case 84:
         this.m_tstates += 4;
         this.m_d8 = this.m_h8;
         break;
      case 85:
         this.m_tstates += 4;
         this.m_d8 = this.m_l8;
         break;
      case 86:
         this.m_tstates += 7;
         this.m_d8 = this.m_memory.read8(this.hl16());
         break;
      case 87:
         this.m_tstates += 4;
         this.m_d8 = this.m_a8;
         break;
      case 88:
         this.m_tstates += 4;
         this.m_e8 = this.m_b8;
         break;
      case 89:
         this.m_tstates += 4;
         this.m_e8 = this.m_c8;
         break;
      case 90:
         this.m_tstates += 4;
         this.m_e8 = this.m_d8;
         break;
      case 91:
         this.m_tstates += 4;
         break;
      case 92:
         this.m_tstates += 4;
         this.m_e8 = this.m_h8;
         break;
      case 93:
         this.m_tstates += 4;
         this.m_e8 = this.m_l8;
         break;
      case 94:
         this.m_tstates += 7;
         this.m_e8 = this.m_memory.read8(this.hl16());
         break;
      case 95:
         this.m_tstates += 4;
         this.m_e8 = this.m_a8;
         break;
      case 96:
         this.m_tstates += 4;
         this.m_h8 = this.m_b8;
         break;
      case 97:
         this.m_tstates += 4;
         this.m_h8 = this.m_c8;
         break;
      case 98:
         this.m_tstates += 4;
         this.m_h8 = this.m_d8;
         break;
      case 99:
         this.m_tstates += 4;
         this.m_h8 = this.m_e8;
         break;
      case 100:
         this.m_tstates += 4;
         break;
      case 101:
         this.m_tstates += 4;
         this.m_h8 = this.m_l8;
         break;
      case 102:
         this.m_tstates += 7;
         this.m_h8 = this.m_memory.read8(this.hl16());
         break;
      case 103:
         this.m_tstates += 4;
         this.m_h8 = this.m_a8;
         break;
      case 104:
         this.m_tstates += 4;
         this.m_l8 = this.m_b8;
         break;
      case 105:
         this.m_tstates += 4;
         this.m_l8 = this.m_c8;
         break;
      case 106:
         this.m_tstates += 4;
         this.m_l8 = this.m_d8;
         break;
      case 107:
         this.m_tstates += 4;
         this.m_l8 = this.m_e8;
         break;
      case 108:
         this.m_tstates += 4;
         this.m_l8 = this.m_h8;
         break;
      case 109:
         this.m_tstates += 4;
         break;
      case 110:
         this.m_tstates += 7;
         this.m_l8 = this.m_memory.read8(this.hl16());
         break;
      case 111:
         this.m_tstates += 4;
         this.m_l8 = this.m_a8;
         break;
      case 112:
         this.m_tstates += 7;
         this.m_memory.write8(this.hl16(), this.m_b8);
         break;
      case 113:
         this.m_tstates += 7;
         this.m_memory.write8(this.hl16(), this.m_c8);
         break;
      case 114:
         this.m_tstates += 7;
         this.m_memory.write8(this.hl16(), this.m_d8);
         break;
      case 115:
         this.m_tstates += 7;
         this.m_memory.write8(this.hl16(), this.m_e8);
         break;
      case 116:
         this.m_tstates += 7;
         this.m_memory.write8(this.hl16(), this.m_h8);
         break;
      case 117:
         this.m_tstates += 7;
         this.m_memory.write8(this.hl16(), this.m_l8);
         break;
      case 118:
         this.dec16pc();
         this.endExec();
         break;
      case 119:
         this.m_tstates += 7;
         this.m_memory.write8(this.hl16(), this.m_a8);
         break;
      case 120:
         this.m_tstates += 4;
         this.m_a8 = this.m_b8;
         break;
      case 121:
         this.m_tstates += 4;
         this.m_a8 = this.m_c8;
         break;
      case 122:
         this.m_tstates += 4;
         this.m_a8 = this.m_d8;
         break;
      case 123:
         this.m_tstates += 4;
         this.m_a8 = this.m_e8;
         break;
      case 124:
         this.m_tstates += 4;
         this.m_a8 = this.m_h8;
         break;
      case 125:
         this.m_tstates += 4;
         this.m_a8 = this.m_l8;
         break;
      case 126:
         this.m_tstates += 7;
         this.m_a8 = this.m_memory.read8(this.hl16());
         break;
      case 127:
         this.m_tstates += 4;
         break;
      case 128:
         this.m_tstates += 4;
         this.add_a(this.m_b8);
         break;
      case 129:
         this.m_tstates += 4;
         this.add_a(this.m_c8);
         break;
      case 130:
         this.m_tstates += 4;
         this.add_a(this.m_d8);
         break;
      case 131:
         this.m_tstates += 4;
         this.add_a(this.m_e8);
         break;
      case 132:
         this.m_tstates += 4;
         this.add_a(this.m_h8);
         break;
      case 133:
         this.m_tstates += 4;
         this.add_a(this.m_l8);
         break;
      case 134:
         this.m_tstates += 7;
         this.add_a(this.m_memory.read8(this.hl16()));
         break;
      case 135:
         this.m_tstates += 4;
         this.add_a(this.m_a8);
         break;
      case 136:
         this.m_tstates += 4;
         this.adc_a(this.m_b8);
         break;
      case 137:
         this.m_tstates += 4;
         this.adc_a(this.m_c8);
         break;
      case 138:
         this.m_tstates += 4;
         this.adc_a(this.m_d8);
         break;
      case 139:
         this.m_tstates += 4;
         this.adc_a(this.m_e8);
         break;
      case 140:
         this.m_tstates += 4;
         this.adc_a(this.m_h8);
         break;
      case 141:
         this.m_tstates += 4;
         this.adc_a(this.m_l8);
         break;
      case 142:
         this.m_tstates += 7;
         this.adc_a(this.m_memory.read8(this.hl16()));
         break;
      case 143:
         this.m_tstates += 4;
         this.adc_a(this.m_a8);
         break;
      case 144:
         this.m_tstates += 4;
         this.sub_a(this.m_b8);
         break;
      case 145:
         this.m_tstates += 4;
         this.sub_a(this.m_c8);
         break;
      case 146:
         this.m_tstates += 4;
         this.sub_a(this.m_d8);
         break;
      case 147:
         this.m_tstates += 4;
         this.sub_a(this.m_e8);
         break;
      case 148:
         this.m_tstates += 4;
         this.sub_a(this.m_h8);
         break;
      case 149:
         this.m_tstates += 4;
         this.sub_a(this.m_l8);
         break;
      case 150:
         this.m_tstates += 7;
         this.sub_a(this.m_memory.read8(this.hl16()));
         break;
      case 151:
         this.m_tstates += 4;
         this.sub_a(this.m_a8);
         break;
      case 152:
         this.m_tstates += 4;
         this.sbc_a(this.m_b8);
         break;
      case 153:
         this.m_tstates += 4;
         this.sbc_a(this.m_c8);
         break;
      case 154:
         this.m_tstates += 4;
         this.sbc_a(this.m_d8);
         break;
      case 155:
         this.m_tstates += 4;
         this.sbc_a(this.m_e8);
         break;
      case 156:
         this.m_tstates += 4;
         this.sbc_a(this.m_h8);
         break;
      case 157:
         this.m_tstates += 4;
         this.sbc_a(this.m_l8);
         break;
      case 158:
         this.m_tstates += 7;
         this.sbc_a(this.m_memory.read8(this.hl16()));
         break;
      case 159:
         this.m_tstates += 4;
         this.sbc_a(this.m_a8);
         break;
      case 160:
         this.m_tstates += 4;
         this.and_a(this.m_b8);
         break;
      case 161:
         this.m_tstates += 4;
         this.and_a(this.m_c8);
         break;
      case 162:
         this.m_tstates += 4;
         this.and_a(this.m_d8);
         break;
      case 163:
         this.m_tstates += 4;
         this.and_a(this.m_e8);
         break;
      case 164:
         this.m_tstates += 4;
         this.and_a(this.m_h8);
         break;
      case 165:
         this.m_tstates += 4;
         this.and_a(this.m_l8);
         break;
      case 166:
         this.m_tstates += 7;
         this.and_a(this.m_memory.read8(this.hl16()));
         break;
      case 167:
         this.m_tstates += 4;
         this.and_a(this.m_a8);
         break;
      case 168:
         this.m_tstates += 4;
         this.xor_a(this.m_b8);
         break;
      case 169:
         this.m_tstates += 4;
         this.xor_a(this.m_c8);
         break;
      case 170:
         this.m_tstates += 4;
         this.xor_a(this.m_d8);
         break;
      case 171:
         this.m_tstates += 4;
         this.xor_a(this.m_e8);
         break;
      case 172:
         this.m_tstates += 4;
         this.xor_a(this.m_h8);
         break;
      case 173:
         this.m_tstates += 4;
         this.xor_a(this.m_l8);
         break;
      case 174:
         this.m_tstates += 7;
         this.xor_a(this.m_memory.read8(this.hl16()));
         break;
      case 175:
         this.m_tstates += 4;
         this.xor_a(this.m_a8);
         break;
      case 176:
         this.m_tstates += 4;
         this.or_a(this.m_b8);
         break;
      case 177:
         this.m_tstates += 4;
         this.or_a(this.m_c8);
         break;
      case 178:
         this.m_tstates += 4;
         this.or_a(this.m_d8);
         break;
      case 179:
         this.m_tstates += 4;
         this.or_a(this.m_e8);
         break;
      case 180:
         this.m_tstates += 4;
         this.or_a(this.m_h8);
         break;
      case 181:
         this.m_tstates += 4;
         this.or_a(this.m_l8);
         break;
      case 182:
         this.m_tstates += 7;
         this.or_a(this.m_memory.read8(this.hl16()));
         break;
      case 183:
         this.m_tstates += 4;
         this.or_a(this.m_a8);
         break;
      case 184:
         this.m_tstates += 4;
         this.cmp_a(this.m_b8);
         break;
      case 185:
         this.m_tstates += 4;
         this.cmp_a(this.m_c8);
         break;
      case 186:
         this.m_tstates += 4;
         this.cmp_a(this.m_d8);
         break;
      case 187:
         this.m_tstates += 4;
         this.cmp_a(this.m_e8);
         break;
      case 188:
         this.m_tstates += 4;
         this.cmp_a(this.m_h8);
         break;
      case 189:
         this.m_tstates += 4;
         this.cmp_a(this.m_l8);
         break;
      case 190:
         this.m_tstates += 7;
         this.cmp_a(this.m_memory.read8(this.hl16()));
         break;
      case 191:
         this.m_tstates += 4;
         this.cmp_a(this.m_a8);
         break;
      case 192:
         this.m_tstates += 5;
         if(!this.m_zeroF) {
            this.m_tstates += 6;
            this.m_pc16 = this.pop16();
         }
         break;
      case 193:
         this.m_tstates += 10;
         this.bc16(this.pop16());
         break;
      case 194:
         this.jp_nz();
         break;
      case 195:
         this.m_tstates += 10;
         this.m_pc16 = this.m_memory.read16arg(this.m_pc16);
         break;
      case 196:
         this.call_nz();
         break;
      case 197:
         this.m_tstates += 11;
         this.push(this.bc16());
         break;
      case 198:
         this.m_tstates += 7;
         this.add_a(this.m_memory.read8arg(this.inc16pc()));
         break;
      case 199:
         this.m_tstates += 11;
         this.push(this.m_pc16);
         this.m_pc16 = 0;
         break;
      case 200:
         this.m_tstates += 5;
         if(this.m_zeroF) {
            this.m_tstates += 6;
            this.m_pc16 = this.pop16();
         }
         break;
      case 201:
         this.m_tstates += 10;
         this.m_pc16 = this.pop16();
         break;
      case 202:
         this.jp_z();
         break;
      case 203:
         this.decodeCB(this.mone8());
         break;
      case 204:
         this.call_z();
         break;
      case 205:
         this.m_tstates += 17;
         this.push(this.incinc16(this.m_pc16));
         this.m_pc16 = this.m_memory.read16arg(this.m_pc16);
         break;
      case 206:
         this.m_tstates += 7;
         this.adc_a(this.m_memory.read8arg(this.inc16pc()));
         break;
      case 207:
         this.m_tstates += 11;
         this.push(this.m_pc16);
         this.m_pc16 = 8;
         break;
      case 208:
         this.m_tstates += 5;
         if(!this.m_carryF) {
            this.m_tstates += 6;
            this.m_pc16 = this.pop16();
         }
         break;
      case 209:
         this.m_tstates += 10;
         this.de16(this.pop16());
         break;
      case 210:
         this.jp_nc();
         break;
      case 211:
         this.m_tstates += 11;
         this.m_memory.out(this.m_a8 << 8 | this.m_memory.read8arg(this.inc16pc()), this.m_a8);
         break;
      case 212:
         this.call_nc();
         break;
      case 213:
         this.m_tstates += 11;
         this.push(this.de16());
         break;
      case 214:
         this.m_tstates += 7;
         this.sub_a(this.m_memory.read8arg(this.inc16pc()));
         break;
      case 215:
         this.m_tstates += 11;
         this.push(this.m_pc16);
         this.m_pc16 = 16;
         break;
      case 216:
         this.m_tstates += 5;
         if(this.m_carryF) {
            this.m_tstates += 6;
            this.m_pc16 = this.pop16();
         }
         break;
      case 217:
         this.exx();
         break;
      case 218:
         this.jp_c();
         break;
      case 219:
         this.m_tstates += 11;
         this.m_a8 = this.m_memory.in(this.m_a8 << 8 | this.m_memory.read8arg(this.inc16pc()));
         break;
      case 220:
         this.call_c();
         break;
      case 221:
         var1 = this.mone8();
         this.m_xx16 = this.m_ix16;
         this.decodeXX(var1);
         this.m_ix16 = this.m_xx16;
         break;
      case 222:
         this.m_tstates += 7;
         this.sbc_a(this.m_memory.read8arg(this.inc16pc()));
         break;
      case 223:
         this.m_tstates += 11;
         this.push(this.m_pc16);
         this.m_pc16 = 24;
         break;
      case 224:
         this.m_tstates += 5;
         if(!this.m_parityoverflowF) {
            this.m_tstates += 6;
            this.m_pc16 = this.pop16();
         }
         break;
      case 225:
         this.m_tstates += 10;
         this.hl16(this.pop16());
         break;
      case 226:
         this.jp_po();
         break;
      case 227:
         this.m_tstates += 19;
         var1 = this.m_memory.read16(this.m_sp16);
         this.m_memory.write16(this.m_sp16, this.hl16());
         this.hl16(var1);
         break;
      case 228:
         this.call_po();
         break;
      case 229:
         this.m_tstates += 11;
         this.push(this.hl16());
         break;
      case 230:
         this.m_tstates += 7;
         this.and_a(this.m_memory.read8arg(this.inc16pc()));
         break;
      case 231:
         this.m_tstates += 11;
         this.push(this.m_pc16);
         this.m_pc16 = 32;
         break;
      case 232:
         this.m_tstates += 5;
         if(this.m_parityoverflowF) {
            this.m_tstates += 6;
            this.m_pc16 = this.pop16();
         }
         break;
      case 233:
         this.m_tstates += 4;
         this.m_pc16 = this.hl16();
         break;
      case 234:
         this.jp_pe();
         break;
      case 235:
         this.m_tstates += 4;
         var1 = this.de16();
         this.de16(this.hl16());
         this.hl16(var1);
         break;
      case 236:
         this.call_pe();
         break;
      case 237:
         this.decodeED(this.mone8());
         break;
      case 238:
         this.m_tstates += 7;
         this.xor_a(this.m_memory.read8arg(this.inc16pc()));
         break;
      case 239:
         this.m_tstates += 11;
         this.push(this.m_pc16);
         this.m_pc16 = 40;
         break;
      case 240:
         this.m_tstates += 5;
         if(!this.m_signF) {
            this.m_tstates += 6;
            this.m_pc16 = this.pop16();
         }
         break;
      case 241:
         this.m_tstates += 10;
         this.af16(this.pop16());
         this.retrieveFlags();
         break;
      case 242:
         this.jp_p();
         break;
      case 243:
         this.di();
         break;
      case 244:
         this.call_p();
         break;
      case 245:
         this.m_tstates += 11;
         this.storeFlags();
         this.push(this.af16());
         break;
      case 246:
         this.m_tstates += 7;
         this.or_a(this.m_memory.read8arg(this.inc16pc()));
         break;
      case 247:
         this.m_tstates += 11;
         this.push(this.m_pc16);
         this.m_pc16 = 48;
         break;
      case 248:
         this.m_tstates += 5;
         if(this.m_signF) {
            this.m_tstates += 6;
            this.m_pc16 = this.pop16();
         }
         break;
      case 249:
         this.m_tstates += 6;
         this.m_sp16 = this.hl16();
         break;
      case 250:
         this.jp_m();
         break;
      case 251:
         this.m_tstates += 4;
         this.m_iff1a = 1;
         this.m_iff1b = 1;
         this.nextInstruction();
         this.checkInterrupt();
         break;
      case 252:
         this.call_m();
         break;
      case 253:
         var1 = this.mone8();
         this.m_xx16 = this.m_iy16;
         this.decodeXX(var1);
         this.m_iy16 = this.m_xx16;
         break;
      case 254:
         this.m_tstates += 7;
         this.cmp_a(this.m_memory.read8arg(this.inc16pc()));
         break;
      case 255:
         this.m_tstates += 11;
         this.push(this.m_pc16);
         this.m_pc16 = 56;
      }

   }

   private void or_a(int var1) {
      this.m_a8 |= var1;
      boolean var2;
      if((this.m_a8 & 128) != 0) {
         var2 = true;
      } else {
         var2 = false;
      }

      this.m_signF = var2;
      if(this.m_a8 == 0) {
         var2 = true;
      } else {
         var2 = false;
      }

      this.m_zeroF = var2;
      this.m_halfcarryF = false;
      this.m_parityoverflowF = m_parityTable[this.m_a8];
      this.m_addsubtractF = false;
      this.m_carryF = false;
      if((this.m_a8 & 8) != 0) {
         var2 = true;
      } else {
         var2 = false;
      }

      this.m_3F = var2;
      if((this.m_a8 & 32) != 0) {
         var2 = true;
      } else {
         var2 = false;
      }

      this.m_5F = var2;
   }

   private void push(int var1) {
      this.m_sp16 = this.decdec16(this.m_sp16);
      this.m_memory.write16(this.m_sp16, var1);
   }

   private int rl8(int var1) {
      boolean var3;
      if((var1 & 128) != 0) {
         var3 = true;
      } else {
         var3 = false;
      }

      byte var2;
      if(this.m_carryF) {
         var2 = 1;
      } else {
         var2 = 0;
      }

      var1 = (var1 << 1 | var2) & 255;
      this.m_carryF = var3;
      this.shift_test(var1);
      return var1;
   }

   private void rla() {
      this.m_tstates += 4;
      byte var1;
      if(this.m_carryF) {
         var1 = 1;
      } else {
         var1 = 0;
      }

      boolean var2;
      if((this.m_a8 & 128) != 0) {
         var2 = true;
      } else {
         var2 = false;
      }

      this.m_carryF = var2;
      this.m_a8 = (this.m_a8 << 1 | var1) & 255;
      this.m_halfcarryF = false;
      this.m_addsubtractF = false;
      if((this.m_a8 & 8) != 0) {
         var2 = true;
      } else {
         var2 = false;
      }

      this.m_3F = var2;
      if((this.m_a8 & 32) != 0) {
         var2 = true;
      } else {
         var2 = false;
      }

      this.m_5F = var2;
   }

   private int rlc8(int var1) {
      boolean var3;
      if((var1 & 128) != 0) {
         var3 = true;
      } else {
         var3 = false;
      }

      this.m_carryF = var3;
      byte var2;
      if(this.m_carryF) {
         var2 = 1;
      } else {
         var2 = 0;
      }

      var1 = (var1 << 1 | var2) & 255;
      this.shift_test(var1);
      return var1;
   }

   private void rlca() {
      this.m_tstates += 4;
      boolean var3;
      if((this.m_a8 & 128) != 0) {
         var3 = true;
      } else {
         var3 = false;
      }

      this.m_carryF = var3;
      int var2 = this.m_a8;
      byte var1;
      if(this.m_carryF) {
         var1 = 1;
      } else {
         var1 = 0;
      }

      this.m_a8 = (var2 << 1 | var1) & 255;
      this.m_halfcarryF = false;
      this.m_addsubtractF = false;
      if((this.m_a8 & 8) != 0) {
         var3 = true;
      } else {
         var3 = false;
      }

      this.m_3F = var3;
      if((this.m_a8 & 32) != 0) {
         var3 = true;
      } else {
         var3 = false;
      }

      this.m_5F = var3;
   }

   private int rr8(int var1) {
      boolean var3;
      if((var1 & 1) != 0) {
         var3 = true;
      } else {
         var3 = false;
      }

      byte var2;
      if(this.m_carryF) {
         var2 = 1;
      } else {
         var2 = 0;
      }

      var1 = var1 >> 1 | var2 << 7;
      this.m_carryF = var3;
      this.shift_test(var1);
      return var1;
   }

   private void rra() {
      this.m_tstates += 4;
      byte var1;
      if(this.m_carryF) {
         var1 = 1;
      } else {
         var1 = 0;
      }

      boolean var2;
      if((this.m_a8 & 1) != 0) {
         var2 = true;
      } else {
         var2 = false;
      }

      this.m_carryF = var2;
      this.m_a8 = this.m_a8 >> 1 | var1 << 7;
      this.m_halfcarryF = false;
      this.m_addsubtractF = false;
      if((this.m_a8 & 8) != 0) {
         var2 = true;
      } else {
         var2 = false;
      }

      this.m_3F = var2;
      if((this.m_a8 & 32) != 0) {
         var2 = true;
      } else {
         var2 = false;
      }

      this.m_5F = var2;
   }

   private int rrc8(int var1) {
      boolean var3;
      if((var1 & 1) != 0) {
         var3 = true;
      } else {
         var3 = false;
      }

      this.m_carryF = var3;
      byte var2;
      if(this.m_carryF) {
         var2 = 1;
      } else {
         var2 = 0;
      }

      var1 = var1 >> 1 | var2 << 7;
      this.shift_test(var1);
      return var1;
   }

   private void rrca() {
      this.m_tstates += 4;
      boolean var3;
      if((this.m_a8 & 1) != 0) {
         var3 = true;
      } else {
         var3 = false;
      }

      this.m_carryF = var3;
      int var2 = this.m_a8;
      byte var1;
      if(this.m_carryF) {
         var1 = 1;
      } else {
         var1 = 0;
      }

      this.m_a8 = var2 >> 1 | var1 << 7;
      this.m_halfcarryF = false;
      this.m_addsubtractF = false;
      if((this.m_a8 & 8) != 0) {
         var3 = true;
      } else {
         var3 = false;
      }

      this.m_3F = var3;
      if((this.m_a8 & 32) != 0) {
         var3 = true;
      } else {
         var3 = false;
      }

      this.m_5F = var3;
   }

   private void sbc_a(int var1) {
      int var3 = this.m_a8;
      byte var2;
      if(this.m_carryF) {
         var2 = 1;
      } else {
         var2 = 0;
      }

      int var5 = var3 - var1 - var2;
      var1 = (this.m_a8 & 136) >> 1 | (var1 & 136) >> 2 | (var5 & 136) >> 3;
      this.m_a8 = var5 & 255;
      boolean var4;
      if((this.m_a8 & 128) != 0) {
         var4 = true;
      } else {
         var4 = false;
      }

      this.m_signF = var4;
      if(this.m_a8 == 0) {
         var4 = true;
      } else {
         var4 = false;
      }

      this.m_zeroF = var4;
      this.m_halfcarryF = m_subhalfcarryTable[var1 & 7];
      this.m_parityoverflowF = m_suboverflowTable[var1 >> 4];
      this.m_addsubtractF = true;
      if((var5 & 256) != 0) {
         var4 = true;
      } else {
         var4 = false;
      }

      this.m_carryF = var4;
      if((this.m_a8 & 8) != 0) {
         var4 = true;
      } else {
         var4 = false;
      }

      this.m_3F = var4;
      if((this.m_a8 & 32) != 0) {
         var4 = true;
      } else {
         var4 = false;
      }

      this.m_5F = var4;
   }

   private void sbc_hl(int var1) {
      int var3 = this.hl16();
      byte var2;
      if(this.m_carryF) {
         var2 = 1;
      } else {
         var2 = 0;
      }

      int var5 = var3 - var1 - var2;
      var3 = (var3 & '蠀') >> 9 | (var1 & '蠀') >> 10 | (var5 & '蠀') >> 11;
      var1 = var5 & '\uffff';
      this.hl16(var1);
      boolean var4;
      if(('耀' & var1) != 0) {
         var4 = true;
      } else {
         var4 = false;
      }

      this.m_signF = var4;
      if(var1 == 0) {
         var4 = true;
      } else {
         var4 = false;
      }

      this.m_zeroF = var4;
      this.m_halfcarryF = m_subhalfcarryTable[var3 & 7];
      this.m_parityoverflowF = m_suboverflowTable[var3 >> 4];
      this.m_addsubtractF = true;
      if((65536 & var5) != 0) {
         var4 = true;
      } else {
         var4 = false;
      }

      this.m_carryF = var4;
      if((this.m_h8 & 8) != 0) {
         var4 = true;
      } else {
         var4 = false;
      }

      this.m_3F = var4;
      if((this.m_h8 & 32) != 0) {
         var4 = true;
      } else {
         var4 = false;
      }

      this.m_5F = var4;
   }

   private void setState(byte[] var1, int var2, int var3) {
      var1[var2] = (byte)(var3 >> 8 & 255);
      var1[var2 + 1] = (byte)(var3 & 255);
   }

   private void shift_test(int var1) {
      boolean var2;
      if((var1 & 128) != 0) {
         var2 = true;
      } else {
         var2 = false;
      }

      this.m_signF = var2;
      if(var1 == 0) {
         var2 = true;
      } else {
         var2 = false;
      }

      this.m_zeroF = var2;
      this.m_halfcarryF = false;
      this.m_parityoverflowF = m_parityTable[var1];
      this.m_addsubtractF = false;
      if((var1 & 8) != 0) {
         var2 = true;
      } else {
         var2 = false;
      }

      this.m_3F = var2;
      if((var1 & 32) != 0) {
         var2 = true;
      } else {
         var2 = false;
      }

      this.m_5F = var2;
   }

   private int sla8(int var1) {
      boolean var2;
      if((var1 & 128) != 0) {
         var2 = true;
      } else {
         var2 = false;
      }

      this.m_carryF = var2;
      var1 = var1 << 1 & 255;
      this.shift_test(var1);
      return var1;
   }

   private int sli8(int var1) {
      boolean var2;
      if((var1 & 128) != 0) {
         var2 = true;
      } else {
         var2 = false;
      }

      this.m_carryF = var2;
      var1 = (var1 << 1 | 1) & 255;
      this.shift_test(var1);
      return var1;
   }

   private int sra8(int var1) {
      boolean var2;
      if((var1 & 1) != 0) {
         var2 = true;
      } else {
         var2 = false;
      }

      this.m_carryF = var2;
      var1 = var1 & 128 | var1 >> 1;
      this.shift_test(var1);
      return var1;
   }

   private int srl8(int var1) {
      boolean var2;
      if((var1 & 1) != 0) {
         var2 = true;
      } else {
         var2 = false;
      }

      this.m_carryF = var2;
      var1 >>= 1;
      this.shift_test(var1);
      return var1;
   }

   private void storeFlags() {
      if(this.m_signF) {
         this.m_f8 |= 128;
      } else {
         this.m_f8 &= -129;
      }

      if(this.m_zeroF) {
         this.m_f8 |= 64;
      } else {
         this.m_f8 &= -65;
      }

      if(this.m_halfcarryF) {
         this.m_f8 |= 16;
      } else {
         this.m_f8 &= -17;
      }

      if(this.m_parityoverflowF) {
         this.m_f8 |= 4;
      } else {
         this.m_f8 &= -5;
      }

      if(this.m_addsubtractF) {
         this.m_f8 |= 2;
      } else {
         this.m_f8 &= -3;
      }

      if(this.m_carryF) {
         this.m_f8 |= 1;
      } else {
         this.m_f8 &= -2;
      }

      if(this.m_3F) {
         this.m_f8 |= 8;
      } else {
         this.m_f8 &= -9;
      }

      if(this.m_5F) {
         this.m_f8 |= 32;
      } else {
         this.m_f8 &= -33;
      }

   }

   private int sub16(int var1, int var2) {
      return var1 - var2 & '\uffff';
   }

   private void sub_a(int var1) {
      int var2 = this.m_a8 - var1;
      var1 = (this.m_a8 & 136) >> 1 | (var1 & 136) >> 2 | (var2 & 136) >> 3;
      this.m_a8 = var2 & 255;
      boolean var3;
      if((this.m_a8 & 128) != 0) {
         var3 = true;
      } else {
         var3 = false;
      }

      this.m_signF = var3;
      if(this.m_a8 == 0) {
         var3 = true;
      } else {
         var3 = false;
      }

      this.m_zeroF = var3;
      this.m_halfcarryF = m_subhalfcarryTable[var1 & 7];
      this.m_parityoverflowF = m_suboverflowTable[var1 >> 4];
      this.m_addsubtractF = true;
      if((var2 & 256) != 0) {
         var3 = true;
      } else {
         var3 = false;
      }

      this.m_carryF = var3;
      if((this.m_a8 & 8) != 0) {
         var3 = true;
      } else {
         var3 = false;
      }

      this.m_3F = var3;
      if((this.m_a8 & 32) != 0) {
         var3 = true;
      } else {
         var3 = false;
      }

      this.m_5F = var3;
   }

   private void xor_a(int var1) {
      this.m_a8 ^= var1;
      boolean var2;
      if((this.m_a8 & 128) != 0) {
         var2 = true;
      } else {
         var2 = false;
      }

      this.m_signF = var2;
      if(this.m_a8 == 0) {
         var2 = true;
      } else {
         var2 = false;
      }

      this.m_zeroF = var2;
      this.m_halfcarryF = false;
      this.m_parityoverflowF = m_parityTable[this.m_a8];
      this.m_addsubtractF = false;
      this.m_carryF = false;
      if((this.m_a8 & 8) != 0) {
         var2 = true;
      } else {
         var2 = false;
      }

      this.m_3F = var2;
      if((this.m_a8 & 32) != 0) {
         var2 = true;
      } else {
         var2 = false;
      }

      this.m_5F = var2;
   }

   private int xx16high8() {
      return this.m_xx16 >> 8;
   }

   private void xx16high8(int var1) {
      this.m_xx16 = var1 << 8 | this.m_xx16 & 255;
   }

   private int xx16low8() {
      return this.m_xx16 & 255;
   }

   private void xx16low8(int var1) {
      this.m_xx16 = this.m_xx16 & '\uff00' | var1;
   }

   public void addTStates(int var1) {
      this.m_tstates += var1;
   }

   public void decTStates() {
      --this.m_tstates;
   }

   public void decodeXX(int var1) {
      int var2;
      switch(var1) {
      case 9:
         this.m_tstates += 15;
         this.add_xx(this.bc16());
         break;
      case 25:
         this.m_tstates += 15;
         this.add_xx(this.de16());
         break;
      case 33:
         this.m_tstates += 14;
         this.m_xx16 = this.m_memory.read16arg(this.m_pc16);
         this.m_pc16 = this.incinc16(this.m_pc16);
         break;
      case 34:
         this.m_tstates += 20;
         this.m_memory.write16(this.m_memory.read16arg(this.m_pc16), this.m_xx16);
         this.m_pc16 = this.incinc16(this.m_pc16);
         break;
      case 35:
         this.m_tstates += 10;
         this.inc16xx();
         break;
      case 36:
         this.m_tstates += 8;
         this.xx16high8(this.inc8(this.xx16high8()));
         break;
      case 37:
         this.m_tstates += 8;
         this.xx16high8(this.dec8(this.xx16high8()));
         break;
      case 38:
         this.m_tstates += 11;
         this.xx16high8(this.m_memory.read8arg(this.inc16pc()));
         break;
      case 41:
         this.m_tstates += 15;
         this.add_xx(this.m_xx16);
         break;
      case 42:
         this.m_tstates += 20;
         this.m_xx16 = this.m_memory.read16(this.m_memory.read16arg(this.m_pc16));
         this.m_pc16 = this.incinc16(this.m_pc16);
         break;
      case 43:
         this.m_tstates += 10;
         this.dec16xx();
         break;
      case 44:
         this.m_tstates += 8;
         this.xx16low8(this.inc8(this.xx16low8()));
         break;
      case 45:
         this.m_tstates += 8;
         this.xx16low8(this.dec8(this.xx16low8()));
         break;
      case 46:
         this.m_tstates += 11;
         this.xx16low8(this.m_memory.read8arg(this.inc16pc()));
         break;
      case 52:
         this.m_tstates += 23;
         var2 = this.add16(this.m_xx16, (byte)this.m_memory.read8arg(this.inc16pc()));
         var1 = this.inc8(this.m_memory.read8(var2));
         this.m_memory.write8(var2, var1);
         break;
      case 53:
         this.m_tstates += 23;
         var2 = this.add16(this.m_xx16, (byte)this.m_memory.read8arg(this.inc16pc()));
         var1 = this.dec8(this.m_memory.read8(var2));
         this.m_memory.write8(var2, var1);
         break;
      case 54:
         this.m_tstates += 19;
         this.m_memory.write8(this.add16(this.m_xx16, (byte)this.m_memory.read8arg(this.m_pc16)), this.m_memory.read8arg(this.inc16(this.m_pc16)));
         this.m_pc16 = this.incinc16(this.m_pc16);
         break;
      case 57:
         this.m_tstates += 15;
         this.add_xx(this.m_sp16);
         break;
      case 68:
         this.m_tstates += 8;
         this.m_b8 = this.xx16high8();
         break;
      case 69:
         this.m_tstates += 8;
         this.m_b8 = this.xx16low8();
         break;
      case 70:
         this.m_tstates += 15;
         var1 = this.add16(this.m_xx16, (byte)this.m_memory.read8arg(this.inc16pc()));
         this.m_x8 = var1 >> 8;
         this.m_b8 = this.m_memory.read8(var1);
         break;
      case 76:
         this.m_tstates += 8;
         this.m_c8 = this.xx16high8();
         break;
      case 77:
         this.m_tstates += 8;
         this.m_c8 = this.xx16low8();
         break;
      case 78:
         this.m_tstates += 15;
         var1 = this.add16(this.m_xx16, (byte)this.m_memory.read8arg(this.inc16pc()));
         this.m_x8 = var1 >> 8;
         this.m_c8 = this.m_memory.read8(var1);
         break;
      case 84:
         this.m_tstates += 8;
         this.m_d8 = this.xx16high8();
         break;
      case 85:
         this.m_tstates += 8;
         this.m_d8 = this.xx16low8();
         break;
      case 86:
         this.m_tstates += 19;
         var1 = this.add16(this.m_xx16, (byte)this.m_memory.read8arg(this.inc16pc()));
         this.m_x8 = var1 >> 8;
         this.m_d8 = this.m_memory.read8(var1);
         break;
      case 92:
         this.m_tstates += 8;
         this.m_e8 = this.xx16high8();
         break;
      case 93:
         this.m_tstates += 8;
         this.m_e8 = this.xx16low8();
         break;
      case 94:
         this.m_tstates += 19;
         var1 = this.add16(this.m_xx16, (byte)this.m_memory.read8arg(this.inc16pc()));
         this.m_x8 = var1 >> 8;
         this.m_e8 = this.m_memory.read8(var1);
         break;
      case 96:
         this.m_tstates += 8;
         this.xx16high8(this.m_b8);
         break;
      case 97:
         this.m_tstates += 8;
         this.xx16high8(this.m_c8);
         break;
      case 98:
         this.m_tstates += 8;
         this.xx16high8(this.m_d8);
         break;
      case 99:
         this.m_tstates += 8;
         this.xx16high8(this.m_e8);
         break;
      case 100:
         this.m_tstates += 8;
         break;
      case 101:
         this.m_tstates += 8;
         this.xx16high8(this.xx16low8());
         break;
      case 102:
         this.m_tstates += 19;
         var1 = this.add16(this.m_xx16, (byte)this.m_memory.read8arg(this.inc16pc()));
         this.m_x8 = var1 >> 8;
         this.m_h8 = this.m_memory.read8(var1);
         break;
      case 103:
         this.m_tstates += 8;
         this.xx16high8(this.m_a8);
         break;
      case 104:
         this.m_tstates += 8;
         this.xx16low8(this.m_b8);
         break;
      case 105:
         this.m_tstates += 8;
         this.xx16low8(this.m_c8);
         break;
      case 106:
         this.m_tstates += 8;
         this.xx16low8(this.m_d8);
         break;
      case 107:
         this.m_tstates += 8;
         this.xx16low8(this.m_e8);
         break;
      case 108:
         this.m_tstates += 8;
         this.xx16low8(this.xx16high8());
         break;
      case 109:
         this.m_tstates += 8;
         break;
      case 110:
         this.m_tstates += 19;
         var1 = this.add16(this.m_xx16, (byte)this.m_memory.read8arg(this.inc16pc()));
         this.m_x8 = var1 >> 8;
         this.m_l8 = this.m_memory.read8(var1);
         break;
      case 111:
         this.m_tstates += 8;
         this.xx16low8(this.m_a8);
         break;
      case 112:
         this.m_tstates += 19;
         this.m_memory.write8(this.add16(this.m_xx16, (byte)this.m_memory.read8arg(this.inc16pc())), this.m_b8);
         break;
      case 113:
         this.m_tstates += 19;
         this.m_memory.write8(this.add16(this.m_xx16, (byte)this.m_memory.read8arg(this.inc16pc())), this.m_c8);
         break;
      case 114:
         this.m_tstates += 19;
         this.m_memory.write8(this.add16(this.m_xx16, (byte)this.m_memory.read8arg(this.inc16pc())), this.m_d8);
         break;
      case 115:
         this.m_tstates += 19;
         this.m_memory.write8(this.add16(this.m_xx16, (byte)this.m_memory.read8arg(this.inc16pc())), this.m_e8);
         break;
      case 116:
         this.m_tstates += 19;
         this.m_memory.write8(this.add16(this.m_xx16, (byte)this.m_memory.read8arg(this.inc16pc())), this.m_h8);
         break;
      case 117:
         this.m_tstates += 19;
         this.m_memory.write8(this.add16(this.m_xx16, (byte)this.m_memory.read8arg(this.inc16pc())), this.m_l8);
         break;
      case 119:
         this.m_tstates += 19;
         this.m_memory.write8(this.add16(this.m_xx16, (byte)this.m_memory.read8arg(this.inc16pc())), this.m_a8);
         break;
      case 124:
         this.m_tstates += 8;
         this.m_a8 = this.xx16high8();
         break;
      case 125:
         this.m_tstates += 8;
         this.m_a8 = this.xx16low8();
         break;
      case 126:
         this.m_tstates += 19;
         var1 = this.add16(this.m_xx16, (byte)this.m_memory.read8arg(this.inc16pc()));
         this.m_x8 = var1 >> 8;
         this.m_a8 = this.m_memory.read8(var1);
         break;
      case 132:
         this.m_tstates += 8;
         this.add_a(this.xx16high8());
         break;
      case 133:
         this.m_tstates += 8;
         this.add_a(this.xx16low8());
         break;
      case 134:
         this.m_tstates += 19;
         this.add_a(this.m_memory.read8(this.add16(this.m_xx16, (byte)this.m_memory.read8arg(this.inc16pc()))));
         break;
      case 140:
         this.m_tstates += 8;
         this.adc_a(this.xx16high8());
         break;
      case 141:
         this.m_tstates += 8;
         this.adc_a(this.xx16low8());
         break;
      case 142:
         this.m_tstates += 19;
         this.adc_a(this.m_memory.read8(this.add16(this.m_xx16, (byte)this.m_memory.read8arg(this.inc16pc()))));
         break;
      case 148:
         this.m_tstates += 8;
         this.sub_a(this.xx16high8());
         break;
      case 149:
         this.m_tstates += 8;
         this.sub_a(this.xx16low8());
         break;
      case 150:
         this.m_tstates += 19;
         this.sub_a(this.m_memory.read8(this.add16(this.m_xx16, (byte)this.m_memory.read8arg(this.inc16pc()))));
         break;
      case 156:
         this.m_tstates += 8;
         this.sbc_a(this.xx16high8());
         break;
      case 157:
         this.m_tstates += 8;
         this.sbc_a(this.xx16low8());
         break;
      case 158:
         this.m_tstates += 19;
         this.sbc_a(this.m_memory.read8(this.add16(this.m_xx16, (byte)this.m_memory.read8arg(this.inc16pc()))));
         break;
      case 164:
         this.m_tstates += 8;
         this.and_a(this.xx16high8());
         break;
      case 165:
         this.m_tstates += 8;
         this.and_a(this.xx16low8());
         break;
      case 166:
         this.m_tstates += 19;
         this.and_a(this.m_memory.read8(this.add16(this.m_xx16, (byte)this.m_memory.read8arg(this.inc16pc()))));
         break;
      case 172:
         this.m_tstates += 8;
         this.xor_a(this.xx16high8());
         break;
      case 173:
         this.m_tstates += 8;
         this.xor_a(this.xx16low8());
         break;
      case 174:
         this.m_tstates += 19;
         this.xor_a(this.m_memory.read8(this.add16(this.m_xx16, (byte)this.m_memory.read8arg(this.inc16pc()))));
         break;
      case 180:
         this.m_tstates += 8;
         this.or_a(this.xx16high8());
         break;
      case 181:
         this.m_tstates += 8;
         this.or_a(this.xx16low8());
         break;
      case 182:
         this.m_tstates += 19;
         this.or_a(this.m_memory.read8(this.add16(this.m_xx16, (byte)this.m_memory.read8arg(this.inc16pc()))));
         break;
      case 188:
         this.m_tstates += 8;
         this.cmp_a(this.xx16high8());
         break;
      case 189:
         this.m_tstates += 8;
         this.cmp_a(this.xx16low8());
         break;
      case 190:
         this.m_tstates += 19;
         this.cmp_a(this.m_memory.read8(this.add16(this.m_xx16, (byte)this.m_memory.read8arg(this.inc16pc()))));
         break;
      case 203:
         this.decodeXXCB(0);
         break;
      case 221:
      case 253:
         this.m_tstates += 4;
         this.dec16pc();
         break;
      case 225:
         this.m_tstates += 14;
         this.m_xx16 = this.pop16();
         break;
      case 227:
         this.m_tstates += 23;
         var1 = this.m_memory.read16(this.m_sp16);
         this.m_memory.write16(this.m_sp16, this.m_xx16);
         this.m_xx16 = var1;
         break;
      case 229:
         this.m_tstates += 15;
         this.push(this.m_xx16);
         break;
      case 233:
         this.m_tstates += 8;
         this.m_pc16 = this.m_xx16;
         break;
      case 235:
      case 237:
         this.m_tstates += 8;
         break;
      case 249:
         this.m_tstates += 10;
         this.m_sp16 = this.m_xx16;
         break;
      default:
         this.m_tstates += 4;
         this.dec16pc();
         System.out.println("Unimplemented instruction: " + this.m_memory.read8(this.dec16(this.m_pc16)) + " " + this.m_memory.read8arg(this.m_pc16) + " at " + this.dec16(this.m_pc16));
      }

   }

   public void di() {
      this.m_tstates += 4;
      this.m_iff1a = 0;
      this.m_iff1b = 0;
   }

   public void endExec() {
      this.m_tstates = 0;
   }

   public void ex_af_af() {
      this.m_tstates += 4;
      this.storeFlags();
      int var1 = this.af16();
      this.af16(this.m_af16alt);
      this.m_af16alt = var1;
      this.retrieveFlags();
   }

   public void exec(int var1) {
      this.m_tstates -= var1;
      this.checkInterrupt();

      while(this.m_tstates < 0) {
         this.nextInstruction();
      }

   }

   public void exx() {
      this.m_tstates += 4;
      int var1 = this.bc16();
      this.bc16(this.m_bc16alt);
      this.m_bc16alt = var1;
      var1 = this.de16();
      this.de16(this.m_de16alt);
      this.m_de16alt = var1;
      var1 = this.hl16();
      this.hl16(this.m_hl16alt);
      this.m_hl16alt = var1;
   }

   public int getCyclesLeft() {
      return 0;
   }

   public int getDebug() {
      return 0;
   }

   public long getInstruction() {
      return 0L;
   }

   public ByteArray getState() {
      System.out.println("Z80 getting state...");
      byte[] var2 = new byte[34];
      this.storeFlags();
      var2[0] = (byte)this.m_a8;
      var2[1] = (byte)this.m_f8;
      var2[2] = (byte)this.m_b8;
      var2[3] = (byte)this.m_c8;
      var2[4] = (byte)this.m_d8;
      var2[5] = (byte)this.m_e8;
      var2[6] = (byte)this.m_h8;
      var2[7] = (byte)this.m_l8;
      this.setState(var2, 8, this.m_af16alt);
      this.setState(var2, 10, this.m_bc16alt);
      this.setState(var2, 12, this.m_de16alt);
      this.setState(var2, 14, this.m_hl16alt);
      this.setState(var2, 16, this.m_ix16);
      this.setState(var2, 18, this.m_iy16);
      this.setState(var2, 20, this.m_xx16);
      this.setState(var2, 22, this.m_sp16);
      this.setState(var2, 24, this.m_pc16);
      var2[26] = (byte)this.m_r8;
      var2[27] = (byte)this.m_i8;
      var2[28] = (byte)this.m_im2;
      var2[29] = (byte)this.m_iff1a;
      var2[30] = (byte)this.m_iff1b;
      var2[31] = (byte)this.m_x8;
      byte var1;
      if(this.irq) {
         var1 = 1;
      } else {
         var1 = 0;
      }

      var2[32] = var1;
      if(this.nmi) {
         var1 = 1;
      } else {
         var1 = 0;
      }

      var2[33] = var1;
      System.out.println(this.toString());
      return new ByteArray(var2);
   }

   public int getTStates() {
      return this.m_tstates;
   }

   public String getTag() {
      return null;
   }

   String hex(int var1, int var2) {
      String var3;
      for(var3 = Integer.toHexString(var1); var3.length() < var2; var3 = "0" + var3) {
         ;
      }

      return var3;
   }

   public void incTStates() {
      ++this.m_tstates;
   }

   public boolean init(CpuBoard var1, int var2) {
      this.m_memory = var1;
      return false;
   }

   public void interrupt(int var1, boolean var2) {
      if(var1 == 0) {
         this.irq = var2;
      } else if(var1 == 1) {
         this.nmi = var2;
      }

   }

   public void irq() {
      if(this.m_iff1a != 0) {
         if(this.m_memory.read8opc(this.m_pc16) == 118) {
            this.inc16pc();
         }

         if(this.IRQ_SELF_ACK) {
            this.irq = false;
         }

         this.m_iff1a = 0;
         this.m_iff1b = 0;
         this.push(this.m_pc16);
         if(this.m_im2 == 2) {
            this.m_tstates += 19;
            this.m_pc16 = this.m_memory.read16(this.m_i8 << 8 | 255);
         } else {
            this.m_tstates += 13;
            this.m_pc16 = 56;
         }
      }

   }

   public void nmi() {
      this.m_tstates += 15;
      this.m_iff1b = this.m_iff1a;
      this.m_iff1a = 0;
      if(this.m_memory.read8opc(this.m_pc16) == 118) {
         this.inc16pc();
      }

      if(this.IRQ_SELF_ACK) {
         this.nmi = false;
      }

      this.push(this.m_pc16);
      this.m_pc16 = 102;
   }

   public void pause() {
      // $FF: Couldn't be decompiled
   }

   public int pop16() {
      int var1 = this.m_memory.read16(this.m_sp16);
      this.m_sp16 = this.incinc16(this.m_sp16);
      return var1;
   }

   public void reset() {
      this.m_pc16 = 0;
      this.m_i8 = 0;
      this.m_r8 = 0;
      this.m_im2 = 0;
      this.m_iff1a = 0;
      this.m_iff1b = 0;
   }

   public void restoreState(ByteArray var1) {
      System.out.println("Z80 restoring...");
      byte[] var3 = var1.getArray();
      this.m_a8 = var3[0] & 255;
      this.m_f8 = var3[1] & 255;
      this.m_b8 = var3[2] & 255;
      this.m_c8 = var3[3] & 255;
      this.m_d8 = var3[4] & 255;
      this.m_e8 = var3[5] & 255;
      this.m_h8 = var3[6] & 255;
      this.m_l8 = var3[7] & 255;
      this.m_ix16 = this.getState(var3, 16);
      this.m_iy16 = this.getState(var3, 18);
      this.m_xx16 = this.getState(var3, 20);
      this.m_sp16 = this.getState(var3, 22);
      this.m_pc16 = this.getState(var3, 24);
      this.m_r8 = var3[26] & 255;
      this.m_i8 = var3[27] & 255;
      this.m_im2 = var3[28] & 255;
      this.m_iff1a = var3[29] & 255;
      this.m_iff1b = var3[30] & 255;
      this.m_x8 = var3[31] & 255;
      boolean var2;
      if(var3[32] != 0) {
         var2 = true;
      } else {
         var2 = false;
      }

      this.irq = var2;
      if(var3[33] != 0) {
         var2 = true;
      } else {
         var2 = false;
      }

      this.nmi = var2;
      this.retrieveFlags();
      System.out.println(this.toString());
   }

   public void retrieveFlags() {
      boolean var1;
      if((this.m_f8 & 128) != 0) {
         var1 = true;
      } else {
         var1 = false;
      }

      this.m_signF = var1;
      if((this.m_f8 & 64) != 0) {
         var1 = true;
      } else {
         var1 = false;
      }

      this.m_zeroF = var1;
      if((this.m_f8 & 16) != 0) {
         var1 = true;
      } else {
         var1 = false;
      }

      this.m_halfcarryF = var1;
      if((this.m_f8 & 4) != 0) {
         var1 = true;
      } else {
         var1 = false;
      }

      this.m_parityoverflowF = var1;
      if((this.m_f8 & 2) != 0) {
         var1 = true;
      } else {
         var1 = false;
      }

      this.m_addsubtractF = var1;
      if((this.m_f8 & 1) != 0) {
         var1 = true;
      } else {
         var1 = false;
      }

      this.m_carryF = var1;
      if((this.m_f8 & 8) != 0) {
         var1 = true;
      } else {
         var1 = false;
      }

      this.m_3F = var1;
      if((this.m_f8 & 32) != 0) {
         var1 = true;
      } else {
         var1 = false;
      }

      this.m_5F = var1;
   }

   public void setDebug(int var1) {
   }

   public void setPatch(Patch var1) {
      this.patch = var1;
   }

   public void setProperty(int var1, int var2) {
   }

   public void setTStates(int var1) {
      this.m_tstates = var1;
   }

   public void setTag(String var1) {
   }

   public void stop() {
      this.m_stop = true;
   }

   public void subTStates(int var1) {
      this.m_tstates -= var1;
   }

   public String toString() {
      this.storeFlags();
      return "PC=" + this.hex(this.m_pc16, 4) + ",A=" + this.hex(this.m_a8, 2) + ",F=" + this.hex(this.m_f8, 2) + ",B=" + this.hex(this.m_b8, 2) + ",C=" + this.hex(this.m_c8, 2) + ",D=" + this.hex(this.m_d8, 2) + ",E=" + this.hex(this.m_e8, 2) + ",H=" + this.hex(this.m_h8, 2) + ",L=" + this.hex(this.m_l8, 2) + ",AF1=" + this.hex(this.m_af16alt, 4) + ",BC1=" + this.hex(this.m_bc16alt, 4) + ",DE1=" + this.hex(this.m_de16alt, 4) + ",HL1=" + this.hex(this.m_hl16alt, 4) + ",IX=" + this.hex(this.m_ix16, 4) + ",IY=" + this.hex(this.m_iy16, 4) + ",SP=" + this.hex(this.m_sp16, 4) + ",R=" + this.hex(this.m_r8, 4) + ",I=" + this.hex(this.m_i8, 4) + ",IM=" + this.hex(this.m_im2, 4) + ",IFF1=" + this.hex(this.m_iff1a, 2) + ",IFF2=" + this.hex(this.m_iff1b, 2);
   }

   public void unpause() {
      // $FF: Couldn't be decompiled
   }
}
