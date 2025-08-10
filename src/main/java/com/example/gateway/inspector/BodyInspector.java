package com.example.gateway.inspector;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;

@Component
public class BodyInspector {

    private final ObjectMapper objectMapper;

    public BodyInspector(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public String getBodyAttribute(HttpServletRequest request, String name) {
        try (var inputStream = request.getInputStream()) {
            final var rootNode = objectMapper.readTree(inputStream);

            return Optional.ofNullable(rootNode.get(name))
                    .map(JsonNode::asText)
                    .orElse("");
        } catch (IOException e) {
            System.out.println("Error parsing json");
            throw new RuntimeException();
        }
    }
}
