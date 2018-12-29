package com.messiaen.cryptotoolbox.feature.events.coins.prices;

import com.messiaen.cryptotoolbox.feature.data.entities.Price;

import java.util.List;

import androidx.annotation.NonNull;

public class OnPricesListedEvent {

    @NonNull
    private final List<Price> prices;

    @NonNull
    private final Source source;

    public OnPricesListedEvent(@NonNull List<Price> prices, @NonNull Source source) {
        this.prices = prices;
        this.source = source;

    }

    @NonNull
    public List<Price> getPrices() {
        return prices;
    }

    @NonNull
    public Source getSource() {
        return source;
    }

    public enum Source {
        DATABASE, COINGECKO
    }

}
