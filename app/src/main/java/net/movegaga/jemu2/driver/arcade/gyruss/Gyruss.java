package net.movegaga.jemu2.driver.arcade.gyruss;

import net.movegaga.jemu2.driver.BaseDriver;

import java.net.URL;

import jef.cpu.Z80;
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
import jef.video.Eof_callback;
import jef.video.GfxDecodeInfo;
import jef.video.GfxLayout;
import jef.video.PaletteInitializer;
import jef.video.VideoRenderer;

public class Gyruss extends BaseDriver {
   char[] REGION_CPU4 = new char[131072];
   char[] REGION_GFX1 = new char[8192];
   char[] REGION_GFX2 = new char['耀'];
   char[] REGION_PROMS = new char[544];
   WriteHandler colorram_w;
   WriteHandler gyruss_queuereg_w;
   WriteHandler gyruss_spritebank;
   PaletteInitializer gyruss_vh_convert_color_prom;
   VideoRenderer gyruss_vh_screenrefresh;
   InputPort[] in = new InputPort[6];
   ReadHandler input_port_0_r;
   ReadHandler input_port_1_r;
   ReadHandler input_port_2_r;
   ReadHandler input_port_3_r;
   ReadHandler input_port_4_r;
   ReadHandler input_port_5_r;
   WriteHandler interrupt_enable_w;
   MGyruss m;
   char[] memCpu1 = new char[65536];
   InterruptHandler nmi_interrupt;
   Eof_callback noCallback;
   VGyruss v = new VGyruss();
   WriteHandler videoram_w;

   public Gyruss() {
      this.videoram_w = this.v.videoram_w(this.memCpu1, this.v);
      this.colorram_w = this.videoram_w;
      this.gyruss_spritebank = this.v.gyruss_spritebank_w(this.v);
      this.gyruss_queuereg_w = this.v.gyruss_queuereg_w(this.v);
      this.noCallback = (Eof_callback)this.v;
      this.gyruss_vh_screenrefresh = this.v;
      this.gyruss_vh_convert_color_prom = this.v;
      this.m = new MGyruss();
      this.nmi_interrupt = this.m.nmi_interrupt(this.m);
      this.interrupt_enable_w = this.m.interrupt_enable_w(this.m);
   }

   private GfxLayout charlayout() {
      int[] var1 = new int[]{4, 0};
      int[] var2 = new int[]{0, 1, 2, 3, 64, 65, 66, 67};
      int[] var3 = new int[]{0, 8, 16, 24, 32, 40, 48, 56};
      return new GfxLayout(8, 8, 512, 2, var1, var2, var3, 128);
   }

   private GfxDecodeInfo[] gfxdecodeinfo() {
      return new GfxDecodeInfo[]{new GfxDecodeInfo(this.REGION_GFX1, 0, this.charlayout(), 0, 16), new GfxDecodeInfo(this.REGION_GFX2, 0, this.spritelayout2(), 64, 16), new GfxDecodeInfo(this.REGION_GFX2, 16, this.spritelayout2(), 64, 16), new GfxDecodeInfo(this.REGION_GFX2, 0, this.spritelayout2(), 64, 16)};
   }

   private InputPort[] ipt_gyruss() {
      this.in[0].setBit(1, 255, 1);
      this.in[0].setBit(2, 255, 2);
      this.in[0].setBit(4, 255, 2);
      this.in[0].setBit(8, 255, 4);
      this.in[0].setBit(16, 255, 5);
      this.in[0].setBit(224, 255, 0);
      this.in[1].setBit(1, 255, 8);
      this.in[1].setBit(2, 255, 9);
      this.in[1].setBit(4, 255, 10);
      this.in[1].setBit(8, 255, 11);
      this.in[1].setBit(16, 255, 12);
      this.in[1].setBit(32, 255, 0);
      this.in[1].setBit(64, 255, 0);
      this.in[1].setBit(128, 255, 0);
      this.in[2].setBit(1, 255, 8);
      this.in[2].setBit(2, 255, 9);
      this.in[2].setBit(4, 255, 10);
      this.in[2].setBit(8, 255, 11);
      this.in[2].setBit(16, 255, 12);
      this.in[2].setBit(32, 255, 0);
      this.in[2].setBit(192, 255, 0);
      this.in[3].setBits(255, 255);
      this.in[4].setBits(255, 123);
      this.in[5].setBits(255, 0);
      return this.in;
   }

   private MemoryReadAddress readmem() {
      MemoryReadAddress var1 = new MemoryReadAddress(this.memCpu1);
      var1.setMR(0, 32767, 1);
      var1.setMR('耀', '蟿', 0);
      var1.setMR('退', '\u9fff', 0);
      var1.setMR('ꀀ', 'ꟿ', 0);
      var1.set('쀀', '쀀', this.input_port_4_r);
      var1.set('삀', '삀', this.input_port_0_r);
      var1.set('삠', '삠', this.input_port_1_r);
      var1.set('샀', '샀', this.input_port_2_r);
      var1.set('샠', '샠', this.input_port_3_r);
      var1.set('섀', '섀', this.input_port_5_r);
      return var1;
   }

   private IOReadPort readport() {
      return new IOReadPort();
   }

   private boolean rom_gyruss(URL var1) {
      RomLoader var2 = new RomLoader();
      var2.setMemory(this.memCpu1);
      var2.setZip("gyruss");
      var2.loadROM("gyrussk.1", 0, 8192, -965495747);
      var2.loadROM("gyrussk.2", 8192, 8192, -1528036380);
      var2.loadROM("gyrussk.3", 16384, 8192, 658852504);
      var2.setMemory(this.REGION_CPU4);
      var2.loadROM("gyrussk.9", '\ue000', 8192, -2111049090);
      var2.setMemory(this.REGION_GFX1);
      var2.loadROM("gyrussk.4", 0, 8192, 668480155);
      var2.setMemory(this.REGION_GFX2);
      var2.loadROM("gyrussk.6", 0, 8192, -917906672);
      var2.loadROM("gyrussk.5", 8192, 8192, 1327644954);
      var2.loadROM("gyrussk.8", 16384, 8192, 1204625340);
      var2.loadROM("gyrussk.7", 24576, 8192, -1903347572);
      var2.setMemory(this.REGION_PROMS);
      var2.loadROM("gyrussk.pr3", 0, 32, -1736954445);
      var2.loadROM("gyrussk.pr1", 32, 256, 2127583198);
      var2.loadROM("gyrussk.pr2", 288, 256, -561890687);
      var2.loadZip(var1);
      return true;
   }

   private GfxLayout spritelayout2() {
      int[] var3 = new int[]{4, 0, 131076, 131072};
      int[] var2 = new int[]{0, 1, 2, 3, 64, 65, 66, 67, 128, 129, 130, 131, 192, 193, 194, 195};
      int[] var1 = new int[]{0, 8, 16, 24, 32, 40, 48, 56, 256, 264, 272, 280, 288, 296, 304, 312};
      return new GfxLayout(16, 16, 256, 4, var3, var2, var1, 512);
   }

   private MemoryWriteAddress writemem() {
      MemoryWriteAddress var1 = new MemoryWriteAddress(this.memCpu1);
      var1.setMW(0, 32767, 1);
      var1.set('耀', '菿', this.colorram_w);
      var1.set('萀', '蟿', this.videoram_w);
      var1.setMW('退', '\u9fff', 0);
      var1.setMW('ꀀ', 'ꅿ', 0);
      var1.setMW('ꈀ', 'ꍿ', 0);
      var1.set('꜀', '꜀', this.gyruss_spritebank);
      var1.setMW('꜁', '꜁', 2);
      var1.set('꜂', '꜂', this.gyruss_queuereg_w);
      var1.setMW('ꟼ', 'ꟼ', 0);
      var1.setMW('ꟽ', 'ꟽ', 0);
      var1.setMW('쀀', '쀀', 2);
      var1.set('솀', '솀', this.interrupt_enable_w);
      return var1;
   }

   private IOWritePort writeport() {
      return new IOWritePort();
   }

   public Emulator createEmulator(URL var1, String var2) {
      super.createEmulator(var1, var2);
      this.in[0] = new InputPort();
      this.in[1] = new InputPort();
      this.in[2] = new InputPort();
      this.in[3] = new InputPort();
      this.in[4] = new InputPort();
      this.in[5] = new InputPort();
      this.input_port_0_r = this.in[0];
      this.input_port_1_r = this.in[1];
      this.input_port_2_r = this.in[2];
      this.input_port_3_r = this.in[3];
      this.input_port_4_r = this.in[4];
      this.input_port_5_r = this.in[5];
      this.rom_gyruss(var1);
      this.ipt_gyruss();
      this.v.gyruss();
      EmulatorProperties var3 = new EmulatorProperties();
      var3.setCpuDriver(new CpuDriver(new Z80(), 3072000, this.readmem(), this.writemem(), this.readport(), this.writeport(), this.nmi_interrupt, 1));
      var3.setRotation(1);
      var3.setVisibleArea(0, 255, 16, 239);
      var3.setVideoDimensions(256, 256);
      var3.setGfxDecodeInfo(this.gfxdecodeinfo());
      var3.setPaletteLength(32);
      var3.setColorTableLength(320);
      var3.setPaletteInitializer(this.gyruss_vh_convert_color_prom);
      var3.setVideoEmulator(this.v);
      var3.setInputPorts(this.in);
      this.v.setRegions(this.REGION_PROMS, this.memCpu1, this.REGION_CPU4);
      this.m.init(var3);
      this.v.init(var3);
      return this.m;
   }
}
