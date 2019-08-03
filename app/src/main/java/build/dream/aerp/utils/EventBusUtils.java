package build.dream.aerp.utils;

import org.greenrobot.eventbus.EventBus;

import build.dream.aerp.eventbus.EventBusEvent;

public class EventBusUtils {
    public static void register(Object object) {
        EventBus eventBus = EventBus.getDefault();
        if (!eventBus.isRegistered(object)) {
            eventBus.register(object);
        }
    }

    public static void unregister(Object object) {
        EventBus eventBus = EventBus.getDefault();
        if (eventBus.isRegistered(object)) {
            eventBus.unregister(object);
        }
    }

    public static void post(EventBusEvent eventBusEvent) {
        EventBus.getDefault().post(eventBusEvent);
    }
}
