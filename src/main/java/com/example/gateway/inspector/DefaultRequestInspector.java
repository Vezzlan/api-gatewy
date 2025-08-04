package com.example.gateway.inspector;

import io.micrometer.common.util.StringUtils;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Optional;
import java.util.function.Consumer;

public class DefaultRequestInspector implements RequestInspector {

    private final RequestAttributeLocator attributeLocator;

    private final String attributeName;

    private final Consumer<String> consumer;

    public DefaultRequestInspector(RequestAttributeLocator attributeLocator, String attributeName, Consumer<String> consumer) {
        this.attributeLocator = attributeLocator;
        this.attributeName = attributeName;
        this.consumer = consumer;
    }

    @Override
    public Optional<RequestProcessor> getProcessor(HttpServletRequest request) {
        String attributeValue = attributeLocator.locate(request, attributeName);
        if (StringUtils.isNotEmpty(attributeValue)) {
            return Optional.of(new DefaultRequestProcessor(attributeValue, consumer));
        }
        return Optional.empty();
    }
}
