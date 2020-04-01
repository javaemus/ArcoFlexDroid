package net.movegaga.jemu2.driver.sms.video;

import net.movegaga.jemu2.JEmu2;

import jef.cpu.Cpu;
import jef.map.ReadHandler;
import jef.map.WriteHandler;
import jef.util.ByteArray;
import jef.util.Persistent;
import jef.video.BitMap;
import jef.video.VideoRenderer;

public class VDP implements VideoRenderer, ReadHandler, WriteHandler, Persistent {
   public int addrLatch;
   private Cpu cpu;
   public int curLine;
   public int curOp;
   public int dataLatch;
   public boolean firstByteWritten;
   public boolean hIrq;
   public int hIrqLine;
   public boolean ntsc = true;
   public Palette palette = new Palette();
   public int[] regControl = new int[16];
   public int regStatus = 63;
   public int rwAddress = 0;
   Renderer screen = new Renderer(this);
   public boolean vIrq;
   public int[] vram = new int[16384];

   public VDP(Cpu var1) {
      this.cpu = var1;
      this.regControl[2] = 14;
      this.regControl[5] = 126;
   }

   private void writeVRAM(int var1) {
      this.firstByteWritten = false;
      this.rwAddress &= 16383;
      var1 &= 255;
      this.dataLatch = var1;
      switch(this.curOp) {
      case 0:
      case 1:
      case 2:
         this.vram[this.rwAddress] = var1;
         break;
      case 3:
         this.palette.setPalette(this.rwAddress & 31, var1);
      }

      this.rwAddress = this.rwAddress + 1 & 16383;
   }

   public int getControl(int var1) {
      return this.regControl[var1];
   }

   public ReadHandler getReadVertical() {
      return new ReadVertical();
   }

   public ByteArray getState() {
      ByteArray var2 = new ByteArray();

      int var1;
      for(var1 = 0; var1 < this.vram.length; ++var1) {
         var2.add8((byte)this.vram[var1]);
      }

      for(var1 = 0; var1 < this.palette.rgb.length; ++var1) {
         if(!JEmu2.IS_APPLET) {
            var2.add8((byte)(this.palette.rgb[var1] & 255));
            var2.add8((byte)(this.palette.rgb[var1] >> 8 & 255));
            var2.add8((byte)(this.palette.rgb[var1] >> 16 & 255));
         } else {
            var2.add8((byte)(this.palette.rgb[var1] >> 16 & 255));
            var2.add8((byte)(this.palette.rgb[var1] >> 8 & 255));
            var2.add8((byte)(this.palette.rgb[var1] & 255));
         }
      }

      for(var1 = 0; var1 < this.regControl.length; ++var1) {
         var2.add8((byte)this.regControl[var1]);
      }

      var2.add16(this.regStatus);
      var2.add16(this.rwAddress);
      var2.add16(this.curOp);
      var2.add16(this.dataLatch);
      var2.add16(this.addrLatch);
      var2.add1(this.firstByteWritten);
      var2.add1(this.ntsc);
      var2.add16(this.curLine);
      var2.add16(this.hIrqLine);
      var2.add1(this.vIrq);
      var2.add1(this.hIrq);
      return var2;
   }

   public int read(int var1) {
      if((var1 & 1) == 1) {
         var1 = this.readStatus();
      } else {
         var1 = this.readVRAM();
      }

      return var1;
   }

   public final int readStatus() {
      int var1 = this.regStatus;
      this.firstByteWritten = false;
      this.regStatus &= -225;
      this.hIrq = false;
      this.vIrq = false;
      this.cpu.interrupt(0, false);
      return var1 & 255;
   }

   public int readVRAM() {
      int var1 = this.dataLatch;
      this.dataLatch = this.vram[this.rwAddress];
      this.firstByteWritten = false;
      this.rwAddress = this.rwAddress + 1 & 16383;
      return var1;
   }

   public BitMap renderVideo() {
      return this.screen.bitmap;
   }

   public void renderVideoPost() {
   }

   public void restoreState(ByteArray var1) {
      int var2 = 0;

      int var3;
      for(var3 = 0; var3 < this.vram.length; ++var2) {
         this.vram[var3] = var1.get8(var2) & 255;
         ++var3;
      }

      for(var3 = 0; var3 < this.palette.rgb.length; ++var2) {
         int var5 = var2 + 1;
         int var4 = var1.get8(var2) & 255;
         var2 = var5 + 1;
         int var6 = var1.get8(var5) & 255;
         var5 = var1.get8(var2) & 255;
         if(!JEmu2.IS_APPLET) {
            this.palette.rgb[var3] = var6 << 8 | var4 | var5 << 16;
         } else {
            this.palette.rgb[var3] = var6 << 8 | var5 | var4 << 16;
         }

         ++var3;
      }

      for(var3 = 0; var3 < this.regControl.length; ++var2) {
         this.regControl[var3] = var1.get8(var2) & 255;
         ++var3;
      }

      this.regStatus = var1.get16(var2);
      var2 = var2 + 1 + 1;
      this.rwAddress = var1.get16(var2);
      var2 = var2 + 1 + 1;
      this.curOp = var1.get16(var2);
      var2 = var2 + 1 + 1;
      this.dataLatch = var1.get16(var2);
      var2 = var2 + 1 + 1;
      this.addrLatch = var1.get16(var2);
      var3 = var2 + 1 + 1;
      var2 = var3 + 1;
      this.firstByteWritten = var1.get1(var3);
      var3 = var2 + 1;
      this.ntsc = var1.get1(var2);
      this.curLine = var1.get16(var3);
      var2 = var3 + 1 + 1;
      this.hIrqLine = var1.get16(var2);
      var2 = var2 + 1 + 1;
      var3 = var2 + 1;
      this.vIrq = var1.get1(var2);
      this.hIrq = var1.get1(var3);
   }

   public void setNTSC(boolean var1) {
      this.ntsc = var1;
   }

   public void updateLine(int var1) {
      this.curLine = var1;
      if(this.curLine <= 192) {
         if(this.curLine == 192) {
            this.regStatus |= 128;
            this.vIrq = true;
         } else {
            this.screen.drawLine(this.curLine);
         }

         if(this.hIrqLine == 0) {
            this.regStatus |= 64;
            this.hIrq = true;
            this.hIrqLine = this.regControl[10];
         } else {
            --this.hIrqLine;
         }

         if(this.hIrq && (this.regControl[0] & 16) != 0) {
            this.cpu.interrupt(0, true);
         }
      } else {
         this.hIrqLine = this.regControl[10];
         if(this.vIrq && (this.regControl[1] & 32) != 0 && this.curLine < 224) {
            this.cpu.interrupt(0, true);
         }
      }

      if(this.curLine >= 192) {
         ;
      }

   }

   public void write(int var1, int var2) {
      if((var1 & 1) == 1) {
         this.writeControl(var2);
      } else {
         this.writeVRAM(var2);
      }

   }

   public final void writeControl(int var1) {
      if(!this.firstByteWritten) {
         this.firstByteWritten = true;
         this.addrLatch = this.addrLatch & '\uff00' | var1 & 255;
      } else {
         this.firstByteWritten = false;
         this.addrLatch = this.addrLatch & 255 | var1 << 8;
         this.rwAddress = this.addrLatch & 16383;
         this.curOp = this.addrLatch >> 14 & 3;
         switch(this.curOp) {
         case 0:
            this.dataLatch = this.vram[this.rwAddress] & 255;
            this.rwAddress = this.rwAddress + 1 & 16383;
         case 1:
         default:
            break;
         case 2:
            this.regControl[this.addrLatch >> 8 & 15] = this.addrLatch & 255;
            if(this.hIrq) {
               if((this.addrLatch & 3840) == 0 && (this.addrLatch & 16) == 0) {
                  this.cpu.interrupt(0, false);
               } else if((this.addrLatch & 3840) == 0 && (this.addrLatch & 16) == 16) {
                  this.cpu.interrupt(0, true);
               }
            }

            if(this.vIrq) {
               if((this.addrLatch & 3840) == 256 && (this.addrLatch & 32) == 0) {
                  this.cpu.interrupt(0, false);
               } else if((this.addrLatch & 3840) == 256 && (this.addrLatch & 32) == 32) {
                  this.cpu.interrupt(0, true);
               }
            }
         }
      }

   }

   class ReadVertical implements ReadHandler {
      public int read(int var1) {
         if(VDP.this.ntsc) {
            if(VDP.this.curLine > 218) {
               var1 = VDP.this.curLine - 6;
               return var1;
            }
         } else if(VDP.this.curLine > 242) {
            var1 = VDP.this.curLine - 57;
            return var1;
         }

         var1 = VDP.this.curLine;
         return var1;
      }
   }
}
