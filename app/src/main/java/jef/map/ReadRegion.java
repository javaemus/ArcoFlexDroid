package jef.map;

public class ReadRegion {
   public int from;
   public ReadHandler handler;
   public char[] memory;
   public int until;

   public ReadRegion(int var1, int var2, ReadHandler var3) {
      this.from = var1;
      this.until = var2;
      this.handler = var3;
   }
}
