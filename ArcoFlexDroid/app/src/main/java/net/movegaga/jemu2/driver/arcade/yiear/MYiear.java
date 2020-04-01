package net.movegaga.jemu2.driver.arcade.yiear;

import jef.machine.BasicEmulator;
import jef.machine.Emulator;
import jef.map.InterruptHandler;
import jef.map.WriteHandler;
import jef.video.BitMap;

public class MYiear extends BasicEmulator implements Emulator {
   public static boolean irqEnabled = false;
   public static boolean nmiEnabled = false;

   public WriteHandler interruptEnable() {
      return new WriteInterruptEnable();
   }

   public InterruptHandler interruptIRQ() {
      return new InterruptIRQ();
   }

   public InterruptHandler interruptNMI() {
      return new InterruptNMI();
   }

   public WriteHandler nmiEnable() {
      return new WriteInterruptNMIEnable();
   }

   public BitMap refresh(boolean var1) {
      if(var1) {
         this.backBuffer = this.getDisplay();
      }

      for(int var2 = 0; var2 < 500; ++var2) {
         this.currentSlice = var2;
         this.cd[0].cpu.exec(this.cd[0].frq / 500 / 60);
      }

      this.se.update();
      this.updateInput();
      if(irqEnabled) {
         this.cb[0].interrupt(0, true);
      }

      this.highScoreHandler.update();
      return this.backBuffer;
   }

   public class InterruptIRQ implements InterruptHandler {
      public int irq() {
         return 0;
      }
   }

   public class InterruptNMI implements InterruptHandler {
      public int irq() {
         byte var1;
         if(MYiear.nmiEnabled) {
            var1 = 1;
         } else {
            var1 = -1;
         }

         return var1;
      }
   }

   public class WriteInterruptEnable implements WriteHandler {
      public void write(int var1, int var2) {
         boolean var3;
         if(var2 != 0) {
            var3 = true;
         } else {
            var3 = false;
         }

         MYiear.irqEnabled = var3;
      }
   }

   public class WriteInterruptNMIEnable implements WriteHandler {
      public void write(int var1, int var2) {
         boolean var3;
         if(var2 != 0) {
            var3 = true;
         } else {
            var3 = false;
         }

         MYiear.nmiEnabled = var3;
      }
   }
}
