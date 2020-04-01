package net.movegaga.jemu2.driver.arcade.solomon;

import net.movegaga.jemu2.JEmu2;
import net.movegaga.jemu2.driver.BaseVideo;

import jef.machine.EmulatorProperties;
import jef.map.WriteHandler;
import jef.video.BitMap;
import jef.video.BitMapImpl;
import jef.video.VideoEmulator;
import jef.video.VideoRenderer;

public class VSolomon extends BaseVideo implements VideoEmulator, VideoRenderer, WriteHandler {
   BitMap backBuffer;
   BitMap charLayer;
   boolean[] dirty = new boolean[1024];
   private Solomon driver;
   int fn = 0;

   public VSolomon(Solomon var1) {
      this.driver = var1;
   }

   public void init(EmulatorProperties var1) {
      super.init(var1);
      this.backBuffer = this.getBackBuffer();
      this.charLayer = new BitMapImpl(this.backBuffer.getWidth(), this.backBuffer.getHeight());
   }

   public BitMap renderVideo() {
      int var1;
      boolean var9;
      boolean var10;
      for(var1 = 1023; var1 >= 0; --var1) {
         char var6 = this.driver.mem_cpu1['\ud800' + var1];
         int var4 = (var6 & 112) >> 4;
         int var5 = (this.driver.mem_cpu1['퀀' + var1] & 112) >> 4;
         if(this.dirty[var1] || this.getGfxManager(0).colorCodeHasChanged(var5) || this.getGfxManager(1).colorCodeHasChanged(var4)) {
            this.dirty[var1] = false;
            int var3 = var1 % 32;
            int var2 = var1 / 32;
            if((var6 & 128) != 0) {
               var9 = true;
            } else {
               var9 = false;
            }

            if((var6 & 8) != 0) {
               var10 = true;
            } else {
               var10 = false;
            }

            char var7 = this.driver.mem_cpu1['\udc00' + var1];
            var6 = this.driver.mem_cpu1['\ud800' + var1];
            int var8 = (this.driver.mem_cpu1['퐀' + var1] | (this.driver.mem_cpu1['퀀' + var1] & 7) << 8) & 2047;
            this.drawVisible(this.charLayer, 1, (var7 | (var6 & 7) << 8) & 2047, var4, var9, var10, var3 * 8, var2 * 8, -1, 0);
            if(var8 != 0) {
               this.drawVisible(this.charLayer, 0, var8, var5, false, false, var3 * 8, var2 * 8, 1, 0);
            }
         }
      }

      this.charLayer.toPixels(this.backBuffer.getPixels());

      for(var1 = '\ue07c'; var1 >= '\ue000'; var1 -= 4) {
         char var11 = this.driver.mem_cpu1[var1 + 3];
         char var12 = this.driver.mem_cpu1[var1 + 2];
         if((this.driver.mem_cpu1[var1 + 1] & 64) != 0) {
            var9 = true;
         } else {
            var9 = false;
         }

         if((this.driver.mem_cpu1[var1 + 1] & 128) != 0) {
            var10 = true;
         } else {
            var10 = false;
         }

         this.drawVisible(2, this.driver.mem_cpu1[var1] + (this.driver.mem_cpu1[var1 + 1] & 16) * 16, (this.driver.mem_cpu1[var1 + 1] & 14) >> 1, var9, var10, var11, 241 - var12, 1, 0);
      }

      return this.backBuffer;
   }

   public void write(int var1, int var2) {
      this.driver.mem_cpu1[var1] = (char)var2;
      this.dirty[var1 & 1023] = true;
   }

   public WriteHandler writePaletteRAM() {
      return new WritePaletteRAM();
   }

   public class WritePaletteRAM implements WriteHandler {
      public void write(int var1, int var2) {
         VSolomon.this.driver.mem_cpu1[var1] = (char)var2;
         var2 = ((var1 & '\ufffe') - '\ue400') / 2;
         var1 = VSolomon.this.driver.mem_cpu1[var1 & '\ufffe'] << 8 | VSolomon.this.driver.mem_cpu1[('\ufffe' & var1) + 1];
         if(!JEmu2.IS_APPLET) {
            var1 = (var1 & 15) << 20 | (var1 & 3840) >> 4 | (var1 & '\uf000') << 0;
         } else {
            var1 = (var1 & 15) << 4 | (var1 & 3840) << 12 | (var1 & '\uf000') << 0;
         }

         VSolomon.this.getGfxManager(0).changePalette(var2, var1);
         VSolomon.this.getGfxManager(1).changePalette(var2, var1);
         VSolomon.this.getGfxManager(2).changePalette(var2, var1);
      }
   }
}
