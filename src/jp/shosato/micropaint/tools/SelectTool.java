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

public class SelectTool extends Tool implements MouseClickEventListener {

    private HashSet<FigureComponent> selectedFigures = new HashSet<FigureComponent>();
    public final EventHandler<SelectionChangedEventHandler> onSelectionChanged = new EventHandler<>();

    public SelectTool(Canvas canvas) {
        super(canvas);
    }

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
     * Canvasから呼ばれる
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
