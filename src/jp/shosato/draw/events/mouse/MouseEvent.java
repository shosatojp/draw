package jp.shosato.draw.events.mouse;

import org.joml.Vector2d;

import jp.shosato.draw.events.Event;

public class MouseEvent extends Event {
    private Vector2d pos;
    private int action;
    private int mods;
    private int button;

    public MouseEvent() {

    }

    public MouseEvent(Vector2d pos, int button, int action, int mods) {
        this.pos = new Vector2d(pos);
        this.action = action;
        this.mods = mods;
        this.button = button;
    }

    public Vector2d getPos() {
        return pos;
    }

    public int getAction() {
        return action;
    }

    public int getButton() {
        return button;
    }
}
