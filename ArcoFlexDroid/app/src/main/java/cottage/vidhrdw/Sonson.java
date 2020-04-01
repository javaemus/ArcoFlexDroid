package cottage.vidhrdw;

import cottage.mame.MAMEVideo;
import jef.machine.EmulatorProperties;
import jef.video.BitMap;
import jef.video.BitMapImpl;
import jef.video.PaletteInitializer;
import jef.video.VideoEmulator;
import jef.video.VideoFinalizer;
import jef.video.VideoInitializer;
import jef.video.VideoRenderer;

public class Sonson extends MAMEVideo implements VideoEmulator, VideoRenderer, VideoInitializer, VideoFinalizer, PaletteInitializer {
   static int sonson_scrollx;
   public int[] Fsonson_scrollx = new int[1];

   public void init(EmulatorProperties var1) {
      super.init_bis(var1);
      super.init(var1);
      sonson_scrollx = this.Fsonson_scrollx[0];
   }

   public void initPalette() {
      int var1;
      for(var1 = 0; var1 < this.Machine_drv_total_colors; ++var1) {
         this.setPaletteColor(var1, (this.colorPROM[this.Machine_drv_total_colors + var1] >> 0 & 1) * 14 + (this.colorPROM[this.Machine_drv_total_colors + var1] >> 1 & 1) * 31 + (this.colorPROM[this.Machine_drv_total_colors + var1] >> 2 & 1) * 67 + (this.colorPROM[this.Machine_drv_total_colors + var1] >> 3 & 1) * 143, (this.colorPROM[var1] >> 4 & 1) * 14 + (this.colorPROM[var1] >> 5 & 1) * 31 + (this.colorPROM[var1] >> 6 & 1) * 67 + (this.colorPROM[var1] >> 7 & 1) * 143, (this.colorPROM[var1] >> 0 & 1) * 14 + (this.colorPROM[var1] >> 1 & 1) * 31 + (this.colorPROM[var1] >> 2 & 1) * 67 + (this.colorPROM[var1] >> 3 & 1) * 143);
      }

      var1 = 0 + this.Machine_drv_total_colors * 2;

      int var2;
      for(var2 = 0; var2 < this.getTotalColors(0); ++var1) {
         this.setColor(0, var2, this.colorPROM[var1] & 15);
         ++var2;
      }

      for(var2 = 0; var2 < this.getTotalColors(1); ++var1) {
         this.setColor(1, var2, (this.colorPROM[var1] & 15) + 16);
         ++var2;
      }

   }

   public int initVideo() {
      this.tmpbitmap = new BitMapImpl(256, 256);
      return 0;
   }

   public BitMap renderVideo() {
      int var1;
      for(var1 = videoram_size - 1; var1 >= 0; --var1) {
         if(this.dirtybuffer[var1]) {
            this.dirtybuffer[var1] = false;
            int var2 = var1 / 32;
            BitMap var5 = this.tmpbitmap;
            int var3 = this.Machine_gfx[0];
            char var4 = this.RAM[videoram + var1];
            this.draw(var5, var3, (this.RAM[colorram + var1] & 3) * 256 + var4, this.RAM[colorram + var1] >> 2, 0, 0, var1 % 32 * 8, var2 * 8, 0, -1, 0);
         }
      }

      int[] var6 = new int[32];

      for(var1 = 0; var1 < 5; ++var1) {
         var6[var1] = 0;
      }

      for(var1 = 5; var1 < 32; ++var1) {
         var6[var1] = -this.RAM[sonson_scrollx];
      }

      this.copyScrollBitMap(this.bitmap, this.tmpbitmap, 32, var6, 0, 0, this.visible, -1, 0);

      for(var1 = spriteram_size - 4; var1 >= 0; var1 -= 4) {
         this.draw(this.bitmap, this.Machine_gfx[1], this.RAM[spriteram + var1 + 2] + ((this.RAM[spriteram + var1 + 1] & 32) << 3), this.RAM[spriteram + var1 + 1] & 31, ~this.RAM[spriteram + var1 + 1] & 64, ~this.RAM[spriteram + var1 + 1] & 128, this.RAM[spriteram + var1 + 3], this.RAM[spriteram + var1 + 0], this.Machine_visible_area, 1, 0);
      }

      return this.bitmap;
   }
}
