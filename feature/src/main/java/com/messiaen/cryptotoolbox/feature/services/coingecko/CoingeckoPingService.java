package com.messiaen.cryptotoolbox.feature.services.coingecko;

import com.messiaen.cryptotoolbox.feature.data.results.CoingeckoPingResult;

import retrofit2.Call;
import retrofit2.http.GET;

public interface CoingeckoPingService {

    @GET("ping")
    Call<CoingeckoPingResult> ping();
}
