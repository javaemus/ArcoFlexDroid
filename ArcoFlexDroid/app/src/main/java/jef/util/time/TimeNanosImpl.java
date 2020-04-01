package jef.util.time;

class TimeNanosImpl implements TimeRT {
   public TimeNanosImpl() {
      System.out.println("System.nanoTime timer selected.");
   }

   public long getMillis() {
      return System.nanoTime() / 1000000L;
   }

   public double getMillisDouble() {
      return (double)System.nanoTime() / 1000000.0D;
   }

   public long getNanos() {
      return System.nanoTime();
   }
}
