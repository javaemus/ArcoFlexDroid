package net.movegaga.jemu2.driver.arcade.jrpacman;

import net.movegaga.jemu2.driver.BaseVideo;

import jef.machine.EmulatorProperties;
import jef.map.WriteHandler;
import jef.video.BitMap;
import jef.video.BitMapImpl;
import jef.video.Blitter;
import jef.video.PaletteInitializer;
import jef.video.VideoRenderer;

public class VJrpacman extends BaseVideo implements VideoRenderer, PaletteInitializer, WriteHandler {
   BitMap backBuffer;
   BitMap bgLayer;
   int charBank;
   int colorTableBank;
   boolean[] dirty = new boolean[2048];
   Jrpacman driver;
   int paletteBank;
   int[] rowScroll = new int[36];
   int spriteBank;

   public VJrpacman(Jrpacman var1) {
      this.driver = var1;
   }

   private void setAllDirty() {
      for(int var1 = 0; var1 < this.dirty.length; ++var1) {
         this.dirty[var1] = true;
      }

   }

   public void init(EmulatorProperties var1) {
      super.init(var1);
      this.backBuffer = this.getBackBuffer();
      this.bgLayer = new BitMapImpl(this.backBuffer.getWidth() * 2, this.backBuffer.getHeight());
   }

   public void initPalette() {
      int var1;
      for(var1 = 0; var1 < 32; ++var1) {
         this.setPaletteColor(var1, (this.driver.mem_prom[var1] >> 0 & 1) * 33 + (this.driver.mem_prom[var1] >> 1 & 1) * 71 + (this.driver.mem_prom[var1] >> 2 & 1) * 151, (this.driver.mem_prom[var1] >> 3 & 1) * 33 + (this.driver.mem_prom[var1 + 256] >> 0 & 1) * 71 + (this.driver.mem_prom[var1 + 256] >> 1 & 1) * 151, (this.driver.mem_prom[var1 + 256] >> 2 & 1) * 71 + 0 + (this.driver.mem_prom[var1 + 256] >> 3 & 1) * 151);
      }

      for(var1 = 0; var1 < 256; ++var1) {
         this.setColor(0, var1, this.driver.mem_prom[var1 + 512]);
         this.setColor(0, var1 + 256, this.driver.mem_prom[var1 + 512] + 16);
         if(this.driver.mem_prom[var1 + 512] != 0) {
            this.setColor(1, var1, this.driver.mem_prom[var1 + 512]);
            this.setColor(1, var1 + 256, this.driver.mem_prom[var1 + 512] + 16);
         } else {
            this.setColor(1, var1, 0);
            this.setColor(1, var1 + 256, 0);
         }
      }

   }

   public BitMap renderVideo() {
      int var1;
      BitMap var10;
      char var13;
      int var14;
      int var15;
      for(var1 = 2047; var1 >= 0; --var1) {
         if(this.dirty[var1]) {
            this.dirty[var1] = false;
            int var3 = var1 % 32;
            int var2 = var1 / 32;
            if(var2 >= 2 && var2 < 60) {
               int var4;
               char var5;
               char var6;
               if(var2 < 56) {
                  var10 = this.bgLayer;
                  var5 = this.driver.mem_cpu[var1 + 16384];
                  var4 = this.charBank;
                  var6 = this.driver.mem_cpu[var3 + 16384];
                  int var7 = this.colorTableBank;
                  this.drawVisible(var10, 0, var4 * 256 + var5, (this.paletteBank & 1) * 64 + (var6 & 31) + (var7 & 1) * 32, false, false, (var3 + 2) * 8, var2 * 8, -1, 0);
               } else if(var2 >= 58) {
                  var10 = this.bgLayer;
                  var6 = this.driver.mem_cpu[var1 + 16384];
                  var5 = this.driver.mem_cpu[var1 + 16384 + 128];
                  var4 = this.colorTableBank;
                  this.drawVisible(var10, 0, var6, (this.paletteBank & 1) * 64 + (var5 & 31) + (var4 & 1) * 32, false, false, (var2 - 58) * 8, (var3 - 2) * 8, -1, 0);
               } else {
                  var10 = this.bgLayer;
                  var13 = this.driver.mem_cpu[var1 + 16384];
                  var15 = this.charBank;
                  char var16 = this.driver.mem_cpu[var1 + 16384 + 128];
                  var14 = this.colorTableBank;
                  this.drawVisible(var10, 0, (var15 & 1) * 256 + var13, (this.paletteBank & 1) * 64 + (var16 & 31) + (var14 & 1) * 32, false, false, (var2 - 22) * 8, (var3 - 2) * 8, -1, 0);
               }
            }
         }
      }

      for(var1 = 0; var1 < 2; ++var1) {
         this.rowScroll[var1] = 208;
      }

      for(var1 = 2; var1 < 34; ++var1) {
         this.rowScroll[var1] = 208 - this.driver.mem_cpu[20608];
      }

      for(var1 = 34; var1 < 36; ++var1) {
         this.rowScroll[var1] = 208;
      }

      Blitter.copyWrappedRowsHorizontal(this.bgLayer, this.backBuffer, this.rowScroll, 8, 0);

      for(var1 = 14; var1 > 0; var1 -= 2) {
         char var11 = this.driver.mem_cpu[20597];
         var10 = this.backBuffer;
         char var12 = this.driver.mem_cpu[var1 + 20464];
         var13 = this.driver.mem_cpu[var1 + 20464 + 1];
         var14 = this.colorTableBank;
         var15 = this.paletteBank;
         boolean var8;
         if((this.driver.mem_cpu[var1 + 20464] & 1) != 0) {
            var8 = true;
         } else {
            var8 = false;
         }

         boolean var9;
         if((this.driver.mem_cpu[var1 + 20464] & 2) != 0) {
            var9 = true;
         } else {
            var9 = false;
         }

         this.drawVisible(var10, 1, (var11 & 1) * 64 + (var12 >> 2), (var15 & 1) * 64 + (var13 & 31) + (var14 & 1) * 32, var8, var9, 272 - this.driver.mem_cpu[var1 + 20576 + 1], this.driver.mem_cpu[var1 + 20576] - 31, 2, 0);
      }

      return this.backBuffer;
   }

   public void write(int var1, int var2) {
      if(this.driver.mem_cpu[var1] != var2) {
         this.driver.mem_cpu[var1] = (char)var2;
         var2 = var1 & 2047;
         this.dirty[var2] = true;
         if(var2 < 32) {
            for(var1 = 64; var1 < 1792; var1 += 32) {
               this.dirty[var1 + var2] = true;
            }
         } else if(var2 > 1792) {
            this.dirty[var2 & -129] = true;
         }
      }

   }

   public WriteHandler writeCharBank() {
      return new WriteCharBank();
   }

   public WriteHandler writeColTableBank() {
      return new WriteColTableBank();
   }

   public WriteHandler writePaletteBank() {
      return new WritePaletteBank();
   }

   public class WriteCharBank implements WriteHandler {
      public void write(int var1, int var2) {
         if(VJrpacman.this.charBank != var2) {
            VJrpacman.this.charBank = var2;
            VJrpacman.this.setAllDirty();
         }

      }
   }

   public class WriteColTableBank implements WriteHandler {
      public void write(int var1, int var2) {
         if(VJrpacman.this.colorTableBank != var2) {
            VJrpacman.this.colorTableBank = var2;
            VJrpacman.this.setAllDirty();
         }

      }
   }

   public class WritePaletteBank implements WriteHandler {
      public void write(int var1, int var2) {
         if(VJrpacman.this.paletteBank != var2) {
            VJrpacman.this.paletteBank = var2;
            VJrpacman.this.setAllDirty();
         }

      }
   }
}
