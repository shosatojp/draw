package jp.shosato.micropaint.tools;

import jp.shosato.micropaint.components.Canvas;
import jp.shosato.micropaint.utils.IDrawable2D;

/**
 * 図形に対する操作を実装するクラス。イベントはCanvasからもらう
 */
public abstract class Tool implements IDrawable2D {
    protected Canvas canvas;

    public Tool(Canvas canvas) {
        this.canvas = canvas;
    }
}
