package com.messiaen.cryptotoolbox.feature.ui.holders;

import android.content.Context;
import android.os.Build;
import android.transition.TransitionManager;
import android.view.View;
import android.view.ViewGroup;

import com.messiaen.cryptotoolbox.feature.R;
import com.messiaen.cryptotoolbox.feature.data.models.Coin;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

public class CoinListingPriceViewHolder extends ViewHolder<Coin> {

    private CryptocurrencyListingSeparatorLineViewHolder header;
    private CryptocurrencyListingPriceBodyViewHolder body;
    private CryptocurrencyListingMenuViewHolder menu;

    private ConstraintSet rootConstraintSet;
    private ConstraintSet menuConstraintSet;

    protected Coin item;

    public CoinListingPriceViewHolder(Context context, View view) {
        super(context, view);
    }

    @Override
    protected void onBind() {
        if (view.findViewById(R.id.cryptocurrencies_section_header) != null)
            header = new CryptocurrencyListingSeparatorLineViewHolder(context,
                    view.findViewById(R.id.cryptocurrencies_section_header));
        body = new CryptocurrencyListingPriceBodyViewHolder(context,
                view.findViewById(R.id.cryptocurrencies_section_body));
        menu = new CryptocurrencyListingMenuViewHolder(context,
                view.findViewById(R.id.cryptocurrencies_section_action));

        rootConstraintSet = new ConstraintSet();
        menuConstraintSet = new ConstraintSet();

        rootConstraintSet.clone((ConstraintLayout) view);
        menuConstraintSet.clone(context, R.layout.coin_listing_price_show_menu);
    }

    @Override
    public void onDataChanged(Coin item, Coin before,
                              Coin after) {
        this.item = item;

        if (item.isShowMenu())
            menuConstraintSet.applyTo((ConstraintLayout) view);
        else
            rootConstraintSet.applyTo((ConstraintLayout) view);

        if (header != null)
            header.onDataChanged(item, before, after);
        body.onDataChanged(item, before, after);
        menu.onDataChanged(item, before, after);

        body.view.setOnClickListener(
                v -> {
                    item.setShowMenu(!item.isShowMenu());

                    int headVisibility = View.GONE;
                    if (header != null)
                        headVisibility = header.view.getVisibility();

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
                        TransitionManager.beginDelayedTransition((ViewGroup) view.getParent());
                    if (item.isShowMenu())
                        menuConstraintSet.applyTo((ConstraintLayout) view);
                    else
                        rootConstraintSet.applyTo((ConstraintLayout) view);

                    if (header != null)
                        header.view.setVisibility(headVisibility);
                });
    }

    public Coin getItem() {
        return item;
    }

    @Override
    public void onAttach() {
        if (header != null)
            header.onAttach();
        body.onAttach();
        menu.onAttach();
    }

    @Override
    public void onDetach() {
        if (header != null)
            header.onDetach();
        body.onDetach();
        menu.onDetach();
    }

    public void setFilters(String[] filters) {
        body.setFilters(filters);
    }
}
