package jef.util.time;

public class Throttle {
   static final int DEFAULT_TARGET_FPS = 60;
   static final int DEFAULT_THROTTLE_STEP = 1;
   static final int FRAMES_UNTIL_THROTTLE_RECALC = 40;
   static final float MAX_FPS_DEVIATION = 0.1F;
   static final int MAX_FRAME_SKIP = 5;
   static final int MAX_THROTTLE_STEP = 5;
   static final int MIN_THROTTLE_STEP = 1;
   static final boolean TRY_ALT_SKIP_CALC = true;
   static boolean autoFS;
   static float avgFPS = 0.0F;
   static long fps;
   static int frameDuration;
   static int frameNumber;
   static int fskip;
   static int maxFPS;
   static int minFPS;
   static long minimumSleep;
   static long recalcCount = 0L;
   static long sleep;
   static long sumFPS = 0L;
   static long t;
   static int targetFPS;
   static long tempT;
   static Thread thread;
   static boolean throttle;
   static int throttleStep;

   public static void enable(boolean var0) {
      throttle = var0;
   }

   public static void enableAutoFrameSkip(boolean var0) {
      autoFS = var0;
   }

   public static float getAverageFPS() {
      return avgFPS;
   }

   public static int getFPS() {
      return (int)fps;
   }

   public static int getFrameSkip() {
      return fskip;
   }

   public static long getSleep() {
      return sleep;
   }

   public static int getTargetFPS() {
      return targetFPS;
   }

   public static void init(int var0, Thread var1) {
      targetFPS = var0 + 3;
      thread = var1;
      throttle = true;
      autoFS = true;
      fskip = 0;
      throttleStep = 1;
      frameNumber = 0;
      recalcCount = 0L;
      sumFPS = 0L;
      avgFPS = 0.0F;
      fps = 0L;
      minimumSleep = 0L;
      sleep = minimumSleep;
      throttleStep = 1;
      minFPS = targetFPS - (int)((float)targetFPS * 0.1F);
      if(minFPS == targetFPS) {
         minFPS = targetFPS - 1;
      }

      maxFPS = targetFPS + (int)((float)targetFPS * 0.1F);
      if(maxFPS == targetFPS) {
         maxFPS = targetFPS + 1;
      }

      frameDuration = 1000 / targetFPS;
      fps = (long)targetFPS;
      t = System.currentTimeMillis();
   }

   public static boolean isAutoFrameSkip() {
      return autoFS;
   }

   public static boolean isEnabled() {
      return throttle;
   }

   private static void recalcTiming() {
      tempT = System.currentTimeMillis();
      long var0 = (tempT - t) / 40L;
      if(var0 == 0L) {
         fps = (long)targetFPS;
      } else {
         fps = 1000L / var0;
      }

      t = tempT;
      ++recalcCount;
      sumFPS += fps;
      avgFPS = (float)((int)(sumFPS * 100L / recalcCount)) / 100.0F;
      if(throttle) {
         if(fps < (long)minFPS) {
            if(sleep > minimumSleep) {
               sleep -= ((long)targetFPS - fps) / (long)(targetFPS - minFPS);
            }

            if(sleep <= minimumSleep) {
               ++throttleStep;
               if(throttleStep > 5) {
                  throttleStep = 5;
                  if(autoFS) {
                     throttleStep = 1;
                     ++fskip;
                     if(fskip > 5) {
                        fskip = 5;
                        throttleStep = 5;
                     }
                  }
               } else {
                  sleep += minimumSleep;
               }
            }
         } else if(fps > (long)maxFPS) {
            sleep += (fps - (long)targetFPS) / (long)(maxFPS - targetFPS);
            if(sleep > (long)(1000 / targetFPS - 1)) {
               --throttleStep;
               if(throttleStep < 1) {
                  throttleStep = 1;
                  if(autoFS) {
                     throttleStep = 5;
                     --fskip;
                     if(fskip < 0) {
                        fskip = 0;
                        throttleStep = 1;
                     }
                  }
               } else {
                  sleep -= minimumSleep;
                  if(sleep < minimumSleep) {
                     sleep = minimumSleep;
                  }
               }
            }
         }
      }

   }

   public static void setFrameSkip(int var0) {
      fskip = var0;
   }

   public static boolean skipFrame() {
      boolean var0;
      if(fskip != 0 && frameNumber % (fskip + 1) != 0) {
         var0 = true;
      } else {
         var0 = false;
      }

      return var0;
   }

   public static void throttle() {
      boolean var1 = throttle;
      boolean var0;
      if(frameNumber % throttleStep == 0) {
         var0 = true;
      } else {
         var0 = false;
      }

      if(var1 & var0) {
         try {
            Thread.sleep(sleep);
         } catch (Exception var3) {
            ;
         }
      }

      if(frameNumber < 40) {
         ++frameNumber;
      } else {
         frameNumber = 0;
         recalcTiming();
      }

   }
}
