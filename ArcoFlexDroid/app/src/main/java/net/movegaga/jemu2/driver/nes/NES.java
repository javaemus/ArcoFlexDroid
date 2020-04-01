package net.movegaga.jemu2.driver.nes;

import net.movegaga.jemu2.driver.Driver;
import net.movegaga.jemu2.driver.nes.mappers.CartMapper;

import java.net.URL;

import jef.cpu.m6502.N2A03;
import jef.cpuboard.CpuDriver;
import jef.machine.BasicEmulator;
import jef.machine.Emulator;
import jef.machine.EmulatorProperties;
import jef.map.InputPort;
import jef.map.InterruptHandler;
import jef.map.MemoryReadAddress;
import jef.map.MemoryWriteAddress;
import jef.video.BitMap;

public class NES extends BasicEmulator implements Driver {
   private static final int CPU_FREQ = 1789773;
   private static final int CYCLES_PER_FRAME = 29829;
   private static final int CYCLES_PER_LINE = 113;
   private static final int FPS = 60;
   private static final int LINES_PER_FRAME = 263;
   APU apu;
   CartMapper cart;
   N2A03 cpu;
   DMA dma;
   Joystick joy1;
   Joystick joy2;
   PPU ppu;
   char[] sysram;

   public Emulator createEmulator(URL var1, String var2) {
      this.sysram = new char[2048];
      CartFactory var4 = new CartFactory(this.sysram);
      InputPort var3 = new InputPort();
      var3.setBit(128, 0, 12);
      var3.setBit(64, 0, 13);
      var3.setBit(32, 0, 14);
      var3.setBit(16, 0, 15);
      var3.setBit(8, 0, 10);
      var3.setBit(4, 0, 9);
      var3.setBit(2, 0, 11);
      var3.setBit(1, 0, 8);
      InputPort var5 = new InputPort();
      var5.setBits(255, 255);
      this.cart = var4.createCart(var2);
      this.cpu = new N2A03();
      this.ppu = new PPU(this.cart, this.cpu);
      this.apu = new APU();
      this.dma = new DMA(this.ppu);
      this.joy1 = new Joystick(var3);
      this.joy2 = new Joystick(var5);
      var3 = new InputPort();
      var3.setBit(1, 255, 10);
      var3.setBit(2, 255, 11);
      var3.setBit(4, 255, 8);
      var3.setBit(8, 255, 9);
      var3.setBit(16, 255, 12);
      var3.setBit(32, 255, 13);
      var3.setBits(192, 192);
      MemoryWriteAddress var6 = new MemoryWriteAddress(65536);
      MemoryReadAddress var7 = new MemoryReadAddress(65536);
      CpuMemoryMap var8 = new CpuMemoryMap(this);
      var7.set(0, '\uffff', var8);
      var6.set(0, '\uffff', var8);
      EmulatorProperties var9 = new EmulatorProperties();
      var9.setCpuDriver(new CpuDriver(this.cpu, 1789773, var7, var6, (InterruptHandler)null, 0));
      var9.setFPS(60);
      var9.setVideoDimensions(256, 192);
      var9.setVisibleArea(0, 255, 0, 191);
      var9.setVideoUpdater(this.ppu);
      var9.setInputPorts(new InputPort[]{var3});
      this.init(var9);
      return this;
   }

   public Object getObject(int var1) {
      return null;
   }

   public BitMap refresh(boolean var1) {
      super.refresh(var1);

      for(int var2 = 0; var2 < 263; ++var2) {
         this.cpu.exec(113);
         this.ppu.line(var2);
      }

      return this.ppu.renderVideo();
   }
}
