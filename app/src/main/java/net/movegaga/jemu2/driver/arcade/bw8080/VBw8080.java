package net.movegaga.jemu2.driver.arcade.bw8080;

import net.movegaga.jemu2.driver.BaseVideo;

import java.lang.reflect.Array;

import jef.map.WriteHandler;
import jef.video.BitMap;
import jef.video.PaletteInitializer;
import jef.video.VideoEmulator;
import jef.video.VideoInitializer;
import jef.video.VideoRenderer;

public class VBw8080 extends BaseVideo implements VideoEmulator, VideoRenderer, VideoInitializer, PaletteInitializer {
   static final int BLACK = 0;
   static final int CYAN = 2162687;
   static final int GREEN = 2162464;
   static final int GREEN_CYAN = 2162576;
   static final int ORANGE = 16748576;
   static final int PURPLE = 16720127;
   static final int RED = 16719904;
   static final int WHITE = 16777215;
   static final int YELLOW = 16776992;
   static final int YELLOW_GREEN = 9502496;
   int color_map_select;
   int[][] gfx;
   int[][] overlay;
   int[] pal = new int[2];
   int screen_red;
   int screen_red_enabled;

   public VBw8080() {
      this.gfx = (int[][])Array.newInstance(Integer.TYPE, new int[]{256, 8});
      this.overlay = (int[][])Array.newInstance(Integer.TYPE, new int[]{65536, 2});
      this.screen_red_enabled = 0;
      this.color_map_select = 0;
      this.screen_red = 0;
   }

   private void artworkElement(int var1, int var2, int var3, int var4, int var5, int var6) {
      var6 = (var5 & 255) << 16 | '\uff00' & var5 | (16711680 & var5) >> 16;
      var5 = var4;
      if(var4 > 223) {
         var5 = 223;
      }

      while(var1 <= var2) {
         for(var4 = var3; var4 <= var5; ++var4) {
            this.overlay[(255 - var1) * 224 + var4][1] = var6;
            if(var6 != 16777215) {
               this.overlay[(255 - var1) * 224 + var4][0] = 986895 & var6;
            }
         }

         ++var1;
      }

   }

   private void invad2ct_overlay() {
      this.artworkElement(0, 24, 0, 255, 16776992, 255);
      this.artworkElement(25, 47, 0, 255, 9502496, 255);
      this.artworkElement(48, 70, 0, 255, 2162576, 255);
      this.artworkElement(71, 116, 0, 255, 2162687, 255);
      this.artworkElement(117, 139, 0, 255, 2162576, 255);
      this.artworkElement(140, 162, 0, 255, 2162464, 255);
      this.artworkElement(163, 185, 0, 255, 9502496, 255);
      this.artworkElement(186, 208, 0, 255, 16776992, 255);
      this.artworkElement(209, 231, 0, 255, 16748576, 255);
      this.artworkElement(232, 255, 0, 255, 16719904, 255);
   }

   private void invaders_overlay() {
      this.artworkElement(0, 255, 0, 255, 16777215, 255);
      this.artworkElement(16, 71, 0, 255, 2162464, 255);
      this.artworkElement(0, 15, 16, 133, 2162464, 255);
      this.artworkElement(192, 223, 0, 255, 16719904, 255);
   }

   private void invdpt2m_overlay() {
      this.artworkElement(0, 255, 0, 255, 16777215, 255);
      this.artworkElement(16, 71, 0, 255, 2162464, 255);
      this.artworkElement(0, 15, 16, 133, 2162464, 255);
      this.artworkElement(72, 191, 0, 255, 16776992, 255);
      this.artworkElement(192, 223, 0, 255, 16719904, 255);
   }

   private void invrvnge_overlay() {
      this.artworkElement(0, 255, 0, 255, 16777215, 255);
      this.artworkElement(0, 71, 0, 255, 2162464, 255);
      this.artworkElement(192, 223, 0, 255, 16719904, 255);
   }

   public boolean bw8080() {
      this.artworkElement(0, 255, 0, 255, 16777215, 255);
      this.color_map_select = 0;
      return true;
   }

   public WriteHandler c8080bw_videoram_w(char[] var1, VBw8080 var2) {
      return new Invaders_videoram_w(var1, var2);
   }

   public void initPalette() {
   }

   public int initVideo() {
      this.pal[0] = 0;
      this.pal[1] = 16777215;

      for(int var1 = 0; var1 < 256; ++var1) {
         this.gfx[var1][7] = this.pal[var1 >> 7];
         this.gfx[var1][6] = this.pal[(var1 & 64) >> 6];
         this.gfx[var1][5] = this.pal[(var1 & 32) >> 5];
         this.gfx[var1][4] = this.pal[(var1 & 16) >> 4];
         this.gfx[var1][3] = this.pal[(var1 & 8) >> 3];
         this.gfx[var1][2] = this.pal[(var1 & 4) >> 2];
         this.gfx[var1][1] = this.pal[(var1 & 2) >> 1];
         this.gfx[var1][0] = this.pal[var1 & 1];
      }

      return 0;
   }

   public boolean invad2ct() {
      this.bw8080();
      this.invad2ct_overlay();
      return true;
   }

   public boolean invaders() {
      this.bw8080();
      this.invaders_overlay();
      return true;
   }

   public boolean invadpt2() {
      this.bw8080();
      this.screen_red_enabled = 1;
      return true;
   }

   public boolean invdpt2m() {
      this.bw8080();
      this.invdpt2m_overlay();
      return true;
   }

   public boolean invrvnge() {
      this.bw8080();
      this.invrvnge_overlay();
      return true;
   }

   public BitMap renderVideo() {
      return super.getBackBuffer();
   }

   public void writeVRAM(int var1, int var2, int var3) {
      if(this.getRot() == 0) {
         System.arraycopy(this.gfx[var3], 0, this.getBackBuffer().getPixels(), (var2 - 9216) * 8, 8);
      } else if(this.getRot() == 3) {
         var2 = 7167 - (var2 - 9216);

         for(var1 = 0; var1 < 8; ++var1) {
            int var4 = ((var2 & 31) * 8 + var1) * 224 + (223 - (var2 >> 5));
            this.getBackBuffer().getPixels()[var4] = this.overlay[var4][this.gfx[var3][7 - var1] & 1];
         }
      } else {
         var1 = var2 - 9216;
         var1 = (var1 & 31) * 8 * 224 + (223 - (var1 >> 5));
         this.getBackBuffer().getPixels()[var1] = this.gfx[var3][0];
         this.getBackBuffer().getPixels()[var1 + 224] = this.gfx[var3][1];
         this.getBackBuffer().getPixels()[var1 + 448] = this.gfx[var3][2];
         this.getBackBuffer().getPixels()[var1 + 672] = this.gfx[var3][3];
         this.getBackBuffer().getPixels()[var1 + 896] = this.gfx[var3][4];
         this.getBackBuffer().getPixels()[var1 + 1120] = this.gfx[var3][5];
         this.getBackBuffer().getPixels()[var1 + 1344] = this.gfx[var3][6];
         this.getBackBuffer().getPixels()[var1 + 1568] = this.gfx[var3][7];
      }

   }

   public class Invaders_videoram_w implements WriteHandler {
      char[] mem;
      VBw8080 video;

      public Invaders_videoram_w(char[] var2, VBw8080 var3) {
         this.mem = var2;
         this.video = var3;
      }

      public void write(int var1, int var2) {
         this.mem[var1] = (char)var2;
         this.video.writeVRAM(0, var1, var2);
      }
   }
}
