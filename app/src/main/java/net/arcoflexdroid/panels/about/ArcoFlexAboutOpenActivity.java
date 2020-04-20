package net.arcoflexdroid.panels.about;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import net.arcoflexdroid.R;

import arcoflex056.platform.android.android_urlDownloadProgress;

import static arcadeflex056.settings.current_platform_configuration;

public class ArcoFlexAboutOpenActivity extends DialogFragment {

    public ArcoFlexAboutOpenActivity() {

    }

    @Override
    public void onStart()
    {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null)
        {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View _myView = inflater.inflate(R.layout.activity_arcoflex_about, container, false);

        TextView t = (TextView) _myView.findViewById(R.id.textView2);
        t.setText("ArcoFlex - is a Java Multiple Emulator SYStem for Android OS and Java AWT/Swing. ArcoFlex supports on-screen keyboard, sound output, OpenGL rendering, load/save games states.\n\n");
        t.append("ArcoFlex based on MAME and MESS 0.56.2\n\n");
        if (current_platform_configuration != null && current_platform_configuration.get_URLDownloadProgress_class() != null)
            t.append("Version: "+ ((android_urlDownloadProgress)current_platform_configuration.get_URLDownloadProgress_class())._version+"\n\n");
        t.append("Authors: George Moralis (Shadow) and Chuso (Chusogar)\n");
        //t.append("e-mail: chusogar@gmail.com\n");
        t.append("Web: https://github.com/javaemus\n");

        return _myView;
    }

    @Override
    public void onCancel(DialogInterface dialog)
    {
        super.onCancel(dialog);
    }

}

