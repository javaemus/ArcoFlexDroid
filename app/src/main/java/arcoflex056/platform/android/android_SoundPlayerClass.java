/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package arcoflex056.platform.android;

import arcoflex056.platform.platformConfigurator;

import android.media.AudioAttributes;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;

import static mame056.mame.Machine;

/**
 *
 * @author chusogar
 */
public class android_SoundPlayerClass implements platformConfigurator.i_SoundPlayer_class{

    private AudioTrack _audioTrack;

    public android_SoundPlayerClass(){
        super();
    }

    @Override
    public void createAudioFormat(int stereo) {
        System.out.println("--> createAudioFormat");
        /*_audioTrack = new AudioTrack(
                AudioManager.STREAM_MUSIC,
                Machine.sample_rate,
                (stereo!=0) ? AudioFormat.CHANNEL_OUT_STEREO : AudioFormat.CHANNEL_OUT_MONO,
                //(bits == 8) ? AudioFormat.ENCODING_PCM_8BIT : AudioFormat.ENCODING_PCM_16BIT,
                AudioFormat.ENCODING_PCM_8BIT,
                1024, //bufSizeBytes,
                AudioTrack.MODE_STREAM);*/
        _audioTrack = new AudioTrack(3, 44100, 2, 2, 7056, 1);
    }

    @Override
    public boolean isLineSupported() {
        System.out.println("--> isLineSupported");
        return true;
    }

    @Override
    public Object getAudioFormat() {
        System.out.println("--> getAudioFormat");
        return "audio dummy for android";
    }

    @Override
    public void getLine() throws Exception {
        System.out.println("--> getLine");
    }

    @Override
    public void Play() {

        System.out.println("--> Play");
        _audioTrack.play();
    }

    @Override
    public void Stop() {

        System.out.println("--> Stop");
        _audioTrack.stop();
    }

    @Override
    public void write(byte[] waveBuffer, int offset, int length) {
        //System.out.println("--> Write");
        _audioTrack.write(waveBuffer, 0, length);
    }

}
