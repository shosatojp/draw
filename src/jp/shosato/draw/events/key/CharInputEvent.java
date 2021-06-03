package jp.shosato.draw.events.key;

import jp.shosato.draw.events.Event;

public class CharInputEvent extends Event {
    public final long window;
    public final int codepoint;

    public CharInputEvent(long window, int codepoint) {
        this.window = window;
        this.codepoint = codepoint;
    }
}
