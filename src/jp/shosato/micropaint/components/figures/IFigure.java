package jp.shosato.micropaint.components.figures;

import jp.shosato.micropaint.components.Canvas;
import jp.shosato.micropaint.events.mouse.MouseEvent;
import jp.shosato.micropaint.utils.IDrawable2D;

public interface IFigure extends IDrawable2D {
    public void onMouseMoveDrawing(Canvas canvas, MouseEvent event);

    public void onMouseClickDrawing(Canvas canvas, MouseEvent event);
}
