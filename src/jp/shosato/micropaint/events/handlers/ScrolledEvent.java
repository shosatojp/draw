package jp.shosato.micropaint.events.handlers;

import jp.shosato.micropaint.events.Event;

public class ScrolledEvent extends Event {
    public enum Direction {
        VERTICAL, HORIZONTAL, SCALE
    }

    public final Direction direction;
    public final double offset;

    public ScrolledEvent(Direction direction, double offset) {
        this.direction = direction;
        this.offset = offset;
    }
}
