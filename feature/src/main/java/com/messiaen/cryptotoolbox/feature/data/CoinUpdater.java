package com.messiaen.cryptotoolbox.feature.data;

import com.messiaen.cryptotoolbox.feature.data.models.Coin;

import androidx.annotation.NonNull;

public interface CoinUpdater {

    @NonNull
    String getId();
    void update(@NonNull Coin coin);

}
