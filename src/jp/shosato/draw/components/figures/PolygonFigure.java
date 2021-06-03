package jp.shosato.draw.components.figures;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL15.*;

import java.util.ArrayList;

import org.joml.Vector2d;
import org.joml.Vector4d;

import jp.shosato.draw.components.Canvas;
import jp.shosato.draw.components.FigureComponent;
import jp.shosato.draw.events.Event;
import jp.shosato.draw.events.mouse.MouseEvent;
import jp.shosato.draw.utils.BoundingBox;
import jp.shosato.draw.utils.Utility;

/**
 * ポリゴン
 */
public class PolygonFigure extends FigureComponent {
    protected ArrayList<Vector2d> vertices = new ArrayList<Vector2d>();

    public PolygonFigure(Vector4d color) {
        this.fillColor = color;
        this.strokeColor = new Vector4d(0, 0, 0, 1);
    }

    public PolygonFigure(Vector4d fill, Vector4d stroke) {
        this.fillColor = fill;
        this.strokeColor = stroke;
    }

    public PolygonFigure(ArrayList<Vector2d> vertices, Vector4d color) {
        this.vertices = vertices;
        this.fillColor = color;
    }

    @Override
    public void draw() {
        /* fill */
        if (fillColor != null) {
            glColor4d(fillColor.x, fillColor.y, fillColor.z, fillColor.w);
            glBegin(GL_POLYGON);
            for (Vector2d vertex : vertices) {
                glVertex2d(vertex.x, vertex.y);
            }
            glEnd();
        }

        /* stroke */
        if (strokeColor != null) {
            glColor4d(strokeColor.x, strokeColor.y, strokeColor.z, strokeColor.w);
            glLineWidth((float) strokeWidth);
            glBegin(GL_LINE_LOOP);
            for (Vector2d vertex : vertices) {
                glVertex2d(vertex.x, vertex.y);
            }
            glEnd();
        }
    }

    @Override
    public void onMouseMoveDrawing(Canvas canvas, MouseEvent event) {
    }

    @Override
    public void onMouseClickDrawing(Canvas canvas, MouseEvent event) {
        switch (event.getButton()) {
            case GLFW_MOUSE_BUTTON_LEFT:
                switch (event.getAction()) {
                    case GLFW_PRESS:
                        if (vertices.size() >= 3 && event.getPos().distance(vertices.get(0)) < 30) {
                            this.onFinished.invoke(new Event());
                        } else {
                            this.vertices.add(event.getPos());
                        }
                        break;
                    default:
                        break;
                }
                break;
        }
    }

    @Override
    public boolean contains(Vector2d pos) {
        return Utility.isInsidePolygon(pos, vertices);
    }

    public void addVertex(Vector2d vertex) {
        this.vertices.add(vertex);
    }

    @Override
    public BoundingBox getBB() {
        return Utility.getBB(vertices);
    }

    @Override
    public ArrayList<Vector2d> getVertices() {
        return vertices;
    }
}
