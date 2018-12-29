package com.messiaen.cryptotoolbox.feature.events.coins.logo;

import com.messiaen.cryptotoolbox.feature.data.entities.Logo;

import java.util.List;

import androidx.annotation.NonNull;

public class OnNewLogoEvent {

    @NonNull
    private List<Logo> logos;

    public OnNewLogoEvent(@NonNull List<Logo> logos) {
        this.logos = logos;
    }

    @NonNull
    public List<Logo> getLogos() {
        return logos;
    }

}
