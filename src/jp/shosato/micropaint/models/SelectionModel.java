package jp.shosato.micropaint.models;

import java.util.HashSet;

import jp.shosato.micropaint.components.BasicComponent;
import jp.shosato.micropaint.components.FigureComponent;
import jp.shosato.micropaint.events.handlers.SelectionChangedEvent;
import jp.shosato.micropaint.utils.SingleValueObservable;

/**
 * 選択中の図形を管理
 */
public class SelectionModel {

    public final SingleValueObservable<HashSet<FigureComponent>> selectedFigures = new SingleValueObservable<>(new HashSet<>());

    public void select(final FigureComponent figure) {
        this.selectedFigures.getValue().add(figure);
        this.selectedFigures.notifyChange();
    }

    public void unselect(final FigureComponent figure) {
        this.selectedFigures.getValue().remove(figure);
        this.selectedFigures.notifyChange();
    }

    public void unselectAll() {
        this.selectedFigures.getValue().clear();
        this.selectedFigures.notifyChange();
    }

    public void toggleSelection(final FigureComponent figure) {
        if (selectedFigures.getValue().contains(figure)) {
            unselect(figure);
        } else {
            select(figure);
        }
    }

    public void change(SelectionChangedEvent event) {
        switch (event.action) {
            case TOGGLE:
                toggleSelection(event.targetFigure);
                break;
            case UNSELECTALL:
                unselectAll();
                break;
            case SELECT:
                select(event.targetFigure);
                break;
            case UNSELECT:
                unselect(event.targetFigure);
                break;
        }
    }

}
