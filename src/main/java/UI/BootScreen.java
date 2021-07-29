package UI;

import java.awt.Graphics;

import Engine.GameGlobals;
import Engine.SoundManager;
import Entity.Slime;

public class BootScreen {
    private boolean started = false;
    private boolean ended = false;
    private Slime slime;

    public BootScreen() {
        this.slime = new Slime((GameGlobals.width - GameGlobals.SPRITE_WIDTH) / 2,
                GameGlobals.height / 2 - GameGlobals.SPRITE_HEIGHT, true);
        this.slime.setAbsoluteCoords(true);
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

            new Thread(() -> {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    //
                }

                this.slime.getSprite().stop();
                this.slime.destroy();
                this.ended = true;
            }).start();
        }

        this.started = true;

        GameGlobals.paused = false;
        this.slime.draw(g);
        g.drawString("Rene Produções", (GameGlobals.width - 90) / 2,
                GameGlobals.height / 2 + GameGlobals.SPRITE_HEIGHT);
    }
}
