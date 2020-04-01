package net.movegaga.jemu2.driver.msx;

import jef.sound.chip.k051649.K051649;
import jef.sound.chip.k051649.K051649S;

public class MapperKonami8Ksccp extends Mapper {
   int bank0 = 0;
   int bank1 = 1;
   int bank2 = 2;
   int bank3 = 3;
   private char[] cartMem;
   private K051649 scc;
   int[] sccRAM = new int[160];
   private K051649S sccs;

   public MapperKonami8Ksccp(char[] var1, K051649 var2) {
      this.scc = var2;
      this.cartMem = var1;
   }

   public MapperKonami8Ksccp(char[] var1, K051649S var2) {
      this.sccs = var2;
      this.cartMem = var1;
   }

   public int read(int var1) {
      switch(var1 >> 14) {
      case 1:
         if(var1 < 24576) {
            var1 = this.cartMem[this.bank0 * 8192 + (var1 & 8191)];
         } else {
            var1 = this.cartMem[this.bank1 * 8192 + (var1 & 8191)];
         }
         break;
      case 2:
         if(var1 < 'ꀀ') {
            if(this.bank2 == 63) {
               var1 &= 255;
               if(var1 < 128) {
                  var1 = this.sccRAM[var1];
               } else {
                  var1 = 255;
               }
            } else {
               var1 = this.cartMem[this.bank2 * 8192 + (var1 & 8191)];
            }
         } else {
            var1 = this.cartMem[this.bank3 * 8192 + (var1 & 8191)];
         }
         break;
      default:
         var1 = 255;
      }

      return var1;
   }

   public void write(int var1, int var2) {
      switch(var1 >> 14) {
      case 1:
         if(var1 == 20480) {
            this.bank0 = var2;
         } else if(var1 == 28672) {
            this.bank1 = var2;
         }
         break;
      case 2:
         if(var1 == '退') {
            this.bank2 = var2;
         } else if(var1 == '뀀') {
            this.bank3 = var2;
         } else if(this.bank3 == 128 && var1 >= '렀' && var1 <= '뿿') {
            var1 &= 255;
            if(var1 < 160) {
               if(this.scc != null) {
                  this.scc.K051649_waveform_w(var1 - 0, var2);
               } else {
                  this.sccs.K051649_waveform_w(var1 - 0, var2);
               }

               this.sccRAM[var1] = var2;
            } else if(var1 < 170) {
               if(this.scc != null) {
                  this.scc.K051649_frequency_w(var1 - 160, var2);
               } else {
                  this.sccs.K051649_frequency_w(var1 - 160, var2);
               }
            } else if(var1 < 175) {
               if(this.scc != null) {
                  this.scc.K051649_volume_w(var1 - 170, var2);
               } else {
                  this.sccs.K051649_volume_w(var1 - 170, var2);
               }
            } else if(var1 == 175) {
               if(this.scc != null) {
                  this.scc.K051649_keyonoff_w(var1 - 175, var2);
               } else {
                  this.sccs.K051649_keyonoff_w(var1 - 175, var2);
               }
            } else if(var1 >= 154) {
               ;
            }
         }
      }

   }
}
