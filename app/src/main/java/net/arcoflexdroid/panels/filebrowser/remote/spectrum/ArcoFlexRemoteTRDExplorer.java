package net.arcoflexdroid.panels.filebrowser.remote.spectrum;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.ListFragment;

import net.arcoflexdroid.R;
import net.arcoflexdroid.panels.filebrowser.ArcoFlexFileArrayAdapter;
import net.arcoflexdroid.panels.filebrowser.ArcoFlexFileCatalogDialog;
import net.arcoflexdroid.panels.filebrowser.ArcoFlexFileItem;
import net.arcoflexdroid.panels.filebrowser.ArcoFlexVars;
import net.arcoflexdroid.panels.filebrowser.ArcoFlexZipFile;
import net.arcoflexdroid.panels.filebrowser.ProgressDialogFragment;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ArcoFlexRemoteTRDExplorer extends ListFragment {

    protected String site="https://vtrd.in";
    protected String root="/games.php";

    private List<String> item = null;
    private List<String> path = null;

    private Pattern _section = Pattern.compile("<a href=\\\"(.+)\\\">&nbsp;&nbsp;(.+)</a></td><td>(.+)</td><td>(.+)</td><td>(.+)</td>");

    protected String getSite() {
        //System.out.println("Obtengo SITE!!!!");
        //getPattern();
        return site;
    }

    private void getPattern() {
        BufferedReader br = null;
        String addr = "https://vtrd.in/games.php?t=a";
        //private static Pattern _section = Pattern.compile("\\s*\\[([^]]*)\\]\\s*");
        //Pattern _section = Pattern.compile("<a href=\"(.+)\">&nbsp;&nbsp;(.+)</a></td><td>(.+)</td><td>(.+)</td><td>(.+)</td>");
        Pattern _section = Pattern.compile("<a href=\\\"(.+)\\\">&nbsp;&nbsp;(.+)</a></td><td>(.+)</td><td>(.+)</td><td>(.+)</td>");

        try {
            //System.out.println("Listing "+addr+"!!!!");

            URL url = new URL(addr);
            URLConnection con = url.openConnection();
            con.setRequestProperty("Accept-Encoding", "identity"); // no gzip
            InputStreamReader is = new InputStreamReader(con.getInputStream());

            //System.out.println("InputStreamReader "+is+"!!!!");

            br = new BufferedReader(is);
            String line = "";
            String section = null;
            String firm =null;
            String trdVersion = null;
            int posi = 0;

            //System.out.println("BufferedReader "+br+"!!!!");

            while (line != null) {
                try {

                    line = br.readLine();
                    System.out.println("Read: "+line);

                    if (line != null) {
                        Matcher m = _section.matcher(line);
                        if (m.find()) {
                            System.out.println("Match!");
                            section = m.group(1).trim();
                            System.out.println("Fichero: " + section);
                            firm = m.group(2).trim();
                            System.out.println("Firm: " + firm);
                            trdVersion = m.group(3).trim();
                            System.out.println("TRDVersion: " + trdVersion);

                        } else if (section != null) {
                            System.out.println("Section2: " + section);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace(System.out);
                    line = null;
                }


            }
        } catch (Exception e){
            e.printStackTrace(System.out);
        } finally {
            br = null;
        }
    }

    private TextView myPath;
    private View myView;

    //private ProgressDialog progress;

    private ArcoFlexFileArrayAdapter adapter;
    private String currentDir;
    private boolean isLoading=false;

    protected String getRoot() {
        return root;
    }

    private ProgressDialog pDialog;
    public static final int progress_bar_type = 0;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //System.out.println("------------------------------------------------------------------- onCreateView");
        //progress = new jMESYSProgressDialog(getActivity());

        //myView = inflater.inflate(R.layout.fragment_file_explorer_body, container, false);
        myView = inflater.inflate(R.layout.rowlist, container, false);
        //System.out.println("-------------------------------------------------------------------");

        //System.out.println(myView);

        myPath = (TextView) myView.findViewById(R.id.path);

        //root = Environment.getExternalStorageDirectory().getPath();


        //getDir(root);

        //fill(getSite()+getRoot());
        //new jMESYSAsyncRemoteWOSExplorer.jMESYSSearchTask(this, getSite()+getRoot()).execute();

        List<ArcoFlexFileItem>dir = new ArrayList<ArcoFlexFileItem>();

        //Object result = new DownloadFileFromURL().execute(getSite()+getRoot());
        myPath.setText("Location: " + getSite()+getRoot());

        createTRDrootDIR();

        //new ArcoFlexRemoteTRDExplorer.DownloadFileFromURL().execute(getSite()+getRoot());

        //System.out.println(result.getClass().getName());

        //dir = (List) new DownloadFileFromURL().execute(getSite()+getRoot());

        //System.out.println("Creo adaptador");

        /*adapter = new jMESYSFileArrayAdapter(
                getContext(),
                R.layout.fragment_file_explorer_body,
                dir);
*/
        //System.out.println("FIN Creo adaptador");

        //this.setListAdapter(adapter);

        //System.out.println("SET adaptador");

        //setListAdapter(adapter);
        return myView;

    }

    private void createTRDrootDIR() {
        List<ArcoFlexFileItem>dir = new ArrayList<ArcoFlexFileItem>();
        List<ArcoFlexFileItem>fls = new ArrayList<ArcoFlexFileItem>();

        dir.add(new ArcoFlexFileItem("Russian", "? items", "", getSite()+getRoot() + "?t=full_ver", "directory_icon"));
        dir.add(new ArcoFlexFileItem("Demo", "? items", "", getSite()+getRoot() + "?t=demo_ver", "directory_icon"));
        dir.add(new ArcoFlexFileItem("Translate", "? items", "", getSite()+getRoot() + "?t=translat", "directory_icon"));
        dir.add(new ArcoFlexFileItem("Remix", "? items", "", getSite()+getRoot() + "?t=remix", "directory_icon"));
        dir.add(new ArcoFlexFileItem("123", "? items", "", getSite()+getRoot() + "?t=123", "directory_icon"));

        for (char i = 'a'; i <= 'z'; ++i) {
            dir.add(new ArcoFlexFileItem(new Character(i).toString(), "? items", "", getSite()+getRoot() + "?t="+new Character(i).toString(), "directory_icon"));
        }

        // Collections.sort(dir);

        adapter = new ArcoFlexFileArrayAdapter(
                getContext(),
                R.layout.fragment_file_explorer_body,
                dir);

        setListAdapter(adapter);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        //System.out.println("------------------------------------------------------------------- onCreate");
        //progress = new ProgressDialog(getActivity());
    }


    /**
     * Background Async Task to download file
     * */
    public class DownloadFileFromURL extends AsyncTask<String, String, List> {
        private DialogFragment mProgressFragment;
        private static final String PROGRESS_DIALOG_TAG = "ProcessProgressDialog";
        /**
         * Before starting background thread
         * Show Progress Bar Dialog
         * */
        public void showProgress()
        {
            if (mProgressFragment == null)
            {
                mProgressFragment =  new ProgressDialogFragment(getActivity(),
                        "Downloading. Please wait...", null);
            }
            mProgressFragment.setCancelable(false);
            mProgressFragment.show(getFragmentManager(), PROGRESS_DIALOG_TAG);
        }

        public void dismissProgress()
        {
            if (mProgressFragment != null)
            {
                if (mProgressFragment.isAdded())
                {
                    mProgressFragment.dismissAllowingStateLoss();
                }
                mProgressFragment = null;
            }
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgress();
        }

        private String _parentDIR="";
        /**
         * Downloading file in background thread
         * */
        @Override
        protected List<ArcoFlexFileItem> doInBackground(String... f_url) {
            //System.out.println("doInBackground");

            int count;
            BufferedReader br = null;

            boolean rootFound = false;

            List<ArcoFlexFileItem>dir = new ArrayList<ArcoFlexFileItem>();
            List<ArcoFlexFileItem>fls = new ArrayList<ArcoFlexFileItem>();

            try {
                //System.out.println("Ruta: "+f_url[0]);
                URL url = new URL(f_url[0]);
                URLConnection conection = url.openConnection();
                //conection.connect();
                // getting file length
                //int lenghtOfFile = conection.getContentLength();
                //System.out.println("Tamaño: "+lenghtOfFile);

                if (true){
                    //if (lenghtOfFile>0) {
                    // input stream to read file - with 8k buffer
                    //InputStreamReader input = new BufferedInputStream(url.openStream(), 8192);
                    //System.out.println("InputStreamReader "+is+"!!!!");
                    conection.setRequestProperty("Accept-Encoding", "identity"); // no gzip
                    InputStreamReader is = new InputStreamReader(conection.getInputStream());
                    br = new BufferedReader(is);
                    String line = "";
                    int posi = 0;

                    dir.add(0, new ArcoFlexFileItem("..", "Parent Directory", "", getSite(), "directory_up"));

                    while (line != null) {
                        try {
                            line = br.readLine();
                            //System.out.println(line);
                            //analHTMLLine( line, posi );

                        } catch (Exception e) {
                            e.printStackTrace(System.out);
                            line = null;
                        }

                        String section = "";
                        String firm = "";
                        String trdVersion = "";

                        if (line != null) {

                            //System.out.println("Read: "+line);

                            if (line != null) {
                                Matcher m = _section.matcher(line);
                                if (m.find()) {
                                    //System.out.println("Match!");
                                    section = m.group(1).trim();
                                    //System.out.println("Fichero: " + section);
                                    firm = m.group(2).trim();
                                    //System.out.println("Firm: " + firm);
                                    trdVersion = m.group(3).trim();
                                    //System.out.println("TRDVersion: " + trdVersion);

                                    trdVersion = trdVersion.replaceAll("</td><td>", " ");

                                } else if (section != null) {
                                    System.out.println("Section2: " + section);
                                }
                            }


                                if (section.endsWith("/")) { // es un directorio
                                    //dir.add(new Item(ff.getName(),num_item,date_modify,ff.getAbsolutePath(),"directory_icon"));
                                    if(!rootFound) { // parent directory
                                        dir.add(0, new ArcoFlexFileItem("..", "Parent Directory", "", getSite()+section, "directory_up"));
                                        //System.out.println("Directorio UP: "+getSite()+nameObject);
                                        rootFound=true;

                                    } else {
                                        dir.add(new ArcoFlexFileItem(section, "? items", firm+" / "+trdVersion, f_url[0] + section, "directory_icon"));
                                        //System.out.println("Directorio: "+f_url[0]+nameObject);
                                    }
                                } else { // es un fichero
                                    if ((section.length()!=0))
                                        fls.add(new ArcoFlexFileItem(firm,"", trdVersion, site+section,"file_icon"));
                                    //System.out.println("Fichero: "+f_url[0]+"/"+nameObject);


                                }


                        }
                    }
                }
            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            } finally {
                close(br);
            }

            //System.out.println("Fin carga datos");

            //Collections.sort(dir);
            //Collections.sort(fls);
            dir.addAll(fls);
        /*if(f.getName().length() != 0)
            dir.add(0,new Item("..","Parent Directory","",f.getParent(),"directory_up"));*/

            //System.out.println(dir);

            //System.out.println("Fin asíncrono");

            return dir;
        }

        private void close(Reader br)
        {
            try
            {
                if (br != null)
                {
                    br.close();
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        /**
         * Updating progress bar
         * */
        protected void onProgressUpdate(String... progress) {
            // setting progress percentage
            //mProgressFragment.setProgress(<total progress >, progress[0], "<units> example KB or MB");
        }
        /**
         * After completing background task
         * Dismiss the progress dialog
         * **/
        @Override
        protected void onPostExecute(List file_url) {
            //System.out.println("onPostExecute");
            // dismiss the dialog after the file was downloaded

            //System.out.println("RESULT: "+file_url);

            adapter = new ArcoFlexFileArrayAdapter(
                    getContext(),
                    R.layout.fragment_file_explorer_body,
                    file_url);

            setListAdapter(adapter);

            dismissProgress();
            // Displaying downloaded image into image view
            // Reading image path from sdcard
            //String imagePath = Environment.getExternalStorageDirectory().toString() + "/downloadedfile.jpg";
            // setting downloaded into image view
            //dlimg.setImageDrawable(Drawable.createFromPath(imagePath));
            //System.out.println("FIN onPostExecute");
        }

    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        ArcoFlexFileItem o = adapter.getItem(position);
        if(o.getImage().equalsIgnoreCase("directory_icon")||o.getImage().equalsIgnoreCase("directory_up")){
            //currentDir = new File(o.getPath());
            currentDir = o.getPath();
            //fill(currentDir);
            myPath.setText("Location: " + currentDir);

            if (currentDir.equals(getSite())){
                createTRDrootDIR();
            } else {
                new ArcoFlexRemoteTRDExplorer.DownloadFileFromURL().execute(currentDir);
            }
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
        System.out.println("GetPath"+currentDir.toString());
        System.out.println("GetFileName"+o.getName());
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


