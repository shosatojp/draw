package jp.shosato.micropaint.events;

import jp.shosato.micropaint.components.FigureComponent;

@FunctionalInterface
public interface NextDrawingListener {
    public FigureComponent onNextDrawing();
}
