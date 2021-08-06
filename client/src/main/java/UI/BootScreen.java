package UI;

import java.awt.Graphics;

import Engine.GameGlobals;
import Engine.Network;
import Engine.SoundManager;
import Entity.Slime;

public class BootScreen {
    private boolean started = false;
    private boolean ended = false;
    private Slime slime;
    private Text txt;
    private Thread t;

    public BootScreen() {
        this.slime = new Slime((GameGlobals.width - GameGlobals.SPRITE_WIDTH) / 2,
                GameGlobals.height / 2 - GameGlobals.SPRITE_HEIGHT, true);
        this.slime.setAbsoluteCoords(true);

        this.txt = new Text("Renê Produções", 0, 0);
        this.txt.setAbsoluteCoords(true);
        this.txt.setScreenY(GameGlobals.height / 2 + GameGlobals.SPRITE_HEIGHT);
        this.txt.setCenterScreen(true);
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
            SoundManager.playSound("start");
            this.slime.jump();

            this.t = new Thread(() -> {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    //
                }

                this.slime.getSprite().stop();
                this.slime.destroy();
                GameGlobals.network = new Network("127.0.0.1", 8080);
                this.ended = true;
                this.t.interrupt();
            });

            this.t.start();
        }

        this.started = true;

        GameGlobals.paused = false;
        this.slime.draw(g);
        this.txt.draw(g);
    }
}
