package cottage.mame;

import net.movegaga.jemu2.JEmu2;

import jef.machine.EmulatorProperties;
import jef.map.ReadHandler;
import jef.map.WriteHandler;
import jef.video.BitMap;
import jef.video.BitMapImpl;
import jef.video.Eof_callback;
import jef.video.Get_tile_info;
import jef.video.GfxDecodeInfo;
import jef.video.GfxManager;
import jef.video.PaletteInitializer;
import jef.video.TileMap;
import jef.video.VideoEmulator;
import jef.video.VideoFinalizer;
import jef.video.VideoInitializer;
import jef.video.VideoRenderer;

public abstract class MAMEVideo implements VideoEmulator, PaletteInitializer, Eof_callback, VideoInitializer, VideoFinalizer, VideoRenderer {
   protected static final int PALETTE_COLOR_CACHED = 2;
   protected static final int PALETTE_COLOR_TRANSPARENT = 7;
   protected static final int PALETTE_COLOR_TRANSPARENT_FLAG = 4;
   protected static final int PALETTE_COLOR_UNUSED = 0;
   protected static final int PALETTE_COLOR_USED = 3;
   protected static final int PALETTE_COLOR_VISIBLE = 1;
   public static final int TILEMAP_OPAQUE = 0;
   public static final int TILEMAP_TRANSPARENT = 1;
   public static int colorram;
   static int curcode;
   static int curcolor;
   static int curflags;
   static int curgfx;
   public static int paletteram;
   public static int paletteram_2;
   public static int spriteram;
   public static int spriteram_2;
   public static int spriteram_2_size;
   public static int spriteram_3;
   public static int spriteram_3_size;
   public static int spriteram_size;
   public static final int tilemap_scan_rows = 0;
   public static int videoram;
   public static int videoram_size;
   protected char[][] GFX_REGIONS;
   public int Machine_drv_total_colors;
   public int[] Machine_gfx;
   public int[] Machine_pens;
   public int Machine_visible_area;
   public int Machine_visible_area_max_x;
   public int Machine_visible_area_max_y;
   public int Machine_visible_area_min_x;
   public int Machine_visible_area_min_y;
   protected char[] PROM;
   protected char[] RAM;
   protected BitMap backBuffer;
   protected BitMap bitmap;
   public int[] buffered_spriteram;
   protected int[] cliprect;
   protected int color;
   protected char[] colorPROM;
   private int[][] color_table;
   public boolean[] dirtybuffer;
   protected GfxDecodeInfo[] gdi;
   protected GfxManager[] gfxMan;
   protected int height;
   private EmulatorProperties md;
   private int[] palette;
   protected int[] palette_used_colors;
   protected int[] pixels;
   private int rot;
   protected BitMap tmpbitmap;
   protected int total_colors;
   private int vX;
   private int vY;
   private boolean videoModifiesPalette;
   protected int[] visible;
   protected int width;

   public PaletteInitializer BBBB_GGGG_RRRR() {
      return new BBBB_GGGG_RRRR_pi();
   }

   public PaletteInitializer RRRR_GGGG_BBBB() {
      return new RRRR_GGGG_BBBB_pi();
   }

   public void SET_TILE_INFO(int var1, int var2, int var3, int var4) {
      curgfx = var1;
      curcode = var2;
      curcolor = var3;
      curflags = var4;
   }

   public final boolean[] bauto_malloc(int var1) {
      return new boolean[var1];
   }

   public void buffer_spriteram_w(int var1, int var2) {
      var1 = spriteram;

      for(var2 = 0; var2 < spriteram_size; ++var1) {
         this.buffered_spriteram[var2] = this.RAM[var1];
         ++var2;
      }

   }

   public void changecolor_RRRRGGGGBBBBxxxx(int var1, int var2) {
      int var3 = var2 >> 12 & 15;
      int var4 = var2 >> 8 & 15;
      var2 = var2 >> 4 & 15;
      this.setPaletteColor(var1, var3 | var3 << 4, var4 | var4 << 4, var2 | var2 << 4);
   }

   public void changecolor_xBBBBBGGGGGRRRRR(int var1, int var2) {
      this.setPaletteColor(var1, (var2 >> 0 & 31) << 3, (var2 >> 5 & 31) << 3, (var2 >> 10 & 31) << 3);
   }

   public void changecolor_xxxxRRRRGGGGBBBB(int var1, int var2) {
      int var3 = var2 >> 8 & 15;
      int var4 = var2 >> 4 & 15;
      var2 = var2 >> 0 & 15;
      this.setPaletteColor(var1, var3 | var3 << 4, var4 | var4 << 4, var2 | var2 << 4);
   }

   protected void copyBitMap(BitMap var1, BitMap var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9) {
      var2.toPixels(this.pixels);
   }

   protected void copyScrollBitMap(BitMap var1, BitMap var2, int var3, int var4, int var5, int var6, int var7, int var8) {
      if(var3 == 1 && var5 == 1) {
         if((this.rot & 1) == 0) {
            var7 = var2.getWidth();
            var8 = var2.getHeight();
            var4 &= var7 - 1;
            var6 &= var8 - 1;
            var3 = var1.getWidth();
            var5 = var1.getHeight();
         } else {
            var8 = var2.getWidth();
            var7 = var2.getHeight();
            int var9 = var4 & var7 - 1;
            var4 = var6 & var8 - 1;
            var3 = var1.getWidth();
            var5 = var1.getHeight();
            var6 = var9;
         }

         if(var4 + var3 <= var7 && var6 + var5 <= var8) {
            var2.toBitMap(var1, 0, 0, var4, var6, var3, var5);
         } else if(var4 + var3 > var7 && var6 + var5 <= var8) {
            var2.toBitMap(var1, 0, 0, var4, var6, var7 - var4, var5);
            var2.toBitMap(var1, var7 - var4, 0, var4 + (var7 - var4), var6, var3 - (var7 - var4), var5);
         } else if(var4 + var3 <= var7 && var6 + var5 > var8) {
            var2.toBitMap(var1, 0, 0, var4, var6, var3, var5 - (var6 + var5 - var8));
            var2.toBitMap(var1, 0, var5 - (var6 + var5 - var8), var4, 0, var3, var6 + var5 - var8);
         } else {
            var2.toBitMap(var1, var7 - var4, var8 - var6, 0, 0, var4 + var3 - var7, var6 + var5 - var8);
            var2.toBitMap(var1, 0, var8 - var6, var4, 0, var7 - var4, var6 + var5 - var8);
            var2.toBitMap(var1, var7 - var4, 0, 0, var6, var4 + var3 - var7, var8 - var6);
            var2.toBitMap(var1, 0, 0, var4, var6, var7 - var4, var8 - var6);
         }
      }

   }

   protected final void copyScrollBitMap(BitMap var1, BitMap var2, int var3, int var4, int var5, int[] var6, int[] var7, int var8, int var9) {
      if(var3 == 0 && var4 == 0) {
         int var10 = var2.getWidth();
         var9 = var2.getHeight();
         int var11;
         if((this.rot & 1) == 1) {
            var1.getWidth();
            var3 = var7[0];
            var9 = var1.getHeight() / var5;

            for(var3 = 0; var3 < var5; ++var3) {
               var11 = var3 * var9;
               if(var11 >= 0 && var11 < var1.getHeight()) {
                  var8 = -var6[var3] + var7[2];
                  var4 = var8;
                  if(var8 < 0) {
                     var4 = var8 + var10;
                  }

                  if(var1.getWidth() + var4 <= var10) {
                     var2.toBitMap(var1, 0, var11, var4, var11, var1.getWidth(), var9);
                  } else {
                     var2.toBitMap(var1, 0, var11, var4, var11, var10 - var4, var9);
                     var2.toBitMap(var1, var10 - var4, var11, 0, var11, var1.getWidth() - (var10 - var4), var9);
                  }
               }
            }
         } else {
            var4 = var1.getWidth();
            var3 = var7[2];
            var11 = var1.getHeight();
            var10 = var7[0];
            int var12 = (var4 + (var3 << 1)) / var5;

            for(var3 = 0; var3 < var5; ++var3) {
               int var14 = var3 * var12;
               int var13 = var14 - (var7[2] << 1);
               if(var13 >= 0 && var13 < var1.getWidth()) {
                  var8 = -var6[var3 - 2] + var11 + (var10 << 1);
                  var4 = var8;
                  if(var8 < 0) {
                     var4 = var8 + var9;
                  }

                  if(var1.getHeight() + var4 <= var9) {
                     var2.toBitMap(var1, var13, 0, var14, var4, var12, var1.getHeight());
                  } else {
                     var2.toBitMap(var1, var13, 0, var14, var4, var12, var9 - var4);
                     var2.toBitMap(var1, var13, var9 - var4, var14, 0, var12, var1.getHeight() - (var9 - var4));
                  }
               }
            }
         }
      }

   }

   protected final void copyScrollBitMap(BitMap var1, BitMap var2, int var3, int[] var4, int var5, int var6, int[] var7, int var8, int var9) {
      if(var5 == 0 && var6 == 0) {
         int var12 = var2.getWidth();
         int var10 = var2.getHeight();
         int var13;
         int var15;
         if((this.rot & 1) == 0) {
            if(var8 == 2) {
               var6 = var9;
            } else {
               var6 = -1;
            }

            var1.getWidth();
            var5 = var7[0];
            var1.getHeight();
            var13 = var10 / var3;
            byte var11 = 0;
            var10 = var7[2];
            var5 = 0;
            var8 = var11;
            var9 = var10;
            if(var7[2] >= var13) {
               var5 = var7[2] / var13;
               var9 = var10;
               var8 = var11;
            }

            while(var5 < var3) {
               var15 = -var4[var5] + var7[0];
               var10 = var15;
               if(var15 < 0) {
                  var10 = var15 + var12;
               }

               if(var1.getWidth() + var10 <= var12) {
                  var2.toBitMap(var1, 0, var8, var10, var9, var1.getWidth(), var13, var6);
               } else {
                  var2.toBitMap(var1, 0, var8, var10, var9, var12 - var10, var13, var6);
                  var2.toBitMap(var1, var12 - var10, var8, 0, var9, var1.getWidth() - (var12 - var10), var13, var6);
               }

               var9 += var13;
               var8 += var13;
               ++var5;
            }
         } else {
            var5 = var1.getWidth();
            var6 = var7[2];
            var9 = var1.getHeight();
            var15 = var7[0];
            var12 = (var5 + (var6 << 1)) / var3;

            for(var5 = 0; var5 < var3; ++var5) {
               var13 = var5 * var12;
               int var14 = var13 - (var7[2] << 1);
               if(var14 >= 0 && var14 < var1.getWidth()) {
                  var8 = -var4[var5 - 2] + var9 + (var15 << 1);
                  var6 = var8;
                  if(var8 < 0) {
                     var6 = var8 + var10;
                  }

                  if(var1.getHeight() + var6 <= var10) {
                     var2.toBitMap(var1, var14, 0, var13, var6, var12, var1.getHeight());
                  } else {
                     var2.toBitMap(var1, var14, 0, var13, var6, var12, var10 - var6);
                     var2.toBitMap(var1, var14, var10 - var6, var13, 0, var12, var1.getHeight() - (var10 - var6));
                  }
               }
            }
         }
      }

   }

   protected void draw(BitMap var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9, int var10) {
      boolean var11;
      boolean var12;
      GfxManager var13;
      switch(this.rot) {
      case 0:
         var13 = this.gfxMan[var2];
         if(var5 != 0) {
            var11 = true;
         } else {
            var11 = false;
         }

         if(var6 != 0) {
            var12 = true;
         } else {
            var12 = false;
         }

         var13.drawTile(var1, var3, var4, var11, var12, var7 + this.vX, var8 + this.vY, var9, var10);
         break;
      case 1:
         var13 = this.gfxMan[var2];
         if(var6 != 0) {
            var11 = true;
         } else {
            var11 = false;
         }

         if(var5 != 0) {
            var12 = true;
         } else {
            var12 = false;
         }

         var13.drawTile(var1, var3, var4, var11, var12, var7 + this.vX, var8 + this.vY, var9, var10);
         break;
      case 2:
         var13 = this.gfxMan[var2];
         if(var5 != 0) {
            var11 = true;
         } else {
            var11 = false;
         }

         if(var6 != 0) {
            var12 = true;
         } else {
            var12 = false;
         }

         var13.drawTile(var1, var3, var4, var11, var12, var7 + this.vX, var8 + this.vY, var9, var10);
         break;
      case 3:
         var13 = this.gfxMan[var2];
         if(var6 != 0) {
            var11 = true;
         } else {
            var11 = false;
         }

         if(var5 != 0) {
            var12 = true;
         } else {
            var12 = false;
         }

         var13.drawTile(var1, var3, var4, var11, var12, var7 + this.vX, var8 + this.vY, var9, var10);
      }

   }

   protected void draw(BitMap var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9, int var10, int var11) {
      int var13 = var7;
      int var12 = var8;
      if(var9 == 1) {
         var13 = var7 + this.vX;
         var12 = var8 + this.vY;
      }

      boolean var14;
      boolean var15;
      GfxManager var16;
      switch(this.rot) {
      case 0:
         var16 = this.gfxMan[var2];
         if(var5 != 0) {
            var14 = true;
         } else {
            var14 = false;
         }

         if(var6 != 0) {
            var15 = true;
         } else {
            var15 = false;
         }

         var16.drawTile(var1, var3, var4, var14, var15, var13, var12, var10, var11);
         break;
      case 1:
         var16 = this.gfxMan[var2];
         if(var6 != 0) {
            var14 = true;
         } else {
            var14 = false;
         }

         if(var5 != 0) {
            var15 = true;
         } else {
            var15 = false;
         }

         var16.drawTile(var1, var3, var4, var14, var15, var13, var12, var10, var11);
         break;
      case 2:
         var16 = this.gfxMan[var2];
         if(var5 != 0) {
            var14 = true;
         } else {
            var14 = false;
         }

         if(var6 != 0) {
            var15 = true;
         } else {
            var15 = false;
         }

         var16.drawTile(var1, var3, var4, var14, var15, var13, var12, var10, var11);
         break;
      case 3:
         var16 = this.gfxMan[var2];
         if(var6 != 0) {
            var14 = true;
         } else {
            var14 = false;
         }

         if(var5 != 0) {
            var15 = true;
         } else {
            var15 = false;
         }

         var16.drawTile(var1, var3, var4, var14, var15, var13, var12, var10, var11);
      }

   }

   protected void draw(BitMap var1, int var2, int var3, int var4, boolean var5, boolean var6, int var7, int var8, int var9, int var10) {
      switch(this.rot) {
      case 0:
         this.gfxMan[var2].drawTile(var1, var3, var4, var5, var6, var7 + this.vX, var8 + this.vY, var9, var10);
         break;
      case 1:
         this.gfxMan[var2].drawTile(var1, var3, var4, var6, var5, var7 + this.vX, var8 + this.vY, var9, var10);
         break;
      case 2:
         this.gfxMan[var2].drawTile(var1, var3, var4, var5, var6, var7 + this.vX, var8 + this.vY, var9, var10);
         break;
      case 3:
         this.gfxMan[var2].drawTile(var1, var3, var4, var6, var5, var7 + this.vX, var8 + this.vY, var9, var10);
      }

   }

   public void eof_callback() {
   }

   protected void fillBitMap(BitMap var1, int var2, int var3) {
      var3 = 0;

      while(true) {
         try {
            this.pixels[var3] = var2;
         } catch (Exception var4) {
            return;
         }

         ++var3;
      }
   }

   protected void fillBitMap(BitMap var1, int var2, int[] var3) {
      int var4 = 0;

      while(true) {
         try {
            this.pixels[var4] = var2;
         } catch (Exception var5) {
            return;
         }

         ++var4;
      }
   }

   public void finalizeVideo() {
   }

   protected int getColorGranularity(int var1) {
      return 1 << this.gdi[var1].gfx.planes;
   }

   protected EmulatorProperties getMachineDriver() {
      return this.md;
   }

   public int getPaletteColor(int var1) {
      return this.palette[var1];
   }

   protected int getRot() {
      return this.rot;
   }

   protected int getTotalColors(int var1) {
      return this.md.gfx[var1].numberOfColors << this.md.gfx[var1].gfx.planes;
   }

   public void  init(EmulatorProperties md) {
      this.md = md;
      this.width = md.w;
      this.height = md.h;
      this.visible = md.visible;
      this.cliprect = md.visible;
      this.gdi = md.gfx;
      this.total_colors = md.pal;
      this.palette = new int[total_colors];
      palette_used_colors = new int[(1+1+1+3+1) * md.pal];

      this.Machine_pens = palette;

      this.color = md.col;
      this.rot = md.ROT;
      this.pixels = new int[((visible[1]+1) - visible[0]) * ((visible[3]+1) - visible[2])];
      this.videoModifiesPalette = (md.videoFlags & GfxManager.VIDEO_MODIFIES_PALETTE) != 0;
      if ((md.videoFlags & VideoEmulator.VIDEO_BUFFERS_SPRITERAM) != 0)
         this.buffered_spriteram = new int[spriteram_size];

      this.vX =  - visible[0];
      this.vY =  - visible[2];

      switch (rot) {
         case GfxManager.ROT0:
            vX =  - (visible[0]);
            vY =  - (visible[2]);
         case GfxManager.ROT180:
            backBuffer = new BitMapImpl(((visible[1]+1) - visible[0]),((visible[3]+1) - visible[2]), pixels);
            break;
         case GfxManager.ROT90:
            vX =  - (visible[0]);
            vY =  - (visible[2]);
         case GfxManager.ROT270:
            backBuffer = new BitMapImpl(((visible[3]+1) - visible[2]),((visible[1]+1) - visible[0]), pixels);
            break;
      }

      bitmap = backBuffer;
      tmpbitmap = new BitMapImpl(backBuffer.getWidth(), backBuffer.getHeight());

      try {
         this.gfxMan = new GfxManager[gdi.length];
         this.color_table = new int[gdi.length][];
         for (int i = 0; i < gdi.length; i++) {
            color_table[i] = new int[gdi[i].numberOfColors << gdi[i].gfx.planes];
            // Set up a default lookup table
            for (int n = 0; n < color_table[i].length; n++) {
               color_table[i][n] = n + gdi[i].colorOffset;
            }
            gfxMan[i] = new GfxManager();
            gfxMan[i].init(gdi[i], palette, color_table[i], 0, rot, md.videoFlags);
         }
      } catch (Exception e) {}
   }

   public void initPalette() {
   }

   public int initVideo() {
      return 0;
   }

   public void init_bis(EmulatorProperties var1) {
      this.md = var1;
      this.RAM = var1.getMemRegions()[0];
      this.PROM = var1.getMemRegions()[16];
      this.GFX_REGIONS = new char[8][];
      this.GFX_REGIONS[0] = var1.getMemRegions()[8];
      this.GFX_REGIONS[1] = var1.getMemRegions()[9];
      this.GFX_REGIONS[2] = var1.getMemRegions()[10];
      this.GFX_REGIONS[3] = var1.getMemRegions()[11];
      this.GFX_REGIONS[4] = var1.getMemRegions()[12];
      this.GFX_REGIONS[5] = var1.getMemRegions()[13];
      this.GFX_REGIONS[6] = var1.getMemRegions()[14];
      this.GFX_REGIONS[7] = var1.getMemRegions()[15];
      this.colorPROM = var1.getMemRegions()[16];
      this.dirtybuffer = new boolean[videoram_size];

      int var2;
      for(var2 = 0; var2 < videoram_size; ++var2) {
         this.dirtybuffer[var2] = true;
      }

      this.Machine_drv_total_colors = var1.pal;
      this.Machine_gfx = new int[8];

      for(var2 = 0; var2 < 8; this.Machine_gfx[var2] = var2++) {
         ;
      }

      this.Machine_visible_area = 1;
      this.Machine_visible_area_min_x = var1.visible[0];
      this.Machine_visible_area_max_x = var1.visible[1];
      this.Machine_visible_area_min_y = var1.visible[2];
      this.Machine_visible_area_max_y = var1.visible[3];
   }

   protected int[] malloc(int var1) {
      return new int[var1];
   }

   protected void memcpy(int[] var1, int[] var2, int var3) {
      System.arraycopy(var2, 0, var1, 0, var3);
   }

   protected void memset(int[] var1, int var2, int var3) {
      for(int var4 = 0; var4 < var3; ++var4) {
         var1[var4] = var2;
      }

   }

   public final void memset(boolean[] var1, int var2, int var3) {
      boolean var4;
      if(var2 == 0) {
         var4 = false;
      } else {
         var4 = true;
      }

      for(var2 = 0; var2 < var3; ++var2) {
         var1[var2] = var4;
      }

   }

   public WriteHandler paletteram_RRRRGGGGBBBBxxxx_split1_w() {
      return new Paletteram_RRRRGGGGBBBBxxxx_split1_w();
   }

   public WriteHandler paletteram_RRRRGGGGBBBBxxxx_split2_w() {
      return new Paletteram_RRRRGGGGBBBBxxxx_split2_w();
   }

   public WriteHandler paletteram_RRRRGGGGBBBBxxxx_swap_w() {
      return new Paletteram_RRRRGGGGBBBBxxxx_swap_w();
   }

   public ReadHandler paletteram_r() {
      return new Paletteram_r();
   }

   public WriteHandler paletteram_xxxxRRRRGGGGBBBB_swap_w() {
      return new Paletteram_xxxxRRRRGGGGBBBB_swap_w();
   }

   protected void plotPixel(BitMap var1, int var2, int var3, int var4) {
      var1.setPixelFast(var2, var3, var4);
   }

   protected int readPixel(BitMap var1, int var2, int var3) {
      return var1.getPixel(var2, var3);
   }

   public void renderVideoPost() {
      if(this.videoModifiesPalette) {
         for(int var1 = 0; var1 < this.gfxMan.length; ++var1) {
            this.gfxMan[var1].refresh();
         }
      }

   }

   protected void setColor(int var1, int var2, int var3) {
      this.color_table[var1][var2] = var3;
   }

   public void setPaletteColor(int var1, int var2) {
      this.palette[var1] = var2;
      if(this.videoModifiesPalette) {
         for(int var3 = 0; var3 < this.gfxMan.length; ++var3) {
            this.gfxMan[var3].changePalette(var1, var2);
         }
      }

   }

   public void setPaletteColor(int var1, int var2, int var3, int var4) {
      if(!JEmu2.IS_APPLET) {
         var2 |= var4 << 16 | var3 << 8;
      } else {
         var2 = var2 << 16 | var3 << 8 | var4;
      }

      if(this.getPaletteColor(var1) != var2) {
         this.setPaletteColor(var1, var2);
      }

   }

   protected void setRot(int var1) {
      this.rot = var1;
   }

   public TileMap tilemap_create(Get_tile_info var1, int var2, int var3, int var4, int var5, int var6, int var7) {
      return new TileMap(var1, var2, var3, var4, var5, var6, var7);
   }

   public void tilemap_draw(BitMap var1, int[] var2, TileMap var3, int var4, int var5) {
      int var15 = var3.cols;
      int var17 = var3.width;
      int var16 = var3.height;
      boolean var24;
      if((var3.type & 1) != 0) {
         var24 = true;
      } else {
         var24 = false;
      }

      int var6 = 0;
      if(var24) {
         var6 = var3.pen;
      }

      int var8 = this.visible[2] / var16;
      int var19 = (this.visible[3] + 1) / var16;
      int var14 = this.visible[0] / var17;
      int var18 = (this.visible[1] + 1) / var17;

      for(int var7 = 0; var8 < var19; ++var8) {
         int var9 = 0;

         for(int var10 = var14; var10 < var18; ++var10) {
            var3.tile_info.get_tile_info(var15 * var8 + var10);
            char[] var23 = this.GFX_REGIONS[curgfx];
            var4 = curcode * (this.gdi[curgfx].gfx.bytes >> 3);
            int var20 = curcolor << this.gdi[curgfx].gfx.planes;
            int var12 = var7 + var9;
            int var11;
            int var13;
            char var21;
            if(var24) {
               for(var11 = 0; var11 < var16; ++var11) {
                  for(var13 = 0; var13 < var17; ++var4) {
                     var21 = var23[var4];
                     int var22 = var21 >> 4 & 15;
                     if(var22 != var6) {
                        this.pixels[var12 + var13] = this.palette[var20 + var22];
                     }

                     ++var13;
                     int var25 = var21 & 15;
                     if(var25 != var6) {
                        this.pixels[var12 + var13] = this.palette[var20 + var25];
                     }

                     ++var13;
                  }

                  var12 += this.width;
               }
            } else {
               for(var11 = 0; var11 < var16; ++var11) {
                  for(var13 = 0; var13 < var17; ++var4) {
                     var21 = var23[var4];
                     this.pixels[var12 + var13] = this.palette[(var21 >> 4 & 15) + var20];
                     ++var13;
                     this.pixels[var12 + var13] = this.palette[(var21 & 15) + var20];
                     ++var13;
                  }

                  var12 += this.width;
               }
            }

            var9 += var17;
         }

         var7 += this.width * var16;
      }

   }

   public void tilemap_mark_all_tiles_dirty(TileMap var1) {
   }

   public void tilemap_mark_tile_dirty(TileMap var1, int var2) {
   }

   public void tilemap_set_transparent_pen(TileMap var1, int var2) {
      var1.pen = var2;
   }

   protected int[] visible_area() {
      return this.visible;
   }

   public WriteHandler writeVRAM() {
      return new Videoram_w();
   }

   public WriteHandler writeVRAMColors() {
      return new Colorram_w();
   }

   public class BBBB_GGGG_RRRR_pi implements PaletteInitializer {
      public void initPalette() {
         for(int var1 = 0; var1 < MAMEVideo.this.Machine_drv_total_colors; ++var1) {
            char var3 = MAMEVideo.this.colorPROM[var1];
            char var11 = MAMEVideo.this.colorPROM[var1];
            char var4 = MAMEVideo.this.colorPROM[var1];
            char var7 = MAMEVideo.this.colorPROM[var1];
            char var5 = MAMEVideo.this.colorPROM[MAMEVideo.this.Machine_drv_total_colors + var1];
            char var2 = MAMEVideo.this.colorPROM[MAMEVideo.this.Machine_drv_total_colors + var1];
            char var9 = MAMEVideo.this.colorPROM[MAMEVideo.this.Machine_drv_total_colors + var1];
            char var8 = MAMEVideo.this.colorPROM[MAMEVideo.this.Machine_drv_total_colors + var1];
            char var6 = MAMEVideo.this.colorPROM[MAMEVideo.this.Machine_drv_total_colors * 2 + var1];
            char var12 = MAMEVideo.this.colorPROM[MAMEVideo.this.Machine_drv_total_colors * 2 + var1];
            char var10 = MAMEVideo.this.colorPROM[MAMEVideo.this.Machine_drv_total_colors * 2 + var1];
            char var13 = MAMEVideo.this.colorPROM[MAMEVideo.this.Machine_drv_total_colors * 2 + var1];
            MAMEVideo.this.setPaletteColor(var1, (var3 >> 0 & 1) * 14 + (var11 >> 1 & 1) * 31 + (var4 >> 2 & 1) * 67 + (var7 >> 3 & 1) * 143, (var5 >> 0 & 1) * 14 + (var2 >> 1 & 1) * 31 + (var9 >> 2 & 1) * 67 + (var8 >> 3 & 1) * 143, (var6 >> 0 & 1) * 14 + (var12 >> 1 & 1) * 31 + (var10 >> 2 & 1) * 67 + (var13 >> 3 & 1) * 143);
         }

      }
   }

   public class Colorram_w implements WriteHandler {
      public void write(int var1, int var2) {
         if(MAMEVideo.this.RAM[var1] != var2) {
            MAMEVideo.this.dirtybuffer[var1 - MAMEVideo.colorram] = true;
            MAMEVideo.this.RAM[var1] = (char)var2;
         }

      }
   }

   public class Paletteram_RRRRGGGGBBBBxxxx_split1_w implements WriteHandler {
      public void write(int var1, int var2) {
         MAMEVideo.this.RAM[var1] = (char)var2;
         MAMEVideo.this.changecolor_RRRRGGGGBBBBxxxx(var1 - MAMEVideo.paletteram, MAMEVideo.this.RAM[var1] | MAMEVideo.this.RAM[var1 - MAMEVideo.paletteram + MAMEVideo.paletteram_2] << 8);
      }
   }

   public class Paletteram_RRRRGGGGBBBBxxxx_split2_w implements WriteHandler {
      public void write(int var1, int var2) {
         MAMEVideo.this.RAM[var1] = (char)var2;
         MAMEVideo.this.changecolor_RRRRGGGGBBBBxxxx(var1 - MAMEVideo.paletteram_2, MAMEVideo.this.RAM[var1 - MAMEVideo.paletteram_2 + MAMEVideo.paletteram] | MAMEVideo.this.RAM[var1] << 8);
      }
   }

   public class Paletteram_RRRRGGGGBBBBxxxx_swap_w implements WriteHandler {
      public void write(int var1, int var2) {
         MAMEVideo.this.RAM[var1] = (char)var2;
         MAMEVideo.this.changecolor_RRRRGGGGBBBBxxxx(var1 - MAMEVideo.paletteram >> 1, MAMEVideo.this.RAM[var1 | 1] | MAMEVideo.this.RAM[var1 & -2] << 8);
      }
   }

   public class Paletteram_r implements ReadHandler {
      public int read(int var1) {
         return MAMEVideo.this.RAM[var1];
      }
   }

   public class Paletteram_xxxxRRRRGGGGBBBB_swap_w implements WriteHandler {
      public void write(int var1, int var2) {
         MAMEVideo.this.RAM[var1] = (char)var2;
         MAMEVideo.this.changecolor_xxxxRRRRGGGGBBBB(var1 - MAMEVideo.paletteram >> 1, MAMEVideo.this.RAM[var1 | 1] | MAMEVideo.this.RAM[var1 & -2] << 8);
      }
   }

   public class RRRR_GGGG_BBBB_pi implements PaletteInitializer {
      public void initPalette() {
         System.out.println("Machine_drv_total_colors " + MAMEVideo.this.Machine_drv_total_colors);

         for(int var1 = 0; var1 < MAMEVideo.this.Machine_drv_total_colors; ++var1) {
            char var4 = MAMEVideo.this.colorPROM[var1];
            char var11 = MAMEVideo.this.colorPROM[var1];
            char var12 = MAMEVideo.this.colorPROM[var1];
            char var10 = MAMEVideo.this.colorPROM[var1];
            char var5 = MAMEVideo.this.colorPROM[MAMEVideo.this.Machine_drv_total_colors + var1];
            char var9 = MAMEVideo.this.colorPROM[MAMEVideo.this.Machine_drv_total_colors + var1];
            char var7 = MAMEVideo.this.colorPROM[MAMEVideo.this.Machine_drv_total_colors + var1];
            char var6 = MAMEVideo.this.colorPROM[MAMEVideo.this.Machine_drv_total_colors + var1];
            char var3 = MAMEVideo.this.colorPROM[MAMEVideo.this.Machine_drv_total_colors * 2 + var1];
            char var13 = MAMEVideo.this.colorPROM[MAMEVideo.this.Machine_drv_total_colors * 2 + var1];
            char var2 = MAMEVideo.this.colorPROM[MAMEVideo.this.Machine_drv_total_colors * 2 + var1];
            char var8 = MAMEVideo.this.colorPROM[MAMEVideo.this.Machine_drv_total_colors * 2 + var1];
            MAMEVideo.this.setPaletteColor(var1, (var4 >> 0 & 1) * 14 + (var11 >> 1 & 1) * 31 + (var12 >> 2 & 1) * 67 + (var10 >> 3 & 1) * 143, (var5 >> 0 & 1) * 14 + (var9 >> 1 & 1) * 31 + (var7 >> 2 & 1) * 67 + (var6 >> 3 & 1) * 143, (var3 >> 0 & 1) * 14 + (var13 >> 1 & 1) * 31 + (var2 >> 2 & 1) * 67 + (var8 >> 3 & 1) * 143);
         }

      }
   }

   public class Videoram_w implements WriteHandler {
      public void write(int var1, int var2) {
         if(MAMEVideo.this.RAM[var1] != var2) {
            MAMEVideo.this.dirtybuffer[var1 - MAMEVideo.videoram] = true;
            MAMEVideo.this.RAM[var1] = (char)var2;
         }

      }
   }
}
