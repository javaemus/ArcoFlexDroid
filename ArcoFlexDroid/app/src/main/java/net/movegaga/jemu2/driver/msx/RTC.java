package net.movegaga.jemu2.driver.msx;

import java.lang.reflect.Array;
import java.util.Date;

import jef.map.ReadHandler;
import jef.map.WriteHandler;

public class RTC implements ReadHandler, WriteHandler {
   static final int[][] DEFAULTS;
   private Date date;
   int reg;
   int[][] register;

   static {
      int[] var1 = new int[16];
      int[] var2 = new int[16];
      int[] var0 = new int[16];
      var0[5] = 2;
      var0[6] = 15;
      var0[7] = 4;
      var0[8] = 4;
      DEFAULTS = new int[][]{var1, var2, var0, new int[16]};
   }

   public RTC() {
      this.register = (int[][])Array.newInstance(Integer.TYPE, new int[]{4, 16});
      this.date = new Date();

      for(int var1 = 0; var1 < 4; ++var1) {
         for(int var2 = 0; var2 < 16; ++var2) {
            this.register[var1][var2] = DEFAULTS[var1][var2];
         }
      }

   }

   private int mode() {
      return this.register[0][13] & 3;
   }

   private void refresh() {
      int var2 = this.date.getSeconds();
      int var7 = this.date.getMinutes();
      int var5 = this.date.getHours();
      int var1 = this.date.getDay();
      int var3 = this.date.getDate();
      int var6 = this.date.getMonth() + 1;
      int var4 = this.date.getYear() - 80;
      this.register[0][0] = var2 % 10;
      this.register[0][1] = var2 / 10;
      this.register[0][2] = var7 % 10;
      this.register[0][3] = var7 / 10;
      this.register[0][4] = var5 % 10;
      this.register[0][5] = var5 / 10;
      this.register[0][6] = var1;
      this.register[0][7] = var3 % 10;
      this.register[0][8] = var3 / 10;
      this.register[0][9] = (var6 + 1) % 10;
      this.register[0][10] = (var6 + 1) / 10;
      this.register[0][11] = (var4 - 80) % 10;
      this.register[0][12] = (var4 - 80) / 10 % 10;
   }

   private void writeData(int var1) {
      int var2;
      if(this.reg > 12) {
         var2 = 0;
      } else {
         var2 = this.mode();
      }

      this.register[var2][this.reg] = var1 & 15;
   }

   private void writeRegisterSelect(int var1) {
      this.reg = var1;
   }

   public int read(int var1) {
      if(this.reg == 13) {
         var1 = this.mode() | 240;
      } else if(this.reg > 13) {
         var1 = 255;
      } else {
         if(this.reg == 0) {
            this.refresh();
         }

         var1 = this.register[this.mode()][this.reg] | 240;
      }

      return var1;
   }

   public void write(int var1, int var2) {
      if(var1 == 180) {
         this.writeRegisterSelect(var2);
      } else if(var1 == 181) {
         this.writeData(var2);
      }

   }
}
