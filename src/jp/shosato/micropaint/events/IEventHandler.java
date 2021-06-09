package jp.shosato.micropaint.events;

@FunctionalInterface
public interface IEventHandler<TEvent extends Event> {
    public void invoke(TEvent event);
}
