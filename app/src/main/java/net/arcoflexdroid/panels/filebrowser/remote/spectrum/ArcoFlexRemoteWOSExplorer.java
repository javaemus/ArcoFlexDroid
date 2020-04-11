package net.arcoflexdroid.panels.filebrowser.remote.spectrum;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.ListFragment;

import net.arcoflexdroid.R;
import net.arcoflexdroid.panels.filebrowser.ArcoFlexFileArrayAdapter;
import net.arcoflexdroid.panels.filebrowser.ArcoFlexFileCatalogDialog;
import net.arcoflexdroid.panels.filebrowser.ArcoFlexFileItem;
import net.arcoflexdroid.panels.filebrowser.ArcoFlexVars;
import net.arcoflexdroid.panels.filebrowser.ArcoFlexZipFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ArcoFlexRemoteWOSExplorer  extends ListFragment {
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
        new ArcoFlexSearchTask(this, getSite()+getRoot()).execute();

        //setListAdapter(adapter);
        return myView;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        //System.out.println("------------------------------------------------------------------- onCreate");
        //progress = new ProgressDialog(getActivity());
    }




    private static void close(Reader br) {
        //System.out.println("------------------------------------------------------------------- Close");
        try {
            if (br != null) {
                br.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        ArcoFlexFileItem o = adapter.getItem(position);
        if (o.getImage().equalsIgnoreCase("directory_icon") || o.getImage().equalsIgnoreCase("directory_up")) {
            //currentDir = new File(o.getPath());
            currentDir = o.getPath();
            //fill(currentDir);
            new ArcoFlexSearchTask(this, currentDir).execute();
        } else {
            currentDir = o.getPath();
            onFileClick(o);
        }
    }

    private void onFileClick(ArcoFlexFileItem o) {
        //Toast.makeText(this, "Folder Clicked: "+ currentDir, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent();
        intent.putExtra("GetPath", currentDir.toString());
        intent.putExtra("GetFileName", o.getName());
        //setResult(RESULT_OK, intent);
        //finish();

        try {
            ArrayList list = ArcoFlexZipFile.getContent(currentDir.toString());
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

    private class ArcoFlexSearchTask extends AsyncTask<String, Void, Integer> {

        ProgressDialog mDialog;
        String _uri = null;
        ArcoFlexRemoteWOSExplorer _jmesysremotewosexplorer = null;


        ArcoFlexSearchTask(ArcoFlexRemoteWOSExplorer jmesysremotewosexplorer, String uri) {
            _uri = uri;
            _jmesysremotewosexplorer = jmesysremotewosexplorer;
        }


        @Override
        protected void onCancelled() {
            _jmesysremotewosexplorer.isLoading = false;
        }


        protected void onPreExecute() {
            super.onPreExecute();
            //System.out.println("onPreExecute");
            mDialog = new ProgressDialog(getContext());
            mDialog.setTitle("Please Wait");
            mDialog.setMessage("Loading...");
            mDialog.setCancelable(true);
            mDialog.setIndeterminate(true);

            mDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    //theLayout.setVisibility(View.GONE);
                }
            });



            //fill(_uri);

            //mDialog = ProgressDialog.show(getActivity(), "Please Wait", "Loading...");

            // TIMEOUT
            /*Runnable progressRunnable = new Runnable() {

                @Override
                public void run() {
                    if (!isLoading) {
                        System.out.println("TIMEOUT!!!!");
                        //mDialog.cancel();
                        //mDialog.dismiss();
                        mDialog.hide();
                    }
                }
            };

            Handler pdCanceller = new Handler();
            pdCanceller.postDelayed(progressRunnable, 3000);
            */
        }

        /*mDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                theLayout.setVisibility(View.GONE);
            }
        });*/



        protected Integer doInBackground(String... params) {
            //System.out.println("doInBackground");
            //mDialog.show();
            //return mSearchService.fetchSearchResults(mSearchQuery);
            fill(_uri);

            return 1;
        }

        protected void onPostExecute(Integer status) {
            //System.out.println("onPostExecute");
            //mDialog.dismiss();
            //mDialog.cancel();

            mDialog.hide();
        }

        private void fill(String addr) {
            _jmesysremotewosexplorer.fill(addr);
            mDialog.hide();
            //System.out.println("END FILL");
        }
    }

    private void fill(String addr) {
        //System.out.println("FILL!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        //URL[]dirs;

        myPath.setText("Location: " + addr);

        //progress.showProgress();

        boolean rootFound = false;

        isLoading = false;

        List<ArcoFlexFileItem> dir = new ArrayList<ArcoFlexFileItem>();
        List<ArcoFlexFileItem> fls = new ArrayList<ArcoFlexFileItem>();

        try {
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

                while (line != null) {
                    try {

                        line = br.readLine();
                        //System.out.println("Read: "+line);
                        isLoading = true;

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
                        String tagToCut = "<td><a href=\"";
                        posi = line.indexOf(tagToCut);
                        //System.out.println("Posi 1:"+posi);
                        if (posi != -1) {
                            posi += tagToCut.length();
                            //System.out.println("Posi 2:"+posi);
                            line = line.substring(posi);
                            //System.out.println("Line:"+line);
                            posi = 0;
                            //System.out.println("Hasta:"+line.indexOf("\">"));
                            String nameObject = line.substring(posi, line.indexOf("\">"));

                            //line = line.substring(posi, line.indexOf("\">"));


                            //System.out.println("----->"+line);


                            //System.out.println("----->"+line);
                            String date_modify = "no date";

                            tagToCut = "<td align=\"right\">";
                            posi = line.indexOf(tagToCut);
                            posi += tagToCut.length();
                            //System.out.println("Posi 2:"+posi);
                            line = line.substring(posi);
                            //System.out.println("Line:"+line);
                            posi = 0;
                            //System.out.println("Hasta:"+line.indexOf("\">"));
                            //String nameObject=line.substring(posi, line.indexOf("\">"));
                            date_modify = line.substring(posi, line.indexOf("</td>"));

                            String numBytes = "?";
                            tagToCut = "<td align=\"right\">";
                            posi = line.indexOf(tagToCut);
                            posi += tagToCut.length();
                            //System.out.println("Posi 2:"+posi);
                            line = line.substring(posi);
                            //System.out.println("Line:"+line);
                            posi = 0;
                            //System.out.println("Hasta:"+line.indexOf("\">"));
                            //String nameObject=line.substring(posi, line.indexOf("\">"));
                            numBytes = line.substring(posi, line.indexOf("</td>"));


                            if (nameObject.endsWith("/")) { // es un directorio
                                //dir.add(new Item(ff.getName(),num_item,date_modify,ff.getAbsolutePath(),"directory_icon"));
                                if (!rootFound) { // parent directory
                                    dir.add(0, new ArcoFlexFileItem("..", "Parent Directory", "", getSite() + nameObject, "directory_up"));

                                    rootFound = true;

                                } else {
                                    dir.add(new ArcoFlexFileItem(nameObject, "? items", date_modify, addr + nameObject, "directory_icon"));
                                }
                            } else { // es un fichero
                                ArcoFlexFileItem fileWOS = new ArcoFlexFileItem(nameObject, numBytes + " Byte", date_modify, addr + "/" + nameObject, "file_icon");



                                fls.add( fileWOS );
                            }

                        }
                    }
                }
            } finally {
                close(br);
            }

        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
        Collections.sort(dir);
        Collections.sort(fls);
        dir.addAll(fls);
        /*if(f.getName().length() != 0)
            dir.add(0,new Item("..","Parent Directory","",f.getParent(),"directory_up"));*/
        adapter = new ArcoFlexFileArrayAdapter(getContext(), R.layout.fragment_file_explorer_body, dir);
        setListAdapter(adapter);
    }


}


