package UI;

import java.awt.Graphics;

import Engine.GameGlobals;
import Map.MapManager;

public class NetLoadScreen {
    private boolean started = false;
    private boolean ended = false;
    private Text text;
    private Thread t;

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
            this.t = new Thread(() -> {
                while (!GameGlobals.network.isReady()) {
                    try {
                        Thread.sleep(5);
                    } catch (InterruptedException e) {

                    }
                }

                GameGlobals.netLoaded = true;
                GameGlobals.map = MapManager.getMap1();

                this.ended = true;
            });

            this.t.start();
        }

        this.started = true;
        this.text.draw(g);
    }
}
