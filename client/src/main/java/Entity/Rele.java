package Entity;

import Engine.AnimatedSprite;

public class Rele extends GameEntity {
    public static final int IDLE = 0;
    public static final int MOVE = 1;
    public static final int HIT = 2;

    private Thread moveThread = null;

    public Rele(int x, int y) {
        super(new AnimatedSprite[] { new AnimatedSprite("rele/idle", true, 100, x, y, true),
                new AnimatedSprite("rele/move", false, 100, x, y, true),
                new AnimatedSprite("rele/hit", false, 100, x, y, true) }, x, y);
        this.setSolid(true);
        this.setUseDirection(true);
        this.setWidth(this.getHeight() / 24 * 16);
    }

    public void move(int dx, int dy) {
        this.moveThread = new Thread(() -> {
            super.move(MOVE, dx, dy, () -> {
                this.resetAnimation(IDLE);

                this.moveThread.interrupt();
                this.moveThread = null;
            });
        });

        this.moveThread.start();
    }
}
