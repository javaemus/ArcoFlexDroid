package net.movegaga.jemu2.driver.arcade.bublbobl;

import net.movegaga.jemu2.driver.BaseVideo;

import jef.machine.EmulatorProperties;
import jef.map.WriteHandler;
import jef.video.BitMap;
import jef.video.VideoRenderer;

public class VBublbobl extends BaseVideo implements VideoRenderer {
   static final int OBJECT_RAM_LOC = 56576;
   static final int OBJECT_RAM_SIZE = 768;
   private BitMap bitmap;
   int bublbobl_video_enable;
   private Bublbobl driver;

   public VBublbobl(Bublbobl var1) {
      this.driver = var1;
   }

   public void init(EmulatorProperties var1) {
      super.init(var1);
      this.bitmap = this.getBackBuffer();
   }

   public BitMap renderVideo() {
      int[] var15 = this.bitmap.getPixels();

      int var1;
      for(var1 = 0; var1 < var15.length; ++var1) {
         var15[var1] = this.getPaletteColor(255);
      }

      BitMap var19;
      if(this.bublbobl_video_enable == 0) {
         var19 = this.bitmap;
      } else {
         var1 = 0;

         for(int var2 = 0; var2 < 768; var2 += 4) {
            if(this.driver.mem_cpu1['\udd00' + var2] != 0 || this.driver.mem_cpu1['\udd00' + var2 + 1] != 0 || this.driver.mem_cpu1['\udd00' + var2 + 2] != 0 || this.driver.mem_cpu1['\udd00' + var2 + 3] != 0) {
               char var5 = this.driver.mem_cpu1['\udd00' + var2 + 1];
               char var7 = this.driver.mem_cpu1['\udd00' + var2 + 3];
               int var8 = ((var5 & 224) >> 1) + 128;
               int var4 = (var5 & 31) * 128;
               int var3 = var4;
               if((var5 & 160) == 160) {
                  var3 = var4 | 4096;
               }

               int var9 = -this.driver.mem_cpu1['\udd00' + var2 + 0];

               int var6;
               for(var4 = 0; var4 < 32; var1 = var6) {
                  if((this.driver.mem_prom[var4 / 2 + var8] & 8) != 0) {
                     var6 = var1;
                  } else {
                     if((this.driver.mem_prom[var4 / 2 + var8] & 4) == 0) {
                        var5 = this.driver.mem_cpu1['\udd00' + var2 + 2];
                        var1 = var5;
                        if((var7 & 64) != 0) {
                           var1 = var5 - 256;
                        }
                     }

                     int var16 = 0;

                     while(true) {
                        var6 = var1;
                        if(var16 >= 2) {
                           break;
                        }

                        int var12 = var16 * 64 + var3 + (var4 & 7) * 2 + (this.driver.mem_prom[var4 / 2 + var8] & 3) * 16;
                        int var11 = this.driver.mem_cpu1['쀀' + var12] + (this.driver.mem_cpu1['쀀' + var12 + 1] & 3) * 256 + (var7 & 15) * 1024;
                        char var17 = this.driver.mem_cpu1['쀀' + var12 + 1];
                        char var10 = this.driver.mem_cpu1['쀀' + var12 + 1];
                        char var18 = this.driver.mem_cpu1['쀀' + var12 + 1];
                        if(var11 != 0) {
                           boolean var13;
                           if((var10 & 64) != 0) {
                              var13 = true;
                           } else {
                              var13 = false;
                           }

                           boolean var14;
                           if((var18 & 128) != 0) {
                              var14 = true;
                           } else {
                              var14 = false;
                           }

                           this.drawVisible(0, var11, (var17 & 60) >> 2, var13, var14, var1 + var16 * 8, var4 * 8 + var9 & 255, 1, 15);
                        }

                        ++var16;
                     }
                  }

                  ++var4;
               }

               var1 += 16;
            }
         }

         var19 = this.bitmap;
      }

      return var19;
   }

   public WriteHandler writePaletteRAM() {
      return new WritePaletteRAM();
   }

   public class WritePaletteRAM implements WriteHandler {
      public void write(int var1, int var2) {
         VBublbobl.this.driver.mem_cpu1[var1] = (char)var2;
         VBublbobl.this.changecolor_RRRRGGGGBBBBxxxx(var1 - '\uf800' >> 1, VBublbobl.this.driver.mem_cpu1[var1 | 1] | VBublbobl.this.driver.mem_cpu1[var1 & -2] << 8);
      }
   }
}
