package jp.shosato.draw.events.handlers;

import jp.shosato.draw.events.Event;

public class TextInputEvent extends Event {
    private String text;

    public TextInputEvent(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
