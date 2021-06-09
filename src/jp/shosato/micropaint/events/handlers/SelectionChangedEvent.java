package jp.shosato.micropaint.events.handlers;

import java.util.ArrayList;

import jp.shosato.micropaint.components.FigureComponent;
import jp.shosato.micropaint.events.Event;

public class SelectionChangedEvent extends Event {
    public enum Action {
        SELECT, UNSELECT, TOGGLE, UNSELECTALL
    }

    public final Action action;
    public final FigureComponent targetFigure;

    public SelectionChangedEvent(Action action, FigureComponent targetFigure) {
        this.action = action;
        this.targetFigure = targetFigure;
    }
}
