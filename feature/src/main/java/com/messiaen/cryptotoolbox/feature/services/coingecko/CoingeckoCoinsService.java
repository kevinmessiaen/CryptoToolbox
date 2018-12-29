package com.messiaen.cryptotoolbox.feature.services.coingecko;

import com.google.gson.JsonObject;
import com.messiaen.cryptotoolbox.feature.data.entities.CoinId;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface CoingeckoCoinsService {

    @GET("coins/list")
    Call<List<CoinId>> list();

    @GET("coins/{id}")
    Call<JsonObject> getMetadata(@Path("id") String id,
                             @Query("localization") boolean localization,
                             @Query("tickers") boolean tickers,
                             @Query("market_data") boolean marketData,
                             @Query("community_data") boolean communityData,
                             @Query("developer_data") boolean developperData,
                             @Query("sparkline") boolean sparkline);

}
