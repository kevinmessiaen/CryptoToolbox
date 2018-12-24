package com.messiaen.cryptotoolbox.feature.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.messiaen.cryptotoolbox.feature.R;
import com.messiaen.cryptotoolbox.feature.events.cryptocurrencies.CryptocurrenciesDataChangedEvent;
import com.messiaen.cryptotoolbox.feature.events.cryptocurrencies.CryptocurrenciesUpdatedEvent;
import com.messiaen.cryptotoolbox.feature.events.cryptocurrencies.CryptocurrencyClickEvent;
import com.messiaen.cryptotoolbox.feature.events.cryptocurrencies.InvalidateCryptocurrenciesEvent;
import com.messiaen.cryptotoolbox.feature.events.error.NetworkErrorEvent;
import com.messiaen.cryptotoolbox.feature.manager.CryptocurrenciesManager;
import com.messiaen.cryptotoolbox.feature.persistence.entities.CryptocurrencyHolder;
import com.messiaen.cryptotoolbox.feature.ui.adapters.CryptocurrenciesRecyclerViewAdapter;
import com.messiaen.cryptotoolbox.feature.utils.Keyboard;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class CryptocurrenciesFragment extends Fragment implements SearchView.OnQueryTextListener {

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private CryptocurrenciesRecyclerViewAdapter adapter;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public CryptocurrenciesFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cryptocurencies_list, container, false);

        bind(view);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        EventBus.getDefault().register(this);
        CryptocurrenciesManager.getInstance().setComparator(CryptocurrenciesManager.FAVORITES_FIRST);
        CryptocurrenciesManager.getInstance().requestData();
    }

    private void bind(View view) {
        int columnCount = getResources().getInteger(com.messiaen.cryptotoolbox.R.integer.list_crypto_column_size);

        if (view instanceof SwipeRefreshLayout) {
            swipeRefreshLayout = (SwipeRefreshLayout) view;
            swipeRefreshLayout.setOnRefreshListener(
                    () -> EventBus.getDefault().post(new InvalidateCryptocurrenciesEvent()));
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
        super.onStop();

        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
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
    public void onCryptocurrenciesChanged(@NonNull CryptocurrenciesDataChangedEvent event) {
        if (adapter != null) {
            adapter.setFilters(adapter.getFilters());
        } else if (recyclerView != null) {
            adapter = new CryptocurrenciesRecyclerViewAdapter(getActivity(), event.getData());
            recyclerView.setAdapter(adapter);
        }
        if (swipeRefreshLayout != null)
            swipeRefreshLayout.setRefreshing(false);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNetworkError(@NonNull NetworkErrorEvent event) {
        Toast.makeText(getContext(), event.getMessage(), Toast.LENGTH_SHORT).show();
        if (swipeRefreshLayout != null)
            swipeRefreshLayout.setRefreshing(false);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCryptocurrencyClick(CryptocurrencyClickEvent event) {
        CryptocurrencyHolder item = event.getHolder();
        switch (event.getType()) {
            case BUY:
                return;
            case CHART:
                return;
            case NOTIFY:
                return;
            case DETAILS:
                return;
            case FAVORITE:
                item.setIsFavorite(!item.getIsFavorite());
                EventBus.getDefault().post(new CryptocurrenciesUpdatedEvent(item));
                return;
            default:
                return;
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
