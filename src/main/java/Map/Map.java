package Map;

import java.util.List;

import Engine.BaseObject;
import Engine.GameGlobals;
import Engine.SoundManager;

public class Map {
    private List<BaseObject>[] sprites;
    private BaseObject[][][] map;
    private int maxX;
    private int maxY;
    private String bgMusic;
    private boolean bgPlaying = false;

    public Map(List<BaseObject>[] sprites, int maxX, int maxY, String bgMusic) {
        this.sprites = sprites;
        this.maxX = maxX;
        this.maxY = maxY;
        this.bgMusic = bgMusic;
    }

    public BaseObject[][][] mount() {
        BaseObject[][][] ret = new BaseObject[this.maxX][this.maxY][this.sprites.length];
        for (int z = 0; z < this.sprites.length; z++) {
            List<BaseObject> zLayerSprites = this.sprites[z];

            for (BaseObject obj : zLayerSprites) {
                ret[obj.getX()][obj.getY()][z] = obj;
            }
        }

        this.map = ret;
        return ret;
    }

    public BaseObject[][][] getMap() {
        return map;
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

    public boolean playerNear(int x, int y) {
        for (int z = 0; z < this.getMap()[x][y].length; z++) {
            if (GameGlobals.player.equals(this.getMap()[x - 1][y][z])
                    || GameGlobals.player.equals(this.getMap()[x - 1][y - 1][z])
                    || GameGlobals.player.equals(this.getMap()[x][y - 1][z])
                    || GameGlobals.player.equals(this.getMap()[x + 1][y - 1][z])
                    || GameGlobals.player.equals(this.getMap()[x + 1][y][z])
                    || GameGlobals.player.equals(this.getMap()[x + 1][y + 1][z])
                    || GameGlobals.player.equals(this.getMap()[x][y + 1][z])
                    || GameGlobals.player.equals(this.getMap()[x - 1][y + 1][z])) {
                return true;
            }
        }

        return false;
    }

    public void playBG() {
        if (!this.bgPlaying) {
            this.bgPlaying = true;
            SoundManager.playSound(bgMusic);
        }
    }

    public void pauseBG() {
        if (this.bgPlaying) {
            this.bgPlaying = false;
        }
    }
}
