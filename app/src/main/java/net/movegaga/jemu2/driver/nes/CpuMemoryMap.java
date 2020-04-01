package net.movegaga.jemu2.driver.nes;

import jef.map.ReadWriteHandler;

public class CpuMemoryMap implements ReadWriteHandler {
   private ReadWriteHandler cart;
   private DMA dma;
   private Joystick joy1;
   private Joystick joy2;
   private PPU ppu;
   private APU sound;
   private char[] sysram;

   public CpuMemoryMap(NES var1) {
      this.sysram = var1.sysram;
      this.ppu = var1.ppu;
      this.joy1 = var1.joy1;
      this.joy2 = var1.joy2;
      this.cart = var1.cart;
      this.sound = var1.apu;
      this.dma = var1.dma;
   }

   public int read(int var1) {
      switch(61440 & var1) {
      case 0:
      case 4096:
         var1 = this.sysram[var1 & 2047];
         break;
      case 8192:
      case 12288:
         var1 = this.ppu.read(var1 & 7);
         break;
      case 16384:
         var1 -= 16384;
         if(var1 < 24) {
            if(var1 < 20) {
               var1 = this.sound.read(var1);
            } else if(var1 == 22) {
               var1 = this.joy1.read(var1);
            } else if(var1 == 23) {
               var1 = this.joy2.read(var1);
            } else {
               var1 = 255;
            }
         } else {
            var1 = this.cart.read(var1);
         }
         break;
      case 20480:
      case 24576:
      case 28672:
      case 32768:
      case 36864:
      case 40960:
      case 45056:
      case 49152:
      case 53248:
      case 57344:
      case 61440:
         var1 = this.cart.read(var1);
         break;
      default:
         var1 = 0;
      }

      return var1;
   }

   public void write(int var1, int var2) {
      switch(61440 & var1) {
      case 0:
      case 4096:
         this.sysram[var1 & 2047] = (char)var2;
         break;
      case 8192:
      case 12288:
         this.ppu.write(var1 & 7, var2);
         break;
      case 16384:
         var1 -= 16384;
         if(var1 < 24) {
            if(var1 < 20) {
               this.sound.write(var1, var2);
            } else if(var1 == 20) {
               this.dma.write(var1, var2);
            } else if(var1 == 21) {
               this.sound.write(var1, var2);
            } else if(var1 == 22) {
               this.joy1.write(var1, var2);
            } else if(var1 == 23) {
               this.joy2.write(var1, var2);
            }
         } else {
            this.cart.write(var1, var2);
         }
         break;
      case 20480:
      case 24576:
      case 28672:
      case 32768:
      case 36864:
      case 40960:
      case 45056:
      case 49152:
      case 53248:
      case 57344:
      case 61440:
         this.cart.write(var1, var2);
      }

   }
}
