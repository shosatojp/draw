package jp.shosato.micropaint.controllers;

import org.lwjgl.glfw.GLFWKeyCallbackI;

import jp.shosato.micropaint.components.BasicComponent;
import jp.shosato.micropaint.events.key.CharInputEvent;
import jp.shosato.micropaint.events.key.CharInputEventListener;
import jp.shosato.micropaint.events.key.KeyInputEvent;
import jp.shosato.micropaint.events.key.KeyInputEventListener;

public class KeyController {
    private final FocusController focusController;

    public KeyController(FocusController focusController) {
        this.focusController = focusController;
    }

    public void invokeKeyInputEvent(BasicComponent component, KeyInputEvent event) {
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

    public void invokeCharInputEvent(BasicComponent component, CharInputEvent event) {
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
}
