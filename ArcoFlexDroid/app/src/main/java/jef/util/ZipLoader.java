package jef.util;

import net.arcoflexdroid.ArcoFlexDroid;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ZipLoader {
   byte[] buffer = new byte[1024];
   int id;
   ArrayList requests = new ArrayList();
   String zip;

   public ZipLoader(int var1) {
      this.id = var1;
   }

   public ZipLoader(String var1) {
      this.zip = var1 + ".zip";
   }

   private boolean check() {
      System.out.println("Checking results...");
      boolean var2 = true;

      for(int var1 = 0; var1 < this.requests.size(); ++var1) {
         Request var3 = (Request)this.requests.get(var1);
         var2 &= var3.loaded;
         if(!var3.loaded) {
            System.out.println("ERROR: Request for CRC " + Integer.toHexString(var3.romCRC) + " not completed!");
         }
      }

      return var2;
   }

   private Request findRequest(int var1, String var2) {
      int var3 = 0;

      Request var5;
      while(true) {
         Request var4;
         if(var3 >= this.requests.size()) {
            for(var1 = 0; var1 < this.requests.size(); ++var1) {
               var4 = (Request)this.requests.get(var1);
               if(var4.romCRC == 0 && var4.name != null && var4.name.equals(var2)) {
                  var5 = var4;
                  return var5;
               }
            }

            var5 = null;
            break;
         }

         var4 = (Request)this.requests.get(var3);
         if(var4.romCRC == var1) {
            var5 = var4;
            break;
         }

         ++var3;
      }

      return var5;
   }

   private void load(ZipInputStream var1, Request var2) throws IOException {
      int var6 = var2.from;
      int var7 = var2.length;

      while(true) {
         int var8 = var1.read(this.buffer);
         if(var8 == -1 || var7 <= 0) {
            var2.loaded = true;
            return;
         }

         int var3 = 0;
         int var5 = var6;
         int var4 = var7;

         while(true) {
            var7 = var4;
            var6 = var5;
            if(var3 >= var8) {
               break;
            }

            var2.writeMem(var5, this.buffer[var3] + 256 & 255);
            var6 = var3;
            if(var2.bytesPerRead == 2) {
               byte[] var9 = this.buffer;
               var6 = var3 + 1;
               var2.writeMem(var5 + 1, var9[var6] + 256 & 255);
            }

            var5 += var2.offset;
            var4 -= var2.bytesPerRead;
            var3 = var6 + 1;
         }
      }
   }

   public void load() {
      System.out.println("Start loading from " + zip);
      try {
         //if (!JEmu2.IS_APPLET) {
         //   RomCache.check(zip);
         //}
         //URL zipURL = Thread.currentThread().getContextClassLoader().getResource(zip);
         System.out.println("ROM: "+ArcoFlexDroid.mm.getPrefsHelper().getInstallationDIR() + "roms/" + zip);
         URL zipURL = new URL("file://"+ArcoFlexDroid.mm.getPrefsHelper().getInstallationDIR() + "roms/" + zip);
         ZipInputStream zis = new ZipInputStream(zipURL.openStream());

         ZipEntry ze = null;

         while ((ze = zis.getNextEntry()) != null) {
            Request rq = findRequest((int) ze.getCrc(), ze.getName());
            if (rq != null) {
               System.out.println("Loading '" + ze.getName() + "' (" + Long.toHexString(ze.getCrc()) + ")...");
               load(zis, rq);
            }
         }

         zis.close();
         System.out.println("Finished loading from " + zip);
         check();
      } catch (Exception e) {
         throw new RuntimeException(e);
      }
   }

   public void queue(char[] var1, int var2, int var3, int var4) {
      this.requests.add(new Request(var1, var2, var3, var4));
   }

   public void queue(char[] var1, int var2, int var3, int var4, int var5) {
      this.requests.add(new Request(var1, var2, var3, var4, var5));
   }

   public void queue(char[] var1, int var2, int var3, int var4, int var5, int var6) {
      this.requests.add(new Request(var1, var2, var3, var4, var5, var6));
   }

   public void queue(int[] var1, int var2, int var3, int var4) {
      this.requests.add(new Request(var1, var2, var3, var4));
   }

   public void queue(int[] var1, int var2, int var3, int var4, int var5) {
      this.requests.add(new Request(var1, var2, var3, var4, var5));
   }

   class Request {
      int bytesPerRead;
      int from;
      int length;
      boolean loaded;
      private char[] mem;
      private int[] mem32;
      String name;
      int offset;
      int romCRC;

      public Request(char[] var2, int var3, int var4, int var5) {
         this.init((char[])var2, (String)null, var3, var4, var5, 1, 1);
      }

      public Request(char[] var2, int var3, int var4, int var5, int var6) {
         this.init((char[])var2, (String)null, var3, var4, var5, var6, 1);
      }

      public Request(char[] var2, int var3, int var4, int var5, int var6, int var7) {
         this.init((char[])var2, (String)null, var3, var4, var5, var6, var7);
      }

      public Request(char[] var2, String var3, int var4, int var5, int var6) {
         this.init((char[])var2, var3, var4, var5, var6, 1, 1);
      }

      public Request(char[] var2, String var3, int var4, int var5, int var6, int var7) {
         this.init((char[])var2, var3, var4, var5, var6, var7, 1);
      }

      public Request(char[] var2, String var3, int var4, int var5, int var6, int var7, int var8) {
         this.init(var2, var3, var4, var5, var6, var7, var8);
      }

      public Request(int[] var2, int var3, int var4, int var5) {
         this.init((int[])var2, (String)null, var3, var4, var5, 1, 1);
      }

      public Request(int[] var2, int var3, int var4, int var5, int var6) {
         this.init((int[])var2, (String)null, var3, var4, var5, var6, 1);
      }

      public Request(int[] var2, int var3, int var4, int var5, int var6, int var7) {
         this.init((int[])var2, (String)null, var3, var4, var5, var6, var7);
      }

      public Request(int[] var2, String var3, int var4, int var5, int var6) {
         this.init((int[])var2, var3, var4, var5, var6, 1, 1);
      }

      public Request(int[] var2, String var3, int var4, int var5, int var6, int var7) {
         this.init((int[])var2, var3, var4, var5, var6, var7, 1);
      }

      public Request(int[] var2, String var3, int var4, int var5, int var6, int var7, int var8) {
         this.init(var2, var3, var4, var5, var6, var7, var8);
      }

      private void init(char[] var1, String var2, int var3, int var4, int var5, int var6, int var7) {
         this.mem = var1;
         this.name = var2;
         this.romCRC = var3;
         this.from = var4;
         this.length = var5;
         this.offset = var6;
         this.bytesPerRead = var7;
         this.loaded = false;
      }

      private void init(int[] var1, String var2, int var3, int var4, int var5, int var6, int var7) {
         this.mem32 = var1;
         this.name = var2;
         this.romCRC = var3;
         this.from = var4;
         this.length = var5;
         this.offset = var6;
         this.bytesPerRead = var7;
         this.loaded = false;
      }

      public void writeMem(int var1, int var2) {
         if(this.mem != null) {
            this.mem[var1] = (char)var2;
         } else if(this.mem32 != null) {
            this.mem32[var1] = var2;
         }

      }
   }
}
