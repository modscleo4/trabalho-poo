package UI;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import Engine.GameGlobals;
import UI.Components.Button;

public class PauseUI extends UI {
    private Button btnReturn;
    private Button btnSettings;
    private Button btnExit;

    private SettingsUI settingsUI = new SettingsUI();

    public PauseUI() {
        this.btnReturn = new Button("Voltar", 48, GameGlobals.height - 48 - 80);
        this.btnReturn.setAbsoluteCoords(true);
        this.btnReturn.setHandler(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                GameGlobals.paused = !GameGlobals.paused;
            }
        });

        this.btnSettings = new Button("Opções", 48, GameGlobals.height - 48 - 40);
        this.btnSettings.setAbsoluteCoords(true);
        this.btnSettings.setHandler(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                settingsUI.setVisible(!settingsUI.isVisible());
            }
        });

        this.btnExit = new Button("Sair", 48, GameGlobals.height - 48);
        this.btnExit.setAbsoluteCoords(true);
        this.btnExit.setHandler(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                System.exit(0);
            }
        });
    }

    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);

        if (visible) {
            GameGlobals.map.pauseBG();
        } else {
            GameGlobals.map.playBG();
        }
    }

    @Override
    public void draw(Graphics2D g) {
        if (!this.isVisible()) {
            return;
        }

        g.setColor(new Color(0, 0, 0, (int) 255 * 10 / 100));
        g.fillRect(0, 0, GameGlobals.width, GameGlobals.height);

        btnReturn.draw(g);
        btnSettings.draw(g);
        btnExit.draw(g);

        settingsUI.draw(g);
    }
}
