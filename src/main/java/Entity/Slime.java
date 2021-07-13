package Entity;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseEvent;

import javax.swing.Timer;

import Engine.GameGlobals;
import Engine.SoundManager;
import Engine.AnimatedSprite;

public class Slime extends Entity {
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
        super(new AnimatedSprite[] {
                new AnimatedSprite(
                        new String[] { "slime/slime_idle0", "slime/slime_idle1", "slime/slime_idle2",
                                "slime/slime_idle3", "slime/slime_idle4", "slime/slime_idle5", },
                        true, 100, x, y, true),
                new AnimatedSprite(new String[] { "slime/slime_move0", "slime/slime_move1", "slime/slime_move2",
                        "slime/slime_move3", "slime/slime_move4", }, false, 100, x, y, true),
                new AnimatedSprite(new String[] { "slime/slime_hit0", "slime/slime_hit1", }, false, 100, x, y, true),
                new AnimatedSprite(new String[] { "slime/slime_jump0", "slime/slime_jump1", "slime/slime_jump2",
                        "slime/slime_jump3", "slime/slime_jump4", "slime/slime_jump5", "slime/slime_jump6",
                        "slime/slime_jump7", }, false, 100, x, y, true) },
                x, y);
        this.setSolid(true);
        this.setUseDirection(true);
        this.setDirection("right");

        if (!disableTimer) {
            this.internalTimer = new Timer(500, ae -> {
                if (GameGlobals.paused) {
                    return;
                }

                if (GameGlobals.internalClock == 0) {
                    return;
                }

                if (!GameGlobals.map.playerNear(this.getX(), this.getY())) {
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

    @Override
    public void handleMousePressed(MouseEvent e) {
        if (GameGlobals.paused) {
            return;
        }

        if (!GameGlobals.map.playerNear(this.getX(), this.getY())) {
            return;
        }

        if (!GameGlobals.player.isVisible()) {
            return;
        }

        this.hit(GameGlobals.player.getDamage());
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

        int x = this.getScreenX() + 10;
        int y = this.getScreenY() - 5;

        if (x < 0) {
            x = 0;
        } else if (x + 15 > GameGlobals.width) {
            x -= 15;
        }

        if (y < 0) {
            y = 0;
        } else if (y + 15 > GameGlobals.height) {
            y -= 15;
        }

        if (this.takenDamage > 0) {
            g.setColor(Color.RED);
            g.setFont(new Font(Font.MONOSPACED, Font.BOLD, 14));
            g.drawString("-" + this.takenDamage, x, y);
        } else if (this.takenDamage < 0) {
            g.setColor(Color.GREEN);
            g.setFont(new Font(Font.MONOSPACED, Font.BOLD, 14));
            g.drawString("+" + this.takenDamage, x, y);
        }
    }
}