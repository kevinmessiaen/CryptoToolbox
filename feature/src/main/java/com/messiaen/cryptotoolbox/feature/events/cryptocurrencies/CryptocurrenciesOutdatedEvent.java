package com.messiaen.cryptotoolbox.feature.events.cryptocurrencies;

import com.messiaen.cryptotoolbox.feature.persistence.entities.CryptocurrencyHolder;
import com.messiaen.cryptotoolbox.feature.manager.CoinMarketCapManager.DataType;

import java.util.List;

import androidx.annotation.NonNull;

public class CryptocurrenciesOutdatedEvent {

    private final int pos;
    private final List<CryptocurrencyHolder> holders;
    private final DataType dataType;

    public CryptocurrenciesOutdatedEvent(int pos, @NonNull List<CryptocurrencyHolder> holders,
                                         @NonNull DataType dataType) {
        this.pos = pos;
        this.holders = holders;
        this.dataType = dataType;
    }

    public int getPos() {
        return pos;
    }

    public List<CryptocurrencyHolder> getHolders() {
        return holders;
    }

    public DataType getDataType() {
        return dataType;
    }

}
