import java.io.*;
import java.net.*;
import java.util.*;

import javax.swing.JOptionPane;

public class RunningServer extends Thread {
     Socket client;
     OutputStream output;
     Logic logic;

     int player,player2;
     Position[] positions = {new Position(1, 1), new Position(2, 2)};

     boolean clientRunning[] = {true, true};

     RunningServer(Socket client, OutputStream output, Logic logic) {
          this.client = client;
          this.output = output;
          this.logic = logic;

          player = output.cont++;
          player2 = 1 - player;
     }

     @Override
     public void run() {
          try {
               DataInputStream input = new DataInputStream(client.getInputStream());
               output.output[player] = new DataOutputStream(client.getOutputStream());

               String type, direction;

               if(player == 1) {
                    startServer();

                    tick();
               }

               do {
                    type = input.readUTF();

                    switch(type) {
                         case "move":
                              direction = input.readUTF();
                              System.out.println(direction);
                              logic.movement(player, direction);

                              break;

                            }

                    flushData();
               } while(clientRunning[player] && clientRunning[player2]);

               output.output[player].close();
               input.close();
               client.close();
          } catch(IOException e){
               try{
               client.close();
               } catch(IOException e1){
                    e1.printStackTrace();
               }
          } catch(NoSuchElementException e) {
               e.printStackTrace();
          }
     }

     public void startServer(){
          try{
               sendInitialPositions();
               sendPlayer2InitialPositions();

               for (int i = 3; i >= 1; i--) {
                    send("timer");
                    sendPlayer2("timer");
                    sleep(1000);
               }
          } catch (InterruptedException e) {
     }
     }

     private void flushData() {
          try {
               output.output[player].flush();
               output.output[player2].flush();
          } catch(IOException e) {
               e.printStackTrace();
          }
     }

     // ! PLAYER 1
     private void send(String i) {
          if (clientRunning[player]) {
               try {
                    output.output[player].writeUTF(i);
               } catch(IOException e) {
                    clientRunning[player] = false;
                    sendPlayer2Type("WON");
               }
          }
     }

     private void send(int i) {
          if (clientRunning[player]) {
               try {
                    output.output[player].writeInt(i);
               } catch (IOException e) {
                    clientRunning[player] = false;
                    sendPlayer2Type("WON");
               }
          }
     }

     private void sendType(String type) {
          send(type);
          sendPositions();
     }

     private void sendPositions() {
          sendVariationPosition(logic.getPosition(player));
          sendVariationPosition(logic.getPosition(player2));
     }

     private void sendVariationPosition(Position variation){
          send(variation.x);
          send(variation.y);
     }

     private void sendInitialPositions(){
          send(logic.INITIAL_POSITION_PLAYER1.x);
          send(logic.INITIAL_POSITION_PLAYER1.y);
          send(logic.INITIAL_POSITION_PLAYER2.x);
          send(logic.INITIAL_POSITION_PLAYER2.y);
     }


     //! PLAYER 2
     private void sendPlayer2(String i) {
          if (clientRunning[player2]) {
               try {
                    output.output[player2].writeUTF(i);
               } catch (IOException e) {
                    clientRunning[player2] = false;
                    send("LOST");
               }
          }
     }

     private void sendPlayer2(int i) {
          if (clientRunning[player2]) {
               try {
                    output.output[player2].writeInt(i);
               } catch (IOException e) {
                    clientRunning[player2] = false;
                    send("LOST");
               }
          }
     }

     private void sendPlayer2Type(String type) {
          sendPlayer2(type);
          sendPlayer2Positions();
     }

     private void sendPlayer2Positions() {
          sendPlayer2VariationPosition(logic.getPosition(player2));
          sendPlayer2VariationPosition(logic.getPosition(player));
     }

     private void sendPlayer2VariationPosition(Position variation) {
          sendPlayer2(variation.x);
          sendPlayer2(variation.y);
     }

     private void sendPlayer2InitialPositions() {
          sendPlayer2(logic.INITIAL_POSITION_PLAYER1.x);
          sendPlayer2(logic.INITIAL_POSITION_PLAYER1.y);
          sendPlayer2(logic.INITIAL_POSITION_PLAYER2.x);
          sendPlayer2(logic.INITIAL_POSITION_PLAYER2.y);
     }


     public void tick(){
          new Thread(){
               public void run() {
                    while(clientRunning[player] && clientRunning[player2]){
                         try{
                              synchronized(logic){
                                   sendType("COORDINATES");
                                   sendPlayer2Type("COORDINATES");
                                   flushData();

                                   logic.resetPosition();
                              }

                              sleep(120);
                         } catch (InterruptedException e) {
                         }
                    }
               }
          }.start();
     }

     public void endGame(){
          new Thread() {
               public void run() {
                    try {
                         sleep(3000);
                         clientRunning[player2] = false;
                    } catch (InterruptedException e) {
                    }
               }
          }.start();
     }
}
