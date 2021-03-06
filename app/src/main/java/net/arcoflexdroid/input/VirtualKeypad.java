package net.arcoflexdroid.input;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.preference.PreferenceManager;
import android.view.MotionEvent;
import android.view.View;

import androidx.appcompat.widget.Toolbar;

import net.arcoflexdroid.MainActivity;
import net.arcoflexdroid.R;

import java.util.ArrayList;

public class VirtualKeypad {

    public static int LEFT = 1;
    public static int RIGHT = 2;
    public static int UP = 4;
    public static int DOWN = 8;
    public static int BUTTON_FIRE_1 = 16;
    public static int BUTTON_COIN = 32;
    public static int BUTTON_START = 64;
    public static int BUTTON_FIRE_2 = 128;
    public static int BUTTON_FIRE_3 = 256;
    public static int BUTTON_FIRE_4 = 512;

    private static final float DPAD_DEADZONE_VALUES[] = {
            0.1f, 0.14f, 0.1667f, 0.2f, 0.25f,
    };

    private Context context;
    private View view;
    private float scaleX;
    private float scaleY;
    private int transparency;

    private GameKeyListener gameKeyListener;
    private int keyStates;
    private float dpadDeadZone = DPAD_DEADZONE_VALUES[2];

    private ArrayList<Control> controls = new ArrayList<Control>();
    private Control dpad;
    private Control buttons1;
    private Control buttons2;
    private Control buttons3;
    private Control buttons4;
    private Control buttonCoin;
    private Control buttonStart;

    private int _topMargin = 100;

    public VirtualKeypad(View v, GameKeyListener l, int dpadRes, int buttonsRes1, int buttonsRes2, int buttonsRes3, int buttonsRes4, int buttonCoinRes, int buttonStartRes) {
        view = v;
        context = view.getContext();
        gameKeyListener = l;

        dpad = createControl(dpadRes);
        buttons1 = createControl(buttonsRes1);
        buttons2 = createControl(buttonsRes2);
        buttons3 = createControl(buttonsRes3);
        buttons4 = createControl(buttonsRes4);
        buttonCoin = createControl(buttonCoinRes);
        buttonStart = createControl(buttonStartRes);


    }

    public final int getKeyStates() {
        return keyStates;
    }

    public void reset() {
        keyStates = 0;
    }

    public final void destroy() {
    }

    private void calculateTopMargin(){
        // calculates top margin
        Toolbar toolbar = MainActivity.mm.findViewById(R.id.toolbar);
        _topMargin = toolbar.getHeight() + 1;

        //System.out.println("Top Margin="+_topMargin);
    }

    public final void resize(int w, int h) {
        SharedPreferences prefs = PreferenceManager.
                getDefaultSharedPreferences(context);

        //int value = prefs.getInt("dpadDeadZone", 2);
        //value = (value < 0 ? 0 : (value > 4 ? 4 : value));
        dpadDeadZone = DPAD_DEADZONE_VALUES[2];

        dpad.hide(prefs.getBoolean("hideDpad", false));
        buttonStart.hide(prefs.getBoolean("hideDpad", false));
        buttonCoin.hide(prefs.getBoolean("hideDpad", false));
        buttons1.hide(!Wrapper.supportsMultitouch(context) || prefs.getBoolean("hideButtons", false));
        buttons2.hide(!Wrapper.supportsMultitouch(context) || prefs.getBoolean("hideButtons", false));
        buttons3.hide(!Wrapper.supportsMultitouch(context) || prefs.getBoolean("hideButtons", false));
        buttons4.hide(!Wrapper.supportsMultitouch(context) || prefs.getBoolean("hideButtons", false));

        scaleX = (float) w / view.getWidth();
        scaleY = (float) h / view.getHeight();

        float controlScale = getControlScale(prefs);
        float sx = scaleX * controlScale;
        float sy = scaleY * controlScale;
        Resources res = context.getResources();
        for (Control c : controls)
            c.load(res, sx, sy);

        reposition(w, h, prefs);

        transparency = prefs.getInt("vkeypadTransparency", 50);
    }

    public void draw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setAlpha(transparency * 2 + 30);

        for (Control c : controls)
            c.draw(canvas, paint);
    }

    private static float getControlScale(SharedPreferences prefs) {
        String value = prefs.getString("vkeypadSize", null);
        if ("small".equals(value))
            return 1.0f;
        if ("large".equals(value))
            return 1.33333f;
        return 1.2f;
    }

    private Control createControl(int resId) {
        Control c = new Control(resId);
        controls.add(c);
        return c;
    }

    private void makeBottomBottom(int w, int h) {

        calculateTopMargin();

        if (dpad.getWidth() + buttons1.getWidth() + buttons4.getWidth() > w) {
            makeBottomTop(w, h);
            return;
        }

        dpad.move(0, h - dpad.getHeight());
        buttons1.move(w - buttons1.getWidth(), h - buttons1.getHeight());
        buttons2.move(w - buttons1.getWidth() - buttons2.getWidth(), h - buttons2.getHeight());
        buttons3.move(w - buttons1.getWidth() - buttons3.getWidth(), h - buttons3.getHeight() - buttons4.getHeight());
        buttons4.move(w - buttons4.getWidth(), h - buttons4.getHeight() - buttons3.getHeight());

        buttonStart.move(w - buttonStart.getWidth(), 0 + _topMargin);
        buttonCoin.move(0, 0 + _topMargin);
    }

    private void makeTopTop(int w, int h) {

        calculateTopMargin();

        if (dpad.getWidth() + buttons1.getWidth() + buttons2.getWidth() > w) {
            makeBottomTop(w, h);
            return;
        }

        dpad.move(0, 0);
        buttons1.move(w - buttons1.getWidth(), buttons4.getHeight());
        buttons2.move(w - buttons1.getWidth() - buttons2.getWidth(), buttons3.getHeight());
        buttons3.move(w - buttons1.getWidth() - buttons3.getWidth(), 0);
        buttons4.move(w - buttons4.getWidth(), 0);

        buttonStart.move(w - buttonStart.getWidth(), h - buttonStart.getHeight());
        buttonCoin.move(0, h - buttonCoin.getHeight());
    }

    private void makeTopBottom(int w, int h) {

        calculateTopMargin();

        dpad.move(0, 0);
        buttons4.move(w - buttons4.getWidth(), h - buttons4.getHeight());
        buttons3.move(w - buttons4.getWidth() - buttons3.getWidth(), h - buttons1.getHeight());
        buttons2.move(w - buttons2.getWidth() - buttons1.getWidth(), h - buttons4.getHeight());
        buttons1.move(w - buttons1.getWidth(), h - buttons3.getHeight());

        buttonStart.move(w - buttonStart.getWidth(), 0);
        buttonCoin.move(0, 0);
    }

    private void makeBottomTop(int w, int h) {

        calculateTopMargin();

        dpad.move(0, h - dpad.getHeight());
        buttons4.move(w - buttons4.getWidth(), 0);
        buttons3.move(w - buttons4.getWidth() - buttons3.getWidth(), 0);
        buttons2.move(w - buttons2.getWidth() - buttons3.getWidth(), 0 + buttons4.getHeight());
        buttons1.move(w - buttons4.getWidth(), 0 + buttons3.getHeight());

        buttonStart.move(w - buttonStart.getWidth(), h - buttonStart.getHeight());
        buttonCoin.move(0, h - buttonStart.getHeight());
    }

    private void reposition(int w, int h, SharedPreferences prefs) {
        String layout = prefs.getString("vkeypadLayout", "bottom_bottom");

        if ("top_bottom".equals(layout))
            makeTopBottom(w, h);
        else if ("bottom_top".equals(layout))
            makeBottomTop(w, h);
        else if ("top_top".equals(layout))
            makeTopTop(w, h);
        else
            makeBottomBottom(w, h);
    }

    private void setKeyStates(int newStates) {
        if (keyStates == newStates)
            return;

        keyStates = newStates;
        gameKeyListener.onGameKeyChanged(keyStates);
    }

    private int getDpadStates(float x, float y) {

        final float cx = 0.5f;
        final float cy = 0.5f;
        int states = 0;

        if (x < cx - dpadDeadZone)
            states |= LEFT;
        else if (x > cx + dpadDeadZone)
            states |= RIGHT;
        if (y < cy - dpadDeadZone)
            states |= UP;
        else if (y > cy + dpadDeadZone)
            states |= DOWN;

        return states;
    }

    private int getButtons1States(float x, float y, float size) {
        int states = BUTTON_FIRE_1;
        return states;
    }

    private int getButtons2States(float x, float y, float size) {
        int states = BUTTON_FIRE_2;
        return states;
    }

    private int getButtons3States(float x, float y, float size) {
        int states = BUTTON_FIRE_3;
        return states;
    }

    private int getButtons4States(float x, float y, float size) {
        int states = BUTTON_FIRE_4;
        return states;
    }

    private int getButtonCoinStates(float x, float y, float size) {
        int states = BUTTON_COIN;
        return states;
    }

    private int getButtonStartStates(float x, float y, float size) {
        int states = BUTTON_START;
        return states;
    }

    private float getEventX(MotionEvent event, int index, boolean flip) {
        float x = Wrapper.MotionEvent_getX(event, index);
        if (flip)
            x = view.getWidth() - x;
        return (x * scaleX);
    }

    private float getEventY(MotionEvent event, int index, boolean flip) {
        float y = Wrapper.MotionEvent_getY(event, index);
        if (flip)
            y = view.getHeight() - y;
        return y * scaleY;
    }

    private Control findControl(float x, float y) {
        for (Control c : controls) {
            if (c.hitTest(x, y))
                return c;
        }
        return null;
    }

    private int getControlStates(Control c, float x, float y, float size) {
        x = (x - c.getX()) / c.getWidth();
        y = (y - c.getY()) / c.getHeight();

        if (c == dpad)
            return getDpadStates(x, y);
        if (c == buttons1)
            return getButtons1States(x, y, size);
        if (c == buttons2)
            return getButtons2States(x, y, size);
        if (c == buttons3)
            return getButtons3States(x, y, size);
        if (c == buttons4)
            return getButtons4States(x, y, size);
        if (c == buttonStart)
            return getButtonStartStates(x, y, size);
        if (c == buttonCoin)
            return getButtonCoinStates(x, y, size);

        return 0;
    }

    public boolean onTouch(MotionEvent event, boolean flip) {
        System.out.println("TOUCH!!!!");
        int action = event.getAction();

        switch (action & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                setKeyStates(0);
                return true;

            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
            case MotionEvent.ACTION_MOVE:
            case MotionEvent.ACTION_OUTSIDE:
                break;
            default:
                return false;
        }

        int states = 0;
        int n = Wrapper.MotionEvent_getPointerCount(event);
        for (int i = 0; i < n; i++) {
            float x = getEventX(event, i, flip);
            float y = getEventY(event, i, flip);
            Control c = findControl(x, y);
            if (c != null) {
                states |= getControlStates(c, x, y,
                        Wrapper.MotionEvent_getSize(event, i));
            }
        }
        System.out.println("States: "+states);
        setKeyStates(states);
        return true;
    }


    private static class Control {
        private int resId;
        private boolean hidden;
        private boolean disabled;
        private Bitmap bitmap;
        private RectF bounds = new RectF();

        Control(int r) {
            resId = r;
        }

        final float getX() {
            return bounds.left;
        }

        final float getY() {
            return bounds.top;
        }

        final int getWidth() {
            return bitmap.getWidth();
        }

        final int getHeight() {
            return bitmap.getHeight();
        }

        final boolean isEnabled() {
            return !disabled;
        }

        final void hide(boolean b) {
            hidden = b;
        }

        final void disable(boolean b) {
            disabled = b;
        }

        final boolean hitTest(float x, float y) {
            return bounds.contains(x, y);
        }

        final void move(float x, float y) {
            bounds.set(x, y, x + bitmap.getWidth(), y + bitmap.getHeight());
        }

        final void load(Resources res, float sx, float sy) {
            bitmap = ((BitmapDrawable) res.getDrawable(resId)).getBitmap();
            bitmap = Bitmap.createScaledBitmap(bitmap,
                    (int) (sx * bitmap.getWidth()),
                    (int) (sy * bitmap.getHeight()), true);
        }

        final void reload(Resources res, int id) {
            int w = bitmap.getWidth();
            int h = bitmap.getHeight();
            bitmap = ((BitmapDrawable) res.getDrawable(id)).getBitmap();
            bitmap = Bitmap.createScaledBitmap(bitmap, w, h, true);
        }

        final void draw(Canvas canvas, Paint paint) {
            if (!hidden && !disabled && bitmap != null)
                canvas.drawBitmap(bitmap, bounds.left, bounds.top, paint);
        }
    }
}

