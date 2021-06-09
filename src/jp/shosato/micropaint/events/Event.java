package jp.shosato.micropaint.events;

import jp.shosato.micropaint.components.BasicComponent;

/**
 * イベント基底クラス
 */
public class Event {
    private BasicComponent target;
    private BasicComponent currentTarget;
    private boolean cancelled = false;

    public void cancel() {
        this.cancelled = true;
    }

    public boolean cancelled() {
        return this.cancelled;
    }

    public void setTarget(BasicComponent target) {
        this.target = target;
    }

    public void setCurrentTarget(BasicComponent target) {
        this.currentTarget = target;
    }

    public BasicComponent getTarget() {
        return target;
    }

    public BasicComponent getCurrentTarget() {
        return currentTarget;
    }
}
