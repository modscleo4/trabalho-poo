package Entity;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicInteger;

import Engine.AnimatedSprite;
import Engine.GameGlobals;
import Engine.Settings;
import Engine.SoundManager;
import Engine.Sprite;
import Util.Pair;

public abstract class Player extends GameEntity {
    public static final int IDLE = 0;
    public static final int MOVE = 1;
    public static final int HIT = 2;
    public static final int ATTACK = 3;

    private String name;
    private int life = 200;
    private int minDamage = 5;
    private int maxDamage = 10;
    private int takenDamage = 0;
    private Thread damageThread = null;
    private Thread attackThread = null;
    private Thread moveThread = null;

    private Queue<Pair<Integer, Integer>> moveQueue = new LinkedList<>();
    private Queue<Integer> attackQueue = new LinkedList<>();

    public Player(int x, int y, String name) {
        super(new AnimatedSprite[] { new AnimatedSprite(String.format("%s/idle", name), true, 100, x, y, true),
                new AnimatedSprite(String.format("%s/move", name), false, 100, x, y, true),
                new AnimatedSprite(String.format("%s/hit", name), false, 100, x, y, true),
                new AnimatedSprite(String.format("%s/attack", name), false, 100, x, y, true) }, x, y);
        this.setName(name);
        this.setSolid(true);
        this.setUseDirection(true);
    }

    @Override
    public void handleKeyPressed(KeyEvent e) {
        if (GameGlobals.player != this) {
            return;
        }

        if (e.getKeyCode() == Settings.KEY_MOVE_UP) {
            // Debounce
            if (this.moveThread != null || this.attackThread != null) {
                return;
            }

            // manda network
            GameGlobals.network.sendCommand("MOVE", new String[] { "0", "-1" });

            //this.move(0, -1);
        } else if (e.getKeyCode() == Settings.KEY_MOVE_DOWN) {
            // Debounce
            if (this.moveThread != null || this.attackThread != null) {
                return;
            }

            // manda network
            GameGlobals.network.sendCommand("MOVE", new String[] { "0", "1" });

            //this.move(0, 1);
        } else if (e.getKeyCode() == Settings.KEY_MOVE_LEFT) {
            // Debounce
            if (this.moveThread != null || this.attackThread != null) {
                return;
            }

            // manda network
            GameGlobals.network.sendCommand("MOVE", new String[] { "-1", "0" });

            //this.move(-1, 0);
        } else if (e.getKeyCode() == Settings.KEY_MOVE_RIGHT) {
            // Debounce
            if (this.moveThread != null || this.attackThread != null) {
                return;
            }

            // manda network
            GameGlobals.network.sendCommand("MOVE", new String[] { "1", "0" });

            //this.move(1, 0);
        } else if (e.getKeyCode() == Settings.KEY_ATTACK) {
            // Debounce
            if (this.moveThread != null || this.attackThread != null) {
                return;
            }

            // manda network
            GameGlobals.network.sendCommand("ATTACK");

            //this.attack();
        }
    }

    @Override
    public void handleMousePressed(MouseEvent e) {
        //
    }

    public int getLife() {
        return this.life;
    }

    public void setLife(int life) {
        if (life < 0) {
            life = 0;
        }

        if (life == 0) {
            if (GameGlobals.player == this) {
                GameGlobals.result = "lost";
            } else {
                GameGlobals.result = "won";
            }

            this.setVisible(false);
        }

        if (this.damageThread != null) {
            this.damageThread.interrupt();
        }

        this.takenDamage = this.life - life;
        this.damageThread = new Thread(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                return;
            }

            this.takenDamage = 0;
            this.damageThread = null;
        });
        this.damageThread.start();

        this.life = life;
    }

    public int getDamage() {
        return (int) Math.ceil(this.minDamage + Math.random() * (this.maxDamage - this.minDamage));
    }

    public void hit(int damage) {
        if (this.moveThread == null && this.attackThread == null) {
            this.setCurrentSprite(HIT);
        }

        this.setLife(this.getLife() - damage);
        SoundManager.playSound("slime_hit");

        if (this.moveThread == null && this.attackThread == null) {
            this.resetAnimation(IDLE);
        }
    }

    public void move(int dx, int dy) {
        if (this.moveThread != null) {
            moveQueue.add(new Pair<Integer, Integer>(dx, dy));
            return;
        }

        this.moveThread = new Thread(() -> {
            super.move(MOVE, dx, dy, () -> {
                this.resetAnimation(IDLE);

                this.moveThread = null;

                Pair<Integer, Integer> p = moveQueue.poll();
                if (p != null) {
                    this.move(p.getFirst(), p.getSecond());
                }
            });
        });

        this.moveThread.start();
    }

    public void attack(int damage) {
        if (this.attackThread != null) {
            attackQueue.add(damage);
            return;
        }

        int x = this.getX();
        int y = this.getY();

        if (this.useDirection) {
            if (this.getDirection().equals("up")) {
                y--;
            } else if (this.getDirection().equals("down")) {
                y++;
            } else if (this.getDirection().equals("left")) {
                x--;
            } else if (this.getDirection().equals("right")) {
                x++;
            }
        }

        if (GameGlobals.map.enemyAt(x, y) == null) {
            Integer dmg = attackQueue.poll();
            if (dmg != null) {
                this.attack(dmg.intValue());
            }
            return;
        }

        GameGlobals.map.enemyAt(x, y).hit(damage);

        this.attackThread = new Thread(() -> {
            this.setCurrentSprite(ATTACK);

            try {
                Thread.sleep(this.getSprite().getTiming() * (this.getSprite().length() - this.getSprite().getIndex()));
            } catch (InterruptedException e) {
                //
            }

            this.resetAnimation(IDLE);
            this.attackThread = null;

            Integer dmg = attackQueue.poll();
            if (dmg != null) {
                this.attack(dmg.intValue());
            }
        });

        this.attackThread.start();
    }

    public void attack() {
        this.attack(this.getDamage());
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void draw(Graphics2D g) {
        super.draw(g);

        AtomicInteger x = new AtomicInteger(this.getScreenX() + 10);
        AtomicInteger y = new AtomicInteger(this.getScreenY() - 5);

        if (x.get() < 0) {
            x.set(0);
        } else if (x.get() + 15 > GameGlobals.width) {
            x.set(x.get() - 15);
        }

        if (y.get() < 0) {
            y.set(0);
        } else if (y.get() + 15 > GameGlobals.height) {
            y.set(y.get() - 15);
        }

        GameGlobals.uiLayer.addLayer(g2 -> {
            this.drawLifeBar(g2);
            if (this.takenDamage > 0) {
                g2.setColor(Color.RED);
                g2.setFont(new Font(Font.MONOSPACED, Font.BOLD, 14));
                g2.drawString("-" + this.takenDamage, x.get(), y.get());
            } else if (this.takenDamage < 0) {
                g2.setColor(Color.GREEN);
                g2.setFont(new Font(Font.MONOSPACED, Font.BOLD, 14));
                g2.drawString("+" + this.takenDamage, x.get(), y.get());
            }
        });
    }

    public void drawLifeBar(Graphics2D g) {
        if (this.name.equals("omori")) {
            Sprite heart = new Sprite("omori/icon/0", 4, 4, false);
            heart.setAbsoluteCoords(true);
            heart.setWidth(24);
            heart.setHeight(24);
            heart.draw(g);
            g.setColor(Color.BLACK);
            g.fillRect(32, 8, 8 * 20, 16);

            g.setColor(Color.RED);
            g.fillRect(32, 8, 8 * this.getLife() / 10, 16);
        } else {
            Sprite heart = new Sprite("aubrey/icon/0", 788, 4, false);
            heart.setAbsoluteCoords(true);
            heart.setWidth(24);
            heart.setHeight(24);
            heart.draw(g);
            g.setColor(Color.BLACK);
            g.fillRect(624, 8, 8 * 20, 16);

            g.setColor(Color.BLUE);
            g.fillRect(624, 8, 8 * this.getLife() / 10, 16);
        }
    }
}
