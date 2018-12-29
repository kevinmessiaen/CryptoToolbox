package com.messiaen.cryptotoolbox.feature.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.messiaen.cryptotoolbox.feature.R;
import com.messiaen.cryptotoolbox.feature.data.models.Coin;
import com.messiaen.cryptotoolbox.feature.events.requests.OnCoinLogoRequestedEvent;
import com.messiaen.cryptotoolbox.feature.events.requests.OnPricesRequestedEvent;
import com.messiaen.cryptotoolbox.feature.services.CurrenciesManager;
import com.messiaen.cryptotoolbox.feature.tasks.filters.CryptocurrenciesFilteringTask;
import com.messiaen.cryptotoolbox.feature.ui.holders.CoinListingPriceViewHolder;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CoinListingPriceRecyclerViewAdapter extends
        RecyclerView.Adapter<CoinListingPriceViewHolder> {

    private Context context;
    private final List<Coin> cryptocurrencies;
    private List<Coin> filtered;

    private CryptocurrenciesFilteringTask filterTask;
    private String[] filters;

    private List<CoinListingPriceViewHolder> visible = new ArrayList<>();

    private Timer timer = new Timer();

    public CoinListingPriceRecyclerViewAdapter(Context context, List<Coin> items) {
        this.context = context;
        cryptocurrencies = items;
        filtered = cryptocurrencies;
    }

    @Override
    @NonNull
    public CoinListingPriceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.coin_listing_price, parent, false);
        return new CoinListingPriceViewHolder(context, view);
    }

    @Override
    public void onBindViewHolder(@NonNull final CoinListingPriceViewHolder holder, int position) {
        holder.setFilters(filters);
        holder.onDataChanged(filtered.get(position), position > 0 ? filtered.get(position - 1) : null, null);

        if (filtered.get(position).getLogo() == null)
            EventBus.getDefault().post(
                    new OnCoinLogoRequestedEvent(filtered.get(position)));

        String currency = CurrenciesManager.getInstance().getLocal().getName();
        if (filtered.get(position).shouldUpdatePrice(currency)) {
            List<String> coins = new ArrayList<>();
            int i = position;
            while (i < filtered.size() && coins.size() < 50) {
                if (filtered.get(i).shouldUpdatePrice(currency))
                    coins.add(filtered.get(i).getId());
                i++;
            }

            if (!coins.isEmpty())
                EventBus.getDefault().post(new OnPricesRequestedEvent(coins));
        }
    }

    @Override
    public void onViewAttachedToWindow(@NonNull CoinListingPriceViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        holder.onAttach();
        visible.add(holder);
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull CoinListingPriceViewHolder holder) {
        visible.remove(holder);
        holder.onDetach();
        super.onViewDetachedFromWindow(holder);
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                List<String> coins = new ArrayList<>();
                for (CoinListingPriceViewHolder holder : visible)
                    if (holder.getItem() != null)
                        coins.add(holder.getItem().getId());
                if (!coins.isEmpty())
                    EventBus.getDefault().post(new OnPricesRequestedEvent(coins));
            }
        }, 0, 5 * 1000);
    }

    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        timer.cancel();
        super.onDetachedFromRecyclerView(recyclerView);
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

    public List<Coin> getCoins() {
        return cryptocurrencies;
    }

    public void setFiltered(List<Coin> filtered) {
        this.filtered = filtered;
    }

}
