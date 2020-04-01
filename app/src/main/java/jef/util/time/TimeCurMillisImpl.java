package jef.util.time;

class TimeCurMillisImpl implements TimeRT {
   long startMillis = System.currentTimeMillis();

   TimeCurMillisImpl() {
      System.out.println("System.curTimeMillis timer selected");
   }

   public long getMillis() {
      return System.currentTimeMillis() - this.startMillis;
   }

   public double getMillisDouble() {
      return (double)(System.currentTimeMillis() - this.startMillis);
   }

   public long getNanos() {
      return (System.currentTimeMillis() - this.startMillis) * 1000000L;
   }
}
