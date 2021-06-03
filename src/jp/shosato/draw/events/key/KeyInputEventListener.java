package jp.shosato.draw.events.key;

import jp.shosato.draw.events.EventListener;

public interface KeyInputEventListener extends EventListener{
    public void onKeyInput(KeyInputEvent event);
}
