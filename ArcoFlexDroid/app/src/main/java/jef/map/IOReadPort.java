package jef.map;

public class IOReadPort implements ReadHandler {
   static final boolean debug = false;
   private UndefinedRead defread = new UndefinedRead();
   private int mask = 255;
   private ReadHandler[] readMap;
   private int size;

   public IOReadPort() {
      this.size = 256;
      this.readMap = new ReadHandler[this.size];
      this.set(0, this.size - 1, this.defread);
   }

   public IOReadPort(int var1) {
      this.size = var1;
      this.readMap = new ReadHandler[var1];
      this.set(0, var1 - 1, this.defread);
   }

   public ReadHandler[] get() {
      return this.readMap;
   }

   public int getSize() {
      return this.size;
   }

   public int read(int var1) {
      return this.readMap[var1].read(var1);
   }

   public void set(int var1, int var2, ReadHandler var3) {
      for(int var4 = 0; var4 < this.readMap.length; ++var4) {
         int var5 = var4 & this.mask;
         if(var5 >= var1 && var5 <= var2) {
            this.readMap[var4] = var3;
         }
      }

   }

   public void setLineMask(int var1) {
      this.mask = var1;
   }

   public class UndefinedRead implements ReadHandler {
      public int read(int var1) {
         return 0;
      }
   }
}
