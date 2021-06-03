package jp.shosato.draw.events.handlers;

import java.util.ArrayList;

import jp.shosato.draw.components.FigureComponent;
import jp.shosato.draw.events.Event;

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
