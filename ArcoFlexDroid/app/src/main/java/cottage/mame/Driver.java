package cottage.mame;

import java.net.URL;

import jef.machine.Emulator;

public interface Driver {
   String getDriverInfo();

   String getGameInfo();

   Emulator getMachine(URL var1, String var2);

   int getProperty(int var1);

   void setProperty(int var1, int var2);
}
