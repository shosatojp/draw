package jp.shosato.draw.components;

import java.util.ArrayList;

import static org.lwjgl.opengl.GL15.*;
import org.joml.Vector2d;
import org.joml.Vector4d;

import jp.shosato.draw.events.Event;
import jp.shosato.draw.events.EventHandler;
import jp.shosato.draw.events.EventListener;
import jp.shosato.draw.events.focus.FocusInEvent;
import jp.shosato.draw.events.focus.FocusInEventListener;
import jp.shosato.draw.events.handlers.ButtonClickedEvent;
import jp.shosato.draw.events.handlers.ButtonClickedEventHandler;
import jp.shosato.draw.events.key.KeyInputEvent;
import jp.shosato.draw.events.key.KeyInputEventListener;
import jp.shosato.draw.events.mouse.MouseClickEventListener;
import jp.shosato.draw.events.mouse.MouseEnterEventListener;
import jp.shosato.draw.events.mouse.MouseEvent;
import jp.shosato.draw.events.mouse.MouseLeaveEventListener;
import jp.shosato.draw.events.mouse.MouseMoveEventListener;
import jp.shosato.draw.utils.Utility;

import static org.lwjgl.glfw.GLFW.*;

public class ButtonComponent extends LabelComponent implements MouseClickEventListener, MouseMoveEventListener,
        MouseEnterEventListener, MouseLeaveEventListener, FocusInEventListener, KeyInputEventListener {

    private boolean pressed = false;

    public EventHandler<ButtonClickedEventHandler> onButtonClicked = new EventHandler<>();

    private static Vector4d buttonDefaultColor = new Vector4d(0.8f, 0.8f, 0.8f, 1f);
    private static Vector4d buttonHoveredColor = new Vector4d(0.7f, 0.2f, 0.7f, 1f);
    private static Vector4d buttonPressedColor = new Vector4d(0.6f, 0.6f, 0.6f, 1f);

    public ButtonComponent(double w, double h, String text) {
        this(new Vector2d(0, 0), w, h, text);
    }

    public ButtonComponent(Vector2d translate, double w, double h, String text) {
        super(translate, w, h, buttonHoveredColor, text);
        this.cursor = GLFW_HAND_CURSOR;
    }

    @Override
    public void draw() {

        if (this.pressed) {
            this.color = buttonPressedColor;
        } else if (this.getHovered()) {
            this.color = buttonHoveredColor;
        } else {
            this.color = buttonDefaultColor;
        }

        glPushMatrix();
        Utility.glTransform(dimension, translate, scale, rotate);

        glColor4d(color.x, color.y, color.z, 1f);
        Utility.drawRectangleFill(dimension);
        double margin = 2;

        glPushMatrix();
        glTranslated(margin / 2, margin / 2, 0);
        Utility.drawRectangleFill(new Vector2d(dimension).sub(margin, margin));
        glPopMatrix();

        drawText();
        glPopMatrix();
    }

    @Override
    public void onMouseClicked(MouseEvent event) {
        System.out.println(event);
        event.cancel();
        switch (event.getButton()) {
            case GLFW_MOUSE_BUTTON_LEFT:
                switch (event.getAction()) {
                    case GLFW_PRESS:
                        this.pressed = true;
                        break;
                    case GLFW_RELEASE:
                        if (this.pressed) {
                            onButtonClicked.invoke(new ButtonClickedEvent());
                        }
                        this.pressed = false;
                        break;
                    default:
                        break;
                }
                break;
        }
    }

    @Override
    public void onMouseClicked(MouseEvent event, boolean captureing) {
    }

    @Override
    public void onMouseMoved(MouseEvent event) {
        event.cancel();
    }

    @Override
    public void onMouseMoved(MouseEvent event, boolean captureing) {
    }

    @Override
    public void onMouseEnter(MouseEvent event) {
        event.cancel();
    }

    @Override
    public void onMouseEnter(MouseEvent event, boolean captureing) {
    }

    @Override
    public void onMouseLeave(MouseEvent event) {
        event.cancel();
    }

    @Override
    public void onMouseLeave(MouseEvent event, boolean captureing) {
    }

    @Override
    public void onFocusIn(FocusInEvent event) {
    }

    @Override
    public void onKeyInput(KeyInputEvent event) {
        if (event.action == GLFW_PRESS) {
            String text = this.getText().concat(String.valueOf((char) event.key));
            this.setText(text);
        }
    }
}
