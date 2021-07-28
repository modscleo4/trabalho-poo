package Map;

import java.util.ArrayList;
import java.util.List;

import Engine.BaseObject;
import Engine.GameGlobals;
import Engine.Sprite;
import Entity.Player;
import Entity.Slime;

public class MapManager {
    public static final Map map1;

    static {
        List<BaseObject>[] layers = new ArrayList[GameGlobals.maxZ];
        for (int z = 0; z < layers.length; z++) {
            layers[z] = new ArrayList<>();
        }

        for (int i = 0; i < GameGlobals.maxW; i++) {
            for (int j = 0; j < GameGlobals.maxH; j++) {
                layers[0].add(new Sprite("floor", i, j, false));
            }
        }

        GameGlobals.player = new Player(1, 1);
        Slime slime = new Slime(5, 5, false);

        layers[2].add(GameGlobals.player);
        layers[1].add(slime);

        map1 = new Map(layers, GameGlobals.maxW, GameGlobals.maxH, "bg1");
    }
}
