package cottage.drivers;

import java.net.URL;

import cottage.mame.Driver;
import cottage.mame.MAMEConstants;
import cottage.mame.MAMEDriver;
import jef.machine.BasicEmulator;
import jef.machine.Emulator;
import jef.map.InterruptHandler;
import jef.map.WriteHandler;
import jef.video.VideoInitializer;
import jef.video.VideoRenderer;

public class News extends MAMEDriver implements Driver, MAMEConstants {
   InterruptHandler irq0_line_hold;
   BasicEmulator m;
   WriteHandler news_bgpic_w;
   int[] news_bgram;
   WriteHandler news_bgram_w;
   int[] news_fgram;
   WriteHandler news_fgram_w;
   VideoInitializer news_vs;
   VideoRenderer news_vu;
   WriteHandler paletteram_xxxxRRRRGGGGBBBB_swap_w;
   int[][] tiles8x8_layout;
   cottage.vidhrdw.News v = new cottage.vidhrdw.News();

   public News() {
      this.news_fgram = this.v.Fnews_fgram;
      this.news_bgram = this.v.Fnews_bgram;
      this.news_fgram_w = this.v.news_fgram_w();
      this.news_bgram_w = this.v.news_bgram_w();
      this.news_bgpic_w = this.v.news_bgpic_w();
      this.news_vs = this.v;
      this.news_vu = this.v;
      this.paletteram_xxxxRRRRGGGGBBBB_swap_w = this.v.paletteram_xxxxRRRRGGGGBBBB_swap_w();
      this.m = new BasicEmulator();
      this.irq0_line_hold = this.m.irq0_line_hold();
      int[] var5 = new int[]{8};
      int[] var6 = new int[]{8};
      int var1 = this.RGN_FRAC(1, 1);
      int[] var7 = new int[]{4};
      int[] var4 = new int[]{0, 1, 2, 3};
      int[] var3 = new int[]{0, 4, 8, 12, 16, 20, 24, 28};
      int[] var2 = new int[]{0, 32, 64, 96, 128, 160, 192, 224};
      int[] var8 = new int[]{256};
      this.tiles8x8_layout = new int[][]{var5, var6, {var1}, var7, var4, var3, var2, var8};
   }

   private boolean gfxdecodeinfo() {
      this.GDI_ADD(8, 0, this.tiles8x8_layout, 0, 16);
      this.GDI_ADD(-1);
      return true;
   }

   private boolean ipt_news() {
      this.PORT_START();
      this.PORT_DIPNAME(3, 3, this.DEF_STR2(5));
      this.PORT_DIPSETTING(0, this.DEF_STR2(8));
      this.PORT_DIPSETTING(1, this.DEF_STR2(9));
      this.PORT_DIPSETTING(3, this.DEF_STR2(10));
      this.PORT_DIPSETTING(2, this.DEF_STR2(11));
      this.PORT_DIPNAME(12, 12, this.DEF_STR2(38));
      this.PORT_DIPSETTING(12, "Easy");
      this.PORT_DIPSETTING(8, "Medium");
      this.PORT_DIPSETTING(4, "Hard");
      this.PORT_DIPSETTING(0, "Hardest");
      this.PORT_DIPNAME(16, 16, "Helps");
      this.PORT_DIPSETTING(16, "1");
      this.PORT_DIPSETTING(0, "2");
      this.PORT_DIPNAME(32, 0, "Copyright");
      this.PORT_DIPSETTING(0, "Poby");
      this.PORT_DIPSETTING(32, "Virus");
      this.PORT_DIPNAME(64, 64, this.DEF_STR2(44));
      this.PORT_DIPSETTING(64, this.DEF_STR2(2));
      this.PORT_DIPSETTING(0, this.DEF_STR2(1));
      this.PORT_DIPNAME(128, 128, this.DEF_STR2(44));
      this.PORT_DIPSETTING(128, this.DEF_STR2(2));
      this.PORT_DIPSETTING(0, this.DEF_STR2(1));
      this.PORT_START();
      this.PORT_BIT(1, 255, 1);
      this.PORT_BIT(2, 255, 4);
      this.PORT_BIT(4, 255, 10);
      this.PORT_BIT(8, 255, 11);
      this.PORT_BIT(16, 255, 8);
      this.PORT_BIT(32, 255, 9);
      this.PORT_BIT(64, 255, 12);
      this.PORT_BIT(128, 255, 13);
      return true;
   }

   private boolean readmem() {
      this.MR_START(0, 32767, 1);
      this.MR_ADD('耀', '迿', 0);
      this.MR_ADD('쀀', '쀀', this.input_port_0_r);
      this.MR_ADD('쀁', '쀁', this.input_port_1_r);
      this.MR_ADD('\ue000', '\uffff', 0);
      return true;
   }

   private boolean rom_news() {
      this.ROM_REGION(65536, 0, 0);
      this.ROM_LOAD("virus.4", 0, '耀', this.BADCRC(-1442816517));
      this.ROM_REGION(524288, 8, 0);
      this.ROM_LOAD16_BYTE("virus.2", 0, 262144, -1246799656);
      this.ROM_LOAD16_BYTE("virus.3", 1, 262144, -1531854475);
      return true;
   }

   private boolean writemem() {
      this.MW_START(0, 32767, 1);
      this.MW_ADD('耀', '蟿', this.news_fgram_w, this.news_fgram);
      this.MW_ADD('蠀', '迿', this.news_bgram_w, this.news_bgram);
      this.MW_ADD('退', '釿', 0);
      this.MW_ADD('退', '釿', this.paletteram_xxxxRRRRGGGGBBBB_swap_w, 5);
      this.MW_ADD('쀃', '쀃', this.news_bgpic_w);
      this.MW_ADD('\ue000', '\uffff', 0);
      return true;
   }

   public Emulator getMachine(URL var1, String var2) {
      super.getMachine(var1, var2);
      super.setVideoEmulator(this.v);
      this.m = new BasicEmulator();
      if(var2.equals("news")) {
         this.GAME(1993, this.rom_news(), 0, this.mdrv_news(), this.ipt_news(), 0, 0, "Poby / Virus", "News");
      }

      this.m.init(this.md);
      return this.m;
   }

   public boolean mdrv_news() {
      this.MDRV_CPU_ADD(0, 8000000);
      this.MDRV_CPU_MEMORY(this.readmem(), this.writemem());
      this.MDRV_CPU_VBLANK_INT(this.irq0_line_hold, 1);
      this.MDRV_FRAMES_PER_SECOND(60);
      this.MDRV_VBLANK_DURATION(0);
      this.MDRV_VIDEO_ATTRIBUTES(1);
      this.MDRV_SCREEN_SIZE(256, 256);
      this.MDRV_VISIBLE_AREA(0, 255, 16, 239);
      this.MDRV_GFXDECODE(this.gfxdecodeinfo());
      this.MDRV_PALETTE_LENGTH(256);
      this.MDRV_VIDEO_START(this.news_vs);
      this.MDRV_VIDEO_UPDATE(this.news_vu);
      return true;
   }
}
