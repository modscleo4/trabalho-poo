package UI;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class UILayer {
    private List<Consumer<Graphics2D>> layers = new ArrayList<>();

    public void addLayer(Consumer<Graphics2D> c) {
        this.layers.add(c);
    }

    public void clear() {
        this.layers.clear();
    }

    public void draw(Graphics2D g) {
        for (int i = 0; i < this.layers.size(); i++) {
            Consumer<Graphics2D> c = this.layers.get(i);
            c.accept(g);
        }
    }
}
