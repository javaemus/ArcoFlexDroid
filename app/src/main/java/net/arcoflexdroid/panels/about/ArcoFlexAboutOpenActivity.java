package net.arcoflexdroid.panels.about;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import net.arcoflexdroid.R;

import arcoflex056.platform.android.android_urlDownloadProgress;

import static arcadeflex056.settings.current_platform_configuration;

public class ArcoFlexAboutOpenActivity  extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_arcoflex_about);

        TextView t = (TextView)findViewById(R.id.textView2);
        t.setText("ArcoFlex - is a Java Multiple Emulator SYStem for Android OS and Java AWT/Swing. ArcoFlex supports on-screen keyboard, sound output, OpenGL rendering, load/save games states.\n\n");
        t.append("ArcoFlex based on MAME and MESS 0.56.2\n\n");
        t.append("Version: "+ ((android_urlDownloadProgress)current_platform_configuration.get_URLDownloadProgress_class())._version+"\n\n");
        t.append("Authors: George Moralis (Shadow) and Chuso (Chusogar)\n");
        //t.append("e-mail: chusogar@gmail.com\n");
        t.append("Web: https://github.com/javaemus\n");

    }

}

