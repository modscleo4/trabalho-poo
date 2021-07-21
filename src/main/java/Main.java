import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.event.MouseInputAdapter;
import javax.swing.event.MouseInputListener;

import Engine.BaseObject;
import Engine.GameGlobals;
import Engine.Settings;
import Engine.Sprite;
import Entity.Player;
import Entity.Slime;
import Map.Map;
import UI.BootScreen;
import UI.EndUI;
import UI.LoadScreen;
import UI.PauseUI;

public class Main extends JFrame {
    Desenho des = new Desenho();

    class Desenho extends JPanel {
        private List<BaseObject>[] layers;

        private Slime slime;

        private BootScreen bootScreen = new BootScreen();
        private PauseUI pauseUI = new PauseUI();
        private EndUI endUI = new EndUI();
        private LoadScreen loadScreen = new LoadScreen();

        Desenho() {
            this.layers = new ArrayList[GameGlobals.maxZ];

            for (int z = 0; z < layers.length; z++) {
                this.layers[z] = new ArrayList<>();
            }

            for (int i = 0; i < GameGlobals.maxW; i++) {
                for (int j = 0; j < GameGlobals.maxH; j++) {
                    this.layers[0].add(new Sprite("floor", i, j, false));
                }
            }

            GameGlobals.player = new Player(1, 1);
            this.slime = new Slime(5, 5, false);

            this.layers[2].add(GameGlobals.player);
            this.layers[1].add(this.slime);

            this.setPreferredSize(new Dimension(GameGlobals.width, GameGlobals.height));

            GameGlobals.map = new Map(this.layers, GameGlobals.maxW, GameGlobals.maxH, "bg1");

            this.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    for (MouseInputListener listener : GameGlobals.mouseInputListeners) {
                        listener.mousePressed(e);
                    }
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    for (MouseInputListener listener : GameGlobals.mouseInputListeners) {
                        listener.mouseReleased(e);
                    }
                }
            });
            this.addMouseMotionListener(new MouseInputAdapter() {
                @Override
                public void mouseMoved(MouseEvent e) {
                    for (MouseInputListener listener : GameGlobals.mouseInputListeners) {
                        listener.mouseMoved(e);
                    }
                }
            });
        }

        private void draw(Graphics g) {
            g.setColor(Color.BLACK);
            g.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));

            if (!this.loadScreen.isEnded()) {
                this.loadScreen.draw(g);
                return;
            }

            if (!this.bootScreen.isEnded()) {
                this.bootScreen.draw(g);
                return;
            }

            GameGlobals.map.mount();

            for (int i = 0; i < GameGlobals.map.getMap().length; i++) {
                for (int j = 0; j < GameGlobals.map.getMap()[i].length; j++) {
                    for (int z = 0; z < GameGlobals.map.getMap()[i][j].length; z++) {
                        if (GameGlobals.map.getMap()[i][j][z] == null) {
                            continue;
                        }

                        GameGlobals.map.getMap()[i][j][z].draw(g);
                    }
                }
            }

            GameGlobals.player.drawLifeBar(g);

            if (GameGlobals.result.equals("won")) {
                g.setColor(new Color(0, 0, 0, (int) 255 * 10 / 100));
                g.fillRect(0, 0, GameGlobals.width, GameGlobals.height);

                endUI.draw(g);
            } else if (GameGlobals.result.equals("lost")) {
                g.setColor(new Color(0, 0, 0, (int) 255 * 10 / 100));
                g.fillRect(0, 0, GameGlobals.width, GameGlobals.height);

                endUI.draw(g);
            }

            if (GameGlobals.paused) {
                g.setColor(new Color(0, 0, 0, (int) 255 * 10 / 100));
                g.fillRect(0, 0, GameGlobals.width, GameGlobals.height);

                pauseUI.draw(g);
                GameGlobals.map.pauseBG();
                //
            } else {
                GameGlobals.internalClock++;
                GameGlobals.map.playBG();
            }
        }

        @Override
        public void paintComponent(Graphics g) {
            Instant timeStart = Instant.now();

            try {
                g.clearRect(0, 0, this.getWidth(), this.getHeight());

                this.draw(g);

                Toolkit.getDefaultToolkit().sync();

                Instant timeEnd = Instant.now();

                double frametime = Duration.between(timeStart, timeEnd).toNanos() / 10E5F;

                if (Settings.VSync && GameGlobals.TARGET_FRAMETIME - frametime > 0) {
                    try {
                        double currNanos = 10E5 * (frametime - (long) frametime);
                        double targetNanos = 10E5
                                * (GameGlobals.TARGET_FRAMETIME - (long) GameGlobals.TARGET_FRAMETIME);
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
                    g.drawString(String.format("X/Y: %d/%d", GameGlobals.player.getX(), GameGlobals.player.getY()), 0,
                            72);
                }

                this.repaint();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this.getRootPane(), e.getStackTrace(), e.getClass().getName(),
                        JOptionPane.ERROR_MESSAGE);
                System.exit(1);
            }
        }
    }

    Main() {
        super("Trabalho");
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.add(des);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setResizable(false);

        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER && e.isAltDown()) {
                    dispose();

                    if (Settings.fullscreen) {
                        setExtendedState(JFrame.NORMAL);
                        setUndecorated(false);
                        pack();
                        setLocationRelativeTo(null);
                    } else {
                        setExtendedState(JFrame.MAXIMIZED_BOTH);
                        setUndecorated(true);
                    }

                    setVisible(true);

                    Settings.fullscreen = !Settings.fullscreen;

                    return;
                } else if (e.getKeyCode() == Settings.KEY_PAUSE) {
                    GameGlobals.paused = !GameGlobals.paused;
                } else if (e.getKeyCode() == KeyEvent.VK_F3) {
                    Settings.debugInfo = !Settings.debugInfo;
                }

                for (KeyListener listener : GameGlobals.keyListeners) {
                    listener.keyPressed(e);
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                for (KeyListener listener : GameGlobals.keyListeners) {
                    listener.keyReleased(e);
                }
            }
        });
    }

    static public void main(String[] args) {
        Main f = new Main();
        f.setVisible(true);
    }
}
