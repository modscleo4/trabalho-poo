package UI;

import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.sound.sampled.AudioSystem;

import Engine.GameGlobals;
import Engine.Settings;
import Engine.SoundManager;
import UI.Components.Button;

public class SettingsUI extends UI {
    private Button btnVSync;
    private Button btnFullScreen;
    private Button btnSpeaker;

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

        this.btnSpeaker = new Button("", 200, GameGlobals.height - 48 - 0);
        this.btnSpeaker.setAbsoluteCoords(true);
        this.btnSpeaker.setHandler(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                Settings.audioOutputMixer++;
                Settings.audioOutputMixer %= AudioSystem.getMixerInfo().length;
            }
        });
    }

    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);

        this.btnVSync.setVisible(visible);
        this.btnFullScreen.setVisible(visible);
        this.btnSpeaker.setVisible(visible);
    }

    @Override
    public void draw(Graphics2D g) {
        if (!this.isVisible()) {
            return;
        }

        this.btnVSync.setText("VSync: " + (Settings.VSync ? "On" : "Off"));
        this.btnFullScreen.setText(Settings.fullscreen ? "Modo Janela" : "FullScreen");
        this.btnSpeaker.setText("Speaker: " + SoundManager.getCurrentSpeakerName());

        this.btnVSync.draw(g);
        this.btnFullScreen.draw(g);
        this.btnSpeaker.draw(g);
    }

    @Override
    public void close() {
        super.close();

        this.btnVSync.close();
        this.btnFullScreen.close();
        this.btnSpeaker.close();
    }
}
