package cottage.vidhrdw;

import cottage.mame.MAMEVideo;
import jef.machine.BasicEmulator;
import jef.machine.EmulatorProperties;
import jef.map.WriteHandler;
import jef.video.BitMap;
import jef.video.BitMapImpl;
import jef.video.PaletteInitializer;
import jef.video.VideoEmulator;
import jef.video.VideoFinalizer;
import jef.video.VideoInitializer;
import jef.video.VideoRenderer;

public class Troangel extends MAMEVideo implements VideoEmulator, VideoRenderer, VideoInitializer, VideoFinalizer, PaletteInitializer {
   static int flipscreen;
   static int troangel_scroll;
   public int[] Ftroangel_scroll = new int[1];
   BasicEmulator m;

   private void draw_background() {
      int var1;
      for(var1 = videoram_size - 2; var1 >= 0; var1 -= 2) {
         if(this.dirtybuffer[var1] || this.dirtybuffer[var1 + 1]) {
            boolean[] var10 = this.dirtybuffer;
            this.dirtybuffer[var1 + 1] = false;
            var10[var1] = false;
            int var7 = var1 / 2 % 32;
            int var6 = var1 / 2 / 32;
            char var8 = this.RAM[videoram + var1];
            char var9 = this.RAM[videoram + var1 + 1];
            int var5 = var8 & 32;
            int var4 = var5;
            int var3 = var7;
            int var2 = var6;
            if(flipscreen != 0) {
               var3 = 31 - var7;
               var2 = 31 - var6;
               var4 = ~var5;
            }

            this.draw(this.tmpbitmap, this.Machine_gfx[0], var9 + ((var8 & 192) << 2), var8 & 31, var4, flipscreen, var3 * 8, var2 * 8, 0, -1, 0);
         }
      }

      int[] var11 = new int[256];

      for(var1 = 0; var1 < 64; ++var1) {
         var11[var1] = 0;
      }

      for(var1 = 64; var1 < 128; ++var1) {
         var11[var1] = -this.RAM[troangel_scroll + 64];
      }

      for(var1 = 128; var1 < 256; ++var1) {
         var11[var1] = -this.RAM[troangel_scroll + var1];
      }

      this.copyScrollBitMap(this.bitmap, this.tmpbitmap, 256, var11, 0, 0, this.visible, -1, 0);
   }

   private void draw_sprites() {
      for(int var2 = spriteram_size - 4; var2 >= 0; var2 -= 4) {
         char var12 = this.RAM[spriteram + var2 + 1];
         char var10 = this.RAM[spriteram + var2 + 3];
         int var9 = (224 - this.RAM[spriteram + var2 + 0] - 32 & 255) + 32;
         char var11 = this.RAM[spriteram + var2 + 2];
         int var7 = var12 & 128;
         int var8 = var12 & 64;
         int var1 = 0;
         if((var11 & 128) != 0) {
            var1 = 0 + 1;
         }

         int var3 = var1;
         if((var12 & 32) != 0) {
            var3 = var1 + 2;
         }

         int var6 = var8;
         int var5 = var7;
         int var4 = var10;
         var1 = var9;
         if(flipscreen != 0) {
            var4 = 240 - var10;
            var1 = 224 - var9;
            var6 = ~var8;
            var5 = ~var7;
         }

         this.draw(this.bitmap, this.Machine_gfx[var3 + 1], var11 & 63, var12 & 31, var6, var5, var4, var1, this.Machine_visible_area, 1, 0);
      }

   }

   public void init(EmulatorProperties var1) {
      super.init_bis(var1);
      super.init(var1);
      troangel_scroll = this.Ftroangel_scroll[0];
      this.tmpbitmap = new BitMapImpl(256, 256);
   }

   public void initPalette() {
      int var2 = 0;

      int var1;
      for(var1 = 0; var1 < 256; ++var1) {
         this.setPaletteColor(var1, (this.colorPROM[var2 + 256] >> 2 & 1) * 71 + 0 + (this.colorPROM[var2 + 256] >> 3 & 1) * 151, (this.colorPROM[var2 + 0] >> 3 & 1) * 33 + (this.colorPROM[var2 + 256] >> 0 & 1) * 71 + (this.colorPROM[var2 + 256] >> 1 & 1) * 151, (this.colorPROM[var2 + 0] >> 0 & 1) * 33 + (this.colorPROM[var2 + 0] >> 1 & 1) * 71 + (this.colorPROM[var2 + 0] >> 2 & 1) * 151);
         this.setColor(0, var1, var1);
         ++var2;
      }

      var2 += 256;

      for(var1 = 0; var1 < 16; ++var1) {
         this.setPaletteColor(var1 + 256, (this.colorPROM[var2] >> 6 & 1) * 71 + 0 + (this.colorPROM[var2] >> 7 & 1) * 151, (this.colorPROM[var2] >> 3 & 1) * 33 + (this.colorPROM[var2] >> 4 & 1) * 71 + (this.colorPROM[var2] >> 5 & 1) * 151, (this.colorPROM[var2] >> 0 & 1) * 33 + (this.colorPROM[var2] >> 1 & 1) * 71 + (this.colorPROM[var2] >> 2 & 1) * 151);
         ++var2;
      }

      var2 += 16;

      for(var1 = 0; var1 < this.getTotalColors(1); ++var1) {
         this.setColor(1, var1, (~this.colorPROM[var2] & 15) + 256);
         this.setColor(2, var1, (~this.colorPROM[var2] & 15) + 256);
         this.setColor(3, var1, (~this.colorPROM[var2] & 15) + 256);
         this.setColor(4, var1, (~this.colorPROM[var2] & 15) + 256);
         ++var2;
      }

   }

   public BitMap renderVideo() {
      this.draw_background();
      this.draw_sprites();
      return this.bitmap;
   }

   public void setMachine(BasicEmulator var1) {
      this.m = var1;
   }

   public WriteHandler troangel_flipscreen_w() {
      return new Troangel_flipscreen_w();
   }

   class Troangel_flipscreen_w implements WriteHandler {
      public void write(int var1, int var2) {
      }
   }
}
