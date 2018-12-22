package com.messiaen.cryptotoolbox.feature.ui.adapter;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.messiaen.cryptotoolbox.feature.R;
import com.messiaen.cryptotoolbox.feature.api.cmc.CoinMarketCap;
import com.messiaen.cryptotoolbox.feature.data.cryptocurrency.CryptocurrencyHolder;
import com.messiaen.cryptotoolbox.feature.data.cryptocurrency.Quote;
import com.messiaen.cryptotoolbox.feature.listeners.OnRequestCryptocurrencyUpdateListener;
import com.messiaen.cryptotoolbox.feature.tasks.filters.PerformCryptocurrenciesFiltering;
import com.messiaen.cryptotoolbox.feature.ui.fragment.CryptocurrenciesFragment.OnListFragmentInteractionListener;
import com.messiaen.cryptotoolbox.feature.utils.Currencies;
import com.messiaen.cryptotoolbox.feature.utils.Formatters;
import com.messiaen.cryptotoolbox.feature.utils.Highlight;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * {@link RecyclerView.Adapter} that can display a {@link CryptocurrencyHolder} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 */
public class CryptocurrenciesRecyclerViewAdapter extends
        RecyclerView.Adapter<CryptocurrenciesRecyclerViewAdapter.ViewHolder> {

    private final List<CryptocurrencyHolder> cryptocurrencies;
    private List<CryptocurrencyHolder> filtered;

    private PerformCryptocurrenciesFiltering filterTask;
    private String[] filters;

    private final NumberFormat currencyFormat;

    private final OnListFragmentInteractionListener listener;
    private OnRequestCryptocurrencyUpdateListener onRequestCryptocurrencyUpdateListener;

    public CryptocurrenciesRecyclerViewAdapter(List<CryptocurrencyHolder> items,
                                               OnListFragmentInteractionListener listener) {
        cryptocurrencies = items;
        filtered = cryptocurrencies;
        this.listener = listener;
        currencyFormat = Formatters.getCurrencyFormat(Currencies.getDefaultLocal().toString());
    }

    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_cryptocurency, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        holder.updateView(position);
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
            filterTask = new PerformCryptocurrenciesFiltering(this);
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

    public void setOnRequestCryptocurrencyUpdateListener(
            OnRequestCryptocurrencyUpdateListener onRequestCryptocurrencyUpdateListener) {
        this.onRequestCryptocurrencyUpdateListener = onRequestCryptocurrencyUpdateListener;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private final View view;
        private SimpleDraweeView logoSimpleDraweeView;
        private TextView nameTextView;
        private TextView sybolTextView;
        private TextView priceTextView;
        private LinearLayout headerLinearLayout;
        private LinearLayout bodyLinearLayout;
        private ImageView isFavoriteImageView;
        private TextView headerNameTextView;
        private LinearLayout menuLinearLayout;
        private LinearLayout actionBuyLinearLayout;
        private LinearLayout actionNotifyLinearLayout;
        private LinearLayout actionChartsLinearLayout;
        private LinearLayout actionDetailsLinearLayout;
        private CryptocurrencyHolder item;

        private ViewHolder(View view) {
            super(view);
            this.view = view;
            bindView();
        }

        private void bindView() {
            logoSimpleDraweeView = view.findViewById(R.id.cryptocurrency_logo);
            nameTextView = view.findViewById(R.id.cryptocurrency_name);
            sybolTextView = view.findViewById(R.id.cryptocurrency_symbol);
            priceTextView = view.findViewById(R.id.cryptocurrency_price);
            headerLinearLayout = view.findViewById(R.id.cryptocurrencies_section_heading);
            bodyLinearLayout = view.findViewById(R.id.cryptocurrencies_section_body);
            isFavoriteImageView = view.findViewById(R.id.cryptocurrencies_is_favorite);
            headerNameTextView = view.findViewById(R.id.cryptocurrency_header_name);
            menuLinearLayout = view.findViewById(R.id.cryptocurrencies_section_menu);
            actionBuyLinearLayout = view.findViewById(R.id.cryptocurrencies_action_buy);
            actionNotifyLinearLayout = view.findViewById(R.id.cryptocurrencies_action_notify);
            actionChartsLinearLayout = view.findViewById(R.id.cryptocurrencies_action_charts);
            actionDetailsLinearLayout = view.findViewById(R.id.cryptocurrencies_action_details);
        }

        private void updateView(int position) {
            item = filtered.get(position);

            setBody();
            setHeader(position == 0 ? null : filtered.get(position - 1));
            setActionMenu();

            if (onRequestCryptocurrencyUpdateListener != null)
                validate(position);
        }

        private void setBody() {
            Highlight.underlineString(nameTextView, item.getName(), filters);
            Highlight.underlineString(sybolTextView, item.getSymbol(), filters);

            Quote q;
            if (item.getQuote() != null &&
                    (q = item.getQuote().get(Currencies.getDefaultLocal().toString())) != null)
                priceTextView.setText(
                        currencyFormat.format(q.getPrice()));
            else
                priceTextView.setText("");

            if (item.getLogo() != null) {
                Uri uri = Uri.parse(item.getLogo().replace("64x64", "32x32"));
                logoSimpleDraweeView.setImageURI(uri);
            }

            bodyLinearLayout.setOnClickListener(
                    v -> {
                        menuLinearLayout.setVisibility(
                                (menuLinearLayout.getVisibility() == View.GONE)
                                        ? View.VISIBLE : View.GONE);
                        if (listener != null) listener.onCryptocurrencyClick(item);
                    });

            bodyLinearLayout.setOnLongClickListener(
                    v -> {
                        if (listener != null) return listener.onCryptocurrencyLongClick(item);
                        return false;
                    });
        }

        private void setHeader(CryptocurrencyHolder before) {
            isFavoriteImageView.setVisibility(item.getIsFavorite() ? View.VISIBLE : View.GONE);
            headerNameTextView.setText(item.getIsFavorite() ? "FAVORITE" : "ALL");
            headerLinearLayout.setVisibility(
                    (before == null || before.getIsFavorite() != item.getIsFavorite())
                            ? View.VISIBLE : View.GONE);
        }

        private void setActionMenu() {
            menuLinearLayout.setVisibility(View.GONE);
            actionBuyLinearLayout.setOnClickListener(v -> System.out.println("buy"));
            actionNotifyLinearLayout.setOnClickListener(v -> System.out.println("notify"));
            actionChartsLinearLayout.setOnClickListener(v -> System.out.println("charts"));
            actionDetailsLinearLayout.setOnClickListener(v -> System.out.println("details"));
        }

        private void validate(int position) {
            if (item.shouldUpdateQuotes(CoinMarketCap.AUTO_UPDATE_CRYPTOCURRENCY_QUOTES_DELAY))
                onRequestCryptocurrencyUpdateListener
                        .requestCryptocurrencyQuotesUpdate(position, filtered);
            if (item.shouldUpdateMetadata(CoinMarketCap.AUTO_UPDATE_CRYPTOCURRENCY_METADATA_DELAY))
                onRequestCryptocurrencyUpdateListener
                        .requestCryptocurrencyMetadataUpdate(position, filtered);
        }
    }
}
