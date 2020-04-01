package jef.sound.chip.fm;

import jef.util.INT32;

public class FMChan implements FMConstants {
   public int ALGO;
   public int FB;
   public int PAN;
   public FMSlot[] SLOT = new FMSlot[4];
   public long ams;
   public INT32 connect1;
   public INT32 connect2;
   public INT32 connect3;
   public INT32 connect4;
   public int fc;
   FM fm;
   public int fn_h;
   public int kcode;
   public int[] op1_out = new int[2];
   public int pms;

   public FMChan(FM var1) {
      for(int var2 = 0; var2 < 4; ++var2) {
         this.SLOT[var2] = new FMSlot();
      }

      this.fm = var1;
   }

   private boolean FM_KEY_IS(FMSlot var1) {
      boolean var2;
      if(var1.eg_next != 0) {
         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
   }

   private int OP_OUT(int var1, int var2) {
      return this.fm.SIN_TABLE(var1 / 8192 & 2047, var2);
   }

   public void CALC_FCOUNT() {
      if(this.SLOT[0].Incr == -1L) {
         int var1 = this.fc;
         int var2 = this.kcode;
         this.SLOT[0].CALC_FCSLOT((long)var1, var2);
         this.SLOT[2].CALC_FCSLOT((long)var1, var2);
         this.SLOT[1].CALC_FCSLOT((long)var1, var2);
         this.SLOT[3].CALC_FCSLOT((long)var1, var2);
      }

   }

   public void CSMKeyControll() {
      this.FM_KEYOFF(0);
      this.FM_KEYOFF(2);
      this.FM_KEYOFF(1);
      this.FM_KEYOFF(3);
      this.SLOT[0].TLL = this.SLOT[0].TL;
      this.SLOT[2].TLL = this.SLOT[2].TL;
      this.SLOT[1].TLL = this.SLOT[1].TL;
      this.SLOT[3].TLL = this.SLOT[3].TL;
      this.FM_KEYON(0);
      this.FM_KEYON(2);
      this.FM_KEYON(1);
      this.FM_KEYON(3);
   }

   public void FM_CALC_CH() {
      INT32 var8 = this.fm.pg_in1;
      FMSlot var7 = this.SLOT[0];
      long var5 = var7.Cnt + this.SLOT[0].Incr;
      var7.Cnt = var5;
      var8.value = (int)var5;
      var8 = this.fm.pg_in2;
      var7 = this.SLOT[2];
      var5 = var7.Cnt + this.SLOT[2].Incr;
      var7.Cnt = var5;
      var8.value = (int)var5;
      INT32 var9 = this.fm.pg_in3;
      FMSlot var10 = this.SLOT[1];
      var5 = var10.Cnt + this.SLOT[1].Incr;
      var10.Cnt = var5;
      var9.value = (int)var5;
      var8 = this.fm.pg_in4;
      var7 = this.SLOT[3];
      var5 = var7.Cnt + this.SLOT[3].Incr;
      var7.Cnt = var5;
      var8.value = (int)var5;
      int var2 = this.SLOT[0].FM_CALC_EG();
      int var4 = this.SLOT[2].FM_CALC_EG();
      int var3 = this.SLOT[2].FM_CALC_EG();
      int var1 = this.SLOT[3].FM_CALC_EG();
      if(var2 < 2901) {
         if(this.FB != 0) {
            var9 = this.fm.pg_in1;
            var9.value += this.op1_out[0] + this.op1_out[1] >> this.FB;
            this.op1_out[1] = this.op1_out[0];
         }

         this.op1_out[0] = this.OP_OUT(this.fm.pg_in1.value, var2);
         if(this.connect1 == null) {
            var9 = this.fm.pg_in2;
            var9.value += this.op1_out[0];
            var9 = this.fm.pg_in3;
            var9.value += this.op1_out[0];
            var9 = this.fm.pg_in4;
            var9.value += this.op1_out[0];
         } else {
            var9 = this.connect1;
            var9.value += this.op1_out[0];
         }
      }

      if(var4 < 2901) {
         var9 = this.connect2;
         var9.value += this.OP_OUT(this.fm.pg_in2.value, var4);
      }

      if(var3 < 2901) {
         var9 = this.connect3;
         var9.value += this.OP_OUT(this.fm.pg_in3.value, var3);
      }

      if(var1 < 2901) {
         var9 = this.connect4;
         var9.value += this.OP_OUT(this.fm.pg_in4.value, var1);
      }

   }

   public void FM_KEYOFF(int var1) {
      FMSlot var2 = this.SLOT[var1];
      if(this.FM_KEY_IS(var2)) {
         if(var2.evc < 268435456) {
            var2.evc = (FM.ENV_CURVE[var2.evc >> 16] << 16) + 268435456;
         }

         var2.eg_next = 0;
         var2.eve = 536805376;
         var2.evs = var2.evsr;
      }

   }

   public void FM_KEYON(int var1) {
      FMSlot var2 = this.SLOT[var1];
      if(!this.FM_KEY_IS(var2)) {
         var2.Cnt = 0L;
         if((var2.SEG & 8) != 0) {
            var2.eg_next = 6;
         } else {
            var2.eg_next = 3;
         }

         var2.evs = var2.evsa;
         var2.evc = 0;
         var2.eve = 268435456;
      }

   }
}
