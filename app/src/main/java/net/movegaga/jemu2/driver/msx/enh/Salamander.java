package net.movegaga.jemu2.driver.msx.enh;

import net.movegaga.jemu2.driver.msx.MSX;

import jef.video.BitMap;

public class Salamander extends MSX {
   int mem = 0;
   int pmem = 0;
   int scroll = 0;
   int timer = 0;

   public Salamander() {
      super.vdp.skipSprites(true);
   }

   private int getOffs(int var1) {
      if(this.timer < 22) {
         var1 = (this.scroll & 15) >> 1;
      } else {
         var1 = 0;
      }

      return var1;
   }

   public void renderVideoPost() {
      TestValue.update();
      BitMap var3 = this.backbuffer;
      this.pmem = this.mem;
      this.mem = this.ramMapper.read('\ue302') & 7;
      if(this.pmem != this.mem) {
         this.timer = 0;
      }

      if(this.timer == 1) {
         this.scroll = 0;
      }

      ++this.timer;
      if(this.timer < 18 && this.scroll < 15) {
         ++this.scroll;
      }

      int[] var2 = var3.getPixels();

      for(int var1 = 4; var1 < 178; ++var1) {
         System.arraycopy(var2, var3.getWidth() * var1 + 8 + this.getOffs(this.scroll), var2, var3.getWidth() * var1 + 8, 256 - this.getOffs(this.scroll));
      }

      this.vdp.renderSpritesPost(var3.getPixels());
   }
}
