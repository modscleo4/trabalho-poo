package UI.Components;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;

import Engine.GameGlobals;

public class Button extends Text {
    private Color activeColor = Color.GRAY;
    private Color inactiveColor = Color.BLACK;

    public Button(String text, int x, int y) {
        super(text, x, y);
        this.setColor(null);
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
        this.setColor(this.getActiveColor());

        if (GameGlobals.mainWindow != null) {
            GameGlobals.mainWindow.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        }
    }

    @Override
    public void handleMouseExited(MouseEvent e) {
        this.setColor(this.getInactiveColor());

        if (GameGlobals.mainWindow != null) {
            GameGlobals.mainWindow.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }
    }

    public Color getActiveColor() {
        return this.activeColor;
    }

    public void setActiveColor(Color activeColor) {
        this.activeColor = activeColor;
    }

    public Color getInactiveColor() {
        return this.inactiveColor;
    }

    public void setInactiveColor(Color inactiveColor) {
        this.inactiveColor = inactiveColor;
    }

    @Override
    public void draw(Graphics2D g) {
        if (this.getColor() == null) {
            this.setColor(this.getInactiveColor());
        }

        super.draw(g);
        g.drawRect(this.getScreenX() - 2, this.getScreenY() - 8, this.getWidth(), this.getHeight());
    }
}
