package net.movegaga.jemu2.driver.arcade.snowbros;

import net.movegaga.jemu2.driver.BaseVideo;

import jef.machine.EmulatorProperties;
import jef.map.MemoryMap;
import jef.map.ReadHandler;
import jef.map.WriteHandler;
import jef.video.BitMap;
import jef.video.BitMapImpl;
import jef.video.VideoEmulator;
import jef.video.VideoRenderer;

public class VSnowbros extends BaseVideo implements VideoEmulator, VideoRenderer {
   static final int MODE_SNOWBRO3 = 1;
   static final int MODE_SNOWBROS = 0;
   private BitMap black;
   private MemoryMap memMap;
   int mode;
   int[] paletteRAM = new int[1024];

   private int read16(int var1) {
      return this.memMap.read(var1) << 8 | this.memMap.read(var1 + 1);
   }

   private void renderSnowBro3() {
      int var5 = 0;
      int var3 = 0;

      int var2;
      for(int var4 = 0; var4 < 8703; var3 = var2) {
         byte var7 = 0;
         int var11 = this.memMap.read(7340032 + var4 + 7);
         var2 = this.memMap.read(7340032 + var4 + 9);
         int var6 = this.memMap.read(7340032 + var4 + 11);
         int var1 = this.memMap.read(7340032 + var4 + 13);
         int var9 = this.memMap.read(7340032 + var4 + 15);
         int var8 = var11 >> 4 & 15;
         int var10 = ((var9 & 15) << 8) + var1;
         var1 = var2;
         if((var11 & 1) != 0) {
            var1 = -1 - (var2 ^ 255);
         }

         var2 = var6;
         if((var11 & 2) != 0) {
            var2 = -1 - (var6 ^ 255);
         }

         if((var11 & 4) != 0) {
            var1 += var5;
            var3 += var2;
            var2 = var1;
         } else {
            var3 = var2;
            var2 = var1;
         }

         var1 = var2;
         if(var2 > 511) {
            var1 = var2 & 511;
         }

         var2 = var3;
         if(var3 > 511) {
            var2 = var3 & 511;
         }

         byte var12 = var7;
         var5 = var8;
         if(var4 < 4096) {
            var12 = 1;
            var5 = 1;
            System.out.println("TILE=" + var10 + ", sx/sy=" + var1 + "/" + var2);
         }

         this.drawVisible(this.getBackBuffer(), var12, var10, var5, var9 & 128, (var9 & 64) << 1, var1, var2, 1, 0);
         var4 += 16;
         var5 = var1;
      }

   }

   private void renderSnowBros() {
      int var5 = 0;
      int var3 = 0;

      int var2;
      for(int var4 = 0; var4 < 7680; var3 = var2) {
         var2 = this.read16(7340040 + var4) & 255;
         int var6 = this.read16(7340042 + var4) & 255;
         int var7 = this.read16(7340038 + var4);
         int var1 = var2;
         if((var7 & 1) != 0) {
            var1 = -1 - (var2 ^ 255);
         }

         var2 = var6;
         if((var7 & 2) != 0) {
            var2 = -1 - (var6 ^ 255);
         }

         if((var7 & 4) != 0) {
            var1 += var5;
            var3 += var2;
            var2 = var1;
         } else {
            var3 = var2;
            var2 = var1;
         }

         var1 = var2;
         if(var2 > 511) {
            var1 = var2 & 511;
         }

         var2 = var3;
         if(var3 > 511) {
            var2 = var3 & 511;
         }

         if(var1 > -16 && var2 > 0 && var1 < 256 && var2 < 240) {
            var3 = this.read16(7340046 + var4);
            var5 = ((var3 & 15) << 8) + (this.read16(7340044 + var4) & 255);
            if(var5 != 0) {
               this.drawVisible(this.getBackBuffer(), 0, var5, (var7 & 240) >> 4, var3 & 128, var3 & 64, var1, var2, 1, 0);
            }
         }

         var4 += 16;
         var5 = var1;
      }

   }

   public void init(EmulatorProperties var1) {
      super.init(var1);
      this.black = new BitMapImpl(this.getBackBuffer().getWidth(), this.getBackBuffer().getHeight());
   }

   public ReadHandler readPaletteRAM() {
      return new ReadPaletteRAM();
   }

   public BitMap renderVideo() {
      this.black.toPixels(this.getBackBuffer().getPixels());
      switch(this.mode) {
      case 0:
         this.renderSnowBros();
         break;
      case 1:
         this.renderSnowBro3();
      }

      return this.getBackBuffer();
   }

   public void setRegions(MemoryMap var1) {
      this.memMap = var1;
   }

   public WriteHandler writePaletteRAM() {
      return new WritePaletteRAM();
   }

   public class ReadPaletteRAM implements ReadHandler {
      public int read(int var1) {
         return VSnowbros.this.paletteRAM[var1 & 511];
      }
   }

   public class WritePaletteRAM implements WriteHandler {
      public void write(int var1, int var2) {
         var1 &= 1023;
         VSnowbros.this.paletteRAM[var1] = var2;
         VSnowbros.this.changeColor_xBBBBBGGGGGRRRRR(var1 >> 1, VSnowbros.this.paletteRAM[var1 & 16777214] << 8 | VSnowbros.this.paletteRAM[(16777214 & var1) + 1]);
      }
   }
}
