package com.example.gateway.inspector;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class DefaultRequestProcessor implements RequestProcessor {

    private final Supplier<String> supplier;

    private final Consumer<String> consumer;

    public DefaultRequestProcessor(String requestReference, Consumer<String> consumer) {
        this.supplier = () -> requestReference;
        this.consumer = consumer;
    }

    public DefaultRequestProcessor(Supplier<String> supplier, Consumer<String> consumer) {
        this.supplier = supplier;
        this.consumer = consumer;
    }

    @Override
    public String getRequestId() {
        return supplier.get();
    }

    @Override
    public void storeRequestId(String reference) {
        consumer.accept(reference);
    }
}
