package UI;

import java.awt.Graphics;
import java.util.concurrent.atomic.AtomicInteger;

import Engine.GameGlobals;
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
                int total = SpriteManager.filesInDirectory().size() + SoundManager.filesInDirectory().size();
                this.progressBar.setPercentage(0);

                AtomicInteger p = new AtomicInteger(0);
                SpriteManager.loadAll((i) -> {
                    p.set(i);
                    this.progressBar.setPercentage(i * 100 / total);
                });

                SoundManager.loadAll((i) -> {
                    this.progressBar.setPercentage((p.get() + i) * 100 / total);
                });

                GameGlobals.loaded = true;

                this.ended = true;
            }).start();
        }

        this.started = true;
        this.progressBar.draw(g);
    }
}
