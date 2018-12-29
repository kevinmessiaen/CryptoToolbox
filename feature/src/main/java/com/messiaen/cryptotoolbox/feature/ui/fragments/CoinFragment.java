package com.messiaen.cryptotoolbox.feature.ui.fragments;

import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.image.ImageInfo;
import com.facebook.imagepipeline.request.ImageRequest;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.messiaen.cryptotoolbox.feature.R;
import com.messiaen.cryptotoolbox.feature.data.models.Coin;
import com.messiaen.cryptotoolbox.feature.events.coins.OnCoinUpdatedEvent;
import com.messiaen.cryptotoolbox.feature.services.CoinsManager;

import org.greenrobot.eventbus.EventBus;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.transition.TransitionInflater;

public class CoinFragment extends Fragment {

    private static final String COIN_ID = "COIN_ID";

    private View headerView;
    private SimpleDraweeView logoSimpleDraweeView;
    private TextView nameTextView;
    private TextView symbolTextView;

    private Drawable favoriteDrawable;
    private Drawable notFavoriteDrawable;

    private FloatingActionButton fab;

    static CoinFragment newInstance(Coin coin) {
        CoinFragment fragment = new CoinFragment();
        Bundle bundle = new Bundle();
        bundle.putString(COIN_ID, coin.getId());
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        postponeEnterTransition();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setSharedElementEnterTransition(TransitionInflater.from(getContext())
                    .inflateTransition(R.transition.change_bounds));
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.coin_fragment, container, false);

        bind(view);

        Bundle bundle = getArguments();
        String id;
        Coin coin;
        if (bundle == null ||
                (id = bundle.getString(COIN_ID)) == null ||
                (coin = CoinsManager.getInstance().requestData(id)) == null) {
            if (getFragmentManager() != null)
                getFragmentManager().popBackStack();
            return view;
        }

        setModel(coin);

        return view;
    }

    private void bind(@NonNull View view) {
        headerView = view.findViewById(R.id.coin_header_section);
        logoSimpleDraweeView = view.findViewById(R.id.cryptocurrency_logo);
        nameTextView = view.findViewById(R.id.cryptocurrency_name);
        symbolTextView = view.findViewById(R.id.cryptocurrency_symbol);
        fab = view.findViewById(R.id.fab);

        favoriteDrawable = getResources().getDrawable(R.drawable.ic_favorite_secondary_24dp);
        notFavoriteDrawable = getResources().getDrawable(R.drawable.ic_favorite_border_secondary_24dp);
    }

    private void setModel(@NonNull final Coin coin) {
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setImageRequest(ImageRequest.fromUri(coin.getLogoLarge()))
                .setControllerListener(new ImageLoadedListener())
                .build();
        logoSimpleDraweeView.setController(controller);
        nameTextView.setText(coin.getName());
        symbolTextView.setText(coin.getSymbol());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            headerView.setTransitionName("coin_bg_transition_" + coin.getId());
            logoSimpleDraweeView.setTransitionName("coin_logo_transition_" + coin.getId());
            nameTextView.setTransitionName("coin_name_transition_" + coin.getId());
            symbolTextView.setTransitionName("coin_symbol_transition_" + coin.getId());
        }

        fab.setImageDrawable(coin.isFavorite() ? favoriteDrawable : notFavoriteDrawable);

        fab.setOnClickListener(v -> {
            coin.setFavorite(!coin.isFavorite());
            fab.setImageDrawable(coin.isFavorite() ? favoriteDrawable : notFavoriteDrawable);
            EventBus.getDefault().post(new OnCoinUpdatedEvent(coin));
        });
    }

    private class ImageLoadedListener extends BaseControllerListener<ImageInfo> {

        @Override
        public void onFinalImageSet(String id, @Nullable ImageInfo imageInfo,
                                    @Nullable Animatable animatable) {
            super.onFinalImageSet(id, imageInfo, animatable);
            startPostponedEnterTransition();
        }

        @Override
        public void onFailure(String id, Throwable throwable) {
            super.onFailure(id, throwable);
            startPostponedEnterTransition();
        }

    }

}
