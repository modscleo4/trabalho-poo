package UI;

import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import Engine.GameGlobals;
import Engine.Settings;
import UI.Components.Button;

public class SettingsUI extends UI {
    private Button btnVSync;
    private Button btnFullScreen;

    public SettingsUI() {
        this.btnVSync = new Button("", 200, GameGlobals.height - 48 - 80);
        this.btnVSync.setAbsoluteCoords(true);
        this.btnVSync.setHandler(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                Settings.VSync = !Settings.VSync;
            }
        });

        this.btnFullScreen = new Button("", 200, GameGlobals.height - 48 - 40);
        this.btnFullScreen.setAbsoluteCoords(true);
        this.btnFullScreen.setHandler(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                Settings.toggleFullScreen();
            }
        });
    }

    @Override
    public void draw(Graphics2D g) {
        if (!this.isVisible()) {
            return;
        }

        this.btnVSync.setText("VSync: " + (Settings.VSync ? "On" : "Off"));
        this.btnFullScreen.setText(Settings.fullscreen ? "Modo Janela" : "FullScreen");

        this.btnVSync.draw(g);
        this.btnFullScreen.draw(g);
    }
}
