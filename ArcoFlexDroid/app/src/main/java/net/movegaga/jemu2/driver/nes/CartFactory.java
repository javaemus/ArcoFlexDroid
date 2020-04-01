package net.movegaga.jemu2.driver.nes;

import net.movegaga.jemu2.driver.nes.mappers.CartMapper;
import net.movegaga.jemu2.driver.nes.mappers.CartMapper00;

import jef.util.ZipLoader;

public class CartFactory {
   char[] header;
   char[] ram;
   char[] rom;
   char[] vrom;

   public CartFactory(char[] var1) {
      this.ram = var1;
   }

   private CartMapper createCart(int var1) {
      this.ram = new char[8192];
      CartMapper00 var2;
      switch(var1) {
      case 0:
         var2 = new CartMapper00(this.header, this.rom, this.vrom, this.ram);
         break;
      default:
         System.err.println("ERROR: Unknown mapper type!");
         var2 = null;
      }

      return var2;
   }

   private void load(String var1) {
      int var2;
      char var3;
      if(var1.startsWith("nes_antadv")) {
         var2 = -1650306380;
         var3 = 24592;
      } else if(var1.startsWith("nes_mario")) {
         var2 = -1495393451;
         var3 = 24592;
      } else if(var1.startsWith("nes_defender2")) {
         var2 = -1062032865;
         var3 = 24592;
      } else if(var1.startsWith("nes_burger")) {
         var2 = 1365592569;
         var3 = 24592;
      } else if(var1.startsWith("nes_supmario")) {
         var2 = 1365592569;
         var3 = 'ꀐ';
      } else {
         var2 = -1495393451;
         var3 = 24592;
      }

      char[] var6 = new char[var3];
      ZipLoader var7 = new ZipLoader(var1);
      var7.queue((char[])var6, var2, 0, var6.length);
      var7.load();
      this.header = new char[16];
      System.arraycopy(var6, 0, this.header, 0, this.header.length);
      int var4 = 0 + this.header.length;
      var3 = this.header[4];
      char var8 = var3;
      if(var3 == 0) {
         var8 = 1;
      }

      this.rom = new char[var8 * 16384];
      System.arraycopy(var6, var4, this.rom, 0, this.rom.length);
      int var5 = this.rom.length;
      System.out.println("NES ROM length  = " + this.rom.length);
      var3 = this.header[5];
      var8 = var3;
      if(var3 == 0) {
         var8 = 1;
      }

      this.vrom = new char[var8 * 8192];
      System.arraycopy(var6, var4 + var5, this.vrom, 0, this.vrom.length);
      System.out.println("NES VROM length = " + this.vrom.length);
   }

   public CartMapper createCart(String var1) {
      this.load(var1);
      int var2 = this.header[7] & 240 | this.header[6] >> 4 & 15;
      System.out.println("NES Mapper type = $" + Integer.toHexString(var2));
      return this.createCart(var2);
   }
}
