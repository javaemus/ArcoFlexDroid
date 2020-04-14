package net.arcoflexdroid.panels.filebrowser;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static mess056.filemngr.osd_get_cwd;

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

                        //extracting file
                        String local_path = "";
                        byte[] _buffer = new byte[1024];
                        int _count = 0;
                        File _file = new File(osd_get_cwd() + "/" + ze.getName());
                        FileOutputStream fout = new FileOutputStream(_file);
                        try {
                            while ((_count = inputstream.read(_buffer)) != -1)
                                fout.write(_buffer, 0, _count);
                        } finally {
                            fout.close();
                        }

                        item.setRemoteZip(true);

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
