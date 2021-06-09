package jp.shosato.micropaint.events.key;

import jp.shosato.micropaint.events.EventListener;

public interface KeyInputEventListener extends EventListener{
    public void onKeyInput(KeyInputEvent event);
}
