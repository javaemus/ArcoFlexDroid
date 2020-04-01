package jef.sound.chip.ym2203;

import jef.map.ReadHandler;
import jef.map.WriteHandler;
import jef.sound.chip.AY8910;
import jef.sound.chip.fm.FM;
import jef.sound.chip.fm.FMChan;
import jef.sound.chip.fm.FMConstants;
import jef.sound.chip.fm.FMIRQHandler;
import jef.sound.chip.fm.FMOpn;
import jef.sound.chip.fm.FMState;
import jef.sound.chip.fm.FMTimerHandler;
import jef.util.INT32;

public class YM2203 extends FM implements FMConstants {
   public static final boolean DEBUG = false;
   private static FMState State;
   private static FMChan[] cch = new FMChan[8];
   public static long lfo_amd;
   private YM2203_f[] FM2203;
   private int YM2203NumChips;
   private int baseClock;
   private FMIRQHandler irqHandler;
   private FMTimerHandler timeHandler;
   private YM2203 ym2203;

   public YM2203(int var1, int var2, FMTimerHandler var3, FMIRQHandler var4) {
      this.YM2203NumChips = var1;
      this.baseClock = var2;
      this.timeHandler = var3;
      this.irqHandler = var4;
      this.SSG = new AY8910(var1, var2);

      for(var1 = 0; var1 < 4; ++var1) {
         this.out_ch[var1] = new INT32();
      }

      this.ym2203 = this;
   }

   private void YM2203Init(int var1, int var2, int var3, FMTimerHandler var4, FMIRQHandler var5) {
      this.YM2203NumChips = var1;
      this.FMInitTable();
      this.FM2203 = new YM2203_f[this.YM2203NumChips];

      for(var1 = 0; var1 < this.YM2203NumChips; ++var1) {
         this.FM2203[var1] = new YM2203_f();
         this.FM2203[var1].OPN.ST.index = var1;
         this.FM2203[var1].OPN.type = 0;
         this.FM2203[var1].OPN.P_CH = this.FM2203[var1].CH;
         this.FM2203[var1].OPN.ST.clock = var2;
         this.FM2203[var1].OPN.ST.rate = var3;
         this.FM2203[var1].OPN.ST.irq = 0;
         this.FM2203[var1].OPN.ST.status = 0;
         this.FM2203[var1].OPN.ST.timermodel = 0;
         this.FM2203[var1].OPN.ST.Timer_Handler = var4;
         this.FM2203[var1].OPN.ST.IRQ_Handler = var5;
         this.SSG.AY8910_init("AY-3-8910", var1, this.baseClock, 50, super.getSampFreq(), (ReadHandler)null, (ReadHandler)null, (WriteHandler)null, (WriteHandler)null);
         this.SSG.build_mixer_table(var1);
         this.YM2203ResetChip(var1);
      }

   }

   private void YM2203ResetChip(int var1) {
      FMOpn var2 = this.FM2203[var1].OPN;
      var2.setPris(72, 72, 4);
      this.SSG.AY8910_reset(var2.ST.index);
      var2.ST.FM_IRQMASK_SET(3);
      var2.OPNWriteMode(39, 48);
      this.reset_channel(var2.ST, this.FM2203[var1].CH, 3);

      for(var1 = 182; var1 >= 180; --var1) {
         var2.OPNWriteReg(var1, 192);
      }

      for(var1 = 178; var1 >= 48; --var1) {
         var2.OPNWriteReg(var1, 0);
      }

      for(var1 = 38; var1 >= 32; --var1) {
         var2.OPNWriteReg(var1, 0);
      }

   }

   private void YM2203UpdateOne(int var1, int var2) {
      YM2203_f var4 = this.FM2203[var1];
      FMOpn var5 = this.FM2203[var1].OPN;
      State = var4.OPN.ST;
      cch[0] = var4.CH[0];
      cch[1] = var4.CH[1];
      cch[2] = var4.CH[2];
      cch[0].CALC_FCOUNT();
      cch[1].CALC_FCOUNT();
      if((State.mode & 192L) != 0L) {
         if(cch[2].SLOT[0].Incr == -1L) {
            cch[2].SLOT[0].CALC_FCSLOT(var5.SL3.fc[1], var5.SL3.kcode[1]);
            cch[2].SLOT[2].CALC_FCSLOT(var5.SL3.fc[2], var5.SL3.kcode[2]);
            cch[2].SLOT[1].CALC_FCSLOT(var5.SL3.fc[0], var5.SL3.kcode[0]);
            cch[2].SLOT[3].CALC_FCSLOT((long)cch[2].fc, cch[2].kcode);
         }
      } else {
         cch[2].CALC_FCOUNT();
      }

      for(var1 = 0; var1 < var2; ++var1) {
         this.out_ch[3].value = 0;

         for(int var3 = 0; var3 <= 2; ++var3) {
            cch[var3].FM_CALC_CH();
         }

         this.out_ch[3].limit(-268435456, 268435455);
         this.writeLinBuffer(var1, this.readLinBuffer(var1) + (this.out_ch[3].value >> 13) / this.YM2203NumChips);
         State.INTERNAL_TIMER_A(cch[2]);
      }

      State.INTERNAL_TIMER_B(var2);
   }

   private void YM2203UpdateReq(int var1) {
   }

   public int YM2203Read(int var1, int var2) {
      YM2203_f var5 = this.FM2203[var1];
      int var4 = var5.OPN.ST.address;
      byte var3 = 0;
      if((var2 & 1) == 0) {
         var2 = var5.OPN.ST.status;
      } else {
         var2 = var3;
         if(var4 < 16) {
            var2 = this.SSG.AY8910Read(var1);
         }
      }

      return var2;
   }

   public int YM2203Write(int var1, int var2, int var3) {
      FMOpn var5 = this.FM2203[var1].OPN;
      if((var2 & 1) == 0) {
         var5.ST.address = var3 & 255;
         if(var3 < 16) {
            this.SSG.AY8910Write(var1, 0, var3);
         }

         switch(var5.ST.address) {
         case 45:
            var5.setPris(72, 72, 4);
            break;
         case 46:
            var5.setPris(36, 36, 2);
            break;
         case 47:
            var5.setPris(24, 24, 1);
         }
      } else {
         int var4 = var5.ST.address;
         switch(var4 & 240) {
         case 0:
            this.SSG.AY8910Write(var1, var2, var3);
            break;
         case 32:
            this.YM2203UpdateReq(var1);
            var5.OPNWriteMode(var4, var3);
            break;
         default:
            this.YM2203UpdateReq(var1);
            var5.OPNWriteReg(var4, var3);
         }
      }

      return var5.ST.irq;
   }

   public void init(boolean var1, int var2, int var3) {
      super.init(var1, var2, var3);
      this.SSG.init(var1, var2, var3);
      this.YM2203Init(this.YM2203NumChips, this.baseClock, var2, this.timeHandler, this.irqHandler);
   }

   public void update(float var1) {
      super.update(var1);
      this.SSG.update(var1);
   }

   public void writeBuffer() {
      this.SSG.writeBuffer();
      this.clearBuffer();
      int var2 = super.getBufferLength();

      for(int var1 = 0; var1 < this.YM2203NumChips; ++var1) {
         this.YM2203UpdateOne(var1, var2);
      }

   }

   public WriteHandler ym2203_control_port_0_w() {
      return new YM2203_control_port_w(0);
   }

   public WriteHandler ym2203_control_port_1_w() {
      return new YM2203_control_port_w(1);
   }

   public WriteHandler ym2203_control_port_2_w() {
      return new YM2203_control_port_w(2);
   }

   public WriteHandler ym2203_control_port_3_w() {
      return new YM2203_control_port_w(3);
   }

   public WriteHandler ym2203_control_port_4_w() {
      return new YM2203_control_port_w(4);
   }

   public ReadHandler ym2203_read_port_0_r() {
      return new YM2203_read_port_r(0);
   }

   public ReadHandler ym2203_read_port_1_r() {
      return new YM2203_read_port_r(1);
   }

   public ReadHandler ym2203_read_port_2_r() {
      return new YM2203_read_port_r(2);
   }

   public ReadHandler ym2203_read_port_3_r() {
      return new YM2203_read_port_r(3);
   }

   public ReadHandler ym2203_read_port_4_r() {
      return new YM2203_read_port_r(4);
   }

   public ReadHandler ym2203_status_port_0_r() {
      return new YM2203_status_port_r(0);
   }

   public ReadHandler ym2203_status_port_1_r() {
      return new YM2203_status_port_r(1);
   }

   public ReadHandler ym2203_status_port_2_r() {
      return new YM2203_status_port_r(2);
   }

   public ReadHandler ym2203_status_port_3_r() {
      return new YM2203_status_port_r(3);
   }

   public ReadHandler ym2203_status_port_4_r() {
      return new YM2203_status_port_r(4);
   }

   public WriteHandler ym2203_write_port_0_w() {
      return new YM2203_write_port_w(0);
   }

   public WriteHandler ym2203_write_port_1_w() {
      return new YM2203_write_port_w(1);
   }

   public WriteHandler ym2203_write_port_2_w() {
      return new YM2203_write_port_w(2);
   }

   public WriteHandler ym2203_write_port_3_w() {
      return new YM2203_write_port_w(3);
   }

   public WriteHandler ym2203_write_port_4_w() {
      return new YM2203_write_port_w(4);
   }

   public class YM2203_control_port_w implements WriteHandler {
      int c;

      public YM2203_control_port_w(int var2) {
         this.c = var2;
      }

      public void write(int var1, int var2) {
         YM2203.this.YM2203Write(this.c, 0, var2);
      }
   }

   private final class YM2203_f {
      FMChan[] CH;
      FMOpn OPN;

      public YM2203_f() {
         this.OPN = new FMOpn(YM2203.this.ym2203, YM2203.this.irqHandler);
         this.CH = new FMChan[3];

         for(int var2 = 0; var2 < 3; ++var2) {
            this.CH[var2] = new FMChan(YM2203.this.ym2203);
         }

      }
   }

   public class YM2203_read_port_r implements ReadHandler {
      int c;

      public YM2203_read_port_r(int var2) {
         this.c = var2;
      }

      public int read(int var1) {
         return YM2203.this.YM2203Read(this.c, 1);
      }
   }

   public class YM2203_status_port_r implements ReadHandler {
      int c;

      public YM2203_status_port_r(int var2) {
         this.c = var2;
      }

      public int read(int var1) {
         return YM2203.this.YM2203Read(this.c, 0);
      }
   }

   public class YM2203_write_port_w implements WriteHandler {
      int c;

      public YM2203_write_port_w(int var2) {
         this.c = var2;
      }

      public void write(int var1, int var2) {
         YM2203.this.YM2203Write(this.c, 1, var2);
      }
   }
}
