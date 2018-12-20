package com.messiaen.cryptotoolbox.feature.listeners;

import com.messiaen.cryptotoolbox.feature.data.cryptocurrency.CryptocurrencyHolder;

import java.util.List;

public interface OnRequestCryptocurrencyUpdateListener {

    void requestCryptocurrencyMetadataUpdate(int pos, List<CryptocurrencyHolder> holders);

    void requestCryptocurrencyQuotesUpdate(int pos, List<CryptocurrencyHolder> holders);

}
