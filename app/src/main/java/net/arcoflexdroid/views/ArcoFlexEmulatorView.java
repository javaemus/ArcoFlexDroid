package net.arcoflexdroid.views;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.preference.PreferenceManager;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import net.arcoflexdroid.MainActivity;

import java.nio.ShortBuffer;
import java.util.Set;

import arcadeflex056.settings;

import static arcadeflex056.video.screen;
import static mame056.mame.Machine;

public class ArcoFlexEmulatorView  extends SurfaceView implements SensorEventListener, SurfaceHolder.Callback {

    /**
     * SurfaceHolder where we paint
     */
    //private final SurfaceHolder holder;
    public boolean showTestData = true;
    private boolean cLocked = false;


    //public Canvas canvas;

    //ScreenUpdater updater;
    public SurfaceHolder mSurfaceHolder;
    public static ShortBuffer drawCanvasBufferAsShort;
    public Matrix matrixScreen=null;

    public int	m_nPix[] = null;

    private int width = 1024;
    private int height = 768;

    private int[]	m_colorPix = new int[width*height];

    private float fscaleX = 4.0f;
    private float fscaleY = 4.0f;

    public static Bitmap screenBitmap;

    private Context _context;

    /**
     * border color
     */
    //protected Color borderColor = new Color(0);
    /**
     * offset to start printing of C64Screen RGB data
     */
    private int offset;
    /**
     * rectangle where we paint the C64 screen
     */
    private Rect paintRect = null;
    /**
     * rectangle where we paint the C64 border
     */
    private Rect borderRect = null;

    public int getScreenWidth() { return width; }
    public int getScreenHeight() { return height; }

    public ArcoFlexEmulatorView(Context context) {
        super(context);

        _context = context;

        // we need to get key events
        setFocusable(true);
        setFocusableInTouchMode(true);
        setClickable(true);

        // we need to set to transparent pixel format, otherwise the view stays black
        ((Activity) getContext()).getWindow().setFormat(PixelFormat.TRANSPARENT);

        // register our interest in hearing about changes to our surface
        //this.holder = getHolder();
        this.mSurfaceHolder = getHolder();
        //this.holder.addCallback(this);
        this.mSurfaceHolder.addCallback(this);

        // inits pixel array
        int numPix = width * height;
        System.out.println("Número PIXELS: -------> "+numPix);
        m_nPix = new int[numPix];

        for (int i=0 ; i<numPix ; i++){
            m_nPix[i]=0;
        }

        this.updateArcoFlexEmulatorScreen();

        // get the emulator logo
        //this.logo = BitmapFactory.decodeResource(getResources(), R.drawable.a64);

        initArcoFlexUI();
    }



    private void initArcoFlexUI() {

    }

    private void initMatrix() {
        //if (matrixScreen == null) {

        int scaleX = (int)(fscaleX);
        int scaleY = (int)(fscaleY);
        int bufferWidth = getScreenWidth();
        int bufferHeight = getScreenHeight();

        //int pixels = (int) ((getWidth() - bufferWidth*scaleX) / 2);
        int pixels = ((getWidth()-(getScreenWidth()*scaleX))>> 1);
        //System.out.println(pixels);
        //System.out.println(getWidth());
        int pixelsH = (int) ((getHeight() - bufferHeight*scaleX) / 2);

        matrixScreen = new Matrix();
        matrixScreen.setScale(fscaleX, fscaleY);
        matrixScreen.postTranslate(pixels, pixelsH);
        //}
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //System.out.println("Drawing ArcoFlex Emulator View!");
        /*BitMap bmp = MainActivity.mm.emulator.refresh(true);
        MainActivity.mm.pixels = bmp.getPixels();

        m_nPix = MainActivity.mm.pixels;
        draw();*/
        //invalidate();
        MainActivity.mm.mJoystick.invalidate();
    }

    private int NextPot(int v) {
        --v;
        v |= (v >> 1);
        v |= (v >> 2);
        v |= (v >> 4);
        v |= (v >> 8);
        v |= (v >> 16);
        return ++v;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        System.out.println("onSensorChanged: "+event.toString());
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        System.out.println("onAccuracyChanged: "+sensor.toString()+", accuracy: "+accuracy);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        System.out.println("surfaceCreated: "+holder.toString());

        /*if (updater != null)
            updater.start();*/

        initMatrix();
    }

    long t = System.currentTimeMillis();
    int frames;

    public void checkFPS() {


        if (frames % 20 == 0) {
            long t2 = System.currentTimeMillis();
            System.out.println("FPS: " + 20000 / (t2 - t));
            t = t2;
        }
        frames++;

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        System.out.println("surfaceChanged: "+holder.toString()+" format: "+format+", width: "+width+", height: "+height);
        //draw();

        /*if (updater != null) {
            updater.render();
        } else {*/
        checkSettings();
        checkFPS();
        //Canvas c = null;
        //try {
        //c = mSurfaceHolder.lockCanvas(null);
        //synchronized (mSurfaceHolder) {
        //drawCanvasBufferAsShort.position(0);
        //screenBitmap.copyPixelsFromBuffer(drawCanvasBufferAsShort);
                    /*if (c != null) {

                        c.drawBitmap(screenBitmap, matrixScreen, null);*/
                        /*if (((MainActivity) getContext()).vKeyPad != null)
                            ((MainActivity) getContext()).vKeyPad.draw(c);
                    }

                }
            } finally {
                // do this in a finally so that if an exception is thrown
                // during the above, we don't leave the Surface in an
                // inconsistent state
                if (c != null) {
                    mSurfaceHolder.unlockCanvasAndPost(c);
                }
            }*/
        // }
    }

    private void checkSettings() {
        try {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(_context);

            int longo = prefs.getAll().size();

            Set keys = prefs.getAll().keySet();

        /*System.out.println("jMESYS------------------------------");

        for (Iterator i = keys.iterator(); i.hasNext(); ) {
            String key = (String) i.next();
            System.out.println(key + " = " + prefs.getAll().get(key));
        }*/

            //fscaleX = fscaleY = (Float.parseFloat((String) prefs.getAll().get("zoom"))) + 1.00f;
            fscaleX = fscaleY = 4.00f;
            initMatrix();

        } catch (Exception e){
            e.printStackTrace(System.out);
        }
        //System.out.println("jMESYS ------------------------------");
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        System.out.println("surfaceDestroyed: "+holder.toString());

        /*if (updater != null)
            updater.stop();*/
    }

    public void setScreenBitmap(Bitmap bm) {
        this.screenBitmap = bm;
    }

    /**
     * Paint the emulator screen
     */
    public void draw() {
        //checkFPS();
        //System.out.println("DRAW ArcoFlexEmulatorView!");

        /*BitMap bmp = MainActivity.mm.emulator.refresh(true);
        MainActivity.mm.pixels = bmp.getPixels();

        m_nPix = MainActivity.mm.pixels;*/
        updateArcoFlexEmulatorScreen();

        //if (this.hasSurface) {
        try {

            //if (canvas != null) {
                this.mSurfaceHolder = getHolder();
                //this.holder.addCallback(this);
                this.mSurfaceHolder.addCallback(this);
            //}
            // this is where we draw to
            Canvas canvas = this.mSurfaceHolder.lockCanvas();
            cLocked=true;
            /*if (cLocked) {
                this.mSurfaceHolder.unlockCanvasAndPost(canvas);
                cLocked = false;
            }

            if (canvas==null) {
                canvas = this.mSurfaceHolder.lockCanvas();
                cLocked = true;
            }*/

            if (canvas != null){
                // set black as screen background
                Paint paint = new Paint();

                paint.setColor(0);
                paint.setAlpha(255);
                canvas.drawPaint(paint);

                // paint C64's screen border
                //final VIC6569 vic = this.c64.getVIC();

                //paint.setColor(this.borderColor.getRGB());
                //canvas.drawRect(this.borderRect, paint);

                // show C64 screen
                //final Paint paint2 = isUseAntialiasing() ? new Paint(Paint.ANTI_ALIAS_FLAG + Paint.FILTER_BITMAP_FLAG) : null;
                Paint paint2 = new Paint(Paint.ANTI_ALIAS_FLAG + Paint.FILTER_BITMAP_FLAG);

                //this.screenBitmap.setPixels(vic.getRGBData(), this.offset, vic.getBorderWidth(), 0, 0, this.screenBitmap.getWidth(), this.screenBitmap.getHeight());
                //System.out.println(canvas);
                //System.out.println(screenBitmap);
                //System.out.println(paint2);

                //canvas.drawBitmap(this.screenBitmap, (getWidth() - this.screenBitmap.getWidth()) >> 1, (getHeight() - this.screenBitmap.getHeight()) >> 1, paint2);
                //matrixScreen.postTranslate(20, 0);
                //canvas.drawBitmap(this.screenBitmap, matrixScreen, paint2);

                /*(new Thread
                        (new Runnable()
                        {
                            public void run() {
                                resizeEmulatorScreen();
                            }
                        }
                        )).start();*/

                canvas.drawBitmap(this.screenBitmap, 0, 0, paint2);

                // show logo if the emulator is not yet ready
                    /*if (!this.c64.isReady()) {
                        canvas.drawBitmap(this.logo, (getWidth() - this.logo.getWidth()) >> 1, (getHeight() - this.logo.getHeight()) >> 1, null);
                    }*/

                //this.holder.unlockCanvasAndPost(canvas);
                this.mSurfaceHolder.unlockCanvasAndPost(canvas);
                cLocked = false;
            }
            /*MainActivity.mm.runOnUiThread(
                    (new Thread(new Runnable() {
                        public void run() {
                            invalidate();
                        }
                    }
                    ))
            );*/
            //canvas=null;
            //super.draw(canvas);
        } catch (Exception e) {
            // probably the Gameboy instance is not yet initialized, no problem, the next painting might work
            e.printStackTrace(System.out);
        }
        //}
    }



    public int[] getColorPixels() {
        //System.out.println("getColorPixels!!!!");
        //System.out.println(m_colorPix);
        //System.out.println(m_colorPix.length);
        //if (m_colorPix == null) {

        int longo = m_nPix.length;

        m_colorPix = new int[longo];

        try {

            for (int i = 0; i < longo; i++) {
                int col = m_nPix[i];
                /*int r = ((int) m_rgbRedPalette[col] & 0xFF);
                int g = ((int) m_rgbGreenPalette[col] & 0xFF);
                int b = ((int) m_rgbBluePalette[col] & 0xFF);

                m_colorPix[i] = (255 << 24) | (r << 16) | (g << 8) | b;*/
                m_colorPix[i] = (255 << 24) | col;
                //System.out.println(m_colorPix[i]);
            }
            //}
        } catch (Exception e) {

        }

        //System.out.println("Color Pixels ENDs");

        return m_colorPix;
    }

    public int[] getPixels(){ return m_nPix; }

    public void updateArcoFlexEmulatorScreen(){
        //if ((MainActivity.mm != null) && (MainActivity.mm.emulator != null)) {
        if ((screen != null) && (screen._pixels != null) && (Machine != null) && (Machine.scrbitmap != null)) {
            //System.out.println("showTestData!!!!");
            //BitMap bmp = MainActivity.mm.emulator.refresh(true);

            try {
                m_nPix = screen._pixels;
                width = (int) settings.current_platform_configuration.get_video_class().getWidth();
                height = (int) settings.current_platform_configuration.get_video_class().getHeight();
            } catch (Exception e) {
                e.printStackTrace(System.out);
            }

            //width = bmp.getWidth();
            //height = bmp.getHeight();

            //m_nPix = MainActivity.mm.emulator.refresh(true).getPixels();
        }
    }


}

