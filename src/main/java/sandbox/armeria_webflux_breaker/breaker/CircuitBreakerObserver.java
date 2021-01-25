package sandbox.armeria_webflux_breaker.breaker;

import com.linecorp.armeria.client.circuitbreaker.CircuitBreaker;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.BaseSubscriber;

@Slf4j
@AllArgsConstructor
public class CircuitBreakerObserver<T> extends BaseSubscriber<T> {
    private final CircuitBreaker circuitBreaker;
    private final CircuitBreakerGroup circuitBreakerGroup;

    @Override
    protected void hookOnNext(T value) {
        circuitBreaker.onSuccess();
    }

    @Override
    protected void hookOnError(Throwable throwable) {
        if (circuitBreakerGroup.getExceptionFilter().shouldDealWith(throwable)) {
            circuitBreaker.onFailure();
        } else {
            circuitBreaker.onSuccess();
        }
    }
}
