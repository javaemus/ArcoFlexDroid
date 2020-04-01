package jef.util.time;

import jef.machine.Emulator;

public class Timer {
   static final long RESOLUTION = 1000L;
   private static double _timeNow;
   private static double _timeThen;
   private static boolean autoFrameSkip = false;
   private static int checkSkipCurFrame;
   private static int checkSkipFrames = 10;
   public static int currentFPS;
   public static int frameSkip = 0;
   private static Emulator machine;
   private static int skippedFrames = 0;
   private static double timeLate;
   private static double timeNow;
   private static double timeThen;

   public static boolean checkSkip(int var0) {
      int var1 = checkSkipCurFrame - 1;
      checkSkipCurFrame = var1;
      if(var1 <= 0) {
         checkSkipCurFrame = checkSkipFrames;
         if(autoFrameSkip) {
            _timeNow = Time.getMillisDouble();
            currentFPS = (int)Math.round(1000.0D / ((_timeNow - _timeThen) / (double)checkSkipFrames));
            if(currentFPS < var0 - 1 - 2) {
               if(frameSkip < 5) {
                  ++frameSkip;
               }
            } else if(frameSkip > 0) {
               --frameSkip;
            }

            _timeThen = _timeNow;
         }
      }

      var0 = skippedFrames;
      skippedFrames = var0 + 1;
      boolean var2;
      if(var0 >= frameSkip) {
         skippedFrames = 0;
         var2 = false;
      } else {
         var2 = true;
      }

      return var2;
   }

   public static int curFrameSkip() {
      return frameSkip;
   }

   public static boolean getAutoFrameSkip() {
      return autoFrameSkip;
   }

   public static double getFrameProgress() {
      return machine.getProgress();
   }

   public static void reset() {
      timeLate = 0.0D;
      timeThen = 0.0D;
      timeNow = 0.0D;
   }

   public static void setAutoFrameSkip(boolean var0) {
      autoFrameSkip = var0;
   }

   public static void setCheckSkipFrames(int var0) {
      checkSkipFrames = var0;
   }

   public static void setMachine(Emulator var0) {
      machine = var0;
   }

   public static void sync(int var0) {
      double var1 = (double)((long)(1000.0D / (double)var0 + timeThen));

      for(timeNow = Time.getMillisDouble(); var1 > timeNow + timeLate; timeNow = Time.getMillisDouble()) {
         try {
            Thread.sleep(1L);
         } catch (InterruptedException var4) {
            ;
         }
      }

      if(var1 < timeNow) {
         timeLate = timeNow - var1;
      } else {
         timeLate = 0.0D;
      }

      timeThen = timeNow;
   }
}
