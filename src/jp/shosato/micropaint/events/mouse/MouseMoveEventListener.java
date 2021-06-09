package jp.shosato.micropaint.events.mouse;

public interface MouseMoveEventListener extends MouseEventListener {
    public void onMouseMoved(final MouseEvent event);

    public void onMouseMoved(final MouseEvent event, boolean captureing);
}
