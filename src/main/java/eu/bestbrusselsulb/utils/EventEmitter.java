package eu.bestbrusselsulb.utils;

import java.util.ArrayList;
import java.util.List;

public class EventEmitter {

    protected List<EventListener> listeners = new ArrayList<>();

    public void addListener(EventListener listener) {
        listeners.add(listener);
    }

    public void removeListener(EventListener listener) {
        listeners.remove(listener);
    }

    public void emit(EventType type, Object... args) {
        for (EventListener l : listeners) {
            l.onEvent(this, type, args);
        }
    }

    public enum EventType {

        QUIT_COMMAND("Quit command."),
        MESSAGE_RECEIVED("Message received.");

        private String name;

        EventType(String name) {
            this.name = name;
        }

    }

}
