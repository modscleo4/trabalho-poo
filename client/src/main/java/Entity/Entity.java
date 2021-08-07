package Entity;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;

import javax.swing.event.MouseInputListener;

import Engine.AnimatedSprite;
import Engine.BaseObject;
import Engine.GameGlobals;

public abstract class Entity extends BaseObject implements KeyListener, MouseInputListener, AutoCloseable {
    private AnimatedSprite[] sprites;
    private int currentSprite;
    protected boolean interactibleWhenPaused = false;

    public Entity(AnimatedSprite[] sprites, int x, int y) {
        this.sprites = sprites;
        this.currentSprite = 0;
        this.setX(x);
        this.setY(y);

        GameGlobals.keyListeners.add(this);
        GameGlobals.mouseInputListeners.add(this);
    }

    public void handleKeyPressed(KeyEvent e) {

    }

    public void handleKeyReleased(KeyEvent e) {

    }

    public void handleMousePressed(MouseEvent e) {

    }

    public void handleMouseReleased(MouseEvent e) {

    }

    public void handleMouseEntered(MouseEvent e) {

    }

    public void handleMouseExited(MouseEvent e) {

    }

    @Override
    public final void keyPressed(KeyEvent e) {
        if ((GameGlobals.paused || GameGlobals.result != "running") && !this.interactibleWhenPaused) {
            return;
        }

        this.handleKeyPressed(e);
    }

    @Override
    public final void keyReleased(KeyEvent e) {
        if ((GameGlobals.paused || GameGlobals.result != "running") && !this.interactibleWhenPaused) {
            return;
        }

        this.handleKeyReleased(e);
    }

    @Override
    public final void keyTyped(KeyEvent e) {
        //
    }

    @Override
    public final void mouseClicked(MouseEvent e) {
        //
    }

    @Override
    public final void mouseEntered(MouseEvent e) {
        //
    }

    @Override
    public final void mouseExited(MouseEvent e) {
        //
    }

    @Override
    public final void mousePressed(MouseEvent e) {
        if ((GameGlobals.paused || GameGlobals.result != "running") && !this.interactibleWhenPaused) {
            return;
        }

        if (e.getX() < this.getScreenX() || e.getX() > this.getScreenX() + this.getWidth()
                || e.getY() < this.getScreenY() || e.getY() > this.getScreenY() + this.getHeight()) {
            return;
        }

        this.handleMousePressed(e);
    }

    @Override
    public final void mouseReleased(MouseEvent e) {
        if ((GameGlobals.paused || GameGlobals.result != "running") && !this.interactibleWhenPaused) {
            return;
        }

        if (e.getX() < this.getScreenX() || e.getX() > this.getScreenX() + this.getWidth()
                || e.getY() < this.getScreenY() || e.getY() > this.getScreenY() + this.getHeight()) {
            return;
        }

        this.handleMouseReleased(e);
    }

    @Override
    public final void mouseDragged(MouseEvent e) {
        //
    }

    @Override
    public final void mouseMoved(MouseEvent e) {
        if ((GameGlobals.paused || GameGlobals.result != "running") && !this.interactibleWhenPaused) {
            return;
        }

        if (e.getX() < this.getScreenX() || e.getX() > this.getScreenX() + this.getWidth()
                || e.getY() < this.getScreenY() || e.getY() > this.getScreenY() + this.getHeight()) {
            this.handleMouseExited(e);
        } else {
            this.handleMouseEntered(e);
        }
    }

    public void setSprites(AnimatedSprite[] sprites) {
        this.sprites = sprites;
    }

    public AnimatedSprite[] getSprites() {
        return sprites;
    }

    public int getCurrentSprite() {
        return this.currentSprite;
    }

    protected void setCurrentSprite(int currentSprite, boolean animate) {
        if (currentSprite < 0 || currentSprite > this.sprites.length - 1) {
            return;
        }

        this.getSprite().stop();

        if (this.currentSprite != currentSprite) {
            this.currentSprite = currentSprite;
            this.getSprite().reset();
        } else {
            this.getSprite().resetIfEnded();
        }

        if (animate && !this.getSprite().isAnimating()) {
            this.getSprite().animate();
        }
    }

    protected void setCurrentSprite(int currentSprite) {
        this.setCurrentSprite(currentSprite, true);
    }

    @Override
    public void setX(int x) {
        super.setX(x);

        for (AnimatedSprite sprite : this.getSprites()) {
            if (sprite == null) {
                continue;
            }

            sprite.setX(x);
        }
    }

    @Override
    public void setY(int y) {
        super.setY(y);

        for (AnimatedSprite sprite : this.getSprites()) {
            if (sprite == null) {
                continue;
            }

            sprite.setY(y);
        }
    }

    @Override
    public void setWidth(int width) {
        super.setWidth(width);

        for (AnimatedSprite sprite : this.getSprites()) {
            if (sprite == null) {
                continue;
            }

            sprite.setWidth(width);
        }
    }

    @Override
    public void setHeight(int height) {
        super.setHeight(height);

        for (AnimatedSprite sprite : this.getSprites()) {
            if (sprite == null) {
                continue;
            }

            sprite.setHeight(height);
        }
    }

    @Override
    public void setFlipped(boolean flipped) {
        super.setFlipped(flipped);

        for (AnimatedSprite sprite : this.getSprites()) {
            if (sprite == null) {
                continue;
            }

            sprite.setFlipped(flipped);
        }
    }

    @Override
    public void setAbsoluteCoords(boolean absoluteCoords) {
        super.setAbsoluteCoords(absoluteCoords);

        for (AnimatedSprite sprite : this.getSprites()) {
            if (sprite == null) {
                continue;
            }

            sprite.setAbsoluteCoords(absoluteCoords);
        }
    }

    public AnimatedSprite getSprite() {
        return this.getSprites()[this.getCurrentSprite()];
    }

    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);

        for (AnimatedSprite sprite : this.getSprites()) {
            if (sprite == null) {
                continue;
            }

            sprite.setVisible(visible);
        }
    }

    @Override
    public void setCenterScreen(boolean centerScreen) {
        super.setCenterScreen(centerScreen);

        for (AnimatedSprite sprite : this.getSprites()) {
            if (sprite == null) {
                continue;
            }

            sprite.setCenterScreen(centerScreen);
        }
    }

    @Override
    public void setScreenX(int screenX) {
        super.setScreenX(screenX);

        for (AnimatedSprite sprite : this.getSprites()) {
            if (sprite == null) {
                continue;
            }

            sprite.setScreenX(screenX);
        }
    }

    @Override
    public void setScreenY(int screenY) {
        super.setScreenY(screenY);

        for (AnimatedSprite sprite : this.getSprites()) {
            if (sprite == null) {
                continue;
            }

            sprite.setScreenY(screenY);
        }
    }

    @Override
    public void draw(Graphics2D g) {
        AnimatedSprite sprite = this.getSprite();
        if (sprite == null) {
            return;
        }

        sprite.setX(this.getX());
        sprite.setY(this.getY());
        sprite.draw(g);
    }

    public void destroy() {
        GameGlobals.keyListeners.remove(this);
        GameGlobals.mouseInputListeners.remove(this);
    }

    @Override
    public void close() {
        this.destroy();
    }
}
