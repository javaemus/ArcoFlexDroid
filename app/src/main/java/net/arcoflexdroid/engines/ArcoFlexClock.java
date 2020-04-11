package net.arcoflexdroid.engines;

import android.os.SystemClock;

public class ArcoFlexClock  {

    public ArcoFlexClock() {
        super();
    }

    public long uptimeMillis() {
        return SystemClock.uptimeMillis();
    }

}