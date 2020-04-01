package jef.sound;

public class DelayLine {
   private int[] buffer;
   private int position;

   public DelayLine(int var1) {
      this.buffer = new int[var1];
      this.position = 0;
   }

   public int setNewAndGetDelayed(int var1) {
      int var3 = this.buffer[this.position];
      int[] var4 = this.buffer;
      int var2 = this.position;
      this.position = var2 + 1;
      var4[var2] = var1;
      this.position %= this.buffer.length;
      return var3;
   }
}
