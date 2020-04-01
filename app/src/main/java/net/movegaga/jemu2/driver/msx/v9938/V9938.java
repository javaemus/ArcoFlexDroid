package net.movegaga.jemu2.driver.msx.v9938;

import net.movegaga.jemu2.JEmu2;

import jef.cpu.Cpu;
import jef.map.ReadHandler;
import jef.map.WriteHandler;
import jef.util.ByteArray;
import jef.util.Persistent;
import jef.util.StateUtils;
import jef.video.BitMap;
import jef.video.BitMapImpl;
import jef.video.VideoRenderer;

public class V9938 implements VideoRenderer, ReadHandler, WriteHandler, Persistent {
   private static final int[] DEFAULT_CONTROL;
   private static final int[] DEFAULT_STATUS;
   public static final int HEIGHT = 212;
   private static final int INT_IE0 = 1;
   private static final int INT_IE1 = 2;
   public static final int STATUS_REGS = 10;
   static final boolean TRACE = false;
   public static final int WIDTH = 272;
   private static final int[] bgr;
   private static final int[] rgb;
   private static final int[][] screenModeMasks;
   int IRQPending;
   int[] RGB;
   int addrChrPatterns;
   int addrChrTable;
   int addrColorTable;
   int addrSpriteColor;
   int addrSpritePatterns;
   int addrSpriteTable;
   public int adjustHOR;
   public int adjustVER;
   public final BitMap bitmap;
   int colBackGround;
   int colForeGround;
   public Commands command;
   private Cpu cpu;
   int cycles;
   public int displayStart;
   public int displayStartOffset;
   public int framesPerSec;
   boolean incPort3;
   private int ioState;
   public int irqLine = -1;
   LineCallBack lineCallBack;
   public int linesPerField;
   public int linesPerFrame;
   final Palette palette;
   public final int[] pixels = new int['\ue140'];
   public final int[] regControl;
   public final int[] regStatus;
   private int rwAddress;
   private int scrMode;
   final ScreenMode[] screenMode;
   boolean skipSprites;
   private int vBlankIRQ;
   final int[] vram;
   private int writeBuffer;

   static {
      int[] var0 = new int[64];
      var0[1] = 16;
      DEFAULT_CONTROL = var0;
      var0 = new int[16];
      var0[0] = 159;
      var0[2] = 204;
      var0[3] = 64;
      DEFAULT_STATUS = var0;
      var0 = new int[]{0, 0, 2146336, 6348896, 14688288, 14704704, 2105504, 14729280, 2105568, 6316256, 2146496, 8437952, 2129952, 10502336, 10526880, 14737632};
      rgb = var0;
      var0 = new int[]{0, 0, 2146336, 6348896, 2105568, 4219104, 10493984, 4243680, 14688288, 14704736, 12632096, 12632192, 2129952, 12599456, 10526880, 14737632};
      bgr = var0;
      int[] var1 = new int[]{127, 0, 63, 0};
      int[] var7 = new int[]{127, 255, 63, 255};
      int[] var5 = new int[]{127, 255, 0, 255};
      var0 = new int[]{127, 255, 0, 248};
      int[] var6 = new int[]{127, 255, 0, 248};
      int[] var2 = new int[]{127, 255, 0, 248};
      int[] var3 = new int[]{127, 255, 0, 248};
      int[] var4 = new int[]{127, 0, 63, 0};
      screenModeMasks = new int[][]{var1, var7, {127, 128, 60, 255}, var5, {127, 128, 60, 255}, var0, var6, var2, var3, var4};
   }

   public V9938(Cpu var1, int var2) {
      this.bitmap = new BitMapImpl(272, 212, this.pixels);
      this.RGB = new int[16];
      this.vram = new int[131072];
      this.regControl = new int[DEFAULT_CONTROL.length];
      this.regStatus = new int[DEFAULT_STATUS.length];
      this.addrChrPatterns = 0;
      this.addrChrTable = 0;
      this.addrColorTable = 0;
      this.addrSpritePatterns = 0;
      this.addrSpriteTable = 0;
      this.addrSpriteColor = 0;
      this.colForeGround = 0;
      this.colBackGround = 0;
      this.scrMode = 0;
      this.ioState = 0;
      this.writeBuffer = 0;
      this.rwAddress = 0;
      this.cycles = 0;
      this.screenMode = new ScreenMode[10];
      this.framesPerSec = 50;
      this.linesPerField = 212;
      this.linesPerFrame = 313;
      this.displayStart = 50;
      this.displayStartOffset = 0;
      this.adjustHOR = 0;
      this.adjustVER = 0;
      this.palette = new Palette(this);
      this.command = new Commands(this);
      this.cpu = var1;
      this.vBlankIRQ = var2;
      this.reset();
      this.screenMode[0] = new Screen0();
      this.screenMode[1] = new Screen1();
      this.screenMode[2] = new Screen2();
      this.screenMode[3] = new Screen3();
      this.screenMode[4] = new Screen4();
      this.screenMode[5] = new Screen5();
      this.screenMode[6] = new Screen6();
      this.screenMode[7] = new Screen7();
      this.screenMode[8] = new Screen8();
      this.screenMode[9] = new Screen80();
   }

   private void checkSpritesCollision() {
      this.regStatus[0] = this.regStatus[0] & 159 | 31;
      int var6 = 0;

      int var1;
      for(var1 = this.addrSpriteTable; var6 < 32 && this.vram[var1] != 208; var1 += 4) {
         ++var6;
      }

      int var2;
      int var3;
      int var4;
      int var5;
      int var7;
      int var8;
      int var9;
      int var10;
      int var11;
      int var12;
      int var13;
      int[] var14;
      if((this.regControl[1] & 2) == 2) {
         var8 = 0;

         for(var7 = this.addrSpriteTable; var8 < var6; var7 += 4) {
            if((this.vram[var7 + 3] & 15) == 15) {
               var9 = var8 + 1;

               for(var10 = var7 + 4; var9 < var6; var10 += 4) {
                  if((this.vram[var10 + 3] & 15) == 15) {
                     var3 = this.vram[var7 + 0] - this.vram[var10 + 0];
                     if(var3 < 16 || var3 > 240) {
                        var13 = this.vram[var7 + 1] - this.vram[var10 + 1];
                        if(var13 < 16 || var13 > 240) {
                           var1 = this.addrSpritePatterns + ((this.vram[var7 + 2] & 252) << 3);
                           var2 = this.addrSpritePatterns + ((this.vram[var10 + 2] & 252) << 3);
                           if(var3 < 16) {
                              var2 += var3;
                           } else {
                              var3 = 256 - var3;
                              var1 += var3;
                           }

                           var12 = var13;
                           var11 = var3;
                           var4 = var2;
                           var5 = var1;
                           if(var13 > 240) {
                              var12 = 256 - var13;
                              var5 = var2;
                              var4 = var1;
                              var11 = var3;
                           }

                           while(var11 < 16) {
                              var1 = (this.vram[var5] << 8) + this.vram[var5 + 16];
                              if((var1 >> var12 & (this.vram[var4] << 8) + this.vram[var4 + 16]) == var1 >> var12) {
                                 break;
                              }

                              ++var11;
                              ++var5;
                              ++var4;
                           }

                           if(var11 < 16) {
                              var14 = this.regStatus;
                              var14[0] |= 32;
                              return;
                           }
                        }
                     }
                  }

                  ++var9;
               }
            }

            ++var8;
         }
      } else {
         var8 = 0;

         for(var7 = this.addrSpriteTable; var8 < var6; var7 += 4) {
            if((this.vram[var7 + 3] & 15) == 15) {
               var10 = var8 + 1;

               for(var9 = var7 + 4; var10 < var6; var9 += 4) {
                  if((this.vram[var9 + 3] & 15) == 15) {
                     var3 = this.vram[var7 + 0] - this.vram[var9 + 0];
                     if(var3 < 8 || var3 > 248) {
                        var13 = this.vram[var7 + 1] - this.vram[var9 + 1];
                        if(var13 < 8 || var13 > 248) {
                           var1 = this.addrSpritePatterns + (this.vram[var7 + 2] << 3);
                           var2 = this.addrSpritePatterns + (this.vram[var9 + 2] << 3);
                           if(var3 < 8) {
                              var2 += var3;
                           } else {
                              var3 = 256 - var3;
                              var1 += var3;
                           }

                           var12 = var13;
                           var11 = var3;
                           var4 = var2;
                           var5 = var1;
                           if(var13 > 248) {
                              var12 = 256 - var13;
                              var5 = var2;
                              var4 = var1;
                              var11 = var3;
                           }

                           while(var11 < 8 && (this.vram[var4] & this.vram[var5] >> var12) == 0) {
                              ++var11;
                              ++var5;
                              ++var4;
                           }

                           if(var11 < 8) {
                              var14 = this.regStatus;
                              var14[0] |= 32;
                              return;
                           }
                        }
                     }
                  }

                  ++var10;
               }
            }

            ++var8;
         }
      }

   }

   private void port0(int var1) {
      if(this.ioState > 0) {
         int[] var4 = this.vram;
         int var2 = this.regControl[14];
         int var3 = this.rwAddress;
         this.rwAddress = var3 + 1;
         var4[var2 * 16384 + var3] = var1;
         this.rwAddress &= 16383;
         if(this.scrMode > 4 && this.rwAddress == 0) {
            this.regControl[14] = this.regControl[14] + 1 & 7;
         }
      }

   }

   private void port1(int var1) {
      if(this.ioState != 0) {
         this.writeBuffer = (char)var1;
         --this.ioState;
      } else {
         ++this.ioState;
         switch(var1 & 192) {
         case 0:
            this.rwAddress = (char)(this.rwAddress & '\uff00' | this.writeBuffer & 255);
            this.rwAddress = (char)(this.rwAddress & 255 | var1 << 8);
            break;
         case 64:
            this.rwAddress = (char)(this.rwAddress & '\uff00' | this.writeBuffer & 255);
            this.rwAddress = (char)(this.rwAddress & 255 | (var1 & 63) << 8);
            break;
         case 128:
            this.setControl(var1 & 63, this.writeBuffer);
         }
      }

   }

   private void port3(int var1) {
      int var2 = this.regControl[17] & 63;
      if(var2 != 17 && var2 < this.regControl.length) {
         this.setControl(var2, var1);
      }

      if((this.regControl[17] & 128) == 0) {
         this.setControl(17, this.regControl[17] & 128 | var2 + 1 & 63);
      }

   }

   private void setControl(int var1, int var2) {
      int var3 = var2;
      switch(var1 & 255) {
      case 0:
         if((this.regStatus[1] & 1) == 1 && (var2 & 16) == 0) {
            int[] var4 = this.regStatus;
            var4[1] &= 254;
            this.setIRQ(-3);
         }

         this.setScreenMode((var2 & 14) >> 1 | this.regControl[1] & 24);
         var3 = var2;
         break;
      case 1:
         this.setScreenMode((this.regControl[0] & 14) >> 1 | var2 & 24);
         var3 = var2;
         if((this.regStatus[0] & 128) != 0) {
            byte var5;
            if((var2 & 32) != 0) {
               var5 = 1;
            } else {
               var5 = -2;
            }

            this.setIRQ(var5);
            var3 = var2;
         }
         break;
      case 2:
         this.addrChrTable = (screenModeMasks[this.scrMode][0] & var2) << 10;
         var3 = var2;
         break;
      case 3:
         this.addrColorTable = (screenModeMasks[this.scrMode][1] & var2) << 6;
         var3 = var2;
         break;
      case 4:
         this.addrChrPatterns = (screenModeMasks[this.scrMode][2] & var2) << 11;
         var3 = var2;
         break;
      case 5:
         this.addrSpriteTable = (screenModeMasks[this.scrMode][3] & var2) << 7;
         var3 = var2;
         break;
      case 6:
         var3 = var2 & 63;
         this.addrSpritePatterns = var3 << 11;
         break;
      case 7:
         this.colForeGround = var2 >> 4 & 15;
         this.colBackGround = var2 & 15;
         var3 = var2;
      case 8:
      case 12:
      case 13:
      case 19:
      case 20:
      case 21:
      case 22:
      case 24:
      case 25:
      case 26:
      case 27:
      case 28:
      case 29:
      case 30:
      case 31:
      case 32:
      case 33:
      case 34:
      case 35:
      case 36:
      case 37:
      case 38:
      case 39:
      case 40:
      case 41:
      case 42:
      case 43:
      case 45:
         break;
      case 9:
         if((var2 & 2) == 2) {
            this.framesPerSec = 50;
         } else {
            this.framesPerSec = 60;
         }

         if((var2 & 128) == 128) {
            this.linesPerField = 212;
         } else {
            this.linesPerField = 192;
         }

         this.updateTiming();
         System.out.println("FPS:" + this.framesPerSec + " LPF:" + this.linesPerField);
         var3 = var2;
         break;
      case 10:
         var3 = var2 & 7;
         break;
      case 11:
         var3 = var2 & 3;
         break;
      case 14:
         var3 = var2 & 7;
         break;
      case 15:
         var3 = var2 & 15;
         break;
      case 16:
         var3 = var2 & 15;
         break;
      case 17:
         var3 = var2 & 191;
         break;
      case 18:
         if((var2 & 8) == 8) {
            this.adjustHOR = -(16 - (var2 & 15));
            var3 = var2;
         } else {
            this.adjustHOR = var2 & 7;
            var3 = var2;
         }
         break;
      case 23:
         var3 = this.regControl[0];
         var3 = var2;
         break;
      case 44:
         this.regStatus[7] = var2;
         this.cpuToVdp(var2);
         var3 = var2;
         break;
      case 46:
         this.command.exec(var2);
         var3 = var2;
         break;
      default:
         var3 = var2;
      }

      this.regControl[var1] = var3 & 255;
   }

   private void setScreenMode(int var1) {
      switch(var1) {
      case 0:
         var1 = 1;
         break;
      case 1:
         var1 = 2;
         break;
      case 2:
         var1 = 4;
         break;
      case 3:
         var1 = 5;
         break;
      case 4:
         var1 = 6;
         break;
      case 5:
         var1 = 7;
         break;
      case 6:
      case 9:
      case 10:
      case 11:
      case 12:
      case 13:
      case 14:
      case 15:
      case 17:
      default:
         var1 = this.scrMode;
         break;
      case 7:
         var1 = 8;
         break;
      case 8:
         var1 = 3;
         break;
      case 16:
         var1 = 0;
         break;
      case 18:
         var1 = 9;
      }

      if(var1 != this.scrMode) {
         this.addrChrTable = (this.regControl[2] & screenModeMasks[var1][0]) << 10;
         this.addrColorTable = (this.regControl[3] & screenModeMasks[var1][1]) << 6;
         this.addrChrPatterns = (this.regControl[4] & screenModeMasks[var1][2]) << 11;
         this.addrSpriteTable = (this.regControl[5] & screenModeMasks[var1][3]) << 7;
         this.addrSpritePatterns = this.regControl[6] << 11;
         this.scrMode = var1;
      }

   }

   public void addCycles(int var1) {
      this.cycles += var1;
   }

   void cpuToVdp(int var1) {
      if(this.isBusy()) {
         switch(this.regControl[46] >> 4) {
         case 11:
            this.command.nextLgPixel(var1);
            break;
         case 15:
            this.command.nextHsByte(var1);
         }
      }

   }

   public void exec() {
      if(this.cycles > 0) {
         this.cycles -= 13;
         if(this.cycles <= 0) {
            this.cycles = 0;
            this.setBusy(false);
         }
      }

   }

   int getScrMode() {
      return this.scrMode;
   }

   public ByteArray getState() {
      return StateUtils.getState(this);
   }

   boolean isBusy() {
      boolean var1;
      if((this.regStatus[2] & 1) != 0) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public int read(int var1) {
      byte var2 = 0;
      switch(var1 & 3) {
      case 0:
         int[] var3 = this.vram;
         var1 = this.regControl[14];
         int var4 = this.rwAddress;
         this.rwAddress = var4 + 1;
         var4 = var3[var1 * 16384 + var4];
         this.rwAddress &= 16383;
         var1 = var4;
         if(this.scrMode > 3) {
            var1 = var4;
            if(this.rwAddress == 0) {
               this.regControl[14] = this.regControl[14] + 1 & 7;
               var1 = var4;
            }
         }
         break;
      case 1:
         var1 = this.status();
         this.ioState = 1;
         break;
      default:
         var1 = var2;
      }

      return var1;
   }

   public void render(int var1) {
      this.exec();
      switch(this.scrMode) {
      case 4:
      case 5:
      case 8:
      case 9:
         this.screenMode[this.scrMode].renderLine(this, var1);
      case 6:
      case 7:
      default:
      }
   }

   public void renderSprites() {
      int var1 = 0;

      int var2;
      for(var2 = this.addrSpriteTable; var1 < 32 && this.vram[var2] != 208; var2 += 4) {
         ++var1;
      }

      boolean var5;
      if((this.regControl[1] & 1) == 1) {
         var5 = true;
      } else {
         var5 = false;
      }

      int var3;
      int var4;
      int var6;
      int var7;
      int var8;
      if((this.regControl[1] & 2) == 2) {
         var6 = var2 - 4;

         for(var7 = var1; var7 != 0; var6 -= 4) {
            var2 = this.vram[var6 + 0];
            var1 = this.vram[var6 + 1] & 255;
            var4 = this.vram[var6 + 2];
            var3 = this.vram[var6 + 3] & 255;
            if((var3 & 128) == 128) {
               var1 -= 32;
            }

            var2 = (var2 & 255) + 1;
            int var11 = var3 & 15;
            if(var1 <= 240 && var1 >= 0 && (var3 & 15) != 0) {
               int var10 = this.RGB[var11];
               var3 = var1 + 2728;
               var4 = this.addrSpritePatterns + ((var4 & 255) << 3);
               if(var2 < 192) {
                  var8 = var3 + var2 * 272;
                  if(var2 > 176) {
                     var1 = 192 - var2;
                     var3 = var4;
                     var2 = var8;
                  } else {
                     var1 = 16;
                     var2 = var8;
                     var3 = var4;
                  }
               } else {
                  var4 += 256 - var2;
                  if(var2 > 240) {
                     var1 = 240;
                  } else {
                     var1 = var2;
                  }

                  var1 = var2 - var1;
                  var2 = var3;
                  var3 = var4;
               }

               var4 = var2;
               var8 = var3;
               int var9 = var1;
               if(var5) {
                  while(var1 != 0) {
                     var4 = 0;
                     var9 = this.vram[var3];

                     for(var8 = this.vram[var3 + 16]; var4 < 8; var2 += 2) {
                        int[] var12;
                        int[] var13;
                        int[] var14;
                        if((var9 & 128) == 128) {
                           var14 = this.pixels;
                           var12 = this.pixels;
                           var13 = this.pixels;
                           this.pixels[var2 + 272 + 1] = var10;
                           var13[var2 + 272] = var10;
                           var12[var2 + 1] = var10;
                           var14[var2] = var10;
                        }

                        if((var8 & 128) == 128) {
                           var13 = this.pixels;
                           var14 = this.pixels;
                           var12 = this.pixels;
                           this.pixels[var2 + 272 + 17] = var10;
                           var12[var2 + 272 + 16] = var10;
                           var14[var2 + 17] = var10;
                           var13[var2 + 16] = var10;
                        }

                        ++var4;
                        var9 <<= 1;
                        var8 <<= 1;
                     }

                     --var1;
                     ++var3;
                     var2 += 528;
                  }
               } else {
                  while(var9 != 0) {
                     var2 = 0;
                     var3 = this.vram[var8];

                     for(var1 = this.vram[var8 + 16]; var2 < 8; ++var4) {
                        if((var3 & 128) == 128) {
                           this.pixels[var4] = this.RGB[var11];
                        }

                        if((var1 & 128) == 128) {
                           this.pixels[var4 + 8] = this.RGB[var11];
                        }

                        ++var2;
                        var3 <<= 1;
                        var1 <<= 1;
                     }

                     --var9;
                     ++var8;
                     var4 += 264;
                  }
               }
            }

            --var7;
         }
      } else {
         var4 = var2 - 4;

         for(int var15 = var1; var15 != 0; var4 -= 4) {
            if((this.vram[var4 + 3] & 128) == 128) {
               var1 = this.vram[var4 + 1] - 32;
            } else {
               var1 = this.vram[var4 + 1];
            }

            var8 = this.vram[var4 + 3];
            if(var1 <= 248 && var1 >= 0 && (this.vram[var4 + 3] & 15) != 0) {
               var2 = var1 + 2728;
               var3 = this.addrSpritePatterns + (this.vram[var4 + 2] << 3);
               var6 = this.vram[var4 + 0] + 1;
               if(var6 < 192) {
                  var2 += var6 * 272;
                  if(var6 > 184) {
                     var1 = 192 - var6;
                  } else {
                     var1 = 8;
                  }
               } else {
                  var3 += 256 - var6;
                  if(var6 > 248) {
                     var1 = 248;
                  } else {
                     var1 = var6;
                  }

                  var1 = var6 - var1;
               }

               while(var1 != 0) {
                  var7 = 0;

                  for(var6 = this.vram[var3]; var7 < 8; ++var2) {
                     if((var6 & 128) == 128) {
                        this.pixels[var2] = this.RGB[var8 & 15];
                     }

                     ++var7;
                     var6 <<= 1;
                  }

                  --var1;
                  ++var3;
                  var2 += 264;
               }
            }

            --var15;
         }
      }

   }

   public void renderSpritesPost(int[] var1) {
      int var2 = 0;

      int var3;
      for(var3 = this.addrSpriteTable; var2 < 32 && this.vram[var3] != 208; var3 += 4) {
         ++var2;
      }

      int var4;
      int var5;
      int var6;
      int var7;
      int var8;
      int var9;
      if((this.regControl[1] & 2) == 2) {
         var5 = var3 - 4;

         for(var6 = var2; var6 != 0; var5 -= 4) {
            var3 = this.vram[var5 + 0];
            var2 = this.vram[var5 + 1] & 255;
            var7 = this.vram[var5 + 2];
            var4 = this.vram[var5 + 3] & 255;
            if((var4 & 128) == 128) {
               var2 -= 32;
            }

            var3 = (var3 & 255) + 1;
            int var10 = this.RGB[var4 & 15];
            if(var2 <= 240 && var2 >= 0 && (var4 & 15) != 0) {
               var4 = var2 + 2728;
               var7 = this.addrSpritePatterns + ((var7 & 255) << 3);
               if(var3 < 192) {
                  var8 = var4 + var3 * 272;
                  if(var3 > 176) {
                     var2 = 192 - var3;
                     var4 = var7;
                     var3 = var8;
                  } else {
                     var2 = 16;
                     var3 = var8;
                     var4 = var7;
                  }
               } else {
                  var7 += 256 - var3;
                  if(var3 > 240) {
                     var2 = 240;
                  } else {
                     var2 = var3;
                  }

                  var2 = var3 - var2;
                  var3 = var4;
                  var4 = var7;
               }

               while(var2 != 0) {
                  var7 = 0;
                  var8 = this.vram[var4];

                  for(var9 = this.vram[var4 + 16]; var7 < 8; ++var3) {
                     if((var8 & 128) == 128) {
                        var1[var3] = var10;
                     }

                     if((var9 & 128) == 128) {
                        var1[var3 + 8] = var10;
                     }

                     ++var7;
                     var8 <<= 1;
                     var9 <<= 1;
                  }

                  --var2;
                  ++var4;
                  var3 += 264;
               }
            }

            --var6;
         }
      } else {
         var5 = var3 - 4;

         for(var6 = var2; var6 != 0; var5 -= 4) {
            if((this.vram[var5 + 3] & 128) == 128) {
               var2 = this.vram[var5 + 1] - 32;
            } else {
               var2 = this.vram[var5 + 1];
            }

            var9 = this.vram[var5 + 3];
            if(var2 <= 248 && var2 >= 0 && (this.vram[var5 + 3] & 15) != 0) {
               var3 = var2 + 2728;
               var4 = this.addrSpritePatterns + (this.vram[var5 + 2] << 3);
               var7 = this.vram[var5 + 0] + 1;
               if(var7 < 192) {
                  var3 += var7 * 272;
                  if(var7 > 184) {
                     var2 = 192 - var7;
                  } else {
                     var2 = 8;
                  }
               } else {
                  var4 += 256 - var7;
                  if(var7 > 248) {
                     var2 = 248;
                  } else {
                     var2 = var7;
                  }

                  var2 = var7 - var2;
               }

               while(var2 != 0) {
                  var7 = 0;

                  for(var8 = this.vram[var4]; var7 < 8; ++var3) {
                     if((var8 & 128) == 128) {
                        var1[var3] = var9 & 15;
                     }

                     ++var7;
                     var8 <<= 1;
                  }

                  --var2;
                  ++var4;
                  var3 += 264;
               }
            }

            --var6;
         }
      }

   }

   public BitMap renderVideo() {
      switch(this.scrMode) {
      case 0:
      case 1:
      case 2:
      case 3:
      case 6:
      case 7:
         this.screenMode[this.scrMode].frame(this);
      case 4:
      case 5:
      default:
         if(this.lineCallBack != null) {
            this.renderSprites();
         }

         this.irqLine = -1;
         return this.bitmap;
      }
   }

   public void renderVideoPost() {
   }

   public boolean reset() {
      System.arraycopy(DEFAULT_CONTROL, 0, this.regControl, 0, DEFAULT_CONTROL.length);
      System.arraycopy(DEFAULT_STATUS, 0, this.regStatus, 0, DEFAULT_STATUS.length);
      if(JEmu2.IS_APPLET) {
         System.arraycopy(bgr, 0, this.RGB, 0, 16);
      } else {
         System.arraycopy(rgb, 0, this.RGB, 0, 16);
      }

      this.ioState = 1;
      this.rwAddress = 0;
      this.scrMode = 0;
      this.colBackGround = 0;
      this.colForeGround = 0;
      this.addrChrPatterns = 0;
      this.addrChrTable = 0;
      this.addrColorTable = 0;
      this.addrSpritePatterns = 0;
      this.addrSpriteTable = 0;
      this.addrSpriteColor = 0;

      for(int var1 = 0; var1 < this.vram.length; ++var1) {
         this.vram[var1] = 0;
      }

      return true;
   }

   public void restoreState(ByteArray var1) {
      StateUtils.restoreState(var1, this);
   }

   void setBusy(boolean var1) {
      int[] var2;
      if(var1) {
         var2 = this.regStatus;
         var2[2] |= 1;
      } else {
         var2 = this.regStatus;
         var2[2] &= -2;
      }

   }

   void setIRQ(int var1) {
      if((var1 & 128) != 0) {
         this.IRQPending &= var1;
      } else {
         this.IRQPending |= var1;
      }

      Cpu var3 = this.cpu;
      var1 = this.vBlankIRQ;
      boolean var2;
      if(this.IRQPending != 0) {
         var2 = true;
      } else {
         var2 = false;
      }

      var3.interrupt(var1, var2);
   }

   public void setLineCallBack(LineCallBack var1) {
      this.lineCallBack = var1;
   }

   void setScrMode(int var1) {
      this.scrMode = var1;
   }

   public void skipSprites(boolean var1) {
      this.skipSprites = var1;
   }

   public int status() {
      int var1 = this.regStatus[this.regControl[15]];
      int[] var2;
      switch(this.regControl[15]) {
      case 0:
         var2 = this.regStatus;
         var2[0] &= 95;
         this.setIRQ(-2);
         break;
      case 1:
         var2 = this.regStatus;
         var2[1] &= 254;
         this.setIRQ(-3);
         break;
      case 7:
         this.vdpToCpu();
         var1 = this.regStatus[7];
      }

      return var1;
   }

   public void updateLine(int var1) {
      var1 = var1 - this.displayStart + this.displayStartOffset;
      if(var1 >= 0 && var1 < 212) {
         this.render(var1);
      }

      int[] var2;
      if(var1 == 0) {
         var2 = this.regStatus;
         var2[2] &= -65;
         var2 = this.regStatus;
         var2[0] &= -65;
         var2 = this.regStatus;
         var2[0] &= -129;
         this.cpu.interrupt(this.vBlankIRQ, false);
      }

      var2 = this.regStatus;
      var2[2] ^= 32;
      this.cpu.exec(15);
      this.cpu.exec(199);
      this.cpu.exec(15);
      if((this.regControl[23] + var1 & 255) - this.regControl[19] == 0) {
         if((this.regControl[0] & 16) != 0) {
            var2 = this.regStatus;
            var2[1] |= 1;
            this.setIRQ(2);
         }
      } else if((this.regControl[0] & 16) == 0) {
         var2 = this.regStatus;
         var2[1] &= -2;
      }

      if(var1 == this.linesPerField) {
         var2 = this.regStatus;
         var2[1] &= 254;
         this.setIRQ(-3);
         var2 = this.regStatus;
         var2[2] |= 64;
         if(this.scrMode > 0) {
            this.checkSpritesCollision();
         }

         var2 = this.regStatus;
         var2[0] |= 128;
         if((this.regControl[1] & 32) == 32) {
            this.setIRQ(1);
         }
      }

   }

   public void updateTiming() {
      if(this.framesPerSec == 50) {
         this.linesPerFrame = 313;
         this.displayStart = 50;
      } else {
         this.linesPerFrame = 262;
         this.displayStart = 25;
      }

   }

   void vdpToCpu() {
      if(this.isBusy() && this.regControl[46] >> 4 == 10) {
         this.command.nextGetLgPixel();
      }

   }

   public void write(int var1, int var2) {
      switch(var1 & 3) {
      case 0:
         this.port0(var2);
         break;
      case 1:
         this.port1(var2);
         break;
      case 2:
         this.palette.port2(var2);
         break;
      case 3:
         this.port3(var2);
      }

   }
}
