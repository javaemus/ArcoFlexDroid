package net.movegaga.jemu2.driver.arcade.galaxian;

import jef.cpu.Z80;
import jef.cpuboard.CpuBoard;
import jef.cpuboard.FastCpuBoard;
import jef.machine.BasicEmulator;
import jef.machine.Emulator;
import jef.map.ReadHandler;

public class Scramble extends BasicEmulator implements Emulator {
   public CpuBoard createCpuBoard(int var1) {
      return new FastCpuBoard();
   }

   public ReadHandler readProtection1(Z80 var1) {
      return new ReadProtection1(var1);
   }

   public ReadHandler readProtection2(Z80 var1) {
      return new ReadProtection2(var1);
   }

   class ReadProtection1 implements ReadHandler {
      Z80 cpu;

      public ReadProtection1(Z80 var2) {
         this.cpu = var2;
      }

      public int read(int var1) {
         short var2;
         switch(this.cpu.PC) {
         case 474:
            var2 = 128;
            break;
         case 484:
            var2 = 0;
            break;
         default:
            var2 = 0;
         }

         return var2;
      }
   }

   class ReadProtection2 implements ReadHandler {
      Z80 cpu;

      public ReadProtection2(Z80 var2) {
         this.cpu = var2;
      }

      public int read(int var1) {
         short var2;
         switch(this.cpu.PC) {
         case 458:
            var2 = 144;
            break;
         default:
            var2 = 0;
         }

         return var2;
      }
   }
}
