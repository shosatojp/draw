package jp.shosato.micropaint.events.handlers;

import org.joml.Vector4d;

import jp.shosato.micropaint.events.Event;

public class ColorChangedEvent extends Event {
    private Vector4d color;

    public ColorChangedEvent(Vector4d color) {
        this.color = color;
    }

    public Vector4d getColor() {
        return color;
    }
}
