package jef.util;

import net.arcoflexdroid.ArcoFlexDroid;

import net.movegaga.jemu2.JEmu2;

import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public final class RomLoader {
    static final String FILE_SEPERATOR = "/";
    public static float progress = 0.0f;
    public static String _currentROM = "";
    public static ProgressListener progressListener;
    private final int MAXLIST = 256;
    private int id;
    private boolean invert = false;
    private int load_count = 0;
    private boolean noLoad = false;
    private String parent_zip = null;
    private int pid;
    private char[] ram;
    private final boolean[] romlist_cont = new boolean[256];
    private final int[] romlist_crc = new int[256];
    private int romlist_index = 0;
    private final boolean[] romlist_invert = new boolean[256];
    private final int[] romlist_length = new int[256];
    private final boolean[] romlist_loaded = new boolean[256];
    private final String[] romlist_names = new String[256];
    private final int[] romlist_orglength = new int[256];
    private final int[] romlist_skip = new int[256];
    private final int[] romlist_storeat = new int[256];
    private final char[][] romlist_useram = new char[256][];
    private final char[][] romlist_usetmp = new char[256][];
    private String sub = "";
    private String zip;

    private ArcoFlexDroid mm;

    public RomLoader(){
        this.mm = ArcoFlexDroid.mm;
    }

    public final void noLoading() {
        this.noLoad = true;
    }

    public final void setMemory(char[] mem) {
        this.ram = mem;
    }

    public final void setInvert(boolean inv) {
        this.invert = inv;
    }

    public final void setZip(String zip) {
        this.zip = new StringBuilder(String.valueOf(zip)).append(".zip").toString();
    }

    public final void setZip(int id) {
        this.id = id;
    }

    public final void setParentZip(String parent) {
        if (parent == null) {
            this.parent_zip = null;
        } else {
            this.parent_zip = new StringBuilder(String.valueOf(parent)).append(".zip").toString();
        }
    }

    public final void setZip(String zip, String sub) {
        this.zip = new StringBuilder(String.valueOf(zip)).append(".zip").toString();
        this.sub = new StringBuilder(String.valueOf(sub)).append(FILE_SEPERATOR).toString();
    }

    public final void loadROM(String FName, int StoreAt, int Length) {
        loadROM(FName, StoreAt, Length, 0);
    }

    public final void loadROM(String FName, int StoreAt, int Length, int crc) {
        this.romlist_useram[this.romlist_index] = this.ram;
        this.romlist_names[this.romlist_index] = (this.sub + FName).toLowerCase();
        this.romlist_storeat[this.romlist_index] = StoreAt;
        this.romlist_length[this.romlist_index] = Length;
        this.romlist_orglength[this.romlist_index] = Length;
        this.romlist_crc[this.romlist_index] = crc;
        this.romlist_loaded[this.romlist_index] = false;
        this.romlist_cont[this.romlist_index] = false;
        this.romlist_usetmp[this.romlist_index] = null;
        this.romlist_skip[this.romlist_index] = 1;
        this.romlist_invert[this.romlist_index] = this.invert;
        this.romlist_index++;
    }

    public final void loadROM(String FName, int StoreAt, int Length, int crc, int skip) {
        this.romlist_useram[this.romlist_index] = this.ram;
        this.romlist_names[this.romlist_index] = (this.sub + FName).toLowerCase();
        this.romlist_storeat[this.romlist_index] = StoreAt;
        this.romlist_length[this.romlist_index] = Length;
        this.romlist_orglength[this.romlist_index] = Length;
        this.romlist_crc[this.romlist_index] = crc;
        this.romlist_loaded[this.romlist_index] = false;
        this.romlist_cont[this.romlist_index] = false;
        this.romlist_usetmp[this.romlist_index] = null;
        this.romlist_skip[this.romlist_index] = skip + 1;
        this.romlist_invert[this.romlist_index] = this.invert;
        this.romlist_index++;
    }

    public final void continueROM(int StoreAt, int Length) {
        this.romlist_useram[this.romlist_index] = this.ram;
        this.romlist_names[this.romlist_index] = null;
        this.romlist_storeat[this.romlist_index] = StoreAt;
        this.romlist_length[this.romlist_index] = Length;
        this.romlist_orglength[this.romlist_index] = Length;
        this.romlist_crc[this.romlist_index] = 0;
        this.romlist_loaded[this.romlist_index] = true;
        this.romlist_cont[this.romlist_index] = true;
        this.romlist_usetmp[this.romlist_index] = null;
        this.romlist_invert[this.romlist_index] = this.invert;
        int i = this.romlist_index - 1;
        while (this.romlist_cont[i]) {
            i--;
        }
        int[] iArr = this.romlist_length;
        iArr[i] = iArr[i] + Length;
        this.romlist_index++;
        this.load_count++;
    }

    private final void storeMemory() {
        int i = 0;
        while (i < this.romlist_index) {
            int ofs;
            int j;
            if (i + 1 >= this.romlist_index || !this.romlist_cont[i + 1]) {
                ofs = this.romlist_storeat[i];
                int skip = this.romlist_skip[i];
                if (this.romlist_invert[i]) {
                    for (j = 0; j < this.romlist_length[i]; j++) {
                        this.romlist_useram[i][ofs] = (char) ((this.romlist_usetmp[i][j] ^ 255) & 255);
                        ofs += skip;
                    }
                } else {
                    for (j = 0; j < this.romlist_length[i]; j++) {
                        this.romlist_useram[i][ofs] = this.romlist_usetmp[i][j];
                        ofs += skip;
                    }
                }
                this.romlist_usetmp[i] = null;
                i++;
            } else {
                int ofs2;
                int org = i;
                ofs = this.romlist_storeat[i];
                if (this.romlist_invert[i]) {
                    j = 0;
                    while (j < this.romlist_orglength[i]) {
                        ofs2 = ofs + 1;
                        this.romlist_useram[i][ofs] = (char) ((this.romlist_usetmp[i][j] ^ 255) & 255);
                        j++;
                        ofs = ofs2;
                    }
                } else {
                    j = 0;
                    while (j < this.romlist_orglength[i]) {
                        ofs2 = ofs + 1;
                        this.romlist_useram[i][ofs] = this.romlist_usetmp[i][j];
                        j++;
                        ofs = ofs2;
                    }
                }
                int cur_ofs = this.romlist_orglength[i];
                i++;
                while (i < this.romlist_index && this.romlist_cont[i]) {
                    ofs = this.romlist_storeat[i];
                    int cur_ofs2;
                    if (this.romlist_invert[org]) {
                        j = 0;
                        while (j < this.romlist_length[i]) {
                            ofs2 = ofs + 1;
                            cur_ofs2 = cur_ofs + 1;
                            this.romlist_useram[i][ofs] = (char) ((this.romlist_usetmp[org][cur_ofs] ^ 255) & 255);
                            j++;
                            cur_ofs = cur_ofs2;
                            ofs = ofs2;
                        }
                    } else {
                        j = 0;
                        while (j < this.romlist_length[i]) {
                            ofs2 = ofs + 1;
                            cur_ofs2 = cur_ofs + 1;
                            this.romlist_useram[i][ofs] = this.romlist_usetmp[org][cur_ofs];
                            j++;
                            cur_ofs = cur_ofs2;
                            ofs = ofs2;
                        }
                    }
                    i++;
                }
                this.romlist_usetmp[org] = null;
            }
        }
    }

    private void updateProgress(float p) {
        progress = p;
        if (progressListener != null) {
            progressListener.setProgress(p);
        }
    }

    public final void loadZip(URL rootURL) {
        updateProgress(0.0f);
        if (!this.noLoad) {
            loadFromURL(getClass().getClassLoader().getResource("./raw/"+ _currentROM+".zip"), false);
            storeMemory();
            this.romlist_index = 0;
            this.load_count = 0;
        }
    }

    private final void loadFromURL(URL baseURL, boolean bParent) {
        //System.out.println("baseURL: "+baseURL.getFile());

        InputStream is=null;

        //if (bParent) {
            try {
                System.out.println("Context: "+mm);
                System.out.println("Context2: "+ArcoFlexDroid.mm);
                System.out.println("ROM: "+_currentROM);
                //System.out.println("Ruta: "+ArcoFlexDroid.mm.getResources());
                //int em_id = ArcoFlexDroid.mm.getResources().getIdentifier(_currentROM, "raw", ArcoFlexDroid.mm.getPackageName());
                //is = JEmu2Droid.main.getContext().getResources().openRawResource(em_id);
            } catch (Exception e) {
                Exception e2 = e;
                System.out.println("ERROR loading ROM: error opening stream! " + this.id);
                System.out.println(e2);
                JEmu2.log(e2);
                throw new RuntimeException("ERROR loading ROM! " + this.id);
            }
        //}
        //InputStream
        //is = JEmu2Droid.main.getContext().getResources().openRawResource(R.raw.gng);
        System.out.println("DRIVER: "+_currentROM);
        /*is = mm.getResources().openRawResource(
                mm.getResources().getIdentifier(_currentROM,
                        "raw", mm.getPackageName()));*/

        System.out.println("LOCAL FILE! "+mm.getPrefsHelper().getInstallationDIR() + "roms/" + _currentROM+".zip");

        try {
            is = new FileInputStream(mm.getPrefsHelper().getInstallationDIR() + "roms/" + _currentROM+".zip");
        } catch (Exception e){
            System.out.println("ERROR loading ROM: FILE! "+mm.getPrefsHelper().getInstallationDIR() + "roms/" + _currentROM+".zip");
            e.printStackTrace(System.out);
        }
        if (is == null) {
            System.out.println("ERROR loading ROM: null InputStream");
            throw new RuntimeException("ERROR loading ROM!");
        }
        ZipInputStream zis = new ZipInputStream(is);
        ZipEntry ze = null;
        while (true) {
            try {
                ze = zis.getNextEntry();
            } catch (Exception e22) {
                System.out.println("ERROR loading ROM: error getting zip entry");
                System.out.println(e22);
                JEmu2.log(e22);
            }
            if (ze == null) {
                break;
            }
            int i = 0;
            while (i < this.romlist_index) {
                if (this.romlist_loaded[i] || ((int) ze.getCrc()) != this.romlist_crc[i]) {
                    i++;
                } else {
                    if (this.romlist_crc[i] == 0) {
                        System.out.print("Loading " + this.romlist_names[i] + "(NO GOOD DUMP KNOWN)...");
                    } else {
                        System.out.print("Loading " + this.romlist_names[i] + "(" + Integer.toHexString(this.romlist_crc[i]) + ")...");
                    }
                    int length = this.romlist_length[i];
                    byte[] buffer = new byte[length];
                    int nbytes = length;
                    int toreadbytes = length;
                    while (toreadbytes > 0) {
                        try {
                            int nreadbytes = zis.read(buffer, nbytes - toreadbytes, toreadbytes);
                            if (nreadbytes == -1) {
                                break;
                            }
                            toreadbytes -= nreadbytes;
                        } catch (Exception ignored) {
                            System.out.println("ERROR loading ROM: invalid ZIP file");
                            JEmu2.log(ignored);
                            ignored.printStackTrace();
                        }
                    }
                    this.romlist_usetmp[i] = new char[length];
                    for (int j = 0; j < length; j++) {
                        this.romlist_usetmp[i][j] = (char) ((buffer[j] + 256) & 255);
                    }
                    buffer = null;
                    this.romlist_loaded[i] = true;
                    this.load_count++;
                    System.out.println("Ok!");
                    updateProgress(((float) this.load_count) / ((float) this.romlist_index));
                }
            }
        }
        if (this.load_count != this.romlist_index && !bParent) {
            for (int i = 0; i < this.romlist_index; i++) {
                if (!this.romlist_loaded[i]) {
                    JEmu2.log(new StringBuilder(String.valueOf(this.romlist_names[i])).append("(").append(Integer.toHexString(this.romlist_crc[i])).append(") not found !").toString());
                    System.out.println(new StringBuilder(String.valueOf(this.romlist_names[i])).append("(").append(Integer.toHexString(this.romlist_crc[i])).append(") not found !").toString());
                }
            }
        }
    }
}
