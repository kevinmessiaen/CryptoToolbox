package com.messiaen.cryptotoolbox.feature.api.cmc.queries;

public class FilterQueryParameters {

    private int start;
    private int limit = 200;

    public FilterQueryParameters(int start) {
        this.start = start;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }
}
