package jef.sound;

import net.movegaga.jemu2.Config;

import jef.util.time.Time;

public class Settings {
   public static boolean SOUND_AUTORESIZE_BUFFERS;
   public static int SOUND_BUFFER_SIZE;
   public static int SOUND_SAMPLING_FREQ = Integer.parseInt(Config.get("sound.freq"));
   public static boolean STEREO_DELAY = Config.get("sound.stereodelay").equalsIgnoreCase("true");
   public static boolean STEREO_ENHANCED = Config.get("sound.stereo").equalsIgnoreCase("true");

   static {
      boolean var0;
      if(Time.supportsNano()) {
         var0 = false;
      } else {
         var0 = true;
      }

      SOUND_AUTORESIZE_BUFFERS = var0;
      SOUND_BUFFER_SIZE = 4096;
   }

   public static void setAutoResizeBuffers(boolean var0) {
   }

   public static void setSamplingFreq(int var0) {
      SOUND_SAMPLING_FREQ = var0;
      Config.set("sound.freq", Integer.toString(var0));
   }

   public static void setStereoDelay(boolean var0) {
      STEREO_DELAY = var0;
      Config.set("sound.stereodelay", Boolean.toString(var0));
   }

   public static void setStereoEnhanced(boolean var0) {
      STEREO_ENHANCED = var0;
      Config.set("sound.stereo", Boolean.toString(var0));
   }
}
