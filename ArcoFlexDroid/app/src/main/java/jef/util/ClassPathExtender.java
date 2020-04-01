package jef.util;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

public class ClassPathExtender {
   private static final Class[] parameters = new Class[]{URL.class};

   public static void addFile(File var0) throws IOException {
      addURL(var0.toURL());
   }

   public static void addFile(String var0) throws IOException {
      addFile(new File(var0));
   }

   private static void addURL(URL var0) throws IOException {
      URLClassLoader var2 = (URLClassLoader)ClassLoader.getSystemClassLoader();

      try {
         Method var1 = URLClassLoader.class.getDeclaredMethod("addURL", parameters);
         var1.setAccessible(true);
         var1.invoke(var2, new Object[]{var0});
      } catch (Throwable var3) {
         var3.printStackTrace();
         throw new IOException("Error, could not add URL to system classloader");
      }
   }

   public static void setJarDir(File var0) {
      if(!var0.exists()) {
         System.out.println("Directory " + var0.getAbsolutePath() + " does not exist, no external jars present.");
      } else if(!var0.isDirectory()) {
         System.out.println("ERROR: directory " + var0.getAbsolutePath() + " is not a directory, and can\'t be added to the classpath.");
      } else {
         System.out.println("Adding all jars in " + var0.getAbsolutePath() + " to the classpath:");
         File[] var2 = var0.listFiles(new FilenameFilter() {
            public boolean accept(File var1, String var2) {
               return var2.toLowerCase().endsWith(".jar");
            }
         });

         for(int var1 = 0; var1 < var2.length; ++var1) {
            System.out.println("Adding " + var2[var1].getAbsolutePath());

            try {
               addFile(var2[var1]);
            } catch (IOException var3) {
               System.out.println(var3.getMessage());
               var3.printStackTrace();
            }
         }
      }

   }
}
