package Engine;

import java.awt.Image;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

public final class SpriteManager {
    private static final Map<String, Image> sprites = new HashMap<>();

    public static void loadAll() {
        List<String> files = ResourceLoader.filesInDirectory("sprites/");
        for (String f : files) {
            try {
                BufferedInputStream s = new BufferedInputStream(
                        ResourceLoader.loadFile(String.format("sprites/%s", f)));
                sprites.put(f, ImageIO.read(s));
                s.close();
            } catch (IllegalArgumentException | IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static Image getImage(String path) {
        if (!sprites.containsKey(path)) {
            try {
                BufferedInputStream s = new BufferedInputStream(
                        ResourceLoader.loadFile(String.format("sprites/%s.png", path)));
                sprites.put(path, ImageIO.read(s));
                s.close();
            } catch (IllegalArgumentException | IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        return sprites.get(path);
    }
}
