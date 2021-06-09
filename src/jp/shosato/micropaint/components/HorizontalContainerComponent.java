package jp.shosato.micropaint.components;

import java.util.Vector;

import org.joml.Vector2d;
import org.joml.Vector4d;

import jp.shosato.micropaint.utils.Colors;

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

        double x = getChildren().stream().map((BasicComponent _child) -> _child.getBB().getWidth())
                .mapToDouble(Double::doubleValue).sum();

        child.translate.add(x, 0);
        super.addChildComponent(child);
    }
}
