package net.movegaga.jemu2.driver.msx;

public class MapperMirrored extends Mapper {
   private char[] cartMem;

   public MapperMirrored(char[] var1) {
      this.cartMem = var1;
   }

   public int read(int var1) {
      return this.cartMem[var1];
   }

   public void write(int var1, int var2) {
   }
}
