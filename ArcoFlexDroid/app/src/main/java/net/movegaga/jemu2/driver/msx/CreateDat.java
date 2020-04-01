package net.movegaga.jemu2.driver.msx;

import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

public class CreateDat {
   String inputFile;
   ArrayList list;
   String outputFile;
   String outputGameList;
   ZipFile zipFile;

   public CreateDat(String var1) {
      this.inputFile = var1;
      this.outputFile = var1 + ".dat";
      this.outputGameList = var1 + ".gamelist";
      this.list = new ArrayList();

      try {
         ZipFile var3 = new ZipFile(this.inputFile);
         this.zipFile = var3;
      } catch (IOException var2) {
         var2.printStackTrace();
      }

   }

   private String guessMapperType(Entry var1) throws IOException {
      String var15;
      if(var1.size <= 49152L) {
         var15 = ",NORMAL,";
      } else {
         InputStream var14 = this.zipFile.getInputStream(this.zipFile.getEntry(var1.name));
         char[] var12 = new char[(int)var1.size];
         byte[] var13 = new byte[1024];
         int var2 = 0;

         while(true) {
            int var4 = var14.read(var13);
            int var3;
            if(var4 <= 0) {
               int var9 = 0;
               int var6 = 0;
               int var7 = 0;
               int var8 = 0;

               for(int var5 = 0; var5 < var12.length - 1; var8 = var2) {
                  var3 = var9;
                  int var10 = var6;
                  int var11 = var5;
                  var4 = var7;
                  var2 = var8;
                  if(var12[var5] == 50) {
                     var11 = var5 + 1;
                     switch(var12[var11] + var12[var11 + 1] * 256) {
                     case 20480:
                        var2 = var8 + 1;
                        var3 = var9;
                        var10 = var6;
                        var4 = var7;
                        break;
                     case 24576:
                        var3 = var9 + 1;
                        var10 = var6;
                        var4 = var7;
                        var2 = var8;
                        break;
                     case 26624:
                     case 30720:
                        var10 = var6 + 1;
                        var3 = var9;
                        var4 = var7;
                        var2 = var8;
                        break;
                     case 28672:
                        var3 = var9 + 1;
                        var10 = var6;
                        var4 = var7;
                        var2 = var8;
                        break;
                     case 32768:
                        var4 = var7 + 1;
                        var3 = var9;
                        var10 = var6;
                        var2 = var8;
                        break;
                     case 36864:
                        var2 = var8 + 1;
                        var3 = var9;
                        var10 = var6;
                        var4 = var7;
                        break;
                     case 40960:
                        var4 = var7 + 1;
                        var3 = var9;
                        var10 = var6;
                        var2 = var8;
                        break;
                     case 45056:
                        var2 = var8 + 1;
                        var3 = var9;
                        var10 = var6;
                        var4 = var7;
                        break;
                     default:
                        var2 = var8;
                        var4 = var7;
                        var10 = var6;
                        var3 = var9;
                     }
                  }

                  var5 = var11 + 1;
                  var9 = var3;
                  var6 = var10;
                  var7 = var4;
               }

               System.out.println("Guess for " + var1.name + ":");
               System.out.println("cASCII16   : " + var9);
               System.out.println("cASCII8    : " + var6);
               System.out.println("cKONAMI8   : " + var7);
               System.out.println("cKONAMISCC : " + var8);
               if(var9 > var6 && var9 > var7 && var9 > var8) {
                  var15 = ",ASCII_16K,";
               } else if(var6 > var9 && var6 > var7 && var6 > var8) {
                  var15 = ",ASCII_8K,";
               } else if(var7 > var9 && var7 > var6 && var7 > var8) {
                  var15 = ",KONAMI_8K,";
               } else if(var8 > var9 && var8 > var7 && var8 > var6) {
                  var15 = ",KONAMI_8K_SCC,";
               } else {
                  var15 = ",ASCII_8K,";
               }
               break;
            }

            for(var3 = 0; var3 < var4; ++var2) {
               var12[var2] = (char)(var13[var3] + 256 & 255);
               ++var3;
            }
         }
      }

      return var15;
   }

   public static void main(String[] var0) throws IOException {
      (new CreateDat(var0[0])).run();
   }

   private void run() throws IOException {
      ZipInputStream var3 = new ZipInputStream(Thread.currentThread().getContextClassLoader().getResource(this.inputFile).openStream());

      while(true) {
         ZipEntry var2 = var3.getNextEntry();
         if(var2 == null) {
            var3.close();
            FileWriter var7 = new FileWriter(this.outputGameList);
            FileWriter var6 = new FileWriter(this.outputFile);

            for(int var1 = 0; var1 < this.list.size(); ++var1) {
               Entry var5 = (Entry)this.list.get(var1);
               String var4 = "msx1_" + var5.name.replace(' ', '_');
               String var8 = this.guessMapperType(var5);
               var7.write(var4 + "|" + var5.name + "|yes|no\n");
               var6.write(var4 + var8 + this.inputFile.substring(0, this.inputFile.length() - 4) + "," + var5.name + "," + Long.toHexString(var5.crc) + "," + Long.toHexString(var5.size) + "\n");
            }

            var6.flush();
            var7.flush();
            var6.close();
            var7.close();
            return;
         }

         this.list.add(new Entry(var2.getName(), var2.getCrc(), var2.getSize()));
      }
   }

   class Entry {
      long crc;
      String name;
      long size;

      Entry(String var2, long var3, long var5) {
         this.name = var2;
         this.crc = var3;
         this.size = var5;
      }
   }
}
