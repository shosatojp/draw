package jp.shosato.draw.utils;

import static org.lwjgl.glfw.GLFW.*;
import org.joml.Vector2d;

public abstract class Hoverable {
    private boolean hovered = false;

    public abstract boolean contains(Vector2d pos);

    public boolean getHovered() {
        return hovered;
    }

    public void setHovered(boolean hovered) {
        this.hovered = hovered;
    }

    // cursor
    protected int cursor = GLFW_ARROW_CURSOR;

    public void setCursor(int shape) {
        this.cursor = shape;
    }

    public int getCursor() {
        return cursor;
    }
}
