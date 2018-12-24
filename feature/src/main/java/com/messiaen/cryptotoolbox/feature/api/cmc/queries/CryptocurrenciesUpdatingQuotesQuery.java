package com.messiaen.cryptotoolbox.feature.api.cmc.queries;

import com.messiaen.cryptotoolbox.feature.CryptoToolsApplication;
import com.messiaen.cryptotoolbox.feature.api.cmc.results.CryptocurrenciesQuotesDTO;
import com.messiaen.cryptotoolbox.feature.api.cmc.services.CryptocurrenciesService;
import com.messiaen.cryptotoolbox.feature.events.cryptocurrencies.CryptocurrenciesUpdatedEvent;
import com.messiaen.cryptotoolbox.feature.events.error.NetworkErrorEvent;
import com.messiaen.cryptotoolbox.feature.manager.CoinMarketCapManager;
import com.messiaen.cryptotoolbox.feature.utils.Currencies;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;

import retrofit2.Response;

public class CryptocurrenciesUpdatingQuotesQuery extends CryptocurrenciesUpdatingQuery {

    CryptocurrenciesUpdatingQuotesQuery(String id) {
        super(id);
    }

    @Override
    public void execute() {
        try {
            Response<CryptocurrenciesQuotesDTO> response =
                    CoinMarketCapManager
                            .getService(CryptocurrenciesService.class)
                            .getQuotes(CryptoToolsApplication.CMC_API_KEY, id,
                                    Currencies.getDefaultLocal().toString())
                            .execute();

            if (response.isSuccessful()) {
                if (response.body() == null)
                    return;

                EventBus.getDefault().post(new CryptocurrenciesUpdatedEvent(
                        response.body().getData().values()));
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
