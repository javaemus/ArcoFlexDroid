package net.arcoflexdroid.engines;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import net.arcoflexdroid.views.ArcoFlexEmulatorView;

public class ArcoFlexEngine  extends Thread {

    private short[] _audioFrame = new short[882];
    private long _emulTime;
    public boolean _killed = false;
    private boolean _paused = false;
    private long _realTime;
    private long _startTime;
    private ArcoFlexClock _clock;
    private static boolean changedSystem = false;

    private ArcoFlexEmulatorView _emuView;

    public ArcoFlexEngine(ArcoFlexEmulatorView emuView, ArcoFlexClock clock) {
        super();

        _clock = clock;

        _emuView = emuView;
    }

    /*public void setSystem(jMESYSSystem system) {
        _paused = true;
        _paused = false;
        changedSystem = true;
    }*/

    public void kill() {
        _killed = true;
    }

    public void run() {
        System.out.println("Running the Emulator!");

        waitResume();
        while (!this._killed) {
            this._realTime = _clock.uptimeMillis();
            if (this._emulTime > this._realTime) {
                try {
                    sleep(this._emulTime - this._realTime);
                } catch (InterruptedException e) {
                }
            }
            if (this._realTime > this._emulTime + 100) {
                this._emulTime = this._realTime - 20;
            }

            BitmapFactory.Options options = new BitmapFactory.Options();

            try {

                /*if ( _emuView.screenBitmap == null){
                    _emuView.setScreenBitmap(BitmapFactory.decodeResource(_emuView.getResources(), _system.getImageID(), options));
                }*/

                if (_emuView.showTestData) {
                    Bitmap bm = Bitmap.createBitmap(_emuView.getScreenWidth(), _emuView.getScreenHeight(), Bitmap.Config.ARGB_8888);
                    bm.setPixels(_emuView.getColorPixels(),0, _emuView.getScreenWidth(), 0, 0, _emuView.getScreenWidth(), _emuView.getScreenHeight());
                    _emuView.setScreenBitmap(bm);
                } else {
                    //_emuView.setScreenBitmap(BitmapFactory.decodeResource(_emuView.getResources(), _system.getImageID(), options));
                }
            } catch (Exception e) {
                e.printStackTrace(System.out);
            }

            /*if (changedSystem) {
                System.out.println("Actualizo Pantalla...");
                _emuView.draw();
                changedSystem = false;
            }*/

            _emuView.draw();

            this._emulTime += 20;
            if (this._paused) {
                doPause();
                waitResume();
            }
        }
    }

    void waitResume() {
        while (this._paused) {
            try {
                sleep(50);
            } catch (InterruptedException e) {
            }
        }
        if (!this._killed) {
            doResume();
        }
    }

    public void doResume() {

        this._startTime = _clock.uptimeMillis();
        this._emulTime = 0;

        this._paused = false;
    }

    public void doPause() {

        this._paused = true;

    }

}

