package com.messiaen.cryptotoolbox.feature.api.cmc.queries;

import android.annotation.TargetApi;
import android.os.Build;

import com.messiaen.cryptotoolbox.feature.api.cmc.CoinMarketCap;
import com.messiaen.cryptotoolbox.feature.data.cryptocurrency.CryptocurrencyHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import androidx.annotation.NonNull;

public class IdsQueryParameters {

    private String id;


    private IdsQueryParameters(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public static class Builder {

        private List<CryptocurrencyHolder> holders;
        private int pos = 0;
        private Function<CryptocurrencyHolder, Boolean> filter = holder -> true;
        private int size;

        public Builder() {

        }

        public Builder holders(@NonNull List<CryptocurrencyHolder> holders) {
            this.holders = holders;
            return this;
        }

        @NonNull
        public Builder pos(int pos) {
            this.pos = pos;
            return this;
        }

        @NonNull
        public Builder filter(@NonNull Function<CryptocurrencyHolder, Boolean> filter) {
            this.filter = filter;
            return this;
        }

        @NonNull
        public Builder size(int size) {
            this.size = size;
            return this;
        }

        public IdsQueryParameters build() {
            if (holders == null)
                throw new IllegalStateException("holders must be passed calling function holders");

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                return buildWithStream();
            else
                return buildWithoutStream();
        }

        @TargetApi(Build.VERSION_CODES.N)
        private IdsQueryParameters buildWithStream() {
            return new IdsQueryParameters(
                    holders.stream()
                            .skip(pos).filter(holder -> filter.apply(holder))
                            .limit(size).map(holder -> Integer.toString(holder.getId()))
                            .collect(Collectors.joining(",")));
        }

        private IdsQueryParameters buildWithoutStream() {
            List<CryptocurrencyHolder> requestedMetadata = new ArrayList<>();

            int i = pos;
            do {
                if (holders.get(i)
                        .shouldUpdateMetadata(CoinMarketCap.AUTO_UPDATE_CRYPTOCURRENCY_METADATA_DELAY))
                    requestedMetadata.add(holders.get(i));
            } while (requestedMetadata.size() < 100 && ++i < holders.size());

            String id = "";

            for (i = 0; i < requestedMetadata.size(); i++)
                id += requestedMetadata.get(i).getId() + ((i + 1 < requestedMetadata.size()) ? "," : "");

            return new IdsQueryParameters(id);
        }
    }
}
