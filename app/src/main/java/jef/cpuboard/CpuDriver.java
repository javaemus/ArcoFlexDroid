package jef.cpuboard;

import jef.cpu.Cpu;
import jef.map.IOReadPort;
import jef.map.IOWritePort;
import jef.map.InterruptHandler;
import jef.map.ReadMap;
import jef.map.WriteMap;

public class CpuDriver {
   public static final boolean IS_AUDIO_CPU = true;
   public static final boolean IS_NOT_AUDIO_CPU = false;
   public Cpu cpu;
   public int frq;
   public IOReadPort ior;
   public IOWritePort iow;
   public int ipf;
   public InterruptHandler irh;
   public boolean isAudioCpu = false;
   public ReadMap mra;
   public WriteMap mwa;

   public CpuDriver(Cpu var1, int var2, ReadMap var3, WriteMap var4, IOReadPort var5, IOWritePort var6, InterruptHandler var7, int var8) {
      this.cpu = var1;
      this.frq = var2;
      this.mra = var3;
      this.mwa = var4;
      this.ior = var5;
      this.iow = var6;
      this.irh = var7;
      this.ipf = var8;
   }

   public CpuDriver(Cpu var1, int var2, ReadMap var3, WriteMap var4, InterruptHandler var5, int var6) {
      this.cpu = var1;
      this.frq = var2;
      this.mra = var3;
      this.mwa = var4;
      this.irh = var5;
      this.ipf = var6;
      this.ior = new IOReadPort();
      this.iow = new IOWritePort();
   }
}
