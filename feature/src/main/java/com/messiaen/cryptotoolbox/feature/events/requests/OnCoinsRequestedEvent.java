package com.messiaen.cryptotoolbox.feature.events.requests;

public class OnCoinsRequestedEvent {

    private final boolean online;

    public OnCoinsRequestedEvent() {
        online = false;
    }

    public OnCoinsRequestedEvent(boolean isOnline) {
        this.online = isOnline;
    }

    public boolean isOnline() {
        return online;
    }
}
