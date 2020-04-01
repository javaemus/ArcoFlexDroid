package net.movegaga.jemu2.driver.msx;

public class MapperKonami8K extends Mapper {
   int bank0 = 0;
   int bank1 = 1;
   int bank2 = 2;
   int bank3 = 3;
   private char[] cartMem;

   public MapperKonami8K(char[] var1) {
      this.cartMem = var1;
   }

   public int read(int var1) {
      char var2;
      switch(var1 >> 14) {
      case 1:
         if(var1 < 24576) {
            var2 = this.cartMem[this.bank0 * 8192 + (var1 & 8191)];
         } else {
            var2 = this.cartMem[this.bank1 * 8192 + (var1 & 8191)];
         }
         break;
      case 2:
         if(var1 < 'ꀀ') {
            var2 = this.cartMem[this.bank2 * 8192 + (var1 & 8191)];
         } else {
            var2 = this.cartMem[this.bank3 * 8192 + (var1 & 8191)];
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
         if(var1 == 24576) {
            this.bank1 = var2;
         }
         break;
      case 2:
         if(var1 == '耀') {
            this.bank2 = var2;
         } else if(var1 == 'ꀀ') {
            this.bank3 = var2;
         }
      }

   }
}
