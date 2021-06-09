package jp.shosato.micropaint.controllers;

import static org.lwjgl.glfw.GLFW.glfwSetCharCallback;
import static org.lwjgl.glfw.GLFW.glfwSetCursorPosCallback;
import static org.lwjgl.glfw.GLFW.glfwSetKeyCallback;
import static org.lwjgl.glfw.GLFW.glfwSetMouseButtonCallback;

import org.joml.Vector2d;
import org.lwjgl.glfw.GLFWCharCallbackI;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;

import jp.shosato.micropaint.components.BasicComponent;
import jp.shosato.micropaint.events.key.CharInputEvent;
import jp.shosato.micropaint.events.key.KeyInputEvent;
import jp.shosato.micropaint.events.mouse.MouseEvent;

/**
 * イベントはルート要素から子要素をたどっていく途中でキャプチャリングフェーズのイベントを発行。帰りにはバブリングフェーズを発行。
 */
public class Controller {
    private Vector2d pos = new Vector2d();

    private BasicComponent rootComponent;

    private FocusController focusController = new FocusController();
    private MouseController mouseController = new MouseController(focusController);
    private KeyController keyController = new KeyController();
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
                    mouseController.mouseClickEventInvoker.invoke(rootComponent, event, pos);
                }
            }
        });

        glfwSetCursorPosCallback(_window, new GLFWCursorPosCallback() {
            @Override
            public void invoke(long window, double x, double y) {

                // OpenGLのイベントはステートレスだが、マウスクリック時に座標がわからないのは不便なので保存しておく
                pos = new Vector2d(x, y);

                if (rootComponent != null) {
                    mouseController.mouseMoveEventInvoker.invoke(rootComponent, new MouseEvent(pos, 0, 0, 0), pos);
                    mouseController.mouseLeaveEventInvoker.invoke(rootComponent, new MouseEvent(pos, 0, 0, 0), pos);
                    mouseController.mouseEnterEventInvoker.invoke(rootComponent, new MouseEvent(pos, 0, 0, 0), pos);
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
