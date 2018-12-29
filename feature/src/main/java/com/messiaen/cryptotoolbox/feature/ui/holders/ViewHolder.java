package com.messiaen.cryptotoolbox.feature.ui.holders;

import android.content.Context;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public abstract class ViewHolder<I> extends RecyclerView.ViewHolder {

    protected final Context context;
    protected final View view;

    ViewHolder(Context context, View view) {
        super(view);
        this.context = context;
        this.view = view;
        onBind();
    }

    public abstract void onAttach();

    protected abstract void onBind();

    public abstract void onDetach();

    public final void onDataChanged(I item) {
        onDataChanged(item, null, null);
    }

    public abstract void onDataChanged(I item, I before, I after);

}
