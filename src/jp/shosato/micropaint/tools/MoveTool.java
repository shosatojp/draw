package jp.shosato.micropaint.tools;

import org.joml.Vector2d;

import jp.shosato.micropaint.components.Canvas;
import jp.shosato.micropaint.components.FigureComponent;
import jp.shosato.micropaint.events.mouse.MouseClickEventListener;
import jp.shosato.micropaint.events.mouse.MouseEnterEventListener;
import jp.shosato.micropaint.events.mouse.MouseEvent;
import jp.shosato.micropaint.events.mouse.MouseLeaveEventListener;
import jp.shosato.micropaint.events.mouse.MouseMoveEventListener;

import static org.lwjgl.glfw.GLFW.*;

public class MoveTool extends Tool
        implements MouseClickEventListener, MouseMoveEventListener, MouseEnterEventListener, MouseLeaveEventListener {

    private Vector2d prevPos;
    private boolean moving = false;

    /**
     * 選択結果を使うので
     */
    private SelectTool selectTool;

    public MoveTool(Canvas canvas, SelectTool selectTool) {
        super(canvas);
        this.selectTool = selectTool;
    }

    @Override
    public void draw() {

    }

    @Override
    public void onMouseMoved(MouseEvent event) {
        if (moving) {
            Vector2d pos = event.getPos();
            Vector2d diff = new Vector2d(pos).sub(prevPos);
            for (FigureComponent figure : selectTool.getSelected()) {
                figure.move(diff);
            }
            prevPos = pos;
        }
    }

    @Override
    public void onMouseClicked(MouseEvent event) {
        switch (event.getButton()) {
            case GLFW_MOUSE_BUTTON_LEFT:
                switch (event.getAction()) {
                    case GLFW_PRESS:
                        if (event.getTarget() instanceof FigureComponent) {
                            moving = true;
                            prevPos = event.getPos();
                        }
                        break;
                    case GLFW_RELEASE:
                        moving = false;
                        break;
                }
                break;
        }
    }

    @Override
    public void onMouseEnter(MouseEvent event) {
        if (event.getTarget() instanceof FigureComponent) {
            event.getTarget().setCursor(GLFW_HAND_CURSOR);
        }
    }

    @Override
    public void onMouseEnter(MouseEvent event, boolean captureing) {
    }

    @Override
    public void onMouseLeave(MouseEvent event) {
        if (event.getTarget() instanceof FigureComponent) {
            event.getTarget().setCursor(GLFW_ARROW_CURSOR);
        }
    }

    @Override
    public void onMouseLeave(MouseEvent event, boolean captureing) {
    }

    @Override
    public void onMouseClicked(MouseEvent event, boolean captureing) {
    }

    @Override
    public void onMouseMoved(MouseEvent event, boolean captureing) {
    }
}
