package net.movegaga.jemu2.driver.msx;

import java.lang.reflect.Array;

import jef.map.ReadHandler;
import jef.map.WriteHandler;

public class MemoryLayout implements ReadHandler, WriteHandler {
   static final int PSLOT_EXTENDED = 3;
   Mapper[][] cart = (Mapper[][])Array.newInstance(Mapper.class, new int[]{4, 4});
   int cursslot;
   char[][][] mem = (char[][][])Array.newInstance(char[].class, new int[]{4, 4});
   int pslot;
   int sslot;

   private void updateCurSSlot() {
      int var1;
      if(this.pslot == 3) {
         var1 = this.sslot;
      } else {
         var1 = 0;
      }

      this.cursslot = var1;
   }

   public void installMapper(int var1, int var2, Mapper var3) {
      this.cart[var1][var2] = var3;
   }

   public void installMapper(int var1, Mapper var2) {
      this.cart[var1][0] = var2;
   }

   public void installMemory(int var1, int var2, char[] var3) {
      this.mem[var1][var2] = var3;
   }

   public void installMemory(int var1, char[] var2) {
      this.mem[var1][0] = var2;
   }

   public int read(int var1) {
      char[] var2 = this.mem[this.pslot][this.cursslot];
      if(var2 != null) {
         var1 = var2[var1];
      } else if(this.cart[this.pslot][this.cursslot] != null) {
         var1 = this.cart[this.pslot][this.cursslot].read(var1);
      } else {
         var1 = 255;
      }

      return var1;
   }

   public void setPSLOT(int var1) {
      this.pslot = var1;
      this.updateCurSSlot();
   }

   public void setSSLOT(int var1) {
      this.sslot = var1;
      this.updateCurSSlot();
   }

   public void write(int var1, int var2) {
      if(this.cart[this.pslot][this.cursslot] != null) {
         this.cart[this.pslot][this.cursslot].write(var1, var2);
      }

   }
}
