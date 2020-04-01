package net.movegaga.jemu2.driver.msx;

import jef.map.ReadHandler;
import jef.map.WriteHandler;

public class PPI {
   public final Control CONTROL;
   public final PortA PORT_A;
   public final PortB PORT_B;
   public final PortC PORT_C;
   int portA = 0;
   int portC = 255;
   int portD = 255;
   public int pslot3;
   MemoryLayout[] slot;

   public PPI(MemoryLayout[] var1) {
      this.slot = var1;
      this.PORT_A = new PortA();
      this.PORT_B = new PortB();
      this.PORT_C = new PortC();
      this.CONTROL = new Control();
   }

   class Control implements WriteHandler, ReadHandler {
      public int read(int var1) {
         return PPI.this.portD;
      }

      public void write(int var1, int var2) {
         PPI.this.portD = var2;
         if((var2 & 128) == 0) {
            var1 = 1 << (var2 >> 1 & 7);
            PPI var3;
            if((var2 & 1) == 0) {
               var3 = PPI.this;
               var3.portC &= var1 ^ 255;
            } else {
               var3 = PPI.this;
               var3.portC |= var1;
            }
         }

      }
   }

   class PortA implements WriteHandler, ReadHandler {
      public int read(int var1) {
         return PPI.this.portA;
      }

      public void write(int var1, int var2) {
         PPI.this.portA = var2;
         var1 = var2 >> 6 & 3;
         PPI.this.slot[0].setPSLOT(var2 >> 0 & 3);
         PPI.this.slot[1].setPSLOT(var2 >> 2 & 3);
         PPI.this.slot[2].setPSLOT(var2 >> 4 & 3);
         PPI.this.slot[3].setPSLOT(var1);
         PPI.this.pslot3 = var1;
      }
   }

   class PortB implements ReadHandler {
      public int read(int var1) {
         var1 = PPI.this.portC;
         return 0;
      }
   }

   class PortC implements WriteHandler, ReadHandler {
      public int read(int var1) {
         return PPI.this.portC;
      }

      public void write(int var1, int var2) {
         PPI.this.portC = var2;
      }
   }
}
