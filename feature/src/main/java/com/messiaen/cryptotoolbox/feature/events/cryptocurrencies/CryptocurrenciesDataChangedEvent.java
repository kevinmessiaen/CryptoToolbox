package com.messiaen.cryptotoolbox.feature.events.cryptocurrencies;

import com.messiaen.cryptotoolbox.feature.persistence.entities.CryptocurrencyHolder;

import java.util.List;

import androidx.annotation.NonNull;

public class CryptocurrenciesDataChangedEvent {

    private final List<CryptocurrencyHolder> modified;
    private final List<CryptocurrencyHolder> data;

    public CryptocurrenciesDataChangedEvent(List<CryptocurrencyHolder> modified,
                                            @NonNull List<CryptocurrencyHolder> data) {
        this.modified = modified;
        this.data = data;
    }

    public List<CryptocurrencyHolder> getModified() {
        return modified;
    }

    public List<CryptocurrencyHolder> getData() {
        return data;
    }
}
