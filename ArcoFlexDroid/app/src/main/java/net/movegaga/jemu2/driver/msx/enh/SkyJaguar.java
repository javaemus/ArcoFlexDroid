package net.movegaga.jemu2.driver.msx.enh;

import net.movegaga.jemu2.driver.msx.MSX;

import jef.video.BitMap;

public class SkyJaguar extends MSX {
   int scroll;

   public SkyJaguar() {
      super.vdp.skipSprites(true);
   }

   public void renderVideoPost() {
      BitMap var3 = this.backbuffer;
      this.scroll = this.ramMapper.read('\ue322') >> 1 & 7;

      for(int var1 = 16; var1 < 200; ++var1) {
         for(int var2 = 16; var2 < 200; ++var2) {
            var3.setPixelFast(var1, var2 - this.scroll, var3.getPixel(var1, var2));
         }
      }

      this.vdp.renderSprites();
   }
}
