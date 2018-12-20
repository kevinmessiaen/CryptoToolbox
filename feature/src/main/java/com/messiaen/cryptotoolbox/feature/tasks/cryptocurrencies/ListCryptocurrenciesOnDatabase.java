package com.messiaen.cryptotoolbox.feature.tasks.cryptocurrencies;

import android.os.AsyncTask;

import com.messiaen.cryptotoolbox.feature.api.cmc.CoinMarketCap;
import com.messiaen.cryptotoolbox.feature.data.cryptocurrency.CryptocurrenciesManager;
import com.messiaen.cryptotoolbox.feature.data.cryptocurrency.CryptocurrencyHolder;
import com.messiaen.cryptotoolbox.feature.persistence.ApiCallHistory;
import com.messiaen.cryptotoolbox.feature.persistence.DatabaseManager;

import java.util.List;

public class ListCryptocurrenciesOnDatabase extends AsyncTask<Void, Void, Void> {

    @Override
    protected Void doInBackground(Void... voids) {
        List<CryptocurrencyHolder> cryptocurrencies =
                DatabaseManager.getDatabase().cryptocurrencyHolderDao().findAll();

        if (cryptocurrencies != null && cryptocurrencies.size() > 0) {
            ApiCallHistory history =
                    DatabaseManager.getDatabase()
                            .apiCallHistoryDao()
                            .findById(CoinMarketCap.MAP_ALL_CRYPTOCURRENCIES);

            if (history == null || history.shouldUpdate(
                    CoinMarketCap.AUTO_UPDATE_CRYPTOCURRENCY_MAP_DELAY))
                CryptocurrenciesManager.getInstance().onRequestCryptocurrenciesUpdate();

            CryptocurrenciesManager.getInstance().onListCryptocurrencies(cryptocurrencies);
        }

        CryptocurrenciesManager.getInstance().onRequestCryptocurrenciesUpdate();
        return null;
    }
}
