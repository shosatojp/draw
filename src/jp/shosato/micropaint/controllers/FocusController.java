package jp.shosato.micropaint.controllers;

import jp.shosato.micropaint.components.BasicComponent;
import jp.shosato.micropaint.events.focus.FocusInEvent;
import jp.shosato.micropaint.events.focus.FocusInEventListener;
import jp.shosato.micropaint.events.focus.FocusOutEvent;
import jp.shosato.micropaint.events.focus.FocusOutEventListener;

public class FocusController {
    private BasicComponent focused;

    public BasicComponent getFocused() {
        return focused;
    }

    public void trySwitchFocus(BasicComponent newcomponent) {
        // フォーカスが変わったとき
        if (focused != newcomponent) {
            // フォーカスアウト
            if (focused != null) {
                FocusOutEvent outEvent = new FocusOutEvent();
                outEvent.setTarget(focused);
                invokeFocusedOut(focused, outEvent);
            }

            // フォーカスイン
            FocusInEvent inEvent = new FocusInEvent();
            inEvent.setTarget(newcomponent);
            invokeFocusedIn(newcomponent, inEvent);

            // 現在フォーカスしている要素を変更
            focused = newcomponent;
        }
    }

    private void invokeFocusedIn(BasicComponent component, FocusInEvent event) {
        if (component instanceof FocusInEventListener) {
            ((FocusInEventListener) component).onFocusIn(event);
        }

        if (!event.cancelled()) {
            BasicComponent parent = component.getParent();
            if (parent != null) {
                event.setCurrentTarget(parent);
                invokeFocusedIn(parent, event);
            }
        }
    }

    private void invokeFocusedOut(BasicComponent component, FocusOutEvent event) {
        if (component instanceof FocusOutEventListener) {
            ((FocusOutEventListener) component).onFocusOut(event);
        }

        if (!event.cancelled()) {
            BasicComponent parent = component.getParent();
            if (parent != null) {
                event.setCurrentTarget(parent);
                invokeFocusedOut(parent, event);
            }
        }
    }
}
