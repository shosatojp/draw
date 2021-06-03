package jp.shosato.draw.models;

import org.joml.Vector4d;
import org.joml.Vector4f;

import jp.shosato.draw.components.FigureComponent;
import jp.shosato.draw.utils.Pair;
import jp.shosato.draw.utils.Runnable;
import jp.shosato.draw.utils.SingleValueObservable;

public class CurrentFigureModel {
    public final SingleValueObservable<Pair<FigureComponent, Runnable<FigureComponent>>> currentFigure = new SingleValueObservable<>();
}
