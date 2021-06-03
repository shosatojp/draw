package jp.shosato.draw.events.key;

import jp.shosato.draw.events.Event;
import static org.lwjgl.glfw.GLFW.*;

public class KeyInputEvent extends Event {
    public final long window;
    public final int key;
    public final int scancode;
    public final int action;
    public final int mods;

    private final String[] actions = { "GLFW_RELEASE", "GLFW_PRESS", "GLFW_REPEAT" };

    public KeyInputEvent(long window, int key, int scancode, int action, int mods) {
        this.window = window;
        this.key = key;
        this.scancode = scancode;
        this.action = action;
        this.mods = mods;
    }

    @Override
    public String toString() {
        return String.format("%s %s %c(%d) %d", actions[action], getModString(), (char)key, key, scancode);
    }

    public String getModString() {
        switch (mods) {
            case GLFW_MOD_SHIFT:
                return "GLFW_MOD_SHIFT";
            case GLFW_MOD_CONTROL:
                return "GLFW_MOD_CONTROL";
            case GLFW_MOD_ALT:
                return "GLFW_MOD_ALT";
            case GLFW_MOD_SUPER:
                return "GLFW_MOD_SUPER";
            case GLFW_MOD_CAPS_LOCK:
                return "GLFW_MOD_CAPS_LOCK";
            case GLFW_MOD_NUM_LOCK:
                return "GLFW_MOD_NUM_LOCK";
            default:
                return "";
        }
    }
}
