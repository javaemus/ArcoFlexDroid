package net.movegaga.jemu2.driver.arcade.bankp;

import net.movegaga.jemu2.driver.BaseVideo;

import jef.machine.EmulatorProperties;
import jef.map.WriteHandler;
import jef.video.BitMap;
import jef.video.BitMapImpl;
import jef.video.Blitter;
import jef.video.PaletteInitializer;
import jef.video.VideoEmulator;
import jef.video.VideoRenderer;

public class VBankp extends BaseVideo implements VideoEmulator, VideoRenderer, PaletteInitializer, WriteHandler {
   static final int COLORS_1 = 62464;
   static final int COLORS_2 = 64512;
   static final int VRAM_1 = 61440;
   static final int VRAM_2 = 63488;
   static final int VRAM_SIZE = 1024;
   private BitMap backBuffer;
   private BitMap bgLayer;
   private boolean[] dirtybuffer = new boolean[1024];
   private boolean[] dirtybuffer2 = new boolean[1024];
   private Bankp driver;
   int priority;
   private BitMap scrollBgLayer;
   int scroll_x;

   public VBankp(Bankp var1) {
      this.driver = var1;
   }

   public WriteHandler bankp_out_w() {
      return new Bankp_out_w();
   }

   public WriteHandler bankp_scroll_w() {
      return new Bankp_scroll_w();
   }

   public void init(EmulatorProperties var1) {
      super.init(var1);
      this.backBuffer = this.getBackBuffer();
      this.scrollBgLayer = new BitMapImpl(256, 256);
      this.bgLayer = new BitMapImpl(224, 224);
   }

   public void initPalette() {
      int var1 = 0;

      int var2;
      for(var2 = 0; var2 < this.getTotalColors(); ++var2) {
         this.setPaletteColor(var2, (this.driver.mem_prom[var1] >> 0 & 1) * 33 + (this.driver.mem_prom[var1] >> 1 & 1) * 71 + (this.driver.mem_prom[var1] >> 2 & 1) * 151, (this.driver.mem_prom[var1] >> 3 & 1) * 33 + (this.driver.mem_prom[var1] >> 4 & 1) * 71 + (this.driver.mem_prom[var1] >> 5 & 1) * 151, (this.driver.mem_prom[var1] >> 6 & 1) * 71 + 0 + (this.driver.mem_prom[var1] >> 7 & 1) * 151);
         ++var1;
      }

      for(var2 = 0; var2 < this.getTotalColors(0); ++var1) {
         this.setColor(0, var2, this.driver.mem_prom[var1] & 15);
         ++var2;
      }

      var1 += 128;

      for(var2 = 0; var2 < this.getTotalColors(1); ++var1) {
         this.setColor(1, var2, this.driver.mem_prom[var1] & 15);
         ++var2;
      }

   }

   public BitMap renderVideo() {
      for(int var1 = 1023; var1 >= 0; --var1) {
         char var2;
         char var4;
         char var5;
         boolean var7;
         BitMap var8;
         if(this.dirtybuffer[var1]) {
            this.dirtybuffer[var1] = false;
            int var6 = var1 / 32;
            var5 = this.driver.mem_cpu['\uf400' + var1];
            var8 = this.scrollBgLayer;
            char var3 = this.driver.mem_cpu['\uf000' + var1];
            var4 = this.driver.mem_cpu['\uf400' + var1];
            var2 = this.driver.mem_cpu['\uf400' + var1];
            if((var5 & 4) != 0) {
               var7 = true;
            } else {
               var7 = false;
            }

            this.draw(var8, 0, ((var4 & 3) >> 0) * 256 + var3, var2 >> 3, var7, false, var1 % 32 * 8, var6 * 8, -1, 0);
         }

         if(this.dirtybuffer2[var1]) {
            this.dirtybuffer2[var1] = false;
            int var9 = var1 / 32;
            var2 = this.driver.mem_cpu['ﰀ' + var1];
            var8 = this.bgLayer;
            var5 = this.driver.mem_cpu['\uf800' + var1];
            char var10 = this.driver.mem_cpu['ﰀ' + var1];
            var4 = this.driver.mem_cpu['ﰀ' + var1];
            if((var2 & 8) != 0) {
               var7 = true;
            } else {
               var7 = false;
            }

            this.drawVisible(var8, 1, (var10 & 7) * 256 + var5, var4 >> 4, var7, false, var1 % 32 * 8, var9 * 8, -1, 0);
         }
      }

      Blitter.copyEqualSizedBitMap(this.bgLayer, this.backBuffer);
      Blitter.copyWrappedBitMap(this.scrollBgLayer, this.backBuffer, this.scroll_x + 24, 16, 0);
      return this.backBuffer;
   }

   public void write(int var1, int var2) {
      if(this.driver.mem_cpu[var1] != var2) {
         this.dirtybuffer[var1 & 1023] = true;
         this.driver.mem_cpu[var1] = (char)var2;
      }

   }

   public WriteHandler writeVRAM2() {
      return new WriteVRAM2();
   }

   class Bankp_out_w implements WriteHandler {
      public void write(int var1, int var2) {
         VBankp.this.priority = var2 & 3;
         VBankp.this.driver.nmi_line_pulse.write(0, var2 & 16);
      }
   }

   class Bankp_scroll_w implements WriteHandler {
      public void write(int var1, int var2) {
         VBankp.this.scroll_x = var2;
      }
   }

   class WriteVRAM2 implements WriteHandler {
      public void write(int var1, int var2) {
         if(VBankp.this.driver.mem_cpu[var1] != var2) {
            VBankp.this.dirtybuffer2[var1 & 1023] = true;
            VBankp.this.driver.mem_cpu[var1] = (char)var2;
         }

      }
   }
}
