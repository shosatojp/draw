package jp.shosato.draw.events;

import jp.shosato.draw.components.FigureComponent;

@FunctionalInterface
public interface NextDrawingListener {
    public FigureComponent onNextDrawing();
}
