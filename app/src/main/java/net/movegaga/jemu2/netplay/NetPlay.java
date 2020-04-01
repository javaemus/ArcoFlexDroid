package net.movegaga.jemu2.netplay;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public abstract class NetPlay {
   static final boolean TRACE = false;
   public int clients;
   public int id;
   ArrayList[] inControllerUpdates;
   ArrayList outControllerUpdates = new ArrayList();
   ArrayList outControllerUpdatesBuffer = new ArrayList();

   protected void bufferOutUpdates() {
      this.outControllerUpdatesBuffer.clear();

      for(int var1 = 0; var1 < this.outControllerUpdates.size(); ++var1) {
         this.outControllerUpdatesBuffer.add(this.outControllerUpdates.get(var1));
      }

   }

   public abstract void close() throws IOException;

   public List getBufferedSentUpdates() {
      return this.outControllerUpdatesBuffer;
   }

   public List getReceivedControllerUpdates() {
      return this.inControllerUpdates[0];
   }

   public void queueControllerUpdate(ControllerUpdate var1) {
      this.outControllerUpdates.add(var1);
   }

   public abstract void start() throws UnknownHostException, IOException;

   public abstract void sync() throws IOException;

   public String toString() {
      return Integer.toString(this.id);
   }
}
