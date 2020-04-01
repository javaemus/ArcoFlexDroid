package net.movegaga.jemu2.driver.arcade.bw8080;

import jef.machine.BasicEmulator;
import jef.machine.Emulator;
import jef.map.InterruptHandler;
import jef.map.ReadHandler;
import jef.map.WriteHandler;

public class MBw8080 extends BasicEmulator implements Emulator {
   static int count = 0;
   int shift_amount = 0;
   int shift_data1 = 0;
   int shift_data2 = 0;

   public int SHIFT() {
      return (this.shift_data1 << 8 | this.shift_data2) << (this.shift_amount & 7) >> 8 & 255;
   }

   public ReadHandler boothill_shift_data_r(MBw8080 var1) {
      return new Boothill_shift_data_r(var1);
   }

   public int bw_interrupt() {
      ++count;
      byte var1;
      if((count & 1) == 1) {
         var1 = 0;
      } else {
         var1 = 1;
      }

      return var1;
   }

   public WriteHandler c8080bw_shift_amount_w(MBw8080 var1) {
      return new Invaders_shift_amount_w(var1);
   }

   public ReadHandler c8080bw_shift_data_r(MBw8080 var1) {
      return new Invaders_shift_data_r(var1);
   }

   public WriteHandler c8080bw_shift_data_w(MBw8080 var1) {
      return new Invaders_shift_data_w(var1);
   }

   public InterruptHandler invaders_interrupt(MBw8080 var1) {
      return new Invaders_interrupt(var1);
   }

   public WriteHandler invaders_sh_port3_w(MBw8080 var1) {
      return new Invaders_sh_port3_w(var1);
   }

   public WriteHandler invaders_sh_port5_w(MBw8080 var1) {
      return new Invaders_sh_port5_w(var1);
   }

   public ReadHandler invaders_shift_data_rev_r(MBw8080 var1) {
      return new Invaders_shift_data_rev_r(var1);
   }

   public class Boothill_shift_data_r implements ReadHandler {
      MBw8080 b;

      public Boothill_shift_data_r(MBw8080 var2) {
         this.b = var2;
      }

      public int read(int var1) {
         if(MBw8080.this.shift_amount < 16) {
            var1 = this.b.SHIFT();
         } else {
            var1 = MBw8080.this.SHIFT();
            var1 = (var1 & 1) << 7 | (var1 & 2) << 5 | (var1 & 4) << 3 | (var1 & 8) << 1 | (var1 & 16) >> 1 | (var1 & 32) >> 3 | (var1 & 64) >> 5 | (var1 & 128) >> 7;
         }

         return var1;
      }
   }

   public class Invaders_interrupt implements InterruptHandler {
      MBw8080 b;

      public Invaders_interrupt(MBw8080 var2) {
         this.b = var2;
      }

      public int irq() {
         return this.b.bw_interrupt();
      }
   }

   public class Invaders_sh_port3_w implements WriteHandler {
      int Sound = 0;
      MBw8080 b;

      public Invaders_sh_port3_w(MBw8080 var2) {
         this.b = var2;
      }

      public void write(int var1, int var2) {
         this.Sound = var2;
      }
   }

   public class Invaders_sh_port5_w implements WriteHandler {
      int Sound = 0;
      MBw8080 b;

      public Invaders_sh_port5_w(MBw8080 var2) {
         this.b = var2;
      }

      public void write(int var1, int var2) {
         this.Sound = var2;
      }
   }

   public class Invaders_shift_amount_w implements WriteHandler {
      MBw8080 b;

      public Invaders_shift_amount_w(MBw8080 var2) {
         this.b = var2;
      }

      public void write(int var1, int var2) {
         this.b.shift_amount = var2;
      }
   }

   public class Invaders_shift_data_r implements ReadHandler {
      MBw8080 b;

      public Invaders_shift_data_r(MBw8080 var2) {
         this.b = var2;
      }

      public int read(int var1) {
         return this.b.SHIFT();
      }
   }

   public class Invaders_shift_data_rev_r implements ReadHandler {
      MBw8080 b;

      public Invaders_shift_data_rev_r(MBw8080 var2) {
         this.b = var2;
      }

      public int read(int var1) {
         var1 = MBw8080.this.SHIFT();
         return (var1 & 1) << 7 | (var1 & 2) << 5 | (var1 & 4) << 3 | (var1 & 8) << 1 | (var1 & 16) >> 1 | (var1 & 32) >> 3 | (var1 & 64) >> 5 | (var1 & 128) >> 7;
      }
   }

   public class Invaders_shift_data_w implements WriteHandler {
      MBw8080 b;

      public Invaders_shift_data_w(MBw8080 var2) {
         this.b = var2;
      }

      public void write(int var1, int var2) {
         this.b.shift_data2 = this.b.shift_data1;
         this.b.shift_data1 = var2;
      }
   }
}
