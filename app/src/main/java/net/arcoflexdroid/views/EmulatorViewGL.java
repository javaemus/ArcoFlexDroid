package net.arcoflexdroid.views;

import java.util.ArrayList;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.view.MotionEvent;

import net.arcoflexdroid.ArcoFlexDroid;
import net.arcoflexdroid.Emulator;
import net.arcoflexdroid.GLRenderer;
import net.arcoflexdroid.ArcoFlexDroid;
import net.arcoflexdroid.helpers.PrefsHelper;

public  class EmulatorViewGL extends GLSurfaceView implements IEmuView{

    protected int scaleType = PrefsHelper.PREF_ORIGINAL;

    protected ArcoFlexDroid mm = null;

    protected GLRenderer render = null;

    public Renderer getRender() {
        return render;
    }

    public int getScaleType() {
        return scaleType;
    }

    public void setScaleType(int scaleType) {
        this.scaleType = scaleType;
    }

    public void setArcoFlexDroid(ArcoFlexDroid mm) {
        this.mm = mm;
        render.setArcoFlexDroid(mm);
    }

    public EmulatorViewGL(Context context) {
        super(context);
        init();
    }

    public EmulatorViewGL(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    protected void init(){
        this.setKeepScreenOn(true);
        this.setFocusable(true);
        this.setFocusableInTouchMode(true);
        this.requestFocus();
        render = new GLRenderer();
        //setDebugFlags(DEBUG_CHECK_GL_ERROR | DEBUG_LOG_GL_CALLS);
        setRenderer(render);
        setRenderMode(RENDERMODE_WHEN_DIRTY);
        //setRenderMode(RENDERMODE_CONTINUOUSLY);
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if(mm==null)
        {
            setMeasuredDimension(widthMeasureSpec,heightMeasureSpec);
        }
        else
        {
            ArrayList<Integer> l = mm.getMainHelper().measureWindow(widthMeasureSpec, heightMeasureSpec, scaleType);
            setMeasuredDimension(l.get(0).intValue(),l.get(1).intValue());
        }
        //System.out.println("onMeasure"+l.get(0).intValue()+" "+l.get(1).intValue());
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        Emulator.setWindowSize(w, h);
    }

    @Override
    public boolean onTrackballEvent(MotionEvent event) {
        return mm.getInputHandler().onTrackballEvent(event);
    }

}

