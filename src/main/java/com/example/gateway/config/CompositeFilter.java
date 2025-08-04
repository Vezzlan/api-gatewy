package com.example.gateway.config;

import com.example.gateway.context.RequestContext;
import com.example.gateway.inspector.RequestIdFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.function.HandlerFilterFunction;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;

import java.util.function.Function;

@Component
public class CompositeFilter {

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

    public  HandlerFilterFunction<ServerResponse, ServerResponse> addRequestId() {
        return (request, next) -> {
            final var requestId = RequestContext.get();
            ServerRequest modified = generateRequest(request, requestId);
            ServerResponse response = next.handle(modified);
            response.headers().add("X-request-trace-ID", requestId);
            return response;
        };
    }

    private ServerRequest generateRequest(ServerRequest request, String requestId) {
        return ServerRequest
                .from(request)
                .header("X-request-trace-ID", requestId)
                .build();
    }

}
