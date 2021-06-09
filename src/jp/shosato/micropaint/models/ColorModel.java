package jp.shosato.micropaint.models;

import org.joml.Vector4d;

import jp.shosato.micropaint.utils.SingleValueObservable;

public class ColorModel {
    public enum Target {
        FILL, STROKE
    }

    public SingleValueObservable<Vector4d> fillColor = new SingleValueObservable<>(new Vector4d(1, 0, 0, 1));
    public SingleValueObservable<Vector4d> strokeColor = new SingleValueObservable<>(new Vector4d(0, 1, 0, 1));
    public SingleValueObservable<Target> target = new SingleValueObservable<>(Target.FILL);

    public void setColor(Vector4d color) {
        switch (target.getValue()) {
            case FILL:
                fillColor.setValue(color);
                break;
            case STROKE:
                strokeColor.setValue(color);
                break;
        }
    }
}
