package com.messiaen.cryptotoolbox.feature.tasks.filters;

import android.annotation.TargetApi;
import android.os.AsyncTask;
import android.os.Build;

import com.messiaen.cryptotoolbox.feature.data.models.Coin;
import com.messiaen.cryptotoolbox.feature.ui.adapters.CoinListingPriceRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CryptocurrenciesFilteringTask
        extends AsyncTask<String, Void, List<Coin>> {

    private CoinListingPriceRecyclerViewAdapter adapter;

    public CryptocurrenciesFilteringTask(CoinListingPriceRecyclerViewAdapter adapter) {
        this.adapter = adapter;
    }

    @Override
    protected List<Coin> doInBackground(String... strings) {
        List<Coin> filtered;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            filtered = performFiltering(adapter.getCoins(),
                    Arrays.stream(strings).map(String::toUpperCase).collect(Collectors.toList()));
        else
            filtered = performFiltering(adapter.getCoins(), strings);

        return filtered;
    }

    @Override
    protected void onPostExecute(List<Coin> coins) {
        if (coins != null) {
            adapter.setFiltered(coins);
            adapter.notifyDataSetChanged();
        }
    }

    @TargetApi(Build.VERSION_CODES.N)
    private List<Coin> performFiltering(List<Coin> coins,
                                        final List<String> filters) {
        synchronized (coins) {
            List<Coin> filtered = coins.stream()
                    .filter(coin -> coin.filter(filters.stream())).collect(Collectors.toList());

            return isCancelled() ? null : filtered;
        }
    }

    private List<Coin> performFiltering(List<Coin> coins,
                                        String... filters) {
        synchronized (coins) {
            String[] uperCaseFilters = new String[filters.length];
            for (int i = 0; i < filters.length; i++)
                uperCaseFilters[i] = filters[i].toUpperCase();

            List<Coin> filtered = new ArrayList<>();
            for (Coin coin : coins) {
                if (coin.filter(uperCaseFilters))
                    filtered.add(coin);
                if (isCancelled())
                    return null;
            }

            return filtered;
        }
    }
}
