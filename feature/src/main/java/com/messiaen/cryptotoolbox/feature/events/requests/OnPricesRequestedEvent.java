package com.messiaen.cryptotoolbox.feature.events.requests;

import com.messiaen.cryptotoolbox.feature.data.models.Coin;

import java.util.List;

import androidx.annotation.NonNull;

public class OnPricesRequestedEvent {

    @NonNull
    private final List<String> coins;

    public OnPricesRequestedEvent(@NonNull List<String> coins) {
        this.coins = coins;
    }

    @NonNull
    public List<String> getCoins() {
        return coins;
    }

}
