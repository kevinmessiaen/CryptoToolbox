package com.messiaen.cryptotoolbox.feature.ui.fragments;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.android.material.snackbar.Snackbar;
import com.messiaen.cryptotoolbox.feature.R;
import com.messiaen.cryptotoolbox.feature.data.models.Coin;
import com.messiaen.cryptotoolbox.feature.events.coingecko.OnCoingeckoStatusEvent;
import com.messiaen.cryptotoolbox.feature.events.coins.OnCoinClickedEvent;
import com.messiaen.cryptotoolbox.feature.events.coins.OnCoinUpdatedEvent;
import com.messiaen.cryptotoolbox.feature.events.coins.OnCoinsListModifiedEvent;
import com.messiaen.cryptotoolbox.feature.events.error.OnNetworkErrorEvent;
import com.messiaen.cryptotoolbox.feature.events.requests.OnCoingeckoStatusRequestedEvent;
import com.messiaen.cryptotoolbox.feature.events.requests.OnCoinsRefreshingRequestedEvent;
import com.messiaen.cryptotoolbox.feature.services.CoinsManager;
import com.messiaen.cryptotoolbox.feature.services.CurrenciesManager;
import com.messiaen.cryptotoolbox.feature.ui.adapters.CoinListingPriceRecyclerViewAdapter;
import com.messiaen.cryptotoolbox.feature.utils.Keyboard;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class CoinListingFragment extends Fragment implements SearchView.OnQueryTextListener {

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private CoinListingPriceRecyclerViewAdapter adapter;

    private Snackbar networkErrorSnackbar;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public CoinListingFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        postponeEnterTransition();
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_coin_list, container, false);

        bind(view);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        EventBus.getDefault().register(this);
        CurrenciesManager.getInstance().requestData();
        CoinsManager.getInstance().setComparator(CoinsManager.FAVORITES_FIRST);
        CoinsManager.getInstance().requestData();
    }

    private void bind(View view) {
        int columnCount = getResources().getInteger(com.messiaen.cryptotoolbox.R.integer.list_crypto_column_size);

        if (view instanceof SwipeRefreshLayout) {
            swipeRefreshLayout = (SwipeRefreshLayout) view;
            swipeRefreshLayout.setOnRefreshListener(
                    () -> EventBus.getDefault().post(new OnCoinsRefreshingRequestedEvent()));
            swipeRefreshLayout.setEnabled(false);
        }

        Context context = view.getContext();
        recyclerView = view.findViewById(R.id.list);
        if (columnCount <= 1) {
            LinearLayoutManager layoutManager = new LinearLayoutManager(context);
            recyclerView.setLayoutManager(layoutManager);
                /*DividerItemDecoration dividerItemDecoration =
                        new DividerItemDecoration(recyclerView.getContext(),
                                layoutManager.getOrientation());
                recyclerView.addItemDecoration(dividerItemDecoration);*/
        } else {
            recyclerView.setLayoutManager(new StaggeredGridLayoutManager(columnCount, StaggeredGridLayoutManager.VERTICAL));
        }

    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        adapter = null;
        super.onStop();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.cryptocurrencies_filter, menu);

        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setOnQueryTextListener(this);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_search)
            return true;

        return super.onOptionsItemSelected(item);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCoinListModified(@NonNull OnCoinsListModifiedEvent event) {
        if (adapter != null) {
            startPostponedEnterTransition();
            adapter.setFilters(adapter.getFilters());
        } else if (recyclerView != null) {
            adapter = new CoinListingPriceRecyclerViewAdapter(getActivity(), event.getList());
            recyclerView.setAdapter(adapter);
        }
        if (swipeRefreshLayout != null)
            swipeRefreshLayout.setRefreshing(false);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNetworkError(@NonNull OnNetworkErrorEvent event) {
        if (swipeRefreshLayout != null)
            swipeRefreshLayout.setRefreshing(false);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNetworkStatus(@NonNull OnCoingeckoStatusEvent event) {
        if (event.getStatus()) {
            if (networkErrorSnackbar != null && networkErrorSnackbar.isShown())
                networkErrorSnackbar.dismiss();
            networkErrorSnackbar = null;
        } else if (recyclerView != null && networkErrorSnackbar == null) {
            networkErrorSnackbar = Snackbar
                    .make(recyclerView, "Unable to reach server", Snackbar.LENGTH_INDEFINITE)
                    .setAction("Retry", v -> {
                        networkErrorSnackbar = null;
                        EventBus.getDefault().post(new OnCoingeckoStatusRequestedEvent());
                    });
            networkErrorSnackbar.show();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnCoinClicked(OnCoinClickedEvent event) {
        Coin item = event.getCoin();
        switch (event.getType()) {
            case BUY:
                return;
            case CHART:
                return;
            case NOTIFY:
                return;
            case DETAILS:
                FragmentManager manager = getFragmentManager();
                if (manager == null)
                    return;

                FragmentTransaction transaction = manager.beginTransaction();
                Coin coin = event.getCoin();
                CoinFragment fragment = CoinFragment.newInstance(coin);

                View view = event.getView();
                if (view != null) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        View bodyView = view.findViewById(R.id.cryptocurrencies_section_body);
                        SimpleDraweeView logo = view.findViewById(R.id.cryptocurrency_logo);
                        TextView name = view.findViewById(R.id.cryptocurrency_name);
                        TextView symbol = view.findViewById(R.id.cryptocurrency_symbol);
                        transaction.setReorderingAllowed(true);
                        transaction.addSharedElement(bodyView, view.getTransitionName());
                        transaction.addSharedElement(logo, logo.getTransitionName());
                        transaction.addSharedElement(name, name.getTransitionName());
                        transaction.addSharedElement(symbol, symbol.getTransitionName());
                    }
                }

                transaction
                        .hide(this)
                        .add(R.id.main_activity_fragment_content, fragment,
                                coin.getId() + "_" + fragment.getClass().getName())
                        .addToBackStack(null)
                        .commit();
                return;
            case FAVORITE:
                item.setFavorite(!item.isFavorite());
                EventBus.getDefault().post(new OnCoinUpdatedEvent(item));
                return;
            default:
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        if (getActivity() != null && getActivity().getCurrentFocus() != null)
            Keyboard.clear(getActivity(), getActivity().getCurrentFocus());

        if (adapter == null)
            return false;

        adapter.setFilters(query);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (adapter == null)
            return false;

        adapter.setFilters(newText);
        return true;
    }
}
