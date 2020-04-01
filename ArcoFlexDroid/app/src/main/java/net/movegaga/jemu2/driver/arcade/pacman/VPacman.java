package net.movegaga.jemu2.driver.arcade.pacman;

import net.movegaga.jemu2.driver.BaseVideo;

import jef.machine.EmulatorProperties;
import jef.map.WriteHandler;
import jef.video.BitMap;
import jef.video.BitMapImpl;
import jef.video.PaletteInitializer;
import jef.video.VideoEmulator;
import jef.video.VideoRenderer;

public class VPacman extends BaseVideo implements VideoEmulator, VideoRenderer, PaletteInitializer {
   BitMap charLayer;
   boolean[] dirtyRect = new boolean[1024];
   Pacman driver;
   int xOffsetHack = 1;

   public VPacman(Pacman var1) {
      this.driver = var1;
   }

   public void init(EmulatorProperties var1) {
      super.init(var1);
      this.charLayer = new BitMapImpl(this.getBackBuffer().getWidth(), this.getBackBuffer().getHeight());
   }

   public void initPalette() {
      int var1 = 0;

      int var2;
      for(var2 = 0; var2 < this.getTotalColors(); ++var1) {
         int var3 = (this.driver.mem_prom[var1] & 7) << 1;
         int var4 = (this.driver.mem_prom[var1] >> 3 & 7) << 1;
         int var5 = (this.driver.mem_prom[var1] >> 6 & 7) << 2;
         this.setPaletteColor(var1, var3 | var3 << 4, var4 | var4 << 4, var5 | var5 << 4);
         ++var2;
      }

      var1 += 16;

      for(var2 = 0; var2 < this.getTotalColors(1); ++var1) {
         this.setColor(0, var2, this.driver.mem_prom[var1] & 15);
         this.setColor(1, var2, this.driver.mem_prom[var1] & 15);
         ++var2;
      }

   }

   public BitMap renderVideo() {
      int var1;
      for(int var3 = 1023; var3 > 0; --var3) {
         if(this.dirtyRect[var3]) {
            this.dirtyRect[var3] = false;
            int var4 = var3 % 32;
            var1 = var3 / 32;
            int var2;
            if(var1 < 2) {
               if(var4 < 2 || var4 >= 30) {
                  continue;
               }

               var2 = var4 - 2;
               var1 += 34;
            } else if(var1 >= 30) {
               if(var4 < 2 || var4 >= 30) {
                  continue;
               }

               var2 = var4 - 2;
               var1 -= 30;
            } else {
               var2 = var1 - 2;
               var1 = var4 + 2;
            }

            this.drawVisible(this.charLayer, 0, this.driver.mem_cpu[var3 + 16384], this.driver.mem_cpu[var3 + 17408] & 31, false, false, var1 * 8, var2 * 8, -1, 0);
         }
      }

      this.charLayer.toPixels(this.getBackBuffer().getPixels());

      char var5;
      boolean var6;
      boolean var7;
      char var8;
      char var9;
      char var10;
      for(var1 = 14; var1 > 4; var1 -= 2) {
         var5 = this.driver.mem_cpu[var1 + 20576 + 1];
         var9 = this.driver.mem_cpu[var1 + 20576];
         var8 = this.driver.mem_cpu[var1 + 20464];
         var10 = this.driver.mem_cpu[var1 + 20464 + 1];
         if((this.driver.mem_cpu[var1 + 20464] & 1) != 0) {
            var6 = true;
         } else {
            var6 = false;
         }

         if((this.driver.mem_cpu[var1 + 20464] & 2) != 0) {
            var7 = true;
         } else {
            var7 = false;
         }

         this.drawVisible(1, var8 >> 2, var10 & 31, var6, var7, 272 - var5, var9 - 31, 2, 0);
      }

      for(var1 = 4; var1 >= 0; var1 -= 2) {
         var10 = this.driver.mem_cpu[var1 + 20576 + 1];
         var8 = this.driver.mem_cpu[var1 + 20576];
         var5 = this.driver.mem_cpu[var1 + 20464];
         var9 = this.driver.mem_cpu[var1 + 20464 + 1];
         if((this.driver.mem_cpu[var1 + 20464] & 1) != 0) {
            var6 = true;
         } else {
            var6 = false;
         }

         if((this.driver.mem_cpu[var1 + 20464] & 2) != 0) {
            var7 = true;
         } else {
            var7 = false;
         }

         this.drawVisible(1, var5 >> 2, var9 & 31, var6, var7, 272 - var10, var8 - 31 + this.xOffsetHack, 2, 0);
      }

      return this.getBackBuffer();
   }

   public WriteHandler videoram_w() {
      return new Videoram_w();
   }

   public class Videoram_w implements WriteHandler {
      public void write(int var1, int var2) {
         VPacman.this.driver.mem_cpu[(var1 & 2047) + 16384] = (char)var2;
         VPacman.this.dirtyRect[var1 & 1023] = true;
      }
   }
}
