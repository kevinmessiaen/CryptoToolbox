package com.messiaen.cryptotoolbox.feature.tasks.cryptocurrencies;

import android.os.AsyncTask;

import com.messiaen.cryptotoolbox.feature.CryptoToolsApplication;
import com.messiaen.cryptotoolbox.feature.api.cmc.CoinMarketCap;
import com.messiaen.cryptotoolbox.feature.api.cmc.queries.IdsQueryParameters;
import com.messiaen.cryptotoolbox.feature.api.cmc.results.CryptocurrenciesMetadataDTO;
import com.messiaen.cryptotoolbox.feature.api.cmc.services.CryptocurrenciesService;
import com.messiaen.cryptotoolbox.feature.data.cryptocurrency.CryptocurrenciesManager;

import java.io.IOException;

import androidx.annotation.NonNull;
import retrofit2.Response;

public class GatherCryptocurrenciesMetadataOnCoinMarketCap extends AsyncTask<Void, Void, Void> {

    private IdsQueryParameters query;

    public GatherCryptocurrenciesMetadataOnCoinMarketCap(@NonNull IdsQueryParameters query) {
        this.query = query;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        try {
            Response<CryptocurrenciesMetadataDTO> response =
                    CoinMarketCap
                            .getService(CryptocurrenciesService.class)
                            .getMetadata(CryptoToolsApplication.CMC_API_KEY, query.getId())
                            .execute();

            if (response.isSuccessful()) {
                if (response.body() == null)
                    return null;

                CryptocurrenciesManager.getInstance().onUpdate(response.body().getData().values(),
                        CryptocurrenciesManager.METADATA);

                return null;
            } else {
                if (response.errorBody() == null)
                    return null;

                CryptocurrenciesManager.getInstance().onListCryptocurrenciesFailed(response.code(),
                        response.errorBody().string(), CryptocurrenciesManager.METADATA);
                return null;
            }
        } catch (IOException e) {
            CryptocurrenciesManager.getInstance().onListCryptocurrenciesFailed(0, e.getMessage(),
                    CryptocurrenciesManager.METADATA);
            return null;
        }
    }
}
