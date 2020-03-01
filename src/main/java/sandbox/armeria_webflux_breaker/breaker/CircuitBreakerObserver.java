package sandbox.armeria_webflux_breaker.breaker;

import com.linecorp.armeria.client.circuitbreaker.CircuitBreaker;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.BaseSubscriber;

@Slf4j
@AllArgsConstructor
public class CircuitBreakerObserver<T> extends BaseSubscriber<T> {
    private final CircuitBreaker circuitBreaker;

    @Override
    protected void hookOnNext(T value) {
        circuitBreaker.onSuccess();
    }

    @Override
    protected void hookOnError(Throwable throwable) {
        circuitBreaker.onFailure();
    }
}
