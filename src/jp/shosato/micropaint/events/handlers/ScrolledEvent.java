package jp.shosato.micropaint.events.handlers;

import org.joml.Vector2d;

import jp.shosato.micropaint.events.Event;

public class ScrolledEvent extends Event {
    public enum Direction {
        VERTICAL, HORIZONTAL, SCALE
    }

    public final Direction direction;
    public final double offset;
    public final Vector2d pos;

    public ScrolledEvent(Direction direction, double offset, Vector2d pos) {
        this.direction = direction;
        this.offset = offset;
        this.pos = pos;
    }
}
