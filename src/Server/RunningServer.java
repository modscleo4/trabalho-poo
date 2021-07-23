import java.io.*;
import java.net.*;
import java.util.*;

import javax.swing.JOptionPane;

public class RunningServer extends Thread {
     Socket client;
     OutputStream output;
     Logic logic;

     int player, enemy,player2;
     Position[] positions = {new Position(0, 0), new Position(0, 0)};

     boolean clientRunning[] = {true, true};

     RunningServer(Socket client, OutputStream output, Logic logic) {
          this.client = client;
          this.output = output;
          this.logic = logic;

          player = output.cont++;
          enemy = 1 - player;
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

                              logic.movement(player, direction);

                              break;
                         case "hit":
                              /*positions[player].x = input.readInt();
                              positions[player].y = input.readInt();
                              positions[enemy].x = input.readInt();
                              positions[enemy].y = input.readInt();

                              if (logic.attack(enemy, positions[player], positions[enemy])){
                                   synchronized (logic) {
                                        if(logic.getLifes(enemy) == 0)
                                        {
                                             sendType("WON");
                                             sendEnemyType("ENEMY WON");

                                             endGame();
                                        } else{
                                             sendType("HIT");
                                             sendEnemyType("ENEMY HIT");
                                        }

                                        sendLife();
                                        sendEnemyLife();

                                        sleep(120);
                                   }
                              } else {
                                   sendType("MISS");
                                   sendEnemyType("ENEMY MISS");
                              }
                              break;*/
                    }

                    flushData();
               } while(clientRunning[player] && clientRunning[enemy]);

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
          } catch (InterruptedException e) {
               e.printStackTrace();
          }
     }

     public void startServer(){
          try{
               sendInitialPositions();
               sendEnemyInitialPositions();

               for (int i = 10; i >= 1; i--) {
                    send("TIMER");
                    sendEnemy("TIMER");
                    sleep(1000);
               }

               sendLife();
               sendEnemyLife();
          } catch (InterruptedException e) {
     }
     }

     private void flushData() {
          try {
               output.output[player].flush();
               output.output[enemy].flush();
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
                    sendEnemyType("WON");
               }
          }
     }

     private void send(int i) {
          if (clientRunning[player]) {
               try {
                    output.output[player].writeInt(i);
               } catch (IOException e) {
                    clientRunning[player] = false;
                    sendEnemyType("WON");
               }
          }
     }

     private void sendType(String type) {
          send(type);
          sendPositions();
     }

     private void sendPositions() {
          sendVariationPosition(logic.getPosition(player));
          sendVariationPosition(logic.getPosition(enemy));
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
     private void sendEnemy(String i) {
          if (clientRunning[enemy]) {
               try {
                    output.output[enemy].writeUTF(i);
               } catch (IOException e) {
                    clientRunning[enemy] = false;
                    send("LOST");
               }
          }
     }

     private void sendEnemy(int i) {
          if (clientRunning[enemy]) {
               try {
                    output.output[enemy].writeInt(i);
               } catch (IOException e) {
                    clientRunning[enemy] = false;
                    send("LOST");
               }
          }
     }

     private void sendEnemyType(String type) {
          sendEnemy(type);
          sendEnemyPositions();
     }

     private void sendEnemyPositions() {
          sendEnemyVariationPosition(logic.getPosition(enemy));
          sendEnemyVariationPosition(logic.getPosition(player));
     }

     private void sendEnemyVariationPosition(Position variation) {
          sendEnemy(variation.x);
          sendEnemy(variation.y);
     }

     private void sendEnemyInitialPositions() {
          sendEnemy(logic.INITIAL_POSITION_PLAYER1.x);
          sendEnemy(logic.INITIAL_POSITION_PLAYER1.y);
          sendEnemy(logic.INITIAL_POSITION_PLAYER2.x);
          sendEnemy(logic.INITIAL_POSITION_PLAYER2.y);
     }

     private void sendEnemyLife(){
          int[] lifes = logic.getLifes();

          sendEnemy(lifes[enemy]);
          sendEnemy(lifes[player]);
     }

     public void tick(){
          new Thread(){
               public void run() {
                    while(clientRunning[player] && clientRunning[enemy]){
                         try{
                              synchronized(logic){
                                   sendType("COORDINATES");
                                   sendEnemyType("COORDINATES");
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
                         clientRunning[enemy] = false;
                    } catch (InterruptedException e) {
                    }
               }
          }.start();
     }
}
