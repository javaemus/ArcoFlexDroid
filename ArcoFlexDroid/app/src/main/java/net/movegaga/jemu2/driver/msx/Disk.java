package net.movegaga.jemu2.driver.msx;

import jef.util.ZipLoader;

public class Disk {
   char[] data;

   public Disk(String var1, int var2, int var3) {
      this.data = new char[var3];
      ZipLoader var4 = new ZipLoader(var1);
      var4.queue((char[])this.data, var2, 0, var3);
      var4.load();
   }

   public Disk(char[] var1) {
      this.data = var1;
      if((var1.length & 1) == 1) {
         for(int var2 = 1; var2 < var1.length; ++var2) {
            var1[var2 - 1] = var1[var2];
         }
      }

   }

   public boolean read(char[] var1, int var2) {
      boolean var4;
      if(this.data != null) {
         for(int var3 = 0; var3 < 512; ++var3) {
            var1[var3] = this.data[var2 * 512 + var3];
         }

         var4 = true;
      } else {
         var4 = false;
      }

      return var4;
   }
}
