package Engine;

import java.awt.Graphics;
import javax.swing.Timer;

public class AnimatedSprite extends Sprite {
    private String spritesPath;
    private String[] sprites;
    private boolean animating;
    private boolean loop;
    private int timing;
    private int index;
    Timer t;

    public AnimatedSprite(String spritesPath, boolean loop, int timing, int x, int y, boolean solid) {
        super("", x, y, solid);
        this.spritesPath = spritesPath;
        this.timing = timing;
        this.loop = loop;
        this.index = 0;

        this.t = new Timer(this.timing, ae -> {
            if (GameGlobals.paused) {
                return;
            }

            if (!this.loop && this.index == this.getSprites().length - 1) {
                this.stop();
                ((Timer) ae.getSource()).stop();
                return;
            }

            this.index++;

            this.index %= this.getSprites().length;

            this.setPath(this.getSprites()[this.index]);
        });
    }

    public String[] getSprites() {
        if (this.sprites == null) {
            if (this.isUsingDirection()) {
                this.sprites = SpriteManager.spritesInDirectory(this.spritesPath + "/" + this.getDirection())
                        .toArray(new String[0]);
            } else {
                this.sprites = SpriteManager.spritesInDirectory(this.spritesPath).toArray(new String[0]);
            }
        }

        return this.sprites;
    }

    public void reset() {
        this.index = 0;
    }

    public void resetIfEnded() {
        if (this.index == this.getSprites().length - 1) {
            this.reset();
        }
    }

    public void animate() {
        if (this.animating || !this.loop && this.index == this.getSprites().length) {
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
        return this.getSprites().length;
    }

    public int getIndex() {
        return this.index;
    }

    @Override
    public String getPath() {
        return this.getSprites()[this.index];
    }

    @Override
    public void setDirection(String direction) {
        super.setDirection(direction);

        this.sprites = SpriteManager.spritesInDirectory(this.spritesPath + "/" + this.getDirection())
                .toArray(new String[0]);
    }

    @Override
    public void draw(Graphics g) {
        this.animate();
        super.draw(g);
    }
}
