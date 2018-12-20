package com.messiaen.cryptotoolbox.feature.api.cmc.services;

import com.messiaen.cryptotoolbox.feature.api.cmc.results.CryptocurrenciesIDMapDTO;
import com.messiaen.cryptotoolbox.feature.api.cmc.results.CryptocurrenciesListingDTO;
import com.messiaen.cryptotoolbox.feature.api.cmc.results.CryptocurrenciesMetadataDTO;
import com.messiaen.cryptotoolbox.feature.api.cmc.results.CryptocurrenciesQuotesDTO;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface CryptocurrenciesService {

    @GET("v1/cryptocurrency/map")
    Call<CryptocurrenciesIDMapDTO> getCryptocurrenciesIdMap(@Header("X-CMC_PRO_API_KEY") String apiKey);

    @GET("v1/cryptocurrency/info")
    Call<CryptocurrenciesMetadataDTO> getMetadata(@Header("X-CMC_PRO_API_KEY") String apiKey
            , @Query("id") String ids);

    @GET("v1/cryptocurrency/quotes/latest")
    Call<CryptocurrenciesQuotesDTO> getQuotes(@Header("X-CMC_PRO_API_KEY") String apiKey
            , @Query("id") String ids);

    @GET("v1/cryptocurrency/listings/latest")
    Call<CryptocurrenciesListingDTO> getLatestListing(@Header("X-CMC_PRO_API_KEY") String apiKey
            , @Query("start") int start, int limit);
}
