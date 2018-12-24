package com.messiaen.cryptotoolbox.feature.tasks.filters;

import android.annotation.TargetApi;
import android.os.AsyncTask;
import android.os.Build;

import com.messiaen.cryptotoolbox.feature.persistence.entities.CryptocurrencyHolder;
import com.messiaen.cryptotoolbox.feature.ui.adapters.CryptocurrenciesRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CryptocurrenciesFilteringTask
        extends AsyncTask<String, Void, List<CryptocurrencyHolder>> {

    private CryptocurrenciesRecyclerViewAdapter adapter;

    public CryptocurrenciesFilteringTask(CryptocurrenciesRecyclerViewAdapter adapter) {
        this.adapter = adapter;
    }

    @Override
    protected List<CryptocurrencyHolder> doInBackground(String... strings) {
        List<CryptocurrencyHolder> filtered;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            filtered = performFiltering(adapter.getCryptocurrencies(),
                    Arrays.stream(strings).map(String::toUpperCase).collect(Collectors.toList()));
        else
            filtered = performFiltering(adapter.getCryptocurrencies(), strings);

        return filtered;
    }

    @Override
    protected void onPostExecute(List<CryptocurrencyHolder> holders) {
        if (holders != null) {
            adapter.setFiltered(holders);
            adapter.notifyDataSetChanged();
        }
    }

    @TargetApi(Build.VERSION_CODES.N)
    private List<CryptocurrencyHolder> performFiltering(List<CryptocurrencyHolder> holders,
                                                        final List<String> filters) {

        List<CryptocurrencyHolder> filtered = holders.stream()
                .filter(holder -> holder.filter(filters.stream())).collect(Collectors.toList());

        return isCancelled() ? null : filtered;
    }

    private List<CryptocurrencyHolder> performFiltering(List<CryptocurrencyHolder> holders,
                                                        String... filters) {
        String[] uperCaseFilters = new String[filters.length];
        for (int i = 0; i < filters.length; i++)
            uperCaseFilters[i] = filters[i].toUpperCase();

        List<CryptocurrencyHolder> filtered = new ArrayList<>();
        for (CryptocurrencyHolder holder : holders) {
            if (holder.filter(uperCaseFilters))
                filtered.add(holder);
            if (isCancelled())
                return null;
        }

        return filtered;
    }
}
