package com.messiaen.cryptotoolbox.feature.events.cryptocurrencies;

import com.messiaen.cryptotoolbox.feature.persistence.entities.CryptocurrencyHolderUpdater;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class CryptocurrenciesUpdatedEvent {

    private final Collection<? extends CryptocurrencyHolderUpdater> modified;

    public CryptocurrenciesUpdatedEvent(Collection<? extends CryptocurrencyHolderUpdater> modified) {
        this.modified = modified;
    }

    @SafeVarargs
    public <T extends CryptocurrencyHolderUpdater> CryptocurrenciesUpdatedEvent(T... modified) {
        List<T> items = new ArrayList<T>();
        Collections.addAll(items, modified);
        this.modified = items;
    }

    public Collection<? extends CryptocurrencyHolderUpdater> getModified() {
        return modified;
    }
}
