package jp.shosato.draw.views;

import java.util.HashMap;
import java.util.HashSet;

import org.joml.Vector4d;

import jp.shosato.draw.components.Canvas;
import jp.shosato.draw.components.FigureComponent;
import jp.shosato.draw.components.HorizontalContainerComponent;
import jp.shosato.draw.events.Event;
import jp.shosato.draw.events.handlers.SelectionChangedEvent;
import jp.shosato.draw.models.ColorModel;
import jp.shosato.draw.models.CurrentFigureModel;
import jp.shosato.draw.models.SelectionModel;
import jp.shosato.draw.models.StrokeWidthModel;
import jp.shosato.draw.models.ToolModel;
import jp.shosato.draw.tools.DrawTool;
import jp.shosato.draw.tools.MoveTool;
import jp.shosato.draw.tools.SelectTool;
import jp.shosato.draw.tools.Tool;
import jp.shosato.draw.utils.Colors;
import jp.shosato.draw.utils.Pair;
import jp.shosato.draw.utils.Runnable;
import jp.shosato.draw.utils.SingleValueObservable;

public class CanvasView extends HorizontalContainerComponent {

    public final Canvas canvas;
    public final SelectTool selectTool;
    public final DrawTool drawTool;
    public final MoveTool moveTool;

    private final ToolModel toolModel;
    private final SelectionModel selectionModel;
    private final CurrentFigureModel currentFigureModel;
    private final ColorModel colorPanelModel;
    private final StrokeWidthModel strokeWidthModel;

    /**
     * 図形オブジェクトはイベントを受け取るためにGUIのコンポーネントとしており、
     * Canvasの子要素となっているので描画されている図形に関するModelは持たない。
     */
    public CanvasView(ToolModel toolModel, SelectionModel selectionModel, CurrentFigureModel currentFigureModel,
            ColorModel colorPanelModel, StrokeWidthModel strokeWidthModel) {
        super(700, 700);

        this.toolModel = toolModel;
        this.selectionModel = selectionModel;
        this.currentFigureModel = currentFigureModel;
        this.colorPanelModel = colorPanelModel;
        this.strokeWidthModel = strokeWidthModel;

        canvas = new Canvas(700, 700, Colors.WHITE);
        drawTool = new DrawTool(canvas);
        selectTool = new SelectTool(canvas);
        moveTool = new MoveTool(canvas, selectTool);

        this.toolModel.addTool(drawTool);
        this.toolModel.addTool(selectTool);
        this.toolModel.addTool(moveTool);

        this.addChildComponent(canvas);

        // Observers
        this.selectionModel.selectedFigures.addObserver((HashSet<FigureComponent> selected) -> {
            this.selectTool.setSelectedFigures(selected);
        });
        this.toolModel.tools.addObserver((HashMap<Tool, Boolean> tools) -> {
            this.canvas.setTools(tools);
        });
        this.currentFigureModel.currentFigure.addObserver((Pair<FigureComponent, Runnable<FigureComponent>> pair) -> {
            if (pair != null) {
                drawTool.setCurrentFigure(pair.first);
                pair.first.onFinished.addEventHandler((Event event) -> {
                    SingleValueObservable<Pair<FigureComponent, Runnable<FigureComponent>>> current = currentFigureModel.currentFigure;
                    if (current.hasValue()) {
                        canvas.addChildComponent(current.getValue().first);
                        current.setValue(new Pair<>(current.getValue().second.run(), current.getValue().second));
                    }
                });
            }
        });
        this.colorPanelModel.fillColor.addObserver((Vector4d color) -> {
            if (currentFigureModel.currentFigure.hasValue()) {
                currentFigureModel.currentFigure.getValue().first.setFill(color);
            }
        });
        this.colorPanelModel.strokeColor.addObserver((Vector4d color) -> {
            if (currentFigureModel.currentFigure.hasValue()) {
                currentFigureModel.currentFigure.getValue().first.setStroke(color);
            }
        });
        this.strokeWidthModel.strokeWidthObservable.addObserver((Double width) -> {
            if (currentFigureModel.currentFigure.hasValue()) {
                currentFigureModel.currentFigure.getValue().first.setStrokeWidth(width);
            }
        });

        // EventHandlers
        selectTool.onSelectionChanged.addEventHandler((SelectionChangedEvent event) -> {
            this.selectionModel.change(event);
        });
    }
}
