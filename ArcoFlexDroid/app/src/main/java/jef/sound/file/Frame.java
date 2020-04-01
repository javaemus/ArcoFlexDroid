package jef.sound.file;

public class Frame {
   byte chip;
   byte port;
   long timestamp;
   byte value;

   public Frame(int var1) {
      this.chip = (byte)var1;
   }

   public void set(long var1, int var3, int var4) {
      this.timestamp = var1;
      this.port = (byte)var3;
      this.value = (byte)var4;
   }

   public String toString() {
      return "jef.sound.files.Frame chip=" + this.chip + ", timestamp=" + this.timestamp + ", port=" + this.port + ", value=" + this.value;
   }
}
