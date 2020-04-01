package net.movegaga.jemu2.driver.arcade.pingpong;

import java.net.URL;

import cottage.mame.Driver;
import cottage.mame.MAMEConstants;
import cottage.mame.MAMEDriver;
import jef.machine.BasicEmulator;
import jef.machine.Emulator;
import jef.map.InterruptHandler;
import jef.map.WriteHandler;
import jef.sound.chip.SN76496;
import jef.video.PaletteInitializer;
import jef.video.VideoInitializer;
import jef.video.VideoRenderer;

public class Pingpong extends MAMEDriver implements Driver, MAMEConstants {
   static int intenable;
   static int sound_command = 0;
   WriteHandler SN76496_0_w;
   int[][] charlayout;
   WriteHandler coin_w;
   WriteHandler colorram_w;
   VideoInitializer generic_vs;
   InterruptHandler irq0_line_hold;
   BasicEmulator m;
   InterruptHandler pingpong_interrupt;
   PaletteInitializer pingpong_pi;
   VideoRenderer pingpong_vu;
   SN76496 sn = new SN76496(1536000);
   WriteHandler soundlatch_w;
   int[][] spritelayout;
   VPingpong v = new VPingpong();
   WriteHandler videoram_w;

   public Pingpong() {
      this.generic_vs = this.v;
      this.pingpong_vu = this.v;
      this.pingpong_pi = this.v;
      this.colorram_w = this.v.writeVRAMColors();
      this.videoram_w = this.v.writeVRAM();
      this.soundlatch_w = new SoundLatch_w();
      this.SN76496_0_w = new SN76496_write();
      this.m = new BasicEmulator();
      this.irq0_line_hold = this.m.irq0_line_hold();
      this.coin_w = new Coin_w();
      this.pingpong_interrupt = new Pingpong_interrupt();
      int[] var4 = new int[]{8};
      int[] var2 = new int[]{4, 0};
      int[] var1 = new int[]{3, 2, 1, 0, 67, 66, 65, 64};
      int[] var3 = new int[]{0, 8, 16, 24, 32, 40, 48, 56};
      this.charlayout = new int[][]{{8}, var4, {512}, {2}, var2, var1, var3, {128}};
      var4 = new int[]{16};
      int[] var5 = new int[]{16};
      int[] var6 = new int[]{128};
      var2 = new int[]{4, 0};
      var3 = new int[]{195, 194, 193, 192, 131, 130, 129, 128, 67, 66, 65, 64, 3, 2, 1, 0};
      var1 = new int[]{0, 8, 16, 24, 32, 40, 48, 56, 256, 264, 272, 280, 288, 296, 304, 312};
      int[] var7 = new int[]{512};
      this.spritelayout = new int[][]{var4, var5, var6, {2}, var2, var3, var1, var7};
   }

   private boolean gfxdecodeinfo() {
      this.GDI_ADD(8, 0, this.charlayout, 0, 64);
      this.GDI_ADD(9, 0, this.spritelayout, 256, 64);
      this.GDI_ADD(-1);
      return true;
   }

   private boolean ipt_pingpong() {
      this.PORT_START();
      this.PORT_DIPNAME(1, 1, this.DEF_STR2(44));
      this.PORT_DIPSETTING(1, this.DEF_STR2(2));
      this.PORT_DIPSETTING(0, this.DEF_STR2(1));
      this.PORT_DIPNAME(2, 2, this.DEF_STR2(44));
      this.PORT_DIPSETTING(2, this.DEF_STR2(2));
      this.PORT_DIPSETTING(0, this.DEF_STR2(1));
      this.PORT_SERVICE(4, 255);
      this.PORT_BIT(8, 255, 5);
      this.PORT_BIT(16, 255, 4);
      this.PORT_BIT(32, 255, 16);
      this.PORT_BIT(64, 255, 2);
      this.PORT_BIT(128, 255, 1);
      this.PORT_START();
      this.PORT_BIT(1, 255, 141);
      this.PORT_BIT(2, 255, 136);
      this.PORT_BIT(4, 255, 137);
      this.PORT_BIT(8, 255, 140);
      this.PORT_BIT(16, 255, 13);
      this.PORT_BIT(32, 255, 8);
      this.PORT_BIT(64, 255, 9);
      this.PORT_BIT(128, 255, 12);
      this.PORT_START();
      this.PORT_DIPNAME(15, 15, this.DEF_STR2(40));
      this.PORT_DIPSETTING(4, this.DEF_STR2(7));
      this.PORT_DIPSETTING(10, this.DEF_STR2(8));
      this.PORT_DIPSETTING(1, this.DEF_STR2(9));
      this.PORT_DIPSETTING(2, this.DEF_STR2(21));
      this.PORT_DIPSETTING(8, this.DEF_STR2(26));
      this.PORT_DIPSETTING(15, this.DEF_STR2(10));
      this.PORT_DIPSETTING(12, this.DEF_STR2(23));
      this.PORT_DIPSETTING(14, this.DEF_STR2(18));
      this.PORT_DIPSETTING(7, this.DEF_STR2(11));
      this.PORT_DIPSETTING(6, this.DEF_STR2(20));
      this.PORT_DIPSETTING(11, this.DEF_STR2(12));
      this.PORT_DIPSETTING(3, this.DEF_STR2(13));
      this.PORT_DIPSETTING(13, this.DEF_STR2(14));
      this.PORT_DIPSETTING(5, this.DEF_STR2(15));
      this.PORT_DIPSETTING(9, this.DEF_STR2(16));
      this.PORT_DIPSETTING(0, this.DEF_STR2(37));
      this.PORT_DIPNAME(240, 240, this.DEF_STR2(39));
      this.PORT_DIPSETTING(64, this.DEF_STR2(7));
      this.PORT_DIPSETTING(160, this.DEF_STR2(8));
      this.PORT_DIPSETTING(16, this.DEF_STR2(9));
      this.PORT_DIPSETTING(32, this.DEF_STR2(21));
      this.PORT_DIPSETTING(128, this.DEF_STR2(26));
      this.PORT_DIPSETTING(240, this.DEF_STR2(10));
      this.PORT_DIPSETTING(192, this.DEF_STR2(23));
      this.PORT_DIPSETTING(224, this.DEF_STR2(18));
      this.PORT_DIPSETTING(112, this.DEF_STR2(11));
      this.PORT_DIPSETTING(96, this.DEF_STR2(20));
      this.PORT_DIPSETTING(176, this.DEF_STR2(12));
      this.PORT_DIPSETTING(48, this.DEF_STR2(13));
      this.PORT_DIPSETTING(208, this.DEF_STR2(14));
      this.PORT_DIPSETTING(80, this.DEF_STR2(15));
      this.PORT_DIPSETTING(144, this.DEF_STR2(16));
      this.PORT_DIPSETTING(0, this.DEF_STR2(37));
      this.PORT_START();
      this.PORT_DIPNAME(1, 0, this.DEF_STR2(36));
      this.PORT_DIPSETTING(1, this.DEF_STR2(2));
      this.PORT_DIPSETTING(0, this.DEF_STR2(1));
      this.PORT_DIPNAME(6, 6, this.DEF_STR2(38));
      this.PORT_DIPSETTING(6, "Easy");
      this.PORT_DIPSETTING(2, "Normal");
      this.PORT_DIPSETTING(4, "Difficult");
      this.PORT_DIPSETTING(0, "Very Difficult");
      this.PORT_DIPNAME(8, 8, this.DEF_STR2(44));
      this.PORT_DIPSETTING(8, this.DEF_STR2(2));
      this.PORT_DIPSETTING(0, this.DEF_STR2(1));
      this.PORT_DIPNAME(16, 16, this.DEF_STR2(44));
      this.PORT_DIPSETTING(16, this.DEF_STR2(2));
      this.PORT_DIPSETTING(0, this.DEF_STR2(1));
      this.PORT_DIPNAME(32, 32, this.DEF_STR2(44));
      this.PORT_DIPSETTING(32, this.DEF_STR2(2));
      this.PORT_DIPSETTING(0, this.DEF_STR2(1));
      this.PORT_DIPNAME(64, 64, this.DEF_STR2(44));
      this.PORT_DIPSETTING(64, this.DEF_STR2(2));
      this.PORT_DIPSETTING(0, this.DEF_STR2(1));
      this.PORT_DIPNAME(128, 128, this.DEF_STR2(44));
      this.PORT_DIPSETTING(128, this.DEF_STR2(2));
      this.PORT_DIPSETTING(0, this.DEF_STR2(1));
      return true;
   }

   private boolean readmem() {
      this.MR_START(0, 32767, 1);
      this.MR_ADD('耀', '蟿', 0);
      this.MR_ADD('退', '響', 0);
      this.MR_ADD('ꠀ', 'ꠀ', this.input_port_0_r);
      this.MR_ADD('ꢀ', 'ꢀ', this.input_port_1_r);
      this.MR_ADD('꤀', '꤀', this.input_port_2_r);
      this.MR_ADD('ꦀ', 'ꦀ', this.input_port_3_r);
      return true;
   }

   private boolean rom_pingpong() {
      this.ROM_REGION(65536, 0, 0);
      this.ROM_LOAD("pp_e04.rom", 0, 16384, 408235919);
      this.ROM_LOAD("pp_e03.rom", 16384, 16384, -1369505304);
      this.ROM_REGION(8192, 8, 1);
      this.ROM_LOAD("pp_e01.rom", 0, 8192, -774442864);
      this.ROM_REGION(8192, 9, 1);
      this.ROM_LOAD("pp_e02.rom", 0, 8192, 868648928);
      this.ROM_REGION(544, 16, 0);
      this.ROM_LOAD("pingpong.3j", 0, 32, 1040511086);
      this.ROM_LOAD("pingpong.11j", 32, 256, 165243656);
      this.ROM_LOAD("pingpong.5h", 288, 256, -2074737558);
      return true;
   }

   private boolean writemem() {
      this.MW_START(0, 32767, 1);
      this.MW_ADD('耀', '菿', this.colorram_w, 1);
      this.MW_ADD('萀', '蟿', this.videoram_w, 0, 0);
      this.MW_ADD('退', '适', 0);
      this.MW_ADD('逃', '遒', 0, 2, 1);
      this.MW_ADD('道', '響', 0);
      this.MW_ADD('ꀀ', 'ꀀ', this.coin_w);
      this.MW_ADD('ꈀ', 'ꈀ', this.soundlatch_w);
      this.MW_ADD('ꐀ', 'ꐀ', this.SN76496_0_w);
      return true;
   }

   public Emulator getMachine(URL var1, String var2) {
      super.getMachine(var1, var2);
      super.setVideoEmulator(this.v);
      this.m = new BasicEmulator();
      if(var2.equals("pingpong")) {
         this.GAME(1985, this.rom_pingpong(), 0, this.mdrv_pingpong(), this.ipt_pingpong(), 0, 0, "Konami", "Ping Pong");
      }

      this.m.init(this.md);
      return this.m;
   }

   public boolean mdrv_pingpong() {
      this.MDRV_CPU_ADD(0, 3072000);
      this.MDRV_CPU_MEMORY(this.readmem(), this.writemem());
      this.MDRV_CPU_VBLANK_INT(this.pingpong_interrupt, 16);
      this.MDRV_SOUND_ADD(this.sn);
      this.MDRV_FRAMES_PER_SECOND(60);
      this.MDRV_VBLANK_DURATION(0);
      this.MDRV_VIDEO_ATTRIBUTES(1);
      this.MDRV_SCREEN_SIZE(256, 256);
      this.MDRV_VISIBLE_AREA(0, 255, 16, 239);
      this.MDRV_GFXDECODE(this.gfxdecodeinfo());
      this.MDRV_PALETTE_LENGTH(32);
      this.MDRV_COLORTABLE_LENGTH(512);
      this.MDRV_PALETTE_INIT(this.pingpong_pi);
      this.MDRV_VIDEO_START(this.generic_vs);
      this.MDRV_VIDEO_UPDATE(this.pingpong_vu);
      return true;
   }

   public class Coin_w implements WriteHandler {
      public void write(int var1, int var2) {
         Pingpong.intenable = var2 & 12;
      }
   }

   public class Pingpong_interrupt implements InterruptHandler {
      public int irq() {
         byte var1;
         if(Pingpong.this.m.getCurrentSlice() == 0) {
            if((Pingpong.intenable & 4) != 0) {
               var1 = 0;
               return var1;
            }
         } else if(Pingpong.this.m.getCurrentSlice() % 2 != 0 && (Pingpong.intenable & 8) != 0) {
            var1 = 1;
            return var1;
         }

         var1 = -1;
         return var1;
      }
   }

   class SN76496_write implements WriteHandler {
      public void write(int var1, int var2) {
         Pingpong.this.sn.writeCommand(Pingpong.sound_command);
      }
   }

   class SoundLatch_w implements WriteHandler {
      public void write(int var1, int var2) {
         Pingpong.sound_command = var2;
      }
   }
}
