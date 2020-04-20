package net.arcoflexdroid.panels.devices;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTabHost;

import net.arcoflexdroid.MainActivity;
import net.arcoflexdroid.R;
import net.arcoflexdroid.panels.filebrowser.ArcoFlexListView;
import net.arcoflexdroid.panels.filebrowser.ArcoFlexRemoteSearchExplorer;
import net.arcoflexdroid.panels.filebrowser.remote.spectrum.ArcoFlexAsyncRemoteWOSExplorer;
import net.arcoflexdroid.panels.filebrowser.remote.spectrum.ArcoFlexRemoteTRDExplorer;

public class ArcoFlexConfigConsoleFlexDriver extends DialogFragment {

    private FragmentTabHost tabHost;
    //public static boolean _endSelection = false;
    //public static boolean _selectedFile = false;
    public static String _SystemName = "";

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

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View _myView = inflater.inflate(R.layout.activity_arcoflex_file_open, container, false);
        //System.out.println("1");

        //System.out.println("2");
        tabHost = (FragmentTabHost) _myView.findViewById(android.R.id.tabhost);
        //System.out.println("3");
        tabHost.setup(MainActivity.mm, getChildFragmentManager(), android.R.id.tabcontent);
        //System.out.println("4");
        tabHost.addTab(tabHost.newTabSpec("tab1").setIndicator("Devices"),
                ArcoFlexConsoleFlexDevices.class, null);

        return _myView;

    }

}

