package Entity;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.concurrent.atomic.AtomicInteger;

import javax.swing.Timer;

import Engine.AnimatedSprite;
import Engine.GameGlobals;
import Engine.SoundManager;

public class Slime extends Enemy {
    public static final int IDLE = 0;
    public static final int MOVE = 1;
    public static final int HIT = 2;
    public static final int JUMP = 3;

    private int life = 20;
    private int minDamage = 5;
    private int maxDamage = 10;
    private int takenDamage = 0;
    private Thread damageThread = null;

    private Timer internalTimer;

    public Slime(int x, int y, boolean disableTimer) {
        super(new AnimatedSprite[] { new AnimatedSprite("slime/idle", true, 100, x, y, true),
                new AnimatedSprite("slime/move", false, 100, x, y, true),
                new AnimatedSprite("slime/hit", false, 100, x, y, true),
                new AnimatedSprite("slime/jump", false, 100, x, y, true) }, x, y);
        this.setSolid(true);
        this.setDirection("right");
        this.setUseDirection(true);

        if (!disableTimer) {
            this.internalTimer = new Timer(500, ae -> {
                if (GameGlobals.paused) {
                    return;
                }

                if (GameGlobals.internalClock == 0) {
                    return;
                }

                if (!GameGlobals.map.objectNear(this.getX(), this.getY(), GameGlobals.player)) {
                    return;
                }

                if (!GameGlobals.player.isVisible()) {
                    return;
                }

                if (Math.round(0 + Math.random() * (10 - 0)) != 4) {
                    return;
                }

                this.jump();
                GameGlobals.player.hit(this.getDamage());
            });
            this.internalTimer.start();
        }
    }

    public int getLife() {
        return this.life;
    }

    public void setLife(int life) {
        if (life < 0) {
            life = 0;
        }

        if (life == 0) {
            this.setVisible(false);
            this.internalTimer.stop();
            GameGlobals.result = "won";
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

    @Override
    public void hit(int damage) {
        this.setCurrentSprite(HIT);

        this.setLife(this.getLife() - damage);
        SoundManager.playSound("player_hit");

        this.resetAnimation(IDLE);
    }

    public void jump() {
        this.setCurrentSprite(JUMP);

        this.resetAnimation(IDLE);
    }

    public void move(int dx, int dy) {
        this.setCurrentSprite(MOVE);
        this.setX(this.getX() + dx);
        this.setY(this.getY() + dy);

        this.resetAnimation(IDLE);
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
}
