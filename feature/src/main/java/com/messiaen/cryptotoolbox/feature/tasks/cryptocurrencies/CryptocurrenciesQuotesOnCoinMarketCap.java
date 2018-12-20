package com.messiaen.cryptotoolbox.feature.tasks.cryptocurrencies;

import android.os.AsyncTask;

import com.messiaen.cryptotoolbox.feature.CryptoToolsApplication;
import com.messiaen.cryptotoolbox.feature.api.cmc.CoinMarketCap;
import com.messiaen.cryptotoolbox.feature.api.cmc.queries.IdsQueryParameters;
import com.messiaen.cryptotoolbox.feature.api.cmc.results.CryptocurrenciesQuotesDTO;
import com.messiaen.cryptotoolbox.feature.api.cmc.services.CryptocurrenciesService;
import com.messiaen.cryptotoolbox.feature.data.cryptocurrency.CryptocurrenciesManager;

import java.io.IOException;

import androidx.annotation.NonNull;
import retrofit2.Response;

public class CryptocurrenciesQuotesOnCoinMarketCap extends AsyncTask<Void, Void, Void> {

    private IdsQueryParameters query;

    public CryptocurrenciesQuotesOnCoinMarketCap(@NonNull IdsQueryParameters query) {
        this.query = query;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        try {
            Response<CryptocurrenciesQuotesDTO> response =
                    CoinMarketCap
                            .getService(CryptocurrenciesService.class)
                            .getQuotes(CryptoToolsApplication.CMC_API_KEY,
                                    query.getId())
                            .execute();

            if (response.isSuccessful()) {
                if (response.body() == null)
                    return null;

                CryptocurrenciesManager.getInstance().updateQuotes(response.body().getData());

                return null;
            } else {
                if (response.errorBody() == null)
                    return null;

                CryptocurrenciesManager.getInstance().onListCryptocurrenciesFailed(response.code(),
                        response.errorBody().string());
                return null;
            }
        } catch (IOException e) {
            CryptocurrenciesManager.getInstance().onListCryptocurrenciesFailed(0, e.getMessage());
            return null;
        }
    }
}