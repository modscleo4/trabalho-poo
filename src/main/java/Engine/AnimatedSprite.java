package Engine;

import java.awt.Graphics;
import javax.swing.Timer;

public class AnimatedSprite extends Sprite {
    private String sprites[];
    private boolean animating;
    private boolean loop;
    private int timing;
    private int index;
    Timer t;

    public AnimatedSprite(String sprites[], boolean loop, int timing, int x, int y, boolean solid) {
        super(sprites[0], x, y, solid);
        this.sprites = sprites;
        this.timing = timing;
        this.loop = loop;
        this.index = 0;

        t = new Timer(this.timing, ae -> {
            if (GameGlobals.paused) {
                return;
            }

            if (!this.loop && this.index == this.sprites.length - 1) {
                this.stop();
                ((Timer) ae.getSource()).stop();
                return;
            }

            this.index++;

            this.index %= this.sprites.length;

            this.setPath(this.sprites[this.index]);
        });
    }

    public void reset() {
        this.index = 0;
    }

    public void resetIfEnded() {
        if (this.index == this.sprites.length - 1) {
            this.reset();
        }
    }

    public void animate() {
        if (this.animating || !this.loop && this.index == this.sprites.length) {
            return;
        }

        this.animating = true;

        t.start();
    }

    public void stop() {
        this.animating = false;

        t.stop();
    }

    public int length() {
        return this.sprites.length;
    }

    public int getIndex() {
        return this.index;
    }

    @Override
    public String getPath() {
        return this.sprites[this.index];
    }

    @Override
    public void draw(Graphics g) {
        this.animate();
        super.draw(g);
    }
}
