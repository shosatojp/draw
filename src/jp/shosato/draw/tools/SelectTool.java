package jp.shosato.draw.tools;

import static org.lwjgl.glfw.GLFW.*;

import java.util.HashSet;

import jp.shosato.draw.components.BasicComponent;
import jp.shosato.draw.components.Canvas;
import jp.shosato.draw.components.FigureComponent;
import jp.shosato.draw.events.EventHandler;
import jp.shosato.draw.events.handlers.SelectionChangedEvent;
import jp.shosato.draw.events.handlers.SelectionChangedEventHandler;
import jp.shosato.draw.events.handlers.SelectionChangedEvent.Action;
import jp.shosato.draw.events.mouse.MouseClickEventListener;
import jp.shosato.draw.events.mouse.MouseEvent;
import jp.shosato.draw.utils.BoundingBox;

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
