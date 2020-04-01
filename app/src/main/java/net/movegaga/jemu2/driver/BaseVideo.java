package net.movegaga.jemu2.driver;

import net.movegaga.jemu2.JEmu2;

import jef.machine.EmulatorProperties;
import jef.video.BitMap;
import jef.video.BitMapImpl;
import jef.video.GfxManager;
import jef.video.VideoEmulator;
import jef.video.VideoRenderer;

/**
 * Helper class for porting MAME /vidhrdw/* classes to CottAGE
 */
public abstract class BaseVideo implements VideoEmulator, VideoRenderer {

    private int[]              pixels;
    private BitMap             backBuffer;
    //private int                width, height;
    private int[]              visible;
    private EmulatorProperties md;
    private GfxManager[]       gfxMan;
    //private int                colors;
    private int                rotation;
    private int                vX, vY;
    private int[]              palette;
    private int[][]            colorTable;
    private boolean            videoModifiesPalette;

    protected int getTotalColors() {
        return md.pal;
    }

    protected EmulatorProperties getMachineDriver() {
        return md;
    }

    public void init(EmulatorProperties md) {
        System.out.println("BaseVideo.init");
        this.md = md;
        //this.width = md.w;
        //this.height = md.h;
        this.visible = md.visible;
        this.palette = new int[md.pal];

        //this.colors = md.col;
        this.rotation = md.ROT;
        this.pixels = new int[((visible[1] + 1) - visible[0]) * ((visible[3] + 1) - visible[2])];
        this.videoModifiesPalette = (md.videoFlags & GfxManager.VIDEO_MODIFIES_PALETTE) != 0;

        this.vX = -visible[0];
        this.vY = -visible[2];

        switch (rotation) {
            case GfxManager.ROT0:
                vX = -(visible[0]);
                vY = -(visible[2]);
            case GfxManager.ROT180:
                backBuffer = new BitMapImpl(((visible[1] + 1) - visible[0]), ((visible[3] + 1) - visible[2]), pixels);
                break;
            case GfxManager.ROT90:
                vX = -(visible[0]);
                vY = -(visible[2]);
            case GfxManager.ROT270:
                backBuffer = new BitMapImpl(((visible[3] + 1) - visible[2]), ((visible[1] + 1) - visible[0]), pixels);
                break;
        }

        try {
            if (md.gfx != null) {
                this.gfxMan = new GfxManager[md.gfx.length];
                this.colorTable = new int[md.gfx.length][];
                for (int i = 0; i < md.gfx.length; i++) {
                    colorTable[i] = new int[md.gfx[i].numberOfColors << md.gfx[i].gfx.planes];
                    // Set up a default lookup table
                    for (int n = 0; n < colorTable[i].length; n++) {
                        colorTable[i][n] = n + md.gfx[i].colorOffset;
                    }
                    gfxMan[i] = new GfxManager();
                    gfxMan[i].init(md.gfx[i], palette, colorTable[i], 0, rotation, md.videoFlags);
                    System.out.println("inited GFX " + i);
                }
            }
        } catch (Exception e) {
            System.out.println("ERROR initing GFX: " + e);
            e.printStackTrace();
        }
    }

    public void setPaletteColor(int c, int r, int g, int b) {
        int rgb;
        if (JEmu2.IS_APPLET) {
            rgb = r << 16 | g << 8 | b;
        } else {
            rgb = b << 16 | g << 8 | r;
        }
        if (getPaletteColor(c) != rgb)
            setPaletteColor(c, rgb);
    }

    public void setPaletteColor(int c, int rgb) {
        palette[c] = rgb;
        if (videoModifiesPalette) {
            for (int i = 0; i < gfxMan.length; i++) {
                gfxMan[i].changePalette(c, rgb);
            }
        }
    }

    public int getPaletteColor(int c) {
        return palette[c];
    }

    public void renderVideoPost() {
        if (videoModifiesPalette) {
            for (int i = 0; i < gfxMan.length; i++) {
                gfxMan[i].refresh();
            }
        }
    }

    /**
     * This method mimics MAME's TOTAL_COLORS macro.
     */
    protected int getTotalColors(int i) {
        return md.gfx[i].numberOfColors << md.gfx[i].gfx.planes;
    }

    protected void setColor(int p, int i, int col) {
        colorTable[p][i] = col;
    }

    /**
     * Returns the color granularity of a layer
     */
    protected int getColorGranularity(int i) {
        return 1 << md.gfx[i].gfx.planes;
    }

    /**
     * Draw to target BitMap
     *
     * @param target
     * @param type
     * @param tile
     * @param color
     * @param flipx
     * @param flipy
     * @param x
     * @param y
     * @param transparency
     * @param transcolor
     */
    protected void drawVisible(BitMap target, int type, int tile, int color, boolean flipx, boolean flipy, int x,
                               int y, int transparency, int transcolor) {

        switch (rotation) {
            case GfxManager.ROT0:
                gfxMan[type].drawTile(target, tile, color, flipx, flipy, x + vX, y + vY, transparency, transcolor);
                break;
            case GfxManager.ROT90:
                gfxMan[type].drawTile(target, tile, color, flipy, flipx, x + vX, y + vY, transparency, transcolor);
                break;
            case GfxManager.ROT180:
                gfxMan[type].drawTile(target, tile, color, flipx, flipy, x + vX, y + vY, transparency, transcolor);
                break;
            case GfxManager.ROT270:
                gfxMan[type].drawTile(target, tile, color, flipy, flipx, x + vX, y + vY, transparency, transcolor);
                break;
        }
    }

    protected void draw(BitMap target, int type, int tile, int color, boolean flipx, boolean flipy, int x, int y,
                        int transparency, int transcolor) {

        switch (rotation) {
            case GfxManager.ROT0:
                gfxMan[type].drawTile(target, tile, color, flipx, flipy, x, y, transparency, transcolor);
                break;
            case GfxManager.ROT90:
                gfxMan[type].drawTile(target, tile, color, flipy, flipx, x, y, transparency, transcolor);
                break;
            case GfxManager.ROT180:
                gfxMan[type].drawTile(target, tile, color, flipx, flipy, x, y, transparency, transcolor);
                break;
            case GfxManager.ROT270:
                gfxMan[type].drawTile(target, tile, color, flipy, flipx, x, y, transparency, transcolor);
                break;
        }
    }

    /**
     * Draw to back buffer
     *
     * @param type
     * @param tile
     * @param color
     * @param flipx
     * @param flipy
     * @param x
     * @param y
     * @param transparency
     * @param transcolor
     */
    protected void drawVisible(int type, int tile, int color, boolean flipx, boolean flipy, int x, int y,
                               int transparency, int transcolor) {

        switch (rotation) {
            case GfxManager.ROT0:
                gfxMan[type].drawTile(backBuffer, tile, color, flipx, flipy, x + vX, y + vY, transparency, transcolor);
                break;
            case GfxManager.ROT90:
                gfxMan[type].drawTile(backBuffer, tile, color, flipy, flipx, x + vX, y + vY, transparency, transcolor);
                break;
            case GfxManager.ROT180:
                gfxMan[type].drawTile(backBuffer, tile, color, flipx, flipy, x + vX, y + vY, transparency, transcolor);
                break;
            case GfxManager.ROT270:
                gfxMan[type].drawTile(backBuffer, tile, color, flipy, flipx, x + vX, y + vY, transparency, transcolor);
                break;
        }
    }

    /**
     * Draw to target BitMap
     *
     * @param target
     * @param type
     * @param tile
     * @param color
     * @param flipx
     * @param flipy
     * @param x
     * @param y
     * @param transparency
     * @param transcolor
     */
    protected void drawVisible(BitMap target, int type, int tile, int color, int flipx, int flipy, int x, int y,
                               int transparency, int transcolor) {
        switch (rotation) {
            case GfxManager.ROT0:
                gfxMan[type].drawTile(target, tile, color, (flipx != 0), (flipy != 0), x + vX, y + vY, transparency,
                        transcolor);
                break;
            case GfxManager.ROT90:
                gfxMan[type].drawTile(target, tile, color, (flipy != 0), (flipx != 0), x + vX, y + vY, transparency,
                        transcolor);
                break;
            case GfxManager.ROT180:
                gfxMan[type].drawTile(target, tile, color, (flipx != 0), (flipy != 0), x + vX, y + vY, transparency,
                        transcolor);
                break;
            case GfxManager.ROT270:
                gfxMan[type].drawTile(target, tile, color, (flipy != 0), (flipx != 0), x + vX, y + vY, transparency,
                        transcolor);
                break;
        }
    }

    /**
     * Draw to target BitMap
     *
     * @param target
     * @param type
     * @param tile
     * @param color
     * @param flipx
     * @param flipy
     * @param x
     * @param y
     * @param area
     * @param transparency
     * @param transcolor
     */
    protected void draw(BitMap target, int type, int tile, int color, int flipx, int flipy, int x, int y, int area,
                        int transparency, int transcolor) {
        if (area == 1) {
            x += vX;
            y += vY;
        }
        switch (rotation) {
            case GfxManager.ROT0:
                gfxMan[type].drawTile(target, tile, color, (flipx != 0), (flipy != 0), x, y, transparency, transcolor);
                break;
            case GfxManager.ROT90:
                gfxMan[type].drawTile(target, tile, color, (flipy != 0), (flipx != 0), x, y, transparency, transcolor);
                break;
            case GfxManager.ROT180:
                gfxMan[type].drawTile(target, tile, color, (flipx != 0), (flipy != 0), x, y, transparency, transcolor);
                break;
            case GfxManager.ROT270:
                gfxMan[type].drawTile(target, tile, color, (flipy != 0), (flipx != 0), x, y, transparency, transcolor);
                break;
        }
    }

    protected void plotPixel(BitMap dest, int x, int y, int color) {
        dest.setPixelFast(x, y, color);
    }

    protected int readPixel(BitMap bm, int x, int y) {
        return bm.getPixel(x, y);
    }

    protected void copyBitMap(BitMap dest, BitMap src) {
        src.toPixels(pixels);
    }

    protected int[] getVisibleArea() {
        return visible;
    }

    /**
     * This method mimics MAME's copyscrollbitmap function.
     */
    protected void copyScrollBitMap(BitMap dest, BitMap src, int rows, int rowscroll, int cols, int colscroll,
                                    int transparency, int transcolor) {

        if (rows == 1 && cols == 1) {
            int sw, sh;
            int w, h;
            /* XY scrolling playfield */
            if ((rotation & 1) == 0) { // VideoEmulator.ROT0 or
                // VideoEmulator.ROT180
                sw = src.getWidth();
                sh = src.getHeight();
                rowscroll &= (sw - 1);
                colscroll &= (sh - 1);
                w = dest.getWidth();
                h = dest.getHeight();
            } else { // VideoEmulator.ROT90 or VideoEmulator.ROT270
                sh = src.getWidth();
                sw = src.getHeight();
                int r = rowscroll;
                int c = colscroll;
                colscroll = r & (sw - 1);
                rowscroll = c & (sh - 1);
                w = dest.getWidth();
                h = dest.getHeight();
            }

            if (((rowscroll + w) <= sw) && ((colscroll + h) <= sh)) {
                // no wrap
                src.toBitMap(dest, 0, 0, rowscroll, colscroll, w, h);
            } else if (((rowscroll + w) > sw) && ((colscroll + h) <= sh)) {
                // horizontal wrap
                src.toBitMap(dest, 0, 0, rowscroll, colscroll, sw - rowscroll, h);
                src.toBitMap(dest, sw - rowscroll, 0, rowscroll + (sw - rowscroll), colscroll, w - (sw - rowscroll), h);
            } else if (((rowscroll + w) <= sw) && ((colscroll + h) > sh)) {
                // vertical wrap
                src.toBitMap(dest, 0, 0, rowscroll, colscroll, w, h - ((colscroll + h) - sh));
                src.toBitMap(dest, 0, h - ((colscroll + h) - sh), rowscroll, 0, w, (colscroll + h) - sh);
            } else {
                // horizontal/vertical wrap
                src.toBitMap(dest, sw - rowscroll, sh - colscroll, 0, 0, (rowscroll + w) - sw, (colscroll + h) - sh);
                src.toBitMap(dest, 0, sh - colscroll, rowscroll, 0, sw - rowscroll, (colscroll + h) - sh);
                src.toBitMap(dest, sw - rowscroll, 0, 0, colscroll, (rowscroll + w) - sw, sh - colscroll);
                src.toBitMap(dest, 0, 0, rowscroll, colscroll, sw - rowscroll, sh - colscroll);
            }
        }
    }

    protected final void copyScrollBitMap(BitMap target, BitMap src, int rows, int[] scroll, int cols, int colscroll,
                                          int[] clip, int transparency, int transcolor) {
        if (cols == 0 && colscroll == 0) {

            int sw, sh, tw, th, rowheight;
            int tt;

            sw = src.getWidth();
            sh = src.getHeight();

            if ((rotation & 1) == 0) {
                if (transparency == GfxManager.TRANSPARENCY_COLOR)
                    tt = transcolor;
                else
                    tt = -1;

                tw = target.getWidth() + (clip[0] << 1); // we assume now
                // that the visible
                // area is in the
                // middle.
                th = target.getHeight()/* + (clip[2] << 1) */; // we assume now
                // that the
                // visible area
                // is in the
                // middle.
                rowheight = sh / rows;

                int ydst, ysrc;
                ydst = 0;
                ysrc = clip[2];

                int i = 0;
                if (clip[2] >= rowheight) {
                    i = clip[2] / rowheight;
                }

                for (; i < rows; i++) {
                    // if (y >= 0 && y < target.getHeight()) {

                    int x = -scroll[i] + clip[0];
                    if (x < 0)
                        x += sw;
                    if ((x + target.getWidth()) <= sw) {
                        // No wrap
                        src.toBitMap(target, 0, ydst, x, ysrc, target.getWidth(), rowheight, tt);
                    } else {
                        src.toBitMap(target, 0, ydst, x, ysrc, sw - x, rowheight, tt);
                        src.toBitMap(target, sw - x, ydst, 0, ysrc, target.getWidth() - (sw - x), rowheight, tt);
                    }
                    // }
                    ysrc += rowheight;
                    ydst += rowheight;
                }
            } else {
                tw = target.getWidth() + (clip[2] << 1); // we assume now
                // that the visible
                // area is in the
                // middle.
                th = target.getHeight() + (clip[0] << 1); // we assume now
                // that the visible
                // area is in the
                // middle.
                rowheight = tw / rows;
                for (int i = 0; i < rows; i++) {
                    int y = (i * rowheight);
                    int yt = y - (clip[2] << 1);
                    if (yt >= 0 && yt < target.getWidth()) {
                        int x = -scroll[i - 2] + th; // don't know about that
                        // -2 part but it seems
                        // to work
                        if (x < 0)
                            x += sh;
                        if ((x + target.getHeight()) <= sh) {
                            // No wrap
                            src.toBitMap(target, yt, 0, y, x, rowheight, target.getHeight());
                        } else {
                            src.toBitMap(target, yt, 0, y, x, rowheight, sh - x);
                            src.toBitMap(target, yt, sh - x, y, 0, rowheight, target.getHeight() - (sh - x));
                        }
                    }
                }

            }
        }
    }

    protected final void copyScrollBitMap(BitMap target, BitMap src, int cols, int colscroll, int rows, int[] scroll,
                                          int[] clip, int transparency, int transcolor) {
        if (cols == 0 && colscroll == 0) {

            int sw, sh, tw, th, rowheight;

            sw = src.getWidth();
            sh = src.getHeight();

            if ((rotation & 1) == 1) {
                tw = target.getWidth() + (clip[0] << 1); // we assume now
                // that the visible
                // area is in the
                // middle.
                th = target.getHeight()/* + (clip[2] << 1) */; // we assume now
                // that the
                // visible area
                // is in the
                // middle.
                rowheight = th / rows;
                for (int i = 0; i < rows; i++) {
                    int y = (i * rowheight)/* - clip[2] */;
                    if (y >= 0 && y < target.getHeight()) {
                        int x = -scroll[i] + clip[2];
                        if (x < 0)
                            x += sw;
                        if ((x + target.getWidth()) <= sw) {
                            // No wrap
                            src.toBitMap(target, 0, y, x, y, target.getWidth(), rowheight);
                        } else {
                            src.toBitMap(target, 0, y, x, y, sw - x, rowheight);
                            src.toBitMap(target, sw - x, y, 0, y, target.getWidth() - (sw - x), rowheight);
                        }
                    }
                }
            } else {
                tw = target.getWidth() + (clip[2] << 1); // we assume now
                // that the visible
                // area is in the
                // middle.
                th = target.getHeight() + (clip[0] << 1); // we assume now
                // that the visible
                // area is in the
                // middle.
                rowheight = tw / rows;
                for (int i = 0; i < rows; i++) {
                    int y = (i * rowheight);
                    int yt = y - (clip[2] << 1);
                    if (yt >= 0 && yt < target.getWidth()) {
                        int x = -scroll[i - 2] + th; // don't know about that
                        // -2 part but it seems
                        // to work
                        if (x < 0)
                            x += sh;
                        if ((x + target.getHeight()) <= sh) {
                            // No wrap
                            src.toBitMap(target, yt, 0, y, x, rowheight, target.getHeight());
                        } else {
                            src.toBitMap(target, yt, 0, y, x, rowheight, sh - x);
                            src.toBitMap(target, yt, sh - x, y, 0, rowheight, target.getHeight() - (sh - x));
                        }
                    }
                }
            }
        }
    }

    /**
     * @return Returns the rot.
     */
    protected int getRot() {
        return rotation;
    }

    protected BitMap getBackBuffer() {
        return backBuffer;
    }

    protected void changeColor_xBBBBBGGGGGRRRRR(int color, int data) {
        int r = ((data >> 0) & 0x1f);
        int g = ((data >> 5) & 0x1f);
        int b = ((data >> 10) & 0x1f);
        setPaletteColor(color, (r << 3) | (r >> 2), (g << 3) | (g >> 2), (b << 3) | (b >> 2));
    }

    protected void changecolor_xxxxRRRRGGGGBBBB(int color, int data) {
        int r = (data >> 8) & 0x0f;
        int g = (data >> 4) & 0x0f;
        int b = (data >> 0) & 0x0f;
        setPaletteColor(color, (r << 4) | r, (g << 4) | g, (b << 4) | b);
    }

    public void changecolor_RRRRGGGGBBBBxxxx(int color, int data) {
        int r = (data >> 12) & 0x0f;
        int g = (data >> 8) & 0x0f;
        int b = (data >> 4) & 0x0f;
        setPaletteColor(color, (r << 4) | r, (g << 4) | g, (b << 4) | b);
    }

    /**
     * @return Returns the gfxMan.
     */
    protected GfxManager getGfxManager(int i) {
        return gfxMan[i];
    }
}