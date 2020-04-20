package net.arcoflexdroid.panels.devices;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.fragment.app.ListFragment;

import net.arcoflexdroid.MainActivity;
import net.arcoflexdroid.R;
import net.arcoflexdroid.panels.filebrowser.ArcoFlexFileCatalogDialog;
import net.arcoflexdroid.panels.filebrowser.ArcoFlexFileItem;
import net.arcoflexdroid.panels.filebrowser.ArcoFlexJFileChooserDialog;
import net.arcoflexdroid.panels.filebrowser.ArcoFlexVars;
import net.arcoflexdroid.panels.filebrowser.ArcoFlexZipFile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import mess056.messH;

import static mess056.device.devices;
import static mess056.messH.*;

public class ArcoFlexConsoleFlexDevices extends ListFragment {

    public static IODevice[] dev;

    /**
     * Currently displayed files
     */
    private final List<IODevice>currentDevices = new ArrayList<IODevice>();
    private final List<String> currentDevicesSTR = new ArrayList<String>();

    @SuppressWarnings("unchecked")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View _myView = inflater.inflate(R.layout.listdevices_dialog, container, false);

        TextView mySystem = (TextView) _myView.findViewById(R.id.deviceSYS);
        mySystem.setText(ArcoFlexConfigConsoleFlexDriver._SystemName);

        // get all devices of ConsoleFlexDriver selected
        //this.currentFiles.addAll((List) getIntent().getExtras().get("filesToShow"));
        try {


            ArrayList list = new ArrayList();

            int _numDevices = dev.length;
            for (int _i=0 ; _i<_numDevices ; _i++) {
                if (dev[_i] != null) {

                    IODevice item = dev[_i];

                    System.out.println(">>>>>>>>>>>>>>>>>>>>>>Dispositivo #" + _i);
                    System.out.println(devices[item.type].name);
                    System.out.println(devices[item.type].shortname);
                    System.out.println(item.file_extensions);
                    System.out.println(">>>>>>>>>>>>>>>>>>>>>>FIN Dispositivo #" + _i);

                    // add item
                    if (item.type != 0) {
                        list.add(item);
                        currentDevicesSTR.add(devices[item.type].name);
                    }
                }
            }

            currentDevices.addAll(list);


            /*int numFiles = currentFiles.size();
            for (int i=0 ; i< numFiles ; i++){
                ArcoFlexFileItem currItem = currentFiles.get(i);
                currentFilesSTR.add( currItem.getName());
            }

             */

        } catch (Exception e) {
            e.printStackTrace(System.out);
        }

        // display the files for selection
        ArrayAdapter<String> filenamesAdapter = new ArrayAdapter<String>(MainActivity.mm.getApplicationContext(), R.layout.listactivities_textview1, this.currentDevicesSTR);
        //setListAdapter(new ArrayAdapter<String>(this, R.layout.listblack, R.id.list_content, this.currentFilesSTR));

        //jMESYSFileArrayAdapter filenamesAdapter = new jMESYSFileArrayAdapter(getApplicationContext(), R.layout.listactivities_textview1, this.currentFiles);
        // Change color
        //TextView textView=(TextView) findViewById(android.R.id.text1);
        //textView.setTextColor(Color.BLACK);

        setListAdapter(filenamesAdapter);

        return _myView;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {

        final Intent extras = new Intent();

        int _position = position;


        //ArcoFlexFileOpenActivity._endSelection = true;
        //ArcoFlexJFileChooserDialog._selectedFile = true;
        IODevice selDev = this.currentDevices.get(position);
        System.out.println("*************SELECTED!:"+devices[selDev.type].name);

        extras.putExtra("Device_Position", _position);
        //extras.putExtra("selectedFile", this.currentFiles.get(position).getName());

        //setResult(RESULT_OK, extras);
        //this.close();

        //ArcoFlexConfigConsoleFlexDriver.setResult(0);
        //startActivityForResult(new Intent(getContext(), MainActivity.class), 0);
    }

}


