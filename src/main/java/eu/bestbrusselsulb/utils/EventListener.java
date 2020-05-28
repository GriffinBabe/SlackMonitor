package eu.bestbrusselsulb.utils;

import eu.bestbrusselsulb.utils.EventEmitter.EventType;

public interface EventListener {

    public void onEvent(Object emitter, EventType type, Object... args);
}
