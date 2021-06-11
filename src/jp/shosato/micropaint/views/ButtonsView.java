package jp.shosato.micropaint.views;

import org.joml.Vector2d;

import jp.shosato.micropaint.components.ButtonComponent;
import jp.shosato.micropaint.components.FigureComponent;
import jp.shosato.micropaint.components.LabelComponent;
import jp.shosato.micropaint.components.VerticalContainerComponent;
import jp.shosato.micropaint.components.figures.FreeLineFigure;
import jp.shosato.micropaint.components.figures.PolygonFigure;
import jp.shosato.micropaint.components.figures.RectangleFigure;
import jp.shosato.micropaint.events.handlers.ButtonClickedEvent;
import jp.shosato.micropaint.models.CanvasModel;
import jp.shosato.micropaint.models.ColorModel;
import jp.shosato.micropaint.models.CurrentFigureModel;
import jp.shosato.micropaint.models.StrokeWidthModel;
import jp.shosato.micropaint.models.ToolModel;
import jp.shosato.micropaint.utils.Pair;
import jp.shosato.micropaint.utils.Runnable;
import jp.shosato.micropaint.utils.SVGSerializer;

/**
 * ウィンドウ左のボタンをまとめるView
 */
public class ButtonsView extends VerticalContainerComponent {

    private final ButtonComponent selectButton;
    private final ButtonComponent moveButton;
    private final ButtonComponent rectButton;
    private final ButtonComponent polygonButton;
    private final ButtonComponent freelineButton;
    private final ButtonComponent clearButton;
    private final StrokeWidthView strokeWidthView;
    private final ButtonComponent canvasZoominButton;
    private final ButtonComponent canvasZoomoutButton;
    private final LabelComponent zoomLabel;

    public ButtonsView(ToolModel toolModel, ColorModel colorPanelModel, StrokeWidthModel strokeWidthModel,
            CurrentFigureModel currentFigureModel, CanvasModel canvasModel, CanvasView canvasView) {
        super(100, 700);

        // Construct View
        selectButton = new ButtonComponent(70, 40, "選択");
        moveButton = new ButtonComponent(70, 40, "移動");
        rectButton = new ButtonComponent(70, 40, "長方形");
        polygonButton = new ButtonComponent(70, 40, "ポリゴン");
        clearButton = new ButtonComponent(70, 40, "白紙");
        freelineButton = new ButtonComponent(70, 40, "ペン");
        strokeWidthView = new StrokeWidthView(strokeWidthModel);
        canvasZoominButton = new ButtonComponent(70, 40, "+");
        canvasZoomoutButton = new ButtonComponent(70, 40, "-");
        zoomLabel = new LabelComponent(70, 40,
                String.valueOf((int) (canvasModel.canvasScale.getValue().x * 100)) + "%");

        this.addChildComponent(selectButton);
        this.addChildComponent(moveButton);
        this.addChildComponent(rectButton);
        this.addChildComponent(polygonButton);
        this.addChildComponent(freelineButton);
        this.addChildComponent(clearButton);
        this.addChildComponent(strokeWidthView);
        this.addChildComponent(canvasZoominButton);
        this.addChildComponent(canvasZoomoutButton);
        this.addChildComponent(zoomLabel);

        // Observer
        canvasModel.canvasScale.addObserver((Vector2d scale) -> {
            zoomLabel.setText(String.valueOf((int) (canvasModel.canvasScale.getValue().x * 100)) + "%");
        });

        // EventHandlers
        selectButton.onButtonClicked
                .addEventHandler((ButtonClickedEvent event) -> toolModel.setTool(canvasView.selectTool));
        moveButton.onButtonClicked
                .addEventHandler((ButtonClickedEvent event) -> toolModel.setTool(canvasView.moveTool));
        rectButton.onButtonClicked.addEventHandler((ButtonClickedEvent event) -> {
            toolModel.setTool(canvasView.drawTool);
            Runnable<FigureComponent> generator = () -> new RectangleFigure(colorPanelModel.fillColor.getValue());
            currentFigureModel.currentFigure.setValue(new Pair<>(generator.run(), generator));
        });
        polygonButton.onButtonClicked.addEventHandler((ButtonClickedEvent event) -> {
            toolModel.setTool(canvasView.drawTool);
            Runnable<FigureComponent> generator = () -> new PolygonFigure(colorPanelModel.fillColor.getValue());
            currentFigureModel.currentFigure.setValue(new Pair<>(generator.run(), generator));
        });
        freelineButton.onButtonClicked.addEventHandler((ButtonClickedEvent event) -> {
            toolModel.setTool(canvasView.drawTool);
            Runnable<FigureComponent> generator = () -> new FreeLineFigure(colorPanelModel.strokeColor.getValue(),
                    strokeWidthModel.strokeWidthObservable.getValue());
            currentFigureModel.currentFigure.setValue(new Pair<>(generator.run(), generator));
        });
        clearButton.onButtonClicked.addEventHandler((ButtonClickedEvent event) -> {
            canvasView.canvas.removeChildren();
        });
        canvasZoominButton.onButtonClicked.addEventHandler((ButtonClickedEvent event) -> {
            canvasModel.canvasScale.setValue(new Vector2d(canvasModel.canvasScale.getValue()).add(0.1, 0.1));
        });
        canvasZoomoutButton.onButtonClicked.addEventHandler((ButtonClickedEvent event) -> {
            Vector2d nextScale = new Vector2d(canvasModel.canvasScale.getValue()).sub(0.1, 0.1);
            if (nextScale.x > 0.1 && nextScale.y > 0.1)
                canvasModel.canvasScale.setValue(new Vector2d(nextScale));
        });

        ButtonComponent screenshot = new ButtonComponent(70, 40, "保存");
        screenshot.onButtonClicked.addEventHandler((ButtonClickedEvent) -> {
            // BoundingBox bb = canvasView.canvas.getBB();
            // Utility.getScreenshot((int) bb.topLeft.x, (int) bb.topLeft.y, (int)
            // bb.getWidth(), (int) bb.getHeight(),
            // "screenshot.ppm");
            try {
                new SVGSerializer(canvasView.canvas).toFile("test.svg");
            } catch (Exception e) {
                System.out.println(e);
            }
        });
        this.addChildComponent(screenshot);
    }
}
