package net.movegaga.jemu2.driver.arcade.hcastle;

import jef.map.ReadHandler;
import jef.map.WriteHandler;

public class K007121 implements WriteHandler, ReadHandler {
   int[] ctrl = new int[8];
   int[] ram = new int[8192];
   public ReadHandler readControl = new ReadHandler() {
      public int read(int var1) {
         return K007121.this.ctrl[var1 & 7];
      }
   };
   public WriteHandler writeControl = new WriteHandler() {
      public void write(int var1, int var2) {
         K007121.this.ctrl[var1 & 7] = var2;
      }
   };

   public int getScrollX() {
      return (this.ctrl[1] & 1) << 8 | this.ctrl[0];
   }

   public int getScrollY() {
      return this.ctrl[2];
   }

   public int read(int var1) {
      return this.ram[var1 & 8191];
   }

   public void write(int var1, int var2) {
      this.ram[var1 & 8191] = var2;
   }
}
