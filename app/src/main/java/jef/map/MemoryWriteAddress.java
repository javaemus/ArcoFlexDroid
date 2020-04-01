package jef.map;

public class MemoryWriteAddress implements WriteMap {
   static final boolean debug = false;
   public MEMWriteBanked[] BANKS = new MEMWriteBanked[8];
   private WriteHandler RAM;
   private WriteHandler ROM;
   private UndefinedWrite defwrite = new UndefinedWrite();
   public char[] mem;
   public WriteHandler[] writeMap;

   public MemoryWriteAddress(int var1) {
      this.writeMap = new WriteHandler[var1];
      this.set(0, var1 - 1, this.defwrite);
   }

   public MemoryWriteAddress(char[] var1) {
      this.mem = var1;
      this.RAM = new RAMwrite();
      this.ROM = new ROMwrite();
      this.writeMap = new WriteHandler[var1.length];
      this.set(0, var1.length - 1, this.RAM);
   }

   public int getSize() {
      return this.mem.length;
   }

   public void set(int var1, int var2, WriteHandler var3) {
      while(var1 <= var2) {
         this.writeMap[var1] = var3;
         ++var1;
      }

   }

   public void setMW(int var1, int var2, int var3) {
      Object var4 = null;
      switch(var3) {
      case 0:
         var4 = this.RAM;
         break;
      case 1:
      case 2:
         var4 = this.ROM;
         break;
      case 3:
      case 4:
      case 5:
      case 6:
      case 7:
      case 8:
      case 9:
      case 10:
         MEMWriteBanked[] var5 = this.BANKS;
         var4 = new MEMWriteBanked(var1);
         var5[var3 - 3] = (MEMWriteBanked)var4;
      }

      while(var1 <= var2) {
         this.writeMap[var1] = (WriteHandler)var4;
         ++var1;
      }

   }

   public void write(int var1, int var2) {
      this.writeMap[var1].write(var1, var2);
   }

   public class MEMWriteBanked implements WriteHandler {
      int bank_address;
      int startArea;

      public MEMWriteBanked(int var2) {
         this.startArea = var2;
      }

      public void setBankAdr(int var1) {
         this.bank_address = var1 - this.startArea;
      }

      public void write(int var1, int var2) {
         MemoryWriteAddress.this.mem[this.bank_address + var1] = (char)var2;
      }
   }

   public class RAMwrite implements WriteHandler {
      public void write(int var1, int var2) {
         MemoryWriteAddress.this.mem[var1] = (char)var2;
      }
   }

   public class ROMwrite implements WriteHandler {
      public void write(int var1, int var2) {
      }
   }

   public class UndefinedWrite implements WriteHandler {
      public void write(int var1, int var2) {
      }
   }
}
