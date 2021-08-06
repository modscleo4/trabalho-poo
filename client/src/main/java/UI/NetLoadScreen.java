package UI;

import java.awt.Graphics;

import Engine.GameGlobals;

public class NetLoadScreen {
    private boolean started = false;
    private boolean ended = false;
    private Text text;

    public NetLoadScreen() {
        this.text = new Text("Aguardando 2º jogador...", 0, 0);
        this.text.setAbsoluteCoords(true);
        this.text.setCenterScreen(true);
    }

    public boolean isEnded() {
        return this.ended;
    }

    public void reset() {
        this.started = false;
        this.ended = false;
    }

    public void draw(Graphics g) {
        if (this.ended) {
            return;
        }

        if (!this.started) {
            new Thread(() -> {
                while (!GameGlobals.network.isReady()) {
                    //
                }

                GameGlobals.netLoaded = true;

                this.ended = true;
            }).start();
        }

        this.started = true;
        this.text.draw(g);
    }
}
