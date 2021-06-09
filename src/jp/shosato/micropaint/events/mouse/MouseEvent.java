package jp.shosato.micropaint.events.mouse;

import org.joml.Vector2d;

import jp.shosato.micropaint.events.Event;

public class MouseEvent extends Event {
    private Vector2d pos;
    private int action;
    private int mods;
    private int button;
    private Vector2d scroll;

    public MouseEvent() {
    }

    public MouseEvent(Vector2d pos) {
        this(pos, 0, 0, 0, new Vector2d());
    }

    public MouseEvent(Vector2d pos, Vector2d scroll) {
        this(pos, 0, 0, 0, scroll);
    }

    public MouseEvent(Vector2d pos, int button, int action, int mods) {
        this(pos, button, action, mods, new Vector2d());
    }

    public MouseEvent(Vector2d pos, int button, int action, int mods, Vector2d scroll) {
        this.pos = new Vector2d(pos);
        this.action = action;
        this.mods = mods;
        this.button = button;
        this.scroll = new Vector2d(scroll);
    }

    public Vector2d getPos() {
        return pos;
    }

    public void setPos(Vector2d pos) {
        this.pos = pos;
    }

    public int getAction() {
        return action;
    }

    public int getButton() {
        return button;
    }

    public Vector2d getScroll() {
        return scroll;
    }
}
