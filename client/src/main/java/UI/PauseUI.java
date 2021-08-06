package UI;

import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import Engine.GameGlobals;

public class PauseUI {
    private Button btnReturn;
    private Button btnSettings;
    private Button btnExit;

    public PauseUI() {
        this.btnReturn = new Button("Return", 48, GameGlobals.height - 48 - 80);
        this.btnReturn.setAbsoluteCoords(true);
        this.btnReturn.setHandler(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                GameGlobals.paused = !GameGlobals.paused;
            }
        });

        this.btnSettings = new Button("Settings", 48, GameGlobals.height - 48 - 40);
        this.btnSettings.setAbsoluteCoords(true);
        this.btnSettings.setHandler(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                //
            }
        });

        this.btnExit = new Button("Exit", 48, GameGlobals.height - 48);
        this.btnExit.setAbsoluteCoords(true);
        this.btnExit.setHandler(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                System.exit(0);
            }
        });
    }

    public void draw(Graphics g) {
        btnReturn.draw(g);
        btnSettings.draw(g);
        btnExit.draw(g);
    }
}
