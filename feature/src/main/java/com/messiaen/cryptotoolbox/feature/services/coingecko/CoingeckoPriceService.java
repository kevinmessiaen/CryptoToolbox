package com.messiaen.cryptotoolbox.feature.services.coingecko;

import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface CoingeckoPriceService {

    @GET("simple/supported_vs_currencies")
    Call<List<String>> list();

    @GET("simple/price")
    Call<HashMap<String, JsonObject>> getPrices(@Query("ids") String ids,
                                                @Query("vs_currencies") String currencies,
                                                @Query("include_24hr_vol") boolean vol,
                                                @Query("include_last_updated_at") boolean lastUpdate);

}
