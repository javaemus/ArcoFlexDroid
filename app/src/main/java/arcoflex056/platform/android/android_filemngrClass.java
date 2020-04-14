/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package arcoflex056.platform.android;

import android.content.Intent;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import net.arcoflexdroid.MainActivity;
import net.arcoflexdroid.panels.filebrowser.ArcoFlexFileOpenActivity;
import net.arcoflexdroid.panels.filebrowser.ArcoFlexJFileChooserDialog;
import net.arcoflexdroid.panels.filebrowser.ArcoFlexListView;

import arcoflex056.platform.platformConfigurator;
import java.io.File;

import static mess056.filemngr.osd_get_cwd;
import static net.arcoflexdroid.MainActivity.A_FILE_SELECTOR;

/**
 *
 * @author chusogar
 */
public class android_filemngrClass implements platformConfigurator.i_filemngr_class {

    private File currDir = null;
    private String _supFilesStr = null;
    private String[] _arrExtensions = null;
    private File _selectedFile = null;

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

        //ArcoFlexFileOpenActivity._endSelection = false;
        //ArcoFlexJFileChooserDialog._selectedFile = false;

        ArcoFlexListView.currentDir = new File(osd_get_cwd());

        //MainActivity.mm.startActivityForResult(new Intent(MainActivity.mm, ArcoFlexFileOpenActivity.class), A_FILE_SELECTOR);
        ArcoFlexJFileChooserDialog _myDialog = ArcoFlexJFileChooserDialog.newInstance("Some Title");


        FragmentManager fm = MainActivity.mm.getSupportFragmentManager();
        _myDialog.show(fm, "fragment_edit_name");

        _selectedFile = ArcoFlexJFileChooserDialog._fSelected;

        return 0;
        /*if (ArcoFlexFileOpenActivity._selectedFile) {
            System.out.println("FICHERO SELECCIONADO!!!!------------------------------------------");
            return 0;
        } else {
            System.out.println("NO SELECCIONADO!!!!------------------------------------------");
            return 1;
        }*/
    }

    @Override
    public Object getSelectedFile() {
        return _selectedFile;
        //return (new File("file:///storage/emulated/0/Android/data/net.arcoflexdroid/files/ArcoFlexDroid/software/spectrum/Commando.z80"));
    }
    
}
