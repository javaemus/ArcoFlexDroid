package net.movegaga.jemu2.netplay;

public class ControllerUpdate {
   public static final int TYPE_ANALOG = 2;
   public static final int TYPE_KEY = 1;
   public final int controller;
   public final int id;
   public final int type;
   public final int value;

   public ControllerUpdate(int var1, int var2, int var3, int var4) {
      this.id = var1;
      this.type = var2;
      this.controller = var3;
      this.value = var4;
   }

   public String toString() {
      return "CTRL  type=" + this.type + ", ctrl=" + this.controller + ", value=" + this.value;
   }
}
