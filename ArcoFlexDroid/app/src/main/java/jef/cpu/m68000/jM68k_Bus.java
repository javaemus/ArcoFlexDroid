package jef.cpu.m68000;

public interface jM68k_Bus {
   int BYTE = 1;
   int LONG = 4;
   int WORD = 2;

   int fetchOpByte(int var1);

   int fetchOpLong(int var1);

   int fetchOpWord(int var1);

   int readByte(int var1);

   int readLong(int var1);

   int readWord(int var1);

   void reset();

   void writeByte(int var1, int var2);

   void writeLong(int var1, int var2);

   void writeWord(int var1, int var2);
}
