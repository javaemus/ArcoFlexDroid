package cottage.drivers;

import java.net.URL;

import cottage.mame.Driver;
import cottage.mame.MAMEConstants;
import cottage.mame.MAMEDriver;
import jef.machine.BasicEmulator;
import jef.machine.Emulator;
import jef.map.InterruptHandler;
import jef.map.ReadHandler;
import jef.map.WriteHandler;
import jef.video.PaletteInitializer;
import jef.video.VideoInitializer;
import jef.video.VideoRenderer;

public class Mrjong extends MAMEDriver implements Driver, MAMEConstants {
   WriteHandler colorram_w;
   VideoInitializer generic_vs;
   WriteHandler io_0x00_w;
   ReadHandler io_0x03_r;
   BasicEmulator m;
   PaletteInitializer mrjong_pi;
   VideoRenderer mrjong_vu;
   InterruptHandler nmi_line_pulse;
   int[][] spritelayout;
   int[][] tilelayout;
   cottage.vidhrdw.Mrjong v = new cottage.vidhrdw.Mrjong();
   WriteHandler videoram_w;

   public Mrjong() {
      this.generic_vs = this.v;
      this.mrjong_vu = this.v;
      this.mrjong_pi = this.v;
      this.videoram_w = this.v.writeVRAM();
      this.colorram_w = this.v.writeVRAMColors();
      this.m = new BasicEmulator();
      this.nmi_line_pulse = this.m.nmi_interrupt_switched();
      this.io_0x00_w = new Io_0x00_w();
      this.io_0x03_r = new Io_0x03_r();
      int[] var4 = new int[]{2};
      int[] var1 = new int[]{'耀', 0};
      int[] var2 = new int[]{0, 1, 2, 3, 4, 5, 6, 7};
      int[] var3 = new int[]{56, 48, 40, 32, 24, 16, 8, 0};
      int[] var5 = new int[]{64};
      this.tilelayout = new int[][]{{8}, {8}, {512}, var4, var1, var2, var3, var5};
      var1 = new int[]{'耀', 0};
      var2 = new int[]{64, 65, 66, 67, 68, 69, 70, 71, 0, 1, 2, 3, 4, 5, 6, 7};
      var3 = new int[]{184, 176, 168, 160, 152, 144, 136, 128, 56, 48, 40, 32, 24, 16, 8, 0};
      this.spritelayout = new int[][]{{16}, {16}, {128}, {2}, var1, var2, var3, {256}};
   }

   private boolean gfxdecodeinfo() {
      this.GDI_ADD(8, 0, this.tilelayout, 0, 32);
      this.GDI_ADD(8, 0, this.spritelayout, 0, 32);
      this.GDI_ADD(-1);
      return true;
   }

   private boolean ipt_mrjong() {
      this.PORT_START();
      this.PORT_BIT(1, 0, 135);
      this.PORT_BIT(2, 0, 133);
      this.PORT_BIT(4, 0, 134);
      this.PORT_BIT(8, 0, 136);
      this.PORT_BIT(16, 0, 137);
      this.PORT_BIT(32, 0, 3);
      this.PORT_BIT(64, 0, 4);
      this.PORT_BIT(128, 255, 0);
      this.PORT_START();
      this.PORT_BIT(1, 0, 7);
      this.PORT_BIT(2, 0, 5);
      this.PORT_BIT(4, 0, 6);
      this.PORT_BIT(8, 0, 8);
      this.PORT_BIT(16, 0, 9);
      this.PORT_BIT(32, 0, 1);
      this.PORT_BIT(64, 0, 2);
      this.PORT_BIT(128, 0, 0);
      this.PORT_START();
      this.PORT_DIPNAME(1, 1, this.DEF_STR2(33));
      this.PORT_DIPSETTING(1, this.DEF_STR2(34));
      this.PORT_DIPSETTING(0, this.DEF_STR2(35));
      this.PORT_DIPNAME(2, 0, this.DEF_STR2(41));
      this.PORT_DIPSETTING(0, this.DEF_STR2(2));
      this.PORT_DIPSETTING(2, this.DEF_STR2(1));
      this.PORT_DIPNAME(4, 0, this.DEF_STR2(4));
      this.PORT_DIPSETTING(0, "30k");
      this.PORT_DIPSETTING(4, "50k");
      this.PORT_DIPNAME(8, 0, this.DEF_STR2(38));
      this.PORT_DIPSETTING(0, "Normal");
      this.PORT_DIPSETTING(8, "Hard");
      this.PORT_DIPNAME(48, 0, this.DEF_STR2(3));
      this.PORT_DIPSETTING(0, "3");
      this.PORT_DIPSETTING(16, "4");
      this.PORT_DIPSETTING(32, "5");
      this.PORT_DIPSETTING(48, "6");
      this.PORT_DIPNAME(192, 0, this.DEF_STR2(5));
      this.PORT_DIPSETTING(192, this.DEF_STR2(9));
      this.PORT_DIPSETTING(0, this.DEF_STR2(10));
      this.PORT_DIPSETTING(64, this.DEF_STR2(11));
      this.PORT_DIPSETTING(128, this.DEF_STR2(12));
      return true;
   }

   private boolean readmem() {
      this.MR_START(0, 32767, 1);
      this.MR_ADD('耀', '蟿', 0);
      this.MR_ADD('ꀀ', 'ꟿ', 0);
      this.MR_ADD('\ue000', '\ue3ff', 0);
      this.MR_ADD('\ue400', '\ue7ff', 0);
      return true;
   }

   private boolean readport() {
      this.PR_START(0, 0, this.input_port_0_r);
      this.PR_ADD(1, 1, this.input_port_1_r);
      this.PR_ADD(2, 2, this.input_port_2_r);
      this.PR_ADD(3, 3, this.io_0x03_r);
      return true;
   }

   private boolean rom_crazyblk() {
      this.ROM_REGION(65536, 0, 0);
      this.ROM_LOAD("c1.a6", 0, 8192, -492695134);
      this.ROM_LOAD("c2.a7", 8192, 8192, 1963395448);
      this.ROM_LOAD("c3.a7", 16384, 8192, 1768727810);
      this.ROM_LOAD("c4.a8", 24576, 8192, -940203449);
      this.ROM_REGION(8192, 8, 0);
      this.ROM_LOAD("c6.h5", 0, 4096, 724236180);
      this.ROM_LOAD("c5.h4", 4096, 4096, -1731118827);
      this.ROM_REGION(288, 16, 0);
      this.ROM_LOAD("clr.j7", 0, 32, -300092971);
      this.ROM_LOAD("clr.g5", 32, 256, -1129192733);
      return true;
   }

   private boolean rom_mrjong() {
      this.ROM_REGION(65536, 0, 0);
      this.ROM_LOAD("mj00", 0, 8192, -770593069);
      this.ROM_LOAD("mj01", 8192, 8192, 1235864190);
      this.ROM_LOAD("mj02", 16384, 8192, 1263578730);
      this.ROM_LOAD("mj03", 24576, 8192, 741825047);
      this.ROM_REGION(8192, 8, 0);
      this.ROM_LOAD("mj21", 0, 4096, 514432427);
      this.ROM_LOAD("mj20", 4096, 4096, 2125583233);
      this.ROM_REGION(288, 16, 0);
      this.ROM_LOAD("mj61", 0, 32, -1470194905);
      this.ROM_LOAD("mj60", 32, 256, -584372145);
      return true;
   }

   private boolean writemem() {
      this.MW_START(0, 32767, 1);
      this.MW_ADD('耀', '蟿', 0);
      this.MW_ADD('ꀀ', 'ꟿ', 0);
      this.MW_ADD('\ue000', '\ue3ff', this.videoram_w, 0, 0);
      this.MW_ADD('\ue400', '\ue7ff', this.colorram_w, 1);
      this.MW_ADD('\ue000', '\ue03f', 0, 2, 1);
      return true;
   }

   private boolean writeport() {
      this.PW_START(0, 0, this.io_0x00_w);
      return true;
   }

   public Emulator getMachine(URL var1, String var2) {
      super.getMachine(var1, var2);
      super.setVideoEmulator(this.v);
      if(var2.equals("mrjong")) {
         this.GAME(1983, this.rom_mrjong(), 0, this.mdrv_mrjong(), this.ipt_mrjong(), 0, 1, "Kiwako", "Mr. Jong (Japan)");
      } else if(var2.equals("crazyblk")) {
         this.GAME(1983, this.rom_crazyblk(), "mrjong", this.mdrv_mrjong(), this.ipt_mrjong(), 0, 1, "Kiwako (ECI license)", "Crazy Blocks");
      }

      this.m.init(this.md);
      this.v.setMachine(this.m);
      return this.m;
   }

   public boolean mdrv_mrjong() {
      this.MDRV_CPU_ADD(0, 2578000);
      this.MDRV_CPU_MEMORY(this.readmem(), this.writemem());
      this.MDRV_CPU_PORTS(this.readport(), this.writeport());
      this.MDRV_CPU_VBLANK_INT(this.nmi_line_pulse, 1);
      this.MDRV_FRAMES_PER_SECOND(60);
      this.MDRV_VBLANK_DURATION(0);
      this.MDRV_VIDEO_ATTRIBUTES(1);
      this.MDRV_SCREEN_SIZE(256, 256);
      this.MDRV_VISIBLE_AREA(0, 239, 16, 239);
      this.MDRV_GFXDECODE(this.gfxdecodeinfo());
      this.MDRV_PALETTE_LENGTH(16);
      this.MDRV_COLORTABLE_LENGTH(128);
      this.MDRV_PALETTE_INIT(this.mrjong_pi);
      this.MDRV_VIDEO_START(this.generic_vs);
      this.MDRV_VIDEO_UPDATE(this.mrjong_vu);
      return true;
   }

   class Io_0x00_w implements WriteHandler {
      public void write(int var1, int var2) {
      }
   }

   class Io_0x03_r implements ReadHandler {
      public int read(int var1) {
         return 0;
      }
   }
}
