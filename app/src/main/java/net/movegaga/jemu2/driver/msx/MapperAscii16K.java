package net.movegaga.jemu2.driver.msx;

public class MapperAscii16K extends Mapper {
   int bank0 = 0;
   int bank1 = 1;
   private char[] cartMem;
   private char[] sram = new char[2048];

   public MapperAscii16K(char[] var1) {
      this.cartMem = var1;
   }

   public int read(int var1) {
      char var2;
      switch(var1 >> 14) {
      case 1:
         var2 = this.cartMem[this.bank0 * 16384 + (var1 & 16383)];
         break;
      case 2:
         if(this.bank1 == 16) {
            var2 = this.sram[var1 & 2047];
         } else {
            var2 = this.cartMem[this.bank1 * 16384 + (var1 & 16383)];
         }
         break;
      default:
         var2 = 255;
      }

      return var2;
   }

   public void write(int var1, int var2) {
      switch(var1 >> 14) {
      case 1:
         if(var1 >= 24576 && var1 < 28672) {
            this.bank0 = var2;
         } else if(var1 >= 28672 && var1 < '耀') {
            this.bank1 = var2;
         }
         break;
      case 2:
         this.sram[var1 & 2047] = (char)var2;
      }

   }
}
