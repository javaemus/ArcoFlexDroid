package net.movegaga.jemu2.driver.arcade.galaga;

import jef.machine.BasicEmulator;
import jef.machine.Emulator;
import jef.map.InterruptHandler;
import jef.map.ReadHandler;
import jef.map.WriteHandler;
import jef.video.BitMap;

public class MGalaga extends BasicEmulator implements Emulator {
   int coininserted = 0;
   int coinpercred = 0;
   int credits = 0;
   int credpercoin = 0;
   int[] customio = new int[16];
   int customio_command = 0;
   Galaga driver;
   boolean halt23 = false;
   boolean interrupt_enable_1 = false;
   boolean interrupt_enable_2 = false;
   boolean interrupt_enable_3 = false;
   public char[] mem_shared;
   int mode = 0;
   boolean nmi_timer = false;
   VGalaga video;

   public InterruptHandler galaga_interrupt_1() {
      return new Galaga_interrupt_1();
   }

   public InterruptHandler galaga_interrupt_2() {
      return new Galaga_interrupt_2();
   }

   public InterruptHandler galaga_interrupt_3() {
      return new Galaga_interrupt_3();
   }

   public ReadHandler readCustomIO() {
      return new Galaga_customio_r();
   }

   public ReadHandler readCustomIOData() {
      return new Galaga_customio_data_r();
   }

   public ReadHandler readDips() {
      return new Galaga_dsw_r();
   }

   public ReadHandler readSharedRAM() {
      return new Galaga_sharedram_r();
   }

   public final int readinputport(int var1) {
      switch(var1) {
      case 0:
         var1 = 255;
         break;
      case 1:
         var1 = 255;
         break;
      case 2:
         var1 = this.driver.in[2].read(0);
         this.md.input[2].write(this.md.input[2].read(0) | 16);
         break;
      case 3:
         var1 = this.driver.in[2].read(0);
         this.md.input[2].write(this.md.input[2].read(0) | 16);
         break;
      case 4:
         var1 = this.driver.in[4].read(0);
         this.md.input[4].write(255);
         break;
      default:
         var1 = 255;
      }

      return var1;
   }

   public BitMap refresh(boolean var1) {
      if(var1) {
         this.backBuffer = this.getDisplay();
      }

      for(int var2 = 0; var2 < 99; ++var2) {
         this.cd[0].cpu.exec(526);
         if(!this.halt23) {
            this.cd[1].cpu.exec(526);
            this.cd[2].cpu.exec(526);
         }

         if(var2 == 50 || var2 == 0) {
            if(this.nmi_timer) {
               this.cd[0].cpu.interrupt(1, true);
            }

            if(this.interrupt_enable_3 && var2 == 50) {
               this.cd[2].cpu.interrupt(1, true);
            }
         }
      }

      if(this.interrupt_enable_1) {
         this.cd[0].cpu.interrupt(0, true);
      }

      if(this.interrupt_enable_2) {
         this.cd[1].cpu.interrupt(0, true);
      }

      if(this.interrupt_enable_3) {
         this.cd[2].cpu.interrupt(1, true);
      }

      this.updateInput();
      this.se.update();
      this.video.updateStars();
      this.highScoreHandler.update();
      return this.backBuffer;
   }

   public void setRefs(char[] var1, Galaga var2, VGalaga var3) {
      this.mem_shared = var1;
      this.driver = var2;
      this.video = var3;
   }

   public WriteHandler writeCustomIO() {
      return new Galaga_customio_w();
   }

   public WriteHandler writeCustomIOData() {
      return new Galaga_customio_data_w();
   }

   public WriteHandler writeHalt() {
      return new Galaga_halt_w();
   }

   public WriteHandler writeInterruptEnable1() {
      return new Galaga_interrupt_enable_1_w();
   }

   public WriteHandler writeInterruptEnable2() {
      return new Galaga_interrupt_enable_2_w();
   }

   public WriteHandler writeInterruptEnable3() {
      return new Galaga_interrupt_enable_3_w();
   }

   public WriteHandler writeSharedRAM() {
      return new Galaga_sharedram_w();
   }

   public class Galaga_customio_data_r implements ReadHandler {
      public int read(int var1) {
         var1 -= 28672;
         switch(MGalaga.this.customio_command) {
         case 113:
         case 177:
            if(var1 == 0) {
               if(MGalaga.this.mode != 0) {
                  var1 = MGalaga.this.readinputport(4);
               } else {
                  var1 = MGalaga.this.readinputport(4);
                  MGalaga var2;
                  if(MGalaga.this.coinpercred > 0) {
                     if((var1 & 112) != 112 && MGalaga.this.credits < 99) {
                        MGalaga.this.md.input[4].write(MGalaga.this.md.input[4].read(0) | 112);
                        var2 = MGalaga.this;
                        ++var2.coininserted;
                        if(MGalaga.this.coininserted >= MGalaga.this.coinpercred) {
                           var2 = MGalaga.this;
                           var2.credits += MGalaga.this.credpercoin;
                           MGalaga.this.coininserted = 0;
                        }
                     }
                  } else {
                     MGalaga.this.credits = 100;
                  }

                  if((var1 & 4) == 0 && MGalaga.this.credits >= 1) {
                     var2 = MGalaga.this;
                     --var2.credits;
                  }

                  if((var1 & 8) == 0 && MGalaga.this.credits >= 2) {
                     var2 = MGalaga.this;
                     var2.credits -= 2;
                  }

                  var1 = MGalaga.this.credits / 10 * 16 + MGalaga.this.credits % 10;
               }
               break;
            } else if(var1 == 1) {
               var1 = MGalaga.this.readinputport(2);
               break;
            } else if(var1 == 2) {
               var1 = MGalaga.this.readinputport(3);
               break;
            }
         default:
            var1 = 255;
         }

         return var1;
      }
   }

   public class Galaga_customio_data_w implements WriteHandler {
      public void write(int var1, int var2) {
         var1 -= 28672;
         MGalaga.this.customio[var1] = var2;
         switch(MGalaga.this.customio_command) {
         case 225:
            if(var1 == 7) {
               MGalaga.this.coinpercred = MGalaga.this.customio[1];
               MGalaga.this.credpercoin = MGalaga.this.customio[2];
            }
         case 168:
         default:
         }
      }
   }

   public class Galaga_customio_r implements ReadHandler {
      public int read(int var1) {
         return MGalaga.this.customio_command;
      }
   }

   public class Galaga_customio_w implements WriteHandler {
      public void write(int var1, int var2) {
         MGalaga.this.customio_command = var2;
         switch(var2) {
         case 16:
            MGalaga.this.nmi_timer = false;
            return;
         case 161:
            MGalaga.this.mode = 1;
            break;
         case 225:
            MGalaga.this.credits = 0;
            MGalaga.this.mode = 0;
         }

         MGalaga.this.nmi_timer = true;
      }
   }

   public class Galaga_dsw_r implements ReadHandler {
      public int read(int var1) {
         var1 -= 26624;
         return MGalaga.this.driver.in[0].read(0) >> var1 & 1 | (MGalaga.this.driver.in[1].read(0) >> var1 & 1) << 1;
      }
   }

   public class Galaga_halt_w implements WriteHandler {
      public void write(int var1, int var2) {
         if((var2 & 1) != 0) {
            MGalaga.this.halt23 = false;
            MGalaga.this.driver.cpu2.state_HALT = false;
            MGalaga.this.driver.cpu3.state_HALT = false;
         } else if(var2 == 0) {
            MGalaga.this.halt23 = true;
            MGalaga.this.driver.cpu2.reset();
            MGalaga.this.driver.cpu3.reset();
            MGalaga.this.driver.cpu2.state_HALT = true;
            MGalaga.this.driver.cpu3.state_HALT = true;
         }

      }
   }

   public class Galaga_interrupt_1 implements InterruptHandler {
      public int irq() {
         MGalaga.this.video.updateStars();
         byte var1;
         if(MGalaga.this.interrupt_enable_1) {
            var1 = 0;
         } else {
            var1 = -1;
         }

         return var1;
      }
   }

   public class Galaga_interrupt_2 implements InterruptHandler {
      public int irq() {
         byte var1;
         if(MGalaga.this.interrupt_enable_2) {
            var1 = 0;
         } else {
            var1 = -1;
         }

         return var1;
      }
   }

   public class Galaga_interrupt_3 implements InterruptHandler {
      public int irq() {
         byte var1;
         if(MGalaga.this.interrupt_enable_3) {
            var1 = 1;
         } else {
            var1 = -1;
         }

         return var1;
      }
   }

   public class Galaga_interrupt_enable_1_w implements WriteHandler {
      public void write(int var1, int var2) {
         MGalaga var4 = MGalaga.this;
         boolean var3;
         if((var2 & 1) != 0) {
            var3 = true;
         } else {
            var3 = false;
         }

         var4.interrupt_enable_1 = var3;
      }
   }

   public class Galaga_interrupt_enable_2_w implements WriteHandler {
      public void write(int var1, int var2) {
         MGalaga var4 = MGalaga.this;
         boolean var3;
         if((var2 & 1) != 0) {
            var3 = true;
         } else {
            var3 = false;
         }

         var4.interrupt_enable_2 = var3;
      }
   }

   public class Galaga_interrupt_enable_3_w implements WriteHandler {
      public void write(int var1, int var2) {
         MGalaga var4 = MGalaga.this;
         boolean var3;
         if((var2 & 1) == 0) {
            var3 = true;
         } else {
            var3 = false;
         }

         var4.interrupt_enable_3 = var3;
      }
   }

   public class Galaga_sharedram_r implements ReadHandler {
      public int read(int var1) {
         return MGalaga.this.mem_shared[var1];
      }
   }

   public class Galaga_sharedram_w implements WriteHandler {
      public void write(int var1, int var2) {
         if(var1 < '蠀') {
            MGalaga.this.video.dirtybuffer[var1 & 1023] = true;
         }

         MGalaga.this.mem_shared[var1] = (char)var2;
      }
   }
}
