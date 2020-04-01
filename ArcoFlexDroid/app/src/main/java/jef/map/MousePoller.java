package jef.map;

public class MousePoller {
   int x;
   int y;

   public ReadHandler createButtonReader(int var1, int var2, int var3) {
      return new ButtonReader(var1, var2, var3);
   }

   public ReadHandler createXReader() {
      return new ReadHandler() {
         public int read(int var1) {
            return MousePoller.this.getX();
         }
      };
   }

   public ReadHandler createYReader() {
      return new ReadHandler() {
         public int read(int var1) {
            return MousePoller.this.getY();
         }
      };
   }

   public int getX() {
      return this.x / 2 & 255;
   }

   public int getY() {
      return this.x / 2 & 255;
   }

   class ButtonReader implements ReadHandler {
      public ButtonReader(int var2, int var3, int var4) {
      }

      public int read(int var1) {
         return 0;
      }
   }
}
