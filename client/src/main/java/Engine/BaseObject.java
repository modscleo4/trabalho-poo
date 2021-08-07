package Engine;

import java.awt.Graphics2D;

public abstract class BaseObject {
    private int x;
    private int y;
    private int screenX = -1;
    private int screenY = -1;
    private int width = GameGlobals.SPRITE_WIDTH;
    private int height = GameGlobals.SPRITE_HEIGHT;
    private boolean solid;
    private boolean absoluteCoords = false;
    private boolean flipped = false;
    private boolean visible = true;
    private boolean centerScreen = false;

    public int getX() {
        return this.x;
    }

    public int getScreenX() {
        if (this.screenX != -1) {
            return this.screenX;
        }

        if (this.isAbsoluteCoords()) {
            if (this.centerScreen) {
                return (GameGlobals.width - this.getWidth()) / 2;
            }

            return this.getX();
        }

        return this.getX() * GameGlobals.SPRITE_WIDTH
                + (GameGlobals.SPRITE_WIDTH - this.getWidth() > 0 ? (GameGlobals.SPRITE_WIDTH - this.getWidth()) / 2
                        : 0);
    }

    public void setX(int x) {
        if (this.getX() == x) {
            return;
        }

        this.x = x;
    }

    public void setScreenX(int screenX) {
        if (this.getScreenX() == screenX) {
            return;
        }

        this.screenX = screenX;
    }

    public int getY() {
        return y;
    }

    public int getScreenY() {
        if (this.screenY != -1) {
            return this.screenY;
        }

        if (this.isAbsoluteCoords()) {
            if (this.centerScreen) {
                return (GameGlobals.height - this.getHeight()) / 2;
            }

            return this.getY();
        }

        return this.getY() * GameGlobals.SPRITE_HEIGHT
                + (GameGlobals.SPRITE_HEIGHT - this.getHeight() > 0 ? (GameGlobals.SPRITE_HEIGHT - this.getHeight()) / 2
                        : 0);
    }

    public void setY(int y) {
        if (this.getY() == y) {
            return;
        }

        this.y = y;
    }

    public void setScreenY(int screenY) {
        if (this.getScreenY() == screenY) {
            return;
        }

        this.screenY = screenY;
    }

    public int getWidth() {
        return this.width;
    }

    public void setWidth(int width) {
        if (this.getWidth() == width) {
            return;
        }

        this.width = width;
    }

    public int getHeight() {
        return this.height;
    }

    public void setHeight(int height) {
        if (this.getHeight() == height) {
            return;
        }

        this.height = height;
    }

    public boolean isSolid() {
        return solid;
    }

    public void setSolid(boolean solid) {
        if (this.isSolid() == solid) {
            return;
        }

        this.solid = solid;
    }

    public boolean isFlipped() {
        return flipped;
    }

    public void setFlipped(boolean flipped) {
        if (this.isFlipped() == flipped) {
            return;
        }

        this.flipped = flipped;
    }

    public boolean isAbsoluteCoords() {
        return this.absoluteCoords;
    }

    public void setAbsoluteCoords(boolean absoluteCoords) {
        if (this.isAbsoluteCoords() == absoluteCoords) {
            return;
        }

        this.absoluteCoords = absoluteCoords;
    }

    public boolean isVisible() {
        return this.visible;
    }

    public void setVisible(boolean visible) {
        if (this.isVisible() == visible) {
            return;
        }

        this.visible = visible;
    }

    public boolean isCenterScreen() {
        return this.centerScreen;
    }

    public void setCenterScreen(boolean centerScreen) {
        if (this.isCenterScreen() == centerScreen) {
            return;
        }

        this.centerScreen = centerScreen;
    }

    public abstract void draw(Graphics2D g);
}
