package com.demo.circuitbreaker.service;

import com.demo.circuitbreaker.configuration.CustomException;
import org.springframework.cloud.circuitbreaker.resilience4j.ReactiveResilience4JCircuitBreakerFactory;
import org.springframework.cloud.client.circuitbreaker.ReactiveCircuitBreaker;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;


@Service
public class MyService{

    ReactiveCircuitBreaker reactiveCircuitBreaker;

    public MyService(ReactiveResilience4JCircuitBreakerFactory cbFactory) {
        this.reactiveCircuitBreaker = cbFactory.create("my-service");
    }

    public Mono<String> getLibraryBook() {
        return WebClient.create().get().uri("http://localhost:8902/library/borrowBook")
                .retrieve().bodyToMono(String.class);
    }

    public Mono<String> getResponse() throws CustomException {
        return this.reactiveCircuitBreaker.run(getLibraryBook(), ex-> Mono.error(ex));
        }

    private Mono<String> getOutput() throws CustomException {
        return Mono.error(new CustomException());
    }

}




















