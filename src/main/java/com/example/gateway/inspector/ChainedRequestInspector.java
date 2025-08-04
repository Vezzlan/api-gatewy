package com.example.gateway.inspector;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Optional;

public class ChainedRequestInspector implements RequestInspector {

    private final RequestInspector current;

    private final RequestInspector next;

    public ChainedRequestInspector(RequestInspector current, RequestInspector next) {
        this.current = current;
        this.next = next;
    }

    @Override
    public Optional<RequestProcessor> getProcessor(HttpServletRequest request) {
        return current
                .getProcessor(request)
                .or(() -> next.getProcessor(request));
    }
}
