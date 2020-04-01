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

public class Mrjong extends MAMEVideo implements VideoEmulator, VideoRenderer, VideoInitializer, VideoFinalizer, PaletteInitializer {
   static int flipscreen;
   BasicEmulator m;

   public void init(EmulatorProperties var1) {
      super.init_bis(var1);
      super.init(var1);
   }

   public void initPalette() {
      int var2 = 0;

      int var1;
      for(var1 = 0; var1 < this.Machine_drv_total_colors; ++var1) {
         this.setPaletteColor(var1, (this.colorPROM[var2] >> 0 & 1) * 33 + (this.colorPROM[var2] >> 1 & 1) * 71 + (this.colorPROM[var2] >> 2 & 1) * 151, (this.colorPROM[var2] >> 3 & 1) * 33 + (this.colorPROM[var2] >> 4 & 1) * 71 + (this.colorPROM[var2] >> 5 & 1) * 151, (this.colorPROM[var2] >> 6 & 1) * 71 + 0 + (this.colorPROM[var2] >> 7 & 1) * 151);
         ++var2;
      }

      var2 += 16;

      for(var1 = 0; var1 < this.getTotalColors(0); ++var2) {
         this.setColor(0, var1, this.colorPROM[var2] & 15);
         this.setColor(1, var1, this.colorPROM[var2] & 15);
         ++var1;
      }

   }

   public WriteHandler mrjong_flipscreen_w() {
      return new Mrjong_flipscreen_w();
   }

   public BitMap renderVideo() {
      int var1;
      int var2;
      int var3;
      int var4;
      int var5;
      int var6;
      int var7;
      int var9;
      char var10;
      char var11;
      char var12;
      for(var1 = videoram_size - 1; var1 > 0; --var1) {
         if(this.dirtybuffer[var1]) {
            this.dirtybuffer[var1] = false;
            var12 = this.RAM[videoram + var1];
            var10 = this.RAM[colorram + var1];
            var7 = (this.RAM[colorram + var1] & 64) >> 6;
            var6 = (this.RAM[colorram + var1] & 128) >> 7;
            var11 = this.RAM[colorram + var1];
            var9 = 31 - var1 % 32;
            int var8 = 31 - var1 / 32;
            var5 = var7;
            var4 = var6;
            var3 = var9;
            var2 = var8;
            if(flipscreen != 0) {
               var3 = 31 - var9;
               var2 = 31 - var8;
               var5 = ~var7;
               var4 = ~var6;
            }

            this.draw(this.tmpbitmap, this.Machine_gfx[0], var12 | (var10 & 32) << 3, var11 & 31, var5, var4, var3 * 8, var2 * 8, -1, 0);
         }
      }

      this.copyBitMap(this.bitmap, this.tmpbitmap, 0, 0, 0, 0, this.Machine_visible_area, -1, 0);

      for(var1 = spriteram_size - 4; var1 >= 0; var1 -= 4) {
         var11 = this.RAM[spriteram + var1 + 1];
         var10 = this.RAM[spriteram + var1 + 3];
         var7 = (this.RAM[spriteram + var1 + 1] & 1) >> 0;
         var6 = (this.RAM[spriteram + var1 + 1] & 2) >> 1;
         var12 = this.RAM[spriteram + var1 + 3];
         var9 = 224 - this.RAM[spriteram + var1 + 2];
         char var13 = this.RAM[spriteram + var1 + 0];
         var5 = var7;
         var4 = var6;
         var3 = var9;
         var2 = var13;
         if(flipscreen != 0) {
            var3 = 208 - var9;
            var2 = 240 - var13;
            var5 = ~var7;
            var4 = ~var6;
         }

         this.draw(this.bitmap, this.Machine_gfx[1], var11 >> 2 & 63 | (var10 & 32) << 1, var12 & 31, var5, var4, var3, var2, this.Machine_visible_area, 1, 0);
      }

      return this.bitmap;
   }

   public void setMachine(BasicEmulator var1) {
      this.m = var1;
   }

   class Mrjong_flipscreen_w implements WriteHandler {
      public void write(int var1, int var2) {
      }
   }
}
