package net.movegaga.jemu2.driver.msx.v9938;

public class SpriteMode1 implements ScreenMode {
   static final int[] BLANK = new int['\ue140'];
   static final int[] BUFFER = new int['\ue140'];
   static final int[] LINEBUFFER = new int[288];

   public void frame(V9938 var1) {
      int var2 = 0;
      if(var1.irqLine >= 0) {
         var2 = var1.irqLine;
      }

      System.arraycopy(BLANK, 0, BUFFER, 0, BUFFER.length);
      this.render(var1);

      for(int var3 = 0; var3 < 212; ++var3) {
         if(var3 >= var2) {
            for(int var4 = 0; var4 < 256; ++var4) {
               int var5 = BUFFER[var3 * 272 + 8 + var4];
               if(var5 > 0) {
                  var1.pixels[var3 * 272 + 8 + var4] = var1.RGB[var5 & 15];
               }
            }
         }
      }

   }

   public void render(V9938 var1) {
      int var2 = 0;

      int var3;
      for(var3 = var1.addrSpriteTable; var2 < 32 && var1.vram[var3] != 208; var3 += 4) {
         ++var2;
      }

      boolean var6;
      if((var1.regControl[1] & 2) == 1) {
         var6 = true;
      } else {
         var6 = false;
      }

      int var4;
      int var5;
      int var7;
      int var8;
      int var9;
      if((var1.regControl[1] & 2) == 2) {
         var7 = var3 - 4;

         for(var8 = var2; var8 != 0; var7 -= 4) {
            var3 = var1.vram[var7 + 0];
            var2 = var1.vram[var7 + 1] & 255;
            var5 = var1.vram[var7 + 2];
            var4 = var1.vram[var7 + 3] & 255;
            if((var4 & 128) == 128) {
               var2 -= 32;
            }

            var3 = (var3 & 255) + 1;
            int var11 = var4 & 15;
            if(var2 <= 240 && var2 >= 0 && (var4 & 15) != 0) {
               var4 = var2 + 2728;
               var5 = var1.addrSpritePatterns + ((var5 & 255) << 3);
               if(var3 < 192) {
                  var9 = var4 + var3 * 272;
                  if(var3 > 176) {
                     var2 = 192 - var3;
                     var4 = var5;
                     var3 = var9;
                  } else {
                     var2 = 16;
                     var3 = var9;
                     var4 = var5;
                  }
               } else {
                  var5 += 256 - var3;
                  if(var3 > 240) {
                     var2 = 240;
                  } else {
                     var2 = var3;
                  }

                  var2 = var3 - var2;
                  var3 = var4;
                  var4 = var5;
               }

               var5 = var3;
               var9 = var4;
               int var10 = var2;
               if(var6) {
                  while(var2 != 0) {
                     var9 = 0;
                     var5 = var1.vram[var4];

                     for(var10 = var1.vram[var4 + 16]; var9 < 8; var3 += 2) {
                        int var12 = var1.RGB[var11];
                        if((var5 & 128) == 128) {
                           var1.pixels[var3] = var12;
                           var1.pixels[var3 + 1] = var12;
                           var1.pixels[var3 + 272] = var12;
                           var1.pixels[var3 + 272 + 1] = var12;
                        }

                        if((var10 & 128) == 128) {
                           var1.pixels[var3 + 8] = var12;
                           var1.pixels[var3 + 9] = var12;
                           var1.pixels[var3 + 272 + 8] = var12;
                           var1.pixels[var3 + 272 + 9] = var12;
                        }

                        ++var9;
                        var5 <<= 1;
                        var10 <<= 1;
                     }

                     --var2;
                     ++var4;
                     var3 += 528;
                  }
               } else {
                  while(var10 != 0) {
                     var2 = 0;
                     var4 = var1.vram[var9];

                     for(var3 = var1.vram[var9 + 16]; var2 < 8; ++var5) {
                        if((var4 & 128) == 128) {
                           var1.pixels[var5] = var1.RGB[var11];
                        }

                        if((var3 & 128) == 128) {
                           var1.pixels[var5 + 8] = var1.RGB[var11];
                        }

                        ++var2;
                        var4 <<= 1;
                        var3 <<= 1;
                     }

                     --var10;
                     ++var9;
                     var5 += 264;
                  }
               }
            }

            --var8;
         }
      } else {
         var5 = var3 - 4;

         for(int var13 = var2; var13 != 0; var5 -= 4) {
            if((var1.vram[var5 + 3] & 128) == 128) {
               var2 = var1.vram[var5 + 1] - 32;
            } else {
               var2 = var1.vram[var5 + 1];
            }

            var9 = var1.vram[var5 + 3];
            if(var2 <= 248 && var2 >= 0 && (var1.vram[var5 + 3] & 15) != 0) {
               var7 = var2 + 2728;
               var4 = var1.addrSpritePatterns + (var1.vram[var5 + 2] << 3);
               var3 = var1.vram[var5 + 0] + 1;
               if(var3 < 192) {
                  var7 += var3 * 272;
                  if(var3 > 184) {
                     var2 = 192 - var3;
                     var3 = var7;
                  } else {
                     var2 = 8;
                     var3 = var7;
                  }
               } else {
                  var4 += 256 - var3;
                  if(var3 > 248) {
                     var2 = 248;
                  } else {
                     var2 = var3;
                  }

                  var2 = var3 - var2;
                  var3 = var7;
               }

               while(var2 != 0) {
                  var7 = 0;

                  for(var8 = var1.vram[var4]; var7 < 8; ++var3) {
                     if((var8 & 128) == 128) {
                        var1.pixels[var3] = var1.RGB[var9 & 15];
                     }

                     ++var7;
                     var8 <<= 1;
                  }

                  --var2;
                  ++var4;
                  var3 += 264;
               }
            }

            --var13;
         }
      }

   }

   public void renderLine(V9938 var1, int var2) {
      if((var1.regControl[8] & 2) == 0) {
         System.arraycopy(BLANK, 0, LINEBUFFER, 0, LINEBUFFER.length);
         int var5 = var1.addrSpriteTable;
         int var11 = var1.addrSpritePatterns;
         boolean var3;
         if((var1.regControl[1] & 2) == 0) {
            var3 = true;
         } else {
            var3 = false;
         }

         int var12 = var1.regControl[23];
         boolean var4;
         if((var1.regControl[1] & 2) == 1) {
            var4 = true;
         } else {
            var4 = false;
         }

         for(int var6 = 0; var6 < 32 && var1.vram[var5] != 208; var5 += 4) {
            int var8 = var1.vram[var5] + 1 - var12 & 255;
            int var7 = var8;
            if(var8 > 240) {
               var7 = var8 - 256;
            }

            if(var7 <= var2 && var7 + 15 >= var2) {
               int var10 = var1.vram[var5 + 1] + 16;
               int var14 = var1.vram[var5 + 2];
               var8 = var1.vram[var5 + 3] & 15;
               int var13 = var8 & 15;
               boolean var9;
               if((var8 & 128) != 0) {
                  var9 = true;
               } else {
                  var9 = false;
               }

               var8 = var10;
               if(var9) {
                  var8 = var10 - 32;
               }

               if(var8 >= 0 && var8 <= 272) {
                  var7 = (var14 << 3) + var11 + (var2 - var7);
                  int var17 = var1.vram[var7];
                  if(!var4) {
                     if(LINEBUFFER[var8 + 0] == 0 && (var17 & 128) != 0) {
                        LINEBUFFER[var8 + 0] = var13;
                     }

                     if(LINEBUFFER[var8 + 1] == 0 && (var17 & 64) != 0) {
                        LINEBUFFER[var8 + 1] = var13;
                     }

                     if(LINEBUFFER[var8 + 2] == 0 && (var17 & 32) != 0) {
                        LINEBUFFER[var8 + 2] = var13;
                     }

                     if(LINEBUFFER[var8 + 3] == 0 && (var17 & 16) != 0) {
                        LINEBUFFER[var8 + 3] = var13;
                     }

                     if(LINEBUFFER[var8 + 4] == 0 && (var17 & 8) != 0) {
                        LINEBUFFER[var8 + 4] = var13;
                     }

                     if(LINEBUFFER[var8 + 5] == 0 && (var17 & 4) != 0) {
                        LINEBUFFER[var8 + 5] = var13;
                     }

                     if(LINEBUFFER[var8 + 6] == 0 && (var17 & 2) != 0) {
                        LINEBUFFER[var8 + 6] = var13;
                     }

                     if(LINEBUFFER[var8 + 7] == 0 && (var17 & 1) != 0) {
                        LINEBUFFER[var8 + 7] = var13;
                     }

                     if(!var3) {
                        var8 += 8;
                        var7 = var1.vram[var7 + 16];
                        if(LINEBUFFER[var8 + 0] == 0 && (var7 & 128) != 0) {
                           LINEBUFFER[var8 + 0] = var13;
                        }

                        if(LINEBUFFER[var8 + 1] == 0 && (var7 & 64) != 0) {
                           LINEBUFFER[var8 + 1] = var13;
                        }

                        if(LINEBUFFER[var8 + 2] == 0 && (var7 & 32) != 0) {
                           LINEBUFFER[var8 + 2] = var13;
                        }

                        if(LINEBUFFER[var8 + 3] == 0 && (var7 & 16) != 0) {
                           LINEBUFFER[var8 + 3] = var13;
                        }

                        if(LINEBUFFER[var8 + 4] == 0 && (var7 & 8) != 0) {
                           LINEBUFFER[var8 + 4] = var13;
                        }

                        if(LINEBUFFER[var8 + 5] == 0 && (var7 & 4) != 0) {
                           LINEBUFFER[var8 + 5] = var13;
                        }

                        if(LINEBUFFER[var8 + 6] == 0 && (var7 & 2) != 0) {
                           LINEBUFFER[var8 + 6] = var13;
                        }

                        if(LINEBUFFER[var8 + 7] == 0 && (var7 & 1) != 0) {
                           LINEBUFFER[var8 + 7] = var13;
                        }
                     }
                  }
               }
            }

            ++var6;
         }

         for(int var15 = 0; var15 < 256; ++var15) {
            int var16 = LINEBUFFER[var15 + 16];
            if(var16 != 0) {
               var1.pixels[(var2 + 10) * 272 + var15 + 8] = var1.RGB[var16];
            }
         }
      }

   }
}
