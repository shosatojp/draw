package jp.shosato.draw.events.focus;

import jp.shosato.draw.events.EventListener;

public interface FocusOutEventListener extends EventListener {
    public void onFocusOut(FocusOutEvent event);
}
