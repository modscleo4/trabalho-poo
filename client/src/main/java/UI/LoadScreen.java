package UI;

import java.awt.Graphics2D;
import java.util.concurrent.atomic.AtomicInteger;

import Engine.GameGlobals;
import Engine.SoundManager;
import Engine.SpriteManager;
import UI.Components.ProgressBar;

public class LoadScreen extends UI {
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

    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);

        this.progressBar.setVisible(visible);
    }

    @Override
    public void draw(Graphics2D g) {
        if (this.ended) {
            return;
        }

        if (!this.started) {
            this.setVisible(true);

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

                this.setVisible(false);
            }).start();
        }

        this.started = true;
        this.progressBar.draw(g);
    }

    @Override
    public void close() {
        super.close();

        this.progressBar.close();
    }
}
