package net.movegaga.jemu2.driver.msx;

public class MapperAscii8K extends Mapper {
   int bank0 = 0;
   int bank1 = 1;
   int bank2 = 2;
   int bank3 = 3;
   private char[] cartMem;
   int pages;
   private char[] sram = new char[8192];

   public MapperAscii8K(char[] var1) {
      this.cartMem = var1;
   }

   public int read(int var1) {
      char var2;
      switch(var1 >> 14) {
      case 1:
         if(var1 < 24576) {
            if(this.bank0 >= this.pages) {
               var2 = this.sram[var1 & 8191];
            } else {
               var2 = this.cartMem[this.bank0 * 8192 + (var1 & 8191)];
            }
         } else if(this.bank1 >= this.pages) {
            var2 = this.sram[var1 & 8191];
         } else {
            var2 = this.cartMem[this.bank1 * 8192 + (var1 & 8191)];
         }
         break;
      case 2:
         if(var1 < 'ꀀ') {
            if(this.bank2 >= this.pages) {
               var2 = this.sram[var1 & 8191];
            } else {
               var2 = this.cartMem[this.bank2 * 8192 + (var1 & 8191)];
            }
         } else if(this.bank3 >= this.pages) {
            var2 = this.sram[var1 & 8191];
         } else {
            var2 = this.cartMem[this.bank3 * 8192 + (var1 & 8191)];
         }
         break;
      default:
         var2 = 255;
      }

      return var2;
   }

   public void setSize(int var1) {
      this.pages = var1 / 8192;
   }

   public void write(int var1, int var2) {
      if(var1 >= 24576 && var1 < 26624) {
         this.bank0 = var2;
      } else if(var1 >= 26624 && var1 < 28672) {
         this.bank1 = var2;
      } else if(var1 >= 28672 && var1 < 30720) {
         this.bank2 = var2;
      } else if(var1 >= 30720 && var1 < '耀') {
         this.bank3 = var2;
      } else if(this.bank2 >= this.pages && var1 >= '耀' && var1 < 'ꀀ') {
         this.sram[var1 & 8191] = (char)var2;
      } else if(this.bank3 >= this.pages && var1 >= 'ꀀ' && var1 < '쀀') {
         this.sram[var1 & 8191] = (char)var2;
      }

   }
}
