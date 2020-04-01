package net.movegaga.jemu2.driver.msx;

public class MapperRAM extends Mapper {
   int[] offs = new int[4];
   int[] page = new int[4];
   int pageMask;
   char[] ram;

   public MapperRAM(char[] var1) {
      this.ram = var1;
      this.pageMask = this.ram.length / 16384 - 1;
   }

   public int getPage(int var1) {
      return this.page[var1];
   }

   public int read(int var1) {
      return this.ram[this.offs[var1 >> 14] + (var1 & 16383)];
   }

   public void setPage(int var1, int var2) {
      this.page[var1] = this.pageMask & var2;
      this.offs[var1] = this.page[var1] << 14;
   }

   public void write(int var1, int var2) {
      this.ram[this.offs[var1 >> 14] + (var1 & 16383)] = (char)var2;
   }
}
