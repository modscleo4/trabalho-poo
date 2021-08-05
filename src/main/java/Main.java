import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.Duration;
import java.time.Instant;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.event.MouseInputAdapter;
import javax.swing.event.MouseInputListener;

import Engine.GameGlobals;
import Engine.Settings;
import UI.BootScreen;
import UI.EndUI;
import UI.LoadScreen;
import UI.PauseUI;

public class Main extends JFrame {
    Network network = new Network(this, "127.0.0.1", 8080);
    boolean gameRunning = false;

    Desenho des = new Desenho();

    public String globMove;

    Main() {
        super("Trabalho");
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.getContentPane().setLayout(new GridBagLayout());
        this.getContentPane().setBackground(Color.BLACK);
        this.getContentPane().add(des);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setResizable(false);

        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                globMove = KeyEvent.getKeyText(e.getKeyCode());
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

        new Thread() {
            Position playerPosition = new Position(0, 0);
            Position player2Position = new Position(0, 0);

            public void run() {
                startNetwork();
                gameRunning = true;
                String action;

                while (network.alive()) {
                    action = network.readTypeMessage();
                    network.readPosition(playerPosition, player2Position);

                }
            }
        }.start();
    }

    private void startNetwork() {
        Position playerPosition = new Position(0, 0);
        Position player2Position = new Position(0, 0);
        network.readPosition(playerPosition, player2Position);
        for (int i = 10; i >= 1; i--) {
            System.out.println("O jogo começa em: " + i + " segundo(s).");
            network.readTypeMessage();
        }
    }

    class Desenho extends JPanel {
        private BootScreen bootScreen = new BootScreen();
        private PauseUI pauseUI = new PauseUI();
        private EndUI endUI = new EndUI();
        private LoadScreen loadScreen = new LoadScreen();

        Desenho() {
            this.setMaximumSize(new Dimension(GameGlobals.width, GameGlobals.height));
            this.setPreferredSize(new Dimension(GameGlobals.width, GameGlobals.height));

            GameGlobals.player = new Omori(1, 1);
            GameGlobals.player2 = new Aubrey(2, 2);

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

        private void doGameLoop() {
            if (GameGlobals.loaded) {
                GameGlobals.map.mount();
                GameGlobals.map.animateAllSprites();
            }
        }

        private void draw(Graphics g) {
            if (gameRunning) {
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

                for (int z = 0; z < GameGlobals.maxZ; z++) {
                    for (int i = 0; i < GameGlobals.map.getMap().length; i++) {
                        for (int j = 0; j < GameGlobals.map.getMap()[i].length; j++) {
                            if (z >= GameGlobals.map.getMap()[i][j].length
                                    || GameGlobals.map.getMap()[i][j][z] == null) {
                                continue;
                            }

                            GameGlobals.map.getMap()[i][j][z].draw(g);
                        }
                    }
                }

                GameGlobals.uiLayer.draw(g);

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
        }

        @Override
        public void paintComponent(Graphics g) {

            Instant timeStart = Instant.now();

            try {

                g.clearRect(0, 0, this.getWidth(), this.getHeight());

                GameGlobals.uiLayer.clear();

                this.doGameLoop();

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
                    g.drawString(String.format("Sprite: %s", GameGlobals.player.getSprite().getPath()), 0, 84);
                }

                this.repaint();

            } catch (Exception e) {
                JOptionPane.showMessageDialog(this.getRootPane(), e.getStackTrace(), e.getClass().getName(),
                        JOptionPane.ERROR_MESSAGE);
                System.exit(1);
            }

        }

    }

    static public void main(String[] args) {
        Main f = new Main();
        f.setVisible(true);
    }
}
