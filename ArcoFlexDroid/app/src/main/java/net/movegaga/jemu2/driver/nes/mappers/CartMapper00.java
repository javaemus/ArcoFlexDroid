package net.movegaga.jemu2.driver.nes.mappers;

public class CartMapper00 implements CartMapper {
   char[] header;
   char[] ram;
   char[] rom;
   char[] vrom;

   public CartMapper00(char[] var1, char[] var2, char[] var3, char[] var4) {
      this.header = var1;
      this.rom = var2;
      this.vrom = var3;
      this.ram = var4;
   }

   public int read(int var1) {
      char var2;
      if(var1 >= '耀') {
         var2 = this.rom[this.rom.length - 1 & var1];
      } else if(var1 >= 24576) {
         var2 = this.ram[var1 & 8191];
      } else {
         var2 = 255;
      }

      return var2;
   }

   public int readVROM(int var1) {
      return this.vrom[var1];
   }

   public void write(int var1, int var2) {
      if(var1 >= 24576 && var1 < '耀') {
         this.ram[var1 & 8191] = (char)var2;
      }

   }
}
