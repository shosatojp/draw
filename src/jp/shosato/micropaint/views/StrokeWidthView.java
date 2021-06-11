package jp.shosato.micropaint.views;

import jp.shosato.micropaint.components.InputComponent;
import jp.shosato.micropaint.events.handlers.TextInputEvent;
import jp.shosato.micropaint.models.StrokeWidthModel;
import jp.shosato.micropaint.utils.SingleValueObserver;

/**
 * 枠線の幅の入力と表示のView
 */
public class StrokeWidthView extends InputComponent {
    public StrokeWidthView(StrokeWidthModel model) {
        super(100, 40);

        model.strokeWidthObservable.addObserver((Double stroke) -> {
            this.setText(String.valueOf(stroke));
        });

        this.onTextInput.addEventHandler((TextInputEvent event) -> {
            try {
                double width = Double.valueOf(event.getText());
                model.strokeWidthObservable.setValue(width);
            } catch (Exception e) {
                System.err.println(e);
            }
        });
    }
}
