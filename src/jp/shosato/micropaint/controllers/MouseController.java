package jp.shosato.micropaint.controllers;

import org.joml.Vector2d;

import jp.shosato.micropaint.components.BasicComponent;
import jp.shosato.micropaint.events.mouse.MouseClickEventListener;
import jp.shosato.micropaint.events.mouse.MouseEnterEventListener;
import jp.shosato.micropaint.events.mouse.MouseEvent;
import jp.shosato.micropaint.events.mouse.MouseLeaveEventListener;
import jp.shosato.micropaint.events.mouse.MouseMoveEventListener;

public class MouseController {
    public final FocusController focusController;

    public MouseController(FocusController focusController) {
        this.focusController = focusController;
    }

    /**
     * [マウスクリックイベント] 辿る子要素をクリック座標がその範囲に含まれているかによって制限
     */
    public final BasicMouseEventInvoker invokeMouseClickEvent = new BasicMouseEventInvoker(
            MouseClickEventListener.class, (BasicComponent component, MouseEvent event, boolean captureing) -> {
                if (captureing) {
                    ((MouseClickEventListener) component).onMouseClicked(event, true);
                } else {
                    ((MouseClickEventListener) component).onMouseClicked(event);
                }
            }, new BasicMouseEventInvoker.TargetRunnable() {
                @Override
                public void run(BasicComponent component) {
                    focusController.trySwitchFocus(component);
                }
            });

    /**
     * [マウス移動イベント] クリックと同じ
     */
    public final BasicMouseEventInvoker invokeMouseMoveEvent = new BasicMouseEventInvoker(MouseMoveEventListener.class,
            (BasicComponent component, MouseEvent event, boolean captureing) -> {
                if (captureing) {
                    ((MouseMoveEventListener) component).onMouseMoved(event, true);
                } else {
                    ((MouseMoveEventListener) component).onMouseMoved(event);
                }
            });

    /**
     * [マウス離脱イベント] 他のイベントと違い、イベントを発行したい要素外にポインタがあるときに発行するのですべての子要素に対して前の状態と比較する
     */
    public final BasicMouseEventInvoker invokeMouseLeaveEvent = new BasicMouseEventInvoker(
            MouseLeaveEventListener.class, (BasicComponent component, MouseEvent event, boolean captureing) -> {
                if (captureing) {
                    ((MouseLeaveEventListener) component).onMouseLeave(event, true);
                } else {
                    ((MouseLeaveEventListener) component).onMouseLeave(event);
                }
            }, new BasicMouseEventInvoker.Condition() {
                @Override
                public boolean run(BasicComponent component, Vector2d relPos) {
                    return !component.contains(relPos) && component.getHovered();
                }
            }, false, new BasicMouseEventInvoker.Modifier() {
                @Override
                public void run(BasicComponent component) {
                    component.setHovered(false);
                }
            });

    public final BasicMouseEventInvoker invokeMouseEnterEvent = new BasicMouseEventInvoker(
            MouseEnterEventListener.class, (BasicComponent component, MouseEvent event, boolean captureing) -> {
                if (captureing) {
                    ((MouseEnterEventListener) component).onMouseEnter(event, true);
                } else {
                    ((MouseEnterEventListener) component).onMouseEnter(event);
                }
            }, new BasicMouseEventInvoker.Condition() {
                @Override
                public boolean run(BasicComponent component, Vector2d relPos) {
                    return component.contains(relPos) && !component.getHovered();
                }
            }, true, new BasicMouseEventInvoker.Modifier() {
                @Override
                public void run(BasicComponent component) {
                    component.setHovered(true);
                }
            });

}
