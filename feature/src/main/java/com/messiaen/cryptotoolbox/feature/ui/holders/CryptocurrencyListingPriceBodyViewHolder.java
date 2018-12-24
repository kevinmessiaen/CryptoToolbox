package com.messiaen.cryptotoolbox.feature.ui.holders;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.messiaen.cryptotoolbox.feature.R;
import com.messiaen.cryptotoolbox.feature.events.cryptocurrencies.CryptocurrencyClickEvent;
import com.messiaen.cryptotoolbox.feature.persistence.entities.CryptocurrencyHolder;
import com.messiaen.cryptotoolbox.feature.persistence.entities.Quote;
import com.messiaen.cryptotoolbox.feature.utils.Currencies;
import com.messiaen.cryptotoolbox.feature.utils.Formatters;
import com.messiaen.cryptotoolbox.feature.utils.Highlight;

import org.greenrobot.eventbus.EventBus;

import java.text.NumberFormat;

public class CryptocurrencyListingPriceBodyViewHolder extends ViewHolder<CryptocurrencyHolder> {

    private static NumberFormat currencyFormat;

    private SimpleDraweeView logoSimpleDraweeView;
    private ImageView favoriteImageView;
    private TextView nameTextView;
    private TextView sybolTextView;
    private TextView priceTextView;

    private String[] filters;

    public CryptocurrencyListingPriceBodyViewHolder(Context context, View view) {
        super(context, view);
    }

    @Override
    protected void bind() {
        if (currencyFormat == null)
            currencyFormat = Formatters.getCurrencyFormat(Currencies.getDefaultLocal().toString());

        logoSimpleDraweeView = view.findViewById(R.id.cryptocurrency_logo);
        favoriteImageView = view.findViewById(R.id.cryptocurrency_favorite);
        nameTextView = view.findViewById(R.id.cryptocurrency_name);
        sybolTextView = view.findViewById(R.id.cryptocurrency_symbol);
        priceTextView = view.findViewById(R.id.cryptocurrency_price);
    }

    @Override
    public void setData(CryptocurrencyHolder item, CryptocurrencyHolder before,
                        CryptocurrencyHolder after) {
        Highlight.underlineString(nameTextView, item.getName(), filters);
        Highlight.underlineString(sybolTextView, item.getSymbol(), filters);

        if (favoriteImageView != null)
            favoriteImageView.setVisibility(item.getIsFavorite() ? View.VISIBLE : View.GONE);

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

        view.setOnLongClickListener(v -> { EventBus.getDefault().post(
                new CryptocurrencyClickEvent(item, CryptocurrencyClickEvent.Type.FAVORITE));
                return true;
        });
    }

    public void setFilters(String[] filters) {
        this.filters = filters;
    }
}
