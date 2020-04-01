package jef.util;

public class INT32 {
   public int value;

   public void limit(int var1, int var2) {
      if(this.value > var2) {
         this.value = var2;
      } else if(this.value < var1) {
         this.value = var1;
      }

   }
}
