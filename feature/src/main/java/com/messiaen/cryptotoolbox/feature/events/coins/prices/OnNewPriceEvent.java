package com.messiaen.cryptotoolbox.feature.events.coins.prices;

import com.messiaen.cryptotoolbox.feature.data.entities.Price;

import java.util.List;

import androidx.annotation.NonNull;

public class OnNewPriceEvent {

    @NonNull
    private List<Price> values;

    public OnNewPriceEvent(@NonNull List<Price> values) {
        this.values = values;
    }

    @NonNull
    public List<Price> getValues() {
        return values;
    }


}
