package net.movegaga.jemu2.driver;

import java.net.URL;

import jef.machine.Emulator;
import jef.video.VideoConstants;

public abstract class BaseDriver implements Driver, VideoConstants {
    private int height;
    private String manufacturer;
    private String name;
    private int width;

    public Emulator createEmulator(URL url, String name) {
        return null;
    }

    public Object getObject(int enumProperty) {
        switch (enumProperty) {
            case 0:
                return new Integer(this.width);
            case 1:
                return new Integer(this.height);
            case 2:
                return this.name;
            case 3:
                return this.manufacturer;
            default:
                return null;
        }
    }

    private int BIT(int x, int n) {
        return (x >>> n) & 1;
    }

    protected int bitswap24(int val, int B23, int B22, int B21, int B20, int B19, int B18, int B17, int B16, int B15, int B14, int B13, int B12, int B11, int B10, int B9, int B8, int B7, int B6, int B5, int B4, int B3, int B2, int B1, int B0) {
        return (((((((((((((((((((((((BIT(val, B23) << 23) | (BIT(val, B22) << 22)) | (BIT(val, B21) << 21)) | (BIT(val, B20) << 20)) | (BIT(val, B19) << 19)) | (BIT(val, B18) << 18)) | (BIT(val, B17) << 17)) | (BIT(val, B16) << 16)) | (BIT(val, B15) << 15)) | (BIT(val, B14) << 14)) | (BIT(val, B13) << 13)) | (BIT(val, B12) << 12)) | (BIT(val, B11) << 11)) | (BIT(val, B10) << 10)) | (BIT(val, B9) << 9)) | (BIT(val, B8) << 8)) | (BIT(val, B7) << 7)) | (BIT(val, B6) << 6)) | (BIT(val, B5) << 5)) | (BIT(val, B4) << 4)) | (BIT(val, B3) << 3)) | (BIT(val, B2) << 2)) | (BIT(val, B1) << 1)) | (BIT(val, B0) << 0);
    }

    protected void setWidth(int w) {
        this.width = w;
    }

    protected void setHeight(int h) {
        this.height = h;
    }

    protected void setName(String n) {
        this.name = n;
    }

    protected void setManufacturer(String m) {
        this.manufacturer = m;
    }

    protected int RGN_FRAC(int num, int den) {
        return (Integer.MIN_VALUE | ((num & 15) << 27)) | ((den & 15) << 23);
    }
}
