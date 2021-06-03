package jp.shosato.draw.components;

import static org.lwjgl.opengl.GL15.*;

import org.joml.Vector2d;
import org.joml.Vector4d;

import jp.shosato.draw.utils.BoundingBox;
import jp.shosato.draw.utils.Colors;
import jp.shosato.draw.utils.Utility;

public class RectangleComponent extends BasicComponent {
    protected Vector2d topLeft = new Vector2d(0, 0);
    protected Vector2d bottomRight = new Vector2d(100, 50);
    protected Vector4d color = new Vector4d(Colors.GRAY);

    public RectangleComponent() {
    }

    public RectangleComponent(double w, double h, Vector4d color) {
        this(new Vector2d(0, 0), new Vector2d(w, h), color);
    }

    public RectangleComponent(Vector2d topLeft, double w, double h, Vector4d color) {
        this(topLeft, new Vector2d(topLeft.x + w, topLeft.y + h), color);
    }

    public RectangleComponent(Vector2d topLeft, Vector2d bottomRight, Vector4d color) {
        this.topLeft = topLeft;
        this.bottomRight = bottomRight;
        this.color = color;
    }

    @Override
    public void draw() {
        glColor4d(color.x, color.y, color.z, color.w);
        Utility.drawRectangleFill(topLeft, bottomRight);

        // 子要素の描画。子要素は相対座標
        // glPushMatrix();
        // glTranslated(Math.min(topLeft.x, bottomRight.x), Math.min(topLeft.y,
        // bottomRight.y), 0);
        super.draw();
        // glPopMatrix();
    }

    @Override
    public boolean contains(Vector2d pos) {
        return Math.min(topLeft.x, bottomRight.x) <= pos.x && pos.x <= Math.max(topLeft.x, bottomRight.x)
                && Math.min(topLeft.y, bottomRight.y) <= pos.y && pos.y <= Math.max(topLeft.y, bottomRight.y);
    }

    public void setCoordinates(Vector2d topLeft, Vector2d bottomRight) {
        this.topLeft = topLeft;
        this.bottomRight = bottomRight;
    }

    public void setColor(Vector4d color) {
        this.color = color;
    }

    @Override
    public BoundingBox getBB() {
        return Utility.getBB(topLeft, bottomRight);
    }

    @Override
    public void translate(Vector2d d) {
        this.topLeft.add(d);
        this.bottomRight.add(d);

        for (BasicComponent child : children) {
            child.translate(d);
        }
    }
}
