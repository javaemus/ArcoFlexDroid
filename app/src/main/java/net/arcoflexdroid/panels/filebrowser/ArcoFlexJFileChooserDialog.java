package net.arcoflexdroid.panels.filebrowser;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTabHost;

import net.arcoflexdroid.MainActivity;
import net.arcoflexdroid.R;
import net.arcoflexdroid.panels.filebrowser.remote.spectrum.ArcoFlexAsyncRemoteWOSExplorer;
import net.arcoflexdroid.panels.filebrowser.remote.spectrum.ArcoFlexRemoteTRDExplorer;

import java.io.File;

import static arcadeflex056.video.osd_refresh;

public class ArcoFlexJFileChooserDialog extends DialogFragment {

    private FragmentTabHost tabHost;
    public static boolean _endSelection = false;
    //public static boolean _selectedFile = false;
    public static File _fSelected = null;

    public ArcoFlexJFileChooserDialog() {
        // Empty constructor is required for DialogFragment

        // Make sure not to add arguments to the constructor

        // Use `newInstance` instead as shown below

    }

    public static ArcoFlexJFileChooserDialog newInstance(String title) {
        ArcoFlexJFileChooserDialog frag = new ArcoFlexJFileChooserDialog();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        //frag.setCancelable(false); // set MODAL
        return frag;
    }

    @Override
    public void onCancel(DialogInterface dialog)
    {
        super.onCancel(dialog);
        _endSelection = true;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //_endSelection = false;
        return inflater.inflate(R.layout.activity_arcoflex_file_open, container, false);
    }

    /*@Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog d = super.onCreateDialog(savedInstanceState);
        d.getWindow().getAttributes().windowAnimations = R.style.Theme_AppCompat_Dialog;
        d.requestWindowFeature(Window.FEATURE_NO_TITLE);
        d.setCanceledOnTouchOutside(false);
        return d;
    }*/


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Get field from view

        //System.out.println("2");
        tabHost = (FragmentTabHost) view.findViewById(android.R.id.tabhost);
        //System.out.println("3");
        tabHost.setup(MainActivity.mm, getChildFragmentManager(), android.R.id.tabcontent);
        //System.out.println("4");
        tabHost.addTab(tabHost.newTabSpec("tab1").setIndicator("Local"),
                ArcoFlexListView.class, null);

        //System.out.println("5");
        tabHost.addTab(tabHost.newTabSpec("tab2").setIndicator("WOS"),
                ArcoFlexAsyncRemoteWOSExplorer.class, null);
        //System.out.println("6");
        tabHost.addTab(tabHost.newTabSpec("tab3").setIndicator("TRD"),
                ArcoFlexRemoteTRDExplorer.class, null);
        System.out.println("IMPLEMENTAR BookshelfActivity!!!!");
        /*tabHost.addTab(tabHost.newTabSpec("tab4").setIndicator("Favourites"),
                BookshelfActivity.class, null);*/
        tabHost.addTab(tabHost.newTabSpec("tab5").setIndicator("Other"),
                ArcoFlexRemoteSearchExplorer.class, null);


        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

    public int showResult(FragmentManager fm, String fragment_edit_name) {

        // pause emuThread
        //MainActivity._emuThread.suspend();

        this.show(fm, fragment_edit_name);
        osd_refresh();

        //MainActivity._emuThread.resume();

        return 0;
    }
}


