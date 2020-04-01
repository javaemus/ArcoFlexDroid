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
import net.movegaga.jemu2.EmulatorFactory;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import jef.video.BitMap;


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

    private volatile boolean running = true;
    private static int[] pixels;

    protected View emuView = null;
    public static ImageView mImgEm;

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
    public jef.machine.Emulator emulator;

    class AnimationLoop implements Runnable {
        AnimationLoop() {
        }


        public void run() {
            while (ArcoFlexDroid.this.running) {
                /*try {
                    Thread.sleep(33);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    ArcoFlexDroid.this.getEmuView().postInvalidate();
                }*/
                //JEmu2Droid.main.postInvalidate();
                //ArcoFlexDroid.this.drawFrame();
                //JEmu2Droid.main.drawFrame();
                //System.out.println("Pinto!!!!");

                BitMap bmp = ArcoFlexDroid.this.emulator.refresh(true);
                ArcoFlexDroid.this.pixels = bmp.getPixels();

                Bitmap bma = Bitmap.createBitmap(bmp.getWidth(), bmp.getHeight(), Bitmap.Config.RGB_565);
                bma.setPixels(ArcoFlexDroid.this.pixels, 0, bmp.getWidth(), 0, 0, bmp.getWidth(), bmp.getHeight());
                //bm.recycle();

                //Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, 224, 256, matrix, false);
                int displayWidth = getWindowManager().getDefaultDisplay().getWidth();
                int maxWidth = displayWidth;
                int scaleRatio = displayWidth/bmp.getWidth();
                int maxHeight = bmp.getWidth() * scaleRatio;
                //Bitmap bm = Bitmap.createScaledBitmap(bma,maxWidth, maxHeight, false);

                //converting bitmap object to show in imageview2
                FrameLayout mImg;
                //setContentView(R.layout.main);
                //mImg = findViewById(R.id.EmulatorFrame);
                if (bma != null) {
                    //ArcoFlexDroid.this.getEmuView().draw();
                    //mImgEm = findViewById(R.id.EmulatorScreen);
                    //mImg.addView(mImgEm);
                    //mImgEm.setImageBitmap(Bitmap.createScaledBitmap(bma,maxWidth, maxHeight, false));
                    ((ArcoFlexEmulatorView)(emuView)).drawScreenEmulator( Bitmap.createScaledBitmap(bma,maxWidth, maxHeight, false) );
                    ((ArcoFlexEmulatorView)(emuView)).postInvalidate();
                    mImgEm.setImageBitmap(((ArcoFlexEmulatorView)(emuView)).screenBitmap);
                    //((ArcoFlexEmulatorView)(emuView)).draw();
                    //Bitmap bm = Bitmap.createScaledBitmap(bma,maxWidth, maxHeight, false);
            /*int imageSize = bm.getRowBytes() * bm.getHeight();


            ByteBuffer uncompressedBuffer = ByteBuffer.allocateDirect(imageSize);
            bm.copyPixelsToBuffer(uncompressedBuffer);
            uncompressedBuffer.position(0);

            ByteBuffer compressedBuffer = ByteBuffer.allocateDirect(
                     ETC1.getEncodedDataSize(bm.getWidth(), bm.getHeight())).order(ByteOrder.nativeOrder());
            ETC1.encodeImage(uncompressedBuffer, bm.getWidth(), bm.getHeight(), 2, 2 * bm.getWidth(),
                    compressedBuffer);

            Emulator.screenBuff = compressedBuffer;

             */
                }
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
                        emulator = new EmulatorFactory().createEmulator("bankp");
                        this.emulator.reset(true);
                        //setContentView(main, new ViewGroup.LayoutParams(displayWidth, displayHeight));
                        //setContentView(R.layout.main);

                        //new ArcoFlexVideoTask().execute();

                        //new Thread(new AnimationLoop()).start();

                        (new Thread(new Runnable() {
                            public void run() {
                                while (ArcoFlexDroid.this.running) {
                                    /*try {
                                        Thread.sleep(33);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                        ArcoFlexDroid.this.getEmuView().postInvalidate();
                                    }*/
                                    //JEmu2Droid.main.postInvalidate();
                                    //ArcoFlexDroid.this.drawFrame();
                                    //JEmu2Droid.main.drawFrame();
                                    //System.out.println("Pinto!!!!");

                                    BitMap bmp = ArcoFlexDroid.this.emulator.refresh(true);
                                    //ArcoFlexDroid.this.pixels = bmp.getPixels();

                                    Bitmap bma = Bitmap.createBitmap(bmp.getWidth(), bmp.getHeight(), Bitmap.Config.RGB_565);
                                    bma.setPixels(bmp.getPixels(), 0, bmp.getWidth(), 0, 0, bmp.getWidth(), bmp.getHeight());
                                    //bm.recycle();

                                    //Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, 224, 256, matrix, false);
                                    int displayWidth = getWindowManager().getDefaultDisplay().getWidth();
                                    int maxWidth = displayWidth;
                                    //int scaleRatio = displayWidth/bmp.getWidth();
                                    int maxHeight = getWindowManager().getDefaultDisplay().getHeight();
                                    //Bitmap bm = Bitmap.createScaledBitmap(bma,maxWidth, maxHeight, false);
                                    int bmp_width = bmp.getWidth();
                                    int bmp_height = bmp.getHeight();
                                    float ratioBitmap = (float) bmp_width / (float) bmp_height;
                                    float ratioMax = (float) maxWidth / (float) maxHeight;

                                    int finalWidth = maxWidth;
                                    int finalHeight = maxHeight;
                                    if (ratioMax > 1) {
                                        finalWidth = (int) ((float)maxHeight * ratioBitmap);
                                    } else {
                                        finalHeight = (int) ((float)maxWidth / ratioBitmap);
                                    }

                                    //converting bitmap object to show in imageview2

                                    if (bma != null) {
                                        //((ArcoFlexEmulatorView)(emuView)).invalidate();
                                        ((ArcoFlexEmulatorView)(emuView)).drawScreenEmulator( Bitmap.createScaledBitmap(bma, finalWidth, finalHeight, false) );

                                        //((ArcoFlexEmulatorView)(emuView)).draw();

                                        runOnUiThread(
                                                (new Thread(new Runnable() {
                                            public void run() {

                                                mImgEm.setImageBitmap(((ArcoFlexEmulatorView) (emuView)).screenBitmap);
                                            }
                                        }
                                        ))
                                        );

                                    }
                                }
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