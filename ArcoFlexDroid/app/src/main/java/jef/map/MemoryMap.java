package jef.map;

public class MemoryMap implements ReadMap, WriteMap {
   int banks = 4;
   int nrOfReadRegions;
   int nrOfWriteRegions;
   ReadRegion[] readRegions = new ReadRegion[30];
   char[] rom;
   WriteRegion[] writeRegions = new WriteRegion[30];

   public MemoryMap(char[] var1) {
      this.rom = var1;
      this.nrOfReadRegions = 0;
   }

   public char[] findMemory(int var1, int var2) {
      for(int var3 = 0; var3 < this.nrOfReadRegions; ++var3) {
         ReadRegion var4 = this.readRegions[var3];
         if(var4.from == var1 && var4.until == var2) {
            if(var4.memory == null) {
               throw new RuntimeException("Memory for write region 0x" + Integer.toHexString(var1) + "-0x" + Integer.toHexString(var2) + " is null");
            }

            return var4.memory;
         }
      }

      throw new RuntimeException("Could not find memory for write region 0x" + Integer.toHexString(var1) + "-0x" + Integer.toHexString(var2));
   }

   public char[] getMemory() {
      return this.rom;
   }

   public int getSize() {
      return 0;
   }

   public int read(int var1) {
      int var2 = 0;

      while(true) {
         if(var2 >= this.nrOfReadRegions) {
            var1 = 0;
            break;
         }

         ReadRegion var3 = this.readRegions[var2];
         if(var1 >= var3.from && var1 <= var3.until) {
            var1 = var3.handler.read(var1 - var3.from);
            break;
         }

         ++var2;
      }

      return var1;
   }

   public void set(int var1, int var2, ReadHandler var3) {
      ReadRegion[] var5 = this.readRegions;
      int var4 = this.nrOfReadRegions;
      this.nrOfReadRegions = var4 + 1;
      var5[var4] = new ReadRegion(var1, var2, var3);
   }

   public void set(int var1, int var2, WriteHandler var3) {
      WriteRegion[] var5 = this.writeRegions;
      int var4 = this.nrOfWriteRegions;
      this.nrOfWriteRegions = var4 + 1;
      var5[var4] = new WriteRegion(var1, var2, var3);
   }

   public void setBankAddress(int var1, int var2) {
   }

   public void setMR(int var1, int var2, int var3) {
      char[] var4;
      switch(var3) {
      case 0:
      case 2:
         var4 = new char[var2 + 1 - var1];
         this.set(var1, var2, (ReadHandler)(new MEMRead(var4)));
         this.readRegions[this.nrOfReadRegions - 1].memory = var4;
         break;
      case 1:
         this.set(var1, var2, (ReadHandler)(new MEMRead(this.rom)));
         this.readRegions[this.nrOfReadRegions - 1].memory = this.rom;
         break;
      case 3:
      case 4:
      case 5:
      case 6:
      case 7:
      case 8:
      case 9:
      case 10:
         var4 = new char[(var2 + 1 - var1) * this.banks];
         this.set(var1, var2, (ReadHandler)(new MEMReadBanked(var4)));
         this.readRegions[this.nrOfReadRegions - 1].memory = var4;
         break;
      default:
         throw new RuntimeException("MemoryMap: Unknown Reader Type " + var3);
      }

   }

   public void setMW(int var1, int var2, int var3) {
      switch(var3) {
      case 0:
         this.set(var1, var2, (WriteHandler)(new MEMWrite(this.findMemory(var1, var2))));
         break;
      case 1:
      case 2:
         this.set(var1, var2, (WriteHandler)(new ROMwrite()));
         break;
      case 3:
      case 4:
      case 5:
      case 6:
      case 7:
      case 8:
      case 9:
      case 10:
         this.set(var1, var2, (WriteHandler)(new MEMWriteBanked(this.findMemory(var1, var2))));
         break;
      default:
         throw new RuntimeException("MemoryMap: Unknown Writer Type " + var3);
      }

   }

   public void write(int var1, int var2) {
      for(int var3 = 0; var3 < this.nrOfWriteRegions; ++var3) {
         WriteRegion var4 = this.writeRegions[var3];
         if(var1 >= var4.from && var1 <= var4.until) {
            var4.handler.write(var1 - var4.from, var2);
            break;
         }
      }

   }

   public class MEMRead implements ReadHandler {
      public char[] memory;

      public MEMRead(char[] var2) {
         this.memory = var2;
      }

      public int read(int var1) {
         return this.memory[var1];
      }
   }

   public class MEMReadBanked implements ReadHandler {
      public int bank_address;
      public char[] memory;

      public MEMReadBanked(char[] var2) {
         this.memory = var2;
      }

      public int read(int var1) {
         return this.memory[this.bank_address + var1];
      }

      public void setBankAdr(int var1) {
         this.bank_address = var1;
      }
   }

   public class MEMWrite implements WriteHandler {
      public char[] memory;

      public MEMWrite(char[] var2) {
         this.memory = var2;
      }

      public void write(int var1, int var2) {
         this.memory[var1] = (char)var2;
      }
   }

   public class MEMWriteBanked implements WriteHandler {
      public int bank_address;
      public char[] memory;

      public MEMWriteBanked(char[] var2) {
         this.memory = var2;
      }

      public void setBankAdr(int var1) {
         this.bank_address = var1;
      }

      public void write(int var1, int var2) {
         this.memory[this.bank_address + var1] = (char)var2;
      }
   }

   public class ROMwrite implements WriteHandler {
      public void write(int var1, int var2) {
      }
   }
}
