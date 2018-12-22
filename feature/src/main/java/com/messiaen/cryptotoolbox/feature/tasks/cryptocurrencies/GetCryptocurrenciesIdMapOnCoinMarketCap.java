package com.messiaen.cryptotoolbox.feature.tasks.cryptocurrencies;

import android.os.AsyncTask;

import com.messiaen.cryptotoolbox.feature.CryptoToolsApplication;
import com.messiaen.cryptotoolbox.feature.api.cmc.CoinMarketCap;
import com.messiaen.cryptotoolbox.feature.api.cmc.results.CryptocurrenciesIDMapDTO;
import com.messiaen.cryptotoolbox.feature.api.cmc.services.CryptocurrenciesService;
import com.messiaen.cryptotoolbox.feature.data.cryptocurrency.CryptocurrenciesManager;
import com.messiaen.cryptotoolbox.feature.persistence.ApiCallHistory;
import com.messiaen.cryptotoolbox.feature.persistence.DatabaseManager;
import com.messiaen.cryptotoolbox.feature.persistence.dao.ApiCallHistoryDao;

import java.io.IOException;

import retrofit2.Response;

public class GetCryptocurrenciesIdMapOnCoinMarketCap extends AsyncTask<Void, Void, Void> {

    @Override
    protected Void doInBackground(Void... voids) {
        try {
            Response<CryptocurrenciesIDMapDTO> response =
                    CoinMarketCap
                            .getService(CryptocurrenciesService.class)
                            .getCryptocurrenciesIdMap(CryptoToolsApplication.CMC_API_KEY)
                            .execute();

            if (response.isSuccessful()) {
                if (response.body() == null)
                    return null;

                ApiCallHistoryDao apiCallHistoryDao = DatabaseManager.getDatabase().apiCallHistoryDao();
                ApiCallHistory history = apiCallHistoryDao.findById(
                        CoinMarketCap.MAP_ALL_CRYPTOCURRENCIES);

                if (history == null) {
                    apiCallHistoryDao.insert(new ApiCallHistory(
                            CoinMarketCap.MAP_ALL_CRYPTOCURRENCIES,
                            response.body().getStatus().getTimestamp()));
                } else {
                    history.setLastCall(response.body().getStatus().getTimestamp());
                    apiCallHistoryDao.update(history);
                }

                CryptocurrenciesManager.getInstance().onUpdate(response.body().getData(),
                        CryptocurrenciesManager.MAP);

                return null;
            } else {
                if (response.errorBody() == null)
                    return null;

                CryptocurrenciesManager.getInstance().onListCryptocurrenciesFailed(response.code(),
                        response.errorBody().string(), CryptocurrenciesManager.MAP);
                return null;
            }
        } catch (IOException e) {
            CryptocurrenciesManager.getInstance().onListCryptocurrenciesFailed(0, e.getMessage(),
                    CryptocurrenciesManager.MAP);
            return null;
        }
    }
}
