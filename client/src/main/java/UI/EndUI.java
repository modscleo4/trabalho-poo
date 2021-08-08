package UI;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

import Engine.GameGlobals;
import UI.Components.Text;

public class EndUI extends UI {
    private Text txt = new Text("", 0, 0);

    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);

        this.txt.setVisible(visible);
    }

    @Override
    public void draw(Graphics2D g) {
        g.setColor(new Color(0, 0, 0, (int) 255 * 10 / 100));
        g.fillRect(0, 0, GameGlobals.width, GameGlobals.height);

        this.txt.setAbsoluteCoords(true);
        this.txt.setCenterScreen(true);
        this.txt.setFont(new Font(Font.MONOSPACED, Font.BOLD, 20));

        if (GameGlobals.result == "won") {
            this.txt.setText("Venceu!");
            this.txt.setColor(Color.GREEN);
        } else if (GameGlobals.result == "lost") {
            this.txt.setText("Perdeu.");
            this.txt.setColor(Color.RED);
        } else if (GameGlobals.result == "draw") {
            this.txt.setText("Empate.");
            this.txt.setColor(Color.GRAY);
        }

        this.txt.draw(g);
    }

    @Override
    public void close() {
        super.close();

        this.txt.close();
    }
}
