package net.arcoflexdroid.panels.filebrowser.remote.spectrum;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.arcoflexdroid.panels.filebrowser.ArcoFlexFileItem;

public class RemoteWOS  {
    private String _pre_url = "http://www.worldofspectrum.org/infoseek.cgi";
    private String _post_url = "?regexp=";
    private static Pattern pt1 = Pattern.compile("<A HREF=\"(.+?)\" TITLE=\"Download to play off-line in an emulator\">.+?</A>");
    //private static Pattern pt2 = Pattern.compile("http://www.worldofspectrum.org/infoseek\\.cgi\\?regexp=[^\"]+\\\">[^>^<]+</A>");
    private static Pattern pt2 = Pattern.compile("http://www.worldofspectrum.org/infoseek.cgi(.+?)regexp=(.+?)\">(.+?)</A>");
    private String _pre_url_file = "http://www.worldofspectrum.org";

    public RemoteWOS() {
        super();
        //System.out.println("CONSTRUCTOR");
    }

    public static void main(final String args[]) {
        //System.out.println("MAIN");
        RemoteWOS wos = new RemoteWOS();
        //wos.searchWOS("gremlin");
        ArrayList arr = wos.searchWOS("rocky");
        wos.getFileWOS( (ArcoFlexFileItem) arr.get(0) );
    }

    public ArrayList getFileWOS (ArcoFlexFileItem file) {
        ArrayList arr = new ArrayList();

        String s = LoadText(file.getPath(), "iso-8859-1");

        //Pattern pt1 = Pattern.compile("<A HREF=\"(.+?)\" TITLE=\"Download to play off-line in an emulator\">.+?</A>");
        Matcher m = pt1.matcher(s);

        int count=0;

        while(m.find())
        {

			/*jMESYSwosFile file = new jMESYSwosFile();
			file.setUrl(_pre_url+_post_url+m.group(2));
			file.setTitle(m.group(3));
			file.setYear(m.group(4));
			file.setPublisher(m.group(5));*/

            ArcoFlexFileItem item = new ArcoFlexFileItem(getSingleName(m.group(1)), "", "", _pre_url_file+m.group(1), "file_icon");

            //System.out.println((count++)+" Fichero: "+m.group(1));

            arr.add( item );
        }

        return arr;
    }

    private String getSingleName(String file_with_path) {
        StringTokenizer tok = new StringTokenizer(file_with_path,"/");
        String last = "";

        while (tok.hasMoreElements()) {
            last=tok.nextToken();
        }

        return last;
    }

    public ArrayList searchWOS (String tit) {

        ArrayList resList = new ArrayList();

        //System.out.println("SEARCH...");
        //String s = LoadText("http://www.worldofspectrum.org/infoseekid.cgi?id=", "iso-8859-1");

        //String s = LoadText("http://www.worldofspectrum.org/infoseekadv.cgi?what=1&yrorder=1&year=0&type=0&players=0&turns=0&memory=0&language=0&country=0&licence=0&feature=0&publi=0&release=0&format=0&scheme=0&scorder=1&score=0&have=1&hasinlay=on&also=1&sort=1&display=1&loadpics=0&regexp="+tit, "iso-8859-1");

        //String s = LoadText("http://www.worldofspectrum.org/infoseek.cgi?regexp=^Rambo$&pub=^Ocean+Software+Ltd$", "iso-8859-1");

        String s = LoadText("http://www.worldofspectrum.org/infoseek.cgi?loadpics=2&fast=on&&regexp="+tit, "iso-8859-1");
        //System.out.println("Processing...");
        if(s == null)
            System.out.println("UNABLE_CONNECT1");
		/*if(progress.Canceled())
			return ApplyResult.CANCELED;*/

        String url = null;
        //Pattern pt = Pattern.compile("<A HREF=\"(.+?)\" TITLE=\"Download to play off-line in an emulator\">.+?</A>");
        //Pattern pt = Pattern.compile("http://www.worldofspectrum.org/infoseek.cgi(.+?)regexp=(.+?)\">(.+?)</A></FONT><TD><FONT FACE=\"Arial,Helvetica\">(.+?)</FONT><TD><FONT FACE=\"Arial,Helvetica\">(.+?)</FONT>"); //^Rambo$&pub=^Ocean+Software+Ltd$


        //Pattern pt2 = Pattern.compile("http://www.worldofspectrum.org/infoseek.cgi(.+?)regexp=(.+?)\">(.+?)</A>");
        Matcher m = pt2.matcher(s);

        int count=0;

        //System.out.println(s);

        while(m.find())
        {
            //System.out.println("Encontrado Patrón!!!!");
            //url = BaseURL() + m.group(1);
            //System.out.println(m.group());
            //System.out.println(m.group(0));
            //System.out.println(m.group(2));
            //jMESYSFileItem file = new jMESYSFileItem(m.group(0),"", "", _pre_url+_post_url+m.group(0), "file_icon");
            ArcoFlexFileItem file = new ArcoFlexFileItem(m.group(3),"", "", _pre_url+_post_url+m.group(2), "file_icon");
            //file.setUrl(_pre_url+_post_url+m.group(2));
            //file.setTitle(m.group(3));
            //file.setYear(m.group(4));
            //file.setPublisher(m.group(5));

            //System.out.println((count++)+" REGEXP: "+m.group(2)+ " TITLE: "+m.group(3)+" YEAR: "+m.group(4)+" PUBLISHER: "+m.group(5));
            //System.out.println((count++)+" REGEXP: "+m.group(2)+ " TITLE: "+m.group(3));

            resList.add( file );
        }
		/*if(url == null)
			return ApplyResult.NOT_AVAILABLE;
		if(!url.startsWith(HTTP_URL))
			return ApplyResult.FAIL;*/

        return resList;
    }

    protected String LoadText(final String _url, final String _encoding)
    {
        try
        {
            //System.setProperty("http.proxyHost", "proxy.indra.es");
            //System.setProperty("http.proxyPort", "8080");
            //System.getProperties().put("http.proxyUser", "jagsanchez");
            //System.getProperties().put("http.proxyPassword", "jzfjxsJAGS2022");

            //URLConnection connection = new URL(_url).openConnection();
            HttpURLConnection connection = (HttpURLConnection) new URL(_url).openConnection();
            connection.addRequestProperty("User-Agent", "Mozilla/4.0");
            InputStream is = connection.getInputStream();
            int len = connection.getContentLength();
            byte buffer[] = new byte[16384];
            ByteBuffer buf = ByteBuffer.allocate(0);
            int r = -1;
            while((r = is.read(buffer)) != -1)
            {
                ByteBuffer buf1 = ByteBuffer.allocate(buf.capacity() + r);
                buf1.put(buf);
                buf1.put(buffer, 0, r);
                buf = buf1;
                buf.rewind();


            }
            is.close();
            Charset charset = Charset.forName(_encoding);
            CharsetDecoder decoder = charset.newDecoder();

            String result = decoder.decode(buf).toString();

            //System.out.println(result);

            return result;
        }
        catch(Exception e)
        {
            e.printStackTrace(System.out);
        }
        return null;
    }
}

