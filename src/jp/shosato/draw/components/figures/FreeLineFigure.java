package jp.shosato.draw.components.figures;

import java.util.ArrayList;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL15.*;
import org.joml.Vector2d;
import org.joml.Vector4d;

import jp.shosato.draw.components.Canvas;
import jp.shosato.draw.components.FigureComponent;
import jp.shosato.draw.events.Event;
import jp.shosato.draw.events.mouse.MouseEvent;
import jp.shosato.draw.utils.BoundingBox;
import jp.shosato.draw.utils.Utility;

/**
 * 手書きペン
 */
public class FreeLineFigure extends FigureComponent {
    private ArrayList<Vector2d> dots = new ArrayList<Vector2d>();

    public FreeLineFigure(Vector4d color, double width) {
        this.strokeColor = color;
        this.strokeWidth = width;
        this.fillColor = new Vector4d(0, 0, 0, 0);
    }

    @Override
    public void draw() {
        glLineWidth((float) strokeWidth);
        glColor4d(strokeColor.x, strokeColor.y, strokeColor.z, strokeColor.w);
        glBegin(GL_LINE_STRIP);
        for (Vector2d dot : dots) {
            glVertex2d(dot.x, dot.y);
        }
        glEnd();
    }

    public void addDot(Vector2d dot) {
        dots.add(dot);
    }

    @Override
    public void onMouseMoveDrawing(Canvas canvas, MouseEvent event) {
        if (!dots.isEmpty() && dots.get(dots.size() - 1).distance(event.getPos()) > 5) {
            addDot(new Vector2d(event.getPos()));
        }
    }

    @Override
    public void onMouseClickDrawing(Canvas canvas, MouseEvent event) {
        switch (event.getButton()) {
            case GLFW_MOUSE_BUTTON_LEFT:
                switch (event.getAction()) {
                    case GLFW_PRESS: {
                        Vector2d pos = event.getPos();
                        this.addDot(new Vector2d(pos));
                    }
                        break;
                    case GLFW_RELEASE: {
                        Vector2d pos = event.getPos();
                        this.addDot(new Vector2d(pos));
                        this.onFinished.invoke(new Event());
                    }
                        break;
                }
                break;
        }
    }

    // TODO
    @Override
    public boolean contains(Vector2d pos) {
        for (int i = 0, length = dots.size(); i < length - 1; i++) {
            double len = pos.distance(dots.get(i));
            double theta = Utility.getAngleRadian(pos, dots.get(i), dots.get(i + 1));
            if (len * Math.sin(theta) < 10) {
                return true;
            }
        }
        return false;
    }

    @Override
    public BoundingBox getBB() {
        return Utility.getBB(this.dots);
    }

    @Override
    public ArrayList<Vector2d> getVertices() {
        return dots;
    }

    @Override
    public void translate(Vector2d d) {
        for (Vector2d dot : dots) {
            dot.add(d);
        }
    }

}
