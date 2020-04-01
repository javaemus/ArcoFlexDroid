package jef.machine;

import jef.cpuboard.CpuDriver;
import jef.map.InputPort;
import jef.map.NoFunction;
import jef.map.VoidFunction;
import jef.sound.SoundChip;
import jef.video.Eof_callback;
import jef.video.GfxDecodeInfo;
import jef.video.PaletteInitializer;
import jef.video.VideoEmulator;
import jef.video.VideoFinalizer;
import jef.video.VideoInitializer;
import jef.video.VideoRenderer;

public class EmulatorProperties {
   public int ROT = 0;
   public int col = 1;
   public int controllers = 1;
   public CpuDriver[] cpuDriver;
   public Eof_callback eof_callback = new NoEof();
   public int fps = 60;
   public GfxDecodeInfo[] gfx;
   public int h;
   public boolean info = false;
   public VoidFunction init = new NoFunction();
   public PaletteInitializer initProms = new NoProms();
   public InputPort[] input;
   public Emulator mach = null;
   private char[][] memRegions;
   public int pal = 1;
   public int sfps;
   public SoundChip[] soundChips;
   public int spf = 1;
   public int vbd = 0;
   public VideoEmulator ve = new NoVE();
   public VideoRenderer vh_screenrefresh;
   public VideoInitializer vh_start = new NoStart();
   public VideoFinalizer vh_stop = new NoStop();
   public int videoFlags = 0;
   public int[] visible;
   public int w;

   public EmulatorProperties() {
   }

   public EmulatorProperties(CpuDriver[] var1, int var2, int var3, int var4, VoidFunction var5, int var6, int var7, int[] var8, GfxDecodeInfo[] var9, int var10, int var11, PaletteInitializer var12, int var13, Eof_callback var14, VideoInitializer var15, VideoFinalizer var16, VideoRenderer var17, SoundChip[] var18) {
      this.cpuDriver = var1;
      this.fps = var2;
      this.vbd = var3;
      this.spf = var4;
      this.init = var5;
      this.w = var6;
      this.h = var7;
      this.visible = var8;
      this.gfx = var9;
      this.pal = var10;
      this.col = var11;
      this.initProms = var12;
      this.videoFlags = var13;
      this.eof_callback = var14;
      this.vh_start = var15;
      this.vh_stop = var16;
      this.vh_screenrefresh = var17;
      this.soundChips = var18;
   }

   public EmulatorProperties(CpuDriver[] var1, int var2, int var3, int var4, VoidFunction var5, int var6, int var7, int[] var8, GfxDecodeInfo[] var9, int var10, int var11, VideoEmulator var12, PaletteInitializer var13, int var14, Eof_callback var15, VideoInitializer var16, VideoFinalizer var17, VideoRenderer var18, SoundChip[] var19) {
      this.cpuDriver = var1;
      this.fps = var2;
      this.vbd = var3;
      this.spf = var4;
      this.init = var5;
      this.w = var6;
      this.h = var7;
      this.visible = var8;
      this.gfx = var9;
      this.pal = var10;
      this.col = var11;
      this.initProms = var13;
      this.videoFlags = var14;
      this.eof_callback = var15;
      this.vh_start = var16;
      this.vh_stop = var17;
      this.vh_screenrefresh = var18;
      this.soundChips = var19;
      this.ve = var12;
   }

   public EmulatorProperties(CpuDriver[] var1, int var2, int var3, VideoRenderer var4) {
      this.cpuDriver = var1;
      this.w = var2;
      this.h = var3;
      this.vh_screenrefresh = var4;
      this.visible = new int[4];
      this.visible[0] = 0;
      this.visible[1] = 0;
      this.visible[2] = var2 - 1;
      this.visible[3] = var3 - 1;
   }

   public void addCpuDriver(CpuDriver var1) {
      this.addCpuDriver(var1, false);
   }

   public void addCpuDriver(CpuDriver var1, boolean var2) {
      if(this.cpuDriver == null) {
         this.cpuDriver = new CpuDriver[1];
      } else {
         CpuDriver[] var4 = new CpuDriver[this.cpuDriver.length + 1];

         for(int var3 = 0; var3 < this.cpuDriver.length; ++var3) {
            var4[var3] = this.cpuDriver[var3];
         }

         this.cpuDriver = var4;
      }

      this.cpuDriver[this.cpuDriver.length - 1] = var1;
      var1.isAudioCpu = var2;
   }

   public void addSoundChip(SoundChip var1) {
      if(this.soundChips == null) {
         this.setSoundChip(var1);
      } else {
         SoundChip[] var3 = new SoundChip[this.soundChips.length + 1];

         for(int var2 = 0; var2 < this.soundChips.length; ++var2) {
            var3[var2] = this.soundChips[var2];
         }

         this.soundChips = var3;
         this.soundChips[this.soundChips.length - 1] = var1;
      }

   }

   public int getColorTableLength() {
      return this.col;
   }

   public int getControllers() {
      return this.controllers;
   }

   public CpuDriver[] getCpuDriver() {
      return this.cpuDriver;
   }

   public Eof_callback getEofCallBack() {
      return this.eof_callback;
   }

   public int getFPS() {
      return this.fps;
   }

   public GfxDecodeInfo[] getGfxDecodeInfo() {
      return this.gfx;
   }

   public VoidFunction getInitializer() {
      return this.init;
   }

   public InputPort[] getInputPorts() {
      return this.input;
   }

   public Emulator getMachine() {
      return this.mach;
   }

   public char[][] getMemRegions() {
      return this.memRegions;
   }

   public int getPaletteLength() {
      return this.pal;
   }

   public SoundChip[] getSoundChips() {
      return this.soundChips;
   }

   public int getTimeSlicesPerFrame() {
      return this.spf;
   }

   public VideoRenderer getVhRefresh() {
      return this.vh_screenrefresh;
   }

   public VideoInitializer getVhStart() {
      return this.vh_start;
   }

   public VideoFinalizer getVhStop() {
      return this.vh_stop;
   }

   public int getVideoBlankDuration() {
      return this.vbd;
   }

   public VideoEmulator getVideoEmulator() {
      return this.ve;
   }

   public int getVideoHeight() {
      return this.h;
   }

   public int getVideoWidth() {
      return this.w;
   }

   public int[] getVisibleArea() {
      return this.visible;
   }

   public void setColorTableLength(int var1) {
      this.col = var1;
   }

   public void setControllers(int var1) {
      this.controllers = var1;
   }

   public void setCpuDriver(CpuDriver var1) {
      this.cpuDriver = new CpuDriver[]{var1};
   }

   public void setCpuDriver(CpuDriver[] var1) {
      this.cpuDriver = var1;
   }

   public void setDynamicPalette(boolean var1) {
      if(var1) {
         this.videoFlags |= 4;
      } else {
         this.videoFlags &= -5;
      }

   }

   public void setEofCallBack(Eof_callback var1) {
      this.eof_callback = var1;
   }

   public void setFPS(int var1) {
      this.fps = var1;
   }

   public void setGfxDecodeInfo(GfxDecodeInfo var1) {
      this.gfx = new GfxDecodeInfo[1];
      this.gfx[0] = var1;
   }

   public void setGfxDecodeInfo(GfxDecodeInfo[] var1) {
      this.gfx = var1;
   }

   public void setInitializer(VoidFunction var1) {
      this.init = var1;
   }

   public void setInputPorts(InputPort[] var1) {
      this.input = var1;
   }

   public void setMachine(Emulator var1) {
      this.mach = var1;
   }

   public void setMemRegions(char[][] var1) {
      this.memRegions = var1;
   }

   public void setPaletteInitializer(PaletteInitializer var1) {
      this.initProms = var1;
   }

   public void setPaletteLength(int var1) {
      this.pal = var1;
   }

   public void setRotation(int var1) {
      this.ROT = var1;
   }

   public void setSoundChip(SoundChip var1) {
      this.soundChips = new SoundChip[]{var1};
   }

   public void setSoundChips(SoundChip[] var1) {
      this.soundChips = var1;
   }

   public void setSoundFPS(int var1) {
      this.sfps = var1;
   }

   public void setTimeSlicesPerFrame(int var1) {
      this.spf = var1;
   }

   public void setVBlankDuration(int var1) {
      this.vbd = var1;
   }

   public void setVhRefresh(VideoRenderer var1) {
      this.vh_screenrefresh = var1;
   }

   public void setVhStop(VideoFinalizer var1) {
      this.vh_stop = var1;
   }

   public void setVideoDimensions(int var1, int var2) {
      this.w = var1;
      this.h = var2;
   }

   public void setVideoEmulation(Object var1) {
      try {
         this.vh_start = (VideoInitializer)var1;
      } catch (Exception var7) {
         ;
      }

      try {
         this.vh_stop = (VideoFinalizer)var1;
      } catch (Exception var6) {
         ;
      }

      try {
         this.vh_screenrefresh = (VideoRenderer)var1;
      } catch (Exception var5) {
         ;
      }

      try {
         this.initProms = (PaletteInitializer)var1;
      } catch (Exception var4) {
         ;
      }

      try {
         this.eof_callback = (Eof_callback)var1;
      } catch (Exception var3) {
         ;
      }

   }

   public void setVideoEmulator(VideoEmulator var1) {
      this.ve = var1;
   }

   public void setVideoHeight(int var1) {
      this.h = var1;
   }

   public void setVideoInitializer(VideoInitializer var1) {
      this.vh_start = var1;
   }

   public void setVideoUpdater(VideoRenderer var1) {
      this.vh_screenrefresh = var1;
   }

   public void setVideoWidth(int var1) {
      this.w = var1;
   }

   public void setVisibleArea(int var1, int var2, int var3, int var4) {
      this.visible = new int[4];
      this.visible[0] = var1;
      this.visible[1] = var2;
      this.visible[2] = var3;
      this.visible[3] = var4;
   }

   public void setVisibleArea(int[] var1) {
      this.visible = var1;
   }

   public class NoEof implements Eof_callback {
      public void eof_callback() {
      }
   }

   public class NoProms implements PaletteInitializer {
      public void initPalette() {
      }
   }

   public class NoStart implements VideoInitializer {
      public int initVideo() {
         return 0;
      }
   }

   public class NoStop implements VideoFinalizer {
      public void finalizeVideo() {
      }
   }

   public class NoVE implements VideoEmulator {
      public void init(EmulatorProperties var1) {
      }
   }
}
