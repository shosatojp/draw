package jp.shosato.draw.components;

import static org.lwjgl.opengl.GL15.*;

import org.joml.Vector2d;
import org.joml.Vector4d;

import jp.shosato.draw.events.mouse.MouseEvent;
import jp.shosato.draw.utils.BoundingBox;
import jp.shosato.draw.utils.Colors;
import jp.shosato.draw.utils.Utility;

public class RectangleComponent extends BasicComponent {

    protected Vector4d color = new Vector4d(Colors.GRAY);

    public RectangleComponent() {
    }

    public RectangleComponent(double w, double h, Vector4d color) {
        this(new Vector2d(0, 0), w, h, color);
    }

    public RectangleComponent(Vector2d translate, double w, double h, Vector4d color) {
        this.translate = new Vector2d(translate);
        this.dimension = new Vector2d(w, h);
        this.color = new Vector4d(color);
    }

    @Override
    public void draw() {
        glPushMatrix();
        Utility.glTransform(dimension, translate, scale, rotate);

        glColor4d(color.x, color.y, color.z, color.w);
        Utility.drawRectangleFill(new Vector2d(0, 0), dimension);

        for (BasicComponent child : children) {
            child.draw();
        }

        glPopMatrix();
    }

    @Override
    public boolean contains(Vector2d pos) {
        Vector2d _pos = Utility.untransform(pos, getCenter(), translate, scale, rotate);
        return 0 <= _pos.x && _pos.x <= dimension.x && 0 <= _pos.y && _pos.y <= dimension.y;
    }

    public void setColor(Vector4d color) {
        this.color = color;
    }

    @Override
    public Vector2d getCenter() {
        return new Vector2d(dimension).mul(0.5);
    }

    @Override
    public BoundingBox getBB() {
        return Utility.getBB(new Vector2d(0, 0), dimension);
    }
}
