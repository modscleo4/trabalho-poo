package UI;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import Engine.GameGlobals;

public class EndUI {
    private Text txt = new Text("", 0, 0);

    public void draw(Graphics g) {
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
