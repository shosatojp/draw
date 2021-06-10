package jp.shosato.micropaint.models;

import org.joml.Vector2d;

import jp.shosato.micropaint.utils.SingleValueObservable;

public class CanvasModel {
    public SingleValueObservable<Vector2d> canvasScale = new SingleValueObservable<>(new Vector2d(1, 1));
    public SingleValueObservable<Vector2d> canvasTranslate = new SingleValueObservable<>(new Vector2d(0, 0));
}
