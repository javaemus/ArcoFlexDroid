package net.movegaga.jemu2;

import java.io.File;

import jef.util.ClassPathExtender;
import jef.util.RomCache;

public class JEmu2 {
   public static boolean IS_APPLET = true;
   public static final String VERSION = "v4.1.4";
   public static ControlsConfig controlsConfig;

   static {
      System.out.println("JEmu2 v4.1.4");
   }

   public static void log(Exception var0) {
      log("***EXCEPTION*** " + var0.toString());
      StackTraceElement[] var2 = var0.getStackTrace();

      for(int var1 = 0; var1 < var2.length && var1 < 7; ++var1) {
         log(var2[var1].toString());
      }

   }

   public static void log(String var0) {
      System.out.println(var0);
   }

   public static void main(String[] var0) {
      if(System.getProperty("jemu.priority", "normal").equals("low")) {
         Thread.currentThread().setPriority(1);
      } else {
         Thread.currentThread().setPriority(5);
      }

      if(!IS_APPLET) {
         try {
            StringBuilder var1 = new StringBuilder(String.valueOf(System.getProperty("user.home")));
            File var4 = new File(var1.append("/net/movegaga").toString());
            if(!var4.exists()) {
               var4.mkdir();
            }

            var1 = new StringBuilder(String.valueOf(System.getProperty("user.home")));
            var4 = new File(var1.append("/net/movegaga/.cache").toString());
            if(!var4.exists()) {
               var4.mkdir();
            }

            ClassPathExtender.addFile(var4);
            RomCache.init(var4.getAbsolutePath(), "http://www.gagaplay.com/jemu2/applet", 999);
         } catch (Exception var3) {
            var3.printStackTrace();
         }
      }

      String var6 = System.getProperty("jemu.driver", "mspacman");
      new JEmu2();
      EmulatorFactory var5 = new EmulatorFactory();

      try {
         var5.createEmulator(var6);
      } catch (Exception var2) {
         var2.printStackTrace();
      }

   }
}
