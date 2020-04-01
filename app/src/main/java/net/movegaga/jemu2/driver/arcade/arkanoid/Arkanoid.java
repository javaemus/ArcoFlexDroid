package net.movegaga.jemu2.driver.arcade.arkanoid;

import net.movegaga.jemu2.driver.BaseDriver;

import java.net.URL;

import jef.cpu.Z80;
import jef.cpuboard.CpuDriver;
import jef.machine.BasicEmulator;
import jef.machine.Emulator;
import jef.machine.EmulatorProperties;
import jef.map.InputPort;
import jef.map.InterruptHandler;
import jef.map.MemoryReadAddress;
import jef.map.MemoryWriteAddress;
import jef.map.ReadHandler;
import jef.map.SwitchedInterrupt;
import jef.scoring.HighScoreParser;
import jef.sound.SoundChip;
import jef.sound.chip.AY8910;
import jef.util.RomLoader;
import jef.video.GfxDecodeInfo;

public class Arkanoid extends BaseDriver {
   AY8910 ay8910 = new AY8910(1, 1500000);
   final int[][] charlayout;
   Z80 cpu = new Z80();
   InputPort[] in = new InputPort[5];
   InterruptHandler interrupt = new SwitchedInterrupt(0);
   BasicEmulator m = new BasicEmulator();
   char[] mem_cpu = new char[65536];
   char[] mem_gfx = new char[98304];
   char[] mem_prom = new char[1536];
   int paddleSelect;
   VArkanoid v = new VArkanoid(this);

   public Arkanoid() {
      int[] var4 = new int[]{8};
      int[] var5 = new int[]{8};
      int[] var6 = new int[]{4096};
      int[] var7 = new int[]{3};
      int[] var2 = new int[]{0, 262144, 524288};
      int[] var1 = new int[]{0, 1, 2, 3, 4, 5, 6, 7};
      int[] var3 = new int[]{0, 8, 16, 24, 32, 40, 48, 56};
      this.charlayout = new int[][]{var4, var5, var6, var7, var2, var1, var3, {64}};
   }

   public Emulator createEmulator(URL var1, String var2) {
      super.createEmulator(var1, var2);
      RomLoader var6 = new RomLoader();
      var6.setZip("arkatayt");
      var6.setMemory(this.mem_cpu);
      var6.loadROM("arkanoid.1", 0, '耀', 1846160239);
      var6.loadROM("arkanoid.2", '耀', '耀', 1519902038);
      var6.setMemory(this.mem_gfx);
      var6.loadROM("a75_03.rom", 0, '耀', 59471034);
      var6.loadROM("a75_04.rom", '耀', '耀', 1912267161);
      var6.loadROM("a75_05.rom", 65536, '耀', -949783326);
      var6.setMemory(this.mem_prom);
      var6.loadROM("07.bpr", 0, 512, 184070793);
      var6.loadROM("08.bpr", 512, 512, -1414528261);
      var6.loadROM("09.bpr", 1024, 512, -1480146313);
      var6.loadZip(var1);
      this.in[0] = new InputPort();
      this.in[0].setBit(1, 4);
      this.in[0].setBit(2, 5);
      this.in[0].setBit(16, 0, 1);
      this.in[0].setBit(32, 0, 2);
      this.in[0].setBits(204, 76);
      this.in[1] = new InputPort();
      this.in[1].setBit(1, 12);
      this.in[1].setBit(4, 140);
      this.in[1].setBits(250, 250);
      this.in[2] = new InputPort();
      this.in[2].setAnalog(255, 0, 2, 30, 15, 0, 0);
      this.in[3] = new InputPort();
      this.in[3].setAnalog(255, 0, 130, 30, 15, 0, 0);
      this.in[4] = new InputPort();
      this.in[4].setBits(255, 94);
      MemoryReadAddress var3 = new MemoryReadAddress(this.mem_cpu);
      var3.set('퀁', '퀁', this.in[4]);
      var3.set('퀌', '퀌', this.in[0]);
      var3.set('퀐', '퀐', this.in[1]);
      var3.set('퀘', '퀘', new ReadIn2());
      var3.setMR('\uf000', '\uffff', 2);
      MemoryWriteAddress var7 = new MemoryWriteAddress(this.mem_cpu);
      var7.setMW(0, '뿿', 1);
      var7.set('퀀', '퀀', this.ay8910.AY8910_control_port_0_w());
      var7.set('퀁', '퀁', this.ay8910.AY8910_write_port_0_w());
      var7.set('퀈', '퀈', this.v.writeRegD008());
      var7.setMW('퀘', '퀘', 2);
      var7.set('\ue000', '\ue7ff', this.v);
      GfxDecodeInfo var5 = new GfxDecodeInfo(this.mem_gfx, 0, this.charlayout, 0, 64);
      CpuDriver var4 = new CpuDriver(this.cpu, 6000000, var3, var7, this.interrupt, 1);
      AY8910 var8 = this.ay8910;
      EmulatorProperties var9 = new EmulatorProperties();
      var9.setCpuDriver(new CpuDriver[]{var4});
      var9.setFPS(60);
      var9.setVideoDimensions(256, 256);
      var9.setVisibleArea(0, 255, 16, 239);
      var9.setGfxDecodeInfo(new GfxDecodeInfo[]{var5});
      var9.setPaletteLength(512);
      var9.setColorTableLength(512);
      var9.setVideoEmulation(this.v);
      var9.setInputPorts(this.in);
      var9.setRotation(1);
      var9.setSoundChips(new SoundChip[]{var8});
      new HighScoreParser(this.m, this.mem_cpu, 32, 48, 0, '\ue743', 64, '\ue5c3', 0, 1);
      this.v.init(var9);
      this.m.init(var9);
      return this.m;
   }

   public class ReadIn2 implements ReadHandler {
      public int read(int var1) {
         if(Arkanoid.this.paddleSelect != 0) {
            var1 = Arkanoid.this.in[3].read(var1);
         } else {
            var1 = Arkanoid.this.in[2].read(var1);
         }

         return var1;
      }
   }
}
