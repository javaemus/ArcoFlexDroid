package net.arcoflexdroid.views;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.ImageView;

import net.arcoflexdroid.Emulator;
import net.arcoflexdroid.GLRenderer;
import net.arcoflexdroid.ArcoFlexDroid;
import net.arcoflexdroid.R;
import net.arcoflexdroid.helpers.PrefsHelper;

public class ArcoFlexEmulatorView extends SurfaceView implements SurfaceHolder.Callback, IEmuView {

    public SurfaceHolder mSurfaceHolder;
    public static Bitmap screenBitmap;
    public Matrix matrixScreen=null;
    public ArcoFlexDroid mm;

    public ArcoFlexEmulatorView(Context context, AttributeSet attrs) {
        super(context, attrs);

        this.mSurfaceHolder = getHolder();
        //this.holder.addCallback(this);
        this.mSurfaceHolder.addCallback(this);
    }

    public ArcoFlexEmulatorView(Context context) {
        super(context);

        this.mSurfaceHolder = getHolder();
        //this.holder.addCallback(this);
        this.mSurfaceHolder.addCallback(this);
    }

    public void requestRender(){
        System.out.println("Render!!!!");
        //this.onDraw();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        System.out.println("Pinto ArcoFlexEmulatorView View!");
        ImageView imageView=(ImageView) findViewById(R.id.EmulatorScreen);
        Bitmap bitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);
        //canvas = new Canvas(bitmap);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.RED);
        canvas.drawCircle(50, 50, 10, paint);
        if (imageView != null)
            imageView.setImageBitmap(bitmap);
        //canvas.drawBitmap(bitmap, 0, 0, paint);
    }

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

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        System.out.println("surfaceCreated!");
        this.mSurfaceHolder = surfaceHolder;
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        System.out.println("surfaceChanged!");
        mSurfaceHolder = surfaceHolder;
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        System.out.println("surfaceDestroyed");
        mSurfaceHolder = null;
    }

    public void drawScreenEmulator(Bitmap scaledBitmap) {
        this.screenBitmap = scaledBitmap;
        //mm.mImgEm.getImageBitmap(this.screenBitmap);
        //draw();
    }

    public synchronized void draw() {

        ImageView imageView=(ImageView) findViewById(R.id.EmulatorScreen);
        Bitmap bitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.RED);
        canvas.drawCircle(50, 50, 10, paint);
        if (imageView != null)
            imageView.setImageBitmap(bitmap);
        //canvas.drawBitmap(bitmap, 0, 0, paint);
            invalidate();
            //super.draw(canvas);

        //}
    }

    private static Bitmap resize(Bitmap image, int maxWidth, int maxHeight) {
        if (maxHeight > 0 && maxWidth > 0) {
            int width = image.getWidth();
            int height = image.getHeight();
            float ratioBitmap = (float) width / (float) height;
            float ratioMax = (float) maxWidth / (float) maxHeight;

            int finalWidth = maxWidth;
            int finalHeight = maxHeight;
            if (ratioMax > 1) {
                finalWidth = (int) ((float)maxHeight * ratioBitmap);
            } else {
                finalHeight = (int) ((float)maxWidth / ratioBitmap);
            }
            image = Bitmap.createScaledBitmap(image, finalWidth, finalHeight, true);
            return image;
        } else {
            return image;
        }
    }
}
