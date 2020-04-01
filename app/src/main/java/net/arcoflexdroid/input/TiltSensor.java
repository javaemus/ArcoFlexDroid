package net.arcoflexdroid.input;

import java.text.DecimalFormat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.view.Surface;

import net.arcoflexdroid.ArcoFlexDroid;
import net.arcoflexdroid.helpers.PrefsHelper;


public class TiltSensor {


    DecimalFormat df = new DecimalFormat("000.00");

    protected ArcoFlexDroid mm = null;

    public void setArcoFlexDroid(ArcoFlexDroid value) {
        mm = value;
    }

    public static String str;

    public static float rx = 0;
    public static float ry = 0;

    private float tilt_x;
    private float tilt_z;
    private float ang_x;
    private float ang_z;

    private boolean fallback = false;

    private boolean init = false;

    private boolean enabled = false;

    public boolean isEnabled() {
        return enabled;
    }

    // Change this to make the sensors respond quicker, or slower:
    private static final int delay = SensorManager.SENSOR_DELAY_GAME;

    public TiltSensor() {

    }

    @SuppressLint("InlinedApi")
    public void enable(){

        if(!enabled){
            if(mm==null)
                return;
            if(!mm.getPrefsHelper().isTiltSensor())
                return;
            SensorManager man = (SensorManager) mm.getApplicationContext().getSystemService(Context.SENSOR_SERVICE);

            Sensor acc_sensor = null;

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD)
                acc_sensor = man.getDefaultSensor(Sensor.TYPE_GRAVITY);

            if(acc_sensor==null)
            {
                acc_sensor = man.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
                fallback = true;
            }
            enabled = man.registerListener(listen, acc_sensor, delay);
        }

    }

    synchronized public void disable(){
        if(enabled){
            SensorManager man = (SensorManager) mm.getApplicationContext().getSystemService(Context.SENSOR_SERVICE);
            man.unregisterListener(listen);
            enabled = false;
        }
    }

    int old_x = 0;
    int old_y = 0;

    protected void reset(){
        old_x=0;
        old_y=0;
        tilt_x = 0;
        tilt_z = 0;
        mm.getInputHandler().pad_data[0] = 0;
        /*TODO*///Emulator.setPadData(0, 0);
        mm.getInputHandler().handleImageStates(true);
        rx = 0;
        ry = 0;
        /*TODO*///Emulator.setAnalogData(0,0,0);
    }

    // Special class used to handle sensor events:
    private final SensorEventListener listen = new SensorEventListener() {
        synchronized public void onSensorChanged(SensorEvent e) {

            if(mm==null)return;

            PrefsHelper pH = mm.getPrefsHelper();

            final float alpha = 0.1f;
            //final float alpha = 0.3f;
            float value_x = - e.values[0];
            float value_z;

            if(pH.isTiltSwappedYZ())
                value_z = e.values[1];
            else
                value_z = e.values[2];

            try
            {
                int r = mm.getWindowManager().getDefaultDisplay().getRotation();

                if(r == Surface.ROTATION_0)
                    value_x = - e.values[0];
                else if(r == Surface.ROTATION_90)
                    value_x =   e.values[1];
                else if (r == Surface.ROTATION_180)
                    value_x =   e.values[0];
                else
                    value_x = - e.values[1];
            }catch(Error ee){};

            //low pass filter to get gravity
            if(fallback)
            {
                tilt_x = alpha * tilt_x + (1 - alpha) * value_x;
                tilt_z = alpha * tilt_z + (1 - alpha) * value_z;
            }
            else
            {
                tilt_x = value_x;
                tilt_z = value_z;
            }

            if(pH.isTiltInvertedX())
                tilt_x *= -1;

            float deadZone = getDZ();

            boolean run = false;
            /*TODO*///run = Emulator.isInMAME()  && !Emulator.isInMenu() && !Emulator.isPaused();

            if(run)
            {
                if(!init)
                {
                    reset();
                    init = true;
        		    /*
        			CharSequence text = "Tilt sensor is enabled";
        		    int duration = Toast.LENGTH_SHORT;
        		    Toast toast = Toast.makeText(mm, text, duration);
        		    toast.show();*/
                }

                boolean skip = (mm.getInputHandler().pad_data[0]  & IController.COIN_VALUE) != 0 ||
                        (mm.getInputHandler().pad_data[0]  &  IController.START_VALUE) != 0;//avoid player 2,3,4 start or coin

                if(Math.abs(tilt_x) < deadZone || skip)
                {
                    mm.getInputHandler().pad_data[0] &= ~InputHandler.LEFT_VALUE;
                    mm.getInputHandler().pad_data[0] &= ~InputHandler.RIGHT_VALUE;
                    old_x=0;
                    rx = 0;
                }
                else if (tilt_x < 0)
                {
                    mm.getInputHandler().pad_data[0] |= InputHandler.LEFT_VALUE;
                    mm.getInputHandler().pad_data[0] &= ~InputHandler.RIGHT_VALUE;
                    old_x=1;
                }
                else
                {
                    mm.getInputHandler().pad_data[0] &= ~InputHandler.LEFT_VALUE;
                    mm.getInputHandler().pad_data[0] |= InputHandler.RIGHT_VALUE;
                    old_x=2;
                }

                float a = getNeutralPos();

                if(Math.abs(tilt_z - a) < deadZone || skip)
                {
                    mm.getInputHandler().pad_data[0] &= ~InputHandler.UP_VALUE;
                    mm.getInputHandler().pad_data[0] &= ~InputHandler.DOWN_VALUE;
                    old_y=0;
                    ry = 0;
                }
                else if (tilt_z - a > 0)
                {
                    mm.getInputHandler().pad_data[0] |= InputHandler.UP_VALUE;
                    mm.getInputHandler().pad_data[0] &= ~InputHandler.DOWN_VALUE;
                    old_y=1;
                }
                else
                {
                    mm.getInputHandler().pad_data[0] &= ~InputHandler.UP_VALUE;
                    mm.getInputHandler().pad_data[0] |= InputHandler.DOWN_VALUE;
                    old_y=2;
                }

                /*TODO*///Emulator.setPadData(0, mm.getInputHandler().pad_data[0]);
                mm.getInputHandler().handleImageStates(true);

                if(pH.isTiltAnalog() && !skip)
                {
                    if(Math.abs(tilt_x) >=deadZone)
                    {
                        rx = ((float)(tilt_x - 0) / (float)(getSensitivity() - 0));
                        if(rx>1.0f)rx=1.0f;
                        if(rx<-1.0f)rx=-1.0f;
                    }

                    if(Math.abs(tilt_z - a) >=deadZone)
                    {
                        ry = ((float)(tilt_z - a - 0) / (float)(getSensitivity() - 0));
                        if(ry>1.0f)ry=1.0f;
                        if(ry<-1.0f)ry=-1.0f;
                    }
                }

                /*TODO*///Emulator.setAnalogData(0,rx,ry);
            }

            if(!run)
            {
                if(old_x!=0 || old_y!=0)
                    reset();
                init = false;
            }

            /*TODO*///if(Emulator.isDebug())
            /*TODO*///{
            /*TODO*///    ang_x = (float) Math.toDegrees(Math.atan( 9.81f / tilt_x) * 2);
            /*TODO*///    ang_x = ang_x < 0 ? -(ang_x + 180) : 180 - ang_x;
            /*TODO*///    ang_z = (float) Math.toDegrees(Math.atan( 9.81f / tilt_z) * 2);
            /*TODO*///    str = df.format(rx) + " "+ df.format(tilt_x)+" " +df.format(ang_x)+ " "+ df.format(ry) + " "+ df.format(tilt_z)+ " "+df.format(ang_z);
            /*TODO*///    mm.getInputView().invalidate();
            /*TODO*///}
        }

        public void onAccuracyChanged(Sensor event, int res) {}
    };

    protected float getDZ(){
        float v = 0;
        switch(mm.getPrefsHelper().getTiltDZ())
        {
            case 1: v = 0.0f;break;
            case 2: v = 0.1f;break;
            case 3: v = 0.25f;break;
            case 4: v = 0.5f;break;
            case 5: v = 1.5f;break;
        }
        return v;
    }

    protected float getSensitivity(){
        float v = 0;
        switch(mm.getPrefsHelper().getTiltSensitivity())
        {
            case 10: v = 1.0f;break;
            case 9: v = 1.5f;break;
            case 8: v = 2.0f;break;
            case 7: v = 2.5f;break;
            case 6: v = 3.0f;break;
            case 5: v = 3.5f;break;
            case 4: v = 4.0f;break;
            case 3: v = 4.5f;break;
            case 2: v = 5.0f;break;
            case 1: v = 5.5f;break;
        }
        return v;
    }

    protected float getNeutralPos(){
        float v = 0;
        switch(mm.getPrefsHelper().getTiltVerticalNeutralPos())
        {
            case 0: v = 0.0f;break;
            case 1: v = 1.09f;break;
            case 2: v = 2.18f;break;
            case 3: v = 3.27f;break;
            case 4: v = 4.36f;break;
            case 5: v = 5.0f;break;
            case 6: v = 5.45f;break;
            case 7: v = 6.54f;break;
            case 8: v = 7.63f;break;
            case 9: v = 8.72f;break;
            case 10: v = 9.81f;break;
        }
        return v;
    }
}

