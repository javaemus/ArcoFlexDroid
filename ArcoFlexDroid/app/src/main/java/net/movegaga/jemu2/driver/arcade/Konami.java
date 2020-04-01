package net.movegaga.jemu2.driver.arcade;

import jef.cpuboard.BasicCpuBoard;
import jef.cpuboard.BasicDecryptingCpuBoard;
import jef.cpuboard.CpuBoard;
import jef.machine.BasicEmulator;
import jef.machine.Emulator;

public class Konami extends BasicEmulator implements Emulator {
   char[] rom;

   public CpuBoard createCpuBoard(int var1) {
      Object var2;
      if(var1 == 0) {
         var2 = new BasicDecryptingCpuBoard();
      } else {
         var2 = new BasicCpuBoard();
      }

      return (CpuBoard)var2;
   }

   void decode(int var1) {
      int var2 = this.rom.length / 2;

      for(var1 = 0; var1 < var2; ++var1) {
         this.rom[var1 + var2] = (char)this.decodebyte(this.rom[var1], var1);
      }

   }

   int decodebyte(int var1, int var2) {
      int var3;
      if((var2 & 2) != 0) {
         var3 = 0 | 128;
      } else {
         var3 = 0 | 32;
      }

      if((var2 & 8) != 0) {
         var2 = var3 | 8;
      } else {
         var2 = var3 | 2;
      }

      return var1 ^ var2;
   }

   public void konami1_decode(char[] var1) {
      this.rom = var1;
      this.decode(0);
   }

   public void konami1_decode_cpu2(char[] var1) {
      this.rom = var1;
      this.decode(1);
   }
}
