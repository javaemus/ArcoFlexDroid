package net.movegaga.jemu2.driver.arcade.dkong;

import android.media.MediaPlayer;

import net.movegaga.jemu2.driver.BaseDriver;
import net.movegaga.jemu2.driver.Driver;

import java.net.URL;

import jef.cpu.Z80;
import jef.cpuboard.CpuBoard;
import jef.cpuboard.CpuDriver;
import jef.cpuboard.FastCpuBoard;
import jef.machine.BasicEmulator;
import jef.machine.Emulator;
import jef.machine.EmulatorProperties;
import jef.map.InputPort;
import jef.map.MemoryReadAddress;
import jef.map.MemoryWriteAddress;
import jef.map.ReadHandler;
import jef.map.SwitchedInterrupt;
import jef.map.WriteHandler;
import jef.scoring.HighScoreParser;
import jef.util.RomLoader;
import jef.video.GfxDecodeInfo;
import jef.video.PaletteInitializer;
import jef.video.VideoConstants;
import jef.video.VideoRenderer;

public class Dkong extends BaseDriver implements Driver, VideoConstants {
   int[][] charLayout;
   int[][] charLayoutDkongJr;
   Z80 cpu1 = new Z80();
   private MediaPlayer dkstomp;
   private MediaPlayer effect00;
   private MediaPlayer effect01;
   private MediaPlayer effect02;
   BasicEmulator emu;
   final InputPort[] in;
   SwitchedInterrupt interrupt;
   private MediaPlayer jump;
   int mcuStatus;
   char[] mem_cpu1 = new char[65536];
   char[] mem_gfx1;
   char[] mem_gfx2;
   char[] mem_gfx3;
   char[] mem_prom;
   MemoryReadAddress mra1;
   MemoryWriteAddress mwa1;
   PaletteInitializer piDkong3;
   final ReadHandler readInput2;
   private MediaPlayer run01;
   private MediaPlayer run02;
   private MediaPlayer run03;
   int[] sample_order;
   MediaPlayer samples;
   int[][] spriteLayout;
   int[][] spriteLayoutDkong3;
   int[] state;
   VDkong video;
   VideoRenderer vrRadarscp;
   WriteHandler writeGfxBankDkong3;
   WriteHandler writeGfxBankDkongJr;
   WriteHandler writeGridColor;
   WriteHandler writeGridEnable;
   WriteHandler writeInterruptEnable;
   WriteHandler writePaletteBank;
   WriteHandler writeVRAM;

   public Dkong() {
      this.mra1 = new MemoryReadAddress(this.mem_cpu1);
      this.mwa1 = new MemoryWriteAddress(this.mem_cpu1);
      this.interrupt = new SwitchedInterrupt(1);
      this.writeInterruptEnable = this.interrupt;
      this.video = new VDkong(this);
      this.mem_gfx1 = new char[8192];
      this.mem_gfx2 = new char[16384];
      this.mem_gfx3 = new char[2048];
      this.mem_prom = new char[768];
      this.writeVRAM = this.video.writeVRAM();
      this.writePaletteBank = this.video.dkong_palettebank_w();
      this.writeGfxBankDkongJr = this.video.dkongjr_gfxbank_w();
      this.writeGfxBankDkong3 = this.video.dkong3_gfxbank_w();
      this.writeGridColor = this.video.radarscp_grid_color_w();
      this.writeGridEnable = this.video.radarscp_grid_enable_w();
      this.vrRadarscp = this.video.radarscp_vu();
      this.piDkong3 = this.video.dkong3_palette_init();
      this.mcuStatus = 0;
      int[] var1 = new int[]{1, 2, 1, 2, 0, 1, 0};
      this.sample_order = var1;
      this.state = new int[130];
      this.samples = new MediaPlayer();
      this.in = new InputPort[]{new InputPort(), new InputPort(), new InputPort(), new InputPort()};
      this.readInput2 = new ReadIn2();
      this.emu = new BasicEmulator() {
         WriteHandler wrSnd = new WriteHandler() {
            int walk;

            public void write(int var1, int var2) {
               var1 -= 32000;
               /*if(Dkong.this.state[var1] != var2) {
                  if(var2 != 0) {
                     switch(var1) {
                     case 0:
                        switch(this.walk) {
                        case 0:
                           Dkong.this.run01.seekTo(0);
                           Dkong.this.run01.start();
                           break;
                        case 1:
                           Dkong.this.run02.seekTo(0);
                           Dkong.this.run02.start();
                           break;
                        case 2:
                           Dkong.this.run03.seekTo(0);
                           Dkong.this.run03.start();
                        }

                        this.walk = (this.walk + 1) % 3;
                        break;
                     case 1:
                        Dkong.this.jump.seekTo(0);
                        Dkong.this.jump.start();
                        break;
                     case 2:
                        Dkong.this.dkstomp.seekTo(0);
                        Dkong.this.dkstomp.start();
                        break;
                     case 3:
                        Dkong.this.effect01.seekTo(0);
                        Dkong.this.effect01.start();
                        break;
                     case 4:
                        Dkong.this.effect02.seekTo(0);
                        Dkong.this.effect02.start();
                        break;
                     case 5:
                        Dkong.this.effect00.seekTo(0);
                        Dkong.this.effect00.start();
                     }
                  }

                  Dkong.this.state[var1] = var2;
               }*/

            }
         };

         public CpuBoard _createCpuBoard(int var1) {
            return new FastCpuBoard();
         }

         public CpuBoard createCpuBoard(int var1) {
            return new FastCpuBoard() {
               public int read8(int var1) {
                  if(var1 >= 28672 && (var1 < 29696 || var1 >= 30720) && (var1 < '耀' || var1 >= 'ꀀ')) {
                     if(var1 == 31744) {
                        var1 = Dkong.this.in[0].read(0);
                     } else if(var1 == 31872) {
                        var1 = Dkong.this.in[1].read(0);
                     } else if(var1 == 32000) {
                        var1 = Dkong.this.readInput2.read(var1);
                     } else if(var1 == 32128) {
                        var1 = Dkong.this.in[3].read(0);
                     } else {
                        var1 = 0;
                     }
                  } else {
                     var1 = Dkong.this.mem_cpu1[var1];
                  }

                  return var1;
               }

               public void write8(int var1, int var2) {
                  if(var1 >= 24576 && var1 < 29696) {
                     Dkong.this.mem_cpu1[var1] = (char)var2;
                  } else if(var1 >= 29696 && var1 < 30720) {
                     if(Dkong.this.mem_cpu1[var1] != var2) {
                        Dkong.this.mem_cpu1[var1] = (char)var2;
                        Dkong.this.video.dirty[var1 & 1023] = true;
                     }
                  } else if(var1 >= 24576) {
                     if(var1 == 32132) {
                        Dkong.this.writeInterruptEnable.write(var1, var2);
                     } else if(var1 != 32134 && var1 != 32135) {
                        if(var1 >= 32000 && var1 <= 32005) {
                           wrSnd.write(var1, var2);
                        } else {
                           Dkong.this.mem_cpu1[var1] = (char)var2;
                        }
                     } else {
                        Dkong.this.writePaletteBank.write(var1, var2);
                     }
                  }

               }
            };
         }
      };
      int[] var4 = new int[]{8};
      int[] var5 = new int[]{8};
      int[] var6 = new int[]{2};
      int[] var2 = new int[]{0, 16384};
      int[] var3 = new int[]{0, 1, 2, 3, 4, 5, 6, 7};
      var1 = new int[]{0, 8, 16, 24, 32, 40, 48, 56};
      int[] var7 = new int[]{64};
      this.charLayout = new int[][]{var4, var5, {256}, var6, var2, var3, var1, var7};
      var4 = new int[]{512};
      var5 = new int[]{2};
      var3 = new int[]{0, '耀'};
      var1 = new int[]{0, 1, 2, 3, 4, 5, 6, 7};
      var2 = new int[]{0, 8, 16, 24, 32, 40, 48, 56};
      this.charLayoutDkongJr = new int[][]{{8}, {8}, var4, var5, var3, var1, var2, {64}};
      var3 = new int[]{0, '耀'};
      var2 = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 16384, 16385, 16386, 16387, 16388, 16389, 16390, 16391};
      var1 = new int[]{0, 8, 16, 24, 32, 40, 48, 56, 64, 72, 80, 88, 96, 104, 112, 120};
      this.spriteLayout = new int[][]{{16}, {16}, {128}, {2}, var3, var2, var1, {128}};
      var4 = new int[]{256};
      var1 = new int[]{0, 65536};
      var3 = new int[]{0, 1, 2, 3, 4, 5, 6, 7, '耀', '老', '耂', '考', '耄', '者', '耆', '耇'};
      var2 = new int[]{0, 8, 16, 24, 32, 40, 48, 56, 64, 72, 80, 88, 96, 104, 112, 120};
      this.spriteLayoutDkong3 = new int[][]{{16}, {16}, var4, {2}, var1, var3, var2, {128}};
   }

   private GfxDecodeInfo[] gfxDecodeInfoDkong() {
      return new GfxDecodeInfo[]{new GfxDecodeInfo(this.mem_gfx1, 0, this.charLayout, 0, 64), new GfxDecodeInfo(this.mem_gfx2, 0, this.spriteLayout, 0, 64)};
   }

   private GfxDecodeInfo[] gfxDecodeInfoDkong3() {
      return new GfxDecodeInfo[]{new GfxDecodeInfo(this.mem_gfx1, 0, this.charLayoutDkongJr, 0, 64), new GfxDecodeInfo(this.mem_gfx2, 0, this.spriteLayoutDkong3, 0, 64)};
   }

   private GfxDecodeInfo[] gfxDecodeInfoDkongJr() {
      return new GfxDecodeInfo[]{new GfxDecodeInfo(this.mem_gfx1, 0, this.charLayoutDkongJr, 0, 64), new GfxDecodeInfo(this.mem_gfx2, 0, this.spriteLayout, 0, 64)};
   }

   private void initInputDkong() {
      this.in[0] = new InputPort();
      this.in[0].setBit(1, 0, 9);
      this.in[0].setBit(2, 0, 8);
      this.in[0].setBit(4, 0, 10);
      this.in[0].setBit(8, 0, 11);
      this.in[0].setBit(16, 0, 12);
      this.in[0].setBit(32, 0, 0);
      this.in[0].setBit(64, 0, 0);
      this.in[0].setBit(128, 0, 0);
      this.in[1] = new InputPort();
      this.in[1].setBit(1, 0, 9);
      this.in[1].setBit(2, 0, 8);
      this.in[1].setBit(4, 0, 10);
      this.in[1].setBit(8, 0, 11);
      this.in[1].setBit(16, 0, 12);
      this.in[1].setBit(32, 0, 0);
      this.in[1].setBit(64, 0, 0);
      this.in[1].setBit(128, 0, 0);
      this.in[2] = new InputPort();
      this.in[2].setBit(2, 0, 0);
      this.in[2].setBit(4, 0, 4);
      this.in[2].setBit(8, 0, 5);
      this.in[2].setBit(16, 0, 0);
      this.in[2].setBit(32, 0, 0);
      this.in[2].setBit(128, 0, 1);
      this.in[3] = new InputPort();
      this.in[3].setDipName(3, 0, "Lives");
      this.in[3].setDipName(12, 0, "Bonus Life");
      this.in[3].setDipName(112, 0, "Coinage");
      this.in[3].setDipName(128, 128, "Cabinet");
   }

   private void initInputDkong3() {
      this.in[0] = new InputPort();
      this.in[0].setBit(1, 0, 9);
      this.in[0].setBit(2, 0, 8);
      this.in[0].setBit(4, 0, 10);
      this.in[0].setBit(8, 0, 11);
      this.in[0].setBit(16, 0, 12);
      this.in[0].setBit(32, 0, 4);
      this.in[0].setBit(64, 0, 5);
      this.in[0].setBit(128, 0, 2);
      this.in[1] = new InputPort();
      this.in[1].setBit(1, 0, 9);
      this.in[1].setBit(2, 0, 8);
      this.in[1].setBit(4, 0, 10);
      this.in[1].setBit(8, 0, 11);
      this.in[1].setBit(16, 0, 12);
      this.in[1].setBitImpulse(32, 0, 1, 1);
      this.in[1].setBitImpulse(64, 0, 2, 1);
      this.in[1].setBit(128, 0, 0);
      this.in[2] = new InputPort();
      this.in[2].setDipName(7, 0, "Coinage");
      this.in[2].setDipName(128, 0, "Cabinet");
      this.in[3] = new InputPort();
      this.in[2].setDipName(3, 0, "Lives");
      this.in[2].setDipName(12, 0, "Bonus Life");
      this.in[2].setDipName(48, 0, "Additional Bonus");
      this.in[2].setDipName(192, 0, "Difficulty");
   }

   private void initReadMap() {
      this.mra1.setMR(0, 24575, 1);
      this.mra1.setMR(24576, 28671, 0);
      this.mra1.setMR(29696, 30719, 0);
      this.mra1.set(31744, 31744, this.in[0]);
      this.mra1.set(31872, 31872, this.in[1]);
      this.mra1.set(32000, 32000, this.readInput2);
      this.mra1.set(32128, 32128, this.in[3]);
      this.mra1.setMR('耀', '\u9fff', 1);
   }

   private void initReadMapDkong3() {
      this.mra1.setMR(0, 24575, 1);
      this.mra1.setMR(24576, 28671, 0);
      this.mra1.setMR(29696, 30719, 0);
      this.mra1.set(31744, 31744, this.in[0]);
      this.mra1.set(31872, 31872, this.in[1]);
      this.mra1.set(32000, 32000, this.in[2]);
      this.mra1.set(32128, 32128, this.in[3]);
      this.mra1.setMR('耀', '\u9fff', 1);
   }

   private void initWriteMapDkong() {
      this.mwa1.setMW(0, 24575, 1);
      this.mwa1.setMW(24576, 29695, 0);
      this.mwa1.set(29696, 30719, this.writeVRAM);
      this.mwa1.setMW(30720, 30723, 0);
      this.mwa1.setMW(30728, 30728, 0);
      this.mwa1.set(32000, 32005, new WriteHandler() {
         int walk;

         public void write(int var1, int var2) {
            var1 -= 32000;
            if(Dkong.this.state[var1] != var2) {
               if(var2 != 0) {
                  switch(var1) {
                  case 0:
                     switch(this.walk) {
                     case 0:
                        Dkong.this.run01.seekTo(0);
                        Dkong.this.run01.start();
                        break;
                     case 1:
                        Dkong.this.run02.seekTo(0);
                        Dkong.this.run02.start();
                        break;
                     case 2:
                        Dkong.this.run03.seekTo(0);
                        Dkong.this.run03.start();
                     }

                     this.walk = (this.walk + 1) % 3;
                     break;
                  case 1:
                     Dkong.this.jump.seekTo(0);
                     Dkong.this.jump.start();
                     break;
                  case 2:
                     Dkong.this.dkstomp.seekTo(0);
                     Dkong.this.dkstomp.start();
                     break;
                  case 3:
                     Dkong.this.effect01.seekTo(0);
                     Dkong.this.effect01.start();
                     break;
                  case 4:
                     Dkong.this.effect02.seekTo(0);
                     Dkong.this.effect02.start();
                     break;
                  case 5:
                     Dkong.this.effect00.seekTo(0);
                     Dkong.this.effect00.start();
                  }
               }

               Dkong.this.state[var1] = var2;
            }

         }
      });
      this.mwa1.setMW(32129, 32129, 0);
      this.mwa1.setMW(32131, 32131, 0);
      this.mwa1.set(32132, 32132, this.writeInterruptEnable);
      this.mwa1.setMW(32133, 32133, 0);
      this.mwa1.set(32134, 32135, this.writePaletteBank);
   }

   private void initWriteMapDkong3() {
      this.mwa1.setMW(0, 24575, 1);
      this.mwa1.setMW(24576, 26879, 0);
      this.mwa1.set(29696, 30719, this.writeVRAM);
      this.mwa1.set(32385, 32385, this.writeGfxBankDkong3);
      this.mwa1.set(32388, 32388, this.writeInterruptEnable);
      this.mwa1.setMW(32389, 32389, 2);
      this.mwa1.set(32390, 32391, this.writePaletteBank);
      this.mwa1.setMW('耀', '\u9fff', 1);
   }

   private void initWriteMapDkongJr() {
      this.mwa1.setMW(0, 24575, 1);
      this.mwa1.setMW(24576, 26879, 0);
      this.mwa1.set(29696, 30719, this.writeVRAM);
      this.mwa1.setMW(30720, 30723, 0);
      this.mwa1.setMW(30728, 30728, 0);
      this.mwa1.set(31872, 31872, this.writeGfxBankDkongJr);
      this.mwa1.set(32000, 32129, new WriteHandler() {
         int climb;
         int walk;
         boolean walking;

         public void write(int var1, int var2) {
            var1 -= 32000;
            if(Dkong.this.state[var1] != var2) {
               if(var2 != 0 || var1 == 7) {
                  switch(var1) {
                  case 0:
                  case 1:
                  case 2:
                  case 3:
                  case 4:
                  case 5:
                  case 6:
                  case 128:
                  default:
                     break;
                  case 7:
                     boolean var3;
                     if(var2 != 0) {
                        var3 = true;
                     } else {
                        var3 = false;
                     }

                     this.walking = var3;
                  }
               }

               Dkong.this.state[var1] = var2;
            }

         }
      });
      this.mwa1.set(32132, 32132, this.writeInterruptEnable);
      this.mwa1.set(32134, 32135, this.writePaletteBank);
      this.mwa1.setMW('耀', '\u9fff', 1);
   }

   private void initWriteMapRadarscp() {
      this.mwa1.setMW(0, 24575, 1);
      this.mwa1.setMW(24576, 26879, 0);
      this.mwa1.set(29696, 30719, this.writeVRAM);
      this.mwa1.setMW(30720, 30723, 0);
      this.mwa1.setMW(30728, 30728, 0);
      this.mwa1.set(31872, 31872, this.writeGridColor);
      this.mwa1.set(32129, 32129, this.writeGridEnable);
      this.mwa1.setMW(32131, 32131, 0);
      this.mwa1.set(32132, 32132, this.writeInterruptEnable);
      this.mwa1.setMW(32133, 32133, 0);
      this.mwa1.set(32134, 32135, this.writePaletteBank);
   }

   private void loadRomDkong(URL var1) {
      RomLoader var2 = new RomLoader();
      var2.setZip("dkong");
      var2.setMemory(this.mem_cpu1);
      var2.loadROM("c_5et_g.bin", 0, 4096, -1167017845);
      var2.loadROM("c_5ct_g.bin", 4096, 4096, 1589928428);
      var2.loadROM("c_5bt_g.bin", 8192, 4096, 479712036);
      var2.loadROM("c_5at_g.bin", 12288, 4096, -1191159104);
      var2.setMemory(this.mem_gfx1);
      var2.loadROM("v_5h_b.bin", 0, 2048, 315148637);
      var2.loadROM("v_3pt.bin", 2048, 2048, 367642089);
      var2.setMemory(this.mem_gfx2);
      var2.loadROM("l_4m_b.bin", 0, 2048, 1509426509);
      var2.loadROM("l_4n_b.bin", 2048, 2048, 1731086100);
      var2.loadROM("l_4r_b.bin", 4096, 2048, -22390290);
      var2.loadROM("l_4s_b.bin", 6144, 2048, 552791934);
      var2.setMemory(this.mem_prom);
      var2.loadROM("Dkong.2k", 0, 256, 511890293);
      var2.loadROM("Dkong.2j", 256, 256, 716185032);
      var2.loadROM("Dkong.5f", 512, 256, 1150846565);
      var2.loadZip(var1);
   }

   private void loadRomDkong3(URL var1) {
      RomLoader var2 = new RomLoader();
      var2.setZip("dkong3");
      var2.setMemory(this.mem_cpu1);
      var2.loadROM("dk3c.7b", 0, 8192, 953545614);
      var2.loadROM("dk3c.7c", 8192, 8192, -921484423);
      var2.loadROM("dk3c.7d", 16384, 8192, -768726751);
      var2.loadROM("dk3c.7e", '耀', 8192, 1633621175);
      var2.setMemory(this.mem_gfx1);
      var2.loadROM("dk3v.3n", 0, 4096, 1096456647);
      var2.loadROM("dk3v.3p", 4096, 4096, 628379296);
      var2.setMemory(this.mem_gfx2);
      var2.loadROM("dk3v.7c", 0, 4096, -1879435465);
      var2.loadROM("dk3v.7d", 4096, 4096, -1698150778);
      var2.loadROM("dk3v.7e", 8192, 4096, 202044411);
      var2.loadROM("dk3v.7f", 12288, 4096, 1439008354);
      var2.setMemory(this.mem_prom);
      var2.loadROM("dkc1-c.1d", 0, 512, -548094212);
      var2.loadROM("dkc1-c.1c", 256, 512, 1722253120);
      var2.loadROM("dkc1-v.2n", 512, 256, 1357067316);
      var2.loadZip(var1);
   }

   private void loadRomDkongJr(URL var1) {
      RomLoader var2 = new RomLoader();
      var2.setZip("dkongjr");
      var2.setMemory(this.mem_cpu1);
      var2.loadROM("dkj.5b", 0, 4096, -559775400);
      var2.continueROM(12288, 4096);
      var2.loadROM("dkj.5c", 8192, 2048, 1874197238);
      var2.continueROM(18432, 2048);
      var2.continueROM(4096, 2048);
      var2.continueROM(22528, 2048);
      var2.loadROM("dkj.5e", 16384, 2048, -800934232);
      var2.continueROM(10240, 2048);
      var2.continueROM(20480, 2048);
      var2.continueROM(6144, 2048);
      var2.setMemory(this.mem_gfx1);
      var2.loadROM("dkj.3n", 0, 4096, -1924027223);
      var2.loadROM("dkj.3p", 4096, 4096, 1324764069);
      var2.setMemory(this.mem_gfx2);
      var2.loadROM("v_7c.bin", 0, 2048, -595639964);
      var2.loadROM("v_7d.bin", 2048, 2048, 216521974);
      var2.loadROM("v_7e.bin", 4096, 2048, 617742103);
      var2.loadROM("v_7f.bin", 6144, 2048, 260835391);
      var2.setMemory(this.mem_prom);
      var2.loadROM("c-2e.bpr", 0, 256, 1178453933);
      var2.loadROM("c-2f.bpr", 256, 256, 1203372098);
      var2.loadROM("v-2n.bpr", 512, 256, -604928577);
      var2.loadZip(var1);
   }

   private void loadRomRadarscp(URL var1) {
      RomLoader var2 = new RomLoader();
      var2.setZip("radarscp");
      var2.setMemory(this.mem_cpu1);
      var2.loadROM("trs2c5fc", 0, 4096, 1083481613);
      var2.loadROM("trs2c5gc", 4096, 4096, -1347894113);
      var2.loadROM("trs2c5hc", 8192, 4096, 1371022909);
      var2.loadROM("trs2c5kc", 12288, 4096, 520159735);
      var2.setMemory(this.mem_gfx1);
      var2.loadROM("trs2v3gc", 0, 2048, -258657522);
      var2.loadROM("trs2v3hc", 2048, 2048, 363009776);
      var2.setMemory(this.mem_gfx2);
      var2.loadROM("trs2v3dc", 0, 2048, -524612167);
      var2.loadROM("trs2v3cc", 2048, 2048, 1817083309);
      var2.loadROM("trs2v3bc", 4096, 2048, 1876780017);
      var2.loadROM("trs2v3ac", 6144, 2048, -1141495979);
      var2.setMemory(this.mem_gfx3);
      var2.loadROM("trs2v3ec", 0, 2048, 248155499);
      var2.setMemory(this.mem_prom);
      var2.loadROM("rs2-x.xxx", 0, 256, 1415617889);
      var2.loadROM("rs2-c.xxx", 256, 256, 2041042993);
      var2.loadROM("rs2-v.1hc", 512, 256, 461538069);
      var2.loadZip(var1);
   }

   public Emulator createEmulator(URL var1, String var2) {
      System.out.println("VAR2="+var2);
      super.createEmulator(var1, var2);
      EmulatorProperties var3 = null;
      if(var2.equals("dkong")) {
         var3 = this.createMachineDriverDkong(var1);
         new HighScoreParser(this.emu, this.mem_cpu1, 16, 0, 0, 30593, 32, 30433, 200, 1);
      } else if(var2.equals("dkongjr")) {
         var3 = this.createMachineDriverDkongJr(var1);
         new HighScoreParser(this.emu, this.mem_cpu1, 16, 0, 0, 30593, 32, 30433, 200, 1);
      } else if(var2.equals("dkong3")) {
         var3 = this.createMachineDriverDkong3(var1);
         new HighScoreParser(this.emu, this.mem_cpu1, 54, 0, 0, 30528, 32, 30368, 200, 1);
      } else if(var2.equals("radarscp")) {
         var3 = this.mdrv_radarscp(var1);
         this.mem_cpu1[7836] = 195;
         this.mem_cpu1[7837] = 189;
      }

      this.video.init(var3);
      this.emu.init(var3);
      return this.emu;
   }

   public EmulatorProperties createMachineDriverDkong(URL var1) {
      System.out.println("createMachineDriverDkong!!!!!!!!!");
      this.loadRomDkong(var1);
      this.initInputDkong();
      this.initReadMap();
      this.initWriteMapDkong();
      /*TODO*///this.dkstomp = MediaPlayer.create(JEmu2Droid.main.getContext(), 2130968580);
      /*TODO*///this.jump = MediaPlayer.create(JEmu2Droid.main.getContext(), 2130968587);
      /*TODO*///this.run01 = MediaPlayer.create(JEmu2Droid.main.getContext(), 2130968595);
      /*TODO*///this.run02 = MediaPlayer.create(JEmu2Droid.main.getContext(), 2130968596);
      /*TODO*///this.run03 = MediaPlayer.create(JEmu2Droid.main.getContext(), 2130968597);
      /*TODO*///this.effect00 = MediaPlayer.create(JEmu2Droid.main.getContext(), 2130968581);
      /*TODO*///this.effect01 = MediaPlayer.create(JEmu2Droid.main.getContext(), 2130968582);
      /*TODO*///this.effect02 = MediaPlayer.create(JEmu2Droid.main.getContext(), 2130968583);
      CpuDriver var2 = new CpuDriver(this.cpu1, 672000, this.mra1, this.mwa1, this.interrupt, 1);
      EmulatorProperties var3 = new EmulatorProperties();
      var3.setCpuDriver(new CpuDriver[]{var2});
      var3.setFPS(60);
      var3.setVideoWidth(256);
      var3.setVideoHeight(256);
      var3.setVisibleArea(0, 255, 16, 239);
      var3.setGfxDecodeInfo(this.gfxDecodeInfoDkong());
      var3.setPaletteLength(256);
      var3.setColorTableLength(256);
      var3.setPaletteInitializer(this.video);
      var3.setVideoInitializer(this.video);
      var3.setVideoUpdater(this.video);
      var3.setRotation(1);
      var3.setInputPorts(this.in);
      return var3;
   }

   public EmulatorProperties createMachineDriverDkong3(URL var1) {
      this.loadRomDkong3(var1);
      this.initInputDkong3();
      this.initReadMapDkong3();
      this.initWriteMapDkong3();
      CpuDriver var3 = new CpuDriver(this.cpu1, 4000000, this.mra1, this.mwa1, this.interrupt, 1);
      EmulatorProperties var2 = new EmulatorProperties();
      var2.setCpuDriver(new CpuDriver[]{var3});
      var2.setFPS(60);
      var2.setVideoWidth(256);
      var2.setVideoHeight(256);
      var2.setVisibleArea(0, 255, 16, 239);
      var2.setGfxDecodeInfo(this.gfxDecodeInfoDkong3());
      var2.setPaletteLength(256);
      var2.setColorTableLength(256);
      var2.setPaletteInitializer(this.piDkong3);
      var2.setVideoInitializer(this.video);
      var2.setVideoUpdater(this.video);
      var2.setRotation(1);
      var2.setInputPorts(this.in);
      return var2;
   }

   public EmulatorProperties createMachineDriverDkongJr(URL var1) {
      this.loadRomDkongJr(var1);
      this.initInputDkong();
      this.initReadMap();
      this.initWriteMapDkongJr();
      CpuDriver var2 = new CpuDriver(this.cpu1, 3072000, this.mra1, this.mwa1, this.interrupt, 1);
      EmulatorProperties var3 = new EmulatorProperties();
      var3.setCpuDriver(new CpuDriver[]{var2});
      var3.setFPS(60);
      var3.setVideoWidth(256);
      var3.setVideoHeight(256);
      var3.setVisibleArea(0, 255, 16, 239);
      var3.setGfxDecodeInfo(this.gfxDecodeInfoDkongJr());
      var3.setPaletteLength(256);
      var3.setColorTableLength(256);
      var3.setPaletteInitializer(this.video);
      var3.setVideoInitializer(this.video);
      var3.setVideoUpdater(this.video);
      var3.setRotation(1);
      var3.setInputPorts(this.in);
      return var3;
   }

   public EmulatorProperties mdrv_radarscp(URL var1) {
      this.loadRomRadarscp(var1);
      this.initInputDkong();
      this.initReadMap();
      this.initWriteMapRadarscp();
      CpuDriver var3 = new CpuDriver(this.cpu1, 3072000, this.mra1, this.mwa1, this.interrupt, 1);
      EmulatorProperties var2 = new EmulatorProperties();
      var2.setCpuDriver(new CpuDriver[]{var3});
      var2.setFPS(60);
      var2.setVideoWidth(256);
      var2.setVideoHeight(256);
      var2.setVisibleArea(0, 255, 16, 239);
      var2.setGfxDecodeInfo(this.gfxDecodeInfoDkong());
      var2.setPaletteLength(258);
      var2.setColorTableLength(256);
      var2.setPaletteInitializer(this.video);
      var2.setVideoInitializer(this.video);
      var2.setVideoUpdater(this.vrRadarscp);
      var2.setRotation(1);
      var2.setInputPorts(this.in);
      return var2;
   }

   class ReadIn2 implements ReadHandler {
      public int read(int var1) {
         return Dkong.this.in[2].read(var1) | Dkong.this.mcuStatus << 6;
      }
   }
}
