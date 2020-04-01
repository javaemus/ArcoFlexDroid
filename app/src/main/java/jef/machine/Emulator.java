package jef.machine;

import jef.scoring.HighScoreParser;
import jef.video.BitMap;

public interface Emulator {
   int FPS = 0;
   int HEIGHT = 2;
   int NUMBER_OF_CONTROLLERS = 4;
   int ROT = 3;
   int WIDTH = 1;

   long getHighScore();

   double getProgress();

   int getProperty(int var1);

   void init(EmulatorProperties var1);

   boolean isHighScoreSupported();

   void keyPress(int var1);

   void keyRelease(int var1);

   BitMap refresh(boolean var1);

   void reset(boolean var1);

   void setHighScore(long var1);

   void setHighScoreHandler(HighScoreParser var1);

   void setHighScoreSupported(boolean var1);

   void setSound(boolean var1);
}
