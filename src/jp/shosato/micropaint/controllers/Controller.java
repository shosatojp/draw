package jp.shosato.micropaint.controllers;

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
import java.util.Vector;

/**
 * イベントはルート要素から子要素をたどっていく途中でキャプチャリングフェーズのイベントを発行。帰りにはバブリングフェーズを発行。
 */
public class Controller {
    private Vector2d pos = new Vector2d();

    private BasicComponent rootComponent;

    private FocusController focusController = new FocusController();
    private MouseController mouseController = new MouseController(focusController);
    private KeyController keyController = new KeyController(focusController);
    private CursorController cursorController = new CursorController();

    public void setRootComponent(BasicComponent root) {
        this.rootComponent = root;
    }

    public Controller(long _window) {
        glfwSetMouseButtonCallback(_window, new GLFWMouseButtonCallback() {
            @Override
            public void invoke(long window, int button, int action, int mods) {
                MouseEvent event = new MouseEvent(pos, button, action, mods);
                if (rootComponent != null) {
                    mouseController.invokeMouseClickEvent.invoke(rootComponent, event, pos);
                }
            }
        });

        glfwSetCursorPosCallback(_window, new GLFWCursorPosCallback() {
            @Override
            public void invoke(long window, double x, double y) {

                // OpenGLのイベントはステートレスだが、マウスクリック時に座標がわからないのは不便なので保存しておく
                pos = new Vector2d(x, y);

                if (rootComponent != null) {
                    mouseController.invokeMouseMoveEvent.invoke(rootComponent, new MouseEvent(pos, 0, 0, 0), pos);
                    mouseController.invokeMouseLeaveEvent.invoke(rootComponent, new MouseEvent(pos, 0, 0, 0), pos);
                    mouseController.invokeMouseEnterEvent.invoke(rootComponent, new MouseEvent(pos, 0, 0, 0), pos);
                    cursorController.setCursor(window, rootComponent, pos);
                }
            }
        });

        glfwSetKeyCallback(_window, (long window, int key, int scancode, int action, int mods) -> {
            if (focusController.getFocused() != null) {
                KeyInputEvent event = new KeyInputEvent(window, key, scancode, action, mods);
                keyController.invokeKeyInputEvent(focusController.getFocused(), event);
            }
        });

        glfwSetCharCallback(_window, new GLFWCharCallbackI() {
            @Override
            public void invoke(long window, int codepoint) {
                if (focusController.getFocused() != null) {
                    CharInputEvent event = new CharInputEvent(window, codepoint);
                    keyController.invokeCharInputEvent(focusController.getFocused(), event);
                }
            }
        });
    }
}
