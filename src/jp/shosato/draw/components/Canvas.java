package jp.shosato.draw.components;

import java.lang.reflect.Method;
import java.util.EventListener;
import java.util.HashMap;
import java.util.Map.Entry;

import org.joml.Vector2d;
import org.joml.Vector4d;

import jp.shosato.draw.events.mouse.MouseClickEventListener;
import jp.shosato.draw.events.mouse.MouseEnterEventListener;
import jp.shosato.draw.events.mouse.MouseEvent;
import jp.shosato.draw.events.mouse.MouseEventListener;
import jp.shosato.draw.events.mouse.MouseLeaveEventListener;
import jp.shosato.draw.events.mouse.MouseMoveEventListener;
import jp.shosato.draw.tools.MoveTool;
import jp.shosato.draw.tools.Tool;
import jp.shosato.draw.utils.Colors;
import jp.shosato.draw.utils.Utility;

import static org.lwjgl.opengl.GL15.*;

/**
 * 描画する図形の親要素。ツールの有効・無効を管理する
 */
public class Canvas extends RectangleComponent
        implements MouseMoveEventListener, MouseClickEventListener, MouseEnterEventListener, MouseLeaveEventListener {
    /* Tools enable/disable map */
    private HashMap<Tool, Boolean> tools = new HashMap<Tool, Boolean>();

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
            glEnable(GL_SCISSOR_TEST);
            Vector2d viewport = getViewportCoords();
            glScissor((int) viewport.x, (int) viewport.y, (int) dimension.x, (int) dimension.y);

            glColor4d(color.x, color.y, color.z, color.w);
            Utility.drawRectangleFill(dimension);

            /* 図形を描画 */
            for (BasicComponent child : children) {
                child.scale = new Vector2d(2, 2);
                child.draw();
            }

            /* ツールを描画 */
            glPushMatrix();
            Utility.glTransform(dimension, translate, new Vector2d(2, 2), rotate);
            for (Entry<Tool, Boolean> e : tools.entrySet()) {
                if (e.getValue()) {
                    e.getKey().draw();
                }
            }

            glPopMatrix();

            glDisable(GL_SCISSOR_TEST);
        }
        glPopMatrix();
    }

    /**
     * 有効化されているツールを通過させる
     */
    @Override
    public void onMouseClicked(MouseEvent event) {
        for (Entry<Tool, Boolean> e : tools.entrySet()) {
            if (e.getValue() && e.getKey() instanceof MouseClickEventListener) {
                ((MouseClickEventListener) e.getKey()).onMouseClicked(event);
            }
        }
    }

    @Override
    public void onMouseMoved(MouseEvent event) {
        for (Entry<Tool, Boolean> e : tools.entrySet()) {
            if (e.getValue() && e.getKey() instanceof MouseMoveEventListener) {
                ((MouseMoveEventListener) e.getKey()).onMouseMoved(event);
            }
        }
    }

    @Override
    public void onMouseEnter(MouseEvent event) {
        for (Entry<Tool, Boolean> e : tools.entrySet()) {
            if (e.getValue() && e.getKey() instanceof MouseEnterEventListener) {
                ((MouseEnterEventListener) e.getKey()).onMouseEnter(event);
            }
        }
    }

    @Override
    public void onMouseLeave(MouseEvent event) {
        for (Entry<Tool, Boolean> e : tools.entrySet()) {
            if (e.getValue() && e.getKey() instanceof MouseLeaveEventListener) {
                ((MouseLeaveEventListener) e.getKey()).onMouseLeave(event);
            }
        }
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
