package com.messiaen.cryptotoolbox.feature.events.coins;

import com.messiaen.cryptotoolbox.feature.data.entities.CoinId;

import java.util.List;

import androidx.annotation.NonNull;

public class OnNewCoinIdEvent {

    @NonNull
    private List<CoinId> modified;

    public OnNewCoinIdEvent(@NonNull List<CoinId> modified) {
        this.modified = modified;
    }

    @NonNull
    public List<CoinId> getModified() {
        return modified;
    }

}
