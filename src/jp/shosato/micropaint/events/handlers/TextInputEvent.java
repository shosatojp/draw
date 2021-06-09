package jp.shosato.micropaint.events.handlers;

import jp.shosato.micropaint.events.Event;

public class TextInputEvent extends Event {
    private String text;

    public TextInputEvent(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
