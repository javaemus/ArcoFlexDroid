package net.arcoflexdroid.input;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewConfiguration;

import net.arcoflexdroid.MainActivity;

public class ArcoFlexJoystickView extends View implements SensorEventListener, SurfaceHolder.Callback, Runnable {

    /**
     * Default View's size
     */
    private static final int DEFAULT_SIZE = 200;

    /**
     * Simple constructor to use when creating a JoystickView from code.
     * Call another constructor passing null to Attribute.
     * @param context The Context the JoystickView is running in, through which it can
     *        access the current theme, resources, etc.
     */
    public ArcoFlexJoystickView(Context context) {
        this(context, null);
    }


    public ArcoFlexJoystickView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs);
    }


    /**
     * Constructor that is called when inflating a JoystickView from XML. This is called
     * when a JoystickView is being constructed from an XML file, supplying attributes
     * that were specified in the XML file.
     * @param context The Context the JoystickView is running in, through which it can
     *        access the current theme, resources, etc.
     * @param attrs The attributes of the XML tag that is inflating the JoystickView.
     */
    public ArcoFlexJoystickView(Context context, AttributeSet attrs) {
        super(context, attrs);


    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
		/*if (event.getAction() == MotionEvent.ACTION_UP) {
			return true;
		}
		return false;*/
        // to force a new draw
        invalidate();

        if (MainActivity.mm.vKeyPad != null) {
            boolean b = MainActivity.mm.vKeyPad.onTouch(event, false);

            return b;
        }

        return false;
    }

    /**
     * Draw the background, the border and the button
     * @param canvas the canvas on which the shapes will be drawn
     */
    @Override
    protected void onDraw(Canvas canvas) {
        //System.out.println("Draw Joystick!!!!");
        MainActivity.mm.vKeyPad.draw(canvas);
        // to force a new draw
        invalidate();
    }


    /**
     * This is called during layout when the size of this view has changed.
     * Here we get the center of the view and the radius to draw all the shapes.
     *
     * @param w Current width of this view.
     * @param h Current height of this view.
     * @param oldW Old width of this view.
     * @param oldH Old height of this view.
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldW, int oldH) {
        super.onSizeChanged(w, h, oldW, oldH);

    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // setting the measured values to resize the view to a certain width and height
        int d = Math.min(measure(widthMeasureSpec), measure(heightMeasureSpec));
        setMeasuredDimension(d, d);
    }

    private int measure(int measureSpec) {
        if (MeasureSpec.getMode(measureSpec) == MeasureSpec.UNSPECIFIED) {
            // if no bounds are specified return a default size (200)
            return DEFAULT_SIZE;
        } else {
            // As you want to fill the available space
            // always return the full available bounds.
            return MeasureSpec.getSize(measureSpec);
        }
    }


    /*
    USER EVENT
     */




    /*
    GETTERS
     */


    /**
     * Set the background color for this JoystickView.
     * @param color the color of the background
     */
    @Override
    public void setBackgroundColor(int color) {
        //mPaintBackground.setColor(color);
        //invalidate();
    }



    /*
    IMPLEMENTS
     */


    @Override // Runnable
    public void run() {


    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {

    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

    }
}
