package sandbox.armeria_webflux_breaker.breaker;

import org.springframework.stereotype.Component;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import com.linecorp.armeria.client.circuitbreaker.CircuitBreaker;
import com.linecorp.armeria.client.circuitbreaker.CircuitBreakerListener;

import lombok.AllArgsConstructor;
import lombok.Value;

@AllArgsConstructor
@Component
public class CircuitBreakerRegistry {
    private final CircuitBreakerListener listener;

    private final LoadingCache<CircuitBreakerSetting, CircuitBreaker> methodCaches =
            CacheBuilder.newBuilder()
                        .build(CacheLoader.from(this::buildCircuitBreaker));

    public CircuitBreaker getCircuitBreaker(String name, CircuitBreakerGroup group) throws Throwable {
        return methodCaches.get(CircuitBreakerSetting.of(name, group));
    }

    public CircuitBreaker getCircuitBreaker(String name) throws Throwable {
        return getCircuitBreaker(name, CircuitBreakerGroup.DEFAULT);
    }

    private CircuitBreaker buildCircuitBreaker(CircuitBreakerSetting setting) {
        return CircuitBreaker.builder(setting.getName())
                             .listener(listener)
                             .failureRateThreshold(setting.getGroup().getFailureRateThreshold())
                             .build();
    }

    @Value(staticConstructor = "of")
    private static class CircuitBreakerSetting {
        String name;
        CircuitBreakerGroup group;
    }
}
