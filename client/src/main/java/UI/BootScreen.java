package UI;

import java.awt.Graphics2D;

import Engine.GameGlobals;
import Engine.SoundManager;
import Entity.Slime;
import UI.Components.Text;

public class BootScreen extends UI {
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

    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);

        this.slime.setVisible(visible);
        this.txt.setVisible(visible);
    }

    @Override
    public void draw(Graphics2D g) {
        if (this.ended) {
            return;
        }

        if (!this.started) {
            this.setVisible(true);

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

                this.ended = true;

                this.setVisible(false);
            });

            this.t.start();
        }

        this.started = true;

        GameGlobals.paused = false;
        this.slime.draw(g);
        this.txt.draw(g);
    }

    @Override
    public void close() {
        super.close();

        this.slime.close();
        this.txt.close();
    }
}
