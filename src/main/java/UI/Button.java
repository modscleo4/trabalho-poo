package UI;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;

public class Button extends Text {

    public Button(String text, int x, int y) {
        super(text, x, y);
        this.interactibleWhenPaused = true;
    }

    @Override
    public int getWidth() {
        return super.getWidth() + 4;
    }

    @Override
    public int getHeight() {
        return super.getHeight() + 4;
    }

    @Override
    public void handleMousePressed(MouseEvent e) {
        this.handler.mousePressed(e);
    }

    @Override
    public void handleMouseEntered(MouseEvent e) {
        this.setColor(Color.GRAY);
    }

    @Override
    public void handleMouseExited(MouseEvent e) {
        this.setColor(Color.BLACK);
    }

    @Override
    public void draw(Graphics g) {
        super.draw(g);
        g.drawRect(this.getX(), this.getY() - super.getHeight() / 2 - 8, this.getWidth(), this.getHeight());
    }
}
