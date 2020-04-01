package jef.scoring;

import jef.machine.Emulator;

public class HighScoreParser {
   public static final int CHAR_NIBBLES = 16777215;
   int blank_char;
   int delay;
   int initial_high_score;
   int last_addr;
   Emulator m;
   char[] memory;
   private int multiplier;
   int offset;
   boolean save_highscore = false;
   int start_addr;
   ScoreValidator validator;
   int zero_char;

   public HighScoreParser(Emulator var1, char[] var2, int var3, int var4, int var5, int var6, int var7) {
      this.m = var1;
      this.memory = var2;
      this.initial_high_score = var3;
      this.start_addr = var4;
      this.offset = var5;
      this.last_addr = var6;
      this.delay = var7;
      this.blank_char = 16777215;
      this.multiplier = 1;
      var1.setHighScoreHandler(this);
      var1.setHighScoreSupported(true);
      var1.setHighScore(0L);
   }

   public HighScoreParser(Emulator var1, char[] var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9, int var10) {
      this.m = var1;
      this.memory = var2;
      this.blank_char = var3;
      this.zero_char = var4;
      this.initial_high_score = var5;
      this.start_addr = var6;
      this.offset = var7;
      this.last_addr = var8;
      this.delay = var9;
      this.multiplier = var10;
      var1.setHighScoreHandler(this);
      var1.setHighScoreSupported(true);
      var1.setHighScore(0L);
   }

   private long calcScore() {
      long var13 = 0L;
      byte var10 = 0;
      byte var11 = 0;
      byte var12 = 0;
      int var7 = 0;
      int var6 = 0;
      byte var8 = 0;
      byte var9 = 0;
      int var5 = 0;
      long var15;
      int var17;
      char var18;
      if(this.blank_char == 16777215) {
         double var1;
         double var3;
         if(this.start_addr <= this.last_addr) {
            var7 = this.last_addr;
            var15 = var13;
            var17 = var10;

            while(true) {
               var5 = var6;
               var13 = var15;
               if(var7 < this.start_addr) {
                  break;
               }

               var18 = this.memory[var7];
               if(var18 > 153 || var18 < 0) {
                  var13 = 0L;
                  return var13;
               }

               var6 = (int)((double)var6 + 9.0D * Math.pow(10.0D, (double)var17));
               var1 = (double)var15;
               var3 = (double)(var18 & 15);
               var5 = var17 + 1;
               var13 = (long)(var1 + var3 * Math.pow(10.0D, (double)var17));
               var6 = (int)((double)var6 + 9.0D * Math.pow(10.0D, (double)var5));
               var1 = (double)var13;
               var3 = (double)(var18 >> 4);
               var17 = var5 + 1;
               var15 = (long)(var1 + var3 * Math.pow(10.0D, (double)var5));
               var7 -= this.offset;
            }
         } else {
            for(var6 = this.last_addr; var6 <= this.start_addr; var6 += this.offset) {
               char var19 = this.memory[var6];
               if(var19 > 153 || var19 < 0) {
                  var13 = 0L;
                  return var13;
               }

               var5 = (int)((double)var5 + 9.0D * Math.pow(10.0D, (double)var7));
               var1 = (double)var13;
               var3 = (double)(var19 & 15);
               int var20 = var7 + 1;
               var13 = (long)(var1 + var3 * Math.pow(10.0D, (double)var7));
               var5 = (int)((double)var5 + 9.0D * Math.pow(10.0D, (double)var20));
               var3 = (double)var13;
               var1 = (double)(var19 >> 4);
               var7 = var20 + 1;
               var13 = (long)(var3 + var1 * Math.pow(10.0D, (double)var20));
            }
         }
      } else if(this.last_addr > this.start_addr) {
         var5 = this.last_addr;
         var15 = var13;
         var7 = var11;
         var6 = var8;
         var17 = var5;

         while(true) {
            var5 = var6;
            var13 = var15;
            if(var17 < this.start_addr) {
               break;
            }

            label78: {
               var18 = this.memory[var17];
               if(var18 != 0) {
                  var5 = var18;
                  if(var18 != this.blank_char) {
                     break label78;
                  }
               }

               var5 = this.zero_char;
            }

            var5 -= this.zero_char;
            if(var5 > 9 || var5 < 0) {
               var13 = 0L;
               return var13;
            }

            var6 = (int)((double)var6 + 9.0D * Math.pow(10.0D, (double)var7));
            var15 = (long)((double)var15 + (double)var5 * Math.pow(10.0D, (double)var7));
            var17 -= this.offset;
            ++var7;
         }
      } else {
         var17 = this.last_addr;
         var15 = var13;
         var7 = var12;
         var6 = var9;

         while(true) {
            var5 = var6;
            var13 = var15;
            if(var17 > this.start_addr) {
               break;
            }

            label95: {
               var18 = this.memory[var17];
               if(var18 != 0) {
                  var5 = var18;
                  if(var18 != this.blank_char) {
                     break label95;
                  }
               }

               var5 = this.zero_char;
            }

            var5 -= this.zero_char;
            if(var5 > 9 || var5 < 0) {
               var13 = 0L;
               return var13;
            }

            var6 = (int)((double)var6 + 9.0D * Math.pow(10.0D, (double)var7));
            var15 = (long)((double)var15 + (double)var5 * Math.pow(10.0D, (double)var7));
            var17 += this.offset;
            ++var7;
         }
      }

      var15 = var13;
      if(var13 >= (long)var5) {
         var15 = 0L;
      }

      var13 = (long)this.multiplier * var15;
      return var13;
   }

   public void setValidator(ScoreValidator var1) {
      this.validator = var1;
   }

   public void update() {
      long var1 = this.calcScore();
      if(this.save_highscore && var1 > (long)this.initial_high_score) {
         if(this.validator == null || this.validator != null && this.validator.validate(var1)) {
            this.m.setHighScore(var1);
         }
      } else if(this.delay > 0) {
         --this.delay;
      } else if(var1 == (long)this.initial_high_score) {
         this.save_highscore = true;
      }

   }
}
