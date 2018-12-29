package com.messiaen.cryptotoolbox.feature.events.coins;

import com.messiaen.cryptotoolbox.feature.data.models.Coin;

import androidx.annotation.NonNull;

public class OnCoinUpdatedEvent {

    @NonNull
    private final Coin coin;

    public OnCoinUpdatedEvent(@NonNull Coin coin) {
        this.coin = coin;
    }

    @NonNull
    public Coin getCoin() {
        return coin;
    }
}
