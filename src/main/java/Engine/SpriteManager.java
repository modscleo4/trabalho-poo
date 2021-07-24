package Engine;

import java.awt.Image;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import javax.imageio.ImageIO;

public final class SpriteManager {
    private static final Map<String, Image> sprites = new HashMap<>();

    public static List<String> spritesInDirectory(String path) {
        List<String> files = ResourceLoader.filesInDirectory("sprites/" + path, false);
        for (int i = 0; i < files.size(); i++) {
            files.set(i, path + "/" + files.get(i));
        }

        return files;
    }

    public static List<String> filesInDirectory() {
        return ResourceLoader.filesInDirectory("sprites/");
    }

    public static void loadAll(Consumer<Integer> c) {
        List<String> files = filesInDirectory();
        int i = 1;
        for (String f : files) {
            c.accept(i++);
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
