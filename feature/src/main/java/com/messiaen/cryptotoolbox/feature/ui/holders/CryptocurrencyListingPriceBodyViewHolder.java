package com.messiaen.cryptotoolbox.feature.ui.holders;

import android.content.Context;
import android.os.Build;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.messiaen.cryptotoolbox.feature.R;
import com.messiaen.cryptotoolbox.feature.data.entities.Price;
import com.messiaen.cryptotoolbox.feature.data.models.Coin;
import com.messiaen.cryptotoolbox.feature.data.models.Currency;
import com.messiaen.cryptotoolbox.feature.events.coins.OnCoinClickedEvent;
import com.messiaen.cryptotoolbox.feature.events.coins.logo.OnLogoModifiedEvent;
import com.messiaen.cryptotoolbox.feature.events.coins.prices.OnPriceModifiedEvent;
import com.messiaen.cryptotoolbox.feature.events.currencies.OnCurrenciesListModifiedEvent;
import com.messiaen.cryptotoolbox.feature.services.CurrenciesManager;
import com.messiaen.cryptotoolbox.feature.utils.Highlight;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class CryptocurrencyListingPriceBodyViewHolder extends ViewHolder<Coin> {

    private Coin item;

    private SimpleDraweeView logoSimpleDraweeView;
    private ImageView favoriteImageView;
    private TextView nameTextView;
    private TextView symbolTextView;
    private TextView priceTextView;

    private String[] filters;

    CryptocurrencyListingPriceBodyViewHolder(Context context, View view) {
        super(context, view);
    }

    @Override
    protected void onBind() {
        logoSimpleDraweeView = view.findViewById(R.id.cryptocurrency_logo);
        favoriteImageView = view.findViewById(R.id.cryptocurrency_favorite);
        nameTextView = view.findViewById(R.id.cryptocurrency_name);
        symbolTextView = view.findViewById(R.id.cryptocurrency_symbol);
        priceTextView = view.findViewById(R.id.cryptocurrency_price);
    }

    @Override
    public void onDataChanged(Coin item, Coin before,
                              Coin after) {
        this.item = item;

        Highlight.underlineString(nameTextView, item.getName(), filters);
        Highlight.underlineString(symbolTextView, item.getSymbol(), filters);

        if (favoriteImageView != null)
            favoriteImageView.setVisibility(item.isFavorite() ? View.VISIBLE : View.GONE);

        setPrice(item);

        setLogo(item);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            view.setTransitionName("coin_bg_transition_" + item.getId());
            logoSimpleDraweeView.setTransitionName("coin_logo_transition_" + item.getId());
            nameTextView.setTransitionName("coin_name_transition_" + item.getId());
            symbolTextView.setTransitionName("coin_symbol_transition_" + item.getId());
        }

        view.setOnLongClickListener(v -> {
            EventBus.getDefault().post(
                    new OnCoinClickedEvent(item, OnCoinClickedEvent.Type.FAVORITE));
            return true;
        });
    }

    private void setPrice(Coin item) {
        Currency currency = CurrenciesManager.getInstance().getLocal();
        Price price = item.getPrice(currency.getName());
        priceTextView.setText(currency.getFormat().format(price != null ? price.getPrice() : 0));
    }

    private void setLogo(Coin item) {
        logoSimpleDraweeView.setImageURI(item.getLogoSmall());
    }

    @Override
    public void onAttach() {
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDetach() {
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCurrenciesChanged(OnCurrenciesListModifiedEvent event) {
        setPrice(item);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPricesChanged(OnPriceModifiedEvent event) {
        if (event.getModified().contains(item))
            setPrice(item);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLogoChanged(OnLogoModifiedEvent event) {
        if (event.getModified().contains(item))
            setLogo(item);
    }

    void setFilters(String[] filters) {
        this.filters = filters;

        Highlight.underlineString(nameTextView, nameTextView.getText().toString(), filters);
        Highlight.underlineString(symbolTextView, symbolTextView.getText().toString(), filters);
    }
}
