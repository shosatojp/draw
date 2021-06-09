package jp.shosato.micropaint.events.scroll;

import java.util.EventListener;

import jp.shosato.micropaint.events.mouse.MouseEvent;

public interface ScrollInputEventListener extends EventListener {
    public void onScroll(MouseEvent event);

    public void onScroll(MouseEvent event, boolean captureing);
}
