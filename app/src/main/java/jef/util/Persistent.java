package jef.util;

public interface Persistent {
   ByteArray getState();

   void restoreState(ByteArray var1);
}
