package jp.shosato.micropaint.events;

import java.lang.reflect.Method;
import java.util.ArrayList;

public class EventHandler<TEventHandler extends IEventHandler> {
    private ArrayList<TEventHandler> handlers = new ArrayList<>();

    public void addEventHandler(TEventHandler listener) {
        this.handlers.add(listener);
    }

    public void invoke(Event event) {
        for (IEventHandler handler : handlers) {
            handler.invoke(event);
        }
    }
}
