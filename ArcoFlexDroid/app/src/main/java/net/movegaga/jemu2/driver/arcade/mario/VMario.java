package net.movegaga.jemu2.driver.arcade.mario;

import net.movegaga.jemu2.driver.BaseVideo;

import jef.machine.EmulatorProperties;
import jef.map.WriteHandler;
import jef.video.BitMap;
import jef.video.BitMapImpl;
import jef.video.Blitter;
import jef.video.PaletteInitializer;
import jef.video.VideoEmulator;
import jef.video.VideoRenderer;

public class VMario extends BaseVideo implements WriteHandler, VideoEmulator, VideoRenderer, PaletteInitializer {
   BitMap backBuffer;
   BitMap bgLayer;
   boolean[] dirty = new boolean[1024];
   Mario driver;
   int gfxBank;
   int paletteBank;

   public VMario(Mario var1) {
      this.driver = var1;
   }

   public void init(EmulatorProperties var1) {
      super.init(var1);
      this.backBuffer = this.getBackBuffer();
      this.bgLayer = new BitMapImpl(this.backBuffer.getWidth(), this.backBuffer.getHeight());
   }

   public void initPalette() {
      int var1;
      for(var1 = 0; var1 < this.getTotalColors(); ++var1) {
         char var4 = this.driver.mem_prom[var1];
         int var3 = var4 >> 5 & 7;
         int var2 = var4 >> 2 & 7;
         int var5 = var4 & 3;
         this.setPaletteColor(var1, 255 - (var3 << 5 | var3 << 2 | var3 >> 1), 255 - (var2 << 5 | var2 << 2 | var2 >> 1), 255 - (var5 << 2 | var5 | var5 << 4 | var5 << 6));
      }

      for(var1 = 0; var1 < 8; ++var1) {
         this.setColor(0, var1 * 4 + 0, var1 * 8 + 0 + 64);
         this.setColor(0, var1 * 4 + 1, var1 * 8 + 1 + 64);
         this.setColor(0, var1 * 4 + 2, var1 * 8 + 2 + 64);
         this.setColor(0, var1 * 4 + 3, var1 * 8 + 3 + 64);
      }

      for(var1 = 0; var1 < 8; ++var1) {
         this.setColor(0, var1 * 4 + 32 + 0, var1 * 8 + 0 + 192);
         this.setColor(0, var1 * 4 + 32 + 1, var1 * 8 + 1 + 192);
         this.setColor(0, var1 * 4 + 32 + 2, var1 * 8 + 2 + 192);
         this.setColor(0, var1 * 4 + 32 + 3, var1 * 8 + 3 + 192);
      }

      for(var1 = 0; var1 < this.getTotalColors(1); ++var1) {
         this.setColor(1, var1, var1);
      }

   }

   public BitMap renderVideo() {
      int var1;
      char var2;
      for(var1 = 1023; var1 >= 0; --var1) {
         if(this.dirty[var1]) {
            this.dirty[var1] = false;
            BitMap var7 = this.bgLayer;
            char var3 = this.driver.mem_cpu1[var1 + 29696];
            int var4 = this.gfxBank;
            var2 = this.driver.mem_cpu1[var1 + 29696];
            this.draw(var7, 0, (var4 << 8) + var3, this.paletteBank * 8 + (var2 >> 5), 0, 0, (var1 & 31) * 8, ((var1 >> 5) - 2) * 8, 0, -1, 0);
         }
      }

      Blitter.copyEqualSizedBitMap(this.bgLayer, this.backBuffer);

      for(var1 = 0; var1 < 384; var1 += 4) {
         if(this.driver.mem_cpu1[var1 + 26880] != 0) {
            var2 = this.driver.mem_cpu1[var1 + 26880 + 2];
            char var9 = this.driver.mem_cpu1[var1 + 26880 + 1];
            int var8 = this.paletteBank;
            boolean var5;
            if((this.driver.mem_cpu1[var1 + 26880 + 1] & 128) != 0) {
               var5 = true;
            } else {
               var5 = false;
            }

            boolean var6;
            if((this.driver.mem_cpu1[var1 + 26880 + 1] & 64) != 0) {
               var6 = true;
            } else {
               var6 = false;
            }

            this.drawVisible(1, var2, var8 * 16 + (var9 & 15), var5, var6, this.driver.mem_cpu1[var1 + 26880 + 3] - 8, 240 - this.driver.mem_cpu1[var1 + 26880] + 8, 1, 0);
         }
      }

      return this.backBuffer;
   }

   public void write(int var1, int var2) {
      this.driver.mem_cpu1[var1] = (char)var2;
      this.dirty[var1 & 1023] = true;
   }

   public WriteHandler writeGfxBank() {
      return new WriteGfxBank();
   }

   public WriteHandler writePaletteBank() {
      return new WritePaletteBank();
   }

   public class WriteGfxBank implements WriteHandler {
      public void write(int var1, int var2) {
         if(VMario.this.gfxBank != (var2 & 1)) {
            for(var1 = 0; var1 < 1024; ++var1) {
               VMario.this.dirty[var1] = true;
            }

            VMario.this.gfxBank = var2 & 1;
         }

      }
   }

   public class WritePaletteBank implements WriteHandler {
      public void write(int var1, int var2) {
         if(VMario.this.paletteBank != (var2 & 1)) {
            for(var1 = 0; var1 < 1024; ++var1) {
               VMario.this.dirty[var1] = true;
            }

            VMario.this.paletteBank = var2 & 1;
         }

      }
   }
}
