package jp.shosato.micropaint.events.mouse;

import jp.shosato.micropaint.events.EventListener;

public interface MouseEnterEventListener extends EventListener {
    public void onMouseEnter(MouseEvent event);

    public void onMouseEnter(MouseEvent event, boolean captureing);
}
