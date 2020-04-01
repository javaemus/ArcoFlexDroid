package jef.sound.chip.okim6295;

public class ADPCMVoice {
   ADPCMState adpcm = new ADPCMState();
   int base_offset;
   int count;
   boolean playing;
   int sample;
   int volume;
}
