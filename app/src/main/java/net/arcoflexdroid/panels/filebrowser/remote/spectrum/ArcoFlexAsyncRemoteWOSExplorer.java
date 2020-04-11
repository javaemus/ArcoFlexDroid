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

public class ArcoFlexAsyncRemoteWOSExplorer extends ListFragment {
    private List<String> item = null;
    private List<String> path = null;

    protected String site = "http://www.worldofspectrum.org";
    //protected String site = "file://";
    protected String root = "/pub/sinclair/games/";
    //protected String root = "/";

    private TextView myPath;
    private View myView;

    //private ProgressDialog progress;

    private ArcoFlexFileArrayAdapter adapter;
    private String currentDir;
    private boolean isLoading=false;

    protected String getSite() {
        return site;
    }

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
        new DownloadFileFromURL().execute(getSite()+getRoot());

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
                    // Output stream to write file
                    /*OutputStream output = new FileOutputStream("/sdcard/downloadedfile.jpg");
                    byte data[] = new byte[1024];
                    long total = 0;
                    while ((count = input.read(data)) != -1) {
                        total += count;
                        // publishing the progress....
                        // After this onProgressUpdate will be called
                        publishProgress("" + (int) ((total * 100) / lenghtOfFile));
                        // writing data to file
                        //output.write(data, 0, count);
                    }
// flushing output
                    output.flush();
                    // closing streams
                    output.close();
                    input.close();
                    */
                    while (line != null) {
                        try {
                            line = br.readLine();
                            //System.out.println(line);
                            //analHTMLLine( line, posi );

                        } catch (Exception e) {
                            e.printStackTrace(System.out);
                            line = null;
                        }

                        // buscamos el trozo
                        String strIni = "";
                        if (line != null) {
                            //System.out.println("Longo:"+line.length());
                            String tagToCut="<td><a href=\"";
                            posi = line.indexOf( tagToCut );
                            //System.out.println("Posi 1:"+posi);
                            if (posi != -1){
                                posi += tagToCut.length();
                                //System.out.println("Posi 2:"+posi);
                                line = line.substring(posi);
                                //System.out.println("Line:"+line);
                                posi=0;
                                //System.out.println("Hasta:"+line.indexOf("\">"));
                                String nameObject=line.substring(posi, line.indexOf("\">"));

                                //line = line.substring(posi, line.indexOf("\">"));


                                //System.out.println("----->"+line);


                                //System.out.println("----->"+line);
                                String date_modify="no date";

                                tagToCut="<td align=\"right\">";
                                posi = line.indexOf( tagToCut );
                                posi += tagToCut.length();
                                //System.out.println("Posi 2:"+posi);
                                line = line.substring(posi);
                                //System.out.println("Line:"+line);
                                posi=0;
                                //System.out.println("Hasta:"+line.indexOf("\">"));
                                //String nameObject=line.substring(posi, line.indexOf("\">"));
                                date_modify = line.substring(posi, line.indexOf("</td>"));

                                String numBytes = "?";
                                tagToCut="<td align=\"right\">";
                                posi = line.indexOf( tagToCut );
                                posi += tagToCut.length();
                                //System.out.println("Posi 2:"+posi);
                                line = line.substring(posi);
                                //System.out.println("Line:"+line);
                                posi=0;
                                //System.out.println("Hasta:"+line.indexOf("\">"));
                                //String nameObject=line.substring(posi, line.indexOf("\">"));
                                numBytes = line.substring(posi, line.indexOf("</td>"));


                                if (nameObject.endsWith("/")) { // es un directorio
                                    //dir.add(new Item(ff.getName(),num_item,date_modify,ff.getAbsolutePath(),"directory_icon"));
                                    if(!rootFound) { // parent directory
                                        dir.add(0, new ArcoFlexFileItem("..", "Parent Directory", "", getSite()+nameObject, "directory_up"));
                                        //System.out.println("Directorio UP: "+getSite()+nameObject);
                                        rootFound=true;

                                    } else {
                                        dir.add(new ArcoFlexFileItem(nameObject, "? items", date_modify, f_url[0] + nameObject, "directory_icon"));
                                        //System.out.println("Directorio: "+f_url[0]+nameObject);
                                    }
                                } else { // es un fichero
                                    fls.add(new ArcoFlexFileItem(nameObject,numBytes + " Byte", date_modify, f_url[0]+"/"+nameObject,"file_icon"));
                                    //System.out.println("Fichero: "+f_url[0]+"/"+nameObject);


                                }

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

            Collections.sort(dir);
            Collections.sort(fls);
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
            new DownloadFileFromURL().execute(currentDir);
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


