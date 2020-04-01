package jef.sound.chip.fm;

import java.lang.reflect.Array;

public class FMState implements FMConstants {
   public static final int FM_TIMER_INTERVAL = 0;
   public int[] AR_TABLE;
   public int[] DR_TABLE;
   public int[][] DT_TABLE;
   public FMIRQHandler IRQ_Handler;
   public int TA;
   public int TAC;
   public int TB;
   public int TBC;
   public double TimerBase;
   public FMTimerHandler Timer_Handler;
   public int address;
   public int clock;
   public double freqbase;
   public int index;
   public int irq;
   public int irqmask;
   public long mode;
   public int rate;
   public int status;
   public int timermodel;

   public FMState(FMIRQHandler var1) {
      this.DT_TABLE = (int[][])Array.newInstance(Integer.TYPE, new int[]{8, 32});
      this.AR_TABLE = new int[94];
      this.DR_TABLE = new int[94];
      this.IRQ_Handler = var1;
   }

   public void FMSetMode(int var1, int var2) {
      this.mode = (long)var2;
      if((var2 & 32) != 0) {
         this.FM_STATUS_RESET(2);
      }

      if((var2 & 16) != 0) {
         this.FM_STATUS_RESET(1);
      }

      if((var2 & 2) != 0) {
         if(this.TBC == 0) {
            this.TBC = 256 - this.TB << 4;
            if(this.Timer_Handler != null) {
               this.Timer_Handler.n = var1;
               this.Timer_Handler.c = 1;
               this.Timer_Handler.cnt = (double)this.TBC;
               this.Timer_Handler.stepTime = this.TimerBase;
            }
         }
      } else if(this.timermodel == 0 && this.TBC != 0) {
         this.TBC = 0;
         if(this.Timer_Handler != null) {
            this.Timer_Handler.n = var1;
            this.Timer_Handler.c = 1;
            this.Timer_Handler.cnt = 0.0D;
            this.Timer_Handler.stepTime = this.TimerBase;
         }
      }

      if((var2 & 1) != 0) {
         if(this.TAC == 0) {
            this.TAC = 1024 - this.TA;
            if(this.Timer_Handler != null) {
               this.Timer_Handler.n = var1;
               this.Timer_Handler.c = 0;
               this.Timer_Handler.cnt = (double)this.TAC;
               this.Timer_Handler.stepTime = this.TimerBase;
            }
         }
      } else if(this.timermodel == 0 && this.TAC != 0) {
         this.TAC = 0;
         if(this.Timer_Handler != null) {
            this.Timer_Handler.n = var1;
            this.Timer_Handler.c = 0;
            this.Timer_Handler.cnt = 0.0D;
            this.Timer_Handler.stepTime = this.TimerBase;
         }
      }

   }

   public void FM_IRQMASK_SET(int var1) {
      this.irqmask = var1;
      this.FM_STATUS_SET(0);
      this.FM_STATUS_RESET(0);
   }

   public void FM_STATUS_RESET(int var1) {
      this.status &= ~var1;
      if(this.irq != 0 && (this.status & this.irqmask) == 0) {
         this.irq = 0;
         if(this.IRQ_Handler != null) {
            this.IRQ_Handler.irq(this.index, 0);
         }
      }

   }

   public void FM_STATUS_SET(int var1) {
      this.status |= var1;
      if(this.irq == 0 && (this.status & this.irqmask) != 0) {
         this.irq = 1;
         if(this.IRQ_Handler != null) {
            this.IRQ_Handler.irq(this.index, 1);
         }
      }

   }

   public void INTERNAL_TIMER_A(FMChan var1) {
      if(this.TAC != 0 && this.Timer_Handler == null) {
         int var2 = (int)((double)this.TAC - this.freqbase * 4096.0D);
         this.TAC = var2;
         if(var2 <= 0) {
            this.TimerAOver();
            if((this.mode & 128L) != 0L) {
               var1.CSMKeyControll();
            }
         }
      }

   }

   public void INTERNAL_TIMER_B(int var1) {
      if(this.TBC != 0 && this.Timer_Handler == null) {
         var1 = (int)((double)this.TBC - this.freqbase * 4096.0D * (double)var1);
         this.TBC = var1;
         if(var1 <= 0) {
            this.TimerBOver();
         }
      }

   }

   public void TimerAOver() {
      if((this.mode & 4L) != 0L) {
         this.FM_STATUS_SET(1);
      }

      if(this.timermodel == 0) {
         this.TAC = 1024 - this.TA;
      } else {
         this.TAC = 0;
      }

   }

   public void TimerBOver() {
      if((this.mode & 8L) != 0L) {
         this.FM_STATUS_SET(2);
      }

      if(this.timermodel == 0) {
         this.TBC = 256 - this.TB << 4;
      } else {
         this.TBC = 0;
      }

   }

   public void init_timetables(int[] var1, int var2, int var3) {
      double var4;
      int var8;
      for(var8 = 0; var8 <= 3; ++var8) {
         for(int var9 = 0; var9 <= 31; ++var9) {
            var4 = (double)var1[var8 * 32 + var9] * this.freqbase * 8.0D;
            this.DT_TABLE[var8][var9] = (int)var4;
            this.DT_TABLE[var8 + 4][var9] = (int)(-var4);
         }
      }

      for(var8 = 0; var8 < 4; ++var8) {
         var1 = this.AR_TABLE;
         this.DR_TABLE[var8] = 0;
         var1[var8] = 0;
      }

      for(var8 = 4; var8 < 64; ++var8) {
         double var6 = this.freqbase;
         var4 = var6;
         if(var8 < 60) {
            var4 = var6 * (1.0D + (double)(var8 & 3) * 0.25D);
         }

         var4 = var4 * (double)(1 << (var8 >> 2) - 1) * 2.68435456E8D;
         this.AR_TABLE[var8] = (int)(var4 / (double)var2);
         this.DR_TABLE[var8] = (int)(var4 / (double)var3);
      }

      this.AR_TABLE[62] = 268435455;
      this.AR_TABLE[63] = 268435455;

      for(var2 = 64; var2 < 94; ++var2) {
         this.AR_TABLE[var2] = this.AR_TABLE[63];
         this.DR_TABLE[var2] = this.DR_TABLE[63];
      }

   }
}
