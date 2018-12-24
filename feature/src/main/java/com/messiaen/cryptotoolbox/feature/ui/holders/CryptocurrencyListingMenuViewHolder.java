package com.messiaen.cryptotoolbox.feature.ui.holders;

import android.content.Context;
import android.os.Build;
import android.view.View;
import android.widget.LinearLayout;

import com.messiaen.cryptotoolbox.feature.R;
import com.messiaen.cryptotoolbox.feature.events.cryptocurrencies.CryptocurrencyClickEvent;
import com.messiaen.cryptotoolbox.feature.persistence.entities.CryptocurrencyHolder;

import org.greenrobot.eventbus.EventBus;

public class CryptocurrencyListingMenuViewHolder extends ViewHolder<CryptocurrencyHolder> {

    private LinearLayout actionBuyLinearLayout;
    private LinearLayout actionNotifyLinearLayout;
    private LinearLayout actionChartsLinearLayout;
    private LinearLayout actionDetailsLinearLayout;

    public CryptocurrencyListingMenuViewHolder(Context context, View view) {
        super(context, view);
    }

    @Override
    protected void bind() {
        actionBuyLinearLayout = view.findViewById(R.id.cryptocurrency_action_buy);
        actionNotifyLinearLayout = view.findViewById(R.id.cryptocurrency_action_notify);
        actionChartsLinearLayout = view.findViewById(R.id.cryptocurrency_action_charts);
        actionDetailsLinearLayout = view.findViewById(R.id.cryptocurrency_action_details);
    }

    @Override
    public void setData(CryptocurrencyHolder item, CryptocurrencyHolder before, CryptocurrencyHolder after) {
        actionBuyLinearLayout.setOnClickListener(
                v -> EventBus.getDefault().post(new CryptocurrencyClickEvent(item, CryptocurrencyClickEvent.Type.BUY)));
        actionNotifyLinearLayout.setOnClickListener(
                v -> EventBus.getDefault().post(new CryptocurrencyClickEvent(item, CryptocurrencyClickEvent.Type.NOTIFY)));
        actionChartsLinearLayout.setOnClickListener(
                v -> EventBus.getDefault().post(new CryptocurrencyClickEvent(item, CryptocurrencyClickEvent.Type.CHART)));
        actionDetailsLinearLayout.setOnClickListener(
                v -> EventBus.getDefault().post(new CryptocurrencyClickEvent(item, CryptocurrencyClickEvent.Type.DETAILS)));
    }

}
