package com.example.gateway.context;

public class RequestContext {

    private static final ThreadLocal<String> contextHolder = new ThreadLocal<>();

    public static void set(String requestId) {
        contextHolder.set(requestId);
    }

    public static String get() {
        return contextHolder.get();
    }

    public static void clear() {
        contextHolder.remove();
    }
}
