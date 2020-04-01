package net.movegaga.jemu2.netplay;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server extends NetPlay {
   Socket[] clientSocket;
   DataInputStream[] fromClient;
   final int port;
   ServerSocket serverSocket;
   DataOutputStream[] toClient;

   public Server(int var1) {
      this.port = var1;
      this.id = 0;
      this.clients = 1;
      System.out.println("Server initialized on port " + var1);
   }

   public void close() throws IOException {
      for(int var1 = 0; var1 < this.clients; ++var1) {
         this.toClient[var1].write(99);
         this.toClient[var1].close();
         this.fromClient[var1].close();
         this.clientSocket[var1].close();
      }

      this.serverSocket.close();
   }

   public void start() throws IOException {
      System.out.println("Server waiting for client...");
      this.serverSocket = new ServerSocket(this.port);
      this.clientSocket = new Socket[this.clients];
      this.toClient = new DataOutputStream[this.clients];
      this.fromClient = new DataInputStream[this.clients];
      this.inControllerUpdates = new ArrayList[this.clients];

      for(int var1 = 0; var1 < this.clients; ++var1) {
         this.clientSocket[var1] = this.serverSocket.accept();
         this.toClient[var1] = new DataOutputStream(this.clientSocket[var1].getOutputStream());
         this.fromClient[var1] = new DataInputStream(this.clientSocket[var1].getInputStream());
         this.toClient[var1].write(var1 + 1);
      }

   }

   public void sync() throws IOException {
      for(int var1 = 0; var1 < this.clients; ++var1) {
         int var2 = this.fromClient[var1].read();
         if(var2 != var1) {
            throw new IOException("Read client id=" + var2 + ", expected " + var1);
         }

         this.inControllerUpdates[var1].clear();

         for(var2 = 0; var2 < this.outControllerUpdates.size(); ++var2) {
            ControllerUpdate var4 = (ControllerUpdate)this.outControllerUpdates.get(var2);
            this.toClient[var1].write(6);
            this.toClient[var1].write(var4.type);
            this.toClient[var1].write(var4.controller);
            this.toClient[var1].write(var4.value);
         }

         this.toClient[var1].write(4);
         boolean var5 = true;

         boolean var6;
         do {
            int var3 = this.fromClient[var1].read();
            switch(var3) {
            case 6:
               this.fromClient[var1].read();
               this.fromClient[var1].read();
               this.fromClient[var1].read();
               var6 = var5;
               break;
            case 7:
               var6 = false;
               break;
            default:
               throw new IOException("Message from client " + var3 + " not expected.");
            }

            var5 = var6;
         } while(var6);

         this.toClient[var1].write(3);
         if(this.fromClient[var1].read() != 9) {
            throw new IOException("Client out of sync!");
         }
      }

      this.bufferOutUpdates();
      this.outControllerUpdates.clear();
   }
}
