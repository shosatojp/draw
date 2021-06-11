package jp.shosato.micropaint.utils;

/**
 * ジェネリック版Observer
 */
public interface SingleValueObserver<T> {
    public void update(T value);
}
