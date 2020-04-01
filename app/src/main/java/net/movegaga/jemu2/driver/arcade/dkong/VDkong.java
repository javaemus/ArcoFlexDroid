package net.movegaga.jemu2.driver.arcade.dkong;

import net.movegaga.jemu2.driver.BaseVideo;

import jef.machine.EmulatorProperties;
import jef.map.WriteHandler;
import jef.video.BitMap;
import jef.video.BitMapImpl;
import jef.video.PaletteInitializer;
import jef.video.VideoEmulator;
import jef.video.VideoInitializer;
import jef.video.VideoRenderer;

public class VDkong extends BaseVideo implements VideoEmulator, VideoRenderer, VideoInitializer, PaletteInitializer {
   static final int SPRITE_RAM = 26880;
   static final int SPRITE_RAM_SIZE = 384;
   static final int VIDEO_RAM = 29696;
   static final int VIDEO_RAM_SIZE = 1024;
   BitMap backBuffer;
   BitMap charLayer;
   int colorCodes;
   boolean[] dirty = new boolean[1024];
   Dkong driver;
   int gfxBank = 0;
   boolean globalChanged = false;
   int gridOn = 0;
   int paletteBank = 0;

   public VDkong(Dkong var1) {
      this.driver = var1;
   }

   private void draw_grid() {
      char[] var5 = this.driver.mem_gfx3;
      int var1 = 1024;
      int var2 = 0;

      while(var2 <= 223) {
         int var4 = (var5[var1 & 2047] & 127) * 4;
         if(var4 >= 0 && var4 <= 255) {
            if((var5[var1 & 2047] & 128) != 0) {
               if((int)(Math.random() * 2.0D) == 1) {
                  this.backBuffer.getPixels()[var4 * 224 + var2] = this.getPaletteColor(256);
               }
            } else if(this.gridOn != 0) {
               this.backBuffer.getPixels()[var4 * 224 + var2] = this.getPaletteColor(257);
            }
         }

         int var3 = var1 + 1;
         var1 = var3;
         if(var4 >= (var5[var3 & 2047] & 127) * 4) {
            ++var2;
            var1 = var3;
         }
      }

   }

   private void draw_sprites() {
      for(int var1 = 0; var1 < 384; var1 += 4) {
         if(this.driver.mem_cpu1[var1 + 26880] != 0) {
            char var2 = this.driver.mem_cpu1[var1 + 26880 + 3];
            char var5 = this.driver.mem_cpu1[var1 + 26880];
            BitMap var7 = this.backBuffer;
            char var3 = this.driver.mem_cpu1[var1 + 26880 + 1];
            char var6 = this.driver.mem_cpu1[var1 + 26880 + 2];
            char var4 = this.driver.mem_cpu1[var1 + 26880 + 2];
            this.drawVisible(var7, 1, (var6 & 64) * 2 + (var3 & 127), this.paletteBank * 16 + (var4 & 15), this.driver.mem_cpu1[var1 + 26880 + 2] & 128, this.driver.mem_cpu1[var1 + 26880 + 1] & 128, var2 - 8, 240 - var5 + 7, 1, 0);
         }
      }

   }

   private void draw_tiles() {
      for(int var1 = 1023; var1 >= 0; --var1) {
         if(this.dirty[var1]) {
            this.dirty[var1] = false;
            int var5 = var1 / 32;
            char var4 = this.driver.mem_cpu1[var1 + 29696];
            int var3 = this.gfxBank;
            char var6 = this.driver.mem_prom[this.colorCodes + var1 % 32 + var1 / 32 / 4 * 32];
            int var2 = this.paletteBank;
            this.drawVisible(this.charLayer, 0, var4 + var3 * 256, (var6 & 15) + var2 * 16, false, false, var1 % 32 * 8, var5 * 8, -1, 0);
         }
      }

      this.charLayer.toPixels(this.backBuffer.getPixels());
   }

   public WriteHandler dkong3_gfxbank_w() {
      return new Dkong3_gfxbank_w();
   }

   public PaletteInitializer dkong3_palette_init() {
      return new Dkong3_palette_init();
   }

   public WriteHandler dkong_palettebank_w() {
      return new Dkong_palettebank_w();
   }

   public WriteHandler dkongjr_gfxbank_w() {
      return new Dkongjr_gfxbank_w();
   }

   public void init(EmulatorProperties var1) {
      System.out.println("INIT!!!!");
      System.out.println(var1);
      super.init(var1);
      this.backBuffer = this.getBackBuffer();

      this.charLayer = new BitMapImpl(this.backBuffer.getWidth(), this.backBuffer.getHeight());
      //this.charLayer = new BitMapImpl(320, 200);
   }

   public void initPalette() {
      int var2 = 0;

      System.out.println("MEM!!!!!!!!!!"+this.driver.mem_prom);

      for(int var1 = 0; var1 < 256; ++var1) {
         this.setPaletteColor(var1, 255 - ((this.driver.mem_prom[var2 + 256] >> 1 & 1) * 33 + (this.driver.mem_prom[var2 + 256] >> 2 & 1) * 71 + (this.driver.mem_prom[var2 + 256] >> 3 & 1) * 151), 255 - ((this.driver.mem_prom[var2 + 0] >> 2 & 1) * 33 + (this.driver.mem_prom[var2 + 0] >> 3 & 1) * 71 + (this.driver.mem_prom[var2 + 256] >> 0 & 1) * 151), 255 - ((this.driver.mem_prom[var2 + 0] >> 0 & 1) * 85 + (this.driver.mem_prom[var2 + 0] >> 1 & 1) * 170));
         ++var2;
      }

      this.colorCodes = var2 + 256;
   }

   public int initVideo() {
      this.gfxBank = 0;
      this.paletteBank = 0;
      return 0;
   }

   public WriteHandler radarscp_grid_color_w() {
      return new Radarscp_grid_color_w();
   }

   public WriteHandler radarscp_grid_enable_w() {
      return new Radarscp_grid_enable_w();
   }

   public VideoRenderer radarscp_vu() {
      return new Radarscp_vu();
   }

   public BitMap renderVideo() {
      if(this.globalChanged) {
         for(int var1 = 0; var1 < this.dirty.length; ++var1) {
            this.dirty[var1] = true;
         }

         this.globalChanged = false;
      }

      this.draw_tiles();
      this.draw_sprites();
      return this.backBuffer;
   }

   public WriteHandler writeVRAM() {
      return new WriteVRAM();
   }

   public class Dkong3_gfxbank_w implements WriteHandler {
      public void write(int var1, int var2) {
         VDkong.this.globalChanged = true;
         VDkong.this.gfxBank = ~var2 & 1;
      }
   }

   public class Dkong3_palette_init implements PaletteInitializer {
      public void initPalette() {
         int var2 = 0;

         for(int var1 = 0; var1 < 256; ++var1) {
            char var12 = VDkong.this.driver.mem_prom[var2 + 0];
            char var3 = VDkong.this.driver.mem_prom[var2 + 0];
            char var6 = VDkong.this.driver.mem_prom[var2 + 0];
            char var9 = VDkong.this.driver.mem_prom[var2 + 0];
            char var11 = VDkong.this.driver.mem_prom[var2 + 0];
            char var7 = VDkong.this.driver.mem_prom[var2 + 0];
            char var14 = VDkong.this.driver.mem_prom[var2 + 0];
            char var10 = VDkong.this.driver.mem_prom[var2 + 0];
            char var8 = VDkong.this.driver.mem_prom[var2 + 256];
            char var13 = VDkong.this.driver.mem_prom[var2 + 256];
            char var4 = VDkong.this.driver.mem_prom[var2 + 256];
            char var5 = VDkong.this.driver.mem_prom[var2 + 256];
            VDkong.this.setPaletteColor(var1, 255 - ((var12 >> 4 & 1) * 14 + (var3 >> 5 & 1) * 31 + (var6 >> 6 & 1) * 67 + (var9 >> 7 & 1) * 143), 255 - ((var11 >> 0 & 1) * 14 + (var7 >> 1 & 1) * 31 + (var14 >> 2 & 1) * 67 + (var10 >> 3 & 1) * 143), 255 - ((var8 >> 0 & 1) * 14 + (var13 >> 1 & 1) * 31 + (var4 >> 2 & 1) * 67 + (var5 >> 3 & 1) * 143));
            ++var2;
         }

         VDkong.this.colorCodes = var2 + 256;
      }
   }

   public class Dkong_palettebank_w implements WriteHandler {
      public void write(int var1, int var2) {
         int var3 = var1 & 1;
         var1 = VDkong.this.paletteBank;
         if((var2 & 1) != 0) {
            var1 |= 1 << var3;
         } else {
            var1 &= ~(1 << var3);
         }

         VDkong.this.paletteBank = var1;
         VDkong.this.globalChanged = true;
      }
   }

   public class Dkongjr_gfxbank_w implements WriteHandler {
      public void write(int var1, int var2) {
         VDkong.this.globalChanged = true;
         VDkong.this.gfxBank = var2 & 1;
      }
   }

   public class Radarscp_grid_color_w implements WriteHandler {
      public void write(int var1, int var2) {
         VDkong.this.setPaletteColor(257, (~var2 >> 0 & 1) * 255, (~var2 >> 1 & 1) * 255, (~var2 >> 2 & 1) * 255);
      }
   }

   public class Radarscp_grid_enable_w implements WriteHandler {
      public void write(int var1, int var2) {
         VDkong.this.gridOn = var2 & 1;
      }
   }

   public class Radarscp_vu implements VideoRenderer {
      public BitMap renderVideo() {
         VDkong.this.setPaletteColor(256, 255, 0, 0);
         if(VDkong.this.globalChanged) {
            for(int var1 = 0; var1 < VDkong.this.dirty.length; ++var1) {
               VDkong.this.dirty[var1] = true;
            }

            VDkong.this.globalChanged = false;
         }

         VDkong.this.draw_tiles();
         VDkong.this.draw_grid();
         VDkong.this.draw_sprites();
         return VDkong.this.backBuffer;
      }

      public void renderVideoPost() {
      }
   }

   public class WriteVRAM implements WriteHandler {
      public void write(int var1, int var2) {
         VDkong.this.driver.mem_cpu1[var1] = (char)var2;
         VDkong.this.dirty[var1 & 1023] = true;
      }
   }
}
