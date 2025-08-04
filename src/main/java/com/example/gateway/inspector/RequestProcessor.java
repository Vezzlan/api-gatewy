package com.example.gateway.inspector;

public interface RequestProcessor {

    String getRequestId();

    void storeRequestId(String requestId);

    default void execute() {
        storeRequestId(getRequestId());
    }
}
