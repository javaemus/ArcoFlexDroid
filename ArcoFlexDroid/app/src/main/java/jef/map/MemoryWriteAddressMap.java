package jef.map;

import java.util.HashMap;
import java.util.Map;

public class MemoryWriteAddressMap implements WriteMap {
   private MEMWriteBanked[] BANKS = new MEMWriteBanked[8];
   private WriteHandler RAM = new RAMwrite();
   private WriteHandler ROM = new ROMwrite();
   private Map map = new HashMap();
   private int[] mem;

   public MemoryWriteAddressMap(int[] var1) {
      this.mem = var1;
   }

   public int getSize() {
      return 0;
   }

   public void set(int var1, int var2, WriteHandler var3) {
      while(var1 <= var2) {
         Integer var4 = new Integer(var1);
         this.map.put(var4, var3);
         ++var1;
      }

   }

   public void setMW(int var1, int var2, int var3) {
      for(int var4 = var1; var4 <= var2; ++var4) {
         switch(var3) {
         case 1:
         case 2:
            this.map.put(new Integer(var4), this.ROM);
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
            MEMWriteBanked var6 = new MEMWriteBanked(var1);
            var5[var3 - 3] = var6;
            this.map.put(new Integer(var4), var6);
         }
      }

   }

   public void write(int var1, int var2) {
      WriteHandler var3 = (WriteHandler)this.map.get(new Integer(var1));
      if(var3 == null) {
         this.RAM.write(var1, var2);
      } else {
         var3.write(var1, var2);
      }

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
         MemoryWriteAddressMap.this.mem[this.bank_address + var1] = var2;
      }
   }

   public class RAMwrite implements WriteHandler {
      public void write(int var1, int var2) {
         MemoryWriteAddressMap.this.mem[var1] = var2;
      }
   }

   public class ROMwrite implements WriteHandler {
      public void write(int var1, int var2) {
      }
   }
}
