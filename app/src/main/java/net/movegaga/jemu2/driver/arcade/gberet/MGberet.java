package net.movegaga.jemu2.driver.arcade.gberet;

import jef.machine.BasicEmulator;
import jef.machine.Emulator;
import jef.map.InterruptHandler;
import jef.map.WriteHandler;

public class MGberet extends BasicEmulator implements Emulator, WriteHandler, InterruptHandler {
   public boolean interruptenable = false;

   public int irq() {
      byte var1;
      if(this.getCurrentSlice() == 0) {
         var1 = 0;
      } else if(this.getCurrentSlice() % 2 != 0 && this.interruptenable) {
         var1 = 1;
      } else {
         var1 = -1;
      }

      return var1;
   }

   public void write(int var1, int var2) {
      boolean var3;
      if((var2 & 1) != 0) {
         var3 = true;
      } else {
         var3 = false;
      }

      this.interruptenable = var3;
   }
}
