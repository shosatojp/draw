package jp.shosato.draw.events.key;

import jp.shosato.draw.events.EventListener;

public interface CharInputEventListener extends EventListener {
    public void onCharInput(CharInputEvent event);
}
