package jp.shosato.draw.models;

import jp.shosato.draw.components.InputComponent;
import jp.shosato.draw.utils.SingleValueObservable;

public class StrokeWidthModel {
    public SingleValueObservable<Double> strokeWidthObservable = new SingleValueObservable<>(5.0);
}
