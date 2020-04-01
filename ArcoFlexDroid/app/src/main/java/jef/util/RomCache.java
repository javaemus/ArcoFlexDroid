package jef.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.InputStream;
import java.net.URL;

public class RomCache {
   private static boolean inited = false;
   private static String localDir;
   private static String remoteURL;
   private static int size;

   public static void check(String var0) throws Exception {
      System.out.println("Checking classpath for ROM...");
      URL var3 = Thread.currentThread().getContextClassLoader().getResource(var0);
      boolean var1 = true;
      InputStream var2 = null;

      label26: {
         InputStream var5;
         try {
            var5 = var3.openStream();
         } catch (Exception var4) {
            var1 = false;
            break label26;
         }

         var2 = var5;
      }

      if(var2 == null) {
         var1 = false;
      }

      if(!var1) {
         if(!inited) {
            System.out.println("File " + var0 + "could not be found.");
            throw new FileNotFoundException("File " + var0 + "could not be found.");
         }

         System.out.println("Not found -> starting download");
         downloadZip(var0, new File(localDir + "/" + var0));
         resizeCache();
      } else {
         System.out.println(var0 + " found.");
      }

   }

   private static void downloadZip(String var0, File var1) throws Exception {
      System.out.println("downloading " + var0 + " ...");
      long var5 = System.currentTimeMillis();
      BufferedInputStream var8 = new BufferedInputStream((new URL(remoteURL + "/" + var0)).openStream());
      FileOutputStream var7 = new FileOutputStream(var1, false);
      byte[] var9 = new byte[1024];
      int var3 = 0;

      while(true) {
         int var4 = var8.read(var9);
         if(var4 == -1) {
            var7.flush();
            var7.close();
            float var2 = (float)(System.currentTimeMillis() - var5) / 1000.0F;
            var2 = (float)var3 / 1024.0F / var2;
            System.out.println("Download complete (" + var3 + " bytes downloaded, " + var2 + " KB/sec)");
            return;
         }

         var7.write(var9, 0, var4);
         var3 += var4;
      }
   }

   public static void init(String var0, String var1, int var2) {
      System.out.println("Initializing cache...");
      localDir = var0;
      remoteURL = var1;
      size = var2;
      inited = true;
   }

   private static void resizeCache() {
      File[] var7 = (new File(localDir)).listFiles(new FilenameFilter() {
         public boolean accept(File var1, String var2) {
            return var2.endsWith(".zip");
         }
      });

      for(int var1 = var7.length; var1 > size; --var1) {
         System.out.println("Resizing cache...");
         long var5 = Long.MAX_VALUE;
         int var2 = -1;

         for(int var0 = 0; var0 < var1; ++var0) {
            boolean var3;
            if(var7[var0] != null) {
               var3 = true;
            } else {
               var3 = false;
            }

            boolean var4;
            if(var7[var0].lastModified() < var5) {
               var4 = true;
            } else {
               var4 = false;
            }

            if(var3 & var4) {
               var2 = var0;
               var5 = var7[var0].lastModified();
            }
         }

         System.out.println("Deleting " + var7[var2].getName());
         var7[var2].delete();
         var7[var2] = null;
      }

   }
}
