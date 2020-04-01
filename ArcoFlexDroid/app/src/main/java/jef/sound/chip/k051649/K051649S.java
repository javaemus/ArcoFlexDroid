package jef.sound.chip.k051649;

import jef.map.WriteHandler;
import jef.sound.file.ChipFileWriter;
import jef.sound.file.Frame;

public class K051649S {
   static final byte PORT_FREQ = -95;
   static final byte PORT_KEY = -94;
   static final byte PORT_VOLUME = -96;
   static final byte PORT_WAVEFORM = 0;
   public K051649 channel1;
   public K051649 channel2;
   ChipFileWriter chipFile;
   Frame frame;
   public WriteHandler writeFrequency = new WriteFrequency();
   public WriteHandler writeKeyOnOff = new WriteKeyOnOff();
   public WriteHandler writeVolume = new WriteVolume();
   public WriteHandler writeWaveform = new WriteWaveform();

   public K051649S(int var1, float var2) {
      this.channel1 = new K051649(var1, var2, 0);
      this.channel2 = new K051649((int)((double)var1 * 1.01D), var2, 1);
   }

   public void K051649_frequency_w(int var1, int var2) {
      this.channel1.K051649_frequency_w(var1 & 255, var2);
      this.channel2.K051649_frequency_w(var1 & 255, var2);
   }

   public void K051649_keyonoff_w(int var1, int var2) {
      this.channel1.K051649_keyonoff_w(var1 & 255, var2);
      this.channel2.K051649_keyonoff_w(var1 & 255, var2);
   }

   public void K051649_volume_w(int var1, int var2) {
      this.channel1.K051649_volume_w(var1 & 255, var2);
      this.channel2.K051649_volume_w(var1 & 255, var2);
   }

   public void K051649_waveform_w(int var1, int var2) {
      this.channel1.K051649_waveform_w(var1 & 255, var2);
      this.channel2.K051649_waveform_w(var1 & 255, var2);
   }

   public void setChipFileWriter(ChipFileWriter var1) {
      this.chipFile = var1;
      this.frame = new Frame(4);
   }

   public void setSCCplus(boolean var1) {
      this.channel1.setSCCplus(var1);
      this.channel2.setSCCplus(var1);
   }

   class WriteFrequency implements WriteHandler {
      public void write(int var1, int var2) {
         K051649S.this.K051649_frequency_w(var1 & 255, var2);
      }
   }

   class WriteKeyOnOff implements WriteHandler {
      public void write(int var1, int var2) {
         K051649S.this.K051649_keyonoff_w(var1 & 255, var2);
      }
   }

   class WriteVolume implements WriteHandler {
      public void write(int var1, int var2) {
         K051649S.this.K051649_volume_w(var1 & 255, var2);
      }
   }

   class WriteWaveform implements WriteHandler {
      public void write(int var1, int var2) {
         K051649S.this.K051649_waveform_w(var1 & 255, var2);
      }
   }
}
