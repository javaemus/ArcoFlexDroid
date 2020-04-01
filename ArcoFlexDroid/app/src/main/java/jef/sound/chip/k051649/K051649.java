package jef.sound.chip.k051649;

import java.io.PrintStream;

import jef.map.WriteHandler;
import jef.sound.DelayLine;
import jef.sound.Settings;
import jef.sound.SoundChip;

public class K051649 extends SoundChip {
   private static final int DELAY_MS = 20;
   public static final int FREQBASEBITS = 16;
   static final int SHIFT_FREQBASEBITS = 65536;
   private static final int VOICES = 5;
   private boolean DELAY;
   SoundChannel[] channels = new SoundChannel[5];
   DelayLine[] delay;
   int[] f = new int[10];
   boolean isSCCplus = false;
   final int lr;
   int mclock;
   int[] mixer_buffer;
   int mixer_lookup;
   int[] mixer_table;
   boolean mono = true;
   private float mvolume;
   int rate;
   int stream;
   public WriteHandler writeFrequency = new WriteFrequency();
   public WriteHandler writeKeyOnOff = new WriteKeyOnOff();
   public WriteHandler writeVolume = new WriteVolume();
   public WriteHandler writeWaveform = new WriteWaveform();

   public K051649(int var1, float var2) {
      this.mono = true;
      this.DELAY = false;
      this.lr = 0;
      this.mclock = var1;
      this.mvolume = var2;

      for(var1 = 0; var1 < 5; ++var1) {
         this.channels[var1] = new SoundChannel();
      }

      this.init();
   }

   public K051649(int var1, float var2, int var3) {
      this.mono = false;
      this.DELAY = Settings.STEREO_DELAY;
      this.lr = var3;
      this.mclock = var1;
      this.mvolume = var2;

      for(var1 = 0; var1 < 5; ++var1) {
         this.channels[var1] = new SoundChannel();
      }

      if(this.DELAY) {
         this.delay = new DelayLine[3];
         var3 = Settings.SOUND_SAMPLING_FREQ / 1000;

         for(var1 = 0; var1 < 3; ++var1) {
            this.delay[var1] = new DelayLine((var1 + 1) * var3 * 20);
         }
      }

      this.init();
   }

   private void init() {
      SoundChannel[] var2 = this.channels;
      this.rate = Settings.SOUND_SAMPLING_FREQ;
      this.mixer_buffer = new int[this.rate * 2];
      this.make_mixer_table(5);

      for(int var1 = 0; var1 < 5; ++var1) {
         var2[var1].frequency = 0;
         var2[var1].volume = 0;
         var2[var1].counter = 0L;
      }

   }

   private void make_mixer_table(int var1) {
      this.mixer_table = new int[var1 * 512];
      this.mixer_lookup = var1 * 256;

      for(int var2 = 0; var2 < var1 * 256; ++var2) {
         int var4 = var2 * 8 * 16 / var1;
         int var3 = var4;
         if(var4 > 32767) {
            var3 = 32767;
         }

         this.mixer_table[this.mixer_lookup + var2] = var3;
         this.mixer_table[this.mixer_lookup - var2] = -var3;
      }

   }

   public void K051649_frequency_w(int var1, int var2) {
      this.f[var1] = var2;
      this.channels[var1 >> 1].frequency = this.f[var1 & 14] + (this.f[var1 | 1] << 8) & 32767;
   }

   public void K051649_keyonoff_w(int var1, int var2) {
      SoundChannel var4 = this.channels[0];
      boolean var3;
      if((var2 & 1) != 0) {
         var3 = true;
      } else {
         var3 = false;
      }

      var4.key = var3;
      var4 = this.channels[1];
      if((var2 & 2) != 0) {
         var3 = true;
      } else {
         var3 = false;
      }

      var4.key = var3;
      var4 = this.channels[2];
      if((var2 & 4) != 0) {
         var3 = true;
      } else {
         var3 = false;
      }

      var4.key = var3;
      var4 = this.channels[3];
      if((var2 & 8) != 0) {
         var3 = true;
      } else {
         var3 = false;
      }

      var4.key = var3;
      var4 = this.channels[4];
      if((var2 & 16) != 0) {
         var3 = true;
      } else {
         var3 = false;
      }

      var4.key = var3;
   }

   public void K051649_volume_w(int var1, int var2) {
      this.channels[var1 & 7].volume = var2 & 15;
   }

   public void K051649_waveform_w(int var1, int var2) {
      int var3 = var1 >> 5;
      this.channels[var3].waveform[var1 & 31] = var2;
      if(var3 == 3 && !this.isSCCplus) {
         this.channels[4].waveform[var1 & 31] = var2;
      }

   }

   public void init(boolean var1, int var2, int var3) {
      super.init(this.mono, var2, var3);
      PrintStream var4 = System.out;
      StringBuilder var5 = new StringBuilder("SCC set to stereo = ");
      if(this.mono) {
         var1 = false;
      } else {
         var1 = true;
      }

      var4.println(var5.append(var1).toString());
   }

   public void setSCCplus(boolean var1) {
      this.isSCCplus = var1;
   }

   public void writeBuffer() {
      SoundChannel[] var11 = this.channels;

      int var2;
      for(var2 = 0; var2 < super.getBufferLength(); ++var2) {
         this.mixer_buffer[var2] = 0;
      }

      int var3;
      for(var3 = 0; var3 < 5; ++var3) {
         int var7 = var11[var3].volume;
         var2 = var11[var3].frequency;
         boolean var10 = var11[var3].key;
         if(var7 + var2 != 0 && var10) {
            int[] var13 = var11[var3].waveform;
            long var8 = var11[var3].counter;
            int var4 = 0;
            float var1 = (float)this.mclock / (float)(var2 * 16);

            for(int var5 = 0; var5 < super.getBufferLength(); ++var4) {
               var8 += (long)(var1 * 65536.0F / (float)(this.rate >> 5));
               int var6 = (byte)var13[(int)var8 >> 16 & 31] * var7 >> 3;
               var2 = var6;
               if(!this.mono) {
                  var2 = var6;
                  if((this.lr + var3 & 1) == 1) {
                     var6 >>= 1;
                     var2 = var6;
                     if(this.DELAY) {
                        var2 = this.delay[var3 / 2].setNewAndGetDelayed(var6);
                     }
                  }
               }

               int[] var12 = this.mixer_buffer;
               var12[var4] += var2;
               ++var5;
            }

            var11[var3].counter = var8;
         }
      }

      var2 = 0;

      for(var3 = 0; var3 < super.getBufferLength(); ++var3) {
         if(this.mono) {
            this.writeLinBuffer(var3, (int)(this.mvolume * (float)this.mixer_table[this.mixer_lookup + this.mixer_buffer[var2]]));
            ++var2;
         } else {
            this.writeLinBuffer(this.lr, var3, (int)(this.mvolume * (float)this.mixer_table[this.mixer_lookup + this.mixer_buffer[var2]]));
            ++var2;
         }
      }

   }

   class SoundChannel {
      long counter;
      int frequency;
      boolean key;
      int volume;
      int[] waveform = new int[32];
   }

   class WriteFrequency implements WriteHandler {
      public void write(int var1, int var2) {
         K051649.this.K051649_frequency_w(var1 & 255, var2);
      }
   }

   class WriteKeyOnOff implements WriteHandler {
      public void write(int var1, int var2) {
         K051649.this.K051649_keyonoff_w(var1 & 255, var2);
      }
   }

   class WriteVolume implements WriteHandler {
      public void write(int var1, int var2) {
         K051649.this.K051649_volume_w(var1 & 255, var2);
      }
   }

   class WriteWaveform implements WriteHandler {
      public void write(int var1, int var2) {
         K051649.this.K051649_waveform_w(var1 & 255, var2);
      }
   }
}
