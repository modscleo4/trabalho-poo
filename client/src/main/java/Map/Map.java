package Map;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.List;

import Engine.AnimatedSprite;
import Engine.BaseObject;
import Engine.GameGlobals;
import Engine.Sound;
import Engine.SoundManager;
import Entity.Enemy;
import Entity.Entity;
import Entity.Slime;
import UI.Components.Text;

public class Map {
    private List<BaseObject>[] sprites;
    private BaseObject[][][] map;
    private int maxX;
    private int maxY;
    private String bgMusic;
    private boolean bgPlaying = false;
    private Sound bgMusicHandler;
    private boolean initialized = false;
    private int timer = 250;
    private boolean useTimer;

    private Text txtTimer;

    public Map(List<BaseObject>[] sprites, int maxX, int maxY, String bgMusic, boolean useTimer) {
        this.sprites = sprites;
        this.maxX = maxX;
        this.maxY = maxY;
        this.bgMusic = bgMusic;
        this.useTimer = useTimer;

        txtTimer = new Text("", 0, 0);
        txtTimer.setAbsoluteCoords(true);
        txtTimer.setCenterScreen(true);
        txtTimer.setScreenY(50);
        txtTimer.setColor(Color.WHITE);
        txtTimer.setFont(new Font(Font.MONOSPACED, Font.BOLD, 24));
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

    public void draw(Graphics2D g) {
        for (int z = 0; z < GameGlobals.maxZ; z++) {
            for (int i = 0; i < this.getMap().length; i++) {
                for (int j = 0; j < this.getMap()[i].length; j++) {
                    if (z >= this.getMap()[i][j].length || this.getMap()[i][j][z] == null) {
                        continue;
                    }

                    this.getMap()[i][j][z].draw(g);
                }
            }
        }

        if (this.useTimer) {
            txtTimer.setText("" + this.timer);
            txtTimer.draw(g);
        }
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

    public void removeEnemy(Enemy enemy) {
        if (!this.sprites[2].contains(enemy)) {
            return;
        }

        this.sprites[2].remove(enemy);
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

    public int getTimer() {
        return this.timer;
    }

    public void setTimer(int timer) {
        if (timer < 0) {
            return;
        }

        this.timer = timer;
    }

    public void runTimerCycle() {
        this.setTimer(this.getTimer() - 1);
    }
}
