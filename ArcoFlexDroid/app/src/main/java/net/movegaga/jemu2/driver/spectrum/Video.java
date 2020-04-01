/*
 * Created on Dec 17, 2006
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package net.movegaga.jemu2.driver.spectrum;

import jef.video.BitMap;
import jef.video.BitMapImpl;
import jef.video.VideoRenderer;

public class Video implements VideoRenderer {

    Spectrum  spectrum;
    BitMap    bitmap = new BitMapImpl(256, 192);
    int[]     pixels = bitmap.getPixels();

    // ccccCCCCpppppppp...
    int[]     pix    = new int[256 * 16 * 16 * 8];

    public Video(Spectrum spectrum) {
        this.spectrum = spectrum;

        init();
    }

    public void writeChr(int a, char d) {
        //if (d != spectrum.ram[a]) {
            spectrum.mem[a] = d;
            //drawByte(a);
        //}
    }

    public void writeAtt(int a, char d) {
        //if (d != spectrum.ram[a]) {
            spectrum.mem[a] = d;
            /*a -= 0x5800;
            int x = (a & 0x1f);
            int y = (a >> 5);
            a = 0x4000 + (y << 3) + x;
            drawByte(a);
            drawByte(a+32);
            drawByte(a+64);
            drawByte(a+96);
            drawByte(a+128);
            drawByte(a+160);
            drawByte(a+192);
            drawByte(a+224);*/
        //}
    }
    
    /**
     * Character memory:
     * 0x4000  0  1 2 3 ... 31
     * 0x4020 32 33 ...
     * 
     * 0x5800
     * 
     * Attr memory:
     * 0x
     * @param i0
     */
    private void drawByte(int i0) {
        int x = (i0 & 0x1f) << 3;
        int y = (((i0 & 0x00e0)) >> 2) + (((i0 & 0x0700)) >> 8) + (((i0 & 0x1800)) >> 5);
        int chr = spectrum.mem[i0];
        int attr = spectrum.mem[0x5800 + (x >> 3) + ((y >> 3) << 5)];
        int ci = (chr << 3) + (attr << 11);
        int i1 = (y << 8) + (x);
        System.arraycopy(pix, ci, pixels, i1, 8);
    }
    
    public void refreshFrame() {
        for (int i0 = 0x4000; i0 < 0x5800; i0++) {
            drawByte(i0);
        }
    }

    private void init() {
        int[] rgb = { 0x000000, /* black */
        0xbf0000, /* blue */
        0x0000bf, /* red */
        0xbf00bf, /* magenta */
        0x00bf00, /* green */
        0xbfbf00, /* cyan */
        0x00bfbf, /* yellow */
        0xbfbfbf, /* white */
        0x000000, /* black */
        0xff0000, /* bright blue */
        0x0000ff, /* bright red */
        0xff00ff, /* bright magenta */
        0x00ff00, /* bright green */
        0xffff00, /* bright cyan */
        0x00ffff, /* bright yellow */
        0xffffff /* bright white */
        };

        int idx = 0;
        for (int c = 0; c < 256; c++) {
            int cf = (c & 0x07) | ((c & 0x40) >> 3);
            int cb = ((c & 0x78) >> 3);
            //System.out.println("c " + Integer.toHexString(c) + " = " + Integer.toHexString(cf) + "," + Integer.toHexString(cb));
            int colbg = rgb[cb];
            int colfg = rgb[cf];
            for (int i = 0; i < 256; i++) {
                pix[idx++] = ((i & 128) == 128) ? colfg : colbg;
                pix[idx++] = ((i & 64) == 64) ? colfg : colbg;
                pix[idx++] = ((i & 32) == 32) ? colfg : colbg;
                pix[idx++] = ((i & 16) == 16) ? colfg : colbg;
                pix[idx++] = ((i & 8) == 8) ? colfg : colbg;
                pix[idx++] = ((i & 4) == 4) ? colfg : colbg;
                pix[idx++] = ((i & 2) == 2) ? colfg : colbg;
                pix[idx++] = ((i & 1) == 1) ? colfg : colbg;
            }
        }
    }

    public BitMap renderFrame() {
        refreshFrame();
        return bitmap;
    }

    public BitMap renderVideo() {
        return renderFrame();
    }

    public void renderVideoPost() {
        // TODO Auto-generated method stub

    }

}
