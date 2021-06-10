package jp.shosato.micropaint.components;

import java.lang.reflect.Method;
import java.util.EventListener;
import java.util.HashMap;
import java.util.Map.Entry;

import org.joml.Vector2d;
import org.joml.Vector4d;
import static org.lwjgl.glfw.GLFW.*;
import jp.shosato.micropaint.events.EventHandler;
import jp.shosato.micropaint.events.handlers.ScrolledEvent;
import jp.shosato.micropaint.events.handlers.ScrolledEventHandler;
import jp.shosato.micropaint.events.handlers.ScrolledEvent.Direction;
import jp.shosato.micropaint.events.key.KeyInputEvent;
import jp.shosato.micropaint.events.key.KeyInputEventListener;
import jp.shosato.micropaint.events.mouse.MouseClickEventListener;
import jp.shosato.micropaint.events.mouse.MouseEnterEventListener;
import jp.shosato.micropaint.events.mouse.MouseEvent;
import jp.shosato.micropaint.events.mouse.MouseEventListener;
import jp.shosato.micropaint.events.mouse.MouseLeaveEventListener;
import jp.shosato.micropaint.events.mouse.MouseMoveEventListener;
import jp.shosato.micropaint.events.scroll.ScrollInputEventListener;
import jp.shosato.micropaint.tools.MoveTool;
import jp.shosato.micropaint.tools.Tool;
import jp.shosato.micropaint.utils.Colors;
import jp.shosato.micropaint.utils.Utility;

import static org.lwjgl.opengl.GL15.*;

/**
 * 描画する図形の親要素。ツールの有効・無効を管理する
 */
public class Canvas extends RectangleComponent implements MouseMoveEventListener, MouseClickEventListener,
        MouseEnterEventListener, MouseLeaveEventListener, ScrollInputEventListener, KeyInputEventListener {
    /**
     * ツールの有効・無効化リスト
     */
    private HashMap<Tool, Boolean> tools = new HashMap<Tool, Boolean>();

    public EventHandler<ScrolledEventHandler> onScrolled = new EventHandler<>();

    private boolean pressingShift = false;
    private boolean pressingCtrl = false;

    /**
     * キャンバスの拡大率
     */
    public Vector2d canvasScale = new Vector2d(1, 1);
    public Vector2d canvasTranslate = new Vector2d(0, 0);
    private Vector4d backgroundColor = new Vector4d(Colors.GRAY);

    public Canvas(double w, double h) {
        this(new Vector2d(0, 0), w, h, Colors.GRAY);
    }

    public Canvas(double w, double h, Vector4d color) {
        this(new Vector2d(0, 0), w, h, color);
    }

    public Canvas(Vector2d translated, double w, double h, Vector4d color) {
        super(translated, w, h, color);
    }

    public void setTools(HashMap<Tool, Boolean> tools) {
        this.tools = tools;
    }

    /**
     * ビューポート座標を簡易的に計算
     */
    private Vector2d getViewportCoords() {
        Vector2d result = new Vector2d(this.translate);
        BasicComponent p = this;
        while (p.parent != null) {
            p = p.parent;
            result.add(p.translate);
        }
        return result;
    }

    @Override
    public void draw() {
        glPushMatrix();
        Utility.glTransform(dimension, translate, scale, rotate);
        {
            /**
             * フレームバッファに書き込む領域を制限 図形がキャンバス外に行かないように
             */
            glEnable(GL_SCISSOR_TEST);
            Vector2d viewport = getViewportCoords();
            glScissor((int) viewport.x, (int) viewport.y, (int) dimension.x, (int) dimension.y);

            /* キャンバスの背景を描画 */
            glColor3d(backgroundColor.x, backgroundColor.y, backgroundColor.z);
            Utility.drawRectangleFill(dimension);

            glPushMatrix();
            Utility.glTransform(dimension, canvasTranslate, canvasScale, 0);
            {
                /* キャンバスを描画 */
                glColor3d(color.x, color.y, color.z);
                Utility.drawRectangleFill(dimension);

                /* 図形を描画 */
                for (BasicComponent child : children) {
                    child.draw();
                }

                /* ツールを描画 */
                for (Entry<Tool, Boolean> e : tools.entrySet()) {
                    if (e.getValue()) {
                        e.getKey().draw();
                    }
                }
            }
            glPopMatrix();

            glDisable(GL_SCISSOR_TEST);
        }
        glPopMatrix();
    }

    private interface MouseEventInvoker {
        public void invoke(Tool tool, MouseEvent event);
    }

    private MouseEventInvoker mouseClickedInvoker = (Tool tool, MouseEvent event) -> {
        if (tool instanceof MouseClickEventListener)
            ((MouseClickEventListener) tool).onMouseClicked(event);
    };

    private MouseEventInvoker mouseMovedInvoker = (Tool tool, MouseEvent event) -> {
        if (tool instanceof MouseMoveEventListener)
            ((MouseMoveEventListener) tool).onMouseMoved(event);
    };

    private MouseEventInvoker mouseEnterInvoker = (Tool tool, MouseEvent event) -> {
        if (tool instanceof MouseEnterEventListener)
            ((MouseEnterEventListener) tool).onMouseEnter(event);
    };

    private MouseEventInvoker mouseLeavedInvoker = (Tool tool, MouseEvent event) -> {
        if (tool instanceof MouseLeaveEventListener)
            ((MouseLeaveEventListener) tool).onMouseLeave(event);
    };

    private void onMouseEvent(MouseEvent event, MouseEventInvoker invoker) {
        Vector2d original = new Vector2d(event.getPos());
        event.setPos(Utility.untransform(original, getCenter(), canvasTranslate, canvasScale, 0));
        for (Entry<Tool, Boolean> e : tools.entrySet()) {
            if (e.getValue()) {
                invoker.invoke(e.getKey(), event);
            }
        }
        event.setPos(original);
    }

    /**
     * 有効化されているツールを通過させる
     */
    @Override
    public void onMouseClicked(MouseEvent event) {
        onMouseEvent(event, mouseClickedInvoker);
    }

    @Override
    public void onMouseMoved(MouseEvent event) {
        onMouseEvent(event, mouseMovedInvoker);
    }

    @Override
    public void onMouseEnter(MouseEvent event) {
        onMouseEvent(event, mouseEnterInvoker);
    }

    @Override
    public void onMouseLeave(MouseEvent event) {
        onMouseEvent(event, mouseLeavedInvoker);
    }

    @Override
    public void onScroll(MouseEvent event) {
        if (this.pressingShift) {
            onScrolled
                    .invoke(new ScrolledEvent(Direction.HORIZONTAL, event.getScroll().y, new Vector2d(event.getPos())));
        } else if (this.pressingCtrl) {
            onScrolled.invoke(new ScrolledEvent(Direction.VERTICAL, event.getScroll().y, new Vector2d(event.getPos())));
        } else {
            onScrolled.invoke(new ScrolledEvent(Direction.SCALE, event.getScroll().y, new Vector2d(event.getPos())));
        }
    }

    @Override
    public void onKeyInput(KeyInputEvent event) {
        boolean status = false;
        switch (event.action) {
            case GLFW_PRESS:
                status = true;
                break;
            case GLFW_RELEASE:
                status = false;
                break;
            default:
                status = true;
                break;
        }
        switch (event.key) {
            case GLFW_KEY_LEFT_CONTROL:
            case GLFW_KEY_RIGHT_CONTROL:
                this.pressingCtrl = status;
                break;
            case GLFW_KEY_LEFT_SHIFT:
            case GLFW_KEY_RIGHT_SHIFT:
                this.pressingShift = status;
                break;
        }
    }

    @Override
    public void onScroll(MouseEvent event, boolean captureing) {
    }

    @Override
    public void onMouseMoved(MouseEvent event, boolean captureing) {
    }

    @Override
    public void onMouseClicked(MouseEvent event, boolean captureing) {
    }

    @Override
    public void onMouseEnter(MouseEvent event, boolean captureing) {
    }

    @Override
    public void onMouseLeave(MouseEvent event, boolean captureing) {
    }
}
