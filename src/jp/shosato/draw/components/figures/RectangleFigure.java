package jp.shosato.draw.components.figures;

import static org.lwjgl.glfw.GLFW.*;

import org.joml.Vector2d;
import org.joml.Vector4d;

import jp.shosato.draw.components.Canvas;
import jp.shosato.draw.events.Event;
import jp.shosato.draw.events.mouse.MouseEvent;

public class RectangleFigure extends PolygonFigure {

    public RectangleFigure(Vector4d color) {
        super(color);
    }

    private void updateVertices(Vector2d pos) {

        assert (this.vertices.size() > 0);

        Vector2d v0 = this.vertices.get(0);
        Vector2d v1 = new Vector2d(pos.x, v0.y);
        Vector2d v2 = new Vector2d(pos.x, pos.y);
        Vector2d v3 = new Vector2d(v0.x, pos.y);

        this.vertices.clear();
        this.vertices.add(v0);
        this.vertices.add(v1);
        this.vertices.add(v2);
        this.vertices.add(v3);

    }

    @Override
    public void onMouseMoveDrawing(Canvas canvas, MouseEvent event) {
        if (this.vertices.size() > 0) {
            Vector2d pos = event.getPos();
            updateVertices(pos);
        }
    }

    @Override
    public void onMouseClickDrawing(Canvas canvas, MouseEvent event) {
        switch (event.getButton()) {
            case GLFW_MOUSE_BUTTON_LEFT:
                switch (event.getAction()) {
                    case GLFW_PRESS: {
                        Vector2d pos = event.getPos();
                        this.vertices.add(new Vector2d(pos));
                    }
                        break;
                    case GLFW_RELEASE: {
                        if (this.vertices.size() > 0) {
                            Vector2d pos = event.getPos();
                            updateVertices(pos);
                            this.onFinished.invoke(new Event());
                        }
                    }
                        break;
                }
                break;
        }
    }
}
