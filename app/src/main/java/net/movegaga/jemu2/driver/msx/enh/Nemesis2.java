package net.movegaga.jemu2.driver.msx.enh;

import net.movegaga.jemu2.driver.msx.MSX;

import jef.video.BitMap;

public class Nemesis2 extends MSX {
   public static final int TYPE_NEMESIS1 = 57858;
   public static final int TYPE_NEMESIS2 = 57858;
   private int address;
   int pixelOffset = 0;
   int prev;
   int prevPixelOffs = 0;
   int scroll = 0;
   int timer = 0;

   public Nemesis2() {
      super.vdp.skipSprites(true);
      super.vdp.displayStartOffset = -50;
      this.address = '\ue202';
   }

   private int getOffs(int var1) {
      if(this.timer < 22) {
         var1 = (this.scroll & 15) >> 1;
      } else {
         var1 = 0;
      }

      return var1;
   }

   private void setScroll(int var1) {
      if(var1 != this.prev) {
         this.prev = var1;
      } else {
         switch(var1) {
         case 1:
            this.scroll = 0;
            break;
         case 2:
            this.scroll = 1;
            break;
         case 4:
            this.scroll = 2;
            break;
         case 8:
            this.scroll = 3;
            break;
         case 16:
            this.scroll = 4;
            break;
         case 32:
            this.scroll = 5;
            break;
         case 64:
            this.scroll = 6;
            break;
         case 128:
            this.scroll = 7;
         }
      }

   }

   public void renderVideoPost() {
      BitMap var2 = this.backbuffer;
      this.setScroll(this.ramMapper.read(this.address));
      int[] var3 = var2.getPixels();

      for(int var1 = 4; var1 < 186; ++var1) {
         System.arraycopy(var3, var2.getWidth() * var1 + 8 + this.scroll, var3, var2.getWidth() * var1 + 8, 256 - this.scroll);
      }

      this.vdp.renderSpritesPost(var2.getPixels());
   }

   public void renderVideoPost2() {
      BitMap var2 = this.backbuffer;
      this.prevPixelOffs = this.pixelOffset;
      this.pixelOffset = this.ramMapper.read('\ue320') & 7;
      if(this.prevPixelOffs < this.pixelOffset) {
         this.timer = 0;
      }

      if(this.timer == 1) {
         this.scroll = 0;
      }

      ++this.timer;
      if(this.timer < 18 && this.scroll < 15) {
         ++this.scroll;
      }

      int[] var3 = var2.getPixels();

      for(int var1 = 4; var1 < 186; ++var1) {
         System.arraycopy(var3, var2.getWidth() * var1 + 8 + this.getOffs(this.scroll), var3, var2.getWidth() * var1 + 8, 256 - this.getOffs(this.scroll));
      }

      this.vdp.renderSpritesPost(var2.getPixels());
   }

   public void setType(int var1) {
      this.address = var1;
   }
}
