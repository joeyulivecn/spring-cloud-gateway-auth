package com.love.plus.apigateway;

import com.love.plus.apigateway.filter.AuthGlobalFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

@SpringBootApplication
@RestController
public class ApiGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiGatewayApplication.class, args);
    }

    @Bean
    public RouteLocator adminRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(p -> p
                        .path("/api/v1/m/auth/**", "/api/v1/auth/**")
                        .filters(f -> f.addRequestHeader("hello", "mobile"))
                        .uri("http://localhost:8002/api/v1/auth/**")
                )
                .route(p -> p
                        .path("/api/**")
                        .filters(f -> f.addRequestHeader("hello", "admin"))
                        .uri("http://localhost:8003/api/**")
                )
                .build();

    }

    @Bean
    public AuthGlobalFilter authGlobalFilter() {
        return new AuthGlobalFilter();
    }

    @Bean
    public RestTemplate restTemplate() {
        RestTemplate template = new RestTemplate();
        template.setErrorHandler(new RestTemplateErrorHandler());
        return template;
    }

    public class RestTemplateErrorHandler implements ResponseErrorHandler {

        @Override
        public boolean hasError(ClientHttpResponse response) throws IOException {
            return false;
        }

        @Override
        public void handleError(ClientHttpResponse response) throws IOException {

        }
    }
}
