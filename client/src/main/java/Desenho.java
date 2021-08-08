import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.Duration;
import java.time.Instant;
import java.util.Iterator;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.event.MouseInputAdapter;
import javax.swing.event.MouseInputListener;

import Engine.GameGlobals;
import Engine.Settings;
import Map.MapManager;
import UI.BootScreen;
import UI.EndUI;
import UI.LoadScreen;
import UI.LoginUI;
import UI.NetLoadScreen;
import UI.PauseUI;

public class Desenho extends JPanel {
    private BootScreen bootScreen = new BootScreen();
    private PauseUI pauseUI = new PauseUI();
    private EndUI endUI = new EndUI();
    private LoadScreen loadScreen = new LoadScreen();
    private LoginUI loginUI = new LoginUI();
    private NetLoadScreen netLoadScreen = new NetLoadScreen();

    Desenho() {
        this.bootScreen.setVisible(false);
        this.pauseUI.setVisible(false);
        this.endUI.setVisible(false);
        this.loadScreen.setVisible(false);
        this.loginUI.setVisible(false);
        this.netLoadScreen.setVisible(false);

        this.setMaximumSize(new Dimension(GameGlobals.width, GameGlobals.height));
        this.setPreferredSize(new Dimension(GameGlobals.width, GameGlobals.height));

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                Iterator<MouseInputListener> l = GameGlobals.mouseInputListeners.iterator();
                while (l.hasNext()) {
                    try {
                        MouseInputListener listener = l.next();
                        listener.mousePressed(e);
                    } catch (Throwable ex) {
                        ex.printStackTrace();
                    }
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                Iterator<MouseInputListener> l = GameGlobals.mouseInputListeners.iterator();
                while (l.hasNext()) {
                    try {
                        MouseInputListener listener = l.next();
                        listener.mouseReleased(e);
                    } catch (Throwable ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });

        this.addMouseMotionListener(new MouseInputAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                Iterator<MouseInputListener> l = GameGlobals.mouseInputListeners.iterator();
                while (l.hasNext()) {
                    try {
                        MouseInputListener listener = l.next();
                        listener.mouseMoved(e);
                    } catch (Throwable ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
    }

    private void doGameLoop() {
        if (GameGlobals.loaded) {
            GameGlobals.map.mount();
            GameGlobals.map.animateAllSprites();
        }
    }

    private void draw(Graphics2D g) {
        g.setColor(Color.BLACK);
        g.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));

        GameGlobals.map.draw(g);

        GameGlobals.uiLayer.draw(g);

        if (GameGlobals.result.equals("won")) {
            g.setColor(new Color(0, 0, 0, (int) 255 * 10 / 100));
            g.fillRect(0, 0, GameGlobals.width, GameGlobals.height);

            endUI.setVisible(true);
            endUI.draw(g);
        } else if (GameGlobals.result.equals("lost")) {
            g.setColor(new Color(0, 0, 0, (int) 255 * 10 / 100));
            g.fillRect(0, 0, GameGlobals.width, GameGlobals.height);

            endUI.setVisible(true);
            endUI.draw(g);
        }

        pauseUI.setVisible(GameGlobals.paused);
        pauseUI.draw(g);

        GameGlobals.internalClock++;
    }

    @Override
    public void paintComponent(Graphics _g) {
        Graphics2D g = (Graphics2D) _g;
        Instant timeStart = Instant.now();

        try {
            g.clearRect(0, 0, this.getWidth(), this.getHeight());

            GameGlobals.uiLayer.clear();

            if (!this.loadScreen.isEnded()) {
                this.loadScreen.draw(g);
            } else if (!this.bootScreen.isEnded()) {
                this.bootScreen.draw(g);
            } else if (!this.loginUI.isEnded()) {
                this.loginUI.draw(g);
            } else if (!this.netLoadScreen.isEnded()) {
                this.netLoadScreen.draw(g);
            } else {
                this.doGameLoop();

                this.draw(g);
            }

            Toolkit.getDefaultToolkit().sync();

            Instant timeEnd = Instant.now();

            double frametime = Duration.between(timeStart, timeEnd).toNanos() / 10E5F;

            if (Settings.VSync && GameGlobals.TARGET_FRAMETIME - frametime > 0) {
                try {
                    double currNanos = 10E5 * (frametime - (long) frametime);
                    double targetNanos = 10E5 * (GameGlobals.TARGET_FRAMETIME - (long) GameGlobals.TARGET_FRAMETIME);
                    long millis = (long) (GameGlobals.TARGET_FRAMETIME - frametime);
                    int nanos = (int) (targetNanos - currNanos);

                    if (nanos < 0) {
                        millis--;
                        nanos = -nanos + (int) targetNanos;
                    }

                    if (millis < 0) {
                        millis = 0;
                    }

                    Thread.sleep(millis, nanos);
                } catch (InterruptedException e) {

                }
            }

            timeEnd = Instant.now();

            frametime = Duration.between(timeStart, timeEnd).toNanos() / 10E5F;

            if (Settings.debugInfo) {
                g.setColor(Color.BLACK);
                g.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
                g.drawString(String.format("Frametime: %.2f", frametime), 0, 12);
                g.drawString(String.format("FPS: %.2f", 1000 / frametime), 0, 24);
                g.drawString(String.format("OS: %s %s %s", System.getProperty("os.name"),
                        System.getProperty("os.version"), System.getProperty("os.arch")), 0, 48);
                g.drawString(String.format("RAM: %d MB/%d MB", Runtime.getRuntime().freeMemory() / 1024 / 1024,
                        Runtime.getRuntime().maxMemory() / 1024 / 1024), 0, 60);
                g.drawString("Player 1", 0, 72);
                g.drawString(String.format("X/Y: %d/%d", GameGlobals.player.getX(), GameGlobals.player.getY()), 4, 84);
                g.drawString(String.format("Life: %d", GameGlobals.player.getLife()), 4, 96);
                g.drawString(String.format("Sprite: %s", GameGlobals.player.getSprite().getPath()), 4, 108);
                g.drawString("Player 2", 0, 120);
                g.drawString(String.format("X/Y: %d/%d", GameGlobals.player2.getX(), GameGlobals.player2.getY()), 4,
                        132);
                g.drawString(String.format("Life: %d", GameGlobals.player2.getLife()), 4, 144);
                g.drawString(String.format("Sprite: %s", GameGlobals.player2.getSprite().getPath()), 4, 156);

                MapManager.getGridMap().draw(g);
            }

            this.repaint();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this.getRootPane(), e.getStackTrace(), e.getClass().getName(),
                    JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }
}
