package net.arcoflexdroid;

import android.media.AudioTrack;

public class SoundThread implements Runnable {

    protected Thread t = null;
    protected AudioTrack audioTrack;

    byte[] buff;
    int buff_size;

    public void setAudioTrack(AudioTrack audioTrack) {
        this.audioTrack = audioTrack;
    }

    public SoundThread() {
        // Size: (44100/30fps) * bytesize * stereo * (2 buffers)
        buff = new byte[1470 * 2 * 2 * 2];
        buff_size = 0;
        t = new Thread(this, "sound-Thread");
        t.start();
    }

    @Override
    public void run() {

        while (true) {
            synchronized (this) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            if (audioTrack != null) {
                audioTrack.write(buff, 0, buff_size);
            }
        }
    }

    synchronized public void writeSample(byte[] b, int size) {

        System.arraycopy(b, 0, buff, 0, size);
        buff_size = size;
        this.notify();
    }

}

