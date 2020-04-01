package jef.sound.chip.okim6295;

import jef.sound.SoundChip;

public class OKIM6296 extends SoundChip {
   static final int OKIM6295_VOICES = 4;
   static int[] diff_lookup = new int[784];
   static final int[] index_shift = new int[]{-1, -1, -1, -1, 2, 4, 6, 8};
   static boolean tables_computed = false;
   static int[] volume_table = new int[16];
   int bank_offset;
   int command;
   int region_base;
   ADPCMVoice[] voice = new ADPCMVoice[4];

   static void compute_tables() {
      int[][] var5 = new int[16][];
      int[] var6 = new int[4];
      var6[0] = 1;
      var5[0] = var6;
      var6 = new int[]{1, 0, 0, 1};
      var5[1] = var6;
      var6 = new int[]{1, 0, 1, 0};
      var5[2] = var6;
      var6 = new int[]{1, 0, 1, 1};
      var5[3] = var6;
      var6 = new int[]{1, 1, 0, 0};
      var5[4] = var6;
      var6 = new int[]{1, 1, 0, 1};
      var5[5] = var6;
      var6 = new int[]{1, 1, 1, 0};
      var5[6] = var6;
      var5[7] = new int[]{1, 1, 1, 1};
      var6 = new int[4];
      var6[0] = -1;
      var5[8] = var6;
      var6 = new int[]{-1, 0, 0, 1};
      var5[9] = var6;
      var6 = new int[]{-1, 0, 1, 0};
      var5[10] = var6;
      var6 = new int[]{-1, 0, 1, 1};
      var5[11] = var6;
      var6 = new int[]{-1, 1, 0, 0};
      var5[12] = var6;
      var6 = new int[]{-1, 1, 0, 1};
      var5[13] = var6;
      var6 = new int[]{-1, 1, 1, 0};
      var5[14] = var6;
      var5[15] = new int[]{-1, 1, 1, 1};

      int var2;
      int var3;
      for(var2 = 0; var2 <= 48; ++var2) {
         int var4 = (int)Math.floor(16.0D * Math.pow(1.1D, (double)var2));

         for(var3 = 0; var3 < 16; ++var3) {
            diff_lookup[var2 * 16 + var3] = var5[var3][0] * (var5[var3][1] * var4 + var4 / 2 * var5[var3][2] + var4 / 4 * var5[var3][3] + var4 / 8);
         }
      }

      for(var2 = 0; var2 < 16; ++var2) {
         double var0 = 256.0D;

         for(var3 = var2; var3 > 0; --var3) {
            var0 /= 1.412537545D;
         }

         volume_table[var2] = (int)var0;
      }

      tables_computed = true;
   }

   int clock_adpcm(ADPCMState var1, int var2) {
      var1.signal += diff_lookup[var1.step * 16 + (var2 & 15)];
      if(var1.signal > 2047) {
         var1.signal = 2047;
      } else if(var1.signal < -2048) {
         var1.signal = -2048;
      }

      var1.step += index_shift[var2 & 7];
      if(var1.step > 48) {
         var1.step = 48;
      } else if(var1.step < 0) {
         var1.step = 0;
      }

      return var1.signal << 4;
   }

   void reset_adpcm(ADPCMState var1) {
      if(!tables_computed) {
         compute_tables();
      }

      var1.signal = -2;
      var1.step = 0;
   }

   public void writeBuffer() {
   }
}
