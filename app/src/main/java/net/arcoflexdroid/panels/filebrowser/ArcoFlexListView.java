package net.arcoflexdroid.panels.filebrowser;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.sql.Date;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import androidx.fragment.app.ListFragment;

import net.arcoflexdroid.R;

import static arcoflex056.platform.android.android_filemngrClass.currDir;


public class ArcoFlexListView  extends ListFragment {
    private List<String> item = null;
    private List<String> path = null;
    private String root;
    private TextView myPath;
    private ImageView favoriteImg;
    private View myView;

    private ArcoFlexFileArrayAdapter adapter;
    //public static File currentDir=null;

    public ArcoFlexListView(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //myView = inflater.inflate(R.layout.fragment_file_explorer_body, container, false);
        myView = inflater.inflate(R.layout.rowlist, container, false);
        System.out.println("-------------------------------------------------------------------");

        System.out.println(myView);

        myPath = (TextView)myView.findViewById(R.id.path);

        favoriteImg = (ImageView) myView.findViewById(R.id.file_add_favorites);

        /*favoriteImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(),
                        "Position: "+favoriteImg.getTag(),
                        Toast.LENGTH_LONG).show();
            }
        });*/

        //root = Environment.getExternalStorageDirectory().getPath();

        root="/";
        if (currDir == null)
            currDir = new File("/");

        //getDir(root);
        fill(new File(currDir.getAbsolutePath()));

        //setListAdapter(adapter);
        return myView;

    }


/*
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

 */

    private void fill(File f)
    {
        File[]dirs = f.listFiles();
        //this.setTitle("Current Dir: "+f.getName());
        myPath.setText("Location: " + f.getPath());
        List<ArcoFlexFileItem>dir = new ArrayList<ArcoFlexFileItem>();
        List<ArcoFlexFileItem>fls = new ArrayList<ArcoFlexFileItem>();
        try{
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
                    fls.add(new ArcoFlexFileItem(ff.getName(),ff.length() + " Byte", date_modify, currDir.getAbsolutePath(),"file_icon"));
                }
            }
        }catch(Exception e)
        {

        }
        Collections.sort(dir);
        Collections.sort(fls);
        dir.addAll(fls);
        if(f.getName().length() != 0)
            dir.add(0,new ArcoFlexFileItem("..","Parent Directory","",f.getParent(),"directory_up"));
        adapter = new ArcoFlexFileArrayAdapter(getContext(),R.layout.fragment_file_explorer_body,dir);

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
        ArcoFlexFileItem o = adapter.getItem(position);
        if(o.getImage().equalsIgnoreCase("directory_icon")||o.getImage().equalsIgnoreCase("directory_up")){
            currDir = new File(o.getPath());
            fill(currDir);
        }
        else
        {

            //currDir = new File(o.getPath());
            onFileClick(o);
        }
    }

    private void onFileClick(ArcoFlexFileItem o)
    {
        //Toast.makeText(this, "Folder Clicked: "+ currentDir, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent();
        intent.putExtra("GetPath",o.getPath());
        intent.putExtra("GetFileName",o.getName());
        //setResult(RESULT_OK, intent);
        //finish();
        ArcoFlexVars.file = o.getName();
        ArcoFlexVars.path = o.getPath();
        currDir = new File(ArcoFlexVars.path);

        System.out.println("-------------------------Seleccionado!!!!: "+ArcoFlexVars.path);
        System.out.println("-------------------------Seleccionadoxxx!!!!: "+ArcoFlexVars.file);

        /*try {
            ArrayList list = jMESYSZipFile.getContent( "file://"+currentDir.toString()+"/"+o.getName() );
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }

        new AlertDialog.Builder(getContext())
                //.setIcon(R.drawable.ic_launcher)
                .setTitle("[" + o.getName() + "]")
                .setPositiveButton("OK", null).show();*/

        startActivityForResult(new Intent(getContext(), ArcoFlexFileCatalogDialog.class), 0);
    }
}
