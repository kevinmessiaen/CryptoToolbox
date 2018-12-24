package com.messiaen.cryptotoolbox.feature.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.messiaen.cryptotoolbox.feature.R;
import com.messiaen.cryptotoolbox.feature.events.cryptocurrencies.CryptocurrenciesOutdatedEvent;
import com.messiaen.cryptotoolbox.feature.manager.CoinMarketCapManager;
import com.messiaen.cryptotoolbox.feature.manager.CoinMarketCapManager.DataType;
import com.messiaen.cryptotoolbox.feature.persistence.entities.CryptocurrencyHolder;
import com.messiaen.cryptotoolbox.feature.tasks.filters.CryptocurrenciesFilteringTask;
import com.messiaen.cryptotoolbox.feature.ui.holders.CryptocurrencyListingPriceViewHolder;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CryptocurrenciesRecyclerViewAdapter extends
        RecyclerView.Adapter<CryptocurrencyListingPriceViewHolder> {

    private Context context;
    private final List<CryptocurrencyHolder> cryptocurrencies;
    private List<CryptocurrencyHolder> filtered;

    private CryptocurrenciesFilteringTask filterTask;
    private String[] filters;

    public CryptocurrenciesRecyclerViewAdapter(Context context, List<CryptocurrencyHolder> items) {
        this.context = context;
        cryptocurrencies = items;
        filtered = cryptocurrencies;
    }

    @Override
    @NonNull
    public CryptocurrencyListingPriceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cryptocurrency_listing_price, parent, false);
        return new CryptocurrencyListingPriceViewHolder(context, view);
    }

    @Override
    public void onBindViewHolder(@NonNull final CryptocurrencyListingPriceViewHolder holder, int position) {
        holder.setFilters(filters);
        holder.setData(filtered.get(position), position > 0 ? filtered.get(position - 1) : null, null);
        if (filtered.get(position).shouldUpdateMetadata(
                CoinMarketCapManager.AUTO_UPDATE_CRYPTOCURRENCY_METADATA_DELAY))
            EventBus.getDefault().post(
                    new CryptocurrenciesOutdatedEvent(position, filtered, DataType.METADATA));
        if (filtered.get(position).shouldUpdateQuotes(
                CoinMarketCapManager.AUTO_UPDATE_CRYPTOCURRENCY_QUOTES_DELAY))
            EventBus.getDefault().post(
                    new CryptocurrenciesOutdatedEvent(position, filtered, DataType.QUOTES));
    }

    @Override
    public int getItemCount() {
        return filtered.size();
    }

    public String[] getFilters() {
        return filters;
    }

    public void setFilters(String... filters) {
        this.filters = filters;

        if (filterTask != null) {
            filterTask.cancel(true);
            filterTask = null;
        }

        if (filters != null && filters.length > 0) {
            filterTask = new CryptocurrenciesFilteringTask(this);
            filterTask.execute(filters);
        } else {
            filtered = cryptocurrencies;
            notifyDataSetChanged();
        }
    }

    public List<CryptocurrencyHolder> getCryptocurrencies() {
        return cryptocurrencies;
    }

    public void setFiltered(List<CryptocurrencyHolder> filtered) {
        this.filtered = filtered;
    }

}
