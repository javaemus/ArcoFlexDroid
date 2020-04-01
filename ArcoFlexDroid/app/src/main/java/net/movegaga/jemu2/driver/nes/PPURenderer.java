package net.movegaga.jemu2.driver.nes;

import jef.video.BitMap;
import jef.video.BitMapImpl;
import jef.video.VideoRenderer;

public class PPURenderer implements VideoRenderer {
   private BitMap bitmap;
   private int[] pixels;
   private PPU ppu;

   PPURenderer(PPU var1) {
      this.ppu = var1;
      this.bitmap = new BitMapImpl(256, 224);
      this.pixels = this.bitmap.getPixels();
   }

   public void renderFrame() {
      for(int var1 = 0; var1 < 224; ++var1) {
         this.renderLine(var1);
      }

   }

   public void renderLine(int var1) {
      int var4 = this.ppu.addrNameTable;
      int var5 = this.ppu.addrTilePatternTable;
      int var2 = 0;

      for(int var3 = var1 * 256; var2 < 32; ++var2) {
         int var7 = this.ppu.vram[(var1 >> 3 << 5) + var4 + var2] * 16 + var5 + (var1 & 7);
         int var6 = this.ppu.cart.readVROM(var7);
         var7 = this.ppu.cart.readVROM(var7 + 8);
         int[] var10 = this.pixels;
         int var8 = var3 + 1;
         var10[var3] = (var6 >> 7 & 1) + (var7 >> 6 & 2) << 22;
         var10 = this.pixels;
         int var9 = var8 + 1;
         var10[var8] = (var6 >> 6 & 1) + (var7 >> 5 & 2) << 22;
         var10 = this.pixels;
         var3 = var9 + 1;
         var10[var9] = (var6 >> 5 & 1) + (var7 >> 4 & 2) << 22;
         var10 = this.pixels;
         var8 = var3 + 1;
         var10[var3] = (var6 >> 4 & 1) + (var7 >> 3 & 2) << 22;
         var10 = this.pixels;
         var3 = var8 + 1;
         var10[var8] = (var6 >> 3 & 1) + (var7 >> 2 & 2) << 22;
         var10 = this.pixels;
         var9 = var3 + 1;
         var10[var3] = (var6 >> 2 & 1) + (var7 >> 1 & 2) << 22;
         var10 = this.pixels;
         var8 = var9 + 1;
         var10[var9] = (var6 >> 1 & 1) + (var7 << 0 & 2) << 22;
         var10 = this.pixels;
         var3 = var8 + 1;
         var10[var8] = (var6 >> 0 & 1) + (var7 << 1 & 2) << 22;
      }

   }

   public BitMap renderVideo() {
      return this.bitmap;
   }

   public void renderVideoPost() {
   }
}
