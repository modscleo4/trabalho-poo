package Engine;

import java.awt.event.KeyEvent;

import javax.swing.JFrame;

public class Settings {
    public static boolean VSync = true;
    public static boolean fullscreen = false;

    public static int audioOutputMixer = -1;

    public static boolean debugInfo = false;

    public static int KEY_MOVE_UP = KeyEvent.VK_UP;
    public static int KEY_MOVE_DOWN = KeyEvent.VK_DOWN;
    public static int KEY_MOVE_LEFT = KeyEvent.VK_LEFT;
    public static int KEY_MOVE_RIGHT = KeyEvent.VK_RIGHT;

    public static int KEY_JUMP = KeyEvent.VK_SPACE;

    public static int KEY_INTERACT = KeyEvent.VK_ENTER;
    public static int KEY_ATTACK = KeyEvent.VK_C;

    public static int KEY_INVENTORY = KeyEvent.VK_E;

    public static int KEY_PAUSE = KeyEvent.VK_ESCAPE;

    public static void toggleFullScreen() {
        GameGlobals.mainWindow.dispose();

        if (Settings.fullscreen) {
            GameGlobals.mainWindow.setExtendedState(JFrame.NORMAL);
            GameGlobals.mainWindow.setUndecorated(false);
            GameGlobals.mainWindow.pack();
            GameGlobals.mainWindow.setLocationRelativeTo(null);
        } else {
            GameGlobals.mainWindow.setExtendedState(JFrame.MAXIMIZED_BOTH);
            GameGlobals.mainWindow.setUndecorated(true);
        }

        GameGlobals.mainWindow.setVisible(true);

        Settings.fullscreen = !Settings.fullscreen;
    }
}
