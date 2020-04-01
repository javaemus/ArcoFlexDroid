package jef.sound.chip;

import jef.map.WriteHandler;
import jef.sound.SoundChip;

public class Namco extends SoundChip {
   static final int WAV_LENGTH = 32;
   static final char[] sound_prom;
   SoundChannel[] channel;
   int clockFreq;
   float freqDivider;
   char[] mem;
   int numChannels;
   private char[] prom;
   float[] pwavPointer;

   static {
      char[] var0 = new char[]{'\u0007', '\t', '\n', '\u000b', '\f', '\r', '\r', '\u000e', '\u000e', '\u000e', '\r', '\r', '\f', '\u000b', '\n', '\t', '\u0007', '\u0005', '\u0004', '\u0003', '\u0002', '\u0001', '\u0001', '\u0000', '\u0000', '\u0000', '\u0001', '\u0001', '\u0002', '\u0003', '\u0004', '\u0005', '\u0007', '\f', '\u000e', '\u000e', '\r', '\u000b', '\t', '\n', '\u000b', '\u000b', '\n', '\t', '\u0006', '\u0004', '\u0003', '\u0005', '\u0007', '\t', '\u000b', '\n', '\b', '\u0005', '\u0004', '\u0003', '\u0003', '\u0004', '\u0005', '\u0003', '\u0001', '\u0000', '\u0000', '\u0002', '\u0007', '\n', '\f', '\r', '\u000e', '\r', '\f', '\n', '\u0007', '\u0004', '\u0002', '\u0001', '\u0000', '\u0001', '\u0002', '\u0004', '\u0007', '\u000b', '\r', '\u000e', '\r', '\u000b', '\u0007', '\u0003', '\u0001', '\u0000', '\u0001', '\u0003', '\u0007', '\u000e', '\u0007', '\u0000', '\u0007', '\r', '\u000b', '\b', '\u000b', '\r', '\t', '\u0006', '\u000b', '\u000e', '\f', '\u0007', '\t', '\n', '\u0006', '\u0002', '\u0007', '\f', '\b', '\u0004', '\u0005', '\u0007', '\u0002', '\u0000', '\u0003', '\b', '\u0005', '\u0001', '\u0003', '\u0006', '\u0003', '\u0001', '\u0000', '\b', '\u000f', '\u0007', '\u0001', '\b', '\u000e', '\u0007', '\u0002', '\b', '\r', '\u0007', '\u0003', '\b', '\f', '\u0007', '\u0004', '\b', '\u000b', '\u0007', '\u0005', '\b', '\n', '\u0007', '\u0006', '\b', '\t', '\u0007', '\u0007', '\b', '\b', '\u0007', '\u0007', '\b', '\u0006', '\t', '\u0005', '\n', '\u0004', '\u000b', '\u0003', '\f', '\u0002', '\r', '\u0001', '\u000e', '\u0000', '\u000f', '\u0000', '\u000f', '\u0001', '\u000e', '\u0002', '\r', '\u0003', '\f', '\u0004', '\u000b', '\u0005', '\n', '\u0006', '\t', '\u0007', '\b', '\u0000', '\u0001', '\u0002', '\u0003', '\u0004', '\u0005', '\u0006', '\u0007', '\b', '\t', '\n', '\u000b', '\f', '\r', '\u000e', '\u000f', '\u000f', '\u000e', '\r', '\f', '\u000b', '\n', '\t', '\b', '\u0007', '\u0006', '\u0005', '\u0004', '\u0003', '\u0002', '\u0001', '\u0000', '\u0000', '\u0001', '\u0002', '\u0003', '\u0004', '\u0005', '\u0006', '\u0007', '\b', '\t', '\n', '\u000b', '\f', '\r', '\u000e', '\u000f', '\u0000', '\u0001', '\u0002', '\u0003', '\u0004', '\u0005', '\u0006', '\u0007', '\b', '\t', '\n', '\u000b', '\f', '\r', '\u000e', '\u000f'};
      sound_prom = var0;
   }

   public Namco(char[] var1, int var2, int var3) {
      this.prom = sound_prom;
      this.mem = var1;
      this.numChannels = var2;
      this.channel = new SoundChannel[var2];
      this.clockFreq = var3;
      this.pwavPointer = new float[var2];

      for(var3 = 0; var3 < var2; ++var3) {
         this.channel[var3] = new SoundChannel();
      }

   }

   public Namco(char[] var1, int var2, int var3, char[] var4) {
      this.prom = var4;
      this.mem = var1;
      this.numChannels = var2;
      this.channel = new SoundChannel[var2];
      this.clockFreq = var3;
      this.pwavPointer = new float[var2];

      for(var3 = 0; var3 < var2; ++var3) {
         this.channel[var3] = new SoundChannel();
      }

   }

   public WriteHandler getPengoInterface(int var1) {
      return new PengoInterface(var1);
   }

   public void init(boolean var1, int var2, int var3) {
      this.freqDivider = (float)(this.clockFreq / var2);
      this.freqDivider *= (float)var2 / 8000.0F;
      super.init(var1, var2, var3);
   }

   public void writeBuffer() {
      this.clearBuffer();

      int var2;
      for(var2 = 0; var2 < this.numChannels; ++var2) {
         int var3 = this.channel[var2].frequency;
         int var5 = this.channel[var2].volume << 8;
         int var4 = this.channel[var2].wav;
         var3 = (int)((float)var3 / this.freqDivider);
         if(var3 > 0 && var5 > 0) {
            float var1 = (float)var3 * 32.0F / (float)super.getSampFreq();

            for(var3 = 0; var3 < super.getBufferLength(); ++var3) {
               char var6 = this.prom[var4 * 32 + (int)this.pwavPointer[var2]];
               this.writeLinBuffer(var3, this.readLinBuffer(var3) + ((var6 & 15) - 8) * var5);
               this.pwavPointer[var2] = (this.pwavPointer[var2] + var1) % 32.0F;
            }
         }
      }

      for(var2 = 0; var2 < super.getBufferLength(); ++var2) {
         this.writeLinBuffer(var2, this.readLinBuffer(var2) / this.numChannels);
      }

   }

   public class PengoInterface implements WriteHandler {
      int base;

      public PengoInterface(int var2) {
         this.base = var2;
      }

      public void write(int var1, int var2) {
         if(Namco.this.mem[var1] != (var2 & 15)) {
            Namco.this.mem[var1] = (char)(var2 & 15);

            for(var1 = 0; var1 < Namco.this.numChannels; ++var1) {
               int var3 = var1 * 5 + this.base;
               var2 = ((Namco.this.mem[var3 + 20] << 4 | Namco.this.mem[var3 + 19]) << 4 | Namco.this.mem[var3 + 18]) << 4 | Namco.this.mem[var3 + 17];
               if(var1 == 0) {
                  var2 = var2 << 4 | Namco.this.mem[var3 + 16];
               } else {
                  var2 <<= 4;
               }

               Namco.this.channel[var1].frequency = var2;
               Namco.this.channel[var1].volume = Namco.this.mem[var3 + 21] & 15;
               Namco.this.channel[var1].wav = Namco.this.mem[var3 + 5] & 7;
            }
         }

      }
   }

   class SoundChannel {
      int frequency;
      int volume;
      int wav;
   }
}
