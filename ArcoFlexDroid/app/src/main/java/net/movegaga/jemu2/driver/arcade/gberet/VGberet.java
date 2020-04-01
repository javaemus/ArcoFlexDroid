package net.movegaga.jemu2.driver.arcade.gberet;

import net.movegaga.jemu2.driver.BaseVideo;

import jef.machine.EmulatorProperties;
import jef.map.WriteHandler;
import jef.video.BitMap;
import jef.video.BitMapImpl;
import jef.video.Blitter;
import jef.video.PaletteInitializer;
import jef.video.VideoEmulator;
import jef.video.VideoInitializer;
import jef.video.VideoRenderer;

public class VGberet extends BaseVideo implements VideoEmulator, VideoRenderer, VideoInitializer, PaletteInitializer, WriteHandler {
   static final int COLOR_RAM = 49152;
   static final int SCROLL_RAM = 57346;
   static final int SPRITES = 53504;
   static final int SPRITES_2 = 53248;
   static final int SPRITES_SIZE = 192;
   static final int SPRITE_BANK = 57411;
   static final int VIDEO_RAM = 51200;
   static final int VRAM_SIZE = 2048;
   BitMap bgLayer;
   boolean[] dirty = new boolean[2048];
   Gberet driver;
   int[] scroll = new int[32];

   public VGberet(Gberet var1) {
      this.driver = var1;
   }

   public void init(EmulatorProperties var1) {
      super.init(var1);
   }

   public void initPalette() {
      int var1 = 0;

      int var2;
      for(var2 = 0; var2 < this.getTotalColors(); ++var2) {
         this.setPaletteColor(var2, (this.driver.mem_prom[var2] >> 0 & 1) * 33 + (this.driver.mem_prom[var2] >> 1 & 1) * 71 + (this.driver.mem_prom[var2] >> 2 & 1) * 151, (this.driver.mem_prom[var2] >> 3 & 1) * 33 + (this.driver.mem_prom[var2] >> 4 & 1) * 71 + (this.driver.mem_prom[var2] >> 5 & 1) * 151, (this.driver.mem_prom[var2] >> 6 & 1) * 71 + 0 + (this.driver.mem_prom[var2] >> 7 & 1) * 151);
         ++var1;
      }

      for(var2 = 0; var2 < this.getTotalColors(1); ++var2) {
         if((this.driver.mem_prom[var1] & 15) != 0) {
            this.setColor(1, var2, this.driver.mem_prom[var1] & 15);
         } else {
            this.setColor(1, var2, 0);
         }

         ++var1;
      }

      for(var2 = 0; var2 < this.getTotalColors(0); ++var1) {
         this.setColor(0, var2, (this.driver.mem_prom[var1] & 15) + 16);
         ++var2;
      }

   }

   public int initVideo() {
      this.bgLayer = new BitMapImpl(512, 256);
      return 0;
   }

   public BitMap renderVideo() {
      int var1;
      boolean var7;
      boolean var8;
      for(var1 = 2047; var1 >= 0; --var1) {
         if(this.dirty[var1]) {
            this.dirty[var1] = false;
            int var3 = var1 / 64;
            if((this.driver.mem_cpu['쀀' + var1] & 16) != 0) {
               var7 = true;
            } else {
               var7 = false;
            }

            if((this.driver.mem_cpu['쀀' + var1] & 32) != 0) {
               var8 = true;
            } else {
               var8 = false;
            }

            BitMap var9 = this.bgLayer;
            char var2 = this.driver.mem_cpu['저' + var1];
            this.draw(var9, 0, (this.driver.mem_cpu['쀀' + var1] & 64) * 4 + var2, this.driver.mem_cpu['쀀' + var1] & 15, var7, var8, var1 % 64 * 8, var3 * 8, -1, 0);
         }
      }

      for(var1 = 0; var1 < 32; ++var1) {
         this.scroll[var1] = this.driver.mem_cpu['\ue002' + var1] + this.driver.mem_cpu['\ue002' + var1 + 32] * 256 + 8;
      }

      Blitter.copyWrappedRowsHorizontal(this.bgLayer, this.getBackBuffer(), this.scroll, 8, 16);
      char var10;
      if((this.driver.mem_cpu['\ue043'] & 8) != 0) {
         var10 = '퀀';
      } else {
         var10 = '턀';
      }

      for(int var11 = 0; var11 < 192; var11 += 4) {
         int var6 = var10 + var11;
         if(this.driver.mem_cpu[var6 + 3] != 0) {
            char var12 = this.driver.mem_cpu[var6 + 2];
            char var5 = this.driver.mem_cpu[var6 + 1];
            char var4 = this.driver.mem_cpu[var6 + 3];
            if((this.driver.mem_cpu[var6 + 1] & 16) != 0) {
               var7 = true;
            } else {
               var7 = false;
            }

            if((this.driver.mem_cpu[var6 + 1] & 32) != 0) {
               var8 = true;
            } else {
               var8 = false;
            }

            this.drawVisible(1, this.driver.mem_cpu[var6] + ((this.driver.mem_cpu[var6 + 1] & 64) << 2), this.driver.mem_cpu[var6 + 1] & 15, var7, var8, var12 - (var5 & 128) * 2, var4, 2, 0);
         }
      }

      return this.getBackBuffer();
   }

   public void write(int var1, int var2) {
      if(this.driver.mem_cpu[var1] != var2) {
         this.driver.mem_cpu[var1] = (char)var2;
         this.dirty[var1 & 2047] = true;
      }

   }
}
