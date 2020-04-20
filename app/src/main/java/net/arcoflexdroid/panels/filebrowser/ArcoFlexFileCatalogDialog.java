package net.arcoflexdroid.panels.filebrowser;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RadioGroup;

import net.arcoflexdroid.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ArcoFlexFileCatalogDialog extends ListActivity {

    /**
     * Prefix to the result string for fast-loading and running the selected file
     */
    public static final String LOADTYPE_FAST_LOAD_RUN = "fastLoadRun";
    /**
     * Prefix to the result string for normal loading of the selected file
     */
    public static final String LOADTYPE_NORMAL_LOAD = "load";
    /**
     * Prefix to the result string for fast-loading the selected file
     */
    public static final String LOADTYPE_FAST_LOAD = "fastLoad";
    /**
     * Currently displayed files
     */
    //private final List<String> currentFiles = new ArrayList<String>();
    private final List<String> currentFilesSTR = new ArrayList<String>();
    private final List<ArcoFlexFileItem>currentFiles = new ArrayList<ArcoFlexFileItem>();

    @SuppressWarnings("unchecked")
    @Override
    public void onCreate(final Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.loadcatalogfiledialog);



        // get all files from the C64 image
        //this.currentFiles.addAll((List) getIntent().getExtras().get("filesToShow"));
        try {

            // this.currentFiles.addAll((List) jMESYSZipFile.getContent( "file://"+getIntent().getExtras().getString("GetPath")+"/"+getIntent().getExtras().getString("GetFileName") ));
            //this.currentFiles.addAll((List) jMESYSZipFile.getContent( "file://"+ jMESYSVars.path ));
            String prefix = "";
            if ((ArcoFlexVars.path != null) && (!ArcoFlexVars.path.startsWith("http"))) {
                prefix = "file://";
            }

            if (ArcoFlexVars.path.toUpperCase().endsWith(".ZIP"))
                currentFiles.addAll((List) ArcoFlexZipFile.getContent( prefix+ ArcoFlexVars.path ));
            else { // single file
                ArrayList list = new ArrayList();
                ArcoFlexFileItem item = new ArcoFlexFileItem(ArcoFlexVars.file, "", "", prefix+ ArcoFlexVars.path, "file_icon");
                //list.add(ze.getName());
                list.add(item);
                currentFiles.addAll(list);
            }

            int numFiles = currentFiles.size();
            for (int i=0 ; i< numFiles ; i++){
                ArcoFlexFileItem currItem = currentFiles.get(i);
                currentFilesSTR.add( currItem.getName());
            }

        } catch (Exception e) {
            e.printStackTrace(System.out);
        }

        // display the files for selection
        ArrayAdapter<String> filenamesAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.listactivities_textview1, this.currentFilesSTR);
        //setListAdapter(new ArrayAdapter<String>(this, R.layout.listblack, R.id.list_content, this.currentFilesSTR));

        //jMESYSFileArrayAdapter filenamesAdapter = new jMESYSFileArrayAdapter(getApplicationContext(), R.layout.listactivities_textview1, this.currentFiles);
        // Change color
        //TextView textView=(TextView) findViewById(android.R.id.text1);
        //textView.setTextColor(Color.BLACK);

        setListAdapter(filenamesAdapter);
    }

    @Override
    protected void onListItemClick(final ListView l, final View v, final int position, final long id) {
        // return load-type + ':' + name of file to load as result
        final RadioGroup loadTypeRadio = (RadioGroup) findViewById(R.id.loadType);
        String loadType = null;

        switch (loadTypeRadio.getCheckedRadioButtonId()) {
            case R.id.fastLoad:
                loadType = LOADTYPE_FAST_LOAD;
                break;
            case R.id.load:
                loadType = LOADTYPE_NORMAL_LOAD;
                break;
            default:
                loadType = LOADTYPE_FAST_LOAD_RUN;
        }

        final Intent extras = new Intent();

        extras.putExtra("loadType", loadType);
        extras.putExtra("selectedFile", this.currentFiles.get(position).getName());

        //ArcoFlexFileOpenActivity._endSelection = true;
        //ArcoFlexJFileChooserDialog._selectedFile = true;
        ArcoFlexJFileChooserDialog._fSelected = new File(this.currentFiles.get(position).getName());
        System.out.println("*************SELECTED!:"+this.currentFiles.get(position).getPath());
        System.out.println("*************SELECTED2!:"+this.currentFiles.get(position).getName());

        setResult(RESULT_OK, extras);
        finish();
    }

    @Override
    public void onBackPressed() {
        //ArcoFlexFileOpenActivity._endSelection = false;
        //ArcoFlexJFileChooserDialog._selectedFile = false;
        super.onBackPressed();

        setResult(RESULT_CANCELED, null);
        finish();
    }
}

