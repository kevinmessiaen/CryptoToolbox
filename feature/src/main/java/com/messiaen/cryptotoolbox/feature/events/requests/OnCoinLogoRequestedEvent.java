package com.messiaen.cryptotoolbox.feature.events.requests;

import com.messiaen.cryptotoolbox.feature.data.models.Coin;

import androidx.annotation.NonNull;

public class OnCoinLogoRequestedEvent {

    @NonNull
    private final Coin coin;

    public OnCoinLogoRequestedEvent(@NonNull Coin coin) {
        this.coin = coin;
    }

    @NonNull
    public Coin getCoin() {
        return coin;
    }

}
