package com.messiaen.cryptotoolbox.feature.events.cryptocurrencies;

import com.messiaen.cryptotoolbox.feature.persistence.entities.CryptocurrencyHolder;

public class CryptocurrencyClickEvent {

    private final CryptocurrencyHolder holder;
    private final Type type;

    public CryptocurrencyClickEvent(CryptocurrencyHolder holder, Type type) {
        this.holder = holder;
        this.type = type;
    }

    public CryptocurrencyHolder getHolder() {
        return holder;
    }

    public Type getType() {
        return type;
    }

    public enum Type {
        FAVORITE, BUY, NOTIFY, CHART, DETAILS
    }
}
