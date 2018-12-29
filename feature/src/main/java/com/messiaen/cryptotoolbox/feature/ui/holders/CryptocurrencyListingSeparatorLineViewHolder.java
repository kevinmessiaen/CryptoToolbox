package com.messiaen.cryptotoolbox.feature.ui.holders;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.messiaen.cryptotoolbox.feature.R;
import com.messiaen.cryptotoolbox.feature.data.models.Coin;

public class CryptocurrencyListingSeparatorLineViewHolder extends ViewHolder<Coin> {

    private static Drawable favoriteDrawable;
    private static Drawable moneyDrawable;

    private static String favoriteString;
    private static String allString;

    private ImageView separatorImageView;
    private TextView separatorTextView;

    CryptocurrencyListingSeparatorLineViewHolder(Context context, View view) {
        super(context, view);
    }

    @Override
    protected void onBind() {
        if (favoriteDrawable == null) {
            favoriteDrawable = context.getResources().getDrawable(R.drawable.ic_favorite_secondary_24dp);
            moneyDrawable = context.getResources().getDrawable(R.drawable.ic_attach_money_black_24dp);


            favoriteString = context.getString(com.messiaen.cryptotoolbox.R.string.cryptocurrencies_favorites);
            allString = context.getString(com.messiaen.cryptotoolbox.R.string.cryptocurrencies_all);
        }

        separatorImageView = view.findViewById(R.id.separator_image);
        separatorTextView = view.findViewById(R.id.separator_text);
    }

    @Override
    public void onDataChanged(Coin item, Coin before,
                              Coin after) {

        separatorImageView.setImageDrawable(item.isFavorite() ? favoriteDrawable : moneyDrawable);
        separatorTextView.setText(item.isFavorite() ? favoriteString : allString);
        view.setVisibility(
                (before == null || before.isFavorite() != item.isFavorite())
                        ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onAttach() {

    }

    @Override
    public void onDetach() {

    }
}
