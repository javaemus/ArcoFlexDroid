package jef.sound.chip.fm;

import jef.sound.chip.ym2203.YM2203;

public class FMSlot implements FMConstants {
   public int[] AR;
   public int AR_pointer;
   public long Cnt;
   public int[] DR;
   public int DR_pointer;
   public int[] DT;
   public int DT2;
   public long Incr;
   public int KSR;
   public int[] RR;
   public int RR_pointer;
   public int SEG;
   public int SL;
   public int[] SR;
   public int SR_pointer;
   public int TL;
   public int TLL;
   public int amon;
   public long ams;
   public int eg_next;
   public int evc;
   public int eve;
   public int evs;
   public int evsa;
   public int evsd;
   public int evsr;
   public int evss;
   public int ksr;
   public long mul;

   public void CALC_FCSLOT(long var1, int var3) {
      this.Incr = this.mul * var1 + (long)this.DT[var3];
      var3 >>= this.KSR;
      if(this.ksr != var3) {
         this.ksr = var3;
         this.evsa = this.AR[this.AR_pointer + var3];
         this.evsd = this.DR[this.DR_pointer + var3];
         this.evss = this.SR[this.SR_pointer + var3];
         this.evsr = this.RR[this.RR_pointer + var3];
      }

      this.TLL = this.TL;
   }

   public int FM_CALC_EG() {
      int var1 = this.evc + this.evs;
      this.evc = var1;
      if(var1 >= this.eve) {
         this.egNext(this.eg_next);
      }

      int var2 = this.TLL + YM2203.ENV_CURVE[this.evc >> 16];
      var1 = var2;
      if(this.ams != 0L) {
         var1 = (int)((long)var2 + this.ams * YM2203.lfo_amd / 65536L);
      }

      return var1;
   }

   public void egNext(int var1) {
      switch(var1) {
      case 0:
         this.evc = 536805376;
         this.eve = 536805377;
         this.evs = 0;
         break;
      case 1:
         this.evc = 536805376;
         this.eve = 536805377;
         this.evs = 0;
         break;
      case 2:
         this.eg_next = 1;
         this.evc = this.SL;
         this.eve = 536805376;
         this.evs = this.evss;
         break;
      case 3:
         this.eg_next = 2;
         this.evc = 268435456;
         this.eve = this.SL;
         this.evs = this.evsd;
         break;
      case 4:
         if((this.SEG & 2) != 0) {
            this.eg_next = 5;
            this.evc = this.SL + 268435456;
            this.eve = 805306368;
            this.evs = this.evss;
         } else {
            this.evc = 268435456;
         }

         if((this.SEG & 1) != 0) {
            this.evs = 0;
         }
         break;
      case 5:
         if((this.SEG & 2) != 0) {
            this.eg_next = 4;
            this.evc = 268435456;
            this.eve = 536805376;
            this.evs = this.evsd;
         } else {
            this.evc = this.SL + 268435456;
         }

         if((this.SEG & 1) != 0) {
            this.evs = 0;
         }
         break;
      case 6:
         if((this.SEG & 4) != 0) {
            this.eg_next = 5;
            this.evc = this.SL + 268435456;
            this.eve = 805306368;
            this.evs = this.evss;
         } else {
            this.eg_next = 4;
            this.evc = 268435456;
            this.eve = 536805376;
            this.evs = this.evsd;
         }
      }

   }
}
