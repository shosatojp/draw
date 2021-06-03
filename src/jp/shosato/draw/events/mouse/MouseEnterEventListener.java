package jp.shosato.draw.events.mouse;

import jp.shosato.draw.events.EventListener;

public interface MouseEnterEventListener extends EventListener {
    public void onMouseEnter(MouseEvent event);

    public void onMouseEnter(MouseEvent event, boolean captureing);
}
