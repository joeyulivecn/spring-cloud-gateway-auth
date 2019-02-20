package com.love.plus.apigateway.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.love.plus.apigateway.dto.ResultDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class AuthGlobalFilter implements GlobalFilter, Ordered {

    private String[] whiteList = {"/api/v1/auth/token", "/api/v1/auth/token/refresh"};

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String url = exchange.getRequest().getURI().getPath();

        // white list
        if (Arrays.asList(whiteList).contains(url)) {
            return chain.filter(exchange);
        }

        // do not has token
        String token = extractToken(exchange.getRequest());
        if (StringUtils.isEmpty(token)) {
            return buildUnauthorizedResponse(exchange);
        }

        // has token
        String userId = verifyToken(token);
        if (userId == null) {
            return buildUnauthorizedResponse(exchange);
        }

        // set userId
        ServerHttpRequest mutableReq = exchange.getRequest().mutate().header("X-Auth-UserId", "1").build();
        ServerWebExchange mutableExchange = exchange.mutate().request(mutableReq).build();

        return chain.filter(mutableExchange);
    }

    private Mono<Void> buildUnauthorizedResponse(ServerWebExchange exchange) {
        ServerHttpResponse originalResponse = exchange.getResponse();
        originalResponse.setStatusCode(HttpStatus.UNAUTHORIZED);
        originalResponse.getHeaders().add(HttpHeaders.CONTENT_TYPE, "application/json;charset=UTF-8");
        byte[] response = buildJSONErrorObject("401 Unauthorized").getBytes(StandardCharsets.UTF_8);
        DataBuffer dataBuffer = originalResponse.bufferFactory().wrap(response);
        return originalResponse.writeWith(Flux.just(dataBuffer));
    }

    private String buildJSONErrorObject(String message) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode root = mapper.createObjectNode();

        root.put("success", false);
        ObjectNode error = mapper.createObjectNode();
        error.put("message", message);
        root.set("error", error);
        try {
            return mapper.writeValueAsString(root);
        } catch (JsonProcessingException e) {
            return "{\"success\": \"false\", \"error\": {\"message\": \"Invalid token\"}}";
        }
    }

    private String extractToken(ServerHttpRequest request) {
        String token = null;
        String authHeader = request.getHeaders().getFirst("Authorization");
        if (authHeader != null && authHeader.toLowerCase().startsWith("bearer ")) {
            token = authHeader.substring(7);
        }
        return token;
    }

    private String verifyToken(String token) {
        String verifyTokenUrl = "http://localhost:8002/api/v1/auth/token/verify";
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(verifyTokenUrl, token, String.class);
        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            try {
                ObjectMapper mapper = new ObjectMapper();
                mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                ResultDto result = mapper.readValue(responseEntity.getBody(), ResultDto.class);
                if (result.isSuccess()) {
                    return result.getData();
                } else {
                    return null;
                }
            } catch (IOException e) {
                return null;
            }
        }

        return null;
    }

    @Override
    public int getOrder() {
        return -100;
    }
}
