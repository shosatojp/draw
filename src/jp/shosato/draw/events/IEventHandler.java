package jp.shosato.draw.events;

@FunctionalInterface
public interface IEventHandler<TEvent extends Event> {
    public void invoke(TEvent event);
}
