package com.messiaen.cryptotoolbox.feature.ui.holders;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

import com.messiaen.cryptotoolbox.feature.R;
import com.messiaen.cryptotoolbox.feature.data.models.Coin;
import com.messiaen.cryptotoolbox.feature.events.coins.OnCoinClickedEvent;

import org.greenrobot.eventbus.EventBus;

public class CryptocurrencyListingMenuViewHolder extends ViewHolder<Coin> {

    private LinearLayout actionBuyLinearLayout;
    private LinearLayout actionNotifyLinearLayout;
    private LinearLayout actionChartsLinearLayout;
    private LinearLayout actionDetailsLinearLayout;

    CryptocurrencyListingMenuViewHolder(Context context, View view) {
        super(context, view);
    }

    @Override
    protected void onBind() {
        actionBuyLinearLayout = view.findViewById(R.id.cryptocurrency_action_buy);
        actionNotifyLinearLayout = view.findViewById(R.id.cryptocurrency_action_notify);
        actionChartsLinearLayout = view.findViewById(R.id.cryptocurrency_action_charts);
        actionDetailsLinearLayout = view.findViewById(R.id.cryptocurrency_action_details);
    }

    @Override
    public void onDataChanged(Coin item, Coin before, Coin after) {
        actionBuyLinearLayout.setOnClickListener(
                v -> EventBus.getDefault().post(
                        new OnCoinClickedEvent(item, OnCoinClickedEvent.Type.BUY)));
        actionNotifyLinearLayout.setOnClickListener(
                v -> EventBus.getDefault().post(
                        new OnCoinClickedEvent(item, OnCoinClickedEvent.Type.NOTIFY)));
        actionChartsLinearLayout.setOnClickListener(
                v -> EventBus.getDefault().post(
                        new OnCoinClickedEvent(item, OnCoinClickedEvent.Type.CHART)));
        actionDetailsLinearLayout.setOnClickListener(
                v -> EventBus.getDefault().post(
                        new OnCoinClickedEvent(item, OnCoinClickedEvent.Type.DETAILS, (View) view.getParent())));
    }

    @Override
    public void onAttach() {

    }

    @Override
    public void onDetach() {

    }
}
