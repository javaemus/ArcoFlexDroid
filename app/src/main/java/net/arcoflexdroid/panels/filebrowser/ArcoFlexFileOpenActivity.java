package net.arcoflexdroid.panels.filebrowser;

import android.os.Bundle;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTabHost;

import net.arcoflexdroid.R;
import net.arcoflexdroid.panels.filebrowser.remote.spectrum.ArcoFlexAsyncRemoteWOSExplorer;
import net.arcoflexdroid.panels.filebrowser.remote.spectrum.ArcoFlexRemoteTRDExplorer;

public class ArcoFlexFileOpenActivity extends FragmentActivity {

    private FragmentTabHost tabHost;
    //public static boolean _endSelection = false;
    //public static boolean _selectedFile = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //System.out.println("1");
        setContentView(R.layout.activity_arcoflex_file_open);
        //System.out.println("2");
        tabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        //System.out.println("3");
        tabHost.setup(this, getSupportFragmentManager(), android.R.id.tabcontent);
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
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finish();
    }

}
