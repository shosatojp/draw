package jp.shosato.micropaint.events.scroll;

import org.joml.Vector2d;

import jp.shosato.micropaint.events.Event;

public class ScrollInputEvent extends Event {
    public final Vector2d offset;

    public ScrollInputEvent(Vector2d offset) {
        this.offset = new Vector2d(offset);
    }
}
