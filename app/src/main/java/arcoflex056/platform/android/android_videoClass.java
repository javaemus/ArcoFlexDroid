/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package arcoflex056.platform.android;

import arcadeflex036.software_gfx;
import arcadeflex056.settings;
import static arcadeflex056.settings.current_platform_configuration;
import static arcadeflex056.video.osd_refresh;
import static arcadeflex056.video.scanlines;
import static arcadeflex056.video.screen;
import static arcoflex056.platform.platformConfigurator.*;

import static mame056.mame.Machine;
import static mame056.version.build_version;

/**
 *
 * @author chusogar
 */
class android_videoClass implements i_video_class{

    @Override
    public double getWidth() {
        return Machine.scrbitmap.width;
    }

    @Override
    public double getHeight() {
        return Machine.scrbitmap.height;
    }

    @Override
    public void tempCreation() {
        screen = new software_gfx(settings.version + " (based on mame v" + build_version + ")");
        screen.initScreen();
    }
    
}
