package net.movegaga.jemu2.driver.arcade.cps1;

import net.movegaga.jemu2.driver.Driver;

import java.net.URL;

import jef.cpu.Cpu;
import jef.cpu.m68000.jM68k;
import jef.cpuboard.BasicCpuBoard;
import jef.cpuboard.CpuBoard;
import jef.cpuboard.CpuDriver;
import jef.machine.BasicEmulator;
import jef.machine.Emulator;
import jef.machine.EmulatorProperties;
import jef.map.InputPort;
import jef.map.InterruptHandler;
import jef.map.ReadMap;
import jef.map.WriteMap;
import jef.util.ZipLoader;

public class Cps1 extends BasicEmulator implements Driver {
   CPSConfig config;
   long[] gfx1;
   long[] gfx2;
   InputPort[] in = new InputPort[7];
   Cpu m68000 = new jM68k();
   char[] ram1_cpu1 = new char[196608];
   char[] ram2_cpu1 = new char[65536];
   char[] ram_qsh1 = new char[8192];
   char[] ram_qsh2 = new char[8192];
   char[] rom_cpu1 = new char[2097152];
   char[] rom_qsnd = new char[65536];
   VCps1 video = new VCps1(this);

   private void loadROM(String var1) {
      if(var1.equals("sf2")) {
         this.romSF2(var1);
      }

   }

   private void romSF2(String var1) {
      ZipLoader var2 = new ZipLoader(var1);
      var2.queue((char[])this.rom_cpu1, -29757901, 0, 131072, 2);
      var2.queue((char[])this.rom_cpu1, -74265228, 1, 131072, 2);
      var2.queue((char[])this.rom_cpu1, 1772135169, 262144, 131072, 2);
      var2.queue((char[])this.rom_cpu1, 1579342704, 262145, 131072, 2);
      var2.queue((char[])this.rom_cpu1, -1946553883, 524288, 131072, 2);
      var2.queue((char[])this.rom_cpu1, 1651439924, 524289, 131072, 2);
      var2.queue((char[])this.rom_cpu1, -1152716011, 786432, 131072, 2);
      var2.queue((char[])this.rom_cpu1, -1070984213, 786433, 131072, 2);
      char[] var3 = new char[6291456];
      var2.queue(var3, -29757901, 0, 524288, 8, 2);
      var2.queue(var3, 1461795816, 2, 524288, 8, 2);
      var2.queue(var3, -1168991409, 4, 524288, 8, 2);
      var2.queue(var3, 1260073896, 6, 524288, 8, 2);
      var2.queue(var3, 746463785, 2097152, 524288, 8, 2);
      var2.queue(var3, -1252749545, 2097154, 524288, 8, 2);
      var2.queue(var3, 347620114, 2097156, 524288, 8, 2);
      var2.queue(var3, 1587337370, 2097158, 524288, 8, 2);
      var2.queue(var3, -1723073960, 4194304, 524288, 8, 2);
      var2.queue(var3, 1046916509, 4194306, 524288, 8, 2);
      var2.queue(var3, -1044448600, 4194308, 524288, 8, 2);
      var2.queue(var3, 103270449, 4194310, 524288, 8, 2);
      this.gfx1 = this.packBytesToLong(var3);
      var3 = (char[])null;
   }

   public CpuBoard createCpuBoard(int var1) {
      Object var2;
      if(var1 == 0) {
         var2 = new MemMap1();
      } else {
         var2 = new BasicCpuBoard();
      }

      return (CpuBoard)var2;
   }

   public Emulator createEmulator(URL var1, String var2) {
      this.config = Config.createConfig(var2);
      this.loadROM(var2);
      (new EmulatorProperties()).setCpuDriver(new CpuDriver[]{new CpuDriver(this.m68000, 12000000, (ReadMap)null, (WriteMap)null, (InterruptHandler)null, 0)});
      return this;
   }

   public Object getObject(int var1) {
      return null;
   }

   long[] packBytesToLong(char[] var1) {
      long[] var12 = new long[var1.length >> 3];
      int var3 = 0;

      for(int var2 = 0; var2 < var12.length; ++var2) {
         int var5 = var3 + 1;
         char var4 = var1[var3];
         var3 = var5 + 1;
         char var13 = var1[var5];
         int var7 = var3 + 1;
         char var6 = var1[var3];
         var3 = var7 + 1;
         char var14 = var1[var7];
         int var9 = var3 + 1;
         char var8 = var1[var3];
         var3 = var9 + 1;
         char var10 = var1[var9];
         var9 = var3 + 1;
         char var11 = var1[var3];
         var3 = var9 + 1;
         var12[var2] = (long)(var4 << 56 | var13 << 48 | var6 << 40 | var14 << 32 | var8 << 24 | var10 << 16 | var11 << 8 | var1[var9]);
      }

      return var12;
   }

   public int rdPorts1(int var1) {
      switch(var1) {
      case 8388609:
      case 8388625:
         var1 = this.in[4].read(0);
         break;
      case 8388632:
      case 8388633:
         var1 = this.in[0].read(0);
         break;
      case 8388634:
      case 8388635:
         var1 = this.in[1].read(0);
         break;
      case 8388636:
      case 8388637:
         var1 = this.in[2].read(0);
         break;
      case 8388638:
      case 8388639:
         var1 = this.in[3].read(0);
         break;
      case 8388640:
      case 8388641:
         var1 = 0;
         break;
      case 8388982:
      case 8388983:
         var1 = this.in[5].read(0);
         break;
      case 8388984:
      case 8388985:
         var1 = this.in[6].read(0);
         break;
      case 8389116:
      case 8389117:
         var1 = this.in[5].read(0);
         break;
      default:
         if(var1 >= 8388864 && var1 < 8389120) {
            var1 = this.video.readPort(var1 & 255);
         } else {
            var1 = 255;
         }
      }

      return var1;
   }

   public int rdPorts2(int var1) {
      switch(var1) {
      case 15843328:
      case 15843329:
         var1 = this.in[5].read(0);
         break;
      case 15843330:
      case 15843331:
         var1 = this.in[6].read(0);
         break;
      default:
         var1 = 255;
      }

      return var1;
   }

   public void wrPorts1(int var1, int var2) {
      if(var1 >= 8388864 && var1 < 8389120) {
         this.video.writePort(var1 & 255, var2);
      }

   }

   public void wrPorts2(int var1, int var2) {
   }

   class MemMap1 extends BasicCpuBoard {
      public char[] getMemory() {
         return Cps1.this.rom_cpu1;
      }

      public int read8(int var1) {
         var1 &= 16777215;
         if(var1 < 2097152) {
            var1 = Cps1.this.rom_cpu1[2097151 & var1];
         } else if(var1 < 8388608) {
            var1 = Cps1.this.rdUndefined(var1);
         } else if(var1 < 9437184) {
            var1 = Cps1.this.rdPorts1(var1);
         } else if(var1 < 9633792) {
            var1 = Cps1.this.ram1_cpu1[var1 - 9437184];
         } else if(var1 < 15728640) {
            var1 = Cps1.this.rdUndefined(var1);
         } else if(var1 < 15794176) {
            var1 = Cps1.this.rom_qsnd['\uffff' & var1];
         } else if(var1 < 15826944) {
            var1 = Cps1.this.rdUndefined(var1);
         } else if(var1 < 15835136) {
            var1 = Cps1.this.ram_qsh1[var1 & 8191];
         } else if(var1 < 15851520) {
            var1 = Cps1.this.rdPorts2(var1);
         } else if(var1 < 15859712) {
            var1 = Cps1.this.ram_qsh2[var1 & 8191];
         } else if(var1 < 16711680) {
            var1 = Cps1.this.rdUndefined(var1);
         } else {
            var1 = Cps1.this.ram2_cpu1[var1 - 16711680];
         }

         return var1;
      }

      public int read8arg(int var1) {
         return this.read8(var1);
      }

      public int read8opc(int var1) {
         return this.read8(var1);
      }

      public void write8(int var1, int var2) {
         if(var1 >= 2097152) {
            if(var1 < 9437184) {
               Cps1.this.wrPorts1(var1, var2);
            } else if(var1 < 9633792) {
               Cps1.this.ram1_cpu1[var1 - 9437184] = (char)var2;
            } else if(var1 >= 15826944) {
               if(var1 < 15835136) {
                  Cps1.this.ram_qsh1[var1 & 8191] = (char)var2;
               } else if(var1 < 15851520) {
                  Cps1.this.wrPorts2(var1, var2);
               } else if(var1 < 15859712) {
                  Cps1.this.ram_qsh2[var1 & 8191] = (char)var2;
               } else if(var1 >= 16711680) {
                  Cps1.this.ram2_cpu1[var1 - 16711680] = (char)var2;
               }
            }
         }

      }
   }
}
