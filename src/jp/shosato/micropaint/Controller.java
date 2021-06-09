package jp.shosato.micropaint;

import org.joml.Vector2d;
import org.lwjgl.glfw.GLFWCharCallbackI;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWKeyCallbackI;
import org.lwjgl.glfw.GLFWMouseButtonCallback;

import jp.shosato.micropaint.utils.Utility;
import jp.shosato.micropaint.components.BasicComponent;
import jp.shosato.micropaint.events.focus.FocusInEvent;
import jp.shosato.micropaint.events.focus.FocusInEventListener;
import jp.shosato.micropaint.events.focus.FocusOutEvent;
import jp.shosato.micropaint.events.focus.FocusOutEventListener;
import jp.shosato.micropaint.events.key.CharInputEvent;
import jp.shosato.micropaint.events.key.CharInputEventListener;
import jp.shosato.micropaint.events.key.KeyInputEvent;
import jp.shosato.micropaint.events.key.KeyInputEventListener;
import jp.shosato.micropaint.events.mouse.MouseClickEventListener;
import jp.shosato.micropaint.events.mouse.MouseEnterEventListener;
import jp.shosato.micropaint.events.mouse.MouseEvent;
import jp.shosato.micropaint.events.mouse.MouseLeaveEventListener;
import jp.shosato.micropaint.events.mouse.MouseMoveEventListener;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL15.*;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.util.Iterator;
import java.util.Optional;

/**
 * イベントはルート要素から子要素をたどっていく途中でキャプチャリングフェーズのイベントを発行。帰りにはバブリングフェーズを発行。
 */
public class Controller {
    private Vector2d pos = new Vector2d();
    private BasicComponent rootComponent;
    private BasicComponent focused;

    public void setRootComponent(BasicComponent root) {
        this.rootComponent = root;
    }

    public Controller(long _window) {
        glfwSetMouseButtonCallback(_window, new GLFWMouseButtonCallback() {
            @Override
            public void invoke(long window, int button, int action, int mods) {
                MouseEvent event = new MouseEvent(pos, button, action, mods);
                if (rootComponent != null) {
                    invokeMouseClickIn(rootComponent, event, pos);
                }
            }

            private void trySwitchFocus(BasicComponent newcomponent) {
                // フォーカスが変わったとき
                if (focused != newcomponent) {
                    // フォーカスアウト
                    if (focused != null) {
                        FocusOutEvent outEvent = new FocusOutEvent();
                        outEvent.setTarget(focused);
                        invokeFocusedOut(focused, outEvent);
                    }

                    // フォーカスイン
                    FocusInEvent inEvent = new FocusInEvent();
                    inEvent.setTarget(newcomponent);
                    invokeFocusedIn(newcomponent, inEvent);

                    // 現在フォーカスしている要素を変更
                    focused = newcomponent;
                }
            }

            private void invokeFocusedIn(BasicComponent component, FocusInEvent event) {
                if (component instanceof FocusInEventListener) {
                    ((FocusInEventListener) component).onFocusIn(event);
                }

                if (!event.cancelled()) {
                    BasicComponent parent = component.getParent();
                    if (parent != null) {
                        event.setCurrentTarget(parent);
                        invokeFocusedIn(parent, event);
                    }
                }
            }

            private void invokeFocusedOut(BasicComponent component, FocusOutEvent event) {
                if (component instanceof FocusOutEventListener) {
                    ((FocusOutEventListener) component).onFocusOut(event);
                }

                if (!event.cancelled()) {
                    BasicComponent parent = component.getParent();
                    if (parent != null) {
                        event.setCurrentTarget(parent);
                        invokeFocusedOut(parent, event);
                    }
                }
            }

            /**
             * [マウスクリックイベント] 辿る子要素をクリック座標がその範囲に含まれているかによって制限
             */
            private void invokeMouseClickIn(BasicComponent component, MouseEvent event, Vector2d relPos) {
                // captureing phase
                if (component instanceof MouseClickEventListener && !event.cancelled()) {
                    event.setCurrentTarget(component);
                    event.setTarget(component);
                    event.setPos(relPos);
                    ((MouseClickEventListener) component).onMouseClicked(event, true);
                }

                Vector2d untransformed = Utility.untransform(relPos, component.getCenter(), component.translate,
                        component.scale, component.rotate);
                for (Iterator<BasicComponent> iter = component.getChildren().descendingIterator(); iter.hasNext();) {
                    BasicComponent child = iter.next();
                    // すべての要素に対して
                    if (child.contains(untransformed)) {
                        event.setPos(untransformed);
                        invokeMouseClickIn(child, event, untransformed);
                        if (event.cancelled())
                            return;
                    }
                }

                // bubbling phase
                if (event.getTarget() != null && component instanceof MouseClickEventListener && !event.cancelled()) {
                    event.setCurrentTarget(component);
                    event.setPos(relPos);
                    ((MouseClickEventListener) component).onMouseClicked(event);
                }

                // マウスクリック時に最深部の要素をフォーカス
                if (event.getTarget() == component) {
                    trySwitchFocus(component);
                }
            }
        });

        glfwSetCursorPosCallback(_window, new GLFWCursorPosCallback() {
            @Override
            public void invoke(long window, double x, double y) {

                // OpenGLのイベントはステートレスだが、マウスクリック時に座標がわからないのは不便なので保存しておく
                pos = new Vector2d(x, y);

                if (rootComponent != null) {
                    invokeMouseMoveIn(rootComponent, new MouseEvent(pos, 0, 0, 0), pos);
                    invokeMouseLeaveIn(rootComponent, new MouseEvent(pos, 0, 0, 0), pos);
                    invokeMouseEnterIn(rootComponent, new MouseEvent(pos, 0, 0, 0), pos);

                    {
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

            /**
             * [マウス移動イベント] クリックと同じ
             */
            private void invokeMouseMoveIn(BasicComponent component, MouseEvent event, Vector2d relPos) {
                Vector2d originalPos = new Vector2d(relPos);
                Vector2d untransformed = Utility.untransform(relPos, component.getCenter(), component.translate,
                        component.scale, component.rotate);

                boolean cond = component instanceof MouseMoveEventListener;

                // captureing phase
                if (cond) {
                    event.setCurrentTarget(component);
                    event.setTarget(component);
                    event.setPos(originalPos);
                    MouseMoveEventListener listener = (MouseMoveEventListener) component;
                    listener.onMouseMoved(event, true);
                    if (event.cancelled())
                        return;
                }

                for (Iterator<BasicComponent> iter = component.getChildren().descendingIterator(); iter.hasNext();) {
                    BasicComponent child = iter.next();
                    if (child.contains(untransformed)) {
                        event.setPos(untransformed);
                        invokeMouseMoveIn(child, event, untransformed);
                        if (event.cancelled())
                            return;
                    }
                }

                if (event.cancelled())
                    return;

                // bubbling phase
                if (event.getTarget() != null) {
                    // if (cond) {
                    if (component instanceof MouseMoveEventListener) {

                        event.setCurrentTarget(component);
                        event.setPos(originalPos);
                        MouseMoveEventListener listener = (MouseMoveEventListener) component;
                        listener.onMouseMoved(event);
                    }
                }
            }

            /**
             * [マウス離脱イベント] 他のイベントと違い、イベントを発行したい要素外にポインタがあるときに発行するのですべての子要素に対して前の状態と比較する
             */
            private void invokeMouseLeaveIn(BasicComponent component, MouseEvent event, Vector2d relPos) {
                Vector2d originalPos = new Vector2d(relPos);
                Vector2d untransformed = Utility.untransform(relPos, component.getCenter(), component.translate,
                        component.scale, component.rotate);
                boolean cond = !component.contains(originalPos) && component.getHovered();
                // captureing phase
                if (cond) {
                    if (component instanceof MouseLeaveEventListener) {
                        component.setHovered(false);
                        event.setCurrentTarget(component);
                        event.setTarget(component);
                        event.setPos(originalPos);
                        // TODO: 別のイベントにすべき？
                        MouseLeaveEventListener listener = (MouseLeaveEventListener) component;
                        listener.onMouseLeave(event, true);
                        if (event.cancelled())
                            return;
                    }
                }

                for (Iterator<BasicComponent> iter = component.getChildren().descendingIterator(); iter.hasNext();) {
                    BasicComponent child = iter.next();
                    // すべての要素に対して
                    event.setPos(untransformed);
                    invokeMouseLeaveIn(child, event, untransformed);
                    if (event.cancelled())
                        return;
                }

                if (event.cancelled())
                    return;

                // bubbling phase
                if (event.getTarget() != null) {
                    // if (cond) {
                    if (component instanceof MouseLeaveEventListener) {
                        component.setHovered(false);
                        event.setCurrentTarget(component);
                        event.setTarget(component);
                        event.setPos(originalPos);
                        MouseLeaveEventListener listener = (MouseLeaveEventListener) component;
                        listener.onMouseLeave(event);
                    }
                }
            }

            private void invokeMouseEnterIn(BasicComponent component, MouseEvent event, Vector2d relPos) {
                Vector2d originalPos = new Vector2d(relPos);
                Vector2d untransformed = Utility.untransform(relPos, component.getCenter(), component.translate,
                        component.scale, component.rotate);
                boolean cond = component.contains(originalPos) && !component.getHovered();

                // captureing phase
                if (cond) {
                    if (component instanceof MouseEnterEventListener) {
                        component.setHovered(true);
                        event.setCurrentTarget(component);
                        event.setTarget(component);
                        event.setPos(originalPos);
                        MouseEnterEventListener listener = (MouseEnterEventListener) component;
                        listener.onMouseEnter(event, true);
                        if (event.cancelled())
                            return;
                    }
                }

                for (Iterator<BasicComponent> iter = component.getChildren().descendingIterator(); iter.hasNext();) {
                    BasicComponent child = iter.next();
                    // すべての要素に対して
                    if (child.contains(untransformed)) {
                        event.setPos(untransformed);
                        invokeMouseEnterIn(child, event, untransformed);
                        if (event.cancelled())
                            return;
                    }
                }

                if (event.cancelled())
                    return;

                // bubbling phase
                if (event.getTarget() != null) {
                    // if (cond) {
                    if (component instanceof MouseEnterEventListener) {
                        component.setHovered(true);
                        event.setCurrentTarget(component);
                        event.setPos(originalPos);
                        MouseEnterEventListener listener = (MouseEnterEventListener) component;
                        listener.onMouseEnter(event);
                    }
                }
            }
        });

        glfwSetKeyCallback(_window, new GLFWKeyCallbackI() {

            @Override
            public void invoke(long window, int key, int scancode, int action, int mods) {
                if (focused != null) {
                    KeyInputEvent event = new KeyInputEvent(window, key, scancode, action, mods);
                    invokeKeyInputEvent(focused, event);
                }
            }

            private void invokeKeyInputEvent(BasicComponent component, KeyInputEvent event) {
                if (component instanceof KeyInputEventListener) {
                    ((KeyInputEventListener) component).onKeyInput(event);
                }

                if (!event.cancelled()) {
                    BasicComponent parent = component.getParent();
                    if (parent != null) {
                        event.setCurrentTarget(parent);
                        invokeKeyInputEvent(parent, event);
                    }
                }
            }
        });

        glfwSetCharCallback(_window, new GLFWCharCallbackI() {
            @Override
            public void invoke(long window, int codepoint) {
                if (focused != null) {
                    CharInputEvent event = new CharInputEvent(window, codepoint);
                    invokeCharInputEvent(focused, event);
                }
            }

            private void invokeCharInputEvent(BasicComponent component, CharInputEvent event) {
                if (component instanceof CharInputEventListener) {
                    ((CharInputEventListener) component).onCharInput(event);
                }

                if (!event.cancelled()) {
                    BasicComponent parent = component.getParent();
                    if (parent != null) {
                        event.setCurrentTarget(parent);
                        invokeCharInputEvent(parent, event);
                    }
                }
            }
        });

    }
}
