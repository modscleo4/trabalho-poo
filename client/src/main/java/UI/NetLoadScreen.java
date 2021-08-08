package UI;

import java.awt.Graphics2D;

import javax.swing.Timer;

import Engine.GameGlobals;
import UI.Components.Text;

public class NetLoadScreen extends UI {
    private boolean started = false;
    private boolean ended = false;
    private Text txt;
    private Thread t;

    public NetLoadScreen() {
        this.txt = new Text("Aguardando 2º jogador...", 0, 0);
        this.txt.setAbsoluteCoords(true);
        this.txt.setCenterScreen(true);
    }

    public boolean isEnded() {
        return this.ended;
    }

    public void reset() {
        this.started = false;
        this.ended = false;
    }

    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);

        this.txt.setVisible(visible);
    }

    @Override
    public void draw(Graphics2D g) {
        if (this.ended) {
            return;
        }

        if (!this.started) {
            this.setVisible(true);

            this.t = new Thread(() -> {
                while (!GameGlobals.network.isReady()) {
                    try {
                        Thread.sleep(5);
                    } catch (InterruptedException e) {

                    }
                }

                GameGlobals.netLoaded = true;

                new Timer(1000, (ae) -> {
                    if (!GameGlobals.result.equals("running")) {
                        ((Timer) ae.getSource()).stop();
                        return;
                    }

                    GameGlobals.map.runTimerCycle();
                }).start();

                this.ended = true;

                this.setVisible(false);
            });

            this.t.start();
        }

        this.started = true;
        this.txt.draw(g);
    }

    @Override
    public void close() {
        super.close();

        this.txt.close();
    }
}
