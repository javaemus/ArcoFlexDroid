package net.movegaga.jemu2.driver.arcade.pacman;

import jef.cpuboard.BasicCpuBoard;
import jef.cpuboard.CpuBoard;
import jef.cpuboard.FastCpuBoard;
import jef.machine.BasicEmulator;
import jef.machine.Emulator;

public class MPacman extends BasicEmulator implements Emulator {
   public boolean fastBoard = true;

   public CpuBoard createCpuBoard(int var1) {
      Object var2;
      if(this.fastBoard) {
         var2 = new FastCpuBoard();
      } else {
         var2 = new BasicCpuBoard();
      }

      return (CpuBoard)var2;
   }
}
