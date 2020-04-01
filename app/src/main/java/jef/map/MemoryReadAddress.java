package jef.map;

public class MemoryReadAddress implements ReadMap {
   static final boolean debug = false;
   public MEMreadBanked[] BANKS = new MEMreadBanked[8];
   public ReadHandler RAM;
   public char[] mem;
   private ReadHandler[] readMap;

   public MemoryReadAddress(int var1) {
      this.readMap = new ReadHandler[var1];
      this.RAM = new MEMread();
      this.set(0, var1 - 1, this.RAM);
   }

   public MemoryReadAddress(char[] var1) {
      this.mem = var1;
      this.readMap = new ReadHandler[var1.length];
      this.RAM = new MEMread();
      this.set(0, var1.length - 1, this.RAM);
   }

   public char[] getMemory() {
      return this.mem;
   }

   public int getSize() {
      return this.mem.length;
   }

   public int read(int var1) {
      return this.readMap[var1].read(var1);
   }

   public void set(int var1, int var2, ReadHandler var3) {
      while(var1 <= var2) {
         this.readMap[var1] = var3;
         ++var1;
      }

   }

   public void setBankAddress(int var1, int var2) {
      this.BANKS[var1 - 1].setBankAdr(var2);
   }

   public void setMR(int var1, int var2, int var3) {
      Object var4 = null;
      switch(var3) {
      case 0:
      case 1:
      case 2:
         var4 = this.RAM;
         break;
      case 3:
      case 4:
      case 5:
      case 6:
      case 7:
      case 8:
      case 9:
      case 10:
         MEMreadBanked[] var5 = this.BANKS;
         var4 = new MEMreadBanked(var1);
         var5[var3 - 3] = (MEMreadBanked)var4;
      }

      while(var1 <= var2) {
         this.readMap[var1] = (ReadHandler)var4;
         ++var1;
      }

   }

   public class MEMread implements ReadHandler {
      public int read(int var1) {
         return MemoryReadAddress.this.mem[var1];
      }
   }

   public class MEMreadBanked implements ReadHandler {
      int bank_address;
      int startArea;

      public MEMreadBanked(int var2) {
         this.startArea = var2;
      }

      public int read(int var1) {
         return MemoryReadAddress.this.mem[this.bank_address + var1];
      }

      public void setBankAdr(int var1) {
         this.bank_address = var1 - this.startArea;
      }
   }

   public class UndefinedRead implements ReadHandler {
      public int read(int var1) {
         return 0;
      }
   }
}
