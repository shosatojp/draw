package jp.shosato.draw;

import org.joml.Vector2d;
import org.joml.Vector4d;

import jp.shosato.draw.components.ButtonComponent;
import jp.shosato.draw.components.Canvas;
import jp.shosato.draw.components.ColorPickerComponent;
import jp.shosato.draw.components.HorizontalContainerComponent;
import jp.shosato.draw.components.InputComponent;
import jp.shosato.draw.components.LabelComponent;
import jp.shosato.draw.components.RectangleComponent;
import jp.shosato.draw.components.VerticalContainerComponent;
import jp.shosato.draw.components.figures.FreeLineFigure;
import jp.shosato.draw.components.figures.PolygonFigure;
import jp.shosato.draw.components.figures.RectangleFigure;
import jp.shosato.draw.events.handlers.ButtonClickedEvent;
import jp.shosato.draw.events.handlers.ColorChangedEvent;
import jp.shosato.draw.events.handlers.TextInputEvent;
import jp.shosato.draw.components.RootComponent;
import jp.shosato.draw.models.ColorModel;
import jp.shosato.draw.models.CurrentFigureModel;
import jp.shosato.draw.models.SelectionModel;
import jp.shosato.draw.models.StrokeWidthModel;
import jp.shosato.draw.models.ToolModel;
import jp.shosato.draw.tools.DrawTool;
import jp.shosato.draw.tools.MoveTool;
import jp.shosato.draw.tools.SelectTool;
import jp.shosato.draw.utils.Colors;
import jp.shosato.draw.utils.Utility;
import jp.shosato.draw.views.ButtonsView;
import jp.shosato.draw.views.CanvasView;
import jp.shosato.draw.views.ColorPanelView;
import jp.shosato.draw.views.StrokeWidthView;

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
        // createWindowTest(root);
        createWindow(root);
        // root.translate.add(100, 100);

        // 描画・イベントループ
        window.loop();
    }

    private void createWindowTest(RootComponent root) {
        // RectangleComponent rect = new RectangleComponent(100, 200, Colors.WHITE);
        ButtonComponent rect = new ButtonComponent(100, 200, "hoge");
        // rect.rotate = 45;
        rect.translate = new Vector2d(100, 100);
        // LabelComponent rect = new LabelComponent(100, 200, "hoge");
        VerticalContainerComponent v = new VerticalContainerComponent(new Vector2d(100, 100), 300, 300, Colors.WHITE);
        v.rotate = 45;

        v.addChildComponent(rect);
        // rect.scale = new Vector2d(2, 2);
        root.addChildComponent(v);
    }

    private void createWindow(RootComponent root) {

        // Models
        StrokeWidthModel strokeWidthModel = new StrokeWidthModel();
        ColorModel colorPanelModel = new ColorModel();
        ToolModel toolModel = new ToolModel();
        SelectionModel selectionModel = new SelectionModel();
        CurrentFigureModel currentFigureModel = new CurrentFigureModel();

        // Views/Controllers
        ColorPanelView colorPanelView = new ColorPanelView(colorPanelModel);
        CanvasView canvasView = new CanvasView(toolModel, selectionModel, currentFigureModel, colorPanelModel,
                strokeWidthModel);
        ButtonsView buttonsView = new ButtonsView(toolModel, colorPanelModel, strokeWidthModel, currentFigureModel,
                canvasView);

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
