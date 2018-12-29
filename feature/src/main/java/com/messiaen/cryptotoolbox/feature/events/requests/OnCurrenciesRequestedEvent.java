package com.messiaen.cryptotoolbox.feature.events.requests;

public class OnCurrenciesRequestedEvent {

    private final boolean online;

    public OnCurrenciesRequestedEvent() {
        online = false;
    }

    public OnCurrenciesRequestedEvent(boolean isOnline) {
        this.online = isOnline;
    }

    public boolean isOnline() {
        return online;
    }

}
