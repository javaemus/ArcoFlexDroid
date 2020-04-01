package cottage.mame;

import java.net.URL;

import jef.cpu.Cpu;
import jef.cpu.I8080;
import jef.cpu.M6809;
import jef.cpu.Z80;
import jef.cpu.m68000.jM68k;
import jef.cpuboard.CpuDriver;
import jef.machine.Emulator;
import jef.machine.EmulatorProperties;
import jef.map.IOReadPort;
import jef.map.IOWritePort;
import jef.map.InitHandler;
import jef.map.InputPort;
import jef.map.InterruptHandler;
import jef.map.MemoryReadAddress;
import jef.map.MemoryWriteAddress;
import jef.map.NoFunction;
import jef.map.ReadHandler;
import jef.map.ReadMap;
import jef.map.VoidFunction;
import jef.map.WriteHandler;
import jef.map.WriteMap;
import jef.sound.SoundChip;
import jef.util.RomLoader;
import jef.video.Eof_callback;
import jef.video.GfxDecodeInfo;
import jef.video.GfxLayout;
import jef.video.PaletteInitializer;
import jef.video.VideoEmulator;
import jef.video.VideoFinalizer;
import jef.video.VideoInitializer;
import jef.video.VideoRenderer;

public abstract class MAMEDriver implements Driver, MAMEConstants {
   protected static final VoidFunction NOP = new NoFunction();
   protected static final GfxDecodeInfo[] NO_GFX_DECODE_INFO = null;
   protected static final SoundChip[] noSound = null;
   private String DriverName = "";
   private int Fcolorram = -1;
   private int Fpaletteram = -1;
   private int Fpaletteram_2 = -1;
   private int Fspriteram = -1;
   private int Fspriteram_2 = -1;
   private int Fspriteram_2_size = -1;
   private int Fspriteram_3 = -1;
   private int Fspriteram_3_size = -1;
   private int Fspriteram_size = -1;
   private int Fvideoram = -1;
   private int Fvideoram_size = -1;
   private char[][] REGIONS = new char[21][];
   public boolean bInfo = false;
   protected boolean bModify = false;
   public URL base_URL;
   private int cpu_count = 0;
   protected int cpu_num = -1;
   private CpuDriver[] cpus = new CpuDriver[8];
   private int cur_region = 0;
   public String driver_clone;
   public String driver_date;
   public String driver_name;
   public String driver_prod;
   public boolean driver_sound;
   private int gdi_count = 0;
   private GfxDecodeInfo[] gdis = new GfxDecodeInfo[8];
   private int inp_count = 0;
   private InputPort[] inps = new InputPort[10];
   protected ReadHandler input_port_0_r;
   protected ReadHandler input_port_1_r;
   protected ReadHandler input_port_2_r;
   protected ReadHandler input_port_3_r;
   protected ReadHandler input_port_4_r;
   protected ReadHandler input_port_5_r;
   protected ReadHandler input_port_6_r;
   protected ReadHandler input_port_7_r;
   protected ReadHandler input_port_8_r;
   protected ReadHandler input_port_9_r;
   private IOReadPort[] ior = new IOReadPort[8];
   private int ior_count = 0;
   private IOWritePort[] iow = new IOWritePort[8];
   private int iow_count = 0;
   public EmulatorProperties md;
   private MemoryReadAddress[] mra = new MemoryReadAddress[8];
   private int mra_count = 0;
   private MemoryWriteAddress[] mwa = new MemoryWriteAddress[8];
   private int mwa_count = 0;
   public int[] properties = new int[16];
   protected RomLoader romLoader = new RomLoader();
   private int snd_count = 0;
   private SoundChip[] soundChips = new SoundChip[8];
   private int soundLatch = 0;
   protected final ReadHandler soundlatch_r = new Soundlatch_r();
   protected final WriteHandler soundlatch_w = new Soundlatch_w((Soundlatch_w)null);
   private VideoEmulator videoEmulator;

   protected int BADCRC(int var1) {
      return var1;
   }

   protected String DEF_STR2(int var1) {
      return DEF_STR[var1];
   }

   protected void GAME(int var1, boolean var2, int var3, boolean var4, boolean var5, int var6, int var7, String var8, String var9) {
      this.GAME(Integer.toString(var1), var2, (String)null, var4, var5, true, var7, var8, var9);
   }

   protected void GAME(int var1, boolean var2, int var3, boolean var4, boolean var5, InitHandler var6, int var7, String var8, String var9) {
      this.GAME(Integer.toString(var1), var2, (String)null, var4, var5, var6, var7, var8, var9);
   }

   protected void GAME(int var1, boolean var2, int var3, boolean var4, boolean var5, boolean var6, int var7, String var8, String var9) {
      this.GAME(Integer.toString(var1), var2, (String)null, var4, var5, var6, var7, var8, var9);
   }

   protected void GAME(int var1, boolean var2, String var3, boolean var4, boolean var5, int var6, int var7, String var8, String var9) {
      this.GAME(Integer.toString(var1), var2, var3, var4, var5, true, var7, var8, var9);
   }

   protected void GAME(int var1, boolean var2, String var3, boolean var4, boolean var5, InitHandler var6, int var7, String var8, String var9) {
      this.GAME(Integer.toString(var1), var2, var3, var4, var5, var6, var7, var8, var9);
   }

   protected void GAME(int var1, boolean var2, String var3, boolean var4, boolean var5, boolean var6, int var7, String var8, String var9) {
      this.GAME(Integer.toString(var1), var2, var3, var4, var5, var6, var7, var8, var9);
   }

   protected void GAME(int var1, boolean var2, String var3, InputPort[] var4, boolean var5, int var6, String var7, String var8) {
      if(!this.bInfo) {
         System.out.println("Starting...");
         System.out.println(var8);
         System.out.println(Integer.toString(var1) + " - " + var7);
         this.md.ROT = var6;
         this.md.input = var4;
         this.md.ve = this.videoEmulator;
      } else {
         this.bInfo = true;
         this.md.info = this.bInfo;
         this.driver_date = Integer.toString(var1);
         this.driver_prod = var7;
         this.driver_name = var8;
         this.driver_clone = var3;
         if(this.md.soundChips != noSound) {
            var2 = true;
         } else {
            var2 = false;
         }

         this.driver_sound = var2;
         this.md.ROT = var6;
      }

   }

   protected void GAME(int var1, boolean var2, InputPort[] var3, boolean var4, int var5, String var6, String var7) {
      if(!this.bInfo) {
         System.out.println("Starting...");
         System.out.println(var7);
         System.out.println(Integer.toString(var1) + " - " + var6);
         this.md.ROT = var5;
         this.md.input = var3;
         this.md.ve = this.videoEmulator;
      } else {
         this.bInfo = true;
         this.md.info = this.bInfo;
         this.driver_date = Integer.toString(var1);
         this.driver_prod = var6;
         this.driver_name = var7;
         this.driver_clone = null;
         if(this.md.soundChips != noSound) {
            var2 = true;
         } else {
            var2 = false;
         }

         this.driver_sound = var2;
         this.md.ROT = var5;
      }

   }

   protected void GAME(String var1, boolean var2, int var3, boolean var4, boolean var5, int var6, int var7, String var8, String var9) {
      this.GAME(var1, var2, (String)null, var4, var5, true, var7, var8, var9);
   }

   protected void GAME(String var1, boolean var2, int var3, boolean var4, boolean var5, boolean var6, int var7, String var8, String var9) {
      this.GAME(var1, var2, (String)null, var4, var5, var6, var7, var8, var9);
   }

   protected void GAME(String var1, boolean var2, String var3, boolean var4, boolean var5, int var6, int var7, String var8, String var9) {
      this.GAME(var1, var2, var3, var4, var5, true, var7, var8, var9);
   }

   protected void GAME(String var1, boolean var2, String var3, boolean var4, boolean var5, InitHandler var6, int var7, String var8, String var9) {
      this.GAME(var1, var2, var3, var4, var5, true, var7, var8, var9);
      var6.init();
   }

   protected void GAME(String var1, boolean var2, String var3, boolean var4, boolean var5, boolean var6, int var7, String var8, String var9) {
      if(!this.bInfo) {
         this.romLoader.setZip(this.DriverName);
         this.romLoader.setParentZip(var3);
         this.romLoader.loadZip(this.base_URL);
         System.out.println("Starting...");
         System.out.println(var9);
         System.out.println(var1 + " - " + var8);
         this.md.ROT = var7;
         InputPort[] var10 = new InputPort[this.inp_count];

         for(var7 = 0; var7 < this.inp_count; ++var7) {
            var10[var7] = this.inps[var7];
         }

         for(var7 = this.inp_count; var7 < 10; ++var7) {
            this.inps[var7] = null;
         }

         this.md.input = var10;
         this.md.ve = this.videoEmulator;
      } else {
         this.bInfo = true;
         this.md.info = this.bInfo;
         this.driver_date = var1;
         this.driver_prod = var8;
         this.driver_name = var9;
         this.driver_clone = var3;
         if(this.snd_count > 0) {
            var2 = true;
         } else {
            var2 = false;
         }

         this.driver_sound = var2;
         this.md.ROT = var7;
      }

   }

   protected void GDI_ADD(int var1) {
   }

   protected void GDI_ADD(int var1, int var2, GfxLayout var3, int var4, int var5) {
      this.gdis[this.gdi_count] = new GfxDecodeInfo(this.REGIONS[var1], var2, var3, var4, var5);
      ++this.gdi_count;
   }

   protected void GDI_ADD(int var1, int var2, int[][] var3, int var4, int var5) {
      this.gdis[this.gdi_count] = new GfxDecodeInfo(this.REGIONS[var1], var2, var3, var4, var5);
      ++this.gdi_count;
   }

   protected void Helpers(int var1, int var2, int var3, int var4) {
      switch(var1) {
      case 0:
         this.Fvideoram = var2;
         break;
      case 1:
         this.Fcolorram = var2;
         break;
      case 2:
         this.Fspriteram = var2;
         break;
      case 3:
         this.Fspriteram_2 = var2;
         break;
      case 4:
         this.Fspriteram_3 = var2;
         break;
      case 5:
         this.Fpaletteram = var2;
         break;
      case 6:
         this.Fpaletteram_2 = var2;
      }

      switch(var3) {
      case 0:
         this.Fvideoram_size = var4;
         break;
      case 1:
         this.Fspriteram_size = var4;
         break;
      case 2:
         this.Fspriteram_2_size = var4;
         break;
      case 3:
         this.Fspriteram_3_size = var4;
      }

   }

   protected void MDRV_COLORTABLE_LENGTH(int var1) {
      this.mdalloc();
      this.md.col = var1;
   }

   protected void MDRV_CPU_ADD(int var1, int var2) {
      Object var3 = null;
      switch(var1) {
      case 0:
         var3 = new Z80();
         break;
      case 1:
         var3 = new I8080();
         break;
      case 2:
         var3 = new M6809();
         break;
      case 3:
         var3 = new jM68k();
      }

      this.MDRV_CPU_ADD((Cpu)var3, var2);
   }

   protected void MDRV_CPU_ADD(Cpu var1, int var2) {
      this.ior[this.cpu_count] = new IOReadPort();
      this.iow[this.cpu_count] = new IOWritePort();
      this.cpus[this.cpu_count] = new CpuDriver(var1, var2, (ReadMap)null, (WriteMap)null, this.ior[this.cpu_count], this.iow[this.cpu_count], (InterruptHandler)null, 0);
      ++this.cpu_count;
      ++this.cur_region;
   }

   protected void MDRV_CPU_ADD_TAG(String var1, int var2, int var3) {
      Object var4 = null;
      switch(var2) {
      case 0:
         var4 = new Z80();
         break;
      case 1:
         var4 = new I8080();
         break;
      case 2:
         var4 = new M6809();
         break;
      case 3:
         var4 = new jM68k();
      }

      ((Cpu)var4).setTag(var1);
      this.MDRV_CPU_ADD((Cpu)var4, var3);
   }

   protected void MDRV_CPU_FLAGS(int var1) {
      CpuDriver var3 = this.cpus[this.cpu_count - 1];
      boolean var2;
      if((var1 & 1) != 0) {
         var2 = true;
      } else {
         var2 = false;
      }

      var3.isAudioCpu = var2;
   }

   protected void MDRV_CPU_MEMORY(boolean var1, boolean var2) {
      this.cpus[this.cpu_count - 1].mra = this.mra[this.cpu_count - 1];
      this.cpus[this.cpu_count - 1].mwa = this.mwa[this.cpu_count - 1];
   }

   protected void MDRV_CPU_MODIFY(String var1) {
      this.bModify = false;
      this.cpu_num = -1;

      while(this.cpu_num == -1 && this.cpu_count < 0) {
         if(this.cpus[0].cpu.getTag().compareTo(var1) == 0) {
            this.cpu_num = 0;
         }
      }

      if(this.cpu_num != -1) {
         this.bModify = true;
      }

   }

   protected void MDRV_CPU_PORTS(int var1, boolean var2) {
      this.cpus[this.cpu_count - 1].iow = this.iow[this.iow_count - 1];
   }

   protected void MDRV_CPU_PORTS(boolean var1, int var2) {
      this.cpus[this.cpu_count - 1].ior = this.ior[this.ior_count - 1];
   }

   protected void MDRV_CPU_PORTS(boolean var1, boolean var2) {
      this.cpus[this.cpu_count - 1].ior = this.ior[this.ior_count - 1];
      this.cpus[this.cpu_count - 1].iow = this.iow[this.iow_count - 1];
   }

   protected void MDRV_CPU_VBLANK_INT(InterruptHandler var1, int var2) {
      if(this.bModify) {
         this.cpus[this.cpu_num].irh = var1;
         this.cpus[this.cpu_num].ipf = var2;
      } else {
         this.cpus[this.cpu_count - 1].irh = var1;
         this.cpus[this.cpu_count - 1].ipf = var2;
      }

   }

   protected void MDRV_FRAMES_PER_SECOND(double var1) {
      this.mdalloc();
      this.md.fps = (int)var1;
   }

   protected void MDRV_FRAMES_PER_SECOND(int var1) {
      this.mdalloc();
      this.md.fps = var1;
   }

   protected void MDRV_GFXDECODE(boolean var1) {
      this.mdalloc();
      GfxDecodeInfo[] var3 = new GfxDecodeInfo[this.gdi_count];

      for(int var2 = 0; var2 < this.gdi_count; ++var2) {
         var3[var2] = this.gdis[var2];
      }

      this.md.gfx = var3;
   }

   protected void MDRV_IMPORT_FROM(boolean var1) {
   }

   protected void MDRV_INTERLEAVE(int var1) {
      this.mdalloc();
      this.md.spf = var1;
   }

   protected void MDRV_PALETTE_INIT(PaletteInitializer var1) {
      this.mdalloc();
      this.md.initProms = var1;
   }

   protected void MDRV_PALETTE_LENGTH(int var1) {
      this.mdalloc();
      this.md.pal = var1;
      this.md.col = var1;
   }

   protected void MDRV_SCREEN_SIZE(int var1, int var2) {
      this.mdalloc();
      this.md.w = var1;
      this.md.h = var2;
   }

   protected void MDRV_SOUND_ADD(SoundChip var1) {
      this.soundChips[this.snd_count] = var1;
      ++this.snd_count;
   }

   protected void MDRV_VBLANK_DURATION(int var1) {
      this.mdalloc();
      this.md.vbd = var1;
   }

   protected void MDRV_VIDEO_ATTRIBUTES(int var1) {
      this.mdalloc();
      this.md.videoFlags = var1;
   }

   protected void MDRV_VIDEO_EOF(Eof_callback var1) {
      this.mdalloc();
      this.md.eof_callback = var1;
   }

   protected void MDRV_VIDEO_START(VideoInitializer var1) {
      this.mdalloc();
      this.md.vh_start = var1;
   }

   protected void MDRV_VIDEO_UPDATE(VideoRenderer var1) {
      this.mdalloc();
      this.md.vh_screenrefresh = var1;
   }

   protected void MDRV_VISIBLE_AREA(int var1, int var2, int var3, int var4) {
      this.mdalloc();
      this.md.visible = new int[]{var1, var2, var3, var4};
   }

   protected void MR_ADD(int var1, int var2, int var3) {
      this.mra[this.mra_count - 1].setMR(var1, var2, var3);
   }

   protected void MR_ADD(int var1, int var2, ReadHandler var3) {
      this.mra[this.mra_count - 1].set(var1, var2, var3);
   }

   protected void MR_START() {
      MemoryReadAddress[] var2 = this.mra;
      int var1 = this.mra_count;
      this.mra_count = var1 + 1;
      var2[var1] = new MemoryReadAddress(this.REGIONS[this.cur_region - 1]);
   }

   protected void MR_START(int var1, int var2, int var3) {
      this.MR_START();
      this.MR_ADD(var1, var2, var3);
   }

   protected void MR_START(int var1, int var2, ReadHandler var3) {
      this.MR_START();
      this.MR_ADD(var1, var2, var3);
   }

   protected void MW_ADD(int var1, int var2, int var3) {
      this.mwa[this.mwa_count - 1].setMW(var1, var2, var3);
   }

   protected void MW_ADD(int var1, int var2, int var3, int var4) {
      this.mwa[this.mwa_count - 1].setMW(var1, var2, var3);
      this.Helpers(var4, var1, -1, 0);
   }

   protected void MW_ADD(int var1, int var2, int var3, int var4, int var5) {
      this.mwa[this.mwa_count - 1].setMW(var1, var2, var3);
      this.Helpers(var4, var1, var5, var2 + 1 - var1);
   }

   protected void MW_ADD(int var1, int var2, int var3, int[] var4) {
      this.mwa[this.mwa_count - 1].setMW(var1, var2, var3);
      var4[0] = var1;
   }

   protected void MW_ADD(int var1, int var2, int var3, int[] var4, int[] var5) {
      this.mwa[this.mwa_count - 1].setMW(var1, var2, var3);
      var4[0] = var1;
      var5[0] = var2 + 1 - var1;
   }

   protected void MW_ADD(int var1, int var2, WriteHandler var3) {
      this.mwa[this.mwa_count - 1].set(var1, var2, var3);
   }

   protected void MW_ADD(int var1, int var2, WriteHandler var3, int var4) {
      this.mwa[this.mwa_count - 1].set(var1, var2, var3);
      this.Helpers(var4, var1, -1, 0);
   }

   protected void MW_ADD(int var1, int var2, WriteHandler var3, int var4, int var5) {
      this.mwa[this.mwa_count - 1].set(var1, var2, var3);
      this.Helpers(var4, var1, var5, var2 + 1 - var1);
   }

   protected void MW_ADD(int var1, int var2, WriteHandler var3, int[] var4) {
      this.mwa[this.mwa_count - 1].set(var1, var2, var3);
      var4[0] = var1;
   }

   protected void MW_ADD(int var1, int var2, WriteHandler var3, int[] var4, int[] var5) {
      this.mwa[this.mwa_count - 1].set(var1, var2, var3);
      var4[0] = var1;
      var5[0] = var2 + 1 - var1;
   }

   protected void MW_START() {
      MemoryWriteAddress[] var2 = this.mwa;
      int var1 = this.mwa_count;
      this.mwa_count = var1 + 1;
      var2[var1] = new MemoryWriteAddress(this.REGIONS[this.cur_region - 1]);
   }

   protected void MW_START(int var1, int var2, int var3) {
      this.MW_START();
      this.MW_ADD(var1, var2, var3);
   }

   protected void MW_START(int var1, int var2, int var3, int var4) {
      this.MW_START();
      this.MW_ADD(var1, var2, var3, var4);
   }

   protected void MW_START(int var1, int var2, int var3, int var4, int var5) {
      this.MW_START();
      this.MW_ADD(var1, var2, var3, var4, var5);
   }

   protected void MW_START(int var1, int var2, WriteHandler var3) {
      this.MW_START();
      this.MW_ADD(var1, var2, var3);
   }

   protected void MW_START(int var1, int var2, WriteHandler var3, int var4) {
      this.MW_START();
      this.MW_ADD(var1, var2, var3, var4);
   }

   protected void MW_START(int var1, int var2, WriteHandler var3, int var4, int var5) {
      this.MW_START();
      this.MW_ADD(var1, var2, var3, var4, var5);
   }

   protected void PORT_ANALOG(int var1, int var2, int var3, int var4, int var5, int var6, int var7) {
      this.inps[this.inp_count - 1].setAnalog(var1, var2, var3, var4, var5, var6, var7);
   }

   protected void PORT_BIT(int var1, int var2, int var3) {
      this.inps[this.inp_count - 1].setBit(var1, var2, var3);
   }

   protected void PORT_BITX(int var1, int var2, int var3, String var4, int var5, int var6) {
      this.inps[this.inp_count - 1].setDipName(var1, var2, var4);
   }

   protected void PORT_BIT_IMPULSE(int var1, int var2, int var3, int var4) {
      this.inps[this.inp_count - 1].setBitImpulse(var1, var2, var3, var4);
   }

   protected void PORT_DIPNAME(int var1, int var2, String var3) {
      this.inps[this.inp_count - 1].setDipName(var1, var2, var3);
   }

   protected void PORT_DIPSETTING(int var1, String var2) {
      this.inps[this.inp_count - 1].setDipSetting(var1, var2);
   }

   protected void PORT_SERVICE(int var1, int var2) {
      this.inps[this.inp_count - 1].setService(var1, var2);
   }

   protected void PORT_START() {
      ++this.inp_count;
   }

   protected void PR_ADD(int var1, int var2, ReadHandler var3) {
      if(this.ior[this.ior_count - 1] != null) {
         this.ior[this.ior_count - 1].set(var1, var2, var3);
      }

   }

   protected void PR_START() {
      this.ior[this.ior_count] = null;
      IOReadPort[] var2 = this.ior;
      int var1 = this.ior_count;
      this.ior_count = var1 + 1;
      var2[var1] = new IOReadPort();
   }

   protected void PR_START(int var1, int var2, ReadHandler var3) {
      this.PR_START();
      this.PR_ADD(var1, var2, var3);
   }

   protected void PW_ADD(int var1, int var2, WriteHandler var3) {
      if(this.iow[this.iow_count - 1] != null) {
         this.iow[this.iow_count - 1].set(var1, var2, var3);
      }

   }

   protected void PW_START() {
      this.iow[this.iow_count] = null;
      IOWritePort[] var2 = this.iow;
      int var1 = this.iow_count;
      this.iow_count = var1 + 1;
      var2[var1] = new IOWritePort();
   }

   protected void PW_START(int var1, int var2, WriteHandler var3) {
      this.PW_START();
      this.PW_ADD(var1, var2, var3);
   }

   protected int RGN_FRAC(int var1, int var2) {
      return Integer.MIN_VALUE | (var1 & 15) << 27 | (var2 & 15) << 23;
   }

   protected void ROM_CONTINUE(int var1, int var2) {
      this.romLoader.continueROM(var1, var2);
   }

   protected void ROM_LOAD(String var1, int var2, int var3, int var4) {
      this.romLoader.loadROM(var1, var2, var3, var4);
   }

   protected void ROM_LOAD16_BYTE(String var1, int var2, int var3, int var4) {
      this.romLoader.loadROM(var1, var2, var3, var4, 1);
   }

   protected void ROM_REGION(int var1, int var2, int var3) {
      if(this.REGIONS[var2] == null) {
         this.REGIONS[var2] = new char[var1];
      }

      this.romLoader.setMemory(this.REGIONS[var2]);
      RomLoader var5 = this.romLoader;
      boolean var4;
      if((var3 & 2) != 0) {
         var4 = true;
      } else {
         var4 = false;
      }

      var5.setInvert(var4);
   }

   protected void cpu_cause_interrupt(int var1, int var2) {
      this.cpus[var1].cpu.interrupt(var2, true);
   }

   protected void cpu_set_irq_line(int var1, int var2, int var3) {
      this.cpus[var1].cpu.interrupt(var2, true);
   }

   protected void cpu_setbank(int var1, int var2) {
      this.mra[var1 - 1].setBankAddress(var1, var2);
   }

   public String getDriverInfo() {
      return "AbstractDriver";
   }

   public String getGameInfo() {
      return "AbstractDriver supports nothing at all ;o)";
   }

   public Emulator getMachine(URL var1, String var2) {
      this.base_URL = var1;
      this.DriverName = var2;
      this.cur_region = 0;
      this.inp_count = 0;
      this.gdi_count = 0;
      this.cpu_count = 0;

      for(int var3 = 0; var3 < 10; ++var3) {
         this.inps[var3] = new InputPort();
      }

      this.input_port_0_r = this.inps[0];
      this.input_port_1_r = this.inps[1];
      this.input_port_2_r = this.inps[2];
      this.input_port_3_r = this.inps[3];
      this.input_port_4_r = this.inps[4];
      this.input_port_5_r = this.inps[5];
      this.input_port_6_r = this.inps[6];
      this.input_port_7_r = this.inps[7];
      this.input_port_8_r = this.inps[8];
      this.input_port_9_r = this.inps[9];
      return null;
   }

   public Emulator getMachineInfo(String var1) {
      this.romLoader.noLoading();
      this.bInfo = true;
      return this.getMachine((URL)null, var1);
   }

   public int getProperty(int var1) {
      return this.properties[var1];
   }

   protected int getSoundLatch() {
      return this.soundLatch;
   }

   protected void install_mem_read_handler(int var1, int var2, int var3, ReadHandler var4) {
      this.mra[var1].set(var2, var3, var4);
   }

   protected void mdalloc() {
      if(this.md == null) {
         CpuDriver[] var3 = new CpuDriver[this.cpu_count];

         int var1;
         for(var1 = 0; var1 < this.cpu_count; ++var1) {
            var3[var1] = this.cpus[var1];
         }

         SoundChip[] var2 = new SoundChip[this.snd_count];

         for(var1 = 0; var1 < this.snd_count; ++var1) {
            var2[var1] = this.soundChips[var1];
         }

         if(!this.bInfo) {
            System.out.println("cpus :" + var3.length);
            System.out.println("sndchips :" + var2.length);
         }

         this.md = new EmulatorProperties(var3, 0, 0, 0, NOP, 0, 0, (int[])null, (GfxDecodeInfo[])null, 0, 0, (VideoEmulator)null, (PaletteInitializer)null, 0, (Eof_callback)null, (VideoInitializer)null, (VideoFinalizer)null, (VideoRenderer)null, var2);
         if(this.Fvideoram != -1) {
            MAMEVideo.videoram = this.Fvideoram;
         }

         if(this.Fcolorram != -1) {
            MAMEVideo.colorram = this.Fcolorram;
         }

         if(this.Fspriteram != -1) {
            MAMEVideo.spriteram = this.Fspriteram;
         }

         if(this.Fspriteram_2 != -1) {
            MAMEVideo.spriteram_2 = this.Fspriteram_2;
         }

         if(this.Fspriteram_3 != -1) {
            MAMEVideo.spriteram_3 = this.Fspriteram_3;
         }

         if(this.Fpaletteram != -1) {
            MAMEVideo.paletteram = this.Fpaletteram;
         }

         if(this.Fpaletteram_2 != -1) {
            MAMEVideo.paletteram_2 = this.Fpaletteram_2;
         }

         if(this.Fvideoram_size != -1) {
            MAMEVideo.videoram_size = this.Fvideoram_size;
         }

         if(this.Fspriteram_size != -1) {
            MAMEVideo.spriteram_size = this.Fspriteram_size;
         }

         if(this.Fspriteram_2_size != -1) {
            MAMEVideo.spriteram_2_size = this.Fspriteram_2_size;
         }

         if(this.Fspriteram_3_size != -1) {
            MAMEVideo.spriteram_3_size = this.Fspriteram_3_size;
         }

         this.md.setMemRegions(this.REGIONS);
      }

   }

   public char[] memory_region(int var1) {
      return this.REGIONS[var1];
   }

   public int memory_region_length(int var1) {
      return this.REGIONS[var1].length;
   }

   public void setProperty(int var1, int var2) {
      this.properties[var1] = var2;
   }

   protected void setVideoEmulator(VideoEmulator var1) {
      this.videoEmulator = var1;
   }

   protected void soundlatch_w(int var1, int var2) {
      this.soundLatch = var2;
   }

   protected class Soundlatch_r implements ReadHandler {
      public int read(int var1) {
         return MAMEDriver.this.soundLatch;
      }
   }

   private class Soundlatch_w implements WriteHandler {
      private Soundlatch_w() {
      }

      // $FF: synthetic method
      Soundlatch_w(Soundlatch_w var2) {
         this();
      }

      public void write(int var1, int var2) {
         MAMEDriver.this.soundLatch = var2;
      }
   }
}
