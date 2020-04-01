package jef.util;

public class ByteArray {
   byte[] array;
   int size;

   public ByteArray() {
      this.array = new byte[4132];
      this.size = 0;
   }

   public ByteArray(byte[] var1) {
      this.array = var1;
      this.size = this.array.length;
   }

   private void checkSize() {
      if(this.array.length <= this.size) {
         byte[] var2 = this.array;
         this.array = new byte[var2.length * 2];

         for(int var1 = 0; var1 < this.size; ++var1) {
            this.array[var1] = var2[var1];
         }
      }

   }

   public int add(byte[] var1) {
      for(int var2 = 0; var2 < var1.length; ++var2) {
         this.add8(var1[var2]);
      }

      return this.size - 1;
   }

   public int add1(boolean var1) {
      this.checkSize();
      byte[] var4 = this.array;
      int var3 = this.size;
      this.size = var3 + 1;
      byte var2;
      if(var1) {
         var2 = 1;
      } else {
         var2 = 0;
      }

      var4[var3] = (byte)var2;
      return this.size - 1;
   }

   public int add16(int var1) {
      this.checkSize();
      byte[] var3 = this.array;
      int var2 = this.size;
      this.size = var2 + 1;
      var3[var2] = (byte)(var1 >> 8 & 255);
      this.checkSize();
      var3 = this.array;
      var2 = this.size;
      this.size = var2 + 1;
      var3[var2] = (byte)(var1 & 255);
      return this.size - 1;
   }

   public int add32(int var1) {
      this.checkSize();
      byte[] var3 = this.array;
      int var2 = this.size;
      this.size = var2 + 1;
      var3[var2] = (byte)(var1 >>> 24 & 255);
      this.checkSize();
      var3 = this.array;
      var2 = this.size;
      this.size = var2 + 1;
      var3[var2] = (byte)(var1 >>> 16 & 255);
      this.checkSize();
      var3 = this.array;
      var2 = this.size;
      this.size = var2 + 1;
      var3[var2] = (byte)(var1 >>> 8 & 255);
      this.checkSize();
      var3 = this.array;
      var2 = this.size;
      this.size = var2 + 1;
      var3[var2] = (byte)(var1 & 255);
      return this.size - 1;
   }

   public int add8(byte var1) {
      this.checkSize();
      byte[] var3 = this.array;
      int var2 = this.size;
      this.size = var2 + 1;
      var3[var2] = var1;
      return this.size - 1;
   }

   public boolean get1(int var1) {
      boolean var2;
      if(this.array[var1] != 0) {
         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
   }

   public int get16(int var1) {
      return (this.array[var1] & 255) << 8 | this.array[var1 + 1] & 255;
   }

   public int get32(int var1) {
      byte[] var4 = this.array;
      int var2 = var1 + 1;
      byte var5 = var4[var1];
      var4 = this.array;
      int var3 = var2 + 1;
      return (var5 & 255) << 24 | (var4[var2] & 255) << 16 | (this.array[var3] & 255) << 8 | this.array[var3 + 1] & 255;
   }

   public byte get8(int var1) {
      return this.array[var1];
   }

   public byte[] getArray() {
      byte[] var2 = new byte[this.size];

      for(int var1 = 0; var1 < this.size; ++var1) {
         var2[var1] = this.array[var1];
      }

      return var2;
   }

   public int getSize() {
      return this.size;
   }

   public byte[] getSubArray(int var1, int var2) {
      byte[] var4 = new byte[var2 - var1 + 1];

      for(int var3 = var1; var3 <= var2; ++var3) {
         var4[var3 - var1] = this.array[var3];
      }

      return var4;
   }
}
