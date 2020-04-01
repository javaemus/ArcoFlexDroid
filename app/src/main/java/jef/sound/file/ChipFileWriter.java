package jef.sound.file;

import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

public class ChipFileWriter {
   static final boolean TRACE = true;
   DataOutputStream dos;

   public ChipFileWriter(String var1) throws FileNotFoundException {
      this.dos = new DataOutputStream(new FileOutputStream(var1));
   }

   public void close() {
      try {
         this.dos.flush();
         this.dos.close();
      } catch (IOException var2) {
         var2.printStackTrace();
      }

   }

   public void write(Frame var1) {
      try {
         this.dos.writeByte(var1.chip);
         this.dos.writeLong(var1.timestamp);
         this.dos.writeByte(var1.port);
         this.dos.writeByte(var1.value);
         PrintStream var3 = System.out;
         StringBuilder var2 = new StringBuilder("ChipFileWriter : written ");
         var3.println(var2.append(var1).toString());
      } catch (IOException var4) {
         var4.printStackTrace();
         this.close();
      }

   }
}
