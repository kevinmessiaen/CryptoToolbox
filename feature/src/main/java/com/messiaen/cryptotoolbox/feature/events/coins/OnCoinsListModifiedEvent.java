package com.messiaen.cryptotoolbox.feature.events.coins;

import com.messiaen.cryptotoolbox.feature.data.models.Coin;

import java.util.List;

import androidx.annotation.NonNull;

public class OnCoinsListModifiedEvent {

    @NonNull
    private final List<Coin> list;

    public OnCoinsListModifiedEvent(@NonNull List<Coin> list) {
        this.list = list;
    }

    @NonNull
    public List<Coin> getList() {
        return list;
    }
}
