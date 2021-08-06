public class Logica implements ILogica {
    Jogo jogo;

    public boolean jogando;

    Logica(IJogo jogo) {
        this.jogo = (Jogo) jogo;
    }

    public void executa() {
        this.jogo.sendCommand(0, "READY");
        this.jogo.sendCommand(1, "READY");

        new Thread(() -> {
            while (jogo.clienteVivo[0] && jogo.clienteVivo[1]) {
                //
            }
        }).start();
    }

}
