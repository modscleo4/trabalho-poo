package UI;

import java.awt.Graphics2D;

public abstract class UI implements AutoCloseable {
    private boolean visible = true;

    public boolean isVisible() {
        return this.visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public abstract void draw(Graphics2D g);

    @Override
    public void close() {
        this.setVisible(false);
    }
}
