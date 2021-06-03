package jp.shosato.draw.components;

import java.util.Vector;

import org.joml.Vector2d;
import org.joml.Vector4d;

import jp.shosato.draw.utils.Colors;

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

        double y = 0;

        for (BasicComponent _child : getChildren()) {
            y += _child.getBB().getHeight();
        }

        child.translate.add(0, y);
        super.addChildComponent(child);
    }
}
