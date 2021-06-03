package jp.shosato.draw.utils;

import static org.lwjgl.opengl.GL15.*;
import org.joml.Vector2d;

public class BoundingBox implements IDrawable2D {

    public final Vector2d topLeft;
    public final Vector2d bottomRight;

    public BoundingBox(Vector2d topLeft, Vector2d bottomRight) {
        this.topLeft = topLeft;
        this.bottomRight = bottomRight;
    }

    @Override
    public void draw() {
        glEnable(GL_LINE_STIPPLE);

        glColor4d(1, 0, 1, 1);
        glLineStipple(2, (short) 0x0f0f);
        glLineWidth(5);

        Utility.drawRectangleLineStrip(topLeft, bottomRight);

        glDisable(GL_LINE_STIPPLE);
    }

    public void makeMargin(double margin) {
        topLeft.sub(margin, margin);
        bottomRight.add(margin, margin);
    }

    public double getWidth() {
        return Math.abs(this.bottomRight.x - this.topLeft.x);
    }

    public double getHeight() {
        return Math.abs(this.bottomRight.y - this.topLeft.y);
    }

    public Vector2d getDimension() {
        return new Vector2d(getWidth(), getHeight());
    }
}
