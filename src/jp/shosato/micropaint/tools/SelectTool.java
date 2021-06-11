package jp.shosato.micropaint.tools;

import static org.lwjgl.glfw.GLFW.*;

import java.util.HashSet;

import jp.shosato.micropaint.components.BasicComponent;
import jp.shosato.micropaint.components.Canvas;
import jp.shosato.micropaint.components.FigureComponent;
import jp.shosato.micropaint.events.EventHandler;
import jp.shosato.micropaint.events.handlers.SelectionChangedEvent;
import jp.shosato.micropaint.events.handlers.SelectionChangedEventHandler;
import jp.shosato.micropaint.events.handlers.SelectionChangedEvent.Action;
import jp.shosato.micropaint.events.mouse.MouseClickEventListener;
import jp.shosato.micropaint.events.mouse.MouseEvent;
import jp.shosato.micropaint.utils.BoundingBox;

/**
 * 選択ツール
 */
public class SelectTool extends Tool implements MouseClickEventListener {

    /**
     * 選択範囲を描画する図形。状態はModelで持つためこれは一時的
     */
    private HashSet<FigureComponent> selectedFigures = new HashSet<FigureComponent>();

    /**
     * 選択状態が変化したときのイベント
     */
    public final EventHandler<SelectionChangedEventHandler> onSelectionChanged = new EventHandler<>();

    public SelectTool(Canvas canvas) {
        super(canvas);
    }

    /**
     * 選択状態は外部から設定
     */
    public void setSelectedFigures(HashSet<FigureComponent> selectedFigures) {
        this.selectedFigures = selectedFigures;
    }

    @Override
    public void draw() {
        for (FigureComponent figure : selectedFigures) {
            BoundingBox bb = figure.getBB();
            bb.draw();
        }
    }

    /**
     * 選択状態が変化したらイベントを発行
     */
    @Override
    public void onMouseClicked(MouseEvent event) {
        switch (event.getButton()) {
            case GLFW_MOUSE_BUTTON_LEFT:
                switch (event.getAction()) {
                    case GLFW_PRESS: {
                        BasicComponent target = event.getTarget();
                        if (target instanceof FigureComponent) {
                            this.onSelectionChanged
                                    .invoke(new SelectionChangedEvent(Action.TOGGLE, (FigureComponent) target));
                        } else {
                            this.onSelectionChanged.invoke(new SelectionChangedEvent(Action.UNSELECTALL, null));
                        }
                    }
                        break;
                }
                break;
            case GLFW_MOUSE_BUTTON_RIGHT:
                switch (event.getAction()) {
                    case GLFW_PRESS:
                        break;
                }
                break;
        }
    }

    @Override
    public void onMouseClicked(MouseEvent event, boolean captureing) {
    }

    public HashSet<FigureComponent> getSelected() {
        return selectedFigures;
    }

}
