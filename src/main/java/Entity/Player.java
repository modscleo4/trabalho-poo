package Entity;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.concurrent.atomic.AtomicInteger;

import Engine.AnimatedSprite;
import Engine.GameGlobals;
import Engine.Settings;
import Engine.SoundManager;
import Engine.Sprite;

public class Player extends Entity {
    public static final int IDLE = 0;
    public static final int MOVE = 1;
    public static final int HIT = 2;
    public static final int JUMP = 3;

    private int life = 20;
    private int minDamage = 5;
    private int maxDamage = 10;
    private int takenDamage = 0;
    private Thread damageThread = null;
    private Thread moveThread = null;

    public Player(int x, int y) {
        super(new AnimatedSprite[] { new AnimatedSprite("player/idle", true, 100, x, y, true),
                new AnimatedSprite("player/move", false, 100, x, y, true),
                new AnimatedSprite("player/hit", false, 100, x, y, true) }, x, y);
        this.setSolid(true);
        this.setUseDirection(true);
        this.setWidth(this.getHeight() / 24 * 16);
    }

    @Override
    public void handleKeyPressed(KeyEvent e) {
        if (e.getKeyCode() == Settings.KEY_MOVE_UP) {
            if (this.moveThread != null) {
                return;
            }

            this.setDirection("up");

            if (this.getY() == 0 || GameGlobals.map.collide(this, this.getX(), this.getY() - 1)) {
                return;
            }

            this.move(0, -1);
        } else if (e.getKeyCode() == Settings.KEY_MOVE_DOWN) {
            if (this.moveThread != null) {
                return;
            }

            this.setDirection("down");

            if (this.getY() == GameGlobals.maxH - 1 || GameGlobals.map.collide(this, this.getX(), this.getY() + 1)) {
                return;
            }

            this.move(0, +1);
        } else if (e.getKeyCode() == Settings.KEY_MOVE_LEFT) {
            if (this.moveThread != null) {
                return;
            }

            this.setDirection("left");

            if (this.getX() == 0 || GameGlobals.map.collide(this, this.getX() - 1, this.getY())) {
                return;
            }

            //this.setFlipped(true);
            this.move(-1, 0);
        } else if (e.getKeyCode() == Settings.KEY_MOVE_RIGHT) {
            if (this.moveThread != null) {
                return;
            }

            this.setDirection("right");

            if (this.getX() == GameGlobals.maxW - 1 || GameGlobals.map.collide(this, this.getX() + 1, this.getY())) {
                return;
            }

            //this.setFlipped(false);
            this.move(+1, 0);
        } else if (e.getKeyCode() == Settings.KEY_JUMP) {
            this.jump();
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
            GameGlobals.result = "lost";
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
        });
        this.damageThread.start();

        this.life = life;
    }

    public int getDamage() {
        return (int) Math.ceil(this.minDamage + Math.random() * (this.maxDamage - this.minDamage));
    }

    public void hit(int damage) {
        this.setCurrentSprite(HIT);

        this.setLife(this.getLife() - damage);
        SoundManager.playSound("slime_hit");

        this.resetAnimation(IDLE);
    }

    public void jump() {
        this.setCurrentSprite(JUMP);

        this.resetAnimation(IDLE);
    }

    @Override
    public void move(int dx, int dy) {
        this.moveThread = new Thread(() -> {
            this.setCurrentSprite(MOVE);

            super.move(dx, dy, () -> {
                this.resetAnimation(IDLE);

                this.moveThread = null;
            });
        });

        this.moveThread.start();
    }

    @Override
    public void draw(Graphics g) {
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

    public void drawLifeBar(Graphics g) {
        Sprite heart = new Sprite("heart", 4, 4, false);
        heart.setAbsoluteCoords(true);
        heart.setWidth(24);
        heart.setHeight(24);
        heart.draw(g);
        g.setColor(Color.BLACK);
        g.fillRect(32, 8, 8 * 20, 16);
        g.setColor(Color.RED);
        g.fillRect(32, 8, 8 * this.life, 16);
    }
}
