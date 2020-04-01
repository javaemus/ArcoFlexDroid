package jef.sound.chip.ym2413;

import jef.map.WriteHandler;

public class YM2413S {
   WriteHandler c1Data;
   WriteHandler c1Reg;
   WriteHandler c2Data;
   WriteHandler c2Reg;
   public YM2413 channel1;
   public YM2413 channel2;

   public YM2413S(int var1) {
      this.channel1 = new YM2413(var1, 0);
      this.channel2 = new YM2413((int)((double)var1 * 1.01D), 1);
      this.c1Reg = this.channel1.getRegisterPortWrite();
      this.c2Reg = this.channel2.getRegisterPortWrite();
      this.c1Data = this.channel1.getDataPortWrite();
      this.c2Data = this.channel2.getDataPortWrite();
   }

   public WriteHandler getDataPortWrite() {
      return new WriteHandler() {
         public void write(int var1, int var2) {
            YM2413S.this.c1Data.write(var1, var2);
            YM2413S.this.c2Data.write(var1, var2);
         }
      };
   }

   public WriteHandler getRegisterPortWrite() {
      return new WriteHandler() {
         public void write(int var1, int var2) {
            YM2413S.this.c1Reg.write(var1, var2);
            YM2413S.this.c2Reg.write(var1, var2);
         }
      };
   }
}
