package jp.shosato.micropaint.utils;

import java.util.ArrayList;

/**
 * ジェネリック版Observable
 */
public class SingleValueObservable<T> {
    private ArrayList<SingleValueObserver<T>> observers = new ArrayList<>();

    private T value;

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
        notifyChange();
    }

    public SingleValueObservable() {
    }

    public SingleValueObservable(T value) {
        this.value = value;
    }

    public void addObserver(SingleValueObserver<T> observer) {
        this.observers.add(observer);
    }

    public void notifyChange() {
        for (SingleValueObserver<T> observer : observers) {
            observer.update(value);
        }
    }

    public boolean hasValue() {
        return value != null;
    }
}
