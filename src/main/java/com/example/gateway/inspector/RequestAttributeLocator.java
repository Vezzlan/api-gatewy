package com.example.gateway.inspector;

import jakarta.servlet.http.HttpServletRequest;

@FunctionalInterface
public interface RequestAttributeLocator {
    String locate(HttpServletRequest request, String name);
}
