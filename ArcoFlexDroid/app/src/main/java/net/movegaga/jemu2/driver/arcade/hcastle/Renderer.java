package net.movegaga.jemu2.driver.arcade.hcastle;

import net.movegaga.jemu2.driver.BaseVideo;

import jef.map.WriteHandler;
import jef.video.BitMap;
import jef.video.BitMapImpl;
import jef.video.VideoEmulator;
import jef.video.VideoRenderer;

public class Renderer extends BaseVideo implements VideoEmulator, VideoRenderer, WriteHandler {
   BitMap backBuffer;
   private HCastle driver;
   K007121 k1;
   K007121 k2;
   int[] palette = new int[128];
   int[] pixels;

   public Renderer(HCastle var1) {
      this.driver = var1;
      this.backBuffer = new BitMapImpl(256, 224);
      this.pixels = this.backBuffer.getPixels();
      this.k1 = this.driver.k007121_1;
      this.k2 = this.driver.k007121_2;
   }

   private void drawForeGround() {
      for(int var1 = 0; var1 < 224; ++var1) {
         for(int var2 = 0; var2 < 256; var2 += 8) {
            ;
         }
      }

   }

   private void plot(int var1, int var2, int var3) {
      this.pixels[(var2 << 8) + (var1 & 255)] = this.palette[var3];
   }

   public BitMap renderVideo() {
      this.drawForeGround();
      return this.backBuffer;
   }

   public void write(int var1, int var2) {
      this.driver.mem_cpu1[var1] = (char)var2;
      var2 = var1 & -2;
      char[] var3 = this.driver.mem_cpu1;
      var1 = var2 + 1;
      var2 = var3[var2] << 8 | this.driver.mem_cpu1[var1];
      this.palette[var1 >> 1 & 127] = (var2 >> 0 & 15) * 17 << 16 | (var2 >> 4 & 15) * 17 << 8 | (var2 >> 8 & 15) * 17;
   }
}
