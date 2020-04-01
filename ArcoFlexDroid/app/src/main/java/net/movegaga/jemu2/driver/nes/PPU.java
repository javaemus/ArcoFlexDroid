package net.movegaga.jemu2.driver.nes;

import net.movegaga.jemu2.driver.nes.mappers.CartMapper;

import jef.cpu.m6502.N2A03;
import jef.map.ReadWriteHandler;
import jef.video.BitMap;
import jef.video.VideoRenderer;

public class PPU implements ReadWriteHandler, VideoRenderer {
   static final int ATTR_TBL_0 = 9152;
   static final int ATTR_TBL_1 = 10176;
   static final int ATTR_TBL_2 = 11200;
   static final int ATTR_TBL_3 = 12224;
   static final int IMG_PALETTE = 16128;
   static final int NAME_TBL_0 = 8192;
   static final int NAME_TBL_1 = 9216;
   static final int NAME_TBL_2 = 10240;
   static final int NAME_TBL_3 = 11264;
   static final int PALETTE_MIRROR = 16160;
   static final int PATTERN_TBL_0 = 0;
   static final int PATTERN_TBL_1 = 4096;
   static final int SPR_PALETTE = 16144;
   public static final boolean TRACE = true;
   public static final boolean TRACE_REG_R = false;
   public static final boolean TRACE_REG_W = true;
   static final int UNUSED = 12288;
   int addrNameTable;
   int addrSprPatternTable;
   int addrTilePatternTable;
   int bgColor;
   CartMapper cart;
   N2A03 cpu;
   boolean enableBW;
   boolean enableBg;
   boolean enableBgClipping;
   boolean enableSpriteClipping;
   boolean enableSprites;
   boolean nmiEnabled;
   int reg0;
   int reg1;
   int reg2;
   PPURenderer renderer;
   int rwAddress;
   boolean rwFlag;
   int rwSprAddress;
   int scrollH;
   int scrollV;
   int sprSize;
   char[] spram;
   char[] vram;

   public PPU(CartMapper var1, N2A03 var2) {
      this.cpu = var2;
      this.cart = var1;
      this.vram = new char[16384];
      this.spram = new char[256];
      this.rwAddress = 0;
      this.scrollH = 0;
      this.scrollV = 0;
      this.rwFlag = false;
      this.rwSprAddress = 0;
      this.addrNameTable = 8192;
      this.addrTilePatternTable = 0;
      this.addrSprPatternTable = 4096;
      this.renderer = new PPURenderer(this);
   }

   private int readMemory() {
      int var1 = this.readMem(this.rwAddress);
      System.out.println("PPU READ MEM $" + Integer.toHexString(this.rwAddress) + " = " + Integer.toHexString(var1));
      this.updateRwAddress();
      return var1;
   }

   private void updateRwAddress() {
      if((this.reg0 & 4) == 4) {
         this.rwAddress += 32;
      } else {
         ++this.rwAddress;
      }

      this.rwAddress &= '\uffff';
   }

   private void writeMemory(int var1) {
      System.out.println("PPU WRITE MEM $" + Integer.toHexString(this.rwAddress) + " <- " + Integer.toHexString(var1));
      this.vram[this.rwAddress & 16383] = (char)var1;
      this.updateRwAddress();
   }

   public void line(int var1) {
      if(var1 < 224) {
         this.renderer.renderLine(var1);
      } else if(var1 == 241) {
         this.reg2 |= 128;
      } else if(var1 == 243) {
         if(this.nmiEnabled) {
            this.cpu.interrupt(N2A03.TYPE_NMI, true);
         }
      } else if(var1 == 261) {
         this.reg2 &= 63;
         if(this.nmiEnabled) {
            this.cpu.interrupt(N2A03.TYPE_NMI, false);
         }
      }

   }

   public int read(int var1) {
      switch(var1) {
      case 0:
         var1 = this.reg0;
         break;
      case 1:
         var1 = this.reg1;
         break;
      case 2:
         var1 = this.reg2;
         this.rwFlag = false;
         this.reg2 &= -129;
         break;
      case 3:
      case 5:
      case 6:
      default:
         var1 = 255;
         break;
      case 4:
         var1 = this.spram[this.rwSprAddress];
         this.rwSprAddress = this.rwSprAddress + 1 & 255;
         break;
      case 7:
         var1 = this.readMemory();
      }

      return var1;
   }

   public int readMem(int var1) {
      var1 &= 16383;
      if(var1 < 8192) {
         var1 = this.cart.readVROM(var1);
      } else {
         var1 = this.vram[var1];
      }

      return var1;
   }

   public BitMap renderVideo() {
      return this.renderer.renderVideo();
   }

   public void renderVideoPost() {
   }

   public void write(int var1, int var2) {
      boolean var4;
      switch(var1) {
      case 0:
         this.reg0 = var2;
         this.addrNameTable = ((this.reg0 & 3) << 8) + 8192;
         this.addrSprPatternTable = (this.reg0 & 8) << 9;
         this.addrTilePatternTable = (this.reg0 & 16) << 8;
         this.sprSize = ((this.reg0 & 32) >> 2) + 8;
         if((this.reg0 & 128) == 128) {
            var4 = true;
         } else {
            var4 = false;
         }

         this.nmiEnabled = var4;
         System.out.println("PPU WR REG0: $" + Integer.toHexString(this.reg0) + ", ADDR.NAME TBL:$" + Integer.toHexString(this.addrNameTable) + ", ADDR.SPR.PTRN:$" + Integer.toHexString(this.addrSprPatternTable) + ", ADDR.TILE.PTRN:$" + Integer.toHexString(this.addrTilePatternTable) + ", SPR.SIZE:" + this.sprSize);
         break;
      case 1:
         this.reg1 = var2;
         if((var2 & 32) == 32) {
            var1 = 16711680;
         } else {
            var1 = 0;
         }

         this.bgColor = var1;
         int var3 = this.bgColor;
         char var5;
         if((var2 & 64) == 64) {
            var5 = '\uff00';
         } else {
            var5 = 0;
         }

         this.bgColor = var3 | var5;
         var3 = this.bgColor;
         short var6;
         if((var2 & 128) == 128) {
            var6 = 255;
         } else {
            var6 = 0;
         }

         this.bgColor = var3 | var6;
         if((var2 & 16) == 16) {
            var4 = true;
         } else {
            var4 = false;
         }

         this.enableSprites = var4;
         if((var2 & 8) == 8) {
            var4 = true;
         } else {
            var4 = false;
         }

         this.enableBg = var4;
         if((var2 & 4) == 4) {
            var4 = true;
         } else {
            var4 = false;
         }

         this.enableSpriteClipping = var4;
         if((var2 & 2) == 2) {
            var4 = true;
         } else {
            var4 = false;
         }

         this.enableBgClipping = var4;
         if((var2 & 1) == 1) {
            var4 = true;
         } else {
            var4 = false;
         }

         this.enableBW = var4;
         System.out.println("PPU WR REG1: $" + Integer.toHexString(this.reg1));
      case 2:
      default:
         break;
      case 3:
         this.rwSprAddress = var2;
         System.out.println("PPU WR REG1: SPRITE RW ADDRESS: $" + Integer.toHexString(this.rwSprAddress));
         break;
      case 4:
         this.spram[this.rwSprAddress] = (char)var2;
         this.rwSprAddress = this.rwSprAddress + 1 & 255;
         break;
      case 5:
         if(this.rwFlag) {
            this.scrollH = var2;
         } else {
            this.scrollV = var2;
         }

         if(this.rwFlag) {
            var4 = false;
         } else {
            var4 = true;
         }

         this.rwFlag = var4;
         break;
      case 6:
         if(this.rwFlag) {
            this.rwAddress = this.rwAddress & '\uff00' | var2;
         } else {
            this.rwAddress = this.rwAddress & 255 | (var2 & 255) << 8;
         }

         if(this.rwFlag) {
            var4 = false;
         } else {
            var4 = true;
         }

         this.rwFlag = var4;
         break;
      case 7:
         this.writeMemory(var2);
      }

   }
}
