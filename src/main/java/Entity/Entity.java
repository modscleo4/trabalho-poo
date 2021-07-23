package Entity;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;

import javax.swing.Timer;
import javax.swing.event.MouseInputListener;

import Engine.AnimatedSprite;
import Engine.BaseObject;
import Engine.GameGlobals;

public abstract class Entity extends BaseObject implements KeyListener, MouseInputListener {
    private AnimatedSprite[] sprites;
    private int currentSprite;
    protected boolean interactibleWhenPaused = false;
    protected boolean useDirection = false;
    protected boolean isMoving = false;
    private String direction = "down";
    private Thread resetThread = null;

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

    protected void setCurrentSprite(int currentSprite) {
        if (currentSprite < 0 || currentSprite > this.sprites.length - 1) {
            return;
        }

        if (this.currentSprite != currentSprite) {
            this.currentSprite = currentSprite;
            this.getSprite().reset();
        } else {
            this.getSprite().resetIfEnded();
        }
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

    protected void resetAnimation(int index) {
        if (this.resetThread != null) {
            this.resetThread.interrupt();
            this.resetThread = null;
        }

        this.resetThread = new Thread(() -> {
            try {
                Thread.sleep(100 * (this.getSprite().length() - this.getSprite().getIndex()));
            } catch (InterruptedException e) {
                return;
            }

            if (!GameGlobals.paused) {
                this.setCurrentSprite(index);
            }
        });

        this.resetThread.start();
    }

    public AnimatedSprite getSprite() {
        return this.getSprites()[this.getCurrentSprite()];
    }

    public String getDirection() {
        return this.direction;
    }

    public void setDirection(String direction) {
        if (!direction.equals("right") && !direction.equals("left") && !direction.equals("up")
                && !direction.equals("down")) {
            return;
        }

        this.direction = direction;

        for (AnimatedSprite sprite : this.getSprites()) {
            if (sprite == null) {
                continue;
            }

            sprite.setDirection(direction);
        }
    }

    public boolean isUsingDirection() {
        return this.useDirection;
    }

    public void setUseDirection(boolean useDirection) {
        this.useDirection = useDirection;

        for (AnimatedSprite sprite : this.getSprites()) {
            if (sprite == null) {
                continue;
            }

            sprite.setUseDirection(useDirection);
        }
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

    public void move(int dx, int dy, Runnable callback) {
        this.isMoving = true;

        if (dx > 0) {
            this.setDirection("right");
        } else if (dx < 0) {
            this.setDirection("left");
        } else if (dy < 0) {
            this.setDirection("up");
        } else if (dy > 0) {
            this.setDirection("down");
        }

        int screenX = this.getScreenX();
        int screenY = this.getScreenY();

        this.setX(this.getX() + dx);
        this.setY(this.getY() + dy);

        int targetScreenX = this.getScreenX();
        int targetScreenY = this.getScreenY();

        this.setScreenX(screenX);
        this.setScreenY(screenY);

        this.setAbsoluteCoords(true);
        new Timer(25, (ae) -> {
            if (this.getScreenX() == targetScreenX && this.getScreenY() == targetScreenY) {
                this.setAbsoluteCoords(false);
                this.isMoving = false;
                this.setScreenX(-1);
                this.setScreenY(-1);
                if (callback != null) {
                    callback.run();
                }
                ((Timer) ae.getSource()).stop();
                return;
            }

            this.setScreenX(this.getScreenX() + 2 * dx);
            this.setScreenY(this.getScreenY() + 2 * dy);
        }).start();
    }

    public void move(int dx, int dy) {
        this.move(dx, dy, null);
    }

    @Override
    public void draw(Graphics g) {
        AnimatedSprite sprite = this.getSprite();
        if (sprite == null) {
            return;
        }

        sprite.setX(this.getX());
        sprite.setY(this.getY());
        sprite.draw(g);
    }
}
