package com.messiaen.cryptotoolbox.feature.ui.holders;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.DragAndDropPermissions;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.messiaen.cryptotoolbox.feature.R;
import com.messiaen.cryptotoolbox.feature.persistence.entities.CryptocurrencyHolder;

public class CryptocurrencyListingSeparatorLineViewHolder extends ViewHolder<CryptocurrencyHolder> {

    private static Drawable favoriteDrawable;
    private static Drawable moneyDrawable;

    private static String favoriteString;
    private static String allString;

    private ImageView separatorImageView;
    private TextView separatorTextView;

    public CryptocurrencyListingSeparatorLineViewHolder(Context context, View view) {
        super(context, view);
    }

    @Override
    protected void bind() {
        if (favoriteDrawable == null) {
            favoriteDrawable = context.getResources().getDrawable(R.drawable.ic_favorite_black_32dp);
            moneyDrawable = context.getResources().getDrawable(R.drawable.ic_attach_money_black_24dp);

            favoriteString = context.getString(com.messiaen.cryptotoolbox.R.string.cryptocurrencies_favorites);
            allString = context.getString(com.messiaen.cryptotoolbox.R.string.cryptocurrencies_all);
        }

        separatorImageView = view.findViewById(R.id.separator_image);
        separatorTextView = view.findViewById(R.id.separator_text);
    }

    @Override
    public void setData(CryptocurrencyHolder item, CryptocurrencyHolder before,
                        CryptocurrencyHolder after) {

        separatorImageView.setImageDrawable(item.getIsFavorite() ? favoriteDrawable : moneyDrawable);
        separatorTextView.setText(item.getIsFavorite() ? favoriteString : allString);
        view.setVisibility(
                (before == null || before.getIsFavorite() != item.getIsFavorite())
                        ? View.VISIBLE : View.GONE);
    }
}
