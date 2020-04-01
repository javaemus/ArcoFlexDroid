package net.movegaga.jemu2.driver.arcade.blktiger;

import net.movegaga.jemu2.JEmu2;
import net.movegaga.jemu2.driver.BaseVideo;

import jef.machine.EmulatorProperties;
import jef.map.ReadHandler;
import jef.map.WriteHandler;
import jef.video.BitMap;
import jef.video.BitMapImpl;
import jef.video.Blitter;
import jef.video.VideoEmulator;
import jef.video.VideoRenderer;

public class VBlktiger extends BaseVideo implements VideoEmulator, VideoRenderer {
   BitMap bgLayer1;
   BitMap bgLayer2;
   boolean bgLayerEnabled = true;
   BitMap charLayer;
   boolean charLayerEnabled = true;
   boolean[] dirty1 = new boolean[1024];
   boolean[] dirty2 = new boolean[16384];
   Blktiger driver;
   int screenLayout = 0;
   int scrollBank = 0;
   char[] scrollRAM = new char[16384];
   char[] scrollX = new char[2];
   char[] scrollY = new char[2];
   boolean spritesEnabled = true;

   public VBlktiger(Blktiger var1) {
      this.driver = var1;
   }

   public boolean blktiger() {
      return true;
   }

   public void init(EmulatorProperties var1) {
      super.init(var1);
      BitMap var2 = this.getBackBuffer();
      this.charLayer = new BitMapImpl(var2.getWidth(), var2.getHeight());
      this.bgLayer1 = new BitMapImpl(2048, 1024);
      this.bgLayer2 = new BitMapImpl(1024, 2048, this.bgLayer1.getPixels());
      this.getGfxManager(0).setTransparencyOverwrite(true);
   }

   public ReadHandler readBackGround(char[] var1) {
      return new ReadBackGround(var1);
   }

   public BitMap renderVideo() {
      int var1;
      int var2;
      char var3;
      char var4;
      char var5;
      char var6;
      boolean var13;
      char var16;
      BitMap var22;
      if(this.bgLayerEnabled) {
         int var7;
         int var12;
         boolean[] var14;
         char var15;
         if(this.screenLayout != 0) {
            var3 = this.scrollX[0];
            var5 = this.scrollX[1];
            var6 = this.scrollY[0];
            var4 = this.scrollY[1];

            for(var1 = 0; var1 < 18; ++var1) {
               var7 = (var6 >> 4) + var4 * 16 + var1 & 63;

               for(var2 = 0; var2 < 18; ++var2) {
                  int var8 = (var3 >> 4) + var5 * 16 + var2 & 127;
                  var12 = ((var8 & 240) << 5) + ((var7 & 240) << 8) + (var7 & 15) * 32 + (var8 & 15) * 2;
                  char var10 = this.scrollRAM[var12 + 1];
                  int var11 = (var10 & 120) >> 3;
                  if(this.dirty2[var12] || this.dirty2[var12 + 1] || this.getGfxManager(1).colorCodeHasChanged(var11)) {
                     char var9 = this.scrollRAM[var12];
                     var14 = this.dirty2;
                     this.dirty2[var12 + 1] = false;
                     var14[var12] = false;
                     var22 = this.bgLayer1;
                     if((var10 & 128) != 0) {
                        var13 = true;
                     } else {
                        var13 = false;
                     }

                     this.drawVisible(var22, 1, var9 + (var10 & 7) * 256, var11, var13, false, var8 * 16, var7 * 16, -1, 0);
                  }
               }
            }

            var4 = this.scrollX[0];
            var15 = this.scrollX[1];
            var16 = this.scrollY[0];
            var3 = this.scrollY[1];
            Blitter.copyWrappedBitMap(this.bgLayer1, this.getBackBuffer(), var4 + var15 * 256, var16 + var3 * 256);
         } else {
            var6 = this.scrollX[0];
            var5 = this.scrollX[1];
            var3 = this.scrollY[0];
            var4 = this.scrollY[1];

            for(var1 = 0; var1 < 18; ++var1) {
               var7 = (var3 >> 4) + var4 * 16 + var1 & 127;

               for(var2 = 0; var2 < 18; ++var2) {
                  int var19 = (var6 >> 4) + var5 * 16 + var2 & 63;
                  int var20 = ((var19 & 240) << 5) + ((var7 & 240) << 7) + (var7 & 15) * 32 + (var19 & 15) * 2;
                  char var21 = this.scrollRAM[var20 + 1];
                  var12 = (var21 & 120) >> 3;
                  if(this.dirty2[var20] || this.dirty2[var20 + 1] || this.getGfxManager(1).colorCodeHasChanged(var12)) {
                     char var18 = this.scrollRAM[var20];
                     var14 = this.dirty2;
                     this.dirty2[var20 + 1] = false;
                     var14[var20] = false;
                     var22 = this.bgLayer2;
                     if((var21 & 128) != 0) {
                        var13 = true;
                     } else {
                        var13 = false;
                     }

                     this.drawVisible(var22, 1, var18 + (var21 & 7) * 256, var12, var13, false, var19 * 16, var7 * 16, -1, 0);
                  }
               }
            }

            var4 = this.scrollX[0];
            var3 = this.scrollX[1];
            var16 = this.scrollY[0];
            var15 = this.scrollY[1];
            Blitter.copyWrappedBitMap(this.bgLayer2, this.getBackBuffer(), var4 + var3 * 256, var16 + var15 * 256);
         }
      }

      if(this.spritesEnabled) {
         for(var1 = 508; var1 >= 0; var1 -= 4) {
            var6 = this.driver.mem_cpu1['︀' + var1];
            char var17 = this.driver.mem_cpu1['︀' + var1 + 1];
            var16 = this.driver.mem_cpu1['︀' + var1 + 1];
            var3 = this.driver.mem_cpu1['︀' + var1 + 2];
            var4 = this.driver.mem_cpu1['︀' + var1 + 3];
            var5 = this.driver.mem_cpu1['︀' + var1 + 1];
            var22 = this.getBackBuffer();
            if((this.driver.mem_cpu1['︀' + var1 + 1] & 8) != 0) {
               var13 = true;
            } else {
               var13 = false;
            }

            this.drawVisible(var22, 2, var6 + ((var17 & 224) << 3), var16 & 7, var13, false, var4 - ((var5 & 16) << 4), var3, 1, 15);
         }
      }

      if(this.charLayerEnabled) {
         for(var1 = 1023; var1 >= 0; --var1) {
            if(this.dirty1[var1]) {
               this.dirty1[var1] = false;
               var2 = var1 / 32;
               this.drawVisible(this.charLayer, 0, this.driver.mem_cpu1['퀀' + var1] + ((this.driver.mem_cpu1['퐀' + var1] & 224) << 3), this.driver.mem_cpu1['퐀' + var1] & 31, false, false, var1 % 32 * 8, var2 * 8, 1, 3);
            }
         }
      }

      this.charLayer.toBitMap(this.getBackBuffer(), 0, 0, 0, 0, 256, 224);
      return this.getBackBuffer();
   }

   public WriteHandler writeBackGround(char[] var1) {
      return new WriteBackGround(var1);
   }

   public WriteHandler writePaletteRAM(char[] var1) {
      return new WritePaletteRAM(var1);
   }

   public WriteHandler writePaletteRAM2(char[] var1) {
      return new WritePaletteRAM2(var1);
   }

   public WriteHandler writeScreenLayout() {
      return new WriteScreenLayout();
   }

   public WriteHandler writeScrollBank() {
      return new WriteScrollBank();
   }

   public WriteHandler writeScrollX() {
      return new WriteScrollX();
   }

   public WriteHandler writeScrollY() {
      return new WriteScrollY();
   }

   public WriteHandler writeVRAM(char[] var1) {
      return new WriteVRAM(var1);
   }

   public class ReadBackGround implements ReadHandler {
      char[] mem;

      public ReadBackGround(char[] var2) {
         this.mem = var2;
      }

      public int read(int var1) {
         int var2 = VBlktiger.this.scrollBank;
         return VBlktiger.this.scrollRAM[(var1 & 4095) + var2];
      }
   }

   public class WriteBackGround implements WriteHandler {
      char[] mem;

      public WriteBackGround(char[] var2) {
         this.mem = var2;
      }

      public void write(int var1, int var2) {
         var1 = (var1 & 4095) + VBlktiger.this.scrollBank;
         VBlktiger.this.scrollRAM[var1] = (char)var2;
         VBlktiger.this.dirty2[var1] = true;
      }
   }

   public class WritePaletteRAM implements WriteHandler {
      char[] mem;

      public WritePaletteRAM(char[] var2) {
         this.mem = var2;
      }

      public void write(int var1, int var2) {
         if(this.mem[var1] != var2) {
            this.mem[var1] = (char)var2;
            var2 = var1 - '\ud800';
            var1 = this.mem[var1] << 8 | this.mem[var1 + 1024];
            if(!JEmu2.IS_APPLET) {
               var1 = (var1 & 15) << 20 | (var1 & 3840) << 4 | (var1 & '\uf000') >> 8;
            } else {
               var1 = (var1 & 15) << 4 | (var1 & 3840) << 4 | (var1 & '\uf000') << 8;
            }

            var1 |= var1 >> 4;
            VBlktiger.this.getGfxManager(0).changePalette(var2, var1);
            VBlktiger.this.getGfxManager(1).changePalette(var2, var1);
            VBlktiger.this.getGfxManager(2).changePalette(var2, var1);
         }

      }
   }

   public class WritePaletteRAM2 implements WriteHandler {
      char[] mem;

      public WritePaletteRAM2(char[] var2) {
         this.mem = var2;
      }

      public void write(int var1, int var2) {
         if(this.mem[var1] != var2) {
            this.mem[var1] = (char)var2;
            var2 = var1 - '\udc00';
            var1 = this.mem[var1 - 1024] << 8 | this.mem[var1];
            if(!JEmu2.IS_APPLET) {
               var1 = (var1 & 15) << 20 | (var1 & 3840) << 4 | (var1 & '\uf000') >> 8;
            } else {
               var1 = (var1 & 15) << 4 | (var1 & 3840) << 4 | (var1 & '\uf000') << 8;
            }

            var1 |= var1 >> 4;
            VBlktiger.this.getGfxManager(0).changePalette(var2, var1);
            VBlktiger.this.getGfxManager(1).changePalette(var2, var1);
            VBlktiger.this.getGfxManager(2).changePalette(var2, var1);
         }

      }
   }

   public class WriteScreenLayout implements WriteHandler {
      public void write(int var1, int var2) {
         VBlktiger.this.screenLayout = var2;
      }
   }

   public class WriteScrollBank implements WriteHandler {
      public void write(int var1, int var2) {
         VBlktiger.this.scrollBank = (var2 & 3) * 4096;
      }
   }

   public class WriteScrollX implements WriteHandler {
      public void write(int var1, int var2) {
         VBlktiger.this.scrollX[var1 & 1] = (char)var2;
      }
   }

   public class WriteScrollY implements WriteHandler {
      public void write(int var1, int var2) {
         VBlktiger.this.scrollY[var1 & 1] = (char)var2;
      }
   }

   public class WriteVRAM implements WriteHandler {
      char[] mem;

      public WriteVRAM(char[] var2) {
         this.mem = var2;
      }

      public void write(int var1, int var2) {
         this.mem[var1] = (char)var2;
         VBlktiger.this.dirty1[var1 & 1023] = true;
      }
   }
}
