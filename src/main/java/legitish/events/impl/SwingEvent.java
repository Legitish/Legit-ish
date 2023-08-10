package legitish.events.impl;

import legitish.events.Event;

public class SwingEvent extends Event {
    public final Type type;
    public SwingEvent(final Type type) {
        this.type = type;
    }
    public enum Type {
        PRE,
        POST
    }
}
