package jef.util.time;

import net.movegaga.jemu2.Config;

import java.lang.reflect.Method;

public class Time {
   static TimeRT impl;
   public static boolean nanoDisabled = true;
   static boolean supportsNano;

   static {
      init();
   }

   public static long getMillis() {
      return impl.getMillis();
   }

   public static double getMillisDouble() {
      return impl.getMillisDouble();
   }

   public static long getNanos() {
      return impl.getNanos();
   }

   static void init() {
      impl = new TimeCurMillisImpl();
      supportsNano = false;
      Method[] var1 = System.class.getMethods();

      for(int var0 = 0; var0 < var1.length; ++var0) {
         if(var1[var0].getName().equals("nanoTime")) {
            supportsNano = true;
            System.out.println("JRE supports Nano Timer");
            if(!nanoDisabled) {
               impl = new TimeNanosImpl();
            }
            break;
         }
      }

   }

   public static void setNanoEnabled(boolean var0) {
      boolean var1;
      if(var0) {
         var1 = false;
      } else {
         var1 = true;
      }

      nanoDisabled = var1;
      init();
      Config.set("system.nanotime", Boolean.toString(var0));
   }

   public static boolean supportsNano() {
      return supportsNano;
   }
}
