package jef.sound.chip;

import jef.map.ReadHandler;
import jef.map.WriteHandler;
import jef.sound.SoundChip;

public class SegaPCM extends SoundChip implements ReadHandler, WriteHandler {
   public static final int BANK_12M = 13;
   public static final int BANK_256 = 11;
   public static final int BANK_512 = 12;
   public static final int BANK_MASK7 = 7340032;
   public static final int BANK_MASKF = 15728640;
   public static final int BANK_MASKF8 = 16252928;
   int bankmask;
   int bankshift;
   char[] low;
   int max_addr;
   char[] ram;
   char[] rom;

   public SegaPCM(int var1, char[] var2, int var3) {
      this.rom = var2;
      this.ram = new char[256];
      this.max_addr = var2.length;
      this.bankshift = var3 & 255;
      this.low = new char[16];
      var3 >>>= 16;
      var1 = var3;
      if(var3 == 0) {
         var1 = 112;
      }

      for(var3 = 1; var3 < var2.length; var3 *= 2) {
         ;
      }

      this.bankmask = var3 - 1 >>> this.bankshift & var1;

      for(var1 = 0; var1 < this.ram.length; ++var1) {
         this.ram[var1] = 255;
      }

   }

   public void init(boolean var1, int var2, int var3) {
      super.init(false, 32000, var3);
   }

   public int read(int var1) {
      return this.ram[var1 & 255];
   }

   public void write(int var1, int var2) {
      this.ram[var1 & 255] = (char)var2;
   }

   public void writeBuffer() {
      super.clearBuffer();

      for(int var2 = 0; var2 < 16; ++var2) {
         if((this.ram[var2 * 8 + 134] & 1) == 0) {
            int var7 = var2 * 8;
            char var5 = this.ram[var7 + 134];
            int var11 = (this.bankmask & var5) << this.bankshift;
            int var1 = this.ram[var7 + 5] << 16 | this.ram[var7 + 4] << 8 | this.low[var2];
            char var9 = this.ram[var7 + 133];
            char var14 = this.ram[var7 + 132];
            char var10 = this.ram[var7 + 6];
            char var12 = this.ram[var7 + 7];
            char var13 = this.ram[var7 + 2];
            char var8 = this.ram[var7 + 3];
            int var3 = 0;

            while(true) {
               if(var3 >= super.getBufferLength()) {
                  var3 = var5;
                  break;
               }

               byte var6 = 0;
               int var4 = var1;
               if(var1 >>> 16 == var10 + 1) {
                  if((var5 & 2) != 0) {
                     var3 = var5 | 1;
                     break;
                  }

                  var4 = (var9 << 8 | var14) << 8;
               }

               var1 = var6;
               if((var4 >>> 8) + var11 < this.max_addr) {
                  var1 = this.rom[(var4 >>> 8) + var11] - 128;
               }

               super.writeLinBuffer(0, var3, super.readLinBuffer(0, var3) + var1 * var13);
               super.writeLinBuffer(1, var3, super.readLinBuffer(1, var3) + var1 * var8);
               var1 = var4 + var12;
               ++var3;
            }

            this.ram[var7 + 134] = (char)var3;
            this.ram[var7 + 4] = (char)(var1 >>> 8);
            this.ram[var7 + 5] = (char)(var1 >>> 16);
            char[] var15 = this.low;
            if((var3 & 1) != 0) {
               var1 = 0;
            } else {
               var1 &= 255;
            }

            var15[var2] = (char)var1;
         }
      }

   }
}
