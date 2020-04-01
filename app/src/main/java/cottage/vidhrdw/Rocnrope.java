package cottage.vidhrdw;

import cottage.mame.MAMEVideo;
import jef.machine.EmulatorProperties;
import jef.video.BitMap;
import jef.video.PaletteInitializer;
import jef.video.VideoEmulator;
import jef.video.VideoFinalizer;
import jef.video.VideoInitializer;
import jef.video.VideoRenderer;

public class Rocnrope extends MAMEVideo implements VideoEmulator, VideoRenderer, VideoInitializer, VideoFinalizer, PaletteInitializer {
   int flipscreen;

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
         this.setColor(1, var2, this.colorPROM[var1] & 15);
         ++var2;
      }

      for(var2 = 0; var2 < this.getTotalColors(0); ++var1) {
         this.setColor(0, var2, this.colorPROM[var1] & 15);
         ++var2;
      }

   }

   public BitMap renderVideo() {
      int var1;
      for(var1 = videoram_size - 1; var1 >= 0; --var1) {
         if(this.dirtybuffer[var1]) {
            this.dirtybuffer[var1] = false;
            int var5 = var1 / 32;
            char var2 = this.RAM[colorram + var1];
            char var3 = this.RAM[colorram + var1];
            BitMap var6 = this.tmpbitmap;
            char var4 = this.RAM[videoram + var1];
            this.draw(var6, 0, (this.RAM[colorram + var1] & 128) * 2 + var4, this.RAM[colorram + var1] & 15, var2 & 64, var3 & 32, var1 % 32 * 8, var5 * 8, -1, 0);
         }
      }

      this.tmpbitmap.toPixels(this.pixels);

      for(var1 = spriteram_size - 2; var1 >= 0; var1 -= 2) {
         this.draw(this.bitmap, 1, this.RAM[spriteram + var1 + 1], this.RAM[spriteram_2 + var1] & 15, this.RAM[spriteram_2 + var1] & 64, ~this.RAM[spriteram_2 + var1] & 128, 240 - this.RAM[spriteram + var1], this.RAM[spriteram_2 + var1 + 1], 2, 0);
      }

      return this.bitmap;
   }
}
