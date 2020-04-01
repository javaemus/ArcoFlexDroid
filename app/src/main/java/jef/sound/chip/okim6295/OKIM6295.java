package jef.sound.chip.okim6295;

import jef.sound.SoundChip;

public class OKIM6295 extends SoundChip {
   static final int MAX_SAMPLE_CHUNK = 10000;
   static final int OKIM6295_VOICES = 4;
   static int[] diff_lookup = new int[784];
   static final int[] index_shift = new int[]{-1, -1, -1, -1, 2, 4, 6, 8};
   static boolean tables_computed = false;
   static int[] volume_table = new int[16];
   int bank_offset;
   int command;
   char[] region;
   int region_base;
   int[] sample_data = new int[10000];
   ADPCMVoice[] voice = new ADPCMVoice[4];

   public OKIM6295(char[] var1, int var2) {
      this.region = var1;
      compute_tables();

      for(var2 = 0; var2 < 4; ++var2) {
         this.voice[var2] = new ADPCMVoice();
         this.voice[var2].volume = 255;
         this.reset_adpcm(this.voice[var2].adpcm);
      }

   }

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

   private void generate_adpcm(ADPCMVoice var1, int[] var2, int var3) {
      byte var5 = 0;
      int var4 = var3;
      int var6;
      int var12;
      if(var1.playing) {
         int var10 = this.region_base;
         int var11 = this.bank_offset;
         int var9 = var1.base_offset;
         var6 = var1.sample;
         int var8 = var1.count;
         var12 = 0;
         var4 = var3;

         int var13;
         label25: {
            for(var3 = var6; var4 != 0; var3 = var13) {
               char var7 = this.region[var3 / 2 + var10 + var11 + var9];
               var6 = var12 + 1;
               var2[var12] = this.clock_adpcm(var1.adpcm, var7 >> ((var3 & 1) << 2 ^ 4)) * var1.volume / 256;
               --var4;
               var13 = var3 + 1;
               if(var13 >= var8) {
                  var1.playing = false;
                  var3 = var6;
                  break label25;
               }

               var12 = var6;
            }

            var13 = var3;
            var3 = var12;
         }

         var1.sample = var13;
      } else {
         var3 = var5;
      }

      while(true) {
         var6 = var4 - 1;
         if(var4 == 0) {
            return;
         }

         var12 = var3 + 1;
         var2[var3] = 0;
         var4 = var6;
         var3 = var12;
      }
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
      super.clearBuffer();
      int var2 = 0;

      for(int var3 = 0; var3 < 4; ++var3) {
         ADPCMVoice var6 = this.voice[var3];

         int var4;
         for(int var1 = super.getBufferLength(); var1 != 0; var1 -= var4) {
            if(var1 > 10000) {
               var4 = 10000;
            } else {
               var4 = var1;
            }

            this.generate_adpcm(var6, this.sample_data, var4);

            for(int var5 = 0; var5 < var4; ++var2) {
               super.writeLinBuffer(var2, super.readLinBuffer(var2) + this.sample_data[var5]);
               ++var5;
            }
         }
      }

   }
}
