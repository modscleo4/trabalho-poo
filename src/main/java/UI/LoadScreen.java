package UI;

import java.awt.Graphics;

import Engine.SoundManager;
import Engine.SpriteManager;

public class LoadScreen {
    private boolean started = false;
    private boolean ended = false;
    private ProgressBar progressBar;

    public LoadScreen() {
        this.progressBar = new ProgressBar(0, 0, 200, 20);
        this.progressBar.setCenterScreen(true);
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
                this.progressBar.setPercentage(0);
                SpriteManager.loadAll();
                this.progressBar.setPercentage(50);
                SoundManager.loadAll();
                this.progressBar.setPercentage(100);
                this.ended = true;
            }).start();
        }

        this.started = true;
        this.progressBar.draw(g);
    }
}
