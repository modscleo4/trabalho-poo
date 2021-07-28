package Engine;

import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.event.MouseInputListener;

import Entity.Player;
import Map.Map;
import UI.UILayer;

public final class GameGlobals {
    public static boolean loaded = false;
    public static boolean paused = false;

    public static final int MAX_FPS = 60;
    public static final double TARGET_FRAMETIME = 1000F / MAX_FPS;

    public static final int SPRITE_WIDTH = 48;
    public static final int SPRITE_HEIGHT = 48;

    public static final int width = 816;
    public static final int height = 624;
    public static final int maxW = width / SPRITE_WIDTH;
    public static final int maxH = height / SPRITE_HEIGHT;
    public static final int maxZ = 10;
    public static Map map;
    public static long internalClock = 0;

    public static String result = "running";

    public static Player player;

    public static final List<KeyListener> keyListeners = new ArrayList<>();
    public static final List<MouseInputListener> mouseInputListeners = new ArrayList<>();

    public static final UILayer uiLayer = new UILayer();
}
