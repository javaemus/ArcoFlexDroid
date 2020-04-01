package net.movegaga.jemu2.driver.st;

import net.movegaga.jemu2.driver.Driver;

import java.net.URL;

import jef.cpu.m68000.jM68k;
import jef.cpuboard.BasicCpuBoard;
import jef.cpuboard.CpuBoard;
import jef.cpuboard.CpuDriver;
import jef.machine.BasicEmulator;
import jef.machine.Emulator;
import jef.machine.EmulatorProperties;
import jef.map.InterruptHandler;
import jef.map.ReadMap;
import jef.map.WriteMap;
import jef.util.RomLoader;
import jef.video.BitMap;
import jef.video.VideoRenderer;

public class AtariST extends BasicEmulator implements Driver, VideoRenderer {
   static final int F_200Hz = 2;
   static final int F_200HzB = 256;
   static final int F_ACIA = 4;
   static final int F_BLIT = 8;
   static final int F_MONITOR = 64;
   static final int F_PROFILE = 512;
   static final int F_RCV_FULL = 128;
   static final int F_REPAINT = 2048;
   static final int F_TIMERA_ON = 1024;
   static final int F_TRACE0 = 16;
   static final int F_TRACE1 = 32;
   static final int F_VBL = 1;
   String driver;
   int flags;
   jM68k m68000 = new jM68k();
   char[] mem_ram = new char[14680064];
   char[] mem_tos = new char[196608];
   Shifter shifter = new Shifter(this);

   private void processFlags() {
      if(this.flagSet(258) && (this.peek(16775697) & 32) == 0) {
         this.poke(16775697, this.peek(16775697) | 32);
         this.m68000.generateException(69);
         if(this.flagSet(2)) {
            this.resetFlag(2);
            this.setFlag(256);
         } else {
            this.resetFlag(256);
         }
      }

   }

   public CpuBoard createCpuBoard(int var1) {
      return new MemMap1();
   }

   public Emulator createEmulator(URL var1, String var2) {
      this.driver = var2;
      this.loadTOS100(var1);

      for(int var3 = 0; var3 < 8; ++var3) {
         this.mem_ram[var3] = this.mem_tos[var3];
      }

      EmulatorProperties var4 = new EmulatorProperties();
      var4.setCpuDriver(new CpuDriver(this.m68000, 8000000, (ReadMap)null, (WriteMap)null, (InterruptHandler)null, 0));
      var4.setFPS(50);
      var4.setVideoDimensions(320, 200);
      var4.setVisibleArea(0, 319, 0, 199);
      var4.setVideoUpdater(this);
      this.init(var4);
      return this;
   }

   public boolean flagSet(int var1) {
      boolean var2;
      if((this.flags & var1) != 0) {
         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
   }

   public Object getObject(int var1) {
      return null;
   }

   protected void loadTOS100(URL var1) {
      RomLoader var2 = new RomLoader();
      var2.setZip("tos100");
      var2.setMemory(this.mem_tos);
      var2.loadROM("Tos100.IMG", 0, 196608, -751718608);
      var2.loadZip(var1);
   }

   protected void loadTOS102(URL var1) {
      RomLoader var2 = new RomLoader();
      var2.setZip("tos102");
      var2.setMemory(this.mem_tos);
      var2.loadROM("Tos_102.IMG", 0, 196608, -563970036);
      var2.loadZip(var1);
   }

   public int peek(int var1) {
      var1 &= 16777215;
      char var2;
      if(var1 < 14680064) {
         var2 = this.mem_ram[var1];
      } else if(var1 >= 16515072 && var1 < 16711680) {
         var2 = this.mem_tos[var1 - 16515072];
      } else if(var1 >= 16744960 && var1 < 16745984) {
         var2 = this.shifter.read(var1);
      } else {
         var2 = 0;
      }

      return var2;
   }

   public void poke(int var1, int var2) {
      var1 &= 16777215;
      if(var1 < 14680064) {
         this.mem_ram[var1] = (char)var2;
      }

      if((var1 < 16515072 || var1 >= 16711680) && var1 >= 16744960 && var1 < 16745984) {
         this.shifter.write(var1, var2);
      }

   }

   public BitMap refresh(boolean var1) {
      for(int var2 = 0; var2 < 200; ++var2) {
         this.m68000.exec(800);
         this.shifter.render(var2);
         if(var2 % 50 == 0) {
            this.setFlag(2);
         }

         this.processFlags();
      }

      this.m68000.interrupt(4, true);
      return this.shifter.bitmap;
   }

   public BitMap renderVideo() {
      return this.shifter.bitmap;
   }

   public void renderVideoPost() {
   }

   public void resetFlag(int var1) {
      this.flags &= ~var1;
   }

   public void setFlag(int var1) {
      this.flags |= var1;
   }

   class MemMap1 extends BasicCpuBoard {
      public char[] getMemory() {
         return AtariST.this.mem_tos;
      }

      public int read8(int var1) {
         return AtariST.this.peek(var1);
      }

      public int read8arg(int var1) {
         return this.read8(var1);
      }

      public int read8opc(int var1) {
         return this.read8(var1);
      }

      public void write8(int var1, int var2) {
         AtariST.this.poke(var1, var2);
      }
   }
}
