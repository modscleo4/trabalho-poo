package UI.Components;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public class TextBox extends Text {
    private Color inputColor = Color.BLUE;
    private Color activeColor = Color.GRAY;
    private Color inactiveColor = Color.BLACK;
    private boolean listening = false;

    public TextBox(String text, int x, int y) {
        super(text, x, y);
        this.setColor(null);
        this.interactibleWhenPaused = true;
    }

    public TextBox(int x, int y) {
        this("", x, y);
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
    public void handleKeyPressed(KeyEvent e) {
        if (!this.isListening()) {
            return;
        }

        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            this.setListening(false);
            return;
        } else if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
            this.setText(this.getText().substring(0, this.getText().length() - 1));
        } else if (true) {
            this.setText(this.getText() + e.getKeyChar());
        }
    }

    @Override
    public void handleMousePressed(MouseEvent e) {
        this.setListening(true);
    }

    @Override
    public void handleMouseEntered(MouseEvent e) {
        this.setColor(this.getActiveColor());
    }

    @Override
    public void handleMouseExited(MouseEvent e) {
        this.setColor(this.getInactiveColor());
    }

    public Color getInputColor() {
        return this.inputColor;
    }

    public void setInputColor(Color inputColor) {
        this.inputColor = inputColor;
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

    public boolean isListening() {
        return this.listening;
    }

    public void setListening(boolean listening) {
        this.listening = listening;
    }

    @Override
    public void draw(Graphics2D g) {
        if (this.getColor() == null) {
            this.setColor(this.getInactiveColor());
        } else if (this.isListening()) {
            this.setColor(this.getInputColor());
        }

        super.draw(g);
        g.drawRect(this.getScreenX() - 50, this.getScreenY() - 8, this.getWidth() + 100, this.getHeight());
    }
}
