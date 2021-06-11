package jp.shosato.micropaint.components;

import java.util.ArrayList;

import static org.lwjgl.opengl.GL15.*;
import org.joml.Vector2d;
import org.joml.Vector4d;

import jp.shosato.micropaint.events.Event;
import jp.shosato.micropaint.events.EventHandler;
import jp.shosato.micropaint.events.EventListener;
import jp.shosato.micropaint.events.focus.FocusInEvent;
import jp.shosato.micropaint.events.focus.FocusInEventListener;
import jp.shosato.micropaint.events.handlers.ButtonClickedEvent;
import jp.shosato.micropaint.events.handlers.ButtonClickedEventHandler;
import jp.shosato.micropaint.events.key.KeyInputEvent;
import jp.shosato.micropaint.events.key.KeyInputEventListener;
import jp.shosato.micropaint.events.mouse.MouseClickEventListener;
import jp.shosato.micropaint.events.mouse.MouseEnterEventListener;
import jp.shosato.micropaint.events.mouse.MouseEvent;
import jp.shosato.micropaint.events.mouse.MouseLeaveEventListener;
import jp.shosato.micropaint.events.mouse.MouseMoveEventListener;
import jp.shosato.micropaint.utils.Utility;

import static org.lwjgl.glfw.GLFW.*;

/**
 * LabelComponentにホバー・クリック時の動作を追加
 */
public class ButtonComponent extends LabelComponent implements MouseClickEventListener, MouseMoveEventListener,
        MouseEnterEventListener, MouseLeaveEventListener {

    /**
     * クリック状態
     */
    private boolean pressed = false;

    /**
     * ボタンクリックイベントハンドラ
     */
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
        /**
         * ホバーやクリックの状況で色を変更
         */
        if (this.pressed) {
            this.color = buttonPressedColor;
        } else if (this.getHovered()) {
            this.color = buttonHoveredColor;
        } else {
            this.color = buttonDefaultColor;
        }

        /* 座標換 */
        glPushMatrix();
        Utility.glTransform(dimension, translate, scale, rotate);
        {
            /* 縁を描画 */
            glColor4d(color.x, color.y, color.z, 1f);
            Utility.drawRectangleFill(dimension);

            /* ボタンを描画 */
            final double margin = 2;
            glPushMatrix();
            glTranslated(margin / 2, margin / 2, 0);
            Utility.drawRectangleFill(new Vector2d(dimension).sub(margin, margin));
            glPopMatrix();

            /* テキストを描画 */
            drawText();
        }
        glPopMatrix();
    }

    @Override
    public void onMouseClicked(MouseEvent event) {
        /* ボタンはクリックイベントをバブルアップさせない */
        event.cancel();

        switch (event.getButton()) {
            case GLFW_MOUSE_BUTTON_LEFT:
                switch (event.getAction()) {
                    case GLFW_PRESS:
                        this.pressed = true;
                        break;
                    case GLFW_RELEASE:
                        if (this.pressed) {
                            /* ボタンクリック時にイベントを発行 */
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
}
