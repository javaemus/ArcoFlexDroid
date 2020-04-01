package jef.sound.chip.ym2413;

import java.lang.reflect.Array;

import jef.map.WriteHandler;
import jef.sound.DelayLine;
import jef.sound.Settings;
import jef.sound.SoundChip;

public class YM2413 extends SoundChip {
   public static final double AM_DEPTH = 4.8D;
   public static final int AM_DP_BITS = 16;
   public static final int AM_DP_WIDTH = 65536;
   public static final int AM_PG_BITS = 8;
   public static final int AM_PG_WIDTH = 256;
   public static final double AM_SPEED = 3.7D;
   public static int[] AR_ADJUST_TABLE;
   public static final int DB2LIN_AMP_BITS = 11;
   public static int[] DB2LIN_TABLE;
   public static final int DB_BITS = 8;
   public static final int DB_MUTE = 256;
   public static final double DB_STEP = 0.1875D;
   private static final int DELAY_MS = 20;
   public static final int DP_BASE_BITS = 9;
   public static final int DP_BITS = 18;
   public static final int DP_WIDTH = 262144;
   public static final int EG_BITS = 7;
   public static final int EG_DP_BITS = 22;
   public static final int EG_DP_WIDTH = 4194304;
   public static final int EG_MUTE = 128;
   public static final double EG_STEP = 0.375D;
   public static final int PG_BITS = 9;
   public static final int PG_WIDTH = 512;
   public static final int PM_AMP = 256;
   public static final int PM_AMP_BITS = 8;
   public static final double PM_DEPTH = 13.75D;
   public static final int PM_DP_BITS = 16;
   public static final int PM_DP_WIDTH = 65536;
   public static final int PM_PG_BITS = 8;
   public static final int PM_PG_WIDTH = 256;
   public static final double PM_SPEED = 6.4D;
   public static final int SLOT_AMP_BITS = 11;
   private static final int SLOT_BD1 = 12;
   private static final int SLOT_BD2 = 13;
   private static final int SLOT_CYM = 17;
   private static final int SLOT_HH = 14;
   private static final int SLOT_SD = 15;
   private static final int SLOT_TOM = 16;
   public static final int SL_BITS = 4;
   public static final int SL_MUTE = 16;
   public static final double SL_STEP = 3.0D;
   public static final int TL_BITS = 6;
   public static final int TL_MUTE = 64;
   public static final double TL_STEP = 0.75D;
   public static int am_dphase;
   public static int[] amtable;
   public static final int[] default_inst;
   public static OPLL_PATCH[] default_patch;
   public static int[][] dphaseARTable;
   public static int[][] dphaseDRTable;
   public static int[][][] dphaseTable;
   public static int[] fullsintable;
   public static int[] halfsintable;
   public static OPLL_PATCH null_patch;
   public static int pm_dphase;
   public static int[] pmtable;
   public static int[][][] rksTable;
   public static int[][][][] tllTable;
   public static int[][] waveform;
   boolean DELAY;
   int adr;
   int am_phase;
   OPLL_CH[] ch;
   private int clk;
   DelayLine[] delay;
   int lfo_am;
   int lfo_pm;
   int lr;
   int mask;
   int masterVolume;
   boolean mono = true;
   int noiseA;
   int noiseA_dphase;
   int noiseA_idx;
   int noiseA_phase;
   int noiseB;
   int noiseB_dphase;
   int noiseB_idx;
   int noiseB_phase;
   int noise_seed;
   int out;
   int[] output;
   OPLL_PATCH[] patch;
   int[] patch_update;
   int pm_phase;
   private int rate;
   private int[] reg;
   int regPort;
   int rythm_mode;
   OPLL_SLOT[] slot;
   int[] slot_on_flag;
   int whitenoise;

   static {
      int[] var0 = new int[304];
      var0[16] = 97;
      var0[17] = 97;
      var0[18] = 30;
      var0[19] = 23;
      var0[20] = 240;
      var0[21] = 127;
      var0[22] = 7;
      var0[23] = 23;
      var0[32] = 19;
      var0[33] = 65;
      var0[34] = 15;
      var0[35] = 13;
      var0[36] = 206;
      var0[37] = 210;
      var0[38] = 67;
      var0[39] = 19;
      var0[48] = 3;
      var0[49] = 1;
      var0[50] = 153;
      var0[51] = 4;
      var0[52] = 255;
      var0[53] = 195;
      var0[54] = 3;
      var0[55] = 115;
      var0[64] = 33;
      var0[65] = 97;
      var0[66] = 27;
      var0[67] = 7;
      var0[68] = 175;
      var0[69] = 99;
      var0[70] = 64;
      var0[71] = 40;
      var0[80] = 34;
      var0[81] = 33;
      var0[82] = 30;
      var0[83] = 6;
      var0[84] = 240;
      var0[85] = 118;
      var0[86] = 8;
      var0[87] = 40;
      var0[96] = 49;
      var0[97] = 34;
      var0[98] = 22;
      var0[99] = 5;
      var0[100] = 144;
      var0[101] = 113;
      var0[103] = 24;
      var0[112] = 33;
      var0[113] = 97;
      var0[114] = 29;
      var0[115] = 7;
      var0[116] = 130;
      var0[117] = 129;
      var0[118] = 16;
      var0[119] = 23;
      var0[128] = 35;
      var0[129] = 33;
      var0[130] = 45;
      var0[131] = 22;
      var0[132] = 192;
      var0[133] = 112;
      var0[134] = 7;
      var0[135] = 7;
      var0[144] = 97;
      var0[145] = 33;
      var0[146] = 27;
      var0[147] = 6;
      var0[148] = 100;
      var0[149] = 101;
      var0[150] = 24;
      var0[151] = 24;
      var0[160] = 97;
      var0[161] = 97;
      var0[162] = 12;
      var0[163] = 24;
      var0[164] = 133;
      var0[165] = 160;
      var0[166] = 121;
      var0[167] = 7;
      var0[176] = 35;
      var0[177] = 33;
      var0[178] = 135;
      var0[179] = 17;
      var0[180] = 240;
      var0[181] = 164;
      var0[183] = 247;
      var0[192] = 151;
      var0[193] = 225;
      var0[194] = 40;
      var0[195] = 7;
      var0[196] = 255;
      var0[197] = 243;
      var0[198] = 2;
      var0[199] = 248;
      var0[208] = 97;
      var0[209] = 16;
      var0[210] = 12;
      var0[211] = 5;
      var0[212] = 242;
      var0[213] = 196;
      var0[214] = 64;
      var0[215] = 200;
      var0[224] = 1;
      var0[225] = 1;
      var0[226] = 86;
      var0[227] = 3;
      var0[228] = 180;
      var0[229] = 178;
      var0[230] = 35;
      var0[231] = 88;
      var0[240] = 97;
      var0[241] = 65;
      var0[242] = 137;
      var0[243] = 3;
      var0[244] = 241;
      var0[245] = 244;
      var0[246] = 240;
      var0[247] = 19;
      var0[256] = 4;
      var0[257] = 33;
      var0[258] = 40;
      var0[260] = 223;
      var0[261] = 248;
      var0[262] = 255;
      var0[263] = 248;
      var0[272] = 35;
      var0[273] = 34;
      var0[276] = 216;
      var0[277] = 248;
      var0[278] = 248;
      var0[279] = 248;
      var0[288] = 37;
      var0[289] = 24;
      var0[292] = 248;
      var0[293] = 218;
      var0[294] = 248;
      var0[295] = 85;
      default_inst = var0;
   }

   public YM2413(int var1) {
      this.DELAY = false;
      this.clk = var1;
      this.mono = true;
   }

   public YM2413(int var1, int var2) {
      this.DELAY = Settings.STEREO_DELAY;
      this.clk = var1;
      this.lr = var2;
      this.mono = false;
      if(this.DELAY) {
         this.delay = new DelayLine[3];
         var2 = Settings.SOUND_SAMPLING_FREQ / 1000;

         for(var1 = 0; var1 < 3; ++var1) {
            this.delay[var1] = new DelayLine((var1 + 1) * var2 * 20);
         }
      }

   }

   public static final boolean BIT(int var0, int var1) {
      boolean var2;
      if((var0 >> var1 & 1) != 0) {
         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
   }

   public static int DB_NEG(double var0) {
      return (int)(512.0D + var0 / 0.1875D);
   }

   public static int DB_POS(double var0) {
      return (int)(var0 / 0.1875D);
   }

   public static int EG2DB(double var0) {
      return (int)(2.0D * var0);
   }

   public static int HIGHBITS(int var0, int var1) {
      return var0 >> var1;
   }

   private static int OPLL_MASK_BD() {
      return 8192;
   }

   private static int OPLL_MASK_CH(int var0) {
      return 1 << var0;
   }

   private static int OPLL_MASK_CYM() {
      return 1024;
   }

   private static int OPLL_MASK_HH() {
      return 512;
   }

   private static int OPLL_MASK_SD() {
      return 4096;
   }

   private static int OPLL_MASK_TOM() {
      return 2048;
   }

   public static int SL2EG(double var0) {
      return (int)(8.0D * var0);
   }

   public static int TL2EG(double var0) {
      return (int)(2.0D * var0);
   }

   private final double dB2(double var1) {
      return 2.0D * var1;
   }

   private final void init() {
      fullsintable = new int[512];
      halfsintable = new int[512];
      pmtable = new int[256];
      amtable = new int[256];
      DB2LIN_TABLE = new int[1024];
      AR_ADJUST_TABLE = new int[128];
      default_patch = new OPLL_PATCH[38];
      dphaseARTable = (int[][])Array.newInstance(Integer.TYPE, new int[]{16, 16});
      dphaseDRTable = (int[][])Array.newInstance(Integer.TYPE, new int[]{16, 16});
      tllTable = (int[][][][])Array.newInstance(Integer.TYPE, new int[]{16, 8, 64, 4});
      rksTable = (int[][][])Array.newInstance(Integer.TYPE, new int[]{2, 8, 2});
      dphaseTable = (int[][][])Array.newInstance(Integer.TYPE, new int[]{512, 8, 16});
      null_patch = new OPLL_PATCH();

      int var1;
      for(var1 = 0; var1 < 38; ++var1) {
         default_patch[var1] = new OPLL_PATCH();
      }

      this.makePmTable();
      this.makeAmTable();
      this.makeDB2LinTable();
      this.makeAdjustTable();
      this.makeTllTable();
      this.makeRksTable();
      this.makeSinTable();
      this.makeDefaultPatch();
      this.initClock();
      waveform = new int[2][];
      waveform[0] = fullsintable;
      waveform[1] = halfsintable;
      this.output = new int[2];
      this.reg = new int[64];
      this.slot_on_flag = new int[18];
      this.patch_update = new int[2];
      this.ch = new OPLL_CH[9];
      this.slot = new OPLL_SLOT[18];
      this.patch = new OPLL_PATCH[38];

      for(var1 = 0; var1 < 38; ++var1) {
         this.patch[var1] = new OPLL_PATCH();
      }

      for(var1 = 0; var1 < 9; ++var1) {
         this.ch[var1] = new OPLL_CH(this);
         this.slot[var1 * 2] = this.ch[var1].mod;
         this.slot[var1 * 2 + 1] = this.ch[var1].car;
      }

      this.mask = 0;
      this.masterVolume = 32;
      this.reset();
      this.reset_patch(0);
   }

   private void initClock() {
      this.rate = super.getSampFreq();
      this.makeDphaseTable();
      this.makeDphaseARTable();
      this.makeDphaseDRTable();
      pm_dphase = this.rate_adjust((int)(419430.4D / (double)(this.clk / 72)));
      am_dphase = this.rate_adjust((int)(242483.2D / (double)(this.clk / 72)));
   }

   private final void keyOff(int var1) {
      if(this.slot_on_flag[var1 * 2 + 1] != 0) {
         this.ch[var1].car.slotOff();
      }

      this.ch[var1].key_status = 0;
   }

   private final void keyOff_BD() {
      this.keyOff(6);
   }

   private final void keyOff_CYM() {
      if(this.slot_on_flag[17] != 0) {
         this.ch[8].car.slotOff();
      }

   }

   private final void keyOff_HH() {
      if(this.slot_on_flag[14] != 0) {
         this.ch[7].mod.slotOff();
      }

   }

   private final void keyOff_SD() {
      if(this.slot_on_flag[15] != 0) {
         this.ch[7].car.slotOff();
      }

   }

   private final void keyOff_TOM() {
      if(this.slot_on_flag[16] != 0) {
         this.ch[8].mod.slotOff();
      }

   }

   private final void keyOn(int var1) {
      if(this.slot_on_flag[var1 * 2] == 0) {
         this.ch[var1].mod.slotOn();
      }

      if(this.slot_on_flag[var1 * 2 + 1] == 0) {
         this.ch[var1].car.slotOn();
      }

      this.ch[var1].key_status = 1;
   }

   private final void keyOn_BD() {
      this.keyOn(6);
   }

   private final void keyOn_CYM() {
      if(this.slot_on_flag[17] == 0) {
         this.ch[8].car.slotOn();
      }

   }

   private final void keyOn_HH() {
      if(this.slot_on_flag[14] == 0) {
         this.ch[7].mod.slotOn();
      }

   }

   private final void keyOn_SD() {
      if(this.slot_on_flag[15] == 0) {
         this.ch[7].car.slotOn();
      }

   }

   private final void keyOn_TOM() {
      if(this.slot_on_flag[16] == 0) {
         this.ch[8].mod.slotOn();
      }

   }

   private final int lin2db(double var1) {
      int var3;
      if(var1 == 0.0D) {
         var3 = 255;
      } else {
         var3 = Math.min(-((int)(20.0D * log10(var1) / 0.1875D)), 255);
      }

      return var3;
   }

   private static final double log10(double var0) {
      return Math.log(var0) / Math.log(10.0D);
   }

   private final void makeAdjustTable() {
      AR_ADJUST_TABLE[0] = 128;

      for(int var1 = 1; var1 < 128; ++var1) {
         AR_ADJUST_TABLE[var1] = (int)(127.0D - Math.log((double)var1) * 128.0D / Math.log(128.0D));
      }

   }

   private final void makeAmTable() {
      for(int var1 = 0; var1 < 256; ++var1) {
         amtable[var1] = (int)(12.799999999999999D * (1.0D + Math.sin(6.283185307179586D * (double)var1 / 256.0D)));
      }

   }

   private final void makeDB2LinTable() {
      for(int var1 = 0; var1 < 512; ++var1) {
         DB2LIN_TABLE[var1] = (int)(2047.0D * Math.pow(10.0D, -((double)var1) * 0.1875D / 20.0D));
         if(var1 >= 256) {
            DB2LIN_TABLE[var1] = 0;
         }

         DB2LIN_TABLE[var1 + 256 + 256] = -DB2LIN_TABLE[var1];
      }

   }

   private final void makeDefaultPatch() {
      int[] var3 = new int[8];

      for(int var1 = 0; var1 < 19; ++var1) {
         for(int var2 = 0; var2 < 8; ++var2) {
            var3[var2] = default_inst[var1 * 16 + var2];
         }

         default_patch[var1 * 2].dump0(var3);
         default_patch[var1 * 2 + 1].dump1(var3);
      }

   }

   private final void makeDphaseARTable() {
      for(int var1 = 0; var1 < 16; ++var1) {
         for(int var2 = 0; var2 < 16; ++var2) {
            int var4 = var1 + var2 / 4;
            int var3 = var4;
            if(var4 > 15) {
               var3 = 15;
            }

            switch(var1) {
            case 0:
               dphaseARTable[var1][var2] = 0;
               break;
            case 15:
               dphaseARTable[var1][var2] = 0;
               break;
            default:
               dphaseARTable[var1][var2] = this.rate_adjust(((var2 & 3) + 4) * 3 << var3 + 1);
            }
         }
      }

   }

   private final void makeDphaseDRTable() {
      for(int var1 = 0; var1 < 16; ++var1) {
         for(int var2 = 0; var2 < 16; ++var2) {
            int var4 = var1 + (var2 >> 2);
            int var3 = var4;
            if(var4 > 15) {
               var3 = 15;
            }

            switch(var1) {
            case 0:
               dphaseDRTable[var1][var2] = 0;
               break;
            default:
               dphaseDRTable[var1][var2] = this.rate_adjust((var2 & 3) + 4 << var3 - 1);
            }
         }
      }

   }

   private final void makeDphaseTable() {
      for(int var1 = 0; var1 < 512; ++var1) {
         for(int var2 = 0; var2 < 8; ++var2) {
            for(int var3 = 0; var3 < 16; ++var3) {
               dphaseTable[var1][var2][var3] = this.rate_adjust(((new int[]{1, 2, 4, 6, 8, 10, 12, 14, 16, 18, 20, 20, 24, 24, 30, 30})[var3] * var1 << var2) / 4);
            }
         }
      }

   }

   private final void makePmTable() {
      for(int var1 = 0; var1 < 256; ++var1) {
         pmtable[var1] = (int)(Math.pow(2.0D, 13.75D * Math.sin(6.283185307179586D * (double)var1 / 256.0D) / 1200.0D) * 256.0D);
      }

   }

   private final void makeRksTable() {
      for(int var1 = 0; var1 < 2; ++var1) {
         for(int var2 = 0; var2 < 8; ++var2) {
            for(int var3 = 0; var3 < 2; ++var3) {
               if(var3 != 0) {
                  rksTable[var1][var2][var3] = var2 * 2 + var1;
               } else {
                  rksTable[var1][var2][var3] = var2 / 2;
               }
            }
         }
      }

   }

   private final void makeSinTable() {
      int var1;
      for(var1 = 0; var1 < 128; ++var1) {
         fullsintable[var1] = this.lin2db(Math.sin(6.283185307179586D * (double)(var1 + 1) / 512.0D));
      }

      for(var1 = 0; var1 < 128; ++var1) {
         fullsintable[255 - var1] = fullsintable[var1];
      }

      for(var1 = 0; var1 < 256; ++var1) {
         fullsintable[var1 + 256] = fullsintable[var1] + 512;
      }

      for(var1 = 0; var1 < 256; ++var1) {
         halfsintable[var1] = fullsintable[var1];
      }

      for(var1 = 256; var1 < 512; ++var1) {
         halfsintable[var1] = fullsintable[0];
      }

   }

   private final void makeTllTable() {
      double var15 = this.dB2(0.0D);
      double var27 = this.dB2(9.0D);
      double var5 = this.dB2(12.0D);
      double var25 = this.dB2(13.875D);
      double var1 = this.dB2(15.0D);
      double var23 = this.dB2(16.125D);
      double var31 = this.dB2(16.875D);
      double var29 = this.dB2(17.625D);
      double var9 = this.dB2(18.0D);
      double var7 = this.dB2(18.75D);
      double var11 = this.dB2(19.125D);
      double var17 = this.dB2(19.5D);
      double var19 = this.dB2(19.875D);
      double var3 = this.dB2(20.25D);
      double var13 = this.dB2(20.625D);
      double var21 = this.dB2(21.0D);

      for(int var33 = 0; var33 < 16; ++var33) {
         for(int var34 = 0; var34 < 8; ++var34) {
            for(int var35 = 0; var35 < 64; ++var35) {
               for(int var36 = 0; var36 < 4; ++var36) {
                  if(var36 == 0) {
                     tllTable[var33][var34][var35][var36] = TL2EG((double)var35);
                  } else {
                     int var37 = (int)((new double[]{var15, var27, var5, var25, var1, var23, var31, var29, var9, var7, var11, var17, var19, var3, var13, var21})[var33] - this.dB2(3.0D) * (double)(7 - var34));
                     if(var37 <= 0) {
                        tllTable[var33][var34][var35][var36] = TL2EG((double)var35);
                     } else {
                        tllTable[var33][var34][var35][var36] = (int)((double)(var37 >> 3 - var36) / 0.375D + (double)TL2EG((double)var35));
                     }
                  }
               }
            }
         }
      }

   }

   private int rate_adjust(int var1) {
      return (int)((double)var1 * (double)this.clk / 72.0D / (double)this.rate + 0.5D);
   }

   private final void update_ampm() {
      this.pm_phase = this.pm_phase + pm_dphase & '\uffff';
      this.am_phase = this.am_phase + am_dphase & '\uffff';
      this.lfo_am = amtable[HIGHBITS(this.am_phase, 8)];
      this.lfo_pm = pmtable[HIGHBITS(this.pm_phase, 8)];
   }

   private final void update_key_status() {
      int[] var3;
      for(int var1 = 0; var1 < 9; ++var1) {
         var3 = this.slot_on_flag;
         int[] var4 = this.slot_on_flag;
         int var2 = this.reg[var1 + 32] & 16;
         var4[var1 * 2 + 1] = var2;
         var3[var1 * 2] = var2;
      }

      if((this.reg[14] & 32) != 0) {
         var3 = this.slot_on_flag;
         var3[12] |= this.reg[14] & 16;
         var3 = this.slot_on_flag;
         var3[13] |= this.reg[14] & 16;
         var3 = this.slot_on_flag;
         var3[15] |= this.reg[14] & 8;
         var3 = this.slot_on_flag;
         var3[14] |= this.reg[14] & 1;
         var3 = this.slot_on_flag;
         var3[16] |= this.reg[14] & 4;
         var3 = this.slot_on_flag;
         var3[17] |= this.reg[14] & 2;
      }

   }

   private final void update_noise() {
      if((this.noise_seed & 1) != 0) {
         this.noise_seed ^= 134230048;
      }

      this.noise_seed >>= 1;
   }

   private final void update_rhythm_mode() {
      if((this.ch[6].patch_number & 16) != 0) {
         if((this.slot_on_flag[13] | this.reg[14] & 32) == 0) {
            this.slot[12].eg_mode = 6;
            this.slot[13].eg_mode = 6;
            this.ch[6].setPatch(this.reg[54] >> 4);
         }
      } else if((this.reg[14] & 32) != 0) {
         this.ch[6].patch_number = 16;
         this.slot[12].eg_mode = 6;
         this.slot[13].eg_mode = 6;
         this.slot[12].setPatch(this.patch[32]);
         this.slot[13].setPatch(this.patch[33]);
      }

      if((this.ch[7].patch_number & 16) != 0) {
         if((this.slot_on_flag[14] == 0 || this.slot_on_flag[15] == 0) && (this.reg[14] & 32) == 0) {
            this.slot[14].type = 0;
            this.slot[14].eg_mode = 6;
            this.slot[15].eg_mode = 6;
            this.ch[7].setPatch(this.reg[55] >> 4);
         }
      } else if((this.reg[14] & 32) != 0) {
         this.ch[7].patch_number = 17;
         this.slot[14].type = 1;
         this.slot[14].eg_mode = 6;
         this.slot[15].eg_mode = 6;
         this.slot[14].setPatch(this.patch[34]);
         this.slot[15].setPatch(this.patch[35]);
      }

      if((this.ch[8].patch_number & 16) != 0) {
         if((this.slot_on_flag[17] & this.slot_on_flag[16] | this.reg[14] & 32) == 0) {
            this.slot[16].type = 0;
            this.slot[16].eg_mode = 6;
            this.slot[17].eg_mode = 6;
            this.ch[8].setPatch(this.reg[56] >> 4);
         }
      } else if((this.reg[14] & 32) != 0) {
         this.ch[8].patch_number = 18;
         this.slot[16].type = 1;
         this.slot[16].eg_mode = 6;
         this.slot[17].eg_mode = 6;
         this.slot[16].setPatch(this.patch[36]);
         this.slot[17].setPatch(this.patch[37]);
      }

   }

   public int LOWBITS(int var1, int var2) {
      return (1 << var2) - 1 & var1;
   }

   public WriteHandler getDataPortWrite() {
      return new WriteHandler() {
         public void write(int var1, int var2) {
            YM2413.this.writeReg(YM2413.this.regPort, var2);
         }
      };
   }

   public WriteHandler getRegisterPortWrite() {
      return new WriteHandler() {
         public void write(int var1, int var2) {
            YM2413.this.regPort = var2 & 63;
         }
      };
   }

   public void init(boolean var1, int var2, int var3) {
      super.init(this.mono, var2, var3);
      this.init();
   }

   public final short nextSample() {
      byte var2 = 0;
      byte var5 = 0;
      this.update_ampm();
      this.update_noise();

      int var1;
      for(var1 = 0; var1 < 18; ++var1) {
         this.slot[var1].calc_phase(this.lfo_pm);
         this.slot[var1].calc_envelope(this.lfo_am);
      }

      int var3 = 0;

      int var4;
      int var6;
      for(var1 = var2; var3 < 6; var1 = var6) {
         var6 = var1;
         if((this.mask & OPLL_MASK_CH(var3)) == 0) {
            var6 = var1;
            if(this.ch[var3].car.eg_mode != 6) {
               var4 = this.ch[var3].car.calc_slot_car(this.ch[var3].mod.calc_slot_mod());
               var6 = var4;
               if(!this.mono) {
                  var6 = var4;
                  if((this.lr + var3 & 1) == 1) {
                     var4 >>= 1;
                     var6 = var4;
                     if(this.DELAY) {
                        var6 = this.delay[var3 >> 1].setNewAndGetDelayed(var4);
                     }
                  }
               }

               var6 += var1;
            }
         }

         ++var3;
      }

      if(this.ch[6].patch_number <= 15) {
         var4 = var1;
         var6 = var5;
         if((this.mask & OPLL_MASK_CH(6)) == 0) {
            var4 = var1;
            var6 = var5;
            if(this.ch[6].car.eg_mode != 6) {
               var4 = var1 + this.ch[6].car.calc_slot_car(this.ch[6].mod.calc_slot_mod());
               var6 = var5;
            }
         }
      } else {
         var4 = var1;
         var6 = var5;
         if((this.mask & OPLL_MASK_BD()) == 0) {
            var4 = var1;
            var6 = var5;
            if(this.ch[6].car.eg_mode != 6) {
               var6 = 0 + this.ch[6].car.calc_slot_car(this.ch[6].mod.calc_slot_mod());
               var4 = var1;
            }
         }
      }

      int var7;
      if(this.ch[7].patch_number <= 15) {
         var3 = var4;
         var1 = var6;
         if((this.mask & OPLL_MASK_CH(7)) == 0) {
            var3 = var4;
            var1 = var6;
            if(this.ch[7].car.eg_mode != 6) {
               var3 = var4 + this.ch[7].car.calc_slot_car(this.ch[7].mod.calc_slot_mod());
               var1 = var6;
            }
         }
      } else {
         var7 = var6;
         if((this.mask & OPLL_MASK_HH()) == 0) {
            var7 = var6;
            if(this.ch[7].mod.eg_mode != 6) {
               var7 = var6 + this.ch[7].mod.calc_slot_hat(this.ch[8].car.pgout, this.noise_seed & 1);
            }
         }

         var3 = var4;
         var1 = var7;
         if((this.mask & OPLL_MASK_SD()) == 0) {
            var3 = var4;
            var1 = var7;
            if(this.ch[7].car.eg_mode != 6) {
               var1 = var7 - this.ch[7].car.calc_slot_snare(this.noise_seed & 1);
               var3 = var4;
            }
         }
      }

      if(this.ch[8].patch_number <= 15) {
         var7 = var3;
         var6 = var1;
         if((this.mask & OPLL_MASK_CH(8)) == 0) {
            var7 = var3;
            var6 = var1;
            if(this.ch[8].car.eg_mode != 6) {
               var7 = var3 + this.ch[8].car.calc_slot_car(this.ch[8].mod.calc_slot_mod());
               var6 = var1;
            }
         }
      } else {
         var4 = var1;
         if((this.mask & OPLL_MASK_TOM()) == 0) {
            var4 = var1;
            if(this.ch[8].mod.eg_mode != 6) {
               var4 = var1 + this.ch[8].mod.calc_slot_tom();
            }
         }

         var7 = var3;
         var6 = var4;
         if((this.mask & OPLL_MASK_CYM()) == 0) {
            var7 = var3;
            var6 = var4;
            if(this.ch[8].car.eg_mode != 6) {
               var6 = var4 - this.ch[8].car.calc_slot_cym(this.ch[7].mod.pgout);
               var7 = var3;
            }
         }
      }

      return (short)(var7 + var6 << 2);
   }

   public void reset() {
      this.adr = 0;
      this.out = 0;
      this.pm_phase = 0;
      this.am_phase = 0;
      this.noise_seed = '\uffff';
      this.mask = 0;

      int var1;
      for(var1 = 0; var1 < 18; ++var1) {
         this.slot[var1].reset(var1 % 2);
      }

      for(var1 = 0; var1 < 9; ++var1) {
         this.ch[var1].key_status = 0;
         this.ch[var1].setPatch(0);
      }

      for(var1 = 0; var1 < 64; ++var1) {
         this.writeReg(var1, 0);
      }

   }

   public final void reset_patch(int var1) {
      for(var1 = 0; var1 < 38; ++var1) {
         this.patch[var1] = default_patch[var1].copy();
      }

   }

   public final int setMask(int var1) {
      int var2 = this.mask;
      this.mask = var1;
      return var2;
   }

   public final int toggleMask(int var1) {
      int var2 = this.mask;
      this.mask ^= var1;
      return var2;
   }

   public void writeBuffer() {
      this.clearBuffer();
      int var2 = super.getBufferLength();

      for(int var1 = 0; var1 < var2; ++var1) {
         if(this.mono) {
            this.writeLinBuffer(var1, this.nextSample());
         } else {
            this.writeLinBuffer(this.lr, var1, this.nextSample());
         }
      }

   }

   public final void writeReg(int var1, int var2) {
      var2 &= 255;
      int var3 = var1 & 63;
      this.reg[var3] = var2;
      switch(var3) {
      case 0:
         this.patch[0].AM = var2 >> 7 & 1;
         this.patch[0].PM = var2 >> 6 & 1;
         this.patch[0].EG = var2 >> 5 & 1;
         this.patch[0].KR = var2 >> 4 & 1;
         this.patch[0].ML = var2 & 15;

         for(var1 = 0; var1 < 9; ++var1) {
            if(this.ch[var1].patch_number == 0) {
               this.ch[var1].mod.UPDATE_PG();
               this.ch[var1].mod.UPDATE_RKS();
               this.ch[var1].mod.UPDATE_EG();
            }
         }

         return;
      case 1:
         this.patch[1].AM = var2 >> 7 & 1;
         this.patch[1].PM = var2 >> 6 & 1;
         this.patch[1].EG = var2 >> 5 & 1;
         this.patch[1].KR = var2 >> 4 & 1;
         this.patch[1].ML = var2 & 15;

         for(var1 = 0; var1 < 9; ++var1) {
            if(this.ch[var1].patch_number == 0) {
               this.ch[var1].car.UPDATE_PG();
               this.ch[var1].car.UPDATE_RKS();
               this.ch[var1].car.UPDATE_EG();
            }
         }

         return;
      case 2:
         this.patch[0].KL = var2 >> 6 & 3;
         this.patch[0].TL = var2 & 63;

         for(var1 = 0; var1 < 9; ++var1) {
            if(this.ch[var1].patch_number == 0) {
               this.ch[var1].mod.UPDATE_TLL();
            }
         }

         return;
      case 3:
         this.patch[1].KL = var2 >> 6 & 3;
         this.patch[1].WF = var2 >> 4 & 1;
         this.patch[0].WF = var2 >> 3 & 1;
         this.patch[0].FB = var2 & 7;

         for(var1 = 0; var1 < 9; ++var1) {
            if(this.ch[var1].patch_number == 0) {
               this.ch[var1].mod.UPDATE_WF();
               this.ch[var1].car.UPDATE_WF();
            }
         }

         return;
      case 4:
         this.patch[0].AR = var2 >> 4 & 15;
         this.patch[0].DR = var2 & 15;

         for(var1 = 0; var1 < 9; ++var1) {
            if(this.ch[var1].patch_number == 0) {
               this.ch[var1].mod.UPDATE_EG();
            }
         }

         return;
      case 5:
         this.patch[1].AR = var2 >> 4 & 15;
         this.patch[1].DR = var2 & 15;

         for(var1 = 0; var1 < 9; ++var1) {
            if(this.ch[var1].patch_number == 0) {
               this.ch[var1].car.UPDATE_EG();
            }
         }

         return;
      case 6:
         this.patch[0].SL = var2 >> 4 & 15;
         this.patch[0].RR = var2 & 15;

         for(var1 = 0; var1 < 9; ++var1) {
            if(this.ch[var1].patch_number == 0) {
               this.ch[var1].mod.UPDATE_EG();
            }
         }

         return;
      case 7:
         this.patch[1].SL = var2 >> 4 & 15;
         this.patch[1].RR = var2 & 15;

         for(var1 = 0; var1 < 9; ++var1) {
            if(this.ch[var1].patch_number == 0) {
               this.ch[var1].car.UPDATE_EG();
            }
         }
      case 8:
      case 9:
      case 10:
      case 11:
      case 12:
      case 13:
      case 15:
      case 25:
      case 26:
      case 27:
      case 28:
      case 29:
      case 30:
      case 31:
      case 41:
      case 42:
      case 43:
      case 44:
      case 45:
      case 46:
      case 47:
      default:
         break;
      case 14:
         this.update_rhythm_mode();
         if((var2 & 32) != 0) {
            if((var2 & 16) != 0) {
               this.keyOn_BD();
            } else {
               this.keyOff_BD();
            }

            if((var2 & 8) != 0) {
               this.keyOn_SD();
            } else {
               this.keyOff_SD();
            }

            if((var2 & 4) != 0) {
               this.keyOn_TOM();
            } else {
               this.keyOff_TOM();
            }

            if((var2 & 2) != 0) {
               this.keyOn_CYM();
            } else {
               this.keyOff_CYM();
            }

            if((var2 & 1) != 0) {
               this.keyOn_HH();
            } else {
               this.keyOff_HH();
            }
         }

         this.update_key_status();
         this.ch[6].mod.UPDATE_ALL();
         this.ch[6].car.UPDATE_ALL();
         this.ch[7].mod.UPDATE_ALL();
         this.ch[7].car.UPDATE_ALL();
         this.ch[8].mod.UPDATE_ALL();
         this.ch[8].car.UPDATE_ALL();
         break;
      case 16:
      case 17:
      case 18:
      case 19:
      case 20:
      case 21:
      case 22:
      case 23:
      case 24:
         var1 = var3 - 16;
         this.ch[var1].setFnumber(((this.reg[var1 + 32] & 1) << 8) + var2);
         this.ch[var1].mod.UPDATE_ALL();
         this.ch[var1].car.UPDATE_ALL();
         break;
      case 32:
      case 33:
      case 34:
      case 35:
      case 36:
      case 37:
      case 38:
      case 39:
      case 40:
         var1 = var3 - 32;
         this.ch[var1].setFnumber(((var2 & 1) << 8) + this.reg[var1 + 16]);
         this.ch[var1].setBlock(var2 >> 1 & 7);
         this.ch[var1].setSustine(var2 >> 5 & 1);
         if((var2 & 16) != 0) {
            this.keyOn(var1);
         } else {
            this.keyOff(var1);
         }

         this.ch[var1].mod.UPDATE_ALL();
         this.ch[var1].car.UPDATE_ALL();
         this.update_key_status();
         this.update_rhythm_mode();
         break;
      case 48:
      case 49:
      case 50:
      case 51:
      case 52:
      case 53:
      case 54:
      case 55:
      case 56:
         var1 = var2 >> 4 & 15;
         if((this.reg[14] & 32) != 0 && var3 >= 54) {
            switch(var3) {
            case 55:
               this.ch[7].mod.setVolume(var1 << 2);
               break;
            case 56:
               this.ch[8].mod.setVolume(var1 << 2);
            }
         } else {
            this.ch[var3 - 48].setPatch(var1);
         }

         this.ch[var3 - 48].setVolume((var2 & 15) << 2);
         this.ch[var3 - 48].mod.UPDATE_ALL();
         this.ch[var3 - 48].car.UPDATE_ALL();
      }

   }

   private final class OPLL_CH {
      OPLL_SLOT car;
      YM2413 chip;
      int key_status;
      OPLL_SLOT mod;
      int patch_number;

      OPLL_CH(YM2413 var2) {
         this.chip = var2;
         this.mod = YM2413.this.new OPLL_SLOT(this);
         this.mod.reset(0);
         this.car = YM2413.this.new OPLL_SLOT(this);
         this.car.reset(1);
         this.key_status = 0;
      }

      public final void reset() {
         this.mod.reset(0);
         this.car.reset(1);
         this.key_status = 0;
      }

      public final void setBlock(int var1) {
         this.car.block = var1;
         this.mod.block = var1;
      }

      public final void setFnumber(int var1) {
         this.car.fnum = var1;
         this.mod.fnum = var1;
      }

      public final void setPatch(int var1) {
         this.patch_number = var1;
         this.mod.setPatch(this.chip.patch[var1 * 2 + 0]);
         this.car.setPatch(this.chip.patch[var1 * 2 + 1]);
      }

      public final void setSustine(int var1) {
         this.car.sustine = var1;
         if(this.mod.type != 0) {
            this.mod.sustine = var1;
         }

      }

      public final void setVolume(int var1) {
         this.car.setVolume(var1);
      }
   }

   private final class OPLL_PATCH {
      int AM;
      int AR;
      int DR;
      int EG;
      int FB;
      int KL;
      int KR;
      int ML;
      int PM;
      int RR;
      int SL;
      int TL;
      int WF;

      public final OPLL_PATCH copy() {
         OPLL_PATCH var1 = YM2413.this.new OPLL_PATCH();
         var1.TL = this.TL;
         var1.FB = this.FB;
         var1.EG = this.EG;
         var1.ML = this.ML;
         var1.AR = this.AR;
         var1.DR = this.DR;
         var1.SL = this.SL;
         var1.RR = this.RR;
         var1.KR = this.KR;
         var1.KL = this.KL;
         var1.AM = this.AM;
         var1.PM = this.PM;
         var1.WF = this.WF;
         return var1;
      }

      public final void dump0(int[] var1) {
         this.AM = var1[0] >> 7 & 1;
         this.PM = var1[0] >> 6 & 1;
         this.EG = var1[0] >> 5 & 1;
         this.KR = var1[0] >> 4 & 1;
         this.ML = var1[0] & 15;
         this.KL = var1[2] >> 6 & 3;
         this.TL = var1[2] & 63;
         this.FB = var1[3] & 7;
         this.WF = var1[3] >> 3 & 1;
         this.AR = var1[4] >> 4 & 15;
         this.DR = var1[4] & 15;
         this.SL = var1[6] >> 4 & 15;
         this.RR = var1[6] & 15;
      }

      public final void dump1(int[] var1) {
         this.AM = var1[1] >> 7 & 1;
         this.PM = var1[1] >> 6 & 1;
         this.EG = var1[1] >> 5 & 1;
         this.KR = var1[1] >> 4 & 1;
         this.ML = var1[1] & 15;
         this.KL = var1[3] >> 6 & 3;
         this.WF = var1[3] >> 4 & 1;
         this.AR = var1[5] >> 4 & 15;
         this.DR = var1[5] & 15;
         this.SL = var1[7] >> 4 & 15;
         this.RR = var1[7] & 15;
      }
   }

   private final class OPLL_SLOT {
      private static final int ATTACK = 1;
      private static final int DECAY = 2;
      private static final int FINISH = 6;
      private static final int RELEASE = 5;
      private static final int SETTLE = 0;
      private static final int SUSHOLD = 3;
      private static final int SUSTINE = 4;
      private int[] SL = new int[]{this.S2E(0.0D), this.S2E(3.0D), this.S2E(6.0D), this.S2E(9.0D), this.S2E(12.0D), this.S2E(15.0D), this.S2E(18.0D), this.S2E(21.0D), this.S2E(24.0D), this.S2E(27.0D), this.S2E(30.0D), this.S2E(33.0D), this.S2E(36.0D), this.S2E(39.0D), this.S2E(42.0D), this.S2E(48.0D)};
      int block;
      OPLL_CH channel;
      int dphase;
      int eg_dphase;
      int eg_mode;
      int eg_phase;
      int egout;
      int feedback;
      int fnum;
      int[] output;
      OPLL_PATCH patch;
      int pgout;
      int phase;
      int rks;
      int[] sintbl;
      int sustine;
      int tll;
      int type;
      int volume;

      OPLL_SLOT(OPLL_CH var2) {
         this.channel = var2;
         this.output = new int[5];
         this.reset(0);
      }

      private final int EXPAND_BITS(int var1, int var2, int var3) {
         return var1 << var3 - var2;
      }

      private final int S2E(double var1) {
         return YM2413.SL2EG((double)((int)(var1 / 3.0D))) << 15;
      }

      private final void calc_eg_dphase() {
         switch(this.eg_mode) {
         case 1:
            this.eg_dphase = YM2413.dphaseARTable[this.patch.AR][this.rks];
            break;
         case 2:
            this.eg_dphase = YM2413.dphaseDRTable[this.patch.DR][this.rks];
            break;
         case 3:
            this.eg_dphase = 0;
            break;
         case 4:
            this.eg_dphase = YM2413.dphaseDRTable[this.patch.RR][this.rks];
            break;
         case 5:
            if(this.sustine != 0) {
               this.eg_dphase = YM2413.dphaseDRTable[5][this.rks];
            } else if(this.patch.EG != 0) {
               this.eg_dphase = YM2413.dphaseDRTable[this.patch.RR][this.rks];
            } else {
               this.eg_dphase = YM2413.dphaseDRTable[7][this.rks];
            }
            break;
         case 6:
            this.eg_dphase = 0;
            break;
         default:
            this.eg_dphase = 0;
         }

      }

      private final int wave2_4pi(int var1) {
         return var1 >> 1;
      }

      private final int wave2_8pi(int var1) {
         return var1;
      }

      public final void UPDATE_ALL() {
         this.UPDATE_PG();
         this.UPDATE_TLL();
         this.UPDATE_RKS();
         this.UPDATE_WF();
         this.UPDATE_EG();
      }

      public final void UPDATE_EG() {
         this.calc_eg_dphase();
      }

      public final void UPDATE_PG() {
         this.dphase = YM2413.dphaseTable[this.fnum][this.block][this.patch.ML];
      }

      public final void UPDATE_RKS() {
         this.rks = YM2413.rksTable[this.fnum / 256][this.block][this.patch.KR];
      }

      public final void UPDATE_TLL() {
         if(this.type == 0) {
            this.tll = YM2413.tllTable[this.fnum / 32][this.block][this.patch.TL][this.patch.KL];
         } else {
            this.tll = YM2413.tllTable[this.fnum / 32][this.block][this.volume][this.patch.KL];
         }

      }

      public final void UPDATE_WF() {
         this.sintbl = YM2413.waveform[this.patch.WF];
      }

      public final void calc_envelope(int var1) {
         switch(this.eg_mode) {
         case 1:
            this.egout = YM2413.AR_ADJUST_TABLE[YM2413.HIGHBITS(this.eg_phase, 15)];
            this.eg_phase += this.eg_dphase;
            if((4194304 & this.eg_phase) != 0 || this.patch.AR == 15) {
               this.egout = 0;
               this.eg_phase = 0;
               this.eg_mode = 2;
               this.UPDATE_EG();
            }
            break;
         case 2:
            this.egout = YM2413.HIGHBITS(this.eg_phase, 15);
            this.eg_phase += this.eg_dphase;
            if(this.eg_phase >= this.SL[this.patch.SL]) {
               if(this.patch.EG != 0) {
                  this.eg_phase = this.SL[this.patch.SL];
                  this.eg_mode = 3;
                  this.UPDATE_EG();
               } else {
                  this.eg_phase = this.SL[this.patch.SL];
                  this.eg_mode = 4;
                  this.UPDATE_EG();
               }
            }
            break;
         case 3:
            this.egout = YM2413.HIGHBITS(this.eg_phase, 15);
            if(this.patch.EG == 0) {
               this.eg_mode = 4;
               this.UPDATE_EG();
            }
            break;
         case 4:
         case 5:
            this.egout = YM2413.HIGHBITS(this.eg_phase, 15);
            this.eg_phase += this.eg_dphase;
            if(this.egout >= 128) {
               this.eg_mode = 6;
               this.egout = 127;
            }
            break;
         case 6:
            this.egout = 127;
            break;
         default:
            this.egout = 127;
         }

         if(this.patch.AM != 0) {
            this.egout = YM2413.EG2DB((double)(this.egout + this.tll)) + var1;
         } else {
            this.egout = YM2413.EG2DB((double)(this.egout + this.tll));
         }

         if(this.egout >= 256) {
            this.egout = 255;
         }

      }

      public final void calc_phase(int var1) {
         if(this.patch.PM != 0) {
            this.phase += this.dphase * var1 >> 8;
         } else {
            this.phase += this.dphase;
         }

         this.phase &= 262143;
         this.pgout = YM2413.HIGHBITS(this.phase, 9);
      }

      public final int calc_slot_car(int var1) {
         this.output[1] = this.output[0];
         if(this.egout >= 255) {
            this.output[0] = 0;
         } else {
            this.output[0] = YM2413.DB2LIN_TABLE[this.sintbl[this.pgout + this.wave2_8pi(var1) & 511] + this.egout];
         }

         return (this.output[1] + this.output[0]) / 2;
      }

      public final int calc_slot_cym(int var1) {
         if(this.egout >= 255) {
            var1 = 0;
         } else {
            if(!(YM2413.BIT(var1, 1) ^ YM2413.BIT(var1, 8) | YM2413.BIT(var1, 2)) && !(YM2413.BIT(this.pgout, 2) & YM2413.BIT(this.pgout, 4))) {
               var1 = YM2413.DB_POS(3.0D);
            } else {
               var1 = YM2413.DB_NEG(3.0D);
            }

            var1 = YM2413.DB2LIN_TABLE[this.egout + var1];
         }

         return var1;
      }

      public final int calc_slot_hat(int var1, int var2) {
         if(this.egout >= 255) {
            var1 = 0;
         } else {
            if(YM2413.BIT(this.pgout, 1) ^ YM2413.BIT(this.pgout, 8) || YM2413.BIT(this.pgout, 2) || YM2413.BIT(var1, 2) && !YM2413.BIT(var1, 4)) {
               if(var2 != 0) {
                  var1 = YM2413.DB_NEG(3.0D);
               } else {
                  var1 = YM2413.DB_NEG(12.0D);
               }
            } else if(var2 != 0) {
               var1 = YM2413.DB_POS(12.0D);
            } else {
               var1 = YM2413.DB_POS(24.0D);
            }

            var1 = YM2413.DB2LIN_TABLE[this.egout + var1];
         }

         return var1;
      }

      public final int calc_slot_mod() {
         this.output[1] = this.output[0];
         if(this.egout >= 255) {
            this.output[0] = 0;
         } else if(this.patch.FB != 0) {
            int var1 = this.wave2_4pi(this.feedback);
            int var2 = this.patch.FB;
            this.output[0] = YM2413.DB2LIN_TABLE[this.sintbl[this.pgout + (var1 >> 7 - var2) & 511] + this.egout];
         } else {
            this.output[0] = YM2413.DB2LIN_TABLE[this.sintbl[this.pgout] + this.egout];
         }

         this.feedback = (this.output[1] + this.output[0]) / 2;
         return this.feedback;
      }

      public final int calc_slot_snare(int var1) {
         if(this.egout >= 255) {
            var1 = 0;
         } else {
            int[] var2;
            if(YM2413.BIT(this.pgout, 7)) {
               var2 = YM2413.DB2LIN_TABLE;
               if(var1 != 0) {
                  var1 = YM2413.DB_POS(0.0D);
               } else {
                  var1 = YM2413.DB_POS(15.0D);
               }

               var1 = var2[var1 + this.egout];
            } else {
               var2 = YM2413.DB2LIN_TABLE;
               if(var1 != 0) {
                  var1 = YM2413.DB_NEG(0.0D);
               } else {
                  var1 = YM2413.DB_NEG(15.0D);
               }

               var1 = var2[var1 + this.egout];
            }
         }

         return var1;
      }

      public final int calc_slot_tom() {
         int var1;
         if(this.egout >= 255) {
            var1 = 0;
         } else {
            var1 = YM2413.DB2LIN_TABLE[this.sintbl[this.pgout] + this.egout];
         }

         return var1;
      }

      public final void reset(int var1) {
         this.type = var1;
         this.sintbl = YM2413.waveform[0];
         this.phase = 0;
         this.dphase = 0;
         this.output[0] = 0;
         this.output[1] = 0;
         this.feedback = 0;
         this.eg_mode = 0;
         this.eg_phase = 4194304;
         this.eg_dphase = 0;
         this.rks = 0;
         this.tll = 0;
         this.sustine = 0;
         this.fnum = 0;
         this.block = 0;
         this.volume = 0;
         this.pgout = 0;
         this.egout = 0;
         this.patch = YM2413.null_patch;
      }

      public final void setPatch(OPLL_PATCH var1) {
         this.patch = var1;
      }

      public final void setVolume(int var1) {
         this.volume = var1;
      }

      public final void slotOff() {
         if(this.eg_mode == 1) {
            this.eg_phase = this.EXPAND_BITS(YM2413.AR_ADJUST_TABLE[YM2413.HIGHBITS(this.eg_phase, 15)], 7, 22);
         }

         this.eg_mode = 5;
      }

      public final void slotOn() {
         this.eg_mode = 1;
         this.phase = 0;
         this.eg_phase = 0;
      }
   }
}
