package jp.shosato.draw.components;

import java.util.Vector;

import org.joml.Vector2d;
import org.joml.Vector4d;

import jp.shosato.draw.utils.Colors;

public class HorizontalContainerComponent extends RectangleComponent {

    public HorizontalContainerComponent(double w, double h) {
        this(new Vector2d(0, 0), w, h, Colors.GRAY);
    }

    public HorizontalContainerComponent(Vector2d topLeft, double w, double h, Vector4d color) {
        this(topLeft, new Vector2d(topLeft.x + w, topLeft.y + h), color);
    }

    public HorizontalContainerComponent(Vector2d topLeft, Vector2d bottomRight, Vector4d color) {
        super(topLeft, bottomRight, color);
        this.topLeft = new Vector2d(topLeft);
        this.bottomRight = new Vector2d(bottomRight);
        this.color = new Vector4d(color);
    }

    @Override
    public void addChildComponent(BasicComponent child) {

        assert (child instanceof RectangleComponent);

        double x = this.topLeft.x;
        double y = this.topLeft.y;

        for (BasicComponent _child : getChildren()) {
            x += _child.getBB().getWidth();
        }

        child.translate(new Vector2d(x, y).sub(this.topLeft));
        super.addChildComponent(child);
    }
}
