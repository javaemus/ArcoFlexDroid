package net.movegaga.jemu2.driver.arcade.gyruss;

import jef.cpuboard.CpuBoard;
import jef.cpuboard.FastCpuBoard;
import jef.machine.BasicEmulator;
import jef.machine.Emulator;
import jef.map.InterruptHandler;
import jef.map.WriteHandler;

public class MGyruss extends BasicEmulator implements Emulator {
   public boolean irqEnabled = false;

   public CpuBoard createCpuBoard(int var1) {
      return new FastCpuBoard();
   }

   public WriteHandler interrupt_enable_w(MGyruss var1) {
      return new Interrupt_enable_w(var1);
   }

   public InterruptHandler nmi_interrupt(MGyruss var1) {
      return new NMI_interrupt(var1);
   }

   public class Interrupt_enable_w implements WriteHandler {
      MGyruss m;

      public Interrupt_enable_w(MGyruss var2) {
         this.m = var2;
      }

      public void write(int var1, int var2) {
         MGyruss var4 = this.m;
         boolean var3;
         if(var2 != 0) {
            var3 = true;
         } else {
            var3 = false;
         }

         var4.irqEnabled = var3;
      }
   }

   public class NMI_interrupt implements InterruptHandler {
      MGyruss m;

      public NMI_interrupt(MGyruss var2) {
         this.m = var2;
      }

      public int irq() {
         byte var1;
         if(this.m.irqEnabled) {
            var1 = 1;
         } else {
            var1 = -1;
         }

         return var1;
      }
   }
}
