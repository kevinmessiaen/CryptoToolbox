package com.messiaen.cryptotoolbox.feature;

import android.os.Bundle;

import com.messiaen.cryptotoolbox.feature.data.cryptocurrency.CryptocurrenciesManager;
import com.messiaen.cryptotoolbox.feature.data.cryptocurrency.CryptocurrencyHolder;
import com.messiaen.cryptotoolbox.feature.persistence.DatabaseManager;
import com.messiaen.cryptotoolbox.feature.ui.fragment.CryptocurrenciesFragment;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements CryptocurrenciesFragment.OnListFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onCryptocurrencyClick(CryptocurrencyHolder item) {
        item.setIsFavorite(!item.getIsFavorite());
        new Thread(() -> CryptocurrenciesManager.getInstance().notifyChange(item));
    }

    @Override
    public boolean onCryptocurrencyLongClick(CryptocurrencyHolder item) {
        item.setIsFavorite(!item.getIsFavorite());
        new Thread(() -> CryptocurrenciesManager.getInstance().notifyChange(item)).start();
        return true;
    }
}
