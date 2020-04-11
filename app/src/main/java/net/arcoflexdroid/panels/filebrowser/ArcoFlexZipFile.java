package net.arcoflexdroid.panels.filebrowser;

import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ArcoFlexZipFile  {

    public static ArrayList getContent(String strFile) throws Exception {
        ArrayList list = new ArrayList();

        if (strFile.toLowerCase().endsWith(".zip")) {

            URL url = new URL(strFile);

            System.out.println("Accesing "+strFile);

            URLConnection con = url.openConnection();
            con.setRequestProperty("Accept-Encoding", "identity"); // no gzip

            ZipInputStream inputstream = new ZipInputStream(con.getInputStream());

            ZipEntry ze = null;
            boolean end = false;

            while (!end){
                try {
                    ze = inputstream.getNextEntry();
                    if (ze==null) {
                        end=true;
                    } else {

                        System.out.println("Zip Entry: " + ze.getName());
                        ArcoFlexFileItem item = new ArcoFlexFileItem(ze.getName(), "", "", "", "file_icon");
                        //list.add(ze.getName());
                        list.add(item);
                    }
                } catch (Exception e){
                    end = true;
                }
            }
        }

        return list;

    }
}
