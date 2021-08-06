package UI;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class UILayer {
    private List<Consumer<Graphics>> layers = new ArrayList<>();

    public void addLayer(Consumer<Graphics> c) {
        this.layers.add(c);
    }

    public void clear() {
        this.layers.clear();
    }

    public void draw(Graphics g) {
        for (int i = 0; i < this.layers.size(); i++) {
            Consumer<Graphics> c = this.layers.get(i);
            c.accept(g);
        }
    }
}
