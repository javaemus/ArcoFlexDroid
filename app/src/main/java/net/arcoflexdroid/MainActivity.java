package net.arcoflexdroid;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.StrictMode;
import android.view.KeyEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import net.arcoflexdroid.engines.ArcoFlexClock;
import net.arcoflexdroid.engines.ArcoFlexEngine;
import net.arcoflexdroid.panels.about.ArcoFlexAboutOpenActivity;
import net.arcoflexdroid.panels.filebrowser.ArcoFlexFileOpenActivity;
import net.arcoflexdroid.views.ArcoFlexEmulatorView;

import arcadeflex056.osdepend;
import arcadeflex056.settings;
import arcoflex056.platform.platformConfigurator;

import static android.Manifest.permission.*;

import static arcadeflex056.video.osd_refresh;
import static arcadeflex056.video.*;
import static arcoflex056.platform.platformConfigurator.ConfigurePlatform;
import static common.util.ConvertArguments;
import static common.util.argc;
import static common.util.argv;
import static mame056.mame.shutdown_machine;

public class MainActivity extends AppCompatActivity {

    public static MainActivity mm;
    public static Thread _emuThread;
    public static String installDIR;
    private ArcoFlexEngine emulatorEngine;
    private ArcoFlexEmulatorView _emuView;
    //private jMESYSControl _emuControl;
    public static int _maxWidth;
    public static int _maxHeight;

    // dialog type
    static final int A_FILE_SELECTOR = 0;
    static final int A_PREFERENCES = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE,READ_EXTERNAL_STORAGE, INTERNET, ACCESS_NETWORK_STATE}, 1);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mm = this;
        _maxWidth = getWindowManager().getDefaultDisplay().getWidth();
        _maxHeight = getWindowManager().getDefaultDisplay().getHeight();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        //runArcadeFlexGame("commando");
        _emuView = (ArcoFlexEmulatorView) findViewById(R.id.ScreenEmulator);

        installDIR = getExternalFilesDir(null).getAbsolutePath()+"/ArcoFlexDroid/";

        arcadeflex056.settings.installationDir = installDIR;

        emulatorEngine = new ArcoFlexEngine(_emuView, new ArcoFlexClock() );
        emulatorEngine.doPause();
        emulatorEngine.start();

        // init emul
        this.emulatorEngine.doResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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


            /*if (_emuView.canvas != null){
                _emuView.mSurfaceHolder.unlockCanvasAndPost(_emuView.canvas);
            }*/






                /*screen.readkey = KeyEvent.KEYCODE_ESCAPE;

                screen.key[screen.readkey] = true;
                osd_refresh();
                screen.key[screen.readkey] = false;*/

                settings.current_platform_configuration = null;
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

            case R.id.action_elevatob:
                runArcadeFlexGame("elevatob");
                return true;
            case R.id.action_gberet:
                runArcadeFlexGame("exprraid");
                return true;

            case R.id.action_commando:
                runArcadeFlexGame("tapper");
                return true;


            case R.id.action_dkong:
                //runConsoleFlexGame("spec128","-snapshot aquaplan.sna");
                runArcadeFlexGame("dkong");
                return true;


            case R.id.action_gng:
                //runConsoleFlexGame("cpc6128");
                runArcadeFlexGame("gng");
                return true;


            case R.id.action_sf2:
                //runConsoleFlexGame("c64");
                runArcadeFlexGame("flicky");
                return true;


            case R.id.action_bankp:
                runArcadeFlexGame("bankp");
                return true;


            case R.id.action_galaga:
                runArcadeFlexGame("galaga");
                return true;


            case R.id.action_galaxian:
                runArcadeFlexGame("galaxian");
                return true;

            case R.id.action_bombjack:
                runArcadeFlexGame("bombjack");
                return true;


            case R.id.action_mario:
                runArcadeFlexGame("mario");
                return true;


            case R.id.action_gunsmoke:
                runArcadeFlexGame("gunsmoke");
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
