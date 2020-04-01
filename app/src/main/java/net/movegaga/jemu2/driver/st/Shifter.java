package net.movegaga.jemu2.driver.st;

import jef.video.BitMap;
import jef.video.BitMapImpl;

public class Shifter {
   static final int ADDR_MODE = 96;
   static final int ADDR_PALETTE = 64;
   static final int ADDR_VIDEO_HIGH = 1;
   static final int ADDR_VIDEO_LOW = 13;
   static final int ADDR_VIDEO_MID = 3;
   static final int MODE_HIGH = 2;
   static final int MODE_LOW = 0;
   static final int MODE_MEDIUM = 1;
   AtariST atariST;
   BitMap bitmap = new BitMapImpl(320, 200);
   char[] mem = new char[1024];
   int[] palette;
   int[] pixels;
   int videoAddr = 0;
   int videoMode = 0;

   public Shifter(AtariST var1) {
      this.pixels = this.bitmap.getPixels();
      this.palette = new int[16];
      this.atariST = var1;
   }

   private void render16(int var1) {
      int var4 = this.videoAddr;
      int var2 = 0;
      int var3 = var1 * 320;
      var4 += var1 * 160;
      var1 = var3;

      for(var3 = var4; var2 < 320; ++var3) {
         char var5 = this.atariST.mem_ram[var3];
         int[] var6 = this.pixels;
         var4 = var1 + 1;
         var6[var1] = this.palette[var5 >> 4];
         var6 = this.pixels;
         var1 = var4 + 1;
         var6[var4] = this.palette[var5 & 15];
         var2 += 2;
      }

   }

   private void updatePalette(int var1) {
      var1 &= -2;
      int var2 = this.mem[var1] << 8 | this.mem[var1 + 1];
      var1 = var1 - 64 >> 1;
      var2 = (var2 >> 8 & 7) * 36 << 16 | (var2 >> 4 & 7) * 36 << 8 | (var2 & 7) * 36;
      this.palette[var1] = var2;
      System.out.println("Palette update " + var1 + " = " + Integer.toHexString(var2));
   }

   private void updateVideoAddr() {
      this.videoAddr = this.mem[1] << 16 | this.mem[3] << 8;
      System.out.println("video addr set to " + Integer.toHexString(this.videoAddr));
   }

   public char read(int var1) {
      return this.mem[var1 - 16744960];
   }

   public void render(int var1) {
      switch(this.videoMode & 3) {
      case 0:
         this.render16(var1);
         break;
      default:
         System.out.println("*ERROR* Rendering unimpl mode " + this.videoMode);
      }

   }

   public void write(int var1, int var2) {
      System.out.println("write SHIFTER " + Integer.toHexString(var1) + ", " + Integer.toHexString(var2));
      var1 -= 16744960;
      this.mem[var1] = (char)var2;
      if(var1 >= 64 && var1 < 96) {
         this.updatePalette(var1);
      }

      switch(var1) {
      case 1:
      case 3:
         this.updateVideoAddr();
         break;
      case 96:
         this.videoMode = var2;
         System.out.println("video mode set to " + var2);
      }

   }
}
