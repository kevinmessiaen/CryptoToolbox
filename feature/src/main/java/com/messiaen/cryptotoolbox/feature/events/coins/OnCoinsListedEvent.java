package com.messiaen.cryptotoolbox.feature.events.coins;

import com.messiaen.cryptotoolbox.feature.data.entities.CoinId;

import java.util.List;

import androidx.annotation.NonNull;

public class OnCoinsListedEvent {

    @NonNull
    private final List<CoinId> coins;

    @NonNull
    private final Source source;

    public OnCoinsListedEvent(@NonNull List<CoinId> coins, @NonNull Source source) {
        this.coins = coins;
        this.source = source;
    }

    @NonNull
    public List<CoinId> getCoins() {
        return coins;
    }

    @NonNull
    public Source getSource() {
        return source;
    }

    public enum Source {
        DATABASE, COINGECKO
    }

}
