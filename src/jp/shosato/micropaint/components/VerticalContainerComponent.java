package jp.shosato.micropaint.components;

import java.util.Vector;

import org.joml.Vector2d;
import org.joml.Vector4d;

import jp.shosato.micropaint.utils.Colors;

/**
 * 垂直に並べるコンテナ要素
 */
public class VerticalContainerComponent extends RectangleComponent {

    public VerticalContainerComponent(double w, double h) {
        this(new Vector2d(0, 0), w, h, Colors.GRAY);
    }

    public VerticalContainerComponent(Vector2d translate, double w, double h, Vector4d color) {
        super(translate, w, h, color);
    }

    @Override
    public void addChildComponent(BasicComponent child) {

        assert (child instanceof RectangleComponent);

        double y = getChildren().stream().map((BasicComponent _child) -> _child.getBB().getHeight())
                .mapToDouble(Double::doubleValue).sum();

        child.translate.add(0, y);
        super.addChildComponent(child);
    }
}
