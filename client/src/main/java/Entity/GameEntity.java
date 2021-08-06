package Entity;

import javax.swing.Timer;

import Engine.AnimatedSprite;
import Engine.GameGlobals;

public abstract class GameEntity extends Entity {
    protected boolean isMoving = false;
    protected boolean useDirection = false;
    private String direction = "down";
    private Thread resetThread = null;

    public GameEntity(AnimatedSprite[] sprites, int x, int y) {
        super(sprites, x, y);
    }

    protected void cancelResetAnimation() {
        if (this.resetThread != null) {
            this.resetThread.interrupt();
            this.resetThread = null;
        }
    }

    protected void resetAnimation(int index) {
        this.cancelResetAnimation();

        this.resetThread = new Thread(() -> {
            if (this.getSprite().getIndex() < this.getSprite().length()) {
                try {
                    Thread.sleep(
                            this.getSprite().getTiming() * (this.getSprite().length() - this.getSprite().getIndex()));
                } catch (InterruptedException e) {
                    return;
                }
            }

            while (GameGlobals.paused) {
                //
            }

            this.setCurrentSprite(index);
        });

        this.resetThread.start();
    }

    public boolean isUsingDirection() {
        return this.useDirection;
    }

    public void setUseDirection(boolean useDirection) {
        this.useDirection = useDirection;

        for (AnimatedSprite sprite : this.getSprites()) {
            if (sprite == null) {
                continue;
            }

            sprite.setUseDirection(useDirection);
        }
    }

    public String getDirection() {
        return this.direction;
    }

    public void setDirection(String direction) {
        if (!direction.equals("right") && !direction.equals("left") && !direction.equals("up")
                && !direction.equals("down")) {
            return;
        }

        this.direction = direction;

        for (AnimatedSprite sprite : this.getSprites()) {
            if (sprite == null) {
                continue;
            }

            sprite.setDirection(direction);
        }
    }

    protected void move(int MOVE, int dx, int dy, Runnable callback) {
        if (this.isMoving) {
            return;
        }

        this.cancelResetAnimation();

        if (dx == 0 && dy < 0) {
            this.setDirection("up");

            if (!this.isAbsoluteCoords()) {
                if (this.getY() == 0 || GameGlobals.map.collide(this, this.getX(), this.getY() - 1)) {
                    this.isMoving = false;
                    callback.run();
                    return;
                }
            }
        } else if (dx == 0 && dy > 0) {
            this.setDirection("down");

            if (!this.isAbsoluteCoords()) {
                if (this.getY() == GameGlobals.maxH - 1
                        || GameGlobals.map.collide(this, this.getX(), this.getY() + 1)) {
                    this.isMoving = false;
                    callback.run();
                    return;
                }
            }
        } else if (dx < 0 && dy == 0) {
            this.setDirection("left");

            if (!this.isAbsoluteCoords()) {
                if (this.getX() == 0 || GameGlobals.map.collide(this, this.getX() - 1, this.getY())) {
                    this.isMoving = false;
                    callback.run();
                    return;
                }
            }
        } else if (dx > 0 && dy == 0) {
            this.setDirection("right");

            if (!this.isAbsoluteCoords()) {
                if (this.getX() == GameGlobals.maxW - 1
                        || GameGlobals.map.collide(this, this.getX() + 1, this.getY())) {
                    this.isMoving = false;
                    callback.run();
                    return;
                }
            }
        }

        this.isMoving = true;
        this.setCurrentSprite(MOVE, false);

        this.getSprite().reset();
        this.getSprite().stop();

        int screenX = this.getScreenX();
        int screenY = this.getScreenY();

        this.setX(this.getX() + dx);
        this.setY(this.getY() + dy);

        int targetScreenX = this.getScreenX();
        int targetScreenY = this.getScreenY();

        this.setScreenX(screenX);
        this.setScreenY(screenY);

        int delay = (this.getSprite().length() * this.getSprite().getTiming())
                / (int) (Math.sqrt(Math.pow(targetScreenX - screenX, 2) + Math.pow(targetScreenY - screenY, 2)));

        this.setAbsoluteCoords(true);
        Timer t = new Timer(delay, (ae) -> {
            if (GameGlobals.paused) {
                return;
            }

            if (this.getScreenX() == targetScreenX && this.getScreenY() == targetScreenY) {
                this.setAbsoluteCoords(false);
                this.isMoving = false;
                this.setScreenX(-1);
                this.setScreenY(-1);
                if (callback != null) {
                    callback.run();
                }
                ((Timer) ae.getSource()).stop();
                return;
            }

            this.setScreenX(this.getScreenX() + 2 * dx);
            this.setScreenY(this.getScreenY() + 2 * dy);
        });

        this.getSprite().animate();

        t.start();
    }

    public abstract void move(int x, int y);
}
