package Entity;

import Engine.AnimatedSprite;

public abstract class Enemy extends GameEntity {
    public Enemy(AnimatedSprite[] sprites, int x, int y) {
        super(sprites, x, y);
    }

    public abstract void hit(int damage);
}
