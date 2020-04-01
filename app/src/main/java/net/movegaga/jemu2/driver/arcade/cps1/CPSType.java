package net.movegaga.jemu2.driver.arcade.cps1;

public class CPSType {
   int controlReg;
   int cpsbAddr;
   int cpsbValue;
   int layerControl;
   int[] layerEnableMask;
   int multFactor1;
   int multFactor2;
   int multResultHi;
   int multResultLo;
   int[] prio;

   public CPSType(int var1, int var2, int var3, int var4, int var5, int var6, int var7, int[] var8, int var9, int[] var10) {
      this.cpsbAddr = var1;
      this.cpsbValue = var2;
      this.multFactor1 = var3;
      this.multFactor2 = var4;
      this.multResultLo = var5;
      this.multResultHi = var6;
      this.layerControl = var7;
      this.prio = var8;
      this.controlReg = var9;
      this.layerEnableMask = var10;
   }
}
