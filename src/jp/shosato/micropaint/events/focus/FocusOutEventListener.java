package jp.shosato.micropaint.events.focus;

import jp.shosato.micropaint.events.EventListener;

public interface FocusOutEventListener extends EventListener {
    public void onFocusOut(FocusOutEvent event);
}
