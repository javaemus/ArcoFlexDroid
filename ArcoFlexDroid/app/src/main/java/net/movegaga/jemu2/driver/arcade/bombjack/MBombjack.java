package net.movegaga.jemu2.driver.arcade.bombjack;

import jef.cpuboard.CpuBoard;
import jef.cpuboard.FastCpuBoard;
import jef.machine.BasicEmulator;
import jef.machine.Emulator;
import jef.map.InterruptHandler;
import jef.map.WriteHandler;

public class MBombjack extends BasicEmulator implements Emulator {
   public boolean irqEnabled = false;

   public CpuBoard createCpuBoard(int var1) {
      return new FastCpuBoard();
   }

   public InterruptHandler interruptNMI() {
      return new InterruptNMI();
   }

   public WriteHandler writeEnableInterrupt() {
      return new Interrupt_enable_w();
   }

   public class InterruptNMI implements InterruptHandler {
      public int irq() {
         byte var1;
         if(MBombjack.this.irqEnabled) {
            var1 = 1;
         } else {
            var1 = -1;
         }

         return var1;
      }
   }

   public class Interrupt_enable_w implements WriteHandler {
      public void write(int var1, int var2) {
         MBombjack var4 = MBombjack.this;
         boolean var3;
         if(var2 != 0) {
            var3 = true;
         } else {
            var3 = false;
         }

         var4.irqEnabled = var3;
      }
   }
}
