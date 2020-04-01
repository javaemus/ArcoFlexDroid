package net.arcoflexdroid.input;

public interface IController {

    final public static int UP_VALUE = 0x1;
    final public static int LEFT_VALUE=0x4;
    final public static int DOWN_VALUE=0x10;
    final public static int RIGHT_VALUE=0x40;
    final public static int START_VALUE=1<<8;
    final public static int COIN_VALUE=1<<9;
    final public static int E_VALUE=1<<10;
    final public static int F_VALUE=1<<11;
    final public static int C_VALUE=1<<12;
    final public static int A_VALUE=1<<13;
    final public static int B_VALUE=1<<14;
    final public static int D_VALUE=1<<15;
    final public static int EXIT_VALUE=1<<16;
    final public static int OPTION_VALUE=1<<17;

    final public static int STICK_NONE = 0;
    final public static int STICK_UP_LEFT = 1;
    final public static int STICK_UP = 2;
    final public static int STICK_UP_RIGHT = 3;
    final public static int STICK_LEFT = 4;
    final public static int STICK_RIGHT = 5;
    final public static int STICK_DOWN_LEFT = 6;
    final public static int STICK_DOWN = 7;
    final public static int STICK_DOWN_RIGHT = 8;

    final public static int NUM_BUTTONS = 10;

    final public static int BTN_A = 2;
    final public static int BTN_B = 3;
    final public static int BTN_C = 1;
    final public static int BTN_D = 0;
    final public static int BTN_E = 4;
    final public static int BTN_F = 5;
    final public static int BTN_EXIT = 6;
    final public static int BTN_OPTION = 7;
    final public static int BTN_COIN = 8;
    final public static int BTN_START = 9;

    final public static int BTN_PRESS_STATE = 0;
    final public static int BTN_NO_PRESS_STATE = 1;



    //http://stackoverflow.com/questions/2949036/android-how-to-get-a-custom-view-to-redraw-partially
    //http://stackoverflow.com/questions/3874051/getting-the-dirty-region-inside-draw

}
