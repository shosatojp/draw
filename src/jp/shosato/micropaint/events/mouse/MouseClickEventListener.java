package jp.shosato.micropaint.events.mouse;

public interface MouseClickEventListener extends MouseEventListener {
    public void onMouseClicked(final MouseEvent event);

    public void onMouseClicked(final MouseEvent event, boolean captureing);
}
