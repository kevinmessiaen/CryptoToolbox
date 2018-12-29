package com.messiaen.cryptotoolbox.feature.events.coins.logo;

import com.messiaen.cryptotoolbox.feature.data.models.Coin;

import java.util.List;

import androidx.annotation.NonNull;

public class OnLogoModifiedEvent {

    @NonNull
    private List<Coin> modified;

    public OnLogoModifiedEvent(@NonNull List<Coin> modified) {
        this.modified = modified;
    }

    @NonNull
    public List<Coin> getModified() {
        return modified;
    }

}
