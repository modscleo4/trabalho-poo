import java.io.*;
import java.net.*;

public class Server {
     // Definição da porta de conexão
     final static int port = 8080;

     public static void main(String[] args) {
          ServerSocket server = null;

          try {
               server = new ServerSocket(port);
          } catch (IOException e) {
               System.out.println("Could not listen on port: " + port + "\n" + e);
               System.exit(1);
          }

          System.out.println("Server is running...");
          System.out.println("Waiting for players...");

          OutputStream output = new OutputStream();
          Logic logic = new Logic();

          for (int i = 0; i < 2; i++) {
               Socket client = null;

               try {
                    client = server.accept();
               } catch (IOException e) {
                    System.out.println("Failed to connect to server: " + port);
                    System.out.println("Error: " + e);
                    System.exit(1);
               }

               System.out.println("Player " + (i + 1) + " entered the game.");

               new RunningServer(client, output, logic).start();
          }

          try {
               server.close();
          } catch (IOException e) {
               e.printStackTrace();
          }
     }
}
