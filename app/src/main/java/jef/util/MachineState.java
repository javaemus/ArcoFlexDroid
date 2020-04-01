package jef.util;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class MachineState {
   String name;
   ArrayList state;
   boolean used = false;

   public MachineState(String var1) {
      this.name = var1;
      this.state = new ArrayList();
   }

   public void add(ByteArray var1) {
      this.add(var1.getArray());
   }

   public void add(Persistent var1) {
      this.add(var1.getState());
   }

   public void add(byte[] var1) {
      this.state.add(var1);
   }

   public void add(char[] var1) {
      byte[] var3 = new byte[var1.length];

      for(int var2 = 0; var2 < var1.length; ++var2) {
         var3[var2] = (byte)var1[var2];
      }

      this.add(var3);
   }

   public void add(int[] var1) {
      byte[] var3 = new byte[var1.length];

      for(int var2 = 0; var2 < var1.length; ++var2) {
         var3[var2] = (byte)var1[var2];
      }

      this.add(var3);
   }

   public void add16(int var1) {
      this.add(new byte[]{(byte)(var1 >> 8 & 255), (byte)(var1 & 255)});
   }

   public int get16(int var1) {
      byte[] var2 = this.getBytes(var1);
      return (var2[0] & 255) << 8 | var2[1] & 255;
   }

   public ByteArray getByteArray(int var1) {
      return new ByteArray(this.getBytes(var1));
   }

   public byte[] getBytes(int var1) {
      byte[] var2;
      if(this.state.size() == 0) {
         var2 = null;
      } else {
         var2 = (byte[])this.state.get(var1);
      }

      return var2;
   }

   public void getChars(int var1, char[] var2) {
      byte[] var3 = this.getBytes(var1);

      for(var1 = 0; var1 < var2.length; ++var1) {
         var2[var1] = (char)(var3[var1] & 255);
      }

   }

   public char[] getChars(int var1) {
      byte[] var3 = this.getBytes(var1);
      char[] var2 = new char[var3.length];

      for(var1 = 0; var1 < var2.length; ++var1) {
         var2[var1] = (char)(var3[var1] & 255);
      }

      return var2;
   }

   public int[] getInts(int var1) {
      byte[] var3 = this.getBytes(var1);
      int[] var2 = new int[var3.length];

      for(var1 = 0; var1 < var2.length; ++var1) {
         var2[var1] = var3[var1] & 255;
      }

      return var2;
   }

   public void load() throws IOException {
      if(!(new File(System.getProperty("user.home") + "/net/movegaga")).exists()) {
         throw new IOException("Can\'t load file: User directory does not exist");
      } else {
         DataInputStream var2 = new DataInputStream(new FileInputStream(new File(System.getProperty("user.home") + "/net/movegaga/" + this.name + ".state")));

         for(int var1 = 0; var1 < this.state.size(); ++var1) {
            var2.read((byte[])this.state.get(var1));
         }

         var2.close();
         this.used = true;
      }
   }

   public void save() throws IOException {
      File var2 = new File(System.getProperty("user.home") + "/net/movegaga");
      if(!var2.exists()) {
         var2.mkdir();
      }

      DataOutputStream var3 = new DataOutputStream(new FileOutputStream(new File(System.getProperty("user.home") + "/net/movegaga/" + this.name + ".state")));

      for(int var1 = 0; var1 < this.state.size(); ++var1) {
         var3.write((byte[])this.state.get(var1));
      }

      var3.close();
   }

   public boolean used() {
      return this.used;
   }
}
