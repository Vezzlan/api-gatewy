package com.example.gateway.inspector;

import com.example.gateway.context.RequestContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Component
public class RequestIdFilter {

    private final RequestInspector inspectorChain;

    private final DefaultRequestProcessor defaultRequestProcessor;

    private final ObjectMapper objectMapper;

    public RequestIdFilter() {
        this.objectMapper = new ObjectMapper();
        this.inspectorChain = buildInspectorChain();
        this.defaultRequestProcessor = new DefaultRequestProcessor(
                () -> UUID.randomUUID().toString(),
                this::updateRequestHeader
        );
    }

    private RequestInspector buildInspectorChain() {
        DefaultRequestInspector headerInspector = new DefaultRequestInspector(
                HttpServletRequest::getHeader,
                "X-Request-ID",
                this::updateRequestHeader
        );

        DefaultRequestInspector parameterInspector = new DefaultRequestInspector(
                HttpServletRequest::getParameter,
                "requestId",
                this::updateRequestHeader
        );

        DefaultRequestInspector bodyInspector = new DefaultRequestInspector(
                this::getBodyAttribute,
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

    private String getBodyAttribute(HttpServletRequest request, String name) {
        try (var inputStream = request.getInputStream()) {
            var rootNode = objectMapper.readTree(inputStream);

            return Optional.ofNullable(rootNode.get(name))
                    .map(JsonNode::asText)
                    .orElse("");
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    public void getOrCreate(HttpServletRequest request) {
        RequestProcessor processor = inspectorChain
                .getProcessor(request)
                .orElse(defaultRequestProcessor);

        processor.execute();
    }

}
