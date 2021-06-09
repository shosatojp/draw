package jp.shosato.draw.components;

import jp.shosato.draw.events.EventHandler;
import jp.shosato.draw.events.focus.FocusInEvent;
import jp.shosato.draw.events.focus.FocusInEventListener;
import jp.shosato.draw.events.focus.FocusOutEvent;
import jp.shosato.draw.events.focus.FocusOutEventListener;
import jp.shosato.draw.events.handlers.TextInputEvent;
import jp.shosato.draw.events.handlers.TextInputEventHandler;
import jp.shosato.draw.events.key.CharInputEvent;
import jp.shosato.draw.events.key.CharInputEventListener;
import jp.shosato.draw.events.key.KeyInputEvent;
import jp.shosato.draw.events.key.KeyInputEventListener;
import jp.shosato.draw.events.mouse.MouseClickEventListener;
import jp.shosato.draw.events.mouse.MouseEvent;
import jp.shosato.draw.utils.Utility;

import static org.lwjgl.opengl.GL15.*;

import static org.lwjgl.glfw.GLFW.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.text.Position;
import java.awt.FontMetrics;

import org.joml.Vector2d;
import org.joml.Vector4d;

public class InputComponent extends LabelComponent implements CharInputEventListener, FocusInEventListener,
        MouseClickEventListener, KeyInputEventListener, FocusOutEventListener {

    /**
     * テキスト変更イベントハンドラ
     */
    public EventHandler<TextInputEventHandler> onTextChanged = new EventHandler<>();
    /**
     * テキストインプットイベントハンドラ
     */
    public EventHandler<TextInputEventHandler> onTextInput = new EventHandler<>();

    private int age = 0;
    /**
     * カーソル位置
     */
    private int cursorPosition = 0;
    private boolean hasFocus = false;

    public InputComponent(int w, int h) {
        super(w, h);
        this.setText("");
    }

    public InputComponent(Vector2d topLeft, double w, double h, Vector4d color) {
        super(topLeft, w, h, color);
        this.setText("");
    }

    @Override
    public void draw() {
        super.draw();

        // cursor
        if (hasFocus && age % 30 == 0) {

            String text = getText();

            if (text == null)
                return;

            glPushMatrix();
            Utility.glTransform(dimension, translate, scale, rotate);
            {
                /* テキストを描画 */
                drawText();

                /* カーソルを描画 */
                drawCursor(getMetrix(), text);
            }
            glPopMatrix();
        }
    }

    protected void drawCursor(FontMetrics metrics, String text) {
        glPushMatrix();

        /* 文字の左端からカーソルまでの距離 */
        int cursorW = metrics.stringWidth(text.substring(0, this.cursorPosition));
        /* テキストの幅 */
        int w = metrics.stringWidth(text);
        /* テキストの高さ */
        int h = metrics.getHeight();

        /* テキストの左上座標 */
        Vector2d textTopLeft = getTextTopLeft(w, h);
        glTranslated(textTopLeft.x, textTopLeft.y, 0);

        /* カーソルの描画 */
        glLineWidth(2);
        glColor4d(0, 0, 0, 1);
        glBegin(GL_LINES);
        glVertex2d(cursorW, 0);
        glVertex2d(cursorW, h);
        glEnd();

        glPopMatrix();
    }

    /**
     * 文字の入力
     * BSやDelete時は呼ばれない
     */
    @Override
    public void onCharInput(CharInputEvent event) {
        int code = event.codepoint;
        char c = (char) code;

        String text = this.getText();
        if (text == null) {
            text = "";
        }
        List<Character> chars = text.chars().mapToObj(e -> (char) e).collect(Collectors.toList());

        if (33 <= code && code <= 126) {
            chars.add(cursorPosition++, c);
        } else {
            return;
        }
        StringBuilder sb = new StringBuilder();
        for (Character character : chars) {
            sb.append(character);
        }

        this.setText(sb.toString());
        onTextChanged.invoke(new TextInputEvent(sb.toString()));
    }

    /**
     * キーの入力
     */
    @Override
    public void onKeyInput(KeyInputEvent event) {
        if (event.action == GLFW_PRESS) {
            String text = this.getText();
            if (text == null) {
                text = "";
            }
            List<Character> chars = text.chars().mapToObj(e -> (char) e).collect(Collectors.toList());

            switch (event.key) {
                case GLFW_KEY_BACKSPACE:
                    if (cursorPosition > 0 && chars.size() >= cursorPosition)
                        chars.remove(--cursorPosition);
                    break;
                case GLFW_KEY_DELETE:
                    if (chars.size() > cursorPosition && chars.size() > 0)
                        chars.remove(cursorPosition);
                    break;
                case GLFW_KEY_RIGHT:
                    if (chars.size() > cursorPosition)
                        this.cursorPosition++;
                    break;
                case GLFW_KEY_LEFT:
                    if (cursorPosition > 0)
                        this.cursorPosition--;
                    break;
                case GLFW_KEY_HOME:
                    this.cursorPosition = 0;
                    return;
                case GLFW_KEY_END:
                    this.cursorPosition = chars.size();
                    return;
                case GLFW_KEY_ENTER:
                    /* Enterでテキストインプットイベント発行 */
                    this.onTextInput.invoke(new TextInputEvent(text));
                    return;
                default:
                    return;
            }
            StringBuilder sb = new StringBuilder();
            for (Character character : chars) {
                sb.append(character);
            }

            this.setText(sb.toString());
            onTextChanged.invoke(new TextInputEvent(sb.toString()));
        }
    }

    @Override
    public void onFocusIn(FocusInEvent event) {
        hasFocus = true;
    }

    @Override
    public void onFocusOut(FocusOutEvent event) {
        hasFocus = false;
    }

    @Override
    public void onMouseClicked(MouseEvent event) {
    }

    @Override
    public void onMouseClicked(MouseEvent event, boolean captureing) {
    }

}
