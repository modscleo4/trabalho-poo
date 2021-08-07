package UI.Components;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.font.FontRenderContext;

public class Text extends Component {
    private String text;
    private Font font;
    private Color color;
    protected MouseAdapter handler;

    public Text(String text, int x, int y) {
        super(x, y);
        this.setX(x);
        this.setY(y);
        this.setText(text);
        this.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 20));
        this.setColor(Color.BLACK);
        this.interactibleWhenPaused = false;
    }

    @Override
    public int getScreenY() {
        return super.getScreenY() - this.getHeight() / 2;
    }

    @Override
    public int getWidth() {
        return (int) this.getFont().getStringBounds(this.getText(), new FontRenderContext(null, true, true)).getWidth();
    }

    @Override
    public int getHeight() {
        return (int) this.getFont().getStringBounds(this.getText(), new FontRenderContext(null, true, true))
                .getHeight();
    }

    public String getText() {
        return this.text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Font getFont() {
        return this.font;
    }

    public void setFont(Font font) {
        this.font = font;
    }

    public Color getColor() {
        return this.color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setHandler(MouseAdapter handler) {
        this.handler = handler;
    }

    @Override
    public void handleMousePressed(MouseEvent e) {
        if (this.handler == null) {
            return;
        }

        this.handler.mousePressed(e);
    }

    @Override
    public void draw(Graphics2D g) {
        super.draw(g);
        g.setFont(this.getFont());
        g.setColor(this.getColor());
        g.drawString(this.getText(), this.getScreenX(), this.getScreenY() + this.getHeight() / 2);
    }
}
