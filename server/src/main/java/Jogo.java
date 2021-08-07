import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Arrays;
import java.util.NoSuchElementException;

public class Jogo implements IJogo {
    Socket clientSocket;
    DataOutputStream[] os = new DataOutputStream[2];
    int contaJogadoresConectados = 0;
    Logica logica;
    boolean continua = true;
    boolean clienteVivo[] = { true, true };

    public void adicionaJogador(Socket clientSocket) {
        this.clientSocket = clientSocket;
        int numDoJogador = contaJogadoresConectados++;
        try {
            os[numDoJogador] = new DataOutputStream(clientSocket.getOutputStream());
        } catch (IOException e) {
            continua = false;
            e.printStackTrace();
        }
        iniciaThreadDoCliente(clientSocket, numDoJogador);
    }

    void iniciaThreadDoCliente(Socket clientSocket, int numDoJogador) {
        int numJogador = numDoJogador;
        int numAdversario = 1 - numDoJogador;
        new Thread() {
            // vai existir uma thread para cada cliente
            @Override
            public void run() {
                try {
                    DataInputStream is = new DataInputStream(clientSocket.getInputStream());

                    do {
                        Player p = null;

                        if (logica != null && logica.players != null && numJogador < logica.players.size()) {
                            p = logica.players.get(numJogador);
                        }

                        String[] out = is.readUTF().split(" ");

                        String command = out[0];
                        String[] args = Arrays.copyOfRange(out, 1, out.length);

                        switch (command) {
                            case "MOVE":
                                if (!logica.movePlayer(numJogador, Integer.parseInt(args[0]),
                                        Integer.parseInt(args[1]))) {
                                    return;
                                }

                                sendCommand(numJogador, "MOVE", new String[] { "P1", args[0], args[1] });
                                sendCommand(numAdversario, "MOVE", new String[] { "P2", args[0], args[1] });

                                break;
                            case "ATTACK":
                                int x = p.x;
                                int y = p.y;

                                if (p.direction.equals("up")) {
                                    y--;
                                } else if (p.direction.equals("down")) {
                                    y++;
                                } else if (p.direction.equals("left")) {
                                    x--;
                                } else if (p.direction.equals("right")) {
                                    x++;
                                }

                                int damage = logica.getDamage();

                                if (!logica.hitEnemy(x, y, damage)) {
                                    return;
                                }

                                sendCommand(numJogador, "ATTACK", new String[] { "P1", "" + damage });
                                sendCommand(numAdversario, "ATTACK", new String[] { "P2", "" + damage });

                                break;
                        }
                    } while (clienteVivo[numJogador] && clienteVivo[numAdversario]);

                    os[numJogador].close();
                    is.close();
                    clientSocket.close();
                } catch (IOException e) {
                    try {
                        clientSocket.close();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                } catch (NoSuchElementException e) {
                }
            }
        }.start();
    }

    public int numMaximoJogadores() {
        return 2;
    }

    public void iniciaLogica(ILogica logica) {
        this.logica = (Logica) logica;
    }

    public void inicia() {
        logica.jogando = true;
        logica.executa();
    }

    void descarregaEnvio() {
        try {
            for (int i = 0; i < this.numMaximoJogadores(); i++) {
                os[i].flush();
            }
        } catch (IOException e) {
            //
        }
    }

    void sendCommand(int player, String command, String[] args) {
        try {
            if (args == null) {
                os[player].writeUTF(command);
            } else {
                os[player].writeUTF(command + " " + String.join(" ", args));
            }

            os[player].flush();
        } catch (IOException e) {
            //
        }
    }

    void sendCommand(int player, String command) {
        this.sendCommand(player, command, null);
    }

    void ready() {

    }

    void sendAttack() {

    }

    void sendMove() {

    }

    void sendLifes() {

    }

}