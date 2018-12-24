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
        bind();
    }

    protected abstract void bind();

    public final void setData(I item) {
        setData(item, null, null);
    }

    public abstract void setData(I item, I before, I after);

}
