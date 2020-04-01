package net.movegaga.jemu2.driver.arcade.commando;

import net.movegaga.jemu2.driver.BaseVideo;

import jef.machine.EmulatorProperties;
import jef.map.WriteHandler;
import jef.video.BitMap;
import jef.video.BitMapImpl;
import jef.video.Blitter;
import jef.video.PaletteInitializer;
import jef.video.VideoEmulator;
import jef.video.VideoRenderer;

public class VCommando extends BaseVideo implements VideoEmulator, VideoRenderer, PaletteInitializer {
   static final int SIZE_BG_VRAM = 1024;
   static final int SIZE_SPRITE_RAM = 384;
   static final int SIZE_VRAM = 1024;
   BitMap backBuffer;
   BitMap bgLayer;
   boolean[] dirty = new boolean[1024];
   Commando driver;

   public VCommando(Commando var1) {
      this.driver = var1;
   }

   public void init(EmulatorProperties var1) {
      super.init(var1);
      this.backBuffer = this.getBackBuffer();
      this.bgLayer = new BitMapImpl(this.backBuffer.getWidth() * 2, this.backBuffer.getHeight() * 2);
   }

   public void initPalette() {
      for(int var1 = 0; var1 < 256; ++var1) {
         char var3 = this.driver.mem_prom[this.getTotalColors() * 0 + var1];
         char var2 = this.driver.mem_prom[this.getTotalColors() * 1 + var1];
         char var4 = this.driver.mem_prom[this.getTotalColors() * 2 + var1];
         this.setPaletteColor(var1, var3 << 4 | var3, var2 << 4 | var2, var4 << 4 | var4);
      }

   }

   public BitMap renderVideo() {
      int var1;
      char var2;
      boolean var6;
      boolean var7;
      for(var1 = 1023; var1 >= 0; --var1) {
         if(this.dirty[var1]) {
            this.dirty[var1] = false;
            if((this.driver.mem_cpu1['\udc00' + var1] & 16) != 0) {
               var6 = true;
            } else {
               var6 = false;
            }

            if((this.driver.mem_cpu1['\udc00' + var1] & 32) != 0) {
               var7 = true;
            } else {
               var7 = false;
            }

            BitMap var8 = this.bgLayer;
            var2 = this.driver.mem_cpu1['\ud800' + var1];
            this.drawVisible(var8, 1, (this.driver.mem_cpu1['\udc00' + var1] & 192) * 4 + var2, this.driver.mem_cpu1['\udc00' + var1] & 15, var6, var7, (var1 >> 5) * 16, (var1 & 31) * 16, -1, 0);
         }
      }

      var2 = this.driver.mem_cpu1['절'];
      char var4 = this.driver.mem_cpu1['젉'];
      char var3 = this.driver.mem_cpu1['젊'];
      char var9 = this.driver.mem_cpu1['젋'];
      Blitter.copyWrappedBitMap(this.bgLayer, this.backBuffer, -(var3 + (var9 & 1) * 256), -(var2 + (var4 & 1) * 256 + 256));

      for(var1 = 380; var1 >= 0; var1 -= 4) {
         var4 = this.driver.mem_cpu1[var1 + 3 + '︀'];
         var2 = this.driver.mem_cpu1[var1 + 1 + '︀'];
         char var5 = this.driver.mem_cpu1[var1 + 2 + '︀'];
         if((this.driver.mem_cpu1[var1 + 1 + '︀'] & 4) != 0) {
            var6 = true;
         } else {
            var6 = false;
         }

         if((this.driver.mem_cpu1[var1 + 1 + '︀'] & 8) != 0) {
            var7 = true;
         } else {
            var7 = false;
         }

         int var11 = (this.driver.mem_cpu1[var1 + 1 + '︀'] & 192) >> 6;
         if(var11 < 3) {
            this.drawVisible(this.backBuffer, 2, var11 * 256 + this.driver.mem_cpu1['︀' + var1], (this.driver.mem_cpu1[var1 + 1 + '︀'] & 48) >> 4, var6, var7, var4 - (var2 & 1) * 256, var5, 1, 15);
         }
      }

      for(var1 = 1023; var1 >= 0; --var1) {
         var3 = this.driver.mem_cpu1['퐀' + var1];
         int var10 = this.driver.mem_cpu1['퀀' + var1] + (var3 & 192) * 4;
         if(var10 != 32) {
            if((var3 & 16) != 0) {
               var6 = true;
            } else {
               var6 = false;
            }

            if((var3 & 32) != 0) {
               var7 = true;
            } else {
               var7 = false;
            }

            this.drawVisible(this.backBuffer, 0, var10, var3 & 15, var6, var7, (var1 & 31) * 8, (var1 >> 5) * 8, 1, 3);
         }
      }

      return this.backBuffer;
   }

   public WriteHandler writeVRAM() {
      return new WriteVRAM();
   }

   public class WriteVRAM implements WriteHandler {
      public void write(int var1, int var2) {
         VCommando.this.driver.mem_cpu1[var1] = (char)var2;
         VCommando.this.dirty[var1 & 1023] = true;
      }
   }
}
