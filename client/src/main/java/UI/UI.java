package UI;

import java.awt.Graphics2D;

public abstract class UI {
    private boolean visible = false;

    public boolean isVisible() {
        return this.visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public abstract void draw(Graphics2D g);
}
