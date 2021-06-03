package jp.shosato.draw.components.figures;

import jp.shosato.draw.components.Canvas;
import jp.shosato.draw.events.mouse.MouseEvent;
import jp.shosato.draw.utils.IDrawable2D;

public interface IFigure extends IDrawable2D {
    public void onMouseMoveDrawing(Canvas canvas, MouseEvent event);

    public void onMouseClickDrawing(Canvas canvas, MouseEvent event);
}
