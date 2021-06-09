package jp.shosato.micropaint.events.mouse;

import jp.shosato.micropaint.events.EventListener;

public interface MouseLeaveEventListener extends EventListener {
    public void onMouseLeave(MouseEvent event);

    public void onMouseLeave(MouseEvent event, boolean captureing);
}
