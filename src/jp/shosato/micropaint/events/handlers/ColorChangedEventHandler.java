package jp.shosato.micropaint.events.handlers;

import jp.shosato.micropaint.events.IEventHandler;

@FunctionalInterface
public interface ColorChangedEventHandler extends IEventHandler<ColorChangedEvent> {
}
