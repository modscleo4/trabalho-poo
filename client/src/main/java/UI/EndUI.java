package UI;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

import Engine.GameGlobals;
import UI.Components.Text;

public class EndUI extends UI {
    private Text txt = new Text("", 0, 0);

    @Override
    public void draw(Graphics2D g) {
        this.txt.setAbsoluteCoords(true);
        this.txt.setCenterScreen(true);

        if (GameGlobals.result == "won") {
            this.txt.setText("Venceu!");
            this.txt.setColor(Color.GREEN);
            this.txt.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 20));
        } else if (GameGlobals.result == "lost") {
            this.txt.setText("Perdeu.");
            this.txt.setColor(Color.RED);
            this.txt.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 20));
        }

        this.txt.draw(g);
    }
}
