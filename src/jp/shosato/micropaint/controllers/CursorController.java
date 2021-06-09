package jp.shosato.micropaint.controllers;

import static org.lwjgl.glfw.GLFW.glfwCreateStandardCursor;
import static org.lwjgl.glfw.GLFW.glfwSetCursor;

import org.joml.Vector2d;

import jp.shosato.micropaint.components.BasicComponent;
import jp.shosato.micropaint.events.mouse.MouseEvent;
import jp.shosato.micropaint.utils.Utility;

public class CursorController {

    public void setCursor(long _window, BasicComponent rootComponent, Vector2d pos) {
        // カーソルの設定
        MouseEvent cursorSettingEvent = new MouseEvent(pos, 0, 0, 0);
        invokeCursorSetter(rootComponent, cursorSettingEvent, pos);

        int currentCursorShape = 0;
        BasicComponent target = cursorSettingEvent.getTarget();
        if (target != null && target.getCursor() != currentCursorShape) {
            currentCursorShape = target.getCursor();
            long cursor = glfwCreateStandardCursor(currentCursorShape);
            glfwSetCursor(_window, cursor);
        }
    }

    /**
     * カーソル設定用の探索関数。カーソルが指している最前面の要素がほしいのでtargetのみ設定して回る
     */
    private void invokeCursorSetter(BasicComponent component, MouseEvent event, Vector2d relPos) {
        Vector2d untransformed = Utility.untransform(relPos, component.getCenter(), component.translate,
                component.scale, component.rotate);

        event.setTarget(component);

        for (BasicComponent child : component.getChildren()) {
            if (child.contains(untransformed)) {
                event.setPos(untransformed);
                invokeCursorSetter(child, event, untransformed);
                if (event.cancelled())
                    return;
            }
        }
    }
}
