package com.messiaen.cryptotoolbox.feature.events.coins.prices;

import com.messiaen.cryptotoolbox.feature.data.models.Coin;

import java.util.List;

import androidx.annotation.NonNull;

public class OnPriceModifiedEvent {

    @NonNull
    private List<Coin> modified;

    public OnPriceModifiedEvent(@NonNull List<Coin> modified) {
        this.modified = modified;
    }

    @NonNull
    public List<Coin> getModified() {
        return modified;
    }

}
