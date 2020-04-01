package net.movegaga.jemu2;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class Config {
   public static final String KEY_NANOTIMER_ENABLED = "system.nanotime";
   public static final String KEY_RENDERER = "video.renderer";
   public static final String KEY_SOUND_AUTORESIZE_BUFFERS = "sound.autoresize";
   public static final String KEY_SOUND_ENABLED = "sound.enabled";
   public static final String KEY_SOUND_FREQ = "sound.freq";
   public static final String KEY_SOUND_STEREO_DELAY = "sound.stereodelay";
   public static final String KEY_SOUND_STEREO_ENHANCED = "sound.stereo";
   public static final String KEY_VIDEO_FULLSCREEN = "video.fullscreen";
   private static File path;
   private static Properties props;

   static {
      createDefaults();
      File var0 = new File(System.getProperty("user.home") + "/net/movegaga");
      if(!var0.exists()) {
         var0.mkdir();
      }

      path = new File(System.getProperty("user.home") + "/net/movegaga/.config");
      if(!path.exists()) {
         save();
      } else {
         load();
      }

   }

   private static void createDefaults() {
      props = new Properties();
      props.setProperty("sound.enabled", "true");
      props.setProperty("sound.stereo", "true");
      props.setProperty("sound.stereodelay", "true");
      props.setProperty("sound.autoresize", "true");
      props.setProperty("sound.freq", "44100");
      props.setProperty("video.fullscreen", "true");
      props.setProperty("video.renderer", "0");
      props.setProperty("system.nanotime", "true");
      props.setProperty(ControlsConfig.KEYS[0], "KEY_LEFT");
      props.setProperty(ControlsConfig.KEYS[1], "KEY_RIGHT");
      props.setProperty(ControlsConfig.KEYS[2], "KEY_UP");
      props.setProperty(ControlsConfig.KEYS[3], "KEY_DOWN");
      props.setProperty(ControlsConfig.KEYS[4], "KEY_LCONTROL");
      props.setProperty(ControlsConfig.KEYS[5], "KEY_SPACE");
      props.setProperty(ControlsConfig.KEYS[6], "KEY_LSHIFT");
      props.setProperty(ControlsConfig.KEYS[7], "KEY_Z");
      props.setProperty(ControlsConfig.KEYS[8], "KEY_X");
      props.setProperty(ControlsConfig.KEYS[9], "KEY_C");
   }

   public static String get(String var0) {
      System.out.println(var0 + "=" + props.getProperty(var0));
      return props.getProperty(var0);
   }

   private static void load() {
      try {
         Properties var0 = props;
         FileInputStream var1 = new FileInputStream(path);
         var0.load(var1);
      } catch (FileNotFoundException var2) {
         var2.printStackTrace();
      } catch (IOException var3) {
         var3.printStackTrace();
      }

   }

   public static void save() {
//      try {
//         Properties var1 = props;
//         FileOutputStream var0 = new FileOutputStream(path);
//         var1.store(var0, "JEmu2 config");
//      } catch (IOException var2) {
//         var2.printStackTrace();
//      }

   }

   public static void set(String var0, String var1) {
      props.setProperty(var0, var1);
      save();
   }
}
