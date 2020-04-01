package jef.cpuboard;

import jef.cpu.Cpu;

public abstract class CpuBoard {
   public abstract void exec(int var1);

   public abstract Cpu getCpu();

   public abstract char[] getMem();

   public abstract int in(int var1);

   public abstract boolean init(CpuDriver var1);

   public abstract void interrupt(int var1, boolean var2);

   public abstract void out(int var1, int var2);

   public abstract int read16(int var1);

   public abstract int read16arg(int var1);

   public abstract int read8(int var1);

   public abstract int read8arg(int var1);

   public abstract int read8opc(int var1);

   public abstract void reset(boolean var1);

   public abstract void write16(int var1, int var2);

   public abstract void write16fast(int var1, int var2);

   public abstract void write8(int var1, int var2);

   public abstract void write8fast(int var1, int var2);
}
