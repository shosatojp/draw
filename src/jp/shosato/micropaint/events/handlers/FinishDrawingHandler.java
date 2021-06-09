package jp.shosato.micropaint.events.handlers;

import jp.shosato.micropaint.events.Event;
import jp.shosato.micropaint.events.IEventHandler;

@FunctionalInterface
public interface FinishDrawingHandler extends IEventHandler<Event> {
}
