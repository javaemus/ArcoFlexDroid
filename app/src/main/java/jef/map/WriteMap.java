package jef.map;

public interface WriteMap extends WriteHandler {
   int WR_BANK1 = 3;
   int WR_BANK2 = 4;
   int WR_BANK3 = 5;
   int WR_BANK4 = 6;
   int WR_BANK5 = 7;
   int WR_BANK6 = 8;
   int WR_BANK7 = 9;
   int WR_BANK8 = 10;
   int WR_NOP = 2;
   int WR_RAM = 0;
   int WR_ROM = 1;

   int getSize();

   void set(int var1, int var2, WriteHandler var3);

   void setMW(int var1, int var2, int var3);
}
