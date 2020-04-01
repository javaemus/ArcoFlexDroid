package jef.sound.chip.fm;

import jef.sound.SoundChip;
import jef.sound.chip.AY8910;
import jef.util.INT32;

public abstract class FM extends SoundChip implements FMConstants {
   public static int[] DRAR_TABLE = new int[4096];
   public static int[] ENV_CURVE = new int[12289];
   private static int[] SIN_TABLE = new int[2048];
   private static int[] TL_TABLE;
   public AY8910 SSG;
   public INT32[] out_ch = new INT32[4];
   public INT32 pg_in1 = new INT32();
   public INT32 pg_in2 = new INT32();
   public INT32 pg_in3 = new INT32();
   public INT32 pg_in4 = new INT32();

   private double log10(double var1) {
      return Math.log(var1) / Math.log(10.0D);
   }

   public void FMInitTable() {
      TL_TABLE = new int[12460];

      double var1;
      int var3;
      for(var3 = 0; var3 < 6230; ++var3) {
         if(var3 >= 3328) {
            var1 = 0.0D;
         } else {
            var1 = 6.7108863E7D / Math.pow(10.0D, 0.0234375D * (double)var3 / 20.0D);
         }

         TL_TABLE[var3] = (int)var1;
         TL_TABLE[var3 + 6230] = -TL_TABLE[var3];
      }

      int var4;
      int[] var6;
      for(var3 = 1; var3 <= 512; ++var3) {
         int var5 = (int)(20.0D * this.log10(1.0D / Math.sin(6.283185307179586D * (double)var3 / 2048.0D)) / 0.0234375D);
         var4 = var5;
         if(var5 > 3328) {
            var4 = 3328;
         }

         var6 = SIN_TABLE;
         SIN_TABLE[1024 - var3] = var4;
         var6[var3] = var4;
         int[] var7 = SIN_TABLE;
         var6 = SIN_TABLE;
         var4 += 6230;
         var6[2048 - var3] = var4;
         var7[var3 + 1024] = var4;
      }

      var6 = SIN_TABLE;
      SIN_TABLE[1024] = 3328;
      var6[0] = 3328;

      for(var3 = 0; var3 < 4096; ++var3) {
         var1 = Math.pow((double)(4095 - var3) / 4096.0D, 8.0D);
         ENV_CURVE[var3] = (int)(var1 * 4096.0D);
         ENV_CURVE[var3 + 4096] = var3;
         ENV_CURVE[var3 + 8192] = 4095 - var3;
      }

      ENV_CURVE[8191] = 4095;
      var4 = 4095;

      for(var3 = 0; var3 < 4096; ++var3) {
         while(var4 != 0 && ENV_CURVE[var4] < var3) {
            --var4;
         }

         DRAR_TABLE[var3] = var4 << 16;
      }

   }

   public int SIN_TABLE(int var1, int var2) {
      return TL_TABLE[SIN_TABLE[var1] + var2];
   }

   protected void reset_channel(FMState var1, FMChan[] var2, int var3) {
      var1.mode = 0L;
      var1.FM_STATUS_RESET(255);
      var1.TA = 0;
      var1.TAC = 0;
      var1.TB = 0;
      var1.TBC = 0;

      for(int var4 = 0; var4 < var3; ++var4) {
         var2[var4].fc = 0;
         var2[var4].PAN = 3;

         for(int var5 = 0; var5 < 4; ++var5) {
            var2[var4].SLOT[var5].SEG = 0;
            var2[var4].SLOT[var5].eg_next = 0;
            var2[var4].SLOT[var5].evc = 536805376;
            var2[var4].SLOT[var5].eve = 536805377;
            var2[var4].SLOT[var5].evs = 0;
         }
      }

   }

   public void setup_connection(FMChan var1) {
      INT32 var2 = this.out_ch[var1.PAN];
      switch(var1.ALGO) {
      case 0:
         var1.connect1 = this.pg_in2;
         var1.connect2 = this.pg_in3;
         var1.connect3 = this.pg_in4;
         break;
      case 1:
         var1.connect1 = this.pg_in3;
         var1.connect2 = this.pg_in3;
         var1.connect3 = this.pg_in4;
         break;
      case 2:
         var1.connect1 = this.pg_in4;
         var1.connect2 = this.pg_in3;
         var1.connect3 = this.pg_in4;
         break;
      case 3:
         var1.connect1 = this.pg_in2;
         var1.connect2 = this.pg_in4;
         var1.connect3 = this.pg_in4;
         break;
      case 4:
         var1.connect1 = this.pg_in2;
         var1.connect2 = var2;
         var1.connect3 = this.pg_in4;
         break;
      case 5:
         var1.connect1 = null;
         var1.connect2 = var2;
         var1.connect3 = var2;
         break;
      case 6:
         var1.connect1 = this.pg_in2;
         var1.connect2 = var2;
         var1.connect3 = var2;
         break;
      case 7:
         var1.connect1 = var2;
         var1.connect2 = var2;
         var1.connect3 = var2;
      }

      var1.connect4 = var2;
   }
}
