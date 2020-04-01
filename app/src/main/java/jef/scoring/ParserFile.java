package jef.scoring;

import jef.machine.Emulator;
import jef.util.ConfigFile;

public class ParserFile {
   String[][] contents;
   String fileName;

   public ParserFile(String var1) {
      this.fileName = var1;
   }

   public HighScoreParser getHighScoreParser(String var1, Emulator var2, char[] var3) {
      ConfigFile var6 = new ConfigFile(0);
      if(var6 == null) {
         ;
      }

      this.contents = var6.getContents();
      HighScoreParser var7;
      if(this.contents == null) {
         var7 = null;
      } else {
         for(int var4 = 0; var4 < this.contents.length; ++var4) {
            if(this.contents[var4][0].equals(var1)) {
               int var5;
               if(this.contents[var4][1].equals("BCD")) {
                  var5 = 16777215;
               } else {
                  var5 = Integer.parseInt(this.contents[var4][1], 16);
               }

               var7 = new HighScoreParser(var2, var3, var5, Integer.parseInt(this.contents[var4][2], 16), Integer.parseInt(this.contents[var4][3]), Integer.parseInt(this.contents[var4][4], 16), Integer.parseInt(this.contents[var4][5], 16), Integer.parseInt(this.contents[var4][6], 16), Integer.parseInt(this.contents[var4][7]), Integer.parseInt(this.contents[var4][8]));
               return var7;
            }
         }

         var7 = null;
      }

      return var7;
   }
}
