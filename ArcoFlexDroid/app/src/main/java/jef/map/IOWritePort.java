package jef.map;

public class IOWritePort implements WriteHandler {
   static final boolean debug = false;
   private UndefinedWrite defwrite = new UndefinedWrite();
   private int mask = 255;
   private int size;
   private WriteHandler[] writeMap;

   public IOWritePort() {
      this.size = 256;
      this.writeMap = new WriteHandler[this.size];
      this.set(0, this.size - 1, this.defwrite);
   }

   public IOWritePort(int var1) {
      this.size = var1;
      this.writeMap = new WriteHandler[var1];
      this.set(0, var1 - 1, this.defwrite);
   }

   public int getSize() {
      return this.size;
   }

   public void set(int var1, int var2, WriteHandler var3) {
      for(int var4 = 0; var4 < this.writeMap.length; ++var4) {
         int var5 = var4 & this.mask;
         if(var5 >= var1 && var5 <= var2) {
            this.writeMap[var4] = var3;
         }
      }

   }

   public void setLineMask(int var1) {
      this.mask = var1;
   }

   public void write(int var1, int var2) {
      this.writeMap[var1].write(var1, var2);
   }

   public class UndefinedWrite implements WriteHandler {
      public void write(int var1, int var2) {
      }
   }
}
