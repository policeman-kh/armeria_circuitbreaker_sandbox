package sandbox.armeria_webflux_breaker.breaker;

import org.springframework.stereotype.Component;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import com.linecorp.armeria.client.circuitbreaker.CircuitBreaker;
import com.linecorp.armeria.client.circuitbreaker.MetricCollectingCircuitBreakerListener;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Component
public class CircuitBreakerRegistry {
    private final MetricCollectingCircuitBreakerListener listener;

    private final LoadingCache<String, CircuitBreaker> methodCaches =
            CacheBuilder.newBuilder()
                        .build(CacheLoader.from(this::buildCircuitBreaker));

    public CircuitBreaker getCircuitBreaker(String name) throws Throwable {
        return methodCaches.get(name);
    }

    private CircuitBreaker buildCircuitBreaker(String name) {
        return CircuitBreaker.builder(name)
                             .listener(listener)
                             .build();
    }
}
