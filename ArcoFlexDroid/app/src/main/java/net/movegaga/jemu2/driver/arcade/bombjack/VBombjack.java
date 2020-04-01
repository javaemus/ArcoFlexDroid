package net.movegaga.jemu2.driver.arcade.bombjack;

import net.movegaga.jemu2.JEmu2;
import net.movegaga.jemu2.driver.BaseVideo;

import jef.machine.EmulatorProperties;
import jef.map.WriteHandler;
import jef.video.BitMap;
import jef.video.BitMapImpl;
import jef.video.VideoEmulator;
import jef.video.VideoRenderer;

public class VBombjack extends BaseVideo implements VideoEmulator, VideoRenderer {
   BitMap backBuffer;
   public int background_image = 0;
   boolean bgChanged = false;
   BitMap bgLayer;
   BitMap charLayer;
   boolean[] dirty = new boolean[1024];
   private Bombjack driver;

   public VBombjack(Bombjack var1) {
      this.driver = var1;
   }

   public WriteHandler bombjack_background_w() {
      return new WriteBackground();
   }

   public void init(EmulatorProperties var1) {
      super.init(var1);
      this.backBuffer = this.getBackBuffer();
      this.charLayer = new BitMapImpl(this.backBuffer.getWidth(), this.backBuffer.getHeight());
      this.bgLayer = new BitMapImpl(this.backBuffer.getWidth(), this.backBuffer.getHeight());
   }

   public BitMap renderVideo() {
      int var4 = (this.background_image & 7) * 512;

      int var1;
      int var3;
      boolean var10;
      BitMap var12;
      char var18;
      for(var1 = 0; var1 < 256; ++var1) {
         char var2 = this.driver.mem_gfx4[var4 + var1];
         var3 = this.driver.mem_gfx4[var4 + var1 + 256] & 15;
         if((this.background_image & 16) == 0) {
            var2 = 255;
            var3 = 0;
         }

         if(this.bgChanged || this.getGfxManager(1).colorCodeHasChanged(var3)) {
            int var6 = var1 % 16;
            int var5 = var1 / 16;
            int var7 = var6 * 2 + var5 * 2;
            this.dirty[var7] = true;
            this.dirty[var7 + 1] = true;
            var7 += 32;
            this.dirty[var7] = true;
            this.dirty[var7 + 1] = true;
            var18 = this.driver.mem_gfx4[var4 + var1 + 256];
            var12 = this.bgLayer;
            if((var18 & 128) != 0) {
               var10 = true;
            } else {
               var10 = false;
            }

            this.drawVisible(var12, 1, var2, var3, false, var10, var6 * 16, var5 * 16, -1, 0);
         }
      }

      int var13;
      for(var1 = 1023; var1 >= 0; --var1) {
         var3 = var1 % 32 * 8;
         var13 = var1 / 32 * 8;
         if(this.dirty[var1] || this.getGfxManager(0).colorCodeHasChanged(this.driver.mem_cpu1['鐀' + var1] & 15) || this.bgChanged) {
            this.bgLayer.toBitMap(this.charLayer, 232 - var13, var3, 232 - var13, var3, 8, 8);
            this.drawVisible(this.charLayer, 0, this.driver.mem_cpu1['退' + var1] + (this.driver.mem_cpu1['鐀' + var1] & 16) * 16, this.driver.mem_cpu1['鐀' + var1] & 15, false, false, var3, var13, 1, 0);
            this.dirty[var1] = false;
         }
      }

      this.charLayer.toPixels(this.backBuffer.getPixels());
      this.bgChanged = false;

      for(var1 = 92; var1 >= 0; var1 -= 4) {
         char var16 = this.driver.mem_cpu1['頠' + var1 + 3];
         if((this.driver.mem_cpu1['頠' + var1] & 128) != 0) {
            var13 = 177 - this.driver.mem_cpu1['頠' + var1 + 2];
         } else {
            var13 = 225 - this.driver.mem_cpu1['頠' + var1 + 2];
         }

         var18 = this.driver.mem_cpu1['頠' + var1 + 1];
         char var17 = this.driver.mem_cpu1['頠' + var1 + 1];
         byte var14;
         if((this.driver.mem_cpu1['頠' + var1] & 128) != 0) {
            var14 = 48;
         } else {
            var14 = 16;
         }

         var12 = this.backBuffer;
         byte var15;
         if((this.driver.mem_cpu1['頠' + var1] & 128) != 0) {
            var15 = 3;
         } else {
            var15 = 2;
         }

         char var9 = this.driver.mem_cpu1['頠' + var1];
         char var8 = this.driver.mem_cpu1['頠' + var1 + 1];
         if((var18 & 64) != 0) {
            var10 = true;
         } else {
            var10 = false;
         }

         boolean var11;
         if((var17 & 128) != 0) {
            var11 = true;
         } else {
            var11 = false;
         }

         this.drawVisible(var12, var15, var9 & 127, var8 & 15, var10, var11, var16, var13 + var14, 1, 0);
      }

      return this.backBuffer;
   }

   public WriteHandler writePaletteRAM() {
      return new WritePaletteRAM();
   }

   public WriteHandler writeVRAM() {
      return new WriteVRAM();
   }

   public class WriteBackground implements WriteHandler {
      public void write(int var1, int var2) {
         VBombjack.this.driver.mem_cpu1[var1] = (char)var2;
         if(VBombjack.this.background_image != var2) {
            for(var1 = 0; var1 < 1024; ++var1) {
               VBombjack.this.dirty[var1] = true;
            }

            VBombjack.this.background_image = var2;
            VBombjack.this.bgChanged = true;
         }

      }
   }

   public class WritePaletteRAM implements WriteHandler {
      public void write(int var1, int var2) {
         if(VBombjack.this.driver.mem_cpu1[var1] != var2) {
            VBombjack.this.driver.mem_cpu1[var1] = (char)var2;
            var2 = ((var1 & '\ufffe') - '鰀') / 2;
            var1 = VBombjack.this.driver.mem_cpu1[var1 & '\ufffe'] << 8 | VBombjack.this.driver.mem_cpu1[('\ufffe' & var1) + 1];
            if(!JEmu2.IS_APPLET) {
               var1 = (var1 & 15) << 20 | (var1 & 3840) >> 4 | (var1 & '\uf000') << 0;
            } else {
               var1 = (var1 & 15) << 4 | (var1 & 3840) << 12 | (var1 & '\uf000') << 0;
            }

            VBombjack.this.getGfxManager(0).changePalette(var2, var1);
            VBombjack.this.getGfxManager(1).changePalette(var2, var1);
            VBombjack.this.getGfxManager(2).changePalette(var2, var1);
            VBombjack.this.getGfxManager(3).changePalette(var2, var1);
         }

      }
   }

   public class WriteVRAM implements WriteHandler {
      public void write(int var1, int var2) {
         VBombjack.this.driver.mem_cpu1[var1] = (char)var2;
         VBombjack.this.dirty[var1 & 1023] = true;
      }
   }
}
