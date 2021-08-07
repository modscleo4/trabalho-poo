package UI.Components;

import java.awt.Color;
import java.awt.Graphics2D;

public class ProgressBar extends Component {
    private Color color;
    private int percentage;

    public ProgressBar(int x, int y, int width, int height) {
        super(x, y);
        this.setWidth(width);
        this.setHeight(height);
        this.setAbsoluteCoords(true);
        this.setColor(Color.RED);
    }

    public Color getColor() {
        return this.color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public int getPercentage() {
        return this.percentage;
    }

    public void setPercentage(int percentage) {
        if (this.percentage < 0 || this.percentage > 100) {
            return;
        }

        this.percentage = percentage;
    }

    @Override
    public void draw(Graphics2D g) {
        g.setColor(Color.BLACK);
        g.fillRect(this.getScreenX(), this.getScreenY(), this.getWidth(), this.getHeight());
        g.setColor(color);
        g.fillRect(this.getScreenX(), this.getScreenY(), this.getWidth() / 100 * this.getPercentage(),
                this.getHeight());
    }
}
