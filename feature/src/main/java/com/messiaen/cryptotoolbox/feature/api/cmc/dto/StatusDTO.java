package com.messiaen.cryptotoolbox.feature.api.cmc.dto;

import java.util.Date;

public class StatusDTO {

    private Date timestamp;
    private int creditCount;
    private int elapsed;
    private int errorCode;
    private String errorMessage;

    public StatusDTO() {
    }

    public StatusDTO(Date timestamp, int creditCount, int elapsed, int errorCode, String errorMessage) {
        this.timestamp = timestamp;
        this.creditCount = creditCount;
        this.elapsed = elapsed;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public int getCreditCount() {
        return creditCount;
    }

    public void setCreditCount(int creditCount) {
        this.creditCount = creditCount;
    }

    public int getElapsed() {
        return elapsed;
    }

    public void setElapsed(int elapsed) {
        this.elapsed = elapsed;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
