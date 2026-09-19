package com.griddynamics.reactive.course.userservice.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Slf4j
@Component
public class RequestIdFilter implements WebFilter {

    public static final String REQUEST_ID = "requestId";
    public static final String REQUEST_ID_HEADER = "X-Request-ID";
    public static final int BEGIN_REQ_ID_INDEX = 0;
    public static final int END_REQ_ID_INDEX = 10;


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String requestIdHeader = exchange.getRequest().getHeaders().getFirst(REQUEST_ID_HEADER);

        if (requestIdHeader == null || requestIdHeader.isEmpty()) {
            log.info("Request id is null or empty, generating random UUID..");

            requestIdHeader = UUID.randomUUID()
                    .toString()
                    .replace("-", Strings.EMPTY)
                    .substring(BEGIN_REQ_ID_INDEX, END_REQ_ID_INDEX);
        }
        exchange.getRequest().getHeaders().set(REQUEST_ID_HEADER, requestIdHeader);

        final String headerValue = requestIdHeader;

        return chain.filter(exchange).contextWrite(context -> context.put(REQUEST_ID, headerValue));
    }
}
