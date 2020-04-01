package jef.map;

public class Register implements ReadHandler, WriteHandler {
   private WriteHandler handler;
   public int value;

   public Register() {
      this.value = 0;
   }

   public Register(int var1) {
      this.value = var1;
   }

   public void insertWriteHandler(WriteHandler var1) {
      this.handler = var1;
   }

   public int read(int var1) {
      return this.value;
   }

   public void write(int var1, int var2) {
      this.value = var2;
      if(this.handler != null) {
         this.handler.write(0, var2);
      }

   }
}
