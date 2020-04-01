package net.movegaga.jemu2.driver.arcade.arkanoid;

import net.movegaga.jemu2.driver.BaseVideo;

import jef.machine.EmulatorProperties;
import jef.map.WriteHandler;
import jef.video.BitMap;
import jef.video.BitMapImpl;
import jef.video.PaletteInitializer;
import jef.video.VideoEmulator;
import jef.video.VideoRenderer;

public class VArkanoid extends BaseVideo implements VideoEmulator, VideoRenderer, PaletteInitializer, WriteHandler {
   private static final int SPRITES = 59392;
   private static final int SPRITES_SIZE = 64;
   private static final int VRAM = 57344;
   private static final int VRAM_SIZE = 2048;
   BitMap backBuffer;
   BitMap bgLayer;
   boolean change = false;
   private boolean[] dirty = new boolean[2048];
   private Arkanoid driver;
   int gfxBank;
   int paletteBank;

   public VArkanoid(Arkanoid var1) {
      this.driver = var1;
   }

   public void init(EmulatorProperties var1) {
      super.init(var1);
      this.backBuffer = this.getBackBuffer();
      this.bgLayer = new BitMapImpl(this.backBuffer.getWidth(), this.backBuffer.getHeight());
   }

   public void initPalette() {
      for(int var1 = 0; var1 < this.getTotalColors(); ++var1) {
         char var2 = this.driver.mem_prom[this.getTotalColors() * 0 + var1];
         char var4 = this.driver.mem_prom[this.getTotalColors() * 1 + var1];
         char var3 = this.driver.mem_prom[this.getTotalColors() * 2 + var1];
         this.setPaletteColor(var1, var2 << 4 | var2, var4 << 4 | var4, var3 << 4 | var3);
      }

   }

   public BitMap renderVideo() {
      int var1;
      int var2;
      char var4;
      for(var1 = 2046; var1 >= 0; var1 -= 2) {
         int var7 = var1 / 2;
         if(this.dirty[var1] || this.dirty[var1 + 1]) {
            this.dirty[var1] = false;
            this.dirty[var1 + 1] = false;
            var2 = var7 / 32;
            char var3 = this.driver.mem_cpu['\ue000' + var1 + 1];
            var4 = this.driver.mem_cpu['\ue000' + var1];
            int var6 = this.gfxBank;
            BitMap var8 = this.bgLayer;
            char var5 = this.driver.mem_cpu['\ue000' + var1];
            this.drawVisible(var8, 0, var3 + ((var4 & 7) << 8) + var6 * 2048, this.paletteBank * 32 + ((var5 & 248) >> 3), false, false, var7 % 32 * 8, var2 * 8, -1, 0);
         }
      }

      this.copyBitMap(this.backBuffer, this.bgLayer);

      for(var1 = 0; var1 < 64; var1 += 4) {
         var4 = this.driver.mem_cpu['\ue800' + var1];
         var2 = 248 - this.driver.mem_cpu['\ue800' + var1 + 1];
         int var9 = this.driver.mem_cpu['\ue800' + var1 + 3] + ((this.driver.mem_cpu['\ue800' + var1 + 2] & 3) << 8) + this.gfxBank * 1024;
         this.drawVisible(0, var9 * 2, ((this.driver.mem_cpu['\ue800' + var1 + 2] & 248) >> 3) + this.paletteBank * 32, false, false, var4, var2 - 8, 1, 0);
         this.drawVisible(0, var9 * 2 + 1, ((this.driver.mem_cpu['\ue800' + var1 + 2] & 248) >> 3) + this.paletteBank * 32, false, false, var4, var2, 1, 0);
      }

      return this.backBuffer;
   }

   public void write(int var1, int var2) {
      if(this.driver.mem_cpu[var1] != var2) {
         this.dirty[var1 & 2047] = true;
         this.driver.mem_cpu[var1] = (char)var2;
      }

   }

   public WriteHandler writeRegD008() {
      return new WriteRegD008();
   }

   public class WriteRegD008 implements WriteHandler {
      public void write(int var1, int var2) {
         VArkanoid.this.driver.paddleSelect = var2 & 4;
         var1 = (var2 & 32) >> 5;
         if(VArkanoid.this.gfxBank != var1) {
            VArkanoid.this.gfxBank = var1;
            VArkanoid.this.change = true;
         }

         var1 = (var2 & 64) >> 6;
         if(VArkanoid.this.paletteBank != var1) {
            VArkanoid.this.paletteBank = var1;
            VArkanoid.this.change = true;
         }

         if(VArkanoid.this.change) {
            for(var1 = 0; var1 < VArkanoid.this.dirty.length; ++var1) {
               VArkanoid.this.dirty[var1] = true;
            }

            VArkanoid.this.change = false;
         }

      }
   }
}
