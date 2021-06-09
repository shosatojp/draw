package jp.shosato.micropaint.events.key;

import jp.shosato.micropaint.events.Event;

public class CharInputEvent extends Event {
    public final long window;
    public final int codepoint;

    public CharInputEvent(long window, int codepoint) {
        this.window = window;
        this.codepoint = codepoint;
    }
}
