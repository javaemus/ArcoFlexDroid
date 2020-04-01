package net.movegaga.jemu2.driver.arcade.hexa;

import net.movegaga.jemu2.driver.BaseVideo;

import jef.machine.EmulatorProperties;
import jef.map.WriteHandler;
import jef.video.BitMap;
import jef.video.BitMapImpl;
import jef.video.VideoEmulator;

public class VHexa extends BaseVideo implements VideoEmulator, WriteHandler {
   static int charbank;
   static int flipx;
   static int flipy;
   static final int videoram = 57344;
   private char[] PROM;
   char[] RAM;
   BitMap bitmap = new BitMapImpl(256, 224);
   boolean[] dirtybuffer = new boolean[2048];

   public VHexa(Hexa var1) {
      this.RAM = var1.mem_cpu1;
      this.PROM = var1.mem_prom;
   }

   public WriteHandler hexa_d008_w() {
      return new Hexa_d008_w();
   }

   public void init(EmulatorProperties var1) {
      super.init(var1);
      this.initPalette();
   }

   public void initPalette() {
      for(int var1 = 0; var1 < 256; ++var1) {
         this.setPaletteColor(var1, (this.PROM[var1] >> 0 & 1) * 14 + (this.PROM[var1] >> 1 & 1) * 31 + (this.PROM[var1] >> 2 & 1) * 67 + (this.PROM[var1] >> 3 & 1) * 143, (this.PROM[var1 + 256] >> 0 & 1) * 14 + (this.PROM[var1 + 256] >> 1 & 1) * 31 + (this.PROM[var1 + 256] >> 2 & 1) * 67 + (this.PROM[var1 + 256] >> 3 & 1) * 143, (this.PROM[var1 + 512] >> 0 & 1) * 14 + (this.PROM[var1 + 512] >> 1 & 1) * 31 + (this.PROM[var1 + 512] >> 2 & 1) * 67 + (this.PROM[var1 + 512] >> 3 & 1) * 143);
      }

   }

   public BitMap renderVideo() {
      for(int var1 = 2046; var1 >= 0; var1 -= 2) {
         if(this.dirtybuffer[var1] || this.dirtybuffer[var1 + 1]) {
            this.dirtybuffer[var1] = false;
            this.dirtybuffer[var1 + 1] = false;
            int var3 = var1 / 2 % 32;
            int var4 = var1 / 2 / 32;
            int var2 = var3;
            if(flipx != 0) {
               var2 = 31 - var3;
            }

            var3 = var4;
            if(flipy != 0) {
               var3 = 31 - var4;
            }

            char var7 = this.RAM['\ue000' + var1 + 1];
            char var8 = this.RAM['\ue000' + var1];
            int var6 = charbank;
            char var5 = this.RAM['\ue000' + var1];
            this.drawVisible(this.bitmap, 0, var7 + ((var8 & 7) << 8) + (var6 << 11), (var5 & 248) >> 3, flipx, flipy, var2 * 8, var3 * 8, -1, 0);
         }
      }

      return this.bitmap;
   }

   public void write(int var1, int var2) {
      this.RAM[var1] = (char)var2;
      this.dirtybuffer[var1 & 2047] = true;
   }

   public class Hexa_d008_w implements WriteHandler {
      public void write(int var1, int var2) {
         if(VHexa.flipx != (var2 & 1)) {
            VHexa.flipx = var2 & 1;

            for(var1 = 0; var1 < 2048; ++var1) {
               VHexa.this.dirtybuffer[var1] = true;
            }
         }

         if(VHexa.flipy != (var2 & 2)) {
            VHexa.flipy = var2 & 2;

            for(var1 = 0; var1 < 2048; ++var1) {
               VHexa.this.dirtybuffer[var1] = true;
            }
         }

         if(VHexa.charbank != (var2 & 32) >> 5) {
            VHexa.charbank = (var2 & 32) >> 5;

            for(var1 = 0; var1 < 2048; ++var1) {
               VHexa.this.dirtybuffer[var1] = true;
            }
         }

      }
   }
}
