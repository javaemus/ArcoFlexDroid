package cottage.drivers;

import java.net.URL;

import cottage.mame.Driver;
import cottage.mame.MAMEConstants;
import cottage.mame.MAMEDriver;
import jef.machine.BasicEmulator;
import jef.machine.Emulator;
import jef.map.InterruptHandler;
import jef.map.WriteHandler;
import jef.video.PaletteInitializer;
import jef.video.VideoInitializer;
import jef.video.VideoRenderer;

public class Troangel extends MAMEDriver implements Driver, MAMEConstants {
   int[][] charlayout;
   VideoInitializer generic_vs;
   InterruptHandler irq0_line_hold;
   BasicEmulator m;
   int[][] spritelayout;
   PaletteInitializer troangel_pi;
   int[] troangel_scroll;
   VideoRenderer troangel_vu;
   cottage.vidhrdw.Troangel v = new cottage.vidhrdw.Troangel();
   WriteHandler videoram_w;

   public Troangel() {
      this.generic_vs = this.v;
      this.troangel_vu = this.v;
      this.troangel_pi = this.v;
      this.troangel_scroll = this.v.Ftroangel_scroll;
      this.videoram_w = this.v.writeVRAM();
      this.m = new BasicEmulator();
      this.irq0_line_hold = this.m.irq0_line_hold();
      int[] var4 = new int[]{8};
      int[] var5 = new int[]{8};
      int[] var6 = new int[]{1024};
      int[] var2 = new int[]{131072, 65536, 0};
      int[] var3 = new int[]{0, 1, 2, 3, 4, 5, 6, 7};
      int[] var1 = new int[]{0, 8, 16, 24, 32, 40, 48, 56};
      this.charlayout = new int[][]{var4, var5, var6, {3}, var2, var3, var1, {64}};
      var4 = new int[]{16};
      var5 = new int[]{32};
      var1 = new int[]{262144, 131072, 0};
      var3 = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 128, 129, 130, 131, 132, 133, 134, 135};
      var2 = new int[]{0, 8, 16, 24, 32, 40, 48, 56, 64, 72, 80, 88, 96, 104, 112, 120, 16384, 16392, 16400, 16408, 16416, 16424, 16432, 16440, 16448, 16456, 16464, 16472, 16480, 16488, 16496, 16504};
      this.spritelayout = new int[][]{var4, var5, {64}, {3}, var1, var3, var2, {256}};
   }

   private boolean ipt_troangel() {
      this.PORT_START();
      this.PORT_BIT(1, 255, 4);
      this.PORT_BIT(2, 255, 5);
      this.PORT_BIT(4, 255, 2);
      this.PORT_BIT(8, 255, 1);
      this.PORT_BIT(240, 255, 0);
      this.PORT_START();
      this.PORT_BIT(1, 255, 9);
      this.PORT_BIT(2, 255, 8);
      this.PORT_BIT(4, 255, 0);
      this.PORT_BIT(8, 255, 0);
      this.PORT_BIT(16, 255, 0);
      this.PORT_BIT(32, 255, 13);
      this.PORT_BIT(64, 255, 0);
      this.PORT_BIT(128, 255, 12);
      this.PORT_START();
      this.PORT_BIT(1, 255, 137);
      this.PORT_BIT(2, 255, 136);
      this.PORT_BIT(4, 255, 0);
      this.PORT_BIT(8, 255, 0);
      this.PORT_BIT(16, 255, 2);
      this.PORT_BIT(32, 255, 141);
      this.PORT_BIT(64, 255, 0);
      this.PORT_BIT(128, 255, 140);
      this.PORT_START();
      this.PORT_DIPNAME(3, 3, "Time");
      this.PORT_DIPSETTING(3, "180 160 140");
      this.PORT_DIPSETTING(2, "160 140 120");
      this.PORT_DIPSETTING(1, "140 120 100");
      this.PORT_DIPSETTING(0, "120 100 100");
      this.PORT_DIPNAME(4, 4, "Crash Loss Time");
      this.PORT_DIPSETTING(4, "5");
      this.PORT_DIPSETTING(0, "10");
      this.PORT_DIPNAME(8, 8, "Background Sound");
      this.PORT_DIPSETTING(8, "Boat Motor");
      this.PORT_DIPSETTING(0, "Music");
      this.PORT_DIPNAME(240, 240, this.DEF_STR2(5));
      this.PORT_DIPSETTING(160, this.DEF_STR2(46));
      this.PORT_DIPSETTING(176, this.DEF_STR2(6));
      this.PORT_DIPSETTING(192, this.DEF_STR2(7));
      this.PORT_DIPSETTING(208, this.DEF_STR2(8));
      this.PORT_DIPSETTING(224, this.DEF_STR2(9));
      this.PORT_DIPSETTING(240, this.DEF_STR2(10));
      this.PORT_DIPSETTING(112, this.DEF_STR2(11));
      this.PORT_DIPSETTING(96, this.DEF_STR2(12));
      this.PORT_DIPSETTING(80, this.DEF_STR2(13));
      this.PORT_DIPSETTING(64, this.DEF_STR2(14));
      this.PORT_DIPSETTING(48, this.DEF_STR2(15));
      this.PORT_DIPSETTING(0, this.DEF_STR2(37));
      this.PORT_START();
      this.PORT_DIPNAME(1, 1, this.DEF_STR2(41));
      this.PORT_DIPSETTING(1, this.DEF_STR2(2));
      this.PORT_DIPSETTING(0, this.DEF_STR2(1));
      this.PORT_DIPNAME(2, 0, this.DEF_STR2(33));
      this.PORT_DIPSETTING(0, this.DEF_STR2(34));
      this.PORT_DIPSETTING(2, this.DEF_STR2(35));
      this.PORT_DIPNAME(4, 4, "Coin Mode");
      this.PORT_DIPSETTING(4, "Mode 1");
      this.PORT_DIPSETTING(0, "Mode 2");
      this.PORT_DIPNAME(8, 8, "Analog Accelarator");
      this.PORT_DIPSETTING(8, this.DEF_STR2(43));
      this.PORT_DIPSETTING(0, this.DEF_STR2(42));
      this.PORT_DIPSETTING(16, this.DEF_STR2(2));
      this.PORT_DIPSETTING(0, this.DEF_STR2(1));
      this.PORT_DIPNAME(32, 32, this.DEF_STR2(0));
      this.PORT_DIPSETTING(32, this.DEF_STR2(2));
      this.PORT_DIPSETTING(0, this.DEF_STR2(1));
      this.PORT_DIPSETTING(64, this.DEF_STR2(2));
      this.PORT_DIPSETTING(0, this.DEF_STR2(1));
      this.PORT_SERVICE(128, 255);
      return true;
   }

   private boolean rom_troangel() {
      this.ROM_REGION(65536, 0, 0);
      this.ROM_LOAD("ta-a-3k", 0, 8192, -232816234);
      this.ROM_LOAD("ta-a-3m", 8192, 8192, 1484791381);
      this.ROM_LOAD("ta-a-3n", 16384, 8192, -566367676);
      this.ROM_LOAD("ta-a-3q", 24576, 8192, -984022);
      this.ROM_REGION(65536, 1, 0);
      this.ROM_LOAD("ta-s-1a", '\ue000', 8192, 363344400);
      this.ROM_REGION(24576, 8, 1);
      this.ROM_LOAD("ta-a-3c", 0, 8192, 2146781231);
      this.ROM_LOAD("ta-a-3d", 8192, 8192, 116322881);
      this.ROM_LOAD("ta-a-3e", 16384, 8192, -459310376);
      this.ROM_REGION('쀀', 9, 1);
      this.ROM_LOAD("ta-b-5j", 0, 8192, -2037818356);
      this.ROM_LOAD("ta-b-5h", 8192, 8192, -120589667);
      this.ROM_LOAD("ta-b-5e", 16384, 8192, -1960710502);
      this.ROM_LOAD("ta-b-5d", 24576, 8192, -850969273);
      this.ROM_LOAD("ta-b-5c", '耀', 8192, -1047448375);
      this.ROM_LOAD("ta-b-5a", 'ꀀ', 8192, 1210666);
      this.ROM_REGION(800, 16, 0);
      this.ROM_LOAD("ta-a-5a", 0, 256, 31330663);
      this.ROM_LOAD("ta-a-5b", 256, 256, -271508149);
      this.ROM_LOAD("ta-b-1b", 512, 32, -112651798);
      this.ROM_LOAD("ta-b-3d", 544, 256, -314692956);
      return true;
   }

   private boolean troangel_gfxdecodeinfo() {
      this.GDI_ADD(8, 0, this.charlayout, 0, 32);
      this.GDI_ADD(9, 0, this.spritelayout, 256, 32);
      this.GDI_ADD(9, 4096, this.spritelayout, 256, 32);
      this.GDI_ADD(9, 8192, this.spritelayout, 256, 32);
      this.GDI_ADD(9, 12288, this.spritelayout, 256, 32);
      this.GDI_ADD(-1);
      return true;
   }

   private boolean troangel_readmem() {
      this.MR_START(0, 32767, 1);
      this.MR_ADD('耀', '迿', 0);
      this.MR_ADD('退', '郿', 0);
      this.MR_ADD('퀀', '퀀', this.input_port_0_r);
      this.MR_ADD('퀁', '퀁', this.input_port_1_r);
      this.MR_ADD('퀂', '퀂', this.input_port_2_r);
      this.MR_ADD('퀃', '퀃', this.input_port_3_r);
      this.MR_ADD('퀄', '퀄', this.input_port_4_r);
      this.MR_ADD('\ue000', '\ue7ff', 0);
      return true;
   }

   private boolean troangel_writemem() {
      this.MW_START(0, 32767, 1);
      this.MW_ADD('耀', '蟿', this.videoram_w, 0, 0);
      this.MW_ADD('退', '釿', 0, this.troangel_scroll);
      this.MW_ADD('젠', '죿', 0, 2, 1);
      this.MW_ADD('\ue000', '\ue7ff', 0);
      return true;
   }

   public Emulator getMachine(URL var1, String var2) {
      super.getMachine(var1, var2);
      super.setVideoEmulator(this.v);
      if(var2.equals("troangel")) {
         this.GAME(1983, this.rom_troangel(), 0, this.mdrv_troangel(), this.ipt_troangel(), 0, 0, "Irem", "Tropical Angel");
      }

      this.m.init(this.md);
      this.v.setMachine(this.m);
      return this.m;
   }

   public boolean mdrv_troangel() {
      this.MDRV_CPU_ADD(0, 3000000);
      this.MDRV_CPU_MEMORY(this.troangel_readmem(), this.troangel_writemem());
      this.MDRV_CPU_VBLANK_INT(this.irq0_line_hold, 1);
      this.MDRV_FRAMES_PER_SECOND(57);
      this.MDRV_VBLANK_DURATION(1790);
      this.MDRV_VIDEO_ATTRIBUTES(1);
      this.MDRV_SCREEN_SIZE(256, 256);
      this.MDRV_VISIBLE_AREA(8, 247, 8, 247);
      this.MDRV_GFXDECODE(this.troangel_gfxdecodeinfo());
      this.MDRV_PALETTE_LENGTH(272);
      this.MDRV_COLORTABLE_LENGTH(512);
      this.MDRV_PALETTE_INIT(this.troangel_pi);
      this.MDRV_VIDEO_START(this.generic_vs);
      this.MDRV_VIDEO_UPDATE(this.troangel_vu);
      return true;
   }
}
