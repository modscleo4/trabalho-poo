package UI;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import Engine.GameGlobals;

public class EndUI {

    public void draw(Graphics g) {
        Text txt = new Text("", 0, 0);
        txt.setAbsoluteCoords(true);
        txt.setCenterScreen(true);

        if (GameGlobals.result == "won") {
            txt.setText("Venceu!");
            txt.setColor(Color.GREEN);
            txt.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 20));
        } else if (GameGlobals.result == "lost") {
            txt.setText("Perdeu.");
            txt.setColor(Color.RED);
            txt.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 20));
        }

        txt.draw(g);
    }
}
