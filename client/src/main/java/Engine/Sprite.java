package Engine;

import java.awt.Graphics2D;
import java.awt.Image;

public class Sprite extends BaseObject {
    private String path;
    protected boolean useDirection = false;
    private String direction = "down";

    public Sprite(String path, int x, int y, boolean solid) {
        this.setPath(path);
        this.setX(x);
        this.setY(y);
        this.setSolid(solid);
    }

    public String getPath() {
        return this.path;
    }

    protected void setPath(String path) {
        this.path = path;
    }

    public Image getImage() {
        String path = this.getPath();

        return SpriteManager.getImage(path);
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public boolean isUsingDirection() {
        return useDirection;
    }

    public void setUseDirection(boolean useDirection) {
        this.useDirection = useDirection;
    }

    @Override
    public void draw(Graphics2D g) {
        if (this.getImage() == null) {
            return;
        }

        if (!this.isVisible()) {
            return;
        }

        g.drawImage(this.getImage(), this.getScreenX() + (this.isFlipped() ? this.getWidth() : 0), this.getScreenY(),
                (this.isFlipped() ? -1 : 1) * this.getWidth(), this.getHeight(), null);
    }
}
