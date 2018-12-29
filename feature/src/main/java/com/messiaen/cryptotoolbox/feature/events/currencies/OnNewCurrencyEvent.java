package com.messiaen.cryptotoolbox.feature.events.currencies;

import com.messiaen.cryptotoolbox.feature.data.models.Currency;

import java.util.List;

import androidx.annotation.NonNull;

public class OnNewCurrencyEvent {

    @NonNull
    private List<Currency> modified;

    public OnNewCurrencyEvent(@NonNull List<Currency> modified) {
        this.modified = modified;
    }

    @NonNull
    public List<Currency> getModified() {
        return modified;
    }

}
