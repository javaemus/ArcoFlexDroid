package net.movegaga.jemu2.driver.arcade.m62;

import net.movegaga.jemu2.driver.BaseVideo;

import jef.machine.EmulatorProperties;
import jef.map.WriteHandler;
import jef.video.BitMap;
import jef.video.BitMapImpl;
import jef.video.Blitter;
import jef.video.PaletteInitializer;
import jef.video.VideoEmulator;
import jef.video.VideoInitializer;
import jef.video.VideoRenderer;

public class VM62 extends BaseVideo implements VideoEmulator, VideoRenderer, PaletteInitializer, WriteHandler {
   BitMap backBuffer;
   BitMap bgLayer;
   boolean[] dirty = new boolean[4096];
   M62 driver;
   int hScroll = 0;
   int[] rowScroll = new int[32];
   int spriteHeight;
   int vScroll = 0;

   public VM62(M62 var1) {
      this.driver = var1;
   }

   private void drawBackGround(int var1) {
      int var2;
      for(var2 = 4094; var2 >= 0; var2 -= 2) {
         if((this.dirty[var2] || this.dirty[var2 + 1]) && (var1 != 0 || (this.driver.mem_cpu1['퀀' + var2 + 1] & 4) == 0)) {
            this.dirty[var2] = false;
            this.dirty[var2 + 1] = false;
            int var6 = var2 / 2;
            int var4 = var2 / 2 / 64;
            char var5 = this.driver.mem_cpu1['퀀' + var2 + 1];
            BitMap var7 = this.bgLayer;
            char var3 = this.driver.mem_cpu1['퀀' + var2];
            this.draw(var7, 0, ((this.driver.mem_cpu1['퀀' + var2 + 1] & 192) << 2) + var3, this.driver.mem_cpu1['퀀' + var2 + 1] & 31, var5 & 32, 0, var6 % 64 * 8, var4 * 8, 1, -1, 0);
         }
      }

      var2 = -this.vScroll;
      if(var1 != 0) {
         this.bgLayer.toBitMap(this.backBuffer, 0, 0, 0, 0, 384, 256, 0);
      } else {
         this.bgLayer.toBitMap(this.backBuffer, 0, 0, 0, 0, 384, 256, -1);
      }

   }

   private void drawSprites(int var1) {
      for(int var5 = 0; var5 < 256; var5 += 8) {
         if(var1 == 0 || var1 != 0 && (this.driver.mem_cpu1['쀀' + var5] & 16) != 0) {
            int var8 = this.driver.mem_cpu1['쀀' + var5 + 4] + ((this.driver.mem_cpu1['쀀' + var5 + 5] & 7) << 8);
            char var10 = this.driver.mem_cpu1['쀀' + var5 + 0];
            char var11 = this.driver.mem_cpu1['쀀' + var5 + 7];
            char var9 = this.driver.mem_cpu1['쀀' + var5 + 6];
            int var6 = 369 - ((this.driver.mem_cpu1['쀀' + var5 + 3] & 1) * 256 + this.driver.mem_cpu1['쀀' + var5 + 2]);
            boolean var12;
            if((this.driver.mem_cpu1['쀀' + var5 + 5] & 64) != 0) {
               var12 = true;
            } else {
               var12 = false;
            }

            boolean var13;
            if((this.driver.mem_cpu1['쀀' + var5 + 5] & 128) != 0) {
               var13 = true;
            } else {
               var13 = false;
            }

            char var7 = this.driver.mem_prom[(var8 >> 5 & 31) + 1536];
            int var2;
            int var3;
            int var4;
            if(var7 == 1) {
               var2 = var8 & -2;
               var3 = var6 - 16;
               var4 = var7;
            } else {
               var2 = var8;
               var4 = var7;
               var3 = var6;
               if(var7 == 2) {
                  var4 = 3;
                  var2 = var8 & -4;
                  var3 = var6 - 48;
               }
            }

            byte var14;
            if(var13) {
               byte var15 = -1;
               var6 = var2 + var4;
               var14 = var15;
            } else {
               byte var16 = 1;
               var6 = var2;
               var14 = var16;
            }

            int var17;
            do {
               this.drawVisible(1, var6 + var4 * var14, var10 & 15, var12, var13, (var11 & 1) * 256 + var9, var3 + var4 * 16, 1, 0);
               var17 = var4 - 1;
               var4 = var17;
            } while(var17 >= 0);
         }
      }

   }

   void drawBackGround() {
      int var1;
      for(var1 = 2047; var1 >= 0; --var1) {
         if(this.dirty[var1] || this.dirty[var1 + 2048]) {
            boolean[] var5 = this.dirty;
            this.dirty[var1 + 2048] = false;
            var5[var1] = false;
            int var2 = var1 / 64;
            char var4 = this.driver.mem_cpu1['퀀' + var1 + 2048];
            BitMap var6 = this.bgLayer;
            char var3 = this.driver.mem_cpu1['퀀' + var1];
            this.draw(var6, 0, (this.driver.mem_cpu1['퀀' + var1 + 2048] & 192) * 4 + var3, this.driver.mem_cpu1['퀀' + var1 + 2048] & 31, var4 & 32, 0, var1 % 64 * 8, var2 * 8, 0, -1, 0);
         }
      }

      for(var1 = 0; var1 < 6; ++var1) {
         this.rowScroll[var1] = 128;
      }

      for(var1 = 6; var1 < 32; ++var1) {
         this.rowScroll[var1] = this.hScroll + 128;
      }

      Blitter.copyWrappedRowsHorizontal(this.bgLayer, this.backBuffer, this.rowScroll, 8, 0);
   }

   public void init(EmulatorProperties var1) {
      super.init(var1);
      this.bgLayer = new BitMapImpl(384, 256);
      this.backBuffer = this.getBackBuffer();
   }

   public VideoInitializer initKungFum() {
      return new KunfuMStart();
   }

   public void initPalette() {
      int var2 = 0;

      for(int var1 = 0; var1 < this.getTotalColors(); ++var1) {
         int var3 = this.driver.mem_prom[var1] & 15;
         int var4 = this.driver.mem_prom[this.getTotalColors() + var1] >> 0 & 15;
         int var5 = this.driver.mem_prom[this.getTotalColors() * 2 + var1] >> 0 & 15;
         this.setPaletteColor(var1, var3 | var3 << 4, var4 | var4 << 4, var5 | var5 << 4);
         ++var2;
      }

      this.getTotalColors();
   }

   public VideoRenderer kungfumRenderer() {
      return new KungfumRenderer(this);
   }

   public BitMap renderVideo() {
      this.drawBackGround(0);
      this.drawSprites(0);
      this.drawBackGround(1);
      this.drawSprites(1);
      return this.backBuffer;
   }

   void setBgHScroll(int var1, int var2) {
      switch(var1 & 1) {
      case 0:
         this.hScroll = this.hScroll & '\uff00' | var2;
         break;
      case 1:
         this.hScroll = this.hScroll & 255 | var2 << 8;
      }

   }

   public void write(int var1, int var2) {
      if(this.driver.mem_cpu1[var1] != var2) {
         this.driver.mem_cpu1[var1] = (char)var2;
         this.dirty[var1 & 2047] = true;
         this.dirty[(var1 & 2047) + 2048] = true;
      }

   }

   public WriteHandler writeScrollHigh() {
      return new WriteScrollHighKungFum();
   }

   public WriteHandler writeScrollLow() {
      return new WriteScrollLowKungFum();
   }

   public WriteHandler writeVScroll() {
      return new WriteVScroll();
   }

   public WriteHandler writeVScrollLdrun4() {
      return new WriteVScrollLdrun4();
   }

   public class KunfuMStart implements VideoInitializer {
      public int initVideo() {
         VM62.this.bgLayer = new BitMapImpl(512, 256);
         return 0;
      }
   }

   public class KungfumRenderer implements VideoRenderer {
      BaseVideo v;

      public KungfumRenderer(VM62 var2) {
         this.v = var2;
      }

      public BitMap renderVideo() {
         VM62.this.drawBackGround();
         VM62.this.drawSprites(0);
         VM62.this.drawSprites(1);
         return VM62.this.backBuffer;
      }

      public void renderVideoPost() {
         this.v.renderVideoPost();
      }
   }

   public class WriteScrollHighKungFum implements WriteHandler {
      public void write(int var1, int var2) {
         VM62.this.setBgHScroll(1, var2);
      }
   }

   public class WriteScrollLowKungFum implements WriteHandler {
      public void write(int var1, int var2) {
         VM62.this.setBgHScroll(0, var2);
      }
   }

   public class WriteVScroll implements WriteHandler {
      public void write(int var1, int var2) {
         VM62.this.vScroll = var2;
      }
   }

   public class WriteVScrollLdrun4 implements WriteHandler {
      public void write(int var1, int var2) {
         VM62.this.setBgHScroll(var1 ^ 1, var2);
      }
   }
}
