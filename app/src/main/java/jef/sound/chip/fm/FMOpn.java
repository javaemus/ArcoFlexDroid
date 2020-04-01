package jef.sound.chip.fm;

public class FMOpn implements FMConstants {
   private static final int[] MUL_TABLE = new int[]{ML(0.5D), ML(1.0D), ML(2.0D), ML(3.0D), ML(4.0D), ML(5.0D), ML(6.0D), ML(7.0D), ML(8.0D), ML(9.0D), ML(10.0D), ML(11.0D), ML(12.0D), ML(13.0D), ML(14.0D), ML(15.0D), ML(0.71D), ML(1.41D), ML(2.82D), ML(4.24D), ML(5.65D), ML(7.07D), ML(8.46D), ML(9.89D), ML(11.3D), ML(12.72D), ML(14.1D), ML(15.55D), ML(16.96D), ML(18.37D), ML(19.78D), ML(21.2D), ML(0.78D), ML(1.57D), ML(3.14D), ML(4.71D), ML(6.28D), ML(7.85D), ML(9.42D), ML(10.99D), ML(12.56D), ML(14.13D), ML(15.7D), ML(17.27D), ML(18.84D), ML(20.41D), ML(21.98D), ML(23.55D), ML(0.87D), ML(1.73D), ML(3.46D), ML(5.19D), ML(6.92D), ML(8.65D), ML(10.38D), ML(12.11D), ML(13.84D), ML(15.57D), ML(17.3D), ML(19.03D), ML(20.76D), ML(22.49D), ML(24.22D), ML(25.95D)};
   private static final int[] OPN_DTTABLE;
   private static final int[] OPN_FKTABLE;
   private static final int[] RATE_0;
   static final int[] SL_TABLE = new int[]{SC(0.0D), SC(1.0D), SC(2.0D), SC(3.0D), SC(4.0D), SC(5.0D), SC(6.0D), SC(7.0D), SC(8.0D), SC(9.0D), SC(10.0D), SC(11.0D), SC(12.0D), SC(13.0D), SC(14.0D), SC(31.0D)};
   public static final int TYPE_YM2203 = 0;
   private static final double[] freq_table = new double[]{3.98D, 5.56D, 6.02D, 6.37D, 6.88D, 9.63D, 48.1D, 72.2D};
   public long[] FN_TABLE = new long[2048];
   public long LFOCnt;
   public long LFOIncr;
   public long[] LFO_FREQ = new long[8];
   public int[] LFO_wave = new int[512];
   public FMChan[] P_CH;
   public FM3Slot SL3 = new FM3Slot();
   public FMState ST;
   private FM fm;
   public int type;

   static {
      int[] var0 = new int[16];
      var0[7] = 1;
      var0[8] = 2;
      var0[9] = 3;
      var0[10] = 3;
      var0[11] = 3;
      var0[12] = 3;
      var0[13] = 3;
      var0[14] = 3;
      var0[15] = 3;
      OPN_FKTABLE = var0;
      var0 = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 2, 2, 2, 2, 2, 3, 3, 3, 4, 4, 4, 5, 5, 6, 6, 7, 8, 8, 8, 8, 1, 1, 1, 1, 2, 2, 2, 2, 2, 3, 3, 3, 4, 4, 4, 5, 5, 6, 6, 7, 8, 8, 9, 10, 11, 12, 13, 14, 16, 16, 16, 16, 2, 2, 2, 2, 2, 3, 3, 3, 4, 4, 4, 5, 5, 6, 6, 7, 8, 8, 9, 10, 11, 12, 13, 14, 16, 17, 19, 20, 22, 22, 22, 22};
      OPN_DTTABLE = var0;
      RATE_0 = new int[32];
   }

   public FMOpn(FM var1, FMIRQHandler var2) {
      this.fm = var1;
      this.ST = new FMState(var2);
   }

   private static final int ML(double var0) {
      return (int)(2.0D * var0);
   }

   private int OPN_CHAN(int var1) {
      return var1 & 3;
   }

   private int OPN_SLOT(int var1) {
      return var1 >> 2 & 3;
   }

   private static final int SC(double var0) {
      return (int)(8388608.0D * var0 + 2.68435456E8D);
   }

   private void set_ar_ksr(FMChan var1, FMSlot var2, int var3, int[] var4) {
      var2.KSR = 3 - (var3 >> 6);
      var3 &= 31;
      if(var3 != 0) {
         var2.AR = var4;
         var2.AR_pointer = var3 << 1;
      } else {
         var2.AR = RATE_0;
         var2.AR_pointer = 0;
      }

      var2.evsa = var2.AR[var2.ksr];
      if(var2.eg_next == 3) {
         var2.evs = var2.evsa;
      }

      var1.SLOT[0].Incr = -1L;
   }

   private void set_det_mul(FMState var1, FMChan var2, FMSlot var3, int var4) {
      var3.mul = (long)MUL_TABLE[var4 & 15];
      var3.DT = var1.DT_TABLE[var4 >> 4 & 7];
      var2.SLOT[0].Incr = -1L;
   }

   private void set_dr(FMSlot var1, int var2, int[] var3) {
      var2 &= 31;
      if(var2 != 0) {
         var1.DR = var3;
         var1.DR_pointer = var2 << 1;
      } else {
         var1.DR = RATE_0;
         var1.DR_pointer = 0;
      }

      var1.evsd = var1.DR[var1.ksr];
      if(var1.eg_next == 2) {
         var1.evs = var1.evsd;
      }

   }

   private void set_sl_rr(FMSlot var1, int var2, int[] var3) {
      var1.SL = SL_TABLE[var2 >> 4];
      var1.RR_pointer = (var2 & 15) << 2 | 2;
      var1.RR = var3;
      var1.evsr = var1.RR[var1.ksr];
      if(var1.eg_next == 0) {
         var1.evs = var1.evsr;
      }

   }

   private void set_sr(FMSlot var1, int var2, int[] var3) {
      var2 &= 31;
      if(var2 != 0) {
         var1.SR = var3;
         var1.SR_pointer = var2 << 1;
      } else {
         var1.SR = RATE_0;
         var1.SR_pointer = 0;
      }

      var1.evss = var1.SR[var1.ksr];
      if(var1.eg_next == 1) {
         var1.evs = var1.evss;
      }

   }

   private void set_tl(FMChan var1, FMSlot var2, int var3, boolean var4) {
      var3 &= 127;
      var2.TL = (var3 | var3 << 7) * 4096 >> 14;
      if(!var4) {
         var2.TLL = var2.TL;
      }

   }

   public void OPNWriteMode(int var1, int var2) {
      switch(var1) {
      case 33:
      case 34:
      case 35:
      default:
         break;
      case 36:
         this.ST.TA = this.ST.TA & 3 | var2 << 2;
         break;
      case 37:
         this.ST.TA = this.ST.TA & 1020 | var2 & 3;
         break;
      case 38:
         this.ST.TB = var2;
         break;
      case 39:
         this.ST.FMSetMode(this.ST.index, var2);
         break;
      case 40:
         int var3 = var2 & 3;
         if(var3 != 3) {
            var1 = var3;
            if((var2 & 4) != 0) {
               var1 = var3;
               if((this.type & 8) != 0) {
                  var1 = var3 + 3;
               }
            }

            FMChan var4 = this.P_CH[var1];
            if(var1 != 2 || (this.ST.mode & 128L) == 0L) {
               if((var2 & 16) != 0) {
                  var4.FM_KEYON(0);
               } else {
                  var4.FM_KEYOFF(0);
               }

               if((var2 & 32) != 0) {
                  var4.FM_KEYON(2);
               } else {
                  var4.FM_KEYOFF(2);
               }

               if((var2 & 64) != 0) {
                  var4.FM_KEYON(1);
               } else {
                  var4.FM_KEYOFF(1);
               }

               if((var2 & 128) != 0) {
                  var4.FM_KEYON(3);
               } else {
                  var4.FM_KEYOFF(3);
               }
            }
         }
      }

   }

   public void OPNWriteReg(int var1, int var2) {
      int var4 = this.OPN_CHAN(var1);
      if(var4 != 3) {
         int var3 = var4;
         if(var1 >= 256) {
            var3 = var4 + 3;
         }

         FMChan var6 = this.P_CH[var3];
         FMSlot var7 = var6.SLOT[this.OPN_SLOT(var1)];
         switch(var1 & 240) {
         case 48:
            this.set_det_mul(this.ST, var6, var7, var2);
            break;
         case 64:
            boolean var5;
            if(var3 == 2 && (this.ST.mode & 128L) != 0L) {
               var5 = true;
            } else {
               var5 = false;
            }

            this.set_tl(var6, var7, var2, var5);
            break;
         case 80:
            this.set_ar_ksr(var6, var7, var2, this.ST.AR_TABLE);
            break;
         case 96:
            this.set_dr(var7, var2, this.ST.DR_TABLE);
            break;
         case 112:
            this.set_sr(var7, var2, this.ST.DR_TABLE);
            break;
         case 128:
            this.set_sl_rr(var7, var2, this.ST.DR_TABLE);
            break;
         case 144:
            var7.SEG = var2 & 15;
            break;
         case 160:
            switch(this.OPN_SLOT(var1)) {
            case 0:
               var2 += (var6.fn_h & 7) << 8;
               var1 = var6.fn_h >> 3;
               var6.kcode = var1 << 2 | OPN_FKTABLE[var2 >> 7];
               var6.fc = (int)(this.FN_TABLE[var2] >> 7 - var1);
               var6.SLOT[0].Incr = -1L;
               return;
            case 1:
               var6.fn_h = var2 & 63;
               return;
            case 2:
               if(var1 < 256) {
                  var2 += (this.SL3.fn_h[var3] & 7) << 8;
                  var1 = this.SL3.fn_h[var3] >> 3;
                  this.SL3.kcode[var3] = var1 << 2 | OPN_FKTABLE[var2 >> 7];
                  this.SL3.fc[var3] = this.FN_TABLE[var2] >> 7 - var1;
                  this.P_CH[2].SLOT[0].Incr = -1L;
               }

               return;
            case 3:
               if(var1 < 256) {
                  this.SL3.fn_h[var3] = var2 & 63;
               }

               return;
            default:
               return;
            }
         case 176:
            switch(this.OPN_SLOT(var1)) {
            case 0:
               var1 = var2 >> 3 & 7;
               var6.ALGO = var2 & 7;
               if(var1 != 0) {
                  var1 = 9 - var1;
               } else {
                  var1 = 0;
               }

               var6.FB = var1;
               this.fm.setup_connection(var6);
               break;
            case 1:
               if((this.type & 4) != 0) {
                  var6.PAN = var2 >> 6 & 3;
                  this.fm.setup_connection(var6);
               }
            }
         }
      }

   }

   public void setPris(int var1, int var2, int var3) {
      FMState var6 = this.ST;
      double var4;
      if(this.ST.rate != 0) {
         var4 = (double)this.ST.clock / (double)this.ST.rate / (double)var1;
      } else {
         var4 = 0.0D;
      }

      var6.freqbase = var4;
      this.ST.TimerBase = 1.0D / ((double)this.ST.clock / (double)var2);
      if(var3 != 0) {
         this.fm.SSG.AY8910_set_clock(this.ST.index, this.ST.clock * 2 / var3);
      }

      this.ST.init_timetables(OPN_DTTABLE, 399128, 5514396);

      for(var1 = 0; var1 < 2048; ++var1) {
         this.FN_TABLE[var1] = (long)((double)var1 * this.ST.freqbase * 8.0D * 128.0D / 2.0D);
      }

   }
}
