package com.messiaen.cryptotoolbox.feature.ui.fragment;

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
import com.messiaen.cryptotoolbox.feature.data.cryptocurrency.CryptocurrenciesManager;
import com.messiaen.cryptotoolbox.feature.data.cryptocurrency.CryptocurrencyHolder;
import com.messiaen.cryptotoolbox.feature.data.cryptocurrency.OnCryptocurrenciesDataEventListener;
import com.messiaen.cryptotoolbox.feature.listeners.OnRequestCryptocurrencyUpdateListener;
import com.messiaen.cryptotoolbox.feature.ui.adapter.CryptocurrenciesRecyclerViewAdapter;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class CryptocurrenciesFragment extends Fragment implements OnCryptocurrenciesDataEventListener,
        OnRequestCryptocurrencyUpdateListener, SearchView.OnQueryTextListener {

    private OnListFragmentInteractionListener mListener;

    private SearchView searchView;
    private RecyclerView recyclerView;

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

        CryptocurrenciesManager.getInstance().setComparator(CryptocurrenciesManager.FAVORITES_FIRST);
        CryptocurrenciesManager.getInstance().registerListener(this);

        return view;
    }

    private void bind(View view) {
        int columnCount = getResources().getInteger(com.messiaen.cryptotoolbox.R.integer.list_crypto_column_size);

        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            recyclerView = (RecyclerView) view;
            if (columnCount <= 1) {
                LinearLayoutManager layoutManager = new LinearLayoutManager(context);
                recyclerView.setLayoutManager(layoutManager);
                /*DividerItemDecoration dividerItemDecoration =
                        new DividerItemDecoration(recyclerView.getContext(),
                                layoutManager.getOrientation());
                recyclerView.addItemDecoration(dividerItemDecoration);*/
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, columnCount));
            }
        }
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        CryptocurrenciesManager.getInstance().unregisterListener(this);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.cryptocurrencies_filter, menu);

        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setOnQueryTextListener(this);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                return true;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCryptocurrenciesChanged(@NonNull List<CryptocurrencyHolder> cryptocurrencies) {
        getActivity().runOnUiThread(() -> {
            if (recyclerView != null && recyclerView.getAdapter() != null) {
                recyclerView.getAdapter().notifyDataSetChanged();
                CryptocurrenciesRecyclerViewAdapter adapter =
                        (CryptocurrenciesRecyclerViewAdapter) recyclerView.getAdapter();
                adapter.getFilter().filter(adapter.getFilterText());
            } else if (recyclerView != null) {

                CryptocurrenciesRecyclerViewAdapter cryptocurrenciesRecyclerViewAdapter =
                        new CryptocurrenciesRecyclerViewAdapter(cryptocurrencies, mListener);
                cryptocurrenciesRecyclerViewAdapter.setOnRequestCryptocurrencyUpdateListener(this);
                recyclerView.setAdapter(cryptocurrenciesRecyclerViewAdapter);
            }
        });
    }

    @Override
    public void onNetworkError(int code, @NonNull String message) {
        getActivity().runOnUiThread(() -> Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show());
    }

    @Override
    public void requestCryptocurrencyMetadataUpdate(int pos, List<CryptocurrencyHolder> holders) {
        new Thread(() -> CryptocurrenciesManager.getInstance().refreshMetadata(pos, holders)).start();
    }

    @Override
    public void requestCryptocurrencyQuotesUpdate(int pos, List<CryptocurrencyHolder> holders) {
        new Thread(() -> CryptocurrenciesManager.getInstance().refreshQuotes(pos, holders)).start();
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        if (recyclerView == null)
            return false;

        ((CryptocurrenciesRecyclerViewAdapter) recyclerView.getAdapter()).getFilter().filter(query);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (recyclerView == null)
            return false;

        ((CryptocurrenciesRecyclerViewAdapter) recyclerView.getAdapter()).getFilter().filter(newText);
        return true;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        void onCryptocurrencyClick(CryptocurrencyHolder item);

        boolean onCryptocurrencyLongClick(CryptocurrencyHolder item);

    }
}
