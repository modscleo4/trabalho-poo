package Engine;

import java.awt.Graphics2D;
import javax.swing.Timer;

public class AnimatedSprite extends Sprite {
    private String spritesPath;
    private String[] sprites;
    private boolean animating;
    private boolean loop;
    private int timing;
    private int index;
    private boolean preAnimated = true;
    Timer t;

    public AnimatedSprite(String spritesPath, boolean loop, int timing, int x, int y, boolean solid) {
        super("", x, y, solid);
        this.spritesPath = spritesPath;
        this.setTiming(timing);
        this.loop = loop;
        this.setIndex(0);

        this.t = new Timer(this.timing, ae -> {
            // não tem pause em jogo online
            /*if (GameGlobals.paused) {
                return;
            }*/

            if (this.index == this.getSprites().length) {
                if (this.loop) {
                    this.reset();
                } else {
                    this.stop();
                    ((Timer) ae.getSource()).stop();
                    return;
                }
            } else {
                this.setPath(this.getSprites()[this.index++]);
            }
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
        this.setIndex(0);
    }

    public void resetIfEnded() {
        if (this.index == this.getSprites().length) {
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

    private void setIndex(int index) {
        this.index = index;
        this.setPath(this.getSprites()[this.index]);
    }

    public int getTiming() {
        return this.timing;
    }

    public void setTiming(int timing) {
        this.timing = timing;
    }

    public boolean isPreAnimated() {
        return this.preAnimated;
    }

    public void setPreAnimated(boolean preAnimated) {
        this.preAnimated = preAnimated;
    }

    public boolean isAnimating() {
        return this.t.isRunning();
    }

    @Override
    public void setUseDirection(boolean useDirection) {
        super.setUseDirection(useDirection);

        if (useDirection) {
            this.sprites = SpriteManager.spritesInDirectory(this.spritesPath + "/" + this.getDirection())
                    .toArray(new String[0]);
        } else {
            this.sprites = SpriteManager.spritesInDirectory(this.spritesPath).toArray(new String[0]);
        }
        this.reset();
    }

    @Override
    public void setDirection(String direction) {
        super.setDirection(direction);

        this.sprites = SpriteManager.spritesInDirectory(this.spritesPath + "/" + this.getDirection())
                .toArray(new String[0]);
        this.reset();
    }

    @Override
    public void draw(Graphics2D g) {
        super.draw(g);
    }
}
