package net.arcoflexdroid;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;

import android.os.StrictMode;
import android.view.KeyEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;

//import net.arcoflexdroid.engines.ArcoFlexClock;
//import net.arcoflexdroid.engines.ArcoFlexEngine;
import net.arcoflexdroid.input.ArcoFlexJoystickView;
import net.arcoflexdroid.input.ArcoFlexKeyboardMethod;
import net.arcoflexdroid.input.ArcoFlexKeyboardView;
import net.arcoflexdroid.input.GameKeyListener;
import net.arcoflexdroid.input.VirtualKeypad;
import net.arcoflexdroid.panels.about.ArcoFlexAboutOpenActivity;
import net.arcoflexdroid.panels.devices.ArcoFlexConfigConsoleFlexDriver;
import net.arcoflexdroid.panels.devices.ArcoFlexConsoleFlexDevices;
import net.arcoflexdroid.panels.filebrowser.ArcoFlexFileOpenActivity;
import net.arcoflexdroid.panels.filebrowser.ArcoFlexJFileChooserDialog;
import net.arcoflexdroid.panels.menu.ArcoFlexGameItem;
import net.arcoflexdroid.views.ArcoFlexEmulatorView;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import arcadeflex056.osdepend;

import static arcadeflex056.fronthlp.frontend_help;
import static arcadeflex056.settings.*;
import static mame056.driver.*;
import arcoflex056.platform.platformConfigurator;
import mess056.messH;
import mess056.system;

import static mame056.driverH.*;

import static mame056.mame.Machine;
import static mame056.mame.options;
import static mame056.mameH.*;

import static android.Manifest.permission.*;

import static arcadeflex056.video.osd_refresh;
import static arcadeflex056.video.*;
import static arcoflex056.platform.platformConfigurator.ConfigurePlatform;
import static common.util.ConvertArguments;
import static common.util.argc;
import static common.util.argv;
import static mame056.mame.shutdown_machine;
import static mame056.mameH.*;
import static mess056.device.devices;
import static net.arcoflexdroid.R.*;

public class MainActivity extends AppCompatActivity implements GameKeyListener {

    public static MainActivity mm;
    public static boolean suspended = false;
    public static boolean first_start = false;
    public static Thread _emuThread;
    public static String installDIR;
    //private ArcoFlexEngine emulatorEngine;
    public ArcoFlexEmulatorView _emuView;
    //private jMESYSControl _emuControl;
    public static int _maxWidth;
    public static int _maxHeight;

    public Keyboard mKeyboard;
    public ArcoFlexKeyboardView mKeyboardView;
    public boolean kbVisibility = false;

    public ArcoFlexJoystickView mJoystick;
    public VirtualKeypad vKeyPad = null;

    // dialog type
    public static final int A_FILE_SELECTOR = 0;
    public static final int A_PREFERENCES = 1;

    // menu systems
    public HashMap _listConsoleflexDrivers = new HashMap();
    public HashMap _listArcadeflexDrivers = new HashMap();

    @Override
    protected void onUserLeaveHint()
    {
        // When user presses home page
        //System.out.println("Home Button Pressed");
        suspended = true;

        super.onUserLeaveHint();
    }

    @Override
    protected void onResume() {

        super.onResume();

        suspended = false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        System.out.println("ONCREATE!!!!");

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE,READ_EXTERNAL_STORAGE, INTERNET, ACCESS_NETWORK_STATE}, 1);

        //AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);

        setContentView(layout.activity_main);

        Toolbar toolbar = findViewById(id.toolbar);
        setSupportActionBar(toolbar);

        mm = this;
        _maxWidth = getWindowManager().getDefaultDisplay().getWidth();
        _maxHeight = getWindowManager().getDefaultDisplay().getHeight();

        inflateViews();

        /*FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        _emuView = (ArcoFlexEmulatorView) findViewById(id.ScreenEmulator);

        // Create the Keyboard
        createKeyboard();

        // Create the Joystick
        createJoystick();

        installDIR = getExternalFilesDir(null).getAbsolutePath()+"/ArcoFlexDroid/";

        arcadeflex056.settings.installationDir = installDIR;

        // creates structure of directories
        copyFiles();

        suspended = false;

    }

    private void createJoystick(){
        mJoystick = (ArcoFlexJoystickView) findViewById(id.joystick_view);
        //mJoystick.setOnKeyboardActionListener(new ArcoFlexKeyboardMethod());
        //mJoystick.setPreviewEnabled(false);
        //mJoystick.setVisibility(View.VISIBLE);
        vKeyPad = new VirtualKeypad(mJoystick, this, R.drawable.dpad5, R.drawable.button, R.drawable.button, R.drawable.button, R.drawable.button, drawable.button_coin_press, drawable.button_start_press );

        mJoystick.setBackgroundColor(Color.TRANSPARENT);
        //vKeyPad.resize(mJoystick.getWidth(), mJoystick.getHeight());
        mJoystick.setVisibility(View.VISIBLE);
        //vKeyPad.resize(mJoystick.getWidth(), mJoystick.getHeight());

        //vKeyPad.resize(mJoystick.getWidth(), mJoystick.getHeight());

    }

    private void createKeyboard() {
        // Install the key handler
        //mKeyboardView.setOnKeyboardActionListener(mOnKeyboardActionListener);
        mKeyboardView = (ArcoFlexKeyboardView) findViewById(R.id.keyboard_view);
        //mKeyboard = new Keyboard(this, xml.keyboard_full);
        mKeyboard = new Keyboard(this, xml.keyboard_pc_full_p1);
        mKeyboardView.setKeyboard(mKeyboard);
        mKeyboardView.setOnKeyboardActionListener(new ArcoFlexKeyboardMethod());
        mKeyboardView.setPreviewEnabled(false);
        mKeyboardView.setVisibility(View.INVISIBLE);
    }

    private void listDrivers(String _platform) {
        frontend_help(2, new String[] {_platform, "-list"});
    }

    private void createMenuArcadeFlex(Menu menu) {
        GameDriver[] _driversArcadeFlex = mame056.driver.driversArcadeFlex;
        Menu _menu = menu.addSubMenu("ArcadeFlex");
        createDynamicMenu("arcadeflex", _driversArcadeFlex, _menu);
    }

    private void createMenuConsoleFlex(Menu menu) {
        GameDriver[] _driversConsoleFlex = mess056.system.drivers;
        Menu _menu = menu.addSubMenu("ConsoleFlex");
        createDynamicMenu("consoleflex", _driversConsoleFlex, _menu);
    }

    private void createDynamicMenu(String platformFlex, GameDriver[] driverList, Menu _menuFlex) {
        List<ArcoFlexGameItem> listGames = new ArrayList<ArcoFlexGameItem>();

        int longo = driverList.length;

        for (int _i=0 ; _i<longo; _i++){
            if (driverList[_i] != null) {
                ArcoFlexGameItem currDriver = new ArcoFlexGameItem(driverList[_i].name, driverList[_i].description);
                listGames.add(currDriver);
            }
        }

        longo = listGames.size();
        System.out.println(platformFlex + " supports "+longo+" drivers");

        Collections.sort(listGames);

        byte _lastAlphabeticMenuCreated=0x00;
        Menu _currAlphaMenu = null;

        for (int _i=0 ; _i<longo ; _i++){
            if (listGames.get(_i) != null) {

                ArcoFlexGameItem _item = listGames.get(_i);

                // creates a menu folder with the first char of the description
                if (_item.getDescription().getBytes()[0] != _lastAlphabeticMenuCreated) {
                    _lastAlphabeticMenuCreated = _item.getDescription().getBytes()[0];
                    char _charLetter = (char) _lastAlphabeticMenuCreated;
                    _currAlphaMenu = _menuFlex.addSubMenu(Character.toString(_charLetter));
                }

                _currAlphaMenu.add(_item.getDescription());

                if (platformFlex.equals("consoleflex"))
                    _listConsoleflexDrivers.put(_item.getDescription(), _item);
                else
                    _listArcadeflexDrivers.put(_item.getDescription(), _item);

            }
        }
    }

    private void printDriverList(String platformFlex, List<ArcoFlexGameItem> listGames) {
        int longo = listGames.size();
        System.out.println("**************************************************");
        System.out.println(longo+" Supported Drivers for "+platformFlex);

        for (int _i=0 ; _i<longo ; _i++)
            System.out.println(listGames.get(_i).getDescription());

        System.out.println("**************************************************");
    }

    public void inflateViews() {
        System.out.println("InflateViews!!!!");

        /*boolean full = false;
        if (prefsHelper.isPortraitFullscreen() && mainHelper.getscrOrientation() == Configuration.ORIENTATION_PORTRAIT) {
            System.out.println("FULL!!!!");
            setContentView(R.layout.main_fullscreen);
            full = true;
        } else {
            setContentView(R.layout.main);
        }

         */

        /*FrameLayout fl = (FrameLayout) this.findViewById(R.id.EmulatorFrame);



        this.getLayoutInflater().inflate(R.layout.emuview_arcoflex, fl);*/
        //emuView = this.findViewById(R.id.EmulatorViewArcoFlex);
        _emuView = (ArcoFlexEmulatorView) findViewById(id.ScreenEmulator);

        installDIR = getExternalFilesDir(null).getAbsolutePath()+"/ArcoFlexDroid/";

        arcadeflex056.settings.installationDir = installDIR;

        /*emulatorEngine = new ArcoFlexEngine(_emuView, new ArcoFlexClock() );
        emulatorEngine.doPause();
        emulatorEngine.start();

        // init emul
        this.emulatorEngine.doResume();*/

        //mImgEm = findViewById(R.id.EmulatorScreen);
        //mImgEm.setImageBitmap(((ArcoFlexEmulatorView)(emuView)).screenBitmap);

        /*if (full && prefsHelper.isPortraitTouchController()) {
            FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) emuView.getLayoutParams();
            if (lp!=null)
                lp.gravity = Gravity.TOP | Gravity.CENTER;
        }*/

        /*inputView = (InputView) this.findViewById(R.id.InputView);

        ((IEmuView) emuView).setArcoFlexDroid(this);

        inputView.setArcoFlexDroid(this);

        View frame = this.findViewById(R.id.EmulatorFrame);
        frame.setOnTouchListener(inputHandler);


        inputHandler.setInputListeners();*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        Menu menuSystems = menu.addSubMenu("Systems");
        // creates menu entries
        //listDrivers("consoleflex");
        createMenuConsoleFlex(menuSystems);
        //listDrivers("arcadeflex");
        createMenuArcadeFlex(menuSystems);
        return true;
    }

    public void runArcadeFlexGame(String _game) {
        System.out.println("Lanzamps!!!!");
        suspended = false;
        try {
            //setEmulatorRunning(false);
            if (screen != null) {
                screen.readkey = KeyEvent.KEYCODE_ESCAPE;

                screen.key[screen.readkey] = true;
                osd_refresh();
            }

            MESS = false;
            drivers = driversArcadeFlex;


            /*if (_emuView.canvas != null){
                _emuView.mSurfaceHolder.unlockCanvasAndPost(_emuView.canvas);
            }*/






                /*screen.readkey = KeyEvent.KEYCODE_ESCAPE;

                screen.key[screen.readkey] = true;
                osd_refresh();
                screen.key[screen.readkey] = false;*/

                current_platform_configuration = null;
                //shutdown_machine();

                /*if (emulatorEngine != null)
                    emulatorEngine.kill();
                //emulatorEngine = null;*/
                //_emuView.canvas=null;

                //settings.MESS = false;
                switchKeyboard(false);
                ConfigurePlatform(null);
                ConfigurePlatform((platformConfigurator.i_platform_configurator)new arcoflex056.platform.android.android_Configurator());
                //ConvertArguments("arcadeflex", new String[]{"gunsmoke"});//new String[]{"coleco","-cart","HERO.col"});
                ConvertArguments("arcadeflex", new String[]{_game});
                //_emuThread = null;


            //_emuThread.stop();
            if (_emuThread != null) {
                _emuThread.interrupt();
                shutdown_machine();
                screen_colors = 0;
                update_video_first_time = 0;
                mame056.sound.ay8910.num = 0;
                mame056.sound.ay8910.ay8910_index_ym = 0;

            }
            _emuThread = null;

            inflateViews();

            _emuThread = (new Thread(new Runnable() {
                public void run() {
                    try {
                        osdepend.main(argc, argv);
                    } catch (RuntimeException e){
                        System.out.println("RuntimeException!!!!!!!!!!!!");
                        e.printStackTrace(System.out);
                    } catch (Exception e){
                        e.printStackTrace(System.out);
                    }

                }
            }));

            _emuThread.start();

            //emulatorEngine._killed = false;

        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
    }

    public void runConsoleFlexGame(String _game, String[] _parameters) {
        System.out.println("ConsoleFlex!!!!");
        suspended = false;
        try {
            //setEmulatorRunning(false);
            if (screen != null) {
                screen.readkey = KeyEvent.KEYCODE_ESCAPE;

                screen.key[screen.readkey] = true;
                osd_refresh();
            }

            MESS = true;
            drivers = mess056.system.drivers;



            // deletes devices???? --> Machine.gamedrv.dev
            

            /*if (Machine != null && Machine.gamedrv != null)
                Machine.gamedrv.dev = null;*/

             

            // init devices
            options.image_files = new ImageFile[MAX_IMAGES];
            options.image_count=0;
            Machine.gamedrv.name = _game;


            /*if (_emuView.canvas != null){
                _emuView.mSurfaceHolder.unlockCanvasAndPost(_emuView.canvas);
            }*/






                /*screen.readkey = KeyEvent.KEYCODE_ESCAPE;

                screen.key[screen.readkey] = true;
                osd_refresh();
                screen.key[screen.readkey] = false;*/

            current_platform_configuration = null;
            //shutdown_machine();

            /*if (emulatorEngine != null)
                emulatorEngine.kill();*/
            //emulatorEngine = null;
            //_emuView.canvas=null;

            //settings.MESS = false;
            switchKeyboard(true);
            ConfigurePlatform(null);
            ConfigurePlatform((platformConfigurator.i_platform_configurator)new arcoflex056.platform.android.android_Configurator());
            //ConvertArguments("arcadeflex", new String[]{"gunsmoke"});//new String[]{"coleco","-cart","HERO.col"});
            if (_parameters == null) {
                System.out.println("1");
                ConvertArguments("consoleflex", new String[]{_game}); //, "-cassette","SilkWorm(1988)(Mastertronic).wav"});
            } else {
                System.out.println("2");
                ConvertArguments("consoleflex", _parameters); //, "-cassette","SilkWorm(1988)(Mastertronic).wav"});
                //_emuThread = null;
            }


            //_emuThread.stop();
            if (_emuThread != null) {
                _emuThread.interrupt();
                shutdown_machine();
                screen_colors = 0;
                update_video_first_time = 0;
                mame056.sound.ay8910.num = 0;
                mame056.sound.ay8910.ay8910_index_ym = 0;

            }
            _emuThread = null;

            inflateViews();

            _emuThread = (new Thread(new Runnable() {
                public void run() {
                    try {
                        System.out.println("ARGC: "+argc);
                        for (int _i=0;_i<argc;_i++)
                            System.out.println("ARGV: "+argv[_i]);
                        //System.out.println("*********DEV "+Machine.gamedrv.dev);
                        /*Runtime rt = Runtime.getRuntime();
                        Process proc = rt.exec(osdepend.main(argc, argv));
                        Runtime.exec();

                         */

                        osdepend.main(argc, argv);

                    } catch (RuntimeException e){
                        System.out.println("RuntimeException!!!!!!!!!!!!");
                        e.printStackTrace(System.out);
                    } catch (Exception e){
                        e.printStackTrace(System.out);
                    }

                }
            }));

            _emuThread.start();

            //emulatorEngine._killed = false;

        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        switch (id) {
            case R.id.menu_coin:
                System.out.println("Pulso RESET");
                //MainActivity.mm.emulator.keyPress(25);
                //MainActivity.mm.emulator.keyRelease(25);
                /*if ((screen != null) && (screen.key != null)) {
                    screen.readkey = KeyEvent.KEYCODE_DPAD_LEFT;
                    screen.key[screen.readkey] = true;
                    osd_refresh();
                    screen.readkey = KeyEvent.KEYCODE_DPAD_RIGHT;
                    screen.key[screen.readkey] = false;
                    osd_refresh();
                }*/

                mame056.cpuexec.machine_reset();

                return true;

            case R.id.menu_start:
                System.out.println("Pulso SWITCH");

                kbVisibility = !kbVisibility;
                switchKeyboard(kbVisibility);
                //MainActivity.mm.emulator.keyRelease(21);
                /*if ((screen != null) && (screen.key != null)) {
                    screen.readkey = KeyEvent.KEYCODE_DPAD_RIGHT;
                    screen.key[screen.readkey] = true;
                    osd_refresh();
                    screen.readkey = KeyEvent.KEYCODE_DPAD_LEFT;
                    screen.key[screen.readkey] = false;
                    osd_refresh();
                }*/
                return true;

            case R.id.action_about:
                //startActivityForResult(new Intent(this, ArcoFlexAboutOpenActivity.class), A_FILE_SELECTOR);
                ArcoFlexAboutOpenActivity _dialogAbout = new ArcoFlexAboutOpenActivity();
                FragmentManager fm = MainActivity.mm.getSupportFragmentManager();
                _dialogAbout.show(fm, "About ArcoFlexDroid");
                return true;
            case R.id.action_file:
                //startActivityForResult(new Intent(this, ArcoFlexFileOpenActivity.class), A_FILE_SELECTOR);
                ArcoFlexJFileChooserDialog _myDialogFileExplorer = ArcoFlexJFileChooserDialog.newInstance("Some Title");


                FragmentManager fm2 = MainActivity.mm.getSupportFragmentManager();
                _myDialogFileExplorer.show(fm2, "FileExplorer");
                return true;

        }

        // else is a system menu
        String _currSystem = item.getTitle().toString();
        boolean isSystemMenu = false;
        Object _sysItem = null;
        // check ConsoleFlex
        _sysItem = _listConsoleflexDrivers.get(_currSystem);
        System.out.println("ConsoleFlex con "+_currSystem+" = "+_sysItem);
        if (_sysItem != null){
            ArcoFlexGameItem _myItem = (ArcoFlexGameItem) _sysItem;
            showConsoleFlexDevices(_myItem);
            //runConsoleFlexGame(_myItem.getShortname(), null);

            return true;
        }


        _sysItem = _listArcadeflexDrivers.get(_currSystem);
        System.out.println("ArcadeFlex con "+_currSystem+" = "+_sysItem);

        if (_sysItem != null){
            ArcoFlexGameItem _myItem = (ArcoFlexGameItem) _sysItem;
            runArcadeFlexGame(_myItem.getShortname());

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showConsoleFlexDevices(ArcoFlexGameItem _myItem) {
        String consoleflexDriverName = _myItem.getShortname();
        try {
            GameDriver _driver = null;
            GameDriver[] list = system.drivers;

            int _numDrivers = list.length;
            for (int _i=0 ; _i<_numDrivers ; _i++) {
                if (list[_i] != null && list[_i].name.equals(consoleflexDriverName)) {
                    _driver = list[_i];
                }
            }


            ArcoFlexConsoleFlexDevices.input_ports = _driver.input_ports;
            ArcoFlexConsoleFlexDevices.m_input_ports = Machine.input_ports;
            ArcoFlexConsoleFlexDevices.dev = _driver.dev;
            ArcoFlexConsoleFlexDevices.gamedrv = _driver;


            ArcoFlexConfigConsoleFlexDriver._SystemName = consoleflexDriverName;
            ArcoFlexConsoleFlexDevices.gamedrv.name = consoleflexDriverName;

            //startActivityForResult(new Intent(this, ArcoFlexConfigConsoleFlexDriver.class), A_FILE_SELECTOR);
            ArcoFlexConfigConsoleFlexDriver _myDialogDevices = new ArcoFlexConfigConsoleFlexDriver();


            FragmentManager fm2 = MainActivity.mm.getSupportFragmentManager();
            _myDialogDevices.show(fm2, "Driver Configuration");

        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
    }

    public void switchKeyboard(boolean _switch) {
        kbVisibility = _switch;

        if (!kbVisibility) {
            mKeyboardView.setVisibility(View.INVISIBLE);
            mJoystick.setVisibility(View.VISIBLE);
            vKeyPad.resize(mJoystick.getWidth(), mJoystick.getHeight());
        } else {
            mKeyboardView.setVisibility(View.VISIBLE);
            mJoystick.setVisibility(View.INVISIBLE);
        }

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if (newConfig.orientation==Configuration.ORIENTATION_LANDSCAPE)
            System.out.println("LANDSCAPE!!!!");
        else if (newConfig.orientation==Configuration.ORIENTATION_PORTRAIT)
            System.out.println("PORTRAIT!!!!");
    }

    /*@Override
 	protected int[] getKeyLayouts(int orientation) {
         		if (orientation == Configuration.ORIENTATION_LANDSCAPE)
         			return new int [] { R.xml.c64_joystick_landscape, R.xml.c64_qwerty, R.xml.c64_qwerty2 };
         		return new int [] { R.xml.c64_joystick, R.xml.c64_qwerty, R.xml.c64_qwerty2 };
    }*/

    public void copyFiles(){

        int BUFFER_SIZE = 1024;

        try {

            String roms_dir = installDIR;

            File fm = new File(roms_dir + File.separator + "saves/" + "dont-delete-"+ version+".bin");
            if(fm.exists())
                return;

            fm.mkdirs();
            fm.createNewFile();

            // Create a ZipInputStream to read the zip file
            BufferedOutputStream dest = null;
            InputStream fis = mm.getResources().openRawResource(R.raw.files);
            ZipInputStream zis = new ZipInputStream(

                    new BufferedInputStream(fis));
            // Loop over all of the entries in the zip file
            int count;
            byte data[] = new byte[BUFFER_SIZE];
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                if (!entry.isDirectory()) {

                    String destination = roms_dir;
                    String destFN = destination + File.separator + entry.getName();
                    // Write the file to the file system
                    FileOutputStream fos = new FileOutputStream(destFN);
                    dest = new BufferedOutputStream(fos, BUFFER_SIZE);
                    while ((count = zis.read(data, 0, BUFFER_SIZE)) != -1) {
                        dest.write(data, 0, count);
                    }
                    dest.flush();
                    dest.close();
                }
                else
                {
                    File f = new File(roms_dir+ File.separator + entry.getName());
                    f.mkdirs();
                }

            }
            zis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int currentKeyStates = 0;

    public static int current_keycodes [] = new int[15];

    @Override
    public void onGameKeyChanged(int keyStates) {
        if (_emuView != null) {
            manageKey(keyStates, VirtualKeypad.BUTTON_FIRE_1, current_keycodes[0]);
            manageKey(keyStates, VirtualKeypad.UP, current_keycodes[2]);
            manageKey(keyStates, VirtualKeypad.DOWN, current_keycodes[3]);
            manageKey(keyStates, VirtualKeypad.LEFT, current_keycodes[4]);
            manageKey(keyStates, VirtualKeypad.RIGHT, current_keycodes[5]);
            manageKey(keyStates, VirtualKeypad.UP | VirtualKeypad.LEFT, current_keycodes[6]);
            manageKey(keyStates, VirtualKeypad.UP | VirtualKeypad.RIGHT, current_keycodes[7]);
            manageKey(keyStates, VirtualKeypad.DOWN | VirtualKeypad.LEFT, current_keycodes[8]);
            manageKey(keyStates, VirtualKeypad.DOWN | VirtualKeypad.RIGHT, current_keycodes[9]);

            manageKey(keyStates, VirtualKeypad.BUTTON_COIN, current_keycodes[10]);
            manageKey(keyStates, VirtualKeypad.BUTTON_START, current_keycodes[11]);

            manageKey(keyStates, VirtualKeypad.BUTTON_FIRE_2, current_keycodes[12]);
            manageKey(keyStates, VirtualKeypad.BUTTON_FIRE_3, current_keycodes[13]);
            manageKey(keyStates, VirtualKeypad.BUTTON_FIRE_4, current_keycodes[14]);
        }

        currentKeyStates = keyStates;

    }

    private void manageKey(int keyStates, int key, int press) {
        /*final int keyStates = _keyStates;
        final int key = _key;
        final int press = _press;

        new Thread(new Runnable() {
            @Override
            public void run() {*/


        if ((keyStates & key) == key && (currentKeyStates & key) == 0) {
            // Log.i("FC64", "down: " + press );
            //mainView.actionKey(true, press);
            System.out.println("ManageKey PRESS DOWN "+key);
            if (screen !=null && screen.key != null) {
                switch (key) {
                    case 1: // left
                        screen.readkey = KeyEvent.KEYCODE_DPAD_LEFT;
                        break;
                    case 2: // right
                        screen.readkey = KeyEvent.KEYCODE_DPAD_RIGHT;
                        break;
                    case 4: // up
                        screen.readkey = KeyEvent.KEYCODE_DPAD_UP;
                        break;
                    case 8: // down
                        screen.readkey = KeyEvent.KEYCODE_DPAD_DOWN;
                        break;
                    case 16: // fire
                        screen.readkey = KeyEvent.KEYCODE_CTRL_LEFT;
                        break;
                    case 32: // coin
                        screen.readkey = KeyEvent.KEYCODE_5;
                        break;
                    case 64: // start
                        screen.readkey = KeyEvent.KEYCODE_1;
                        break;
                    case 128: // fire 2
                        screen.readkey = KeyEvent.KEYCODE_ALT_LEFT;
                        break;
                    case 256: // fire 3
                        screen.readkey = KeyEvent.KEYCODE_SPACE;
                        break;
                    case 512: // fire 4
                        screen.readkey = KeyEvent.KEYCODE_Z;
                        break;
                }

                screen.key[screen.readkey] = true;
                osd_refresh();
            }
        } else if ((keyStates & key) == 0 && (currentKeyStates & key) == key) {
            // Log.i("FC64", "up: " + press );
            //mainView.actionKey(false, press);
            System.out.println("ManageKey PRESS UP "+key);
            if (screen !=null && screen.key != null) {
                switch (key) {
                    case 1: // left
                        screen.readkey = KeyEvent.KEYCODE_DPAD_LEFT;
                        break;
                    case 2: // right
                        screen.readkey = KeyEvent.KEYCODE_DPAD_RIGHT;
                        break;
                    case 4: // up
                        screen.readkey = KeyEvent.KEYCODE_DPAD_UP;
                        break;
                    case 8: // down
                        screen.readkey = KeyEvent.KEYCODE_DPAD_DOWN;
                        break;
                    case 16: // fire
                        screen.readkey = KeyEvent.KEYCODE_CTRL_LEFT;
                        break;
                    case 32: // coin
                        screen.readkey = KeyEvent.KEYCODE_5;
                        break;
                    case 64: // start
                        screen.readkey = KeyEvent.KEYCODE_1;
                        break;
                    case 128: // fire 2
                        screen.readkey = KeyEvent.KEYCODE_ALT_LEFT;
                        break;
                    case 256: // fire 3
                        screen.readkey = KeyEvent.KEYCODE_SPACE;
                        break;
                    case 512: // fire 4
                        screen.readkey = KeyEvent.KEYCODE_Z;
                        break;
                }

                screen.key[screen.readkey] = false;
                osd_refresh();
            }
        }
            /*}
        }).start();*/
    }

}
