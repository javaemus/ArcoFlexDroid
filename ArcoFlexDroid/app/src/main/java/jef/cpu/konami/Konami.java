package jef.cpu.konami;

import jef.cpu.Cpu;
import jef.cpuboard.CpuBoard;
import jef.map.WriteHandler;

public class Konami implements Cpu {
   static final char CC_C = '\u0001';
   static final char CC_E = '\u0080';
   static final char CC_H = ' ';
   static final char CC_IF = '@';
   static final char CC_II = '\u0010';
   static final char CC_N = '\b';
   static final char CC_V = '\u0002';
   static final char CC_Z = '\u0004';
   public static final char CLEAR_LINE = '\u0000';
   public static final boolean DEBUG = false;
   public static final boolean ERRORLOG = true;
   public static final int KONAMI_CWAI = 8;
   public static final int KONAMI_FIRQ_LINE = 1;
   public static final int KONAMI_INT_FIRQ = 2;
   public static final int KONAMI_INT_IRQ = 1;
   public static final int KONAMI_INT_NMI = 4;
   public static final int KONAMI_INT_NONE = 0;
   public static final int KONAMI_IRQ_LINE = 0;
   public static final int KONAMI_LDS = 32;
   public static final int KONAMI_SYNC = 16;
   public static final char SET_LINE = '\u0001';
   static final int[] cycles1;
   static final char[] flags8d;
   static final char[] flags8i;
   int CC;
   int D;
   int DP;
   int EA;
   int PC;
   int PPC;
   int S;
   int U;
   int X;
   int Y;
   private CpuBoard bus;
   int extra_cycles;
   int int_state;
   int ireg;
   WriteHandler irq_callback;
   int[] irq_state = new int[2];
   int konami_ICount = '썐';
   WriteHandler konami_cpu_setlines_callback;
   int nmi_state;

   static {
      char[] var0 = new char[256];
      var0[0] = 4;
      var0[128] = 10;
      var0[129] = 8;
      var0[130] = 8;
      var0[131] = 8;
      var0[132] = 8;
      var0[133] = 8;
      var0[134] = 8;
      var0[135] = 8;
      var0[136] = 8;
      var0[137] = 8;
      var0[138] = 8;
      var0[139] = 8;
      var0[140] = 8;
      var0[141] = 8;
      var0[142] = 8;
      var0[143] = 8;
      var0[144] = 8;
      var0[145] = 8;
      var0[146] = 8;
      var0[147] = 8;
      var0[148] = 8;
      var0[149] = 8;
      var0[150] = 8;
      var0[151] = 8;
      var0[152] = 8;
      var0[153] = 8;
      var0[154] = 8;
      var0[155] = 8;
      var0[156] = 8;
      var0[157] = 8;
      var0[158] = 8;
      var0[159] = 8;
      var0[160] = 8;
      var0[161] = 8;
      var0[162] = 8;
      var0[163] = 8;
      var0[164] = 8;
      var0[165] = 8;
      var0[166] = 8;
      var0[167] = 8;
      var0[168] = 8;
      var0[169] = 8;
      var0[170] = 8;
      var0[171] = 8;
      var0[172] = 8;
      var0[173] = 8;
      var0[174] = 8;
      var0[175] = 8;
      var0[176] = 8;
      var0[177] = 8;
      var0[178] = 8;
      var0[179] = 8;
      var0[180] = 8;
      var0[181] = 8;
      var0[182] = 8;
      var0[183] = 8;
      var0[184] = 8;
      var0[185] = 8;
      var0[186] = 8;
      var0[187] = 8;
      var0[188] = 8;
      var0[189] = 8;
      var0[190] = 8;
      var0[191] = 8;
      var0[192] = 8;
      var0[193] = 8;
      var0[194] = 8;
      var0[195] = 8;
      var0[196] = 8;
      var0[197] = 8;
      var0[198] = 8;
      var0[199] = 8;
      var0[200] = 8;
      var0[201] = 8;
      var0[202] = 8;
      var0[203] = 8;
      var0[204] = 8;
      var0[205] = 8;
      var0[206] = 8;
      var0[207] = 8;
      var0[208] = 8;
      var0[209] = 8;
      var0[210] = 8;
      var0[211] = 8;
      var0[212] = 8;
      var0[213] = 8;
      var0[214] = 8;
      var0[215] = 8;
      var0[216] = 8;
      var0[217] = 8;
      var0[218] = 8;
      var0[219] = 8;
      var0[220] = 8;
      var0[221] = 8;
      var0[222] = 8;
      var0[223] = 8;
      var0[224] = 8;
      var0[225] = 8;
      var0[226] = 8;
      var0[227] = 8;
      var0[228] = 8;
      var0[229] = 8;
      var0[230] = 8;
      var0[231] = 8;
      var0[232] = 8;
      var0[233] = 8;
      var0[234] = 8;
      var0[235] = 8;
      var0[236] = 8;
      var0[237] = 8;
      var0[238] = 8;
      var0[239] = 8;
      var0[240] = 8;
      var0[241] = 8;
      var0[242] = 8;
      var0[243] = 8;
      var0[244] = 8;
      var0[245] = 8;
      var0[246] = 8;
      var0[247] = 8;
      var0[248] = 8;
      var0[249] = 8;
      var0[250] = 8;
      var0[251] = 8;
      var0[252] = 8;
      var0[253] = 8;
      var0[254] = 8;
      var0[255] = 8;
      flags8i = var0;
      var0 = new char[256];
      var0[0] = 4;
      var0[127] = 2;
      var0[128] = 8;
      var0[129] = 8;
      var0[130] = 8;
      var0[131] = 8;
      var0[132] = 8;
      var0[133] = 8;
      var0[134] = 8;
      var0[135] = 8;
      var0[136] = 8;
      var0[137] = 8;
      var0[138] = 8;
      var0[139] = 8;
      var0[140] = 8;
      var0[141] = 8;
      var0[142] = 8;
      var0[143] = 8;
      var0[144] = 8;
      var0[145] = 8;
      var0[146] = 8;
      var0[147] = 8;
      var0[148] = 8;
      var0[149] = 8;
      var0[150] = 8;
      var0[151] = 8;
      var0[152] = 8;
      var0[153] = 8;
      var0[154] = 8;
      var0[155] = 8;
      var0[156] = 8;
      var0[157] = 8;
      var0[158] = 8;
      var0[159] = 8;
      var0[160] = 8;
      var0[161] = 8;
      var0[162] = 8;
      var0[163] = 8;
      var0[164] = 8;
      var0[165] = 8;
      var0[166] = 8;
      var0[167] = 8;
      var0[168] = 8;
      var0[169] = 8;
      var0[170] = 8;
      var0[171] = 8;
      var0[172] = 8;
      var0[173] = 8;
      var0[174] = 8;
      var0[175] = 8;
      var0[176] = 8;
      var0[177] = 8;
      var0[178] = 8;
      var0[179] = 8;
      var0[180] = 8;
      var0[181] = 8;
      var0[182] = 8;
      var0[183] = 8;
      var0[184] = 8;
      var0[185] = 8;
      var0[186] = 8;
      var0[187] = 8;
      var0[188] = 8;
      var0[189] = 8;
      var0[190] = 8;
      var0[191] = 8;
      var0[192] = 8;
      var0[193] = 8;
      var0[194] = 8;
      var0[195] = 8;
      var0[196] = 8;
      var0[197] = 8;
      var0[198] = 8;
      var0[199] = 8;
      var0[200] = 8;
      var0[201] = 8;
      var0[202] = 8;
      var0[203] = 8;
      var0[204] = 8;
      var0[205] = 8;
      var0[206] = 8;
      var0[207] = 8;
      var0[208] = 8;
      var0[209] = 8;
      var0[210] = 8;
      var0[211] = 8;
      var0[212] = 8;
      var0[213] = 8;
      var0[214] = 8;
      var0[215] = 8;
      var0[216] = 8;
      var0[217] = 8;
      var0[218] = 8;
      var0[219] = 8;
      var0[220] = 8;
      var0[221] = 8;
      var0[222] = 8;
      var0[223] = 8;
      var0[224] = 8;
      var0[225] = 8;
      var0[226] = 8;
      var0[227] = 8;
      var0[228] = 8;
      var0[229] = 8;
      var0[230] = 8;
      var0[231] = 8;
      var0[232] = 8;
      var0[233] = 8;
      var0[234] = 8;
      var0[235] = 8;
      var0[236] = 8;
      var0[237] = 8;
      var0[238] = 8;
      var0[239] = 8;
      var0[240] = 8;
      var0[241] = 8;
      var0[242] = 8;
      var0[243] = 8;
      var0[244] = 8;
      var0[245] = 8;
      var0[246] = 8;
      var0[247] = 8;
      var0[248] = 8;
      var0[249] = 8;
      var0[250] = 8;
      var0[251] = 8;
      var0[252] = 8;
      var0[253] = 8;
      var0[254] = 8;
      var0[255] = 8;
      flags8d = var0;
      cycles1 = new int[]{1, 1, 1, 1, 1, 1, 1, 1, 4, 4, 4, 4, 5, 5, 5, 5, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 3, 3, 7, 6, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 4, 4, 3, 3, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 3, 3, 3, 3, 3, 1, 1, 1, 3, 3, 3, 3, 3, 3, 3, 3, 5, 5, 5, 5, 5, 5, 5, 5, 3, 3, 3, 3, 3, 3, 3, 3, 5, 5, 5, 5, 5, 5, 5, 5, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 5, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 6, 2, 2, 2, 4, 4, 4, 4, 4, 2, 2, 2, 2, 3, 3, 2, 1, 3, 2, 2, 11, 22, 11, 2, 4, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 2, 2, 3, 2, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
   }

   private void CHECK_IRQ_LINES() {
      if(this.irq_state[0] != 0 || this.irq_state[1] != 0) {
         this.int_state &= -17;
      }

      if(this.irq_state[1] != 0 && (this.CC & 64) == 0) {
         if((this.int_state & 8) != 0) {
            this.int_state &= -9;
            this.extra_cycles += 7;
         } else {
            this.CC &= -129;
            this.PUSHWORD(this.PC);
            this.PUSHBYTE(this.CC);
            this.extra_cycles += 10;
         }

         this.CC |= 80;
         this.PC = this.RM16('\ufff6');
         if(this.irq_callback != null) {
            this.irq_callback.write(0, 1);
         }
      } else if(this.irq_state[0] != 0 && (this.CC & 16) == 0) {
         if((this.int_state & 8) != 0) {
            this.int_state &= -9;
            this.extra_cycles += 7;
         } else {
            this.CC |= 128;
            this.PUSHWORD(this.PC);
            this.PUSHWORD(this.U);
            this.PUSHWORD(this.Y);
            this.PUSHWORD(this.X);
            this.PUSHBYTE(this.DP);
            this.PUSHBYTE(this.B());
            this.PUSHBYTE(this.A());
            this.PUSHBYTE(this.CC);
            this.extra_cycles += 19;
         }

         this.CC |= 16;
         this.PC = this.RM16('\ufff8');
         if(this.irq_callback != null) {
            this.irq_callback.write(0, 0);
         }
      }

   }

   private void absa() {
      int var1;
      if((this.A() & 128) != 0) {
         var1 = -this.A();
      } else {
         var1 = this.A();
      }

      this.CLR_NZVC();
      this.SET_FLAGS8(0, this.A(), var1);
      this.A(var1);
   }

   private void absd() {
      int var1;
      if((this.D & '耀') != 0) {
         var1 = -this.D;
      } else {
         var1 = this.D;
      }

      this.CLR_NZVC();
      this.SET_FLAGS16(0, this.D, var1);
      this.D = '\uffff' & var1;
   }

   private void abx() {
      this.X += this.B();
   }

   private void adca_di() {
      int var1 = this.DIRBYTE();
      int var2 = this.A() + var1 + (this.CC & 1);
      this.CLR_HNZVC();
      this.SET_FLAGS8(this.A(), var1, var2);
      this.SET_H(this.A(), var1, var2);
      this.A(var2);
   }

   private void adca_ex() {
      int var2 = this.EXTBYTE();
      int var1 = this.A() + var2 + (this.CC & 1);
      this.CLR_HNZVC();
      this.SET_FLAGS8(this.A(), var2, var1);
      this.SET_H(this.A(), var2, var1);
      this.A(var1);
   }

   private void adca_im() {
      int var1 = this.IMMBYTE();
      int var2 = this.A() + var1 + (this.CC & 1);
      this.CLR_HNZVC();
      this.SET_FLAGS8(this.A(), var1, var2);
      this.SET_H(this.A(), var1, var2);
      this.A(var2);
   }

   private void adca_ix() {
      int var2 = this.RM(this.EA);
      int var1 = this.A() + var2 + (this.CC & 1);
      this.CLR_HNZVC();
      this.SET_FLAGS8(this.A(), var2, var1);
      this.SET_H(this.A(), var2, var1);
      this.A(var1);
   }

   private void adcb_di() {
      int var1 = this.DIRBYTE();
      int var2 = this.B() + var1 + (this.CC & 1);
      this.CLR_HNZVC();
      this.SET_FLAGS8(this.B(), var1, var2);
      this.SET_H(this.B(), var1, var2);
      this.B(var2);
   }

   private void adcb_ex() {
      int var2 = this.EXTBYTE();
      int var1 = this.B() + var2 + (this.CC & 1);
      this.CLR_HNZVC();
      this.SET_FLAGS8(this.B(), var2, var1);
      this.SET_H(this.B(), var2, var1);
      this.B(var1);
   }

   private void adcb_im() {
      int var1 = this.IMMBYTE();
      int var2 = this.B() + var1 + (this.CC & 1);
      this.CLR_HNZVC();
      this.SET_FLAGS8(this.B(), var1, var2);
      this.SET_H(this.B(), var1, var2);
      this.B(var2);
   }

   private void adcb_ix() {
      int var2 = this.RM(this.EA);
      int var1 = this.B() + var2 + (this.CC & 1);
      this.CLR_HNZVC();
      this.SET_FLAGS8(this.B(), var2, var1);
      this.SET_H(this.B(), var2, var1);
      this.B(var1);
   }

   private void adda_di() {
      int var2 = this.DIRBYTE();
      int var1 = this.A() + var2;
      this.CLR_HNZVC();
      this.SET_FLAGS8(this.A(), var2, var1);
      this.SET_H(this.A(), var2, var1);
      this.A(var1);
   }

   private void adda_ex() {
      int var2 = this.EXTBYTE();
      int var1 = this.A() + var2;
      this.CLR_HNZVC();
      this.SET_FLAGS8(this.A(), var2, var1);
      this.SET_H(this.A(), var2, var1);
      this.A(var1);
   }

   private void adda_im() {
      int var1 = this.IMMBYTE();
      int var2 = this.A() + var1;
      this.CLR_HNZVC();
      this.SET_FLAGS8(this.A(), var1, var2);
      this.SET_H(this.A(), var1, var2);
      this.A(var2);
   }

   private void adda_ix() {
      int var2 = this.RM(this.EA);
      int var1 = this.A() + var2;
      this.CLR_HNZVC();
      this.SET_FLAGS8(this.A(), var2, var1);
      this.SET_H(this.A(), var2, var1);
      this.A(var1);
   }

   private void addb_di() {
      int var2 = this.DIRBYTE();
      int var1 = this.B() + var2;
      this.CLR_HNZVC();
      this.SET_FLAGS8(this.B(), var2, var1);
      this.SET_H(this.B(), var2, var1);
      this.B(var1);
   }

   private void addb_ex() {
      int var1 = this.EXTBYTE();
      int var2 = this.B() + var1;
      this.CLR_HNZVC();
      this.SET_FLAGS8(this.B(), var1, var2);
      this.SET_H(this.B(), var1, var2);
      this.B(var2);
   }

   private void addb_im() {
      int var2 = this.IMMBYTE();
      int var1 = this.B() + var2;
      this.CLR_HNZVC();
      this.SET_FLAGS8(this.B(), var2, var1);
      this.SET_H(this.B(), var2, var1);
      this.B(var1);
   }

   private void addb_ix() {
      int var1 = this.RM(this.EA);
      int var2 = this.B() + var1;
      this.CLR_HNZVC();
      this.SET_FLAGS8(this.B(), var1, var2);
      this.SET_H(this.B(), var1, var2);
      this.B(var2);
   }

   private void addd_di() {
      int var1 = this.DIRWORD();
      int var3 = this.D;
      int var2 = var3 + var1;
      this.CLR_NZVC();
      this.SET_FLAGS16(var3, var1, var2);
      this.D = '\uffff' & var2;
   }

   private void addd_ex() {
      int var2 = this.EXTWORD();
      int var3 = this.D;
      int var1 = var3 + var2;
      this.CLR_NZVC();
      this.SET_FLAGS16(var3, var2, var1);
      this.D = '\uffff' & var1;
   }

   private void addd_im() {
      int var1 = this.IMMWORD();
      int var2 = this.D;
      int var3 = var2 + var1;
      this.CLR_NZVC();
      this.SET_FLAGS16(var2, var1, var3);
      this.D = '\uffff' & var3;
   }

   private void addd_ix() {
      int var3 = this.RM16(this.EA);
      int var1 = this.D;
      int var2 = var1 + var3;
      this.CLR_NZVC();
      this.SET_FLAGS16(var1, var3, var2);
      this.D = var2;
   }

   private void anda_di() {
      int var1 = this.DIRBYTE();
      this.A(this.A() & var1);
      this.CLR_NZV();
      this.SET_NZ8(this.A());
   }

   private void anda_ex() {
      int var1 = this.EXTBYTE();
      this.A(this.A() & var1);
      this.CLR_NZV();
      this.SET_NZ8(this.A());
   }

   private void anda_im() {
      int var1 = this.IMMBYTE();
      this.A(this.A() & var1);
      this.CLR_NZV();
      this.SET_NZ8(this.A());
   }

   private void anda_ix() {
      this.A(this.A() & this.RM(this.EA));
      this.CLR_NZV();
      this.SET_NZ8(this.A());
   }

   private void andb_di() {
      int var1 = this.DIRBYTE();
      this.B(this.B() & var1);
      this.CLR_NZV();
      this.SET_NZ8(this.B());
   }

   private void andb_ex() {
      int var1 = this.EXTBYTE();
      this.B(this.B() & var1);
      this.CLR_NZV();
      this.SET_NZ8(this.B());
   }

   private void andb_im() {
      int var1 = this.IMMBYTE();
      this.B(this.B() & var1);
      this.CLR_NZV();
      this.SET_NZ8(this.B());
   }

   private void andb_ix() {
      this.B(this.B() & this.RM(this.EA));
      this.CLR_NZV();
      this.SET_NZ8(this.B());
   }

   private void andcc() {
      int var1 = this.IMMBYTE();
      this.CC &= var1;
      this.CHECK_IRQ_LINES();
   }

   private void asl_di() {
      int var1 = this.DIRBYTE();
      int var2 = var1 << 1;
      this.CLR_NZVC();
      this.SET_FLAGS8(var1, var1, var2);
      this.WM(this.EA, var2);
   }

   private void asl_ex() {
      int var2 = this.EXTBYTE();
      int var1 = var2 << 1;
      this.CLR_NZVC();
      this.SET_FLAGS8(var2, var2, var1);
      this.WM(this.EA, var1 & 255);
   }

   private void asl_ix() {
      int var1 = this.RM(this.EA);
      int var2 = var1 << 1 & 255;
      this.CLR_NZVC();
      this.SET_FLAGS8(var1, var1, var2);
      this.WM(this.EA, var2);
   }

   private void asla() {
      int var1 = this.A() << 1;
      this.CLR_NZVC();
      this.SET_FLAGS8(this.A(), this.A(), var1);
      this.A(var1);
   }

   private void aslb() {
      int var1 = this.B() << 1;
      this.CLR_NZVC();
      this.SET_FLAGS8(this.B(), this.B(), var1);
      this.B(var1);
   }

   private void asld() {
      for(int var1 = this.IMMBYTE(); var1 != 0; --var1) {
         int var2 = this.D << 1;
         this.CLR_NZVC();
         this.SET_FLAGS16(this.D, this.D, var2);
         this.D = '\uffff' & var2;
      }

   }

   private void asld_di() {
      for(int var1 = this.DIRBYTE(); var1 != 0; --var1) {
         int var2 = this.D << 1;
         this.CLR_NZVC();
         this.SET_FLAGS16(this.D, this.D, var2);
         this.D = var2;
      }

   }

   private void asld_ex() {
      for(int var1 = this.EXTBYTE(); var1 != 0; --var1) {
         int var2 = this.D << 1;
         this.CLR_NZVC();
         this.SET_FLAGS16(this.D, this.D, var2);
         this.D = '\uffff' & var2;
      }

   }

   private void asld_ix() {
      for(int var1 = this.RM(this.EA); var1 != 0; --var1) {
         int var2 = this.D << 1;
         this.CLR_NZVC();
         this.SET_FLAGS16(this.D, this.D, var2);
         this.D = '\uffff' & var2;
      }

   }

   private void aslw_di() {
      int var2 = this.DIRWORD();
      int var1 = var2 << 1;
      this.CLR_NZVC();
      this.SET_FLAGS16(var2, var2, var1);
      this.WM16(this.EA, var1);
   }

   private void aslw_ex() {
      int var2 = this.EXTWORD();
      int var1 = var2 << 1 & '\uffff';
      this.CLR_NZVC();
      this.SET_FLAGS16(var2, var2, var1);
      this.WM16(this.EA, var1);
   }

   private void aslw_ix() {
      int var2 = this.RM16(this.EA);
      int var1 = var2 << 1 & '\uffff';
      this.CLR_NZVC();
      this.SET_FLAGS16(var2, var2, var1);
      this.WM16(this.EA, var1);
   }

   private void asr_di() {
      int var1 = this.DIRBYTE();
      this.CLR_NZC();
      this.CC |= var1 & 1;
      var1 = var1 & 128 | var1 >> 1;
      this.SET_NZ8(var1);
      this.WM(this.EA, var1);
   }

   private void asr_ex() {
      int var1 = this.EXTBYTE();
      this.CLR_NZC();
      this.CC |= var1 & 1;
      var1 = var1 & 128 | var1 >> 1;
      this.SET_NZ8(var1);
      this.WM(this.EA, var1);
   }

   private void asr_ix() {
      int var1 = this.RM(this.EA);
      this.CLR_NZC();
      this.CC |= var1 & 1;
      var1 = var1 >> 1 | var1 & 128;
      this.SET_NZ8(var1);
      this.WM(this.EA, var1);
   }

   private void asra() {
      this.CLR_NZC();
      this.CC |= this.A() & 1;
      this.A(this.A() & 128 | this.A() >> 1);
      this.SET_NZ8(this.A());
   }

   private void asrb() {
      this.CLR_NZC();
      this.CC |= this.B() & 1;
      this.A(this.B() & 128 | this.B() >> 1);
      this.SET_NZ8(this.B());
   }

   private void asrd() {
      for(int var1 = this.IMMBYTE(); var1 != 0; --var1) {
         this.CLR_NZC();
         this.CC |= this.D & 1;
         this.D = this.D & '耀' | this.D >> 1;
         this.SET_NZ16(this.D);
      }

   }

   private void asrd_di() {
      for(int var1 = this.DIRBYTE(); var1 != 0; --var1) {
         this.CLR_NZC();
         this.CC |= this.D & 1;
         this.D = this.D & '耀' | this.D >> 1;
         this.SET_NZ16(this.D);
      }

   }

   private void asrd_ex() {
      for(int var1 = this.EXTBYTE(); var1 != 0; --var1) {
         this.CLR_NZC();
         this.CC |= this.D & 1;
         this.D = this.D & '耀' | this.D >> 1;
         this.SET_NZ16(this.D);
      }

   }

   private void asrd_ix() {
      for(int var1 = this.RM(this.EA); var1 != 0; --var1) {
         this.CLR_NZC();
         this.CC |= this.D & 1;
         this.D = this.D & '耀' | this.D >> 1;
         this.SET_NZ16(this.D);
      }

   }

   private void asrw_di() {
      int var1 = this.DIRWORD();
      this.CLR_NZC();
      this.CC |= var1 & 1;
      var1 = '耀' & var1 | var1 >> 1;
      this.SET_NZ16(var1);
      this.WM16(this.EA, var1);
   }

   private void asrw_ex() {
      int var1 = this.EXTWORD();
      this.CLR_NZC();
      this.CC |= var1 & 1;
      var1 = '耀' & var1 | var1 >> 1;
      this.SET_NZ16(var1);
      this.WM16(this.EA, var1);
   }

   private void asrw_ix() {
      int var1 = this.RM16(this.EA);
      this.CLR_NZC();
      this.CC |= var1 & 1;
      var1 = '耀' & var1 | var1 >> 1;
      this.SET_NZ16(var1);
      this.WM16(this.EA, var1);
   }

   private void bcc() {
      boolean var1;
      if((this.CC & 1) == 0) {
         var1 = true;
      } else {
         var1 = false;
      }

      this.BRANCH(var1);
   }

   private void bcs() {
      this.BRANCH(this.CC & 1);
   }

   private void beq() {
      this.BRANCH(this.CC & 4);
   }

   private void bge() {
      boolean var1;
      if(this.NXORV() == 0) {
         var1 = true;
      } else {
         var1 = false;
      }

      this.BRANCH(var1);
   }

   private void bgt() {
      boolean var1;
      if(this.NXORV() == 0 && (this.CC & 4) == 0) {
         var1 = true;
      } else {
         var1 = false;
      }

      this.BRANCH(var1);
   }

   private void bhi() {
      boolean var1;
      if((this.CC & 5) == 0) {
         var1 = true;
      } else {
         var1 = false;
      }

      this.BRANCH(var1);
   }

   private void bita_di() {
      int var1 = this.DIRBYTE();
      int var2 = this.A();
      this.CLR_NZV();
      this.SET_NZ8(var2 & var1);
   }

   private void bita_ex() {
      int var1 = this.EXTBYTE();
      int var2 = this.A();
      this.CLR_NZV();
      this.SET_NZ8(var2 & var1);
   }

   private void bita_im() {
      int var2 = this.IMMBYTE();
      int var1 = this.A();
      this.CLR_NZV();
      this.SET_NZ8(var1 & var2);
   }

   private void bita_ix() {
      int var1 = this.A();
      int var2 = this.RM(this.EA);
      this.CLR_NZV();
      this.SET_NZ8(var1 & var2);
   }

   private void bitb_di() {
      int var2 = this.DIRBYTE();
      int var1 = this.B();
      this.CLR_NZV();
      this.SET_NZ8(var1 & var2);
   }

   private void bitb_ex() {
      int var1 = this.EXTBYTE();
      int var2 = this.B();
      this.CLR_NZV();
      this.SET_NZ8(var2 & var1);
   }

   private void bitb_im() {
      int var1 = this.IMMBYTE();
      int var2 = this.B();
      this.CLR_NZV();
      this.SET_NZ8(var2 & var1);
   }

   private void bitb_ix() {
      int var2 = this.B();
      int var1 = this.RM(this.EA);
      this.CLR_NZV();
      this.SET_NZ8(var2 & var1);
   }

   private void ble() {
      boolean var1;
      if(this.NXORV() == 0 && (this.CC & 4) == 0) {
         var1 = false;
      } else {
         var1 = true;
      }

      this.BRANCH(var1);
   }

   private void bls() {
      this.BRANCH(this.CC & 5);
   }

   private void blt() {
      this.BRANCH(this.NXORV());
   }

   private void bmi() {
      this.BRANCH(this.CC & 8);
   }

   private void bmove() {
      while(this.U != 0) {
         int var1 = this.RM(this.Y);
         this.WM(this.X, var1);
         ++this.Y;
         ++this.X;
         --this.U;
         this.konami_ICount -= 2;
      }

   }

   private void bne() {
      boolean var1;
      if((this.CC & 4) == 0) {
         var1 = true;
      } else {
         var1 = false;
      }

      this.BRANCH(var1);
   }

   private void bpl() {
      boolean var1;
      if((this.CC & 8) == 0) {
         var1 = true;
      } else {
         var1 = false;
      }

      this.BRANCH(var1);
   }

   private void bra() {
      int var1 = this.IMMBYTE();
      this.PC += (byte)var1;
      if(var1 == 254 && this.konami_ICount > 0) {
         this.konami_ICount = 0;
      }

   }

   private void brn() {
      this.IMMBYTE();
   }

   private void bset() {
      while(this.U != 0) {
         int var1 = this.A();
         this.WM(this.X, var1);
         ++this.X;
         --this.U;
         this.konami_ICount -= 2;
      }

   }

   private void bset2() {
      while(this.U != 0) {
         this.WM16(this.X, this.D);
         this.X += 2;
         --this.U;
         this.konami_ICount -= 3;
      }

   }

   private void bsr() {
      int var1 = this.IMMBYTE();
      this.PUSHWORD(this.PC);
      this.PC += (byte)var1;
   }

   private void bvc() {
      boolean var1;
      if((this.CC & 2) == 0) {
         var1 = true;
      } else {
         var1 = false;
      }

      this.BRANCH(var1);
   }

   private void bvs() {
      this.BRANCH(this.CC & 2);
   }

   private void clr_di() {
      this.DIRECT();
      this.WM(this.EA, 0);
      this.CLR_NZVC();
      this.SEZ();
   }

   private void clr_ex() {
      this.EXTENDED();
      this.WM(this.EA, 0);
      this.CLR_NZVC();
      this.SEZ();
   }

   private void clr_ix() {
      this.WM(this.EA, 0);
      this.CLR_NZVC();
      this.SEZ();
   }

   private void clra() {
      this.A(0);
      this.CLR_NZVC();
      this.SEZ();
   }

   private void clrb() {
      this.B(0);
      this.CLR_NZVC();
      this.SEZ();
   }

   private void clrd() {
      this.D = 0;
      this.CLR_NZVC();
      this.SEZ();
   }

   private void clrw_di() {
      this.DIRECT();
      this.WM16(this.EA, 0);
      this.CLR_NZVC();
      this.SEZ();
   }

   private void clrw_ex() {
      this.EXTENDED();
      this.WM16(this.EA, 0);
      this.CLR_NZVC();
      this.SEZ();
   }

   private void clrw_ix() {
      this.WM16(this.EA, 0);
      this.CLR_NZVC();
      this.SEZ();
   }

   private void cmpa_im() {
      int var1 = this.IMMBYTE();
      int var2 = this.A();
      this.CLR_NZVC();
      this.SET_FLAGS8(this.A(), var1, var2 - var1);
   }

   private void cmpa_ix() {
      int var1 = this.RM(this.EA);
      int var2 = this.A();
      this.CLR_NZVC();
      this.SET_FLAGS8(this.A(), var1, var2 - var1);
   }

   private void cmpb_im() {
      int var1 = this.IMMBYTE();
      int var2 = this.B();
      this.CLR_NZVC();
      this.SET_FLAGS8(this.B(), var1, var2 - var1);
   }

   private void cmpb_ix() {
      int var2 = this.RM(this.EA);
      int var1 = this.B();
      this.CLR_NZVC();
      this.SET_FLAGS8(this.B(), var2, var1 - var2);
   }

   private void cmpd_di() {
      int var1 = this.DIRWORD();
      int var2 = this.D;
      this.CLR_NZVC();
      this.SET_FLAGS16(var2, var1, var2 - var1);
   }

   private void cmpd_ex() {
      int var2 = this.EXTWORD();
      int var1 = this.D;
      this.CLR_NZVC();
      this.SET_FLAGS16(var1, var2, var1 - var2);
   }

   private void cmpd_im() {
      int var1 = this.IMMWORD();
      int var2 = this.D;
      this.CLR_NZVC();
      this.SET_FLAGS16(var2, var1, var2 - var1);
   }

   private void cmpd_ix() {
      int var1 = this.RM16(this.EA);
      int var2 = this.D;
      this.CLR_NZVC();
      this.SET_FLAGS16(var2, var1, var2 - var1);
   }

   private void cmps_di() {
      int var2 = this.DIRWORD();
      int var1 = this.S;
      this.CLR_NZVC();
      this.SET_FLAGS16(var1, var2, var1 - var2);
   }

   private void cmps_ex() {
      int var1 = this.EXTWORD();
      int var2 = this.S;
      this.CLR_NZVC();
      this.SET_FLAGS16(var2, var1, var2 - var1);
   }

   private void cmps_im() {
      int var2 = this.IMMWORD();
      int var1 = this.S;
      this.CLR_NZVC();
      this.SET_FLAGS16(var1, var2, var1 - var2);
   }

   private void cmps_ix() {
      int var2 = this.RM16(this.EA);
      int var1 = this.S;
      this.CLR_NZVC();
      this.SET_FLAGS16(var1, var2, var1 - var2);
   }

   private void cmpu_di() {
      int var2 = this.DIRWORD();
      int var1 = this.U;
      this.CLR_NZVC();
      this.SET_FLAGS16(var1, var2, var1 - var2);
   }

   private void cmpu_ex() {
      int var2 = this.EXTWORD();
      int var1 = this.U;
      this.CLR_NZVC();
      this.SET_FLAGS16(var1, var2, var1 - var2);
   }

   private void cmpu_im() {
      int var2 = this.IMMWORD();
      int var1 = this.U;
      this.CLR_NZVC();
      this.SET_FLAGS16(var1, var2, var1 - var2);
   }

   private void cmpu_ix() {
      int var2 = this.RM16(this.EA);
      int var1 = this.U;
      this.CLR_NZVC();
      this.SET_FLAGS16(this.U, var2, var1 - var2);
   }

   private void cmpx_di() {
      int var1 = this.DIRWORD();
      int var2 = this.X;
      this.CLR_NZVC();
      this.SET_FLAGS16(var2, var1, var2 - var1);
   }

   private void cmpx_ex() {
      int var2 = this.EXTWORD();
      int var1 = this.X;
      this.CLR_NZVC();
      this.SET_FLAGS16(var1, var2, var1 - var2);
   }

   private void cmpx_im() {
      int var2 = this.IMMWORD();
      int var1 = this.X;
      this.CLR_NZVC();
      this.SET_FLAGS16(var1, var2, var1 - var2);
   }

   private void cmpx_ix() {
      int var1 = this.RM16(this.EA);
      int var2 = this.X;
      this.CLR_NZVC();
      this.SET_FLAGS16(var2, var1, var2 - var1);
   }

   private void cmpy_di() {
      int var1 = this.DIRWORD();
      int var2 = this.Y;
      this.CLR_NZVC();
      this.SET_FLAGS16(var2, var1, var2 - var1);
   }

   private void cmpy_ex() {
      int var2 = this.EXTWORD();
      int var1 = this.Y;
      this.CLR_NZVC();
      this.SET_FLAGS16(var1, var2, var1 - var2);
   }

   private void cmpy_im() {
      int var2 = this.IMMWORD();
      int var1 = this.Y;
      this.CLR_NZVC();
      this.SET_FLAGS16(var1, var2, var1 - var2);
   }

   private void cmpy_ix() {
      int var2 = this.RM16(this.EA);
      int var1 = this.Y;
      this.CLR_NZVC();
      this.SET_FLAGS16(var1, var2, var1 - var2);
   }

   private void com_di() {
      int var1 = ~this.DIRBYTE();
      this.CLR_NZV();
      this.SET_NZ8(var1);
      this.SEC();
      this.WM(this.EA, var1);
   }

   private void com_ex() {
      int var1 = ~this.EXTBYTE();
      this.CLR_NZV();
      this.SET_NZ8(var1);
      this.SEC();
      this.WM(this.EA, var1 & 255);
   }

   private void com_ix() {
      int var1 = ~this.RM(this.EA) & 255;
      this.CLR_NZV();
      this.SET_NZ8(var1);
      this.SEC();
      this.WM(this.EA, var1);
   }

   private void coma() {
      this.A(~this.A());
      this.CLR_NZV();
      this.SET_NZ8(this.A());
      this.SEC();
   }

   private void comb() {
      this.B(~this.B());
      this.CLR_NZV();
      this.SET_NZ8(this.B());
      this.SEC();
   }

   private void daa() {
      int var2 = 0;
      int var3 = this.A() & 240;
      int var4 = this.A() & 15;
      if(var4 > 9 || (this.CC & 32) != 0) {
         var2 = 0 | 6;
      }

      int var1 = var2;
      if(var3 > 128) {
         var1 = var2;
         if(var4 > 9) {
            var1 = var2 | 96;
         }
      }

      label19: {
         if(var3 <= 144) {
            var2 = var1;
            if((this.CC & 1) == 0) {
               break label19;
            }
         }

         var2 = var1 | 96;
      }

      var1 = var2 + this.A();
      this.CLR_NZV();
      this.SET_NZ8(var1);
      this.SET_C8(var1);
      this.A(var1);
   }

   private void dec_di() {
      int var1 = this.DIRBYTE() - 1 & 255;
      this.CLR_NZV();
      this.SET_FLAGS8D(var1);
      this.WM(this.EA, var1);
   }

   private void dec_ex() {
      int var1 = this.EXTBYTE() - 1;
      this.CLR_NZV();
      this.SET_FLAGS8D(var1);
      this.WM(this.EA, var1 & 255);
   }

   private void dec_ix() {
      int var1 = this.RM(this.EA) - 1 & 255;
      this.CLR_NZV();
      this.SET_FLAGS8D(var1);
      this.WM(this.EA, var1);
   }

   private void deca() {
      this.A(this.A() - 1);
      this.CLR_NZV();
      this.SET_FLAGS8D(this.A());
   }

   private void decb() {
      this.B(this.B() - 1);
      this.CLR_NZV();
      this.SET_FLAGS8D(this.B());
   }

   private void decbjnz() {
      this.B(this.B() - 1);
      this.CLR_NZV();
      this.SET_FLAGS8D(this.B());
      boolean var1;
      if((this.CC & 4) == 0) {
         var1 = true;
      } else {
         var1 = false;
      }

      this.BRANCH(var1);
   }

   private void decd() {
      int var1 = this.D - 1;
      this.CLR_NZV();
      this.SET_FLAGS16(this.D, this.D, var1);
      this.D = '\uffff' & var1;
   }

   private void decw_di() {
      int var1 = this.DIRWORD();
      int var2 = var1 - 1;
      this.CLR_NZV();
      this.SET_FLAGS16(var1, var1, var2);
      this.WM16(this.EA, var2);
   }

   private void decw_ex() {
      int var1 = this.EXTWORD();
      int var2 = var1 - 1;
      this.CLR_NZV();
      this.SET_FLAGS16(var1, var1, var2);
      this.WM16(this.EA, var2);
   }

   private void decw_ix() {
      int var1 = this.RM16(this.EA);
      int var2 = var1 - 1;
      this.CLR_NZV();
      this.SET_FLAGS16(var1, var1, var2);
      this.WM16(this.EA, var2);
   }

   private void decxjnz() {
      --this.X;
      this.CLR_NZV();
      this.SET_NZ16(this.X);
      boolean var1;
      if((this.CC & 4) == 0) {
         var1 = true;
      } else {
         var1 = false;
      }

      this.BRANCH(var1);
   }

   private void divx() {
      int var1;
      int var2;
      if(this.B() != 0) {
         var2 = this.X / this.B();
         var1 = this.X % this.B();
      } else {
         var2 = 0;
         var1 = 0;
      }

      this.CLR_ZC();
      this.SET_Z16(var2);
      if((var2 & 128) != 0) {
         this.SEC();
      }

      this.X = var2;
      this.B(var1);
   }

   private void dumpRegisters() {
   }

   private void eora_di() {
      int var1 = this.DIRBYTE();
      this.A(this.A() ^ var1);
      this.CLR_NZV();
      this.SET_NZ8(this.A());
   }

   private void eora_ex() {
      int var1 = this.EXTBYTE();
      this.A(this.A() ^ var1);
      this.CLR_NZV();
      this.SET_NZ8(this.A());
   }

   private void eora_im() {
      int var1 = this.IMMBYTE();
      this.A(this.A() ^ var1);
      this.CLR_NZV();
      this.SET_NZ8(this.A());
   }

   private void eora_ix() {
      this.A(this.A() ^ this.RM(this.EA));
      this.CLR_NZV();
      this.SET_NZ8(this.A());
   }

   private void eorb_di() {
      int var1 = this.DIRBYTE();
      this.B(this.B() ^ var1);
      this.CLR_NZV();
      this.SET_NZ8(this.B());
   }

   private void eorb_ex() {
      int var1 = this.EXTBYTE();
      this.B(this.B() ^ var1);
      this.CLR_NZV();
      this.SET_NZ8(this.B());
   }

   private void eorb_im() {
      int var1 = this.IMMBYTE();
      this.B(this.B() ^ var1);
      this.CLR_NZV();
      this.SET_NZ8(this.B());
   }

   private void eorb_ix() {
      this.B(this.B() ^ this.RM(this.EA));
      this.CLR_NZV();
      this.SET_NZ8(this.B());
   }

   private void exg() {
      int var2 = this.IMMBYTE();
      int var1 = this.GETREG(var2 >> 4);
      this.SETREG(this.GETREG(var2 & 15), var2 >> 4);
      this.SETREG(var1, var2 & 15);
   }

   private void inc_di() {
      int var1 = this.DIRBYTE() + 1 & 255;
      this.CLR_NZV();
      this.SET_FLAGS8I(var1);
      this.WM(this.EA, var1);
   }

   private void inc_ex() {
      int var1 = this.EXTBYTE() + 1;
      this.CLR_NZV();
      this.SET_FLAGS8I(var1);
      this.WM(this.EA, var1 & 255);
   }

   private void inc_ix() {
      int var1 = this.RM(this.EA) + 1 & 255;
      this.CLR_NZV();
      this.SET_FLAGS8I(var1);
      this.WM(this.EA, var1);
   }

   private void inca() {
      this.A(this.A() + 1);
      this.CLR_NZV();
      this.SET_FLAGS8I(this.A());
   }

   private void incb() {
      this.B(this.B() + 1);
      this.CLR_NZV();
      this.SET_FLAGS8I(this.B());
   }

   private void incd() {
      int var1 = this.D + 1;
      this.CLR_NZV();
      this.SET_FLAGS16(this.D, this.D, var1);
      this.D = '\uffff' & var1;
   }

   private void incw_di() {
      int var2 = this.DIRWORD();
      int var1 = var2 + 1;
      this.CLR_NZV();
      this.SET_FLAGS16(var2, var2, var1);
      this.WM16(this.EA, var1);
   }

   private void incw_ex() {
      int var2 = this.EXTWORD();
      int var1 = var2 + 1;
      this.CLR_NZV();
      this.SET_FLAGS16(var2, var2, var1);
      this.WM16(this.EA, var1);
   }

   private void incw_ix() {
      int var1 = this.RM16(this.EA);
      int var2 = var1 + 1;
      this.CLR_NZV();
      this.SET_FLAGS16(var1, var1, var2);
      this.WM16(this.EA, var2);
   }

   private void jmp_di() {
      this.DIRECT();
      this.PC = this.EA;
   }

   private void jmp_ex() {
      this.EXTENDED();
      this.PC = this.EA;
   }

   private void jmp_ix() {
      this.PC = this.EA;
   }

   private void jsr_di() {
      this.DIRECT();
      this.PUSHWORD(this.PC);
      this.PC = this.EA;
   }

   private void jsr_ex() {
      this.EXTENDED();
      this.PUSHWORD(this.PC);
      this.PC = this.EA;
   }

   private void jsr_ix() {
      this.PUSHWORD(this.PC);
      this.PC = this.EA;
   }

   private void konami_direct(int var1) {
      switch(var1) {
      case 0:
      case 1:
      case 2:
      case 3:
      case 4:
      case 5:
      case 6:
      case 7:
         this.illegal(var1);
         break;
      case 8:
      case 9:
      case 10:
      case 11:
      case 12:
      case 13:
      case 14:
      case 15:
         this.illegal(var1);
         break;
      case 16:
      case 17:
         this.illegal(var1);
         break;
      case 18:
         this.lda_di();
         break;
      case 19:
         this.ldb_di();
         break;
      case 20:
      case 21:
         this.illegal(var1);
         break;
      case 22:
         this.adda_di();
         break;
      case 23:
         this.addb_di();
         break;
      case 24:
      case 25:
         this.illegal(var1);
         break;
      case 26:
         this.adca_di();
         break;
      case 27:
         this.adcb_di();
         break;
      case 28:
      case 29:
         this.illegal(var1);
         break;
      case 30:
         this.suba_di();
         break;
      case 31:
         this.subb_di();
         break;
      case 32:
      case 33:
         this.illegal(var1);
         break;
      case 34:
         this.sbca_di();
         break;
      case 35:
         this.sbcb_di();
         break;
      case 36:
      case 37:
         this.illegal(var1);
         break;
      case 38:
         this.anda_di();
         break;
      case 39:
         this.andb_di();
         break;
      case 40:
      case 41:
         this.illegal(var1);
         break;
      case 42:
         this.bita_di();
         break;
      case 43:
         this.bitb_di();
         break;
      case 44:
      case 45:
         this.illegal(var1);
         break;
      case 46:
         this.eora_di();
         break;
      case 47:
         this.eorb_di();
         break;
      case 48:
      case 49:
         this.illegal(var1);
         break;
      case 50:
         this.sbca_di();
         break;
      case 51:
         this.sbcb_di();
         break;
      case 52:
      case 53:
         this.illegal(var1);
         break;
      case 54:
         this.anda_di();
         break;
      case 55:
         this.andb_di();
         break;
      case 56:
         this.illegal(var1);
         break;
      case 57:
         this.setline_di();
         break;
      case 58:
         this.sta_di();
         break;
      case 59:
         this.stb_di();
         break;
      case 60:
      case 61:
      case 62:
      case 63:
         this.illegal(var1);
         break;
      case 64:
         this.illegal(var1);
         break;
      case 65:
         this.ldd_di();
         break;
      case 66:
         this.illegal(var1);
         break;
      case 67:
         this.ldx_di();
         break;
      case 68:
         this.illegal(var1);
         break;
      case 69:
         this.ldy_di();
         break;
      case 70:
         this.illegal(var1);
         break;
      case 71:
         this.ldu_di();
         break;
      case 72:
         this.illegal(var1);
         break;
      case 73:
         this.lds_di();
         break;
      case 74:
         this.illegal(var1);
         break;
      case 75:
         this.cmpd_di();
         break;
      case 76:
         this.illegal(var1);
         break;
      case 77:
         this.cmpx_di();
         break;
      case 78:
         this.illegal(var1);
         break;
      case 79:
         this.cmpy_di();
         break;
      case 80:
         this.illegal(var1);
         break;
      case 81:
         this.cmpu_di();
         break;
      case 82:
         this.illegal(var1);
         break;
      case 83:
         this.cmps_di();
         break;
      case 84:
         this.illegal(var1);
         break;
      case 85:
         this.addd_di();
         break;
      case 86:
         this.illegal(var1);
         break;
      case 87:
         this.subd_di();
         break;
      case 88:
         this.std_di();
         break;
      case 89:
         this.stx_di();
         break;
      case 90:
         this.sty_di();
         break;
      case 91:
         this.stu_di();
         break;
      case 92:
         this.sts_di();
         break;
      case 93:
      case 94:
      case 95:
         this.illegal(var1);
         break;
      case 96:
      case 97:
      case 98:
      case 99:
      case 100:
      case 101:
      case 102:
      case 103:
         this.illegal(var1);
         break;
      case 104:
      case 105:
      case 106:
      case 107:
      case 108:
      case 109:
      case 110:
      case 111:
         this.illegal(var1);
         break;
      case 112:
      case 113:
      case 114:
      case 115:
      case 116:
      case 117:
      case 118:
      case 119:
         this.illegal(var1);
         break;
      case 120:
      case 121:
      case 122:
      case 123:
      case 124:
      case 125:
      case 126:
      case 127:
         this.illegal(var1);
         break;
      case 128:
      case 129:
         this.illegal(var1);
         break;
      case 130:
         this.clr_di();
         break;
      case 131:
      case 132:
         this.illegal(var1);
         break;
      case 133:
         this.com_di();
         break;
      case 134:
      case 135:
         this.illegal(var1);
         break;
      case 136:
         this.neg_di();
         break;
      case 137:
      case 138:
         this.illegal(var1);
         break;
      case 139:
         this.inc_di();
         break;
      case 140:
      case 141:
         this.illegal(var1);
         break;
      case 142:
         this.dec_di();
         break;
      case 143:
         this.illegal(var1);
         break;
      case 144:
      case 145:
         this.illegal(var1);
         break;
      case 146:
         this.tst_di();
         break;
      case 147:
      case 148:
         this.illegal(var1);
         break;
      case 149:
         this.lsr_di();
         break;
      case 150:
      case 151:
         this.illegal(var1);
         break;
      case 152:
         this.ror_di();
         break;
      case 153:
      case 154:
         this.illegal(var1);
         break;
      case 155:
         this.asr_di();
         break;
      case 156:
      case 157:
         this.illegal(var1);
         break;
      case 158:
         this.asl_di();
         break;
      case 159:
         this.illegal(var1);
         break;
      case 160:
      case 161:
         this.illegal(var1);
         break;
      case 162:
         this.rol_di();
         break;
      case 163:
         this.lsrw_di();
         break;
      case 164:
         this.rorw_di();
         break;
      case 165:
         this.asrw_di();
         break;
      case 166:
         this.aslw_di();
         break;
      case 167:
         this.rolw_di();
         break;
      case 168:
         this.jmp_di();
         break;
      case 169:
         this.jsr_di();
         break;
      case 170:
      case 171:
      case 172:
      case 173:
      case 174:
      case 175:
         this.illegal(var1);
         break;
      case 176:
      case 177:
      case 178:
      case 179:
      case 180:
      case 181:
      case 182:
      case 183:
         this.illegal(var1);
         break;
      case 184:
         this.illegal(var1);
         break;
      case 185:
         this.lsrd_di();
         break;
      case 186:
         this.illegal(var1);
         break;
      case 187:
         this.rord_di();
         break;
      case 188:
         this.illegal(var1);
         break;
      case 189:
         this.asrd_di();
         break;
      case 190:
         this.illegal(var1);
         break;
      case 191:
         this.asld_di();
         break;
      case 192:
         this.illegal(var1);
         break;
      case 193:
         this.rold_di();
         break;
      case 194:
         this.illegal(var1);
         break;
      case 195:
         this.clrw_di();
         break;
      case 196:
         this.illegal(var1);
         break;
      case 197:
         this.negw_di();
         break;
      case 198:
         this.illegal(var1);
         break;
      case 199:
         this.incw_di();
         break;
      case 200:
         this.illegal(var1);
         break;
      case 201:
         this.decw_di();
         break;
      case 202:
         this.illegal(var1);
         break;
      case 203:
         this.tstw_di();
         break;
      case 204:
      case 205:
      case 206:
      case 207:
         this.illegal(var1);
         break;
      case 208:
      case 209:
      case 210:
      case 211:
      case 212:
      case 213:
      case 214:
      case 215:
         this.illegal(var1);
         break;
      case 216:
      case 217:
      case 218:
      case 219:
      case 220:
      case 221:
      case 222:
      case 223:
         this.illegal(var1);
         break;
      case 224:
      case 225:
      case 226:
      case 227:
      case 228:
      case 229:
      case 230:
      case 231:
         this.illegal(var1);
         break;
      case 232:
      case 233:
      case 234:
      case 235:
      case 236:
      case 237:
      case 238:
      case 239:
         this.illegal(var1);
         break;
      case 240:
      case 241:
      case 242:
      case 243:
      case 244:
      case 245:
      case 246:
      case 247:
         this.illegal(var1);
         break;
      case 248:
      case 249:
      case 250:
      case 251:
      case 252:
      case 253:
      case 254:
      case 255:
         this.illegal(var1);
      }

   }

   private void konami_extended(int var1) {
      switch(var1) {
      case 0:
      case 1:
      case 2:
      case 3:
      case 4:
      case 5:
      case 6:
      case 7:
         this.illegal(var1);
         break;
      case 8:
      case 9:
      case 10:
      case 11:
      case 12:
      case 13:
      case 14:
      case 15:
         this.illegal(var1);
         break;
      case 16:
      case 17:
         this.illegal(var1);
         break;
      case 18:
         this.lda_ex();
         break;
      case 19:
         this.ldb_ex();
         break;
      case 20:
      case 21:
         this.illegal(var1);
         break;
      case 22:
         this.adda_ex();
         break;
      case 23:
         this.addb_ex();
         break;
      case 24:
      case 25:
         this.illegal(var1);
         break;
      case 26:
         this.adca_ex();
         break;
      case 27:
         this.adcb_ex();
         break;
      case 28:
      case 29:
         this.illegal(var1);
         break;
      case 30:
         this.suba_ex();
         break;
      case 31:
         this.subb_ex();
         break;
      case 32:
      case 33:
         this.illegal(var1);
         break;
      case 34:
         this.sbca_ex();
         break;
      case 35:
         this.sbcb_ex();
         break;
      case 36:
      case 37:
         this.illegal(var1);
         break;
      case 38:
         this.anda_ex();
         break;
      case 39:
         this.andb_ex();
         break;
      case 40:
      case 41:
         this.illegal(var1);
         break;
      case 42:
         this.bita_ex();
         break;
      case 43:
         this.bitb_ex();
         break;
      case 44:
      case 45:
         this.illegal(var1);
         break;
      case 46:
         this.eora_ex();
         break;
      case 47:
         this.eorb_ex();
         break;
      case 48:
      case 49:
         this.illegal(var1);
         break;
      case 50:
         this.sbca_ex();
         break;
      case 51:
         this.sbcb_ex();
         break;
      case 52:
      case 53:
         this.illegal(var1);
         break;
      case 54:
         this.anda_ex();
         break;
      case 55:
         this.andb_ex();
         break;
      case 56:
         this.illegal(var1);
         break;
      case 57:
         this.setline_ex();
         break;
      case 58:
         this.sta_ex();
         break;
      case 59:
         this.stb_ex();
         break;
      case 60:
      case 61:
      case 62:
      case 63:
         this.illegal(var1);
         break;
      case 64:
         this.illegal(var1);
         break;
      case 65:
         this.ldd_ex();
         break;
      case 66:
         this.illegal(var1);
         break;
      case 67:
         this.ldx_ex();
         break;
      case 68:
         this.illegal(var1);
         break;
      case 69:
         this.ldy_ex();
         break;
      case 70:
         this.illegal(var1);
         break;
      case 71:
         this.ldu_ex();
         break;
      case 72:
         this.illegal(var1);
         break;
      case 73:
         this.lds_ex();
         break;
      case 74:
         this.illegal(var1);
         break;
      case 75:
         this.cmpd_ex();
         break;
      case 76:
         this.illegal(var1);
         break;
      case 77:
         this.cmpx_ex();
         break;
      case 78:
         this.illegal(var1);
         break;
      case 79:
         this.cmpy_ex();
         break;
      case 80:
         this.illegal(var1);
         break;
      case 81:
         this.cmpu_ex();
         break;
      case 82:
         this.illegal(var1);
         break;
      case 83:
         this.cmps_ex();
         break;
      case 84:
         this.illegal(var1);
         break;
      case 85:
         this.addd_ex();
         break;
      case 86:
         this.illegal(var1);
         break;
      case 87:
         this.subd_ex();
         break;
      case 88:
         this.std_ex();
         break;
      case 89:
         this.stx_ex();
         break;
      case 90:
         this.sty_ex();
         break;
      case 91:
         this.stu_ex();
         break;
      case 92:
         this.sts_ex();
         break;
      case 93:
      case 94:
      case 95:
         this.illegal(var1);
         break;
      case 96:
      case 97:
      case 98:
      case 99:
      case 100:
      case 101:
      case 102:
      case 103:
         this.illegal(var1);
         break;
      case 104:
      case 105:
      case 106:
      case 107:
      case 108:
      case 109:
      case 110:
      case 111:
         this.illegal(var1);
         break;
      case 112:
      case 113:
      case 114:
      case 115:
      case 116:
      case 117:
      case 118:
      case 119:
         this.illegal(var1);
         break;
      case 120:
      case 121:
      case 122:
      case 123:
      case 124:
      case 125:
      case 126:
      case 127:
         this.illegal(var1);
         break;
      case 128:
      case 129:
         this.illegal(var1);
         break;
      case 130:
         this.clr_ex();
         break;
      case 131:
      case 132:
         this.illegal(var1);
         break;
      case 133:
         this.com_ex();
         break;
      case 134:
      case 135:
         this.illegal(var1);
         break;
      case 136:
         this.neg_ex();
         break;
      case 137:
      case 138:
         this.illegal(var1);
         break;
      case 139:
         this.inc_ex();
         break;
      case 140:
      case 141:
         this.illegal(var1);
         break;
      case 142:
         this.dec_ex();
         break;
      case 143:
         this.illegal(var1);
         break;
      case 144:
      case 145:
         this.illegal(var1);
         break;
      case 146:
         this.tst_ex();
         break;
      case 147:
      case 148:
         this.illegal(var1);
         break;
      case 149:
         this.lsr_ex();
         break;
      case 150:
      case 151:
         this.illegal(var1);
         break;
      case 152:
         this.ror_ex();
         break;
      case 153:
      case 154:
         this.illegal(var1);
         break;
      case 155:
         this.asr_ex();
         break;
      case 156:
      case 157:
         this.illegal(var1);
         break;
      case 158:
         this.asl_ex();
         break;
      case 159:
         this.illegal(var1);
         break;
      case 160:
      case 161:
         this.illegal(var1);
         break;
      case 162:
         this.rol_ex();
         break;
      case 163:
         this.lsrw_ex();
         break;
      case 164:
         this.rorw_ex();
         break;
      case 165:
         this.asrw_ex();
         break;
      case 166:
         this.aslw_ex();
         break;
      case 167:
         this.rolw_ex();
         break;
      case 168:
         this.jmp_ex();
         break;
      case 169:
         this.jsr_ex();
         break;
      case 170:
      case 171:
      case 172:
      case 173:
      case 174:
      case 175:
         this.illegal(var1);
         break;
      case 176:
      case 177:
      case 178:
      case 179:
      case 180:
      case 181:
      case 182:
      case 183:
         this.illegal(var1);
         break;
      case 184:
         this.illegal(var1);
         break;
      case 185:
         this.lsrd_ex();
         break;
      case 186:
         this.illegal(var1);
         break;
      case 187:
         this.rord_ex();
         break;
      case 188:
         this.illegal(var1);
         break;
      case 189:
         this.asrd_ex();
         break;
      case 190:
         this.illegal(var1);
         break;
      case 191:
         this.asld_ex();
         break;
      case 192:
         this.illegal(var1);
         break;
      case 193:
         this.rold_ex();
         break;
      case 194:
         this.illegal(var1);
         break;
      case 195:
         this.clrw_ex();
         break;
      case 196:
         this.illegal(var1);
         break;
      case 197:
         this.negw_ex();
         break;
      case 198:
         this.illegal(var1);
         break;
      case 199:
         this.incw_ex();
         break;
      case 200:
         this.illegal(var1);
         break;
      case 201:
         this.decw_ex();
         break;
      case 202:
         this.illegal(var1);
         break;
      case 203:
         this.tstw_ex();
         break;
      case 204:
      case 205:
      case 206:
      case 207:
         this.illegal(var1);
         break;
      case 208:
      case 209:
      case 210:
      case 211:
      case 212:
      case 213:
      case 214:
      case 215:
         this.illegal(var1);
         break;
      case 216:
      case 217:
      case 218:
      case 219:
      case 220:
      case 221:
      case 222:
      case 223:
         this.illegal(var1);
         break;
      case 224:
      case 225:
      case 226:
      case 227:
      case 228:
      case 229:
      case 230:
      case 231:
         this.illegal(var1);
         break;
      case 232:
      case 233:
      case 234:
      case 235:
      case 236:
      case 237:
      case 238:
      case 239:
         this.illegal(var1);
         break;
      case 240:
      case 241:
      case 242:
      case 243:
      case 244:
      case 245:
      case 246:
      case 247:
         this.illegal(var1);
         break;
      case 248:
      case 249:
      case 250:
      case 251:
      case 252:
      case 253:
      case 254:
      case 255:
         this.illegal(var1);
      }

   }

   private void konami_indexed(int var1) {
      switch(var1) {
      case 0:
      case 1:
      case 2:
      case 3:
      case 4:
      case 5:
      case 6:
      case 7:
         this.illegal(var1);
         break;
      case 8:
         this.leax();
         break;
      case 9:
         this.leay();
         break;
      case 10:
         this.leau();
         break;
      case 11:
         this.leas();
         break;
      case 12:
      case 13:
      case 14:
      case 15:
         this.illegal(var1);
         break;
      case 16:
      case 17:
         this.illegal(var1);
         break;
      case 18:
         this.lda_ix();
         break;
      case 19:
         this.ldb_ix();
         break;
      case 20:
      case 21:
         this.illegal(var1);
         break;
      case 22:
         this.adda_ix();
         break;
      case 23:
         this.addb_ix();
         break;
      case 24:
      case 25:
         this.illegal(var1);
         break;
      case 26:
         this.adca_ix();
         break;
      case 27:
         this.adcb_ix();
         break;
      case 28:
      case 29:
         this.illegal(var1);
         break;
      case 30:
         this.suba_ix();
         break;
      case 31:
         this.subb_ix();
         break;
      case 32:
      case 33:
         this.illegal(var1);
         break;
      case 34:
         this.sbca_ix();
         break;
      case 35:
         this.sbcb_ix();
         break;
      case 36:
      case 37:
         this.illegal(var1);
         break;
      case 38:
         this.anda_ix();
         break;
      case 39:
         this.andb_ix();
         break;
      case 40:
      case 41:
         this.illegal(var1);
         break;
      case 42:
         this.bita_ix();
         break;
      case 43:
         this.bitb_ix();
         break;
      case 44:
      case 45:
         this.illegal(var1);
         break;
      case 46:
         this.eora_ix();
         break;
      case 47:
         this.eorb_ix();
         break;
      case 48:
      case 49:
         this.illegal(var1);
         break;
      case 50:
         this.ora_ix();
         break;
      case 51:
         this.orb_ix();
         break;
      case 52:
      case 53:
         this.illegal(var1);
         break;
      case 54:
         this.cmpa_ix();
         break;
      case 55:
         this.cmpb_ix();
         break;
      case 56:
         this.illegal(var1);
         break;
      case 57:
         this.setline_ix();
         break;
      case 58:
         this.sta_ix();
         break;
      case 59:
         this.stb_ix();
         break;
      case 60:
      case 61:
      case 62:
      case 63:
         this.illegal(var1);
         break;
      case 64:
         this.illegal(var1);
         break;
      case 65:
         this.ldd_ix();
         break;
      case 66:
         this.illegal(var1);
         break;
      case 67:
         this.ldx_ix();
         break;
      case 68:
         this.illegal(var1);
         break;
      case 69:
         this.ldy_ix();
         break;
      case 70:
         this.illegal(var1);
         break;
      case 71:
         this.ldu_ix();
         break;
      case 72:
         this.illegal(var1);
         break;
      case 73:
         this.lds_ix();
         break;
      case 74:
         this.illegal(var1);
         break;
      case 75:
         this.cmpd_ix();
         break;
      case 76:
         this.illegal(var1);
         break;
      case 77:
         this.cmpx_ix();
         break;
      case 78:
         this.illegal(var1);
         break;
      case 79:
         this.cmpy_ix();
         break;
      case 80:
         this.illegal(var1);
         break;
      case 81:
         this.cmpu_ix();
         break;
      case 82:
         this.illegal(var1);
         break;
      case 83:
         this.cmps_ix();
         break;
      case 84:
         this.illegal(var1);
         break;
      case 85:
         this.addd_ix();
         break;
      case 86:
         this.illegal(var1);
         break;
      case 87:
         this.subd_ix();
         break;
      case 88:
         this.std_ix();
         break;
      case 89:
         this.stx_ix();
         break;
      case 90:
         this.sty_ix();
         break;
      case 91:
         this.stu_ix();
         break;
      case 92:
         this.sts_ix();
         break;
      case 93:
         this.illegal(var1);
         break;
      case 94:
         this.illegal(var1);
         break;
      case 95:
         this.illegal(var1);
         break;
      case 96:
      case 97:
      case 98:
      case 99:
      case 100:
      case 101:
      case 102:
      case 103:
         this.illegal(var1);
         break;
      case 104:
      case 105:
      case 106:
      case 107:
      case 108:
      case 109:
      case 110:
      case 111:
         this.illegal(var1);
         break;
      case 112:
      case 113:
      case 114:
      case 115:
      case 116:
      case 117:
      case 118:
      case 119:
         this.illegal(var1);
         break;
      case 120:
      case 121:
      case 122:
      case 123:
      case 124:
      case 125:
      case 126:
      case 127:
         this.illegal(var1);
         break;
      case 128:
      case 129:
         this.illegal(var1);
         break;
      case 130:
         this.clr_ix();
         break;
      case 131:
      case 132:
         this.illegal(var1);
         break;
      case 133:
         this.com_ix();
         break;
      case 134:
      case 135:
         this.illegal(var1);
         break;
      case 136:
         this.neg_ix();
         break;
      case 137:
      case 138:
         this.illegal(var1);
         break;
      case 139:
         this.inc_ix();
         break;
      case 140:
      case 141:
         this.illegal(var1);
         break;
      case 142:
         this.dec_ix();
         break;
      case 143:
         this.illegal(var1);
         break;
      case 144:
      case 145:
         this.illegal(var1);
         break;
      case 146:
         this.tst_ix();
         break;
      case 147:
      case 148:
         this.illegal(var1);
         break;
      case 149:
         this.lsr_ix();
         break;
      case 150:
      case 151:
         this.illegal(var1);
         break;
      case 152:
         this.ror_ix();
         break;
      case 153:
      case 154:
         this.illegal(var1);
         break;
      case 155:
         this.asr_ix();
         break;
      case 156:
      case 157:
         this.illegal(var1);
         break;
      case 158:
         this.asl_ix();
         break;
      case 159:
         this.illegal(var1);
         break;
      case 160:
      case 161:
         this.illegal(var1);
         break;
      case 162:
         this.rol_ix();
         break;
      case 163:
         this.lsrw_ix();
         break;
      case 164:
         this.rorw_ix();
         break;
      case 165:
         this.asrw_ix();
         break;
      case 166:
         this.aslw_ix();
         break;
      case 167:
         this.rolw_ix();
         break;
      case 168:
         this.jmp_ix();
         break;
      case 169:
         this.jsr_ix();
         break;
      case 170:
      case 171:
      case 172:
      case 173:
      case 174:
      case 175:
         this.illegal(var1);
         break;
      case 176:
      case 177:
      case 178:
      case 179:
      case 180:
      case 181:
      case 182:
      case 183:
         this.illegal(var1);
         break;
      case 184:
         this.illegal(var1);
         break;
      case 185:
         this.lsrd_ix();
         break;
      case 186:
         this.illegal(var1);
         break;
      case 187:
         this.rord_ix();
         break;
      case 188:
         this.illegal(var1);
         break;
      case 189:
         this.asrd_ix();
         break;
      case 190:
         this.illegal(var1);
         break;
      case 191:
         this.asld_ix();
         break;
      case 192:
         this.illegal(var1);
         break;
      case 193:
         this.rold_ix();
         break;
      case 194:
         this.illegal(var1);
         break;
      case 195:
         this.clrw_ix();
         break;
      case 196:
         this.illegal(var1);
         break;
      case 197:
         this.negw_ix();
         break;
      case 198:
         this.illegal(var1);
         break;
      case 199:
         this.incw_ix();
         break;
      case 200:
         this.illegal(var1);
         break;
      case 201:
         this.decw_ix();
         break;
      case 202:
         this.illegal(var1);
         break;
      case 203:
         this.tstw_ix();
         break;
      case 204:
      case 205:
      case 206:
      case 207:
         this.illegal(var1);
         break;
      case 208:
      case 209:
      case 210:
      case 211:
      case 212:
      case 213:
      case 214:
      case 215:
         this.illegal(var1);
         break;
      case 216:
      case 217:
      case 218:
      case 219:
      case 220:
      case 221:
      case 222:
      case 223:
         this.illegal(var1);
         break;
      case 224:
      case 225:
      case 226:
      case 227:
      case 228:
      case 229:
      case 230:
      case 231:
         this.illegal(var1);
         break;
      case 232:
      case 233:
      case 234:
      case 235:
      case 236:
      case 237:
      case 238:
      case 239:
         this.illegal(var1);
         break;
      case 240:
      case 241:
      case 242:
      case 243:
      case 244:
      case 245:
      case 246:
      case 247:
         this.illegal(var1);
         break;
      case 248:
      case 249:
      case 250:
      case 251:
      case 252:
      case 253:
      case 254:
      case 255:
         this.illegal(var1);
      }

   }

   private void lbcc() {
      boolean var1;
      if((this.CC & 1) == 0) {
         var1 = true;
      } else {
         var1 = false;
      }

      this.LBRANCH(var1);
   }

   private void lbcs() {
      this.LBRANCH(this.CC & 1);
   }

   private void lbeq() {
      this.LBRANCH(this.CC & 4);
   }

   private void lbge() {
      boolean var1;
      if(this.NXORV() == 0) {
         var1 = true;
      } else {
         var1 = false;
      }

      this.LBRANCH(var1);
   }

   private void lbgt() {
      boolean var1;
      if(this.NXORV() == 0 && (this.CC & 4) == 0) {
         var1 = true;
      } else {
         var1 = false;
      }

      this.LBRANCH(var1);
   }

   private void lbhi() {
      boolean var1;
      if((this.CC & 5) == 0) {
         var1 = true;
      } else {
         var1 = false;
      }

      this.LBRANCH(var1);
   }

   private void lble() {
      boolean var1;
      if(this.NXORV() == 0 && (this.CC & 4) == 0) {
         var1 = false;
      } else {
         var1 = true;
      }

      this.LBRANCH(var1);
   }

   private void lbls() {
      this.LBRANCH(this.CC & 5);
   }

   private void lblt() {
      this.LBRANCH(this.NXORV());
   }

   private void lbmi() {
      this.LBRANCH(this.CC & 8);
   }

   private void lbne() {
      boolean var1;
      if((this.CC & 4) == 0) {
         var1 = true;
      } else {
         var1 = false;
      }

      this.LBRANCH(var1);
   }

   private void lbpl() {
      boolean var1;
      if((this.CC & 8) == 0) {
         var1 = true;
      } else {
         var1 = false;
      }

      this.LBRANCH(var1);
   }

   private void lbra() {
      this.EA = this.IMMWORD();
      this.PC += (short)this.EA;
      if(this.EA == '�' && this.konami_ICount > 0) {
         this.konami_ICount = 0;
      }

   }

   private void lbrn() {
      this.IMMWORD();
   }

   private void lbsr() {
      this.EA = this.IMMWORD();
      this.PUSHWORD(this.PC);
      this.PC += (short)this.EA;
   }

   private void lbvc() {
      boolean var1;
      if((this.CC & 2) == 0) {
         var1 = true;
      } else {
         var1 = false;
      }

      this.LBRANCH(var1);
   }

   private void lbvs() {
      this.LBRANCH(this.CC & 2);
   }

   private void lda_di() {
      this.A(this.DIRBYTE());
      this.CLR_NZV();
      this.SET_NZ8(this.A());
   }

   private void lda_ex() {
      this.A(this.EXTBYTE());
      this.CLR_NZV();
      this.SET_NZ8(this.A());
   }

   private void lda_im() {
      this.A(this.IMMBYTE());
      this.CLR_NZV();
      this.SET_NZ8(this.A());
   }

   private void lda_ix() {
      this.A(this.RM(this.EA));
      this.CLR_NZV();
      this.SET_NZ8(this.A());
   }

   private void ldb_di() {
      this.A(this.DIRBYTE());
      this.CLR_NZV();
      this.SET_NZ8(this.A());
   }

   private void ldb_ex() {
      this.B(this.EXTBYTE());
      this.CLR_NZV();
      this.SET_NZ8(this.B());
   }

   private void ldb_im() {
      this.B(this.IMMBYTE());
      this.CLR_NZV();
      this.SET_NZ8(this.B());
   }

   private void ldb_ix() {
      this.B(this.RM(this.EA));
      this.CLR_NZV();
      this.SET_NZ8(this.B());
   }

   private void ldd_di() {
      this.D = this.DIRWORD();
      this.CLR_NZV();
      this.SET_NZ16(this.D);
   }

   private void ldd_ex() {
      this.D = this.EXTWORD();
      this.CLR_NZV();
      this.SET_NZ16(this.D);
   }

   private void ldd_im() {
      this.D = this.IMMWORD();
      this.CLR_NZV();
      this.SET_NZ16(this.D);
   }

   private void ldd_ix() {
      this.D = this.RM16(this.EA);
      this.CLR_NZV();
      this.SET_NZ16(this.D);
   }

   private void lds_di() {
      this.S = this.DIRWORD();
      this.CLR_NZV();
      this.SET_NZ16(this.S);
      this.int_state |= 32;
   }

   private void lds_ex() {
      this.S = this.EXTWORD();
      this.CLR_NZV();
      this.SET_NZ16(this.S);
      this.int_state |= 32;
   }

   private void lds_im() {
      this.S = this.IMMWORD();
      this.CLR_NZV();
      this.SET_NZ16(this.S);
   }

   private void lds_ix() {
      this.S = this.RM16(this.EA);
      this.CLR_NZV();
      this.SET_NZ16(this.S);
      this.int_state |= 32;
   }

   private void ldu_di() {
      this.U = this.DIRWORD();
      this.CLR_NZV();
      this.SET_NZ16(this.U);
   }

   private void ldu_ex() {
      this.U = this.EXTWORD();
      this.CLR_NZV();
      this.SET_NZ16(this.U);
   }

   private void ldu_im() {
      this.U = this.IMMWORD();
      this.CLR_NZV();
      this.SET_NZ16(this.U);
   }

   private void ldu_ix() {
      this.U = this.RM16(this.EA);
      this.CLR_NZV();
      this.SET_NZ16(this.U);
   }

   private void ldx_di() {
      this.X = this.DIRWORD();
      this.CLR_NZV();
      this.SET_NZ16(this.X);
   }

   private void ldx_ex() {
      this.X = this.EXTWORD();
      this.CLR_NZV();
      this.SET_NZ16(this.X);
   }

   private void ldx_im() {
      this.X = this.IMMWORD();
      this.CLR_NZV();
      this.SET_NZ16(this.X);
   }

   private void ldx_ix() {
      this.X = this.RM16(this.EA);
      this.CLR_NZV();
      this.SET_NZ16(this.X);
   }

   private void ldy_di() {
      this.Y = this.DIRWORD();
      this.CLR_NZV();
      this.SET_NZ16(this.Y);
   }

   private void ldy_ex() {
      this.Y = this.EXTWORD();
      this.CLR_NZV();
      this.SET_NZ16(this.Y);
   }

   private void ldy_im() {
      this.Y = this.IMMWORD();
      this.CLR_NZV();
      this.SET_NZ16(this.Y);
   }

   private void ldy_ix() {
      this.Y = this.RM16(this.EA);
      this.CLR_NZV();
      this.SET_NZ16(this.Y);
   }

   private void leas() {
      this.S = this.EA;
      this.int_state |= 32;
   }

   private void leau() {
      this.U = this.EA;
   }

   private void leax() {
      this.X = this.EA;
      this.CLR_Z();
      this.SET_Z(this.X);
   }

   private void leay() {
      this.Y = this.EA;
      this.CLR_Z();
      this.SET_Z(this.Y);
   }

   private void lmul() {
      int var1 = this.X * this.Y;
      this.X = var1 >>> 16;
      this.Y = '\uffff' & var1;
      this.CLR_ZC();
      this.SET_Z(var1);
      if(('耀' & var1) != 0) {
         this.SEC();
      }

   }

   private void lsr_di() {
      int var1 = this.DIRBYTE();
      this.CLR_NZC();
      this.CC |= var1 & 1;
      var1 >>= 1;
      this.SET_Z8(var1);
      this.WM(this.EA, var1);
   }

   private void lsr_ex() {
      int var1 = this.EXTBYTE();
      this.CLR_NZC();
      this.CC |= var1 & 1;
      var1 >>= 1;
      this.SET_Z8(var1);
      this.WM(this.EA, var1);
   }

   private void lsr_ix() {
      int var1 = this.RM(this.EA);
      this.CLR_NZC();
      this.CC |= var1 & 1;
      var1 >>= 1;
      this.SET_Z8(var1);
      this.WM(this.EA, var1);
   }

   private void lsra() {
      this.CLR_NZC();
      this.CC |= this.A() & 1;
      this.A(this.A() >> 1);
      this.SET_Z8(this.A());
   }

   private void lsrb() {
      this.CLR_NZC();
      this.CC |= this.B() & 1;
      this.B(this.B() >> 1);
      this.SET_Z8(this.B());
   }

   private void lsrd() {
      for(int var1 = this.IMMBYTE(); var1 != 0; --var1) {
         this.CLR_NZC();
         this.CC |= this.D & 1;
         this.D >>= 1;
         this.SET_Z16(this.D);
      }

   }

   private void lsrd_di() {
      for(int var1 = this.DIRBYTE(); var1 != 0; --var1) {
         this.CLR_NZC();
         this.CC |= this.D & 1;
         this.D >>= 1;
         this.SET_Z16(this.D);
      }

   }

   private void lsrd_ex() {
      for(int var1 = this.EXTBYTE(); var1 != 0; --var1) {
         this.CLR_NZC();
         this.CC |= this.D & 1;
         this.D >>= 1;
         this.SET_Z16(this.D);
      }

   }

   private void lsrd_ix() {
      for(int var1 = this.RM(this.EA); var1 != 0; --var1) {
         this.CLR_NZC();
         this.CC |= this.D & 1;
         this.D >>= 1;
         this.SET_Z16(this.D);
      }

   }

   private void lsrw_di() {
      int var1 = this.DIRWORD();
      this.CLR_NZC();
      this.CC |= var1 & 1;
      var1 >>= 1;
      this.SET_Z16(var1);
      this.WM16(this.EA, var1);
   }

   private void lsrw_ex() {
      int var1 = this.EXTWORD();
      this.CLR_NZC();
      this.CC |= var1 & 1;
      var1 >>= 1;
      this.SET_Z16(var1);
      this.WM16(this.EA, var1);
   }

   private void lsrw_ix() {
      int var1 = this.RM16(this.EA);
      this.CLR_NZC();
      this.CC |= var1 & 1;
      var1 >>= 1;
      this.SET_Z16(var1);
      this.WM16(this.EA, var1);
   }

   private void move() {
      int var1 = this.RM(this.Y);
      this.WM(this.X, var1);
      ++this.Y;
      ++this.X;
      --this.U;
   }

   private void mul() {
      int var1 = this.A() * this.B() & '\uffff';
      this.CLR_ZC();
      this.SET_Z16(var1);
      if((var1 & 128) != 0) {
         this.SEC();
      }

      this.D = var1;
   }

   private void neg_di() {
      int var1 = this.DIRBYTE();
      int var2 = -var1;
      this.CLR_NZVC();
      this.SET_FLAGS8(0, var1, var2);
      this.WM(this.EA, var2 & 255);
   }

   private void neg_ex() {
      int var1 = this.EXTBYTE();
      int var2 = -var1;
      this.CLR_NZVC();
      this.SET_FLAGS8(0, var1, var2);
      this.WM(this.EA, var2 & 255);
   }

   private void neg_ix() {
      int var1 = this.RM(this.EA);
      int var2 = -var1;
      this.CLR_NZVC();
      this.SET_FLAGS8(0, var1, var2);
      this.WM(this.EA, var2);
   }

   private void nega() {
      int var1 = -this.A();
      this.CLR_NZVC();
      this.SET_FLAGS8(0, this.A(), var1);
      this.A(-this.A());
   }

   private void negb() {
      int var1 = -this.B();
      this.CLR_NZVC();
      this.SET_FLAGS8(0, this.B(), var1);
      this.B(-this.B());
   }

   private void negd() {
      int var1 = -this.D;
      this.CLR_NZVC();
      this.SET_FLAGS16(0, this.D, var1);
      this.D = '\uffff' & var1;
   }

   private void negw_di() {
      int var1 = this.DIRWORD();
      int var2 = -var1;
      this.CLR_NZVC();
      this.SET_FLAGS16(0, var1, var2);
      this.WM16(this.EA, var2);
   }

   private void negw_ex() {
      int var1 = this.EXTWORD();
      int var2 = -var1;
      this.CLR_NZVC();
      this.SET_FLAGS16(0, var1, var2);
      this.WM16(this.EA, var2);
   }

   private void negw_ix() {
      int var2 = this.RM16(this.EA);
      int var1 = -var2;
      this.CLR_NZVC();
      this.SET_FLAGS16(0, var2, var1);
      this.WM16(this.EA, var1);
   }

   private void nop() {
   }

   private void opcode2() {
      int var1 = this.ROP_ARG(this.PC);
      ++this.PC;
      switch(var1) {
      case 7:
         this.EA = 0;
         this.konami_extended(this.ireg);
         this.konami_ICount -= 2;
         return;
      case 8:
      case 9:
      case 10:
      case 11:
      case 12:
      case 13:
      case 14:
      case 16:
      case 17:
      case 18:
      case 19:
      case 20:
      case 21:
      case 22:
      case 23:
      case 24:
      case 25:
      case 26:
      case 27:
      case 28:
      case 29:
      case 30:
      case 31:
      case 39:
      case 47:
      case 55:
      case 63:
      case 64:
      case 65:
      case 66:
      case 67:
      case 68:
      case 69:
      case 70:
      case 71:
      case 72:
      case 73:
      case 74:
      case 75:
      case 76:
      case 77:
      case 78:
      case 79:
      case 87:
      case 95:
      case 103:
      case 111:
      case 119:
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
      case 162:
      case 163:
      case 164:
      case 165:
      case 166:
      case 170:
      case 171:
      case 172:
      case 173:
      case 174:
      case 178:
      case 179:
      case 180:
      case 181:
      case 182:
      case 186:
      case 187:
      case 188:
      case 189:
      case 190:
      case 192:
      case 193:
      case 194:
      case 195:
      case 197:
      case 198:
      case 199:
      case 200:
      case 201:
      case 202:
      case 203:
      case 205:
      case 206:
      case 207:
      case 210:
      case 211:
      case 212:
      case 213:
      case 214:
      case 218:
      case 219:
      case 220:
      case 221:
      case 222:
      case 226:
      case 227:
      case 228:
      case 229:
      case 230:
      case 234:
      case 235:
      case 236:
      case 237:
      case 238:
      case 242:
      case 243:
      case 244:
      case 245:
      case 246:
      case 250:
      case 251:
      case 252:
      case 253:
      case 254:
      default:
         System.out.println("KONAMI: Unknown/Invalid postbyte at PC = 0x" + Integer.toHexString(this.PC - 1));
         this.EA = 0;
         break;
      case 15:
         this.EA = this.IMMWORD();
         this.EA = this.RM16(this.EA);
         this.konami_ICount -= 4;
         break;
      case 32:
         this.EA = this.X++;
         this.konami_ICount -= 2;
         break;
      case 33:
         this.EA = this.X;
         this.X += 2;
         this.konami_ICount -= 3;
         break;
      case 34:
         --this.X;
         this.EA = this.X;
         this.konami_ICount -= 2;
         break;
      case 35:
         this.X -= 2;
         this.EA = this.X;
         this.konami_ICount -= 3;
         break;
      case 36:
         this.EA = this.IMMBYTE();
         this.EA = this.X + (byte)this.EA;
         this.konami_ICount -= 2;
         break;
      case 37:
         this.EA = this.IMMWORD();
         this.EA += this.X;
         this.konami_ICount -= 4;
         break;
      case 38:
         this.EA = this.X;
         break;
      case 40:
         this.EA = this.X++;
         this.EA = this.RM16(this.EA);
         this.konami_ICount -= 5;
         break;
      case 41:
         this.EA = this.X;
         this.X += 2;
         this.EA = this.RM16(this.EA);
         this.konami_ICount -= 6;
         break;
      case 42:
         --this.X;
         this.EA = this.X;
         this.EA = this.RM16(this.EA);
         this.konami_ICount -= 5;
         break;
      case 43:
         this.X -= 2;
         this.EA = this.X;
         this.EA = this.RM16(this.EA);
         this.konami_ICount -= 6;
         break;
      case 44:
         this.EA = this.IMMBYTE();
         this.EA = this.X + (byte)this.EA;
         this.EA = this.RM16(this.EA);
         this.konami_ICount -= 4;
         break;
      case 45:
         this.EA = this.IMMWORD();
         this.EA += this.X;
         this.EA = this.RM16(this.EA);
         this.konami_ICount -= 7;
         break;
      case 46:
         this.EA = this.X;
         this.EA = this.RM16(this.EA);
         this.konami_ICount -= 3;
         break;
      case 48:
         this.EA = this.Y++;
         this.konami_ICount -= 2;
         break;
      case 49:
         this.EA = this.Y;
         this.Y += 2;
         this.konami_ICount -= 3;
         break;
      case 50:
         --this.Y;
         this.EA = this.Y;
         this.konami_ICount -= 2;
         break;
      case 51:
         this.Y -= 2;
         this.EA = this.Y;
         this.konami_ICount -= 3;
         break;
      case 52:
         this.EA = this.IMMBYTE();
         this.EA = this.Y + (byte)this.EA;
         this.konami_ICount -= 2;
         break;
      case 53:
         this.EA = this.IMMWORD();
         this.EA += this.Y;
         this.konami_ICount -= 4;
         break;
      case 54:
         this.EA = this.Y;
         break;
      case 56:
         this.EA = this.Y++;
         this.EA = this.RM16(this.EA);
         this.konami_ICount -= 5;
         break;
      case 57:
         this.EA = this.Y;
         this.Y += 2;
         this.EA = this.RM16(this.EA);
         this.konami_ICount -= 6;
         break;
      case 58:
         --this.Y;
         this.EA = this.Y;
         this.EA = this.RM16(this.EA);
         this.konami_ICount -= 5;
         break;
      case 59:
         this.Y -= 2;
         this.EA = this.Y;
         this.EA = this.RM16(this.EA);
         this.konami_ICount -= 6;
         break;
      case 60:
         this.EA = this.IMMBYTE();
         this.EA = this.Y + (byte)this.EA;
         this.EA = this.RM16(this.EA);
         this.konami_ICount -= 4;
         break;
      case 61:
         this.EA = this.IMMWORD();
         this.EA += this.Y;
         this.EA = this.RM16(this.EA);
         this.konami_ICount -= 7;
         break;
      case 62:
         this.EA = this.Y;
         this.EA = this.RM16(this.EA);
         this.konami_ICount -= 3;
         break;
      case 80:
         this.EA = this.U++;
         this.konami_ICount -= 2;
         break;
      case 81:
         this.EA = this.U;
         this.U += 2;
         this.konami_ICount -= 3;
         break;
      case 82:
         --this.U;
         this.EA = this.U;
         this.konami_ICount -= 2;
         break;
      case 83:
         this.U -= 2;
         this.EA = this.U;
         this.konami_ICount -= 3;
         break;
      case 84:
         this.EA = this.IMMBYTE();
         this.EA = this.U + (byte)this.EA;
         this.konami_ICount -= 2;
         break;
      case 85:
         this.EA = this.IMMWORD();
         this.EA += this.U;
         this.konami_ICount -= 4;
         break;
      case 86:
         this.EA = this.U;
         break;
      case 88:
         this.EA = this.U++;
         this.EA = this.RM16(this.EA);
         this.konami_ICount -= 5;
         break;
      case 89:
         this.EA = this.U;
         this.U += 2;
         this.EA = this.RM16(this.EA);
         this.konami_ICount -= 6;
         break;
      case 90:
         --this.U;
         this.EA = this.U;
         this.EA = this.RM16(this.EA);
         this.konami_ICount -= 5;
         break;
      case 91:
         this.U -= 2;
         this.EA = this.U;
         this.EA = this.RM16(this.EA);
         this.konami_ICount -= 6;
         break;
      case 92:
         this.EA = this.IMMBYTE();
         this.EA = this.U + (byte)this.EA;
         this.EA = this.RM16(this.EA);
         this.konami_ICount -= 4;
         break;
      case 93:
         this.EA = this.IMMWORD();
         this.EA += this.U;
         this.EA = this.RM16(this.EA);
         this.konami_ICount -= 7;
         break;
      case 94:
         this.EA = this.U;
         this.EA = this.RM16(this.EA);
         this.konami_ICount -= 3;
         break;
      case 96:
         this.EA = this.S++;
         this.konami_ICount -= 2;
         break;
      case 97:
         this.EA = this.S;
         this.S += 2;
         this.konami_ICount -= 3;
         break;
      case 98:
         --this.S;
         this.EA = this.S;
         this.konami_ICount -= 2;
         break;
      case 99:
         this.S -= 2;
         this.EA = this.S;
         this.konami_ICount -= 3;
         break;
      case 100:
         this.EA = this.IMMBYTE();
         this.EA = this.S + (byte)this.EA;
         this.konami_ICount -= 2;
         break;
      case 101:
         this.EA = this.IMMWORD();
         this.EA += this.S;
         this.konami_ICount -= 4;
         break;
      case 102:
         this.EA = this.S;
         break;
      case 104:
         this.EA = this.S++;
         this.EA = this.RM16(this.EA);
         this.konami_ICount -= 5;
         break;
      case 105:
         this.EA = this.S;
         this.S += 2;
         this.EA = this.RM16(this.EA);
         this.konami_ICount -= 6;
         break;
      case 106:
         --this.S;
         this.EA = this.S;
         this.EA = this.RM16(this.EA);
         this.konami_ICount -= 5;
         break;
      case 107:
         this.S -= 2;
         this.EA = this.S;
         this.EA = this.RM16(this.EA);
         this.konami_ICount -= 6;
         break;
      case 108:
         this.EA = this.IMMBYTE();
         this.EA = this.S + (byte)this.EA;
         this.EA = this.RM16(this.EA);
         this.konami_ICount -= 4;
         break;
      case 109:
         this.EA = this.IMMWORD();
         this.EA += this.S;
         this.EA = this.RM16(this.EA);
         this.konami_ICount -= 7;
         break;
      case 110:
         this.EA = this.S;
         this.EA = this.RM16(this.EA);
         this.konami_ICount -= 3;
         break;
      case 112:
         this.EA = this.PC++;
         this.konami_ICount -= 2;
         break;
      case 113:
         this.EA = this.PC;
         this.PC += 2;
         this.konami_ICount -= 3;
         break;
      case 114:
         --this.PC;
         this.EA = this.PC;
         this.konami_ICount -= 2;
         break;
      case 115:
         this.PC -= 2;
         this.EA = this.PC;
         this.konami_ICount -= 3;
         break;
      case 116:
         this.EA = this.IMMBYTE();
         this.EA = this.PC - 1 + (byte)this.EA;
         this.konami_ICount -= 2;
         break;
      case 117:
         this.EA = this.IMMWORD();
         this.EA += this.PC - 2;
         this.konami_ICount -= 4;
         break;
      case 118:
         this.EA = this.PC;
         break;
      case 120:
         this.EA = this.PC++;
         this.EA = this.RM16(this.EA);
         this.konami_ICount -= 5;
         break;
      case 121:
         this.EA = this.PC;
         this.PC += 2;
         this.EA = this.RM16(this.EA);
         this.konami_ICount -= 6;
         break;
      case 122:
         --this.PC;
         this.EA = this.PC;
         this.EA = this.RM16(this.EA);
         this.konami_ICount -= 5;
         break;
      case 123:
         this.PC -= 2;
         this.EA = this.PC;
         this.EA = this.RM16(this.EA);
         this.konami_ICount -= 6;
         break;
      case 124:
         this.EA = this.IMMBYTE();
         this.EA = this.PC - 1 + (byte)this.EA;
         this.EA = this.RM16(this.EA);
         this.konami_ICount -= 4;
         break;
      case 125:
         this.EA = this.IMMWORD();
         this.EA += this.PC - 2;
         this.EA = this.RM16(this.EA);
         this.konami_ICount -= 7;
         break;
      case 126:
         this.EA = this.PC;
         this.EA = this.RM16(this.EA);
         this.konami_ICount -= 3;
         break;
      case 160:
         this.EA = this.X + (byte)this.A();
         --this.konami_ICount;
         break;
      case 161:
         this.EA = this.X + (byte)this.B();
         --this.konami_ICount;
         break;
      case 167:
         this.EA = this.X + this.D;
         this.konami_ICount -= 4;
         break;
      case 168:
         this.EA = this.X + (byte)this.A();
         this.EA = this.RM16(this.EA);
         this.konami_ICount -= 4;
         break;
      case 169:
         this.EA = this.X + (byte)this.B();
         this.EA = this.RM16(this.EA);
         this.konami_ICount -= 4;
         break;
      case 175:
         this.EA = this.X + this.D;
         this.EA = this.RM16(this.EA);
         this.konami_ICount -= 7;
         break;
      case 176:
         this.EA = this.Y + (byte)this.A();
         --this.konami_ICount;
         break;
      case 177:
         this.EA = this.Y + (byte)this.B();
         --this.konami_ICount;
         break;
      case 183:
         this.EA = this.Y + this.D;
         this.konami_ICount -= 4;
         break;
      case 184:
         this.EA = this.Y + (byte)this.A();
         this.EA = this.RM16(this.EA);
         this.konami_ICount -= 4;
         break;
      case 185:
         this.EA = this.Y + (byte)this.B();
         this.EA = this.RM16(this.EA);
         this.konami_ICount -= 4;
         break;
      case 191:
         this.EA = this.Y + this.D;
         this.EA = this.RM16(this.EA);
         this.konami_ICount -= 7;
         break;
      case 196:
         this.EA = 0;
         this.konami_direct(this.ireg);
         --this.konami_ICount;
         return;
      case 204:
         this.EA = this.DIRWORD();
         this.konami_ICount -= 4;
         break;
      case 208:
         this.EA = this.U + (byte)this.A();
         --this.konami_ICount;
         break;
      case 209:
         this.EA = this.U + (byte)this.B();
         --this.konami_ICount;
         break;
      case 215:
         this.EA = this.U + this.D;
         this.konami_ICount -= 4;
         break;
      case 216:
         this.EA = this.U + (byte)this.A();
         this.EA = this.RM16(this.EA);
         this.konami_ICount -= 4;
         break;
      case 217:
         this.EA = this.U + (byte)this.B();
         this.EA = this.RM16(this.EA);
         this.konami_ICount -= 4;
         break;
      case 223:
         this.EA = this.U + this.D;
         this.EA = this.RM16(this.EA);
         this.konami_ICount -= 7;
         break;
      case 224:
         this.EA = this.S + (byte)this.A();
         --this.konami_ICount;
         break;
      case 225:
         this.EA = this.S + (byte)this.B();
         --this.konami_ICount;
         break;
      case 231:
         this.EA = this.S + this.D;
         this.konami_ICount -= 4;
         break;
      case 232:
         this.EA = this.S + (byte)this.A();
         this.EA = this.RM16(this.EA);
         this.konami_ICount -= 4;
         break;
      case 233:
         this.EA = this.S + (byte)this.B();
         this.EA = this.RM16(this.EA);
         this.konami_ICount -= 4;
         break;
      case 239:
         this.EA = this.S + this.D;
         this.EA = this.RM16(this.EA);
         this.konami_ICount -= 7;
         break;
      case 240:
         this.EA = this.PC + (byte)this.A();
         --this.konami_ICount;
         break;
      case 241:
         this.EA = this.PC + (byte)this.B();
         --this.konami_ICount;
         break;
      case 247:
         this.EA = this.PC + this.D;
         this.konami_ICount -= 4;
         break;
      case 248:
         this.EA = this.PC + (byte)this.A();
         this.EA = this.RM16(this.EA);
         this.konami_ICount -= 4;
         break;
      case 249:
         this.EA = this.PC + (byte)this.B();
         this.EA = this.RM16(this.EA);
         this.konami_ICount -= 4;
         break;
      case 255:
         this.EA = this.PC + this.D;
         this.EA = this.RM16(this.EA);
         this.konami_ICount -= 7;
      }

      this.konami_indexed(this.ireg);
   }

   private void opcodeMain(int var1) {
      switch(var1) {
      case 0:
      case 1:
      case 2:
      case 3:
      case 4:
      case 5:
      case 6:
      case 7:
         this.illegal(var1);
         break;
      case 8:
      case 9:
      case 10:
      case 11:
         this.opcode2();
         break;
      case 12:
         this.pshs();
         break;
      case 13:
         this.pshu();
         break;
      case 14:
         this.puls();
         break;
      case 15:
         this.pulu();
         break;
      case 16:
         this.lda_im();
         break;
      case 17:
         this.ldb_im();
         break;
      case 18:
      case 19:
         this.opcode2();
         break;
      case 20:
         this.adda_im();
         break;
      case 21:
         this.addb_im();
         break;
      case 22:
      case 23:
         this.opcode2();
         break;
      case 24:
         this.adca_im();
         break;
      case 25:
         this.adcb_im();
         break;
      case 26:
      case 27:
         this.opcode2();
         break;
      case 28:
         this.suba_im();
         break;
      case 29:
         this.subb_im();
         break;
      case 30:
      case 31:
         this.opcode2();
         break;
      case 32:
         this.sbca_im();
         break;
      case 33:
         this.sbcb_im();
         break;
      case 34:
      case 35:
         this.opcode2();
         break;
      case 36:
         this.anda_im();
         break;
      case 37:
         this.andb_im();
         break;
      case 38:
      case 39:
         this.opcode2();
         break;
      case 40:
         this.bita_im();
         break;
      case 41:
         this.bitb_im();
         break;
      case 42:
      case 43:
         this.opcode2();
         break;
      case 44:
         this.eora_im();
         break;
      case 45:
         this.eorb_im();
         break;
      case 46:
      case 47:
         this.opcode2();
         break;
      case 48:
         this.ora_im();
         break;
      case 49:
         this.orb_im();
         break;
      case 50:
      case 51:
         this.opcode2();
         break;
      case 52:
         this.cmpa_im();
         break;
      case 53:
         this.cmpb_im();
         break;
      case 54:
      case 55:
         this.opcode2();
         break;
      case 56:
         this.setline_im();
         break;
      case 57:
      case 58:
      case 59:
         this.opcode2();
         break;
      case 60:
         this.andcc();
         break;
      case 61:
         this.orcc();
         break;
      case 62:
         this.exg();
         break;
      case 63:
         this.tfr();
         break;
      case 64:
         this.ldd_im();
         break;
      case 65:
         this.opcode2();
         break;
      case 66:
         this.ldx_im();
         break;
      case 67:
         this.opcode2();
         break;
      case 68:
         this.ldy_im();
         break;
      case 69:
         this.opcode2();
         break;
      case 70:
         this.ldu_im();
         break;
      case 71:
         this.opcode2();
         break;
      case 72:
         this.lds_im();
         break;
      case 73:
         this.opcode2();
         break;
      case 74:
         this.cmpd_im();
         break;
      case 75:
         this.opcode2();
         break;
      case 76:
         this.cmpx_im();
         break;
      case 77:
         this.opcode2();
         break;
      case 78:
         this.cmpy_im();
         break;
      case 79:
         this.opcode2();
         break;
      case 80:
         this.cmpu_im();
         break;
      case 81:
         this.opcode2();
         break;
      case 82:
         this.cmps_im();
         break;
      case 83:
         this.opcode2();
         break;
      case 84:
         this.addd_im();
         break;
      case 85:
         this.opcode2();
         break;
      case 86:
         this.subd_im();
         break;
      case 87:
         this.opcode2();
         break;
      case 88:
         this.opcode2();
         break;
      case 89:
         this.opcode2();
         break;
      case 90:
         this.opcode2();
         break;
      case 91:
         this.opcode2();
         break;
      case 92:
         this.opcode2();
         break;
      case 93:
         this.illegal(var1);
         break;
      case 94:
         this.illegal(var1);
         break;
      case 95:
         this.illegal(var1);
         break;
      case 96:
         this.bra();
         break;
      case 97:
         this.bhi();
         break;
      case 98:
         this.bcc();
         break;
      case 99:
         this.bne();
         break;
      case 100:
         this.bvc();
         break;
      case 101:
         this.bpl();
         break;
      case 102:
         this.bge();
         break;
      case 103:
         this.bgt();
         break;
      case 104:
         this.lbra();
         break;
      case 105:
         this.lbhi();
         break;
      case 106:
         this.lbcc();
         break;
      case 107:
         this.lbne();
         break;
      case 108:
         this.lbvc();
         break;
      case 109:
         this.lbpl();
         break;
      case 110:
         this.lbge();
         break;
      case 111:
         this.lbgt();
         break;
      case 112:
         this.brn();
         break;
      case 113:
         this.bls();
         break;
      case 114:
         this.bcs();
         break;
      case 115:
         this.beq();
         break;
      case 116:
         this.bvs();
         break;
      case 117:
         this.bmi();
         break;
      case 118:
         this.blt();
         break;
      case 119:
         this.ble();
         break;
      case 120:
         this.lbrn();
         break;
      case 121:
         this.lbls();
         break;
      case 122:
         this.lbcs();
         break;
      case 123:
         this.lbeq();
         break;
      case 124:
         this.lbvs();
         break;
      case 125:
         this.lbmi();
         break;
      case 126:
         this.lblt();
         break;
      case 127:
         this.lble();
         break;
      case 128:
         this.clra();
         break;
      case 129:
         this.clrb();
         break;
      case 130:
         this.opcode2();
         break;
      case 131:
         this.coma();
         break;
      case 132:
         this.comb();
         break;
      case 133:
         this.opcode2();
         break;
      case 134:
         this.nega();
         break;
      case 135:
         this.negb();
         break;
      case 136:
         this.opcode2();
         break;
      case 137:
         this.inca();
         break;
      case 138:
         this.incb();
         break;
      case 139:
         this.opcode2();
         break;
      case 140:
         this.deca();
         break;
      case 141:
         this.decb();
         break;
      case 142:
         this.opcode2();
         break;
      case 143:
         this.rts();
         break;
      case 144:
         this.tsta();
         break;
      case 145:
         this.tstb();
         break;
      case 146:
         this.opcode2();
         break;
      case 147:
         this.lsra();
         break;
      case 148:
         this.lsrb();
         break;
      case 149:
         this.opcode2();
         break;
      case 150:
         this.rora();
         break;
      case 151:
         this.rorb();
         break;
      case 152:
         this.opcode2();
         break;
      case 153:
         this.asra();
         break;
      case 154:
         this.asrb();
         break;
      case 155:
         this.opcode2();
         break;
      case 156:
         this.asla();
         break;
      case 157:
         this.aslb();
         break;
      case 158:
         this.opcode2();
         break;
      case 159:
         this.rti();
         break;
      case 160:
         this.rola();
         break;
      case 161:
         this.rolb();
         break;
      case 162:
         this.opcode2();
         break;
      case 163:
         this.opcode2();
         break;
      case 164:
         this.opcode2();
         break;
      case 165:
         this.opcode2();
         break;
      case 166:
         this.opcode2();
         break;
      case 167:
         this.opcode2();
         break;
      case 168:
         this.opcode2();
         break;
      case 169:
         this.opcode2();
         break;
      case 170:
         this.bsr();
         break;
      case 171:
         this.lbsr();
         break;
      case 172:
         this.decbjnz();
         break;
      case 173:
         this.decxjnz();
         break;
      case 174:
         this.nop();
         break;
      case 175:
         this.illegal(var1);
         break;
      case 176:
         this.abx();
         break;
      case 177:
         this.daa();
         break;
      case 178:
         this.sex();
         break;
      case 179:
         this.mul();
         break;
      case 180:
         this.lmul();
         break;
      case 181:
         this.divx();
         break;
      case 182:
         this.bmove();
         break;
      case 183:
         this.move();
         break;
      case 184:
         this.lsrd();
         break;
      case 185:
         this.opcode2();
         break;
      case 186:
         this.rord();
         break;
      case 187:
         this.opcode2();
         break;
      case 188:
         this.asrd();
         break;
      case 189:
         this.opcode2();
         break;
      case 190:
         this.asld();
         break;
      case 191:
         this.opcode2();
         break;
      case 192:
         this.rold();
         break;
      case 193:
         this.opcode2();
         break;
      case 194:
         this.clrd();
         break;
      case 195:
         this.opcode2();
         break;
      case 196:
         this.negd();
         break;
      case 197:
         this.opcode2();
         break;
      case 198:
         this.incd();
         break;
      case 199:
         this.opcode2();
         break;
      case 200:
         this.decd();
         break;
      case 201:
         this.opcode2();
         break;
      case 202:
         this.tstd();
         break;
      case 203:
         this.opcode2();
         break;
      case 204:
         this.absa();
         break;
      case 205:
         this.opcode2();
         break;
      case 206:
         this.absd();
         break;
      case 207:
         this.bset();
         break;
      case 208:
         this.bset2();
         break;
      default:
         this.illegal(var1);
      }

   }

   private void ora_im() {
      int var1 = this.IMMBYTE();
      this.A(this.A() | var1);
      this.CLR_NZV();
      this.SET_NZ8(this.A());
   }

   private void ora_ix() {
      this.A(this.A() | this.RM(this.EA));
      this.CLR_NZV();
      this.SET_NZ8(this.A());
   }

   private void orb_im() {
      int var1 = this.IMMBYTE();
      this.B(this.B() | var1);
      this.CLR_NZV();
      this.SET_NZ8(this.B());
   }

   private void orb_ix() {
      this.B(this.B() | this.RM(this.EA));
      this.CLR_NZV();
      this.SET_NZ8(this.B());
   }

   private void orcc() {
      int var1 = this.IMMBYTE();
      this.CC |= var1;
      this.CHECK_IRQ_LINES();
   }

   private void pshs() {
      int var1 = this.IMMBYTE();
      if((var1 & 128) != 0) {
         this.PUSHWORD(this.PC);
         this.konami_ICount -= 2;
      }

      if((var1 & 64) != 0) {
         this.PUSHWORD(this.U);
         this.konami_ICount -= 2;
      }

      if((var1 & 32) != 0) {
         this.PUSHWORD(this.Y);
         this.konami_ICount -= 2;
      }

      if((var1 & 16) != 0) {
         this.PUSHWORD(this.X);
         this.konami_ICount -= 2;
      }

      if((var1 & 8) != 0) {
         this.PUSHBYTE(this.DP);
         --this.konami_ICount;
      }

      if((var1 & 4) != 0) {
         this.PUSHBYTE(this.B());
         --this.konami_ICount;
      }

      if((var1 & 2) != 0) {
         this.PUSHBYTE(this.A());
         --this.konami_ICount;
      }

      if((var1 & 1) != 0) {
         this.PUSHBYTE(this.CC);
         --this.konami_ICount;
      }

   }

   private void pshu() {
      int var1 = this.IMMBYTE();
      if((var1 & 128) != 0) {
         this.PSHUWORD(this.PC);
         this.konami_ICount -= 2;
      }

      if((var1 & 64) != 0) {
         this.PSHUWORD(this.S);
         this.konami_ICount -= 2;
      }

      if((var1 & 32) != 0) {
         this.PSHUWORD(this.Y);
         this.konami_ICount -= 2;
      }

      if((var1 & 16) != 0) {
         this.PSHUWORD(this.X);
         this.konami_ICount -= 2;
      }

      if((var1 & 8) != 0) {
         this.PSHUBYTE(this.DP);
         --this.konami_ICount;
      }

      if((var1 & 4) != 0) {
         this.PSHUBYTE(this.B());
         --this.konami_ICount;
      }

      if((var1 & 2) != 0) {
         this.PSHUBYTE(this.A());
         --this.konami_ICount;
      }

      if((var1 & 1) != 0) {
         this.PSHUBYTE(this.CC);
         --this.konami_ICount;
      }

   }

   private void puls() {
      int var1 = this.IMMBYTE();
      if((var1 & 1) != 0) {
         this.CC = this.PULLBYTE();
         --this.konami_ICount;
      }

      if((var1 & 2) != 0) {
         this.A(this.PULLBYTE());
         --this.konami_ICount;
      }

      if((var1 & 4) != 0) {
         this.B(this.PULLBYTE());
         --this.konami_ICount;
      }

      if((var1 & 8) != 0) {
         this.DP = this.PULLBYTE();
         --this.konami_ICount;
      }

      if((var1 & 16) != 0) {
         this.X = this.PULLWORD();
         this.konami_ICount -= 2;
      }

      if((var1 & 32) != 0) {
         this.Y = this.PULLWORD();
         this.konami_ICount -= 2;
      }

      if((var1 & 64) != 0) {
         this.U = this.PULLWORD();
         this.konami_ICount -= 2;
      }

      if((var1 & 128) != 0) {
         this.PC = this.PULLWORD();
         this.konami_ICount -= 2;
      }

      if((var1 & 1) != 0) {
         this.CHECK_IRQ_LINES();
      }

   }

   private void pulu() {
      int var1 = this.IMMBYTE();
      if((var1 & 1) != 0) {
         this.CC = this.PULUBYTE();
         --this.konami_ICount;
      }

      if((var1 & 2) != 0) {
         this.A(this.PULUBYTE());
         --this.konami_ICount;
      }

      if((var1 & 4) != 0) {
         this.B(this.PULUBYTE());
         --this.konami_ICount;
      }

      if((var1 & 8) != 0) {
         this.DP = this.PULUBYTE();
         --this.konami_ICount;
      }

      if((var1 & 16) != 0) {
         this.X = this.PULUWORD();
         this.konami_ICount -= 2;
      }

      if((var1 & 32) != 0) {
         this.Y = this.PULUWORD();
         this.konami_ICount -= 2;
      }

      if((var1 & 64) != 0) {
         this.S = this.PULUWORD();
         this.konami_ICount -= 2;
      }

      if((var1 & 128) != 0) {
         this.PC = this.PULUWORD();
         this.konami_ICount -= 2;
      }

      if((var1 & 1) != 0) {
         this.CHECK_IRQ_LINES();
      }

   }

   private void rol_di() {
      int var1 = this.DIRBYTE();
      int var2 = this.CC & 1 | var1 << 1;
      this.CLR_NZVC();
      this.SET_FLAGS8(var1, var1, var2);
      this.WM(this.EA, var2);
   }

   private void rol_ex() {
      int var2 = this.EXTBYTE();
      int var1 = this.CC & 1 | var2 << 1;
      this.CLR_NZVC();
      this.SET_FLAGS8(var2, var2, var1);
      this.WM(this.EA, var1);
   }

   private void rol_ix() {
      int var2 = this.RM(this.EA);
      int var1 = this.CC & 1 | var2 << 1 & 255;
      this.CLR_NZVC();
      this.SET_FLAGS8(var2, var2, var1);
      this.WM(this.EA, var1);
   }

   private void rola() {
      int var1 = this.A();
      int var2 = this.CC & 1 | var1 << 1;
      this.CLR_NZVC();
      this.SET_FLAGS8(var1, var1, var2);
      this.A(var2);
   }

   private void rolb() {
      int var1 = this.B();
      int var2 = this.CC & 1 | var1 << 1;
      this.CLR_NZVC();
      this.SET_FLAGS8(var1, var1, var2);
      this.B(var2);
   }

   private void rold() {
      for(int var1 = this.IMMBYTE(); var1 != 0; --var1) {
         this.CLR_NZC();
         if((this.D & '耀') != 0) {
            this.SEC();
         }

         int var2 = this.CC & 1 | this.D << 1;
         this.SET_NZ16(var2);
         this.D = '\uffff' & var2;
      }

   }

   private void rold_di() {
      for(int var1 = this.DIRBYTE(); var1 != 0; --var1) {
         this.CLR_NZC();
         if((this.D & '耀') != 0) {
            this.SEC();
         }

         int var2 = this.CC & 1 | this.D << 1;
         this.SET_NZ16(var2);
         this.D = var2;
      }

   }

   private void rold_ex() {
      for(int var1 = this.EXTBYTE(); var1 != 0; --var1) {
         this.CLR_NZC();
         if((this.D & '耀') != 0) {
            this.SEC();
         }

         int var2 = this.CC & 1 | this.D << 1;
         this.SET_NZ16(var2);
         this.D = '\uffff' & var2;
      }

   }

   private void rold_ix() {
      for(int var1 = this.RM(this.EA); var1 != 0; --var1) {
         this.CLR_NZC();
         if((this.D & '耀') != 0) {
            this.SEC();
         }

         int var2 = this.CC & 1 | this.D << 1;
         this.SET_NZ16(var2);
         this.D = var2;
      }

   }

   private void rolw_di() {
      int var1 = this.DIRWORD();
      int var2 = this.CC & 1 | var1 << 1;
      this.CLR_NZVC();
      this.SET_FLAGS16(var1, var1, var2);
      this.WM16(this.EA, var2);
   }

   private void rolw_ex() {
      int var2 = this.EXTWORD();
      int var1 = (this.CC & 1 | var2 << 1) & '\uffff';
      this.CLR_NZVC();
      this.SET_FLAGS16(var2, var2, var1);
      this.WM16(this.EA, var1);
   }

   private void rolw_ix() {
      int var2 = this.RM16(this.EA);
      int var1 = (this.CC & 1 | var2 << 1) & '\uffff';
      this.CLR_NZVC();
      this.SET_FLAGS16(var2, var2, var1);
      this.WM16(this.EA, var1);
   }

   private void ror_di() {
      int var1 = this.DIRBYTE();
      int var2 = this.CC;
      this.CLR_NZC();
      this.CC |= var1 & 1;
      var1 = (var2 & 1) << 7 | var1 >> 1;
      this.SET_NZ8(var1);
      this.WM(this.EA, var1);
   }

   private void ror_ex() {
      int var1 = this.EXTBYTE();
      int var2 = this.CC;
      this.CLR_NZC();
      this.CC |= var1 & 1;
      var1 = (var2 & 1) << 7 | var1 >> 1;
      this.SET_NZ8(var1);
      this.WM(this.EA, var1 & 255);
   }

   private void ror_ix() {
      int var2 = this.RM(this.EA);
      int var1 = this.CC;
      this.CLR_NZC();
      this.CC |= var2 & 1;
      var1 = (var1 & 1) << 7 | var2 >> 1;
      this.SET_NZ8(var1);
      this.WM(this.EA, var1);
   }

   private void rora() {
      int var1 = this.CC;
      this.CLR_NZC();
      this.CC |= this.A() & 1;
      var1 = (var1 & 1) << 7 | this.A() >> 1;
      this.SET_NZ8(var1);
      this.A(var1);
   }

   private void rorb() {
      int var1 = this.CC;
      this.CLR_NZC();
      this.CC |= this.B() & 1;
      var1 = (var1 & 1) << 7 | this.B() >> 1;
      this.SET_NZ8(var1);
      this.B(var1);
   }

   private void rord() {
      for(int var1 = this.IMMBYTE(); var1 != 0; --var1) {
         int var2 = this.CC;
         this.CLR_NZC();
         this.CC |= this.D & 1;
         var2 = (var2 & 1) << 15 | this.D >> 1;
         this.SET_NZ16(var2);
         this.D = '\uffff' & var2;
      }

   }

   private void rord_di() {
      for(int var1 = this.DIRBYTE(); var1 != 0; --var1) {
         int var2 = this.CC;
         this.CLR_NZC();
         this.CC |= this.D & 1;
         var2 = (var2 & 1) << 15 | this.D >> 1;
         this.SET_NZ16(var2);
         this.D = var2;
      }

   }

   private void rord_ex() {
      for(int var1 = this.EXTBYTE(); var1 != 0; --var1) {
         int var2 = this.CC;
         this.CLR_NZC();
         this.CC |= this.D & 1;
         var2 = (var2 & 1) << 15 | this.D >> 1;
         this.SET_NZ16(var2);
         this.D = var2;
      }

   }

   private void rord_ix() {
      for(int var1 = this.RM(this.EA); var1 != 0; --var1) {
         int var2 = this.CC;
         this.CLR_NZC();
         this.CC |= this.D & 1;
         var2 = (var2 & 1) << 15 | this.D >> 1;
         this.SET_NZ16(var2);
         this.D = var2;
      }

   }

   private void rorw_di() {
      int var2 = this.DIRWORD();
      int var1 = this.CC;
      this.CLR_NZC();
      this.CC |= var2 & 1;
      var1 = (var1 & 1) << 15 | var2 >> 1;
      this.SET_NZ16(var1);
      this.WM16(this.EA, var1);
   }

   private void rorw_ex() {
      int var2 = this.EXTWORD();
      int var1 = this.CC;
      this.CLR_NZC();
      this.CC |= var2 & 1;
      var1 = (var1 & 1) << 15 | var2 >> 1;
      this.SET_NZ16(var1);
      this.WM16(this.EA, var1);
   }

   private void rorw_ix() {
      int var2 = this.RM16(this.EA);
      int var1 = this.CC;
      this.CLR_NZC();
      this.CC |= var2 & 1;
      var1 = (var1 & 1) << 15 | var2 >> 1;
      this.SET_NZ16(var1);
      this.WM16(this.EA, var1);
   }

   private void rti() {
      this.CC = this.PULLBYTE();
      if((this.CC & 128) != 0) {
         this.konami_ICount -= 9;
         this.A(this.PULLBYTE());
         this.B(this.PULLBYTE());
         this.DP = this.PULLBYTE();
         this.X = this.PULLWORD();
         this.Y = this.PULLWORD();
         this.U = this.PULLWORD();
      }

      this.PC = this.PULLWORD();
      this.CHECK_IRQ_LINES();
   }

   private void rts() {
      this.PC = this.PULLWORD();
   }

   private void sbca_di() {
      int var1 = this.DIRBYTE();
      int var2 = this.A() - var1 - (this.CC & 1);
      this.CLR_NZVC();
      this.SET_FLAGS8(this.A(), var1, var2);
      this.A(var2);
   }

   private void sbca_ex() {
      int var1 = this.EXTBYTE();
      int var2 = this.A() - var1 - (this.CC & 1);
      this.CLR_NZVC();
      this.SET_FLAGS8(this.A(), var1, var2);
      this.A(var2);
   }

   private void sbca_im() {
      int var1 = this.IMMBYTE();
      int var2 = this.A() - var1 - (this.CC & 1);
      this.CLR_NZVC();
      this.SET_FLAGS8(this.A(), var1, var2);
      this.A(var2);
   }

   private void sbca_ix() {
      int var2 = this.RM(this.EA);
      int var1 = this.A() - var2 - (this.CC & 1);
      this.CLR_NZVC();
      this.SET_FLAGS8(this.A(), var2, var1);
      this.A(var1);
   }

   private void sbcb_di() {
      int var1 = this.DIRBYTE();
      int var2 = this.B() - var1 - (this.CC & 1);
      this.CLR_NZVC();
      this.SET_FLAGS8(this.B(), var1, var2);
      this.B(var2);
   }

   private void sbcb_ex() {
      int var2 = this.EXTBYTE();
      int var1 = this.B() - var2 - (this.CC & 1);
      this.CLR_NZVC();
      this.SET_FLAGS8(this.B(), var2, var1);
      this.B(var1);
   }

   private void sbcb_im() {
      int var1 = this.IMMBYTE();
      int var2 = this.B() - var1 - (this.CC & 1);
      this.CLR_NZVC();
      this.SET_FLAGS8(this.B(), var1, var2);
      this.B(var2);
   }

   private void sbcb_ix() {
      int var2 = this.RM(this.EA);
      int var1 = this.B() - var2 - (this.CC & 1);
      this.CLR_NZVC();
      this.SET_FLAGS8(this.B(), var2, var1);
      this.B(var1);
   }

   private void setline_di() {
      int var1 = this.DIRBYTE();
      if(this.konami_cpu_setlines_callback != null) {
         this.konami_cpu_setlines_callback.write(0, var1);
      }

   }

   private void setline_ex() {
      int var1 = this.EXTBYTE();
      if(this.konami_cpu_setlines_callback != null) {
         this.konami_cpu_setlines_callback.write(0, var1);
      }

   }

   private void setline_im() {
      int var1 = this.IMMBYTE();
      if(this.konami_cpu_setlines_callback != null) {
         this.konami_cpu_setlines_callback.write(0, var1);
      }

   }

   private void setline_ix() {
      int var1 = this.RM(this.EA);
      if(this.konami_cpu_setlines_callback != null) {
         this.konami_cpu_setlines_callback.write(0, var1);
      }

   }

   private void sex() {
      byte var1 = (byte)this.B();
      this.D = var1;
      this.CLR_NZV();
      this.SET_NZ16(var1);
   }

   private void sta_di() {
      this.CLR_NZV();
      this.SET_NZ8(this.A());
      this.DIRECT();
      this.WM(this.EA, this.A());
   }

   private void sta_ex() {
      this.CLR_NZV();
      this.SET_NZ8(this.A());
      this.EXTENDED();
      this.WM(this.EA, this.A());
   }

   private void sta_ix() {
      this.CLR_NZV();
      this.SET_NZ8(this.A());
      this.WM(this.EA, this.A());
   }

   private void stb_di() {
      this.CLR_NZV();
      this.SET_NZ8(this.B());
      this.DIRECT();
      this.WM(this.EA, this.B());
   }

   private void stb_ex() {
      this.CLR_NZV();
      this.SET_NZ8(this.B());
      this.EXTENDED();
      this.WM(this.EA, this.B());
   }

   private void stb_ix() {
      this.CLR_NZV();
      this.SET_NZ8(this.B());
      this.WM(this.EA, this.B());
   }

   private void std_di() {
      this.CLR_NZV();
      this.SET_NZ16(this.D);
      this.DIRECT();
      this.WM16(this.EA, this.D);
   }

   private void std_ex() {
      this.CLR_NZV();
      this.SET_NZ16(this.D);
      this.EXTENDED();
      this.WM16(this.EA, this.D);
   }

   private void std_ix() {
      this.CLR_NZV();
      this.SET_NZ16(this.D);
      this.WM16(this.EA, this.D);
   }

   private void sts_di() {
      this.CLR_NZV();
      this.SET_NZ16(this.S);
      this.DIRECT();
      this.WM16(this.EA, this.S);
   }

   private void sts_ex() {
      this.CLR_NZV();
      this.SET_NZ16(this.S);
      this.EXTENDED();
      this.WM16(this.EA, this.S);
   }

   private void sts_ix() {
      this.CLR_NZV();
      this.SET_NZ16(this.S);
      this.WM16(this.EA, this.S);
   }

   private void stu_di() {
      this.CLR_NZV();
      this.SET_NZ16(this.U);
      this.DIRECT();
      this.WM16(this.EA, this.U);
   }

   private void stu_ex() {
      this.CLR_NZV();
      this.SET_NZ16(this.U);
      this.EXTENDED();
      this.WM16(this.EA, this.U);
   }

   private void stu_ix() {
      this.CLR_NZV();
      this.SET_NZ16(this.U);
      this.WM16(this.EA, this.U);
   }

   private void stx_di() {
      this.CLR_NZV();
      this.SET_NZ16(this.X);
      this.DIRECT();
      this.WM16(this.EA, this.X);
   }

   private void stx_ex() {
      this.CLR_NZV();
      this.SET_NZ16(this.X);
      this.EXTENDED();
      this.WM16(this.EA, this.X);
   }

   private void stx_ix() {
      this.CLR_NZV();
      this.SET_NZ16(this.X);
      this.WM16(this.EA, this.X);
   }

   private void sty_di() {
      this.CLR_NZV();
      this.SET_NZ16(this.Y);
      this.DIRECT();
      this.WM16(this.EA, this.Y);
   }

   private void sty_ex() {
      this.CLR_NZV();
      this.SET_NZ16(this.Y);
      this.EXTENDED();
      this.WM16(this.EA, this.Y);
   }

   private void sty_ix() {
      this.CLR_NZV();
      this.SET_NZ16(this.Y);
      this.WM16(this.EA, this.Y);
   }

   private void suba_di() {
      int var1 = this.DIRBYTE();
      int var2 = this.A() - var1;
      this.CLR_NZVC();
      this.SET_FLAGS8(this.A(), var1, var2);
      this.A(var2);
   }

   private void suba_ex() {
      int var2 = this.EXTBYTE();
      int var1 = this.A() - var2;
      this.CLR_NZVC();
      this.SET_FLAGS8(this.A(), var2, var1);
      this.A(var1);
   }

   private void suba_im() {
      int var1 = this.IMMBYTE();
      int var2 = this.A() - var1;
      this.CLR_NZVC();
      this.SET_FLAGS8(this.A(), var1, var2);
      this.A(var2);
   }

   private void suba_ix() {
      int var1 = this.RM(this.EA);
      int var2 = this.A() - var1;
      this.CLR_NZVC();
      this.SET_FLAGS8(this.A(), var1, var2);
      this.A(var2);
   }

   private void subb_di() {
      int var2 = this.DIRBYTE();
      int var1 = this.B() - var2;
      this.CLR_NZVC();
      this.SET_FLAGS8(this.B(), var2, var1);
      this.B(var1);
   }

   private void subb_ex() {
      int var1 = this.EXTBYTE();
      int var2 = this.B() - var1;
      this.CLR_NZVC();
      this.SET_FLAGS8(this.B(), var1, var2);
      this.B(var2);
   }

   private void subb_im() {
      int var2 = this.IMMBYTE();
      int var1 = this.B() - var2;
      this.CLR_NZVC();
      this.SET_FLAGS8(this.B(), var2, var1);
      this.B(var1);
   }

   private void subb_ix() {
      int var2 = this.RM(this.EA);
      int var1 = this.B() - var2;
      this.CLR_NZVC();
      this.SET_FLAGS8(this.B(), var2, var1);
      this.B(var1);
   }

   private void subd_di() {
      int var1 = this.DIRWORD();
      int var2 = this.D;
      int var3 = var2 - var1;
      this.CLR_NZVC();
      this.SET_FLAGS16(var2, var1, var3);
      this.D = '\uffff' & var3;
   }

   private void subd_ex() {
      int var3 = this.EXTWORD();
      int var2 = this.D;
      int var1 = var2 - var3;
      this.CLR_NZVC();
      this.SET_FLAGS16(var2, var3, var1);
      this.D = '\uffff' & var1;
   }

   private void subd_im() {
      int var2 = this.IMMWORD();
      int var3 = this.D;
      int var1 = var3 - var2;
      this.CLR_NZVC();
      this.SET_FLAGS16(var3, var2, var1);
      this.D = '\uffff' & var1;
   }

   private void subd_ix() {
      int var1 = this.RM16(this.EA);
      int var2 = this.D;
      int var3 = var2 - var1;
      this.CLR_NZVC();
      this.SET_FLAGS16(var2, var1, var3);
      this.D = var3;
   }

   private void tfr() {
      int var1 = this.IMMBYTE();
      this.SETREG(this.GETREG(var1 & 15), var1 >> 4 & 7);
   }

   private void tst_di() {
      int var1 = this.DIRBYTE();
      this.CLR_NZV();
      this.SET_NZ8(var1);
   }

   private void tst_ex() {
      int var1 = this.EXTBYTE();
      this.CLR_NZV();
      this.SET_NZ8(var1);
   }

   private void tst_ix() {
      int var1 = this.RM(this.EA);
      this.CLR_NZV();
      this.SET_NZ8(var1);
   }

   private void tsta() {
      this.CLR_NZV();
      this.SET_NZ8(this.A());
   }

   private void tstb() {
      this.CLR_NZV();
      this.SET_NZ8(this.B());
   }

   private void tstd() {
      this.CLR_NZV();
      this.SET_NZ16(this.D);
   }

   private void tstw_di() {
      this.CLR_NZV();
      this.SET_NZ16(this.DIRWORD());
   }

   private void tstw_ex() {
      this.CLR_NZV();
      this.SET_NZ16(this.EXTWORD());
   }

   private void tstw_ix() {
      this.CLR_NZV();
      this.SET_NZ16(this.RM16(this.EA));
   }

   int A() {
      return this.D >> 8;
   }

   void A(int var1) {
      this.D = this.D & 255 | (var1 & 255) << 8;
   }

   int B() {
      return this.D & 255;
   }

   void B(int var1) {
      this.D = this.D & '\uff00' | var1 & 255;
   }

   void BRANCH(int var1) {
      boolean var2;
      if(var1 != 0) {
         var2 = true;
      } else {
         var2 = false;
      }

      this.BRANCH(var2);
   }

   void BRANCH(boolean var1) {
      int var2 = this.IMMBYTE();
      if(var1) {
         this.PC += (byte)var2;
      }

   }

   void CLC() {
      this.CC &= -2;
   }

   void CLH() {
      this.CC &= -33;
   }

   void CLN() {
      this.CC &= -9;
   }

   void CLR_HNZC() {
      this.CC &= -46;
   }

   void CLR_HNZVC() {
      this.CC &= -48;
   }

   void CLR_NZC() {
      this.CC &= -14;
   }

   void CLR_NZV() {
      this.CC &= -15;
   }

   void CLR_NZVC() {
      this.CC &= -16;
   }

   void CLR_Z() {
      this.CC &= -5;
   }

   void CLR_ZC() {
      this.CC &= -6;
   }

   void CLV() {
      this.CC &= -3;
   }

   void CLZ() {
      this.CC &= -5;
   }

   int DIRBYTE() {
      this.DIRECT();
      return this.RM(this.EA);
   }

   void DIRECT() {
      this.EA = this.DP;
      this.EA = this.IMMBYTE();
   }

   int DIRWORD() {
      this.DIRECT();
      return this.RM16(this.EA);
   }

   int EXTBYTE() {
      this.EXTENDED();
      return this.RM(this.EA);
   }

   void EXTENDED() {
      this.EA = this.IMMWORD();
   }

   int EXTWORD() {
      this.EXTENDED();
      return this.RM16(this.EA);
   }

   int GETREG(int var1) {
      switch(var1) {
      case 0:
         var1 = this.A();
         break;
      case 1:
         var1 = this.B();
         break;
      case 2:
         var1 = this.X;
         break;
      case 3:
         var1 = this.Y;
         break;
      case 4:
         var1 = this.S;
         break;
      case 5:
         var1 = this.U;
         break;
      default:
         System.out.println("Unknown TFR/EXG idx at PC:0x" + Integer.toHexString(this.PC));
         var1 = 255;
      }

      return var1;
   }

   void IMM16() {
      this.EA = this.PC;
      this.PC += 2;
   }

   void IMM8() {
      this.EA = this.PC++;
   }

   int IMMBYTE() {
      int var1 = this.PC;
      this.PC = var1 + 1;
      return this.ROP_ARG(var1);
   }

   int IMMWORD() {
      int var1 = this.PC;
      this.PC = var1 + 1;
      int var2 = this.ROP_ARG(var1);
      var1 = this.PC;
      this.PC = var1 + 1;
      return var2 << 8 | this.ROP_ARG(var1);
   }

   void LBRANCH(int var1) {
      boolean var2;
      if(var1 != 0) {
         var2 = true;
      } else {
         var2 = false;
      }

      this.LBRANCH(var2);
   }

   void LBRANCH(boolean var1) {
      int var2 = this.IMMWORD();
      if(var1) {
         --this.konami_ICount;
         this.PC += (short)var2;
      }

   }

   int NXORV() {
      return this.CC & 8 ^ (this.CC & 2) << 2;
   }

   void PSHUBYTE(int var1) {
      --this.U;
      this.bus.write8(this.U, var1);
   }

   void PSHUWORD(int var1) {
      this.U -= 2;
      this.WM16(this.U, var1);
   }

   int PULLBYTE() {
      CpuBoard var2 = this.bus;
      int var1 = this.S;
      this.S = var1 + 1;
      return var2.read8(var1);
   }

   int PULLWORD() {
      CpuBoard var3 = this.bus;
      int var1 = this.S;
      this.S = var1 + 1;
      int var2 = var3.read8(var1);
      var3 = this.bus;
      var1 = this.S;
      this.S = var1 + 1;
      return var2 << 8 | var3.read8(var1);
   }

   int PULUBYTE() {
      CpuBoard var2 = this.bus;
      int var1 = this.U;
      this.U = var1 + 1;
      return var2.read8(var1);
   }

   int PULUWORD() {
      CpuBoard var3 = this.bus;
      int var1 = this.U;
      this.U = var1 + 1;
      var1 = var3.read8(var1);
      var3 = this.bus;
      int var2 = this.U;
      this.U = var2 + 1;
      return var1 << 8 | var3.read8(var2);
   }

   void PUSHBYTE(int var1) {
      --this.S;
      this.bus.write8(this.S, var1);
   }

   void PUSHWORD(int var1) {
      this.S -= 2;
      this.WM16(this.S, var1);
   }

   int RM(int var1) {
      return this.bus.read8(var1);
   }

   int RM16(int var1) {
      int var2 = this.bus.read8(var1);
      return this.bus.read8(var1 + 1 & '\uffff') | var2 << 8;
   }

   int ROP(int var1) {
      return this.bus.read8opc(var1);
   }

   int ROP_ARG(int var1) {
      return this.bus.read8arg(var1);
   }

   void SEC() {
      this.CC |= 1;
   }

   void SEH() {
      this.CC |= 32;
   }

   void SEN() {
      this.CC |= 8;
   }

   void SETREG(int var1, int var2) {
      switch(var2) {
      case 0:
         this.A(var1);
         break;
      case 1:
         this.B(var1);
         break;
      case 2:
         this.X = var1;
         break;
      case 3:
         this.Y = var1;
         break;
      case 4:
         this.S = var1;
         break;
      case 5:
         this.U = var1;
         break;
      default:
         System.out.println("Unknown TFR/EXG idx at PC:0x" + Integer.toHexString(this.PC));
      }

   }

   void SET_C16(int var1) {
      this.CC |= (65536 & var1) >> 16;
   }

   void SET_C8(int var1) {
      this.CC |= (var1 & 256) >> 8;
   }

   void SET_FLAGS16(int var1, int var2, int var3) {
      this.SET_N16(var3);
      this.SET_Z16(var3);
      this.SET_V16(var1, var2, var3);
      this.SET_C16(var3);
   }

   void SET_FLAGS8(int var1, int var2, int var3) {
      this.SET_N8(var3);
      this.SET_Z8(var3);
      this.SET_V8(var1, var2, var3);
      this.SET_C8(var3);
   }

   void SET_FLAGS8D(int var1) {
      this.CC |= flags8d[var1 & 255];
   }

   void SET_FLAGS8I(int var1) {
      this.CC |= flags8i[var1 & 255];
   }

   void SET_H(int var1, int var2, int var3) {
      this.CC |= ((var1 ^ var2 ^ var3) & 16) << 1;
   }

   void SET_N16(int var1) {
      this.CC |= ('耀' & var1) >> 12;
   }

   void SET_N8(int var1) {
      this.CC |= (var1 & 128) >> 4;
   }

   void SET_NZ16(int var1) {
      this.SET_N16(var1);
      this.SET_Z(var1);
   }

   void SET_NZ8(int var1) {
      this.SET_N8(var1);
      this.SET_Z(var1);
   }

   void SET_V16(int var1, int var2, int var3) {
      this.CC |= ((var1 ^ var2 ^ var3 ^ var3 >> 1) & '耀') >> 14;
   }

   void SET_V8(int var1, int var2, int var3) {
      this.CC |= ((var1 ^ var2 ^ var3 ^ var3 >> 1) & 128) >> 6;
   }

   void SET_Z(int var1) {
      if(var1 == 0) {
         this.SEZ();
      }

   }

   void SET_Z16(int var1) {
      this.SET_Z(var1);
   }

   void SET_Z8(int var1) {
      this.SET_Z(var1);
   }

   void SEV() {
      this.CC |= 2;
   }

   void SEZ() {
      this.CC |= 4;
   }

   byte SIGNED(byte var1) {
      return var1;
   }

   void WM(int var1, int var2) {
      this.bus.write8(var1, var2 & 255);
   }

   void WM16(int var1, int var2) {
      this.bus.write8(var1, var2 >> 8);
      this.bus.write8(var1 + 1 & '\uffff', var2 & 255);
   }

   public void exec(int var1) {
      this.konami_ICount = var1 - this.extra_cycles;
      this.extra_cycles = 0;
      if((this.int_state & 24) != 0) {
         this.konami_ICount = 0;
      } else {
         do {
            this.PPC = this.PC;
            this.ireg = this.ROP(this.PC);
            ++this.PC;
            this.opcodeMain(this.ireg);
            this.konami_ICount -= cycles1[this.ireg];
         } while(this.konami_ICount > 0);

         this.konami_ICount -= this.extra_cycles;
         this.extra_cycles = 0;
      }

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

   public String getTag() {
      return null;
   }

   void illegal(int var1) {
      throw new RuntimeException("Illegal opcode 0x" + Integer.toHexString(var1));
   }

   public boolean init(CpuBoard var1, int var2) {
      this.bus = var1;
      return true;
   }

   public void interrupt(int var1, boolean var2) {
   }

   void konami_set_irq_line(int var1, int var2) {
      this.irq_state[var1] = var2;
      if(var2 != 0) {
         this.CHECK_IRQ_LINES();
      }

   }

   void konami_set_nmi_line(int var1) {
      if(this.nmi_state != var1) {
         this.nmi_state = var1;
         if(var1 != 0 && (this.int_state & 32) != 0) {
            this.int_state &= -17;
            if((this.int_state & 8) != 0) {
               this.int_state &= -9;
               this.extra_cycles += 7;
            } else {
               this.CC |= 128;
               this.PUSHWORD(this.PC);
               this.PUSHWORD(this.U);
               this.PUSHWORD(this.Y);
               this.PUSHWORD(this.X);
               this.PUSHBYTE(this.DP);
               this.PUSHBYTE(this.B());
               this.PUSHBYTE(this.A());
               this.PUSHBYTE(this.CC);
               this.extra_cycles += 19;
            }

            this.CC |= 80;
            this.PC = this.RM16('￼');
         }
      }

   }

   public void reset() {
      this.int_state = 0;
      this.nmi_state = 0;
      this.irq_state[0] = 0;
      this.irq_state[0] = 0;
      this.DP = 0;
      this.CC |= 16;
      this.CC |= 64;
      this.PC = this.RM16('\ufffe');
   }

   public void setDebug(int var1) {
   }

   public void setProperty(int var1, int var2) {
   }

   public void setTag(String var1) {
   }
}
