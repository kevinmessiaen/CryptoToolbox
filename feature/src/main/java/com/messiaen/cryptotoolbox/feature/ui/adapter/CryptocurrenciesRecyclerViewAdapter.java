package com.messiaen.cryptotoolbox.feature.ui.adapter;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.messiaen.cryptotoolbox.feature.R;
import com.messiaen.cryptotoolbox.feature.api.cmc.CoinMarketCap;
import com.messiaen.cryptotoolbox.feature.data.cryptocurrency.CryptocurrencyHolder;
import com.messiaen.cryptotoolbox.feature.listeners.OnRequestCryptocurrencyUpdateListener;
import com.messiaen.cryptotoolbox.feature.ui.fragment.CryptocurrenciesFragment.OnListFragmentInteractionListener;
import com.messiaen.cryptotoolbox.feature.utils.Highlight;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * {@link RecyclerView.Adapter} that can display a {@link CryptocurrencyHolder} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 */
public class CryptocurrenciesRecyclerViewAdapter extends
        RecyclerView.Adapter<CryptocurrenciesRecyclerViewAdapter.ViewHolder>
        implements Filterable {

    private final List<CryptocurrencyHolder> cryptocurrencies;
    private List<CryptocurrencyHolder> filtered;
    private final OnListFragmentInteractionListener listener;
    private OnRequestCryptocurrencyUpdateListener onRequestCryptocurrencyUpdateListener;
    private final NumberFormat currencyFormat;

    private String filter = "";

    public CryptocurrenciesRecyclerViewAdapter(List<CryptocurrencyHolder> items,
                                               OnListFragmentInteractionListener listener) {
        cryptocurrencies = items;
        filtered = cryptocurrencies;
        this.listener = listener;
        currencyFormat = NumberFormat.getCurrencyInstance(Locale.US);
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
        holder.item = filtered.get(position);

        Highlight.underlineString(holder.nameTextView, holder.item.getName(), filter);
        Highlight.underlineString(holder.sybolTextView, holder.item.getSymbol(), filter);

        if (holder.item.getQuote() != null)
            holder.priceTextView.setText(
                    currencyFormat.format(holder.item.getQuote().get("USD").getPrice()));
        else
            holder.priceTextView.setText("");

        if (holder.item.getLogo() != null) {
            Uri uri = Uri.parse(holder.item.getLogo().replace("64x64", "32x32"));
            holder.logoSimpleDraweeView.setImageURI(uri);
        }

        if (position == 0 && holder.item.getIsFavorite()) {
            holder.headerLinearLayout.setVisibility(View.VISIBLE);
            holder.isFavoriteImageView.setVisibility(View.VISIBLE);
            holder.headerNameTextView.setText("FAVORITES");
        } else if (position > 0 && !holder.item.getIsFavorite() &&
                filtered.get(position - 1).getIsFavorite()) {
            holder.headerLinearLayout.setVisibility(View.VISIBLE);
            holder.isFavoriteImageView.setVisibility(View.GONE);
            holder.headerNameTextView.setText("ALL");
        } else if (holder.item.getIsFavorite()) {
            holder.isFavoriteImageView.setVisibility(View.VISIBLE);
            holder.headerLinearLayout.setVisibility(View.GONE);
        } else {
            holder.headerLinearLayout.setVisibility(View.GONE);
            holder.isFavoriteImageView.setVisibility(View.GONE);
        }

        holder.menuLinearLayout.setVisibility(View.GONE);

        holder.bodyLinearLayout.setOnClickListener(
                v -> {
                    holder.menuLinearLayout.setVisibility(
                            (holder.menuLinearLayout.getVisibility() == View.GONE)
                                    ? View.VISIBLE : View.GONE);
                    if (listener != null) listener.onCryptocurrencyClick(holder.item);
                });

        holder.bodyLinearLayout.setOnLongClickListener(
                v -> {
                    if (listener != null) return listener.onCryptocurrencyLongClick(holder.item);
                    return false;
                });

        if (onRequestCryptocurrencyUpdateListener != null &&
                holder.item.shouldUpdateQuotes(CoinMarketCap.AUTO_UPDATE_CRYPTOCURRENCY_QUOTES_DELAY))
            onRequestCryptocurrencyUpdateListener
                    .requestCryptocurrencyQuotesUpdate(position, filtered);
        else if (onRequestCryptocurrencyUpdateListener != null &&
                holder.item.shouldUpdateMetadata(CoinMarketCap.AUTO_UPDATE_CRYPTOCURRENCY_METADATA_DELAY))
            onRequestCryptocurrencyUpdateListener
                    .requestCryptocurrencyMetadataUpdate(position, filtered);

    }

    @Override
    public int getItemCount() {
        return filtered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String charString = constraint.toString().toLowerCase();
                filter = charString;
                if (charString.isEmpty())
                    filtered = cryptocurrencies;
                else {
                    List<CryptocurrencyHolder> tmp = new ArrayList<>();
                    for (CryptocurrencyHolder holder : cryptocurrencies)
                        if (holder.getName().toLowerCase().contains(charString) ||
                                holder.getSymbol().toLowerCase().contains(charString))
                            tmp.add(holder);
                    filtered = tmp;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filtered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filtered = (ArrayList<CryptocurrencyHolder>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private final View view;
        private final SimpleDraweeView logoSimpleDraweeView;
        private final TextView nameTextView;
        private final TextView sybolTextView;
        private final TextView priceTextView;
        private final LinearLayout headerLinearLayout;
        private final LinearLayout bodyLinearLayout;
        private final ImageView isFavoriteImageView;
        private final TextView headerNameTextView;
        private final LinearLayout menuLinearLayout;
        private CryptocurrencyHolder item;

        private ViewHolder(View view) {
            super(view);
            this.view = view;
            logoSimpleDraweeView = view.findViewById(R.id.cryptocurrency_logo);
            nameTextView = view.findViewById(R.id.cryptocurrency_name);
            sybolTextView = view.findViewById(R.id.cryptocurrency_symbol);
            priceTextView = view.findViewById(R.id.cryptocurrency_price);
            headerLinearLayout = view.findViewById(R.id.cryptocurrencies_section_heading);
            bodyLinearLayout = view.findViewById(R.id.cryptocurrencies_section_body);
            isFavoriteImageView = view.findViewById(R.id.cryptocurrencies_is_favorite);
            headerNameTextView = view.findViewById(R.id.cryptocurrency_header_name);
            menuLinearLayout= view.findViewById(R.id.cryptocurrencies_section_menu);
        }
    }

    public String getFilterText() {
        return filter;
    }

    public void setOnRequestCryptocurrencyUpdateListener(OnRequestCryptocurrencyUpdateListener onRequestCryptocurrencyUpdateListener) {
        this.onRequestCryptocurrencyUpdateListener = onRequestCryptocurrencyUpdateListener;
    }
}
