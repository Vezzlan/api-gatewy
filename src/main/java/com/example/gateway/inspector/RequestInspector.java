package com.example.gateway.inspector;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Optional;

@FunctionalInterface
public interface RequestInspector {
    Optional<RequestProcessor> getProcessor(HttpServletRequest request);

    default RequestInspector orElse(RequestInspector next) {
        return new ChainedRequestInspector(this, next);
    }
}
