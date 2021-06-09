package jp.shosato.micropaint.events.focus;

import jp.shosato.micropaint.events.EventListener;

public interface FocusInEventListener extends EventListener {
    public void onFocusIn(FocusInEvent event);
}
