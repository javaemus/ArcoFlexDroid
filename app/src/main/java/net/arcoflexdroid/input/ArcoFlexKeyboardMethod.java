package net.arcoflexdroid.input;

import android.inputmethodservice.InputMethodService;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputConnection;

import net.arcoflexdroid.R;

public class ArcoFlexKeyboardMethod extends InputMethodService implements KeyboardView.OnKeyboardActionListener {

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
    }

    @Override
    public void onRelease(int i) {
        System.out.println("onRelease!!!! "+i);
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

