package com.messiaen.cryptotoolbox.feature.data.cryptocurrency;

import java.util.List;

import androidx.annotation.NonNull;

public interface OnCryptocurrenciesDataEventListener {

    public void onCryptocurrenciesChanged(@NonNull List<CryptocurrencyHolder> cryptocurrencies);

    public void onNetworkError(int code, @NonNull String message);

}
