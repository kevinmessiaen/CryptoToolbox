package com.messiaen.cryptotoolbox.feature.events.coingecko;

public class OnCoingeckoStatusEvent {

    private final boolean status;
    private final long retry;

    public OnCoingeckoStatusEvent(boolean status) {
        this.status = status;
        this.retry = 5000;
    }

    public OnCoingeckoStatusEvent(boolean status, long retry) {
        this.status = status;
        this.retry = retry;
    }

    public boolean getStatus() {
        return status;
    }

    public long getRetry() {
        return retry;
    }
}
