import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    // Definição da porta de conexão
    private final static int PORT = 8080;

    public static void main(String[] args) {
        ServerSocket serverSocket = null;

        try {
            serverSocket = new ServerSocket(PORT);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Could not listen on port: " + PORT);
            System.exit(1);
        }

        System.out.println("Server is running...");
        System.out.println("Waiting for players...");

        while (true) {
            IJogo jogo = new Jogo();
            int numMaximoJogadores = jogo.numMaximoJogadores();

            for (int i = 0; i < numMaximoJogadores; i++) {
                Socket clientSocket = null;
                try {
                    System.out.println("Esperando conexao de um jogador.");
                    clientSocket = serverSocket.accept();
                } catch (IOException e) {
                    e.printStackTrace();
                    System.out.println("Accept falhou");
                    System.exit(1);
                }
                System.out.println("Accept Funcionou!");
                jogo.adicionaJogador(clientSocket);
            }

            System.out.println("Iniciando jogo");
            jogo.iniciaLogica(new Logica(jogo));
            jogo.inicia();
        }
    }
}
