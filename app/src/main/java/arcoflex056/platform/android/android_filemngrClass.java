/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package arcoflex056.platform.android;

import arcoflex056.platform.platformConfigurator;
import java.io.File;

/**
 *
 * @author chusogar
 */
public class android_filemngrClass implements platformConfigurator.i_filemngr_class {

    private File currDir = null;
    private String _supFilesStr = null;
    private String[] _arrExtensions = null;

    @Override
    public void setCurrentDirectory(File file) {
        currDir = file;
    }

    @Override
    public void setFileFilter(String _supFilesStr, String[] _arrExtensions) {
        this._supFilesStr = _supFilesStr;
        this._arrExtensions = _arrExtensions;
    }

    @Override
    public File getCurrentDirectory() {
        return currDir;
    }

    @Override
    public int showOpenDialog(Object obj) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object getSelectedFile() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
