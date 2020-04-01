package jef.machine;

import jef.cpuboard.BasicDecryptingCpuBoard;
import jef.cpuboard.CpuBoard;

public class DecryptingEmulator extends BasicEmulator {
   private int offset;

   public DecryptingEmulator() {
      this.offset = 65536;
   }

   public DecryptingEmulator(int var1) {
      this.offset = var1;
   }

   public CpuBoard createCpuBoard(int var1) {
      return new BasicDecryptingCpuBoard(this.offset);
   }
}
