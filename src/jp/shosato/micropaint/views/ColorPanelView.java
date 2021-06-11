package jp.shosato.micropaint.views;

import org.joml.Vector4d;

import jp.shosato.micropaint.components.ButtonComponent;
import jp.shosato.micropaint.components.ColorPickerComponent;
import jp.shosato.micropaint.components.HorizontalContainerComponent;
import jp.shosato.micropaint.components.LabelComponent;
import jp.shosato.micropaint.components.VerticalContainerComponent;
import jp.shosato.micropaint.events.handlers.ButtonClickedEvent;
import jp.shosato.micropaint.events.handlers.ColorChangedEvent;
import jp.shosato.micropaint.models.ColorModel;
import jp.shosato.micropaint.utils.Utility;

/**
 * 色選択パネルのView
 */
public class ColorPanelView extends VerticalContainerComponent {
    private final ColorPickerComponent colorPicker;
    private final LabelComponent fillColorLabel;
    private final LabelComponent strokeColorLabel;
    private final ButtonComponent fillColorButton;
    private final ButtonComponent strokeColorButton;

    private final ColorModel colorPanelModel;

    public ColorPanelView(ColorModel colorPanelModel) {
        // Construct components
        super(200, 700);

        this.colorPanelModel = colorPanelModel;

        colorPicker = new ColorPickerComponent(200, 200);
        fillColorLabel = new LabelComponent(200, 40, getFillColorText());
        strokeColorLabel = new LabelComponent(200, 40, getStrokeColorText());
        fillColorButton = new ButtonComponent(70, 40, "Fill");
        strokeColorButton = new ButtonComponent(70, 40, "Stroke");

        HorizontalContainerComponent colorButtons = new HorizontalContainerComponent(200, 50);
        colorButtons.addChildComponent(fillColorButton);
        colorButtons.addChildComponent(strokeColorButton);

        this.addChildComponent(colorPicker);
        this.addChildComponent(fillColorLabel);
        this.addChildComponent(strokeColorLabel);
        this.addChildComponent(colorButtons);

        // Observers
        colorPanelModel.fillColor.addObserver((Vector4d color) -> {
            fillColorLabel.setText(getFillColorText());
        });
        colorPanelModel.strokeColor.addObserver((Vector4d color) -> {
            strokeColorLabel.setText(getStrokeColorText());
        });

        // EventHandlers
        colorPicker.onColorChanged.addEventHandler((ColorChangedEvent event) -> {
            colorPanelModel.setColor(event.getColor());
        });
        fillColorButton.onButtonClicked.addEventHandler((ButtonClickedEvent event) -> {
            colorPanelModel.target.setValue(ColorModel.Target.FILL);
        });
        strokeColorButton.onButtonClicked.addEventHandler((ButtonClickedEvent event) -> {
            colorPanelModel.target.setValue(ColorModel.Target.STROKE);
        });
    }

    private String getFillColorText() {
        return "Fill:" + Utility.getColorCode(colorPanelModel.fillColor.getValue());
    }

    private String getStrokeColorText() {
        return "Stroke:" + Utility.getColorCode(colorPanelModel.strokeColor.getValue());
    }
}
