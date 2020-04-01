package net.arcoflexdroid.input;

import net.arcoflexdroid.ArcoFlexDroid;
import android.graphics.Rect;

public class InputValue {

    private int type;
    private int value;

    private int o_x1;
    private int o_y1;
    private int o_x2;
    private int o_y2;

    private float dx = 1;
    private float dy = 1;
    private int ax = 0;
    private int ay = 0;

    private int xoff_tmp = 0;
    private int yoff_tmp = 0;

    private int xoff = 0;
    private int yoff = 0;

    private int sz_x1;
    private int sz_y1;
    private int sz_x2;
    private int sz_y2;

    private Rect rect = null;

    private Rect origRect = null;

    private ArcoFlexDroid mm = null;

    public InputValue(int d[], ArcoFlexDroid mm){
        this.mm = mm;
        //data = d;
        type = d[0];
        value = d[1];

        if(type == InputHandler.TYPE_STICK_RECT && mm.getPrefsHelper().isTouchDZ())
        {
            if(value == InputHandler.STICK_LEFT)
            {
                d[4] -= d[4] * 0.18f;
            }
            if(value == InputHandler.STICK_RIGHT)
            {
                d[2] += d[4] * 0.18f;
                d[4] -= (d[4] * 0.18f);
            }
        }

        o_x1 = d[2];
        o_y1 = d[3];
        o_x2 = o_x1 + d[4];
        o_y2 = o_y1 + d[5];
    }

    public void setFixData(float dx, float dy, int ax, int ay)
    {
        this.dx = dx;
        this.dy = dy;
        this.ax = ax;
        this.ay = ay;
        rect = null;
    }

    public void setSize(int sz_x1,int sz_y1, int sz_x2, int sz_y2)
    {
        this.sz_x1 = sz_x1;
        this.sz_x2 = sz_x2;
        this.sz_y1 = sz_y1;
        this.sz_y2 = sz_y2;
        rect = null;
    }

    public void setOffset(int xoff,int yoff)
    {
        this.xoff = xoff;
        this.yoff = yoff;
        xoff_tmp = 0;
        yoff_tmp = 0;
        rect = null;
    }

    public void setOffsetTMP(int xoff_tmp,int yoff_tmp)
    {
        this.xoff_tmp = xoff_tmp;
        this.yoff_tmp = yoff_tmp;
        rect = null;
    }

    public void commitChanges()
    {
        xoff += xoff_tmp;
        yoff += yoff_tmp;
        xoff_tmp=0;
        yoff_tmp=0;
    }

    public Rect getRect()
    {
        if(rect==null)
        {
            rect =
                    new Rect( (int)(o_x1 * dx) + (int)(ax * 1) + xoff + xoff_tmp  + (int)(sz_x1*dx),
                            (int)(o_y1 * dy) + (int)(ay * 1) + yoff + yoff_tmp  + (int)(sz_y1*dy),
                            (int)(o_x2 * dx ) + (int)(ax * 1) + xoff + xoff_tmp + (int)(sz_x2*dx),
                            (int)(o_y2 * dy ) + (int)(ay * 1) + yoff + yoff_tmp + (int)(sz_y2*dy)
                    );
        }
        return rect;
    }

    protected Rect getOrigRect()
    {
        if(origRect==null)
        {
            origRect =  new Rect( o_x1, o_y1, o_x2, o_y2);
        }
        return origRect;
    }


    public int getType(){
        return type;
    }

    public int  getValue(){
        return value;
    }

    public int getXoff_tmp() {
        return xoff_tmp;
    }

    public int getYoff_tmp() {
        return yoff_tmp;
    }

    public int getXoff() {
        return xoff;
    }

    public int getYoff() {
        return yoff;
    }
}
