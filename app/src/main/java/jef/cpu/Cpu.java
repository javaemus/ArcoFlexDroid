package jef.cpu;

import jef.cpuboard.CpuBoard;

public interface Cpu {
   int INTERRUPT_TYPE_FIRQ = 2;
   int INTERRUPT_TYPE_IGNORE = -1;
   int INTERRUPT_TYPE_IRQ = 0;
   int INTERRUPT_TYPE_NMI = 1;
   int PROPERTY_Z80_IRQ_VECTOR = 0;
   boolean TRACE = false;

   void exec(int var1);

   int getCyclesLeft();

   int getDebug();

   long getInstruction();

   String getTag();

   boolean init(CpuBoard var1, int var2);

   void interrupt(int var1, boolean var2);

   void reset();

   void setDebug(int var1);

   void setProperty(int var1, int var2);

   void setTag(String var1);
}
