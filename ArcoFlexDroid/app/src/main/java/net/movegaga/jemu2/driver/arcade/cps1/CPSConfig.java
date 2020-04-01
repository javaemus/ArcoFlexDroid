package net.movegaga.jemu2.driver.arcade.cps1;

public class CPSConfig {
   int bank_scroll1;
   int bank_scroll2;
   int bank_scroll3;
   CPSType cpsType;
   int end_scroll2;
   int end_scroll3;
   int kludge;
   String name;
   int start_scroll2;
   int start_scroll3;

   public CPSConfig(String var1, CPSType var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9, int var10) {
      this.name = var1;
      this.cpsType = var2;
      this.bank_scroll1 = var3;
      this.bank_scroll2 = var4;
      this.bank_scroll3 = var5;
      this.start_scroll2 = var6;
      this.end_scroll2 = var7;
      this.start_scroll3 = var8;
      this.end_scroll3 = var9;
      this.kludge = var10;
   }
}
