package cottage.vidhrdw;

import cottage.mame.MAMEVideo;
import jef.machine.BasicEmulator;
import jef.machine.EmulatorProperties;
import jef.map.WriteHandler;
import jef.video.BitMap;
import jef.video.PaletteInitializer;
import jef.video.VideoEmulator;
import jef.video.VideoFinalizer;
import jef.video.VideoInitializer;
import jef.video.VideoRenderer;

public class Pooyan extends MAMEVideo implements VideoEmulator, VideoRenderer, VideoInitializer, VideoFinalizer, PaletteInitializer {
   static int flipscreen;
   BasicEmulator m;

   public void init(EmulatorProperties var1) {
      super.init_bis(var1);
      super.init(var1);
   }

   public void initPalette() {
      int var1 = 0;

      int var2;
      for(var2 = 0; var2 < this.Machine_drv_total_colors; ++var2) {
         this.setPaletteColor(var2, (this.colorPROM[var1] >> 0 & 1) * 33 + (this.colorPROM[var1] >> 1 & 1) * 71 + (this.colorPROM[var1] >> 2 & 1) * 151, (this.colorPROM[var1] >> 3 & 1) * 33 + (this.colorPROM[var1] >> 4 & 1) * 71 + (this.colorPROM[var1] >> 5 & 1) * 151, (this.colorPROM[var1] >> 6 & 1) * 71 + 0 + (this.colorPROM[var1] >> 7 & 1) * 151);
         ++var1;
      }

      for(var2 = 0; var2 < this.getTotalColors(1); ++var1) {
         this.setColor(1, var2, this.colorPROM[var1] & 15);
         ++var2;
      }

      for(var2 = 0; var2 < this.getTotalColors(0); ++var1) {
         this.setColor(0, var2, (this.colorPROM[var1] & 15) + 16);
         ++var2;
      }

   }

   public WriteHandler pooyan_flipscreen_w() {
      return new Pooyan_flipscreen_w();
   }

   public BitMap renderVideo() {
      int var1;
      for(var1 = videoram_size - 1; var1 >= 0; --var1) {
         if(this.dirtybuffer[var1]) {
            this.dirtybuffer[var1] = false;
            int var4 = var1 / 32;
            boolean var5;
            if((this.RAM['耀' + var1] & 64) == 0) {
               var5 = true;
            } else {
               var5 = false;
            }

            boolean var6;
            if((this.RAM['耀' + var1] & 128) == 0) {
               var6 = true;
            } else {
               var6 = false;
            }

            BitMap var7 = this.tmpbitmap;
            int var3 = this.Machine_gfx[0];
            char var2 = this.RAM[videoram + var1];
            this.draw(var7, var3, (this.RAM[colorram + var1] & 32) * 8 + var2, this.RAM['耀' + var1] & 15, var5, var6, (31 - var1 % 32) * 8, (31 - var4) * 8, -1, 0);
         }
      }

      this.copyBitMap(this.bitmap, this.tmpbitmap, 0, 0, 0, 0, this.Machine_visible_area, -1, 0);

      for(var1 = 0; var1 < spriteram_size; var1 += 2) {
         this.draw(this.bitmap, this.Machine_gfx[1], this.RAM[spriteram + var1 + 1], this.RAM[spriteram_2 + var1] & 15, this.RAM[spriteram_2 + var1] & 64, ~this.RAM[spriteram_2 + var1] & 128, 240 - this.RAM[spriteram + var1], this.RAM[spriteram_2 + var1 + 1], this.Machine_visible_area, 2, 0);
      }

      return this.bitmap;
   }

   public void setMachine(BasicEmulator var1) {
      this.m = var1;
   }

   class Pooyan_flipscreen_w implements WriteHandler {
      public void write(int var1, int var2) {
         if(Pooyan.flipscreen != (var2 & 1)) {
            Pooyan.flipscreen = var2 & 1;
            Pooyan.this.memset(Pooyan.this.dirtybuffer, 1, Pooyan.videoram_size);
         }

      }
   }
}
