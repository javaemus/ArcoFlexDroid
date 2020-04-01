package jef.sound.chip;

import jef.map.WriteHandler;
import jef.sound.SoundChip;

public class SN76496 extends SoundChip {
   private static final int FB_PNOISE = 65536;
   private static final int FB_WNOISE = 81922;
   private static final int MAX_OUTPUT = 16383;
   private static final int NG_PRESET = 3893;
   private static final int STEP = 65536;
   private int[] Count = new int[4];
   private int LastRegister;
   private int NoiseFB;
   private int[] Output = new int[4];
   private int[] Period = new int[4];
   private int RNG;
   private int[] Register = new int[8];
   private int UpdateStep;
   private int[] VolTable = new int[16];
   private int[] Volume = new int[4];
   private int clockFrequency;
   private int gain = 16;
   int[] vol = new int[4];
   int writePointer = 0;

   public SN76496(int var1) {
      this.clockFrequency = var1;
   }

   public int calcSample() {
      int var1;
      if(this.writePointer >= super.getBufferLength()) {
         var1 = super.readLinBuffer(this.writePointer - 1);
      } else {
         if(this.writePointer == 0) {
            this.clearBuffer();
         }

         int[] var3 = this.vol;
         int[] var5 = this.vol;
         int[] var4 = this.vol;
         this.vol[3] = 0;
         var4[2] = 0;
         var5[1] = 0;
         var3[0] = 0;

         for(var1 = 0; var1 < 3; ++var1) {
            if(this.Output[var1] != 0) {
               var3 = this.vol;
               var3[var1] += this.Count[var1];
            }

            var3 = this.Count;

            for(var3[var1] -= 65536; this.Count[var1] <= 0; var3[var1] += this.Period[var1]) {
               var3 = this.Count;
               var3[var1] += this.Period[var1];
               if(this.Count[var1] > 0) {
                  var3 = this.Output;
                  var3[var1] ^= 1;
                  if(this.Output[var1] != 0) {
                     var3 = this.vol;
                     var3[var1] += this.Period[var1];
                  }
                  break;
               }

               var3 = this.Count;
               var3[var1] += this.Period[var1];
               var3 = this.vol;
            }

            if(this.Output[var1] != 0) {
               var3 = this.vol;
               var3[var1] -= this.Count[var1];
            }
         }

         var1 = 65536;

         int var2;
         do {
            if(this.Count[3] < var1) {
               var2 = this.Count[3];
            } else {
               var2 = var1;
            }

            if(this.Output[3] != 0) {
               var3 = this.vol;
               var3[3] += this.Count[3];
            }

            var3 = this.Count;
            var3[3] -= var2;
            if(this.Count[3] <= 0) {
               if((this.RNG & 1) == 1) {
                  this.RNG ^= this.NoiseFB;
               }

               this.RNG >>= 1;
               this.Output[3] = this.RNG & 1;
               var3 = this.Count;
               var3[3] += this.Period[3];
               if(this.Output[3] != 0) {
                  var3 = this.vol;
                  var3[3] += this.Period[3];
               }
            }

            if(this.Output[3] != 0) {
               var3 = this.vol;
               var3[3] -= this.Count[3];
            }

            var2 = var1 - var2;
            var1 = var2;
         } while(var2 > 0);

         var2 = this.vol[0] * this.Volume[0] + this.vol[1] * this.Volume[1] + this.vol[2] * this.Volume[2] + this.vol[3] * this.Volume[3];
         var1 = var2;
         if(var2 > 1073676288) {
            var1 = 1073676288;
         }

         this.writeLinBuffer(this.writePointer, var1 / 65536);
         ++this.writePointer;
      }

      return var1;
   }

   public WriteHandler getWriteCommandHandler() {
      return new WriteCommand();
   }

   public void init(boolean var1, int var2, int var3) {
      super.init(var1, 22050, var3);
      this.UpdateStep = 1646264320 / (this.clockFrequency / 16);

      for(var2 = 0; var2 < 4; ++var2) {
         this.Volume[var2] = 0;
      }

      this.LastRegister = 0;

      for(var2 = 0; var2 < 8; var2 += 2) {
         this.Register[var2] = 0;
         this.Register[var2 + 1] = 15;
      }

      for(var2 = 0; var2 < 4; ++var2) {
         this.Output[var2] = 0;
         int[] var6 = this.Period;
         int[] var5 = this.Count;
         var3 = this.UpdateStep;
         var5[var2] = var3;
         var6[var2] = var3;
      }

      this.RNG = 3893;
      this.Output[3] = this.RNG & 1;
      this.gain &= 255;
      float var4 = 5461.0F;

      while(true) {
         var2 = this.gain;
         this.gain = var2 - 1;
         if(var2 <= 0) {
            for(var2 = 0; var2 < 15; ++var2) {
               if(var4 > 5461.0F) {
                  this.VolTable[var2] = 5461;
               } else {
                  this.VolTable[var2] = (int)var4;
               }

               var4 = (float)((double)var4 / 1.258925412D);
            }

            this.VolTable[15] = 0;
            return;
         }

         var4 = (float)((double)var4 * 1.023292992D);
      }
   }

   public void writeBuffer() {
      int var2 = super.getBufferLength();

      int var1;
      for(var1 = 0; var1 < 4; ++var1) {
         if(this.Volume[var1] == 0 && this.Count[var1] <= var2 * 65536) {
            int[] var3 = this.Count;
            var3[var1] += var2 * 65536;
         }
      }

      for(var1 = this.writePointer; var1 < var2; ++var1) {
         this.calcSample();
      }

      this.writePointer = 0;
   }

   public void writeCommand(int var1) {
      int var2;
      int var3;
      if((var1 & 128) != 0) {
         var3 = (var1 & 112) >> 4;
         var2 = var3 / 2;
         this.LastRegister = var3;
         this.Register[var3] = this.Register[var3] & 1008 | var1 & 15;
         switch(var3) {
         case 0:
         case 2:
         case 4:
            this.Period[var2] = this.UpdateStep * this.Register[var3];
            if(this.Period[var2] == 0) {
               this.Period[var2] = this.UpdateStep;
            }

            if(var3 == 4 && (this.Register[6] & 3) == 3) {
               this.Period[3] = this.Period[2] * 2;
            }
            break;
         case 1:
         case 3:
         case 5:
         case 7:
            this.Volume[var2] = this.VolTable[var1 & 15];
            break;
         case 6:
            var2 = this.Register[6];
            if((var2 & 4) != 0) {
               var1 = 81922;
            } else {
               var1 = 65536;
            }

            this.NoiseFB = var1;
            var1 = var2 & 3;
            if(var1 == 3) {
               this.Period[3] = this.Period[2] * 2;
            } else {
               this.Period[3] = this.UpdateStep << var1 + 5;
            }

            this.RNG = 3893;
            this.Output[3] = this.RNG & 1;
         }
      } else {
         var2 = this.LastRegister;
         var3 = var2 / 2;
         switch(var2) {
         case 0:
         case 2:
         case 4:
            this.Register[var2] = this.Register[var2] & 15 | (var1 & 63) << 4;
            this.Period[var3] = this.UpdateStep * this.Register[var2];
            if(this.Period[var3] == 0) {
               this.Period[var3] = this.UpdateStep;
            }

            if(var2 == 4 && (this.Register[6] & 3) == 3) {
               this.Period[3] = this.Period[2] * 2;
            }
         case 1:
         case 3:
         }
      }

   }

   public class WriteCommand implements WriteHandler {
      public void write(int var1, int var2) {
         SN76496.this.writeCommand(var2);
      }
   }
}
