package net.arcoflexdroid;

import android.content.Intent;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;

import android.os.StrictMode;
import android.view.KeyEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import net.arcoflexdroid.engines.ArcoFlexClock;
import net.arcoflexdroid.engines.ArcoFlexEngine;
import net.arcoflexdroid.input.ArcoFlexKeyboardMethod;
import net.arcoflexdroid.input.ArcoFlexKeyboardView;
import net.arcoflexdroid.panels.about.ArcoFlexAboutOpenActivity;
import net.arcoflexdroid.panels.filebrowser.ArcoFlexFileOpenActivity;
import net.arcoflexdroid.panels.menu.ArcoFlexGameItem;
import net.arcoflexdroid.panels.menu.ArcoFlexMenuItem;
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
import static mame056.driverH.*;

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
import static net.arcoflexdroid.R.*;

public class MainActivity extends AppCompatActivity {

    public static MainActivity mm;
    public static Thread _emuThread;
    public static String installDIR;
    private ArcoFlexEngine emulatorEngine;
    private ArcoFlexEmulatorView _emuView;
    //private jMESYSControl _emuControl;
    public static int _maxWidth;
    public static int _maxHeight;

    public Keyboard mKeyboard;
    public ArcoFlexKeyboardView mKeyboardView;

    // dialog type
    public static final int A_FILE_SELECTOR = 0;
    public static final int A_PREFERENCES = 1;

    // menu systems
    public HashMap _listConsoleflexDrivers = new HashMap();
    public HashMap _listArcadeflexDrivers = new HashMap();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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

        //runArcadeFlexGame("commando");
        _emuView = (ArcoFlexEmulatorView) findViewById(id.ScreenEmulator);

        // Create the Keyboard
        //mKeyboard= new Keyboard(this,R.xml.keyboard);

        // Lookup the KeyboardView
        //mKeyboardView= (KeyboardView)findViewById(R.id.keyboardview);
        // Attach the keyboard to the view
        //mKeyboardView.setKeyboard( mKeyboard );

        // Do not show the preview balloons
        //mKeyboardView.setPreviewEnabled(false);

        // Install the key handler
        //mKeyboardView.setOnKeyboardActionListener(mOnKeyboardActionListener);
        mKeyboardView = (ArcoFlexKeyboardView) findViewById(R.id.keyboard_view);
        //mKeyboard = new Keyboard(this, xml.keyboard_full);
        mKeyboard = new Keyboard(this, xml.keyboard_pc_full_p1);
        mKeyboardView.setKeyboard(mKeyboard);
        mKeyboardView.setOnKeyboardActionListener(new ArcoFlexKeyboardMethod());
        mKeyboardView.setPreviewEnabled(false);
        mKeyboardView.setVisibility(View.VISIBLE);

        installDIR = getExternalFilesDir(null).getAbsolutePath()+"/ArcoFlexDroid/";

        arcadeflex056.settings.installationDir = installDIR;

        // creates structure of directories
        copyFiles();
        
        emulatorEngine = new ArcoFlexEngine(_emuView, new ArcoFlexClock() );
        emulatorEngine.doPause();
        emulatorEngine.start();

        // init emul
        this.emulatorEngine.doResume();
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

        //printDriverList(platformFlex, listGames);
        for (int _i=0 ; _i<longo ; _i++){
            if (listGames.get(_i) != null) {

                ArcoFlexGameItem _item = listGames.get(_i);

                _menuFlex.add(_item.getDescription());

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

        emulatorEngine = new ArcoFlexEngine(_emuView, new ArcoFlexClock() );
        emulatorEngine.doPause();
        emulatorEngine.start();

        // init emul
        this.emulatorEngine.doResume();

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
        System.out.println("LAnzamps!!!!");
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

                if (emulatorEngine != null)
                    emulatorEngine.kill();
                //emulatorEngine = null;
                //_emuView.canvas=null;

                //settings.MESS = false;
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

            emulatorEngine._killed = false;

        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
    }

    public void runConsoleFlexGame(String _game) {
        System.out.println("ConsoleFlex!!!!");
        try {
            //setEmulatorRunning(false);
            if (screen != null) {
                screen.readkey = KeyEvent.KEYCODE_ESCAPE;

                screen.key[screen.readkey] = true;
                osd_refresh();
            }

            MESS = true;
            drivers = mess056.system.drivers;

            // init devices
            options.image_files = new ImageFile[MAX_IMAGES];
            options.image_count=0;

            /*if (_emuView.canvas != null){
                _emuView.mSurfaceHolder.unlockCanvasAndPost(_emuView.canvas);
            }*/






                /*screen.readkey = KeyEvent.KEYCODE_ESCAPE;

                screen.key[screen.readkey] = true;
                osd_refresh();
                screen.key[screen.readkey] = false;*/

            current_platform_configuration = null;
            //shutdown_machine();

            if (emulatorEngine != null)
                emulatorEngine.kill();
            //emulatorEngine = null;
            //_emuView.canvas=null;

            //settings.MESS = false;
            ConfigurePlatform(null);
            ConfigurePlatform((platformConfigurator.i_platform_configurator)new arcoflex056.platform.android.android_Configurator());
            //ConvertArguments("arcadeflex", new String[]{"gunsmoke"});//new String[]{"coleco","-cart","HERO.col"});
            ConvertArguments("consoleflex", new String[]{_game}); //, "-cassette","SilkWorm(1988)(Mastertronic).wav"});
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

            emulatorEngine._killed = false;

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
                System.out.println("Pulso SAVE");
                //MainActivity.mm.emulator.keyPress(25);
                //MainActivity.mm.emulator.keyRelease(25);
                screen.readkey = KeyEvent.KEYCODE_DPAD_LEFT;
                screen.key[screen.readkey] = true;
                osd_refresh();
                screen.readkey = KeyEvent.KEYCODE_DPAD_RIGHT;
                screen.key[screen.readkey] = false;
                osd_refresh();
                return true;

            case R.id.menu_start:
                System.out.println("Pulso NEW");
                //MainActivity.mm.emulator.keyRelease(21);
                screen.readkey = KeyEvent.KEYCODE_DPAD_RIGHT;
                screen.key[screen.readkey] = true;
                osd_refresh();
                screen.readkey = KeyEvent.KEYCODE_DPAD_LEFT;
                screen.key[screen.readkey] = false;
                osd_refresh();
                return true;

            case R.id.action_about: startActivityForResult(new Intent(this, ArcoFlexAboutOpenActivity.class), A_FILE_SELECTOR); return true;
            case R.id.action_file: startActivityForResult(new Intent(this, ArcoFlexFileOpenActivity.class), A_FILE_SELECTOR); return true;

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
            runConsoleFlexGame(_myItem.getShortname());

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

}
