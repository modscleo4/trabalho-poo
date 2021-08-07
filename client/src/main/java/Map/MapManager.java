package Map;

import java.util.ArrayList;
import java.util.List;

import Engine.BaseObject;
import Engine.GameGlobals;
import Engine.Sprite;

public class MapManager {
    public static Map map1;

    public static Map getMap1() {
        if (map1 != null) {
            return map1;
        }

        List<BaseObject>[] layers = new ArrayList[GameGlobals.maxZ];
        for (int z = 0; z < layers.length; z++) {
            layers[z] = new ArrayList<>();
        }

        Sprite background = new Sprite("background/background_arena", 0, 0, false);

        background.setWidth(GameGlobals.width);
        background.setHeight(GameGlobals.height);
        layers[0].add(background);

        for (int i = 0; i < GameGlobals.maxW; i++) {
            for (int j = 0; j < GameGlobals.maxH; j++) {
                layers[1].add(new Sprite("grid", i, j, false));
            }
        }

        //Slime slime = new Slime(5, 5, false);
        //Rele rele = new Rele(4, 4);

        layers[3].add(GameGlobals.player);
        layers[3].add(GameGlobals.player2);
        //layers[3].add(rele);
        //layers[2].add(slime);

        map1 = new Map(layers, GameGlobals.maxW, GameGlobals.maxH, "bg1");

        return map1;
    }
}
