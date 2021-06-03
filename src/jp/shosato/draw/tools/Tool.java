package jp.shosato.draw.tools;

import jp.shosato.draw.components.Canvas;
import jp.shosato.draw.utils.IDrawable2D;

/**
 * 図形に対する操作を実装するクラス。イベントはCanvasからもらう
 */
public abstract class Tool implements IDrawable2D {
    protected Canvas canvas;

    public Tool(Canvas canvas) {
        this.canvas = canvas;
    }
}
