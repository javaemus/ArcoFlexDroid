/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package arcoflex056.platform.android;

import android.graphics.Bitmap;

/*import net.arcoflexdroid.ArcoFlexDroid;
import net.arcoflexdroid.R;
import net.arcoflexdroid.views.ArcoFlexEmulatorView;*/

import arcadeflex056.settings;

import static arcoflex056.platform.platformConfigurator.*;
import static arcadeflex056.video.osd_refresh;
import static arcadeflex056.video.scanlines;
import static arcadeflex056.video.screen;
import static mame056.mame.Machine;

/**
 *
 * @author chusogar
 */
public class android_softwareGFXClass implements Runnable, i_software_gfx_class {

    public Thread _thread;

    @Override
    public void setTitle(String title) {
        System.out.println("--> TITLE: "+title);
    }

    @Override
    public void blit() {
        //blit2();
        /*TODO*///ArcoFlexDroid.mm.running = true;
    }

    public static void blit2() {
        // nothing to do
        //System.out.println("--> blit "+screen._pixels[0]);
        /*if (ArcoFlexDroid.mm.running) {
            int _mWidth = (int) settings.current_platform_configuration.get_video_class().getWidth();
            int _mHeight = (int) settings.current_platform_configuration.get_video_class().getHeight();


            ArcoFlexDroid.mm.pixels = screen._pixels;

            Bitmap bma = Bitmap.createBitmap(_mWidth, _mHeight, Bitmap.Config.RGB_565);
            bma.setPixels(ArcoFlexDroid.mm.pixels, 0, _mWidth, 0, 0, _mWidth, _mHeight);

            int displayWidth = ArcoFlexDroid.mm._maxWidth;
            int maxWidth = displayWidth;

            int maxHeight = ArcoFlexDroid.mm._maxHeight;


            int bmp_width = _mWidth;
            int bmp_height = _mHeight;
            float ratioBitmap = (float) bmp_width / (float) bmp_height;
            float ratioMax = (float) maxWidth / (float) maxHeight;

            int finalWidth = maxWidth;
            int finalHeight = maxHeight;
            if (ratioMax > 1) {
                finalWidth = (int) ((float) maxHeight * ratioBitmap);
            } else {
                finalHeight = (int) ((float) maxWidth / ratioBitmap);
            }


            if (bma != null) {
                ((ArcoFlexEmulatorView) (ArcoFlexDroid.mm.emuView)).drawScreenEmulator(Bitmap.createScaledBitmap(bma, finalWidth, finalHeight, false));

                ArcoFlexDroid.mm.runOnUiThread(
                        (new Thread(new Runnable() {
                            public void run() {
                                ArcoFlexDroid.mm.mImgEm = ArcoFlexDroid.mm.findViewById(R.id.EmulatorScreen);

                                ArcoFlexDroid.mm.mImgEm.setImageBitmap(((ArcoFlexEmulatorView) (ArcoFlexDroid.mm.emuView)).screenBitmap);
                                ArcoFlexDroid.mm.running = true;
                                ArcoFlexDroid.mm.emuView.invalidate();
                            }
                        }
                        ))
                );

            }
        }*/
    }




    @Override
    public void initScreen() {
        System.out.println("--> InitScreen");
        screen.setSize((scanlines == 0), Machine.scrbitmap.width, Machine.scrbitmap.height);
        //_width = Machine.scrbitmap.width;
        //_height = Machine.scrbitmap.height;
        //this.setBackground(Color.BLACK);
        this.start();
        this.run();
        //this.setLocation((int) ((current_platform_configuration.get_video_class().getWidth() - screen.getWidth()) / 2.0D), (int) ((current_platform_configuration.get_video_class().getHeight() - screen.getHeight()) / 2.0D));
        //this.setVisible(true);
        //this.setResizable((scanlines == 1));
    }

    @Override
    public void setSize(boolean scanlines, int width, int height) {
        System.out.println("--> setSize");
        System.out.println(width + "x" + height);

    }

    public void start() {
        /* Check if thread exists. */
        if (_thread != null) {
            return;
        }

        /* Create and start thread. */
        _thread = new Thread(this);
        _thread.start();
    }

    public void stop() {
        /* Destroy thread. */
        _thread = null;
    }

    @Override
    public void run() {
        System.out.println("--> RUN");
    }

    @Override
    public void reint() {
        System.out.println("--> reInt");
    }
    
}
