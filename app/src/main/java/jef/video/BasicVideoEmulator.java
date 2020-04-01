package jef.video;

import jef.machine.EmulatorProperties;

public class BasicVideoEmulator implements VideoEmulator, Eof_callback, VideoInitializer, VideoFinalizer, VideoRenderer, PaletteInitializer {
   protected BitMap bitmap;

   public void eof_callback() {
   }

   public void finalizeVideo() {
   }

   protected BitMap getDisplay() {
      return this.bitmap;
   }

   public void init(EmulatorProperties var1) {
   }

   public void initPalette() {
   }

   public int initVideo() {
      return 0;
   }

   public BitMap renderVideo() {
      return this.bitmap;
   }

   public void renderVideoPost() {
   }
}
