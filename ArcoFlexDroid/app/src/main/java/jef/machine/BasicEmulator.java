package jef.machine;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import jef.cpuboard.BasicCpuBoard;
import jef.cpuboard.CpuBoard;
import jef.cpuboard.CpuDriver;
import jef.map.InterruptHandler;
import jef.map.WriteHandler;
import jef.scoring.HighScoreParser;
import jef.sound.Settings;
import jef.sound.SoundSystem;
import jef.util.time.Timer;
import jef.video.BitMap;

public class BasicEmulator implements Emulator {
   protected BitMap backBuffer;
   public CpuBoard[] cb;
   public CpuDriver[] cd;
   protected int currentSlice = 0;
   private long highScore;
   protected HighScoreParser highScoreHandler;
   private boolean highScoreSupported;
   public boolean interrupt_enabled = true;
   public EmulatorProperties md;
   public boolean nmi_interrupt_enabled = true;
   public SoundSystem se;
   protected int slicesPerFrame;
   protected boolean soundEnabled = true;

   public BasicEmulator() {
      Timer.setMachine(this);
   }

   protected int[] charToInt(char[] var1) {
      int[] var3 = new int[var1.length];

      for(int var2 = 0; var2 < var1.length; ++var2) {
         var3[var2] = var1[var2];
      }

      return var3;
   }

   protected void copyCharToInt(char[] var1, int[] var2) {
      for(int var3 = 0; var3 < var1.length; ++var3) {
         var2[var3] = var1[var3];
      }

   }

   public CpuBoard createCpuBoard(int var1) {
      return new BasicCpuBoard();
   }

   public int getCurrentSlice() {
      return this.currentSlice;
   }

   protected BitMap getDisplay() {
      BitMap var1 = this.md.vh_screenrefresh.renderVideo();
      this.md.vh_screenrefresh.renderVideoPost();
      return var1;
   }

   public long getHighScore() {
      return this.highScore;
   }

   public double getProgress() {
      double var1 = (double)this.md.getCpuDriver()[0].frq / (double)this.md.fps;
      double var5 = var1 / (double)this.slicesPerFrame;
      double var3 = (double)this.cb[0].getCpu().getCyclesLeft();
      return 1.0D - (var1 - ((double)(this.currentSlice + 1) * var5 - var3)) / var1;
   }

   public int getProperty(int var1) {
      if(var1 == 0) {
         var1 = this.md.fps;
      } else if(var1 == 3) {
         var1 = this.md.ROT;
      } else if(var1 == 1) {
         var1 = this.md.visible[1] - this.md.visible[0] + 1;
      } else if(var1 == 2) {
         var1 = this.md.visible[3] - this.md.visible[2] + 1;
      } else if(var1 == 4) {
         var1 = this.md.controllers;
      } else {
         var1 = -1;
      }

      return var1;
   }

   public void init(EmulatorProperties var1) {
      var1.mach = this;
      this.md = var1;
      if(!var1.info) {
         this.cd = var1.cpuDriver;
         this.cb = new CpuBoard[this.cd.length];
         this.slicesPerFrame = var1.spf;

         int var2;
         for(var2 = 0; var2 < this.cd.length; ++var2) {
            if(this.slicesPerFrame < this.cd[var2].ipf) {
               this.slicesPerFrame = this.cd[var2].ipf;
            }

            this.cb[var2] = this.createCpuBoard(var2);
            this.cb[var2].init(this.cd[var2]);
            this.cd[var2].cpu.setTag("CPU #" + Integer.toString(var2));
            this.cd[var2].cpu.reset();
         }

         if(this.slicesPerFrame == 0) {
            this.slicesPerFrame = 1;
         }

         this.se = new SoundSystem();
         if(var1.soundChips != null) {
            if(var1.sfps > 0) {
               var2 = var1.sfps;
            } else {
               var2 = var1.fps;
            }

            System.out.println("Sound initializing...");

            try {
               this.se.init(var1.soundChips, Settings.SOUND_SAMPLING_FREQ, Settings.SOUND_BUFFER_SIZE, var2);
            } catch (Exception var4) {
               System.out.println("Failed to initialize sound!");
               var4.printStackTrace();
               this.soundEnabled = false;
            }
         }

         System.out.println("Video initializing...");
         var1.ve.init(var1);
         if(var1.initProms != null) {
            System.out.println("Generating palette...");
            var1.initProms.initPalette();
         }

         if(var1.vh_start != null) {
            System.out.println("Starting video emulation...");
            var1.vh_start.initVideo();
         }

         System.out.println("Video initialized successfully.");
         System.out.println("Machine initializing...");
         var1.init.exec();
         System.out.println("Machine initialized successfully.");
         System.out.println(Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory());
      }

   }

   protected void interleave(int var1, char[] var2, int var3, char[] var4, int var5, int var6) {
      int var7 = var5;

      for(var5 = var3; var5 < var3 + var6; ++var5) {
         var4[var7] = var2[var5];
         var7 += var1;
      }

   }

   protected void interleave(int var1, char[] var2, int var3, int[] var4, int var5, int var6) {
      int var7 = var5;

      for(var5 = var3; var5 < var3 + var6; ++var5) {
         var4[var7] = var2[var5];
         var7 += var1;
      }

   }

   public InterruptHandler interrupt() {
      return new Interrupt();
   }

   public InterruptHandler interruptNMI() {
      return new NMI_interrupt();
   }

   public WriteHandler interrupt_enable() {
      return new Interrupt_enable();
   }

   public InterruptHandler interrupt_switched() {
      return new Interrupt_switched();
   }

   public InterruptHandler irq0_line_hold() {
      return new Interrupt_switched();
   }

   public boolean isHighScoreSupported() {
      return this.highScoreSupported;
   }

   public void keyPress(int var1) {
      if(this.md.input != null) {
         for(int var2 = 0; var2 < this.md.input.length; ++var2) {
            this.md.input[var2].keyPress(var1);
         }
      }

   }

   public void keyRelease(int var1) {
      if(this.md.input != null) {
         for(int var2 = 0; var2 < this.md.input.length; ++var2) {
            this.md.input[var2].keyRelease(var1);
         }
      }

   }

   public WriteHandler nmi_interrupt_enable() {
      return new NMI_interrupt_enable();
   }

   public InterruptHandler nmi_interrupt_switched() {
      return new NMI_interrupt_switched();
   }

   protected char rdUndefined(int var1) {
      System.out.println("(WARNING) undefined read " + Integer.toHexString(var1));
      return 'ÿ';
   }

   public int readinputport(int var1) {
      if(this.md.input == null) {
         var1 = 0;
      } else {
         var1 = this.md.input[var1].read(0);
      }

      return var1;
   }

   public BitMap refresh(boolean var1) {
      boolean var5 = false;
      int var2 = 0;

      boolean var8;
      for(int var4 = 0; var4 < this.slicesPerFrame; var5 = var8) {
         this.currentSlice = var4;

         int var7;
         for(int var6 = 0; var6 < this.cd.length; var2 = var7) {
            label54: {
               if(this.cd[var6].isAudioCpu) {
                  var7 = var2;
                  if(!this.cd[var6].isAudioCpu) {
                     break label54;
                  }

                  var7 = var2;
                  if(!this.soundEnabled) {
                     break label54;
                  }
               }

               var7 = this.cd[var6].frq / this.md.fps / this.slicesPerFrame;
               int var3 = var2;
               if(var6 == 0) {
                  var3 = var2 + var7;
               }

               this.cd[var6].cpu.exec(var7);
               var2 = this.cd[var6].ipf;
               var7 = var3;
               if(var2 > 0) {
                  var7 = var3;
                  if(var4 % (this.slicesPerFrame / var2) == 0) {
                     this.cb[var6].interrupt(this.cd[var6].irh.irq(), true);
                     var7 = var3;
                  }
               }
            }

            ++var6;
         }

         var8 = var5;
         if(!var5) {
            var8 = var5;
            if(var1) {
               var8 = var5;
               if(var2 > this.md.getVideoBlankDuration()) {
                  this.backBuffer = this.getDisplay();
                  var8 = true;
               }
            }
         }

         ++var4;
      }

      this.se.update();
      this.updateInput();
      if(this.highScoreHandler != null) {
         this.highScoreHandler.update();
      }

      return this.backBuffer;
   }

   public void reset(boolean var1) {
      if(!this.highScoreSupported) {
         for(int var2 = 0; var2 < this.cd.length; ++var2) {
            this.cd[var2].cpu.reset();
         }
      }

   }

   public void resetHighScore() {
      this.highScore = 0L;
   }

   public void setHighScore(long var1) {
      if(this.highScoreSupported && var1 > this.highScore) {
         this.highScore = var1;
      }

   }

   public void setHighScoreHandler(HighScoreParser var1) {
      this.highScoreHandler = var1;
      if(var1 != null) {
         this.setHighScoreSupported(true);
      } else {
         this.setHighScoreSupported(false);
      }

      this.resetHighScore();
   }

   public void setHighScoreSupported(boolean var1) {
      this.highScoreSupported = var1;
   }

   public void setSound(boolean var1) {
      this.soundEnabled = var1;
      if(this.md.soundChips != null) {
         for(int var2 = 0; var2 < this.md.soundChips.length; ++var2) {
            if(this.soundEnabled) {
               this.md.soundChips[var2].enable();
            } else {
               this.md.soundChips[var2].disable();
            }
         }
      }

   }

   protected void updateInput() {
      if(this.md.input != null) {
         for(int var1 = 0; var1 < this.md.input.length; ++var1) {
            this.md.input[var1].update();
         }
      }

   }

   protected void wrUndefined(int var1, int var2) {
      System.out.println("(WARNING) undefined write " + Integer.toHexString(var1) + ", " + Integer.toHexString(var2));
   }

   protected void writeMemoryDump(char[] var1, String var2) throws IOException {
      boolean var4 = true;
      File var5 = null;

      int var3;
      for(var3 = 0; var4; ++var3) {
         var5 = new File("./" + var2 + ".dump" + var3);
         var4 = var5.exists();
      }

      DataOutputStream var6 = new DataOutputStream(new FileOutputStream(var5));
      byte[] var7 = new byte[var1.length];

      for(var3 = 0; var3 < var7.length; ++var3) {
         var7[var3] = (byte)var1[var3];
      }

      var6.write(var7);
      var6.close();
   }

   public class Interrupt implements InterruptHandler {
      public int irq() {
         return 0;
      }
   }

   public class Interrupt_enable implements WriteHandler {
      public void write(int var1, int var2) {
         BasicEmulator var4 = BasicEmulator.this;
         boolean var3;
         if(var2 != 0) {
            var3 = true;
         } else {
            var3 = false;
         }

         var4.interrupt_enabled = var3;
      }
   }

   public class Interrupt_switched implements InterruptHandler {
      public int irq() {
         byte var1;
         if(BasicEmulator.this.interrupt_enabled) {
            var1 = 0;
         } else {
            var1 = -1;
         }

         return var1;
      }
   }

   public class NMI_interrupt implements InterruptHandler {
      public int irq() {
         return 1;
      }
   }

   public class NMI_interrupt_enable implements WriteHandler {
      public void write(int var1, int var2) {
         BasicEmulator var4 = BasicEmulator.this;
         boolean var3;
         if(var2 != 0) {
            var3 = true;
         } else {
            var3 = false;
         }

         var4.nmi_interrupt_enabled = var3;
      }
   }

   public class NMI_interrupt_switched implements InterruptHandler {
      public int irq() {
         byte var1;
         if(BasicEmulator.this.nmi_interrupt_enabled) {
            var1 = 1;
         } else {
            var1 = -1;
         }

         return var1;
      }
   }
}
