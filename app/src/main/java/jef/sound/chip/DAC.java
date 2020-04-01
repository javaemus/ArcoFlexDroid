package jef.sound.chip;

import jef.sound.SoundChip;

public class DAC extends SoundChip {
   int value;
   int writePointer = 0;

   public void calcSample() {
      if(this.writePointer < super.getBufferLength()) {
         this.writeLinBuffer(this.writePointer, this.value);
         ++this.writePointer;
      }

   }

   public void setAmplitude(int var1) {
      this.value = var1;
   }

   public void writeBuffer() {
      int var2 = super.getBufferLength();

      for(int var1 = this.writePointer; var1 < var2; ++var1) {
         this.calcSample();
      }

      this.writePointer = 0;
   }
}
