package jef.sound;

import jef.util.time.MediatedTimer;

public class SoundSystem {
   SoundChip[] sc;
   MediatedTimer timer;

   public void init(SoundChip[] var1, int var2, int var3, int var4) {
      this.sc = var1;
      this.timer = new MediatedTimer(20);
      System.out.println("SoundChip emulators found:" + this.sc.length);

      for(var3 = 0; var3 < this.sc.length; ++var3) {
         this.sc[var3].init(true, var2, var4);
      }

   }

   public void update() {
      if(this.sc != null) {
         float var1 = this.timer.getMs();

         for(int var2 = 0; var2 < this.sc.length; ++var2) {
            this.sc[var2].update(var1);
         }
      }

   }
}
