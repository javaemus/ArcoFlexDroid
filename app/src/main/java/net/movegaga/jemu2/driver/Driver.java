package net.movegaga.jemu2.driver;

import java.net.URL;

import jef.machine.Emulator;

public interface Driver {
    public static final int PROPERTY_HEIGHT = 1;
    public static final int PROPERTY_MANUFACTURER = 3;
    public static final int PROPERTY_NAME = 2;
    public static final int PROPERTY_WIDTH = 0;

    Emulator createEmulator(URL url, String str);

    Object getObject(int i);
}
