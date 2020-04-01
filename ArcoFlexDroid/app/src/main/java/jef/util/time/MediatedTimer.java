package jef.util.time;

public class MediatedTimer {
   private long frame = 0L;
   private int framesToMediate;
   private float millis = 16.666F;
   private double prevTime = Time.getMillisDouble();

   public MediatedTimer(int var1) {
      if(!Time.nanoDisabled && Time.supportsNano) {
         this.framesToMediate = 1;
      } else {
         this.framesToMediate = var1;
      }

   }

   public float getMs() {
      long var3 = this.frame + 1L;
      this.frame = var3;
      if(var3 == (long)this.framesToMediate) {
         double var1 = Time.getMillisDouble();
         this.millis = (float)(var1 - this.prevTime) / (float)this.framesToMediate;
         this.prevTime = var1;
         this.frame = 0L;
      }

      return this.millis;
   }
}
