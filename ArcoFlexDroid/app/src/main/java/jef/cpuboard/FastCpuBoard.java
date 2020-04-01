package jef.cpuboard;

import jef.cpu.Cpu;
import jef.map.InterruptHandler;
import jef.map.ReadHandler;
import jef.map.ReadMap;
import jef.map.WriteHandler;
import jef.map.WriteMap;

public class FastCpuBoard extends CpuBoard {
   public Cpu cpu;
   public int frq;
   private ReadHandler ior;
   private WriteHandler iow;
   public int ipf;
   public InterruptHandler irqHandler;
   public char[] mem;
   private ReadMap mra;
   private WriteMap mwa;

   public void exec(int var1) {
      this.cpu.exec(var1);
   }

   public Cpu getCpu() {
      return this.cpu;
   }

   public char[] getMem() {
      return this.mem;
   }

   public int in(int var1) {
      return this.ior.read(var1);
   }

   public boolean init(CpuDriver var1) {
      this.cpu = var1.cpu;
      this.frq = var1.frq;
      this.mra = var1.mra;
      this.mwa = var1.mwa;
      this.ior = var1.ior;
      this.iow = var1.iow;
      this.mem = var1.mra.getMemory();
      this.irqHandler = var1.irh;
      this.ipf = var1.ipf;
      this.cpu.init(this, 0);
      return true;
   }

   public void interrupt(int var1, boolean var2) {
      this.cpu.interrupt(var1, var2);
   }

   public void out(int var1, int var2) {
      this.iow.write(var1, var2);
   }

   public int read16(int var1) {
      return this.read8(var1) | this.read8(var1 + 1) << 8;
   }

   public int read16arg(int var1) {
      return this.mem[var1] | this.mem[var1 + 1] << 8;
   }

   public int read8(int var1) {
      return this.mra.read(var1);
   }

   public int read8arg(int var1) {
      return this.mem[var1];
   }

   public int read8opc(int var1) {
      return this.mem[var1];
   }

   public void reset(boolean var1) {
      this.cpu.reset();
      if(var1) {
         for(int var2 = 0; var2 < this.mem.length; ++var2) {
            this.mem[var2] = 0;
         }
      }

   }

   public void write16(int var1, int var2) {
      this.write8(var1 + 1, var2 >> 8);
      this.write8(var1, var2 & 255);
   }

   public void write16fast(int var1, int var2) {
      this.mem[var1 + 1] = (char)(var2 >> 8);
      this.mem[var1] = (char)(var2 & 255);
   }

   public void write8(int var1, int var2) {
      this.mwa.write(var1, var2);
   }

   public void write8fast(int var1, int var2) {
      this.mem[var1] = (char)var2;
   }
}
