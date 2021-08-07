package UI.Components;

import Engine.AnimatedSprite;
import Entity.Entity;

public abstract class Component extends Entity {
    public Component(int x, int y) {
        super(new AnimatedSprite[] { null }, x, y);
    }
}
