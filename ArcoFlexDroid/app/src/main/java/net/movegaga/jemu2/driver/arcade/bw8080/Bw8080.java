package net.movegaga.jemu2.driver.arcade.bw8080;

import net.movegaga.jemu2.driver.BaseDriver;

import java.net.URL;

import jef.cpu.I8080;
import jef.cpuboard.CpuDriver;
import jef.machine.Emulator;
import jef.machine.EmulatorProperties;
import jef.map.IOReadPort;
import jef.map.IOWritePort;
import jef.map.InputPort;
import jef.map.InterruptHandler;
import jef.map.MemoryReadAddress;
import jef.map.MemoryWriteAddress;
import jef.map.ReadHandler;
import jef.map.WriteHandler;
import jef.util.RomLoader;
import jef.util.ZipLoader;
import jef.video.VideoInitializer;
import jef.video.VideoRenderer;

public class Bw8080 extends BaseDriver {
   char[] REGION_PROMS = new char[2048];
   ReadHandler boothill_shift_data_r;
   WriteHandler c8080bw_shift_amount_w;
   ReadHandler c8080bw_shift_data_r;
   WriteHandler c8080bw_shift_data_w;
   WriteHandler c8080bw_videoram_w;
   InputPort[] in = new InputPort[4];
   ReadHandler input_port_0_r;
   ReadHandler input_port_1_r;
   ReadHandler input_port_2_r;
   ReadHandler input_port_3_r;
   InterruptHandler invaders_interrupt;
   WriteHandler invaders_sh_port3_w;
   WriteHandler invaders_sh_port5_w;
   ReadHandler invaders_shift_data_rev_r;
   VideoRenderer invaders_vh_screenrefresh;
   VideoInitializer invaders_vh_start;
   MBw8080 m;
   char[] memCpu1 = new char[65536];
   VBw8080 v = new VBw8080();

   public Bw8080() {
      this.c8080bw_videoram_w = this.v.c8080bw_videoram_w(this.memCpu1, this.v);
      this.invaders_vh_screenrefresh = this.v;
      this.invaders_vh_start = this.v;
      this.m = new MBw8080();
      this.c8080bw_shift_amount_w = this.m.c8080bw_shift_amount_w(this.m);
      this.c8080bw_shift_data_w = this.m.c8080bw_shift_data_w(this.m);
      this.invaders_sh_port3_w = this.m.invaders_sh_port3_w(this.m);
      this.invaders_sh_port5_w = this.m.invaders_sh_port5_w(this.m);
      this.c8080bw_shift_data_r = this.m.c8080bw_shift_data_r(this.m);
      this.invaders_shift_data_rev_r = this.m.invaders_shift_data_rev_r(this.m);
      this.boothill_shift_data_r = this.m.boothill_shift_data_r(this.m);
      this.invaders_interrupt = this.m.invaders_interrupt(this.m);
   }

   private MemoryReadAddress c8080bw_readmem() {
      MemoryReadAddress var1 = new MemoryReadAddress(this.memCpu1);
      var1.setMR(0, 8191, 1);
      var1.setMR(8192, 16383, 0);
      var1.setMR(16384, 25599, 1);
      return var1;
   }

   private IOReadPort c8080bw_readport() {
      IOReadPort var1 = new IOReadPort();
      var1.set(0, 0, this.input_port_0_r);
      var1.set(1, 1, this.input_port_1_r);
      var1.set(2, 2, this.input_port_2_r);
      var1.set(3, 3, this.c8080bw_shift_data_r);
      return var1;
   }

   private MemoryWriteAddress c8080bw_writemem() {
      MemoryWriteAddress var1 = new MemoryWriteAddress(this.memCpu1);
      var1.setMW(0, 8191, 1);
      var1.setMW(8192, 9215, 0);
      var1.set(9216, 16383, this.c8080bw_videoram_w);
      var1.setMW(16384, 25599, 1);
      return var1;
   }

   private IOReadPort checkmat_readport() {
      IOReadPort var1 = new IOReadPort();
      var1.set(0, 0, this.input_port_0_r);
      var1.set(1, 1, this.input_port_1_r);
      var1.set(2, 2, this.input_port_2_r);
      var1.set(3, 3, this.input_port_3_r);
      return var1;
   }

   private IOWritePort checkmat_writeport() {
      return new IOWritePort();
   }

   private InputPort[] ipt_280zzzap() {
      this.in[0].setBit(1, 0, 0);
      this.in[0].setBit(2, 0, 0);
      this.in[0].setBit(4, 0, 12);
      this.in[0].setBit(8, 0, 13);
      this.in[0].setBit(16, 0, 14);
      this.in[0].setBit(32, 0, 0);
      this.in[0].setBit(64, 255, 1);
      this.in[0].setBit(128, 255, 4);
      this.in[1].setAnalog(255, 127, 257, 100, 10, 1, 254);
      this.in[2].setBits(255, 0);
      return this.in;
   }

   private InputPort[] ipt_checkmat() {
      this.in[0].setBit(1, 0, 10);
      this.in[0].setBit(2, 0, 11);
      this.in[0].setBit(4, 0, 8);
      this.in[0].setBit(8, 0, 9);
      this.in[0].setBit(16, 0, 138);
      this.in[0].setBit(32, 0, 139);
      this.in[0].setBit(64, 0, 136);
      this.in[0].setBit(128, 0, 137);
      this.in[2].setBits(255, 0);
      this.in[3].setBit(1, 0, 4);
      this.in[3].setBit(2, 0, 5);
      this.in[3].setBit(4, 0, 6);
      this.in[3].setBit(16, 0, 0);
      this.in[3].setBit(32, 0, 0);
      this.in[3].setBit(64, 0, 0);
      this.in[3].setBit(128, 0, 1);
      return this.in;
   }

   private InputPort[] ipt_invaders() {
      this.in[0].setBit(1, 0, 0);
      this.in[0].setBit(2, 0, 0);
      this.in[0].setBit(4, 0, 0);
      this.in[0].setBit(8, 0, 0);
      this.in[0].setBit(16, 0, 0);
      this.in[0].setBit(32, 0, 0);
      this.in[0].setBit(64, 0, 0);
      this.in[0].setBit(128, 0, 0);
      this.in[1].setBit(1, 255, 1);
      this.in[1].setBit(2, 0, 5);
      this.in[1].setBit(4, 0, 4);
      this.in[1].setBit(8, 0, 0);
      this.in[1].setBit(16, 0, 12);
      this.in[1].setBit(32, 0, 8);
      this.in[1].setBit(64, 0, 9);
      this.in[1].setBit(128, 255, 0);
      this.in[2].setBits(255, 0);
      return this.in;
   }

   private InputPort[] ipt_maze() {
      this.in[0].setBit(1, 0, 8);
      this.in[0].setBit(2, 0, 9);
      this.in[0].setBit(4, 0, 11);
      this.in[0].setBit(8, 0, 10);
      this.in[0].setBit(16, 0, 136);
      this.in[0].setBit(32, 0, 137);
      this.in[0].setBit(64, 0, 139);
      this.in[0].setBit(128, 0, 138);
      this.in[1].setBit(1, 0, 4);
      this.in[1].setBit(2, 0, 5);
      this.in[1].setBit(4, 0, 0);
      this.in[1].setBit(8, 0, 1);
      this.in[1].setBit(16, 0, 0);
      this.in[1].setBit(32, 0, 0);
      this.in[1].setBit(64, 0, 0);
      this.in[1].setService(128, 0);
      this.in[2].setBits(255, 0);
      return this.in;
   }

   private boolean rom_280zzzap(URL var1) {
      RomLoader var2 = new RomLoader();
      var2.setZip("280zzzap");
      var2.setMemory(this.memCpu1);
      var2.loadROM("zzzaph", 0, 1024, 531131932);
      var2.loadROM("zzzapg", 1024, 1024, -1774601109);
      var2.loadROM("zzzapf", 2048, 1024, -1379471903);
      var2.loadROM("zzzape", 3072, 1024, 1193579478);
      var2.loadROM("zzzapd", 4096, 1024, 1277431521);
      var2.loadROM("zzzapc", 5120, 1024, 1854254767);
      var2.loadZip(var1);
      return true;
   }

   private boolean rom_checkmat(URL var1) {
      RomLoader var2 = new RomLoader();
      var2.setZip("checkmat");
      var2.setMemory(this.memCpu1);
      var2.loadROM("checkmat.h", 0, 1024, 880912081);
      var2.loadROM("checkmat.g", 1024, 1024, -547379887);
      var2.loadROM("checkmat.f", 2048, 1024, 626549766);
      var2.loadROM("checkmat.e", 3072, 1024, 1496518020);
      var2.loadZip(var1);
      return true;
   }

   private boolean rom_invaders(URL var1) {
      ZipLoader var2 = new ZipLoader(2130968585);
      var2.queue((char[])this.memCpu1, 1934580440, 0, 2048);
      var2.queue((char[])this.memCpu1, 1811597898, 2048, 2048);
      var2.queue((char[])this.memCpu1, 214871446, 4096, 2048);
      var2.queue((char[])this.memCpu1, 350566576, 6144, 2048);
      var2.load();
      return true;
   }

   private boolean rom_maze(URL var1) {
      RomLoader var2 = new RomLoader();
      var2.setZip("maze");
      var2.setMemory(this.memCpu1);
      var2.loadROM("invaders.h", 0, 2048, -226095873);
      var2.loadROM("invaders.g", 2048, 2048, 1710938169);
      var2.loadZip(var1);
      return true;
   }

   private IOWritePort writeport_2_4() {
      IOWritePort var1 = new IOWritePort();
      var1.set(2, 2, this.c8080bw_shift_amount_w);
      var1.set(3, 3, this.invaders_sh_port3_w);
      var1.set(4, 4, this.c8080bw_shift_data_w);
      var1.set(5, 5, this.invaders_sh_port5_w);
      return var1;
   }

   private IOWritePort writeport_4_3() {
      IOWritePort var1 = new IOWritePort();
      var1.set(3, 3, this.c8080bw_shift_data_w);
      var1.set(4, 4, this.c8080bw_shift_amount_w);
      return var1;
   }

   public Emulator createEmulator(URL var1, String var2) {
      super.createEmulator(var1, var2);
      this.in[0] = new InputPort();
      this.in[1] = new InputPort();
      this.in[2] = new InputPort();
      this.in[3] = new InputPort();
      this.input_port_0_r = this.in[0];
      this.input_port_1_r = this.in[1];
      this.input_port_2_r = this.in[2];
      this.input_port_3_r = this.in[3];
      EmulatorProperties var3 = null;
      if(var2.equals("280zzzap")) {
         var3 = this.machine_driver_280zzzap();
         this.rom_280zzzap(var1);
         this.ipt_280zzzap();
         this.v.bw8080();
      } else if(var2.equals("maze")) {
         var3 = this.machine_driver_maze();
         this.rom_maze(var1);
         this.ipt_maze();
         this.v.bw8080();
      } else if(var2.equals("checkmat")) {
         var3 = this.machine_driver_checkmat();
         this.rom_checkmat(var1);
         this.ipt_checkmat();
         this.v.bw8080();
      } else if(var2.equals("invaders")) {
         var3 = this.machine_driver_invaders();
         this.rom_invaders(var1);
         this.ipt_invaders();
         this.v.invaders();
      }

      this.m.init(var3);
      this.v.init(var3);
      return this.m;
   }

   public EmulatorProperties machine_driver_280zzzap() {
      EmulatorProperties var1 = new EmulatorProperties();
      var1.setCpuDriver(new CpuDriver(new I8080(), 2000000, this.c8080bw_readmem(), this.c8080bw_writemem(), this.c8080bw_readport(), this.writeport_4_3(), this.invaders_interrupt, 2));
      var1.setFPS(60);
      var1.setVideoDimensions(256, 256);
      var1.setVisibleArea(0, 255, 0, 223);
      var1.setPaletteLength(256);
      var1.setColorTableLength(0);
      var1.setVideoInitializer(this.invaders_vh_start);
      var1.setVideoUpdater(this.invaders_vh_screenrefresh);
      var1.setRotation(0);
      var1.setInputPorts(this.in);
      return var1;
   }

   public EmulatorProperties machine_driver_checkmat() {
      EmulatorProperties var1 = new EmulatorProperties();
      var1.setCpuDriver(new CpuDriver(new I8080(), 2000000, this.c8080bw_readmem(), this.c8080bw_writemem(), this.checkmat_readport(), this.checkmat_writeport(), this.invaders_interrupt, 2));
      var1.setFPS(60);
      var1.setVideoDimensions(256, 256);
      var1.setVisibleArea(0, 255, 0, 223);
      var1.setPaletteLength(256);
      var1.setColorTableLength(0);
      var1.setVideoInitializer(this.invaders_vh_start);
      var1.setVideoUpdater(this.invaders_vh_screenrefresh);
      var1.setRotation(0);
      var1.setInputPorts(this.in);
      return var1;
   }

   public EmulatorProperties machine_driver_invaders() {
      EmulatorProperties var1 = new EmulatorProperties();
      var1.setCpuDriver(new CpuDriver(new I8080(), 2000000, this.c8080bw_readmem(), this.c8080bw_writemem(), this.c8080bw_readport(), this.writeport_2_4(), this.invaders_interrupt, 2));
      var1.setFPS(60);
      var1.setVideoDimensions(256, 256);
      var1.setVisibleArea(0, 255, 0, 223);
      var1.setPaletteLength(256);
      var1.setColorTableLength(0);
      var1.setVideoInitializer(this.invaders_vh_start);
      var1.setVideoUpdater(this.invaders_vh_screenrefresh);
      var1.setRotation(3);
      var1.setInputPorts(this.in);
      return var1;
   }

   public EmulatorProperties machine_driver_maze() {
      EmulatorProperties var1 = new EmulatorProperties();
      var1.setCpuDriver(new CpuDriver(new I8080(), 2000000, this.c8080bw_readmem(), this.c8080bw_writemem(), this.c8080bw_readport(), this.writeport_2_4(), this.invaders_interrupt, 2));
      var1.setFPS(60);
      var1.setVideoDimensions(256, 256);
      var1.setVisibleArea(0, 255, 0, 223);
      var1.setPaletteLength(256);
      var1.setColorTableLength(0);
      var1.setVideoInitializer(this.invaders_vh_start);
      var1.setVideoUpdater(this.invaders_vh_screenrefresh);
      var1.setRotation(0);
      var1.setInputPorts(this.in);
      return var1;
   }
}
