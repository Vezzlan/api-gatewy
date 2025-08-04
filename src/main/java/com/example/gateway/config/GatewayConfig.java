package com.example.gateway.config;

import com.example.gateway.inspector.RequestIdFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.*;

import static org.springframework.cloud.gateway.server.mvc.filter.BeforeFilterFunctions.uri;
import static org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions.http;
import static org.springframework.web.servlet.function.RouterFunctions.route;

@Configuration
public class GatewayConfig {

    private final CompositeFilter compositeFilter;

    public GatewayConfig(CompositeFilter compositeFilter) {
        this.compositeFilter = compositeFilter;
    }

    @Bean
    public RouterFunction<ServerResponse> getRoute() {
        return route().GET("/get", http())
                .filter(compositeFilter.extractRequestId())
                .before(uri("http://httpbin.org:80"))
                .build();
    }

}
