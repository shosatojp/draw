package jp.shosato.micropaint.events.key;

import jp.shosato.micropaint.events.EventListener;

public interface CharInputEventListener extends EventListener {
    public void onCharInput(CharInputEvent event);
}
