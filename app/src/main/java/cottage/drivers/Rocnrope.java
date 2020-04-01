package cottage.drivers;

import net.movegaga.jemu2.driver.arcade.Konami;

import java.net.URL;

import cottage.mame.Driver;
import cottage.mame.MAMEConstants;
import cottage.mame.MAMEDriver;
import jef.machine.Emulator;
import jef.map.InitHandler;
import jef.map.InterruptHandler;
import jef.map.WriteHandler;
import jef.video.PaletteInitializer;
import jef.video.VideoRenderer;

public class Rocnrope extends MAMEDriver implements Driver, MAMEConstants {
   int[][] charlayout;
   WriteHandler colorram_w;
   WriteHandler interrupt_enable_w;
   InterruptHandler irq0_line_hold;
   Konami m;
   WriteHandler rocnrope_interrupt_vector_w;
   PaletteInitializer rocnrope_pi;
   VideoRenderer rocnrope_vu;
   int[][] spritelayout;
   cottage.vidhrdw.Rocnrope v = new cottage.vidhrdw.Rocnrope();
   WriteHandler videoram_w;

   public Rocnrope() {
      this.videoram_w = this.v.writeVRAM();
      this.colorram_w = this.v.writeVRAMColors();
      this.rocnrope_vu = this.v;
      this.rocnrope_pi = this.v;
      this.m = new Konami();
      this.irq0_line_hold = this.m.irq0_line_hold();
      this.interrupt_enable_w = this.m.interrupt_enable();
      this.rocnrope_interrupt_vector_w = new Rocnrope_interrupt_vector_w();
      int[] var4 = new int[]{512};
      int[] var2 = new int[]{4, 0, 65540, 65536};
      int[] var3 = new int[]{0, 1, 2, 3, 64, 65, 66, 67};
      int[] var1 = new int[]{0, 8, 16, 24, 32, 40, 48, 56};
      int[] var5 = new int[]{128};
      this.charlayout = new int[][]{{8}, {8}, var4, {4}, var2, var3, var1, var5};
      var4 = new int[]{16};
      var5 = new int[]{16};
      var3 = new int[]{4, 0, 131076, 131072};
      var2 = new int[]{0, 1, 2, 3, 64, 65, 66, 67, 128, 129, 130, 131, 192, 193, 194, 195};
      var1 = new int[]{0, 8, 16, 24, 32, 40, 48, 56, 256, 264, 272, 280, 288, 296, 304, 312};
      int[] var6 = new int[]{512};
      this.spritelayout = new int[][]{var4, var5, {256}, {4}, var3, var2, var1, var6};
   }

   private boolean gfxdecodeinfo() {
      this.GDI_ADD(8, 0, this.charlayout, 0, 16);
      this.GDI_ADD(9, 0, this.spritelayout, 256, 16);
      this.GDI_ADD(-1);
      return true;
   }

   private boolean ipt_rocnrope() {
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
      this.PORT_BIT(1, 255, 8);
      this.PORT_BIT(2, 255, 9);
      this.PORT_BIT(4, 255, 10);
      this.PORT_BIT(8, 255, 11);
      this.PORT_BIT(16, 255, 12);
      this.PORT_BIT(32, 255, 13);
      this.PORT_BIT(64, 255, 0);
      this.PORT_BIT(128, 255, 0);
      this.PORT_START();
      this.PORT_BIT(1, 255, 136);
      this.PORT_BIT(2, 255, 137);
      this.PORT_BIT(4, 255, 138);
      this.PORT_BIT(8, 255, 139);
      this.PORT_BIT(16, 255, 140);
      this.PORT_BIT(32, 255, 141);
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
      this.PORT_DIPSETTING(0, "Disabled");
      this.PORT_START();
      this.PORT_DIPNAME(3, 3, this.DEF_STR2(3));
      this.PORT_DIPSETTING(3, "3");
      this.PORT_DIPSETTING(2, "4");
      this.PORT_DIPSETTING(1, "5");
      this.PORT_DIPNAME(4, 0, this.DEF_STR2(33));
      this.PORT_DIPSETTING(0, this.DEF_STR2(34));
      this.PORT_DIPSETTING(4, this.DEF_STR2(35));
      this.PORT_DIPNAME(120, 120, this.DEF_STR2(38));
      this.PORT_DIPSETTING(120, "Easy 1");
      this.PORT_DIPSETTING(112, "Easy 2");
      this.PORT_DIPSETTING(104, "Easy 3");
      this.PORT_DIPSETTING(96, "Easy 4");
      this.PORT_DIPSETTING(88, "Normal 1");
      this.PORT_DIPSETTING(80, "Normal 2");
      this.PORT_DIPSETTING(72, "Normal 3");
      this.PORT_DIPSETTING(64, "Normal 4");
      this.PORT_DIPSETTING(56, "Normal 5");
      this.PORT_DIPSETTING(48, "Normal 6");
      this.PORT_DIPSETTING(40, "Normal 7");
      this.PORT_DIPSETTING(32, "Normal 8");
      this.PORT_DIPSETTING(24, "Difficult 1");
      this.PORT_DIPSETTING(16, "Difficult 2");
      this.PORT_DIPSETTING(8, "Difficult 3");
      this.PORT_DIPSETTING(0, "Difficult 4");
      this.PORT_DIPNAME(128, 0, this.DEF_STR2(36));
      this.PORT_DIPSETTING(128, this.DEF_STR2(2));
      this.PORT_DIPSETTING(0, this.DEF_STR2(1));
      this.PORT_START();
      this.PORT_DIPNAME(7, 7, "First Bonus");
      this.PORT_DIPSETTING(7, "20000");
      this.PORT_DIPSETTING(5, "30000");
      this.PORT_DIPSETTING(4, "40000");
      this.PORT_DIPSETTING(3, "50000");
      this.PORT_DIPSETTING(2, "60000");
      this.PORT_DIPSETTING(1, "70000");
      this.PORT_DIPSETTING(0, "80000");
      this.PORT_DIPNAME(56, 56, "Repeated Bonus");
      this.PORT_DIPSETTING(56, "40000");
      this.PORT_DIPSETTING(24, "50000");
      this.PORT_DIPSETTING(16, "60000");
      this.PORT_DIPSETTING(8, "70000");
      this.PORT_DIPSETTING(0, "80000");
      this.PORT_DIPNAME(64, 0, "Grant Repeated Bonus");
      this.PORT_DIPSETTING(64, this.DEF_STR2(43));
      this.PORT_DIPSETTING(0, this.DEF_STR2(42));
      this.PORT_DIPNAME(128, 0, "Unknown DSW 8");
      this.PORT_DIPSETTING(128, this.DEF_STR2(2));
      this.PORT_DIPSETTING(0, this.DEF_STR2(1));
      return true;
   }

   private boolean readmem() {
      this.MR_START(12416, 12416, this.input_port_0_r);
      this.MR_ADD(12417, 12417, this.input_port_1_r);
      this.MR_ADD(12418, 12418, this.input_port_2_r);
      this.MR_ADD(12419, 12419, this.input_port_3_r);
      this.MR_ADD(12288, 12288, this.input_port_4_r);
      this.MR_ADD(12544, 12544, this.input_port_5_r);
      this.MR_ADD(16384, 24575, 0);
      this.MR_ADD(24576, '\uffff', 1);
      return true;
   }

   private boolean rom_rocnrope() {
      this.ROM_REGION(131072, 0, 0);
      this.ROM_LOAD("rr1.1h", 24576, 8192, -2096549580);
      this.ROM_LOAD("rr2.2h", '耀', 8192, 1974437527);
      this.ROM_LOAD("rr3.3h", 'ꀀ', 8192, -1307348303);
      this.ROM_LOAD("rr4.4h", '쀀', 8192, 2060134917);
      this.ROM_LOAD("rnr_h5.vid", '\ue000', 8192, 353002084);
      this.ROM_REGION(65536, 1, 0);
      this.ROM_LOAD("rnr_7a.snd", 0, 4096, 1976747234);
      this.ROM_LOAD("rnr_8a.snd", 4096, 4096, -901569106);
      this.ROM_REGION(16384, 8, 1);
      this.ROM_LOAD("rnr_h12.vid", 0, 8192, -502184647);
      this.ROM_LOAD("rnr_h11.vid", 8192, 8192, 379227967);
      this.ROM_REGION('耀', 9, 1);
      this.ROM_LOAD("rnr_a11.vid", 0, 8192, -1344619938);
      this.ROM_LOAD("rnr_a12.vid", 8192, 8192, 88911851);
      this.ROM_LOAD("rnr_a9.vid", 16384, 8192, -1658755406);
      this.ROM_LOAD("rnr_a10.vid", 24576, 8192, -1342774737);
      this.ROM_REGION(544, 16, 0);
      this.ROM_LOAD("a17_prom.bin", 0, 32, 581774398);
      this.ROM_LOAD("b16_prom.bin", 32, 256, 1963628151);
      this.ROM_LOAD("rocnrope.pr3", 288, 256, -1245226457);
      return true;
   }

   private boolean rom_rocnropk() {
      this.ROM_REGION(131072, 0, 0);
      this.ROM_LOAD("rnr_h1.vid", 24576, 8192, 266191350);
      this.ROM_LOAD("rnr_h2.vid", '耀', 8192, -828525414);
      this.ROM_LOAD("rnr_h3.vid", 'ꀀ', 8192, 1831306329);
      this.ROM_LOAD("rnr_h4.vid", '쀀', 8192, -1691459798);
      this.ROM_LOAD("rnr_h5.vid", '\ue000', 8192, 353002084);
      this.ROM_REGION(65536, 1, 0);
      this.ROM_LOAD("rnr_7a.snd", 0, 4096, 1976747234);
      this.ROM_LOAD("rnr_8a.snd", 4096, 4096, -901569106);
      this.ROM_REGION(16384, 8, 1);
      this.ROM_LOAD("rnr_h12.vid", 0, 8192, -502184647);
      this.ROM_LOAD("rnr_h11.vid", 8192, 8192, 379227967);
      this.ROM_REGION('耀', 9, 1);
      this.ROM_LOAD("rnr_a11.vid", 0, 8192, -1344619938);
      this.ROM_LOAD("rnr_a12.vid", 8192, 8192, 88911851);
      this.ROM_LOAD("rnr_a9.vid", 16384, 8192, -1658755406);
      this.ROM_LOAD("rnr_a10.vid", 24576, 8192, -1342774737);
      this.ROM_REGION(544, 16, 0);
      this.ROM_LOAD("a17_prom.bin", 0, 32, 581774398);
      this.ROM_LOAD("b16_prom.bin", 32, 256, 1963628151);
      this.ROM_LOAD("rocnrope.pr3", 288, 256, -1245226457);
      return true;
   }

   private boolean writemem() {
      this.MW_START(16384, 16447, 0, 3);
      this.MW_ADD(16448, 17407, 0);
      this.MW_ADD(17408, 17471, 0, 2, 1);
      this.MW_ADD(17472, 18431, 0);
      this.MW_ADD(18432, 19455, this.colorram_w, 1);
      this.MW_ADD(19456, 20479, this.videoram_w, 0, 0);
      this.MW_ADD(20480, 24575, 0);
      this.MW_ADD(24576, '\uffff', 1);
      this.MW_ADD('肂', '肂', 2);
      this.MW_ADD('肃', '肃', 2);
      this.MW_ADD('肄', '肄', 2);
      this.MW_ADD('肇', '肇', this.interrupt_enable_w);
      this.MW_ADD('膂', '膍', this.rocnrope_interrupt_vector_w);
      return true;
   }

   public Emulator getMachine(URL var1, String var2) {
      super.getMachine(var1, var2);
      super.setVideoEmulator(this.v);
      if(var2.equals("rocnrope")) {
         this.GAME(1983, this.rom_rocnrope(), 0, this.mdrv_rocnrope(), this.ipt_rocnrope(), this.init_rocnrope(), 3, "Konami", "Roc\'n Rope");
      } else if(var2.equals("rocnropk")) {
         this.GAME(1983, this.rom_rocnropk(), "rocnrope", this.mdrv_rocnrope(), this.ipt_rocnrope(), this.init_rocnropk(), 3, "Konami + Kosuka", "Roc\'n Rope (Kosuka)");
      }

      this.m.init(this.md);
      return this.m;
   }

   public InitHandler init_rocnrope() {
      return new Init_rocnrope();
   }

   public InitHandler init_rocnropk() {
      return new Init_rocnropk();
   }

   public boolean mdrv_rocnrope() {
      this.MDRV_CPU_ADD(2, 2048000);
      this.MDRV_CPU_MEMORY(this.readmem(), this.writemem());
      this.MDRV_CPU_VBLANK_INT(this.irq0_line_hold, 1);
      this.MDRV_FRAMES_PER_SECOND(60);
      this.MDRV_VBLANK_DURATION(0);
      this.MDRV_VIDEO_ATTRIBUTES(1);
      this.MDRV_SCREEN_SIZE(256, 256);
      this.MDRV_VISIBLE_AREA(0, 255, 16, 239);
      this.MDRV_GFXDECODE(this.gfxdecodeinfo());
      this.MDRV_PALETTE_LENGTH(32);
      this.MDRV_COLORTABLE_LENGTH(512);
      this.MDRV_PALETTE_INIT(this.rocnrope_pi);
      this.MDRV_VIDEO_UPDATE(this.rocnrope_vu);
      return true;
   }

   public class Init_rocnrope implements InitHandler {
      public void init() {
         char[] var2 = Rocnrope.this.memory_region(0);
         int var1 = Rocnrope.this.memory_region_length(0) / 2;
         Rocnrope.this.m.konami1_decode(var2);
         var2[var1 + 28733] = 152;
      }
   }

   public class Init_rocnropk implements InitHandler {
      public void init() {
         Rocnrope.this.m.konami1_decode(Rocnrope.this.memory_region(0));
      }
   }

   class Rocnrope_interrupt_vector_w implements WriteHandler {
      public void write(int var1, int var2) {
         Rocnrope.this.memory_region(0)['\ufff2' + var1 - '膂'] = (char)var2;
      }
   }
}
