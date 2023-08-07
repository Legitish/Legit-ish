package legitish.events.impl;

import legitish.events.Event;

public class MouseEvent extends Event {
    public final Button button;

    public MouseEvent(final Button button) {
        this.button = button;
    }

    public enum Button {
        LEFT,
        RIGHT
    }
}
