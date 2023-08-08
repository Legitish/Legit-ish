package legitish.events.impl;

import legitish.events.Event;

public class LivingUpdateEvent extends Event {
    public Type type;

    public LivingUpdateEvent(Type type) {
        this.type = type;
    }

    public enum Type {
        PRE,
        POST
    }
}
