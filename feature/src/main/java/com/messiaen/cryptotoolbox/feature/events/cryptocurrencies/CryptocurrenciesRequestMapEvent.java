package com.messiaen.cryptotoolbox.feature.events.cryptocurrencies;

public class CryptocurrenciesRequestMapEvent {

    private final boolean online;

    public CryptocurrenciesRequestMapEvent() {
        this(false);
    }

    public CryptocurrenciesRequestMapEvent(boolean online) {
        this.online = online;
    }

    public boolean isOnline() {
        return online;
    }
}
