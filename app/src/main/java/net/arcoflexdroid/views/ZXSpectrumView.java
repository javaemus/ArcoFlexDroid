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
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import net.arcoflexdroid.ArcoFlexDroid;

import java.nio.ShortBuffer;
import java.util.Iterator;
import java.util.Set;


/**
 * Created by jagsanchez on 24/11/2017.
 */

public class ZXSpectrumView extends SurfaceView implements SensorEventListener, SurfaceHolder.Callback , IEmuView{

    /**
     * SurfaceHolder where we paint
     */
    //private final SurfaceHolder holder;
    public boolean showTestData = true;

    protected ArcoFlexDroid mm = null;

    //ScreenUpdater updater;
    public SurfaceHolder mSurfaceHolder;
    public static ShortBuffer drawCanvasBufferAsShort;
    public Matrix matrixScreen=null;

    private int	m_nPix[] = null;
    private int	m_colorPix[] = null;

    private int width = 272;
    private int height = 208;

    private float fscaleX = 4.0f;
    private float fscaleY = 4.0f;

    public static Bitmap screenBitmap;

    private Context _context;

    protected void init(Context c, AttributeSet attrib){
        System.out.println("ZX INIT!!!!");

    }

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

    public ZXSpectrumView(Context context) {
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
        m_nPix = new int[numPix];

        for (int i=0 ; i<numPix ; i++){
            m_nPix[i]=0;
        }

        this.showTestData();
        getColorPixels();

        // get the emulator logo
        //this.logo = BitmapFactory.decodeResource(getResources(), R.drawable.a64);

        initZXSpectrumUI();
    }

    private void initZXSpectrumUI() {

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
        System.out.println("Pinto ZX Spectrum View!");
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
        draw();

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

            fscaleX = fscaleY = (Float.parseFloat((String) prefs.getAll().get("zoom"))) + 1.00f;
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
     * Paint the C64 screen
     */
    public void draw() {
        //checkFPS();

        //System.out.println("DRAW ZXSpectrumView!");
        //if (this.hasSurface) {
        try {
            // this is where we draw to
            //Canvas canvas = this.holder.lockCanvas();
            Canvas canvas = this.mSurfaceHolder.lockCanvas();
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
            canvas.drawBitmap(this.screenBitmap, matrixScreen, paint2);

            // show logo if the emulator is not yet ready
                /*if (!this.c64.isReady()) {
                    canvas.drawBitmap(this.logo, (getWidth() - this.logo.getWidth()) >> 1, (getHeight() - this.logo.getHeight()) >> 1, null);
                }*/

            //this.holder.unlockCanvasAndPost(canvas);
            this.mSurfaceHolder.unlockCanvasAndPost(canvas);
            invalidate();
            super.draw(canvas);
        } catch (Exception e) {
            // probably the Gameboy instance is not yet initialized, no problem, the next painting might work
            //e.printStackTrace(System.out);
        }
        //}
    }

    public int[] getColorPixels() {

        if (m_colorPix == null) {

            int longo = m_nPix.length;

            m_colorPix = new int[longo];

            for (int i = 0; i < longo; i++) {
                int col = m_nPix[i];
                int r = ((int) m_rgbRedPalette[col] & 0xFF);
                int g = ((int) m_rgbGreenPalette[col] & 0xFF);
                int b = ((int) m_rgbBluePalette[col] & 0xFF);

                m_colorPix[i] = (255 << 24) | (r << 16) | (g << 8) | b;

                //System.out.println(m_colorPix[i]);
            }
        }

        //System.out.println("Color Pixels ENDs");

        return m_colorPix;
    }

    public int[] getPixels(){ return m_nPix; }

    public void showTestData() {
        m_nPix[10930]=13;
        m_nPix[10931]=13;
        m_nPix[10932]=13;
        m_nPix[10933]=13;
        m_nPix[10934]=13;
        m_nPix[10935]=13;
        m_nPix[10946]=8;
        m_nPix[10947]=8;
        m_nPix[10948]=8;
        m_nPix[10949]=8;
        m_nPix[10950]=8;
        m_nPix[10951]=8;
        m_nPix[10960]=9;
        m_nPix[10961]=9;
        m_nPix[10962]=9;
        m_nPix[10963]=9;
        m_nPix[10976]=11;
        m_nPix[10977]=11;
        m_nPix[10978]=11;
        m_nPix[10979]=11;
        m_nPix[10980]=11;
        m_nPix[10981]=11;
        m_nPix[10982]=11;
        m_nPix[10983]=11;
        m_nPix[10984]=11;
        m_nPix[10985]=11;
        m_nPix[10994]=3;
        m_nPix[10995]=3;
        m_nPix[10996]=3;
        m_nPix[10997]=3;
        m_nPix[10998]=3;
        m_nPix[10999]=3;
        m_nPix[11010]=4;
        m_nPix[11011]=4;
        m_nPix[11012]=4;
        m_nPix[11013]=4;
        m_nPix[11014]=4;
        m_nPix[11015]=4;
        m_nPix[11024]=13;
        m_nPix[11025]=13;
        m_nPix[11026]=13;
        m_nPix[11027]=13;
        m_nPix[11031]=13;
        m_nPix[11032]=13;
        m_nPix[11033]=13;
        m_nPix[11034]=13;
        m_nPix[11040]=8;
        m_nPix[11041]=8;
        m_nPix[11042]=8;
        m_nPix[11043]=8;
        m_nPix[11051]=9;
        m_nPix[11052]=9;
        m_nPix[11053]=9;
        m_nPix[11054]=9;
        m_nPix[11055]=9;
        m_nPix[11056]=9;
        m_nPix[11064]=11;
        m_nPix[11065]=11;
        m_nPix[11066]=11;
        m_nPix[11067]=11;
        m_nPix[11074]=3;
        m_nPix[11075]=3;
        m_nPix[11076]=3;
        m_nPix[11077]=3;
        m_nPix[11078]=3;
        m_nPix[11079]=3;
        m_nPix[11088]=4;
        m_nPix[11089]=4;
        m_nPix[11090]=4;
        m_nPix[11091]=4;
        m_nPix[11094]=4;
        m_nPix[11095]=4;
        m_nPix[11096]=4;
        m_nPix[11097]=4;
        m_nPix[11107]=15;
        m_nPix[11108]=15;
        m_nPix[11109]=15;
        m_nPix[11110]=15;
        m_nPix[11111]=15;
        m_nPix[11113]=15;
        m_nPix[11117]=15;
        m_nPix[11201]=13;
        m_nPix[11202]=13;
        m_nPix[11203]=13;
        m_nPix[11204]=13;
        m_nPix[11205]=13;
        m_nPix[11206]=13;
        m_nPix[11207]=13;
        m_nPix[11208]=13;
        m_nPix[11217]=8;
        m_nPix[11218]=8;
        m_nPix[11219]=8;
        m_nPix[11220]=8;
        m_nPix[11221]=8;
        m_nPix[11222]=8;
        m_nPix[11223]=8;
        m_nPix[11224]=8;
        m_nPix[11232]=9;
        m_nPix[11233]=9;
        m_nPix[11234]=9;
        m_nPix[11235]=9;
        m_nPix[11248]=11;
        m_nPix[11249]=11;
        m_nPix[11250]=11;
        m_nPix[11251]=11;
        m_nPix[11252]=11;
        m_nPix[11253]=11;
        m_nPix[11254]=11;
        m_nPix[11255]=11;
        m_nPix[11256]=11;
        m_nPix[11257]=11;
        m_nPix[11265]=3;
        m_nPix[11266]=3;
        m_nPix[11267]=3;
        m_nPix[11268]=3;
        m_nPix[11269]=3;
        m_nPix[11270]=3;
        m_nPix[11271]=3;
        m_nPix[11272]=3;
        m_nPix[11281]=4;
        m_nPix[11282]=4;
        m_nPix[11283]=4;
        m_nPix[11284]=4;
        m_nPix[11285]=4;
        m_nPix[11286]=4;
        m_nPix[11287]=4;
        m_nPix[11288]=4;
        m_nPix[11296]=13;
        m_nPix[11297]=13;
        m_nPix[11298]=13;
        m_nPix[11299]=13;
        m_nPix[11303]=13;
        m_nPix[11304]=13;
        m_nPix[11305]=13;
        m_nPix[11306]=13;
        m_nPix[11312]=8;
        m_nPix[11313]=8;
        m_nPix[11314]=8;
        m_nPix[11315]=8;
        m_nPix[11322]=9;
        m_nPix[11323]=9;
        m_nPix[11324]=9;
        m_nPix[11325]=9;
        m_nPix[11326]=9;
        m_nPix[11327]=9;
        m_nPix[11328]=9;
        m_nPix[11329]=9;
        m_nPix[11336]=11;
        m_nPix[11337]=11;
        m_nPix[11338]=11;
        m_nPix[11339]=11;
        m_nPix[11345]=3;
        m_nPix[11346]=3;
        m_nPix[11347]=3;
        m_nPix[11348]=3;
        m_nPix[11349]=3;
        m_nPix[11350]=3;
        m_nPix[11351]=3;
        m_nPix[11352]=3;
        m_nPix[11360]=4;
        m_nPix[11361]=4;
        m_nPix[11362]=4;
        m_nPix[11363]=4;
        m_nPix[11366]=4;
        m_nPix[11367]=4;
        m_nPix[11368]=4;
        m_nPix[11369]=4;
        m_nPix[11381]=15;
        m_nPix[11385]=15;
        m_nPix[11386]=15;
        m_nPix[11388]=15;
        m_nPix[11389]=15;
        m_nPix[11472]=13;
        m_nPix[11473]=13;
        m_nPix[11474]=13;
        m_nPix[11475]=13;
        m_nPix[11476]=13;
        m_nPix[11477]=13;
        m_nPix[11478]=13;
        m_nPix[11479]=13;
        m_nPix[11480]=13;
        m_nPix[11481]=13;
        m_nPix[11488]=8;
        m_nPix[11489]=8;
        m_nPix[11490]=8;
        m_nPix[11491]=8;
        m_nPix[11492]=8;
        m_nPix[11493]=8;
        m_nPix[11494]=8;
        m_nPix[11495]=8;
        m_nPix[11496]=8;
        m_nPix[11497]=8;
        m_nPix[11504]=9;
        m_nPix[11505]=9;
        m_nPix[11506]=9;
        m_nPix[11507]=9;
        m_nPix[11520]=11;
        m_nPix[11521]=11;
        m_nPix[11522]=11;
        m_nPix[11523]=11;
        m_nPix[11524]=11;
        m_nPix[11525]=11;
        m_nPix[11526]=11;
        m_nPix[11527]=11;
        m_nPix[11528]=11;
        m_nPix[11529]=11;
        m_nPix[11536]=3;
        m_nPix[11537]=3;
        m_nPix[11538]=3;
        m_nPix[11539]=3;
        m_nPix[11540]=3;
        m_nPix[11541]=3;
        m_nPix[11542]=3;
        m_nPix[11543]=3;
        m_nPix[11544]=3;
        m_nPix[11545]=3;
        m_nPix[11552]=4;
        m_nPix[11553]=4;
        m_nPix[11554]=4;
        m_nPix[11555]=4;
        m_nPix[11556]=4;
        m_nPix[11557]=4;
        m_nPix[11558]=4;
        m_nPix[11559]=4;
        m_nPix[11560]=4;
        m_nPix[11561]=4;
        m_nPix[11568]=13;
        m_nPix[11569]=13;
        m_nPix[11570]=13;
        m_nPix[11571]=13;
        m_nPix[11575]=13;
        m_nPix[11576]=13;
        m_nPix[11577]=13;
        m_nPix[11578]=13;
        m_nPix[11584]=8;
        m_nPix[11585]=8;
        m_nPix[11586]=8;
        m_nPix[11587]=8;
        m_nPix[11593]=9;
        m_nPix[11594]=9;
        m_nPix[11595]=9;
        m_nPix[11596]=9;
        m_nPix[11597]=9;
        m_nPix[11598]=9;
        m_nPix[11599]=9;
        m_nPix[11600]=9;
        m_nPix[11601]=9;
        m_nPix[11602]=9;
        m_nPix[11608]=11;
        m_nPix[11609]=11;
        m_nPix[11610]=11;
        m_nPix[11611]=11;
        m_nPix[11616]=3;
        m_nPix[11617]=3;
        m_nPix[11618]=3;
        m_nPix[11619]=3;
        m_nPix[11620]=3;
        m_nPix[11621]=3;
        m_nPix[11622]=3;
        m_nPix[11623]=3;
        m_nPix[11624]=3;
        m_nPix[11625]=3;
        m_nPix[11632]=4;
        m_nPix[11633]=4;
        m_nPix[11634]=4;
        m_nPix[11635]=4;
        m_nPix[11636]=4;
        m_nPix[11638]=4;
        m_nPix[11639]=4;
        m_nPix[11640]=4;
        m_nPix[11641]=4;
        m_nPix[11653]=15;
        m_nPix[11657]=15;
        m_nPix[11659]=15;
        m_nPix[11661]=15;
        m_nPix[11744]=13;
        m_nPix[11745]=13;
        m_nPix[11746]=13;
        m_nPix[11747]=13;
        m_nPix[11748]=13;
        m_nPix[11749]=13;
        m_nPix[11750]=13;
        m_nPix[11751]=13;
        m_nPix[11752]=13;
        m_nPix[11753]=13;
        m_nPix[11760]=8;
        m_nPix[11761]=8;
        m_nPix[11762]=8;
        m_nPix[11763]=8;
        m_nPix[11764]=8;
        m_nPix[11765]=8;
        m_nPix[11766]=8;
        m_nPix[11767]=8;
        m_nPix[11768]=8;
        m_nPix[11769]=8;
        m_nPix[11776]=9;
        m_nPix[11777]=9;
        m_nPix[11778]=9;
        m_nPix[11779]=9;
        m_nPix[11792]=11;
        m_nPix[11793]=11;
        m_nPix[11794]=11;
        m_nPix[11795]=11;
        m_nPix[11808]=3;
        m_nPix[11809]=3;
        m_nPix[11810]=3;
        m_nPix[11811]=3;
        m_nPix[11812]=3;
        m_nPix[11813]=3;
        m_nPix[11814]=3;
        m_nPix[11815]=3;
        m_nPix[11816]=3;
        m_nPix[11817]=3;
        m_nPix[11824]=4;
        m_nPix[11825]=4;
        m_nPix[11826]=4;
        m_nPix[11827]=4;
        m_nPix[11828]=4;
        m_nPix[11829]=4;
        m_nPix[11830]=4;
        m_nPix[11831]=4;
        m_nPix[11832]=4;
        m_nPix[11833]=4;
        m_nPix[11841]=13;
        m_nPix[11842]=13;
        m_nPix[11843]=13;
        m_nPix[11844]=13;
        m_nPix[11846]=13;
        m_nPix[11847]=13;
        m_nPix[11848]=13;
        m_nPix[11849]=13;
        m_nPix[11856]=8;
        m_nPix[11857]=8;
        m_nPix[11858]=8;
        m_nPix[11859]=8;
        m_nPix[11865]=9;
        m_nPix[11866]=9;
        m_nPix[11867]=9;
        m_nPix[11868]=9;
        m_nPix[11871]=9;
        m_nPix[11872]=9;
        m_nPix[11873]=9;
        m_nPix[11874]=9;
        m_nPix[11880]=11;
        m_nPix[11881]=11;
        m_nPix[11882]=11;
        m_nPix[11883]=11;
        m_nPix[11888]=3;
        m_nPix[11889]=3;
        m_nPix[11890]=3;
        m_nPix[11891]=3;
        m_nPix[11892]=3;
        m_nPix[11893]=3;
        m_nPix[11894]=3;
        m_nPix[11895]=3;
        m_nPix[11896]=3;
        m_nPix[11897]=3;
        m_nPix[11904]=4;
        m_nPix[11905]=4;
        m_nPix[11906]=4;
        m_nPix[11907]=4;
        m_nPix[11908]=4;
        m_nPix[11910]=4;
        m_nPix[11911]=4;
        m_nPix[11912]=4;
        m_nPix[11913]=4;
        m_nPix[11925]=15;
        m_nPix[11929]=15;
        m_nPix[11931]=15;
        m_nPix[11933]=15;
        m_nPix[12016]=13;
        m_nPix[12017]=13;
        m_nPix[12018]=13;
        m_nPix[12019]=13;
        m_nPix[12022]=13;
        m_nPix[12023]=13;
        m_nPix[12024]=13;
        m_nPix[12025]=13;
        m_nPix[12032]=8;
        m_nPix[12033]=8;
        m_nPix[12034]=8;
        m_nPix[12035]=8;
        m_nPix[12038]=8;
        m_nPix[12039]=8;
        m_nPix[12040]=8;
        m_nPix[12041]=8;
        m_nPix[12048]=9;
        m_nPix[12049]=9;
        m_nPix[12050]=9;
        m_nPix[12051]=9;
        m_nPix[12064]=11;
        m_nPix[12065]=11;
        m_nPix[12066]=11;
        m_nPix[12067]=11;
        m_nPix[12080]=3;
        m_nPix[12081]=3;
        m_nPix[12082]=3;
        m_nPix[12083]=3;
        m_nPix[12086]=3;
        m_nPix[12087]=3;
        m_nPix[12088]=3;
        m_nPix[12089]=3;
        m_nPix[12096]=4;
        m_nPix[12097]=4;
        m_nPix[12098]=4;
        m_nPix[12099]=4;
        m_nPix[12102]=4;
        m_nPix[12103]=4;
        m_nPix[12104]=4;
        m_nPix[12105]=4;
        m_nPix[12113]=13;
        m_nPix[12114]=13;
        m_nPix[12115]=13;
        m_nPix[12116]=13;
        m_nPix[12118]=13;
        m_nPix[12119]=13;
        m_nPix[12120]=13;
        m_nPix[12121]=13;
        m_nPix[12128]=8;
        m_nPix[12129]=8;
        m_nPix[12130]=8;
        m_nPix[12131]=8;
        m_nPix[12137]=9;
        m_nPix[12138]=9;
        m_nPix[12139]=9;
        m_nPix[12140]=9;
        m_nPix[12152]=11;
        m_nPix[12153]=11;
        m_nPix[12154]=11;
        m_nPix[12155]=11;
        m_nPix[12160]=3;
        m_nPix[12161]=3;
        m_nPix[12162]=3;
        m_nPix[12163]=3;
        m_nPix[12166]=3;
        m_nPix[12167]=3;
        m_nPix[12168]=3;
        m_nPix[12169]=3;
        m_nPix[12176]=4;
        m_nPix[12177]=4;
        m_nPix[12178]=4;
        m_nPix[12179]=4;
        m_nPix[12180]=4;
        m_nPix[12182]=4;
        m_nPix[12183]=4;
        m_nPix[12184]=4;
        m_nPix[12185]=4;
        m_nPix[12288]=13;
        m_nPix[12289]=13;
        m_nPix[12290]=13;
        m_nPix[12291]=13;
        m_nPix[12294]=13;
        m_nPix[12295]=13;
        m_nPix[12296]=13;
        m_nPix[12297]=13;
        m_nPix[12304]=8;
        m_nPix[12305]=8;
        m_nPix[12306]=8;
        m_nPix[12307]=8;
        m_nPix[12310]=8;
        m_nPix[12311]=8;
        m_nPix[12312]=8;
        m_nPix[12313]=8;
        m_nPix[12320]=9;
        m_nPix[12321]=9;
        m_nPix[12322]=9;
        m_nPix[12323]=9;
        m_nPix[12336]=11;
        m_nPix[12337]=11;
        m_nPix[12338]=11;
        m_nPix[12339]=11;
        m_nPix[12340]=11;
        m_nPix[12341]=11;
        m_nPix[12342]=11;
        m_nPix[12343]=11;
        m_nPix[12352]=3;
        m_nPix[12353]=3;
        m_nPix[12354]=3;
        m_nPix[12355]=3;
        m_nPix[12358]=3;
        m_nPix[12359]=3;
        m_nPix[12360]=3;
        m_nPix[12361]=3;
        m_nPix[12368]=4;
        m_nPix[12369]=4;
        m_nPix[12370]=4;
        m_nPix[12371]=4;
        m_nPix[12374]=4;
        m_nPix[12375]=4;
        m_nPix[12376]=4;
        m_nPix[12377]=4;
        m_nPix[12385]=13;
        m_nPix[12386]=13;
        m_nPix[12387]=13;
        m_nPix[12388]=13;
        m_nPix[12390]=13;
        m_nPix[12391]=13;
        m_nPix[12392]=13;
        m_nPix[12393]=13;
        m_nPix[12400]=8;
        m_nPix[12401]=8;
        m_nPix[12402]=8;
        m_nPix[12403]=8;
        m_nPix[12409]=9;
        m_nPix[12410]=9;
        m_nPix[12411]=9;
        m_nPix[12412]=9;
        m_nPix[12413]=9;
        m_nPix[12414]=9;
        m_nPix[12415]=9;
        m_nPix[12416]=9;
        m_nPix[12424]=11;
        m_nPix[12425]=11;
        m_nPix[12426]=11;
        m_nPix[12427]=11;
        m_nPix[12432]=3;
        m_nPix[12433]=3;
        m_nPix[12434]=3;
        m_nPix[12435]=3;
        m_nPix[12438]=3;
        m_nPix[12439]=3;
        m_nPix[12440]=3;
        m_nPix[12441]=3;
        m_nPix[12448]=4;
        m_nPix[12449]=4;
        m_nPix[12450]=4;
        m_nPix[12451]=4;
        m_nPix[12452]=4;
        m_nPix[12453]=4;
        m_nPix[12454]=4;
        m_nPix[12455]=4;
        m_nPix[12456]=4;
        m_nPix[12457]=4;
        m_nPix[12560]=13;
        m_nPix[12561]=13;
        m_nPix[12562]=13;
        m_nPix[12563]=13;
        m_nPix[12576]=8;
        m_nPix[12577]=8;
        m_nPix[12578]=8;
        m_nPix[12579]=8;
        m_nPix[12582]=8;
        m_nPix[12583]=8;
        m_nPix[12584]=8;
        m_nPix[12585]=8;
        m_nPix[12592]=9;
        m_nPix[12593]=9;
        m_nPix[12594]=9;
        m_nPix[12595]=9;
        m_nPix[12608]=11;
        m_nPix[12609]=11;
        m_nPix[12610]=11;
        m_nPix[12611]=11;
        m_nPix[12612]=11;
        m_nPix[12613]=11;
        m_nPix[12614]=11;
        m_nPix[12615]=11;
        m_nPix[12624]=3;
        m_nPix[12625]=3;
        m_nPix[12626]=3;
        m_nPix[12627]=3;
        m_nPix[12640]=4;
        m_nPix[12641]=4;
        m_nPix[12642]=4;
        m_nPix[12643]=4;
        m_nPix[12646]=4;
        m_nPix[12647]=4;
        m_nPix[12648]=4;
        m_nPix[12649]=4;
        m_nPix[12658]=13;
        m_nPix[12659]=13;
        m_nPix[12660]=13;
        m_nPix[12661]=13;
        m_nPix[12662]=13;
        m_nPix[12663]=13;
        m_nPix[12664]=13;
        m_nPix[12672]=8;
        m_nPix[12673]=8;
        m_nPix[12674]=8;
        m_nPix[12675]=8;
        m_nPix[12681]=9;
        m_nPix[12682]=9;
        m_nPix[12683]=9;
        m_nPix[12684]=9;
        m_nPix[12685]=9;
        m_nPix[12686]=9;
        m_nPix[12687]=9;
        m_nPix[12688]=9;
        m_nPix[12689]=9;
        m_nPix[12696]=11;
        m_nPix[12697]=11;
        m_nPix[12698]=11;
        m_nPix[12699]=11;
        m_nPix[12704]=3;
        m_nPix[12705]=3;
        m_nPix[12706]=3;
        m_nPix[12707]=3;
        m_nPix[12710]=3;
        m_nPix[12711]=3;
        m_nPix[12712]=3;
        m_nPix[12713]=3;
        m_nPix[12720]=4;
        m_nPix[12721]=4;
        m_nPix[12722]=4;
        m_nPix[12723]=4;
        m_nPix[12724]=4;
        m_nPix[12725]=4;
        m_nPix[12726]=4;
        m_nPix[12727]=4;
        m_nPix[12728]=4;
        m_nPix[12729]=4;
        m_nPix[12832]=13;
        m_nPix[12833]=13;
        m_nPix[12834]=13;
        m_nPix[12835]=13;
        m_nPix[12848]=8;
        m_nPix[12849]=8;
        m_nPix[12850]=8;
        m_nPix[12851]=8;
        m_nPix[12854]=8;
        m_nPix[12855]=8;
        m_nPix[12856]=8;
        m_nPix[12857]=8;
        m_nPix[12864]=9;
        m_nPix[12865]=9;
        m_nPix[12866]=9;
        m_nPix[12867]=9;
        m_nPix[12880]=11;
        m_nPix[12881]=11;
        m_nPix[12882]=11;
        m_nPix[12883]=11;
        m_nPix[12884]=11;
        m_nPix[12885]=11;
        m_nPix[12886]=11;
        m_nPix[12887]=11;
        m_nPix[12896]=3;
        m_nPix[12897]=3;
        m_nPix[12898]=3;
        m_nPix[12899]=3;
        m_nPix[12912]=4;
        m_nPix[12913]=4;
        m_nPix[12914]=4;
        m_nPix[12915]=4;
        m_nPix[12918]=4;
        m_nPix[12919]=4;
        m_nPix[12920]=4;
        m_nPix[12921]=4;
        m_nPix[12930]=13;
        m_nPix[12931]=13;
        m_nPix[12932]=13;
        m_nPix[12933]=13;
        m_nPix[12934]=13;
        m_nPix[12935]=13;
        m_nPix[12936]=13;
        m_nPix[12944]=8;
        m_nPix[12945]=8;
        m_nPix[12946]=8;
        m_nPix[12947]=8;
        m_nPix[12954]=9;
        m_nPix[12955]=9;
        m_nPix[12956]=9;
        m_nPix[12957]=9;
        m_nPix[12958]=9;
        m_nPix[12959]=9;
        m_nPix[12960]=9;
        m_nPix[12961]=9;
        m_nPix[12962]=9;
        m_nPix[12968]=11;
        m_nPix[12969]=11;
        m_nPix[12970]=11;
        m_nPix[12971]=11;
        m_nPix[12976]=3;
        m_nPix[12977]=3;
        m_nPix[12978]=3;
        m_nPix[12979]=3;
        m_nPix[12982]=3;
        m_nPix[12983]=3;
        m_nPix[12984]=3;
        m_nPix[12985]=3;
        m_nPix[12992]=4;
        m_nPix[12993]=4;
        m_nPix[12994]=4;
        m_nPix[12995]=4;
        m_nPix[12996]=4;
        m_nPix[12997]=4;
        m_nPix[12998]=4;
        m_nPix[12999]=4;
        m_nPix[13000]=4;
        m_nPix[13001]=4;
        m_nPix[13104]=13;
        m_nPix[13105]=13;
        m_nPix[13106]=13;
        m_nPix[13107]=13;
        m_nPix[13110]=13;
        m_nPix[13111]=13;
        m_nPix[13112]=13;
        m_nPix[13113]=13;
        m_nPix[13120]=8;
        m_nPix[13121]=8;
        m_nPix[13122]=8;
        m_nPix[13123]=8;
        m_nPix[13126]=8;
        m_nPix[13127]=8;
        m_nPix[13128]=8;
        m_nPix[13129]=8;
        m_nPix[13136]=9;
        m_nPix[13137]=9;
        m_nPix[13138]=9;
        m_nPix[13139]=9;
        m_nPix[13152]=11;
        m_nPix[13153]=11;
        m_nPix[13154]=11;
        m_nPix[13155]=11;
        m_nPix[13168]=3;
        m_nPix[13169]=3;
        m_nPix[13170]=3;
        m_nPix[13171]=3;
        m_nPix[13174]=3;
        m_nPix[13175]=3;
        m_nPix[13176]=3;
        m_nPix[13177]=3;
        m_nPix[13184]=4;
        m_nPix[13185]=4;
        m_nPix[13186]=4;
        m_nPix[13187]=4;
        m_nPix[13190]=4;
        m_nPix[13191]=4;
        m_nPix[13192]=4;
        m_nPix[13193]=4;
        m_nPix[13202]=13;
        m_nPix[13203]=13;
        m_nPix[13204]=13;
        m_nPix[13205]=13;
        m_nPix[13206]=13;
        m_nPix[13207]=13;
        m_nPix[13208]=13;
        m_nPix[13216]=8;
        m_nPix[13217]=8;
        m_nPix[13218]=8;
        m_nPix[13219]=8;
        m_nPix[13227]=9;
        m_nPix[13228]=9;
        m_nPix[13229]=9;
        m_nPix[13230]=9;
        m_nPix[13231]=9;
        m_nPix[13232]=9;
        m_nPix[13233]=9;
        m_nPix[13234]=9;
        m_nPix[13240]=11;
        m_nPix[13241]=11;
        m_nPix[13242]=11;
        m_nPix[13243]=11;
        m_nPix[13248]=3;
        m_nPix[13249]=3;
        m_nPix[13250]=3;
        m_nPix[13251]=3;
        m_nPix[13254]=3;
        m_nPix[13255]=3;
        m_nPix[13256]=3;
        m_nPix[13257]=3;
        m_nPix[13264]=4;
        m_nPix[13265]=4;
        m_nPix[13266]=4;
        m_nPix[13267]=4;
        m_nPix[13268]=4;
        m_nPix[13269]=4;
        m_nPix[13270]=4;
        m_nPix[13271]=4;
        m_nPix[13272]=4;
        m_nPix[13273]=4;
        m_nPix[13376]=13;
        m_nPix[13377]=13;
        m_nPix[13378]=13;
        m_nPix[13379]=13;
        m_nPix[13382]=13;
        m_nPix[13383]=13;
        m_nPix[13384]=13;
        m_nPix[13385]=13;
        m_nPix[13392]=8;
        m_nPix[13393]=8;
        m_nPix[13394]=8;
        m_nPix[13395]=8;
        m_nPix[13398]=8;
        m_nPix[13399]=8;
        m_nPix[13400]=8;
        m_nPix[13401]=8;
        m_nPix[13408]=9;
        m_nPix[13409]=9;
        m_nPix[13410]=9;
        m_nPix[13411]=9;
        m_nPix[13424]=11;
        m_nPix[13425]=11;
        m_nPix[13426]=11;
        m_nPix[13427]=11;
        m_nPix[13440]=3;
        m_nPix[13441]=3;
        m_nPix[13442]=3;
        m_nPix[13443]=3;
        m_nPix[13446]=3;
        m_nPix[13447]=3;
        m_nPix[13448]=3;
        m_nPix[13449]=3;
        m_nPix[13456]=4;
        m_nPix[13457]=4;
        m_nPix[13458]=4;
        m_nPix[13459]=4;
        m_nPix[13462]=4;
        m_nPix[13463]=4;
        m_nPix[13464]=4;
        m_nPix[13465]=4;
        m_nPix[13475]=13;
        m_nPix[13476]=13;
        m_nPix[13477]=13;
        m_nPix[13478]=13;
        m_nPix[13479]=13;
        m_nPix[13488]=8;
        m_nPix[13489]=8;
        m_nPix[13490]=8;
        m_nPix[13491]=8;
        m_nPix[13503]=9;
        m_nPix[13504]=9;
        m_nPix[13505]=9;
        m_nPix[13506]=9;
        m_nPix[13512]=11;
        m_nPix[13513]=11;
        m_nPix[13514]=11;
        m_nPix[13515]=11;
        m_nPix[13520]=3;
        m_nPix[13521]=3;
        m_nPix[13522]=3;
        m_nPix[13523]=3;
        m_nPix[13526]=3;
        m_nPix[13527]=3;
        m_nPix[13528]=3;
        m_nPix[13529]=3;
        m_nPix[13536]=4;
        m_nPix[13537]=4;
        m_nPix[13538]=4;
        m_nPix[13539]=4;
        m_nPix[13541]=4;
        m_nPix[13542]=4;
        m_nPix[13543]=4;
        m_nPix[13544]=4;
        m_nPix[13545]=4;
        m_nPix[13648]=13;
        m_nPix[13649]=13;
        m_nPix[13650]=13;
        m_nPix[13651]=13;
        m_nPix[13652]=13;
        m_nPix[13653]=13;
        m_nPix[13654]=13;
        m_nPix[13655]=13;
        m_nPix[13656]=13;
        m_nPix[13657]=13;
        m_nPix[13664]=8;
        m_nPix[13665]=8;
        m_nPix[13666]=8;
        m_nPix[13667]=8;
        m_nPix[13668]=8;
        m_nPix[13669]=8;
        m_nPix[13670]=8;
        m_nPix[13671]=8;
        m_nPix[13672]=8;
        m_nPix[13673]=8;
        m_nPix[13680]=9;
        m_nPix[13681]=9;
        m_nPix[13682]=9;
        m_nPix[13683]=9;
        m_nPix[13684]=9;
        m_nPix[13685]=9;
        m_nPix[13686]=9;
        m_nPix[13687]=9;
        m_nPix[13688]=9;
        m_nPix[13689]=9;
        m_nPix[13696]=11;
        m_nPix[13697]=11;
        m_nPix[13698]=11;
        m_nPix[13699]=11;
        m_nPix[13700]=11;
        m_nPix[13701]=11;
        m_nPix[13702]=11;
        m_nPix[13703]=11;
        m_nPix[13704]=11;
        m_nPix[13705]=11;
        m_nPix[13712]=3;
        m_nPix[13713]=3;
        m_nPix[13714]=3;
        m_nPix[13715]=3;
        m_nPix[13716]=3;
        m_nPix[13717]=3;
        m_nPix[13718]=3;
        m_nPix[13719]=3;
        m_nPix[13720]=3;
        m_nPix[13721]=3;
        m_nPix[13728]=4;
        m_nPix[13729]=4;
        m_nPix[13730]=4;
        m_nPix[13731]=4;
        m_nPix[13732]=4;
        m_nPix[13733]=4;
        m_nPix[13734]=4;
        m_nPix[13735]=4;
        m_nPix[13736]=4;
        m_nPix[13737]=4;
        m_nPix[13747]=13;
        m_nPix[13748]=13;
        m_nPix[13749]=13;
        m_nPix[13750]=13;
        m_nPix[13751]=13;
        m_nPix[13760]=8;
        m_nPix[13761]=8;
        m_nPix[13762]=8;
        m_nPix[13763]=8;
        m_nPix[13769]=9;
        m_nPix[13770]=9;
        m_nPix[13771]=9;
        m_nPix[13772]=9;
        m_nPix[13775]=9;
        m_nPix[13776]=9;
        m_nPix[13777]=9;
        m_nPix[13778]=9;
        m_nPix[13784]=11;
        m_nPix[13785]=11;
        m_nPix[13786]=11;
        m_nPix[13787]=11;
        m_nPix[13792]=3;
        m_nPix[13793]=3;
        m_nPix[13794]=3;
        m_nPix[13795]=3;
        m_nPix[13796]=3;
        m_nPix[13797]=3;
        m_nPix[13798]=3;
        m_nPix[13799]=3;
        m_nPix[13800]=3;
        m_nPix[13801]=3;
        m_nPix[13808]=4;
        m_nPix[13809]=4;
        m_nPix[13810]=4;
        m_nPix[13811]=4;
        m_nPix[13813]=4;
        m_nPix[13814]=4;
        m_nPix[13815]=4;
        m_nPix[13816]=4;
        m_nPix[13817]=4;
        m_nPix[13920]=13;
        m_nPix[13921]=13;
        m_nPix[13922]=13;
        m_nPix[13923]=13;
        m_nPix[13924]=13;
        m_nPix[13925]=13;
        m_nPix[13926]=13;
        m_nPix[13927]=13;
        m_nPix[13928]=13;
        m_nPix[13929]=13;
        m_nPix[13936]=8;
        m_nPix[13937]=8;
        m_nPix[13938]=8;
        m_nPix[13939]=8;
        m_nPix[13940]=8;
        m_nPix[13941]=8;
        m_nPix[13942]=8;
        m_nPix[13943]=8;
        m_nPix[13944]=8;
        m_nPix[13945]=8;
        m_nPix[13952]=9;
        m_nPix[13953]=9;
        m_nPix[13954]=9;
        m_nPix[13955]=9;
        m_nPix[13956]=9;
        m_nPix[13957]=9;
        m_nPix[13958]=9;
        m_nPix[13959]=9;
        m_nPix[13960]=9;
        m_nPix[13961]=9;
        m_nPix[13968]=11;
        m_nPix[13969]=11;
        m_nPix[13970]=11;
        m_nPix[13971]=11;
        m_nPix[13972]=11;
        m_nPix[13973]=11;
        m_nPix[13974]=11;
        m_nPix[13975]=11;
        m_nPix[13976]=11;
        m_nPix[13977]=11;
        m_nPix[13984]=3;
        m_nPix[13985]=3;
        m_nPix[13986]=3;
        m_nPix[13987]=3;
        m_nPix[13988]=3;
        m_nPix[13989]=3;
        m_nPix[13990]=3;
        m_nPix[13991]=3;
        m_nPix[13992]=3;
        m_nPix[13993]=3;
        m_nPix[14000]=4;
        m_nPix[14001]=4;
        m_nPix[14002]=4;
        m_nPix[14003]=4;
        m_nPix[14004]=4;
        m_nPix[14005]=4;
        m_nPix[14006]=4;
        m_nPix[14007]=4;
        m_nPix[14008]=4;
        m_nPix[14009]=4;
        m_nPix[14019]=13;
        m_nPix[14020]=13;
        m_nPix[14021]=13;
        m_nPix[14022]=13;
        m_nPix[14023]=13;
        m_nPix[14032]=8;
        m_nPix[14033]=8;
        m_nPix[14034]=8;
        m_nPix[14035]=8;
        m_nPix[14041]=9;
        m_nPix[14042]=9;
        m_nPix[14043]=9;
        m_nPix[14044]=9;
        m_nPix[14045]=9;
        m_nPix[14046]=9;
        m_nPix[14047]=9;
        m_nPix[14048]=9;
        m_nPix[14049]=9;
        m_nPix[14050]=9;
        m_nPix[14056]=11;
        m_nPix[14057]=11;
        m_nPix[14058]=11;
        m_nPix[14059]=11;
        m_nPix[14064]=3;
        m_nPix[14065]=3;
        m_nPix[14066]=3;
        m_nPix[14067]=3;
        m_nPix[14068]=3;
        m_nPix[14069]=3;
        m_nPix[14070]=3;
        m_nPix[14071]=3;
        m_nPix[14072]=3;
        m_nPix[14073]=3;
        m_nPix[14080]=4;
        m_nPix[14081]=4;
        m_nPix[14082]=4;
        m_nPix[14083]=4;
        m_nPix[14085]=4;
        m_nPix[14086]=4;
        m_nPix[14087]=4;
        m_nPix[14088]=4;
        m_nPix[14089]=4;
        m_nPix[14193]=13;
        m_nPix[14194]=13;
        m_nPix[14195]=13;
        m_nPix[14196]=13;
        m_nPix[14197]=13;
        m_nPix[14198]=13;
        m_nPix[14199]=13;
        m_nPix[14200]=13;
        m_nPix[14209]=8;
        m_nPix[14210]=8;
        m_nPix[14211]=8;
        m_nPix[14212]=8;
        m_nPix[14213]=8;
        m_nPix[14214]=8;
        m_nPix[14215]=8;
        m_nPix[14216]=8;
        m_nPix[14224]=9;
        m_nPix[14225]=9;
        m_nPix[14226]=9;
        m_nPix[14227]=9;
        m_nPix[14228]=9;
        m_nPix[14229]=9;
        m_nPix[14230]=9;
        m_nPix[14231]=9;
        m_nPix[14232]=9;
        m_nPix[14233]=9;
        m_nPix[14240]=11;
        m_nPix[14241]=11;
        m_nPix[14242]=11;
        m_nPix[14243]=11;
        m_nPix[14244]=11;
        m_nPix[14245]=11;
        m_nPix[14246]=11;
        m_nPix[14247]=11;
        m_nPix[14248]=11;
        m_nPix[14249]=11;
        m_nPix[14257]=3;
        m_nPix[14258]=3;
        m_nPix[14259]=3;
        m_nPix[14260]=3;
        m_nPix[14261]=3;
        m_nPix[14262]=3;
        m_nPix[14263]=3;
        m_nPix[14264]=3;
        m_nPix[14273]=4;
        m_nPix[14274]=4;
        m_nPix[14275]=4;
        m_nPix[14276]=4;
        m_nPix[14277]=4;
        m_nPix[14278]=4;
        m_nPix[14279]=4;
        m_nPix[14280]=4;
        m_nPix[14292]=13;
        m_nPix[14293]=13;
        m_nPix[14294]=13;
        m_nPix[14304]=8;
        m_nPix[14305]=8;
        m_nPix[14306]=8;
        m_nPix[14307]=8;
        m_nPix[14314]=9;
        m_nPix[14315]=9;
        m_nPix[14316]=9;
        m_nPix[14317]=9;
        m_nPix[14318]=9;
        m_nPix[14319]=9;
        m_nPix[14320]=9;
        m_nPix[14321]=9;
        m_nPix[14328]=11;
        m_nPix[14329]=11;
        m_nPix[14330]=11;
        m_nPix[14331]=11;
        m_nPix[14337]=3;
        m_nPix[14338]=3;
        m_nPix[14339]=3;
        m_nPix[14340]=3;
        m_nPix[14341]=3;
        m_nPix[14342]=3;
        m_nPix[14343]=3;
        m_nPix[14344]=3;
        m_nPix[14352]=4;
        m_nPix[14353]=4;
        m_nPix[14354]=4;
        m_nPix[14355]=4;
        m_nPix[14358]=4;
        m_nPix[14359]=4;
        m_nPix[14360]=4;
        m_nPix[14361]=4;
        m_nPix[14466]=13;
        m_nPix[14467]=13;
        m_nPix[14468]=13;
        m_nPix[14469]=13;
        m_nPix[14470]=13;
        m_nPix[14471]=13;
        m_nPix[14482]=8;
        m_nPix[14483]=8;
        m_nPix[14484]=8;
        m_nPix[14485]=8;
        m_nPix[14486]=8;
        m_nPix[14487]=8;
        m_nPix[14496]=9;
        m_nPix[14497]=9;
        m_nPix[14498]=9;
        m_nPix[14499]=9;
        m_nPix[14500]=9;
        m_nPix[14501]=9;
        m_nPix[14502]=9;
        m_nPix[14503]=9;
        m_nPix[14504]=9;
        m_nPix[14505]=9;
        m_nPix[14512]=11;
        m_nPix[14513]=11;
        m_nPix[14514]=11;
        m_nPix[14515]=11;
        m_nPix[14516]=11;
        m_nPix[14517]=11;
        m_nPix[14518]=11;
        m_nPix[14519]=11;
        m_nPix[14520]=11;
        m_nPix[14521]=11;
        m_nPix[14530]=3;
        m_nPix[14531]=3;
        m_nPix[14532]=3;
        m_nPix[14533]=3;
        m_nPix[14534]=3;
        m_nPix[14535]=3;
        m_nPix[14546]=4;
        m_nPix[14547]=4;
        m_nPix[14548]=4;
        m_nPix[14549]=4;
        m_nPix[14550]=4;
        m_nPix[14551]=4;
        m_nPix[14564]=13;
        m_nPix[14565]=13;
        m_nPix[14566]=13;
        m_nPix[14576]=8;
        m_nPix[14577]=8;
        m_nPix[14578]=8;
        m_nPix[14579]=8;
        m_nPix[14587]=9;
        m_nPix[14588]=9;
        m_nPix[14589]=9;
        m_nPix[14590]=9;
        m_nPix[14591]=9;
        m_nPix[14592]=9;
        m_nPix[14600]=11;
        m_nPix[14601]=11;
        m_nPix[14602]=11;
        m_nPix[14603]=11;
        m_nPix[14610]=3;
        m_nPix[14611]=3;
        m_nPix[14612]=3;
        m_nPix[14613]=3;
        m_nPix[14614]=3;
        m_nPix[14615]=3;
        m_nPix[14624]=4;
        m_nPix[14625]=4;
        m_nPix[14626]=4;
        m_nPix[14627]=4;
        m_nPix[14630]=4;
        m_nPix[14631]=4;
        m_nPix[14632]=4;
        m_nPix[14633]=4;
        m_nPix[30552]=15;
        m_nPix[30553]=15;
        m_nPix[30554]=15;
        m_nPix[30555]=15;
        m_nPix[30556]=15;
        m_nPix[30560]=15;
        m_nPix[30564]=15;
        m_nPix[30568]=15;
        m_nPix[30569]=15;
        m_nPix[30570]=15;
        m_nPix[30571]=15;
        m_nPix[30576]=15;
        m_nPix[30580]=15;
        m_nPix[30593]=15;
        m_nPix[30594]=15;
        m_nPix[30595]=15;
        m_nPix[30596]=15;
        m_nPix[30602]=15;
        m_nPix[30608]=15;
        m_nPix[30612]=15;
        m_nPix[30616]=15;
        m_nPix[30617]=15;
        m_nPix[30618]=15;
        m_nPix[30619]=15;
        m_nPix[30620]=15;
        m_nPix[30633]=15;
        m_nPix[30634]=15;
        m_nPix[30635]=15;
        m_nPix[30640]=15;
        m_nPix[30641]=15;
        m_nPix[30642]=15;
        m_nPix[30643]=15;
        m_nPix[30644]=15;
        m_nPix[30648]=15;
        m_nPix[30649]=15;
        m_nPix[30650]=15;
        m_nPix[30651]=15;
        m_nPix[30652]=15;
        m_nPix[30826]=15;
        m_nPix[30832]=15;
        m_nPix[30836]=15;
        m_nPix[30840]=15;
        m_nPix[30844]=15;
        m_nPix[30848]=15;
        m_nPix[30852]=15;
        m_nPix[30864]=15;
        m_nPix[30873]=15;
        m_nPix[30875]=15;
        m_nPix[30880]=15;
        m_nPix[30881]=15;
        m_nPix[30883]=15;
        m_nPix[30884]=15;
        m_nPix[30888]=15;
        m_nPix[30904]=15;
        m_nPix[30908]=15;
        m_nPix[30912]=15;
        m_nPix[30920]=15;
        m_nPix[31098]=15;
        m_nPix[31104]=15;
        m_nPix[31108]=15;
        m_nPix[31112]=15;
        m_nPix[31116]=15;
        m_nPix[31120]=15;
        m_nPix[31121]=15;
        m_nPix[31124]=15;
        m_nPix[31136]=15;
        m_nPix[31144]=15;
        m_nPix[31148]=15;
        m_nPix[31152]=15;
        m_nPix[31154]=15;
        m_nPix[31156]=15;
        m_nPix[31160]=15;
        m_nPix[31176]=15;
        m_nPix[31180]=15;
        m_nPix[31184]=15;
        m_nPix[31192]=15;
        m_nPix[31370]=15;
        m_nPix[31376]=15;
        m_nPix[31380]=15;
        m_nPix[31384]=15;
        m_nPix[31385]=15;
        m_nPix[31386]=15;
        m_nPix[31387]=15;
        m_nPix[31392]=15;
        m_nPix[31394]=15;
        m_nPix[31396]=15;
        m_nPix[31408]=15;
        m_nPix[31416]=15;
        m_nPix[31420]=15;
        m_nPix[31424]=15;
        m_nPix[31426]=15;
        m_nPix[31428]=15;
        m_nPix[31432]=15;
        m_nPix[31433]=15;
        m_nPix[31434]=15;
        m_nPix[31435]=15;
        m_nPix[31448]=15;
        m_nPix[31452]=15;
        m_nPix[31456]=15;
        m_nPix[31457]=15;
        m_nPix[31458]=15;
        m_nPix[31459]=15;
        m_nPix[31464]=15;
        m_nPix[31465]=15;
        m_nPix[31466]=15;
        m_nPix[31467]=15;
        m_nPix[31642]=15;
        m_nPix[31648]=15;
        m_nPix[31652]=15;
        m_nPix[31656]=15;
        m_nPix[31658]=15;
        m_nPix[31664]=15;
        m_nPix[31667]=15;
        m_nPix[31668]=15;
        m_nPix[31680]=15;
        m_nPix[31683]=15;
        m_nPix[31684]=15;
        m_nPix[31688]=15;
        m_nPix[31689]=15;
        m_nPix[31690]=15;
        m_nPix[31691]=15;
        m_nPix[31692]=15;
        m_nPix[31696]=15;
        m_nPix[31700]=15;
        m_nPix[31704]=15;
        m_nPix[31720]=15;
        m_nPix[31724]=15;
        m_nPix[31728]=15;
        m_nPix[31736]=15;
        m_nPix[31914]=15;
        m_nPix[31920]=15;
        m_nPix[31924]=15;
        m_nPix[31928]=15;
        m_nPix[31931]=15;
        m_nPix[31936]=15;
        m_nPix[31940]=15;
        m_nPix[31952]=15;
        m_nPix[31956]=15;
        m_nPix[31960]=15;
        m_nPix[31964]=15;
        m_nPix[31968]=15;
        m_nPix[31972]=15;
        m_nPix[31976]=15;
        m_nPix[31992]=15;
        m_nPix[31996]=15;
        m_nPix[32000]=15;
        m_nPix[32008]=15;
        m_nPix[32186]=15;
        m_nPix[32193]=15;
        m_nPix[32194]=15;
        m_nPix[32195]=15;
        m_nPix[32200]=15;
        m_nPix[32204]=15;
        m_nPix[32208]=15;
        m_nPix[32212]=15;
        m_nPix[32225]=15;
        m_nPix[32226]=15;
        m_nPix[32227]=15;
        m_nPix[32228]=15;
        m_nPix[32232]=15;
        m_nPix[32236]=15;
        m_nPix[32240]=15;
        m_nPix[32244]=15;
        m_nPix[32248]=15;
        m_nPix[32249]=15;
        m_nPix[32250]=15;
        m_nPix[32251]=15;
        m_nPix[32252]=15;
        m_nPix[32265]=15;
        m_nPix[32266]=15;
        m_nPix[32267]=15;
        m_nPix[32272]=15;
        m_nPix[32280]=15;
        m_nPix[34856]=15;
        m_nPix[34857]=15;
        m_nPix[34858]=15;
        m_nPix[34859]=15;
        m_nPix[34864]=15;
        m_nPix[34865]=15;
        m_nPix[34866]=15;
        m_nPix[34867]=15;
        m_nPix[34868]=15;
        m_nPix[34872]=15;
        m_nPix[34873]=15;
        m_nPix[34874]=15;
        m_nPix[34875]=15;
        m_nPix[34876]=15;
        m_nPix[34881]=15;
        m_nPix[34882]=15;
        m_nPix[34883]=15;
        m_nPix[34888]=15;
        m_nPix[34889]=15;
        m_nPix[34890]=15;
        m_nPix[34891]=15;
        m_nPix[34896]=15;
        m_nPix[34897]=15;
        m_nPix[34898]=15;
        m_nPix[34899]=15;
        m_nPix[34900]=15;
        m_nPix[34913]=15;
        m_nPix[34914]=15;
        m_nPix[34915]=15;
        m_nPix[34920]=15;
        m_nPix[34924]=15;
        m_nPix[34929]=15;
        m_nPix[34930]=15;
        m_nPix[34931]=15;
        m_nPix[34936]=15;
        m_nPix[34937]=15;
        m_nPix[34938]=15;
        m_nPix[34939]=15;
        m_nPix[34940]=15;
        m_nPix[34944]=15;
        m_nPix[34945]=15;
        m_nPix[34946]=15;
        m_nPix[34947]=15;
        m_nPix[34952]=15;
        m_nPix[34953]=15;
        m_nPix[34954]=15;
        m_nPix[34955]=15;
        m_nPix[34956]=15;
        m_nPix[34961]=15;
        m_nPix[34962]=15;
        m_nPix[34963]=15;
        m_nPix[34968]=15;
        m_nPix[34972]=15;
        m_nPix[34977]=15;
        m_nPix[34978]=15;
        m_nPix[34979]=15;
        m_nPix[34980]=15;
        m_nPix[34993]=15;
        m_nPix[34994]=15;
        m_nPix[34995]=15;
        m_nPix[35002]=15;
        m_nPix[35008]=15;
        m_nPix[35009]=15;
        m_nPix[35010]=15;
        m_nPix[35011]=15;
        m_nPix[35016]=15;
        m_nPix[35017]=15;
        m_nPix[35018]=15;
        m_nPix[35019]=15;
        m_nPix[35020]=15;
        m_nPix[35024]=15;
        m_nPix[35025]=15;
        m_nPix[35026]=15;
        m_nPix[35027]=15;
        m_nPix[35033]=15;
        m_nPix[35034]=15;
        m_nPix[35035]=15;
        m_nPix[35040]=15;
        m_nPix[35041]=15;
        m_nPix[35042]=15;
        m_nPix[35043]=15;
        m_nPix[35049]=15;
        m_nPix[35050]=15;
        m_nPix[35051]=15;
        m_nPix[35052]=15;
        m_nPix[35056]=15;
        m_nPix[35057]=15;
        m_nPix[35058]=15;
        m_nPix[35059]=15;
        m_nPix[35060]=15;
        m_nPix[35128]=15;
        m_nPix[35132]=15;
        m_nPix[35136]=15;
        m_nPix[35144]=15;
        m_nPix[35152]=15;
        m_nPix[35156]=15;
        m_nPix[35160]=15;
        m_nPix[35164]=15;
        m_nPix[35168]=15;
        m_nPix[35186]=15;
        m_nPix[35192]=15;
        m_nPix[35196]=15;
        m_nPix[35200]=15;
        m_nPix[35204]=15;
        m_nPix[35208]=15;
        m_nPix[35216]=15;
        m_nPix[35220]=15;
        m_nPix[35226]=15;
        m_nPix[35234]=15;
        m_nPix[35240]=15;
        m_nPix[35244]=15;
        m_nPix[35248]=15;
        m_nPix[35264]=15;
        m_nPix[35268]=15;
        m_nPix[35273]=15;
        m_nPix[35275]=15;
        m_nPix[35280]=15;
        m_nPix[35284]=15;
        m_nPix[35290]=15;
        m_nPix[35296]=15;
        m_nPix[35300]=15;
        m_nPix[35306]=15;
        m_nPix[35312]=15;
        m_nPix[35316]=15;
        m_nPix[35320]=15;
        m_nPix[35328]=15;
        m_nPix[35400]=15;
        m_nPix[35404]=15;
        m_nPix[35408]=15;
        m_nPix[35416]=15;
        m_nPix[35424]=15;
        m_nPix[35428]=15;
        m_nPix[35432]=15;
        m_nPix[35436]=15;
        m_nPix[35440]=15;
        m_nPix[35458]=15;
        m_nPix[35464]=15;
        m_nPix[35465]=15;
        m_nPix[35468]=15;
        m_nPix[35472]=15;
        m_nPix[35480]=15;
        m_nPix[35488]=15;
        m_nPix[35492]=15;
        m_nPix[35498]=15;
        m_nPix[35506]=15;
        m_nPix[35512]=15;
        m_nPix[35513]=15;
        m_nPix[35516]=15;
        m_nPix[35520]=15;
        m_nPix[35536]=15;
        m_nPix[35544]=15;
        m_nPix[35548]=15;
        m_nPix[35552]=15;
        m_nPix[35556]=15;
        m_nPix[35562]=15;
        m_nPix[35568]=15;
        m_nPix[35572]=15;
        m_nPix[35578]=15;
        m_nPix[35584]=15;
        m_nPix[35588]=15;
        m_nPix[35592]=15;
        m_nPix[35600]=15;
        m_nPix[35672]=15;
        m_nPix[35673]=15;
        m_nPix[35674]=15;
        m_nPix[35675]=15;
        m_nPix[35680]=15;
        m_nPix[35681]=15;
        m_nPix[35682]=15;
        m_nPix[35683]=15;
        m_nPix[35688]=15;
        m_nPix[35689]=15;
        m_nPix[35690]=15;
        m_nPix[35691]=15;
        m_nPix[35696]=15;
        m_nPix[35700]=15;
        m_nPix[35704]=15;
        m_nPix[35705]=15;
        m_nPix[35706]=15;
        m_nPix[35707]=15;
        m_nPix[35712]=15;
        m_nPix[35713]=15;
        m_nPix[35714]=15;
        m_nPix[35715]=15;
        m_nPix[35730]=15;
        m_nPix[35736]=15;
        m_nPix[35738]=15;
        m_nPix[35740]=15;
        m_nPix[35745]=15;
        m_nPix[35746]=15;
        m_nPix[35747]=15;
        m_nPix[35752]=15;
        m_nPix[35753]=15;
        m_nPix[35754]=15;
        m_nPix[35755]=15;
        m_nPix[35760]=15;
        m_nPix[35761]=15;
        m_nPix[35762]=15;
        m_nPix[35763]=15;
        m_nPix[35770]=15;
        m_nPix[35778]=15;
        m_nPix[35784]=15;
        m_nPix[35786]=15;
        m_nPix[35788]=15;
        m_nPix[35792]=15;
        m_nPix[35808]=15;
        m_nPix[35816]=15;
        m_nPix[35820]=15;
        m_nPix[35824]=15;
        m_nPix[35825]=15;
        m_nPix[35826]=15;
        m_nPix[35827]=15;
        m_nPix[35834]=15;
        m_nPix[35840]=15;
        m_nPix[35841]=15;
        m_nPix[35842]=15;
        m_nPix[35843]=15;
        m_nPix[35850]=15;
        m_nPix[35856]=15;
        m_nPix[35860]=15;
        m_nPix[35864]=15;
        m_nPix[35872]=15;
        m_nPix[35873]=15;
        m_nPix[35874]=15;
        m_nPix[35875]=15;
        m_nPix[35944]=15;
        m_nPix[35948]=15;
        m_nPix[35952]=15;
        m_nPix[35960]=15;
        m_nPix[35968]=15;
        m_nPix[35972]=15;
        m_nPix[35976]=15;
        m_nPix[35978]=15;
        m_nPix[35984]=15;
        m_nPix[36002]=15;
        m_nPix[36008]=15;
        m_nPix[36011]=15;
        m_nPix[36012]=15;
        m_nPix[36020]=15;
        m_nPix[36024]=15;
        m_nPix[36032]=15;
        m_nPix[36034]=15;
        m_nPix[36042]=15;
        m_nPix[36050]=15;
        m_nPix[36056]=15;
        m_nPix[36059]=15;
        m_nPix[36060]=15;
        m_nPix[36064]=15;
        m_nPix[36067]=15;
        m_nPix[36068]=15;
        m_nPix[36080]=15;
        m_nPix[36088]=15;
        m_nPix[36089]=15;
        m_nPix[36090]=15;
        m_nPix[36091]=15;
        m_nPix[36092]=15;
        m_nPix[36096]=15;
        m_nPix[36098]=15;
        m_nPix[36106]=15;
        m_nPix[36112]=15;
        m_nPix[36114]=15;
        m_nPix[36122]=15;
        m_nPix[36128]=15;
        m_nPix[36132]=15;
        m_nPix[36136]=15;
        m_nPix[36139]=15;
        m_nPix[36140]=15;
        m_nPix[36144]=15;
        m_nPix[36216]=15;
        m_nPix[36220]=15;
        m_nPix[36224]=15;
        m_nPix[36232]=15;
        m_nPix[36240]=15;
        m_nPix[36244]=15;
        m_nPix[36248]=15;
        m_nPix[36251]=15;
        m_nPix[36256]=15;
        m_nPix[36274]=15;
        m_nPix[36280]=15;
        m_nPix[36284]=15;
        m_nPix[36288]=15;
        m_nPix[36292]=15;
        m_nPix[36296]=15;
        m_nPix[36304]=15;
        m_nPix[36307]=15;
        m_nPix[36314]=15;
        m_nPix[36322]=15;
        m_nPix[36328]=15;
        m_nPix[36332]=15;
        m_nPix[36336]=15;
        m_nPix[36340]=15;
        m_nPix[36352]=15;
        m_nPix[36356]=15;
        m_nPix[36360]=15;
        m_nPix[36364]=15;
        m_nPix[36368]=15;
        m_nPix[36371]=15;
        m_nPix[36378]=15;
        m_nPix[36384]=15;
        m_nPix[36387]=15;
        m_nPix[36394]=15;
        m_nPix[36400]=15;
        m_nPix[36404]=15;
        m_nPix[36408]=15;
        m_nPix[36412]=15;
        m_nPix[36416]=15;
        m_nPix[36488]=15;
        m_nPix[36489]=15;
        m_nPix[36490]=15;
        m_nPix[36491]=15;
        m_nPix[36496]=15;
        m_nPix[36497]=15;
        m_nPix[36498]=15;
        m_nPix[36499]=15;
        m_nPix[36500]=15;
        m_nPix[36504]=15;
        m_nPix[36513]=15;
        m_nPix[36514]=15;
        m_nPix[36515]=15;
        m_nPix[36520]=15;
        m_nPix[36524]=15;
        m_nPix[36528]=15;
        m_nPix[36529]=15;
        m_nPix[36530]=15;
        m_nPix[36531]=15;
        m_nPix[36532]=15;
        m_nPix[36545]=15;
        m_nPix[36546]=15;
        m_nPix[36547]=15;
        m_nPix[36552]=15;
        m_nPix[36556]=15;
        m_nPix[36561]=15;
        m_nPix[36562]=15;
        m_nPix[36563]=15;
        m_nPix[36568]=15;
        m_nPix[36569]=15;
        m_nPix[36570]=15;
        m_nPix[36571]=15;
        m_nPix[36572]=15;
        m_nPix[36576]=15;
        m_nPix[36580]=15;
        m_nPix[36586]=15;
        m_nPix[36593]=15;
        m_nPix[36594]=15;
        m_nPix[36595]=15;
        m_nPix[36600]=15;
        m_nPix[36604]=15;
        m_nPix[36609]=15;
        m_nPix[36610]=15;
        m_nPix[36611]=15;
        m_nPix[36612]=15;
        m_nPix[36625]=15;
        m_nPix[36626]=15;
        m_nPix[36627]=15;
        m_nPix[36632]=15;
        m_nPix[36636]=15;
        m_nPix[36640]=15;
        m_nPix[36644]=15;
        m_nPix[36650]=15;
        m_nPix[36656]=15;
        m_nPix[36660]=15;
        m_nPix[36665]=15;
        m_nPix[36666]=15;
        m_nPix[36667]=15;
        m_nPix[36672]=15;
        m_nPix[36673]=15;
        m_nPix[36674]=15;
        m_nPix[36675]=15;
        m_nPix[36681]=15;
        m_nPix[36682]=15;
        m_nPix[36683]=15;
        m_nPix[36684]=15;
        m_nPix[36688]=15;
        m_nPix[36689]=15;
        m_nPix[36690]=15;
        m_nPix[36691]=15;
        m_nPix[36692]=15;
        m_nPix[39233]=15;
        m_nPix[39234]=15;
        m_nPix[39235]=15;
        m_nPix[39240]=15;
        m_nPix[39241]=15;
        m_nPix[39242]=15;
        m_nPix[39243]=15;
        m_nPix[39256]=15;
        m_nPix[39257]=15;
        m_nPix[39258]=15;
        m_nPix[39259]=15;
        m_nPix[39260]=15;
        m_nPix[39264]=15;
        m_nPix[39268]=15;
        m_nPix[39272]=15;
        m_nPix[39273]=15;
        m_nPix[39274]=15;
        m_nPix[39275]=15;
        m_nPix[39282]=15;
        m_nPix[39288]=15;
        m_nPix[39292]=15;
        m_nPix[39297]=15;
        m_nPix[39298]=15;
        m_nPix[39299]=15;
        m_nPix[39305]=15;
        m_nPix[39306]=15;
        m_nPix[39307]=15;
        m_nPix[39313]=15;
        m_nPix[39314]=15;
        m_nPix[39315]=15;
        m_nPix[39320]=15;
        m_nPix[39324]=15;
        m_nPix[39336]=15;
        m_nPix[39340]=15;
        m_nPix[39345]=15;
        m_nPix[39346]=15;
        m_nPix[39347]=15;
        m_nPix[39352]=15;
        m_nPix[39353]=15;
        m_nPix[39354]=15;
        m_nPix[39355]=15;
        m_nPix[39360]=15;
        m_nPix[39364]=15;
        m_nPix[39368]=15;
        m_nPix[39376]=15;
        m_nPix[39377]=15;
        m_nPix[39378]=15;
        m_nPix[39379]=15;
        m_nPix[39380]=15;
        m_nPix[39504]=15;
        m_nPix[39508]=15;
        m_nPix[39512]=15;
        m_nPix[39516]=15;
        m_nPix[39528]=15;
        m_nPix[39536]=15;
        m_nPix[39540]=15;
        m_nPix[39544]=15;
        m_nPix[39548]=15;
        m_nPix[39553]=15;
        m_nPix[39555]=15;
        m_nPix[39560]=15;
        m_nPix[39564]=15;
        m_nPix[39568]=15;
        m_nPix[39572]=15;
        m_nPix[39578]=15;
        m_nPix[39584]=15;
        m_nPix[39588]=15;
        m_nPix[39592]=15;
        m_nPix[39596]=15;
        m_nPix[39608]=15;
        m_nPix[39609]=15;
        m_nPix[39611]=15;
        m_nPix[39612]=15;
        m_nPix[39616]=15;
        m_nPix[39620]=15;
        m_nPix[39624]=15;
        m_nPix[39628]=15;
        m_nPix[39632]=15;
        m_nPix[39636]=15;
        m_nPix[39640]=15;
        m_nPix[39648]=15;
        m_nPix[39776]=15;
        m_nPix[39780]=15;
        m_nPix[39784]=15;
        m_nPix[39788]=15;
        m_nPix[39800]=15;
        m_nPix[39809]=15;
        m_nPix[39811]=15;
        m_nPix[39816]=15;
        m_nPix[39820]=15;
        m_nPix[39824]=15;
        m_nPix[39828]=15;
        m_nPix[39832]=15;
        m_nPix[39833]=15;
        m_nPix[39836]=15;
        m_nPix[39840]=15;
        m_nPix[39850]=15;
        m_nPix[39856]=15;
        m_nPix[39860]=15;
        m_nPix[39864]=15;
        m_nPix[39865]=15;
        m_nPix[39868]=15;
        m_nPix[39880]=15;
        m_nPix[39882]=15;
        m_nPix[39884]=15;
        m_nPix[39888]=15;
        m_nPix[39892]=15;
        m_nPix[39896]=15;
        m_nPix[39900]=15;
        m_nPix[39904]=15;
        m_nPix[39908]=15;
        m_nPix[39912]=15;
        m_nPix[39920]=15;
        m_nPix[40048]=15;
        m_nPix[40052]=15;
        m_nPix[40056]=15;
        m_nPix[40057]=15;
        m_nPix[40058]=15;
        m_nPix[40059]=15;
        m_nPix[40072]=15;
        m_nPix[40073]=15;
        m_nPix[40074]=15;
        m_nPix[40075]=15;
        m_nPix[40082]=15;
        m_nPix[40088]=15;
        m_nPix[40089]=15;
        m_nPix[40090]=15;
        m_nPix[40091]=15;
        m_nPix[40096]=15;
        m_nPix[40100]=15;
        m_nPix[40104]=15;
        m_nPix[40106]=15;
        m_nPix[40108]=15;
        m_nPix[40113]=15;
        m_nPix[40114]=15;
        m_nPix[40115]=15;
        m_nPix[40122]=15;
        m_nPix[40128]=15;
        m_nPix[40132]=15;
        m_nPix[40136]=15;
        m_nPix[40138]=15;
        m_nPix[40140]=15;
        m_nPix[40152]=15;
        m_nPix[40154]=15;
        m_nPix[40156]=15;
        m_nPix[40160]=15;
        m_nPix[40164]=15;
        m_nPix[40168]=15;
        m_nPix[40172]=15;
        m_nPix[40176]=15;
        m_nPix[40180]=15;
        m_nPix[40184]=15;
        m_nPix[40192]=15;
        m_nPix[40193]=15;
        m_nPix[40194]=15;
        m_nPix[40195]=15;
        m_nPix[40320]=15;
        m_nPix[40324]=15;
        m_nPix[40328]=15;
        m_nPix[40330]=15;
        m_nPix[40344]=15;
        m_nPix[40353]=15;
        m_nPix[40355]=15;
        m_nPix[40360]=15;
        m_nPix[40368]=15;
        m_nPix[40369]=15;
        m_nPix[40370]=15;
        m_nPix[40371]=15;
        m_nPix[40372]=15;
        m_nPix[40376]=15;
        m_nPix[40379]=15;
        m_nPix[40380]=15;
        m_nPix[40388]=15;
        m_nPix[40394]=15;
        m_nPix[40400]=15;
        m_nPix[40404]=15;
        m_nPix[40408]=15;
        m_nPix[40411]=15;
        m_nPix[40412]=15;
        m_nPix[40424]=15;
        m_nPix[40428]=15;
        m_nPix[40432]=15;
        m_nPix[40436]=15;
        m_nPix[40440]=15;
        m_nPix[40444]=15;
        m_nPix[40448]=15;
        m_nPix[40452]=15;
        m_nPix[40456]=15;
        m_nPix[40464]=15;
        m_nPix[40592]=15;
        m_nPix[40596]=15;
        m_nPix[40600]=15;
        m_nPix[40603]=15;
        m_nPix[40616]=15;
        m_nPix[40624]=15;
        m_nPix[40628]=15;
        m_nPix[40632]=15;
        m_nPix[40640]=15;
        m_nPix[40644]=15;
        m_nPix[40648]=15;
        m_nPix[40652]=15;
        m_nPix[40656]=15;
        m_nPix[40660]=15;
        m_nPix[40666]=15;
        m_nPix[40672]=15;
        m_nPix[40676]=15;
        m_nPix[40680]=15;
        m_nPix[40684]=15;
        m_nPix[40696]=15;
        m_nPix[40700]=15;
        m_nPix[40704]=15;
        m_nPix[40708]=15;
        m_nPix[40712]=15;
        m_nPix[40716]=15;
        m_nPix[40720]=15;
        m_nPix[40724]=15;
        m_nPix[40728]=15;
        m_nPix[40736]=15;
        m_nPix[40865]=15;
        m_nPix[40866]=15;
        m_nPix[40867]=15;
        m_nPix[40872]=15;
        m_nPix[40876]=15;
        m_nPix[40888]=15;
        m_nPix[40889]=15;
        m_nPix[40890]=15;
        m_nPix[40891]=15;
        m_nPix[40892]=15;
        m_nPix[40896]=15;
        m_nPix[40900]=15;
        m_nPix[40904]=15;
        m_nPix[40912]=15;
        m_nPix[40916]=15;
        m_nPix[40920]=15;
        m_nPix[40924]=15;
        m_nPix[40929]=15;
        m_nPix[40930]=15;
        m_nPix[40931]=15;
        m_nPix[40937]=15;
        m_nPix[40938]=15;
        m_nPix[40939]=15;
        m_nPix[40945]=15;
        m_nPix[40946]=15;
        m_nPix[40947]=15;
        m_nPix[40952]=15;
        m_nPix[40956]=15;
        m_nPix[40968]=15;
        m_nPix[40972]=15;
        m_nPix[40977]=15;
        m_nPix[40978]=15;
        m_nPix[40979]=15;
        m_nPix[40984]=15;
        m_nPix[40985]=15;
        m_nPix[40986]=15;
        m_nPix[40987]=15;
        m_nPix[40993]=15;
        m_nPix[40994]=15;
        m_nPix[40995]=15;
        m_nPix[41000]=15;
        m_nPix[41001]=15;
        m_nPix[41002]=15;
        m_nPix[41003]=15;
        m_nPix[41004]=15;
        m_nPix[41008]=15;
        m_nPix[41009]=15;
        m_nPix[41010]=15;
        m_nPix[41011]=15;
        m_nPix[41012]=15;
        m_nPix[41018]=15;
        m_nPix[47961]=15;
        m_nPix[47962]=15;
        m_nPix[47963]=15;
        m_nPix[47964]=15;
        m_nPix[47965]=15;
        m_nPix[47966]=15;
        m_nPix[47978]=15;
        m_nPix[47985]=15;
        m_nPix[47986]=15;
        m_nPix[47987]=15;
        m_nPix[47993]=15;
        m_nPix[47994]=15;
        m_nPix[47995]=15;
        m_nPix[48001]=15;
        m_nPix[48002]=15;
        m_nPix[48003]=15;
        m_nPix[48017]=15;
        m_nPix[48018]=15;
        m_nPix[48019]=15;
        m_nPix[48025]=15;
        m_nPix[48026]=15;
        m_nPix[48027]=15;
        m_nPix[48032]=15;
        m_nPix[48040]=15;
        m_nPix[48041]=15;
        m_nPix[48042]=15;
        m_nPix[48043]=15;
        m_nPix[48044]=15;
        m_nPix[48049]=15;
        m_nPix[48050]=15;
        m_nPix[48051]=15;
        m_nPix[48057]=15;
        m_nPix[48058]=15;
        m_nPix[48059]=15;
        m_nPix[48232]=15;
        m_nPix[48239]=15;
        m_nPix[48249]=15;
        m_nPix[48250]=15;
        m_nPix[48256]=15;
        m_nPix[48260]=15;
        m_nPix[48264]=15;
        m_nPix[48268]=15;
        m_nPix[48272]=15;
        m_nPix[48276]=15;
        m_nPix[48288]=15;
        m_nPix[48292]=15;
        m_nPix[48296]=15;
        m_nPix[48300]=15;
        m_nPix[48304]=15;
        m_nPix[48312]=15;
        m_nPix[48320]=15;
        m_nPix[48324]=15;
        m_nPix[48328]=15;
        m_nPix[48332]=15;
        m_nPix[48504]=15;
        m_nPix[48506]=15;
        m_nPix[48507]=15;
        m_nPix[48508]=15;
        m_nPix[48509]=15;
        m_nPix[48511]=15;
        m_nPix[48522]=15;
        m_nPix[48528]=15;
        m_nPix[48532]=15;
        m_nPix[48536]=15;
        m_nPix[48540]=15;
        m_nPix[48548]=15;
        m_nPix[48560]=15;
        m_nPix[48568]=15;
        m_nPix[48572]=15;
        m_nPix[48576]=15;
        m_nPix[48584]=15;
        m_nPix[48592]=15;
        m_nPix[48600]=15;
        m_nPix[48604]=15;
        m_nPix[48776]=15;
        m_nPix[48778]=15;
        m_nPix[48783]=15;
        m_nPix[48794]=15;
        m_nPix[48801]=15;
        m_nPix[48802]=15;
        m_nPix[48803]=15;
        m_nPix[48804]=15;
        m_nPix[48809]=15;
        m_nPix[48810]=15;
        m_nPix[48811]=15;
        m_nPix[48818]=15;
        m_nPix[48819]=15;
        m_nPix[48832]=15;
        m_nPix[48840]=15;
        m_nPix[48844]=15;
        m_nPix[48848]=15;
        m_nPix[48856]=15;
        m_nPix[48857]=15;
        m_nPix[48858]=15;
        m_nPix[48859]=15;
        m_nPix[48864]=15;
        m_nPix[48872]=15;
        m_nPix[48876]=15;
        m_nPix[49048]=15;
        m_nPix[49050]=15;
        m_nPix[49055]=15;
        m_nPix[49066]=15;
        m_nPix[49076]=15;
        m_nPix[49080]=15;
        m_nPix[49084]=15;
        m_nPix[49089]=15;
        m_nPix[49104]=15;
        m_nPix[49112]=15;
        m_nPix[49116]=15;
        m_nPix[49120]=15;
        m_nPix[49128]=15;
        m_nPix[49136]=15;
        m_nPix[49144]=15;
        m_nPix[49148]=15;
        m_nPix[49320]=15;
        m_nPix[49322]=15;
        m_nPix[49323]=15;
        m_nPix[49324]=15;
        m_nPix[49325]=15;
        m_nPix[49327]=15;
        m_nPix[49338]=15;
        m_nPix[49347]=15;
        m_nPix[49352]=15;
        m_nPix[49356]=15;
        m_nPix[49360]=15;
        m_nPix[49376]=15;
        m_nPix[49380]=15;
        m_nPix[49384]=15;
        m_nPix[49388]=15;
        m_nPix[49392]=15;
        m_nPix[49400]=15;
        m_nPix[49408]=15;
        m_nPix[49412]=15;
        m_nPix[49416]=15;
        m_nPix[49420]=15;
        m_nPix[49592]=15;
        m_nPix[49599]=15;
        m_nPix[49609]=15;
        m_nPix[49610]=15;
        m_nPix[49611]=15;
        m_nPix[49616]=15;
        m_nPix[49617]=15;
        m_nPix[49618]=15;
        m_nPix[49625]=15;
        m_nPix[49626]=15;
        m_nPix[49627]=15;
        m_nPix[49632]=15;
        m_nPix[49633]=15;
        m_nPix[49634]=15;
        m_nPix[49635]=15;
        m_nPix[49636]=15;
        m_nPix[49649]=15;
        m_nPix[49650]=15;
        m_nPix[49651]=15;
        m_nPix[49657]=15;
        m_nPix[49658]=15;
        m_nPix[49659]=15;
        m_nPix[49664]=15;
        m_nPix[49665]=15;
        m_nPix[49666]=15;
        m_nPix[49667]=15;
        m_nPix[49668]=15;
        m_nPix[49672]=15;
        m_nPix[49673]=15;
        m_nPix[49674]=15;
        m_nPix[49675]=15;
        m_nPix[49676]=15;
        m_nPix[49681]=15;
        m_nPix[49682]=15;
        m_nPix[49683]=15;
        m_nPix[49689]=15;
        m_nPix[49690]=15;
        m_nPix[49691]=15;
        m_nPix[49865]=15;
        m_nPix[49866]=15;
        m_nPix[49867]=15;
        m_nPix[49868]=15;
        m_nPix[49869]=15;
        m_nPix[49870]=15;
    }

    public static byte m_rgbRedPalette[] =
            {
                    (byte)0x00,
                    (byte)0x00,
                    (byte)0x20,
                    (byte)0x60,
                    (byte)0x20,
                    (byte)0x40,
                    (byte)0xA0,
                    (byte)0x40,
                    (byte)0xE0,
                    (byte)0xE0,
                    (byte)0xC0,
                    (byte)0xC0,
                    (byte)0x20,
                    (byte)0xC0,
                    (byte)0xA0,
                    (byte)0xE0
            };

    public static byte m_rgbGreenPalette[] =
            {
                    (byte)0x00,
                    (byte)0x00,
                    (byte)0xC0,
                    (byte)0xE0,
                    (byte)0x20,
                    (byte)0x60,
                    (byte)0x20,
                    (byte)0xC0,
                    (byte)0x20,
                    (byte)0x60,
                    (byte)0xC0,
                    (byte)0xC0,
                    (byte)0x80,
                    (byte)0x40,
                    (byte)0xA0,
                    (byte)0xE0
            };

    public static byte m_rgbBluePalette[] =
            {
                    (byte)0x00,
                    (byte)0x00,
                    (byte)0x20,
                    (byte)0x60,
                    (byte)0xE0,
                    (byte)0xE0,
                    (byte)0x20,
                    (byte)0xE0,
                    (byte)0x20,
                    (byte)0x60,
                    (byte)0x20,
                    (byte)0x80,
                    (byte)0x20,
                    (byte)0xA0,
                    (byte)0xA0,
                    (byte)0xE0
            };

    @Override
    public void setArcoFlexDroid(ArcoFlexDroid mm) {
        this.mm = mm;
    }

    @Override
    public void setScaleType(int scaleType) {

    }

    @Override
    public int getScaleType() {
        return 0;
    }
}
