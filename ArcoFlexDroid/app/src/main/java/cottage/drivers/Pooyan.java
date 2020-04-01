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

public class Pooyan extends MAMEDriver implements Driver, MAMEConstants {
   int[][] charlayout;
   WriteHandler colorram_w;
   VideoInitializer generic_vs;
   WriteHandler interrupt_enable_w;
   BasicEmulator m;
   InterruptHandler nmi_line_pulse;
   WriteHandler pooyan_flipscreen_w;
   PaletteInitializer pooyan_pi;
   VideoRenderer pooyan_vu;
   int[][] spritelayout;
   cottage.vidhrdw.Pooyan v = new cottage.vidhrdw.Pooyan();
   WriteHandler videoram_w;

   public Pooyan() {
      this.generic_vs = this.v;
      this.pooyan_vu = this.v;
      this.pooyan_pi = this.v;
      this.videoram_w = this.v.writeVRAM();
      this.colorram_w = this.v.writeVRAMColors();
      this.pooyan_flipscreen_w = this.v.pooyan_flipscreen_w();
      this.m = new BasicEmulator();
      this.nmi_line_pulse = this.m.nmi_interrupt_switched();
      this.interrupt_enable_w = this.m.nmi_interrupt_enable();
      int[] var4 = new int[]{8};
      int[] var5 = new int[]{256};
      int[] var6 = new int[]{4};
      int[] var2 = new int[]{4, 0, '耄', '耀'};
      int[] var3 = new int[]{0, 1, 2, 3, 64, 65, 66, 67};
      int[] var1 = new int[]{0, 8, 16, 24, 32, 40, 48, 56};
      int[] var7 = new int[]{128};
      this.charlayout = new int[][]{var4, {8}, var5, var6, var2, var3, var1, var7};
      var4 = new int[]{16};
      var5 = new int[]{4};
      var3 = new int[]{4, 0, '耄', '耀'};
      var2 = new int[]{0, 1, 2, 3, 64, 65, 66, 67, 128, 129, 130, 131, 192, 193, 194, 195};
      var1 = new int[]{0, 8, 16, 24, 32, 40, 48, 56, 256, 264, 272, 280, 288, 296, 304, 312};
      this.spritelayout = new int[][]{var4, {16}, {64}, var5, var3, var2, var1, {512}};
   }

   private boolean gfxdecodeinfo() {
      this.GDI_ADD(8, 0, this.charlayout, 0, 16);
      this.GDI_ADD(9, 0, this.spritelayout, 256, 16);
      this.GDI_ADD(-1);
      return true;
   }

   private boolean ipt_pooyan() {
      this.PORT_START();
      this.PORT_BIT(1, 255, 1);
      this.PORT_BIT(2, 255, 2);
      this.PORT_BIT(4, 255, 2);
      this.PORT_BIT(8, 255, 4);
      this.PORT_BIT(16, 255, 5);
      this.PORT_BIT(32, 255, 0);
      this.PORT_BIT(64, 255, 0);
      this.PORT_BIT(128, 255, 0);
      this.PORT_START();
      this.PORT_BIT(1, 255, 0);
      this.PORT_BIT(2, 255, 0);
      this.PORT_BIT(4, 255, 10);
      this.PORT_BIT(8, 255, 11);
      this.PORT_BIT(16, 255, 12);
      this.PORT_BIT(32, 255, 0);
      this.PORT_BIT(64, 255, 0);
      this.PORT_BIT(128, 255, 0);
      this.PORT_START();
      this.PORT_BIT(1, 255, 0);
      this.PORT_BIT(2, 255, 0);
      this.PORT_BIT(4, 255, 138);
      this.PORT_BIT(8, 255, 139);
      this.PORT_BIT(16, 255, 140);
      this.PORT_BIT(32, 255, 0);
      this.PORT_BIT(64, 255, 0);
      this.PORT_BIT(128, 255, 0);
      this.PORT_START();
      this.PORT_DIPNAME(15, 15, this.DEF_STR2(39));
      this.PORT_DIPSETTING(2, this.DEF_STR2(7));
      this.PORT_DIPSETTING(5, this.DEF_STR2(8));
      this.PORT_DIPSETTING(8, this.DEF_STR2(9));
      this.PORT_DIPSETTING(4, this.DEF_STR2(21));
      this.PORT_DIPSETTING(1, this.DEF_STR2(26));
      this.PORT_DIPSETTING(15, this.DEF_STR2(10));
      this.PORT_DIPSETTING(3, this.DEF_STR2(23));
      this.PORT_DIPSETTING(7, this.DEF_STR2(18));
      this.PORT_DIPSETTING(14, this.DEF_STR2(11));
      this.PORT_DIPSETTING(6, this.DEF_STR2(20));
      this.PORT_DIPSETTING(13, this.DEF_STR2(12));
      this.PORT_DIPSETTING(12, this.DEF_STR2(13));
      this.PORT_DIPSETTING(11, this.DEF_STR2(14));
      this.PORT_DIPSETTING(10, this.DEF_STR2(15));
      this.PORT_DIPSETTING(9, this.DEF_STR2(16));
      this.PORT_DIPSETTING(0, this.DEF_STR2(37));
      this.PORT_DIPNAME(240, 240, this.DEF_STR2(40));
      this.PORT_DIPSETTING(0, "Attract Mode - No Play");
      this.PORT_DIPSETTING(32, this.DEF_STR2(7));
      this.PORT_DIPSETTING(80, this.DEF_STR2(8));
      this.PORT_DIPSETTING(128, this.DEF_STR2(9));
      this.PORT_DIPSETTING(64, this.DEF_STR2(21));
      this.PORT_DIPSETTING(16, this.DEF_STR2(26));
      this.PORT_DIPSETTING(240, this.DEF_STR2(10));
      this.PORT_DIPSETTING(48, this.DEF_STR2(23));
      this.PORT_DIPSETTING(112, this.DEF_STR2(18));
      this.PORT_DIPSETTING(224, this.DEF_STR2(11));
      this.PORT_DIPSETTING(96, this.DEF_STR2(20));
      this.PORT_DIPSETTING(208, this.DEF_STR2(12));
      this.PORT_DIPSETTING(192, this.DEF_STR2(13));
      this.PORT_DIPSETTING(176, this.DEF_STR2(14));
      this.PORT_DIPSETTING(160, this.DEF_STR2(15));
      this.PORT_DIPSETTING(144, this.DEF_STR2(16));
      this.PORT_START();
      this.PORT_DIPNAME(3, 3, this.DEF_STR2(3));
      this.PORT_DIPSETTING(3, "3");
      this.PORT_DIPSETTING(2, "4");
      this.PORT_DIPSETTING(1, "5");
      this.PORT_DIPNAME(4, 0, this.DEF_STR2(33));
      this.PORT_DIPSETTING(0, this.DEF_STR2(34));
      this.PORT_DIPSETTING(4, this.DEF_STR2(35));
      this.PORT_DIPNAME(8, 8, this.DEF_STR2(4));
      this.PORT_DIPSETTING(8, "50000 80000");
      this.PORT_DIPSETTING(0, "30000 70000");
      this.PORT_DIPNAME(112, 112, this.DEF_STR2(38));
      this.PORT_DIPSETTING(112, "Easiest");
      this.PORT_DIPSETTING(96, "Easier");
      this.PORT_DIPSETTING(80, "Easy");
      this.PORT_DIPSETTING(64, "Normal");
      this.PORT_DIPSETTING(48, "Medium");
      this.PORT_DIPSETTING(32, "Difficult");
      this.PORT_DIPSETTING(16, "Hard");
      this.PORT_DIPSETTING(0, "Hardest");
      this.PORT_DIPNAME(128, 0, this.DEF_STR2(36));
      this.PORT_DIPSETTING(128, this.DEF_STR2(2));
      this.PORT_DIPSETTING(0, this.DEF_STR2(1));
      return true;
   }

   private boolean readmem() {
      this.MR_START(0, 32767, 1);
      this.MR_ADD('耀', '迿', 0);
      this.MR_ADD('ꀀ', 'ꀀ', this.input_port_4_r);
      this.MR_ADD('ꂀ', 'ꂀ', this.input_port_0_r);
      this.MR_ADD('ꂠ', 'ꂠ', this.input_port_1_r);
      this.MR_ADD('ꃀ', 'ꃀ', this.input_port_2_r);
      this.MR_ADD('ꃠ', 'ꃠ', this.input_port_3_r);
      return true;
   }

   private boolean rom_pootan() {
      this.ROM_REGION(65536, 0, 0);
      this.ROM_LOAD("poo_ic22.bin", 0, 8192, 1102199332);
      this.ROM_LOAD("poo_ic23.bin", 8192, 8192, -908507551);
      this.ROM_LOAD("3.6a", 16384, 8192, -31810040);
      this.ROM_LOAD("poo_ic25.bin", 24576, 8192, -1964746257);
      this.ROM_REGION(65536, 1, 0);
      this.ROM_LOAD("xx.7a", 0, 4096, -69029016);
      this.ROM_LOAD("xx.8a", 4096, 4096, -512140483);
      this.ROM_REGION(8192, 8, 1);
      this.ROM_LOAD("poo_ic13.bin", 0, 4096, 199754468);
      this.ROM_LOAD("poo_ic14.bin", 4096, 4096, -878538602);
      this.ROM_REGION(8192, 9, 1);
      this.ROM_LOAD("6.9a", 0, 4096, -1294417631);
      this.ROM_LOAD("5.8a", 4096, 4096, 278381238);
      this.ROM_REGION(544, 16, 0);
      this.ROM_LOAD("pooyan.pr1", 0, 32, -1603638002);
      this.ROM_LOAD("pooyan.pr2", 32, 256, -2106291189);
      this.ROM_LOAD("pooyan.pr3", 288, 256, -1932210848);
      return true;
   }

   private boolean rom_pooyan() {
      this.ROM_REGION(65536, 0, 0);
      this.ROM_LOAD("1.4a", 0, 8192, -1154376605);
      this.ROM_LOAD("2.5a", 8192, 8192, -1589232232);
      this.ROM_LOAD("3.6a", 16384, 8192, -31810040);
      this.ROM_LOAD("4.7a", 24576, 8192, -1643144244);
      this.ROM_REGION(65536, 1, 0);
      this.ROM_LOAD("xx.7a", 0, 4096, -69029016);
      this.ROM_LOAD("xx.8a", 4096, 4096, -512140483);
      this.ROM_REGION(8192, 8, 1);
      this.ROM_LOAD("8.10g", 0, 4096, -1826936341);
      this.ROM_LOAD("7.9g", 4096, 4096, -1142499612);
      this.ROM_REGION(8192, 9, 1);
      this.ROM_LOAD("6.9a", 0, 4096, -1294417631);
      this.ROM_LOAD("5.8a", 4096, 4096, 278381238);
      this.ROM_REGION(544, 16, 0);
      this.ROM_LOAD("pooyan.pr1", 0, 32, -1603638002);
      this.ROM_LOAD("pooyan.pr2", 32, 256, -2106291189);
      this.ROM_LOAD("pooyan.pr3", 288, 256, -1932210848);
      return true;
   }

   private boolean rom_pooyans() {
      this.ROM_REGION(65536, 0, 0);
      this.ROM_LOAD("ic22_a4.cpu", 0, 8192, -1855264809);
      this.ROM_LOAD("ic23_a5.cpu", 8192, 8192, -1880912799);
      this.ROM_LOAD("ic24_a6.cpu", 16384, 8192, 643834250);
      this.ROM_LOAD("ic25_a7.cpu", 24576, 8192, 1026166957);
      this.ROM_REGION(65536, 1, 0);
      this.ROM_LOAD("xx.7a", 0, 4096, -69029016);
      this.ROM_LOAD("xx.8a", 4096, 4096, -512140483);
      this.ROM_REGION(8192, 8, 1);
      this.ROM_LOAD("ic13_g10.cpu", 0, 4096, 1949544105);
      this.ROM_LOAD("ic14_g9.cpu", 4096, 4096, -2017363810);
      this.ROM_REGION(8192, 9, 1);
      this.ROM_LOAD("6.9a", 0, 4096, -1294417631);
      this.ROM_LOAD("5.8a", 4096, 4096, 278381238);
      this.ROM_REGION(544, 16, 0);
      this.ROM_LOAD("pooyan.pr1", 0, 32, -1603638002);
      this.ROM_LOAD("pooyan.pr2", 32, 256, -2106291189);
      this.ROM_LOAD("pooyan.pr3", 288, 256, -1932210848);
      return true;
   }

   private boolean writemem() {
      this.MW_START(0, 32767, 1);
      this.MW_ADD('耀', '菿', this.colorram_w, 1);
      this.MW_ADD('萀', '蟿', this.videoram_w, 0, 0);
      this.MW_ADD('蠀', '迿', 0);
      this.MW_ADD('逐', '逿', 0, 2, 1);
      this.MW_ADD('鐐', '鐿', 0, 3);
      this.MW_ADD('ꀀ', 'ꀀ', 2);
      this.MW_ADD('ꆀ', 'ꆀ', this.interrupt_enable_w);
      this.MW_ADD('ꆇ', 'ꆇ', this.pooyan_flipscreen_w);
      return true;
   }

   public Emulator getMachine(URL var1, String var2) {
      super.getMachine(var1, var2);
      super.setVideoEmulator(this.v);
      if(var2.equals("pooyan")) {
         this.GAME(1982, this.rom_pooyan(), 0, this.mdrv_pooyan(), this.ipt_pooyan(), 0, 3, "Konami", "Pooyan");
      } else if(var2.equals("pooyans")) {
         this.GAME(1982, this.rom_pooyans(), "pooyan", this.mdrv_pooyan(), this.ipt_pooyan(), 0, 3, "[Konami] (Stern license)", "Pooyan (Stern)");
      } else if(var2.equals("pootan")) {
         this.GAME(1982, this.rom_pootan(), "pooyan", this.mdrv_pooyan(), this.ipt_pooyan(), 0, 3, "bootleg", "Pootan");
      }

      this.m.init(this.md);
      this.v.setMachine(this.m);
      return this.m;
   }

   public boolean mdrv_pooyan() {
      this.MDRV_CPU_ADD(0, 3072000);
      this.MDRV_CPU_MEMORY(this.readmem(), this.writemem());
      this.MDRV_CPU_VBLANK_INT(this.nmi_line_pulse, 1);
      this.MDRV_FRAMES_PER_SECOND(60);
      this.MDRV_VBLANK_DURATION(0);
      this.MDRV_VIDEO_ATTRIBUTES(1);
      this.MDRV_SCREEN_SIZE(256, 256);
      this.MDRV_VISIBLE_AREA(0, 255, 16, 239);
      this.MDRV_GFXDECODE(this.gfxdecodeinfo());
      this.MDRV_PALETTE_LENGTH(32);
      this.MDRV_COLORTABLE_LENGTH(512);
      this.MDRV_PALETTE_INIT(this.pooyan_pi);
      this.MDRV_VIDEO_START(this.generic_vs);
      this.MDRV_VIDEO_UPDATE(this.pooyan_vu);
      return true;
   }
}
