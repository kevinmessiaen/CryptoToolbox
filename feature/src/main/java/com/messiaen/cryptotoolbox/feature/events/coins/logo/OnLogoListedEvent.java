package com.messiaen.cryptotoolbox.feature.events.coins.logo;

import com.messiaen.cryptotoolbox.feature.data.entities.Logo;
import com.messiaen.cryptotoolbox.feature.events.coins.prices.OnPricesListedEvent;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;

public class OnLogoListedEvent {

    @NonNull
    private final List<Logo> logos;

    @NonNull
    private final Source source;

    public OnLogoListedEvent(@NonNull Logo logo, Source source) {
        logos = new ArrayList<>();
        logos.add(logo);
        this.source = source;
    }

    public OnLogoListedEvent(@NonNull List<Logo> logos, Source source) {
        this.logos = logos;
        this.source = source;
    }

    @NonNull
    public List<Logo> getLogos() {
        return logos;
    }

    @NonNull
    public Source getSource() {
        return source;
    }

    public enum Source {
        DATABASE, COINGECKO
    }
}
