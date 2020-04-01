package jef.sound;

public final class JavaSoundImpl implements Sound {
   int LINE_BUF_SIZE;
   int channels;
   int framesize;
   boolean running;
   int samfreq;
   SoundChip sc;

   public JavaSoundImpl(SoundChip var1, int var2, int var3, int var4) {
      this.sc = var1;
      this.channels = var4;
      this.framesize = var3;
      this.LINE_BUF_SIZE = Settings.SOUND_BUFFER_SIZE;
      this.samfreq = var2;
   }

   public void finalize() {
      this.terminate();

      try {
         super.finalize();
      } catch (Throwable var2) {
         ;
      }

   }

   public void init() {
      this.running = false;
   }

   public void terminate() {
      this.running = false;
   }

   public void update() {
      this.sc.writeBuffer();
      this.sc.getBufferLength();
      int var1 = this.channels;
   }
}
