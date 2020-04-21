package net.arcoflexdroid.panels.devices;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
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
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import mess056.messH;

import static arcoflex056.platform.android.android_filemngrClass.currDir;
import static mess056.device.devices;
import static mess056.messH.*;

public class ArcoFlexConsoleFlexDevices extends ListFragment {
    private List<String> item = null;
    private List<String> path = null;
    private String root;
    private TextView myPath;
    private ImageView favoriteImg;
    private View myView;

    private ArcoFlexDeviceArrayAdapter adapter;
    public static IODevice[] dev;

    public ArcoFlexConsoleFlexDevices(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //myView = inflater.inflate(R.layout.fragment_file_explorer_body, container, false);
        myView = inflater.inflate(R.layout.rowlist, container, false);

        myPath = (TextView)myView.findViewById(R.id.path);

        favoriteImg = (ImageView) myView.findViewById(R.id.file_add_favorites);

        // fills content
        fill(ArcoFlexConfigConsoleFlexDriver._SystemName);

        //setListAdapter(adapter);
        return myView;

    }


/*
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

 */

    private void fill(String _sysName)
    {

        //this.setTitle("Current Dir: "+f.getName());
        myPath.setText("System: " + _sysName);

        List<ArcoFlexDeviceItem>fls = new ArrayList<ArcoFlexDeviceItem>();

        try{
            //ArrayList list = new ArrayList();

            int _numDevices = dev.length;
            for (int _i=0 ; _i<_numDevices ; _i++) {
                if (dev[_i] != null) {

                    ArcoFlexDeviceItem item = new ArcoFlexDeviceItem(
                            devices[dev[_i].type].name,
                            _i,
                            dev[_i].type,
                            dev[_i].file_extensions);

                    System.out.println(">>>>>>>>>>>>>>>>>>>>>>Dispositivo #" + _i);
                    System.out.println(devices[dev[_i].type].name);
                    System.out.println(devices[dev[_i].type].shortname);
                    System.out.println(dev[_i].file_extensions);
                    System.out.println(">>>>>>>>>>>>>>>>>>>>>>FIN Dispositivo #" + _i);

                    // add item
                    if (item.getType() != 0) {
                        fls.add(item);
                        //currentDevicesSTR.add(devices[item.type].name);
                    }
                }
            }

            /*currentDevices.addAll(list);
            for(File ff: dirs)
            {
                Date lastModDate = new Date(ff.lastModified());
                DateFormat formater = DateFormat.getDateTimeInstance();
                String date_modify = formater.format(lastModDate);
                if(ff.isDirectory()){


                    File[] fbuf = ff.listFiles();
                    int buf = 0;
                    if(fbuf != null){
                        buf = fbuf.length;
                    }
                    else buf = 0;
                    String num_item = String.valueOf(buf);
                    if(buf == 0) num_item = num_item + " item";
                    else num_item = num_item + " items";

                    //String formated = lastModDate.toString();
                    dir.add(new ArcoFlexFileItem(ff.getName(),num_item,date_modify,ff.getPath(),"directory_icon"));
                }
                else
                {
                    fls.add(new ArcoFlexDeviceItem(ff.getName(),ff.length() + " Byte", date_modify, currDir.getAbsolutePath(),"file_icon"));
                }
            }

             */
        }catch(Exception e)
        {

        }

        //Collections.sort(fls);

        //if(f.getName().length() != 0)
        //    fls.add(0,new ArcoFlexFileItem("..","Parent Directory","",f.getParent(),"directory_up"));
        adapter = new ArcoFlexDeviceArrayAdapter(getContext(),R.layout.fragment_device_list_body,fls);

        setListAdapter(adapter);
    }


    private void getDir(String dirPath)
    {
        myPath.setText("Location: " + dirPath);
        currDir = new File(dirPath);
        item = new ArrayList<String>();
        path = new ArrayList<String>();
        File f = new File(dirPath);
        //File f=Environment.getExternalStorageDirectory();
        File[] files = f.listFiles();

        if(!dirPath.equals(root))
        {
            item.add(root);
            path.add(root);
            item.add("../");
            path.add(f.getParent());
        }

        for(int i=0; i < files.length; i++)
        {
            File file = files[i];

            //if(!file.isHidden() && file.canRead()){
            path.add(file.getPath());
            if(file.isDirectory()){
                item.add(file.getName() + "/");
            }else{
                item.add(file.getName());
            }
            //}

            System.out.println("------------------------------------------");
            System.out.println(file.getName());
            System.out.println(file.getPath());
            System.out.println(file.getAbsoluteFile().getAbsolutePath());
            System.out.println("------------------------------------------");
        }

        ArrayAdapter<String> fileList = new ArrayAdapter<String>(getContext(), R.layout.row, item);
        //jMESYSFileArrayAdapter fileList = new jMESYSFileArrayAdapter<String>(getContext(), R.layout.row, item);

        setListAdapter(fileList);

    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        ArcoFlexDeviceItem o = adapter.getItem(position);
        /*if(o.getImage().equalsIgnoreCase("directory_icon")||o.getImage().equalsIgnoreCase("directory_up")){
            currDir = new File(o.getPath());
            fill(currDir);
        }
        else
        {

            //currDir = new File(o.getPath());
            onFileClick(o);
        }

         */
    }

    private void onFileClick(ArcoFlexFileItem o)
    {
        //Toast.makeText(this, "Folder Clicked: "+ currentDir, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent();
        intent.putExtra("GetPath",o.getPath());
        intent.putExtra("GetFileName",o.getName());

        ArcoFlexVars.file = o.getName();
        ArcoFlexVars.path = o.getPath();
        currDir = new File(ArcoFlexVars.path);

        System.out.println("-------------------------Seleccionado!!!!: "+ArcoFlexVars.path);
        System.out.println("-------------------------Seleccionadoxxx!!!!: "+ArcoFlexVars.file);
        ArcoFlexJFileChooserDialog.setResult(0);

        /*try {
            ArrayList list = jMESYSZipFile.getContent( "file://"+currentDir.toString()+"/"+o.getName() );
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }

        new AlertDialog.Builder(getContext())
                //.setIcon(R.drawable.ic_launcher)
                .setTitle("[" + o.getName() + "]")
                .setPositiveButton("OK", null).show();*/

        //startActivityForResult(new Intent(getContext(), ArcoFlexFileCatalogDialog.class), 0);
    }
}
