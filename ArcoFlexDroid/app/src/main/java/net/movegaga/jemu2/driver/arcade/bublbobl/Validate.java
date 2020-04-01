package net.movegaga.jemu2.driver.arcade.bublbobl;

import jef.scoring.ScoreValidator;

public class Validate implements ScoreValidator {
   public boolean validate(long var1) {
      boolean var3;
      if(var1 % 10L == 0L) {
         var3 = true;
      } else {
         var3 = false;
      }

      return var3;
   }
}
