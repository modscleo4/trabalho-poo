package UI;

import java.awt.Graphics;

public abstract class UI {
    private boolean visible = false;

    public boolean isVisible() {
        return this.visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    abstract void draw(Graphics g);
}
