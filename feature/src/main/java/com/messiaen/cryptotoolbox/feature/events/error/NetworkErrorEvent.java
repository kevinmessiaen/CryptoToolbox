package com.messiaen.cryptotoolbox.feature.events.error;

import androidx.annotation.NonNull;

public class NetworkErrorEvent {

    private final int code;
    private final String message;
    private final int taskId;

    public NetworkErrorEvent(int code, @NonNull String message) {
        this.code = code;
        this.message = message;
        this.taskId = -1;
    }

    public NetworkErrorEvent(int code, @NonNull String message, int taskId) {
        this.code = code;
        this.message = message;
        this.taskId = taskId;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public int getTaskId() {
        return taskId;
    }
}
