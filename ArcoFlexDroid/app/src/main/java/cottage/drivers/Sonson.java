package cottage.drivers;

import java.net.URL;

import cottage.mame.Driver;
import cottage.mame.MAMEConstants;
import cottage.mame.MAMEDriver;
import jef.machine.BasicEmulator;
import jef.machine.Emulator;
import jef.map.InterruptHandler;
import jef.map.WriteHandler;
import jef.sound.chip.AY8910;
import jef.video.PaletteInitializer;
import jef.video.VideoInitializer;
import jef.video.VideoRenderer;

public class Sonson extends MAMEDriver implements Driver, MAMEConstants {
   AY8910 ay8910 = new AY8910(2, 1500000);
   int[][] charlayout;
   WriteHandler colorram_w;
   VideoInitializer generic_vs;
   InterruptHandler interrupt;
   InterruptHandler irq0_line_hold;
   BasicEmulator m;
   PaletteInitializer sonson_pi;
   int[] sonson_scrollx;
   VideoRenderer sonson_vu;
   int[][] spritelayout;
   cottage.vidhrdw.Sonson v = new cottage.vidhrdw.Sonson();
   WriteHandler videoram_w;

   public Sonson() {
      this.sonson_scrollx = this.v.Fsonson_scrollx;
      this.sonson_pi = this.v;
      this.sonson_vu = this.v;
      this.generic_vs = this.v;
      this.videoram_w = this.v.writeVRAM();
      this.colorram_w = this.v.writeVRAMColors();
      this.m = new BasicEmulator();
      this.irq0_line_hold = this.m.irq0_line_hold();
      this.interrupt = this.m.irq0_line_hold();
      int[] var5 = new int[]{8};
      int[] var6 = new int[]{8};
      int[] var7 = new int[]{this.RGN_FRAC(1, 2)};
      int var2 = this.RGN_FRAC(0, 2);
      int var1 = this.RGN_FRAC(1, 2);
      int[] var4 = new int[]{0, 1, 2, 3, 4, 5, 6, 7};
      int[] var3 = new int[]{0, 8, 16, 24, 32, 40, 48, 56};
      int[] var8 = new int[]{64};
      this.charlayout = new int[][]{var5, var6, var7, {2}, {var2, var1}, var4, var3, var8};
      var1 = this.RGN_FRAC(1, 3);
      var5 = new int[]{3};
      var6 = new int[]{this.RGN_FRAC(0, 3), this.RGN_FRAC(1, 3), this.RGN_FRAC(2, 3)};
      var3 = new int[]{135, 134, 133, 132, 131, 130, 129, 128, 7, 6, 5, 4, 3, 2, 1, 0};
      var4 = new int[]{0, 8, 16, 24, 32, 40, 48, 56, 64, 72, 80, 88, 96, 104, 112, 120};
      this.spritelayout = new int[][]{{16}, {16}, {var1}, var5, var6, var3, var4, {256}};
   }

   private boolean gfxdecodeinfo() {
      this.GDI_ADD(8, 0, this.charlayout, 0, 64);
      this.GDI_ADD(9, 0, this.spritelayout, 256, 32);
      this.GDI_ADD(-1);
      return true;
   }

   private boolean ipt_sonson() {
      this.PORT_START();
      this.PORT_BIT(1, 255, 12);
      this.PORT_BIT(2, 255, 0);
      this.PORT_BIT(4, 255, 8);
      this.PORT_BIT(8, 255, 9);
      this.PORT_BIT(16, 255, 10);
      this.PORT_BIT(32, 255, 11);
      this.PORT_BIT(64, 255, 0);
      this.PORT_BIT(128, 255, 0);
      this.PORT_START();
      this.PORT_BIT(1, 255, 140);
      this.PORT_BIT(2, 255, 0);
      this.PORT_BIT(4, 255, 136);
      this.PORT_BIT(8, 255, 137);
      this.PORT_BIT(16, 255, 138);
      this.PORT_BIT(32, 255, 139);
      this.PORT_BIT(64, 255, 0);
      this.PORT_BIT(128, 255, 0);
      this.PORT_START();
      this.PORT_BIT(1, 255, 4);
      this.PORT_BIT(2, 255, 5);
      this.PORT_BIT(4, 255, 0);
      this.PORT_BIT(8, 255, 0);
      this.PORT_BIT(16, 255, 1);
      this.PORT_BIT(32, 255, 2);
      this.PORT_BIT(64, 255, 0);
      this.PORT_BIT(128, 255, 0);
      this.PORT_START();
      this.PORT_DIPNAME(15, 15, this.DEF_STR2(5));
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
      this.PORT_DIPNAME(16, 16, "Coinage affects");
      this.PORT_DIPSETTING(16, this.DEF_STR2(39));
      this.PORT_DIPSETTING(0, this.DEF_STR2(40));
      this.PORT_DIPNAME(32, 0, this.DEF_STR2(36));
      this.PORT_DIPSETTING(32, this.DEF_STR2(2));
      this.PORT_DIPSETTING(0, this.DEF_STR2(1));
      this.PORT_SERVICE(64, 255);
      this.PORT_DIPNAME(128, 128, this.DEF_STR2(0));
      this.PORT_DIPSETTING(128, this.DEF_STR2(2));
      this.PORT_DIPSETTING(0, this.DEF_STR2(1));
      this.PORT_START();
      this.PORT_DIPNAME(3, 3, this.DEF_STR2(3));
      this.PORT_DIPSETTING(3, "3");
      this.PORT_DIPSETTING(2, "4");
      this.PORT_DIPSETTING(1, "5");
      this.PORT_DIPSETTING(0, "7");
      this.PORT_DIPNAME(4, 0, "2 Players Game");
      this.PORT_DIPSETTING(4, "1 Credit");
      this.PORT_DIPSETTING(0, "2 Credits");
      this.PORT_DIPNAME(24, 8, this.DEF_STR2(4));
      this.PORT_DIPSETTING(8, "20000 80000 100000");
      this.PORT_DIPSETTING(0, "30000 90000 120000");
      this.PORT_DIPSETTING(24, "20000");
      this.PORT_DIPSETTING(16, "30000");
      this.PORT_DIPNAME(96, 96, this.DEF_STR2(38));
      this.PORT_DIPSETTING(96, "Easy");
      this.PORT_DIPSETTING(64, "Medium");
      this.PORT_DIPSETTING(32, "Hard");
      this.PORT_DIPSETTING(0, "Hardest");
      this.PORT_DIPNAME(128, 128, "Freeze");
      this.PORT_DIPSETTING(128, this.DEF_STR2(2));
      this.PORT_DIPSETTING(0, this.DEF_STR2(1));
      return true;
   }

   private boolean readmem() {
      this.MR_START(0, 6143, 0);
      this.MR_ADD(16384, '\uffff', 1);
      this.MR_ADD(12290, 12290, this.input_port_0_r);
      this.MR_ADD(12291, 12291, this.input_port_1_r);
      this.MR_ADD(12292, 12292, this.input_port_2_r);
      this.MR_ADD(12293, 12293, this.input_port_3_r);
      this.MR_ADD(12294, 12294, this.input_port_4_r);
      return true;
   }

   private boolean rom_sonson() {
      this.ROM_REGION(65536, 0, 0);
      this.ROM_LOAD("ss.01e", 16384, 16384, -851391404);
      this.ROM_LOAD("ss.02e", '耀', 16384, -1018731225);
      this.ROM_LOAD("ss.03e", '쀀', 16384, 533784361);
      this.ROM_REGION(65536, 1, 0);
      this.ROM_LOAD("ss_6.c11", '\ue000', 8192, 288736394);
      this.ROM_REGION(16384, 8, 1);
      this.ROM_LOAD("ss_7.b6", 0, 8192, -1727491919);
      this.ROM_LOAD("ss_8.b5", 8192, 8192, -1819738238);
      this.ROM_REGION('쀀', 9, 1);
      this.ROM_LOAD("ss_9.m5", 0, 8192, -1934505265);
      this.ROM_LOAD("ss_10.m6", 8192, 8192, -134053538);
      this.ROM_LOAD("ss_11.m3", 16384, 8192, 1304090762);
      this.ROM_LOAD("ss_12.m4", 24576, 8192, -1442453881);
      this.ROM_LOAD("ss_13.m1", '耀', 8192, 1712430074);
      this.ROM_LOAD("ss_14.m2", 'ꀀ', 8192, -514919090);
      this.ROM_REGION(832, 16, 0);
      this.ROM_LOAD("ssb4.b2", 0, 32, -924126668);
      this.ROM_LOAD("ssb5.b1", 32, 32, 239291101);
      this.ROM_LOAD("ssb2.c4", 64, 256, -986504762);
      this.ROM_LOAD("ssb3.h7", 320, 256, 2100048458);
      this.ROM_LOAD("ssb1.k11", 576, 256, -1605694210);
      return true;
   }

   private boolean rom_sonsonj() {
      this.ROM_REGION(65536, 0, 0);
      this.ROM_LOAD("ss_0.l9", 16384, 8192, 1885083279);
      this.ROM_LOAD("ss_1.j9", 24576, 8192, 251901309);
      this.ROM_LOAD("ss_2.l8", '耀', 8192, -1572626083);
      this.ROM_LOAD("ss_3.j8", 'ꀀ', 8192, -882612198);
      this.ROM_LOAD("ss_4.l7", '쀀', 8192, 1279169601);
      this.ROM_LOAD("ss_5.j7", '\ue000', 8192, -2072025588);
      this.ROM_REGION(65536, 1, 0);
      this.ROM_LOAD("ss_6.c11", '\ue000', 8192, 288736394);
      this.ROM_REGION(16384, 8, 1);
      this.ROM_LOAD("ss_7.b6", 0, 8192, -1727491919);
      this.ROM_LOAD("ss_8.b5", 8192, 8192, -1819738238);
      this.ROM_REGION('쀀', 9, 1);
      this.ROM_LOAD("ss_9.m5", 0, 8192, -1934505265);
      this.ROM_LOAD("ss_10.m6", 8192, 8192, -134053538);
      this.ROM_LOAD("ss_11.m3", 16384, 8192, 1304090762);
      this.ROM_LOAD("ss_12.m4", 24576, 8192, -1442453881);
      this.ROM_LOAD("ss_13.m1", '耀', 8192, 1712430074);
      this.ROM_LOAD("ss_14.m2", 'ꀀ', 8192, -514919090);
      this.ROM_REGION(832, 16, 0);
      this.ROM_LOAD("ssb4.b2", 0, 32, -924126668);
      this.ROM_LOAD("ssb5.b1", 32, 32, 239291101);
      this.ROM_LOAD("ssb2.c4", 64, 256, -986504762);
      this.ROM_LOAD("ssb3.h7", 320, 256, 2100048458);
      this.ROM_LOAD("ssb1.k11", 576, 256, -1605694210);
      return true;
   }

   private boolean sound_readmem() {
      this.MR_START(0, 2047, 0);
      this.MR_ADD('ꀀ', 'ꀀ', this.soundlatch_r);
      this.MR_ADD('\ue000', '\uffff', 1);
      return true;
   }

   private boolean sound_writemem() {
      this.MW_START(0, 2047, 0);
      this.MW_ADD(8192, 8192, this.ay8910.AY8910_control_port_0_w());
      this.MW_ADD(8193, 8193, this.ay8910.AY8910_write_port_0_w());
      this.MW_ADD(16384, 16384, this.ay8910.AY8910_control_port_1_w());
      this.MW_ADD(16385, 16385, this.ay8910.AY8910_write_port_1_w());
      this.MW_ADD('\ue000', '\uffff', 1);
      return true;
   }

   private boolean writemem() {
      this.MW_START(0, 4095, 0);
      this.MW_ADD(4096, 5119, this.videoram_w, 0, 0);
      this.MW_ADD(5120, 6143, this.colorram_w, 1);
      this.MW_ADD(8224, 8319, 0, 2, 1);
      this.MW_ADD(12288, 12288, 0, this.sonson_scrollx);
      this.MW_ADD(12296, 12296, 2);
      this.MW_ADD(12304, 12304, this.soundlatch_w);
      this.MW_ADD(12312, 12312, 2);
      this.MW_ADD(12313, 12313, new Sonson_sh_irqtrigger_w());
      this.MW_ADD(16384, '\uffff', 1);
      return true;
   }

   public Emulator getMachine(URL var1, String var2) {
      super.getMachine(var1, var2);
      super.setVideoEmulator(this.v);
      this.m = new BasicEmulator();
      if(var2.equals("sonson")) {
         this.GAME(1984, this.rom_sonson(), 0, this.mdrv_sonson(), this.ipt_sonson(), 0, 0, "Capcom", "Son Son");
      } else if(var2.equals("sonsonj")) {
         this.GAME(1984, this.rom_sonsonj(), "sonson", this.mdrv_sonson(), this.ipt_sonson(), 0, 0, "Capcom", "Son Son (Japan)");
      }

      this.m.init(this.md);
      return this.m;
   }

   public boolean mdrv_sonson() {
      this.MDRV_CPU_ADD(2, 2000000);
      this.MDRV_CPU_MEMORY(this.readmem(), this.writemem());
      this.MDRV_CPU_VBLANK_INT(this.irq0_line_hold, 1);
      this.MDRV_CPU_ADD(2, 2000000);
      this.MDRV_CPU_FLAGS(1);
      this.MDRV_CPU_MEMORY(this.sound_readmem(), this.sound_writemem());
      this.MDRV_CPU_VBLANK_INT(this.interrupt, 4);
      this.MDRV_SOUND_ADD(this.ay8910);
      this.MDRV_FRAMES_PER_SECOND(60);
      this.MDRV_VBLANK_DURATION(0);
      this.MDRV_INTERLEAVE(100);
      this.MDRV_VIDEO_ATTRIBUTES(1);
      this.MDRV_SCREEN_SIZE(256, 256);
      this.MDRV_VISIBLE_AREA(8, 247, 8, 247);
      this.MDRV_GFXDECODE(this.gfxdecodeinfo());
      this.MDRV_PALETTE_LENGTH(32);
      this.MDRV_COLORTABLE_LENGTH(512);
      this.MDRV_PALETTE_INIT(this.sonson_pi);
      this.MDRV_VIDEO_START(this.generic_vs);
      this.MDRV_VIDEO_UPDATE(this.sonson_vu);
      return true;
   }

   public class Sonson_sh_irqtrigger_w implements WriteHandler {
      int last;

      public void write(int var1, int var2) {
         if(this.last == 0 && var2 == 1) {
            Sonson.this.cpu_cause_interrupt(1, 2);
         }

         this.last = var2;
      }
   }
}
