package jef.util;

public class Statistics {
   private static int frameNr;
   private static long frameTimeStart;
   public static int frames_per_second;
   private static long pixels;
   private static long pixelsErr;
   public static long pixels_err_per_second;
   public static long pixels_per_second;

   public static void frame() {
      if(System.currentTimeMillis() - frameTimeStart > 1000L) {
         frameTimeStart = System.currentTimeMillis();
         frames_per_second = frameNr;
         pixels_per_second = pixels;
         pixels_err_per_second = pixelsErr;
         frameNr = 0;
         pixels = 0L;
         pixelsErr = 0L;
      } else {
         ++frameNr;
      }

   }

   public static void pixels(int var0) {
      pixels += (long)var0;
   }

   public static void pixelsErr(int var0) {
      pixelsErr += (long)var0;
   }
}
