package com.example.gateway.config;

import com.example.gateway.context.RequestContext;
import com.example.gateway.inspector.RequestIdFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.function.ServerRequest;

import java.util.function.Function;

@Component
public class CompositeFilter {

    private static final String TRACE_HEADER = "X-request-trace-ID";

    private final RequestIdFilter idFilter;

    public CompositeFilter(RequestIdFilter idFilter) {
        this.idFilter = idFilter;
    }

    public  Function<ServerRequest, ServerRequest> extractOrCreateRequestId() {
        return request -> {
            idFilter.getOrCreate(request.servletRequest());
            return request;
        };
    }

    public Function<ServerRequest, ServerRequest> addRequestId() {
        return request -> ServerRequest.from(request).header(TRACE_HEADER, RequestContext.get()).build();
    }

}
