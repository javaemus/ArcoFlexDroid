/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package arcoflex056.platform.android;

import arcoflex056.platform.platformConfigurator;
import static mame056.inputH.*;
import static arcadeflex056.video.screen;

import android.view.KeyEvent;

/**
 *
 * @author jagsanchez
 */
class android_inputClass implements platformConfigurator.i_input_class {

    static KeyboardInfo[] keylist = {
            new KeyboardInfo( "A",			KeyEvent.KEYCODE_A,				KEYCODE_A ),
            new KeyboardInfo( "B",			KeyEvent.KEYCODE_B,				KEYCODE_B ),
            new KeyboardInfo( "C",			KeyEvent.KEYCODE_C,				KEYCODE_C ),
            new KeyboardInfo( "D",			KeyEvent.KEYCODE_D,				KEYCODE_D ),
            new KeyboardInfo( "E",			KeyEvent.KEYCODE_E,				KEYCODE_E ),
            new KeyboardInfo( "F",			KeyEvent.KEYCODE_F,				KEYCODE_F ),
            new KeyboardInfo( "G",			KeyEvent.KEYCODE_G,				KEYCODE_G ),
            new KeyboardInfo( "H",			KeyEvent.KEYCODE_H,				KEYCODE_H ),
            new KeyboardInfo( "I",			KeyEvent.KEYCODE_I,				KEYCODE_I ),
            new KeyboardInfo( "J",			KeyEvent.KEYCODE_J,				KEYCODE_J ),
            new KeyboardInfo( "K",			KeyEvent.KEYCODE_K,				KEYCODE_K ),
            new KeyboardInfo( "L",			KeyEvent.KEYCODE_L,				KEYCODE_L ),
            new KeyboardInfo( "M",			KeyEvent.KEYCODE_M,				KEYCODE_M ),
            new KeyboardInfo( "N",			KeyEvent.KEYCODE_N,				KEYCODE_N ),
            new KeyboardInfo( "O",			KeyEvent.KEYCODE_O,				KEYCODE_O ),
            new KeyboardInfo( "P",			KeyEvent.KEYCODE_P,				KEYCODE_P ),
            new KeyboardInfo( "Q",			KeyEvent.KEYCODE_Q,				KEYCODE_Q ),
            new KeyboardInfo( "R",			KeyEvent.KEYCODE_R,				KEYCODE_R ),
            new KeyboardInfo( "S",			KeyEvent.KEYCODE_S,				KEYCODE_S ),
            new KeyboardInfo( "T",			KeyEvent.KEYCODE_T,				KEYCODE_T ),
            new KeyboardInfo( "U",			KeyEvent.KEYCODE_U,				KEYCODE_U ),
            new KeyboardInfo( "V",			KeyEvent.KEYCODE_V,				KEYCODE_V ),
            new KeyboardInfo( "W",			KeyEvent.KEYCODE_W,				KEYCODE_W ),
            new KeyboardInfo( "X",			KeyEvent.KEYCODE_X,				KEYCODE_X ),
            new KeyboardInfo( "Y",			KeyEvent.KEYCODE_Y,				KEYCODE_Y ),
            new KeyboardInfo( "Z",			KeyEvent.KEYCODE_Z,				KEYCODE_Z ),
            new KeyboardInfo( "0",			KeyEvent.KEYCODE_0,				KEYCODE_0 ),
            new KeyboardInfo( "1",			KeyEvent.KEYCODE_1,				KEYCODE_1 ),
            new KeyboardInfo( "2",			KeyEvent.KEYCODE_2,				KEYCODE_2 ),
            new KeyboardInfo( "3",			KeyEvent.KEYCODE_3,				KEYCODE_3 ),
            new KeyboardInfo( "4",			KeyEvent.KEYCODE_4,				KEYCODE_4 ),
            new KeyboardInfo( "5",			KeyEvent.KEYCODE_5,				KEYCODE_5 ),
            new KeyboardInfo( "6",			KeyEvent.KEYCODE_6,				KEYCODE_6 ),
            new KeyboardInfo( "7",			KeyEvent.KEYCODE_7,				KEYCODE_7 ),
            new KeyboardInfo( "8",			KeyEvent.KEYCODE_8,				KEYCODE_8 ),
            new KeyboardInfo( "9",			KeyEvent.KEYCODE_9,				KEYCODE_9 ),
            new KeyboardInfo( "0 PAD",		KeyEvent.KEYCODE_NUMPAD_0,		KEYCODE_0_PAD ),
            new KeyboardInfo( "1 PAD",		KeyEvent.KEYCODE_NUMPAD_1,		KEYCODE_1_PAD ),
            new KeyboardInfo( "2 PAD",		KeyEvent.KEYCODE_NUMPAD_2,            KEYCODE_2_PAD ),
            new KeyboardInfo( "3 PAD",		KeyEvent.KEYCODE_NUMPAD_3,		KEYCODE_3_PAD ),
            new KeyboardInfo( "4 PAD",		KeyEvent.KEYCODE_NUMPAD_4,		KEYCODE_4_PAD ),
            new KeyboardInfo( "5 PAD",		KeyEvent.KEYCODE_NUMPAD_5,		KEYCODE_5_PAD ),
            new KeyboardInfo( "6 PAD",		KeyEvent.KEYCODE_NUMPAD_6,		KEYCODE_6_PAD ),
            new KeyboardInfo( "7 PAD",		KeyEvent.KEYCODE_NUMPAD_7,		KEYCODE_7_PAD ),
            new KeyboardInfo( "8 PAD",		KeyEvent.KEYCODE_NUMPAD_8,		KEYCODE_8_PAD ),
            new KeyboardInfo( "9 PAD",		KeyEvent.KEYCODE_NUMPAD_9,		KEYCODE_9_PAD ),
            new KeyboardInfo( "F1",			KeyEvent.KEYCODE_F1,			KEYCODE_F1 ),
            new KeyboardInfo( "F2",			KeyEvent.KEYCODE_F2,			KEYCODE_F2 ),
            new KeyboardInfo( "F3",			KeyEvent.KEYCODE_F3,			KEYCODE_F3 ),
            new KeyboardInfo( "F4",			KeyEvent.KEYCODE_F4,			KEYCODE_F4 ),
            new KeyboardInfo( "F5",			KeyEvent.KEYCODE_F5,			KEYCODE_F5 ),
            new KeyboardInfo( "F6",			KeyEvent.KEYCODE_F6,			KEYCODE_F6 ),
            new KeyboardInfo( "F7",			KeyEvent.KEYCODE_F7,			KEYCODE_F7 ),
            new KeyboardInfo( "F8",			KeyEvent.KEYCODE_F8,			KEYCODE_F8 ),
            new KeyboardInfo( "F9",			KeyEvent.KEYCODE_F9,			KEYCODE_F9 ),
            new KeyboardInfo( "F10",		KeyEvent.KEYCODE_F10,		KEYCODE_F10 ),
            new KeyboardInfo( "F11",		KeyEvent.KEYCODE_F11,		KEYCODE_F11 ),
            new KeyboardInfo( "F12",		KeyEvent.KEYCODE_F12,		KEYCODE_F12 ),
            new KeyboardInfo( "ESC",		KeyEvent.KEYCODE_ESCAPE,		KEYCODE_ESC ),
            /*TODO*///new KeyboardInfo( "~",			KeyEvent.KEYCODE_BACK_QUOTE,		KEYCODE_TILDE ),
            /*TODO the rest codes */
            /*
            new KeyboardInfo( "-",          KEY_MINUS,          KEYCODE_MINUS ),
            new KeyboardInfo( "=",          KEY_EQUALS,         KEYCODE_EQUALS ),*/
            new KeyboardInfo( "BKSPACE",	KeyEvent.KEYCODE_BACK,		KEYCODE_BACKSPACE ),
            new KeyboardInfo( "TAB",	KeyEvent.KEYCODE_TAB,		KEYCODE_TAB ),
            /*TODO*///new KeyboardInfo( "[",          128,      KEYCODE_OPENBRACE ),
            /*TODO*///new KeyboardInfo( "]",          KeyEvent.KEYCODE_CLOSE_BRACKET,     KEYCODE_CLOSEBRACE ),
            new KeyboardInfo( "ENTER",		KeyEvent.KEYCODE_ENTER,			KEYCODE_ENTER ),
            new KeyboardInfo( ";",          KeyEvent.KEYCODE_SEMICOLON,          KEYCODE_COLON ),
            /*TODO*///new KeyboardInfo( ":",          KeyEvent.KEYCODE_QUOTE,          KEYCODE_QUOTE ),
            new KeyboardInfo( "\\",         KeyEvent.KEYCODE_BACKSLASH,      KEYCODE_BACKSLASH ),
            //new KeyboardInfo( "<",          KEY_BACKSLASH2,     KEYCODE_BACKSLASH2 ),
            new KeyboardInfo( ",",          KeyEvent.KEYCODE_COMMA,          KEYCODE_COMMA ),
            /*new KeyboardInfo( ".",          KEY_STOP,           KEYCODE_STOP ),
            new KeyboardInfo( "/",          KEY_SLASH,          KEYCODE_SLASH ),*/
            new KeyboardInfo( "SPACE",		KeyEvent.KEYCODE_SPACE,		KEYCODE_SPACE ),
            ///new KeyboardInfo( "INS",		KEY_INSERT,			KEYCODE_INSERT ),
            new KeyboardInfo( "DEL",		KeyEvent.KEYCODE_DEL,			KEYCODE_DEL ),
            /*new KeyboardInfo( "HOME",		KEY_HOME,			KEYCODE_HOME ),
            new KeyboardInfo( "END",		KEY_END,			KEYCODE_END ),*/
            new KeyboardInfo( "PGUP",		KeyEvent.KEYCODE_PAGE_UP,		KEYCODE_PGUP ),
            new KeyboardInfo( "PGDN",		KeyEvent.KEYCODE_PAGE_DOWN,		KEYCODE_PGDN ),
            new KeyboardInfo( "LEFT",		KeyEvent.KEYCODE_DPAD_LEFT,	        KEYCODE_LEFT ),
            new KeyboardInfo( "RIGHT",		KeyEvent.KEYCODE_DPAD_RIGHT,		KEYCODE_RIGHT ),
            new KeyboardInfo( "UP",			KeyEvent.KEYCODE_DPAD_UP,                 KEYCODE_UP ),
            new KeyboardInfo( "DOWN",		KeyEvent.KEYCODE_DPAD_DOWN,		KEYCODE_DOWN ),
            /*new KeyboardInfo( "/ PAD",      KEY_SLASH_PAD,      KEYCODE_SLASH_PAD ),*/
            new KeyboardInfo( "* PAD",      KeyEvent.KEYCODE_STAR,       KEYCODE_ASTERISK ),
            new KeyboardInfo( "- PAD",      KeyEvent.KEYCODE_MINUS,      KEYCODE_MINUS_PAD ),
            new KeyboardInfo( "+ PAD",      KeyEvent.KEYCODE_PLUS,       KEYCODE_PLUS_PAD ),
            //new KeyboardInfo( ". PAD",      KeyEvent.KEYCODE_DEL_PAD,        KEYCODE_DEL_PAD ),
            /*new KeyboardInfo( "ENTER PAD",  KEY_ENTER_PAD,      KEYCODE_ENTER_PAD ),
            new KeyboardInfo( "PRTSCR",     KEY_PRTSCR,         KEYCODE_PRTSCR ),
            new KeyboardInfo( "PAUSE",      KEY_PAUSE,          KEYCODE_PAUSE ),*/
            //LSHIFT + RSHOFT SAME IN JAVA
            new KeyboardInfo( "LSHIFT",		KeyEvent.KEYCODE_SHIFT_LEFT,			KEYCODE_LSHIFT ),
            new KeyboardInfo( "RSHIFT",		KeyEvent.KEYCODE_SHIFT_RIGHT,			KEYCODE_RSHIFT ),
            //LCONTROL + RCONTROL ARE THE SAME IN JAVA....
            new KeyboardInfo( "LCTRL",		KeyEvent.KEYCODE_CTRL_LEFT,		KEYCODE_LCONTROL ),
            new KeyboardInfo( "RCTRL",		KeyEvent.KEYCODE_CTRL_RIGHT,		KEYCODE_RCONTROL ),
            //RALT - LALT ARE THE SAME IN JAVA
            new KeyboardInfo( "ALT",		KeyEvent.KEYCODE_ALT_LEFT,		KEYCODE_LALT ),
            /*TODO*///new KeyboardInfo( "ALTGR",		KeyEvent.KEYCODE_ALT_GRAPH,		KEYCODE_RALT ),
            /*new KeyboardInfo( "LWIN",		KEY_LWIN,			KEYCODE_OTHER ),
            new KeyboardInfo( "RWIN",		KEY_RWIN,			KEYCODE_OTHER ),
            new KeyboardInfo( "MENU",		KEY_MENU,			KEYCODE_OTHER ),*/
            new KeyboardInfo( "SCRLOCK",    KeyEvent.KEYCODE_SCROLL_LOCK,        KEYCODE_SCRLOCK ),
            /*new KeyboardInfo( "NUMLOCK",    KEY_NUMLOCK,        KEYCODE_NUMLOCK ),
            new KeyboardInfo( "CAPSLOCK",   KEY_CAPSLOCK,       KEYCODE_CAPSLOCK ),*/
            new KeyboardInfo( null, 0, 0 )	/* end of table */
    };


    @Override
    public KeyboardInfo[] osd_get_key_list() {
        return keylist;
    }

    @Override
    public int osd_is_key_pressed(int keycode) {
        if (keycode >= 256) return 0;

        //TODO          /*if (keycode == KEY_PAUSE)
           /*         {
                            static int pressed,counter;
                            int res;

                            res = key[KEY_PAUSE] ^ pressed;
                            if (res)
                            {
                                    if (counter > 0)
                                    {
                                            if (--counter == 0)
                                                    pressed = key[KEY_PAUSE];
                                    }
                                    else counter = 10;
                            }

                            return res;
                    }*/
        if (screen != null && screen.key != null)
            return screen.key[keycode] ? 1 :0;
        else
            return 0;
    }
    
}
