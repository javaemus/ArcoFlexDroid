package net.movegaga.jemu2.driver.msx;

import java.lang.reflect.Array;
import java.util.ArrayList;

import jef.cpu.z80.Patch;
import jef.cpu.z80.Z80;
import jef.map.MemoryWriteAddress;
import jef.util.ZipLoader;

public class FDC extends Patch {
   static final int[][] INFO;
   static final int INFO_HEADS = 1;
   static final int INFO_NAMES = 2;
   static final int INFO_PERCLUSTER = 5;
   static final int INFO_PERFAT = 4;
   static final int INFO_PERTRACK = 3;
   static final int INFO_SECTORS = 0;
   static final boolean TRACE = false;
   private final char[] buf = new char[512];
   private int curDisk = 0;
   private final ArrayList[] disks = new ArrayList[2];
   private final MSX msx;

   static {
      int[] var0 = new int[]{1280, 2, 112, 8, 2, 2};
      int[] var1 = new int[]{320, 1, 64, 8, 1, 1};
      INFO = new int[][]{{720, 1, 112, 9, 2, 2}, {1440, 2, 112, 9, 3, 2}, {640, 1, 112, 8, 1, 2}, var0, {360, 1, 64, 9, 2, 1}, {720, 2, 112, 9, 2, 2}, var1, {640, 2, 112, 8, 1, 2}};
   }

   public FDC(Z80 var1, MSX var2) {
      super(var1);
      var1.setPatch(this);
      this.msx = var2;
      this.disks[0] = new ArrayList();
      this.disks[1] = new ArrayList();
   }

   private void dskchg() {
      Z80 var2 = this.z80;
      var2.m_iff1a |= 1;
      int var1 = this.z80.m_a8 & 1;
      Disk var3 = null;
      if(this.disks[var1].size() > 0) {
         var3 = (Disk)this.disks[var1].get(this.curDisk);
      }

      if(var3 == null) {
         this.z80.m_a8 = 2;
         this.z80.m_f8 = 1;
         this.z80.retrieveFlags();
      } else {
         this.z80.m_b8 = 0;
         var2 = this.z80;
         var2.m_f8 &= -2;
         this.z80.retrieveFlags();
      }

   }

   private void getdpb() {
      int var1 = this.z80.m_a8 & 1;
      Disk var12 = null;
      if(this.disks[var1].size() > 0) {
         var12 = (Disk)this.disks[var1].get(this.curDisk);
      }

      int var9 = this.z80.m_h8;
      int var10 = this.z80.m_l8;
      if(var12 == null) {
         this.z80.m_a8 = 2;
         this.z80.m_f8 = 1;
      } else if(!var12.read(this.buf, 0)) {
         this.z80.m_a8 = 13;
         this.z80.m_f8 = 1;
      } else {
         int var8 = this.buf[12] * 256 + this.buf[11];
         char var6 = this.buf[20];
         char var5 = this.buf[19];
         int var3 = this.buf[23] * 256 + this.buf[22];
         int var2 = this.buf[15] * 256 + this.buf[14];
         char var4 = this.buf[16];
         char var7 = this.buf[17];
         char var13 = this.buf[21];
         var10 = (var9 << 8 | var10) + 1;
         MemoryWriteAddress var17 = this.msx.mwa;
         var9 = var10 + 1;
         var17.write(var10, var13);
         var17 = this.msx.mwa;
         var1 = var9 + 1;
         var17.write(var9, this.buf[11]);
         var17 = this.msx.mwa;
         var9 = var1 + 1;
         var17.write(var1, this.buf[12]);
         var10 = (var8 >> 5) - 1;

         for(var1 = 0; (1 << var1 & var10) != 0; ++var1) {
            ;
         }

         var17 = this.msx.mwa;
         int var11 = var9 + 1;
         var17.write(var9, var10);
         var17 = this.msx.mwa;
         var9 = var11 + 1;
         var17.write(var11, var1);
         var10 = this.buf[13] - 1;

         for(var1 = 0; (1 << var1 & var10) != 0; ++var1) {
            ;
         }

         var17 = this.msx.mwa;
         var11 = var9 + 1;
         var17.write(var9, var10);
         var17 = this.msx.mwa;
         var9 = var11 + 1;
         var17.write(var11, var1 + 1);
         var17 = this.msx.mwa;
         var1 = var9 + 1;
         var17.write(var9, this.buf[14]);
         var17 = this.msx.mwa;
         var9 = var1 + 1;
         var17.write(var1, this.buf[15]);
         var17 = this.msx.mwa;
         var10 = var9 + 1;
         var17.write(var9, var4);
         var17 = this.msx.mwa;
         var1 = var10 + 1;
         var17.write(var10, var7);
         int var16 = var2 + var4 * var3 + this.buf[17] * 32 / var8;
         var17 = this.msx.mwa;
         var8 = var1 + 1;
         var17.write(var1, var16 & 255);
         var17 = this.msx.mwa;
         var1 = var8 + 1;
         var17.write(var8, var16 >> 8 & 255);
         int var15 = (var6 * 256 + var5 - var16) / this.buf[13];
         var17 = this.msx.mwa;
         int var14 = var1 + 1;
         var17.write(var1, var15 & 255);
         var17 = this.msx.mwa;
         var1 = var14 + 1;
         var17.write(var14, var15 >> 8 & 255);
         var17 = this.msx.mwa;
         var14 = var1 + 1;
         var17.write(var1, this.buf[22]);
         var1 = var2 + var4 * var3;
         this.msx.mwa.write(var14, var1 & 255);
         this.msx.mwa.write(var14 + 1, var1 >> 8 & 255);
         Z80 var18 = this.z80;
         var18.m_f8 &= -2;
         this.z80.retrieveFlags();
      }

   }

   private void phydio() {
      int var1 = this.z80.m_a8 & 1;
      Disk var6 = null;
      if(this.disks[var1].size() > 0) {
         var6 = (Disk)this.disks[var1].get(this.curDisk);
      }

      this.z80.di();
      int var2 = this.z80.m_d8 << 8 | this.z80.m_e8;
      int var4 = this.z80.m_h8 << 8 | this.z80.m_l8;
      int var3 = this.z80.m_b8;
      int var5 = this.z80.m_c8;
      var1 = var5;
      if(var5 < 248) {
         var1 = 249;
      }

      if(var6 == null) {
         this.z80.m_a8 = 2;
         this.z80.m_f8 = 1;
         this.z80.retrieveFlags();
      } else if(var2 + var3 > INFO[var1 - 248][0]) {
         this.z80.m_a8 = 8;
         this.z80.m_f8 = 1;
         this.z80.retrieveFlags();
      } else {
         if((this.z80.m_f8 & 1) != 0) {
            System.out.println("phydio:write not implemented...");
         } else {
            for(var1 = var4; var3 != 0; --var3) {
               if(!var6.read(this.buf, var2)) {
                  this.z80.m_a8 = 4;
                  this.z80.m_f8 = 1;
                  this.z80.retrieveFlags();
                  return;
               }

               Z80 var7 = this.z80;
               --var7.m_b8;

               for(var4 = 0; var4 < 512; ++var1) {
                  this.msx.ramMapper.write(var1, this.buf[var4]);
                  ++var4;
               }

               ++var2;
            }
         }

         Z80 var8 = this.z80;
         var8.m_f8 &= -2;
         this.z80.retrieveFlags();
      }

   }

   public void addDisk(String var1, int var2, int var3) {
      char[] var6 = new char[var3];
      ZipLoader var7 = new ZipLoader(var1);
      var7.queue((char[])var6, var2, 0, var3);
      var7.load();
      if(var3 <= 737281) {
         this.disks[0].add(new Disk(var6));
      } else {
         int var5 = var3 / 737280;
         char[][] var8 = (char[][])Array.newInstance(Character.TYPE, new int[]{var5, 737280});
         var2 = 0;

         for(var3 = 0; var3 < var5; ++var3) {
            for(int var4 = 0; var4 < 737280; ++var2) {
               var8[var3][var4] = var6[var2];
               ++var4;
            }

            this.disks[0].add(new Disk(var8[var3]));
         }
      }

   }

   public void exec(int var1) {
      switch(var1) {
      case 16400:
         this.phydio();
         break;
      case 16403:
         this.dskchg();
      case 16406:
         this.getdpb();
      }

   }

   public void nextDisk() {
      if(this.disks[0].size() > 0) {
         this.curDisk = (this.curDisk + 1) % this.disks[0].size();
      }

   }
}
