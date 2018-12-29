package com.messiaen.cryptotoolbox.feature.events.coins;

import android.view.View;

import com.messiaen.cryptotoolbox.feature.data.models.Coin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class OnCoinClickedEvent {

    @NonNull
    private final Coin coin;

    @NonNull
    private final Type type;

    @Nullable
    private final View view;

    public OnCoinClickedEvent(@NonNull Coin coin, @NonNull Type type) {
        this.coin = coin;
        this.type = type;
        view = null;
    }

    public OnCoinClickedEvent(@NonNull Coin coin, @NonNull Type type, @Nullable View view) {
        this.coin = coin;
        this.type = type;
        this.view = view;
    }

    @NonNull
    public Coin getCoin() {
        return coin;
    }

    @NonNull
    public Type getType() {
        return type;
    }

    @Nullable
    public View getView() {
        return view;
    }

    public enum Type {
        FAVORITE, BUY, NOTIFY, CHART, DETAILS
    }
}
