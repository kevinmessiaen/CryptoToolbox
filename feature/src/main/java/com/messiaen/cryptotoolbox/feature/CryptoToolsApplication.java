package com.messiaen.cryptotoolbox.feature;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.messiaen.cryptotoolbox.feature.api.cmc.CoinMarketCap;
import com.messiaen.cryptotoolbox.feature.persistence.DatabaseManager;

import androidx.annotation.ColorInt;

public class CryptoToolsApplication extends Application {

    @ColorInt
    public static int COLOR_ACCENT_LIGHT;

    public static String CMC_API_KEY;
    private String cmcApiUrl;
    private String databaseName;

    @Override
    public void onCreate() {
        super.onCreate();

        bind(BuildConfig.DEBUG);

        CoinMarketCap.init(cmcApiUrl);
        DatabaseManager.init(getApplicationContext(), databaseName);

        Fresco.initialize(this);
    }

    public void bind(boolean debug) {
        COLOR_ACCENT_LIGHT = getResources().getColor(com.messiaen.cryptotoolbox.R.color.colorAccentLight);

        CMC_API_KEY = getString(com.messiaen.cryptotoolbox.R.string.cmc_sandbox_api_key);
        cmcApiUrl = getString(debug ? com.messiaen.cryptotoolbox.R.string.cmc_sandbox_url :
                com.messiaen.cryptotoolbox.R.string.cmc_pro_url);
        databaseName = getString(com.messiaen.cryptotoolbox.R.string.database_name);
    }
}
