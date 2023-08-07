package legitish.events;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventBus {
    private final Map<Class<? extends Event>, List<EventSubscribers>> eventListeners;

    public EventBus() {
        eventListeners = new HashMap<>();
    }

    public void subscribe(final Object subscriber) {
        Class<?> subscriberClass = subscriber.getClass();
        Method[] methods = subscriberClass.getMethods();

        for (Method method : methods) {
            if (method.isAnnotationPresent(Subscribe.class) && method.getParameterCount() == 1) {
                Class<? extends Event> eventType = method.getAnnotation(Subscribe.class).eventType();
                if (Event.class.isAssignableFrom(eventType)) {
                    eventListeners.computeIfAbsent(eventType, key -> new ArrayList<>()).add(new EventSubscribers(subscriber, method));
                }
            }
        }
    }

    public void unsubscribe(final Object subscriber) {
        for (List<EventSubscribers> wrappers : eventListeners.values()) {
            wrappers.removeIf(wrapper -> wrapper.subscriber == subscriber);
        }
    }

    public void call(final Event event) {
        Class<? extends Event> eventClass = event.getClass();
        List<EventSubscribers> wrappers = eventListeners.get(eventClass);

        if (wrappers != null) {
            for (EventSubscribers wrapper : wrappers) {
                try {
                    wrapper.method.invoke(wrapper.subscriber, event);
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        }
    }

    private static class EventSubscribers {
        private final Object subscriber;
        private final Method method;
        private EventSubscribers(Object subscriber, Method method) {
            this.subscriber = subscriber;
            this.method = method;
        }
    }
}
