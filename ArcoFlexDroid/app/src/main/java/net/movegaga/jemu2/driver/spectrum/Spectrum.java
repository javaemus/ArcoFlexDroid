/*
 * Created on Dec 17, 2006
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package net.movegaga.jemu2.driver.spectrum;

import java.io.IOException;
import java.net.URL;

//import org.lwjgl.input.Keyboard;

import jef.cpu.Cpu;
import jef.cpu.z80.Z80;
import jef.cpuboard.BasicCpuBoard;
import jef.cpuboard.CpuBoard;
import jef.cpuboard.CpuDriver;
import jef.machine.BasicEmulator;
import jef.machine.Emulator;
import jef.machine.EmulatorProperties;
import jef.map.MemoryReadAddress;
import jef.map.MemoryWriteAddress;
import jef.map.WriteHandler;
import jef.sound.Settings;
import jef.sound.chip.DAC;
import jef.util.ConfigFile;
import jef.util.MachineState;
import jef.util.RomLoader;
import jef.util.ZipLoader;
import jef.video.BitMap;
import net.movegaga.jemu2.JEmu2;
//import net.movegaga.jemu2.applet.KeyboardRemap;
import net.movegaga.jemu2.driver.Driver;

/**
 * @author Erik Duijs
 * 
 * Driver for the Sinclair ZX Spectrum home computer.<br/> <br/> Emulates only
 * the 48K Spectrum model (no 128K supported yet).<br/> Supports the Kempston
 * Joystick interface, which is mapped to the cursor keys and space key.<br/>
 * Supports SNA and Z80 snapshot files.<br/> <br/> <b>To do:</b>
 * <ul>
 * <li>2) Emulate 128K + AY8910.</li>
 * <li>3) Support .tzx tape images.</li>
 * </ul>
 */
public class Spectrum extends BasicEmulator implements Driver {

    static final int CPU_CLOCK_SPEED = 3581000;

    Z80 z80 = new Z80();
    Video video = new Video(this);
    DAC speaker = new DAC();
    char[] mem = new char[0x10000];

    // Memory map
    MemoryWriteAddress mwa = new MemoryWriteAddress(mem);
    MemoryReadAddress mra = new MemoryReadAddress(mem);

    String driver;

    // External config files
    //String[][]         ROM_LIST        = new ConfigFile("/specroms.txt").getContents();

    static final int VIDEO_LINES = 311;
    private float soundUpdatesPerLine;
    private float soundPtr;

    private MachineState state;
    private boolean keyf6;
    private boolean keyf7;


    public CpuBoard createCpuBoard(int id) {
        return new IO();
    }

    /**
     * Initialize machine state.<br/> Necessary to initialize the indexes if a
     * state is loaded from a previous JEmu2 session of the currently loaded
     * game.
     */
    private void initState() {
        state = new MachineState(driver);
        state.add(z80);
        state.add(mem);
    }

    public Emulator createEmulator(URL url, String name) {
        this.driver = name;

        // ---------------------------------------------------------------
        // Initialize Machine State saving
        // ---------------------------------------------------------------
        initState();

        // ---------------------------------------------------------------
        // load BIOS
        // ---------------------------------------------------------------
        ZipLoader zip = new ZipLoader("zx48");
        zip.queue(mem, 0xddee531f, 0, 0x4000);
        zip.load();

        // ---------------------------------------------------------------
        // Initialize memory map
        // ---------------------------------------------------------------
        mwa.setMW(0x0000, 0x3fff, MemoryWriteAddress.WR_ROM);
        mwa.set(0x4000, 0x57ff, new WriteHandler() {
            public void write(int address, int data) {
                video.writeChr(address, (char) data);
            }
        });
        mwa.set(0x5800, 0x5aff, new WriteHandler() {
            public void write(int address, int data) {
                video.writeAtt(address, (char) data);
            }
        });
        mwa.setMW(0x5b00, 0xffff, MemoryWriteAddress.WR_RAM);
        mra.setMR(0x0000, 0xffff, MemoryReadAddress.RD_RAM);

        // ---------------------------------------------------------------
        // Initialize emulator properties
        // ---------------------------------------------------------------
        EmulatorProperties ep = new EmulatorProperties();
        ep.setCpuDriver(new CpuDriver(z80, CPU_CLOCK_SPEED, mra, mwa, null, null, null, 0));
        ep.setFPS(50);
        ep.setVideoDimensions(256, 192);
        ep.setVisibleArea(0, 255, 0, 191);
        ep.setSoundChip(speaker);
        ep.setVideoUpdater(video);

        this.soundUpdatesPerLine = (float) ((float) Settings.SOUND_SAMPLING_FREQ / (50f * (float) VIDEO_LINES));
        this.soundPtr = 0;
        System.out.println("SoundUpdatesPerLine = " + soundUpdatesPerLine);

        this.init(ep);
        //loadZ80(driver);
        this.reset(true);

        return this;
    }

    @Override
    public Object getObject(int i) {
        return null;
    }

    private void loadZ80(String name) {
       /* for (int i = 0; i < ROM_LIST.length; i++) {
            String[] g = ROM_LIST[i];
            if (g[0].equals(name)) {
                Loader zl = new Loader(this);
                int crc = (int) Long.parseLong(g[1], 16);
                int size = Integer.parseInt(g[2], 10);

                zl.load(name, crc, size);
                return;
            }
        }*/

		RomLoader var9 = new RomLoader();
		var9.setZip("ZX48");
		var9.setMemory(this.mem);
		var9.loadROM("ZX48.ROM", 0, 16384, 1);
		try {
            var9.loadZip(new URL("z80"));
        } catch (Exception e){
		    e.printStackTrace(System.out);
        }

        //System.out.println(name + " not found!");
    }

    boolean irq = true;


    /**
     * This is called every frame
     */
    public BitMap refresh(boolean render) {
        // Check pause button -------------------------
        /*if (JEmu2.IS_APPLET || Keyboard.isCreated()) {
            if (!JEmu2.IS_APPLET) {
                if (Keyboard.isKeyDown(Keyboard.KEY_F6)) {
                    if (!keyf6) {
                        saveState();
                        keyf6 = true;
                    }
                } else {
                    keyf6 = false;
                }
                if (Keyboard.isKeyDown(Keyboard.KEY_F7)) {
                    if (!keyf7) {
                        loadState();
                        keyf7 = true;
                    }
                } else {
                    keyf7 = false;
                }
            } else {
                if (KeyboardRemap.state[Keyboard.KEY_6]) {
                    if (!keyf6) {
                        saveState();
                        keyf6 = true;
                    }
                } else {
                    keyf6 = false;
                }
                if (KeyboardRemap.state[Keyboard.KEY_7]) {
                    if (!keyf7) {
                        loadState();
                        keyf7 = true;
                    }
                } else {
                    keyf7 = false;
                }

            }
        }*/
        for (int i = 0; i < VIDEO_LINES; i++) {
            z80.exec(228);
            soundPtr += soundUpdatesPerLine;
            while (soundPtr >= 1f && speaker.getBufferLength() > 0) {
                speaker.calcSample();
                soundPtr -= 1f;
            }
        }

        se.update();
        z80.interrupt(Cpu.INTERRUPT_TYPE_IRQ, irq);
        //irq = !irq; // hack
        return video.renderFrame();
    }

    class IO extends BasicCpuBoard {
        /**
         * Write to port
         */
        public void out(int port, int value) {
            if ((port & 1) == 0) { // ULA
                speaker.setAmplitude((value & 0x10) != 0 ? 0x7fff : 0);
                z80.addTStates(4);
            }
        }

        /**
         * Read from port
         */
        public int in(int port) {

            int ret = 0xff;
            if ((port & 1) == 0) { // ULA
                z80.addTStates(1);
            }

            return ret;
        }


        /**
         * Save the machine state.
         */
        private void saveState() {
            try {
                state = new MachineState(driver);
                state.add(z80);
                state.add(mem);
                state.save();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        /**
         * Load the machine state.
         */
        private void loadState() {
            try {
                state.load();
                z80.restoreState(state.getByteArray(0));
                mem = state.getChars(1);

                // don't allow high score uploads after loading a game state
                //this.setHighScoreSupported(false);

            } catch (IOException e) {
                System.out.println("Machine state could not be loaded:");
                System.out.println(e);
            }
        }

        public Object getObject(int enumProperty) {
            return null;
        }

    }
}
