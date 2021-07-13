package UI;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import Engine.GameGlobals;

public class EndUI {
    public void draw(Graphics g) {
        if (GameGlobals.result == "won") {
            g.setColor(Color.GREEN);
            g.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 20));
            g.drawString("Venceu!", (GameGlobals.width - 120) / 2, GameGlobals.height / 2);
        } else if (GameGlobals.result == "lost") {
            g.setColor(Color.RED);
            g.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 20));
            g.drawString("Perdeu.", (GameGlobals.width - 120) / 2, GameGlobals.height / 2);
        }
    }
}
