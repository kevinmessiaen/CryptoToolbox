package com.messiaen.cryptotoolbox.feature.ui.holders;

import android.content.Context;
import android.os.Build;
import android.transition.TransitionManager;
import android.transition.Visibility;
import android.view.View;
import android.view.ViewGroup;

import com.messiaen.cryptotoolbox.feature.R;
import com.messiaen.cryptotoolbox.feature.persistence.entities.CryptocurrencyHolder;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

public class CryptocurrencyListingPriceViewHolder extends ViewHolder<CryptocurrencyHolder> {

    private CryptocurrencyListingSeparatorLineViewHolder header;
    private CryptocurrencyListingPriceBodyViewHolder body;
    private CryptocurrencyListingMenuViewHolder menu;

    private ConstraintSet rootConstraintSet;
    private ConstraintSet menuConstraintSet;

    public CryptocurrencyListingPriceViewHolder(Context context, View view) {
        super(context, view);
    }

    @Override
    protected void bind() {
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
        menuConstraintSet.clone(context, R.layout.cryptocurrency_listing_price_show_menu);
    }

    @Override
    public void setData(CryptocurrencyHolder item, CryptocurrencyHolder before,
                        CryptocurrencyHolder after) {
        if (header != null)
            header.setData(item, before, after);
        body.setData(item, before, after);
        menu.setData(item, before, after);

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

    public void setFilters(String[] filters) {
        body.setFilters(filters);
    }
}
