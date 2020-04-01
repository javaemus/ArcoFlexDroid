package net.movegaga.jemu2.driver.msx;

public class MapperRType extends Mapper {
   int bank0 = 23;
   int bank1 = 0;
   private char[] cartMem;

   public MapperRType(char[] var1) {
      this.cartMem = var1;
   }

   public int read(int var1) {
      char var2;
      switch(var1 >> 14) {
      case 1:
         var2 = this.cartMem[this.bank0 * 16384 + (var1 & 16383)];
         break;
      case 2:
         var2 = this.cartMem[this.bank1 * 16384 + (var1 & 16383)];
         break;
      default:
         var2 = 255;
      }

      return var2;
   }

   public void write(int var1, int var2) {
      if(var1 >= 28672 && var1 < '耀') {
         byte var3;
         if((var2 & 16) == 16) {
            var3 = 23;
         } else {
            var3 = 31;
         }

         this.bank1 = var2 & var3;
         System.out.println("bank1 = " + this.bank1);
      }

   }
}
