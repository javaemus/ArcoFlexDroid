package net.movegaga.jemu2.driver.arcade.cps;

import net.movegaga.jemu2.driver.Driver;

import java.net.URL;

import jef.cpu.Cpu;
import jef.cpu.m68000.jM68k;
import jef.cpu.z80.Z80;
import jef.cpuboard.BasicCpuBoard;
import jef.cpuboard.CpuBoard;
import jef.cpuboard.CpuDriver;
import jef.machine.BasicEmulator;
import jef.machine.Emulator;
import jef.machine.EmulatorProperties;
import jef.map.InputPort;
import jef.map.InterruptHandler;
import jef.map.MemoryMap;
import jef.map.ReadHandler;
import jef.map.ReadMap;
import jef.map.Register;
import jef.map.WriteHandler;
import jef.map.WriteMap;
import jef.sound.SoundChip;
import jef.sound.chip.okim6295.OKIM6295;
import jef.sound.chip.ym2151.YM2151;
import jef.sound.chip.ym2151.YMInterruptHandler;
import jef.util.RomLoader;
import jef.video.VideoConstants;

/**
 * @author edy
 * 
 * CPS1 HW driver. Partially based no Final Burn
 */
public class Cps1 extends BasicEmulator implements Driver, VideoConstants {

    //Emulator           emu            = new BasicEmulator();

    // -----------------------------------------------------------
    // Properties related to CPU #1
    // -----------------------------------------------------------
    Cpu                m68000         = new jM68k();
    char[]             mem_cpu1       = new char[0x200000];
    MemoryMap          memMap1        = new MemoryMap(mem_cpu1);
    InterruptHandler   cps1_interrupt = new CPS1Interrupt();

    // -----------------------------------------------------------
    // Properties related to CPU #2
    // -----------------------------------------------------------
    Z80                z80            = new Z80();
    char[]             mem_cpu2       = new char[0x18000];
    int                bankAddr       = 0x8000;

    // -----------------------------------------------------------
    // Properties related to Video
    // -----------------------------------------------------------
    VCps               video          = new VCps();
    int[]              mem_gfx1       = new int[0x600000];
    char[]             temp;

    // -----------------------------------------------------------
    // Properties related to Sound
    // -----------------------------------------------------------
    YMInterruptHandler musInterrupt   = new YMInterruptHandler() {
                                          public void irq(boolean irq) {
                                              if (irq) {
                                                  z80.interrupt(Cpu.INTERRUPT_TYPE_IRQ, irq);
                                                  z80.exec(1000);
                                              }
                                          }
                                      };
    Register           soundLatch     = new Register();
    Register           soundFade      = new Register();
    char[]             mem_samples    = new char[0x40000];
    YM2151             ym2151         = new YM2151(1f, 3579545, musInterrupt, null);
    ReadHandler        ymReadStatus   = ym2151.getReadStatusRead();
    WriteHandler       ymWriteReg     = ym2151.getRegisterPortWrite();
    WriteHandler       ymWriteData    = ym2151.getDataPortWrite();
    OKIM6295           okim6295       = new OKIM6295(mem_samples, 7576);

    class MemMapSound extends BasicCpuBoard {

        public int read8opc(int address) {
            return read8(address);
        }

        public int read8arg(int address) {
            return read8(address);
        }

        public int read8(int address) {
            if (address < 0x8000) return mem_cpu2[address];
            if (address < 0xc000) return mem_cpu2[address + bankAddr];
            if (address < 0xd000) return 255;
            if (address < 0xd800) return mem_cpu2[address];
            if (address == 0xf001) return ymReadStatus.read(0);
            //if (address == 0xf002) return okim6295.
            if (address == 0xf008) return soundLatch.read(0);
            if (address == 0xf00a) return soundFade.read(0);
            
            return 255;
        }

        public void write8(int address, int data) {
            if (address < 0xd000) return;
            if (address < 0xd800) mem_cpu2[address] = (char) data;
            if (address == 0xf000) ymWriteReg.write(address, data);
            if (address == 0xf001) ymWriteData.write(address, data);
            //if (address == 0xf002) okim6295.;
            if (address == 0xf004) {
                System.out.println("bank " + data);
                int length = mem_cpu2.length - 0x10000;
                bankAddr = (data * 0x4000) & (length-1);
                bankAddr += 0x8000;
            }
        }

        /** Read a word */
        public int read16(int address) {
            return read8(address++) | (read8(address) << 8);
        }

        /** Read a word */
        public int read16arg(int address) {
            return read16(address);
        }

        public void write16(int address, int data) {
            write8(address++, data & 0xff);
            write8(address, data >> 8);
        }

        public int[] getMemory() {
            return null;
        }
    }

    // -----------------------------------------------------------
    // MISC. Handlers
    // -----------------------------------------------------------
    InputPort[] in = new InputPort[7];

    private ReadMap readmem() {
        ReadMap mra = memMap1;
        mra.setMR(0x000000, 0x1fffff, ReadMap.RD_ROM);
        mra.set(0x800000, 0x800003, in[4]);
        mra.set(0x800010, 0x800013, in[4]);
        mra.set(0x800018, 0x800019, in[0]);
        mra.set(0x80001a, 0x80001b, in[1]);
        mra.set(0x80001c, 0x80001d, in[2]);
        mra.set(0x80001e, 0x80001f, in[3]);
        mra.setMR(0x800020, 0x800021, ReadMap.RD_NOP);
        mra.set(0x800176, 0x800177, in[6]);
        mra.set(0x8001fc, 0x8001fc, in[6]);
        mra.set(0x800100, 0x8001ff, video.cps1_output_r());
        mra.setMR(0x900000, 0x92ffff, ReadMap.RD_BANK3);
        mra.setMR(0xf18000, 0xf19fff, ReadMap.RD_RAM);
        mra.set(0xf1c000, 0xf1c001, in[6]);
        mra.setMR(0xff0000, 0xffffff, ReadMap.RD_BANK2);
        return mra;
    }

    private WriteMap writemem() {
        WriteMap mwa = memMap1;
        mwa.setMW(0x000000, 0x1fffff, WriteMap.WR_ROM);
        mwa.set(0x800181, 0x800181, soundLatch);
        mwa.set(0x800189, 0x800189, soundFade);
        mwa.set(0x800100, 0x8001ff, video.cps1_output_w());
        mwa.setMW(0x900000, 0x92ffff, WriteMap.WR_BANK3);
        mwa.setMW(0xf18000, 0xf19fff, WriteMap.WR_RAM);
        mwa.setMW(0xff0000, 0xffffff, WriteMap.WR_BANK2);
        return mwa;
    }

    private InputPort[] initInputSF2() {
        in[0].setBit(0x01, InputPort.COIN1);
        in[0].setBit(0x02, InputPort.COIN2);
        in[0].setBit(0x04, InputPort.COIN3);
        in[0].setBit(0x08, InputPort.UNKNOWN);
        in[0].setBit(0x10, InputPort.START1);
        in[0].setBit(0x20, InputPort.START2);
        in[0].setBit(0x40, InputPort.SERVICE1);
        in[0].setBit(0x80, InputPort.UNKNOWN);
        in[1].setBits(0x07, 0x07);
        in[1].setBits(0x38, 0x38);
        in[1].setBits(0x40, 0x40);
        in[1].setBits(0x80, 0x80);
        in[2].setBits(0x07, 0x04);
        in[2].setBits(0x08, 0x08);
        in[2].setBits(0x10, 0x10);
        in[2].setBits(0x20, 0x20);
        in[2].setBits(0x40, 0x40);
        in[2].setBits(0x80, 0x80);
        in[3].setBits(0x03, 0x03);
        in[3].setBits(0x04, 0x04);
        in[3].setBits(0x08, 0x08);
        in[3].setBits(0x10, 0x10);
        in[3].setBits(0x20, 0x00);
        in[3].setBits(0x40, 0x00);
        in[3].setService(0x80);
        in[4].setBit(0x01, InputPort.JOY_RIGHT);
        in[4].setBit(0x02, InputPort.JOY_LEFT);
        in[4].setBit(0x04, InputPort.JOY_DOWN);
        in[4].setBit(0x08, InputPort.JOY_UP);
        in[4].setBit(0x10, InputPort.BUTTON1);
        in[4].setBit(0x20, InputPort.BUTTON2);
        in[4].setBit(0x40, InputPort.BUTTON3);
        in[4].setBit(0x80, 0x80);
        in[5].setBit(0x01, InputPort.JOY_RIGHT_2);
        in[5].setBit(0x02, InputPort.JOY_LEFT_2);
        in[5].setBit(0x04, InputPort.JOY_DOWN_2);
        in[5].setBit(0x08, InputPort.JOY_UP_2);
        in[5].setBit(0x10, InputPort.BUTTON1_2);
        in[5].setBit(0x20, InputPort.BUTTON2_2);
        in[5].setBit(0x40, InputPort.BUTTON3_2);
        in[5].setBits(0x80, 0x80);
        in[6].setBit(0x01, InputPort.BUTTON4);
        in[6].setBit(0x02, InputPort.BUTTON5);
        in[6].setBit(0x04, InputPort.BUTTON6);
        in[6].setBits(0x88, 0x88);
        in[6].setBit(0x10, InputPort.BUTTON4_2);
        in[6].setBit(0x20, InputPort.BUTTON5_2);
        in[6].setBit(0x40, InputPort.BUTTON6_2);
        return in;
    }

    public class CPS1Interrupt implements InterruptHandler {

        public int irq() {
            return 2;
        }
    }
    
    public class SoundInterrupt implements InterruptHandler {

        public int irq() {
            return Cpu.INTERRUPT_TYPE_IGNORE;
        }
        
    }
    
    public CpuBoard createCpuBoard(int id) {
        if (id == 0) return new BasicCpuBoard();
        else return new MemMapSound();
    }

    public EmulatorProperties initSF2() {
        CpuDriver[] cpuDriver = new CpuDriver[2];

        cpuDriver[0] = new CpuDriver(m68000, 8000000, readmem(), writemem(), cps1_interrupt, 1);
        cpuDriver[1] = new CpuDriver(z80, 3579545, null, null, new SoundInterrupt(), 1);
        
        SoundChip[] soundChips = new SoundChip[1];
        soundChips[0] = ym2151;
        //soundChips[1] = okim6295;

        EmulatorProperties md = new EmulatorProperties();
        md.setCpuDriver(cpuDriver);
        md.setFPS(60);
        md.setTimeSlicesPerFrame(100);
        md.setVideoWidth(36 * 8);
        md.setVideoHeight(28 * 8);
        md.setVisibleArea(32, 32 + 0x30 * 8 - 1, 32 + 16, 32 + 16 + 0x1c * 8 - 1);
        md.setVideoInitializer(video);
        md.setVideoUpdater(video);
        md.setInputPorts(in);
        md.setSoundChips(soundChips);
        md.setControllers(2);

        return md;
    }

    private boolean rom_forgottn(URL url) {

        char[] temp = new char[0x80000];

        RomLoader romLoader = new RomLoader();
        romLoader.setZip("forgottn");

        romLoader.setMemory(temp);
        romLoader.loadROM("lwu11a", 0x00000, 0x20000, 0xddf78831);
        romLoader.loadROM("lwu15a", 0x20000, 0x20000, 0xf7ce2097);
        romLoader.loadROM("lwu10a", 0x40000, 0x20000, 0x8cb38c81);
        romLoader.loadROM("lwu10a", 0x60000, 0x20000, 0xd70ef9fd);
        romLoader.loadZip(url);

        even(0, 0x20000, 0, temp);
        odd(0x20000, 0x20000, 0, temp);
        even(0x40000, 0x20000, 0x40000, temp);
        odd(0x60000, 0x20000, 0x40000, temp);

        temp = null;

        return true;
    }

    private boolean rom_sf2(URL url) {
        temp = new char[0x100000];

        RomLoader romLoader = new RomLoader();
        romLoader.setZip("sf2");
        romLoader.setMemory(temp); /* 68000 code */
        romLoader.loadROM("sf2e_30b.rom", 0x00000, 0x20000, 0x57bd7051);
        romLoader.loadZip(url);
        romLoader = new RomLoader();
        romLoader.setZip("sf2");
        romLoader.setMemory(temp); /* 68000 code */
        romLoader.loadROM("sf2e_37b.rom", 0x20000, 0x20000, 0x62691cdd);
        romLoader.loadZip(url);
        romLoader = new RomLoader();
        romLoader.setZip("sf2");
        romLoader.setMemory(temp); /* 68000 code */
        romLoader.loadROM("sf2e_31b.rom", 0x40000, 0x20000, 0xa673143d);
        romLoader.loadZip(url);
        romLoader = new RomLoader();
        romLoader.setZip("sf2");
        romLoader.setMemory(temp); /* 68000 code */
        romLoader.loadROM("sf2e_38b.rom", 0x60000, 0x20000, 0x4c2ccef7);
        romLoader.loadZip(url);
        romLoader = new RomLoader();
        romLoader.setZip("sf2");
        romLoader.setMemory(temp); /* 68000 code */
        romLoader.loadROM("sf2_28b.rom", 0x80000, 0x20000, 0x4009955e);
        romLoader.loadZip(url);
        romLoader = new RomLoader();
        romLoader.setZip("sf2");
        romLoader.setMemory(temp); /* 68000 code */
        romLoader.loadROM("sf2_35b.rom", 0xa0000, 0x20000, 0x8c1f3994);
        romLoader.loadZip(url);
        romLoader = new RomLoader();
        romLoader.setZip("sf2");
        romLoader.setMemory(temp); /* 68000 code */
        romLoader.loadROM("sf2_29b.rom", 0xc0000, 0x20000, 0xbb4af315);
        romLoader.loadZip(url);
        romLoader = new RomLoader();
        romLoader.setZip("sf2");
        romLoader.setMemory(temp); /* 68000 code */
        romLoader.loadROM("sf2_36b.rom", 0xe0000, 0x20000, 0xc02a13eb);
        romLoader.loadZip(url);

        even(0, 0x20000, 0, temp);
        odd(0x20000, 0x20000, 0, temp);
        even(0x40000, 0x20000, 0x40000, temp);
        odd(0x60000, 0x20000, 0x40000, temp);
        even(0x80000, 0x20000, 0x80000, temp);
        odd(0xa0000, 0x20000, 0x80000, temp);
        even(0xc0000, 0x20000, 0xc0000, temp);
        odd(0xe0000, 0x20000, 0xc0000, temp);

        SepTableCalc();

        temp = null;
        temp = new char[0x200000];
        romLoader = new RomLoader();
        romLoader.setZip("sf2");
        romLoader.setMemory(temp);
        romLoader.loadROM("sf2gfx01.rom", 0x000000, 0x80000, 0x22c9cc8e);
        romLoader.loadZip(url);
        romLoader = new RomLoader();
        romLoader.setZip("sf2");
        romLoader.setMemory(temp);
        romLoader.loadROM("sf2gfx10.rom", 0x080000, 0x80000, 0x57213be8);
        romLoader.loadZip(url);
        romLoader = new RomLoader();
        romLoader.setZip("sf2");
        romLoader.setMemory(temp);
        romLoader.loadROM("sf2gfx20.rom", 0x100000, 0x80000, 0xba529b4f);
        romLoader.loadZip(url);
        romLoader = new RomLoader();
        romLoader.setZip("sf2");
        romLoader.setMemory(temp);
        romLoader.loadROM("sf2gfx02.rom", 0x180000, 0x80000, 0x4b1b33a8);
        romLoader.loadZip(url);

        loadTiles(temp, 0 * 0x200000, 8 + 0 * 4);
        temp = null;
        temp = new char[0x200000];

        romLoader = new RomLoader();
        romLoader.setZip("sf2");
        romLoader.setMemory(temp);
        romLoader.loadROM("sf2gfx11.rom", 0x000000, 0x80000, 0x2c7e2229);
        romLoader.loadZip(url);
        romLoader = new RomLoader();
        romLoader.setZip("sf2");
        romLoader.setMemory(temp);
        romLoader.loadROM("sf2gfx21.rom", 0x080000, 0x80000, 0xb5548f17);
        romLoader.loadZip(url);
        romLoader = new RomLoader();
        romLoader.setZip("sf2");
        romLoader.setMemory(temp);
        romLoader.loadROM("sf2gfx03.rom", 0x100000, 0x80000, 0x14b84312);
        romLoader.loadZip(url);
        romLoader = new RomLoader();
        romLoader.setZip("sf2");
        romLoader.setMemory(temp);
        romLoader.loadROM("sf2gfx12.rom", 0x180000, 0x80000, 0x5e9cd89a);
        romLoader.loadZip(url);

        loadTiles(temp, 1 * 0x200000, 8 + 1 * 4);
        temp = null;
        temp = new char[0x200000];

        romLoader = new RomLoader();
        romLoader.setZip("sf2");
        romLoader.setMemory(temp);
        romLoader.loadROM("sf2gfx22.rom", 0x000000, 0x80000, 0x994bfa58);
        romLoader.loadZip(url);
        romLoader = new RomLoader();
        romLoader.setZip("sf2");
        romLoader.setMemory(temp);
        romLoader.loadROM("sf2gfx04.rom", 0x080000, 0x80000, 0x3e66ad9d);
        romLoader.loadZip(url);
        romLoader = new RomLoader();
        romLoader.setZip("sf2");
        romLoader.setMemory(temp);
        romLoader.loadROM("sf2gfx13.rom", 0x100000, 0x80000, 0xc1befaa8);
        romLoader.loadZip(url);
        romLoader = new RomLoader();
        romLoader.setZip("sf2");
        romLoader.setMemory(temp);
        romLoader.loadROM("sf2gfx23.rom", 0x180000, 0x80000, 0x0627c831);
        romLoader.loadZip(url);
        romLoader = null;
        loadTiles(temp, 2 * 0x200000, 8 + 2 * 4);
        temp = null;
        
        romLoader = new RomLoader();
        romLoader.setZip("sf2");
        romLoader.setMemory(mem_cpu2);
        romLoader.loadROM( "sf2_09.bin",    0x00000, 0x10000, 0xa4823a1b);
        romLoader.setMemory(mem_samples);
        romLoader.loadROM( "sf2_18.bin",       0x00000, 0x20000, 0x7f162009);
        romLoader.loadROM( "sf2_19.bin",       0x20000, 0x20000, 0xbeade53f);
        romLoader.loadZip(url);
        
        for (int i = 0; i < 0x8000; i++) {
            mem_cpu2[i + 0x10000] = mem_cpu2[i + 0x8000];
            mem_cpu2[i + 0x8000] = 0;
        }


        return true;
    }

    // Precalculated table of the Separate function
    int[] SepTable   = new int[256];

    int   START_ADDR = 0;

    int loadTiles(char[] gfx, int Tile, int nStart) {
        // left side of 16x16 tiles
        loadOne(gfx, Tile, nStart, 1, 0);
        loadOne(gfx, Tile, nStart + 1, 1, 2);
        // right side of 16x16 tiles
        loadOne(gfx, Tile + 4, nStart + 2, 1, 0);
        loadOne(gfx, Tile + 4, nStart + 3, 1, 2);
        return 0;
    }

    // ----------------------------CPS1--------------------------------
    // Load 1 rom and interleve in the CPS style:
    // rom : aa bb
    // --ba --ba --ba --ba --ba --ba --ba --ba 8 pixels (four bytes)
    // (skip four bytes)

    int loadOne(char[] rom, int Tile, int nNum, int nWord, int nShift) {
        int i = 0;
        int pt = 0, pr = 0;

        for (i = 0, pt = 0, pr = START_ADDR; i < 0x80000; pt += 8) {
            int Pix; // Eight pixels
            int b;
            b = rom[pr++];
            i++;
            Pix = SepTable[b];
            if (nWord != 0) {
                b = rom[pr++];
                i++;
                Pix |= SepTable[b] << 1;
            }

            Pix <<= nShift;
            orGfx(Tile + pt, Pix);
        }
        START_ADDR += 0x80000;

        START_ADDR = START_ADDR % 0x200000;

        return 0;
    }

    void orGfx(int offset, int orValue) {
        int i = READ_INT(mem_gfx1, offset);
        i |= orValue;
        WRITE_INT(mem_gfx1, offset, i);
    }

    private int READ_INT(int[] v, int a) {
        return (v[a++] << 24) | (v[a++] << 16) | (v[a++] << 8) | v[a];
    }

    private void WRITE_INT(int[] v, int a, int value) {
        v[a++] = (value >> 24) & 255;
        v[a++] = (value >> 16) & 255;
        v[a++] = (value >> 8) & 255;
        v[a] = (value) & 255;
    }

    boolean bDone;

    int SepTableCalc() {
        int i = 0;
        if (bDone)
            return 0; // Already done it
        for (i = 0; i < 256; i++)
            SepTable[i] = Separate(i);
        bDone = true; // done it
        return 0;
    }

    static int Separate(int b) {
        int a = b;
        a = ((a & 0x000000f0) << 12) | (a & 0x0000000f);
        a = ((a & 0x000c000c) << 6) | (a & 0x00030003);
        a = ((a & 0x02020202) << 3) | (a & 0x01010101);
        return a;
    }

    private void odd(int begin, int length, int start, char[] temp) {
        for (int i = 0; i < length; i++) {
            mem_cpu1[start + 1 + i * 2] = temp[begin + i];
        }

    }

    private void even(int begin, int length, int start, char[] temp) {
        for (int i = 0; i < length; i++) {
            mem_cpu1[start + i * 2] = temp[begin + i];
        }
    }

    public Emulator createEmulator(URL url, String name) {
        //super.createEmulator(url, name);

        in[0] = new InputPort();
        in[1] = new InputPort();
        in[2] = new InputPort();
        in[3] = new InputPort();
        in[4] = new InputPort();
        in[5] = new InputPort();
        in[6] = new InputPort();

        EmulatorProperties md = null;
        if (name.equals("forgottn")) {
            rom_forgottn(url);
            initInputSF2();
            md = initSF2();
        } else if (name.equals("sf2")) {
            rom_sf2(url);
            initInputSF2();
            md = initSF2();
        }

        video.setRegions(mem_gfx1, memMap1);

        // REGION_GFX1 = null;

        System.gc();

        video.init(md);
        this.init(md);
        return this;
    }

    public Object getObject(int enumProperty) {
        // TODO Auto-generated method stub
        return null;
    }

}
