package net.movegaga.jemu2.driver.arcade.yiear;

import net.movegaga.jemu2.driver.BaseVideo;

import jef.machine.EmulatorProperties;
import jef.map.WriteHandler;
import jef.video.BitMap;
import jef.video.BitMapImpl;
import jef.video.Blitter;
import jef.video.PaletteInitializer;
import jef.video.VideoEmulator;
import jef.video.VideoRenderer;

public class VYiear extends BaseVideo implements VideoEmulator, VideoRenderer, PaletteInitializer, WriteHandler {
   private static final int SPRITES = 20480;
   private static final int SPRITES2 = 21504;
   private static final int SPRITES_SIZE = 48;
   private static final int VRAM = 22528;
   private static final int VRAM_SIZE = 2048;
   BitMap backBuffer;
   BitMap bgLayer;
   private boolean[] dirty = new boolean[2048];
   private Yiear driver;

   public VYiear(Yiear var1) {
      this.driver = var1;
   }

   public void init(EmulatorProperties var1) {
      super.init(var1);
      this.backBuffer = this.getBackBuffer();
      this.bgLayer = new BitMapImpl(this.backBuffer.getWidth(), this.backBuffer.getHeight());
   }

   public void initPalette() {
      int var1 = 0;

      for(int var2 = 0; var2 < this.getTotalColors(); ++var2) {
         this.setPaletteColor(var2, (this.driver.mem_prom[var1] >> 0 & 1) * 33 + (this.driver.mem_prom[var1] >> 1 & 1) * 71 + (this.driver.mem_prom[var1] >> 2 & 1) * 151, (this.driver.mem_prom[var1] >> 3 & 1) * 33 + (this.driver.mem_prom[var1] >> 4 & 1) * 71 + (this.driver.mem_prom[var1] >> 5 & 1) * 151, (this.driver.mem_prom[var1] >> 6 & 1) * 71 + 0 + (this.driver.mem_prom[var1] >> 7 & 1) * 151);
         ++var1;
      }

   }

   public BitMap renderVideo() {
      int var1;
      int var2;
      boolean var5;
      boolean var6;
      for(var1 = 2046; var1 >= 0; var1 -= 2) {
         if(this.dirty[var1] || this.dirty[var1 + 1]) {
            boolean[] var7 = this.dirty;
            this.dirty[var1 + 1] = false;
            var7[var1] = false;
            var2 = var1 / 2;
            int var4 = var1 / 2 / 32;
            if((this.driver.mem_cpu[var1 + 22528] & 128) != 0) {
               var5 = true;
            } else {
               var5 = false;
            }

            if((this.driver.mem_cpu[var1 + 22528] & 64) != 0) {
               var6 = true;
            } else {
               var6 = false;
            }

            BitMap var10 = this.bgLayer;
            char var3 = this.driver.mem_cpu[var1 + 22528 + 1];
            this.drawVisible(var10, 0, (this.driver.mem_cpu[var1 + 22528] & 16) << 4 | var3, 0, var5, var6, var2 % 32 * 8, var4 * 8, -1, 0);
         }
      }

      Blitter.copyEqualSizedBitMap(this.bgLayer, this.backBuffer);

      for(var1 = 46; var1 >= 0; var1 -= 2) {
         char var9 = this.driver.mem_cpu[var1 + 21504];
         int var8 = 240 - this.driver.mem_cpu[var1 + 20480 + 1];
         if((~this.driver.mem_cpu[var1 + 20480] & 64) != 0) {
            var5 = true;
         } else {
            var5 = false;
         }

         if((this.driver.mem_cpu[var1 + 20480] & 128) != 0) {
            var6 = true;
         } else {
            var6 = false;
         }

         var2 = var8;
         if(var1 < 38) {
            var2 = var8 + 1;
         }

         this.drawVisible(1, this.driver.mem_cpu[var1 + 21504 + 1] + (this.driver.mem_cpu[var1 + 20480] & 1) * 256, 0, var5, var6, var9, var2, 1, 0);
      }

      return this.backBuffer;
   }

   public void write(int var1, int var2) {
      if(this.driver.mem_cpu[var1] != var2) {
         this.driver.mem_cpu[var1] = (char)var2;
         this.dirty[var1 & 2047] = true;
      }

   }
}
