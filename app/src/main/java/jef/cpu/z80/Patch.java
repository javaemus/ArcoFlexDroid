package jef.cpu.z80;

public abstract class Patch {
   protected Z80 z80;

   public Patch(Z80 var1) {
      this.z80 = var1;
   }

   public abstract void exec(int var1);
}
