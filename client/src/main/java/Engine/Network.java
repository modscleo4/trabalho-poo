package Engine;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Arrays;

import javax.swing.JOptionPane;

import Entity.Aubrey;
import Entity.Omori;

/* P1 - OMOLI, P2 - AUBREY */

public class Network {
    Socket socket = null;
    DataInputStream input = null;
    DataOutputStream output = null;

    boolean ready = false;

    public Network(String IP, int port) {
        try {
            this.socket = new Socket(IP, port);
            this.output = new DataOutputStream(this.socket.getOutputStream());
            this.input = new DataInputStream(this.socket.getInputStream());

            new Thread(() -> {
                while (true) {
                    this.readCommand();
                }
            }).start();
        } catch (UnknownHostException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Servidor não encontrado.", "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro na comunicação com o servidor", "Error",
                    JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }

    public boolean isReady() {
        return this.ready;
    }

    /**
    * Exemplos: ATTACK -> sendCommand("ATTACK")
    *           MOVE DX DY -> sendCommand("MOVE", new String[] {"DX", "DY"})
    *
    *
    * @param command Comando
    * @param args Argumentos do comando
    */
    public void sendCommand(String command, String[] args) {
        try {
            if (args == null) {
                output.writeUTF(command);
            } else {
                output.writeUTF(command + " " + String.join(" ", args));
            }

            output.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendCommand(String command) {
        this.sendCommand(command, null);
    }

    /**
     * Exemplos: READY
     *           ATTACK PX
     *           MOVE PX DX DY
     *           SPAWNENEMY X Y
     */
    public void readCommand() {
        try {
            String[] out = input.readUTF().split(" ");

            String command = out[0];
            String[] args = Arrays.copyOfRange(out, 1, out.length);

            switch (command) {
                case "READY":
                    if (args[0].equals("P1")) {
                        GameGlobals.player = new Omori(1, 1);
                        GameGlobals.player2 = new Aubrey(2, 2);
                    } else {
                        GameGlobals.player = new Aubrey(2, 2);
                        GameGlobals.player2 = new Omori(1, 1);
                    }

                    this.ready = true;
                    break;

                case "ATTACK":
                    if (args[0].equals("P1")) {
                        GameGlobals.player.attack(Integer.parseInt(args[1]));
                    } else if (args[0].equals("P2")) {
                        GameGlobals.player2.attack(Integer.parseInt(args[1]));
                    }

                    break;

                case "MOVE":
                    int dx = Integer.parseInt(args[1]);
                    int dy = Integer.parseInt(args[2]);

                    if (args[0].equals("P1")) {
                        GameGlobals.player.move(dx, dy);
                    } else if (args[0].equals("P2")) {
                        GameGlobals.player2.move(dx, dy);
                    }
                    break;

                default:
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
