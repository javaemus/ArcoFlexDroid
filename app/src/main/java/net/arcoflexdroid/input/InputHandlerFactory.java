package net.arcoflexdroid.input;

import java.lang.reflect.Method;

import android.view.MotionEvent;

import net.arcoflexdroid.ArcoFlexDroid;

public class InputHandlerFactory {

    @SuppressWarnings("unused")
    static public InputHandler createInputHandler(ArcoFlexDroid mm){
        try {
            Class[] cArg = new Class[1];
            cArg[0] = Integer.TYPE;
            Method m = MotionEvent.class.getMethod("getAxisValue",cArg);
            return new InputHandlerExt(mm);//MultiTouch
            //return new InputHandler(mm);//FAKED para pruebas
        } catch (NoSuchMethodException e) {
            return new InputHandler(mm);
        }
    }
}

