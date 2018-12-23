package com.messiaen.cryptotoolbox.feature.api.cmc.queries;

import android.annotation.TargetApi;
import android.os.Build;

import com.messiaen.cryptotoolbox.feature.persistence.entities.CryptocurrencyHolder;
import com.messiaen.cryptotoolbox.feature.events.cryptocurrencies.CryptocurrenciesOutdatedEvent;
import com.messiaen.cryptotoolbox.feature.manager.CoinMarketCapManager;
import com.messiaen.cryptotoolbox.feature.manager.CoinMarketCapManager.DataType;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import androidx.annotation.NonNull;

public abstract class CryptocurrenciesUpdatingQuery extends Query {

    protected final String id;

    CryptocurrenciesUpdatingQuery(String id) {
        this.id = id;
    }

    public static class Builder {

        private List<CryptocurrencyHolder> holders;
        private int pos = 0;
        private Function<CryptocurrencyHolder, Boolean> filter = holder -> true;
        private int size;
        private DataType type;

        public Builder() {

        }

        public Builder event(@NonNull CryptocurrenciesOutdatedEvent event) {
            return holders(event.getHolders())
                    .pos(event.getPos())
                    .size(event.getDataType().getSize())
                    .filter(event.getDataType().getFilter())
                    .type(event.getDataType());
        }

        @NonNull
        private Builder holders(@NonNull List<CryptocurrencyHolder> holders) {
            this.holders = holders;
            return this;
        }

        @NonNull
        private Builder pos(int pos) {
            this.pos = pos;
            return this;
        }

        @NonNull
        private Builder filter(@NonNull Function<CryptocurrencyHolder, Boolean> filter) {
            this.filter = filter;
            return this;
        }

        @NonNull
        private Builder size(int size) {
            this.size = size;
            return this;
        }

        @NonNull
        private Builder type(@NonNull DataType type) {
            this.type = type;
            return this;
        }

        public CryptocurrenciesUpdatingQuery build() {
            if (holders == null)
                throw new IllegalStateException("holders must be passed calling function holders");

            String id = (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                    ? buildIdWithStream() : buildIdWithoutStream();

            switch (type) {
                case METADATA:
                    return new CryptocurrenciesUpdatingMetadataQuery(id);
                case QUOTES:
                    return new CryptocurrenciesUpdatingQuotesQuery(id);
                default:
                    return null;
            }
        }

        @TargetApi(Build.VERSION_CODES.N)
        private String buildIdWithStream() {
            return holders.stream()
                    .skip(pos).filter(holder -> filter.apply(holder))
                    .limit(size).map(holder -> Integer.toString(holder.getId()))
                    .collect(Collectors.joining(","));
        }

        private String buildIdWithoutStream() {
            List<CryptocurrencyHolder> requestedMetadata = new ArrayList<>();

            int i = pos;
            do {
                if (holders.get(i)
                        .shouldUpdateMetadata(CoinMarketCapManager.AUTO_UPDATE_CRYPTOCURRENCY_METADATA_DELAY))
                    requestedMetadata.add(holders.get(i));
            } while (requestedMetadata.size() < 100 && ++i < holders.size());

            StringBuilder id = new StringBuilder();
            for (i = 0; i < requestedMetadata.size(); i++)
                id.append(requestedMetadata.get(i).getId() + ((i + 1 < requestedMetadata.size()) ? "," : ""));

            return id.toString();
        }
    }
}
