package net.movegaga.jemu2.driver.msx.v9938;

public class DisplayTiming {
   public static final int CPU_DISPLAY = 171;
   public static final int CPU_HSYNC = 17;
   public static final int CPU_LINE = 229;
   public static final int CPU_L_BORDER = 9;
   public static final int CPU_L_ERASE = 17;
   public static final int CPU_R_BORDER = 9;
   public static final int CPU_R_ERASE = 17;
   public static final DisplayTiming NTSC192;
   public static final DisplayTiming NTSC212;
   public static final DisplayTiming PAL192;
   public static final DisplayTiming PAL212;
   public static final float SCALE_XTAL_CPU = 0.16776557F;
   public static final int XTAL_DISPLAY = 1024;
   public static final int XTAL_LINE = 1365;
   public static final int XTAL_L_BORDER = 56;
   public static final int XTAL_L_ERASE = 102;
   public static final int XTAL_R_BORDER = 57;
   public static final int XTAL_R_ERASE = 26;
   public static final int XTAL_SYNC = 100;
   public final int framesPerSec;
   public final int lineBottomBorderStart;
   public final int lineBottomEraseStart;
   public final int lineDisplayStart;
   public final int lineTopBorderStart;
   public final int lineTopEraseStart;
   public final int lineVSyncStart;
   public final int linesBottomBorder;
   public final int linesBottomErase;
   public final int linesDisplay;
   public final int linesPerFrame;
   public final int linesSync;
   public final int linesTopBorder;
   public final int linesTopErase;

   static {
      System.out.println("V9938 HORIZONTAL TIMING:");
      System.out.println("CPU_LINE     = 229");
      System.out.println("CPU_L_ERASE  = 17");
      System.out.println("CPU_L_BORDER = 9");
      System.out.println("CPU_DISPLAY  = 171");
      System.out.println("CPU_R_BORDER = 9");
      System.out.println("CPU_R_ERASE  = 17");
      System.out.println("CPU_HSYNC    = 17");
      PAL212 = new DisplayTiming(50, 313, 3, 13, 43, 212, 39, 3);
      PAL192 = new DisplayTiming(50, 313, 3, 13, 53, 192, 49, 3);
      NTSC212 = new DisplayTiming(60, 262, 3, 13, 16, 212, 15, 3);
      NTSC192 = new DisplayTiming(60, 262, 3, 13, 26, 212, 25, 3);
   }

   public DisplayTiming(int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8) {
      this.framesPerSec = var1;
      this.linesPerFrame = var2;
      this.linesSync = var3;
      this.linesTopErase = var4;
      this.linesTopBorder = var5;
      this.linesDisplay = var6;
      this.linesBottomBorder = var7;
      this.linesBottomErase = var8;
      this.lineTopEraseStart = 0;
      this.lineTopBorderStart = var4;
      this.lineDisplayStart = this.lineTopBorderStart + var5;
      this.lineBottomBorderStart = this.lineDisplayStart + var6;
      this.lineBottomEraseStart = this.lineBottomBorderStart + var7;
      this.lineVSyncStart = this.lineBottomEraseStart + this.linesBottomErase;
   }

   public void printTiming() {
      System.out.println("V9938 vertical timing:");
      System.out.println("fps                   = " + this.framesPerSec);
      System.out.println("lineTopEraseStart     = " + this.lineTopEraseStart);
      System.out.println("lineTopBorderStart    = " + this.lineTopBorderStart);
      System.out.println("lineDisplayStart      = " + this.lineDisplayStart);
      System.out.println("lineBottomBorderStart = " + this.lineBottomBorderStart);
      System.out.println("lineBottomEraseStart  = " + this.lineBottomEraseStart);
      System.out.println("lineLast              = " + this.lineVSyncStart);
   }
}
