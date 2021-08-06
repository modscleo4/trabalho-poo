import java.net.Socket;

interface IJogo {
    int numMaximoJogadores();

    void adicionaJogador(Socket clientSocket);

    void iniciaLogica(ILogica logica);

    void inicia();
}
