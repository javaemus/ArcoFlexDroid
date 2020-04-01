/*

Java Emulation Framework

This library contains a framework for creating emulation software.

Copyright (C) 2002 Erik Duijs (erikduijs@yahoo.com)

*/

package jef.util;

import net.arcoflexdroid.ArcoFlexDroid;

import net.movegaga.jemu2.JEmu2;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * @author edy
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class ConfigFile {

   String[][] contents;

   public ConfigFile(int file) {
      ArrayList list = new ArrayList();

      String thisLine;
      try {
         //InputStream is = getClass().getResourceAsStream(file);
         InputStream is = ArcoFlexDroid.mm.getResources().openRawResource(file);
         if (is != null) {
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            while ((thisLine = br.readLine()) != null) {
               thisLine.trim();
               if (thisLine.length() > 0 && !thisLine.startsWith("#")) {
                  String[] s = thisLine.split(",");
                  for (int i = 0; i < s.length; i++) {
                     s[i] = s[i].trim();
                  }
                  list.add(s);
               }
            }
            contents = new String[list.size()][];
            for (int i = 0; i < list.size(); i++) {
               String[] record = (String[])list.get(i);
               contents[i] = record;
            }
         }

      } catch (Exception e) {
         JEmu2.log(e);
         e.printStackTrace();
      }
   }

   public String[][] getContents() {
      return contents;
   }

}
