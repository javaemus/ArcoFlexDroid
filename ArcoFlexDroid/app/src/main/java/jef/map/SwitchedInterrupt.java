package jef.map;

public class SwitchedInterrupt implements InterruptHandler, WriteHandler {
   private boolean enabled = true;
   private int irqType = -1;

   public SwitchedInterrupt(int var1) {
      this.irqType = var1;
   }

   public int irq() {
      int var1;
      if(this.enabled) {
         var1 = this.irqType;
      } else {
         var1 = -1;
      }

      return var1;
   }

   public void write(int var1, int var2) {
      boolean var3;
      if(var2 != 0) {
         var3 = true;
      } else {
         var3 = false;
      }

      this.enabled = var3;
   }
}
