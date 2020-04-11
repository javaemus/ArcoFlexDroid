package net.arcoflexdroid.panels.filebrowser;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.StringTokenizer;
import androidx.fragment.app.ListFragment;

import net.arcoflexdroid.panels.filebrowser.remote.spectrum.RemoteWOS;

import net.arcoflexdroid.R;

public class ArcoFlexRemoteSearchExplorer  extends ListFragment {
    public static String prefixWOS = "http://www.worldofspectrum.org/infoseekadv.cgi?what=1&yrorder=1&year=0&type=0&players=0&turns=0&memory=0&language=0&country=0&licence=0&feature=0&publi=0&release=0&format=0&scheme=0&scorder=1&score=0&have=1&also=1&sort=1&display=4&loadpics=0";
    public static String searchWOS = "&regexp=";

    public static String prefixWOSinlay = "http://www.worldofspectrum.org/pics/inlays";

    private TextView myPath;
    private Button myButton;

    private String currentDir;

    private ArcoFlexFileArrayAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

    }

    public static String getWOSInlay(String _file) throws Exception {
        String _inlay = null;

        //System.out.println("getWOSInlay!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! ");
        //URL[]dirs;

        String WOSfile = getWOSfile( _file );

        boolean found = false;

        String addr = prefixWOS + searchWOS + WOSfile;

        BufferedReader br = null;

        try {
            //System.out.println("Listing "+addr+"!!!!");

            URL url = new URL(addr);
            URLConnection con = url.openConnection();
            con.setRequestProperty("Accept-Encoding", "identity"); // no gzip
            InputStreamReader is = new InputStreamReader(con.getInputStream());

            //System.out.println("InputStreamReader "+is+"!!!!");

            br = new BufferedReader(is);
            String line = "";
            int posi = 0;

            //System.out.println("BufferedReader "+br+"!!!!");

            while ((line != null) && (!found)){
                try {

                    line = br.readLine();
                    //System.out.println("Read: " + line);


                } catch (Exception e) {
                    e.printStackTrace(System.out);
                    line = null;
                }

                //contents.append(line);

                //System.out.println(line);

                // buscamos el trozo
                String strIni = "";
                if (line != null) {
                    //System.out.println("Longo:"+line.length());
                    String tagToCut = "IMG SRC=\"/pics/inlays";
                    posi = line.indexOf(tagToCut);
                    //System.out.println("Posi 1:"+posi);
                    if (posi != -1) {
                        posi += tagToCut.length();
                        //System.out.println("Posi 2:"+posi);
                        line = line.substring(posi);
                        //System.out.println("Line:"+line);
                        posi = 0;
                        //System.out.println("Hasta:"+line.indexOf("\">"));
                        String nameObject = line.substring(posi, line.indexOf("\""));

                        _inlay = prefixWOSinlay + nameObject;
                        found = true;

                        //line = line.substring(posi, line.indexOf("\">"));


                        System.out.println("----->" + _inlay);

                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace(System.out);

        } finally {
            br = null;
        }



        return _inlay;
    }

    private static String getWOSfile(String file) {
        String result = file;
        StringTokenizer tok = new StringTokenizer(file, ".(");

        result = tok.nextToken();

        return result;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View myView = inflater.inflate(R.layout.fragment_search_tab, container, false);
        System.out.println("1-------------------------------------------------------------------");
        ArcoFlexFileItem i = null;
        System.out.println("2-------------------------------------------------------------------");
        //System.out.println(myView);
        myPath = (TextView) myView.findViewById(R.id.searchFileText);

        myButton = (Button) myView.findViewById(R.id.buttonSearch);
        System.out.println("3-------------------------------------------------------------------");
        myButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Click!");
                try {

                    EditText strSearch = (EditText) myView.findViewById(R.id.searchFileText);
                    System.out.println("Paso 2!");
                    System.out.println(strSearch);
                    //System.out.println(v);
                    System.out.println("Searching: "+strSearch.getText());
                    RemoteWOS wos = new RemoteWOS();
                    ArrayList arr = wos.searchWOS(String.valueOf(strSearch.getText()));

                    if (arr != null ){

                        int numResults=arr.size();
                        ArrayList finalArr = new ArrayList();

                        for (int i=0 ; i<numResults ; i++) {
                            ArcoFlexFileItem currItem = (ArcoFlexFileItem) arr.get(i);
                            String url_to_get_files = currItem.getName();
                            ArrayList arrFiles = wos.getFileWOS(currItem);

                            int numFiles = arrFiles.size();

                            for (int j=0 ; j<numFiles ; j++){
                                finalArr.add( arrFiles.get(j) );
                            }
                        }

                        adapter = new ArcoFlexFileArrayAdapter(
                                getContext(),
                                R.layout.fragment_file_explorer_body,
                                finalArr);

                        setListAdapter(adapter);
                    }

                } catch (Exception e) {
                    e.printStackTrace( System.out );
                }
            }});

        return myView;

    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        ArcoFlexFileItem o = adapter.getItem(position);
        if(o.getImage().equalsIgnoreCase("directory_icon")||o.getImage().equalsIgnoreCase("directory_up")){

        }
        else
        {
            currentDir = o.getPath();
            onFileClick(o);
        }
    }
    private void onFileClick(ArcoFlexFileItem o)
    {
        //Toast.makeText(this, "Folder Clicked: "+ currentDir, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent();
        intent.putExtra("GetPath",currentDir.toString());
        intent.putExtra("GetFileName",o.getName());
        //setResult(RESULT_OK, intent);
        //finish();

        try {
            ArrayList list = ArcoFlexZipFile.getContent( currentDir.toString() );
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }

        /*new AlertDialog.Builder(getContext())
                .setIcon(R.drawable.filexxhdpi)
                .setTitle("[" + currentDir.toString()+"/"+o.getName() + "]")
                .setPositiveButton("OK", null).show();*/


        //setResult(RESULT_OK, intent);
        //finish();
        ArcoFlexVars.file = o.getName();
        ArcoFlexVars.path = currentDir.toString();

        /*try {
            ArrayList list = jMESYSZipFile.getContent( "file://"+currentDir.toString()+"/"+o.getName() );
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }*/

        /*new AlertDialog.Builder(getContext())
                //.setIcon(R.drawable.ic_launcher)
                .setTitle("[" + o.getName() + "]")
                .setPositiveButton("OK", null).show();*/

        startActivityForResult(new Intent(getContext(), ArcoFlexFileCatalogDialog.class), 0);
        //setResult(RESULT_OK, intent);
    }
}

