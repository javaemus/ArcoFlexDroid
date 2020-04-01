package net.movegaga.jemu2.driver.arcade.pingpong;

import cottage.mame.MAMEVideo;
import jef.machine.EmulatorProperties;
import jef.video.BitMap;
import jef.video.PaletteInitializer;
import jef.video.VideoEmulator;
import jef.video.VideoFinalizer;
import jef.video.VideoInitializer;
import jef.video.VideoRenderer;

public class VPingpong extends MAMEVideo implements VideoEmulator, VideoRenderer, VideoInitializer, VideoFinalizer, PaletteInitializer {
   public void init(EmulatorProperties var1) {
      super.init_bis(var1);
      super.init(var1);
   }

   public void initPalette() {
      int var1 = 0;

      int var2;
      for(var2 = 0; var2 < this.total_colors; ++var2) {
         this.setPaletteColor(var2, (this.colorPROM[var2] >> 0 & 1) * 33 + (this.colorPROM[var2] >> 1 & 1) * 71 + (this.colorPROM[var2] >> 2 & 1) * 151, (this.colorPROM[var2] >> 3 & 1) * 33 + (this.colorPROM[var2] >> 4 & 1) * 71 + (this.colorPROM[var2] >> 5 & 1) * 151, (this.colorPROM[var2] >> 6 & 1) * 71 + 0 + (this.colorPROM[var2] >> 7 & 1) * 151);
         ++var1;
      }

      for(var2 = 0; var2 < this.getTotalColors(1); ++var1) {
         int var3 = this.colorPROM[var1] & 15;
         this.setColor(1, var2, (var3 >> 0 & 1) << 3 | (var3 >> 1 & 1) << 2 | (var3 >> 2 & 1) << 1 | (var3 >> 3 & 1) << 0);
         ++var2;
      }

      for(var2 = 0; var2 < this.getTotalColors(0); ++var1) {
         this.setColor(0, var2, (this.colorPROM[var1] & 15) + 16);
         ++var2;
      }

   }

   public BitMap renderVideo() {
      int var1;
      char var2;
      char var3;
      char var4;
      char var5;
      char var7;
      for(var1 = videoram_size - 1; var1 >= 0; --var1) {
         if(this.dirtybuffer[var1]) {
            int var6 = var1 / 32;
            this.dirtybuffer[var1] = false;
            var7 = this.RAM[colorram + var1];
            var5 = this.RAM[colorram + var1];
            var3 = this.RAM[colorram + var1];
            var4 = this.RAM[videoram + var1];
            var2 = this.RAM[colorram + var1];
            this.draw(this.tmpbitmap, this.Machine_gfx[0], var4 + ((var2 & 32) << 3), var3 & 31, var7 & 64, var5 & 128, var1 % 32 * 8, var6 * 8, -1, 0);
         }
      }

      this.copyBitMap(this.bitmap, this.tmpbitmap, 0, 0, 0, 0, this.Machine_visible_area, -1, 0);

      for(var1 = spriteram_size - 4; var1 >= 0; var1 -= 4) {
         char var8 = this.RAM[spriteram + var1 + 3];
         var2 = this.RAM[spriteram + var1 + 1];
         var7 = this.RAM[spriteram + var1];
         var5 = this.RAM[spriteram + var1];
         var4 = this.RAM[spriteram + var1];
         var3 = this.RAM[spriteram + var1 + 2];
         this.draw(this.bitmap, this.Machine_gfx[1], var3 & 127, var4 & 31, var7 & 64, var5 & 128, var8, 241 - var2, 2, 0);
      }

      return this.bitmap;
   }
}
