package com.griddynamics.reactive.course.userservice.config;

import javax.annotation.PostConstruct;

import org.springframework.context.annotation.Configuration;

import reactor.core.publisher.Hooks;
import reactor.core.publisher.Operators;

@Configuration
public class MdcContextConfig {

    public static final String MDC_CONTEXT = "mdc-context";


    @PostConstruct
    public void setupMdcContext() {
        Hooks.onEachOperator(MDC_CONTEXT,
                Operators.lift((scannable, subscriber) ->
                        new MdcContextLifter<>(subscriber)));
    }
}
