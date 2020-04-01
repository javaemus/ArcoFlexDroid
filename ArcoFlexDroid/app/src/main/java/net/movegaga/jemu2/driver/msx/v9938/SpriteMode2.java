package net.movegaga.jemu2.driver.msx.v9938;

public class SpriteMode2 implements ScreenMode {
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
      int var5 = ((var1.regControl[5] & 248) << 7) + (var1.regControl[11] << 15);
      int var7 = 0;

      for(int var6 = var5 + 512; var7 < 32 && var1.vram[var6 + 4] != 216; var5 += 16) {
         int var3 = var1.vram[var6 + 0];
         int var12 = var1.vram[var6 + 1] & 255;
         int var2 = var1.vram[var6 + 2];
         int var4 = (var3 & 255) + 1 - var1.regControl[23] & 255;
         var3 = var12 + 8;
         int var8 = var1.addrSpritePatterns + ((var2 & 255) << 3);
         if(var4 < 212) {
            var3 += var4 * 272;
            if(var4 > 196) {
               var2 = 212 - var4;
               var4 = var8;
            } else {
               var2 = 16;
               var4 = var8;
            }
         } else {
            var8 += 256 - var4;
            if(var4 > 240) {
               var2 = 240;
            } else {
               var2 = var4;
            }

            var2 = var4 - var2;
            var4 = var8;
         }

         int var9 = var2;
         var8 = var2;

         int var13;
         for(var2 = var3; var8 != 0; var9 = var13) {
            int var10 = var12;
            if((var1.vram[var5 + 16 - var8] & 128) == 128) {
               var10 = var12 - 32;
            }

            int var11 = var1.vram[var5 + 16 - var9];
            boolean var17;
            if((var11 & 64) != 0) {
               var17 = true;
            } else {
               var17 = false;
            }

            int var14 = var11 & 15;
            var13 = var9 - 1;
            var9 = var2;
            if(var10 <= 240) {
               var9 = var2;
               if(var10 >= 0) {
                  var11 = 0;
                  var10 = var1.vram[var4];

                  for(var9 = var1.vram[var4 + 16]; var11 < 8; ++var2) {
                     if(!var17) {
                        if((var10 & 128) == 128 && BUFFER[var2] == 0) {
                           BUFFER[var2] = var14;
                        }

                        if((var9 & 128) == 128 && BUFFER[var2 + 8] == 0) {
                           BUFFER[var2 + 8] = var14;
                        }
                     } else {
                        int[] var16;
                        if((var10 & 128) == 128) {
                           var16 = BUFFER;
                           var16[var2] |= var14;
                        }

                        if((var9 & 128) == 128) {
                           var16 = BUFFER;
                           int var15 = var2 + 8;
                           var16[var15] |= var14;
                        }
                     }

                     ++var11;
                     var10 <<= 1;
                     var9 <<= 1;
                  }

                  var9 = var2;
               }
            }

            --var8;
            ++var4;
            var2 = var9 + 264;
         }

         ++var7;
         var6 += 4;
      }

   }

   public void renderLine(V9938 var1, int var2) {
      if((var1.regControl[8] & 2) == 0) {
         System.arraycopy(BLANK, 0, LINEBUFFER, 0, LINEBUFFER.length);
         int var5 = ((var1.regControl[5] & 248) << 7) + (var1.regControl[11] << 15);
         int var6 = var5 + 512;
         int var12 = var1.addrSpritePatterns;
         boolean var3;
         if((var1.regControl[1] & 2) == 0) {
            var3 = true;
         } else {
            var3 = false;
         }

         int var13 = var1.regControl[23];
         boolean var7 = false;

         int var4;
         boolean var19;
         for(var4 = 0; var4 < 32 && var1.vram[var6] != 216; var7 = var19) {
            int var8 = var1.vram[var6] + 1 - var13 & 255;
            int var9 = var8;
            if(var8 > 240) {
               var9 = var8 - 256;
            }

            var19 = var7;
            if(var9 <= var2) {
               if(var9 + 15 < var2) {
                  var19 = var7;
               } else {
                  int var11 = var1.vram[var6 + 1] + 16;
                  int var15 = var1.vram[var6 + 2];
                  int var16 = var2 - var9;
                  var8 = var1.vram[var5 + var16];
                  int var14 = var8 & 15;
                  boolean var20;
                  if((var8 & 64) != 0) {
                     var20 = true;
                  } else {
                     var20 = false;
                  }

                  boolean var10;
                  if((var8 & 128) != 0) {
                     var10 = true;
                  } else {
                     var10 = false;
                  }

                  var8 = var11;
                  if(var10) {
                     var8 = var11 - 32;
                  }

                  var11 = var8 - var1.adjustHOR;
                  var19 = var7;
                  if(var11 >= 0) {
                     var19 = var7;
                     if(var11 <= 272) {
                        int var21 = (var15 << 3) + var12 + var16;
                        var15 = var1.vram[var21];
                        if(var20) {
                           var19 = var7;
                           if(var7) {
                              int[] var17;
                              if((var15 & 128) != 0) {
                                 var17 = LINEBUFFER;
                                 var8 = var11 + 0;
                                 var17[var8] |= var14;
                              }

                              if((var15 & 64) != 0) {
                                 var17 = LINEBUFFER;
                                 var8 = var11 + 1;
                                 var17[var8] |= var14;
                              }

                              if((var15 & 32) != 0) {
                                 var17 = LINEBUFFER;
                                 var8 = var11 + 2;
                                 var17[var8] |= var14;
                              }

                              if((var15 & 16) != 0) {
                                 var17 = LINEBUFFER;
                                 var8 = var11 + 3;
                                 var17[var8] |= var14;
                              }

                              if((var15 & 8) != 0) {
                                 var17 = LINEBUFFER;
                                 var8 = var11 + 4;
                                 var17[var8] |= var14;
                              }

                              if((var15 & 4) != 0) {
                                 var17 = LINEBUFFER;
                                 var8 = var11 + 5;
                                 var17[var8] |= var14;
                              }

                              if((var15 & 2) != 0) {
                                 var17 = LINEBUFFER;
                                 var8 = var11 + 6;
                                 var17[var8] |= var14;
                              }

                              if((var15 & 1) != 0) {
                                 var17 = LINEBUFFER;
                                 var8 = var11 + 7;
                                 var17[var8] |= var14;
                              }

                              var19 = var7;
                              if(!var3) {
                                 var9 = var11 + 8;
                                 var21 = var1.vram[var21 + 16];
                                 if((var21 & 128) != 0) {
                                    var17 = LINEBUFFER;
                                    var8 = var9 + 0;
                                    var17[var8] |= var14;
                                 }

                                 if((var21 & 64) != 0) {
                                    var17 = LINEBUFFER;
                                    var8 = var9 + 1;
                                    var17[var8] |= var14;
                                 }

                                 if((var21 & 32) != 0) {
                                    var17 = LINEBUFFER;
                                    var8 = var9 + 2;
                                    var17[var8] |= var14;
                                 }

                                 if((var21 & 16) != 0) {
                                    var17 = LINEBUFFER;
                                    var8 = var9 + 3;
                                    var17[var8] |= var14;
                                 }

                                 if((var21 & 8) != 0) {
                                    var17 = LINEBUFFER;
                                    var8 = var9 + 4;
                                    var17[var8] |= var14;
                                 }

                                 if((var21 & 4) != 0) {
                                    var17 = LINEBUFFER;
                                    var8 = var9 + 5;
                                    var17[var8] |= var14;
                                 }

                                 if((var21 & 2) != 0) {
                                    var17 = LINEBUFFER;
                                    var8 = var9 + 6;
                                    var17[var8] |= var14;
                                 }

                                 var19 = var7;
                                 if((var21 & 1) != 0) {
                                    var17 = LINEBUFFER;
                                    var8 = var9 + 7;
                                    var17[var8] |= var14;
                                    var19 = var7;
                                 }
                              }
                           }
                        } else {
                           var7 = true;
                           if(LINEBUFFER[var11 + 0] == 0 && (var15 & 128) != 0) {
                              LINEBUFFER[var11 + 0] = var14;
                           }

                           if(LINEBUFFER[var11 + 1] == 0 && (var15 & 64) != 0) {
                              LINEBUFFER[var11 + 1] = var14;
                           }

                           if(LINEBUFFER[var11 + 2] == 0 && (var15 & 32) != 0) {
                              LINEBUFFER[var11 + 2] = var14;
                           }

                           if(LINEBUFFER[var11 + 3] == 0 && (var15 & 16) != 0) {
                              LINEBUFFER[var11 + 3] = var14;
                           }

                           if(LINEBUFFER[var11 + 4] == 0 && (var15 & 8) != 0) {
                              LINEBUFFER[var11 + 4] = var14;
                           }

                           if(LINEBUFFER[var11 + 5] == 0 && (var15 & 4) != 0) {
                              LINEBUFFER[var11 + 5] = var14;
                           }

                           if(LINEBUFFER[var11 + 6] == 0 && (var15 & 2) != 0) {
                              LINEBUFFER[var11 + 6] = var14;
                           }

                           if(LINEBUFFER[var11 + 7] == 0 && (var15 & 1) != 0) {
                              LINEBUFFER[var11 + 7] = var14;
                           }

                           var19 = var7;
                           if(!var3) {
                              var9 = var11 + 8;
                              var21 = var1.vram[var21 + 16];
                              if(LINEBUFFER[var9 + 0] == 0 && (var21 & 128) != 0) {
                                 LINEBUFFER[var9 + 0] = var14;
                              }

                              if(LINEBUFFER[var9 + 1] == 0 && (var21 & 64) != 0) {
                                 LINEBUFFER[var9 + 1] = var14;
                              }

                              if(LINEBUFFER[var9 + 2] == 0 && (var21 & 32) != 0) {
                                 LINEBUFFER[var9 + 2] = var14;
                              }

                              if(LINEBUFFER[var9 + 3] == 0 && (var21 & 16) != 0) {
                                 LINEBUFFER[var9 + 3] = var14;
                              }

                              if(LINEBUFFER[var9 + 4] == 0 && (var21 & 8) != 0) {
                                 LINEBUFFER[var9 + 4] = var14;
                              }

                              if(LINEBUFFER[var9 + 5] == 0 && (var21 & 4) != 0) {
                                 LINEBUFFER[var9 + 5] = var14;
                              }

                              if(LINEBUFFER[var9 + 6] == 0 && (var21 & 2) != 0) {
                                 LINEBUFFER[var9 + 6] = var14;
                              }

                              var19 = var7;
                              if(LINEBUFFER[var9 + 7] == 0) {
                                 var19 = var7;
                                 if((var21 & 1) != 0) {
                                    LINEBUFFER[var9 + 7] = var14;
                                    var19 = var7;
                                 }
                              }
                           }
                        }
                     }
                  }
               }
            }

            ++var4;
            var5 += 16;
            var6 += 4;
         }

         for(int var18 = 0; var18 < 256; ++var18) {
            var4 = LINEBUFFER[var18 + 16 - var1.adjustHOR];
            if(var4 != 0) {
               var1.pixels[var2 * 272 + var18 + 8 - var1.adjustHOR] = var1.RGB[var4];
            }
         }
      }

   }
}
