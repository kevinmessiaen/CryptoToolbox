package com.messiaen.cryptotoolbox.feature.events.error;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class OnNetworkErrorEvent {

    private final int code;

    @NonNull
    private final String message;

    @Nullable
    private final Class<?> clazz;

    public OnNetworkErrorEvent(int code, @NonNull String message) {
        this.code = code;
        this.message = message;
        this.clazz = null;
    }

    public OnNetworkErrorEvent(int code, @NonNull String message, @Nullable Class<?> clazz) {
        this.code = code;
        this.message = message;
        this.clazz = clazz;
    }

    public int getCode() {
        return code;
    }

    @NonNull
    public String getMessage() {
        return message;
    }

    @Nullable
    public Class<?> getClazz() {
        return clazz;
    }
}
