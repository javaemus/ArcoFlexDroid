package jef.map;

import java.util.HashMap;
import java.util.Map;

public class MemoryReadAddressMap implements ReadMap {
   MEMreadBanked[] BANKS = new MEMreadBanked[8];
   ReadHandler[] handlers;
   Map map = new HashMap(256);
   char[] mem;
   ReadHandler memRead = new MEMRead();

   public MemoryReadAddressMap(char[] var1) {
      this.handlers = new ReadHandler[]{this.memRead, this.memRead, this.memRead};
      this.mem = var1;
   }

   public char[] getMemory() {
      return this.mem;
   }

   public int getSize() {
      return 0;
   }

   public int read(int var1) {
      ReadHandler var2 = (ReadHandler)this.map.get(new Integer(var1));
      if(var2 == null) {
         var1 = this.memRead.read(var1);
      } else {
         var1 = var2.read(var1);
      }

      return var1;
   }

   public void set(int var1, int var2, ReadHandler var3) {
      while(var1 <= var2) {
         Integer var4 = new Integer(var1);
         this.map.put(var4, var3);
         ++var1;
      }

   }

   public void setBankAddress(int var1, int var2) {
      this.BANKS[var1 - 1].setBankAdr(var2);
   }

   public void setMR(int var1, int var2, int var3) {
      while(var1 <= var2) {
         ++var1;
      }

   }

   class MEMRead implements ReadHandler {
      public int read(int var1) {
         return MemoryReadAddressMap.this.mem[var1];
      }
   }

   class MEMreadBanked implements ReadHandler {
      int bank_address;
      int startArea;

      public MEMreadBanked(int var2) {
         this.startArea = var2;
      }

      public int read(int var1) {
         return MemoryReadAddressMap.this.mem[this.bank_address + var1];
      }

      public void setBankAdr(int var1) {
         this.bank_address = var1 - this.startArea;
      }
   }
}
