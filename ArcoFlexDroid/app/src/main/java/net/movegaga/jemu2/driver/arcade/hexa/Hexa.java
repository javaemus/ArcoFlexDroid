package net.movegaga.jemu2.driver.arcade.hexa;

import net.movegaga.jemu2.driver.BaseDriver;

import java.net.URL;

import jef.cpu.z80.Z80;
import jef.cpuboard.CpuDriver;
import jef.machine.BasicEmulator;
import jef.machine.Emulator;
import jef.machine.EmulatorProperties;
import jef.map.InputPort;
import jef.map.MemoryReadAddress;
import jef.map.MemoryWriteAddress;
import jef.map.WriteHandler;
import jef.sound.chip.AY8910;
import jef.util.ZipLoader;
import jef.video.GfxDecodeInfo;

public class Hexa extends BaseDriver {
   WriteHandler AY8910_control_port_0_w;
   WriteHandler AY8910_write_port_0_w;
   AY8910 ay8910 = new AY8910(1, 1500000);
   int[][] charlayout;
   WriteHandler hexa_d008_w;
   char[] mem_cpu1 = new char[98304];
   char[] mem_gfx1 = new char[98304];
   char[] mem_prom = new char[768];
   VHexa v = new VHexa(this);

   public Hexa() {
      this.hexa_d008_w = this.v.hexa_d008_w();
      this.AY8910_write_port_0_w = this.ay8910.AY8910_write_port_0_w();
      this.AY8910_control_port_0_w = this.ay8910.AY8910_control_port_0_w();
      int[] var4 = new int[]{8};
      int[] var2 = new int[]{0, 262144, 524288};
      int[] var3 = new int[]{0, 1, 2, 3, 4, 5, 6, 7};
      int[] var1 = new int[]{0, 8, 16, 24, 32, 40, 48, 56};
      int[] var5 = new int[]{64};
      this.charlayout = new int[][]{var4, {8}, {4096}, {3}, var2, var3, var1, var5};
   }

   public Emulator createEmulator(URL var1, String var2) {
      super.createEmulator(var1, var2);
      BasicEmulator var6 = new BasicEmulator();
      InputPort[] var3 = new InputPort[]{new InputPort(), null};
      var3[0].setBit(1, 255, 10);
      var3[0].setBit(2, 255, 11);
      var3[0].setBit(4, 255, 8);
      var3[0].setBit(8, 255, 9);
      var3[0].setBit(16, 255, 12);
      var3[0].setBit(32, 255, 4);
      var3[0].setBit(64, 255, 1);
      var3[0].setBit(128, 255, 13);
      var3[1] = new InputPort();
      var3[1].setBits(255, 251);
      MemoryReadAddress var5 = new MemoryReadAddress(this.mem_cpu1);
      MemoryWriteAddress var4 = new MemoryWriteAddress(this.mem_cpu1);
      var5.setMR(0, 32767, 1);
      var5.setMR('耀', '뿿', 0);
      var5.setMR('쀀', '쟿', 0);
      var5.set('퀁', '퀁', var3[0]);
      var5.setMR('\ue000', '\ue7ff', 0);
      var4.setMW(0, '뿿', 1);
      var4.setMW('쀀', '쟿', 0);
      var4.set('퀀', '퀀', this.AY8910_control_port_0_w);
      var4.set('퀁', '퀁', this.AY8910_write_port_0_w);
      var4.set('퀈', '퀈', this.hexa_d008_w);
      var4.set('\ue000', '\ue7ff', this.v);
      EmulatorProperties var7 = new EmulatorProperties();
      var7.setCpuDriver(new CpuDriver(new Z80(), 4000000, var5, var4, var6.irq0_line_hold(), 1));
      var7.setSoundChip(this.ay8910);
      var7.setFPS(60);
      var7.setVideoDimensions(256, 256);
      var7.setVisibleArea(0, 255, 16, 239);
      var7.setGfxDecodeInfo(new GfxDecodeInfo(this.mem_gfx1, 0, this.charlayout, 0, 32));
      var7.setPaletteLength(256);
      var7.setVideoEmulation(this.v);
      var7.setInputPorts(var3);
      ZipLoader var8 = new ZipLoader("hexa");
      var8.queue((char[])this.mem_cpu1, -1733294714, 0, '耀');
      var8.queue((char[])this.mem_cpu1, 1029505132, 65536, '耀');
      var8.queue((char[])this.mem_gfx1, -158261802, 0, '耀');
      var8.queue((char[])this.mem_gfx1, 1849529810, '耀', '耀');
      var8.queue((char[])this.mem_gfx1, -1476047, 65536, '耀');
      var8.queue((char[])this.mem_prom, -2002758220, 0, 256);
      var8.queue((char[])this.mem_prom, 1050495282, 256, 256);
      var8.queue((char[])this.mem_prom, -15387028, 512, 256);
      var8.load();
      char[] var9 = this.mem_cpu1;
      char[] var10 = this.mem_cpu1;
      this.mem_cpu1[294] = 0;
      var10[293] = 0;
      var9[292] = 0;
      var6.init(var7);
      this.v.init(var7);
      return var6;
   }
}
