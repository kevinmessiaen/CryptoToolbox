package com.messiaen.cryptotoolbox.feature.events.currencies;

import com.messiaen.cryptotoolbox.feature.data.models.Currency;

import java.util.List;

import androidx.annotation.NonNull;

public class OnCurrenciesListModifiedEvent {

    @NonNull
    private final Currency local;

    @NonNull
    private final List<Currency> list;

    public OnCurrenciesListModifiedEvent(@NonNull Currency local, @NonNull List<Currency> list) {
        this.local = local;
        this.list = list;
    }

    @NonNull
    public Currency getLocal() {
        return local;
    }

    @NonNull
    public List<Currency> getList() {
        return list;
    }
}