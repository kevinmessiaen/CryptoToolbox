package com.messiaen.cryptotoolbox.feature.api.cmc.queries;

import com.messiaen.cryptotoolbox.feature.CryptoToolsApplication;
import com.messiaen.cryptotoolbox.feature.api.cmc.results.CryptocurrenciesMetadataDTO;
import com.messiaen.cryptotoolbox.feature.api.cmc.services.CryptocurrenciesService;
import com.messiaen.cryptotoolbox.feature.events.cryptocurrencies.CryptocurrenciesUpdatedEvent;
import com.messiaen.cryptotoolbox.feature.events.error.NetworkErrorEvent;
import com.messiaen.cryptotoolbox.feature.manager.CoinMarketCapManager;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;

import retrofit2.Response;

public class CryptocurrenciesUpdatingMetadataQuery extends CryptocurrenciesUpdatingQuery {

    CryptocurrenciesUpdatingMetadataQuery(String id) {
        super(id);
    }

    @Override
    public void execute() {
        try {
            Response<CryptocurrenciesMetadataDTO> response =
                    CoinMarketCapManager
                            .getService(CryptocurrenciesService.class)
                            .getMetadata(CryptoToolsApplication.CMC_API_KEY, id)
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
