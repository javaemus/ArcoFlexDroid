package net.movegaga.jemu2.driver.arcade.circusc;

import net.movegaga.jemu2.driver.BaseVideo;

import jef.machine.EmulatorProperties;
import jef.map.WriteHandler;
import jef.video.BitMap;
import jef.video.BitMapImpl;
import jef.video.PaletteInitializer;
import jef.video.VideoEmulator;
import jef.video.VideoRenderer;

public class VCircusc extends BaseVideo implements VideoEmulator, VideoRenderer, PaletteInitializer, WriteHandler {
   BitMap backBuffer;
   BitMap charLayer;
   boolean[] dirty = new boolean[1024];
   Circusc driver;
   int[] scroll = new int[32];

   public VCircusc(Circusc var1) {
      this.driver = var1;
   }

   public void init(EmulatorProperties var1) {
      super.init(var1);
      this.backBuffer = this.getBackBuffer();
      this.charLayer = new BitMapImpl(256, 256);
   }

   public void initPalette() {
      int var1 = 0;

      int var2;
      for(var2 = 0; var2 < this.getTotalColors(); ++var1) {
         char var5 = this.driver.mem_prom[var1];
         int var4 = (var5 & 7) << 5;
         int var3 = (var5 >> 3 & 7) << 5;
         int var6 = (var5 >> 6 & 3) << 6;
         this.setPaletteColor(var1, var4 >> 3 | var4 | var4 >> 6, var3 >> 3 | var3 | var3 >> 6, var6 >> 2 | var6 | var6 >> 4 | var6 >> 6);
         ++var2;
      }

      for(var2 = 0; var2 < this.getTotalColors(1); ++var1) {
         this.setColor(1, var2, this.driver.mem_prom[var1] & 15);
         ++var2;
      }

      for(var2 = 0; var2 < this.getTotalColors(0); ++var1) {
         this.setColor(0, var2, (this.driver.mem_prom[var1] & 15) + 16);
         ++var2;
      }

   }

   public BitMap renderVideo() {
      int var1;
      char var2;
      char var4;
      char var5;
      BitMap var12;
      for(var1 = 1023; var1 >= 0; --var1) {
         if((this.driver.mem_cpu1[var1 + 12288] & 16) >> 4 == 1 && this.dirty[var1]) {
            int var3 = var1 / 32;
            var2 = this.driver.mem_cpu1[var1 + 12288];
            var4 = this.driver.mem_cpu1[var1 + 12288];
            var12 = this.charLayer;
            var5 = this.driver.mem_cpu1[var1 + 13312];
            this.draw(var12, 0, (this.driver.mem_cpu1[var1 + 12288] & 32) * 8 + var5, this.driver.mem_cpu1[var1 + 12288] & 15, var2 & 64, var4 & 128, var1 % 32 * 8, var3 * 8, 0, -1, 0);
         }
      }

      for(var1 = 0; var1 < 10; ++var1) {
         this.scroll[var1] = 0;
      }

      for(var1 = 10; var1 < 32; ++var1) {
         this.scroll[var1] = this.driver.mem_cpu1[7168];
      }

      this.copyScrollBitMap(this.backBuffer, this.charLayer, 0, 0, 32, this.scroll, this.getVisibleArea(), -1, 0);
      short var14;
      if((this.driver.mem_cpu1[5] & 1) != 0) {
         var14 = 14592;
      } else {
         var14 = 14336;
      }

      char var15;
      for(int var13 = 0; var13 < 256; var13 += 4) {
         var5 = this.driver.mem_cpu1[var14 + var13 + 2];
         char var8 = this.driver.mem_cpu1[var14 + var13 + 3];
         var15 = this.driver.mem_cpu1[var14 + var13 + 1];
         var4 = this.driver.mem_cpu1[var14 + var13 + 1];
         var12 = this.backBuffer;
         char var7 = this.driver.mem_cpu1[var14 + var13 + 0];
         char var9 = this.driver.mem_cpu1[var14 + var13 + 1];
         char var6 = this.driver.mem_cpu1[var14 + var13 + 1];
         boolean var10;
         if((var15 & 64) != 0) {
            var10 = true;
         } else {
            var10 = false;
         }

         boolean var11;
         if((var4 & 128) != 0) {
            var11 = true;
         } else {
            var11 = false;
         }

         this.drawVisible(var12, 1, var7 + (var9 & 32) * 8, var6 & 15, var10, var11, var5, var8, 2, 0);
      }

      for(var1 = 1023; var1 >= 0; --var1) {
         if((this.driver.mem_cpu1[var1 + 12288] & 16) >> 4 == 0) {
            int var16 = var1 / 32;
            var15 = this.driver.mem_cpu1[var1 + 12288];
            var2 = this.driver.mem_cpu1[var1 + 12288];
            var12 = this.backBuffer;
            var5 = this.driver.mem_cpu1[var1 + 13312];
            this.draw(var12, 0, (this.driver.mem_cpu1[var1 + 12288] & 32) * 8 + var5, this.driver.mem_cpu1[var1 + 12288] & 15, var15 & 64, var2 & 128, var1 % 32 * 8, var16 * 8, 1, -1, 0);
         }
      }

      return this.backBuffer;
   }

   public void write(int var1, int var2) {
      if(this.driver.mem_cpu1[var1] != var2) {
         this.driver.mem_cpu1[var1] = (char)var2;
         this.dirty[var1 & 1023] = true;
      }

   }
}
