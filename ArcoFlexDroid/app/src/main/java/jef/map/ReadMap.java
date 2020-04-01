package jef.map;

public interface ReadMap extends ReadHandler {
   int RD_BANK1 = 3;
   int RD_BANK2 = 4;
   int RD_BANK3 = 5;
   int RD_BANK4 = 6;
   int RD_BANK5 = 7;
   int RD_BANK6 = 8;
   int RD_BANK7 = 9;
   int RD_BANK8 = 10;
   int RD_NOP = 2;
   int RD_RAM = 0;
   int RD_ROM = 1;

   char[] getMemory();

   int getSize();

   void set(int var1, int var2, ReadHandler var3);

   void setBankAddress(int var1, int var2);

   void setMR(int var1, int var2, int var3);
}
