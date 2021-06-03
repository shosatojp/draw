package jp.shosato.draw.components;

import java.util.Vector;

import org.joml.Vector2d;
import org.joml.Vector4d;

import jp.shosato.draw.utils.Colors;

public class HorizontalContainerComponent extends RectangleComponent {

    public HorizontalContainerComponent(double w, double h) {
        this(new Vector2d(0, 0), w, h, Colors.GRAY);
    }

    public HorizontalContainerComponent(Vector2d translate, double w, double h, Vector4d color) {
        super(translate, w, h, color);
    }

    @Override
    public void addChildComponent(BasicComponent child) {

        assert (child instanceof RectangleComponent);

        double x = 0;

        for (BasicComponent _child : getChildren()) {
            x += _child.getBB().getWidth();
        }

        child.translate.add(x, 0);
        super.addChildComponent(child);
    }
}
