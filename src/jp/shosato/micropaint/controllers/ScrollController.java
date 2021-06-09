package jp.shosato.micropaint.controllers;

import jp.shosato.micropaint.components.BasicComponent;
import jp.shosato.micropaint.events.mouse.MouseEvent;
import jp.shosato.micropaint.events.scroll.ScrollInputEventListener;

public class ScrollController {
    /**
     * [マウス移動イベント] クリックと同じ
     */
    public final BasicMouseEventInvoker scrollEventInvoker = new BasicMouseEventInvoker(ScrollInputEventListener.class,
            (BasicComponent component, MouseEvent event, boolean captureing) -> {
                if (captureing) {
                    ((ScrollInputEventListener) component).onScroll(event, true);
                } else {
                    ((ScrollInputEventListener) component).onScroll(event);
                }
            });
}
