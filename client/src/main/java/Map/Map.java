package Map;

import java.util.List;

import Engine.AnimatedSprite;
import Engine.BaseObject;
import Engine.Sound;
import Engine.SoundManager;
import Entity.Enemy;
import Entity.Entity;
import Entity.Slime;

public class Map {
    private List<BaseObject>[] sprites;
    private BaseObject[][][] map;
    private int maxX;
    private int maxY;
    private String bgMusic;
    private boolean bgPlaying = false;
    private Sound bgMusicHandler;
    private boolean initialized = false;

    public Map(List<BaseObject>[] sprites, int maxX, int maxY, String bgMusic) {
        this.sprites = sprites;
        this.maxX = maxX;
        this.maxY = maxY;
        this.bgMusic = bgMusic;
    }

    public BaseObject[][][] mount() {
        this.map = new BaseObject[this.maxX][this.maxY][this.sprites.length];
        for (int z = 0; z < this.sprites.length; z++) {
            List<BaseObject> zLayerSprites = this.sprites[z];

            for (BaseObject obj : zLayerSprites) {
                this.map[obj.getX()][obj.getY()][z] = obj;
            }
        }

        return this.map;
    }

    public void animateAllSprites() {
        if (this.initialized) {
            return;
        }

        for (int z = 0; z < this.sprites.length; z++) {
            for (BaseObject obj : this.sprites[z]) {
                if (AnimatedSprite.class.isInstance(obj) && ((AnimatedSprite) obj).isPreAnimated()) {
                    ((AnimatedSprite) obj).animate();
                } else if (Entity.class.isInstance(obj) && ((Entity) obj).getSprite().isPreAnimated()) {
                    ((Entity) obj).getSprite().animate();
                }
            }
        }

        this.initialized = true;
    }

    public BaseObject[][][] getMap() {
        return this.map;
    }

    public boolean collide(BaseObject obj, int x, int y) {
        if (this.getMap() == null) {
            this.mount();
        }

        for (int z = 0; z < this.getMap()[x][y].length; z++) {
            if (this.getMap()[x][y][z] == null) {
                continue;
            }

            if (obj.equals(this.getMap()[x][y][z])) {
                continue;
            }

            if (this.getMap()[x][y][z].isSolid()) {
                return true;
            }
        }

        return false;
    }

    public boolean objectNear(int x, int y, BaseObject obj) {
        if (obj == null) {
            return false;
        }

        for (int z = 0; z < this.getMap()[x][y].length; z++) {
            if (obj.equals(this.getMap()[x - 1][y][z]) || obj.equals(this.getMap()[x - 1][y - 1][z])
                    || obj.equals(this.getMap()[x][y - 1][z]) || obj.equals(this.getMap()[x + 1][y - 1][z])
                    || obj.equals(this.getMap()[x + 1][y][z]) || obj.equals(this.getMap()[x + 1][y + 1][z])
                    || obj.equals(this.getMap()[x][y + 1][z]) || obj.equals(this.getMap()[x - 1][y + 1][z])) {
                return true;
            }
        }

        return false;
    }

    public Enemy enemyAt(int x, int y) {
        for (int z = 0; z < this.getMap()[x][y].length; z++) {
            if (Enemy.class.isInstance(this.getMap()[x][y][z])) {
                return (Enemy) this.getMap()[x][y][z];
            }
        }

        return null;
    }

    public void spawnEnemy(int x, int y) {
        if (this.enemyAt(x, y) != null) {
            return;
        }

        Enemy enemy = new Slime(x, y, true);
        this.sprites[2].add(enemy);
        enemy.getSprite().animate();
    }

    public void playBG() {
        if (!this.bgPlaying) {
            this.bgPlaying = true;

            if (bgMusicHandler == null) {
                bgMusicHandler = SoundManager.playSound(bgMusic, true);
            } else {
                bgMusicHandler.play();
            }
        }
    }

    public void pauseBG() {
        if (this.bgPlaying) {
            this.bgPlaying = false;

            bgMusicHandler.pause();
        }
    }
}
