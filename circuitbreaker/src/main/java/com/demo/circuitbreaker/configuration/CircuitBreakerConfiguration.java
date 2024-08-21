package com.demo.circuitbreaker.configuration;

import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;
import org.springframework.cloud.circuitbreaker.resilience4j.ReactiveResilience4JCircuitBreakerFactory;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder;
import org.springframework.cloud.client.circuitbreaker.Customizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.time.Duration;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;

@Configuration
public class CircuitBreakerConfiguration
{
        @Bean
        public Customizer<ReactiveResilience4JCircuitBreakerFactory> circuitBreakerFactoryCustomizer()
        {
            CircuitBreakerConfig circuitBreakerConfig = CircuitBreakerConfig.custom()
                    .slidingWindowSize(5)
                    .permittedNumberOfCallsInHalfOpenState(5)
                    .failureRateThreshold(1)
                    .minimumNumberOfCalls(3)
                    .waitDurationInOpenState(Duration.ofSeconds(5))
//                    .recordExceptions(CustomException.class)
                    .automaticTransitionFromOpenToHalfOpenEnabled(true)
                    .build();

            CircuitBreakerRegistry circuitBreakerRegistry = CircuitBreakerRegistry.of(circuitBreakerConfig);

            return factory -> {

                factory.configure(builder -> builder.timeLimiterConfig(TimeLimiterConfig.custom()
                                .timeoutDuration(Duration.ofSeconds(4))
                                .build())
                        .circuitBreakerConfig(circuitBreakerConfig), "my-service");

                factory.configureCircuitBreakerRegistry(circuitBreakerRegistry);

                factory.configureDefault(id -> new Resilience4JConfigBuilder(id)
                        .timeLimiterConfig(TimeLimiterConfig.custom()
                                .timeoutDuration(Duration.ofSeconds(4))
                                .build())
                        .circuitBreakerConfig(circuitBreakerConfig)
                        .build());

                factory.addCircuitBreakerCustomizer(
                        circuitBreaker -> circuitBreaker.getEventPublisher().onStateTransition(e -> {
                            switch (e.getStateTransition().getToState()) {
                                case CLOSED:
                                    System.out.println("Circuit Breaker is now CLOSED.");
                                    break;
                                case HALF_OPEN:
                                    System.out.println("Circuit Breaker is now HALF_OPEN.");
                                    break;
                                case OPEN:
                                    System.out.println("Circuit Breaker is now OPEN!");
                                    break;
                                case METRICS_ONLY:
                                    break;
                                default:
                                    break;
                            }
                        }), "my-service");
            };
        }
}


