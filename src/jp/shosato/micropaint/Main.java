package jp.shosato.micropaint;

import org.joml.Vector2d;
import org.joml.Vector4d;

import jp.shosato.micropaint.components.ButtonComponent;
import jp.shosato.micropaint.components.Canvas;
import jp.shosato.micropaint.components.ColorPickerComponent;
import jp.shosato.micropaint.components.HorizontalContainerComponent;
import jp.shosato.micropaint.components.InputComponent;
import jp.shosato.micropaint.components.LabelComponent;
import jp.shosato.micropaint.components.RectangleComponent;
import jp.shosato.micropaint.components.RootComponent;
import jp.shosato.micropaint.components.VerticalContainerComponent;
import jp.shosato.micropaint.components.figures.FreeLineFigure;
import jp.shosato.micropaint.components.figures.PolygonFigure;
import jp.shosato.micropaint.components.figures.RectangleFigure;
import jp.shosato.micropaint.controllers.Controller;
import jp.shosato.micropaint.events.handlers.ButtonClickedEvent;
import jp.shosato.micropaint.events.handlers.ColorChangedEvent;
import jp.shosato.micropaint.events.handlers.TextInputEvent;
import jp.shosato.micropaint.models.CanvasModel;
import jp.shosato.micropaint.models.ColorModel;
import jp.shosato.micropaint.models.CurrentFigureModel;
import jp.shosato.micropaint.models.SelectionModel;
import jp.shosato.micropaint.models.StrokeWidthModel;
import jp.shosato.micropaint.models.ToolModel;
import jp.shosato.micropaint.tools.DrawTool;
import jp.shosato.micropaint.tools.MoveTool;
import jp.shosato.micropaint.tools.SelectTool;
import jp.shosato.micropaint.utils.Colors;
import jp.shosato.micropaint.utils.Utility;
import jp.shosato.micropaint.views.ButtonsView;
import jp.shosato.micropaint.views.CanvasView;
import jp.shosato.micropaint.views.ColorPanelView;
import jp.shosato.micropaint.views.StrokeWidthView;

class Main {
    private Window window;

    private Main() {
        // ウィンドウとコントロールの作成
        window = new Window(1000, 700);
        Controller ctrl = new Controller(window.getWindowHandle());

        // ウィンドウのルート要素
        RootComponent root = new RootComponent(window, ctrl);
        ctrl.setRootComponent(root);
        window.setRootComponent(root);

        // ウィンドウの組み立て
        createWindow(root);

        // 描画・イベントループ
        window.loop();
    }

    private void createWindow(RootComponent root) {

        // Models
        StrokeWidthModel strokeWidthModel = new StrokeWidthModel();
        ColorModel colorPanelModel = new ColorModel();
        ToolModel toolModel = new ToolModel();
        SelectionModel selectionModel = new SelectionModel();
        CurrentFigureModel currentFigureModel = new CurrentFigureModel();
        CanvasModel canvasModel = new CanvasModel();

        // Views/Controllers
        ColorPanelView colorPanelView = new ColorPanelView(colorPanelModel);
        CanvasView canvasView = new CanvasView(toolModel, selectionModel, currentFigureModel, colorPanelModel,
                canvasModel, strokeWidthModel);
        ButtonsView buttonsView = new ButtonsView(toolModel, colorPanelModel, strokeWidthModel, currentFigureModel,
                canvasModel, canvasView);

        //
        HorizontalContainerComponent hstack = new HorizontalContainerComponent(window.getWidth(), window.getHeight());
        hstack.addChildComponent(buttonsView);
        hstack.addChildComponent(canvasView);
        hstack.addChildComponent(colorPanelView);

        root.addChildComponent(hstack);
    }

    public static void main(String[] args) {
        new Main();
    }
}
