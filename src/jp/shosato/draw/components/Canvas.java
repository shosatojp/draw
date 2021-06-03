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

import static org.lwjgl.opengl.GL15.*;

/**
 * 描画する図形の親要素。ツールの有効・無効を管理する
 */
public class Canvas extends RectangleComponent
        implements MouseMoveEventListener, MouseClickEventListener, MouseEnterEventListener, MouseLeaveEventListener {
    // Tools enable/disable map
    private HashMap<Tool, Boolean> tools = new HashMap<Tool, Boolean>();

    public Canvas(double w, double h) {
        this(new Vector2d(0, 0), new Vector2d(w, h), new Vector4d(Colors.GRAY));
    }

    public Canvas(double w, double h, Vector4d color) {
        this(new Vector2d(0, 0), new Vector2d(w, h), color);
    }

    public Canvas(Vector2d topLeft, double w, double h, Vector4d color) {
        this(topLeft, new Vector2d(topLeft.x + w, topLeft.y + h), color);
    }

    public Canvas(Vector2d topLeft, Vector2d bottomRight, Vector4d color) {
        super(topLeft, bottomRight, color);
    }

    public void setTools(HashMap<Tool, Boolean> tools) {
        this.tools = tools;
    }

    @Override
    public void draw() {
        glEnable(GL_SCISSOR_TEST);
        glScissor((int) this.topLeft.x, (int) this.topLeft.y, (int) (this.bottomRight.x - this.topLeft.x),
                (int) (this.bottomRight.y - this.topLeft.y));
        glPushMatrix();

        // glTranslated(-this.topLeft.x, -this.topLeft.y, 0);
        // glScaled(0.5, 0.5, 1);

        // 図形を描画
        super.draw();

        // ツールを描画
        for (Entry<Tool, Boolean> e : tools.entrySet()) {
            if (e.getValue()) {
                e.getKey().draw();
            }
        }

        glPopMatrix();
        glDisable(GL_SCISSOR_TEST);
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
