package net.movegaga.jemu2.driver.msx.enh;

import net.movegaga.jemu2.driver.msx.MSX;

import jef.video.BitMap;

public class Knightmare extends MSX {
   int mem = 0;
   int pmem = 0;
   int scroll = 0;
   int timer = 0;

   public Knightmare() {
      super.vdp.skipSprites(true);
      super.vdp.displayStartOffset = -50;
   }

   public void renderVideoPost() {
      BitMap var4 = this.backbuffer;
      this.pmem = this.mem;
      this.mem = this.ramMapper.read('\ue09a') & 7;
      if(this.pmem != this.mem) {
         this.timer = 0;
         this.scroll = 0;
      }

      ++this.timer;
      if(this.timer < 64 && this.scroll < 63) {
         ++this.scroll;
      }

      int[] var3 = var4.getPixels();
      int var2 = ((this.scroll + 0 & 63) >> 3) + 0 & 7;

      for(int var1 = 17 - var2; var1 < 186; ++var1) {
         System.arraycopy(var3, var4.getWidth() * var1 + 8, var3, (var1 - (7 - var2)) * var4.getWidth() + 8, 256);
      }

      this.vdp.renderSpritesPost(var4.getPixels());
   }
}
