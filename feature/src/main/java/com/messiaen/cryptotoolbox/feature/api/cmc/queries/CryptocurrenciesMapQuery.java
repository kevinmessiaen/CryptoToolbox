package com.messiaen.cryptotoolbox.feature.api.cmc.queries;

import com.messiaen.cryptotoolbox.feature.CryptoToolsApplication;
import com.messiaen.cryptotoolbox.feature.api.cmc.results.CryptocurrenciesIDMapDTO;
import com.messiaen.cryptotoolbox.feature.api.cmc.services.CryptocurrenciesService;
import com.messiaen.cryptotoolbox.feature.events.cryptocurrencies.CryptocurrenciesUpdatedEvent;
import com.messiaen.cryptotoolbox.feature.events.error.NetworkErrorEvent;
import com.messiaen.cryptotoolbox.feature.manager.CoinMarketCapManager;
import com.messiaen.cryptotoolbox.feature.persistence.entities.ApiCallHistory;
import com.messiaen.cryptotoolbox.feature.persistence.DatabaseManager;
import com.messiaen.cryptotoolbox.feature.persistence.dao.ApiCallHistoryDao;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;

import retrofit2.Response;

public class CryptocurrenciesMapQuery extends Query {

    @Override
    public void execute() {
        try {
            Response<CryptocurrenciesIDMapDTO> response =
                    CoinMarketCapManager
                            .getService(CryptocurrenciesService.class)
                            .getCryptocurrenciesIdMap(CryptoToolsApplication.CMC_API_KEY)
                            .execute();

            if (response.isSuccessful()) {
                if (response.body() == null)
                    return;

                ApiCallHistoryDao apiCallHistoryDao = DatabaseManager.getDatabase().apiCallHistoryDao();
                ApiCallHistory history = apiCallHistoryDao.findById(
                        CoinMarketCapManager.MAP_ALL_CRYPTOCURRENCIES);

                if (history == null) {
                    apiCallHistoryDao.insert(new ApiCallHistory(
                            CoinMarketCapManager.MAP_ALL_CRYPTOCURRENCIES,
                            response.body().getStatus().getTimestamp()));
                } else {
                    history.setLastCall(response.body().getStatus().getTimestamp());
                    apiCallHistoryDao.update(history);
                }

                EventBus.getDefault().post(new CryptocurrenciesUpdatedEvent(response.body().getData()));
            } else {
                if (response.errorBody() == null)
                    return;

                EventBus.getDefault().post(new NetworkErrorEvent(response.code(),
                        response.errorBody().string()));
            }
        } catch (IOException e) {
            EventBus.getDefault().post(new NetworkErrorEvent(0, e.getMessage()));
        }
    }
}
