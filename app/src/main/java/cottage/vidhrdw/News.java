package cottage.vidhrdw;

import cottage.mame.MAMEVideo;
import jef.machine.EmulatorProperties;
import jef.map.WriteHandler;
import jef.video.BitMap;
import jef.video.Get_tile_info;
import jef.video.PaletteInitializer;
import jef.video.TileMap;
import jef.video.VideoEmulator;
import jef.video.VideoFinalizer;
import jef.video.VideoInitializer;
import jef.video.VideoRenderer;

public class News extends MAMEVideo implements VideoEmulator, VideoRenderer, VideoInitializer, VideoFinalizer, PaletteInitializer {
   static TileMap bg_tilemap;
   static int bgpic;
   static TileMap fg_tilemap;
   static int news_bgram;
   static int news_fgram;
   public int[] Fnews_bgram = new int[1];
   public int[] Fnews_fgram = new int[1];

   public Get_tile_info get_bg_tile_info() {
      return new Get_bg_tile_info();
   }

   public Get_tile_info get_fg_tile_info() {
      return new Get_fg_tile_info();
   }

   public void init(EmulatorProperties var1) {
      super.init_bis(var1);
      super.init(var1);
      news_fgram = this.Fnews_fgram[0];
      news_bgram = this.Fnews_bgram[0];
   }

   public int initVideo() {
      fg_tilemap = this.tilemap_create(this.get_fg_tile_info(), 0, 1, 8, 8, 32, 32);
      this.tilemap_set_transparent_pen(fg_tilemap, 0);
      bg_tilemap = this.tilemap_create(this.get_bg_tile_info(), 0, 0, 8, 8, 32, 32);
      return 0;
   }

   public WriteHandler news_bgpic_w() {
      return new News_bgpic_w();
   }

   public WriteHandler news_bgram_w() {
      return new News_bgram_w();
   }

   public WriteHandler news_fgram_w() {
      return new News_fgram_w();
   }

   public BitMap renderVideo() {
      this.tilemap_draw(this.bitmap, this.cliprect, bg_tilemap, 0, 0);
      this.tilemap_draw(this.bitmap, this.cliprect, fg_tilemap, 0, 0);
      return this.bitmap;
   }

   public class Get_bg_tile_info implements Get_tile_info {
      public void get_tile_info(int var1) {
         int var3 = News.this.RAM[News.news_bgram + var1 * 2] << 8 | News.this.RAM[News.news_bgram + var1 * 2 + 1];
         int var2 = var3 & 4095;
         var1 = var2;
         if((var2 & 3584) == 3584) {
            var1 = var2 & 511 | News.bgpic << 9;
         }

         News.this.SET_TILE_INFO(0, var1, ('\uf000' & var3) >> 12, 0);
      }
   }

   public class Get_fg_tile_info implements Get_tile_info {
      public void get_tile_info(int var1) {
         var1 = News.this.RAM[News.news_fgram + var1 * 2] << 8 | News.this.RAM[News.news_fgram + var1 * 2 + 1];
         News.this.SET_TILE_INFO(0, var1 & 4095, ('\uf000' & var1) >> 12, 0);
      }
   }

   public class News_bgpic_w implements WriteHandler {
      public void write(int var1, int var2) {
         if(News.bgpic != var2) {
            News.bgpic = var2;
         }

      }
   }

   public class News_bgram_w implements WriteHandler {
      public void write(int var1, int var2) {
         News.this.RAM[var1] = (char)var2;
      }
   }

   public class News_fgram_w implements WriteHandler {
      public void write(int var1, int var2) {
         News.this.RAM[var1] = (char)var2;
      }
   }
}
