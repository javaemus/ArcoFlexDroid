package net.movegaga.jemu2.netplay;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class Client extends NetPlay {
   InputStream fromServer;
   final String host;
   final int port;
   Socket socket;
   OutputStream toServer;

   public Client(String var1, int var2) {
      this.port = var2;
      this.host = var1;
      System.out.println("Client set up to connect to " + var1 + " on port " + var2);
   }

   public void close() throws IOException {
      this.toServer.write(99);
      this.fromServer.close();
      this.toServer.close();
      this.socket.close();
   }

   public void sendControllerUpdate(ControllerUpdate var1) {
      this.outControllerUpdates.add(var1);
   }

   public void start() throws IOException {
      this.socket = new Socket(this.host, this.port);
      this.fromServer = this.socket.getInputStream();
      this.toServer = this.socket.getOutputStream();
      this.id = this.fromServer.read();
   }

   public void sync() throws IOException {
      int var2;
      do {
         var2 = this.fromServer.read();
         switch(var2) {
         case 3:
            this.toServer.write(9);
            break;
         case 4:
            for(int var1 = 0; var1 < this.outControllerUpdates.size(); ++var1) {
               ControllerUpdate var3 = (ControllerUpdate)this.outControllerUpdates.get(var1);
               this.toServer.write(6);
               this.toServer.write(var3.type);
               this.toServer.write(var3.controller);
               this.toServer.write(var3.value);
            }

            this.toServer.write(7);
            break;
         case 6:
            this.fromServer.read();
            this.fromServer.read();
            this.fromServer.read();
            break;
         case 99:
            throw new IOException("Server ended NetPlay session.");
         default:
            throw new IOException("Message from server " + var2 + " not expected.");
         }
      } while(var2 != 3);

      this.bufferOutUpdates();
      this.outControllerUpdates.clear();
   }
}
