package jef.cpu.m6502;

class DebugInstruction {
   protected String code = "";
   protected int numOperands = 0;
   protected int opcode = 0;

   public DebugInstruction(int var1, String var2, int var3) {
      this.opcode = var1;
      this.code = var2;
      this.numOperands = var3;
   }
}
