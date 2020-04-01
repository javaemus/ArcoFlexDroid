package jef.sound;

public abstract class SoundChip {
   static final int FRAME_BUFFERS = 3;
   public static final int LEFT = 0;
   public static final int MONO = 1;
   public static final int RIGHT = 1;
   public static final int STEREO = 2;
   public int LINE_BUFSIZE;
   public int SAMPLE_FREQUENCY;
   boolean autoresize;
   int bufferLength;
   private int channels;
   protected boolean enabled;
   private int fps;
   public int frameSize;
   protected int[] linBuffer;
   protected byte[] linByteStream;
   protected boolean mono;
   protected byte[] muBuffer;
   private Sound stream;

   public SoundChip() {
      this.LINE_BUFSIZE = Settings.SOUND_BUFFER_SIZE;
      this.enabled = true;
      this.channels = 1;
      this.autoresize = false;
   }

   public void clearBuffer() {
      for(int var1 = 0; var1 < this.linBuffer.length; ++var1) {
         this.linBuffer[var1] = 0;
      }

   }

   public void disable() {
      if(this.enabled) {
         this.enabled = false;
         this.stream.terminate();
      }

   }

   public void enable() {
      if(!this.enabled) {
         this.enabled = true;
         this.init(this.mono, this.SAMPLE_FREQUENCY, this.fps);
      }

   }

   public int getBufferLength() {
      return this.bufferLength;
   }

   public byte[] getByteStream() {
      for(int var1 = 0; var1 < this.linBuffer.length; ++var1) {
         this.linByteStream[var1 << 1] = (byte)(this.linBuffer[var1] >> 8);
         this.linByteStream[(var1 << 1) + 1] = (byte)(this.linBuffer[var1] & 255);
      }

      return this.linByteStream;
   }

   public int getSampFreq() {
      return this.SAMPLE_FREQUENCY;
   }

   public void init(boolean var1, int var2, int var3) {
      this.SAMPLE_FREQUENCY = var2;
      this.mono = var1;
      this.fps = var3;
      this.autoresize = true;
      if(var1) {
         this.initChannels(1);
      } else {
         this.initChannels(2);
      }

      System.out.println("SoundChip " + this.toString() + " initialized.");
   }

   protected void initChannels(int var1) {
      this.channels = var1;
      this.frameSize = this.SAMPLE_FREQUENCY / this.fps * this.channels;
      this.linBuffer = new int[this.frameSize * 3];
      this.linByteStream = new byte[this.frameSize * 2 * 3];
      this.stream = new JavaSoundImpl(this, this.SAMPLE_FREQUENCY, this.frameSize, this.channels);
      this.stream.init();
   }

   protected int readLinBuffer(int var1) {
      return this.linBuffer[var1];
   }

   protected int readLinBuffer(int var1, int var2) {
      return this.linBuffer[this.channels * var2 + var1];
   }

   public void update(float var1) {
      if(this.autoresize) {
         this.bufferLength = (int)((float)this.SAMPLE_FREQUENCY / (1000.0F / var1) + 0.5F);
         if(this.bufferLength > this.linBuffer.length / this.channels) {
            this.bufferLength = this.linBuffer.length / this.channels;
         }
      } else {
         this.bufferLength = this.SAMPLE_FREQUENCY / this.fps;
      }

      if(this.enabled) {
         this.stream.update();
      }

   }

   public abstract void writeBuffer();

   protected void writeLinBuffer(int var1, int var2) {
      this.linBuffer[var1] = var2;
   }

   protected void writeLinBuffer(int var1, int var2, int var3) {
      this.linBuffer[this.channels * var2 + var1] = var3;
   }
}
