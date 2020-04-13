package net.arcoflexdroid.input;

import android.inputmethodservice.InputMethodService;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputConnection;

import net.arcoflexdroid.MainActivity;
import net.arcoflexdroid.R;

import java.util.HashMap;

import static arcadeflex056.video.osd_refresh;
import static arcadeflex056.video.screen;
import static mame056.inputH.*;

public class ArcoFlexKeyboardMethod extends InputMethodService implements KeyboardView.OnKeyboardActionListener {

    public static boolean bKeyboard_P1 = true;

    @Override
    public View onCreateInputView() {
        KeyboardView keyboardView = (KeyboardView) getLayoutInflater().inflate(R.layout.keyboard_view, null);
        Keyboard keyboard = new Keyboard(this, R.xml.keyboard_full);
        keyboardView.setKeyboard(keyboard);
        keyboardView.setOnKeyboardActionListener(this);
        return keyboardView;
    }

    @Override
    public void onPress(int i) {

        System.out.println("onPress!!!! "+i);

        if (i==67) { // delete key
            screen.readkey = KeyEvent.KEYCODE_BACK;
            screen.key[screen.readkey] = true;
            osd_refresh();
        } else if ((i != 555001) && (screen != null) && (screen.key != null)){
            screen.readkey = i;
            screen.key[screen.readkey] = true;
            osd_refresh();
        }

    }

    @Override
    public void onRelease(int i) {

        System.out.println("onRelease!!!! "+i);
        switch (i){

            case 555001: // switch keyboard group
                if ((MainActivity.mm != null) && (MainActivity.mm.mKeyboard != null)){
                    if (bKeyboard_P1) {
                        MainActivity.mm.mKeyboard = new Keyboard(MainActivity.mm, R.xml.keyboard_pc_full_p2);
                    } else {
                        MainActivity.mm.mKeyboard = new Keyboard(MainActivity.mm, R.xml.keyboard_pc_full_p1);
                    }
                    bKeyboard_P1 = !bKeyboard_P1;
                    MainActivity.mm.mKeyboardView.setKeyboard(MainActivity.mm.mKeyboard);
                    //mKeyboardView.setOnKeyboardActionListener(new ArcoFlexKeyboardMethod());
                    MainActivity.mm.mKeyboardView.setPreviewEnabled(false);
                    MainActivity.mm.mKeyboardView.setVisibility(View.VISIBLE);
                }
                break;




        }

        if (i==67) { // delete key
            screen.readkey = KeyEvent.KEYCODE_BACK;
            screen.key[screen.readkey] = false;
            osd_refresh();
        } else if ((i != 555001) && (screen != null) && (screen.key != null)) {

            screen.readkey = i;
            screen.key[screen.readkey] = false;
            osd_refresh();

        }
    }

    @Override
    public void onKey(int primatyCode, int[] keyCodes) {
        InputConnection inputConnection = getCurrentInputConnection();
        System.out.println("onKey!!!!");
        if (inputConnection != null) {
            switch(primatyCode) {
                case Keyboard.KEYCODE_DELETE :
                    CharSequence selectedText = inputConnection.getSelectedText(0);

                    if (TextUtils.isEmpty(selectedText)) {
                        inputConnection.deleteSurroundingText(1, 0);
                    } else {
                        inputConnection.commitText("", 1);
                    }

                    break;
                default :
                    char code = (char) primatyCode;
                    inputConnection.commitText(String.valueOf(code), 1);

                    break;
            }

        }
    }

    @Override
    public void onText(CharSequence charSequence) {
        System.out.println("onText!!!!");
    }

    @Override
    public void swipeLeft() {

    }

    @Override
    public void swipeRight() {

    }

    @Override
    public void swipeDown() {

    }

    @Override
    public void swipeUp() {

    }


}

