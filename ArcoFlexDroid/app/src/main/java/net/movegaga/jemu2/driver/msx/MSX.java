package net.movegaga.jemu2.driver.msx;

import net.arcoflexdroid.R;
import net.movegaga.jemu2.driver.Driver;
import net.movegaga.jemu2.driver.msx.enh.LCBNemesis;
import net.movegaga.jemu2.driver.msx.v9938.V9938;

import java.net.URL;

import jef.cpu.z80.Z80;
import jef.cpuboard.CpuDriver;
import jef.machine.BasicEmulator;
import jef.machine.Emulator;
import jef.machine.EmulatorProperties;
import jef.map.IOReadPort;
import jef.map.IOWritePort;
import jef.map.InterruptHandler;
import jef.map.MemoryReadAddress;
import jef.map.MemoryWriteAddress;
import jef.map.ReadHandler;
import jef.map.Register;
import jef.map.WriteHandler;
import jef.sound.Settings;
import jef.sound.SoundChip;
import jef.sound.chip.AY8910;
import jef.sound.chip.k051649.K051649;
import jef.sound.chip.k051649.K051649S;
import jef.sound.chip.ym2413.YM2413;
import jef.sound.chip.ym2413.YM2413S;
import jef.util.ByteArray;
import jef.util.ConfigFile;
import jef.util.MachineState;
import jef.util.RomLoader;
import jef.util.StateUtils;
import jef.video.BitMap;
import jef.video.VideoRenderer;

public class MSX extends BasicEmulator implements Driver, VideoRenderer {
   public static final String ASCII_16K = "ASCII_16K";
   public static final String ASCII_8K = "ASCII_8K";
   public static final String CHAR_BCD = Integer.toHexString(16777215);
   public static final int CPU_CLOCK_SPEED = 3579545;
   public static final String KONAMI_8K = "KONAMI_8K";
   public static final String KONAMI_8K_SCC = "KONAMI_8K_SCC";
   public static final String KONAMI_8K_SCC2 = "KONAMI_8K_SCC2";
   public static final String KONAMI_8K_SCCP = "KONAMI_8K_SCCP";
   public static final String KONAMI_8K_SCCP2 = "KONAMI_8K_SCCP2";
   public static final String NORMAL = "NORMAL";
   public static final String RTYPE = "RTYPE";
   public static final String[][] SCORE_PARSERS;
   public int FPS;
   String[][] ROM_LIST;
   final boolean STEREO;
   protected BitMap backbuffer;
   Z80 cpu;
   char[] crtA;
   char[] crtB;
   private String driver;
   char[] ext;
   FDC fdc;
   IOReadPort ior;
   IOWritePort iow;
   boolean keyf6;
   boolean keyf7;
   boolean keyf8;
   MapperAscii16K mapperAscii16K;
   MapperAscii8K mapperAscii8K;
   MapperKonami8K mapperKonami8K;
   MapperKonami8Kscc mapperKonami8Kscc;
   MapperKonami8Ksccp mapperKonami8Ksccp;
   MapperMirrored mapperMirrored;
   MapperRType mapperRType;
   MemoryReadAddress mra;
   YM2413 music;
   YM2413S musics;
   MemoryWriteAddress mwa;
   PPI ppi;
   AY8910 psg;
   private final char[] ram;
   public MapperRAM ramMapper;
   char[] rom;
   RTC rtc;
   K051649 scc;
   K051649S sccs;
   MemoryLayout[] slot;
   int sslot;
   private MachineState state;
   public V9938 vdp;

   static {
      String var3 = CHAR_BCD;
      String var2 = CHAR_BCD;
      String var4 = CHAR_BCD;
      String[] var7 = new String[]{"msxe_nemesis2", CHAR_BCD, "0", "0", "21f4", "1", "21f1", "300", "100"};
      String var1 = CHAR_BCD;
      String var6 = CHAR_BCD;
      String var0 = CHAR_BCD;
      String var5 = CHAR_BCD;
      SCORE_PARSERS = new String[][]{{"msx1_nemesis", var3, "0", "0", "2056", "1", "2054", "300", "100"}, {"msx1_nemsccp", var2, "0", "0", "2056", "1", "2054", "300", "100"}, {"msx1_nemesis2", var4, "0", "0", "21f4", "1", "21f1", "300", "100"}, var7, {"msx2_nemesis3", var1, "0", "0", "2355", "1", "2351", "300", "100"}, {"msx1_salamand", var6, "0", "0", "2274", "1", "2271", "300", "100"}, {"msx1_parodius", var0, "0", "0", "2234", "1", "2231", "300", "100"}, {"msx1_penguin", var5, "0", "0", "2085", "1", "2083", "300", "1"}};
   }

   public MSX() {
      this.STEREO = Settings.STEREO_ENHANCED;
      this.ROM_LIST = (new ConfigFile(R.raw.msxroms)).getContents();
      this.FPS = 50;
      this.slot = new MemoryLayout[4];
      this.rom = new char['耀'];
      this.crtA = new char[1048576];
      this.crtB = new char[65536];
      this.ext = new char[65536];
      this.ram = new char[262144];
      this.ramMapper = new MapperRAM(this.ram);
      this.mwa = new MemoryWriteAddress(65536);
      this.mra = new MemoryReadAddress(65536);
      this.iow = new IOWritePort();
      this.ior = new IOReadPort();
      this.cpu = new Z80();
      this.vdp = new V9938(this.cpu, 0);
      this.psg = new AY8910(1, 1789772);
      this.music = new YM2413(3579545);
      this.musics = new YM2413S(3579545);
      this.scc = new K051649(1789772, 0.6F);
      this.sccs = new K051649S(1789772, 0.6F);
      this.rtc = new RTC();
      this.fdc = new FDC(this.cpu, this);
      this.initMemorySlots();
      this.initMemoryMap();
      this.ppi = new PPI(this.slot);
      this.initPortMap();
      this.mapperMirrored = new MapperMirrored(this.crtA);
      this.mapperKonami8K = new MapperKonami8K(this.crtA);
      if(this.STEREO) {
         this.mapperKonami8Kscc = new MapperKonami8Kscc(this.crtA, this.sccs);
         this.mapperKonami8Ksccp = new MapperKonami8Ksccp(this.crtA, this.sccs);
      } else {
         this.mapperKonami8Kscc = new MapperKonami8Kscc(this.crtA, this.scc);
      }

      this.mapperAscii8K = new MapperAscii8K(this.crtA);
      this.mapperAscii16K = new MapperAscii16K(this.crtA);
      this.mapperRType = new MapperRType(this.crtA);
   }

   private void initState() {
      this.state = new MachineState(this.driver);
      this.state.add(this.ram);
      ByteArray var2 = new ByteArray();
      var2.add8((byte)this.slot[0].pslot);
      var2.add8((byte)this.slot[1].pslot);
      var2.add8((byte)this.slot[2].pslot);
      var2.add8((byte)this.slot[3].pslot);
      var2.add8((byte)this.slot[0].sslot);
      var2.add8((byte)this.slot[1].sslot);
      var2.add8((byte)this.slot[2].sslot);
      var2.add8((byte)this.slot[3].sslot);
      var2.add8((byte)this.slot[0].cursslot);
      var2.add8((byte)this.slot[1].cursslot);
      var2.add8((byte)this.slot[2].cursslot);
      var2.add8((byte)this.slot[3].cursslot);
      var2.add8((byte)this.sslot);
      var2.add8((byte)this.ramMapper.pageMask);
      var2.add8((byte)this.ramMapper.page[0]);
      var2.add8((byte)this.ramMapper.page[1]);
      var2.add8((byte)this.ramMapper.page[2]);
      var2.add8((byte)this.ramMapper.page[3]);
      var2.add8((byte)this.ppi.pslot3);
      var2.add8((byte)this.ppi.portA);
      var2.add8((byte)this.ppi.portC);
      var2.add8((byte)this.ppi.portD);
      var2.add8((byte)this.mapperAscii16K.bank0);
      var2.add8((byte)this.mapperAscii16K.bank1);
      var2.add8((byte)this.mapperAscii8K.bank0);
      var2.add8((byte)this.mapperAscii8K.bank1);
      var2.add8((byte)this.mapperAscii8K.bank2);
      var2.add8((byte)this.mapperAscii8K.bank3);
      var2.add8((byte)this.mapperKonami8K.bank0);
      var2.add8((byte)this.mapperKonami8K.bank1);
      var2.add8((byte)this.mapperKonami8K.bank2);
      var2.add8((byte)this.mapperKonami8K.bank3);
      var2.add8((byte)this.mapperKonami8Kscc.bank0);
      var2.add8((byte)this.mapperKonami8Kscc.bank1);
      var2.add8((byte)this.mapperKonami8Kscc.bank2);
      var2.add8((byte)this.mapperKonami8Kscc.bank3);

      for(int var1 = 0; var1 < this.mapperKonami8Kscc.sccRAM.length; ++var1) {
         var2.add8((byte)this.mapperKonami8Kscc.sccRAM[var1]);
      }

      this.state.add(var2);
      this.state.add(StateUtils.getState(this.cpu));
      this.state.add(StateUtils.getState(this.vdp));
      this.state.add(StateUtils.getState(this.vdp.command));
   }

   private void loadState() {
      // $FF: Couldn't be decompiled
   }

   private void saveState() {
      // $FF: Couldn't be decompiled
   }

   public Emulator createEmulator(URL var1, String var2) {
      this.driver = var2;
      boolean var4;
      if(var2.indexOf("_dsk_") >= 0) {
         var4 = true;
      } else {
         var4 = false;
      }

      if(var2.indexOf("_60Hz_") >= 0) {
         this.FPS = 60;
      }

      this.initState();
      this.cpu.IRQ_SELF_ACK = false;
      EmulatorProperties var8 = new EmulatorProperties();
      var8.setCpuDriver(new CpuDriver(this.cpu, 3579545, this.mra, this.mwa, this.ior, this.iow, (InterruptHandler)null, 0));
      var8.setFPS(this.FPS);
      var8.setVideoDimensions(272, 208);
      var8.setVisibleArea(0, 271, 0, 211);
      var8.setVideoUpdater(this);
      boolean var6 = false;

      boolean var3;
      for(int var5 = 0; var5 < this.ROM_LIST.length; var6 = var3) {
         var3 = var6;
         if(this.ROM_LIST[var5][0].equals(var2)) {
            String var9 = this.ROM_LIST[var5][1];
            String var10 = this.ROM_LIST[var5][2];
            String var11 = this.ROM_LIST[var5][3];
            int var13 = (int)Long.parseLong(this.ROM_LIST[var5][4], 16);
            int var7 = Integer.parseInt(this.ROM_LIST[var5][5], 16);
            if(var4) {
               this.fdc.addDisk(var10, var13, var7);
            }

            if(var9.equals("NORMAL")) {
               if(!var4) {
                  this.loadCrtA(var1, var10, var11, var13, var7);
               }

               this.slot[1].installMapper(1, this.mapperMirrored);
               this.slot[2].installMapper(1, this.mapperMirrored);
               var3 = var6;
            } else {
               if(!var4) {
                  this.loadCrtAM(var1, var10, var11, var13, var7);
               }

               if(var9.equals("KONAMI_8K")) {
                  this.installKonami8K();
                  var3 = var6;
               } else if(var9.equals("KONAMI_8K_SCC")) {
                  this.installKonami8Kscc();
                  var3 = true;
               } else if(var9.equals("KONAMI_8K_SCC2")) {
                  this.installKonami8Kscc2();
                  var3 = true;
               } else if(var9.equals("KONAMI_8K_SCCP")) {
                  this.installKonami8Ksccp();
                  var3 = true;
               } else if(var9.equals("KONAMI_8K_SCCP2")) {
                  this.installKonami8Ksccp2();
                  var3 = true;
               } else if(var9.equals("ASCII_16K")) {
                  this.installAscii16K();
                  var3 = var6;
               } else if(var9.equals("ASCII_8K")) {
                  this.installAscii8K();
                  this.mapperAscii8K.setSize(var7);
                  var3 = var6;
               } else {
                  var3 = var6;
                  if(var9.equals("RTYPE")) {
                     this.installRType();
                     var3 = var6;
                  }
               }
            }
         }

         ++var5;
      }

      if(var2.startsWith("msx2")) {
         this.loadBIOS2(var1);
      } else {
         this.loadBIOS(var1);
      }

      this.loadFmPac(var1);
      this.vdp.reset();
      SoundChip[] var12;
      if(var6) {
         if(this.STEREO) {
            var12 = new SoundChip[]{this.psg, this.sccs.channel1, this.sccs.channel2};
         } else {
            var12 = new SoundChip[]{this.psg, this.scc};
         }

         var8.setSoundChips(var12);
      } else {
         if(this.STEREO) {
            var12 = new SoundChip[]{this.psg, this.musics.channel1, this.musics.channel2};
         } else {
            var12 = new SoundChip[]{this.psg, this.music};
         }

         var8.setSoundChips(var12);
      }

      this.driver.equals("msxe_nemesis2");
      if(this.driver.equals("msxe_nemesis")) {
         this.vdp.setLineCallBack(new LCBNemesis('\ue062', this));
      }

      if(this.driver.equals("msxe_nemesis3")) {
         this.vdp.setLineCallBack(new LCBNemesis('\ue362', this));
      }

      if(this.driver.equals("msxe_parodius")) {
         this.vdp.setLineCallBack(new LCBNemesis('\ue242', this));
      }

      if(this.driver.equals("msxe_salamand")) {
         this.vdp.setLineCallBack(new LCBNemesis('\ue616', this));
      }

      this.init(var8);
      return this;
   }

   public Object getObject(int var1) {
      return null;
   }

   protected void initMemoryMap() {
      rwFFFF var1 = new rwFFFF();
      this.mwa.set(0, 16383, this.slot[0]);
      this.mwa.set(16384, 32767, this.slot[1]);
      this.mwa.set('耀', '뿿', this.slot[2]);
      this.mwa.set('쀀', '\ufffe', this.slot[3]);
      this.mwa.set('\uffff', '\uffff', var1);
      this.mra.set(0, 16383, this.slot[0]);
      this.mra.set(16384, 32767, this.slot[1]);
      this.mra.set('耀', '뿿', this.slot[2]);
      this.mra.set('쀀', '\ufffe', this.slot[3]);
      this.mra.set('\uffff', '\uffff', var1);
   }

   protected void initMemorySlots() {
      this.slot[0] = new MemoryLayout();
      this.slot[0].installMemory(0, this.rom);
      this.slot[0].installMemory(2, this.crtB);
      this.slot[0].installMemory(3, 1, this.ext);
      this.slot[0].installMapper(3, 2, this.ramMapper);
      this.slot[1] = new MemoryLayout();
      this.slot[1].installMemory(0, this.rom);
      this.slot[1].installMemory(2, this.crtB);
      this.slot[1].installMemory(3, 1, this.ext);
      this.slot[1].installMapper(3, 2, this.ramMapper);
      this.slot[2] = new MemoryLayout();
      this.slot[2].installMemory(2, this.crtB);
      this.slot[2].installMemory(3, 1, this.ext);
      this.slot[2].installMapper(3, 2, this.ramMapper);
      this.slot[3] = new MemoryLayout();
      this.slot[3].installMemory(2, this.crtB);
      this.slot[3].installMemory(3, 1, this.ext);
      this.slot[3].installMapper(3, 2, this.ramMapper);
   }

   protected void initPortMap() {
      rwMemMap var1 = new rwMemMap();
      if(this.STEREO) {
         this.iow.set(124, 124, this.musics.getRegisterPortWrite());
         this.iow.set(125, 125, this.musics.getDataPortWrite());
      } else {
         this.iow.set(124, 124, this.music.getRegisterPortWrite());
         this.iow.set(125, 125, this.music.getDataPortWrite());
      }

      this.iow.set(152, 155, this.vdp);
      this.iow.set(160, 160, this.psg.AY8910_control_port_0_w());
      this.iow.set(161, 161, this.psg.AY8910_write_port_0_w());
      this.iow.set(168, 168, this.ppi.PORT_A);
      this.iow.set(170, 170, this.ppi.PORT_C);
      this.iow.set(171, 171, this.ppi.CONTROL);
      this.iow.set(180, 181, this.rtc);
      this.iow.set(252, 255, var1);
      this.ior.set(152, 155, this.vdp);
      this.ior.set(162, 162, new Register(255));
      this.ior.set(168, 168, this.ppi.PORT_A);
      this.ior.set(169, 169, this.ppi.PORT_B);
      this.ior.set(170, 170, this.ppi.PORT_C);
      this.ior.set(171, 171, this.ppi.CONTROL);
      this.ior.set(181, 181, this.rtc);
      this.ior.set(252, 255, var1);
   }

   void installAscii16K() {
      this.slot[1].installMapper(1, this.mapperAscii16K);
      this.slot[2].installMapper(1, this.mapperAscii16K);
   }

   void installAscii8K() {
      this.slot[1].installMapper(1, this.mapperAscii8K);
      this.slot[2].installMapper(1, this.mapperAscii8K);
   }

   void installKonami8K() {
      this.slot[1].installMapper(1, this.mapperKonami8K);
      this.slot[2].installMapper(1, this.mapperKonami8K);
   }

   void installKonami8Kscc() {
      this.slot[1].installMapper(1, this.mapperKonami8Kscc);
      this.slot[2].installMapper(1, this.mapperKonami8Kscc);
      this.sccs.setSCCplus(false);
   }

   void installKonami8Kscc2() {
      this.slot[1].installMapper(2, this.mapperKonami8Kscc);
      this.slot[2].installMapper(2, this.mapperKonami8Kscc);
      this.sccs.setSCCplus(false);
   }

   void installKonami8Ksccp() {
      this.slot[1].installMapper(1, this.mapperKonami8Ksccp);
      this.slot[2].installMapper(1, this.mapperKonami8Ksccp);
      this.sccs.setSCCplus(true);
   }

   void installKonami8Ksccp2() {
      this.slot[1].installMapper(2, this.mapperKonami8Ksccp);
      this.slot[2].installMapper(2, this.mapperKonami8Ksccp);
      this.sccs.setSCCplus(false);
   }

   void installRType() {
      this.slot[1].installMapper(1, this.mapperRType);
      this.slot[2].installMapper(1, this.mapperRType);
   }

   protected void loadBIOS(URL var1) {
      RomLoader var2 = new RomLoader();
      var2.setZip("msx");
      var2.setMemory(this.rom);
      var2.loadROM("MSX.ROM", 0, '耀', -2113570466);
      var2.loadZip(var1);
   }

   protected void loadBIOS2(URL var1) {
      RomLoader var2 = new RomLoader();
      var2.setZip("msx2");
      var2.setMemory(this.rom);
      var2.loadROM("MSX2.ROM", 0, '耀', 1826288549);
      var2.setMemory(this.ext);
      var2.loadROM("MSX2EXT.ROM", 0, 16384, 1713602255);
      var2.loadZip(var1);
      this.loadDISK(var1);
   }

   protected void loadCrtA(URL var1, String var2, String var3, int var4, int var5) {
      char[] var9 = new char[var5];
      RomLoader var7 = new RomLoader();
      var7.setZip(var2);
      var7.setMemory(var9);
      var7.loadROM(var3, 0, var5, var4);
      var7.loadZip(var1);
      int var6 = var9[3] * 256 + var9[2];
      var4 = var6 & '\ue000';
      if(var6 == 0) {
         var4 = '耀';
      }

      char[] var8 = new char[]{var9[0], var9[1]};
      System.out.println("TYPE: " + var8[0] + "," + var8[1]);
      System.out.println("EXEC start address:" + Integer.toHexString(var6));
      System.out.println("OFFSET:" + Integer.toHexString(var4));

      for(var6 = 0; var6 < var5; ++var6) {
         this.crtA[var6 + var4] = var9[var6];
         if(var4 == 0 && var5 <= 16384) {
            this.crtA[var6 + 16384] = var9[var6];
            this.crtA['耀' + var6] = var9[var6];
            this.crtA['쀀' + var6] = var9[var6];
         }
      }

   }

   protected void loadCrtAM(URL var1, String var2, String var3, int var4, int var5) {
      RomLoader var6 = new RomLoader();
      var6.setZip(var2);
      var6.setMemory(this.crtA);
      var6.loadROM(var3, 0, var5, var4);
      var6.loadZip(var1);
      char var8 = this.crtA[3];
      char var7 = this.crtA[2];
      System.out.println("EXEC start address:" + Integer.toHexString(var8 * 256 + var7));
   }

   protected void loadDISK(URL var1) {
      RomLoader var4 = new RomLoader();
      var4.setZip("msxdisk");
      var4.setMemory(this.ext);
      var4.loadROM("DISK.ROM", 16384, 16384, 1914659295);
      var4.loadZip(var1);
      int[] var5 = new int[]{16400, 16403, 16406};

      for(int var2 = 0; var2 < var5.length; ++var2) {
         int var3 = var5[var2];
         this.ext[var3 + 0] = 237;
         this.ext[var3 + 1] = 254;
         this.ext[var3 + 2] = 201;
      }

   }

   protected void loadFmPac(URL var1) {
      RomLoader var2 = new RomLoader();
      var2.setZip("fmpac");
      var2.setMemory(this.crtB);
      var2.loadROM("fmpac.ROM", 0, 65536, 243552349);
      var2.loadZip(var1);
   }

   public BitMap refresh(boolean var1) {
      for(int var2 = 0; var2 < this.vdp.linesPerFrame; ++var2) {
         this.vdp.updateLine(var2);
      }

      this.backbuffer = this.vdp.renderVideo();
      this.renderVideoPost();
      return this.backbuffer;
   }

   public BitMap renderVideo() {
      return this.backbuffer;
   }

   public void renderVideoPost() {
   }

   public void reset(boolean var1) {
      super.reset(var1);
      this.vdp.reset();
   }

   class rwFFFF implements ReadHandler, WriteHandler {
      public int read(int var1) {
         if(MSX.this.ppi.pslot3 == 3) {
            var1 = MSX.this.sslot ^ 255;
         } else {
            var1 = MSX.this.slot[3].read('\uffff');
         }

         return var1;
      }

      public void write(int var1, int var2) {
         if(MSX.this.ppi.pslot3 == 3) {
            MSX.this.sslot = var2;
            MSX.this.slot[0].setSSLOT(MSX.this.sslot & 3);
            MSX.this.slot[1].setSSLOT(MSX.this.sslot >> 2 & 3);
            MSX.this.slot[2].setSSLOT(MSX.this.sslot >> 4 & 3);
            MSX.this.slot[3].setSSLOT(MSX.this.sslot >> 6 & 3);
         } else {
            MSX.this.slot[3].write('\uffff', var2);
         }

      }
   }

   class rwMemMap implements ReadHandler, WriteHandler {
      public int read(int var1) {
         return MSX.this.ramMapper.getPage(var1 - 252);
      }

      public void write(int var1, int var2) {
         MSX.this.ramMapper.setPage(var1 - 252, var2);
      }
   }
}
