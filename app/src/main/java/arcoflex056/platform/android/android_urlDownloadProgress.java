/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package arcoflex056.platform.android;

import android.widget.Toast;

//import net.arcoflexdroid.ArcoFlexDroid;

import arcoflex056.platform.platformConfigurator;

/**
 *
 * @author chusogar
 */
public class android_urlDownloadProgress implements platformConfigurator.i_URLDownloadProgress_class{

    public static String _version;
    public static String _romName;
    public static String _fileName;

    @Override
    public void setVersion(String _version) {
        System.out.println("-->"+_version);
        this._version = _version;
        /*ArcoFlexDroid.mm.runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(ArcoFlexDroid.mm,
                        "VERSION "+android_urlDownloadProgress._version, Toast.LENGTH_LONG).show();
            }
        });*/

    }

    @Override
    public void setVisible(boolean _visible) {
        // nothing TO-DO
    }

    @Override
    public void setRomName(String _romName) {
        System.out.println("-->"+"ROM "+_romName);
        this._romName = _romName;
        /*ArcoFlexDroid.mm.runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(ArcoFlexDroid.mm,
                        "ROM "+android_urlDownloadProgress._romName, Toast.LENGTH_LONG).show();
            }
        });*/

    }

    @Override
    public void setFileName(String _fileName) {
        System.out.println("-->"+"FILE "+_fileName);
        this._fileName = _fileName;
        /*ArcoFlexDroid.mm.runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(ArcoFlexDroid.mm,
                        "FILE "+android_urlDownloadProgress._fileName, Toast.LENGTH_LONG).show();
            }
        });*/

    }
    
}
