package jp.shosato.draw.components;

import org.joml.Vector2d;

import jp.shosato.draw.Controller;
import jp.shosato.draw.Window;
import jp.shosato.draw.utils.BoundingBox;

public class RootComponent extends BasicComponent {

    public RootComponent(Window window, Controller controller) {
        this.window = window;
        this.controller = controller;
        this.parent = null;
    }

    @Override
    public boolean contains(Vector2d pos) {
        return true;
    }

    @Override
    public BoundingBox getBB() {
        return null;
    }

    @Override
    public void translate(Vector2d d) {
    }
}
