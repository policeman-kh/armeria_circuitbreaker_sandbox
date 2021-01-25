package sandbox.armeria_webflux_breaker.breaker;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CircuitBreakerGroup {
    // Default group to use default failure threshold and all exception deal with as failure.
    DEFAULT(0.5, e -> true);
    // Define new enum if you need to change failure threshold or exclude a specific exception.
    private final double failureRateThreshold;
    private final ExceptionFilter exceptionFilter;
}
