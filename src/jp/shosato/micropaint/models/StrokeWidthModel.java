package jp.shosato.micropaint.models;

import jp.shosato.micropaint.components.InputComponent;
import jp.shosato.micropaint.utils.SingleValueObservable;

public class StrokeWidthModel {
    public final SingleValueObservable<Double> strokeWidthObservable = new SingleValueObservable<>(5.0);
}
