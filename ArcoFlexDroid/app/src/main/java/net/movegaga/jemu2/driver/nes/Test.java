package net.movegaga.jemu2.driver.nes;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Test {
   static HashMap addr1;
   static HashMap addr2;
   static BufferedReader br1;
   static BufferedReader br2;
   static int ignored1;
   static int ignored2;
   static int line1;
   static int line2;
   static int pc1;
   static int pc2;
   static ArrayList unique = new ArrayList();

   public static void main(String[] var0) throws Exception {
      addr1 = new HashMap();
      addr2 = new HashMap();
      br1 = new BufferedReader(new FileReader(new File("D:\\eclipse\\workspace\\JEmu2v2\\final\\jemu.log")));
      br2 = new BufferedReader(new FileReader(new File("D:\\eclipse\\workspace\\JEmu2v2\\final\\nes\\nes")));

      while(nextLine1() != null) {
         ;
      }

      while(nextLine2() != null) {
         ;
      }

   }

   private static String nextLine1() throws IOException {
      String var0;
      while(true) {
         var0 = br1.readLine();
         ++line1;
         if(var0 != null && var0.startsWith("6502 :::")) {
            pc1 = Integer.parseInt(var0.substring(9, 13), 16);
            addr1.put(Integer.valueOf(pc1), Integer.valueOf(line1));
            break;
         }

         if(var0 == null) {
            var0 = null;
            break;
         }
      }

      return var0;
   }

   private static String nextLine2() throws IOException {
      String var0 = br2.readLine();
      ++line2;
      pc2 = Integer.parseInt(var0.substring(1, 5), 16);
      if(addr1.get(Integer.valueOf(pc2)) == null) {
         System.out.println("Line " + line2 + ":" + var0);
         System.exit(0);
      }

      return var0;
   }
}
