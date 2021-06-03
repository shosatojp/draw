package jp.shosato.draw.events.mouse;

import jp.shosato.draw.events.EventListener;

public interface MouseLeaveEventListener extends EventListener {
    public void onMouseLeave(MouseEvent event);

    public void onMouseLeave(MouseEvent event, boolean captureing);
}
