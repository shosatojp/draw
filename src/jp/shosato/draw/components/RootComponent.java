package jp.shosato.draw.components;

import org.joml.Vector2d;

import jp.shosato.draw.Controller;
import jp.shosato.draw.Window;
import jp.shosato.draw.utils.BoundingBox;

/**
 * GUIのルート要素
 */
public class RootComponent extends BasicComponent {

    public RootComponent(Window window, Controller controller) {
        this.dimension = new Vector2d(window.getWidth(), window.getHeight());
        this.window = window;
        this.controller = controller;
        this.parent = null;
    }

    /**
     * どんな座標も含まれているものとする
     */
    @Override
    public boolean contains(Vector2d pos) {
        return true;
    }

    @Override
    public BoundingBox getBB() {
        return null;
    }

    @Override
    public Vector2d getCenter() {
        return new Vector2d(dimension).div(2);
    }
}
