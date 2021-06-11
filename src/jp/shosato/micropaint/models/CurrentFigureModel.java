package jp.shosato.micropaint.models;

import org.joml.Vector4d;
import org.joml.Vector4f;

import jp.shosato.micropaint.components.FigureComponent;
import jp.shosato.micropaint.utils.Pair;
import jp.shosato.micropaint.utils.Runnable;
import jp.shosato.micropaint.utils.SingleValueObservable;

/**
 * 現在描画中の図形を管理
 */
public class CurrentFigureModel {
    public final SingleValueObservable<Pair<FigureComponent, Runnable<FigureComponent>>> currentFigure = new SingleValueObservable<>();
}
