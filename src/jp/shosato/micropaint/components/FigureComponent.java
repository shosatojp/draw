package jp.shosato.micropaint.components;

import java.util.ArrayList;

import org.joml.Vector2d;
import org.joml.Vector4d;
import org.joml.Vector4f;

import jp.shosato.micropaint.components.figures.IFigure;
import jp.shosato.micropaint.events.EventHandler;
import jp.shosato.micropaint.events.handlers.FinishDrawingHandler;
import jp.shosato.micropaint.events.mouse.MouseClickEventListener;
import jp.shosato.micropaint.events.mouse.MouseEnterEventListener;
import jp.shosato.micropaint.events.mouse.MouseEvent;
import jp.shosato.micropaint.events.mouse.MouseLeaveEventListener;
import jp.shosato.micropaint.events.mouse.MouseMoveEventListener;
import jp.shosato.micropaint.utils.BoundingBox;
import jp.shosato.micropaint.utils.IBoundingBox;

public abstract class FigureComponent extends BasicComponent implements IFigure, MouseClickEventListener,
        MouseEnterEventListener, MouseLeaveEventListener, MouseMoveEventListener {

    /**
     * 描画終了イベントハンドラ
     */
    public EventHandler<FinishDrawingHandler> onFinished = new EventHandler<>();

    protected Vector4d fillColor = new Vector4d(1, 1, 1, 1);
    protected Vector4d strokeColor = new Vector4d(0, 0, 0, 1);
    protected double strokeWidth = 5;

    public FigureComponent() {
    }

    public ArrayList<Vector2d> getVertices() {
        return null;
    };

    public void setFill(Vector4d color) {
        this.fillColor = color;
    }

    public Vector4d getFill() {
        return fillColor;
    }

    public void setStroke(Vector4d color) {
        this.strokeColor = color;
    }

    public Vector4d getStroke() {
        return strokeColor;
    }

    public double getStrokeWidth() {
        return strokeWidth;
    }

    public void setStrokeWidth(double width) {
        this.strokeWidth = width;
    }

    /* dだけ移動 */
    public abstract void move(Vector2d d);

    @Override
    public void onMouseClicked(MouseEvent event) {
    }

    @Override
    public void onMouseClicked(MouseEvent event, boolean captureing) {
    }

    @Override
    public void onMouseEnter(MouseEvent event) {
    }

    @Override
    public void onMouseEnter(MouseEvent event, boolean captureing) {
    }

    @Override
    public void onMouseLeave(MouseEvent event) {
    }

    @Override
    public void onMouseLeave(MouseEvent event, boolean captureing) {
    }

    @Override
    public void onMouseMoved(MouseEvent event) {
    }

    @Override
    public void onMouseMoved(MouseEvent event, boolean captureing) {
    }

    @Override
    public void onMouseClickDrawing(Canvas canvas, MouseEvent event) {
    }

    @Override
    public void onMouseMoveDrawing(Canvas canvas, MouseEvent event) {
    }

    @Override
    public Vector2d getCenter() {
        /* this is nomeaning because it has no children */
        return new Vector2d(0, 0);
    }
}
