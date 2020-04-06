package net.arcoflexdroid;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.opengl.ETC1;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
//import android.support.v7.widget.Toolbar;

import net.arcoflexdroid.helpers.DialogHelper;
import net.arcoflexdroid.helpers.MainHelper;
import net.arcoflexdroid.helpers.MenuHelper;
import net.arcoflexdroid.helpers.PrefsHelper;
import net.arcoflexdroid.input.ControlCustomizer;
import net.arcoflexdroid.input.InputHandler;
import net.arcoflexdroid.input.InputHandlerExt;
import net.arcoflexdroid.input.InputHandlerFactory;
import net.arcoflexdroid.views.ArcoFlexEmulatorView;
import net.arcoflexdroid.views.EmulatorViewGL;
import net.arcoflexdroid.views.IEmuView;
import net.arcoflexdroid.views.InputView;
//import net.movegaga.jemu2.EmulatorFactory;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import arcadeflex056.osdepend;
import arcadeflex056.settings;
import arcoflex056.platform.platformConfigurator;

import static arcoflex056.platform.platformConfigurator.ConfigurePlatform;
import static common.util.ConvertArguments;
import static common.util.argc;
import static common.util.argv;
import static arcadeflex056.video.*;

//import jef.video.BitMap;



final class NotificationHelper
{
    private static NotificationManager notificationManager = null;

    public static void addNotification(Context ctx, String onShow, String title, String message)
    {
        if(notificationManager == null)
            notificationManager = (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);
        int icon = R.mipmap.ic_launcher; // TODO: don't hard-code
        long when = System.currentTimeMillis();
        Notification notification = new Notification(icon, /*onShow*/null, when);
        notification.flags |= Notification.FLAG_ONGOING_EVENT | Notification.FLAG_AUTO_CANCEL;
        CharSequence contentTitle = title;
        CharSequence contentText = message;
        Intent notificationIntent = new Intent(ctx, ArcoFlexDroid.class);
        PendingIntent contentIntent = PendingIntent.getActivity(ctx, 0, notificationIntent, 0);

        /*TODO*///        notification.setLatestEventInfo(ctx, contentTitle, contentText, contentIntent);
        notificationManager.notify(1, notification);
    }

    public static void removeNotification()
    {
        if(notificationManager != null)
            notificationManager.cancel(1);
    }
}

public class ArcoFlexDroid extends Activity {

    public boolean running = false;
    public static int[] pixels;

    public View emuView = null;
    public static ImageView mImgEm;
    public static int _maxWidth;
    public static int _maxHeight;

    protected InputView inputView = null;

    protected MainHelper mainHelper = null;
    protected MenuHelper menuHelper = null;
    protected PrefsHelper prefsHelper = null;
    protected DialogHelper dialogHelper = null;

    protected InputHandler inputHandler = null;

    protected FileExplorer fileExplore = null;

    protected NetPlay netPlay = null;

    public NetPlay getNetPlay() {
        return netPlay;
    }

    public FileExplorer getFileExplore() {
        return fileExplore;
    }

    public MenuHelper getMenuHelper() {
        return menuHelper;
    }

    public PrefsHelper getPrefsHelper() {
        return prefsHelper;
    }

    public MainHelper getMainHelper() {
        return mainHelper;
    }

    public DialogHelper getDialogHelper() {
        return dialogHelper;
    }

    public View getEmuView() {
        return emuView;
    }

    public InputView getInputView() {
        return inputView;
    }

    public InputHandler getInputHandler() {
        return inputHandler;
    }

    public static ArcoFlexDroid mm = new ArcoFlexDroid();




    // JEmu2
    //public jef.machine.Emulator emulator;
    // end JEmu2

    class AnimationLoop implements Runnable {
        AnimationLoop() {
        }


        public void run() {
            while (true) {

                // JEmu2
                /*BitMap bmp = ArcoFlexDroid.this.emulator.refresh(true);
                ArcoFlexDroid.this.pixels = bmp.getPixels();

                Bitmap bma = Bitmap.createBitmap(bmp.getWidth(), bmp.getHeight(), Bitmap.Config.RGB_565);
                bma.setPixels(ArcoFlexDroid.this.pixels, 0, bmp.getWidth(), 0, 0, bmp.getWidth(), bmp.getHeight());
                // end
                 */
                ((arcoflex056.platform.android.android_softwareGFXClass)(settings.current_platform_configuration.get_software_gfx_class())).blit2();
            }
        }
    }

    private  void drawFrame() {
        System.out.println("drawFrame!");


        //if(mm.getEmuView() instanceof EmulatorViewGL)
        //    ((ArcoFlexEmulatorView)mm.getEmuView()).requestRender();

    }


    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        Log.d("EMULATOR", "onCreate " + this);
        System.out.println("onCreate intent:" + getIntent().getAction());

        overridePendingTransition(0, 0);
        getWindow().setWindowAnimations(0);

        _maxWidth = getWindowManager().getDefaultDisplay().getWidth();
        _maxHeight = getWindowManager().getDefaultDisplay().getHeight();

        prefsHelper = new PrefsHelper(this);

        dialogHelper = new DialogHelper(this);

        mainHelper = new MainHelper(this);

        fileExplore = new FileExplorer(this);

        netPlay = new NetPlay(this);

        menuHelper = new MenuHelper(this);

        inputHandler = InputHandlerFactory.createInputHandler(this);

        mainHelper.detectDevice();

        inflateViews();

        Emulator.setArcoFlexDroid(this);

        mainHelper.updateArcoFlexDroid();

        //mainHelper.checkNewViewIntent(this.getIntent());

        if (!Emulator.isEmulating()) {
            if (prefsHelper.getROMsDIR() == null) {
                if (DialogHelper.savedDialog == DialogHelper.DIALOG_NONE)
                    showDialog(DialogHelper.DIALOG_ROMs_DIR);
            } else {
                boolean res = getMainHelper().ensureInstallationDIR(mainHelper.getInstallationDIR());
                if (res == false) {
                    this.getPrefsHelper().setInstallationDIR(this.getPrefsHelper().getOldInstallationDIR());
                } else {
                    try {
                        mm = this;

                        // JEmu2
                        //emulator = new EmulatorFactory().createEmulator("bankp");
                        //this.emulator.reset(true);
                        // end JEmu2

                        ConfigurePlatform((platformConfigurator.i_platform_configurator)new arcoflex056.platform.android.android_Configurator());
                        //ConvertArguments("arcadeflex", new String[]{"gunsmoke"});//new String[]{"coleco","-cart","HERO.col"});
                        ConvertArguments("arcadeflex", new String[]{"atetris", "-snapshot","wotef.sna"});

                        (new Thread(new Runnable() {
                            public void run() {
                                System.exit(osdepend.main(argc, argv));

                            }
                        })).start();

                        //debugMsg("");

                    } catch (Exception e){
                        e.printStackTrace(System.out);
                    }
                    runArcoFlexDroid();

                }
            }
        }
        new Thread(new AnimationLoop()).start();
    }

    public void inflateViews() {
        System.out.println("InflateViews!!!!");
        inputHandler.unsetInputListeners();

        Emulator.setPortraitFull(getPrefsHelper().isPortraitFullscreen());

        boolean full = false;
        if (prefsHelper.isPortraitFullscreen() && mainHelper.getscrOrientation() == Configuration.ORIENTATION_PORTRAIT) {
            System.out.println("FULL!!!!");
            setContentView(R.layout.main_fullscreen);
            full = true;
        } else {
            setContentView(R.layout.main);
        }

        FrameLayout fl = (FrameLayout) this.findViewById(R.id.EmulatorFrame);

        Emulator.setVideoRenderMode(getPrefsHelper().getVideoRenderMode());

        //if (prefsHelper.getVideoRenderMode() == PrefsHelper.PREF_RENDER_SW) {
        	/*
        	if(emuView != null && (emuView instanceof EmulatorViewSW))
        	{
        		EmulatorViewSW s = (EmulatorViewSW)emuView;
        		s.getHolder().removeCallback(s);
        	}*/

        /*    this.getLayoutInflater().inflate(R.layout.emuview_sw, fl);
            emuView = this.findViewById(R.id.EmulatorViewSW);
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN && prefsHelper.getNavBarMode() != PrefsHelper.PREF_NAVBAR_VISIBLE)
                this.getLayoutInflater().inflate(R.layout.emuview_gl_ext, fl);
            else
                this.getLayoutInflater().inflate(R.layout.emuview_gl, fl);

            emuView = this.findViewById(R.id.EmulatorViewGL);
        }*/

        this.getLayoutInflater().inflate(R.layout.emuview_arcoflex, fl);
        //emuView = this.findViewById(R.id.EmulatorViewArcoFlex);
        emuView = new ArcoFlexEmulatorView(ArcoFlexDroid.this);

        mImgEm = findViewById(R.id.EmulatorScreen);
        mImgEm.setImageBitmap(((ArcoFlexEmulatorView)(emuView)).screenBitmap);

        if (full && prefsHelper.isPortraitTouchController()) {
            FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) emuView.getLayoutParams();
            if (lp!=null)
                lp.gravity = Gravity.TOP | Gravity.CENTER;
        }

        inputView = (InputView) this.findViewById(R.id.InputView);

        ((IEmuView) emuView).setArcoFlexDroid(this);

        inputView.setArcoFlexDroid(this);

        View frame = this.findViewById(R.id.EmulatorFrame);
        frame.setOnTouchListener(inputHandler);


        inputHandler.setInputListeners();
    }

    public void runArcoFlexDroid() {

        getMainHelper().copyFiles();
        getMainHelper().removeFiles();

        Emulator.emulate(mainHelper.getLibDir(), mainHelper.getInstallationDIR());

        System.out.println("runArcoFlexDroid!!!!");
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        overridePendingTransition(0, 0);

        inflateViews();

        getMainHelper().updateArcoFlexDroid();

        overridePendingTransition(0, 0);
    }

    //MENU STUFF
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        if (menuHelper != null) {
            if (menuHelper.createOptionsMenu(menu)) return true;
        }

        getMenuInflater().inflate(R.menu.menu_main, menu);
        //return true;

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (menuHelper != null) {
            if (menuHelper.prepareOptionsMenu(menu)) return true;
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (menuHelper != null) {
            if (menuHelper.optionsItemSelected(item))
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //ACTIVITY
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mainHelper != null)
            mainHelper.activityResult(requestCode, resultCode, data);
    }

    //LIVE CYCLE
    @Override
    protected void onResume() {
        Log.d("EMULATOR", "onResume " + this);
        super.onResume();

        if (prefsHelper != null)
            prefsHelper.resume();

        if (DialogHelper.savedDialog != -1)
            showDialog(DialogHelper.savedDialog);
        else if (!ControlCustomizer.isEnabled())
            Emulator.resume();

        if (inputHandler != null) {
            if (inputHandler.getTiltSensor() != null)
                inputHandler.getTiltSensor().enable();
            inputHandler.resume();
        }

        NotificationHelper.removeNotification();
        //System.out.println("OnResume");
    }

    @Override
    protected void onPause() {
        Log.d("EMULATOR", "onPause " + this);
        super.onPause();
        if (prefsHelper != null)
            prefsHelper.pause();
        if (!ControlCustomizer.isEnabled())
            Emulator.pause();
        if (inputHandler != null) {
            if (inputHandler.getTiltSensor() != null)
                inputHandler.getTiltSensor().disable();
        }

        if (dialogHelper != null) {
            dialogHelper.removeDialogs();
        }

        if (prefsHelper.isNotifyWhenSuspend())
            NotificationHelper.addNotification(getApplicationContext(), "ArcoFlexDroid was suspended!", "ArcoFlexDroid was suspended", "Press to return to ArcoFlexDroid");

        //System.out.println("OnPause");
    }

    @Override
    protected void onStart() {
        Log.d("EMULATOR", "onStart " + this);
        super.onStart();
        try {
            InputHandlerExt.resetAutodetected();
        } catch (Error e) {
        }
        ;
        //System.out.println("OnStart");
    }

    @Override
    protected void onStop() {
        Log.d("EMULATOR", "onStop " + this);
        super.onStop();
        //System.out.println("OnStop");
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.d("EMULATOR", "onNewIntent " + this);
        System.out.println("onNewIntent action:" + intent.getAction());
        mainHelper.checkNewViewIntent(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("EMULATOR", "onDestroy " + this);

        View frame = this.findViewById(R.id.EmulatorFrame);
        if (frame != null)
            frame.setOnTouchListener(null);

        if (inputHandler != null) {
            inputHandler.unsetInputListeners();

            if (inputHandler.getTiltSensor() != null)
                inputHandler.getTiltSensor().disable();
        }

        if (emuView != null)
            ((IEmuView) emuView).setArcoFlexDroid(null);

        /*
        if(inputView!=null)
           inputView.setArcoFlexDroid(null);

        if(filterView!=null)
           filterView.setArcoFlexDroid(null);

        prefsHelper = null;

        dialogHelper = null;

        mainHelper = null;

        fileExplore = null;

        menuHelper = null;

        inputHandler = null;

        inputView = null;

        emuView = null;

        filterView = null; */
    }


    //Dialog Stuff
    @Override
    protected Dialog onCreateDialog(int id) {

        if (dialogHelper != null) {
            Dialog d = dialogHelper.createDialog(id);
            if (d != null) return d;
        }
        return super.onCreateDialog(id);
    }

    @Override
    protected void onPrepareDialog(int id, Dialog dialog) {
        if (dialogHelper != null)
            dialogHelper.prepareDialog(id, dialog);
    }

    @Override
    public boolean dispatchGenericMotionEvent(MotionEvent event) {
        if (inputHandler != null)
            return inputHandler.genericMotion(event);
        return false;
    }

}