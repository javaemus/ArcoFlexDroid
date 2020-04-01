package jef.map;

public class WriteRegion {
   public int from;
   public WriteHandler handler;
   public int until;

   public WriteRegion(int var1, int var2, WriteHandler var3) {
      this.from = var1;
      this.until = var2;
      this.handler = var3;
   }
}
