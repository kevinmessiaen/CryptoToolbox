package com.messiaen.cryptotoolbox.feature;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.messiaen.cryptotoolbox.feature.services.CoinMarketCapManager;
import com.messiaen.cryptotoolbox.feature.services.DatabaseManager;
import com.messiaen.cryptotoolbox.feature.services.coingecko.CoingeckoManager;

import androidx.annotation.ColorInt;

public class CryptoToolsApplication extends Application {

    @ColorInt
    public static int COLOR_ACCENT_LIGHT;

    private String coingeckoUrl;
    private String databaseName;

    @Override
    public void onCreate() {
        super.onCreate();

        bind(BuildConfig.DEBUG);

        CoingeckoManager.init(coingeckoUrl);
        DatabaseManager.init(getApplicationContext(), databaseName);

        Fresco.initialize(this);
    }

    public void bind(boolean debug) {
        COLOR_ACCENT_LIGHT = getResources().getColor(com.messiaen.cryptotoolbox.R.color.colorSecondary);

        coingeckoUrl = getString(com.messiaen.cryptotoolbox.R.string.coingecko_api_url);
        databaseName = getString(com.messiaen.cryptotoolbox.R.string.database_name);
    }
}
