package net.movegaga.jemu2.driver.arcade.pacman;

import jef.map.ReadHandler;

public class TheGlobDecoder implements ReadHandler {
   int counter = 10;
   char[] mem;
   int readOffset = 98304;

   public TheGlobDecoder(char[] var1) {
      this.mem = var1;
   }

   private void decrypt_rom_8(char[] var1) {
      for(int var2 = 0; var2 < 16384; ++var2) {
         char var3 = var1[var2];
         int var4 = ~var3;
         var1[65536 + var2] = (char)((var4 & 4) >> 1 | (var4 & 32) >> 5 | (var3 & 1) << 5 | (var3 & 2) << 1 | (var4 & 8) << 4 | (var4 & 16) >> 1 | (var4 & 64) >> 2 | (var4 & 128) >> 1);
      }

   }

   private void decrypt_rom_9(char[] var1) {
      for(int var2 = 0; var2 < 16384; ++var2) {
         char var3 = var1[var2];
         int var4 = ~var3;
         var1[81920 + var2] = (char)((var4 & 4) >> 1 | (var4 & 32) >> 5 | (var3 & 1) << 5 | (var4 & 2) << 6 | (var3 & 8) << 1 | (var4 & 16) >> 1 | (var4 & 64) >> 4 | (var4 & 128) >> 1);
      }

   }

   private void decrypt_rom_A(char[] var1) {
      for(int var2 = 0; var2 < 16384; ++var2) {
         char var4 = var1[var2];
         int var3 = ~var4;
         var1[98304 + var2] = (char)((var3 & 4) >> 1 | (var3 & 32) >> 5 | (var3 & 1) << 6 | (var4 & 2) << 1 | (var3 & 8) << 4 | (var3 & 16) << 1 | (var3 & 64) >> 2 | (var4 & 128) >> 4);
      }

   }

   private void decrypt_rom_B(char[] var1) {
      for(int var2 = 0; var2 < 16384; ++var2) {
         char var3 = var1[var2];
         int var4 = ~var3;
         var1[114688 + var2] = (char)((var4 & 4) >> 1 | (var4 & 32) >> 5 | (var4 & 1) << 6 | (var4 & 2) << 6 | (var3 & 8) << 1 | (var4 & 16) << 1 | (var4 & 64) >> 4 | (var3 & 128) >> 4);
      }

   }

   public ReadHandler getDecryptionSetter() {
      return new SetDecryption();
   }

   public void init() {
      System.out.println("Decrypting roms...");
      this.decrypt_rom_8(this.mem);
      this.decrypt_rom_9(this.mem);
      this.decrypt_rom_A(this.mem);
      this.decrypt_rom_B(this.mem);
      System.out.println("Ok");
   }

   public int read(int var1) {
      return this.mem[this.readOffset + var1];
   }

   public class SetDecryption implements ReadHandler {
      public int read(int var1) {
         if((var1 & 1) != 0) {
            TheGlobDecoder var2 = TheGlobDecoder.this;
            --var2.counter;
            if(TheGlobDecoder.this.counter < 0) {
               TheGlobDecoder.this.counter = 15;
            }
         } else {
            TheGlobDecoder.this.counter = TheGlobDecoder.this.counter + 1 & 15;
         }

         switch(TheGlobDecoder.this.counter) {
         case 8:
            TheGlobDecoder.this.readOffset = 65536;
            break;
         case 9:
            TheGlobDecoder.this.readOffset = 81920;
            break;
         case 10:
            TheGlobDecoder.this.readOffset = 98304;
            break;
         case 11:
            TheGlobDecoder.this.readOffset = 114688;
         }

         return 0;
      }
   }
}
