public class Logica implements ILogica {
    Jogo jogo;

    public boolean jogando;

    Logica(IJogo jogo) {
        this.jogo = (Jogo) jogo;
    }

    public void executa() {
        this.jogo.sendCommand(0, "READY", new String[] { "P1" });
        this.jogo.sendCommand(1, "READY", new String[] { "P2" });

        new Thread(() -> {
            while (jogo.clienteVivo[0] && jogo.clienteVivo[1]) {
                //
            }
        }).start();
    }

    public int getDamage() {
        return (int) Math.ceil(5 + Math.random() * (10 - 5));
    }
}
