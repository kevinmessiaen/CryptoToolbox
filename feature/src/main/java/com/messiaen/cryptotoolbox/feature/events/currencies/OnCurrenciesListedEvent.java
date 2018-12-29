package com.messiaen.cryptotoolbox.feature.events.currencies;

import com.messiaen.cryptotoolbox.feature.data.models.Currency;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;

public class OnCurrenciesListedEvent {

    @NonNull
    private final List<Currency> currencies;

    @NonNull
    private final Source source;

    public OnCurrenciesListedEvent(@NonNull List<Currency> currencies, @NonNull Source source) {
        this.currencies = currencies;
        this.source = source;

    }

    @NonNull
    public List<Currency> getCurrencies() {
        return currencies;
    }

    @NonNull
    public Source getSource() {
        return source;
    }

    public enum Source {
        DATABASE, COINGECKO
    }
}
