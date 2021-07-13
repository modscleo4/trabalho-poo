package Entity;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

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

    public Player(int x, int y) {
        super(new AnimatedSprite[] {
                new AnimatedSprite(
                        new String[] { "player/player_idle0", "player/player_idle1", "player/player_idle2",
                                "player/player_idle3", "player/player_idle4", "player/player_idle5", },
                        true, 100, x, y, true),
                new AnimatedSprite(
                        new String[] { "player/player_move0", "player/player_move1", "player/player_move2",
                                "player/player_move3", "player/player_move4", "player/player_move5", },
                        false, 100, x, y, true),
                new AnimatedSprite(new String[] { "player/player_hit0", "player/player_hit1", }, false, 100, x, y,
                        true),
                new AnimatedSprite(new String[] { "player/player_jump0", "player/player_jump1", "player/player_jump2",
                        "player/player_jump3", "player/player_jump4", "player/player_jump5", "player/player_jump6",
                        "player/player_jump7", }, false, 100, x, y, true) },
                x, y);
        this.setSolid(true);
        this.setUseDirection(true);
        this.setWidth(this.getHeight() / 24 * 16);
    }

    @Override
    public void handleKeyPressed(KeyEvent e) {
        if (e.getKeyCode() == Settings.KEY_MOVE_UP) {
            this.setDirection("up");

            if (this.getY() == 0 || GameGlobals.map.collide(this, this.getX(), this.getY() - 1)) {
                return;
            }

            this.move(0, -1);
        } else if (e.getKeyCode() == Settings.KEY_MOVE_DOWN) {
            this.setDirection("down");

            if (this.getY() == GameGlobals.maxH - 1 || GameGlobals.map.collide(this, this.getX(), this.getY() + 1)) {
                return;
            }

            this.move(0, +1);
        } else if (e.getKeyCode() == Settings.KEY_MOVE_LEFT) {
            this.setDirection("left");

            if (this.getX() == 0 || GameGlobals.map.collide(this, this.getX() - 1, this.getY())) {
                return;
            }

            //this.setFlipped(true);
            this.move(-1, 0);
        } else if (e.getKeyCode() == Settings.KEY_MOVE_RIGHT) {
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

    public void move(int dx, int dy) {
        this.setCurrentSprite(MOVE);

        if (dx > 0) {
            this.setDirection("right");
        } else if (dx < 0) {
            this.setDirection("left");
        } else if (dy < 0) {
            this.setDirection("up");
        } else if (dy > 0) {
            this.setDirection("down");
        }

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
