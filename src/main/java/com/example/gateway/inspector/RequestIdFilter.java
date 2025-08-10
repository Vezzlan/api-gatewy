package com.example.gateway.inspector;

import com.example.gateway.context.RequestContext;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import java.util.UUID;

@Component
public class RequestIdFilter {

    private final RequestInspector requestInspector;

    private final DefaultRequestProcessor defaultRequestProcessor;

    private final BodyInspector bodyInspector;

    public RequestIdFilter(BodyInspector bodyInspector) {
        this.bodyInspector = bodyInspector;
        this.requestInspector = buildInspectorChain();
        this.defaultRequestProcessor = new DefaultRequestProcessor(
                () -> UUID.randomUUID().toString(),
                this::updateRequestHeader
        );
    }

    public void getOrCreate(HttpServletRequest request) {
        RequestProcessor processor = requestInspector
                .getProcessor(request)
                .orElse(defaultRequestProcessor);

        processor.execute();
    }

    private RequestInspector buildInspectorChain() {
        final var headerInspector = new DefaultRequestInspector(
                HttpServletRequest::getHeader,
                "X-Request-ID",
                this::updateRequestHeader
        );

        final var parameterInspector = new DefaultRequestInspector(
                HttpServletRequest::getParameter,
                "requestId",
                this::updateRequestHeader
        );

        final var bodyInspector = new DefaultRequestInspector(
                this.bodyInspector::getBodyAttribute,
                "requestId",
                this::updateRequestHeader
        );

        return headerInspector
                .orElse(parameterInspector)
                .orElse(bodyInspector);
    }

    private void updateRequestHeader(String requestId) {
        RequestContext.set(requestId);
    }

}
