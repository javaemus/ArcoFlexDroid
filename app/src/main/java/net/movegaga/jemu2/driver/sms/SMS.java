package net.movegaga.jemu2.driver.sms;

import net.movegaga.jemu2.driver.Driver;
import net.movegaga.jemu2.driver.sms.video.VDP;

import java.io.IOException;
import java.net.URL;

import jef.cpu.z80.Z80;
import jef.cpuboard.CpuDriver;
import jef.machine.BasicEmulator;
import jef.machine.Emulator;
import jef.machine.EmulatorProperties;
import jef.map.IOReadPort;
import jef.map.IOWritePort;
import jef.map.InputPort;
import jef.map.InterruptHandler;
import jef.map.MemoryReadAddress;
import jef.map.MemoryWriteAddress;
import jef.map.ReadHandler;
import jef.map.Register;
import jef.map.WriteHandler;
import jef.scoring.ParserFile;
import jef.sound.Settings;
import jef.sound.SoundChip;
import jef.sound.chip.SN76496;
import jef.sound.chip.ym2413.YM2413;
import jef.sound.chip.ym2413.YM2413S;
import jef.util.ByteArray;
import jef.util.MachineState;
import jef.util.Persistent;
import jef.util.RomLoader;
import jef.video.BitMap;

public class SMS extends BasicEmulator implements Driver {
   private static final int CPU_CLOCK_SPEED = 3579545;
   private static final int FPS = 60;
   String[][] ROM_LIST;
   final boolean STEREO;
   protected BitMap backbuffer;
   int bankPage0;
   int bankPage1;
   int bankPage2;
   int banks;
   Z80 cpu;
   char[] crt;
   char[] crtRam;
   InputPort dd;
   private String driver;
   boolean europe;
   ParserFile highScores;
   IOReadPort ior;
   IOWritePort iow;
   private boolean key0;
   private boolean keyf6;
   private boolean keyf7;
   int lineCycles;
   int lines;
   MemoryReadAddress mra;
   MemoryWriteAddress mwa;
   int nat1;
   int nat2;
   private boolean page2rom;
   SN76496 psg;
   char[] ram;
   private int ramBank2;
   private float soundPtr;
   private float soundUpdatesPerLine;
   private MachineState state;
   VDP vdp;
   YM2413 ym2413;
   YM2413S ym2413s;

   public SMS() {
      this.STEREO = Settings.STEREO_ENHANCED;
      this.cpu = new Z80();
      this.vdp = new VDP(this.cpu);
      this.psg = new SN76496(3579545);
      this.ym2413 = new YM2413(3579545);
      this.ym2413s = new YM2413S(3579545);
      this.crt = new char[1048576];
      this.ram = new char[8192];
      this.crtRam = new char['耀'];
      this.mwa = new MemoryWriteAddress(65536);
      this.mra = new MemoryReadAddress(65536);
      this.iow = new IOWritePort();
      this.ior = new IOReadPort();
      this.dd = new InputPort();
      this.ROM_LIST = null;
      this.highScores = new ParserFile("/smsscores.txt");
      this.europe = true;
      this.page2rom = true;
      this.ramBank2 = 0;
   }

   private void initState() {
      this.state = new MachineState(this.driver);
      this.state.add(this.ram);
      this.state.add(this.crtRam);
      ByteArray var1 = new ByteArray();
      var1.add16(this.banks);
      var1.add16(this.bankPage0);
      var1.add16(this.bankPage1);
      var1.add16(this.bankPage2);
      var1.add16(this.lines);
      var1.add16(this.lineCycles);
      var1.add1(this.page2rom);
      var1.add16(this.ramBank2);
      this.state.add(var1);
      this.state.add((Persistent)this.cpu);
      this.state.add((Persistent)this.vdp);
   }

   private void loadCrt(URL var1, String var2, String var3, int var4, int var5) {
      RomLoader var6 = new RomLoader();
      var6.setZip(var2);
      var6.setMemory(this.crt);
      var6.loadROM(var3, 0, var5, var4);
      var6.loadZip(var1);
      if(var5 % 1024 != 0) {
         System.out.println("Skip header");

         for(var4 = 512; var4 < var5; ++var4) {
            this.crt[var4 - 512] = this.crt[var4];
         }
      }

   }

   private void loadState() {
      try {
         this.state.load();
         this.ram = this.state.getChars(0);
         this.crtRam = this.state.getChars(1);
         ByteArray var1 = this.state.getByteArray(2);
         this.banks = var1.get16(0);
         this.bankPage0 = var1.get16(2);
         this.bankPage1 = var1.get16(4);
         this.bankPage2 = var1.get16(6);
         this.lines = var1.get16(8);
         this.lineCycles = var1.get16(10);
         this.page2rom = var1.get1(12);
         this.ramBank2 = var1.get16(13);
         this.cpu.restoreState(this.state.getByteArray(3));
         this.vdp.restoreState(this.state.getByteArray(4));
         this.setHighScoreSupported(false);
      } catch (IOException var2) {
         System.out.println("Machine state could not be loaded:");
         System.out.println(var2);
      }

   }

   private void saveState() {
      try {
         MachineState var1 = new MachineState(this.driver);
         this.state = var1;
         this.state.add(this.ram);
         this.state.add(this.crtRam);
         ByteArray var3 = new ByteArray();
         var3.add16(this.banks);
         var3.add16(this.bankPage0);
         var3.add16(this.bankPage1);
         var3.add16(this.bankPage2);
         var3.add16(this.lines);
         var3.add16(this.lineCycles);
         var3.add1(this.page2rom);
         var3.add16(this.ramBank2);
         this.state.add(var3);
         this.state.add((Persistent)this.cpu);
         this.state.add((Persistent)this.vdp);
         this.state.save();
      } catch (IOException var2) {
         var2.printStackTrace();
      }

   }

   public Emulator createEmulator(URL var1, String var2) {
      this.driver = var2;
      this.initState();
      this.bankPage0 = 0;
      this.bankPage1 = 1;
      this.bankPage2 = 2;
      InputPort var6 = new InputPort();
      var6.setBit(1, 255, 10);
      var6.setBit(2, 255, 11);
      var6.setBit(4, 255, 8);
      var6.setBit(8, 255, 9);
      var6.setBit(16, 255, 12);
      var6.setBit(32, 255, 13);
      var6.setBits(192, 192);
      this.dd.setBit(16, 255, 4);
      this.dd.setBits(239, 239);
      MemMap var7 = new MemMap();
      this.mra.set(0, '\uffff', var7);
      this.mwa.set(0, '\uffff', var7);
      Register var9 = new Register(255);
      ReadCtrl2 var14 = new ReadCtrl2();
      ReadHandler var12 = this.vdp.getReadVertical();
      WriteNat var8 = new WriteNat();
      WriteHandler var10 = this.psg.getWriteCommandHandler();
      Register var11 = new Register(255);
      this.ior.setLineMask(195);
      this.ior.set(0, 255, var9);
      this.ior.set(64, 67, var12);
      this.ior.set(128, 131, this.vdp);
      this.ior.set(192, 192, var6);
      this.ior.set(193, 193, var14);
      this.ior.set(194, 194, var11);
      this.iow.setLineMask(243);
      this.iow.set(1, 1, var8);
      this.iow.set(3, 3, var8);
      this.iow.set(112, 115, var10);
      this.iow.set(176, 179, this.vdp);
      if(this.STEREO) {
         this.iow.set(240, 240, this.ym2413s.getRegisterPortWrite());
         this.iow.set(241, 241, this.ym2413s.getDataPortWrite());
      } else {
         this.iow.set(240, 240, this.ym2413.getRegisterPortWrite());
         this.iow.set(241, 241, this.ym2413.getDataPortWrite());
      }

      this.iow.set(242, 242, var11);
      this.setNTSC(false);
      EmulatorProperties var16 = new EmulatorProperties();
      var16.setCpuDriver(new CpuDriver(this.cpu, 3579545, this.mra, this.mwa, this.ior, this.iow, (InterruptHandler)null, 0));
      var16.setFPS(60);
      var16.setVideoDimensions(256, 192);
      var16.setVisibleArea(0, 255, 0, 191);
      var16.setVideoUpdater(this.vdp);
      var16.setInputPorts(new InputPort[]{var6});
      SoundChip[] var13;
      if(this.STEREO) {
         var13 = new SoundChip[]{this.psg, this.ym2413s.channel1, this.ym2413s.channel2};
      } else {
         var13 = new SoundChip[]{this.psg, this.ym2413};
      }

      var16.setSoundChips(var13);

      for(int var3 = 0; var3 < this.ROM_LIST.length; ++var3) {
         if(this.ROM_LIST[var3][0].equals(var2)) {
            String var17 = this.ROM_LIST[var3][1];
            String var15 = this.ROM_LIST[var3][2];
            int var5 = (int)Long.parseLong(this.ROM_LIST[var3][3], 16);
            int var4 = Integer.parseInt(this.ROM_LIST[var3][4], 16);
            this.banks = var4 / 16384;
            System.out.println("BANKS = " + this.banks);
            this.loadCrt(var1, var17, var15, var5, var4);
         }
      }

      this.init(var16);
      this.highScores.getHighScoreParser(var2, this, this.ram);
      return this;
   }

   public Object getObject(int var1) {
      return null;
   }

   public BitMap refresh(boolean var1) {
      for(int var2 = 0; var2 < this.lines; ++var2) {
         this.cpu.exec(this.lineCycles);
         this.vdp.updateLine(var2);

         for(this.soundPtr += this.soundUpdatesPerLine; this.soundPtr >= 1.0F && this.psg.getBufferLength() > 0; --this.soundPtr) {
            this.psg.calcSample();
         }
      }

      this.backbuffer = this.vdp.renderVideo();
      this.se.update();
      this.updateInput();
      if(this.highScoreHandler != null) {
         this.highScoreHandler.update();
      }

      return this.backbuffer;
   }

   public void setNTSC(boolean var1) {
      this.vdp.setNTSC(var1);
      if(var1) {
         this.lines = 262;
         this.lineCycles = 228;
      } else {
         this.lines = 312;
         this.lineCycles = 229;
      }

      this.soundUpdatesPerLine = 22050.0F / (60.0F * (float)this.lines);
      System.out.println("SoundUpdatesPerLine = " + this.soundUpdatesPerLine);
   }

   class MemMap implements ReadHandler, WriteHandler {
      public int read(int var1) {
         char var2;
         switch(61440 & var1) {
         case 0:
         case 4096:
         case 8192:
         case 12288:
            var2 = SMS.this.crt[SMS.this.bankPage0 * 16384 + (var1 & 16383)];
            break;
         case 16384:
         case 20480:
         case 24576:
         case 28672:
            var2 = SMS.this.crt[SMS.this.bankPage1 * 16384 + (var1 & 16383)];
            break;
         case 32768:
         case 36864:
         case 40960:
         case 45056:
            if(SMS.this.page2rom) {
               var2 = SMS.this.crt[SMS.this.bankPage2 * 16384 + (var1 & 16383)];
            } else {
               var2 = SMS.this.crtRam[SMS.this.ramBank2 * 16384 + (var1 & 16383)];
            }
            break;
         case 49152:
         case 53248:
         case 57344:
         case 61440:
            var2 = SMS.this.ram[var1 & 8191];
            break;
         default:
            var2 = SMS.this.crt[var1];
         }

         return var2;
      }

      public void write(int var1, int var2) {
         switch(61440 & var1) {
         case 32768:
         case 36864:
         case 40960:
         case 45056:
            if(!SMS.this.page2rom) {
               SMS.this.crtRam[SMS.this.ramBank2 * 16384 + (var1 & 16383)] = (char)var2;
            }
            break;
         case 49152:
         case 53248:
         case 57344:
         case 61440:
            if(var1 >= '￼') {
               switch(57343 & var1) {
               case 57340:
                  if((var2 & 8) != 0) {
                     SMS.this.page2rom = false;
                     if((var2 & 4) != 0) {
                        SMS.this.ramBank2 = 1;
                     } else {
                        SMS.this.ramBank2 = 0;
                     }
                  } else {
                     SMS.this.page2rom = true;
                  }
                  break;
               case 57341:
                  SMS.this.bankPage0 = var2 % SMS.this.banks;
                  break;
               case 57342:
                  SMS.this.bankPage1 = var2 % SMS.this.banks;
                  break;
               case 57343:
                  SMS.this.bankPage2 = var2 % SMS.this.banks;
               }
            }

            SMS.this.ram[var1 & 8191] = (char)var2;
         }

      }
   }

   class ReadCtrl2 implements ReadHandler {
      public int read(int var1) {
         if(SMS.this.nat1 == 1) {
            var1 = 255 | 64;
         } else {
            var1 = 255 & -65;
         }

         if(SMS.this.nat2 == 1) {
            var1 |= 128;
         } else {
            var1 &= -129;
         }

         return var1;
      }
   }

   class WriteNat implements WriteHandler {
      public void write(int var1, int var2) {
         if((var2 & 32) == 32) {
            SMS.this.nat1 = 1;
         } else {
            SMS.this.nat1 = 0;
         }

         if((var2 & 128) == 128) {
            SMS.this.nat2 = 1;
         } else {
            SMS.this.nat2 = 0;
         }

         if(!SMS.this.europe) {
            SMS.this.nat1 = ~SMS.this.nat1;
            SMS.this.nat2 = ~SMS.this.nat2;
         }

      }
   }
}
