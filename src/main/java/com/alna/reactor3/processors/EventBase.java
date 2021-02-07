package com.alna.reactor3.processors;

import java.util.UUID;

public class EventBase {

    private UUID eventUUID = UUID.randomUUID();

    public UUID getEventUUID() {
        return eventUUID;
    }

    @Override
    public String toString() {
        return "EventBase{" +
                "eventUUID=" + eventUUID +
                '}';
    }
}
